import java.applet.Applet;

/**
 * This applet can be used to test the Java Bean launcher and the JVE
 * It shows the values of the parameters "One" and "Two" so you can edit a
 * JavaBean launcher configuration and enter these on the Applet parameters tab
 */
public class MyApplet extends Applet {

     private java.awt.Label ivjLabel = null ;
     private java.awt.List ivjList = null ;
/**
 * This method initializes 
 * 
 */
public MyApplet() {
     super() ;
     init() ;
}
/**
 * This method initializes this
 * 
 * @return void
 */
public void init() {
     try {
         this.add(getIvjLabel(), getIvjLabel().getName()) ;  // JVE Generated
         this.add(getIvjList(), getIvjList().getName()) ;  // JVE Generated
         this.setSize(142, 110) ;  // JVE Generated
         this.setBackground(java.awt.Color.blue) ;  // JVE Generated
         // Set the parameters
         getIvjList().add("One = " + getParameter("One"));
         getIvjList().add("Two = " + getParameter("Two"));               
     }
     catch (java.lang.Throwable e) {
         //  Do Something
     }
}
/**
 * This method initializes ivjLabel
 * 
 * @return java.awt.Label
 */
private java.awt.Label getIvjLabel() {
     if(ivjLabel == null) {
          try {
               ivjLabel = new java.awt.Label() ; // Explicit Instance
               ivjLabel.setText("This is an Applet") ;  // JVE Generated
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjLabel;
}
/**
 * This method initializes ivjList
 * 
 * @return java.awt.List
 */
private java.awt.List getIvjList() {
     if(ivjList == null) {
          try {
               ivjList = new java.awt.List() ; // Explicit Instance
          }
          catch (java.lang.Throwable e) {
              //  Do Something
          }
     }
     return ivjList;
}
}
