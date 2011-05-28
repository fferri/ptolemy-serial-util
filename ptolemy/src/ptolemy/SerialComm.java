package ptolemy;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;

/**
 *
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class SerialComm implements SerialPortEventListener {
	protected static final ResourceBundle res = ResourceBundle.getBundle("ptolemy/resources/Application");
	
	protected CommPortIdentifier serialPortId;
	protected SerialPort serialPort;
	protected InputStream inputStream;
	protected OutputStream outputStream;
	protected StringBuffer lineBuffer = new StringBuffer();

	public CommPortIdentifier getOpenSerialPortId() {
		return serialPortId;
	}

	public void open(CommPortIdentifier portId) {
		synchronized (this) {
			if (serialPort != null) {
				close();
			}
			serialPortId = portId;
			try {
				serialPort = (SerialPort) serialPortId.open(this.getClass().toString(), 9600);
				serialPort.setSerialPortParams(
					9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();
				Application.println(String.format(res.getString("OPENED_SERIAL_PORT"), serialPort.getName()));
			} catch (PortInUseException e) {
				Application.printerr(String.format(res.getString("ERR_PORT_IN_USE"), serialPortId.getName()));
			} catch (Exception e) {
				Application.printerr(e.toString());
			}
		}
	}

	public void close() {
		synchronized (this) {
			if (serialPort != null) {
				serialPort.removeEventListener();
				String serialPortName = serialPort.getName();

				try {
					outputStream.close();
					inputStream.close();
				} catch (IOException e) {
				}

				serialPort.close();
				serialPort = null;
				inputStream = null;
				outputStream = null;

				serialPortId = null;

				Application.println(String.format(res.getString("CLOSED_SERIAL_PORT"), serialPortName));
			}
		}
	}

	public boolean isOpen() {
		return serialPort != null;
	}

	public void sendCommand(String cmd) {
		try {
			outputStream.write(cmd.getBytes());
			outputStream.write('\n');
			outputStream.flush();
			Application.println("-> " + cmd);
		} catch (IOException e) {
			Application.printerr("sendCommand: " + String.format(res.getString("ERR_CANNOT_SEND") + " (%s)", cmd, e));
		}
	}

	public void serialEvent(SerialPortEvent spe) {
		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			int data;
			try {
				while ((data = inputStream.read()) > -1) {
					char ch = (char) data;
					if (data == '\n' || data == '\r') {
						if (lineBuffer.length() == 0) {
							continue;
						}
						String received = lineBuffer.toString();
						Application.println("<- " + received);
						Application.getApplication().getDataProcessor().parseAnswerAndNotify(received);
						lineBuffer.delete(0, lineBuffer.length());
					} else {
						lineBuffer.append(ch);
					}
				}
			} catch (IOException e) {
				Application.printerr("serialEvent: " + e.toString());
				close();
			}
		}
	}
	
	private boolean bPeriodic = false;

	public void enablePeriodicRead(int adcNum, final View cb) {
		bPeriodic = true;
		new Thread() {

			@Override
			public void run() {
				while (bPeriodic) {
					//view.doRead();
					//try { Thread.sleep(1000); } catch (InterruptedException ex) { }
					//view.doRead2();
					//sendCommand("H2,P400,Q2,A0,A1,P100,L2,P400,Q2,A0,A1");
					sendCommand(Application.getApplication().getConfig().getPeriodicReadCommand());
					try {
						Thread.sleep(Application.getApplication().getConfig().getPeriodicReadInterval());
					} catch (InterruptedException ex) {
						Thread.yield();
					}
				}
			}
		}.start();
	}

	public void disablePeriodicRead() {
		bPeriodic = false;
	}
}
