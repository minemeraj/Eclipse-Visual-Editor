/**
 * Example AWT Frame with some common components on it
 */
public class AwtComponents extends java.awt.Frame {
	private java.awt.Button ivjButton1 = null;
	private java.awt.Checkbox ivjCheckbox1 = null;
	private java.awt.Choice ivjChoice1 = null;
	private java.awt.Label ivjLabel1 = null;
	private java.awt.List ivjList1 = null;
	private java.awt.Scrollbar ivjScrollbar1 = null;
	private java.awt.TextArea ivjTextArea1 = null;
	private java.awt.TextField ivjTextField1 = null;
public AwtComponents() {
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
		ivjButton1.setLabel("Button");
	}
	return ivjButton1;
}
/**
 * Return the Checkbox1 property value.
 * @return java.awt.Checkbox
 */
private java.awt.Checkbox getCheckbox1() {
	if (ivjCheckbox1 == null) {
		ivjCheckbox1 = new java.awt.Checkbox();
		ivjCheckbox1.setName("Checkbox1");
		ivjCheckbox1.setLabel("Checkbox");
	}
	return ivjCheckbox1;
}
/**
 * Return the Choice1 property value.
 * @return java.awt.Choice
 */
private java.awt.Choice getChoice1() {
	if (ivjChoice1 == null) {
		ivjChoice1 = new java.awt.Choice();
		ivjChoice1.setName("Choice1");
	}
	return ivjChoice1;
}
/**
 * Return the Label1 property value.
 * @return java.awt.Label
 */

private java.awt.Label getLabel1() {
	if (ivjLabel1 == null) {
		ivjLabel1 = new java.awt.Label();
		ivjLabel1.setName("Label1");
		ivjLabel1.setText("Label");
	}
	return ivjLabel1;
}
/**
 * Return the List1 property value.
 * @return java.awt.List
 */

private java.awt.List getList1() {
	if (ivjList1 == null) {
		ivjList1 = new java.awt.List();
		ivjList1.setName("List1");
	}
	return ivjList1;
}
/**
 * Return the Scrollbar1 property value.
 * @return java.awt.Scrollbar
 */
private java.awt.Scrollbar getScrollbar1() {
	if (ivjScrollbar1 == null) {
		ivjScrollbar1 = new java.awt.Scrollbar();
		ivjScrollbar1.setName("Scrollbar1");
	}
	return ivjScrollbar1;
}
/**
 * Return the TextArea1 property value.
 * @return java.awt.TextArea
 */
private java.awt.TextArea getTextArea1() {
	if (ivjTextArea1 == null) {
		ivjTextArea1 = new java.awt.TextArea();
		ivjTextArea1.setName("TextArea1");
		ivjTextArea1.setRows(2);
		ivjTextArea1.setColumns(6);
	}
	return ivjTextArea1;
}
/**
 * Return the TextField1 property value.
 * @return java.awt.TextField
 */
private java.awt.TextField getTextField1() {
	if (ivjTextField1 == null) {
		ivjTextField1 = new java.awt.TextField();
		ivjTextField1.setName("TextField1");
		ivjTextField1.setText("TextField");
	}
	return ivjTextField1;
}
/**
 * Initialize the class.
 */
private void initialize() {
	this.setName("Frame1");
	this.setLayout(new java.awt.FlowLayout());
	this.setBounds(45, 25, 270, 227);
	this.setTitle("Hello World");
	this.add(getCheckbox1(), getCheckbox1().getName());
	this.add(getButton1(), getButton1().getName());
	this.add(getLabel1(), getLabel1().getName());
	this.add(getChoice1(), getChoice1().getName());
	this.add(getList1(), getList1().getName());
	this.add(getScrollbar1(), getScrollbar1().getName());
	this.add(getTextField1(), getTextField1().getName());
	this.add(getTextArea1(), getTextArea1().getName());
}
}
