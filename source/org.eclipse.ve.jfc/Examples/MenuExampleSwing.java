import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;

/**
 * Insert the type's description here.
 * Creation date: (4/9/2002 2:13:52 PM)
 * @author: Administrator
 */
class MenuExampleSwing {
	private javax.swing.JFrame ivjJFrame1 = null;  //  @jve:visual-info  decl-index=0 visual-constraint="0,43"
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JMenuBar ivjJFrame1JMenuBar = null;
	private javax.swing.JMenu ivjJMenu1 = null;
	private javax.swing.JMenu ivjJMenu2 = null;
	private javax.swing.JMenu ivjJMenu3 = null;
	private javax.swing.JMenuItem ivjJMenuItem1 = null;
	private javax.swing.JMenuItem ivjJMenuItem2 = null;
	private javax.swing.JCheckBoxMenuItem ivjJCheckBoxMenuItem1 = null;
	private javax.swing.JCheckBoxMenuItem ivjJCheckBoxMenuItem2 = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JMenu ivjJMenu4 = null;
	private javax.swing.JMenu ivjJMenu5 = null;
	private javax.swing.JMenu ivjJMenu6 = null;
	private javax.swing.JMenuItem ivjJMenuItem4 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem1 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem2 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem3 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem4 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem5 = null;
	private javax.swing.JRadioButtonMenuItem ivjJRadioButtonMenuItem6 = null;
/**
 * Test constructor comment.
 */
public MenuExampleSwing() {
	super();
	initialize();

	getJFrame1().setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
	getJFrame1().setVisible( true );
}
/**
 * Return the JCheckBoxMenuItem1 property value.
 * @return javax.swing.JCheckBoxMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBoxMenuItem getJCheckBoxMenuItem1() {
	if (ivjJCheckBoxMenuItem1 == null) {
		try {
			ivjJCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
			ivjJCheckBoxMenuItem1.setName("JCheckBoxMenuItem1");
			ivjJCheckBoxMenuItem1.setText("Bold");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJCheckBoxMenuItem1;
}
/**
 * Return the JCheckBoxMenuItem2 property value.
 * @return javax.swing.JCheckBoxMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JCheckBoxMenuItem getJCheckBoxMenuItem2() {
	if (ivjJCheckBoxMenuItem2 == null) {
		try {
			ivjJCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
			ivjJCheckBoxMenuItem2.setName("JCheckBoxMenuItem2");
			ivjJCheckBoxMenuItem2.setFont(new java.awt.Font("dialog", 2, 12));
			ivjJCheckBoxMenuItem2.setText("Italics");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJCheckBoxMenuItem2;
}
/**
 * Return the JFrame1 property value.
 * @return javax.swing.JFrame
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JFrame getJFrame1() {
	if (ivjJFrame1 == null) {
		try {
			ivjJFrame1 = new javax.swing.JFrame();
			ivjJFrame1.setName("JFrame1");
			ivjJFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			ivjJFrame1.setBounds(45, 39, 530, 145);
			ivjJFrame1.setJMenuBar(getJFrame1JMenuBar());
			getJFrame1().setContentPane(getJFrameContentPane());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrame1;
}
/**
 * Return the JFrame1JMenuBar property value.
 * @return javax.swing.JMenuBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuBar getJFrame1JMenuBar() {
	if (ivjJFrame1JMenuBar == null) {
		try {
			ivjJFrame1JMenuBar = new javax.swing.JMenuBar();
			ivjJFrame1JMenuBar.setName("JFrame1JMenuBar");
			ivjJFrame1JMenuBar.add(getJMenu1());
			ivjJFrame1JMenuBar.add(getJMenu2());
			ivjJFrame1JMenuBar.add(getJMenu4());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrame1JMenuBar;
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
			ivjJFrameContentPane.setLayout(new java.awt.FlowLayout());
			getJFrameContentPane().add(getJLabel1(), getJLabel1().getName());
			ivjJFrameContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(20,5,5,20)) ;  // JVE Generated
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
			ivjJLabel1.setText("The quick brown fox jumped over the lazy dog.");
			ivjJLabel1.setForeground(java.awt.Color.black);
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
 * Return the JMenu1 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu1() {
	if (ivjJMenu1 == null) {
		try {
			ivjJMenu1 = new javax.swing.JMenu();
			ivjJMenu1.setName("JMenu1");
			ivjJMenu1.setMnemonic('F');
			ivjJMenu1.setText("File");
			ivjJMenu1.add(getJMenuItem4());
			ivjJMenu1.add(getJMenuItem1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu1;
}
/**
 * Return the JMenu2 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu2() {
	if (ivjJMenu2 == null) {
		try {
			ivjJMenu2 = new javax.swing.JMenu();
			ivjJMenu2.setName("JMenu2");
			ivjJMenu2.setMnemonic('x');
			ivjJMenu2.setText("Text Settings");
			ivjJMenu2.add(getJMenu6());
			ivjJMenu2.add(getJMenu3());
			ivjJMenu2.add(getJMenu5());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu2;
}
/**
 * Return the JMenu3 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu3() {
	if (ivjJMenu3 == null) {
		try {
			ivjJMenu3 = new javax.swing.JMenu();
			ivjJMenu3.setName("JMenu3");
			ivjJMenu3.setText("Size");
			ivjJMenu3.add(getJRadioButtonMenuItem4());
			ivjJMenu3.add(getJRadioButtonMenuItem5());
			ivjJMenu3.add(getJRadioButtonMenuItem6());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu3;
}
/**
 * Return the JMenu4 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu4() {
	if (ivjJMenu4 == null) {
		try {
			ivjJMenu4 = new javax.swing.JMenu();
			ivjJMenu4.setName("JMenu4");
			ivjJMenu4.setMnemonic('h');
			ivjJMenu4.setText("Help");
			ivjJMenu4.add(getJMenuItem2());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu4;
}
/**
 * Return the JMenu5 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu5() {
	if (ivjJMenu5 == null) {
		try {
			ivjJMenu5 = new javax.swing.JMenu();
			ivjJMenu5.setName("JMenu5");
			ivjJMenu5.setText("Color");
			ivjJMenu5.add(getJRadioButtonMenuItem3());
			ivjJMenu5.add(getJRadioButtonMenuItem2());
			ivjJMenu5.add(getJRadioButtonMenuItem1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu5;
}
/**
 * Return the JMenu6 property value.
 * @return javax.swing.JMenu
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenu getJMenu6() {
	if (ivjJMenu6 == null) {
		try {
			ivjJMenu6 = new javax.swing.JMenu();
			ivjJMenu6.setName("JMenu6");
			ivjJMenu6.setText("Style");
			ivjJMenu6.add(getJCheckBoxMenuItem1());
			ivjJMenu6.add(getJCheckBoxMenuItem2());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenu6;
}
/**
 * Return the JMenuItem1 property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getJMenuItem1() {
	if (ivjJMenuItem1 == null) {
		try {
			ivjJMenuItem1 = new javax.swing.JMenuItem();
			ivjJMenuItem1.setName("JMenuItem1");
			ivjJMenuItem1.setText("Exit");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenuItem1;
}
/**
 * Return the JMenuItem2 property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getJMenuItem2() {
	if (ivjJMenuItem2 == null) {
		try {
			ivjJMenuItem2 = new javax.swing.JMenuItem();
			ivjJMenuItem2.setName("JMenuItem2");
			ivjJMenuItem2.setText("About...");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenuItem2;
}
/**
 * Return the JMenuItem4 property value.
 * @return javax.swing.JMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JMenuItem getJMenuItem4() {
	if (ivjJMenuItem4 == null) {
		try {
			ivjJMenuItem4 = new javax.swing.JMenuItem();
			ivjJMenuItem4.setName("JMenuItem4");
			ivjJMenuItem4.setText("Reset");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJMenuItem4;
}
/**
 * Return the JRadioButtonMenuItem1 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem1() {
	if (ivjJRadioButtonMenuItem1 == null) {
		try {
			ivjJRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem1.setName("JRadioButtonMenuItem1");
			ivjJRadioButtonMenuItem1.setText("Green");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem1;
}
/**
 * Return the JRadioButtonMenuItem2 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem2() {
	if (ivjJRadioButtonMenuItem2 == null) {
		try {
			ivjJRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem2.setName("JRadioButtonMenuItem2");
			ivjJRadioButtonMenuItem2.setText("Red");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem2;
}
/**
 * Return the JRadioButtonMenuItem3 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem3() {
	if (ivjJRadioButtonMenuItem3 == null) {
		try {
			ivjJRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem3.setName("JRadioButtonMenuItem3");
			ivjJRadioButtonMenuItem3.setText("Black");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem3;
}
/**
 * Return the JRadioButtonMenuItem4 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem4() {
	if (ivjJRadioButtonMenuItem4 == null) {
		try {
			ivjJRadioButtonMenuItem4 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem4.setName("JRadioButtonMenuItem4");
			ivjJRadioButtonMenuItem4.setSelected(false);
			ivjJRadioButtonMenuItem4.setText("12");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem4;
}
/**
 * Return the JRadioButtonMenuItem5 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem5() {
	if (ivjJRadioButtonMenuItem5 == null) {
		try {
			ivjJRadioButtonMenuItem5 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem5.setName("JRadioButtonMenuItem5");
			ivjJRadioButtonMenuItem5.setText("14");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem5;
}
/**
 * Return the JRadioButtonMenuItem6 property value.
 * @return javax.swing.JRadioButtonMenuItem
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem6() {
	if (ivjJRadioButtonMenuItem6 == null) {
		try {
			ivjJRadioButtonMenuItem6 = new javax.swing.JRadioButtonMenuItem();
			ivjJRadioButtonMenuItem6.setName("JRadioButtonMenuItem6");
			ivjJRadioButtonMenuItem6.setText("16");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJRadioButtonMenuItem6;
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

private void initializeMenuItems() {
	getJCheckBoxMenuItem1().setSelected( true );
	getJCheckBoxMenuItem2().setSelected( false );
	getJRadioButtonMenuItem3().setSelected( true );
	getJRadioButtonMenuItem4().setSelected( true );
	
	getJLabel1().setFont( new Font( getJLabel1().getFont().getName(),
	                                Font.BOLD,
	                                12 ) );
	getJLabel1().setForeground( java.awt.Color.black );
}

/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		ButtonGroup group1 = new ButtonGroup();
		group1.add( getJRadioButtonMenuItem1() );
		group1.add( getJRadioButtonMenuItem2() );
		group1.add( getJRadioButtonMenuItem3() );
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add( getJRadioButtonMenuItem4() );
		group2.add( getJRadioButtonMenuItem5() );
		group2.add( getJRadioButtonMenuItem6() );
		
		initializeMenuItems();		
		
		getJMenuItem1().addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.exit( 1 );	
			}			
		} );		
		getJMenuItem4().addActionListener( new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				initializeMenuItems();	
			}
		} );
		getJCheckBoxMenuItem1().addItemListener( new ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				java.awt.Font currentFont = getJLabel1().getFont();
				int style = currentFont.getStyle();
                if( getJCheckBoxMenuItem1().isSelected() ) {
                	style += java.awt.Font.BOLD;
                }
                else {
                	style -= java.awt.Font.BOLD;
                }
                currentFont = new java.awt.Font(
                	currentFont.getName(),
                	style,
                	currentFont.getSize() );
                getJLabel1().setFont( currentFont );
			}
		} );
		getJCheckBoxMenuItem2().addItemListener( new ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				java.awt.Font currentFont = getJLabel1().getFont();
				int style = currentFont.getStyle();
                if( getJCheckBoxMenuItem2().isSelected() ) {
                	style += java.awt.Font.ITALIC;
                }
                else {
                	style -= java.awt.Font.ITALIC;
                }
                currentFont = new java.awt.Font(
                	currentFont.getName(),
                	style,
                	currentFont.getSize() );
                getJLabel1().setFont( currentFont );
			}
		} );
		getJRadioButtonMenuItem1().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem1().isSelected() )
					getJLabel1().setForeground( java.awt.Color.green );
			}
		} );
		getJRadioButtonMenuItem2().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem2().isSelected() )
					getJLabel1().setForeground( java.awt.Color.red );
			}
		} );
		getJRadioButtonMenuItem3().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem3().isSelected() )
					getJLabel1().setForeground( java.awt.Color.black );
			}
		} );
		getJRadioButtonMenuItem4().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem4().isSelected() )
					getJLabel1().setFont( new Font(
						getJLabel1().getFont().getName(),
						getJLabel1().getFont().getStyle(),
						12 ) );
			}
		} );
		getJRadioButtonMenuItem5().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem5().isSelected() )
					getJLabel1().setFont( new Font(
						getJLabel1().getFont().getName(),
						getJLabel1().getFont().getStyle(),
						14 ) );
			}
		} );
		getJRadioButtonMenuItem6().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if( getJRadioButtonMenuItem6().isSelected() )
					getJLabel1().setFont( new Font(
						getJLabel1().getFont().getName(),
						getJLabel1().getFont().getStyle(),
						16 ) );
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
		MenuExampleSwing aTest;
		aTest = new MenuExampleSwing();
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.lang.Object");
		exception.printStackTrace(System.out);
	}
}
}