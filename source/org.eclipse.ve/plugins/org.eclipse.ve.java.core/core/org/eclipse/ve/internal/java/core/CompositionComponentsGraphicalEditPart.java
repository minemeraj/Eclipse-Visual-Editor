/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: CompositionComponentsGraphicalEditPart.java,v $
 *  $Revision: 1.13 $  $Date: 2005-06-10 17:47:02 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.BeanComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;
/**
 * Composition Graphical Edit Part for Java Beans Compositions.
 */
public class CompositionComponentsGraphicalEditPart extends ContentsGraphicalEditPart implements ICDEContextMenuContributor {


	public CompositionComponentsGraphicalEditPart(Object model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		VisualInfoXYLayoutEditPolicy ep = new VisualInfoXYLayoutEditPolicy(getContainerPolicy());
		ep.setZoomable(true);
		ep.setGriddable(false);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, ep);
	}
	
	
	/**
	 * Get the container policy for this editpart. Subclasses may override and return something different.
	 * @return container policy.
	 * 
	 * @since 1.0.0
	 */
	protected ContainerPolicy getContainerPolicy() {
		return new CompositionContainerPolicy(EditDomain.getEditDomain(this));
	}

	protected List getModelChildren() {
		BeanComposition comp = (BeanComposition) getModel();
		return comp != null ? comp.getComponents() : Collections.EMPTY_LIST;
	}
	
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			if (!msg.isTouch() && msg.getFeatureID(BeanComposition.class) == JCMPackage.BEAN_COMPOSITION__COMPONENTS)
				queueRefreshChildren();
		}
	};
	
	/**
	 * Queue up a refresh child for next async exec.
	 *  
	 * @since 1.0.0
	 */
	protected void queueRefreshChildren() {
		CDEUtilities.displayExec(this, "REFRESH_CHILDREN", new Runnable() { //$NON-NLS-1$
			public void run() {
				// Test if active because this could of been queued up and not run until AFTER it was deactivated.
				if (isActive())
					refreshChildren();
			}
		});
	}
	
	public void activate() {
		super.activate();
		if (getModel() != null)
			((BeanComposition) getModel()).eAdapters().add(compositionAdapter);
	}
	
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanComposition) getModel()).eAdapters().remove(compositionAdapter);
	}

	public void setModel(Object model) {
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(compositionAdapter);		
		super.setModel(model);
	}

	protected EditPart createChild(Object model) {
		// If the model object is in error then we create a special placeholder
		if ( model instanceof IJavaInstance){
			IBeanProxyHost modelBeanProxy = BeanProxyUtilities.getBeanProxyHost((IJavaInstance)model);
			// If we have a fatal error then we use a special graphical edit part
			// We must NOT use the one defined on the class as for some classes, e.g. Component it
			// has a lot of behavior that relies on the live JavaBean being present
			IJavaInstance javaModel = (IJavaInstance)model;
			JavaHelpers awtComponentClass = Utilities.getJavaType("java.awt.Component",javaModel.eResource().getResourceSet()); //$NON-NLS-1$
			// This is a hack because the trap to no use the defined edit part for Component must not be generalized
			// A better fix would be that the edit part is more robust and can deal with no bean proxy there
			if(modelBeanProxy == null || (awtComponentClass.isAssignableFrom(javaModel.eClass()) && !modelBeanProxy.isBeanProxyInstantiated())){
				// The DefaultGraphicalEditPart will show the icon and its label provider will indicate to the
				// user that the JavaBean failed to be created
				JavaBeanGraphicalEditPart result = new JavaBeanGraphicalEditPart(model);
				return result;
			}
		}
		return super.createChild(model);
	}

	public Object getAdapter(Class key) {
		if (key == IActionFilter.class)
			return getCompositionActionFilter();
		
		Object result = super.getAdapter(key);
		if (result == null && getModel() != null) {
			// See if any of the MOF adapters on our target can return a value for the request
			Iterator mofAdapters = ((Notifier) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(key);
					if (mofAdapterAdapter != null) {
						return mofAdapterAdapter;
					}
				}
			}
		}
		return result;
	}
	
	protected IActionFilter getCompositionActionFilter() {
		return CDEActionFilter.INSTANCE;
	}
	
	/**
	 * Return a list of edit policies.
	 * 
	 * This should not be needed but GEF doesn't make the edit policies public except by a specific key.
	 */
	public List getEditPolicies() {
		List result = new ArrayList();
		AbstractEditPart.EditPolicyIterator i = super.getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result.isEmpty() ? Collections.EMPTY_LIST : result;
	}

}
