package ptolemy;

import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.jdesktop.application.SingleFrameApplication;
import ptolemy.data.Config;
import ptolemy.exprparser.ExprParser;
import ptolemy.exprparser.ParseException;

/**
 * The main class of the application.
 *
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Application extends SingleFrameApplication {
	protected static final ResourceBundle res = ResourceBundle.getBundle("ptolemy/resources/Application");

	// per i messaggi (sostituisce System.out)
	static PrintStream printStream = null;
	// finche' non appare l'interfaccia grafica, i messaggi
	// vengono mantenuti in questo buffer:
	static LinkedList<String> printStreamBuffer = new LinkedList<String>();
	
	public static class ConfigToken {private ConfigToken() {}}

	private final Config config = new Config(new ConfigToken());

	public Config getConfig() {
		return config;
	}

	private final SerialComm serialComm = new SerialComm();

	public SerialComm getSerialCommObject() {
		return serialComm;
	}

	private ExprParser compExpr;

	public ExprParser getCompExpr() {
		return compExpr;
	}

	private final DataProcessor dataProcessor = new DataProcessor();

	public DataProcessor getDataProcessor() {
		return dataProcessor;
	}
	
	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override
	protected void startup() {
		try {
			reloadConfig();
		} catch(InvalidConfigurationException e) {
			JOptionPane.showMessageDialog(null, e.toString(), res.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			//System.exit(1);
		}

		// if serial port is configured and present, automatically open it
		CommPortIdentifier portId = config.getSerialPortId();
		if (portId != null) {
			//serialCommThread.open(portId);
			serialComm.open(portId);
		}

		show(new View(this));
	}

	@Override
	protected void shutdown() {
		if (getSerialCommObject().isOpen()) {
			System.err.println("Warning: serial port left open. closing.");
			getSerialCommObject().close();
		}

		super.shutdown();
	}

	/**
	 * This method is to initialize the specified window by injecting resources.
	 * Windows shown in our application come fully initialized from the GUI
	 * builder, so this additional configuration is not needed.
	 */
	@Override
	protected void configureWindow(java.awt.Window root) {
	}

	/**
	 * A convenient static getter for the application instance.
	 * @return the instance of Application
	 */
	public static Application getApplication() {
		return Application.getInstance(Application.class);
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		//JOptionPane.showMessageDialog(null, "java.library.path: " + System.getProperty("java.library.path").replaceAll(";", "\n"));
		launch(Application.class, args);
	}

	public void reloadConfig() throws InvalidConfigurationException {
		try {
			config.load();

			if(serialComm.isOpen()) {
				String oldSerial = serialComm.getOpenSerialPortId().getName();
				String newSerial = config.getSerialPortId().getName();
				if (!oldSerial.equals(newSerial)) {
					serialComm.close();
					serialComm.open(config.getSerialPortId());
				}
			}

			compExpr = ExprParser.parseString(config.getComputeFormula());

		} catch (IOException e) {
			throw new InvalidConfigurationException(e);
		} catch (ParseException e) {
			throw new InvalidConfigurationException(e, res.getString("COMPEXPR_PARSE_ERROR"));
		}
	}

	public synchronized static void println(String txt) {
		if (printStream == null) {
			printStreamBuffer.offer(txt);
		} else {
			printStream.println(txt);
		}
	}

	public static void printerr(String e) {
		println(res.getString("ERROR_PREFIX") + e);
	}

	public synchronized static void setPrintStream(PrintStream ps) {
		printStream = ps;

		if (printStream != null) {
			// quando viene impostato un PrintStream
			// svuota il buffer
			while (!printStreamBuffer.isEmpty()) {
				println(printStreamBuffer.remove());
			}
		}
	}
}
