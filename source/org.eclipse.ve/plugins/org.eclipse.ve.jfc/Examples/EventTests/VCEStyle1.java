/**
 * Insert the type's description here.
 * Creation date: (18/02/2003 15:56:07)
 * @author: 
 */
class VCEStyle1 extends java.awt.Button implements java.awt.event.ActionListener, java.awt.event.KeyListener {
	private java.awt.Button ivjCancelButton = null;
/**
 * VCEStyle1 constructor comment.
 */
public VCEStyle1() {
	super();
	initialize();
}
/**
 * VCEStyle1 constructor comment.
 * @param label java.lang.String
 */
public VCEStyle1(String label) {
	super(label);
}
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getCancelButton()) 
		connEtoC3(e);
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void cancelButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	return;
}
/**
 * Comment
 */
public void cancelButton_KeyPressed(java.awt.event.KeyEvent keyEvent) {
	return;
}
/**
 * connEtoC1:  (VCEStyle1.key.keyPressed(java.awt.event.KeyEvent) --> VCEStyle1.vCEStyle1_KeyPressed(Ljava.awt.event.KeyEvent;)V)
 * @param arg1 java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.KeyEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.vCEStyle1_KeyPressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (CancelButton.key.keyPressed(java.awt.event.KeyEvent) --> VCEStyle1.cancelButton_KeyPressed(Ljava.awt.event.KeyEvent;)V)
 * @param arg1 java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.KeyEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cancelButton_KeyPressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (CancelButton.action.actionPerformed(java.awt.event.ActionEvent) --> VCEStyle1.cancelButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cancelButton_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the CancelButton property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getCancelButton() {
	if (ivjCancelButton == null) {
		try {
			ivjCancelButton = new java.awt.Button();
			ivjCancelButton.setName("CancelButton");
			ivjCancelButton.setBounds(27, 103, 56, 23);
			ivjCancelButton.setLabel("Cancel");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCancelButton;
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
	this.addKeyListener(this);
	getCancelButton().addKeyListener(this);
	getCancelButton().addActionListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("VCEStyle1");
		setSize(71, 23);
		setLabel("OK");
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the KeyListener interface.
 * @param e java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void keyPressed(java.awt.event.KeyEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == this) 
		connEtoC1(e);
	if (e.getSource() == getCancelButton()) 
		connEtoC2(e);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the KeyListener interface.
 * @param e java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void keyReleased(java.awt.event.KeyEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the KeyListener interface.
 * @param e java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void keyTyped(java.awt.event.KeyEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		java.awt.Frame frame = new java.awt.Frame();
		VCEStyle1 aVCEStyle1;
		aVCEStyle1 = new VCEStyle1();
		frame.add("Center", aVCEStyle1);
		frame.setSize(aVCEStyle1.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of VCEStyle1");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void vCEStyle1_KeyPressed(java.awt.event.KeyEvent keyEvent) {
	return;
}
}
