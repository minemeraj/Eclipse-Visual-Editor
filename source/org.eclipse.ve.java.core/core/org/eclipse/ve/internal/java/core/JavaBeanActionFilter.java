package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaBeanActionFilter.java,v $
 *  $Revision: 1.4 $  $Date: 2004-05-26 18:23:30 $ 
 */
import org.eclipse.emf.ecore.*;
import org.eclipse.gef.EditPart;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.CDEActionFilter;
import org.eclipse.ve.internal.cde.core.EditDomain;

/**
 * This action filter is used by EditParts that implement IJavaBeanEditPart
 * to filter out objects that meet the criteria specified in the plugin
 * extension point="org.eclipse.ui.popupMenus". This enables context menu capability
 * for those editparts that contain models that meet this criteria.
 * 
 * Editparts that wish to use the filter need to implement IAdaptable and in the 
 * getAdapter(Class type) method, return this filter for the type IAdapter.class.  
 * 
 * As an example, one could specify an object constribution action to a javax.swing.JTabbedPane that
 * would be on a popup only for JTabbedPane. This action would select the next page in the tabbed pane.
 * We could also add a menu in which to add the object contribution. The popup looks like "Switch to-->Next".
 * Here's how the xmi would look in the plugin.xml:
 * 
 *<extension point="org.eclipse.ui.popupMenus"> 
 * 	<objectContribution 
 *		id="org.eclipse.ve.internal.java.core.editorpart.jtabbedpane.action.popup.object"
 *		objectClass="org.eclipse.ve.internal.java.core.visual.JTabbedPaneGraphicalEditPart">
 *		<visibility> 
 *			<and> 
 *				<objectState name="BEANTYPE" value="javax.swing.JTabbedPane"/> 
 *			</and> 
 *		</visibility> 
 *		<menu
 *			id="jtabbedpaneMenu"
 *			path="additions"
 *			label="Switch to">
 *			<separator name="jtabbedpaneActionsGroup"/>
 *		</menu>
 *		<action
 *			id="org.eclipse.ve.internal.java.core.editorpart.jtabbedpane.nextaction" 
 *			label="Next"
 *			menubarPath="jtabbedpaneMenu/jtabbedpaneActionsGroup"
 *			class="org.eclipse.ve.internal.java.core.editorpart.JTabbedPaneNextObjectActionDelegate"> 
 *		</action> 
 *	</objectContribution> 
 * </extension>
 *
 * The valid tests are: 
 *   (name)        - (value)
 *   BEANTYPE      - "" means the model of the EditPart is any type of bean, IJavaInstance
 *                   "class" means the model of the EditPart can be assigned to this type (through the JavaModel (EMF), not straight Java).
 *   PROPERTY      - "nameOfFeature" tests if the given feature is an available property of the model of the editpart. (Model needs to be EMF, does not need to be JavaModel).
 */
public class JavaBeanActionFilter extends CDEActionFilter {
	
	public static final JavaBeanActionFilter INSTANCE = new JavaBeanActionFilter();	// Only one is needed. All the info it needs comes from the input.
	
	public static final String 
		BEAN_TYPE_STRING = "BEANTYPE", //$NON-NLS-1$
		PROPERTY_STRING = "PROPERTY"; //$NON-NLS-1$

	
	/*
	 * Protected so that only singleton INSTANCE, or a subclass can instantiate it.
	 */	
	protected JavaBeanActionFilter() {
	}

	/**
	 * @see org.eclipse.ui.IActionFilter#testAttribute(Object, String, String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (!(target instanceof EditPart))	//Can only handle edit parts
			return false;
			
		if (name.equals(BEAN_TYPE_STRING) && ((EditPart) target).getModel() instanceof IJavaInstance) {
			// Really don't like fluffing it up. But that is the best way because isInstance tests in java model land are complicated.
			// Besides it only will fluff it up once per project and find that it is invalid only once. So following tests will be quicker.
			EClassifier type = JavaRefFactory.eINSTANCE.reflectType(value, JavaEditDomainHelper.getResourceSet(EditDomain.getEditDomain((EditPart)target)));
			if (type != null)
				return type.isInstance(((EditPart) target).getModel());
		} else if (name.equals(PROPERTY_STRING)) {
			// This allows an extension so that a popup action could be provided if a component
			// had a specific property with its name equal to 'value'.			
			return testAttributeForPropertyName(target, value);
		}
		
		// Pass this test up to the parent CDEActionFilter
		return super.testAttribute(target, name, value);
	}
	

	
	/**
	 * Return true if this target bean has a structural feature name equal to 'value'
	 */
	public boolean testAttributeForPropertyName(Object target, String value) {
		if (((EditPart) target).getModel() != null) {
			JavaClass modelType = (JavaClass) (((EObject)((EditPart) target).getModel())).eClass();
			EStructuralFeature textSF = modelType.getEStructuralFeature(value);
			if (textSF != null)
				return true;
		}
		return false;
	}
}
