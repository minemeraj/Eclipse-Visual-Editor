package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: DefaultLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.propertysheet.INeedData;
/**
 * A default label provider that uses the ID of the EMF object
 * as the string and the image comes from the ClassDescriptorDecorator.
 * If it doesn't have an ID, then it uses the classname (e.g. FlowLayout would be "(flowLayout)".
 */
public class DefaultLabelProvider extends LabelProvider implements INeedData {

	protected EditDomain domain;

	public void setData(Object data) {
		if (data instanceof EditDomain)
			domain = (EditDomain) data;
	}

	/**
	 * The <code>LabelProvider</code> implementation of this 
	 * <code>ILabelProvider</code> method returns the element's <code>toString</code>
	 * string. Subclasses may override.
	 */
	public String getText(Object element) {
		if (!(element instanceof EObject))
			return super.getText(element);

		EObject el = (EObject) element;
		Resource res = el.eResource();
		if (res instanceof XMLResource) {
			XMLResource xres = (XMLResource) res;
			String id = xres.getID(el);
			if (id != null)
				return id;
		}
		
		// No id or not xml resource. Just use "(classname)"
		return MessageFormat.format(CDEEmfMessages.getString("DefaultLabelProvider.getText"), new Object[] {CDEUtilities.lowCaseFirstCharacter(el.eClass().getName())}); //$NON-NLS-1$
	}

	/**
	 * The default image comes from the ClassDescriptorDecorator.
	 */
	public Image getImage(Object element) {
		if (!(element instanceof EObject))
			return super.getImage(element);

		return ClassDescriptorDecoratorPolicy.getPolicy(domain).getIcon(((EObject) element).eClass()).getImage();
	}
}