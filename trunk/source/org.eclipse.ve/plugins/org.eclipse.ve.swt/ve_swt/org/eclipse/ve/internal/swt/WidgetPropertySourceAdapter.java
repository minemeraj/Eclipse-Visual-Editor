/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: WidgetPropertySourceAdapter.java,v $ $Revision: 1.15 $ $Date: 2004-05-19 23:04:11 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.*;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.FeatureAttributeValue;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.propertysheet.EToolsPropertyDescriptor;

/**
 * Property Source adapter for SWT Widgets. It handles the style bits.
 * 
 * @since 1.0.0
 */
public class WidgetPropertySourceAdapter extends BeanPropertySourceAdapter {

	private static String[] EXPERT_FILTER_FLAGS = new String[] { IPropertySheetEntry.FILTER_ID_EXPERT};

	private static final Object SWEET_STYLE_ID = "org.eclipse.ve.sweet.stylebits";

	protected static final String SWT_TYPE_ID = "org.eclipse.swt.SWT";
	
	protected static final String STYLE_NONE = "NONE";
	
	protected static final int STYLE_NOT_SET = -1;

	static class StyleBitPropertyID {

		String propertyName;

		public Integer[] bitValues;

		private String[] fInitStrings;

		// These are the packaged init strings for fields were for each entry in the
		// primary array there are two entries in the secondary one, the first entry
		// is the fully-qualified class and the second is the field name. 
		// It is assumed all of the bit settings will be static field access, otherwise
		// it will not be able to set the expressions correctly. (i.e. can't handle straight numbers
		// because we would need to get AST involved to form parse tree). Maybe in next release.
		// This will allow us to handle customized bits that aren't in org.eclipse.swt
		private String[][] packagedInitStrings;

		StyleBitPropertyID(String aPropertyName, Integer[] values, String[] initStrings) {
			propertyName = aPropertyName;
			bitValues = values;
			fInitStrings = initStrings;
		}
		
		public String getFamilyName() {
			return propertyName;
		}

		/**
		 * Check to see if this field is one of style bits for this family.
		 * 
		 * @param existingField
		 * @return <code>true</code> if one of this family.
		 * 
		 * @since 1.0.0
		 */
		public boolean includeInitString(PTFieldAccess existingField) {
			// Check the intString against the existing ones
			if (!(existingField.getReceiver() instanceof PTName))
				return false;	// We don't have a valid receiver, so it can't match.
			return getStyle(((PTName) existingField.getReceiver()).getName(), existingField.getField()) != STYLE_NOT_SET;
		}

		private final static String[] UNKNOWN = new String[] {"???", "???"};
		public String[] getInitString(int val) {
			initializepackagedInitStrings();
			for (int i = 0; i < bitValues.length; i++) {
				if (bitValues[i].intValue() == val) { return packagedInitStrings[i]; }
			}
			return UNKNOWN;
		}
		
		/**
		 * Get the style value for the given classname and field.
		 * @param className
		 * @param fieldName
		 * @return the style value or <code>STYLE_NOT_SET</code> if not found.
		 * 
		 * @since 1.0.0
		 */
		public int getStyle(String className, String fieldName) {
			initializepackagedInitStrings();
			for (int i = 0; i < packagedInitStrings.length; i++) {
				if (packagedInitStrings[i][1].equals(fieldName) && packagedInitStrings[i][0].equals(className)) {
					return bitValues[i].intValue();
				}
			}
			return STYLE_NOT_SET;
		}

		private void initializepackagedInitStrings() {
			if (packagedInitStrings == null) {
				packagedInitStrings = new String[fInitStrings.length][2];
				for (int i = 0; i < fInitStrings.length; i++) {
					int lastDot = fInitStrings[i].lastIndexOf('.');
					if (lastDot != -1) {
						packagedInitStrings[i][0] = fInitStrings[i].substring(0, lastDot);
						packagedInitStrings[i][1] = fInitStrings[i].substring(lastDot+1);
					} else {
						packagedInitStrings[i][0] = "";
						packagedInitStrings[i][1] = fInitStrings[i];
					}
				}
			}
		}
	}

	public static class StyleBitPropertyDescriptor extends EToolsPropertyDescriptor {

		String fPropertyName;

		String fDisplayName;

		String[] fNames;

		String[] fInitStrings;

		Integer[] fValues;

		boolean fIsExpert;

