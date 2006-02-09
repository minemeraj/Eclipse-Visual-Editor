/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: WidgetPropertySourceAdapter.java,v $ $Revision: 1.35 $ $Date: 2006-02-09 15:22:12 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.INumberBeanProxy;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.jcm.JCMPackage;

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

	public static class StyleBitPropertyID {		
		// These are the packaged init strings for fields were for each entry in the
		// primary array there are two entries in the secondary one, the first entry
		// is the fully-qualified class and the second is the field name. 
		// It is assumed all of the bit settings will be static field access, otherwise
		// it will not be able to set the expressions correctly. (i.e. can't handle straight numbers
		// because we would need to get AST involved to form parse tree). Maybe in next release.
		// This will allow us to handle customized bits that aren't in org.eclipse.swt
		private String[][] packagedInitStrings;
		
		private StyleBitPropertyDescriptor propertyDescriptor;
	
		StyleBitPropertyID() {
		}
		
		void setPropertyDescriptor(StyleBitPropertyDescriptor propertyDescriptor) {
			this.propertyDescriptor = propertyDescriptor;
		}
		
		/**
		 * Get the family name for this style.
		 * @return
		 * 
		 * @since 1.0.0
		 */
		public String getFamilyName() {
			return propertyDescriptor.styleBits.fPropertyName;
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
	
		/**
		 * Get the packaged init string for the given value. A packaged init string is two entry array, [0] is classname, [1] is field name.
		 * if [0] is "", then [1]  is most likely an integer constant and not a field. 
		 * @param val
		 * @return packaged init string or <code>StyleBintPropertyDecorator.UNKNOWN</code> if the value is not one of the style bits of this family.
		 * 
		 * @since 1.1.0
		 */
		public String[] getInitString(int val) {
			initializepackagedInitStrings();
			for (int i = 0; i < propertyDescriptor.fValues.length; i++) {
				if (propertyDescriptor.fValues[i].intValue() == val) { return packagedInitStrings[i]; }
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
					return propertyDescriptor.fValues[i].intValue();
				}
			}
			return STYLE_NOT_SET;
		}
	
		/*
		 * Split the initstrings up into entries of (class, field) so that it can
		 * easy to create the parse allocation.
		 * The packaged init strings is an array of a two-entry arrays.
		 * Each entry of packagedInitStrings is a field, each field is
		 * a two-entry array, where first is the classname (or none if no class), and second
		 * is the field name (or probably an integer constant if class is "").
		 */
		private void initializepackagedInitStrings() {
			if (packagedInitStrings == null) {
				packagedInitStrings = new String[propertyDescriptor.fInitStrings.length][2];
				for (int i = 0; i < propertyDescriptor.fInitStrings.length; i++) {
					int lastDot = propertyDescriptor.fInitStrings[i].lastIndexOf('.');
					if (lastDot != -1) {
						packagedInitStrings[i][0] = propertyDescriptor.fInitStrings[i].substring(0, lastDot);
						packagedInitStrings[i][1] = propertyDescriptor.fInitStrings[i].substring(lastDot+1);
					} else {
						packagedInitStrings[i][0] = ""; //$NON-NLS-1$
						packagedInitStrings[i][1] = propertyDescriptor.fInitStrings[i];
					}
				}
			}
		}
	}

	private static final String SWEET_STYLE_ID = "org.eclipse.ve.sweet.stylebits"; //$NON-NLS-1$

	protected static final String SWT_TYPE_ID = "org.eclipse.swt.SWT"; //$NON-NLS-1$
	
	protected static final String STYLE_NONE = "NONE"; //$NON-NLS-1$
	
	protected static final int STYLE_NOT_SET = -1;
	protected static final Integer STYLE_NOT_SET_INTEGER = new Integer(STYLE_NOT_SET);
	private final static String STYLE_NOT_SET_INITSTRING = String.valueOf(STYLE_NOT_SET);	
	
	static final String[] UNKNOWN = new String[] { "???", "???"}; //$NON-NLS-1$ //$NON-NLS-2$

	public static class StyleBitPropertyDescriptor extends EToolsPropertyDescriptor {

		String[] fNames;

		String[] fInitStrings;

		Number[] fValues;

		private ILabelProvider labelProvider; // Performance cache because property sheets asks for this twice always

		private final SweetStyleBits styleBits;

		public StyleBitPropertyDescriptor(SweetStyleBits styleBits) {
			super(new StyleBitPropertyID(), styleBits.fPropertyName);
			((StyleBitPropertyID) getId()).setPropertyDescriptor(this);
			this.styleBits = styleBits;
			
			// If there is just one value then change this to be "displayname" and add another one with "<not set>"
			// This is for style bits like SWT.BORDER that become called "border" with "BORDER" and "<not set>";
			if (styleBits.fNames.length != 1) {
				fNames = styleBits.fNames;
				fInitStrings = styleBits.fInitStrings;
				fValues = styleBits.fValues;
			} else {
				fNames = new String[] { styleBits.fNames[0], SWTMessages.WidgetPropertySourceAdapter_NotSet}; 
				fInitStrings = new String[] { styleBits.fInitStrings[0], STYLE_NOT_SET_INITSTRING};
				fValues = new Number[] { styleBits.fValues[0], STYLE_NOT_SET_INTEGER};
			}
		}

		public CellEditor createPropertyEditor(Composite parent) {
			return new EnumeratedIntValueCellEditor(parent, fNames, fValues, fInitStrings);
		}

		public ILabelProvider getLabelProvider() {
			if (labelProvider == null) { return new EnumeratedIntValueLabelProvider(fNames, fValues); }
			return labelProvider;
		}

		public String[] getFilterFlags() {
			return styleBits.fIsExpert ? EXPERT_FILTER_FLAGS : null;
		}

		public String getDisplayName() {
			return styleBits.fDisplayName;
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] propertyDescriptors = super.getPropertyDescriptors();
		List descriptorsList = new ArrayList(propertyDescriptors.length + 5);
		for (int i = 0; i < propertyDescriptors.length; i++) {
			descriptorsList.add(propertyDescriptors[i]);
		}
		
		// KLUDGE: We don't want style bits if we are "this" (subclassing) because style bits are contributed
		// by the constructors. And we don't have an allocation statement (i.e. no new ThisClass(parent,style))
		// to generate the style into. So we just remove it.
		// TODO A better way would to do this maybe would be to generate a "private checkStyle()" method that can
		// be called in the ctor's super(parent, checkStyle(style)). And this checkStyle method would take the
		// incoming style and add in the style's selected from the property sheet and return the merged styles.
		// Of course doing this means these styles CAN'T be turned off by users of the class.
		// We also can't deal with style bits on things that are implicitly created, cause these are given to us by 
		// whomever created them and can't be altered as there is no constructor to re-generate
		if (((EObject) getTarget()).eContainmentFeature() != JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart()
			&& !(((IJavaInstance)getTarget()).isImplicitAllocation()))
			mergeStyleBits(descriptorsList, (JavaClass) getBean().getJavaType());
		
		return (IPropertyDescriptor[]) descriptorsList.toArray(new IPropertyDescriptor[descriptorsList.size()]);

	}
	
	/**
	 * Collects the style bits, creates property descriptors for them, and adds them to the argument
	 * 
	 * @param propertyDescriptors
	 *            list into which new property descriptors are added for the constructor style bits
	 * 
	 * @since 1.0.0
	 */
	protected void mergeStyleBits(List propertyDescriptors, JavaClass jClass) {

		StyleBitPropertyDescriptor[] styleDescriptors = getStyleDescriptors(jClass);
		if (styleDescriptors != null) {
			// Merge the style bit property descriptors that have been cached
			for (int i = 0; i < styleDescriptors.length; i++)
				propertyDescriptors.add(styleDescriptors[i]);
		}
	}

	public static class SweetStyleBits {

		public String fPropertyName;

		public String fDisplayName;

		public boolean fIsExpert;

		public String[] fNames;

		public String[] fInitStrings;

		public Number[] fValues;

		public SweetStyleBits(String propertyName, String displayName, boolean isExpert, String[] names, String[] initStrings, Number[] values) {
			fPropertyName = propertyName;
			fDisplayName = displayName;
			fIsExpert = isExpert;
			fNames = names;
			fInitStrings = initStrings;
			fValues = values;
		}
	}

	/**
	 * Get the StyleBitDescriptors for this type.
	 * @param jType
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static StyleBitPropertyDescriptor[] getStyleDescriptors(JavaHelpers jType) {

		FeatureAttributeValue value = BeanUtilities.getSetBeanDecoratorFeatureAttributeValue(jType, SWEET_STYLE_ID);
		// If the value has been previously calculated it is cached so return it
		try {
			if (value != null)
				if (value.getInternalValue() != null)
					return (StyleBitPropertyDescriptor[]) value.getInternalValue();
				else if (value.getValue() != null) {
					// otherwise derive it from the target values
					// There are some style bits picked up by introspection
					// Turn them into an array of IDE style bits that we can use, and from that into property descriptor, and then cache these back onto the FeatureAttribute value so
					// we don't have to re-do this rebuild each time.
					Object[] outerArray = (Object[]) value.getValue();
					StyleBitPropertyDescriptor[] styleDescriptors = new StyleBitPropertyDescriptor[outerArray.length];
					for (int i = 0; i < outerArray.length; i++) {
						Object[] innerArray = (Object[]) outerArray[i];
						// The first element is a String for the internal canonnical name
						String propertyName = (String) innerArray[0];
						// The second element is the user visible name
						String displayName = (String) innerArray[1];
						// The third element is a Boolean value for whether the property is expert or not
						boolean expert = ((Boolean) innerArray[2]).booleanValue();
						// The next is a three element array of name, initString, and actual value * n for the number of allowble values
						// Iterate over it to extract the names and strings and turn these into two separate String arrays
						Object[] triplicateArray = (Object[]) innerArray[3];
						int numberOfValues = triplicateArray.length / 3;
						String[] names = new String[numberOfValues];
						String[] initStrings = new String[numberOfValues];
						Number[] values = new Number[numberOfValues];
						for (int j = 0, index = 0; j < triplicateArray.length; j += 3, ++index) {
							names[index] = (String) triplicateArray[j];
							initStrings[index] = (String) triplicateArray[j + 1];
							values[index] = (Number) triplicateArray[j + 2];
						}
						SweetStyleBits styleBits = new SweetStyleBits(propertyName, displayName, expert, names, initStrings, values);
						styleDescriptors[i] = new StyleBitPropertyDescriptor(styleBits);
					}
					value.setInternalValue(styleDescriptors);
					return styleDescriptors;
				}
		} catch (ClassCastException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
		return null;
	}

	private WidgetProxyAdapter widgetProxyAdapter;

	protected WidgetProxyAdapter getWidgetProxyAdapter() {
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
			return getStyleBitPropertyValue((StyleBitPropertyID)descriptorID);
		} else {
			return "???"; //$NON-NLS-1$
		}
	}

	protected Object getStyleBitPropertyValue(StyleBitPropertyID descriptorID) {
		int currentStyleValue = getWidgetProxyAdapter().getStyle() | getExplicitStyle();
		// The style value represents all the bits together. We must return a single int value the represents the style that is set
		// for the family that this property represents
		Number[] availableValues = descriptorID.propertyDescriptor.fValues;
		for (int i = 0; i < availableValues.length; i++) {
			if ((availableValues[i].intValue() & currentStyleValue) == availableValues[i].intValue()) { return availableValues[i]; }
		}
		return STYLE_NOT_SET_INTEGER;
	}

	public boolean isPropertySet(Object descriptorID) {
		if (descriptorID instanceof EStructuralFeature) {
			return super.isPropertySet(descriptorID);
		} else if (descriptorID instanceof StyleBitPropertyID) {
			// We don't worry about isSet for factory args because we use the 
			int currentValue = ((Number) getPropertyValue(descriptorID)).intValue();
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
			if (getBean().isParseTreeAllocation()) {
				PTExpression styleExpression = getStyleExpression(((ParseTreeAllocation) getBean().getAllocation()).getExpression());
				if (styleExpression != null) {
					try {
						styleBeanProxy = BasicAllocationProcesser.instantiateWithExpression(styleExpression, getWidgetProxyAdapter().getBeanProxyDomain());
						if (styleBeanProxy != null && !(styleBeanProxy instanceof INumberBeanProxy)) {
							styleBeanProxy.getProxyFactoryRegistry().releaseProxy(styleBeanProxy);
							styleBeanProxy = null;	// Not an integer, so an invalid return. Really wasn't the style expression.
						}
					} catch (AllocationException e) {
						SwtPlugin.getDefault().getLogger().log(e.getCause(), Level.INFO);
					}
				}
			}
			explicitStyle = styleBeanProxy != null ? ((INumberBeanProxy) styleBeanProxy).intValue() : SWT.NONE;
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
		} else if (allocationExp instanceof PTMethodInvocation) {
			// Could be a factory.
			Map factoryMap = getFactoryArgumentsMap();
			Integer styleArgIndex = (Integer) factoryMap.get("style");	//$NON-NLS-1$
			return (PTExpression) (styleArgIndex != null ? ((PTMethodInvocation) allocationExp).getArguments().get(styleArgIndex.intValue()) : null);
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
			if (!isFactoryType()) {
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
				int intValue = val != null ? ((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) val)).intValue() : STYLE_NOT_SET;
				// See if we are changing it. If not, then don't do anything. Don't want to signal an unneeded change.	
				if (((Number) getPropertyValue(feature)).intValue() == intValue)
					return; // The property has not changed. Don't do anything.
				JavaAllocation alloc = getBean().getAllocation();
				if (alloc.isParseTree()) {
					// Get the changed allocation. If null, then it means don't change it. This could occur because the expression
					// was not understood, so we couldn't change the style.
					PTExpression newAllocation = getChangedAllocation(((ParseTreeAllocation) alloc).getExpression(), propertyID, intValue);
					if (newAllocation != null) {
						ParseTreeAllocation newParseTreeAlloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
						newParseTreeAlloc.setExpression(newAllocation);
						getBean().setAllocation(newParseTreeAlloc);
					}
				}
			} else {
				int intValue = val != null ? ((INumberBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) val)).intValue() : STYLE_NOT_SET;
				// See if we are changing it. If not, then don't do anything. Don't want to signal an unneeded change.	
				if (((Number) getPropertyValue(feature)).intValue() == intValue) {
					return;	// The property has not changed. Don't do anything.			
				}
				Map factoryMap = getFactoryArgumentsMap();
				Integer argIndex = (Integer) factoryMap.get("style");	//$NON-NLS-1$
				ParseTreeAllocation allocation = (ParseTreeAllocation) getBean().getAllocation();
				PTMethodInvocation methodInvocation = (PTMethodInvocation)allocation.getExpression();
				if (argIndex == null) {
					// See if we can expand to include style. we will use SWT.NONE as the default for the new style setting until we can apply the real setting lower down.
					PTFieldAccess swtNone = InstantiationFactory.eINSTANCE.createPTFieldAccess(InstantiationFactory.eINSTANCE.createPTName(SWT_TYPE_ID), STYLE_NONE);
					int newIndex = expandToIncludeProperty("style", swtNone, methodInvocation); //$NON-NLS-1$
					if (newIndex == COULD_NOT_EXPAND)
						return;	// Couldn't expand it
					else
						argIndex = new Integer(newIndex);	// This is now the arg index. Use it to apply the new bit.
				}
				// Put "null" there as a place-holder so that we don't loose the entry from arg list. This could happen because the
				// current arg(argIndex) could be pulled and put into a new expression. This would then remove the setting from index argIndex.
				// This could accidently collapse the following args into the argIndex arg. By putting the placeholder there we don't loose
				// the spot.
				PTExpression existingStyleBitExpression = (PTExpression) methodInvocation.getArguments().set(argIndex.intValue(), InstantiationFactory.eINSTANCE.createPTNullLiteral());
				PTExpression changedStyleExpression = getChangedStyleExpression(existingStyleBitExpression,propertyID,intValue);
				if (changedStyleExpression == null) {
					// Set go back to no style expression. The default for Widget is SWT.NONE.
					PTName name = InstantiationFactory.eINSTANCE.createPTName(SWT_TYPE_ID);
					changedStyleExpression = InstantiationFactory.eINSTANCE.createPTFieldAccess(name, STYLE_NONE); 
				}
				methodInvocation.getArguments().set(argIndex.intValue(),changedStyleExpression);
				getBean().setAllocation(allocation);  // Set the allocation back into the bean to trigger notification

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
				// Put "null" there as a place-holder so that we don't loose the entry from arg list. This could happen because the
				// current arg(1) could be pulled and put into a new expression. This would then remove the setting from index 1.
				// This could accidently collapse the third arg into the second arg. By putting the placeholder there we don't loose
				// the spot.
				PTExpression currentArg = (PTExpression) classInstanceCreation.getArguments().set(1, InstantiationFactory.eINSTANCE.createPTNullLiteral());
				PTExpression newArg = getChangedStyleExpression(currentArg, propertyID, styleBit);
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