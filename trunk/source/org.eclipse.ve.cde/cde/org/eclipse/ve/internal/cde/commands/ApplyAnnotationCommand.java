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
 *  $RCSfile: ApplyAnnotationCommand.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:48 $ 
 */


import java.util.Collections;

import org.eclipse.emf.common.util.BasicEMap;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;
/**
 * This command is used to apply settings to an Annotation. If the 
 * Annotation doesn't exist, it will create it as needed.
 *
 * The setting is a BasicEMap.Entry.
 *
 * This command is used because creating the necessary annotation 
 * can be complicated. It also allows this to go into a compound command where previously
 * in the command the annotation may of been created, but at the time
 * the command was constructed it wasn't available. By putting this command after
 * those commands, the annotation will be available and it won't need to be created in
 * this command.
 *
 * An example of where this would be used is in the create request case. When the create request
 * is creating the commands, the annotation may not yet be live, and so the annotation to add the
 * appropriate value can't be found at that time. However, when the create request command
 * is executed, it will be available. So if this command is placed after the commands for the
 * create request the create request command will first create the annotion, then this command will
 * have an annotation to work with (and so it won't create one).
 */

public class ApplyAnnotationCommand extends CommandWrapper {
	protected Object modelObject;	// The model object this applies against.
	protected EditDomain domain;
	
	protected BasicEMap.Entry newSetting;	// The new setting to be applied
	
	public ApplyAnnotationCommand(Object modelObject, EditDomain domain) {
		this.modelObject = modelObject;
		this.domain = domain;	
	}
	
	public ApplyAnnotationCommand(Object modelObject, BasicEMap.Entry newValue, EditDomain domain) {
		this(modelObject, domain);
		setValue(newValue);
	}
	
	public void setValue(BasicEMap.Entry newSetting) {
		this.newSetting = newSetting;
	}
	
	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return modelObject != null && domain != null && newSetting != null;
	}
	
	public void execute() {
		if (command != null)
			super.execute();
			
		CommandBuilder cb = new CommandBuilder(null);
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(modelObject);
		boolean createdAnnotation = false;
		if (annotation == null) {
			// We don't have an annotation. We need to create one
			createdAnnotation = true;
			annotation = AnnotationPolicy.createAnnotation(modelObject);
		}
		
		ApplyKeyedValueCommand c = new ApplyKeyedValueCommand();
		c.setTarget(annotation);
		c.setValue(newSetting);
		cb.append(c);
		
		if (createdAnnotation) {
			// Now if we created the annotation, we need to add it to the diagram data, then we apply the model
			// to it so that they are connected after everything is set up.
			cb.append(AnnotationPolicy.getDefaultAddAnnotationsCommand(Collections.singletonList(annotation), domain));
			cb.append(AnnotationPolicy.getApplyModelToAnnotationCommand(modelObject, annotation, domain.getAnnotationLinkagePolicy()));
		}
		
		command = cb.getCommand();
		command.execute();
	}
}


