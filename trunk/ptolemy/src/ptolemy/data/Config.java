package ptolemy.data;

import gnu.io.CommPortIdentifier;
import java.io.File;
import java.io.IOException;
import java.text.Annotation;
import java.util.Enumeration;
import java.util.ResourceBundle;
import ptolemy.Application;

/**
 *
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Config extends PropertiesPersistent {
	protected static final ResourceBundle res = ResourceBundle.getBundle("ptolemy/resources/Application");

	private static class ConfigEntry extends Annotation {
		public ConfigEntry(String propertyName) {
			super(propertyName);
		}
	}
	
	private String serialPort;
	private Integer deviceId;
	private String computeFormula;
	private Integer periodicReadInterval;
	private String periodicReadCommand;
	private String read1Command;
	private String read2Command;
	private Integer mobileMeanSize;

	public Config(Application.ConfigToken t) {
		if(t == null)
			throw new RuntimeException("Only Application can create a Config object");
		
		initDefaults();

		try {
			File f = new File(getDefaultConfigFileName());
			if (!f.exists()) save();
		} catch (IOException e) {
			Application.printerr(res.getString("CFG_FILE_CREATION_FAILED"));
		}
	}

	private void initDefaults() {
		CommPortIdentifier serialPortId = getSerialPortId();
		if(serialPortId != null) serialPort = serialPortId.getName();
		else Application.printerr(res.getString("ERR_CREATING_SERIAL_PORT"));
		deviceId = 0;
		computeFormula = "(VSon-VSoff)/(VRon-VRoff)";
		periodicReadInterval = 2000;
		periodicReadCommand = "H2,P400,Q2,A0,A1,P100,L2,P400,Q2,A0,A1";
		read1Command = "H2,P400,A0,A1,L2";
		read2Command = "A0,A1";
		mobileMeanSize = 1;
	}

	public static String getDefaultConfigDir() {
		String osName = System.getProperty("os.name").toLowerCase();
		String homeDir = System.getProperty("user.home");
		String dir = "ptolemy";
		String sep = System.getProperty("file.separator");

		if (osName.startsWith("windows")) {
			return homeDir + sep + dir + sep;
		} else {
			return homeDir + sep + "." + dir + sep;
		}
	}

	public static String getDefaultConfigFileName() {
		return getDefaultConfigDir() + "config.properties";
	}

	public final void loadFromFile(String fileName) throws IOException {
		loadFromProperties(this, Config.class, fileName);
	}

	public final void saveToFile(String fileName) throws IOException {
		new File(getDefaultConfigDir()).mkdir();
		saveToProperties(this, Config.class, fileName);
	}

	public final void load() throws IOException {
		loadFromFile(getDefaultConfigFileName());
	}

	public final void save() throws IOException {
		saveToFile(getDefaultConfigFileName());
	}

	@Getter(property="serial.port")
	public String getSerialPort() {
		return serialPort;
	}

	public CommPortIdentifier getSerialPortId() {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier defaultPortId = null;

		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			if (defaultPortId == null) {
				defaultPortId = portId;
			}
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(serialPort)) {
					return portId;
				}
			}
		}
		return defaultPortId;
	}

	@Setter(property="serial.port")
	public void setSerialPort(String portName) {
		serialPort = portName;
	}

	@Getter(property="device.id")
	public Integer getDeviceId() {
		return deviceId;
	}

	@Setter(property="device.id")
	public void setDeviceId(Integer id) {
		deviceId = id;
	}

	@Getter(property="expr")
	public String getComputeFormula() {
		return computeFormula;
	}

	@Setter(property="expr")
	public void setComputeFormula(String newFormula) {
		computeFormula = newFormula;
	}

	@Getter(property="periodicreadinterval")
	public Integer getPeriodicReadInterval() {
		return periodicReadInterval;
	}

	@Setter(property="periodicreadinterval")
	public void setPeriodicReadInterval(Integer i) {
		periodicReadInterval = i;
	}

	@Getter(property="periodicreadcommand")
	public String getPeriodicReadCommand() {
		return periodicReadCommand;
	}

	@Setter(property="periodicreadcommand")
	public void setPeriodicReadCommand(String c) {
		periodicReadCommand = c;
	}

	@Getter(property="read1command")
	public String getRead1Command() {
		return read1Command;
	}

	@Setter(property="read1command")
	public void setRead1Command(String c) {
		read1Command = c;
	}

	@Getter(property="read2command")
	public String getRead2Command() {
		return read2Command;
	}

	@Setter(property="read2command")
	public void setRead2Command(String c) {
		read2Command = c;
	}

	@Getter(property="mobilemeansize")
	public Integer getMobileMeanSize() {
		return mobileMeanSize;
	}

	@Setter(property="mobilemeansize")
	public void setMobileMeanSize(Integer s) {
		mobileMeanSize = s;
		if(s < 1) mobileMeanSize = 1;
	}
}
