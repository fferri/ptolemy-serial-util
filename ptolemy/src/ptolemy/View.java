package ptolemy;

import gnu.io.CommPortIdentifier;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import ptolemy.data.Config;

/**
 * The application's main frame.
 * 
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class View extends FrameView {
	protected static final ResourceBundle res = ResourceBundle
			.getBundle("ptolemy/resources/Application");

	private SerialComm serialCommObject;
	private Config config;

	public View(SingleFrameApplication app) {
		super(app);

		initComponents();

		serialCommObject = Application.getApplication().getSerialCommObject();
		config = Application.getApplication().getConfig();

		updateMenus();

		timeline = new Timeline();
		Application.getApplication().getDataProcessor().setTimeline(timeline);

		changeChartsVisibiity();

		Application.setPrintStream(new PrintStream(
				textArea2OutputStream(textArea1)));

		ResourceMap resourceMap = getResourceMap();
		int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
		messageTimer = new Timer(messageTimeout, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				statusMessageLabel.setText("");
			}
		});
		messageTimer.setRepeats(false);
		int busyAnimationRate = resourceMap
				.getInteger("StatusBar.busyAnimationRate");
		for(int i = 0; i < busyIcons.length; i++) {
			busyIcons[i] = resourceMap
					.getIcon("StatusBar.busyIcons[" + i + "]");
		}
		busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
				statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
			}
		});
		idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
		statusAnimationLabel.setIcon(idleIcon);
		progressBar.setVisible(false);

		// connecting action tasks to status bar via TaskMonitor
		TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
		taskMonitor
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						String propertyName = evt.getPropertyName();
						if("started".equals(propertyName)) {
							if(!busyIconTimer.isRunning()) {
								statusAnimationLabel.setIcon(busyIcons[0]);
								busyIconIndex = 0;
								busyIconTimer.start();
							}
							progressBar.setVisible(true);
							progressBar.setIndeterminate(true);
						} else if("done".equals(propertyName)) {
							busyIconTimer.stop();
							statusAnimationLabel.setIcon(idleIcon);
							progressBar.setVisible(false);
							progressBar.setValue(0);
						} else if("message".equals(propertyName)) {
							String text = (String) (evt.getNewValue());
							statusMessageLabel.setText((text == null) ? ""
									: text);
							messageTimer.restart();
						} else if("progress".equals(propertyName)) {
							int value = (Integer) (evt.getNewValue());
							progressBar.setVisible(true);
							progressBar.setIndeterminate(false);
							progressBar.setValue(value);
						}
					}
				});
	}

	public final void updateMenus() {
		menuFileOpenserial.setEnabled(!serialCommObject.isOpen());
		menuFileCloseserial.setEnabled(serialCommObject.isOpen());
	}

	@Action
	public final void showPreferences() {
		// if (prefsBox == null) {
		JFrame mainFrame = Application.getApplication().getMainFrame();
		prefsBox = new Preferences(mainFrame);
		prefsBox.setLocationRelativeTo(mainFrame);
		// }
		Application.getApplication().show(prefsBox);
	}

	@Action
	public final void showAboutBox() {
		if(aboutBox == null) {
			JFrame mainFrame = Application.getApplication().getMainFrame();
			aboutBox = new AboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}
		Application.getApplication().show(aboutBox);
	}

	private void initComponents() {
		mainPanel = new javax.swing.JPanel();
		buttonRead = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		textArea1 = new javax.swing.JTextArea();
		buttonRead2 = new javax.swing.JButton();
		buttonReadLoop = new javax.swing.JToggleButton();
		menuBar = new javax.swing.JMenuBar();
		javax.swing.JMenu menuFile = new javax.swing.JMenu();
		menuFileExportCSV = new javax.swing.JMenuItem();
		menuFileOpenserial = new javax.swing.JMenuItem();
		menuFileCloseserial = new javax.swing.JMenuItem();
		menuFileSeparator1 = new javax.swing.JPopupMenu.Separator();
		menuFilePreferences = new javax.swing.JMenuItem();
		menuFileSeparator2 = new javax.swing.JPopupMenu.Separator();
		menuFileSeparator21 = new javax.swing.JPopupMenu.Separator();
		javax.swing.JMenuItem menuFileExit = new javax.swing.JMenuItem();
		menuGraph = new javax.swing.JMenu();
		menuGraphShow = new javax.swing.JMenuItem();
		menuGraphEmpty = new javax.swing.JMenuItem();
		menuGraphSeparator1 = new javax.swing.JPopupMenu.Separator();
		menuGraphChkComposite = new javax.swing.JCheckBoxMenuItem();
		menuGraphChkSensorHi = new javax.swing.JCheckBoxMenuItem();
		menuGraphChkSensorLo = new javax.swing.JCheckBoxMenuItem();
		menuGraphChkReferenceHi = new javax.swing.JCheckBoxMenuItem();
		menuGraphChkReferenceLo = new javax.swing.JCheckBoxMenuItem();
		javax.swing.JMenu helpMenu = new javax.swing.JMenu();
		javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
		statusPanel = new javax.swing.JPanel();
		javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
		statusMessageLabel = new javax.swing.JLabel();
		statusAnimationLabel = new javax.swing.JLabel();
		progressBar = new javax.swing.JProgressBar();
		inputBox = new ptolemy.JTextFieldWithHistory();

		mainPanel.setName("mainPanel"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(ptolemy.Application.class).getContext()
				.getActionMap(View.class, this);
		buttonRead.setAction(actionMap.get("doRead")); // NOI18N
		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(ptolemy.Application.class).getContext()
				.getResourceMap(View.class);
		buttonRead.setText(resourceMap.getString("buttonRead.text")); // NOI18N
		buttonRead.setName("buttonRead"); // NOI18N

		jScrollPane1.setName("jScrollPane1"); // NOI18N

		textArea1.setBackground(resourceMap.getColor("textArea1.background")); // NOI18N
		textArea1.setColumns(20);
		textArea1.setEditable(false);
		textArea1.setForeground(resourceMap.getColor("textArea1.foreground")); // NOI18N
		textArea1.setRows(5);
		textArea1.setName("textArea1"); // NOI18N
		jScrollPane1.setViewportView(textArea1);

		buttonRead2.setAction(actionMap.get("doRead2")); // NOI18N
		buttonRead2.setName("buttonRead2"); // NOI18N

		buttonReadLoop.setAction(actionMap.get("doReadLoopToggle")); // NOI18N
		buttonReadLoop.setText(resourceMap.getString("buttonReadLoop.text")); // NOI18N
		buttonReadLoop.setName("buttonReadLoop"); // NOI18N

		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(
				mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout
				.setHorizontalGroup(mainPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jScrollPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																636,
																Short.MAX_VALUE)
														.addGroup(
																mainPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				buttonRead)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				buttonRead2)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				buttonReadLoop)))
										.addContainerGap()));
		mainPanelLayout
				.setVerticalGroup(mainPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mainPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																buttonRead)
														.addComponent(
																buttonRead2)
														.addComponent(
																buttonReadLoop))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												322, Short.MAX_VALUE)
										.addContainerGap()));

		menuBar.setName("menuBar"); // NOI18N

		menuFile.setText(resourceMap.getString("menuFile.text")); // NOI18N
		menuFile.setName("menuFile"); // NOI18N

		menuFileExportCSV.setAction(actionMap.get("doExportCSV")); // NOI18N
		menuFileExportCSV.setText(resourceMap
				.getString("menuFileExportCSV.text")); // NOI18N
		menuFileExportCSV.setName("menuFileExportCSV"); // NOI18N
		menuFile.add(menuFileExportCSV);

		menuFileSeparator21.setName("menuFileSeparator21");
		menuFile.add(menuFileSeparator21);

		menuFileOpenserial.setAction(actionMap.get("doOpenSerial")); // NOI18N
		menuFileOpenserial.setText(resourceMap
				.getString("menuFileOpenserial.text")); // NOI18N
		menuFileOpenserial.setName("menuFileOpenserial"); // NOI18N
		menuFile.add(menuFileOpenserial);

		menuFileCloseserial.setAction(actionMap.get("doCloseSerial")); // NOI18N
		menuFileCloseserial.setText(resourceMap
				.getString("menuFileCloseserial.text")); // NOI18N
		menuFileCloseserial.setName("menuFileCloseserial"); // NOI18N
		menuFile.add(menuFileCloseserial);

		menuFileSeparator1.setName("menuFileSeparator1"); // NOI18N
		menuFile.add(menuFileSeparator1);

		menuFilePreferences.setAction(actionMap.get("showPreferences")); // NOI18N
		menuFilePreferences.setText(resourceMap
				.getString("menuFilePreferences.text")); // NOI18N
		menuFilePreferences.setName("menuFilePreferences"); // NOI18N
		menuFile.add(menuFilePreferences);

		menuFileSeparator2.setName("menuFileSeparator2"); // NOI18N
		menuFile.add(menuFileSeparator2);

		menuFileExit.setAction(actionMap.get("quit")); // NOI18N
		menuFileExit.setName("menuFileExit"); // NOI18N
		menuFile.add(menuFileExit);

		menuBar.add(menuFile);

		menuGraph.setText(resourceMap.getString("menuGraph.text")); // NOI18N
		menuGraph.setName("menuGraph"); // NOI18N

		menuGraphShow.setAction(actionMap.get("showGraphWindow")); // NOI18N
		menuGraphShow.setText(resourceMap.getString("menuGraphShow.text")); // NOI18N
		menuGraphShow.setName("menuGraphShow"); // NOI18N
		menuGraph.add(menuGraphShow);

		menuGraphEmpty.setAction(actionMap.get("emptyGraphData")); // NOI18N
		menuGraphEmpty.setText(resourceMap.getString("menuGraphEmpty.text")); // NOI18N
		menuGraphEmpty.setName("menuGraphEmpty"); // NOI18N
		menuGraph.add(menuGraphEmpty);

		menuGraphSeparator1.setName("menuGraphSeparator1"); // NOI18N
		menuGraph.add(menuGraphSeparator1);

		menuGraphChkComposite.setAction(actionMap.get("changeChartsVisibiity")); // NOI18N
		menuGraphChkComposite.setSelected(true);
		menuGraphChkComposite.setText(resourceMap
				.getString("menuGraphChkComposite.text")); // NOI18N
		menuGraphChkComposite.setName("menuGraphChkComposite"); // NOI18N
		menuGraph.add(menuGraphChkComposite);

		menuGraphChkSensorHi.setAction(actionMap.get("changeChartsVisibiity")); // NOI18N
		menuGraphChkSensorHi.setText(resourceMap
				.getString("menuGraphChkSensorHi.text")); // NOI18N
		menuGraphChkSensorHi.setName("menuGraphChkSensorHi"); // NOI18N
		menuGraph.add(menuGraphChkSensorHi);

		menuGraphChkSensorLo.setAction(actionMap.get("changeChartsVisibiity")); // NOI18N
		menuGraphChkSensorLo.setText(resourceMap
				.getString("menuGraphChkSensorLo.text")); // NOI18N
		menuGraphChkSensorLo.setName("menuGraphChkSensorLo"); // NOI18N
		menuGraph.add(menuGraphChkSensorLo);

		menuGraphChkReferenceHi.setAction(actionMap
				.get("changeChartsVisibiity")); // NOI18N
		menuGraphChkReferenceHi.setText(resourceMap
				.getString("menuGraphChkReferenceHi.text")); // NOI18N
		menuGraphChkReferenceHi.setName("menuGraphChkReferenceHi"); // NOI18N
		menuGraph.add(menuGraphChkReferenceHi);

		menuGraphChkReferenceLo.setAction(actionMap
				.get("changeChartsVisibiity")); // NOI18N
		menuGraphChkReferenceLo.setText(resourceMap
				.getString("menuGraphChkReferenceLo.text")); // NOI18N
		menuGraphChkReferenceLo.setName("menuGraphChkReferenceLo"); // NOI18N
		menuGraph.add(menuGraphChkReferenceLo);

		menuBar.add(menuGraph);

		helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N

		aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
		aboutMenuItem.setName("aboutMenuItem"); // NOI18N
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		statusPanel.setName("statusPanel"); // NOI18N

		statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

		statusMessageLabel.setName("statusMessageLabel"); // NOI18N

		statusAnimationLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

		progressBar.setName("progressBar"); // NOI18N

		inputBox.setText(resourceMap.getString("inputBox.text")); // NOI18N
		inputBox.setName("inputBox"); // NOI18N

		javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(
				statusPanel);
		statusPanel.setLayout(statusPanelLayout);
		statusPanelLayout
				.setHorizontalGroup(statusPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(statusPanelSeparator,
								javax.swing.GroupLayout.DEFAULT_SIZE, 656,
								Short.MAX_VALUE)
						.addGroup(
								statusPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(statusMessageLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												inputBox,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												476, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												progressBar,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(statusAnimationLabel)
										.addContainerGap()));
		statusPanelLayout
				.setVerticalGroup(statusPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								statusPanelLayout
										.createSequentialGroup()
										.addComponent(
												statusPanelSeparator,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												2,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(
												statusPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																statusPanelLayout
																		.createSequentialGroup()
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				20,
																				Short.MAX_VALUE)
																		.addGroup(
																				statusPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								statusMessageLabel)
																						.addComponent(
																								statusAnimationLabel)
																						.addComponent(
																								progressBar,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGap(3,
																				3,
																				3))
														.addGroup(
																statusPanelLayout
																		.createSequentialGroup()
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				inputBox,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addContainerGap()))));

		setComponent(mainPanel);
		setMenuBar(menuBar);
		setStatusBar(statusPanel);
	}

	public final OutputStream textArea2OutputStream(final JTextArea t) {
		return new OutputStream() {

			JTextArea ta = t;

			public void write(int b) {
				byte[] bs = new byte[1];
				bs[0] = (byte) b;
				ta.append(new String(bs));
				ta.setCaretPosition(ta.getDocument().getLength());
			}
		};
	}

	@Action
	public final void doRead() {
		if(!serialCommObject.isOpen()) {
			JOptionPane.showMessageDialog(getComponent(),
					res.getString("ERR_PORT_NOT_OPEN"), res.getString("ERROR"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		serialCommObject.sendCommand(config.getRead1Command());
	}

	@Action
	public final void doRead2() {
		if(!serialCommObject.isOpen()) {
			JOptionPane.showMessageDialog(getComponent(),
					res.getString("ERR_PORT_NOT_OPEN"), res.getString("ERROR"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		serialCommObject.sendCommand(config.getRead2Command());
	}

	@Action
	public final void doExportCSV() {
		final JFileChooser fc = new JFileChooser();
		if(JFileChooser.APPROVE_OPTION == fc.showSaveDialog(getRootPane())) {
			DataProcessor dp = Application.getApplication().getDataProcessor();
			try {
				dp.exportCSVData(fc.getSelectedFile());
			} catch(Exception e) {
				JOptionPane.showMessageDialog(getRootPane(),
						res.getString("ERROR") + ": " + e.toString());
			}
		}
	}

	@Action
	public final void doOpenSerial() {
		CommPortIdentifier id = config.getSerialPortId();

		if(id == null) {
			JOptionPane.showMessageDialog(getComponent(),
					res.getString("NO_SERIAL_PORT_CONFIGURED"),
					res.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		serialCommObject.open(id);
		updateMenus();
	}

	@Action
	public final void doCloseSerial() {
		serialCommObject.close();
		updateMenus();
	}

	@Action
	public final void doReadLoopToggle() {
		if(!serialCommObject.isOpen()) {
			JOptionPane.showMessageDialog(getComponent(),
					res.getString("ERR_PORT_NOT_OPEN"), res.getString("ERROR"),
					JOptionPane.ERROR_MESSAGE);
			buttonReadLoop.setSelected(false);
			return;
		}

		if(buttonReadLoop.isSelected()) {
			enablePeriodicRead();
		} else {
			disablePeriodicRead();
		}
	}

	@Action
	public final void showGraphWindow() {
		timeline.setVisible(true);
	}

	@Action
	public final void changeChartsVisibiity() {
		timeline.setChartVisible(Timeline.Index.SENSOR_HI,
				menuGraphChkSensorHi.isSelected());
		timeline.setChartVisible(Timeline.Index.SENSOR_LO,
				menuGraphChkSensorLo.isSelected());
		timeline.setChartVisible(Timeline.Index.REFERENCE_HI,
				menuGraphChkReferenceHi.isSelected());
		timeline.setChartVisible(Timeline.Index.REFERENCE_LO,
				menuGraphChkReferenceLo.isSelected());
		timeline.setChartVisible(Timeline.Index.COMPUTED,
				menuGraphChkComposite.isSelected());
	}

	@Action
	public final void emptyGraphData() {
		timeline.clear();
	}

	public final void enablePeriodicRead() {
		buttonReadLoop.setSelected(true);
		buttonRead.setEnabled(false);
		buttonRead2.setEnabled(false);

		timeline.setVisible(true);

		serialCommObject.enablePeriodicRead(0, this);
	}

	public final void disablePeriodicRead() {
		buttonReadLoop.setSelected(false);
		buttonRead.setEnabled(true);
		buttonRead2.setEnabled(true);

		// timeline.setVisible(false);

		serialCommObject.disablePeriodicRead();
	}

	private javax.swing.JButton buttonRead;
	private javax.swing.JButton buttonRead2;
	private javax.swing.JToggleButton buttonReadLoop;
	private javax.swing.JTextField inputBox;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem menuFileExportCSV;
	private javax.swing.JMenuItem menuFileCloseserial;
	private javax.swing.JMenuItem menuFileOpenserial;
	private javax.swing.JMenuItem menuFilePreferences;
	private javax.swing.JPopupMenu.Separator menuFileSeparator1;
	private javax.swing.JPopupMenu.Separator menuFileSeparator2;
	private javax.swing.JPopupMenu.Separator menuFileSeparator21;
	private javax.swing.JMenu menuGraph;
	private javax.swing.JCheckBoxMenuItem menuGraphChkComposite;
	private javax.swing.JCheckBoxMenuItem menuGraphChkReferenceHi;
	private javax.swing.JCheckBoxMenuItem menuGraphChkReferenceLo;
	private javax.swing.JCheckBoxMenuItem menuGraphChkSensorHi;
	private javax.swing.JCheckBoxMenuItem menuGraphChkSensorLo;
	private javax.swing.JMenuItem menuGraphEmpty;
	private javax.swing.JPopupMenu.Separator menuGraphSeparator1;
	private javax.swing.JMenuItem menuGraphShow;
	private javax.swing.JProgressBar progressBar;
	private javax.swing.JLabel statusAnimationLabel;
	private javax.swing.JLabel statusMessageLabel;
	private javax.swing.JPanel statusPanel;
	private javax.swing.JTextArea textArea1;

	private final Timer messageTimer;
	private final Timer busyIconTimer;
	private final Icon idleIcon;
	private final Icon[] busyIcons = new Icon[15];
	private int busyIconIndex = 0;
	private Timeline timeline;
	private JDialog aboutBox;
	private JDialog prefsBox;
}
