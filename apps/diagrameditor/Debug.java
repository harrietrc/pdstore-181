package diagrameditor;

/**
 * Convenience class for printing debug statements or creating event logs
 * 
 * @author wliu080
 * 
 */
public class Debug {
	private boolean isOn = false;
	private String logPath = "";

	public Debug(boolean inDebugMode) {
		isOn = inDebugMode;
	}

	public Debug(boolean inDebugMode, String logPath) {
		isOn = inDebugMode;
		this.logPath = logPath;
	}

	/**
	 * Performs a System.out.print on the provided string when Debug has been
	 * initialized in Debug mode
	 * 
	 * @param s
	 *            The string to be printed
	 */
	public void p(String s) {
		if (isOn)
			System.out.print(s);
	}

	/**
	 * Performs a System.out.println on the provided string when Debug has been
	 * initialized in Debug mode
	 * 
	 * @param s
	 *            The string to be printed
	 */
	public void pl(String s) {
		if (isOn)
			System.out.println(s);
	}

	public void log(String s) {
		// TODO: implement logging
	}

	public void setLogPath(String s) {
		logPath = s;
	}
}
