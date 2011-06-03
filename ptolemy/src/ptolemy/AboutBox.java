package ptolemy;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * 
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class AboutBox extends javax.swing.JDialog {
	private static final long serialVersionUID = 2722729397969154034L;

	public AboutBox(java.awt.Frame parent) {
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(closeButton);
	}

	public class closeAboutBox extends AbstractAction {
		private static final long serialVersionUID = -4596305164154597430L;

		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
	}

	private void initComponents() {

		closeButton = new javax.swing.JButton();
		closeButton.setText("Close");
		javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
		javax.swing.JLabel versionLabel = new javax.swing.JLabel();
		javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
		panel1 = new ptolemy.JBubbles();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("About Ptolemy...");
		setMinimumSize(new java.awt.Dimension(562, 245));
		setModal(true);
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentHidden(java.awt.event.ComponentEvent evt) {
				formComponentHidden(evt);
			}

			public void componentShown(java.awt.event.ComponentEvent evt) {
				formComponentShown(evt);
			}
		});

		closeButton.setAction(new closeAboutBox());

		appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(
				appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD,
				appTitleLabel.getFont().getSize() + 4));
		appTitleLabel.setText("Ptolemy");

		versionLabel.setFont(versionLabel.getFont().deriveFont(
				versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
		versionLabel.setText("Version:");

		appVersionLabel.setText("1.2, Federico Ferri (C) 2011");

		javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(
				panel1);
		panel1.setLayout(panel1Layout);
		panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 164,
				Short.MAX_VALUE));
		panel1Layout.setVerticalGroup(panel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 194,
				Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(closeButton)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														versionLabel))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																												.addComponent(
																														appVersionLabel)))
																				.addComponent(
																						appTitleLabel))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		panel1,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		appTitleLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																				.addComponent(
																						versionLabel)
																				.addComponent(
																						appVersionLabel)))
												.addComponent(
														panel1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(closeButton).addContainerGap()));

		pack();
	}

	private void formComponentHidden(java.awt.event.ComponentEvent evt) {
		((ptolemy.JBubbles) panel1).stopAnimation();
	}

	private void formComponentShown(java.awt.event.ComponentEvent evt) {
		((ptolemy.JBubbles) panel1).startAnimation();
	}

	private void formWindowClosed(java.awt.event.WindowEvent evt) {
		((ptolemy.JBubbles) panel1).stopAnimation();
	}

	private javax.swing.JButton closeButton;
	private javax.swing.JPanel panel1;
}