		private ILabelProvider labelProvider; // Performance cache because property sheets asks for this twice always

		public StyleBitPropertyDescriptor(String propertyName, String displayName, boolean isExpert, String[] names, String[] initStrings,
				Integer[] values) {
			super(new StyleBitPropertyID(propertyName, values, initStrings), propertyName);
			fPropertyName = propertyName;
			fNames = names;
			fDisplayName = displayName;
			fIsExpert = isExpert;
			fInitStrings = initStrings;
			fValues = values;
		}

		public CellEditor createPropertyEditor(Composite parent) {
			return new EnumeratedIntValueCellEditor(parent, fNames, fValues, fInitStrings);
		}

		public ILabelProvider getLabelProvider() {
			if (labelProvider == null) { return new EnumeratedIntValueLabelProvider(fNames, fValues); }
			return labelProvider;
		}

		public String[] getFilterFlags() {
			return fIsExpert ? EXPERT_FILTER_FLAGS : null;
		}

		public String getDisplayName() {
			return fDisplayName;
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
		List descriptorsList = new ArrayList(propertyDescriptors.length + 5);
		for (int i = 0; i < propertyDescriptors.length; i++) {
			descriptorsList.add(propertyDescriptors[i]);
		}
		mergeStyleBits(descriptorsList, ((EObject) getTarget()).eClass());
		IPropertyDescriptor[] resultArray = new IPropertyDescriptor[descriptorsList.size()];
		descriptorsList.toArray(resultArray);
		return resultArray;

	}

	private final static String STYLE_NOT_SET_INITSTRING = String.valueOf(STYLE_NOT_SET);
	
	/**
	 * Collects the style bits, creates property descriptors for them, and adds them to the argument
	 * 
	 * @param propertyDescriptors
	 *            list into which new property descriptors are added for the constructor style bits
	 * 
	 * @since 1.0.0
	 */
	protected void mergeStyleBits(List propertyDescriptors, EClass eClass) {

		BeanDecorator beanDecor = Utilities.getBeanDecorator(eClass);
		Map styleDetails = getStyleDetails(beanDecor);
		while (styleDetails == null || styleDetails.size() == 0) {
			eClass = ((JavaClass) eClass).getSupertype();
			if (eClass == null)
				return;
			beanDecor = Utilities.getBeanDecorator(eClass);
			styleDetails = getStyleDetails(beanDecor);
		}

		Iterator sweetProperties = styleDetails.keySet().iterator();
		while (sweetProperties.hasNext()) {
			// The properties are instances of BeanDecoratorImpl.Sweet
			// and the third is an Integer[] of actual values
			String propertyName = (String) sweetProperties.next();
			SweetStyleBits styleBits = (SweetStyleBits) styleDetails.get(propertyName);
			String[] names = styleBits.fNames;
			String[] initStrings = styleBits.fInitStrings;
			Integer[] values = styleBits.fValues;

			// If there is just one value then change this to be ON and add another one with OFF
			// The propertyName is also changed to be the first name
			// This is for style bits like SWT.BORDER that become called "border" with "BORDER" and "not set";
			if (names.length == 1) {
				names = new String[] { names[0], "<not set>"};
				initStrings = new String[] { initStrings[0], STYLE_NOT_SET_INITSTRING};
				values = new Integer[] { values[0], new Integer(STYLE_NOT_SET)};
			}
			IPropertyDescriptor property = new StyleBitPropertyDescriptor(propertyName, styleBits.fDisplayName, styleBits.fIsExpert, names,
					initStrings, values);
			propertyDescriptors.add(property);
		}

	}

	public static class SweetStyleBits {

		public String fPropertyName;

		public String fDisplayName;

		public boolean fIsExpert;

		public String[] fNames;

		public String[] fInitStrings;

		public Integer[] fValues;

		public SweetStyleBits(String propertyName, String displayName, boolean isExpert, String[] names, String[] initStrings, Integer[] values) {
			fPropertyName = propertyName;
			fDisplayName = displayName;
			fIsExpert = isExpert;
			fNames = names;
			fInitStrings = initStrings;
			fValues = values;
		}
	}

