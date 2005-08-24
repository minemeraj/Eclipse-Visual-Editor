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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: FreeFormJavaBeanComponentCellEditor.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.propertysheet.*;

/**
 * TODO 
 * - Right now this assumes the container/components relationship to find components and the inverse to go from a component to its top level container on the free form
 *   This doesn't hold true for JTabbedPane, JSplitPane, etc...  
 * - Don't let a class tab to itself ( this is just silly from a user point of view ) 
 */
public class FreeFormJavaBeanComponentCellEditor extends ObjectComboBoxCellEditor implements INeedData , ISourced  {
	
	protected EditDomain editDomain;
	protected BeanSubclassComposition beanComposition; // The top level object ( the free form )
	protected List javaObjects; // Store the java objects on the free form that match the search class
	protected List javaObjectLabels;
	protected JavaClass componentClass;
	protected EStructuralFeature sf_container_components , sf_conmponent_parent , sf_constraint_component;
	
	
public FreeFormJavaBeanComponentCellEditor(Composite aComposite){
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
public void setSources(Object[] sources, IPropertySource[] propertySources, IPropertyDescriptor[] descriptors){
	javaObjects = new ArrayList();  // A list of components found
	javaObjectLabels = new ArrayList(); // and their labels
	// Find the top most parent contaer of the source
	// This is because you can only tab to someone on the same overall window as you - if you imagine two JPanels on the
	// free form it doesn't make sense to tab across them
	EObject firstSource = (EObject)sources[0];
	ResourceSet rset = firstSource.eResource().getResourceSet();
	// Now we have a resource set we can get some meta information
	sf_container_components = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONTAINER_COMPONENTS);					
	sf_constraint_component = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
	componentClass = Utilities.getJavaClass("java.awt.Component",rset);	 //$NON-NLS-1$
	// walk the chain of a component's parent until we get none
	// This needs improving as we are assuming here that the component is contained by its container, which works for container/components
	// but may not work for other relationships
	EObject componentParent = firstSource.eContainer();	
	EObject lastComponent = firstSource;
	while( !(componentParent instanceof BeanSubclassComposition) ){
		lastComponent = componentParent;		
		componentParent = componentParent.eContainer();
	}
	
	processComponent((IJavaObjectInstance)lastComponent,javaObjects,javaObjectLabels);

	// Now we know the children set the items
	String[] listItems = new String[javaObjectLabels.size()];
	System.arraycopy(javaObjectLabels.toArray(),0,listItems,0,listItems.length);
	setItems(listItems);
}
protected void processComponent(IJavaObjectInstance component, List javaObjects, List javaObjectLabels){
	javaObjects.add(component);	
	// The label used for the icon is the same one used by the JavaBeans tree view
	ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(component.getJavaType());
	if ( labelProvider != null ) {
		javaObjectLabels.add(labelProvider.getText(component));		
	} else { 
		// If no label provider exists use the toString of the target VM JavaBean itself
		javaObjectLabels.add(BeanProxyUtilities.getBeanProxy(component).toBeanString()); 
	}
	// Now process the component to see if it has any child components
	// Right now only deal with java.awt.Container and its components relationship although this needs to be improved
	// so we deal with other containment relationships that give up components, e.g. JTabbedPane, JSplitPane, ScrollPane, JScrollPane, etc...
	// This should probably be an ad-hoc decorator key value pair ( similar to the way the default GridBagConstraints can be specified on a per-component basis
	if ( sf_container_components == null ) {

	}
	if ( componentClass.isAssignableFrom(component.getJavaType())) { 
		Iterator components = ((List)component.eGet(sf_container_components)).iterator();
		while(components.hasNext()){
			// The child is not a component in the JBCF model - it's a constraint component
			EObject constraintChild = (EObject)components.next();
			IJavaObjectInstance childComponent = (IJavaObjectInstance)constraintChild.eGet(sf_constraint_component);	// Get the component out of the constraint
			processComponent(childComponent,javaObjects,javaObjectLabels);
		}
	}
}
}
