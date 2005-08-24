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
 *  $RCSfile: MemberJavaBeanCellEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.propertysheet.*;

/**
 */
public class MemberJavaBeanCellEditor extends ObjectComboBoxCellEditor implements INeedData , ISourced , IExecutableExtension {
	
	protected EditDomain editDomain;
	protected String className; // The name of the class that we will show all occurences of on the free form
	protected BeanSubclassComposition beanComposition; // The top level object ( the free form )
	protected List javaObjects; // Store the java objects on the free form that match the search class
	protected List javaObjectLabels;
	
public MemberJavaBeanCellEditor(Composite aComposite){
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
public void setInitializationData(IConfigurationElement element, String data, Object object){
	// Use this to set the class name passed in
	if ( object instanceof String ) {
		className = (String)object;
	}
}
public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors){
	// Find the Composition ( the free form surface top level object )
	EObject firstSource = (EObject)sources[0];
	beanComposition = (BeanSubclassComposition) EcoreUtil.getObjectByType(firstSource.eResource().getContents(), JCMPackage.eINSTANCE.getBeanSubclassComposition());		
	// Having got the root object we can now ask it for all of its child objects that match the desired type we are supposed to list
	javaObjects = new ArrayList();
	javaObjectLabels = new ArrayList();
	Iterator components = beanComposition.getMembers().iterator();
	// To do comparisons that allow for inheritance we need to find the EMF JavaClass that represents the class we are searching for
	JavaClass javaClass = Utilities.getJavaClass(className,firstSource.eResource().getResourceSet());
	while(components.hasNext()){
		Object component = components.next();
		if ( component instanceof IJavaObjectInstance ) { // We might have non java components 
			// Test the class
			IJavaObjectInstance javaComponent = (IJavaObjectInstance) component;
			JavaClass componentClass = (JavaClass) javaComponent.getJavaType();
			if ( javaClass.isAssignableFrom(componentClass)) {
				javaObjects.add(component);	
				// The label used for the icon is the same one used by the JavaBeans tree view
				ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(componentClass);
				if ( labelProvider != null ) {
					javaObjectLabels.add(labelProvider.getText(component));		
				} else { 
					// If no label provider exists use the toString of the target VM JavaBean itself
					javaObjectLabels.add(BeanProxyUtilities.getBeanProxy(javaComponent).toBeanString()); 
				}
			}
		}
	}
	
	// TODO: Figure out local variables as well
	
	
	// Now we know the children set the items
	String[] listItems = new String[javaObjectLabels.size()];
	System.arraycopy(javaObjectLabels.toArray(),0,listItems,0,listItems.length);
	setItems(listItems);
}
}
