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
/*
 * $RCSfile: SubclassCompositionComponentsTreeEditPart.java,v $ $Revision: 1.6 $ $Date: 2005-02-15 23:23:54 $
 */
package org.eclipse.ve.internal.java.vce;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.CompositionComponentsTreeEditPart;

/**
 * Composition Graphical Edit Part that instantiates and disposes bean proxies
 */
public class SubclassCompositionComponentsTreeEditPart extends CompositionComponentsTreeEditPart {

	public SubclassCompositionComponentsTreeEditPart(Object model) {
		super(model);
	}

	protected List getModelChildren() {
		BeanSubclassComposition comp = (BeanSubclassComposition) getModel();
		if (comp != null) {
			List components = super.getModelChildren();
			if (comp.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) {
				ArrayList children = new ArrayList(components.size() + 1);
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
				queueRefreshChildren();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.CompositionComponentsTreeEditPart#getContainerPolicy()
	 */
	protected ContainerPolicy getContainerPolicy() {
		return new SubclassCompositionContainerPolicy(EditDomain.getEditDomain(this));
	}
}
