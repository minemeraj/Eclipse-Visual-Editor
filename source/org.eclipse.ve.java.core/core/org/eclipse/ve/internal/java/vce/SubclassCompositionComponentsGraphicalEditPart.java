package org.eclipse.ve.internal.java.vce;
/*******************************************************************************
 * Copyright (c) 2001, 2003, 2004 IBM Corporation and others. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Common Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/
/*
 * $RCSfile: SubclassCompositionComponentsGraphicalEditPart.java,v $ $Revision:
 * 1.1 $ $Date: 2004-03-26 23:08:01 $
 */
import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import org.eclipse.ve.internal.cde.core.ContainerPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.CompositionComponentsGraphicalEditPart;
/**
 * Subclass Composition Graphical Edit Part for a bean subclass.
 */
public class SubclassCompositionComponentsGraphicalEditPart
		extends
			CompositionComponentsGraphicalEditPart {
	public SubclassCompositionComponentsGraphicalEditPart(Object model) {
		super(model);
	}
	protected ContainerPolicy getContainerPolicy() {
		return new SubclassCompositionContainerPolicy(EditDomain
				.getEditDomain(this));
	}
	protected List getModelChildren() {
		BeanSubclassComposition comp = (BeanSubclassComposition) getModel();
		if (comp != null) {
			List children = super.getModelChildren();
			if (comp.eIsSet(JCMPackage.eINSTANCE
					.getBeanSubclassComposition_ThisPart())) {
				ArrayList newChildren = new ArrayList(children.size() + 1);
				newChildren.add(comp.getThisPart());
				newChildren.addAll(children);
				return newChildren;
			} else
				return children;
		} else
			return Collections.EMPTY_LIST;
	}
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			switch (msg.getFeatureID(BeanSubclassComposition.class)) {
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART :
					queueRefreshChildren();
					break;
			}
		}
	};
	public void activate() {
		super.activate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().add(
					compositionAdapter);
	}
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(
					compositionAdapter);
	}
}
