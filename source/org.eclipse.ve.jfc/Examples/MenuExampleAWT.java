import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * Insert the type's description here.
 * Creation date: (4/24/2002 5:04:56 PM)
 * @author: Administrator
 */
class MenuExampleAWT {
	private java.awt.CheckboxMenuItem ivjCheckboxMenuItem1 = null;
	private java.awt.Panel ivjContentsPane = null;
	private java.awt.Frame ivjFrame1 = null;  //  @jve:visual-info  decl-index=0 visual-constraint="0,43"
	private java.awt.MenuBar ivjFrame1MenuBar = null;
	private java.awt.Label ivjLabel1 = null;
	private java.awt.Menu ivjMenu1 = null;
	private java.awt.Menu ivjMenu2 = null;
	private java.awt.Menu ivjMenu3 = null;
	private java.awt.MenuItem ivjMenuItem3 = null;
	private java.awt.MenuItem ivjMenuItem4 = null;
	private java.awt.MenuItem ivjMenuItem5 = null;
/**
 * MenuExampleAWT constructor comment.
 */
public MenuExampleAWT() {
	super();
	initialize();

	getFrame1().addWindowListener( new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent e) {
			System.exit( 1 );
		}		
		
	} );
	getFrame1().setVisible( true );	
}
/**
 * Return the CheckboxMenuItem1 property value.
 * @return java.awt.CheckboxMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.CheckboxMenuItem getCheckboxMenuItem1() {
	if (ivjCheckboxMenuItem1 == null) {
		try {
			ivjCheckboxMenuItem1 = new java.awt.CheckboxMenuItem();
			ivjCheckboxMenuItem1.setLabel("Display Text");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCheckboxMenuItem1;
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
			ivjContentsPane.setLayout(null);
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
 * Return the Frame1 property value.
 * @return java.awt.Frame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Frame getFrame1() {
	if (ivjFrame1 == null) {
		try {
			ivjFrame1 = new java.awt.Frame();
			ivjFrame1.setName("Frame1");
			ivjFrame1.setMenuBar(getFrame1MenuBar());
			ivjFrame1.setLayout(new java.awt.BorderLayout());
			ivjFrame1.setBounds(41, 40, 477, 130);
			getFrame1().add(getContentsPane(), "Center");
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
 * Return the Frame1MenuBar property value.
 * @return java.awt.MenuBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.MenuBar getFrame1MenuBar() {
	if (ivjFrame1MenuBar == null) {
		try {
			ivjFrame1MenuBar = new java.awt.MenuBar();
			ivjFrame1MenuBar.add(getMenu2());
			ivjFrame1MenuBar.add(getMenu1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFrame1MenuBar;
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
			ivjLabel1.setFont(new java.awt.Font("sansserif", 1, 18));
			ivjLabel1.setText("The lazy dog jumped over the quick red fox.");
			ivjLabel1.setBounds(39, 27, 395, 47);
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
 * Return the Menu1 property value.
 * @return java.awt.Menu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Menu getMenu1() {
	if (ivjMenu1 == null) {
		try {
			ivjMenu1 = new java.awt.Menu();
			ivjMenu1.setLabel("Settings");
			ivjMenu1.add(getCheckboxMenuItem1());
			ivjMenu1.add(getMenu3());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenu1;
}
/**
 * Return the Menu2 property value.
 * @return java.awt.Menu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Menu getMenu2() {
	if (ivjMenu2 == null) {
		try {
			ivjMenu2 = new java.awt.Menu();
			ivjMenu2.setLabel("File");
			ivjMenu2.add(getMenuItem5());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenu2;
}
/**
 * Return the Menu3 property value.
 * @return java.awt.Menu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Menu getMenu3() {
	if (ivjMenu3 == null) {
		try {
			ivjMenu3 = new java.awt.Menu();
			ivjMenu3.setLabel("Colors...");
			ivjMenu3.add(getMenuItem3());
			ivjMenu3.add(getMenuItem4());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenu3;
}
/**
 * Return the MenuItem3 property value.
 * @return java.awt.MenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.MenuItem getMenuItem3() {
	if (ivjMenuItem3 == null) {
		try {
			ivjMenuItem3 = new java.awt.MenuItem();
			ivjMenuItem3.setLabel("Paint text green");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenuItem3;
}
/**
 * Return the MenuItem4 property value.
 * @return java.awt.MenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.MenuItem getMenuItem4() {
	if (ivjMenuItem4 == null) {
		try {
			ivjMenuItem4 = new java.awt.MenuItem();
			ivjMenuItem4.setLabel("Paint text red");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenuItem4;
}
/**
 * Return the MenuItem5 property value.
 * @return java.awt.MenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.MenuItem getMenuItem5() {
	if (ivjMenuItem5 == null) {
		try {
			ivjMenuItem5 = new java.awt.MenuItem();
			ivjMenuItem5.setLabel("Exit");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMenuItem5;
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
		// user code begin {1}
		getCheckboxMenuItem1().setState( true );
		getFrame1().setBackground( java.awt.Color.black );
		getLabel1().setForeground( java.awt.Color.green );				
		
		getMenuItem3().addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				getLabel1().setForeground( java.awt.Color.green );
			}
		} );
		getMenuItem4().addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				getLabel1().setForeground( java.awt.Color.red );
			}
		} );
		getMenuItem5().addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.exit( 1 );
			}
		} );
		getCheckboxMenuItem1().addItemListener( new ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				getLabel1().setVisible( getCheckboxMenuItem1().getState() );				
			}
		} );		
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
		MenuExampleAWT aMenuExampleAWT;
		aMenuExampleAWT = new MenuExampleAWT();
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.lang.Object");
		exception.printStackTrace(System.out);
	}
}
}