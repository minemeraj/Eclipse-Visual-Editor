/**
 * Insert the type's description here.
 * Creation date: (5/13/2002 10:00:54 AM)
 * @author: 
 */
class FlowBundleTest extends java.awt.Frame {
	private static java.util.ResourceBundle resbundle = java.util.ResourceBundle.getBundle("bundle");  //$NON-NLS-1$
	private java.awt.Button ivjButton1 = null;
	private java.awt.Panel ivjContentsPane = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.awt.Label ivjLabel1 = null;

class IvjEventHandler implements java.awt.event.WindowListener {
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {};
		public void windowClosing(java.awt.event.WindowEvent e) {
			if (e.getSource() == FlowBundleTest.this) 
				connEtoC1(e);
		};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
/**
 * BundleTest constructor comment.
 */
public FlowBundleTest() {
	super();
	initialize();
}
/**
 * BundleTest constructor comment.
 * @param title java.lang.String
 */
public FlowBundleTest(String title) {
	super(title);
}
/**
 * connEtoC1:  (BundleTest.window.windowClosing(java.awt.event.WindowEvent) --> BundleTest.dispose()V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the Button1 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton1() {
	if (ivjButton1 == null) {
		try {
			ivjButton1 = new java.awt.Button();
			ivjButton1.setName("Button1");
			ivjButton1.setLabel(resbundle.getString("Button1_label"));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton1;
}
/**
 * Return the ContentsPane property value.
 * @return java.awt.Panel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Panel getContentsPane() {
	if (ivjContentsPane == null) {
		try {
			ivjContentsPane = new java.awt.Panel();
			ivjContentsPane.setName("ContentsPane");
			ivjContentsPane.setLayout(new java.awt.FlowLayout());
			getContentsPane().add(getButton1(), getButton1().getName());
			getContentsPane().add(getLabel1(), getLabel1().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjContentsPane;
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
			ivjLabel1.setText(resbundle.getString("Label1_text"));
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
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	this.addWindowListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("BundleTest");
		setSize(152, 134);
		setTitle(resbundle.getString("BundleTest_title"));
		add(getContentsPane(), "Center");
		initConnections();
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
		BundleTest aBundleTest;
		aBundleTest = new BundleTest();
		aBundleTest.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aBundleTest.show();
		java.awt.Insets insets = aBundleTest.getInsets();
		aBundleTest.setSize(aBundleTest.getWidth() + insets.left + insets.right, aBundleTest.getHeight() + insets.top + insets.bottom);
		aBundleTest.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.awt.Frame");//$NON-NLS-1$
		exception.printStackTrace(System.out);
	}
}
}
