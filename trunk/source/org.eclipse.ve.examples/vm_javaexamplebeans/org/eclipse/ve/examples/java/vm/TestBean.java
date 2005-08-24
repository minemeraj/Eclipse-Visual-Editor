/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.vm;

public class TestBean {
	private boolean fieldTrueOrFalse = false;
	protected transient java.beans.PropertyChangeSupport propertyChange;
	private java.awt.Color fieldColor = new java.awt.Color(0);
	private java.awt.Color fieldColorPropEditor = new java.awt.Color(0);
	private java.lang.String fieldString = new String();
	private int fieldEnumeration = 0;
	private java.lang.String fieldHidden = new String();
	private java.lang.String fieldInternalName = new String();
	private java.awt.Rectangle fieldRect = new java.awt.Rectangle(0,0, 10, 10);	

	// Test for various default types
	private int fint;
	private int fDay;
	private int fMonth;	
	private Integer fInteger;
	private boolean fboolean;
	private Boolean fBoolean;
	private java.math.BigDecimal fBigDecimal;
	private java.math.BigInteger fBigInteger;
	private byte fbyte;
	private Byte fByte;
	private double fdouble;
	private Double fDouble;
	private float ffloat;
	private Float fFloat;
	private long flong;
	private Long fLong;
	private short fshort;
	private Short fShort;
	private int fEven;
/**
 * TestBean constructor comment.
 */
public TestBean() {
	super();
}
/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(listener);
}
/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}
public java.math.BigDecimal getBigDecimal() {
	return fBigDecimal;
}
public java.math.BigInteger getBigInteger() {
	return fBigInteger;
}
public boolean getBoolean() {
	return fboolean;
}
public Boolean getBooleanClass() {
	return fBoolean;
}
public byte getByte() {
	return fbyte;
}
public Byte getByteClass() {
	return fByte;
}
/**
 * Gets the color property (java.awt.Color) value.
 * @return The color property value.
 * @see #setColor
 */
public java.awt.Color getColor() {
	return fieldColor;
}
/**
 * Gets the colorPropEditor property (java.awt.Color) value.
 * @return The colorPropEditor property value.
 * @see #setColorPropEditor
 */
public java.awt.Color getColorPropEditor() {
	return fieldColorPropEditor;
}
public double getDouble() {
	return fdouble;
}
public Double getDoubleClass() {
	return fDouble;
}
/**
 * Gets the enumeration property (int) value.
 * @return The enumeration property value.
 * @see #setEnumeration
 */
public int getEnumeration() {
	return fieldEnumeration;
}
public int getEven(){
	return fEven;
}
public float getFloat() {
	return ffloat;
}
public Float getFloatClass() {
	return fFloat;
}
/**
 * Gets the hidden property (java.lang.String) value.
 * @return The hidden property value.
 * @see #setHidden
 */
public java.lang.String getHidden() {
	return fieldHidden;
}
public int getInteger() {
	return fint;
}
public Integer getIntegerClass() {
	return fInteger;
}
/**
 * Gets the internalName property (java.lang.String) value.
 * @return The internalName property value.
 * @see #setInternalName
 */
public java.lang.String getInternalName() {
	return fieldInternalName;
}
public long getLong() {
	return flong;
}
public Long getLongClass() {
	return fLong;
}
/**
 * Accessor for the propertyChange field.
 */
protected java.beans.PropertyChangeSupport getPropertyChange() {
	if (propertyChange == null) {
		propertyChange = new java.beans.PropertyChangeSupport(this);
	};
	return propertyChange;
}
/**
 * Gets the recct property (java.awt.Rectangle) value.
 * @return The string property value.
 * @see #setString
 */
public java.awt.Rectangle getRect() {
	// See if returning a new one each time screws up the property sheet.
	return fieldRect.getBounds();
}
public short getShort() {
	return fshort;
}
public Short getShortClass() {
	return fShort;
}
/**
 * Gets the string property (java.lang.String) value.
 * @return The string property value.
 * @see #setString
 */
public java.lang.String getString() {
	return fieldString;
}
/**
 * Gets the trueOrFalse property (boolean) value.
 * @return The trueOrFalse property value.
 * @see #setTrueOrFalse
 */
public boolean getTrueOrFalse() {
	return fieldTrueOrFalse;
}
/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(listener);
}
public void setBigDecimal(java.math.BigDecimal newValue) {
	java.math.BigDecimal oldValue = fBigDecimal;
	fBigDecimal = newValue;
	firePropertyChange("BigDecimal", oldValue, newValue);
}
public void setBigInteger(java.math.BigInteger newValue) {
	java.math.BigInteger oldValue = fBigInteger;
	fBigInteger = newValue;
	firePropertyChange("BigInteger", oldValue, newValue);
}
public void setBoolean(boolean newValue) {
	boolean oldValue = fboolean;
	fboolean = newValue;
	firePropertyChange("boolean", new Boolean(oldValue), new Boolean(newValue));
}
public void setBooleanClass(Boolean newValue) {
	Boolean oldValue = fBoolean;
	fBoolean = newValue;
	firePropertyChange("booleanClass", oldValue, newValue);
}
public void setDay(int aDay){
	fDay = aDay;
}
public int getDay(){
	return fDay;
}
public void setMonth(int aMonth){
	fMonth = aMonth;
}
public int getMonth(){
	return fMonth;
}
public void setByte(byte newValue) {
	byte oldValue = fbyte;
	fbyte = newValue;
	firePropertyChange("byte", new Byte(oldValue), new Byte(newValue));
}
public void setByteClass(Byte newValue) {
	Byte oldValue = fByte;
	fByte = newValue;
	firePropertyChange("byteClass", oldValue, newValue);
}
/**
 * Sets the color property (java.awt.Color) value.
 * @param color The new value for the property.
 * @see #getColor
 */
