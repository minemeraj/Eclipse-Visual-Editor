import java.awt.FileDialog;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
 
/**
 * Insert the type's description here.
 * Creation date: (10/31/00 11:35:53 PM)
 * @author: Administrator
 */
public class TestAwtToDoList {
	private java.awt.Button ivjButton1 = null;
	private java.awt.Button ivjButton2 = null;
	private java.awt.Button ivjButton3 = null;
	private java.awt.Button ivjButton4 = null;
	private java.awt.Panel ivjContentsPane = null;
	private java.awt.Frame ivjFrame1 = null;
	private java.awt.Label ivjLabel1 = null;
	private java.awt.Label ivjLabel2 = null;
	private java.awt.List ivjList1 = null;
	private java.awt.TextField ivjTextField1 = null;
/**
 * TestAwtDoToList constructor comment.
 */
public TestAwtToDoList() {
	super();
	initialize();
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
			ivjButton1.setBounds(198, 54, 131, 50); // Test comment - shouldnt be touched
			ivjButton1.setLabel("Add Item");
			ivjButton1.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    String newTask = getTextField1().getText();
                    if( !newTask.equals( "" ) ) {
                    	getList1().add( newTask );
                    	getTextField1().setText( "" );                    	
                    }
				}				
			} );
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
 * Return the Button2 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton2() {
	if (ivjButton2 == null) {
		try {
			ivjButton2 = new java.awt.Button();
			ivjButton2.setName("Button2");
			ivjButton2.setBounds(198, 134, 131, 25);
			ivjButton2.setEnabled(false);
			ivjButton2.setLabel("Remove Item");
			ivjButton2.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    getList1().remove( getList1().getSelectedItem() );
                    getButton2().setEnabled( false );
                    getButton2().transferFocus();
				}				
			} );
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton2;
}
/**
 * Return the Button3 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton3() {
	if (ivjButton3 == null) {
		try {
			ivjButton3 = new java.awt.Button();
			ivjButton3.setName("Button3");
			ivjButton3.setBounds(198, 215, 131, 25);
			ivjButton3.setLabel("Open To-Do File");
			ivjButton3.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    FileDialog dialog = new FileDialog( getFrame1(), "Select a File" );
                    dialog.show();
                    loadList( dialog.getDirectory() + dialog.getFile() );
				}				
			} );
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton3;
}
/**
 * Return the Button4 property value.
 * @return java.awt.Button
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Button getButton4() {
	if (ivjButton4 == null) {
		try {
			ivjButton4 = new java.awt.Button();
			ivjButton4.setName("Button4");
			ivjButton4.setBounds(198, 251, 131, 25);
			ivjButton4.setLabel("Save To-Do File");
			ivjButton4.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    FileDialog dialog = new FileDialog( getFrame1(), "Select a File" );
                    dialog.show();
                    saveList( dialog.getDirectory() + dialog.getFile() );
				}				
			} );
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButton4;
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
			getContentsPane().add(getTextField1(), getTextField1().getName());
			getContentsPane().add(getButton1(), getButton1().getName());
			getContentsPane().add(getButton2(), getButton2().getName());
			getContentsPane().add(getList1(), getList1().getName());
			getContentsPane().add(getLabel2(), getLabel2().getName());
			getContentsPane().add(getButton3(), getButton3().getName());
			getContentsPane().add(getButton4(), getButton4().getName());
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
public java.awt.Frame getFrame1() {
	if (ivjFrame1 == null) {
		try {
			ivjFrame1 = new java.awt.Frame();
			ivjFrame1.setName("Frame1");
			ivjFrame1.setLayout(new java.awt.BorderLayout());
			ivjFrame1.setBounds(22, 26, 360, 320);
			ivjFrame1.setTitle("To-Do List AWT controls");
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
 * Return the Label1 property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getLabel1() {
	if (ivjLabel1 == null) {
		try {
			ivjLabel1 = new java.awt.Label();
			ivjLabel1.setName("Label1");
			ivjLabel1.setText("To-Do Item");
			ivjLabel1.setBounds(32, 33, 98, 15);
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
 * Return the Label2 property value.
 * @return java.awt.Label
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.Label getLabel2() {
	if (ivjLabel2 == null) {
		try {
			ivjLabel2 = new java.awt.Label();
			ivjLabel2.setName("Label2");
			ivjLabel2.setText("To-Do List");
			ivjLabel2.setBounds(32, 104, 98, 15);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLabel2;
}
/**
 * Return the List1 property value.
 * @return java.awt.List
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.List getList1() {
	if (ivjList1 == null) {
		try {
			ivjList1 = new java.awt.List();
			ivjList1.setName("List1");
			ivjList1.setBounds(32, 133, 140, 143);
			ivjList1.addItemListener( new ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent arg0) {					
					if( arg0.getStateChange() == ItemEvent.SELECTED ) {
						getButton2().setEnabled( true );
					}
				}				
			} );
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjList1;
}
/**
 * Return the TextField1 property value.
 * @return java.awt.TextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private java.awt.TextField getTextField1() {
	if (ivjTextField1 == null) {
		try {
			ivjTextField1 = new java.awt.TextField();
			ivjTextField1.setName("TextField1");
			ivjTextField1.setBounds(32, 57, 140, 19);
			ivjTextField1.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
                    String newTask = getTextField1().getText();
                    if( !newTask.equals( "" ) ) {
                    	getList1().add( newTask );
                    	getTextField1().setText( "" );                    	
                    }
				}				
			} );
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjTextField1;
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

protected void saveList( String filename ) {
	System.out.println( filename );
	FileWriter fileOutStream;
	PrintWriter dataOutStream;
	// carriage return and line feed constant
	String crlf = System.getProperties().getProperty("line.separator");
	String[] tasks = getList1().getItems();

	// write the file from the list
	try {
		fileOutStream = new FileWriter( filename );
		dataOutStream = new PrintWriter(fileOutStream);
		// for every item in the list, write a line to the output file		
		for (int i = 0; i < tasks.length; i++)
			dataOutStream.write( tasks[ i ] + crlf);
		fileOutStream.close();
		dataOutStream.close();
	} catch (Throwable exc) {
		System.out.println( exc.getMessage() );
		handleException(exc);
	}
}

protected void loadList( String filename ) {
	FileReader fileInStream;
	BufferedReader dataInStream;
	String result;
	try {
		// read the file and fill the list
		fileInStream = new FileReader( filename );
		dataInStream = new BufferedReader(fileInStream);
		// clear the existing entries from the list
		getList1().removeAll();
		// for each line in the file create an item in the list
		while ((result = dataInStream.readLine()) != null) {
			if (result.length() != 0)
				getList1().add( result );
		}
		fileInStream.close();
		dataInStream.close();
	} catch (Throwable exc) {
		handleException(exc);
	}
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		TestAwtToDoList aTestAwtDoToList;
		aTestAwtDoToList = new TestAwtToDoList();
		java.awt.Frame frame = aTestAwtDoToList.getFrame1();
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent arg0) {
				System.exit( 1 );
			}		
		} );
		frame.setVisible( true );
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.lang.Object");
		exception.printStackTrace(System.out);
	}
}
}
