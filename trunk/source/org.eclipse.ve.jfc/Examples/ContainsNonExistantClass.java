/**
 * This has a button as well as a non-existant class
 */
public class ContainsNonExistantClass {

     private java.awt.Button ivjButton = null;  //  @jve:visual-info  decl-index=0 visual-constraint="60,29"
     private Frog ivjString = null;  //  @jve:visual-info  decl-index=0 visual-constraint="251,39"
	/**
	 * This method initializes ivjButton
	 * 
	 * @return java.awt.Button
	 */
	private java.awt.Button getIvjButton() {
		if(ivjButton == null) {
			ivjButton = new java.awt.Button();
			ivjButton.setSize(76, 52);
			ivjButton.setLabel("Frog");
		}
		return ivjButton;
	}
	/**
	 * This method initializes ivjString
	 * 
	 * @return java.lang.String
	 */
	private Frog getIvjString() {
		if(ivjString == null) {
			ivjString = new Frog();
		}
		return ivjString;
	}
}