	public static Map getStyleDetails(BeanDecorator beanDecorator) {

		FeatureAttributeValue value = (FeatureAttributeValue) beanDecorator.getAttributes().get(SWEET_STYLE_ID);
		// If the value has been previously calculated it is cached so return it
		if (value != null && value.isSetValueJava())
			return (Map) value.getValueJava();
		// otherwise derive it from the target values
		if (value != null && value.isSetValueProxy()) {
			// There are some style bits picked up by introspection
			// Turn them into a map of IDE style bits that we can use, and then cache these back onto the BeanDecorator so
			// we don't have to re-do this target VM query each time
			IArrayBeanProxy outerArray = (IArrayBeanProxy) value.getValueProxy();
			Map styleDetails = new HashMap();
			for (int i = 0; i < outerArray.getLength(); i++) {
				try {
					IArrayBeanProxy innerArray = (IArrayBeanProxy) outerArray.get(i);
					// The first element is a String for the internal canonnical name
					String propertyName = ((IStringBeanProxy) innerArray.get(0)).stringValue();
					// The second element is the user visible name
					String displayName = ((IStringBeanProxy) innerArray.get(1)).stringValue();
					// The third element is a Boolean value for whether the property is expert or not
					boolean expert = ((IBooleanBeanProxy) innerArray.get(2)).booleanValue();
					// The next is a three element array of name, initString, and actual value * n for the number of allowble values
					// Iterate over it to extract the names and strings and turn these into two separate String arrays
					IArrayBeanProxy triplicateArray = (IArrayBeanProxy) innerArray.get(3);
					int numberOfValues = triplicateArray.getLength() / 3;
					String[] names = new String[numberOfValues];
					String[] initStrings = new String[numberOfValues];
					Integer[] values = new Integer[numberOfValues];
					for (int j = 0; j < triplicateArray.getLength(); j = j + 3) {
						int index = j / 3;
						names[index] = ((IStringBeanProxy) triplicateArray.get(j)).stringValue();
						initStrings[index] = ((IStringBeanProxy) triplicateArray.get(j + 1)).stringValue();
						values[index] = new Integer(((IIntegerBeanProxy) triplicateArray.get(j + 2)).intValue());
					}
					styleDetails.put(propertyName, new SweetStyleBits(propertyName, displayName, expert, names, initStrings, values));
				} catch (ThrowableProxy exc) {
					SwtPlugin.getDefault().getLogger().log(exc, Level.WARNING);
				}
			}
			value.setValueJava(styleDetails);
			return styleDetails;
		}
		return null;
	}

	private WidgetProxyAdapter widgetProxyAdapter;

	private WidgetProxyAdapter getWidgetProxyAdapter() {
		if (widgetProxyAdapter == null) {
			widgetProxyAdapter = (WidgetProxyAdapter) BeanProxyUtilities.getBeanProxyHost(getBean());
		}
		return widgetProxyAdapter;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object descriptorID) {
		if (descriptorID instanceof EStructuralFeature) {
			return super.getPropertyValue(descriptorID);
		} else if (descriptorID instanceof StyleBitPropertyID) {
			int currentStyleValue = getWidgetProxyAdapter().getStyle();
			// The style value represents all the bits together. We must return a single int value the represents the style that is set
			// for the family that this property represents
			Integer[] availableValues = ((StyleBitPropertyID) descriptorID).bitValues;
			for (int i = 0; i < availableValues.length; i++) {
				if ((availableValues[i].intValue() & currentStyleValue) == availableValues[i].intValue()) { return availableValues[i]; }
			}
			return new Integer(STYLE_NOT_SET);
		} else {
			return "???";
		}
	}

	public boolean isPropertySet(Object descriptorID) {
		if (descriptorID instanceof EStructuralFeature) {
			return super.isPropertySet(descriptorID);
		} else if (descriptorID instanceof StyleBitPropertyID) {
			int currentValue = ((Integer) getPropertyValue(descriptorID)).intValue();
			// If the current property value is -1 then this means it is the "UNSET" value from a single value'd property and by definition must be
			// not set
			if (currentValue == STYLE_NOT_SET)
				return false;
			// The current value is the one set on the actual target VM
			// If the explicit style bitAND the current value is 1 then it is set
			return (currentValue & getExplicitStyle()) != 0;
		} else {
			return false;
		}
	}
	
	private int explicitStyle = STYLE_NOT_SET;	// Cached style. Will be cleared if allocation has been changed. 

