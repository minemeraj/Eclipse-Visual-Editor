/**
 * Insert the type's description here.
 * Creation date: (6/14/2002 10:05:56 AM)
 * @author: 
 */
class VAJJSplitPaneTest extends javax.swing.JFrame {
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JButton ivjJButton2 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JSplitPane ivjJSplitPane1 = null;
/**
 * VAJJSplitPaneTest constructor comment.
 */
public VAJJSplitPaneTest() {
	super();
	initialize();
}
/**
 * VAJJSplitPaneTest constructor comment.
 * @param title java.lang.String
 */
public VAJJSplitPaneTest(String title) {
	super(title);
}
/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton1() {
	if (ivjJButton1 == null) {
		try {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setName("JButton1");
			ivjJButton1.setText("Top Button");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButton1;
}
/**
 * Return the JButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton2() {
	if (ivjJButton2 == null) {
		try {
			ivjJButton2 = new javax.swing.JButton();
			ivjJButton2.setName("JButton2");
			ivjJButton2.setText("Bottom Button");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButton2;
}
/**
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(null);
			getJFrameContentPane().add(getJSplitPane1(), getJSplitPane1().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrameContentPane;
}
/**
 * Return the JSplitPane1 property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JSplitPane getJSplitPane1() {
	if (ivjJSplitPane1 == null) {
		try {
			ivjJSplitPane1 = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
			ivjJSplitPane1.setName("JSplitPane1");
			ivjJSplitPane1.setBounds(28, 23, 326, 181);
			getJSplitPane1().add(getJButton1(), "top");
			getJSplitPane1().add(getJButton2(), "bottom");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJSplitPane1;
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
		setName("VAJJSplitPaneTest");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(426, 240);
		setContentPane(getJFrameContentPane());
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
		VAJJSplitPaneTest aVAJJSplitPaneTest;
		aVAJJSplitPaneTest = new VAJJSplitPaneTest();
		aVAJJSplitPaneTest.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aVAJJSplitPaneTest.show();
		java.awt.Insets insets = aVAJJSplitPaneTest.getInsets();
		aVAJJSplitPaneTest.setSize(aVAJJSplitPaneTest.getWidth() + insets.left + insets.right, aVAJJSplitPaneTest.getHeight() + insets.top + insets.bottom);
		aVAJJSplitPaneTest.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
}
