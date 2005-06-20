/*******************************************************************************
 * Copyright (c)  2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: CDECreationTool.java,v $ $Revision: 1.4 $ $Date: 2005-06-20 23:54:40 $
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.IDomainedFactory;

/**
 * This is the base Creation Tool that all CDE editors should use. It can be a CreationTool itself for most
 * cases. It does:
 * <p>
 * <bl>
 * <li>Will use the Cursor out of the CreateRequest's extended data so that edit policies can return their specific cursor to use.
 * <li>Uses the CDEUtilities.calculateCursor to return the appropriate cursor for the state of the domain.
 * <li>Dispose of the cursor from the CreateRequest when no longer needed.
 * <li>Support the CreationPolicy for EClassifiers. </bl>
 * 
 * @author JoeWin
 */
public class CDECreationTool extends CreationTool {

	public static String CREATION_POLICY_KEY = "org.eclipse.ve.internal.cde.core.creationtool.policy";

	/**
	 * Creation Policy.
	 * <p>
	 * This is called when the type of the new object from the creation request's factory (must be an EMF object) supports a CreationPolicy. This
	 * policy is used to massage the command for the creation to add other commands before/after the main command.
	 * 
	 * @since 1.0.0
	 */
	public interface CreationPolicy {

		/**
		 * Get the command to do creation. Take the incoming aCommand and return a command that uses this command and adds more commands before or
		 * after it.
		 * 
		 * @param aCommand
		 *            the starting command to work with
		 * @param domain
		 *            the editDomain to work in
		 * @param createRequest
		 *            the create request that is being processed.
		 * @return the new modified command, or the original if unchanged.
		 * 
		 * @since 1.0.0
		 */
		public Command getCommand(Command aCommand, EditDomain domain, CreateRequest createRequest);
	}

	protected CreationPolicy fCreationPolicy;

	protected boolean fHasLookedupCreationPolicy = false;

	protected Cursor editPolicyCursor;

	protected CDECreationTool(CreationFactory aFactory) {
		super(aFactory);
	}

	/**
	 * For when constructed through ToolEntry.createTool().
	 * 
	 * 
	 * @since 1.1.0
	 */
	public CDECreationTool() {
	}

	public void activate() {
		super.activate();
		CreationFactory fact = getFactory();
		if (fact instanceof IDomainedFactory)
			((IDomainedFactory) fact).setEditDomain((EditDomain) getDomain());
	}

	protected Command getCommand() {
		Command result = super.getCommand();
		if (result != null) {
			if (fCreationPolicy == null && !fHasLookedupCreationPolicy) {
				Object type = getFactory().getObjectType();
				if (type instanceof EClassifier) {
					ClassDescriptorDecorator decorator = (ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
							(EClassifier) type, ClassDescriptorDecorator.class, CREATION_POLICY_KEY);
					if (decorator != null) {
						String creationPolicyClassName = (String) decorator.getKeyedValues().get(CREATION_POLICY_KEY);
						if (creationPolicyClassName != null) {
							try {
								fCreationPolicy = (CreationPolicy) CDEPlugin.createInstance(null, creationPolicyClassName);
							} catch (Exception exc) {
								// If the class can't be created then just drop down and let the regular command be returned
							}
						}
					}
				}
				fHasLookedupCreationPolicy = true;
			}
			if (fCreationPolicy != null) { return fCreationPolicy.getCommand(result, (EditDomain) getDomain(), getCreateRequest()); }
		}
		return result;
	}

	protected void showTargetFeedback() {
		super.showTargetFeedback();
		Object cursor = getTargetRequest().getExtendedData().get(Cursor.class);
		if (cursor instanceof Cursor) {
			editPolicyCursor = (Cursor) cursor;
		}
	}

	protected Cursor getEditPolicyCursor() {
		if (editPolicyCursor != null) {
			return editPolicyCursor;
		} else {
			return getDefaultCursor();
		}
	}

	protected Cursor calculateCursor() {
		Cursor result = CDEUtilities.calculateCursor((EditDomain) getDomain());
		if (result != null)
			return result;
		Command command = getCurrentCommand();
		if (command == null || !command.canExecute()) {
			return getDisabledCursor();
		} else {
			return getEditPolicyCursor();
		}
	}

	protected void eraseTargetFeedback() {
		super.eraseTargetFeedback();
		Object cursor = getTargetRequest().getExtendedData().get(Cursor.class);
		if (cursor == null && editPolicyCursor != null) {
			setCursor(getDefaultCursor());
			editPolicyCursor.dispose();
			editPolicyCursor = null;
		}
	}
}
