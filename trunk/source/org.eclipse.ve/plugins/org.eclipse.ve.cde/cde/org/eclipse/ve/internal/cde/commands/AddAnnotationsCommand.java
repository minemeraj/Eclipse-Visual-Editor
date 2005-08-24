/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.commands;
/*
 *  $RCSfile: AddAnnotationsCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.List;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

import org.eclipse.gef.commands.Command;

/**
 * This is used to add a set of annotations to the diagram data. Subclasses
 * may be used to add additional commands. For example, if there is a nameInComposition,
 * it would be desired that the name be unique, and this can't be known until the
 * time the set of annotations are actually added. Then the subclass (AddAnnotatationsWithNames)
 * would be used in that case, and it would look at the set of annotations being added and return
 * command to make them unique in the current editdomain.
 * @version 	1.0
 * @author
 */
public class AddAnnotationsCommand extends CommandWrapper {

	protected EditDomain domain;
	protected List annotations;

	/**
	 * Constructor for AddAnnotationsCommand.
	 */
	public AddAnnotationsCommand() {
	}

	public void setDomain(EditDomain domain) {
		this.domain = domain;
	}

	public void setAnnotations(List annotations) {
		this.annotations = annotations;
	}

	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return domain != null && annotations != null;
	}

	public void execute() {
		if (command != null)
			super.execute();
		if (annotations.isEmpty())
			return;

		CommandBuilder cb = new CommandBuilder(null);

		cb.append(getAdditionalCommands(annotations, domain));
		cb.applyAttributeSettings(domain.getDiagramData(), CDMPackage.eINSTANCE.getDiagramData_Annotations(), annotations);

		command = cb.getCommand();
		command.execute();
	}

	/**
	 * Override this to return additional commands to modify the annotations, if needed,
	 * for the current environment. This would be used if the actual modifications can't
	 * be known until the moment the annotations are to be added.
	 * 
	 * Note: The command to add the annotations to the diagramData will be append AFTER
	 * these commands.
	 */
	protected Command getAdditionalCommands(List annotations, EditDomain domain) {
		return null;
	}
}
