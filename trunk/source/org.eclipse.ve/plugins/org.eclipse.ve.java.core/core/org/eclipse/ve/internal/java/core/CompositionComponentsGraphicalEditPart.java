package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositionComponentsGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2004-05-26 18:23:30 $ 
 */

import java.util.*;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;

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
			if (msg.getFeatureID(BeanComposition.class) == JCMPackage.BEAN_COMPOSITION__COMPONENTS)
				queueRefreshChildren();
		}
	};
	
	/**
	 * Queue up a refresh child for next async exec.
	 *  
	 * @since 1.0.0
	 */
	protected void queueRefreshChildren() {
		CDEUtilities.displayExec(getViewer().getControl().getDisplay(), new Runnable() {
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

	protected EditPart createChild(Object model) {
		// If the model object is in error then we create a special placeholder
		if ( model instanceof IJavaInstance){
			IBeanProxyHost modelBeanProxy = BeanProxyUtilities.getBeanProxyHost((IJavaInstance)model);
			// If we have a fatal error then we use a special graphical edit part
			// We must NOT use the one defined on the class as for some classes, e.g. Component it
			// has a lot of behavior that relies on the live JavaBean being present
			if(modelBeanProxy == null || modelBeanProxy.getErrorStatus() == IBeanProxyHost.ERROR_SEVERE){
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
