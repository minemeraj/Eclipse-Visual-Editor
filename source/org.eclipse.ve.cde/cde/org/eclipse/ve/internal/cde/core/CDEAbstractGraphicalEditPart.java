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
/*
 *  $RCSfile: CDEAbstractGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-14 23:18:16 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * Base edit part that handles edit part contributors the the graphical edit part.
 * 
 * @since 1.2
 */
public abstract class CDEAbstractGraphicalEditPart extends AbstractGraphicalEditPart {

	protected List fEditPartContributors;

	public void addEditPartContributor(GraphicalEditPartContributor anEditPartContributor) {
		if (fEditPartContributors == null) {
			fEditPartContributors = new ArrayList(1);
		}
		fEditPartContributors.add(anEditPartContributor);
	}

	protected void fireActivated() {
		super.fireActivated();
		EditPartContributorRegistry contributorRegistry = (EditPartContributorRegistry) EditDomain.getEditDomain(this).getData(
				EditPartContributorRegistry.class);
		if (contributorRegistry != null) {
			contributorRegistry.graphicalEditPartActivated(this);
		}
	}

}
