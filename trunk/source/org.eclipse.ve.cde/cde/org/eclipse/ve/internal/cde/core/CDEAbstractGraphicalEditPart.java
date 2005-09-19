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
 *  $Revision: 1.3 $  $Date: 2005-09-19 20:37:48 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * Base edit part that handles edit part contributors the the graphical edit part.
 * 
 * @since 1.2
 */
public abstract class CDEAbstractGraphicalEditPart extends AbstractGraphicalEditPart {

	protected List fEditPartContributors;

	private void addEditPartContributor(GraphicalEditPartContributor anEditPartContributor) {
		if (fEditPartContributors == null) {
			fEditPartContributors = new ArrayList(1);
		}
		fEditPartContributors.add(anEditPartContributor);
	}
	
	protected EditDomain getEditDomain(){
		return EditDomain.getEditDomain(this);
	}
	
	public void activate() {
		super.activate();
		List editPartContributors = getEditDomain().getEditPartContributors(this);
		if(editPartContributors != null){
			Iterator iter = editPartContributors.iterator();
			while(iter.hasNext()){
				GraphicalEditPartContributor graphicalEditPartContributor = ((EditPartContributorFactory)iter.next()).getGraphicalEditPartContributor(this);
				if(graphicalEditPartContributor != null){
					addEditPartContributor(graphicalEditPartContributor);
				}
			}
		}
	}
	
	public void deactivate() {
		if (fEditPartContributors != null) {
			for (Iterator itr = fEditPartContributors.iterator(); itr.hasNext();) {
				EditPartContributor contributor = (EditPartContributor) itr.next();
				contributor.dispose();
				
			}
			fEditPartContributors = null;
		}
		super.deactivate();
	}
}
