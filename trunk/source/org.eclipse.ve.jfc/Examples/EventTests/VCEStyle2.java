/**
 * Insert the type's description here.
 * Creation date: (18/02/2003 15:39:41)
 * @author: 
 */
class VCEStyle2 extends java.awt.Button {
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.awt.Button ivjCancelButton = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.KeyListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == VCEStyle2.this.getCancelButton()) 
				connEtoC3(e);
		};
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getSource() == VCEStyle2.this.getCancelButton()) 
				connEtoC2(e);
			if (e.getSource() == VCEStyle2.this) 
				connEtoC1(e);
		};
		public void keyReleased(java.awt.event.KeyEvent e) {};
		public void keyTyped(java.awt.event.KeyEvent e) {};
	};
/**
 * VCEStule2 constructor comment.
 */
public VCEStyle2() {
	super();
	initialize();
}
/**
 * VCEStule2 constructor comment.
 * @param label java.lang.String
 */
public VCEStyle2(String label) {
	super(label);
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
 * connEtoC1:  (VCEStule2.key.keyPressed(java.awt.event.KeyEvent) --> VCEStule2.vCEStule2_KeyPressed(Ljava.awt.event.KeyEvent;)V)
 * @param arg1 java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.KeyEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.vCEStyle2_KeyPressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (Button1.key.keyPressed(java.awt.event.KeyEvent) --> VCEStule2.button1_KeyPressed(Ljava.awt.event.KeyEvent;)V)
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
 * connEtoC3:  (Button1.action.actionPerformed(java.awt.event.ActionEvent) --> VCEStule2.button1_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
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
 * Return the Button1 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getCancelButton() {
	if (ivjCancelButton == null) {
		try {
			ivjCancelButton = new java.awt.Button();
			ivjCancelButton.setName("CancelButton");
			ivjCancelButton.setBounds(40, 96, 56, 23);
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
	getCancelButton().addKeyListener(ivjEventHandler);
	getCancelButton().addActionListener(ivjEventHandler);
	this.addKeyListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("VCEStyle2");
		setSize(71, 23);
		setLabel("VCEStyle2");
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
		java.awt.Frame frame = new java.awt.Frame();
		VCEStyle2 aVCEStyle2;
		aVCEStyle2 = new VCEStyle2();
		frame.add("Center", aVCEStyle2);
		frame.setSize(aVCEStyle2.getSize());
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
		System.err.println("Exception occurred in main() of VCEStule2");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void vCEStyle2_KeyPressed(java.awt.event.KeyEvent keyEvent) {
	return;
}
}
