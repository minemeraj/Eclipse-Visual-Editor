import java.util.* ;

/**
 * Insert the type's description here.
 * Creation date: (11/30/2001 10:19:19 AM)
 * @author: Administrator
 */
public class AWTtest1 {
	private java.awt.Container ivjContainer = null ;
	private java.awt.Button ivjButton1 = null;
	private java.awt.Checkbox ivjCheckbox1 = null;
	private java.awt.Checkbox ivjCheckbox2 = null;
	private java.awt.Label ivjLabel1 = null;
/**
 * AWTtest1 constructor comment.
 */
public AWTtest1() {
	super();
}

/**
 * Return the Button1 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. <--- NOT anymore */
private java.awt.Button getButton1() {
	int I ;
	if (ivjButton1 == null) {
		try {
			/* This comment is part of the code and will not be removed
			   neigher will the following loop */
			for (I=0; I<5; I++) 
			  System.out.println(I) ;
			ivjButton1 = new java.awt.Button();
			ivjButton1.setName("the Name");
			ivjButton1.setBackground(java.awt.Color.blue);
			ivjButton1.setBounds(71, 76, 98, 39);
			ivjButton1.setForeground(java.awt.Color.white);
			ivjButton1.setLabel("the Label");
			// user code begin {1}
			/* CodeGen by default will parse here as well */
			ivjButton1.setVisible(true) ;
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
 * Return the Checkbox1 property value.
 * @return java.awt.Checkbox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Checkbox getCheckbox1() {
	if (ivjCheckbox1 == null) {
		try {
			ivjCheckbox1 = new java.awt.Checkbox();
			ivjCheckbox1.setName("Checkbox1");
			ivjCheckbox1.setBounds(287, 99, 91, 23);
			ivjCheckbox1.setEnabled(false) ;
			ivjCheckbox1.setLabel("Grayed");
			// user code begin {1}

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckbox1;
}
/**
 * Return the Checkbox2 property value.
 * @return java.awt.Checkbox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */ 
private java.awt.Checkbox getCheckbox2() {
	if (ivjCheckbox2 == null) {
		try {
			ivjCheckbox2 = new java.awt.Checkbox();
			ivjCheckbox2.setName("Checkbox2");
			ivjCheckbox2.setBounds(287, 140, 91, 23);
			ivjCheckbox2.setForeground(java.awt.Color.red);
			ivjCheckbox2.setLabel("Active");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckbox2;
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
			ivjLabel1.setText("AWT Container null Layout");
			ivjLabel1.setBounds(158, 16, 126, 33);
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
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Container getContainer() {
	if (ivjContainer != null) return ivjContainer ;
	try {
		// user code begin {1}
		// user code end
		ivjContainer = new java.awt.Container() ;
		
		ivjContainer.setName("AWTtest1");
		ivjContainer.setSize(457, 308);
		ivjContainer.add(getButton1(), getButton1().getName());
		ivjContainer.add(getLabel1(), getLabel1().getName());
		ivjContainer.add(getCheckbox1(), getCheckbox1().getName());
		ivjContainer.add(getCheckbox2(), getCheckbox2().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
	return ivjContainer ;
}


/* Instance Variables can be defined here too */
int  finBetweenInstanceVariable ;


/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
}