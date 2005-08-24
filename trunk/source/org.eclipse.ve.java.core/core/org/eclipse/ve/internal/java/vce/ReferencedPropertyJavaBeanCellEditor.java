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
package org.eclipse.ve.internal.java.vce;
/*
 *  $RCSfile: ReferencedPropertyJavaBeanCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourced;
import org.eclipse.ve.internal.propertysheet.ObjectComboBoxCellEditor;

/**
 * A Java Bean cell editor which lists all beans of the speficied type which has the 
 * currently selected bean set for the specified value.
 * 
 * For example, this could be used by a CheckboxGroup to determine which Checkboxes have it
 * set as their group
 */
public class ReferencedPropertyJavaBeanCellEditor extends ObjectComboBoxCellEditor implements INeedData , ISourced , IExecutableExtension {
	
	protected EditDomain editDomain;
	protected String className; // The name of the class that we will check the property value for
	protected String propertyName; // The property to check for reference to current bean
	protected BeanSubclassComposition beanComposition; // The top level object ( the free form )
	protected List javaObjects; // Store the java objects  that match the search class and property
	protected List javaObjectLabels;
	
public ReferencedPropertyJavaBeanCellEditor(Composite aComposite){
	super(aComposite);
}

protected String isCorrectObject(Object value) {
	return null;
}

protected Object doGetObject(int index) {
	return javaObjects.get(index);
}
	
protected int doGetIndex(Object value) {
	return javaObjects.indexOf(value);
}
public void setData(Object anObject){
	editDomain = (EditDomain) anObject;
}

/**
 * The first two parameters are ignored, the third specifies the target class name and target property, comma deliminated
 */
public void setInitializationData(IConfigurationElement element, String data, Object object){
	// Use this to set the class name passed in
	if ( object instanceof String ) {
		String temp = (String)object;
		int index = temp.indexOf(',');
		if ( index != -1 ){
			className = temp.substring(0,index);
			propertyName = temp.substring(index + 1);
		}
	}
}
public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors){
	EObject firstSource = (EObject)sources[0];
		
	javaObjects = new ArrayList();
	javaObjectLabels = new ArrayList();
	// To do comparisons that allow for inheritance we need to find the EMF JavaClass that represents the class we are searching for
	JavaClass javaClass = Utilities.getJavaClass(className,firstSource.eResource().getResourceSet());
	EReference propSf = (EReference) javaClass.getEStructuralFeature(propertyName);
	EObject[] comps = InverseMaintenanceAdapter.getReferencedBy(firstSource, propSf);
	for ( int i = 0; i < comps.length; i++ ) {
		EObject component = comps[i];
		if (component instanceof IJavaObjectInstance) {
			// We might have non java components 
			IJavaObjectInstance javaComponent = (IJavaObjectInstance) component;
			JavaClass componentClass = (JavaClass) javaComponent.getJavaType();
			javaObjects.add(component);
			// The label used for the icon is the same one used by the JavaBeans tree view
			ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentClass);
			if (labelProvider != null) {
				javaObjectLabels.add(labelProvider.getText(component));
			} else {
				// If no label provider exists use the toString of the target VM JavaBean itself
				javaObjectLabels.add(BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString());
			}
		}
	}
	
	// Now we know the children set the items
	String[] listItems = new String[javaObjectLabels.size()];
	System.arraycopy(javaObjectLabels.toArray(),0,listItems,0,listItems.length);
	setItems(listItems);
}
}
