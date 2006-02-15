/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.propertysheet;
/*
 *  $RCSfile: MinmaxValidator.java,v $
 *  $Revision: 1.6 $  $Date: 2006-02-15 16:11:40 $ 
 */


 
import java.text.MessageFormat;
import java.text.ParseException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ICellEditorValidator;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.StringTokenizer;

/**
 * Validates the number to verify if it is within the bounds 
 * specified for this validator.
 * You can specify a min only, a max only, or a min and max.
 * the min and max will be included in the range. I.e. if
 * equal to the min or max, then the answer will be true.
 *
 * Null is considered valid, if null is not wanted, then
 * another validator needs to check for this.
 *
 * Note: This won't appropriately handle Big... numbers. They
 * can be bigger than a double value so the test would be
 * inaccurate.
 */
public class MinmaxValidator implements ICellEditorValidator, IExecutableExtension {
	public static final String 
		MIN_ONLY = "minonly",	// $NON-NLS-1$ //$NON-NLS-1$
		MAX_ONLY = "maxonly";	// $NON-NLS-1$ //$NON-NLS-1$
		
	public static final Long LONG_UNDERFLOW = new Long(-42L);
	public static final Long LONG_OVERFLOW = new Long(42L);
		
	protected static final MessageFormat sMinMaxError, sMinError, sMaxError;
	protected static final String sNotNumberError;
	static {
		sMinMaxError = new MessageFormat(PropertysheetMessages.minmax_bad_WARN_);
		sMinError = new MessageFormat(PropertysheetMessages.min_bad_WARN_);
		sMaxError = new MessageFormat(PropertysheetMessages.max_bad_WARN_);
		sNotNumberError = PropertysheetMessages.not_number_WARN_;
	}
		
	protected static final NumberFormat sNumberFormat;
	static { 
		sNumberFormat = NumberFormat.getInstance();
		sNumberFormat.setMaximumFractionDigits(8);
	}
	
	protected String fType = null;	// Min/max/ or both
	
	protected Number fMinValue, fMaxValue;

	/**
	 * Default to min/max of Integer type.
	 */
	public MinmaxValidator() {
		this(new Long(Integer.MIN_VALUE), new Long(Integer.MAX_VALUE));
	}
		
	public MinmaxValidator(int min, int max) {
		this(new Long(min), new Long(max));
	}
	
	/**
	 * The type will be one the two above to indicate
	 * setting min only or max only.
	 */
	public MinmaxValidator(int in, String type) {
		this(new Long(in), type);
	}
	
	public MinmaxValidator(Number min, Number max) {
		setMinMax(min, max);
	}
	
	/**
	 * The type will be one the two above to indicate
	 * setting min only or max only.
	 */
	public MinmaxValidator(Number in, String type) {
		setOnly(in, type);
	}

	public void setMinMax(int min, int max) {
		setMinMax(new Long(min), new Long(max));
	}
	
	/**
	 * This will only expect initData to be a string.
	 * The string should be:
	 *   a) min: minvalue
	 *   b) max: maxvalue
	 *   c) minvalue, maxvalue
	 *
	 * e.g.:
	 *   min: 3
	 *   3, 10
	 */
	public void setInitializationData(IConfigurationElement ce, String pName, Object initData) {
		if (initData instanceof String) {
			StringTokenizer st = new StringTokenizer((String) initData, ":,", true); //$NON-NLS-1$
			String s = null;
			if (st.hasMoreTokens())
				s = st.nextToken();
			if ("min".equalsIgnoreCase(s)) { //$NON-NLS-1$
				if (!st.hasMoreTokens())
					return;	// Invalid format;
				s = st.nextToken();
				if (!st.hasMoreTokens())
					return;	// Invalid format;
				s = st.nextToken();
				try {				
					Number min = sNumberFormat.parse(s);
					setOnly(min, MIN_ONLY);
				} catch (ParseException e) {
				}
			} else if ("max".equalsIgnoreCase(s)) { //$NON-NLS-1$
				try {
					if (!st.hasMoreTokens())
						return;	// Invalid format;
					s = st.nextToken();
					if (!st.hasMoreTokens())
						return;	// Invalid format;
					s = st.nextToken();				
					Number max = sNumberFormat.parse(s);
					setOnly(max, MAX_ONLY);
				} catch (ParseException e) {
				}
			} else {
				try {
					// Should be number, number
					Number min = sNumberFormat.parse(s);
					if (!st.hasMoreTokens())
						return;	// Invalid format;				
					s = st.nextToken();
					if (!st.hasMoreTokens())
						return;	// Invalid format;
					s = st.nextToken();
					Number max = sNumberFormat.parse(s);
					setMinMax(min, max);
				} catch (ParseException e) {
				}
			}
		}
	}
	
	public void setMinMax(Number min, Number max) {
		fType = null;
		fMinValue = min;
		fMaxValue = max;
	}
	
	/**
	 * The type will be one the two above to indicate
	 * setting min only or max only.
	 */	
	public void setOnly(int in, String type) {
		setOnly(new Long(in), type);
	}
	
	public void setOnly(Number in, String type) {
		if (MIN_ONLY.equals(type)) {
			fType = MIN_ONLY;
			fMinValue = in;
		} else {
			fType = MAX_ONLY;
			fMaxValue = in;
		}
	}		

	public String isValid(Object value) {
		if (value instanceof Number) {
			// Check for out of bounds Long values
			if (value == LONG_UNDERFLOW || value == LONG_OVERFLOW) {
				if (fType == null)
					return sMinMaxError.format(new Object[] {sNumberFormat.format(fMinValue.longValue()), sNumberFormat.format(fMaxValue.longValue())});
				else if (fType == MIN_ONLY)
					return (value != LONG_UNDERFLOW) ? null : sMinError.format(new Object[] {sNumberFormat.format(fMinValue.longValue())});
				else
					return (value != LONG_OVERFLOW) ? null : sMaxError.format(new Object[] {sNumberFormat.format(fMaxValue.longValue())});
			}
			
			// We need to know whether a floating or integer because long has more precision
			// than double so we need to do comparisons as either double or longs.
			if (!(value instanceof Double || value instanceof Float)) {
				// It is an integer type value
				long l = ((Number) value).longValue();
				if (fType == null)
					return (fMinValue.longValue() <= l && l <= fMaxValue.longValue()) ? null : sMinMaxError.format(new Object[] {sNumberFormat.format(fMinValue.longValue()), sNumberFormat.format(fMaxValue.longValue())});
				else if (fType == MIN_ONLY)
					return (fMinValue.longValue() <= l) ? null : sMinError.format(new Object[] {sNumberFormat.format(fMinValue.longValue())});
				else
					return (l <= fMaxValue.longValue()) ? null : sMaxError.format(new Object[] {sNumberFormat.format(fMaxValue.longValue())});
			} else {
				// It is a floating type value
				double d = ((Number) value).doubleValue();
				if (fType == null)
					return (fMinValue.doubleValue() <= d && d <= fMaxValue.doubleValue()) ? null : sMinMaxError.format(new Object[] {sNumberFormat.format(fMinValue.doubleValue()), sNumberFormat.format(fMaxValue.doubleValue())});
				else if (fType == MIN_ONLY)
					return (fMinValue.doubleValue() <= d) ? null : sMinError.format(new Object[] {sNumberFormat.format(fMinValue.doubleValue())});
				else
					return (d <= fMaxValue.doubleValue()) ? null : sMaxError.format(new Object[] {sNumberFormat.format(fMaxValue.doubleValue())});				
			}
		} else if (value != null)
			return sNotNumberError;
		else 
			return null;
	}
					
}
