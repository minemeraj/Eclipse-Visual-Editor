import javax.swing.JColorChooser;
import javax.swing.JFrame;

import java.awt.event.ActionListener;

public class CardLayoutExample extends JFrame {

     private javax.swing.JPanel ivjJPanel = null ;
     private javax.swing.JPanel ivjJPanel2 = null ;
     private javax.swing.JPanel ivjJPanel4 = null ;
     private javax.swing.JPanel ivjJPanel3 = null ;
     private javax.swing.JLabel ivjJLabel_1 = null ;
     private javax.swing.JLabel ivjJLabel = null ;
     private javax.swing.JButton ivjJButton = null ;
     private javax.swing.JButton ivjJButton2 = null ;
     private javax.swing.JButton ivjJButton3 = null ;
     private javax.swing.JPanel ivjJPanel5 = null ;
     private javax.swing.JPanel ivjJPanel7 = null ;
     private javax.swing.JCheckBox ivjJCheckBox_2 = null ;
     private javax.swing.JCheckBox ivjJCheckBox_1 = null ;
     private javax.swing.JButton ivjJButton_1 = null ;
     private javax.swing.JTextField ivjJTextField_1 = null ;
     private javax.swing.JLabel ivjJLabel_3 = null ;
     private javax.swing.JPanel ivjJPanel_3 = null ;
     private javax.swing.JPanel ivjJPanel6 = null ;
     private javax.swing.JLabel ivjJLabel_4 = null ;
     private javax.swing.JTextField ivjJTextField = null ;
     private javax.swing.JCheckBox ivjJCheckBox = null ;
     private javax.swing.JLabel ivjJLabel2 = null ;
     private javax.swing.JTextField ivjJTextField2 = null ;
     private javax.swing.JCheckBox ivjJCheckBox2 = null ;
     private javax.swing.JPanel ivjJPanel8 = null ;
     private javax.swing.JLabel ivjJLabel3 = null ;
     private javax.swing.JLabel ivjJLabel4 = null ;
/**
 * This method initializes 
 * 
 */
public CardLayoutExample() {
     super() ;
     initialize() ;
     setVisible( true );
}
/**
 * This method initializes this
 * 
 * @return void
 */
private void initialize() {
     try {
         this.setContentPane(getIvjJPanel()) ;  // JVE Generated
         this.setSize(456, 256) ;  // JVE Generated
         this.setTitle("Self-Configuring Wizard") ;  // JVE Generated
               
     }
     catch (java.lang.Throwable e) {
         //  Do Something
     }
}
/**
 * This method initializes ivjJPanel
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel() {               
     if(ivjJPanel == null) {
          try {          	
               java.awt.BorderLayout layBorderLayout_1 = new java.awt.BorderLayout() ;  // Explicit Instance
               ivjJPanel = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel.setLayout(layBorderLayout_1) ;  // JVE Generated
               ivjJPanel.add(getIvjJPanel2(), "North") ;  // JVE Generated
               ivjJPanel.add(getIvjJPanel4(), "Center") ;  // JVE Generated
               ivjJPanel.add(getIvjJPanel3(), "South") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
              e.printStackTrace();
          }
     }
     return ivjJPanel;
}
/**
 * This method initializes ivjJPanel2
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel2() {
     if(ivjJPanel2 == null) {
          try {
               java.awt.GridLayout layGridLayout_2 = new java.awt.GridLayout() ;  // Explicit Instance
               layGridLayout_2.setColumns(1) ;  // JVE Generated
               layGridLayout_2.setRows(0) ;  // JVE Generated               
               ivjJPanel2 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel2.setLayout(layGridLayout_2) ;  // JVE Generated
               ivjJPanel2.add(getIvjJLabel_1(), getIvjJLabel_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJLabel(), getIvjJLabel().getName()) ;  // JVE Generated
               ivjJPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
              e.printStackTrace();
          }
     }
     return ivjJPanel2;
}

/**
 * This method initializes ivjJPanel4
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel4() {
     if(ivjJPanel4 == null) {
          try {
               java.awt.CardLayout layCardLayout_3 = new java.awt.CardLayout() ;  // Explicit Instance
               ivjJPanel4 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel4.setLayout(layCardLayout_3) ;  // JVE Generated
               ivjJPanel4.add(getIvjJPanel8(), "Instructions") ;  // JVE Generated
               ivjJPanel4.add(getIvjJPanel5(), "TextProps") ;  // JVE Generated
               ivjJPanel4.add(getIvjJPanel7(), "ButtonProps") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel4;
}
/**
 * This method initializes ivjJPanel3
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel3() {
     if(ivjJPanel3 == null) {
          try {
               ivjJPanel3 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel3.add(getIvjJButton(), getIvjJButton().getName()) ;  // JVE Generated
               ivjJPanel3.add(getIvjJButton2(), getIvjJButton2().getName()) ;  // JVE Generated
               ivjJPanel3.add(getIvjJButton3(), getIvjJButton3().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel3;
}
/**
 * This method initializes ivjJLabel_1
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel_1() {
     if(ivjJLabel_1 == null) {
          try {
               ivjJLabel_1 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel_1.setText("The Center panel of this frame has a CardLayout layout manager.") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel_1;
}
/**
 * This method initializes ivjJLabel
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel() {
     if(ivjJLabel == null) {
          try {
               ivjJLabel = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel.setText("Use the buttons on the South panel to flip through the pages.") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel;
}
/**
 * This method initializes ivjJButton
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton() {
     if(ivjJButton == null) {
          try {
               ivjJButton = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton.setText("<== Previous") ;  // JVE Generated
               ivjJButton.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           getCardLayout().previous( getIvjJPanel4() );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton;
}
/**
 * This method initializes ivjJButton2
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton2() {
     if(ivjJButton2 == null) {
          try {
               ivjJButton2 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton2.setText("Next ==>") ;  // JVE Generated
               ivjJButton2.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           getCardLayout().next( getIvjJPanel4() );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton2;
}
/**
 * This method initializes ivjJButton3
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton3() {
     if(ivjJButton3 == null) {
          try {
               ivjJButton3 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton3.setText("Exit") ;  // JVE Generated
               ivjJButton3.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           System.exit( 1 );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton3;
}
/**
 * This method initializes ivjJPanel5
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel5() {
     if(ivjJPanel5 == null) {
          try {
               ivjJPanel5 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel5.setLayout(null) ;  // JVE Generated
               ivjJPanel5.add(getIvjJPanel_3(), getIvjJPanel_3().getName()) ;  // JVE Generated
               ivjJPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Page 2 of 3 - Text Properties" ,0,0)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel5;
}
/**
 * This method initializes ivjJPanel7
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel7() {
     if(ivjJPanel7 == null) {
          try {
               ivjJPanel7 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel7.setLayout(null) ;  // JVE Generated
               ivjJPanel7.add(getIvjJPanel6(), getIvjJPanel6().getName()) ;  // JVE Generated
               ivjJPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Page 3 of 3 - Button Properties" ,0,0)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel7;
}
/**
 * This method initializes ivjJButton_1
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton_1() {
     if(ivjJButton_1 == null) {
          try {
               ivjJButton_1 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton_1.setText("Set Text Color...") ;  // JVE Generated
               ivjJButton_1.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           java.awt.Color col = getIvjJLabel_1().getForeground();
                           col = JColorChooser.showDialog( getIvjJPanel(), "Text Color", col );
                           getIvjJLabel_1().setForeground( col );
                           getIvjJLabel().setForeground( col );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton_1;
}
/**
 * This method initializes ivjJCheckBox_1
 * 
 * @return javax.swing.JCheckBox
 */
private javax.swing.JCheckBox getIvjJCheckBox_1() {
     if(ivjJCheckBox_1 == null) {
          try {
               ivjJCheckBox_1 = new javax.swing.JCheckBox() ;  // Explicit Instance
               ivjJCheckBox_1.setText("Bold") ;  // JVE Generated
               ivjJCheckBox_1.setSelected( true );
               ivjJCheckBox_1.addItemListener(
                   new java.awt.event.ItemListener() {
                       public void itemStateChanged(java.awt.event.ItemEvent e) {
                       	   java.awt.Font currentFont = getIvjJLabel_1().getFont();
                       	   int style = currentFont.getStyle();
                           if( getIvjJCheckBox_1().isSelected() ) {
                               style += java.awt.Font.BOLD;
                           }
                           else {
                           	   style -= java.awt.Font.BOLD;
                           }
                           currentFont = new java.awt.Font(
                                                  currentFont.getName(),
                                                  style,
                                                  currentFont.getSize() );
                           getIvjJLabel_1().setFont( currentFont );
                           getIvjJLabel().setFont( currentFont );                               
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJCheckBox_1;
}
/**
 * This method initializes ivjJCheckBox_2
 * 
 * @return javax.swing.JCheckBox
 */
private javax.swing.JCheckBox getIvjJCheckBox_2() {
     if(ivjJCheckBox_2 == null) {
          try {
               ivjJCheckBox_2 = new javax.swing.JCheckBox() ;  // Explicit Instance
               ivjJCheckBox_2.setText("Italics") ;  // JVE Generated
               ivjJCheckBox_2.addItemListener(
                   new java.awt.event.ItemListener() {
                       public void itemStateChanged(java.awt.event.ItemEvent e) {
                       	   java.awt.Font currentFont = getIvjJLabel_1().getFont();
                       	   int style = currentFont.getStyle();
                           if( getIvjJCheckBox_2().isSelected() ) {
                           	   style += java.awt.Font.ITALIC;
                           }
                           else {
                           	   style -= java.awt.Font.ITALIC;
                           }
                           currentFont = new java.awt.Font(
                                                  currentFont.getName(),
                                                  style,
                                                  currentFont.getSize() );
                           getIvjJLabel_1().setFont( currentFont );
                           getIvjJLabel().setFont( currentFont );                               
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJCheckBox_2;
}
/**
 * This method initializes ivjJLabel_3
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel_3() {
     if(ivjJLabel_3 == null) {
          try {
               ivjJLabel_3 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel_3.setText("Size") ;  // JVE Generated
               ivjJLabel_3.setForeground( java.awt.Color.black );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel_3;
}
/**
 * This method initializes ivjJTextField_1
 * 
 * @return javax.swing.JTextField
 */
private javax.swing.JTextField getIvjJTextField_1() {
     if(ivjJTextField_1 == null) {
          try {
               ivjJTextField_1 = new javax.swing.JTextField() ;  // Explicit Instance
               ivjJTextField_1.setColumns(2) ;  // JVE Generated
               ivjJTextField_1.setText("12") ;  // JVE Generated
               ivjJTextField_1.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           try {
                           	   int newSize = Integer.parseInt( getIvjJTextField_1().getText() );
                               java.awt.Font currentFont = getIvjJLabel_1().getFont();
                          	   currentFont = new java.awt.Font(
                                                  currentFont.getName(),
                                                  currentFont.getStyle(),
                                                  newSize );
                               getIvjJLabel_1().setFont( currentFont );
                               getIvjJLabel().setFont( currentFont );
                           }
                           catch( Exception e2 ) {
                           }
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTextField_1;
}
/**
 * This method initializes ivjJPanel_3
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel_3() {
     if(ivjJPanel_3 == null) {
          try {
               java.awt.FlowLayout layFlowLayout_7 = new java.awt.FlowLayout() ;  // Explicit Instance
               layFlowLayout_7.setHgap(12) ;  // JVE Generated
               ivjJPanel_3 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel_3.setLayout(layFlowLayout_7) ;  // JVE Generated
               ivjJPanel_3.add(getIvjJButton_1(), getIvjJButton_1().getName()) ;  // JVE Generated
               ivjJPanel_3.add(getIvjJCheckBox_1(), getIvjJCheckBox_1().getName()) ;  // JVE Generated
               ivjJPanel_3.add(getIvjJCheckBox_2(), getIvjJCheckBox_2().getName()) ;  // JVE Generated
               ivjJPanel_3.add(getIvjJLabel_3(), getIvjJLabel_3().getName()) ;  // JVE Generated
               ivjJPanel_3.add(getIvjJTextField_1(), getIvjJTextField_1().getName()) ;  // JVE Generated
               ivjJPanel_3.setBounds(128, 24, 164, 118) ;  // JVE Generated
               ivjJPanel_3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel_3;
}
/**
 * This method initializes ivjJPanel6
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel6() {
     if(ivjJPanel6 == null) {
          try {
               ivjJPanel6 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel6.setLayout(null) ;  // JVE Generated
               ivjJPanel6.add(getIvjJLabel_4(), getIvjJLabel_4().getName()) ;  // JVE Generated
               ivjJPanel6.add(getIvjJTextField(), getIvjJTextField().getName()) ;  // JVE Generated
               ivjJPanel6.add(getIvjJCheckBox(), getIvjJCheckBox().getName()) ;  // JVE Generated
               ivjJPanel6.add(getIvjJLabel2(), getIvjJLabel2().getName()) ;  // JVE Generated
               ivjJPanel6.add(getIvjJTextField2(), getIvjJTextField2().getName()) ;  // JVE Generated
               ivjJPanel6.add(getIvjJCheckBox2(), getIvjJCheckBox2().getName()) ;  // JVE Generated
               ivjJPanel6.setBounds(108, 23, 198, 119) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel6;
}
/**
 * This method initializes ivjJLabel_4
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel_4() {
     if(ivjJLabel_4 == null) {
          try {
               ivjJLabel_4 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel_4.setBounds(10, 12, 61, 15) ;  // JVE Generated
               ivjJLabel_4.setText("Button 1:") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel_4;
}
/**
 * This method initializes ivjJTextField
 * 
 * @return javax.swing.JTextField
 */
private javax.swing.JTextField getIvjJTextField() {
     if(ivjJTextField == null) {
          try {
               ivjJTextField = new javax.swing.JTextField() ;  // Explicit Instance
               ivjJTextField.setBounds(79, 12, 106, 19) ;  // JVE Generated
               ivjJTextField.setText("<== Previous") ;  // JVE Generated
               ivjJTextField.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           getIvjJButton().setText( getIvjJTextField().getText() );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTextField;
}
/**
 * This method initializes ivjJCheckBox
 * 
 * @return javax.swing.JCheckBox
 */
private javax.swing.JCheckBox getIvjJCheckBox() {
     if(ivjJCheckBox == null) {
          try {
               ivjJCheckBox = new javax.swing.JCheckBox() ;  // Explicit Instance
               ivjJCheckBox.setBounds(79, 37, 106, 19) ;  // JVE Generated
               ivjJCheckBox.setText("enabled") ;  // JVE Generated
               ivjJCheckBox.setSelected(true) ;  // JVE Generated
               ivjJCheckBox.addItemListener(
                   new java.awt.event.ItemListener() {
                       public void itemStateChanged(java.awt.event.ItemEvent e) {
                       	   getIvjJButton().setEnabled( getIvjJCheckBox().isSelected() );                       	                              
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJCheckBox;
}
/**
 * This method initializes ivjJLabel2
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel2() {
     if(ivjJLabel2 == null) {
          try {
               ivjJLabel2 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel2.setBounds(10, 73, 61, 15) ;  // JVE Generated
               ivjJLabel2.setText("Button 2:") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel2;
}
/**
 * This method initializes ivjJTextField2
 * 
 * @return javax.swing.JTextField
 */
private javax.swing.JTextField getIvjJTextField2() {
     if(ivjJTextField2 == null) {
          try {
               ivjJTextField2 = new javax.swing.JTextField() ;  // Explicit Instance
               ivjJTextField2.setBounds(79, 73, 106, 19) ;  // JVE Generated
               ivjJTextField2.setText("Next ==>") ;  // JVE Generated
               ivjJTextField2.addActionListener(
                   new ActionListener() {
                       public void actionPerformed(java.awt.event.ActionEvent e) {
                           getIvjJButton2().setText( getIvjJTextField2().getText() );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTextField2;
}
/**
 * This method initializes ivjJCheckBox2
 * 
 * @return javax.swing.JCheckBox
 */
private javax.swing.JCheckBox getIvjJCheckBox2() {
     if(ivjJCheckBox2 == null) {
          try {
               ivjJCheckBox2 = new javax.swing.JCheckBox() ;  // Explicit Instance
               ivjJCheckBox2.setBounds(79, 98, 106, 19) ;  // JVE Generated
               ivjJCheckBox2.setText("enabled") ;  // JVE Generated
               ivjJCheckBox2.setSelected(true) ;  // JVE Generated
               ivjJCheckBox2.addItemListener(
                   new java.awt.event.ItemListener() {
                       public void itemStateChanged(java.awt.event.ItemEvent e) {
                       	   getIvjJButton2().setEnabled( getIvjJCheckBox2().isSelected() );
                       }                   	
                   }
               );
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJCheckBox2;
}
/**
 * This method initializes ivjJPanel8
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel8() {
     if(ivjJPanel8 == null) {
          try {
               java.awt.FlowLayout layFlowLayout_8 = new java.awt.FlowLayout() ;  // Explicit Instance
               ivjJPanel8 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel8.setLayout(layFlowLayout_8) ;  // JVE Generated
               ivjJPanel8.add(getIvjJLabel3(), getIvjJLabel3().getName()) ;  // JVE Generated
               ivjJPanel8.add(getIvjJLabel4(), getIvjJLabel4().getName()) ;  // JVE Generated
               ivjJPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Page 1 of 3 - Instructions" ,0,0)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel8;
}
/**
 * This method initializes ivjJLabel3
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel3() {
     if(ivjJLabel3 == null) {
          try {
               ivjJLabel3 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel3.setText("Flip through the pages to adjust the properties of the text") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel3;
}
/**
 * This method initializes ivjJLabel4
 * 
 * @return javax.swing.JLabel
 */
private javax.swing.JLabel getIvjJLabel4() {
     if(ivjJLabel4 == null) {
          try {
               ivjJLabel4 = new javax.swing.JLabel() ;  // Explicit Instance
               ivjJLabel4.setText("above and the buttons below.") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel4;
}

protected java.awt.CardLayout getCardLayout() {
    return( ( java.awt.CardLayout ) getIvjJPanel4().getLayout() );
}

public static void main( String[] args ) {
    javax.swing.JFrame frame = new CardLayoutExample();
    frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
    frame.setVisible( true );
}
}
