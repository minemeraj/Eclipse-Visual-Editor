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
package org.eclipse.ve.internal.java.core;

/*
 *  $RCSfile: BeanPropertyDescriptorAdapter.java,v $
 *  $Revision: 1.24 $  $Date: 2005-10-06 15:18:33 $ 
 */ 
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;
import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.common.FeatureAttributeValue;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.util.logger.proxy.Logger;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.decorators.BasePropertyDecorator;
import org.eclipse.ve.internal.cde.decorators.DecoratorsPackage;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.properties.AbstractPropertyDescriptorAdapter;

import org.eclipse.ve.internal.java.vce.JavaBeanLabelProvider;

import org.eclipse.ve.internal.propertysheet.DefaultWrapperedValidator;
import org.eclipse.ve.internal.propertysheet.IEToolsPropertyDescriptor;


/**
 * The creation method for BeanProperties is different than for regular MOF properties
 * The order is
 * 	1	-	Look for a CellEditor described directly on the EMF feature
 *	2	-	Look for a java.beans.PropertyEditor described on the Beans PropertyDescriptor
 *	3	-	Look for any enumerated values on the BeanInfo.  If so use a special property editor that can handle these
 *	4	-	Look for an CellEditor described on the EMF type
 */
public class BeanPropertyDescriptorAdapter extends AbstractPropertyDescriptorAdapter implements IEToolsPropertyDescriptor {
	
	public static final Class BEAN_PROPERTY_DESCRIPTOR_TYPE = BeanPropertyDescriptorAdapter.class;
	protected Class editorClass , labelProviderClass; // The editor class is cache'd to save repeated calculation - The instance however cannot be as this involves
	private String editorClassNameAndData;
	private String labelProviderClassNameAndData;
	private ILabelProvider labelProvider;
	private boolean noLabelProviderExplicit = false;
	
public CellEditor createPropertyEditor(Composite parent){
	
	// If we already have an editor class then create an instance and return it
	if ( editorClass != null )
		return createCellEditorInstance(editorClass,parent, editorClassNameAndData, null);
	else if ("".equals(editorClassNameAndData)) //$NON-NLS-1$
		return null;	// Specifically requested no cell editor
	
	if (!((EStructuralFeature) getTarget()).isChangeable())
		return null;	// The feature is a read-only, so no editor to allow changes
	
	PropertyDecorator propertyDecorator = null;  // Decorator holding java.beans.PropertyDescriptor information from the BeanInfo
	
	// Step 1 - See if the emf feature decorator has it.
	BasePropertyDecorator decorator = getBaseDecorator();
	if (decorator != null && decorator.isSetCellEditorClassname()) {
		try {
			String classNameAndData = decorator.getCellEditorClassname();
			if (classNameAndData == null) {
				editorClassNameAndData = ""; //$NON-NLS-1$
				return null;	// It is explicitly set to no cell editor for this type.
			}
			editorClass = CDEPlugin.getClassFromString(classNameAndData);
			editorClassNameAndData = classNameAndData;	// Set here so that if class not found, this left null
		} catch (ClassNotFoundException e) {
			// One specified, but incorrect, log it, but continue and see if we can get another way.
			Logger logger = JavaVEPlugin.getPlugin().getLogger();
			if (logger.isLoggingLevel(Level.WARNING))
					logger.log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		}
	}
	
	//	2 - Look for a java.beans.PropertyEditor on the Bean PropertyDescriptor
	if ( editorClass == null ) {
		propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
		if ( propertyDecorator != null ) {
			JavaClass propertyEditorClass = propertyDecorator.getPropertyEditorClass();
			// If we have a property editor class explicitly defined on the feature then instantiate it
			if ( propertyEditorClass != null ) {				
				return new BeanFeatureEditor(
				(JavaHelpers) ((EStructuralFeature)getTarget()).getEType(),
				parent,
				propertyEditorClass.getQualifiedNameForReflection());
			}
			// 3	-	VAJava had a wierd feature ( that we have to support for backward compatiliby )
			// where you could get enumerated values code generated for you
			// These were stored with a key of ENUMERATIONVALUES and were an array
			// that was arranged in a tiplicate repeating pattern of displayName, value, initString
			// e.g. "On" , Boolean.TRUE , "Boolean.TRUE" , "Off" , Boolean.FALSE , "Boolean.FALSE"
			// or	 "Vertical" , new Integer(0) , "java.awt.Scrollbar.HORIZONTAL" , "Horizontal" , new Integer(1) , "java.awt.Scrollbar.VERTICAL"
			FeatureAttributeValue featureValue = (FeatureAttributeValue)propertyDecorator.getAttributes().get(IBaseBeanInfoConstants.ENUMERATIONVALUES);
			if (featureValue != null){
				return new BeanPropertyEnumeratedCellEditor(
					parent,
					(Object[]) featureValue.getValue(),
					(JavaHelpers) ((EStructuralFeature)target).getEType());
			}			
		}
	}
		
	// Step 4 - If not on feature, then get it from the type, look for BasePropertyDecorator.
		if ( editorClass == null ) {
			BasePropertyDecorator bdec = (BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(((EStructuralFeature) target).getEType(), BasePropertyDecorator.class, DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_CellEditorClassname());
			if (bdec != null) {
				try {
					String classNameAndData = bdec.getCellEditorClassname();
					editorClass = CDEPlugin.getClassFromString(classNameAndData);
					editorClassNameAndData = classNameAndData;	// Set here so that left unset if class not found.
				} catch (ClassNotFoundException e) {
					// One specified, but incorrect, log it, but continue and see if we can get another way.
					Logger logger = JavaVEPlugin.getPlugin().getLogger();
					if (logger.isLoggingLevel(Level.WARNING))
						logger.log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e), Level.WARNING); //$NON-NLS-1$
				}
			}
				
		}
		
