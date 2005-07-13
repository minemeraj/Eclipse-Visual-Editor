/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
 *  $RCSfile: JavaBeanActionFilter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-07-13 13:21:04 $ 
 */
import org.eclipse.emf.ecore.*;
import org.eclipse.gef.EditPart;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
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
 */
public class JavaBeanActionFilter extends CDEActionFilter {
	
	public static final JavaBeanActionFilter INSTANCE = new JavaBeanActionFilter();	// Only one is needed. All the info it needs comes from the input.
	
	public static final String 
		BEAN_TYPE_STRING = "BEANTYPE"; //$NON-NLS-1$
	
	/*
	 * Protected so that only singleton INSTANCE, or a subclass can instantiate it.
	 */	
	protected JavaBeanActionFilter() {
	}

	public boolean testAttributeForPropertyName(Object target, String value) {
		// The "text" property can be present because the edit part's parent is a TabFolder
		// which allows direct edit of the TabItem text
		if("text".equals(value)){
			if(isTabFolderParent(target)) return true;
		}
		
		return super.testAttributeForPropertyName(target,value);
	}
	
	public static boolean isTabFolderParent(Object target){
		Object parentModel = ((EditPart)target).getParent().getModel();
		if(parentModel instanceof IJavaObjectInstance && 
		   ((IJavaObjectInstance)parentModel).getJavaType().getQualifiedName().equals("org.eclipse.swt.widgets.TabFolder")){
			return true;
		}
		return false;
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
		}
		
		// Pass this test up to the parent CDEActionFilter
		return super.testAttribute(target, name, value);
	}
}
