package org.eclipse.ve.internal.java.vce;
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
 *  $RCSfile: SubclassCompositionComponentsGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.*;
/**
 * Subclass Composition Graphical Edit Part for a bean subclass.
 */
public class SubclassCompositionComponentsGraphicalEditPart extends ContentsGraphicalEditPart {

	public SubclassCompositionComponentsGraphicalEditPart(Object model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		VisualInfoXYLayoutEditPolicy ep = new VisualInfoXYLayoutEditPolicy(new SubclassCompositionContainerPolicy(EditDomain.getEditDomain(this)));
		ep.setZoomable(true);
		installEditPolicy(EditPolicy.LAYOUT_ROLE, ep);
	}
	
	protected List getModelChildren() {
		BeanSubclassComposition comp = (BeanSubclassComposition) getModel();
		if (comp != null) {
			List components = comp.getComponents();
			if (comp.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) {
				ArrayList children = new ArrayList(components.size()+1);
				children.add(comp.getThisPart());
				children.addAll(components);
				return children;
			} else
				return components;
		} else
			return Collections.EMPTY_LIST;
	}
	
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			switch (msg.getFeatureID(BeanSubclassComposition.class)) {
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART:
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__COMPONENTS:				
					refreshChildren();
					break;
			}
		}

	};
	
	public void activate() {
		super.activate();
		if (getModel() != null) 
			((BeanSubclassComposition) getModel()).eAdapters().add(compositionAdapter);
	}
	
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(compositionAdapter);
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
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
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

}
