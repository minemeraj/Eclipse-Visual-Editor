package org.eclipse.ve.internal.propertysheet;
/*******************************************************************************
 * Copyright (c)  2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NumberCellEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-11 22:41:34 $ 
 */


import org.eclipse.swt.widgets.Composite;
import java.text.*;
import org.eclipse.core.runtime.*;

/**
 * Number Celleditor that formats according to the current locale.
 * It can also handle a null by allowing null to come in. It will be
 * considered invalid but it won't bomb.
 *
 * It will format in locale dependent format (e.g. 3,000 instead of 3000).
 *
 * Note: Big... (e.g. BigInteger) is not supported by this editor.
 */

public class NumberCellEditor extends ObjectCellEditor implements IExecutableExtension {
	public static final int
		// Type of number to be returned.
		NUMBER = 0,	// Whatever it produces
		BYTE = 1,
		DOUBLE = 2,
		FLOAT = 3,
		INTEGER = 4,
		LONG = 5,
		SHORT = 6;
		
	protected static final MinmaxValidator[] sMinMaxValidators = {
		null,
		new MinmaxValidator(new Byte(Byte.MIN_VALUE), new Byte(Byte.MAX_VALUE)),
		new MinmaxValidator(new Double(Double.MIN_VALUE), new Double(Double.MAX_VALUE)),
		new MinmaxValidator(new Float(Float.MIN_VALUE), new Float(Float.MAX_VALUE)),
		new MinmaxValidator(new Integer(Integer.MIN_VALUE), new Integer(Integer.MAX_VALUE)),
		new MinmaxValidator(new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE)),
		new MinmaxValidator(new Short(Short.MIN_VALUE), new Short(Short.MAX_VALUE))
	};
		
	protected static final String sNotNumberError, sNotIntegerError;
	static {
		sNotNumberError = PropertysheetMessages.getString(PropertysheetMessages.NOT_NUMBER);
		sNotIntegerError = PropertysheetMessages.getString(PropertysheetMessages.NOT_INTEGER);		
	}	
	
	protected NumberFormat fFormatter;
	{
		fFormatter = NumberFormat.getInstance();
		fFormatter.setMaximumFractionDigits(20);
		fFormatter.setMaximumIntegerDigits(20);
	}
	
	protected int fNumberType = NUMBER;

	public NumberCellEditor(Composite parent){
		super(parent);
	}
	
	/**
	 * This will only expect initData to be a string.
	 * The string should be the type,
	 *   a) integer
	 *   b) long
	 *   c) etc.
	 *
	 * number is the default.
	 */
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String) {
			String type = ((String) initData).trim();
			if ("byte".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(BYTE);
			else if ("double".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(DOUBLE);
			else if ("float".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(FLOAT);
			else if ("integer".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(INTEGER);
			else if ("long".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(LONG);
			else if ("short".equalsIgnoreCase(type)) //$NON-NLS-1$
				setType(SHORT);
		}
	}
				
	public void setType(int type) {
		switch (type) {
			case NUMBER:
			case DOUBLE:
			case FLOAT:
				fFormatter.setParseIntegerOnly(false);			
				break;
			case BYTE:
			case INTEGER:
			case LONG:
			case SHORT:
				fFormatter.setParseIntegerOnly(true);
				break;
			default:
				return;	// Invalid type, do nothing
		}
		
		fNumberType = type;
	}
	
	protected String isCorrectObject(Object value) {
		return (value == null || value instanceof Number) ?
			null : (fFormatter.isParseIntegerOnly() ? sNotIntegerError : sNotNumberError);
	}
	
	protected String isCorrectString(String value) {
		String text = value.trim();
		Number result = null;
		if ((fNumberType == DOUBLE || fNumberType == FLOAT) && (text.indexOf('e') != -1 || text.indexOf('E') != -1)) {
			// We have a double/float with an exponent. This is scientific notation. Formatter handles them badly, so use parse instead.
			try {
				if (fNumberType == DOUBLE)
					result = new Double(Double.parseDouble(text));
				else
					result = new Float(Float.parseFloat(text));
			} catch (NumberFormatException e) {
			}
		} else {
			// integral or not scientific notation. Let formatter handle it.
			ParsePosition parsePosition = new ParsePosition(0);
			result = fFormatter.parse(text, parsePosition);
			if (parsePosition.getErrorIndex() != -1 || parsePosition.getIndex() != text.length())
				result = null;	// Some error
			// Check for out of bounds with long type
			if (fNumberType == LONG && result instanceof Double) {
				result = (result.doubleValue() < 0) ? MinmaxValidator.LONG_UNDERFLOW : MinmaxValidator.LONG_OVERFLOW;
			}
		}
		
		if (result != null) {
			// Now see if it is valid for the requested type.
			MinmaxValidator v = sMinMaxValidators[fNumberType];			
			
			// Double/Float are special because the min/max are on the absolute value, not signed value.
			if (fNumberType == DOUBLE || fNumberType == FLOAT) {
				double d = result.doubleValue();
				if (d == 0.0 || d == -0.0)
					return null;	// +/- zero are valid values.
				result = new Double(Math.abs(d));
			}
			if (v != null) {
				String e = v.isValid(result);
				if (e == null || e.length() == 0)
					return null;
				return e;	// It didn't fit in a the number type.
			}
		}
		return (fFormatter.isParseIntegerOnly() ? sNotIntegerError : sNotNumberError);
	}
	
	
	/**
	 * Return the object that the string represents.
	 */
	protected Object doGetObject(String v) {
		try {
			if (v == null)
				return v;
			Number n = null;
			// Float and Double are done separately below because parseFloat and parseDouble can
			// result in different values when casting a double back to a float.
			switch (fNumberType) {
				case BYTE:
					n = new Byte(fFormatter.parse(v).byteValue());
					break;				
				case DOUBLE:
					if (v.indexOf('E') == -1 && v.indexOf('e') == -1)
						n = new Double(fFormatter.parse(v).doubleValue());
					else
						n = new Double(Double.parseDouble(v));	// It has scientific notation. The formatter just doesn't handle that very well.
					break;
				case FLOAT:
					if (v.indexOf('E') == -1 && v.indexOf('e') == -1)
						n = new Float(fFormatter.parse(v).floatValue());
					else
						n = new Float(Float.parseFloat(v));	// It has scientific notation. The formatter just doesn't handle that very well.
					break;
				case INTEGER:
					n = new Integer(fFormatter.parse(v).intValue());
					break;
				case LONG:
					n = new Long(fFormatter.parse(v).longValue());
					break;
				case SHORT:
					n = new Short(fFormatter.parse(v).shortValue());
					break;
			}
			return n;		
		} catch (ParseException exc) {
			// Shouldn't occur because we already tested validity, and this wouldn't be called if invalid.
		}
		return null;	
	}
	
	/**
	 * Return the string for the object passed in.
	 */
	protected String doGetString(Object value) {
		if ( value instanceof Number ) {
			switch (fNumberType) {
				case DOUBLE:
				case FLOAT:
					// The formatter doesn't handle big/small floats. (i.e. more than the max digits we set).
					// It doesn't go to scientific notation as necessary. The only way to test this is to
					// roundtrip the number. If they come up to be the same, then it is ok. Else format using
					// toString.
					String result = fFormatter.format(value);
					try {
						Number roundTrip = fFormatter.parse(result);
						if (roundTrip.doubleValue() != ((Number) value).doubleValue())
							result = value.toString();
					} catch (ParseException e) {
						result = value.toString();
					}
					return result;
				default:
					return fFormatter.format(value);
			}
		} else 
			return null;	// Invalid or null. No string to display.
	}
}