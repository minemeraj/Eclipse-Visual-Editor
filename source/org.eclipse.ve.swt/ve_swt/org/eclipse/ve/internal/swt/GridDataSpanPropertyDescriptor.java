/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridDataSpanPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-12-01 20:19:43 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jem.java.JavaDataType;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;
 

/**
 * Property descriptor for GridData span settings. This adapter makes sure that when setting a span value that it 
 * will create/delete fillers and columns/rows as needed.
 *  
 * @since 1.2.0
 */
public class GridDataSpanPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	protected boolean horizontalSpan;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor#setValue(org.eclipse.ui.views.properties.IPropertySource, java.lang.Object)
	 */
	public Command setValue(IPropertySource source, Object setValue) {
		// The target of the BeanPropertySourceAdapter is the composite
		IJavaObjectInstance griddata = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost griddataProxyHost = BeanProxyUtilities.getBeanProxyHost(griddata);	
		Object oldSpanValue = source.getPropertyValue(getTarget());
		IJavaInstance oldSpan = (IJavaInstance) (oldSpanValue instanceof IPropertySource ? (IJavaObjectInstance) ((IPropertySource) oldSpanValue).getEditableValue() : oldSpanValue);
		EditDomain domain = griddataProxyHost.getBeanProxyDomain().getEditDomain();
		
		if (oldSpan != setValue) {
			IBeanProxy spanValue = BeanProxyUtilities.getBeanProxy((IJavaInstance) setValue);
			if (spanValue instanceof IIntegerBeanProxy) {
				CompositeContainerPolicy cp = new CompositeContainerPolicy(domain);
				CommandBuilder cb = new CommandBuilder();
				// KLUDGE: We need to get the composite that the control is in. The control is the owner of the griddata, which is the source.
				// But the griddata may be implicit. So if not implicit, we get the control through the inverse adapter. Griddata is not meant
				// to be shared so we will use the first one for the layout data setting. If implicit, then we get the control through
				// the implicit allocation. Then from the control we get the composite. If we don't have the composite we can't tell
				// what other controls will be affected by the span request and so the helper can't create the correct commands.
				IJavaObjectInstance control;
				boolean implicitGriddata = griddata.isImplicitAllocation();
				if (!implicitGriddata) {
					// It is set, so we can use the inverse adapter.
					control = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(griddata, 
							(EReference) JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), 
									SWTConstants.SF_CONTROL_LAYOUTDATA)); 
				} else {
					// It is implicit, so we can get it out of the allocation. We need to do this when implicit because it could of
					// been just a fluffed up entry for the property sheet. In that case it will not have a control through the inverse adapter.
					control = (IJavaObjectInstance) ((ImplicitAllocation) griddata.getAllocation()).getParent();
					
					// KLUDGE: We also have to morph it into an explicit. We assume it uses default ctor.
					// This is because by default there is no implicit griddata. The gridlayout will fluff one up
					// after it runs once. But in real code it can't be accessed implicitly during initialization because
					// it will still be null. Also we don't handle implicit correctly AND we need to apply the span
					// to this griddata, NOT the fluffed up one that the helper would normally do.
					// By canceling the allocation, it becomes a create with null ctor. And it will then promote correctly.
					cb.cancelAttributeSetting(griddata, JavaInstantiation.getAllocationFeature(griddata));
				}
				
				if (control != null) {
					// Now the control will never be a fluffed up entry in the property sheet. So we would use the inverse adapter to find the composite.
					// Also, control's can't be shared, so it will be first container.
					IJavaObjectInstance container = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(control,  (EReference) JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), 
							SWTConstants.SF_COMPOSITE_CONTROLS));
					if (container != null) {
						cp.setContainer(container);
						GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper(cp);
						helper.startRequest();
						Rectangle childDim = helper.getChildDimensions(control);
						Point spanTo;
						if (horizontalSpan)
							spanTo = new Point(((IIntegerBeanProxy) spanValue).intValue(), childDim.height);
						else
							spanTo = new Point(childDim.width, ((IIntegerBeanProxy) spanValue).intValue());
						helper.spanChild(control, spanTo, horizontalSpan ? PositionConstants.EAST : PositionConstants.SOUTH, implicitGriddata ? griddata : null);
						cb.append(helper.stopRequest());
						return cb.getCommand();
					}
				}
			}
		}
		
		// Same value, so we just apply the new setting so we have a touch.
		RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
		cbld.applyAttributeSetting(griddata, (EStructuralFeature) getTarget(), setValue);
		return cbld.getCommand();
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor#resetValue(org.eclipse.ui.views.properties.IPropertySource)
	 */
	public Command resetValue(IPropertySource source) {
		// Resetting means set back to span of one. To do this we just fluff up a new value of setting to one.
		IJavaObjectInstance griddata = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost griddataProxyHost = BeanProxyUtilities.getBeanProxyHost(griddata);	
		ResourceSet rset = EMFEditDomainHelper.getResourceSet(griddataProxyHost.getBeanProxyDomain().getEditDomain());
		Object oneValue = BeanProxyUtilities.wrapperBeanProxyAsPrimitiveType(griddataProxyHost.getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(1), (JavaDataType) ((EStructuralFeature) getTarget()).getEType(), rset, "1");
		return setValue(source, oneValue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			horizontalSpan = ((EStructuralFeature) newTarget).getName().equals("horizontalSpan");
	}
}
