/**
 * Example of components laid out using GridLayout
 */
public class GridLayoutExample extends java.awt.Frame {
	private java.awt.Button ivjButton1 = null;
	private java.awt.Button ivjButton10 = null;
	private java.awt.Button ivjButton11 = null;
	private java.awt.Button ivjButton12 = null;
	private java.awt.Button ivjButton2 = null;
	private java.awt.Button ivjButton3 = null;
	private java.awt.Button ivjButton4 = null;
	private java.awt.Button ivjButton5 = null;
	private java.awt.Button ivjButton6 = null;
	private java.awt.Button ivjButton7 = null;
	private java.awt.Button ivjButton8 = null;
	private java.awt.Button ivjButton9 = null;
	private java.awt.GridLayout ivjFrame1GridLayout = null;
public GridLayoutExample() {
	super();
	initialize();
}
/**
 * Return the Button1 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton1() {
	if (ivjButton1 == null) {
		ivjButton1 = new java.awt.Button();
		ivjButton1.setName("Button1");
		ivjButton1.setBackground(java.awt.Color.darkGray);
		ivjButton1.setForeground(java.awt.Color.white);
		ivjButton1.setLabel("Button1");
	}
	return ivjButton1;
}
/**
 * Return the Button10 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton10() {
	if (ivjButton10 == null) {
		ivjButton10 = new java.awt.Button();
		ivjButton10.setName("Button10");
		ivjButton10.setLabel("Button10");
	}
	return ivjButton10;
}
/**
 * Return the Button11 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton11() {
	if (ivjButton11 == null) {
		ivjButton11 = new java.awt.Button();
		ivjButton11.setName("Button11");
		ivjButton11.setLabel("Button11");
	}
	return ivjButton11;
}
/**
 * Return the Button12 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton12() {
	if (ivjButton12 == null) {
		ivjButton12 = new java.awt.Button();
		ivjButton12.setName("Button12");
		ivjButton12.setLabel("Button12");
	}
	return ivjButton12;
}
/**
 * Return the Button2 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton2() {
	if (ivjButton2 == null) {
		ivjButton2 = new java.awt.Button();
		ivjButton2.setName("Button2");
		ivjButton2.setLabel("Button2");
	}
	return ivjButton2;
}
/**
 * Return the Button3 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton3() {
	if (ivjButton3 == null) {
		ivjButton3 = new java.awt.Button();
		ivjButton3.setName("Button3");
		ivjButton3.setLabel("Button4");
	}
	return ivjButton3;
}
/**
 * Return the Button4 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton4() {
	if (ivjButton4 == null) {
		ivjButton4 = new java.awt.Button();
		ivjButton4.setName("Button4");
		ivjButton4.setLabel("Button3");
	}
	return ivjButton4;
}
/**
 * Return the Button5 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton5() {
	if (ivjButton5 == null) {
		ivjButton5 = new java.awt.Button();
		ivjButton5.setName("Button5");
		ivjButton5.setLabel("Button5");
	}
	return ivjButton5;
}
/**
 * Return the Button6 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton6() {
	if (ivjButton6 == null) {
		ivjButton6 = new java.awt.Button();
		ivjButton6.setName("Button6");
		ivjButton6.setLabel("Button7");
	}
	return ivjButton6;
}
/**
 * Return the Button7 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton7() {
	if (ivjButton7 == null) {
		ivjButton7 = new java.awt.Button();
		ivjButton7.setName("Button7");
		ivjButton7.setLabel("Button6");
	}
	return ivjButton7;
}
/**
 * Return the Button8 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton8() {
	if (ivjButton8 == null) {
		ivjButton8 = new java.awt.Button();
		ivjButton8.setName("Button8");
		ivjButton8.setLabel("Button8");
	}
	return ivjButton8;
}
/**
 * Return the Button9 property value.
 * @return java.awt.Button
 */
private java.awt.Button getButton9() {
	if (ivjButton9 == null) {
		ivjButton9 = new java.awt.Button();
		ivjButton9.setName("Button9");
		ivjButton9.setLabel("Button9");
	}
	return ivjButton9;
}
/**
 * Return the Frame1GridLayout property value.
 * @return java.awt.GridLayout
 */
private java.awt.GridLayout getFrame1GridLayout() {
	ivjFrame1GridLayout = new java.awt.GridLayout();
	ivjFrame1GridLayout.setRows(4);
	ivjFrame1GridLayout.setVgap(8);
	ivjFrame1GridLayout.setHgap(5);
	return ivjFrame1GridLayout;
}
/**
 * Initialize the class.
 */

private void initialize() {

	this.setName("Frame1");
	this.setLayout(getFrame1GridLayout());
	this.setBounds(33, 62, 304, 246);
	this.add(getButton1(), getButton1().getName());
	this.add(getButton2(), getButton2().getName());
	this.add(getButton4(), getButton4().getName());
	this.add(getButton3(), getButton3().getName());
	this.add(getButton5(), getButton5().getName());
	this.add(getButton7(), getButton7().getName());
	this.add(getButton6(), getButton6().getName());
	this.add(getButton8(), getButton8().getName());
	this.add(getButton9(), getButton9().getName());
	this.add(getButton10(), getButton10().getName());
	this.add(getButton11(), getButton11().getName());
	this.add(getButton12(), getButton12().getName());	
}
}
