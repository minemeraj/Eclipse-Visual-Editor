
public class CardLayout1 {

     private final static String PanelCard1 = "JPanel1018613402901" ;

     private javax.swing.JFrame ivjJFrame = null ;  //  @jve:visual-info  declIndex=1 visual-constraint="27,21"
     private javax.swing.JPanel ivjJPanel1 = null ;
     private javax.swing.JPanel ivjJPanel = null ;
     private javax.swing.JSplitPane ivjJSplitPane_1 = null ;
     private javax.swing.JDesktopPane ivjJDesktopPane = null ;
     private javax.swing.JPanel ivjJPanel3 = null ;
     private javax.swing.JInternalFrame ivjJInternalFrame = null ;
     private javax.swing.JTabbedPane ivjJTabbedPane = null ;
     private javax.swing.JPanel ivjJPanel4 = null ;
     private javax.swing.JPanel ivjJPanel5 = null ;
     private javax.swing.JLabel ivjJLabel = null ;
     private javax.swing.JLabel ivjJLabel2 = null ;
     private javax.swing.JPanel ivjJPanel6 = null ;
     private javax.swing.JTree ivjJTree_1 = null ;
     private javax.swing.JComboBox ivjJComboBox_1 = null ;
     private javax.swing.JScrollBar ivjJScrollBar_1 = null ;
     private javax.swing.JSlider ivjJSlider_1 = null ;
     private javax.swing.JTextArea ivjJTextArea_1 = null ;
     private javax.swing.JPasswordField ivjJPasswordField_1 = null ;
     private javax.swing.JLabel ivjJLabel_3 = null ;
     private javax.swing.JToggleButton ivjJToggleButton_1 = null ;
     private javax.swing.JRadioButton ivjJRadioButton_3 = null ;
     private javax.swing.JCheckBox ivjJCheckBox_1 = null ;
     private javax.swing.JToolBar ivjJToolBar = null ;
     private javax.swing.JButton ivjJButton = null ;
     private javax.swing.JButton ivjJButton2 = null ;
     private javax.swing.JButton ivjJButton3 = null ;
     private javax.swing.JPanel ivjJPanel2 = null ;
     private javax.swing.JButton ivjJButton_4 = null ;
     private javax.swing.JTable ivjJTable = null ;
     private javax.swing.table.TableColumn ivjTableColumn = null ;
     private javax.swing.table.TableColumn ivjTableColumn2 = null ;
     private javax.swing.JProgressBar ivjJProgressBar = null ;
     private javax.swing.JScrollPane ivjJScrollPane = null ;
     private javax.swing.JScrollPane ivjJScrollPane2 = null ;
     private javax.swing.JList ivjJList = null ;
     private javax.swing.JTable ivjJTable2 = null ;
     private javax.swing.table.TableColumn ivjTableColumn3 = null ;
     private javax.swing.table.TableColumn ivjTableColumn4 = null ;
     private javax.swing.JPanel ivjJPanel7 = null ;
     private javax.swing.JInternalFrame ivjJInternalFrame2 = null ;
     private javax.swing.JButton ivjJButton4 = null ;
     private javax.swing.JButton ivjJButton5 = null ;
     private javax.swing.JPanel ivjJPanel8 = null ;
     private javax.swing.JButton ivjJButton6 = null ;
     private javax.swing.JButton ivjJButton7 = null ;
     private javax.swing.JButton ivjJButton8 = null ;
/**
 * This method initializes ivjJFrame
 * 
 * @return javax.swing.JFrame
 */
public CardLayout1 () {
	getIvjJFrame().setVisible(true);
}
/**
 * This method initializes ivjJFrame
 * 
 * @return javax.swing.JFrame
 */
private javax.swing.JFrame getIvjJFrame() {
     if(ivjJFrame == null) {
          try {
               ivjJFrame = new javax.swing.JFrame() ;  // Explicit Instance
               ivjJFrame.setContentPane(getIvjJPanel()) ;  // JVE Generated
               ivjJFrame.setSize(391, 391) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJFrame;
}
/**
 * This method initializes ivjJPanel
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel() {
     if(ivjJPanel1 == null) {
          try {
               java.awt.BorderLayout layBorderLayout_1 = new java.awt.BorderLayout() ;  // Explicit Instance
               ivjJPanel1 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel1.setLayout(layBorderLayout_1) ;  // JVE Generated
               ivjJPanel1.add(getIvjJPanel1(), "Center") ;  // JVE Generated
               ivjJPanel1.add(getIvjJPanel8(), "South") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel1;
}
/**
 * This method initializes ivjJPanel
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel1() {
     if(ivjJPanel == null) {
          try {
               java.awt.CardLayout layCardLayout_4 = new java.awt.CardLayout() ;  // Explicit Instance
               ivjJPanel = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel.setLayout(layCardLayout_4) ;  // JVE Generated
               ivjJPanel.add(getIvjJPanel6(), "card1") ;  // JVE Generated
               ivjJPanel.add(getIvjJSplitPane_1(), "card2") ;  // JVE Generated
               ivjJPanel.add(getIvjJDesktopPane(), "card3") ;  // JVE Generated
               ivjJPanel.add(getIvjJTabbedPane(), "card4") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel;
}
/**
 * This method initializes ivjJSplitPane_1
 * 
 * @return javax.swing.JSplitPane
 */
private javax.swing.JSplitPane getIvjJSplitPane_1() {
     if(ivjJSplitPane_1 == null) {
          try {
               ivjJSplitPane_1 = new javax.swing.JSplitPane() ;  // Explicit Instance
               ivjJSplitPane_1.setLeftComponent(null) ;  // JVE Generated
               ivjJSplitPane_1.setRightComponent(null) ;  // JVE Generated
               ivjJSplitPane_1.setLeftComponent(getIvjJScrollPane()) ;  // JVE Generated
               ivjJSplitPane_1.setRightComponent(getIvjJScrollPane2()) ;  // JVE Generated
               ivjJSplitPane_1.setDividerLocation(100) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJSplitPane_1;
}
/**
 * This method initializes ivjJDesktopPane
 * 
 * @return javax.swing.JDesktopPane
 */
private javax.swing.JDesktopPane getIvjJDesktopPane() {
     if(ivjJDesktopPane == null) {
          try {
               ivjJDesktopPane = new javax.swing.JDesktopPane() ;  // Explicit Instance
               ivjJDesktopPane.add(getIvjJInternalFrame(), getIvjJInternalFrame().getName()) ;  // JVE Generated
               ivjJDesktopPane.add(getIvjJInternalFrame2(), getIvjJInternalFrame2().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJDesktopPane;
}
/**
 * This method initializes ivjJInternalFrame
 * 
 * @return javax.swing.JInternalFrame
 */
private javax.swing.JInternalFrame getIvjJInternalFrame() {
     if(ivjJInternalFrame == null) {
          try {
               ivjJInternalFrame = new javax.swing.JInternalFrame() ;  // Explicit Instance
               ivjJInternalFrame.setContentPane(getIvjJPanel3()) ;  // JVE Generated
               ivjJInternalFrame.setBounds(22, 21, 136, 105) ;  // JVE Generated
               ivjJInternalFrame.setVisible(true) ;  // JVE Generated
               ivjJInternalFrame.setClosable(true) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJInternalFrame;
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
               ivjJPanel3.add(getIvjJButton5(), getIvjJButton5().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel3;
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
               ivjJTabbedPane.addTab("page 1", null, getIvjJPanel4(), null) ;  // JVE Generated
               ivjJTabbedPane.addTab("page 2", null, getIvjJPanel5(), null) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTabbedPane;
}
/**
 * This method initializes ivjJPanel4
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel4() {
     if(ivjJPanel4 == null) {
          try {
               ivjJPanel4 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel4.add(getIvjJLabel(), getIvjJLabel().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel4;
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
               ivjJPanel5.add(getIvjJLabel2(), getIvjJLabel2().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel5;
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
               ivjJLabel.setText("This is the panel for page 1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel;
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
               ivjJLabel2.setText("This is the panel for page 2") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel2;
}
/**
 * This method initializes ivjJPanel6
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel6() {
     if(ivjJPanel6 == null) {
          try {
               java.awt.BorderLayout layBorderLayout_5 = new java.awt.BorderLayout() ;  // Explicit Instance
               ivjJPanel6 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel6.setLayout(layBorderLayout_5) ;  // JVE Generated
               ivjJPanel6.add(getIvjJToolBar(), "North") ;  // JVE Generated
               ivjJPanel6.add(getIvjJPanel2(), "Center") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel6;
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
               ivjJCheckBox_1.setText("chek 1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJCheckBox_1;
}
/**
 * This method initializes ivjJRadioButton_3
 * 
 * @return javax.swing.JRadioButton
 */
private javax.swing.JRadioButton getIvjJRadioButton_3() {
     if(ivjJRadioButton_3 == null) {
          try {
               ivjJRadioButton_3 = new javax.swing.JRadioButton() ;  // Explicit Instance
               ivjJRadioButton_3.setText("Radio1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJRadioButton_3;
}
/**
 * This method initializes ivjJToggleButton_1
 * 
 * @return javax.swing.JToggleButton
 */
private javax.swing.JToggleButton getIvjJToggleButton_1() {
     if(ivjJToggleButton_1 == null) {
          try {
               ivjJToggleButton_1 = new javax.swing.JToggleButton() ;  // Explicit Instance
               ivjJToggleButton_1.setText("JToggle1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJToggleButton_1;
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
               ivjJLabel_3.setText("JLabel1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJLabel_3;
}
/**
 * This method initializes ivjJPasswordField_1
 * 
 * @return javax.swing.JPasswordField
 */
private javax.swing.JPasswordField getIvjJPasswordField_1() {
     if(ivjJPasswordField_1 == null) {
          try {
               ivjJPasswordField_1 = new javax.swing.JPasswordField() ;  // Explicit Instance
               ivjJPasswordField_1.setText("JPassWordField1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPasswordField_1;
}
/**
 * This method initializes ivjJTextArea_1
 * 
 * @return javax.swing.JTextArea
 */
private javax.swing.JTextArea getIvjJTextArea_1() {
     if(ivjJTextArea_1 == null) {
          try {
               ivjJTextArea_1 = new javax.swing.JTextArea() ;  // Explicit Instance
               ivjJTextArea_1.setText("JTextArea1") ;  // JVE Generated
               ivjJTextArea_1.setRows(2) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTextArea_1;
}
/**
 * This method initializes ivjJSlider_1
 * 
 * @return javax.swing.JSlider
 */
private javax.swing.JSlider getIvjJSlider_1() {
     if(ivjJSlider_1 == null) {
          try {
               ivjJSlider_1 = new javax.swing.JSlider() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJSlider_1;
}
/**
 * This method initializes ivjJScrollBar_1
 * 
 * @return javax.swing.JScrollBar
 */
private javax.swing.JScrollBar getIvjJScrollBar_1() {
     if(ivjJScrollBar_1 == null) {
          try {
               ivjJScrollBar_1 = new javax.swing.JScrollBar() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJScrollBar_1;
}
/**
 * This method initializes ivjJComboBox_1
 * 
 * @return javax.swing.JComboBox
 */
private javax.swing.JComboBox getIvjJComboBox_1() {
     if(ivjJComboBox_1 == null) {
          try {
               ivjJComboBox_1 = new javax.swing.JComboBox() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJComboBox_1;
}
/**
 * This method initializes ivjJTree_1
 * 
 * @return javax.swing.JTree
 */
private javax.swing.JTree getIvjJTree_1() {
     if(ivjJTree_1 == null) {
          try {
               ivjJTree_1 = new javax.swing.JTree() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTree_1;
}
/**
 * This method initializes ivjJToolBar
 * 
 * @return javax.swing.JToolBar
 */
private javax.swing.JToolBar getIvjJToolBar() {
     if(ivjJToolBar == null) {
          try {
               ivjJToolBar = new javax.swing.JToolBar() ;  // Explicit Instance
               ivjJToolBar.add(getIvjJButton(), getIvjJButton().getName()) ;  // JVE Generated
               ivjJToolBar.add(getIvjJButton2(), getIvjJButton2().getName()) ;  // JVE Generated
               ivjJToolBar.add(getIvjJButton3(), getIvjJButton3().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJToolBar;
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
               ivjJButton.setText("copy") ;  // JVE Generated
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
               ivjJButton2.setText("cut") ;  // JVE Generated
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
               ivjJButton3.setText("paste") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton3;
}
/**
 * This method initializes ivjJPanel2
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel2() {
     if(ivjJPanel2 == null) {
          try {
               ivjJPanel2 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel2.add(getIvjJButton_4(), getIvjJButton_4().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJCheckBox_1(), getIvjJCheckBox_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJRadioButton_3(), getIvjJRadioButton_3().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJToggleButton_1(), getIvjJToggleButton_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJLabel_3(), getIvjJLabel_3().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJPasswordField_1(), getIvjJPasswordField_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJTextArea_1(), getIvjJTextArea_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJSlider_1(), getIvjJSlider_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJScrollBar_1(), getIvjJScrollBar_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJComboBox_1(), getIvjJComboBox_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJTree_1(), getIvjJTree_1().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJTable(), getIvjJTable().getName()) ;  // JVE Generated
               ivjJPanel2.add(getIvjJProgressBar(), getIvjJProgressBar().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel2;
}
/**
 * This method initializes ivjJButton_4
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton_4() {
     if(ivjJButton_4 == null) {
          try {
               ivjJButton_4 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton_4.setText("ok 1") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton_4;
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
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjTableColumn2;
}
/**
 * This method initializes ivjJProgressBar
 * 
 * @return javax.swing.JProgressBar
 */
private javax.swing.JProgressBar getIvjJProgressBar() {
     if(ivjJProgressBar == null) {
          try {
               ivjJProgressBar = new javax.swing.JProgressBar() ;  // Explicit Instance
               ivjJProgressBar.setValue(25) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJProgressBar;
}
/**
 * This method initializes ivjJScrollPane
 * 
 * @return javax.swing.JScrollPane
 */
private javax.swing.JScrollPane getIvjJScrollPane() {
     if(ivjJScrollPane == null) {
          try {
               ivjJScrollPane = new javax.swing.JScrollPane() ;  // Explicit Instance
               ivjJScrollPane.setViewportView(getIvjJList()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJScrollPane;
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
               ivjJScrollPane2.setViewportView(getIvjJTable2()) ;  // JVE Generated
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
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJList;
}
/**
 * This method initializes ivjJTable2
 * 
 * @return javax.swing.JTable
 */
private javax.swing.JTable getIvjJTable2() {
     if(ivjJTable2 == null) {
          try {
               ivjJTable2 = new javax.swing.JTable() ;  // Explicit Instance
               ivjJTable2.addColumn(getIvjTableColumn3()) ;  // JVE Generated
               ivjJTable2.addColumn(getIvjTableColumn4()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJTable2;
}
/**
 * This method initializes ivjTableColumn3
 * 
 * @return javax.swing.table.TableColumn
 */
private javax.swing.table.TableColumn getIvjTableColumn3() {
     if(ivjTableColumn3 == null) {
          try {
               ivjTableColumn3 = new javax.swing.table.TableColumn() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjTableColumn3;
}

/**
 * This method initializes ivjTableColumn4
 * 
 * @return javax.swing.table.TableColumn
 */
private javax.swing.table.TableColumn getIvjTableColumn4() {
     if(ivjTableColumn4 == null) {
          try {
               ivjTableColumn4 = new javax.swing.table.TableColumn() ;  // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjTableColumn4;
}

/**
 * This method initializes ivjJInternalFrame2
 * 
 * @return javax.swing.JInternalFrame
 */
private javax.swing.JInternalFrame getIvjJInternalFrame2() {
     if(ivjJInternalFrame2 == null) {
          try {
               ivjJInternalFrame2 = new javax.swing.JInternalFrame() ;  // Explicit Instance
               ivjJInternalFrame2.setContentPane(getIvjJPanel7()) ;  // JVE Generated
               ivjJInternalFrame2.setBounds(162, 127, 174, 154) ;  // JVE Generated
               ivjJInternalFrame2.setVisible(true) ;  // JVE Generated
               ivjJInternalFrame2.setClosable(true) ;  // JVE Generated
               ivjJInternalFrame2.setMaximizable(true) ;  // JVE Generated
               ivjJInternalFrame2.setIconifiable(true) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJInternalFrame2;
}
/**
 * This method initializes ivjJPanel7
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel7() {
     if(ivjJPanel7 == null) {
          try {
               java.awt.FlowLayout layFlowLayout_6 = new java.awt.FlowLayout() ;  // Explicit Instance
               ivjJPanel7 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel7.setLayout(layFlowLayout_6) ;  // JVE Generated
               ivjJPanel7.add(getIvjJButton4(), getIvjJButton4().getName()) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel7;
}
/**
 * This method initializes ivjJButton4
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton4() {
     if(ivjJButton4 == null) {
          try {
               ivjJButton4 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton4.setText("exit") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton4;
}
/**
 * This method initializes ivjJButton5
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton5() {
     if(ivjJButton5 == null) {
          try {
               ivjJButton5 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton5.setText("apply") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton5;
}
/**
 * This method initializes ivjJPanel8
 * 
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getIvjJPanel8() {
     if(ivjJPanel8 == null) {
          try {
               ivjJPanel8 = new javax.swing.JPanel() ;  // Explicit Instance
               ivjJPanel8.add(getIvjJButton6(), getIvjJButton6().getName()) ;  // JVE Generated
               ivjJPanel8.add(getIvjJButton7(), getIvjJButton7().getName()) ;  // JVE Generated
               ivjJPanel8.add(getIvjJButton8(), getIvjJButton8().getName()) ;  // JVE Generated
               ivjJPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)) ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJPanel8;
}
/**
 * This method initializes ivjJButton6
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton6() {
     if(ivjJButton6 == null) {
          try {
               ivjJButton6 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton6.setText("Next") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton6;
}
/**
 * This method initializes ivjJButton7
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton7() {
     if(ivjJButton7 == null) {
          try {
               ivjJButton7 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton7.setText("Previous") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton7;
}
/**
 * This method initializes ivjJButton8
 * 
 * @return javax.swing.JButton
 */
private javax.swing.JButton getIvjJButton8() {
     if(ivjJButton8 == null) {
          try {
               ivjJButton8 = new javax.swing.JButton() ;  // Explicit Instance
               ivjJButton8.setText("Cancel") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjJButton8;
}
}