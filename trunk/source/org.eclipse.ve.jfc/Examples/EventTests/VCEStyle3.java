/**
 * Insert the type's description here.
 * Creation date: (18/02/2003 16:16:06)
 * @author: 
 */
class VCEStyle3 extends java.awt.Button {
	private java.awt.Button ivjCancelButton = null;
/**
 * VCEStyle3 constructor comment.
 */
public VCEStyle3() {
	super();
	initialize();
}
/**
 * VCEStyle3 constructor comment.
 * @param label java.lang.String
 */
public VCEStyle3(String label) {
	super(label);
}
/**
 * Comment
 */
public void cancelButton_MouseEntered(java.awt.event.MouseEvent mouseEvent) {
	return;
}
/**
 * Comment
 */
public void cancelButton_MouseExited(java.awt.event.MouseEvent mouseEvent) {
	return;
}
/**
 * connEtoC1:  (VCEStyle3.key.keyPressed(java.awt.event.KeyEvent) --> VCEStyle3.vCEStyle3_KeyPressed(Ljava.awt.event.KeyEvent;)V)
 * @param arg1 java.awt.event.KeyEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.KeyEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.vCEStyle3_KeyPressed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (CancelButton.mouse.mouseEntered(java.awt.event.MouseEvent) --> VCEStyle3.cancelButton_MouseEntered(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cancelButton_MouseEntered(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (CancelButton.mouse.mouseExited(java.awt.event.MouseEvent) --> VCEStyle3.cancelButton_MouseExited(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.cancelButton_MouseExited(arg1);
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
			ivjCancelButton.setBounds(33, 95, 56, 23);
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
	this.addKeyListener(new java.awt.event.KeyAdapter() {
		public void keyPressed(java.awt.event.KeyEvent e) {
			connEtoC1(e);
		};
	});
	getCancelButton().addMouseListener(new java.awt.event.MouseAdapter() {
		public void mouseEntered(java.awt.event.MouseEvent e) {
			connEtoC2(e);
		};
	});
	getCancelButton().addMouseListener(new java.awt.event.MouseAdapter() {
		public void mouseExited(java.awt.event.MouseEvent e) {
			connEtoC3(e);
		};
	});
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("VCEStyle3");
		setSize(71, 23);
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
		VCEStyle3 aVCEStyle3;
		aVCEStyle3 = new VCEStyle3();
		frame.add("Center", aVCEStyle3);
		frame.setSize(aVCEStyle3.getSize());
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
		System.err.println("Exception occurred in main() of VCEStyle3");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void vCEStyle3_KeyPressed(java.awt.event.KeyEvent keyEvent) {
	return;
}
/**
 * Comment
 */
public void vCEStyle3_KeyReleased(java.awt.event.KeyEvent keyEvent) {
	return;
}
}
