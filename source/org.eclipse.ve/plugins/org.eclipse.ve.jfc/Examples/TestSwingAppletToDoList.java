import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Insert the type's description here.
 * Creation date: (11/06/00 11:22:03 PM)
 * @author: Administrator
 * 
 * This class was converted to applet from the TestSwingTodoList example
 */
//public class TestSwingToDoList {
public class TestSwingAppletToDoList extends javax.swing.JApplet {  
	private javax.swing.JButton ivjJButton1 = null;
//	private javax.swing.JFrame ivjJFrame1 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JLabel ivjJLabel11 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
     private javax.swing.JScrollPane ivjJScrollPane = null ;
     private javax.swing.JTable ivjJTable = null ;
     private javax.swing.table.TableColumn ivjTableColumn = null ;
     private javax.swing.table.TableColumn ivjTableColumn2 = null ;
     
     private int taskCount = 0;
     private javax.swing.JTabbedPane ivjJTabbedPane = null ;
     private javax.swing.JScrollPane ivjJScrollPane2 = null ;
     private javax.swing.JList ivjJList = null ;
/**
 * TestSwingToDoList constructor comment.
 */
//public TestSwingToDoList() {
public TestSwingAppletToDoList() {
	super();
//	initialize();
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
			ivjJButton1.setText("Add Item");
			ivjJButton1.setBounds(190, 51, 131, 25);
			ivjJButton1.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					String newTask = getJTextField1().getText();
					if( !newTask.equals( "" ) ) {
						DefaultListModel listModel = ( DefaultListModel ) getIvjJList().getModel();
						listModel.addElement( newTask );
						TableModel model = getIvjJTable().getModel();
						model.setValueAt( newTask, taskCount, 0 );
						model.setValueAt( new GregorianCalendar().getTime().toString(), taskCount, 1 );						
						getJTextField1().setText( "" );
						if( taskCount < model.getRowCount() - 1 )
						    taskCount++;
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
	return ivjJButton1;
}
/**
 * Return the JFrame1 property value.
 * @return javax.swing.JFrame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
//public javax.swing.JFrame getJFrame1() {
//	if (ivjJFrame1 == null) {
//		try {
//			ivjJFrame1 = new javax.swing.JFrame();
//			ivjJFrame1.setName("JFrame1");
//			ivjJFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//			ivjJFrame1.setSize(360, 320);
//			ivjJFrame1.setTitle("To-Do List  Swing Controls");
//			getJFrame1().setContentPane(getJFrameContentPane());
//			// user code begin {1}
//			// user code end
//		} catch (java.lang.Throwable ivjExc) {
//			// user code begin {2}
//			// user code end
//			handleException(ivjExc);
//		}
//	}
//	return ivjJFrame1;
//}
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
			getJFrameContentPane().add(getJLabel1(), getJLabel1().getName());
			getJFrameContentPane().add(getJTextField1(), getJTextField1().getName());
			getJFrameContentPane().add(getJButton1(), getJButton1().getName());
			getJFrameContentPane().add(getJLabel11(), getJLabel11().getName());
			ivjJFrameContentPane.add(getIvjJTabbedPane(), getIvjJTabbedPane().getName()) ;  // JVE Generated
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
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("To-Do Item");
			ivjJLabel1.setBounds(10, 33, 98, 15);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
/**
 * Return the JLabel11 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel11() {
	if (ivjJLabel11 == null) {
		try {
			ivjJLabel11 = new javax.swing.JLabel();
			ivjJLabel11.setName("JLabel11");
			ivjJLabel11.setText("To-Do List");
			ivjJLabel11.setBounds(10, 104, 98, 15);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel11;
}
/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setBounds(10, 57, 140, 19);
			ivjJTextField1.addActionListener( new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					String newTask = getJTextField1().getText();
					if( !newTask.equals( "" ) ) {
						DefaultListModel listModel = ( DefaultListModel ) getIvjJList().getModel();
						listModel.addElement( newTask );
						TableModel model = getIvjJTable().getModel();
						model.setValueAt( newTask, taskCount, 0 );
						model.setValueAt( new GregorianCalendar().getTime().toString(), taskCount, 1 );						
						getJTextField1().setText( "" );
						if( taskCount < model.getRowCount() - 1 )
						    taskCount++;
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
	return ivjJTextField1;
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
//private void initialize() {
public void init() {	
	try {
		// user code begin {1}
		// user code end
		setContentPane(getJFrameContentPane()) ;
		this.setSize(355, 296);
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
//public static void main(java.lang.String[] args) {
//	try {
//		TestSwingToDoList aTestSwingToDoList;
//		aTestSwingToDoList = new TestSwingToDoList();
//		javax.swing.JFrame frame = aTestSwingToDoList.getJFrame1();
//		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
//		frame.setVisible( true );
//	} catch (Throwable exception) {
//		System.err.println("Exception occurred in main() of java.lang.Object");
//		exception.printStackTrace(System.out);
//	}
//}
/**
 * This method initializes ivjJScrollPane
 * 
 * @return javax.swing.JScrollPane
 */
private javax.swing.JScrollPane getIvjJScrollPane() {
     if(ivjJScrollPane == null) {
          try {
               ivjJScrollPane = new javax.swing.JScrollPane() ;  // Explicit Instance
               ivjJScrollPane.setViewportView(getIvjJTable()) ;  // JVE Generated
               ivjJScrollPane.setBounds(10, 130, 329, 146) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJScrollPane;
}
/**
 * This method initializes ivjJTable
 * 
 * @return javax.swing.JTable
 */
private javax.swing.JTable getIvjJTable() {
     if(ivjJTable == null) {
          try {
               ivjJTable = new javax.swing.JTable() ;  // Explicit Instance
               ivjJTable.addColumn(getIvjTableColumn()) ;  // JVE Generated
               ivjJTable.addColumn(getIvjTableColumn2()) ;  // JVE Generated
               ivjJTable.setAutoCreateColumnsFromModel(false) ;  // JVE Generated
			   ivjJTable.setModel(new ToDoListModel());
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTable;
}
/**
 * This method initializes ivjTableColumn
 * 
 * @return javax.swing.table.TableColumn
 */
private javax.swing.table.TableColumn getIvjTableColumn() {
     if(ivjTableColumn == null) {
          try {
               ivjTableColumn = new javax.swing.table.TableColumn() ;  // Explicit Instance
               ivjTableColumn.setHeaderValue( "Task" );
               ivjTableColumn.setPreferredWidth(40) ;  // JVE Generated
               ivjTableColumn.setResizable(false) ;  // JVE Generated
               ivjTableColumn.setModelIndex(0) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjTableColumn;
}

/**
 * This method initializes ivjTableColumn2
 * 
 * @return javax.swing.table.TableColumn
 */
private javax.swing.table.TableColumn getIvjTableColumn2() {
     if(ivjTableColumn2 == null) {
          try {
               ivjTableColumn2 = new javax.swing.table.TableColumn() ;  // Explicit Instance
               ivjTableColumn2.setHeaderValue( "Time Added" );
               ivjTableColumn2.setPreferredWidth(90) ;  // JVE Generated
               ivjTableColumn2.setModelIndex(1) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjTableColumn2;
}

/**
 * This method initializes ivjJTabbedPane
 * 
 * @return javax.swing.JTabbedPane
 */
private javax.swing.JTabbedPane getIvjJTabbedPane() {
     if(ivjJTabbedPane == null) {
          try {
               ivjJTabbedPane = new javax.swing.JTabbedPane() ;  // Explicit Instance
               ivjJTabbedPane.addTab("Task List", null, getIvjJScrollPane2(), null) ;  // JVE Generated
               ivjJTabbedPane.addTab("Details", null, getIvjJScrollPane(), null) ;  // JVE Generated
               ivjJTabbedPane.setBounds(5, 120, 335, 165) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTabbedPane;
}
/**
 * This method initializes ivjJScrollPane2
 * 
 * @return javax.swing.JScrollPane
 */
private javax.swing.JScrollPane getIvjJScrollPane2() {
     if(ivjJScrollPane2 == null) {
          try {
               ivjJScrollPane2 = new javax.swing.JScrollPane() ;  // Explicit Instance
               ivjJScrollPane2.setViewportView(getIvjJList()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJScrollPane2;
}
/**
 * This method initializes ivjJList
 * 
 * @return javax.swing.JList
 */
private javax.swing.JList getIvjJList() {
     if(ivjJList == null) {
          try {
               ivjJList = new javax.swing.JList() ;  // Explicit Instance
               ivjJList.setModel( new DefaultListModel() );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJList;
}
}