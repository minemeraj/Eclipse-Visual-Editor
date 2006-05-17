/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: ClassDescriptorDecoratorPolicy.java,v $
 *  $Revision: 1.15 $  $Date: 2006-05-17 20:13:53 $ 
 */

import java.util.HashMap;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.*;
import org.eclipse.ve.internal.cde.utility.UtilityFactory;

import org.eclipse.ve.internal.propertysheet.INeedData;

/**
 * This class is used to get access decorators for a specific
 * EClassifier. It will look up the parent chain to find the decorators.
 * This allows for the inheritance of settings. It uses the ClassDecoratorFeatureAccess
 * philosophy to find the decorator. These methods are utility methods.
 * If no decorators that have a setting are found, then the default decorator
 * will be used or the type requested will be found.
 *
 * If a decorator is found, and if the setting in question is not set
 * on that decorator, then an inherited decorator setting will be used
 * instead.
 *
 * It will also allow access to certain common specific settings, such as DisplayName, etc.
 * In those cases it provides some special overrides in that certain settings are not to
 * be overridden.
 * The following settings are not inherited:
 *   a) DisplayName: If there is not a decorator for the class, then there
 *                   is no displayname. That is because displayname of
 *                   subclasses shouldn't be the same as an inherited class.
 *   b) ShortDescription: Same reason as display name.
 *   c) Expert: Same reason as above.
 *   d) Hidden: Same reason as above.
 *
 * There will be an instance of this class stored in the EditDomain under the CLASS_DESCRIPTOR_DECORATOR_POLICY_KEY
 * specified here. There is a static helper method in this class to get the policy from any editpart,
 * or from the edit domain.
 *
 * The reason it is an instance and stored in the EditDomain is because each editor may want to have a different
 * default ClassDescriptorDecorator for classes that don't have a decorator anywhere in their heirarchy.
 */
public class ClassDescriptorDecoratorPolicy {
	private static final String DEFAULT_ICON_PATH = FileLocator.find(CDEPlugin.getPlugin().getBundle(), new Path("images/somepart.gif"), null).toString(); //$NON-NLS-1$
	public static final String CLASS_DESCRIPTOR_DECORATOR_POLICY_KEY = "org.eclipse.ve.internal.cde.core.classdescriptordecoratorpolicykey"; //$NON-NLS-1$

	/**
	 * Helper method to get the policy from an editpart.
	 */
	public static ClassDescriptorDecoratorPolicy getPolicy(EditPart editpart) {
		return getPolicy(EditDomain.getEditDomain(editpart));
	}

	/**
	 * Helper method to get the policy from the editdomain.
	 */
	public static ClassDescriptorDecoratorPolicy getPolicy(EditDomain domain) {
		return (ClassDescriptorDecoratorPolicy) domain.getData(CLASS_DESCRIPTOR_DECORATOR_POLICY_KEY);
	}

	/**
	 * Helper method to set the ClassDescriptorDecoratorPolicy into the EditDomain.
	 */
	public static void setClassDescriptorDecorator(EditDomain domain, ClassDescriptorDecoratorPolicy policy) {
		policy.editDomain = domain;
		domain.setData(CLASS_DESCRIPTOR_DECORATOR_POLICY_KEY, policy);
	}

	public ClassDescriptorDecoratorPolicy() {
		// Create a default ClassDescriptorDecorator. This is the one used if none is found.

		// Initialize a default decorator for the metaclass that
		// will be used if a decorator is not specified for a specific class
		ClassDescriptorDecorator classDecor = DecoratorsFactory.eINSTANCE.createClassDescriptorDecorator();
		classDecor.setGraphViewClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.emf.DefaultGraphicalEditPart"); //$NON-NLS-1$
		classDecor.setTreeViewClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.emf.DefaultTreeEditPart"); //$NON-NLS-1$
		classDecor.setGraphic(UtilityFactory.eINSTANCE.createGIFFileGraphic(DEFAULT_ICON_PATH));	
		classDecor.setLabelProviderClassname("org.eclipse.ve.cde/org.eclipse.ve.internal.cde.emf.DefaultLabelProvider"); //$NON-NLS-1$
		registerDefaultDecorator(ClassDescriptorDecorator.class, classDecor);
	}

	public ClassDescriptorDecoratorPolicy(ClassDescriptorDecorator defaultDecorator) {
		registerDefaultDecorator(ClassDescriptorDecorator.class, defaultDecorator);
	}

	private EditDomain editDomain;
	private HashMap defaultDecorators = new HashMap(3);
	public void registerDefaultDecorator(Class decClass, EAnnotation defaultDecor) {
		defaultDecorators.put(decClass, defaultDecor);
	}

	public EAnnotation getDefaultDecorator(Class decClass) {
		return (EAnnotation) defaultDecorators.get(decClass);
	}

