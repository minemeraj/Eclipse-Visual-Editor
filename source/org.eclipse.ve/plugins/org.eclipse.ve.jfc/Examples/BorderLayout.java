/**
 * Insert the type's description here.
 * Creation date: (2/22/2002 5:10:46 PM)
 * @author: 
 */
class BorderLayout {
	private java.awt.Frame ivjFrame1 = null;
	private java.awt.Label ivjLabel1 = null;
	private java.awt.Scrollbar ivjScrollbar1 = null;
public BorderLayout() {
	super();
	initialize();
}
/**
 * Return the Frame1 property value.
 * @return java.awt.Frame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Frame getFrame1() {
	if (ivjFrame1 == null) {
		try {
			ivjFrame1 = new java.awt.Frame();
			ivjFrame1.setName("Frame1");
			ivjFrame1.setLayout(new java.awt.BorderLayout());
			ivjFrame1.setBounds(31, 27, 200, 200);
			getFrame1().add(getLabel1(), "North");
			getFrame1().add(getScrollbar1(), "East");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFrame1;
}
/**
 * Return the Label1 property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getLabel1() {
	if (ivjLabel1 == null) {
		try {
			ivjLabel1 = new java.awt.Label();
			ivjLabel1.setName("Label1");
			ivjLabel1.setText("Frog");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLabel1;
}
/**
 * Return the Scrollbar1 property value.
 * @return java.awt.Scrollbar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Scrollbar getScrollbar1() {
	if (ivjScrollbar1 == null) {
		try {
			ivjScrollbar1 = new java.awt.Scrollbar();
			ivjScrollbar1.setName("Scrollbar1");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjScrollbar1;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		BorderLayout aBorderLayout;
		aBorderLayout = new BorderLayout();
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.lang.Object");
		exception.printStackTrace(System.out);
	}
}
}
