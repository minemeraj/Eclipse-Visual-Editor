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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: LayoutPropertyDescriptor.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:52:55 $ 
 */
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.ILayoutSwitcher;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

public class LayoutPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

public ILabelProvider getLabelProvider() {
	return new LayoutLabelProvider();
}
public CellEditor createPropertyEditor(Composite parent) {
	return new LayoutCellEditor(parent);
}
/**
 * It's here that we create the command to apply the new value to the 'layout' property of the composite and we also create 
 * multiple commands to update the composite's controls. 
 * We do this by getting the new layout classes' factory's constaint converter and based on the layout class, 
 * the converter will create the command(s) necessary to update the constraints for any controls in the composite. 
 * 
 * With this capability, this allows us to create layoutData objects on the composite's controls when going from a FillLayout
 * to GirdLayout and vice versa... although changing to a FillLayout actually removes the layoutData.
 */
public Command setValue(IPropertySource source, Object setValue){
	// The target of the BeanPropertySourceAdapter is the composite
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
		ILayoutPolicyFactory layoutFactory = BeanSWTUtilities.getLayoutPolicyFactoryFromLayout(layoutManagerClass, domain);
		if (layoutFactory != null) {
			CompositeContainerPolicy cp = new CompositeContainerPolicy(domain);
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
	ILayoutPolicyFactory layoutFactory = BeanSWTUtilities.getLayoutPolicyFactoryFromLayout(layoutManagerClass, domain);
	if (layoutFactory != null) {
		CompositeContainerPolicy cp = new CompositeContainerPolicy(domain);
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
