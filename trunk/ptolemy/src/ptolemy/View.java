package ptolemy;

import gnu.io.CommPortIdentifier;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import ptolemy.data.Config;

/**
 * The application's main frame.
 * 
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class View extends JFrame {
	private static final long serialVersionUID = -1209245808507815927L;

	private SerialComm serialCommObject;
	private Config config;

	public View() {
		initComponents();

		serialCommObject = Application.getApplication().getSerialCommObject();
		config = Application.getApplication().getConfig();

		updateMenus();

		timeline = new Timeline();
		Application.getApplication().getDataProcessor().setTimeline(timeline);

		new changeChartsVisibiity().actionPerformed(null);

		Application.setPrintStream(new PrintStream(
				textArea2OutputStream(textArea1)));
	}

	public final void updateMenus() {
		menuFileOpenserial.setEnabled(!serialCommObject.isOpen());
		menuFileCloseserial.setEnabled(serialCommObject.isOpen());
	}

	public class showPreferences extends AbstractAction {
		private static final long serialVersionUID = -3588170301017320033L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame mainFrame = Application.getApplication().getMainFrame();
			Preferences prefsBox = new Preferences(mainFrame);
			prefsBox.setLocationRelativeTo(mainFrame);
			prefsBox.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			prefsBox.setVisible(true);
		}
	}

	public class showAboutBox extends AbstractAction {
		private static final long serialVersionUID = 5092801286181139469L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFrame mainFrame = Application.getApplication().getMainFrame();
			AboutBox aboutBox = new AboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
			aboutBox.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			aboutBox.setVisible(true);
		}
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
		inputBox = new ptolemy.JTextFieldWithHistory();

		buttonRead.setAction(new doRead());
		buttonRead.setText("Read (1)");

		textArea1.setBackground(Color.black);
		textArea1.setColumns(20);
		textArea1.setEditable(false);
		textArea1.setForeground(Color.yellow);
		textArea1.setRows(5);
		jScrollPane1.setViewportView(textArea1);

		buttonRead2.setAction(new doRead2());
		buttonRead2.setText("Read (2)");

		buttonReadLoop.setAction(new doReadLoopToggle());
		buttonReadLoop.setText("Read loop");

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

		menuFile.setText("File");

		menuFileExportCSV.setAction(new doExportCSV());
		menuFileExportCSV.setText("Export CSV...");
		menuFile.add(menuFileExportCSV);

		menuFile.add(menuFileSeparator21);

		menuFileOpenserial.setAction(new doOpenSerial());
		menuFileOpenserial.setText("Open serial port");
		menuFile.add(menuFileOpenserial);

		menuFileCloseserial.setAction(new doCloseSerial());
		menuFileCloseserial.setText("Close serial port");
		menuFile.add(menuFileCloseserial);

		menuFile.add(menuFileSeparator1);

		menuFilePreferences.setAction(new showPreferences());
		menuFilePreferences.setText("Preferences...");
		menuFile.add(menuFilePreferences);

		menuFile.add(menuFileSeparator2);

		menuFileExit.setAction(new quitApplication());
		menuFileExit.setText("Quit");
		menuFile.add(menuFileExit);

		menuBar.add(menuFile);

		menuGraph.setText("Graph");

		menuGraphShow.setAction(new showGraphWindow());
		menuGraphShow.setText("Show graph window...");
		menuGraph.add(menuGraphShow);

		menuGraphEmpty.setAction(new emptyGraphData());
		menuGraphEmpty.setText("Clear graph");
		menuGraph.add(menuGraphEmpty);

		menuGraph.add(menuGraphSeparator1);

		menuGraphChkComposite.setAction(new changeChartsVisibiity());
		menuGraphChkComposite.setSelected(true);
		menuGraphChkComposite.setText("Show track 'Composite'");
		menuGraph.add(menuGraphChkComposite);

		menuGraphChkSensorHi.setAction(new changeChartsVisibiity());
		menuGraphChkSensorHi.setText("Show track 'Light sensor, Led ON'");
		menuGraph.add(menuGraphChkSensorHi);

		menuGraphChkSensorLo.setAction(new changeChartsVisibiity());
		menuGraphChkSensorLo.setText("Show track 'Light sensor, Led OFF'");
		menuGraph.add(menuGraphChkSensorLo);

		menuGraphChkReferenceHi.setAction(new changeChartsVisibiity());
		menuGraphChkReferenceHi
				.setText("Show track 'Reference sensor, Led ON'");
		menuGraph.add(menuGraphChkReferenceHi);

		menuGraphChkReferenceLo.setAction(new changeChartsVisibiity());
		menuGraphChkReferenceLo
				.setText("Show track 'Reference sensor, Led OFF'");
		menuGraph.add(menuGraphChkReferenceLo);

		menuBar.add(menuGraph);

		helpMenu.setText("?");

		aboutMenuItem.setAction(new showAboutBox());
		aboutMenuItem.setText("About Ptolemy...");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		inputBox.setText("");
		inputBox.setToolTipText("Use this input to send raw commands to the board (see manual)");

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
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												inputBox,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												476, Short.MAX_VALUE)
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
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				inputBox,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addContainerGap()))));

		setJMenuBar(menuBar);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(mainPanel);
		getContentPane().add(statusPanel);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
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

	public class quitApplication extends AbstractAction {
		private static final long serialVersionUID = -8873732033500011472L;

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public class doRead extends AbstractAction {
		private static final long serialVersionUID = 6300706191255833394L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!serialCommObject.isOpen()) {
				JOptionPane.showMessageDialog(View.this,
						"Error: serial port is not open", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			serialCommObject.sendCommand(config.getRead1Command());
		}
	}

	public class doRead2 extends AbstractAction {
		private static final long serialVersionUID = 7865506179505823395L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!serialCommObject.isOpen()) {
				JOptionPane.showMessageDialog(View.this,
						"Error: serial port is not open", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			serialCommObject.sendCommand(config.getRead2Command());
		}
	}

	public class doExportCSV extends AbstractAction {
		private static final long serialVersionUID = -5881304405804723304L;

		@Override
		public void actionPerformed(ActionEvent e) {
			final JFileChooser fc = new JFileChooser();
			if(JFileChooser.APPROVE_OPTION == fc.showSaveDialog(getRootPane())) {
				DataProcessor dp = Application.getApplication()
						.getDataProcessor();
				try {
					dp.exportCSVData(fc.getSelectedFile());
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(View.this,
							"Error: " + ex.toString(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public class doOpenSerial extends AbstractAction {
		private static final long serialVersionUID = 604452437931934941L;

		@Override
		public void actionPerformed(ActionEvent e) {
			CommPortIdentifier id = config.getSerialPortId();

			if(id == null) {
				JOptionPane.showMessageDialog(View.this,
						"Error: no serial port configured", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			serialCommObject.open(id);
			updateMenus();
		}
	}

	public class doCloseSerial extends AbstractAction {
		private static final long serialVersionUID = 4314771988028199994L;

		@Override
		public void actionPerformed(ActionEvent e) {
			serialCommObject.close();
			updateMenus();
		}
	}

	public class doReadLoopToggle extends AbstractAction {
		private static final long serialVersionUID = -5786333779312285979L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!serialCommObject.isOpen()) {
				JOptionPane.showMessageDialog(View.this,
						"Error: serial port is not open", "Error",
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
	}

	public class showGraphWindow extends AbstractAction {
		private static final long serialVersionUID = 8627627209349699675L;

		@Override
		public void actionPerformed(ActionEvent e) {
			timeline.setVisible(true);
		}
	}

	public class changeChartsVisibiity extends AbstractAction {
		private static final long serialVersionUID = 6051856489827063052L;

		@Override
		public void actionPerformed(ActionEvent e) {
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
	}

	public class emptyGraphData extends AbstractAction {
		private static final long serialVersionUID = -8353420425831950568L;

		@Override
		public void actionPerformed(ActionEvent e) {
			timeline.clear();
		}
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
	private javax.swing.JPanel statusPanel;
	private javax.swing.JTextArea textArea1;

	private Timeline timeline;
}
