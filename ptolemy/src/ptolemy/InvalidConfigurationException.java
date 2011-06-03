package ptolemy;

public class InvalidConfigurationException extends Exception {
	private static final long serialVersionUID = -5062712960193732677L;
	
	private Exception nestedException;

	public InvalidConfigurationException(Exception e) {
		super(e.toString());
		nestedException = e;
	}

	public InvalidConfigurationException(Exception e, String s) {
		super(s);
		nestedException = e;
	}

	public String toString() {
		return super.toString() + "\n\n" + nestedException.toString();
	}

	public Exception getNestedException() {
		return nestedException;
	}
}