public void setColor(java.awt.Color color) {
	java.awt.Color oldValue = fieldColor;
	fieldColor = color;
	firePropertyChange("color", oldValue, color);
}
/**
 * Sets the colorPropEditor property (java.awt.Color) value.
 * @param colorPropEditor The new value for the property.
 * @see #getColorPropEditor
 */
public void setColorPropEditor(java.awt.Color colorPropEditor) {
	java.awt.Color oldValue = fieldColorPropEditor;
	fieldColorPropEditor = colorPropEditor;
	firePropertyChange("colorPropEditor", oldValue, colorPropEditor);
}
public void setDouble(double newValue) {
	double oldValue = fdouble;
	fdouble = newValue;
	firePropertyChange("double", new Double(oldValue), new Double(newValue));
}
public void setDoubleClass(Double newValue) {
	Double oldValue = fDouble;
	fDouble = newValue;
	firePropertyChange("doubleClass", oldValue, newValue);
}
/**
 * Sets the enumeration property (int) value.
 * @param enumeration The new value for the property.
 * @see #getEnumeration
 */
public void setEnumeration(int enumeration) {
	int oldValue = fieldEnumeration;
	fieldEnumeration = enumeration;
	firePropertyChange("enumeration", new Integer(oldValue), new Integer(enumeration));
}
public void setEven(int newValue) {
	int oldValue = fEven;
	fEven = newValue;
	firePropertyChange("even", new Integer(oldValue), new Integer(newValue));
}
public void setFloat(float newValue) {
	float oldValue = ffloat;
	ffloat = newValue;
	firePropertyChange("float", new Float(oldValue), new Float(newValue));
}
public void setFloatClass(Float newValue) {
	Float oldValue = fFloat;
	fFloat = newValue;
	firePropertyChange("floatClass", oldValue, newValue);
}
/**
 * Sets the hidden property (java.lang.String) value.
 * @param hidden The new value for the property.
 * @see #getHidden
 */
public void setHidden(java.lang.String hidden) {
	String oldValue = fieldHidden;
	fieldHidden = hidden;
	firePropertyChange("hidden", oldValue, hidden);
}
public void setInteger(int newValue) {
	int oldValue = fint;
	fint = newValue;
	firePropertyChange("integer", new Integer(oldValue), new Integer(newValue));
}
public void setIntegerClass(Integer newValue) {
	Integer oldValue = fInteger;
	fInteger = newValue;
	firePropertyChange("integerClass", oldValue, newValue);
}
/**
 * Sets the internalName property (java.lang.String) value.
 * @param internalName The new value for the property.
 * @see #getInternalName
 */
public void setInternalName(java.lang.String internalName) {
	String oldValue = fieldInternalName;
	fieldInternalName = internalName;
	firePropertyChange("internalName", oldValue, internalName);
}
public void setLong(long newValue) {
	long oldValue = flong;
	flong = newValue;
	firePropertyChange("long", new Long(oldValue), new Long(newValue));
}
public void setLongClass(Long newValue) {
	Long oldValue = fLong;
	fLong = newValue;
	firePropertyChange("longClass", oldValue, newValue);
}
/**
 * Sets the rect property (java.awt.Rectangle) value.
 * @param rect The new value for the property.
 * @see #getString
 */
public void setRect(java.awt.Rectangle rect) {
	java.awt.Rectangle oldValue = fieldRect;
	// Test where setting/getting always creates a new rect (like what happens in AWT).
	fieldRect = rect.getBounds();
	firePropertyChange("rect", oldValue, fieldRect);
}
public void setShort(short newValue) {
	short oldValue = fshort;
	fshort = newValue;
	firePropertyChange("short", new Short(oldValue), new Short(newValue));
}
public void setShortClass(Short newValue) {
	Short oldValue = fShort;
	fShort = newValue;
	firePropertyChange("shortClass", oldValue, newValue);
}
/**
 * Sets the string property (java.lang.String) value.
 * @param string The new value for the property.
 * @see #getString
 */
public void setString(java.lang.String string) {
	String oldValue = fieldString;
	fieldString = string;
	firePropertyChange("string", oldValue, string);
}
/**
 * Sets the trueOrFalse property (boolean) value.
 * @param trueOrFalse The new value for the property.
 * @see #getTrueOrFalse
 */
public void setTrueOrFalse(boolean trueOrFalse) {
	boolean oldValue = fieldTrueOrFalse;
	fieldTrueOrFalse = trueOrFalse;
	firePropertyChange("trueOrFalse", new Boolean(oldValue), new Boolean(trueOrFalse));
}
}
