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
 *  $RCSfile: ActionBarChildComponentPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-14 21:39:00 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * Component policy for the child editparts of the Action Bar editpart contributions.
 * 
 * @since 1.2.0
 */

public class ActionBarChildComponentPolicy extends ComponentEditPolicy {
	protected EditPartContributor contributor;
	protected IJavaInstance viewerControl;

	public ActionBarChildComponentPolicy(EditPartContributor contributor, IJavaInstance viewerControl) {
		super();
		this.contributor = contributor;
		this.viewerControl = viewerControl;
	}

	/*
	 * If the control this viewer is associated with is not implicit, create the delete command to remove this viewer and remove it's association with
	 * the control
	 * 
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		if (viewerControl != null && !(viewerControl.getAllocation().isImplicit())) {
			RuledCommandBuilder rb = new RuledCommandBuilder(EditDomain.getEditDomain(getHost()));
			rb.setPropertyRule(false);
			rb.cancelMembership((EObject) getHost().getModel());
			rb.append(getNotificationCommand());
			return rb.getCommand();
		}
		return null;
	}

	private Command getNotificationCommand() {
		return new AbstractCommand() {

			protected boolean prepare() {
				return true;
			}

			public void execute() {
				CDEUtilities.displayExec(getHost(), "EDITPART_CONTRIBUTION_NOTIFY", new EditPartRunnable(getHost()) { //$NON-NLS-1$

					protected void doRun() {
						contributor.notifyContributionChanged();
						getHost().deactivate();
					}
				});
			};
		};
	}
}
