package ptolemy;

import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import ptolemy.data.Config;
import ptolemy.exprparser.ExprParser;
import ptolemy.exprparser.ParseException;

/**
 * The main class of the application.
 * 
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Application {
	private static Application instance = null;

	public static synchronized Application getApplication() {
		if(instance == null)
			instance = new Application();
		return instance;
	}

	// per i messaggi (sostituisce System.out)
	static PrintStream printStream = null;
	// finche' non appare l'interfaccia grafica, i messaggi
	// vengono mantenuti in questo buffer:
	static LinkedList<String> printStreamBuffer = new LinkedList<String>();

	public static class ConfigToken {
		private ConfigToken() {
		}
	}

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

	private View view;

	protected void startup() {
		try {
			reloadConfig();
		} catch(InvalidConfigurationException e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error",
					JOptionPane.ERROR_MESSAGE);
			// System.exit(1);
		}

		// if serial port is configured and present, automatically open it
		CommPortIdentifier portId = config.getSerialPortId();

		view = new View();
		
		if(portId != null) {
			//serialComm.open(portId);
			view.new doOpenSerial().actionPerformed(null);
		}
		
		view.setVisible(true);
	}

	public View getMainFrame() {
		return view;
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		// JOptionPane.showMessageDialog(null, "java.library.path: " +
		// System.getProperty("java.library.path").replaceAll(";", "\n"));

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override public void run() {
				SerialComm sc = Application.getApplication().getSerialCommObject();
				if(sc.isOpen()) {
					System.err.println("Warning: serial port left open. closing.");
					sc.close();
				}
			}
		});
		
		Application app = new Application();
		
		app.startup();
	}

	public void reloadConfig() throws InvalidConfigurationException {
		try {
			config.load();

			if(serialComm.isOpen()) {
				String oldSerial = serialComm.getOpenSerialPortId().getName();
				String newSerial = config.getSerialPortId().getName();
				if(!oldSerial.equals(newSerial)) {
					serialComm.close();
					serialComm.open(config.getSerialPortId());
				}
			}

			compExpr = ExprParser.parseString(config.getComputeFormula());

		} catch(IOException e) {
			throw new InvalidConfigurationException(e);
		} catch(ParseException e) {
			throw new InvalidConfigurationException(e,
					"Espressione matematica non valida");
		}
	}

	public synchronized static void println(String txt) {
		if(printStream == null) {
			printStreamBuffer.offer(txt);
		} else {
			printStream.println(txt);
		}
	}

	public static void printerr(String e) {
		println("Error: " + e);
	}

	public synchronized static void setPrintStream(PrintStream ps) {
		printStream = ps;

		if(printStream != null) {
			// quando viene impostato un PrintStream
			// svuota il buffer
			while(!printStreamBuffer.isEmpty()) {
				println(printStreamBuffer.remove());
			}
		}
	}
}
