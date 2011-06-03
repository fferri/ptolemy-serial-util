package ptolemy;

import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import ptolemy.data.Config;

/**
 *
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Preferences extends javax.swing.JDialog {
	private static final long serialVersionUID = -2569461896876272981L;

	protected static final ResourceBundle res = ResourceBundle.getBundle("ptolemy/resources/Application");

	private Config config = Application.getApplication().getConfig();

	/** Creates new form Preferences */
	public Preferences(java.awt.Frame parent) {
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(buttonOk);

		populateSerialPortCombo();
		populateDeviceIdCombo();

		try {
			config.load();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(getParent(),
				res.getString("FAILED_TO_LOAD_PREFERENCES"),
				res.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}

		updateValuesFromConfig();

		if (!serialPortCombo.getSelectedItem().equals(config.getSerialPort())) {
			JOptionPane.showMessageDialog(getParent(),
				res.getString("CONFIGURED_SERIAL_PORT_NOT_AVAILABLE"),
				res.getString("ERROR"), JOptionPane.WARNING_MESSAGE);
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

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(ptolemy.Application.class).getContext()
				.getResourceMap(Preferences.class);
		setTitle(resourceMap.getString("Form.title")); // NOI18N
		setMinimumSize(new java.awt.Dimension(391, 263));
		setModal(true);
		setName("Form"); // NOI18N
		setResizable(false);

		serialPortLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		serialPortLabel.setText(resourceMap.getString("serialPortLabel.text")); // NOI18N
		serialPortLabel.setName("serialPortLabel"); // NOI18N

		serialPortCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] {"Item 1", "Item 2", "Item 3", "Item 4" }));
		serialPortCombo.setName("serialPortCombo"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(ptolemy.Application.class).getContext()
				.getActionMap(Preferences.class, this);
		buttonOk.setAction(actionMap.get("savePreferencesAndClose")); // NOI18N
		buttonOk.setName("buttonOk"); // NOI18N

		buttonCancel.setAction(actionMap.get("closePreferences")); // NOI18N
		buttonCancel.setName("buttonCancel"); // NOI18N

		idLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		idLabel.setText(resourceMap.getString("idLabel.text")); // NOI18N
		idLabel.setName("idLabel"); // NOI18N

		idCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Item 1", "Item 2", "Item 3", "Item 4" }));
		idCombo.setName("idCombo"); // NOI18N

		expr.setText(resourceMap.getString("expr.text")); // NOI18N
		expr.setName("expr"); // NOI18N

		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
		jLabel1.setName("jLabel1"); // NOI18N

		interval.setName("interval"); // NOI18N

		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
		jLabel2.setName("jLabel2"); // NOI18N

		jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
		jLabel3.setName("jLabel3"); // NOI18N

		periodicreadCmd.setText(resourceMap.getString("periodicreadCmd.text")); // NOI18N
		periodicreadCmd.setName("periodicreadCmd"); // NOI18N

		jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
		jLabel4.setName("jLabel4"); // NOI18N

		read1cmd.setText(resourceMap.getString("read1cmd.text")); // NOI18N
		read1cmd.setName("read1cmd"); // NOI18N

		read2cmd.setText(resourceMap.getString("read2cmd.text")); // NOI18N
		read2cmd.setName("read2cmd"); // NOI18N

		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
		jLabel5.setName("jLabel5"); // NOI18N

		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
		jLabel6.setName("jLabel6"); // NOI18N

		mobMeanSize.setMajorTickSpacing(8);
		mobMeanSize.setMaximum(32);
		mobMeanSize.setMinimum(1);
		mobMeanSize.setMinorTickSpacing(1);
		mobMeanSize.setPaintTicks(true);
		mobMeanSize.setSnapToTicks(true);
		mobMeanSize.setValue(1);
		mobMeanSize.setName("mobMeanSize"); // NOI18N
		mobMeanSize
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						mobMeanSizePropertyChange(evt);
					}
				});

		jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
		jLabel7.setName("jLabel7"); // NOI18N

		lblMMS.setText(resourceMap.getString("lblMMS.text")); // NOI18N
		lblMMS.setName("lblMMS"); // NOI18N

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

	private void mobMeanSizePropertyChange(java.beans.PropertyChangeEvent evt) {
		lblMMS.setText("(" + mobMeanSize.getValue() + ")");
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
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				String portName = portId.getName();
				serialPortCombo.addItem(portName);
			}
		}
	}

	private void populateDeviceIdCombo() {
		idCombo.removeAllItems();
		for (int i = 0; i < 16; i++) {
			idCombo.addItem(new Integer(i));
		}
	}

	@Action
	public void savePreferencesAndClose() {
		try {
			savePreferences();
			closePreferences();
		} catch(InvalidConfigurationException e) {
			JOptionPane.showMessageDialog(rootPane, e.toString(), res.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Action
	public void closePreferences() {
		setVisible(false);
	}

	@Action
	public void savePreferences() throws InvalidConfigurationException {
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
		} catch (IOException e) {
			JOptionPane.showMessageDialog(getParent(),
				res.getString("FAILED_TO_SAVE_PREFERENCES"),
				res.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
		}

		Application.getApplication().reloadConfig();
	}
        // Variables declaration - do not modify//GEN-BEGIN:variables
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
        // End of variables declaration//GEN-END:variables
}
