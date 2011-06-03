package ptolemy;

import gnu.io.CommPortIdentifier;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ptolemy.data.Config;

/**
 * 
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Preferences extends JDialog {
	private static final long serialVersionUID = -2569461896876272981L;

	private Config config = Application.getApplication().getConfig();

	/** Creates new form Preferences */
	public Preferences(JFrame parent) {
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(buttonOk);

		populateSerialPortCombo();
		populateDeviceIdCombo();

		try {
			config.load();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(getParent(),
					"Error: Failed to load preferences", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		updateValuesFromConfig();

		if(!serialPortCombo.getSelectedItem().equals(config.getSerialPort())) {
			JOptionPane
					.showMessageDialog(
							getParent(),
							"Error: the previously configured serial port is no longer available",
							"Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void initComponents() {

		serialPortLabel = new javax.swing.JLabel();
		serialPortCombo = new javax.swing.JComboBox();
		buttonOk = new javax.swing.JButton();
		buttonCancel = new javax.swing.JButton();
		idLabel = new javax.swing.JLabel();
		idCombo = new javax.swing.JComboBox();
		expr = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		interval = new javax.swing.JSpinner();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		periodicreadCmd = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		read1cmd = new javax.swing.JTextField();
		read2cmd = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		mobMeanSize = new javax.swing.JSlider();
		jLabel7 = new javax.swing.JLabel();
		lblMMS = new javax.swing.JLabel();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Settings");
		setMinimumSize(new Dimension(391, 263));
		setModal(true);
		setResizable(false);

		serialPortLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		serialPortLabel.setText("Serial port:");

		buttonOk.setAction(new savePreferencesAndClose());
		buttonOk.setText("OK");
		
		buttonCancel.setAction(new closePreferences());
		buttonCancel.setText("Cancel");
		
		idLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		idLabel.setText("Device ID:");

		expr.setText("");

		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel1.setText("Computation expr:");

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel2.setText("Periodic read interval:"); // NOI18N

		jLabel3.setText("ms");

		periodicreadCmd.setText("???periodicReadCmd???");

		jLabel4.setText("Periodic read command:");

		read1cmd.setText("???read1cmd???");

		read2cmd.setText("???read2cmd???");

		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel5.setText("Read(1) command:");

		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel6.setText("Read(2) command:");

		mobMeanSize.setMajorTickSpacing(8);
		mobMeanSize.setMaximum(32);
		mobMeanSize.setMinimum(1);
		mobMeanSize.setMinorTickSpacing(1);
		mobMeanSize.setPaintTicks(true);
		mobMeanSize.setSnapToTicks(true);
		mobMeanSize.setValue(1);
		mobMeanSize.addChangeListener(new ChangeListener() {
			@Override public void stateChanged(ChangeEvent e) {
				lblMMS.setText("(" + mobMeanSize.getValue() + ")");
			}
		});

		jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel7.setText("Moving average size:");

		lblMMS.setText("");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		buttonCancel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		buttonOk))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				false)
																				.addComponent(
																						jLabel4,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						jLabel2,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						serialPortLabel,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						idLabel,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						jLabel1,
																						javax.swing.GroupLayout.Alignment.LEADING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						99,
																						Short.MAX_VALUE)
																				.addComponent(
																						jLabel5,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						jLabel6,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						jLabel7,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						periodicreadCmd,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						253,
																						Short.MAX_VALUE)
																				.addComponent(
																						expr,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						253,
																						Short.MAX_VALUE)
																				.addComponent(
																						idCombo,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						0,
																						253,
																						Short.MAX_VALUE)
																				.addComponent(
																						serialPortCombo,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						0,
																						253,
																						Short.MAX_VALUE)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										interval,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										96,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										jLabel3))
																				.addComponent(
																						read2cmd,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						253,
																						Short.MAX_VALUE)
																				.addComponent(
																						read1cmd,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						253,
																						Short.MAX_VALUE)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										mobMeanSize,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										211,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addComponent(
																										lblMMS,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										38,
																										Short.MAX_VALUE)))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(serialPortLabel)
												.addComponent(
														serialPortCombo,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(idLabel)
												.addComponent(
														idCombo,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel1)
												.addComponent(
														expr,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														interval,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel2)
												.addComponent(jLabel3))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														periodicreadCmd,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel4))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														read1cmd,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel5))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														read2cmd,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel6))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(
																						lblMMS)
																				.addComponent(
																						mobMeanSize,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		33,
																		Short.MAX_VALUE)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						buttonOk)
																				.addComponent(
																						buttonCancel)))
												.addComponent(jLabel7))
								.addContainerGap()));

		pack();
	}

	public final void updateValuesFromConfig() {
		serialPortCombo.setSelectedItem(config.getSerialPort());
		idCombo.setSelectedItem(config.getDeviceId());
		expr.setText(config.getComputeFormula());
		periodicreadCmd.setText(config.getPeriodicReadCommand());
		read1cmd.setText(config.getRead1Command());
		read2cmd.setText(config.getRead2Command());
		interval.setValue(config.getPeriodicReadInterval());
		mobMeanSize.setValue(config.getMobileMeanSize());
	}

	private void populateSerialPortCombo() {
		serialPortCombo.removeAllItems();

		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
		while(portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();
			if(portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				String portName = portId.getName();
				serialPortCombo.addItem(portName);
			}
		}
	}

	private void populateDeviceIdCombo() {
		idCombo.removeAllItems();
		for(int i = 0; i < 16; i++) {
			idCombo.addItem(new Integer(i));
		}
	}

	public class savePreferencesAndClose extends AbstractAction {
		private static final long serialVersionUID = -4935303979798053712L;

		@Override
		public void actionPerformed(ActionEvent e) {
			new savePreferences().actionPerformed(null);
			new closePreferences().actionPerformed(null);
		}
	}

	public class closePreferences extends AbstractAction {
		private static final long serialVersionUID = 4578900902576170958L;

		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}

	public class savePreferences extends AbstractAction {
		private static final long serialVersionUID = -8427503176443073763L;

		@Override
		public void actionPerformed(ActionEvent e) {
			config.setSerialPort((String) serialPortCombo.getSelectedItem());
			config.setDeviceId((Integer) idCombo.getSelectedItem());
			config.setComputeFormula(expr.getText());
			config.setPeriodicReadInterval((Integer) interval.getValue());
			config.setPeriodicReadCommand(periodicreadCmd.getText());
			config.setRead1Command(read1cmd.getText());
			config.setRead2Command(read2cmd.getText());
			config.setMobileMeanSize(mobMeanSize.getValue());

			try {
				config.save();
			} catch(IOException ex) {
				JOptionPane.showMessageDialog(getParent(),
						"Error: failed to save preferences", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			try {
				Application.getApplication().reloadConfig();
			} catch(InvalidConfigurationException ex) {
				JOptionPane.showMessageDialog(rootPane, ex.toString(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private javax.swing.JButton buttonCancel;
	private javax.swing.JButton buttonOk;
	private javax.swing.JTextField expr;
	private javax.swing.JComboBox idCombo;
	private javax.swing.JLabel idLabel;
	private javax.swing.JSpinner interval;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel lblMMS;
	private javax.swing.JSlider mobMeanSize;
	private javax.swing.JTextField periodicreadCmd;
	private javax.swing.JTextField read1cmd;
	private javax.swing.JTextField read2cmd;
	private javax.swing.JComboBox serialPortCombo;
	private javax.swing.JLabel serialPortLabel;
}
