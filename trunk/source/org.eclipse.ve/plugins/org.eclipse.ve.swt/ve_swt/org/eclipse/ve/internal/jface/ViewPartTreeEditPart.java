/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewPartTreeEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-03 15:06:36 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;

import org.eclipse.ve.internal.swt.SwtPlugin;


public class ViewPartTreeEditPart extends JavaBeanTreeEditPart {
	
	private EStructuralFeature sf_delegate_control;

	public ViewPartTreeEditPart(Object aModel) {
		super(aModel);
	}
	
	protected List getChildJavaBeans() {
		List result = new ArrayList(1);
		IJavaInstance delegate_control = BeanUtilities.getFeatureValue((IJavaInstance)getModel(),sf_delegate_control);
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	public void setModel(Object aModel) {
		sf_delegate_control = ((EObject)aModel).eClass().getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL);
		super.setModel(aModel);
	}
	
	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(viewPartAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(viewPartAdapter);
	}	
	
	protected Adapter viewPartAdapter = new EditPartAdapterRunnable(this) {
		public void doRun() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_delegate_control) {
				queueExec(ViewPartTreeEditPart.this, "DELEGATE_CONTROL"); //$NON-NLS-1$
			}
		}
	};
}
