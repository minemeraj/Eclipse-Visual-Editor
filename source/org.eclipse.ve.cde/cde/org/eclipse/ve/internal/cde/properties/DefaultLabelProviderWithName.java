package org.eclipse.ve.internal.cde.properties;
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
 *  $RCSfile: DefaultLabelProviderWithName.java,v $
 *  $Revision: 1.2 $  $Date: 2004-03-26 23:07:50 $ 
 */

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.emf.DefaultLabelProvider;
import org.eclipse.ve.internal.cdm.Annotation;

/**
 * This is a default label provider that works with Name In Composition, if
 * one is available for the element.
 * @version 	1.0
 * @author
 */
public class DefaultLabelProviderWithName extends DefaultLabelProvider {

	/**
	 * This is used to put into a decorator key for Label provider classname so that this provider can be instantiated dynamically.
	 */
	public final static String DECORATOR_CLASSNAME_VALUE = "org.eclipse.ve.cde/org.eclipse.ve.internal.cde.properties.DefaultLabelProviderWithName";
	
	public DefaultLabelProviderWithName(){
		super();
	}
	/**
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		Annotation ann = policy.getAnnotation(element);
		if (ann != null) {
			Object ks = ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if (ks instanceof String) {
				return (String) ks;
			}
		}

		return super.getText(element);
	}

	/**
	 * @see IBaseLabelProvider#isLabelProperty(Object, String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		if (NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY.equals(property))
			return true;
		return false;
	}

}
