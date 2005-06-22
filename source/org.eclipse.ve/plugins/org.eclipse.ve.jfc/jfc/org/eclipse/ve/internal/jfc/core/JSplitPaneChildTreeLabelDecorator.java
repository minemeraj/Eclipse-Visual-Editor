package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JSplitPaneChildTreeLabelDecorator.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-22 14:53:04 $ 
 */

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;

/**
 * Label decorator for JSplitPane children in the Outline Viewer. 
 * Will add text to the component text based on the split pane's orientation.
 */
public class JSplitPaneChildTreeLabelDecorator implements ILabelDecorator {
	protected EReference  sfLeftComponent,
		sfRightComponent,
		sfTopComponent,
		sfBottomComponent,
		sf_constraintComponent,
		sf_constraintConstraint;

	public void initializeSFs(EObject component) {
		ResourceSet rset = component.eResource().getResourceSet();
		if (sfLeftComponent == null)
			sfLeftComponent = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		if (sfRightComponent == null)
			sfRightComponent = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		if (sfBottomComponent == null)
			sfBottomComponent = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		if (sfTopComponent == null)
			sfTopComponent = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
		if (sf_constraintComponent == null)
			sf_constraintComponent = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		if (sf_constraintConstraint == null)
			sf_constraintConstraint = (EReference) JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
	}

	public Image decorateImage(Image image, Object anObject) {
		return image;
	}
	public String decorateText(String text, Object anObject) {
		if (anObject == null)
			return ""; //$NON-NLS-1$
		if (!(anObject instanceof IJavaObjectInstance))
			return anObject.toString();
		initializeSFs((EObject) anObject);
		IJavaObjectInstance component = (IJavaObjectInstance) anObject;
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(component, InverseMaintenanceAdapter.ADAPTER_KEY);
		if (ai != null) {
			List backrefs = Arrays.asList(ai.getFeatures());
			if (backrefs.contains(sfLeftComponent)) {
				text = JFCMessages.Decorator_JSplitPane_Left + " - " + text; 
			} else if (backrefs.contains(sfRightComponent)) {
				text = JFCMessages.Decorator_JSplitPane_Right + " - " + text; 
			} else if (backrefs.contains(sfTopComponent)) {
				text = JFCMessages.Decorator_JSplitPane_Top + " - " + text; 
			} else if (backrefs.contains(sfBottomComponent)) {
				text = JFCMessages.Decorator_JSplitPane_Bottom + " - " + text; 
			} else if (backrefs.contains(sf_constraintComponent)) {
				EObject constraintComponent = ai.getFirstReferencedBy(sf_constraintComponent);
				// See whether the component is in severe error.  If so then don't include it here
				if (BeanProxyUtilities.getBeanProxyHost(component).isBeanProxyInstantiated()) {
					IJavaObjectInstance constraintString = (IJavaObjectInstance) constraintComponent.eGet(sf_constraintConstraint);
					if (constraintString != null) {
						// We know the constraints value should be a bean so we can use its toString to get the string value
						String constraint = BeanProxyUtilities.getBeanProxy(constraintString).toBeanString();
						if (constraint != null) {
							if (constraint.equals("left")) //$NON-NLS-1$
								text = JFCMessages.Decorator_JSplitPane_Left + " - " + text; 
							else if (constraint.equals("right")) //$NON-NLS-1$
								text = JFCMessages.Decorator_JSplitPane_Right + " - " + text; 
							else if (constraint.equals("top")) //$NON-NLS-1$
								text = JFCMessages.Decorator_JSplitPane_Top + " - " + text; 
							else if (constraint.equals("bottom")) //$NON-NLS-1$
								text = JFCMessages.Decorator_JSplitPane_Bottom + " - " + text; 
						}
					}
				}
			}
		}
		return text;
	}
	public void addListener(ILabelProviderListener p1) {
	}
	public void dispose() {
	}
	public boolean isLabelProperty(Object p1, String p2) {
		return false;
	}
	public void removeListener(ILabelProviderListener p1) {
	}
}