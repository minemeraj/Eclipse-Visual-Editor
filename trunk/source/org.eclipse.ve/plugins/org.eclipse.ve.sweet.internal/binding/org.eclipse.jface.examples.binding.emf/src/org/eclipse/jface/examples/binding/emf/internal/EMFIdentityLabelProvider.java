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
package org.eclipse.jface.examples.binding.emf.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.converters.IdentityConverter;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * This class is an IdentityConverted that also implements an
 * {@link  org.eclipse.jface.databinding.IColumnConverter} and provide
 * 
 * A single colum is converted for each element, it is a String value for the
 * feature property this converter s constructed with.
 * 
 * e.g., new EMFIdentityLabelProvider(Lodging.class, "name") will use Identity
 * conversion to the Lodging element, and getConvertedColumn(loging, 0) will
 * convert a Lodging instance name property to String.
 * 
 */
public class EMFIdentityLabelProvider extends IdentityConverter implements
		ILabelProvider {

	private EStructuralFeature feature = null;

	private String featureName;

	public EMFIdentityLabelProvider(Class type, String featureName) {
		super(type);
		this.featureName = featureName;
	}

	public EMFIdentityLabelProvider(Class type, EStructuralFeature feature) {
		super(type);
		this.featureName = feature.getName();
		this.feature = feature;
	}

	public Image getConvertedColumnImage(Object object, int column) {
		return null;
	}

	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(Object element) {
		if (feature == null)
			feature = ((EObject) element).eClass().getEStructuralFeature(
					featureName);
		return ((EObject) element).eGet(feature).toString();
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