	if ( editorClass != null ) {
		return createCellEditorInstance(editorClass,parent, editorClassNameAndData, null);
	} else {
		if (propertyDecorator != null) {
			// TODO - should we always do this, or just for interfaces ?
			return new TypeReferenceCellEditor((JavaHelpers) propertyDecorator.getPropertyType(), parent);
		} else
			return null;
	}
}
	
private static String[] EXPERT_FILTER_FLAGS = new String[] {IPropertySheetEntry.FILTER_ID_EXPERT};
public String[] getFilterFlags(){
	// Property sheet only worries about expert, so we will see if we are expert, and if so return that.
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
	return (propertyDecorator != null && propertyDecorator.isExpert()) ? EXPERT_FILTER_FLAGS : null;
}

public ICellEditorValidator getValidator() {

	// Step 1 - See if the base decorator on the feature has it.
	BasePropertyDecorator decorator = getBaseDecorator();
	if (decorator != null && !decorator.getCellEditorValidatorClassnames().isEmpty()) {
		try {
			ICellEditorValidator[] validators = null;
			if (!decorator.getCellEditorValidatorClassnames().isEmpty()) {
				validators = new ICellEditorValidator[decorator.getCellEditorValidatorClassnames().size()];
				Iterator itr = decorator.getCellEditorValidatorClassnames().iterator();
				for (int i = 0; itr.hasNext(); i++)
					validators[i] = (ICellEditorValidator) CDEPlugin.createInstance(null, (String) itr.next());
			}

			return (validators.length == 1 ? validators[0] : new DefaultWrapperedValidator(validators));
		} catch (Exception e) {
			String msg =
				MessageFormat.format(
					CDEMessages.Object_noinstantiate_EXC_, 
					new Object[] { decorator.getCellEditorValidatorClassnames()});
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		}
	}

	// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator on the type.
	BasePropertyDecorator bdec =
		(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
			((EStructuralFeature) target).getEType(),
			BasePropertyDecorator.class,
			DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_CellEditorValidatorClassnames());
	if (bdec != null && !bdec.getCellEditorValidatorClassnames().isEmpty()) {
		try {
			ICellEditorValidator[] validators = null;
			if (!bdec.getCellEditorValidatorClassnames().isEmpty()) {
				validators = new ICellEditorValidator[bdec.getCellEditorValidatorClassnames().size()];
				Iterator itr = bdec.getCellEditorValidatorClassnames().iterator();
				for (int i = 0; itr.hasNext(); i++)
					validators[i] = (ICellEditorValidator) CDEPlugin.createInstance(null, (String) itr.next());
			}

			return (validators.length == 1 ? validators[0] : new DefaultWrapperedValidator(validators));
		} catch (Exception e) {
			String msg =
				MessageFormat.format(
					CDEMessages.Object_noinstantiate_EXC_, 
					new Object[] { bdec.getCellEditorValidatorClassnames()});
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
		}
	}

	return null;
}


