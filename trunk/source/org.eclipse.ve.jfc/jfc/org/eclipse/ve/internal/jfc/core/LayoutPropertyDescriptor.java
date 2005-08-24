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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: LayoutPropertyDescriptor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:38:09 $ 
 */
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.*;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

public class LayoutPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	
	
public ILabelProvider getLabelProvider() {
	return new LayoutManagerLabelProvider();
}
public CellEditor createPropertyEditor(Composite parent) {
	return new LayoutManagerCellEditor(parent);
}
/**
 * It's here that we create the command to apply the new value to the 'layout' property of the container and we also create 
 * multiple commands to update the container's components. 
 * We do this by getting the new layoutManager's factory's constaint converter and based on the layoutmanager, 
 * the converter will create the command(s) necessary to update the constraints for any components in the container. 
 * 
 * With this capability, this allows us to create constraints on the container's components when going from a FlowLayoutManager
 * to BorderLayoutManager and vice versa... although changing to a FlowLayoutManager actually removes the constraints.
 */
public Command setValue(IPropertySource source, Object setValue){
	// The target of the BeanPropertySourceAdapter is the container
	IJavaObjectInstance container = (IJavaObjectInstance) source.getEditableValue();
	IBeanProxyHost containerProxyHost = BeanProxyUtilities.getBeanProxyHost(container);	
	IPropertySource oldLayoutPS = (IPropertySource)source.getPropertyValue(getTarget());
	IJavaObjectInstance oldLayout = oldLayoutPS != null ? (IJavaObjectInstance) oldLayoutPS.getEditableValue() : null;
	EditDomain domain = containerProxyHost.getBeanProxyDomain().getEditDomain();

	if (oldLayout != setValue) {
		// Get the layout factory's layout switcher and let it create the necessary commands to apply the new
		// layout property to the container and the constraint commands for each of the children.
		EClassifier layoutManagerClass = null;
		if (setValue != null)
			layoutManagerClass = ((IJavaObjectInstance)setValue).eClass();
		ILayoutPolicyFactory layoutFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerClass, domain);
		if (layoutFactory != null) {
			ContainerPolicy cp = new ContainerPolicy(domain);
			cp.setContainer(container);
			ILayoutSwitcher switcher = layoutFactory.getLayoutSwitcher(cp);
			if (switcher != null) {
				return switcher.getCommand((EStructuralFeature)getTarget(), (IJavaObjectInstance) setValue);
			}
		}
	}

	// Same value, or had no switcher, so we just apply the new setting.
	RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
	cbld.applyAttributeSetting(container, (EStructuralFeature) getTarget(), setValue);
	return cbld.getCommand();
}

public Command resetValue(IPropertySource source){
	
	if (!source.isPropertySet(getTarget()))
		return null;	// Property wasn't set to do unset.
		
	// The target of the BeanPropertySourceAdapter is the container
	IJavaObjectInstance container = (IJavaObjectInstance) source.getEditableValue();

	// Get the layout factory's layout switcher and let it create the necessary commands to apply the new
	// layout property to the container and the constraint commands for each of the children.
	IBeanProxyHost containerProxyHost = BeanProxyUtilities.getBeanProxyHost(container);
	EditDomain domain = containerProxyHost.getBeanProxyDomain().getEditDomain();
	IBeanProxy defaultLayoutManager = (IBeanProxy) ((IInternalBeanProxyHost) containerProxyHost).getOriginalSettingsTable().get(getTarget());
	EClassifier layoutManagerClass = null;
	if (defaultLayoutManager != null)
		layoutManagerClass = BeanProxyUtilities.getJavaType(defaultLayoutManager, container.eResource().getResourceSet());
	ILayoutPolicyFactory layoutFactory = BeanAwtUtilities.getLayoutPolicyFactoryFromLayoutManger(layoutManagerClass, domain);
	if (layoutFactory != null) {
		ContainerPolicy cp = new ContainerPolicy(domain);
		cp.setContainer(container);
		ILayoutSwitcher switcher = layoutFactory.getLayoutSwitcher(cp);
		if (switcher != null) {
			return switcher.getCancelCommand((EStructuralFeature)getTarget(), defaultLayoutManager);
		}
	}

	// No switcher, so we just cancel the setting.	The constraints of any children will probably be bad.
	RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
	cbld.cancelAttributeSetting(container, (EStructuralFeature) getTarget());	
	return cbld.getCommand();
}

}
