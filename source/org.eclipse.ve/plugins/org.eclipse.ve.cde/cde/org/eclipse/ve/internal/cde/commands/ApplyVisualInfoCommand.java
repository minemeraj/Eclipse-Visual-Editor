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
 *  $RCSfile: ApplyVisualInfoCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEMap;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.model.*;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * This command is used to apply settings to a VisualInfo. If the VisualInfo or
 * the Annotation doesn't exist, it will create them as needed.
 *
 * The setting may be either a KeyedValue or a Point, Dimension, or Rectangle.
 * If it is not a KeyedValue, it will apply it as a VisualConstraint and use
 * the VisualConstraint key.
 *
 * This command is used because creating the necessary annotation and visual info
 * can be complicated. It also allows this to go into a compound command where previously
 * in the command the annotation and visual info may of been created, but at the time
 * the command was constructed these weren't available. By putting this command after
 * those commands, the visual info will be available and it won't need to be created in
 * this command.
 *
 * An example of where this would be used is in the create request case. When the create request
 * is creating the commands, the annotation may not yet be live, and so the command to add the
 * appropriate constraint can't be found at that time. However, when the create request command
 * is executed, it will be available. So if this command is placed after the commands for the
 * create request the create request command will first create the annotion, then this command will
 * have an annotation to work with (and so it won't create one).
 */

public class ApplyVisualInfoCommand extends CommandWrapper {
	protected Object modelObject; // The model object this applies against.
	protected EditDomain domain;
	protected Diagram diagram;

	protected Object newSetting; // The new setting to be applied, may be KeyedValue, Point, Dimension, or Rectangle.

	public ApplyVisualInfoCommand(Object modelObject, EditDomain domain, Diagram diagram) {
		super();

		this.modelObject = modelObject;
		this.domain = domain;
		this.diagram = diagram;
	}

	public ApplyVisualInfoCommand(Object modelObject, Object newValue, EditDomain domain, Diagram diagram) {
		this(modelObject, domain, diagram);
		setValue(newValue);
	}

	public void setValue(Object newSetting) {
		// Only accept newSetting if valid type
		if (newSetting instanceof BasicEMap.Entry
			|| newSetting instanceof Point
			|| newSetting instanceof Dimension
			|| newSetting instanceof Rectangle)
			this.newSetting = newSetting;
	}

	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return modelObject != null && domain != null && diagram != null && newSetting != null;
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

		VisualInfo vi = annotation.getVisualInfo(diagram);
		if (vi == null) {
			// We need to create one and add it (via commands) to the annotation, if the annotation already existed.
			// This is so that the undo will remove it from the annotation. If the annotation didn't exist, we will
			// just set it.
			CDMFactory fact = CDMFactory.eINSTANCE;
			vi = fact.createVisualInfo();
			// Set the diagram. It needs to be through a command so that on undo it will be removed from the diagram.
			cb.applyAttributeSetting(vi, fact.getCDMPackage().getVisualInfo_Diagram(), diagram);
			if (createdAnnotation)
				annotation.getVisualInfos().add(vi);
			else
				cb.applyAttributeSetting(annotation, fact.getCDMPackage().getAnnotation_VisualInfos(), vi);
		}

		if (newSetting instanceof BasicEMap.Entry) {
			ApplyKeyedValueCommand c = new ApplyKeyedValueCommand();
			c.setTarget(vi);
			c.setValue((BasicEMap.Entry) newSetting);
			cb.append(c);
		} else {
			ApplyVisualConstraintCommand c = new ApplyVisualConstraintCommand(null);
			c.setTarget(vi);
			c.setConstraint(newSetting);
			cb.append(c);
		}

		if (createdAnnotation) {
			// Now if we created the annotation, we need to add it to the diagram data, then we apply the model
			// to it so that they are connected after everything is set up.
			List a = new ArrayList(1);
			a.add(annotation);
			cb.append(AnnotationPolicy.getDefaultAddAnnotationsCommand(a, domain));
			cb.append(AnnotationPolicy.getApplyModelToAnnotationCommand(modelObject, annotation, domain.getAnnotationLinkagePolicy()));
		}

		command = cb.getCommand();
		command.execute();
	}
}
