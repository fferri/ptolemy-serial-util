package ptolemy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JTextField;

/**
 *
 * @author Federico Ferri
 */
public class JTextFieldWithHistory extends JTextField implements KeyListener {
	// History container:
	private LinkedList<String> commandHistory = new LinkedList<String>();
	private ListIterator<String> commandHistoryPtr = commandHistory.listIterator();

	public JTextFieldWithHistory() {
		addKeyListener(this);
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			String rawCmd = getText();
			setText("");
			Application.getApplication().getSerialCommObject().sendCommand(rawCmd);
			commandHistory.remove(rawCmd);
			commandHistory.add(rawCmd);
			commandHistoryPtr = commandHistory.listIterator(commandHistory.size());
		} else if (keyCode == KeyEvent.VK_UP) {
			if (commandHistoryPtr.hasPrevious()) {
				setText(commandHistoryPtr.previous());
			}
		} else if (keyCode == KeyEvent.VK_DOWN) {
			if (commandHistoryPtr.hasNext()) {
				setText(commandHistoryPtr.next());
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int cp = getCaretPosition();
		setText(getText().toUpperCase());
		setCaretPosition(cp);
	}
}
