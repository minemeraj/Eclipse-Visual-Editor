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
 *  $RCSfile: DefaultTreeEditPartFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-02 20:41:41 $ 
 */

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gef.EditPart;

/**
 * Default factory for creating GraphicalEditParts.
 * It gets the class string out of the decorator of the EObject and
 * uses the graphviewClassname attribute.
 */
public class DefaultTreeEditPartFactory extends AbstractEditPartFactory {

	protected ClassDescriptorDecoratorPolicy policy;

	public DefaultTreeEditPartFactory(ClassDescriptorDecoratorPolicy policy) {
		this.policy = policy;
	}

	public EditPart createEditPart(EditPart parentEP, Object modelObject) {
		String epClassString = modelObject instanceof EObject ? policy.getTreeViewClassname(((EObject) modelObject).eClass()) : "org.eclipse.ve.internal.cde.core:org.eclipse.ve.internal.cde.core.CDEDefaultTreeEditPart"; //$NON-NLS-1$
		return createEditPart(epClassString, modelObject);
	}

}