public Object getHelpContextIds(){
	return null;
}
/**
 * 1	-	If the feature has a LabelProvider then we should use this.
 * 2	- 	See whether a java.beans.PropertyEditor exists for the feature, if so
 * 			BeanCellRenderer which is able to wrapper the PropertyEditor to get the string
 * 3	-	See whether there are any enumeration values on from the BeanInfo.  If so use
 * 			a special label provider that knows how to handle these
 * 4	-	See if a LabelProvider exists for the JavaHelpers that can handle this particular type
 *			such as an editor that can handle IStringBeanProxy or IDimensionBeanProxy without 
 * 			going over to the target VM
 * 5	-	Just a BeanCellRenderer. It will do a toBeanString.
 */
public ILabelProvider getLabelProvider(){
	
	if(labelProvider != null) 
		return labelProvider;
	else if (noLabelProviderExplicit)
		return null;
	
	if ( labelProviderClass != null ) {
		labelProvider = createLabelProviderInstance(labelProviderClass, labelProviderClassNameAndData, null, this);
		return labelProvider;
	} else if ("".equals(labelProviderClassNameAndData)) { //$NON-NLS-1$
		noLabelProviderExplicit = true;
		return null;	// Explicitly said no label provider
	}
		
	// Step 1 - See if the emf feature decorator has a label provider.
	BasePropertyDecorator decorator = getBaseDecorator();
	if (decorator != null && decorator.isSetLabelProviderClassname()) {
		try {
			String classNameAndData = decorator.getLabelProviderClassname();
			if (classNameAndData == null) {
				labelProviderClassNameAndData = ""; //$NON-NLS-1$
				return null;	// It is explicitly set to no label provider.
			}
			labelProviderClass = CDEPlugin.getClassFromString(classNameAndData);
			labelProviderClassNameAndData = classNameAndData;	// Set here so that it stays unset if class not found
		} catch (ClassNotFoundException e) {
			// One specified, but incorrect, log it, but continue and see if we can get another way.
			JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e), Level.WARNING); //$NON-NLS-1$
		}
	}
	
	// Step 2 - See if there is a java.beans.PropertyEditor on the decorator that holds information
	//			about the introspected java.beans.PropertyDescriptor
	if ( labelProviderClass == null ) {
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
		if ( propertyDecorator != null ) {
			JavaClass propertyEditorClass = propertyDecorator.getPropertyEditorClass();
			// If we have a property editor class explicitly defined on the feature then instantiate it
			if ( propertyEditorClass != null ) {
				// java.beans.PropertyEditorManager does not allow property editors to be de-registered
				// Sun provide a number of defaults that we de-register by placing a placeholder 
				// DummyPropertyEditor there that we must detect here
				return labelProvider = new BeanCellRenderer(propertyEditorClass.getQualifiedNameForReflection());
			}
			// 3	-	Look for the enumeration values
			FeatureAttributeValue featureValue = (FeatureAttributeValue)propertyDecorator.getAttributes().get(IBaseBeanInfoConstants.ENUMERATIONVALUES);
			if (featureValue != null) {
				return labelProvider = new EnumeratedLabelProvider(
							(Object[])featureValue.getValue(),
							(JavaHelpers) ((EStructuralFeature)target).getEType());
			}
		}			
	}
	
	// Step 4 - See if there is a label provider on the decorator for the class on its BasePropertyDecorator
	if ( labelProviderClass == null ) {
		BasePropertyDecorator bdec = (BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(((EStructuralFeature) target).getEType(), BasePropertyDecorator.class, DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_LabelProviderClassname());
		if (bdec != null) {
			try {
				String classNameAndData = bdec.getLabelProviderClassname();
				labelProviderClass = CDEPlugin.getClassFromString(classNameAndData);
				labelProviderClassNameAndData = classNameAndData;	// Set here so that it stays unset if class not found
			} catch (ClassNotFoundException e) {
				// One specified, but incorrect, log it, but continue and see if we can get another way.
				JavaVEPlugin.getPlugin().getLogger().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e), Level.WARNING); //$NON-NLS-1$
			}
		}
	}	
	
	if ( labelProviderClass != null ) {
		return labelProvider = createLabelProviderInstance(labelProviderClass, labelProviderClassNameAndData, null, this);
	} else {
		// Step 5 - Default label provider uses the label provider from the value itself
		// (which is what is going to be used by the JavaBeans tree viewer) and if no provider exists the toString()
		return labelProvider = new JavaBeanLabelProvider();
	}
}
public boolean isExpandable() {
	// Step 1 - See if the feature decorator has it.
	BasePropertyDecorator decorator = getBaseDecorator();
	if (decorator != null && decorator.isSetEntryExpandable())
		return decorator.isEntryExpandable();				
			
	// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator.
	BasePropertyDecorator bdec = (BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(((EStructuralFeature) target).getEType(), BasePropertyDecorator.class, DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_EntryExpandable());
	if (bdec != null)
		return bdec.isEntryExpandable();				
				
	return true;	
}
	
public boolean areNullsInvalid() {

	// Step 1 - See if the feature decorator has it.
	BasePropertyDecorator decorator = getBaseDecorator();
	if (decorator != null && decorator.isSetNullInvalid())
		return decorator.isNullInvalid();				
			
	// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator.
	BasePropertyDecorator bdec = (BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(((EStructuralFeature) target).getEType(), BasePropertyDecorator.class, DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_NullInvalid());
	if (bdec != null)
		return bdec.isNullInvalid();
				
	return false;	
}

public String getCategory(){
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
	return (propertyDecorator != null) ? propertyDecorator.getCategory() : null;
}

public String getDescription(){
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
	return (propertyDecorator != null) ? propertyDecorator.getShortDescription() : null;
}

public boolean isReadOnly(){
	return !(((EStructuralFeature) getTarget()).isChangeable());
}

public String primGetDisplayName(){
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)target);
	if ( propertyDecorator != null) {
		if (propertyDecorator.getDisplayName() != null){
			return propertyDecorator.getDisplayName();
		}
	}
	return null;

}
protected EStructuralFeature getFeature(){
	return (EStructuralFeature)getTarget();
}
protected BasePropertyDecorator getBaseDecorator() {
	// Return the BasePropertyDecorator for this feature.
	return (BasePropertyDecorator) CDEUtilities.findDecorator((EModelElement) target, BasePropertyDecorator.class);
}
/**
 * instantiate the  class passed in. If it has an ctor that
 * takes a JavaHelpers, use that one.
 * A CellEditor must be constructed with a Composite
 */
protected CellEditor createCellEditorInstance(Class clazz,Composite aComposite, String data, Object initData) {
	Constructor ctor = null;
	CellEditor editor = null;
	try {
		ctor = clazz.getConstructor(new Class[] {JavaHelpers.class,Composite.class});
	} catch (NoSuchMethodException e) {
	};
	try {
		if (ctor != null) {
			editor = (CellEditor) ctor.newInstance(new Object[] {
				(JavaHelpers) ((EStructuralFeature)getTarget()).getEType(),
				aComposite});
		} else {
			ctor = clazz.getConstructor(new Class[] {Composite.class});
			editor = (CellEditor) ctor.newInstance(new Object[] {aComposite});
		}
		
		CDEPlugin.setInitializationData(editor, data, initData);
		ICellEditorValidator validator = getValidator();
		if (validator != null)
			editor.setValidator(validator);
	} catch (Exception exc) {
		Logger logger = JavaVEPlugin.getPlugin().getLogger();
		if (logger.isLoggingLevel(Level.WARNING)) {
			String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { clazz}); 
			logger.log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, exc), Level.WARNING);
		}
	}
	return editor;
}
}