	/**
	 * Return the style that is actually explictly set when this widget is constructed by us.
	 * @return the int representation of the style.
	 * 
	 * @since 1.0.0
	 */
	protected int getExplicitStyle() {
		if (explicitStyle == STYLE_NOT_SET) {
			IBeanProxy styleBeanProxy = null;
			// Get the arguments from the source that are the style bits that are explicitly set
			if (getBean().getAllocation() instanceof ParseTreeAllocation) {
				PTExpression styleExpression = getStyleExpression(((ParseTreeAllocation) getBean().getAllocation()).getExpression());
				if (styleExpression != null) {
					try {
						styleBeanProxy = BasicAllocationProcesser.instantiateWithExpression(styleExpression, getWidgetProxyAdapter().getBeanProxyDomain());
						if (styleBeanProxy != null && !(styleBeanProxy instanceof IIntegerBeanProxy)) {
							styleBeanProxy.getProxyFactoryRegistry().releaseProxy(styleBeanProxy);
							styleBeanProxy = null;	// Not an integer, so an invalid return. Really wasn't the style expression.
						}
					} catch (AllocationException e) {
						SwtPlugin.getDefault().getLogger().log(e.getCause(), Level.INFO);
					}
				}
			}
			explicitStyle = styleBeanProxy != null ? ((IIntegerBeanProxy) styleBeanProxy).intValue() : SWT.NONE;
		}
		return explicitStyle;
	}
	
	
	/**
	 * Return the style bit expression out of the allocation expression.
	 * <p>
	 * The default will expect it to be the second arg of a Class instance creation expression.
	 * <p>
	 * Subclasses should override and return the style expression if the default is not sufficient.
	 * 
	 * @param allocationExp
	 *            the allocation expression for allocating the object.
	 * @return the style expression or <code>null</code> if not found.
	 * 
	 * @since 1.0.0
	 */
	protected PTExpression getStyleExpression(PTExpression allocationExp) {
		if (allocationExp instanceof PTClassInstanceCreation) {
			PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExp;
			if (classInstanceCreation.getArguments().size() == 2)
				return (PTExpression) classInstanceCreation.getArguments().get(1);
		}
		return null; // Not found or not of what we expect.
	}
	

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(Object descriptorID) {
		if (!(descriptorID instanceof StyleBitPropertyID)) {
			super.resetPropertyValue(descriptorID);
		} else {
			setPropertyValue(descriptorID, null);
		}
	}