	/**
	 * Find the decorator for this class only. Do not handle inheritance.
	 */
	protected ClassDescriptorDecorator findDecorator(EClassifier eClass) {
		return (ClassDescriptorDecorator) CDEUtilities.findDecorator(eClass, ClassDescriptorDecorator.class);
	}

	/**
	 * Find the decorator of the type requested with the feature set for this class,
	 * return default of that type if none found.
	 */
	public EAnnotation findDecorator(EClassifier eClass, Class decoratorType, EStructuralFeature feature) {
		EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithFeature(eClass, decoratorType, feature);
		return (decr != null) ? decr : getDefaultDecorator(decoratorType);
	}

	/**
	 * Find the decorator of the type requested with the KeyedValue key set for this class,
	 * return default of that type if none found.
	 */
	public EAnnotation findDecorator(EClassifier eClass, Class decoratorType, String key) {
		EAnnotation decr = ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(eClass, decoratorType, key);
		return (decr != null) ? decr : getDefaultDecorator(decoratorType);
	}

	/**
	 * Return the customizer class. This is an inheritable setting.
	 */
	public String getCustomizerClassname(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_CustomizerClassname());
		return decr.getCustomizerClassname();
	}

	/**
	 * Return the default palette. This is an inheritable setting.
	 */
	public String getDefaultPalette(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_DefaultPalette());
		return decr.getDefaultPalette();
	}

	/**
	 * Return the display name. This is not an inheritable setting, so default won't be used.
	 */
	public String getDisplayName(EClassifier eClass) {
		ClassDescriptorDecorator decor = findDecorator(eClass);
		return (decor != null && decor.getDisplayNameString() != null) ? decor.getDisplayNameString().getStringValue() : null;
	}

	/**
	 * Return the short description. This is not an inheritable setting, so default won't be used.
	 */
	public String getDescription(EClassifier eClass) {
		ClassDescriptorDecorator decor = findDecorator(eClass);
		return (decor != null && decor.getDescriptionString() != null) ? decor.getDescriptionString().getStringValue() : null;
	}

	/**
	 * Return the graph view class. This is an inheritable setting.
	 */
	public String getGraphViewClassname(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_GraphViewClassname());
		return decr.getGraphViewClassname();
	}

	/**
	 * Return the model adapter class. This is an inheritable setting.
	 */
	public String getModelAdapterClassname(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_ModelAdapterClassname());
		return decr.getModelAdapterClassname();
	}

	/**
	 * Return the icon. This is an inheritable setting.
	 */
	public IGraphic getIcon(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_Graphic());
		return decr.getGraphic();
	}

	/**
	 * Return the tree view class. This is an inheritable setting.
	 */
	public String getTreeViewClassname(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_TreeViewClassname());
		return decr.getTreeViewClassname();
	}
	/**
	 * Return the expert setting. This is not an inheritable setting, so default won't be used.
	 */
	public boolean isExpert(EClassifier eClass) {
		ClassDescriptorDecorator decor = findDecorator(eClass);
		return (decor != null) ? decor.isFiltered(IPropertySheetEntry.FILTER_ID_EXPERT) : false;
	}
	/**
	 * Return the hidden setting. This is not an inheritable setting, so default won't be used.
	 */
	public boolean isHidden(EClassifier eClass) {
		ClassDescriptorDecorator decor = findDecorator(eClass);
		return (decor != null) ? decor.isHidden() : false;
	}

	/**
	 * Return the labelProvider class. This is an inheritable setting.
	 */
	public String getLabelProviderClassname(EClassifier eClass) {
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_LabelProviderClassname());
		return decr.getLabelProviderClassname();
	}

	/**
	 * Return the labelProvider itself. 
	 */
	public ILabelProvider getLabelProvider(EClassifier eClass) {
		
		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(eClass,
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_LabelProviderClassname());
		if(decr != null){
			ILabelProvider result = decr.getLabelProvider();
			if (result instanceof INeedData)
				((INeedData) result).setData(editDomain);
			return result;
		}
		return null;		
	}

	/**
	 * @param object
	 * @return An instantiated Graphical edit part for the argument
	 */
	public GraphicalEditPart createGraphicalEditPart(EObject object) {

		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(object.eClass(),
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_GraphViewClassname());
		
		return decr != null ? decr.createGraphicalEditPart(object) : null;
	}
	
	/**
	 * @param object
	 * @return An instantiated Tree edit part for the argument
	 */
	public TreeEditPart createTreeEditPart(EObject object) {

		ClassDescriptorDecorator decr =
			(ClassDescriptorDecorator) findDecorator(object.eClass(),
				ClassDescriptorDecorator.class,
				DecoratorsPackage.eINSTANCE.getClassDescriptorDecorator_TreeViewClassname());
		
		return decr != null ? decr.createTreeEditPart(object) : null;
	}	
}