	/**
	 * Find existing style that is equivalent, and update it. This handles changing a bit, it won't add or remove
	 * an expression.
	 * 
	 * @param current field or OR infix. Any other type cannot be handled.
	 * @param id
	 * @param style
	 * @return true, updated, false not updated.
	 */
	protected boolean updateStyleBit(PTExpression current, StyleBitPropertyID id, int style) {
		if (current instanceof PTFieldAccess) {
			PTFieldAccess field = (PTFieldAccess) current;
			// We have a single bit setting, see we can change this one field.
			if (isSameStyleFamily(field, id)) {
				updateField(field, id.getInitString(style));
				return true;
			}
			return false;
		} else if (current instanceof PTInfixExpression) {
			PTInfixExpression infix = (PTInfixExpression) current;
			if (infix.getOperator() == PTInfixOperator.OR_LITERAL) {
				// This is an or expression, so we can handle it.
				// See if we can update just the left or right side.
				if (updateStyleBit(infix.getLeftOperand(), id, style) || updateStyleBit(infix.getRightOperand(), id, style))
					return true;
				if (!infix.getExtendedOperands().isEmpty()) {
					// Now walk the extended. One of them may also be changable.
					List ext = infix.getExtendedOperands();
					for (int i = 0; i < ext.size(); i++) {
						if (updateStyleBit((PTExpression) ext.get(i), id, style))
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Update the field sent it, or if <code>null</code> create one, to the updateTo class/field name.
	 * 
	 * @param current the field access to update, or <code>null</code> if should create one.
	 * @param updateTo the class/fieldname to update the field to
	 * @return the updated or created field access.
	 * 
	 * @since 1.0.0
	 */
	protected PTFieldAccess updateField(PTFieldAccess current, String[] updateTo) {
		if (current == null)
			current = InstantiationFactory.eINSTANCE.createPTFieldAccess();
		if (current.getReceiver() == null || !(current.getReceiver() instanceof PTName) || !(((PTName) current.getReceiver()).getName().equals(updateTo[0]))) {
			// need to change receiver
			current.setReceiver(InstantiationFactory.eINSTANCE.createPTName(updateTo[0]));
		}
		current.setField(updateTo[1]);
		return current;
	}
	
	/**
	 * Helper to add/update style bit to a style argument. Assume the syle is constructed from Infix/Field expressions only.
	 * If not then we won't change the expression. 
	 * 
	 * @param current expression for current style setting. If <code>null</code>, then just create new field expression.
	 * @param id the property id object
	 * @param style the style bits to set.
	 * @return the augmented expression with style set.
	 * 
	 * @since 1.0.0
	 */
	protected PTExpression addStyleBit(PTExpression current, StyleBitPropertyID id, int style) {
		if (updateStyleBit(current, id, style))
			return current;

		// Need to add a new element. It wasn't existing in the current expression.
		PTFieldAccess field = updateField(null, id.getInitString(style));

		if (current == null) {
			return field;	// We did not have one before, so just return the new field.
		} else if (current instanceof PTFieldAccess) {
			// Or it into the current expression. Need a new infix for this.
			return InstantiationFactory.eINSTANCE.createPTInfixExpression(current, PTInfixOperator.OR_LITERAL, field, null);
		} else {
			if (current instanceof PTInfixExpression) {
				if (((PTInfixExpression) current).getOperator() == PTInfixOperator.OR_LITERAL) {
					// We are in an infix and are an OR type, so we can just add our new guy to the extended ops.
					((PTInfixExpression) current).getExtendedOperands().add(field);
					return current;
				} else {
					// Add our new guy to the right with an OR. Need a new infix for this because we weren't an or infix previously.
					return InstantiationFactory.eINSTANCE.createPTInfixExpression(current, PTInfixOperator.OR_LITERAL, field, null);
				}
			} else {
				// Return untouched. Don't know what to do with it. We could treat it as if it was an int and do an infix, such as a factory, but what if it wasn't.
				// Anyway, for factories we would need to handle AND'ing bits out possibly. Maybe a later release.
				return current;	
			}
		}
	}

	/**
	 * Remove a stylebit from the expression.  Assume the syle is constructed from Infix/Field expressions only.
	 * If not then we won't change the expression. If we remove the last expression so that nothing is set, then
	 * <code>null</code> is returned. 
	 * 
	 * @param styleExpression the style expression to modify
	 * @param id
	 * @param style
	 * @return the modified style expression with the bit removed, or <code>null</code> if all bits removed.
	 */
	protected PTExpression removeStyleBit(PTExpression styleExpression, StyleBitPropertyID id, int style) {
		if (styleExpression instanceof PTFieldAccess) {
			// A single style bit
			if (isSameStyleFamily((PTFieldAccess) styleExpression, id)) {
				return null;	// There was nothing left.
			}
			return styleExpression;	// It wasn't for this bit, so nothing to remove.
		} else if (styleExpression instanceof PTInfixExpression && ((PTInfixExpression) styleExpression).getOperator() == PTInfixOperator.OR_LITERAL) {
			// We have an OR infix, so we can handle it.
			PTInfixExpression infixExpression = (PTInfixExpression) styleExpression;
			if (infixExpression.getLeftOperand() instanceof PTFieldAccess) {
				if (isSameStyleFamily((PTFieldAccess) infixExpression.getLeftOperand(), id)) {
					// Need to remove the left operand from the list since it is the bit of interest.
					if (infixExpression.getExtendedOperands().isEmpty())
						return infixExpression.getRightOperand();
					else {
						// Need to shift left
						infixExpression.setLeftOperand(infixExpression.getRightOperand());
						infixExpression.setRightOperand((PTExpression) infixExpression.getExtendedOperands().remove(0));
						return infixExpression;
					}
				}
			}
			if (infixExpression.getRightOperand() instanceof PTFieldAccess) {
				if (isSameStyleFamily((PTFieldAccess) infixExpression.getRightOperand(), id)) {
					// Need to remove the right operand from the list since it is the bit of interest.
					if (infixExpression.getExtendedOperands().isEmpty())
						return infixExpression.getLeftOperand();
					else {
						// Need to shift left
						infixExpression.setRightOperand((PTExpression) infixExpression.getExtendedOperands().remove(0));
						return infixExpression;
					}
				}
			}
			
			if (!infixExpression.getExtendedOperands().isEmpty()) {
				// See if any of the extended operands are of interest.
				List ext = infixExpression.getExtendedOperands();
				for (int i = 0; i < ext.size(); i++) {
					if (ext.get(i) instanceof PTFieldAccess) {
						if (isSameStyleFamily((PTFieldAccess) ext.get(i), id)) {
							// Remove it.
							ext.remove(i);
							return infixExpression;	// So return now.
						}
					}
				}
			}
			
			// None of the operands were a straight field access of interest. So now go through each
			// of the operands and do remove recursively. Since we already tested each for field access
			// of interest, we won't get a null back from removeStyleBit for these tests.
			infixExpression.setLeftOperand(removeStyleBit(infixExpression.getLeftOperand(), id, style));
			infixExpression.setRightOperand(removeStyleBit(infixExpression.getRightOperand(), id, style));
			if (!infixExpression.getExtendedOperands().isEmpty()) {
				// Go through the extended operands.
				List ext = infixExpression.getExtendedOperands();
				for (int i = 0; i < ext.size(); i++) {
					ext.set(i, removeStyleBit((PTExpression) ext.get(i), id, style));
				}
			}
			return infixExpression;
		}
		return styleExpression;	// Couldn't change it or didn't need to.
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 * 
	 * An internal function is that if val is <code>null</code> and feature is a style bit, then this means reset.
	 */
	public void setPropertyValue(Object feature, Object val) {
		if (feature instanceof EStructuralFeature) {
			super.setPropertyValue(feature, val);
		} else if (feature instanceof StyleBitPropertyID) {
			StyleBitPropertyID propertyID = (StyleBitPropertyID) feature;
			// The tree for creating the example for a single style bit is
			// <allocation xmi:type=ParseTreeAllocation>
			//   <expression xmi:type=PTClassInstanceCreation>
			//     <arguments xmi:type=PTInstanceReference>
			//     <arguments xmi:type=PTFieldAccess field="NONE>
			//       <receiver xmi:type=PTName name="org.eclipse.swt.SWT">
			// And for a set of style bits is
			// <allocation xmi:type=ParseTreeAllocation>
			//   <expression xmi:type=PTClassInstanceCreation>
			//     <arguments xmi:type=PTInstanceReference>
			//     <arguments xmi:type=PTInfixExpression operator=OR>
			//       <leftOperator xmi:type=PTName name="org.eclipse.swt.BORDER">
			//       <rightOperator xmi:type=PTName name="org.eclipse.swt.CHECK">
			// A value of -1 (or null) means that we are unsetting the property value
			int intValue = val != null ? ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) val)).intValue() : STYLE_NOT_SET;
			
			// See if we are changing it. If not, then don't do anything. Don't want to signal an unneeded change.	
			if (((Integer) getPropertyValue(feature)).intValue() == intValue)
				return;	// The property has not changed. Don't do anything.
				
			JavaAllocation alloc = getBean().getAllocation();
			if (alloc instanceof ParseTreeAllocation) {
				// Get the changed allocation. If null, then it means don't change it. This could occur because the expression
				// was not understood, so we couldn't change the style.
				PTExpression newAllocation = getChangedAllocation(((ParseTreeAllocation) alloc).getExpression(), propertyID,
						intValue);
				if (newAllocation != null) {
					ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
					newParseTreeAlloc.setExpression(newAllocation);
					getBean().setAllocation(newParseTreeAlloc);
				}
			}
		}
	}
	
	/**
	 * This method finds the style argument, sends it to <code>getChangedStyleExpression</code>, and then sets it back into the allocation expression.
	 * <p>
	 * Subclasses may override to handle the style being in a different spot. This default implementation assumes that
	 * the style expression is the second arg the constructor. This is true for virtually of the Widgets. Shell is one that
	 * is different, so we need a subclass to handle that.
	 * 
	 * @param allocationExpression the allocation expression of the current bean.
	 * @param propertyID the style feature being changed
	 * @param styleBit the style value to set
	 * @return the new expression for allocation, or <code>null</code>
	 * 		if it is not to be changed (such as not understood, or not a format that can be understood) 
	 * 		to indicate this so that the allocation will not be changed.
	 * 
	 * @since 1.0.0
	 */
	protected PTExpression getChangedAllocation(PTExpression allocationExpression, StyleBitPropertyID propertyID, int styleBit) {
		if (allocationExpression instanceof PTClassInstanceCreation) {
			PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExpression;
			// We are assuming the second arg is the style bit. For all the standard widgets, this is true.
			// override classes should handle their particular patterns.
			// Use >= 2 because some have three or more, but the second arg is the style.
			// TODO It would be nice if we could get this info from introspection instead of hard-coding.
			if (classInstanceCreation.getArguments().size() >= 2) {
				PTExpression newArg = getChangedStyleExpression((PTExpression) classInstanceCreation.getArguments().get(1), propertyID, styleBit);
				if (newArg == null) {
					// Set go back to no style expression. The default for Widget is SWT.NONE.
					PTName name = InstantiationFactory.eINSTANCE.createPTName(SWT_TYPE_ID);
					newArg = InstantiationFactory.eINSTANCE.createPTFieldAccess(name, STYLE_NONE); 
				}
				classInstanceCreation.getArguments().set(1, newArg);
				return classInstanceCreation;
			}
		}
		return null;	// Don't change it. We can't just return the same one because no way of knowing something under it had changed.
		
	}	
	
	/**
	 * Get the changed style expression. If may be the same one but modified, or it may be a new one.
	 * It should return <code>null</code> for all styles were unset.
	 * 
	 * @param styleExpression
	 * @param propertyID
	 * @param style
	 * @return the modified or new style expression, or <code>null</code> if the final result had no styles set.
	 * 
	 * @since 1.0.0
	 */
	protected PTExpression getChangedStyleExpression(PTExpression styleExpression, StyleBitPropertyID propertyID, int style) {
		return (style != STYLE_NOT_SET) ? 
			addStyleBit(styleExpression, propertyID, style) : 
			removeStyleBit(styleExpression, propertyID, style);		
	}
	
	/**
	 * @param existingField
	 *            The existing field access
	 * @param propertyID
	 *            Instance of StyleBitPropertyID that we are testing to see whether it is part of the same family or not
	 * @return <code>true</code> if in same style family.
	 * @since 1.0.0
	 */
	private boolean isSameStyleFamily(PTFieldAccess existingField, StyleBitPropertyID propertyID) {
		// SWT.NONE is always replaced
		if (existingField.getReceiver() instanceof PTName && SWT_TYPE_ID.equals(((PTName)existingField.getReceiver()).getName()) &&
				STYLE_NONE.equals(existingField.getField()))
			return true;
		// Iterate over the style bits to see if any of them are in the same family
		return propertyID.includeInitString(existingField);
	}
	
	/**
	 * Used by subclasses to see if a certain family is set anywhere in the expression.
	 * 
	 * @param expression
	 * @param propertyID
	 * @return <code>true</code> if set somewhere in expression.
	 * 
	 * @since 1.0.0
	 */
	protected boolean isSameStyleFamilySet(PTExpression expression, StyleBitPropertyID propertyID) {
		if (expression instanceof PTFieldAccess)
			return isSameStyleFamily((PTFieldAccess) expression, propertyID);
		else if (expression instanceof PTInfixExpression && ((PTInfixExpression) expression).getOperator() == PTInfixOperator.OR_LITERAL) {
			// It is an infix or op, so we can test the entries.
			PTInfixExpression infix = (PTInfixExpression) expression;
			if (isSameStyleFamily((PTFieldAccess) infix.getLeftOperand(), propertyID) || isSameStyleFamily((PTFieldAccess) infix.getRightOperand(), propertyID))
				return true;
			if (!(infix.getExtendedOperands().isEmpty())) {
				// Check each of the extended operands.
				List ext = infix.getExtendedOperands();
				for (int i = 0; i < ext.size(); i++) {
					if (isSameStyleFamily((PTFieldAccess) ext.get(i), propertyID))
						return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Return the property id for the requested family, or <code>null</code> if not found.
	 * @param familyName
	 * @return the property id if found, else <code>null</code>
	 * 
	 * @since 1.0.0
	 */
	protected StyleBitPropertyID findFamily(String familyName) {
		IPropertyDescriptor[] properties = getPropertyDescriptors();
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getId() instanceof StyleBitPropertyID) {
				StyleBitPropertyID id = (StyleBitPropertyID) properties[i].getId();
				if (id.getFamilyName().equals(familyName))
					return id;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		if (msg.getFeature() == JavaInstantiation.getAllocationFeature(getBean()))
			explicitStyle = STYLE_NOT_SET;	// The allocation has been changed in some way. Reset the explicit style cache.
		super.notifyChanged(msg);
	}
}