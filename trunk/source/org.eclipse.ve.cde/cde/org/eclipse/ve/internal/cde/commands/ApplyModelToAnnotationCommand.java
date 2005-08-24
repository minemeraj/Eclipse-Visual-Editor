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
 *  $RCSfile: ApplyModelToAnnotationCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.GenericAnnotationLinkagePolicy;
import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
/**
 * This command will apply the model to the annotation.
 * It will not change the connection state of the annotation (it
 * won't make the annotation alive if not already alive).
 *
 * NOTE: the modelObject needs to be live so that its ID can be found.
 */

public class ApplyModelToAnnotationCommand extends AbstractCommand {

	protected AnnotationLinkagePolicy linkagePolicy;
	protected Object modelObject;
	protected Annotation annotation;
	protected Object oldModelObject;
	
	/**
	 * Construct the command. A null modelObject means to unset the linkage.
	 */
	public ApplyModelToAnnotationCommand(Object modelObject, Annotation annotation, AnnotationLinkagePolicy linkagePolicy) {
		this.modelObject = modelObject;
		this.annotation = annotation;
		this.linkagePolicy = linkagePolicy;
	}
	
	protected boolean prepare() {
		return annotation != null && linkagePolicy != null;
	}
	
	public void execute() {
		if (annotation instanceof AnnotationEMF) {
			oldModelObject = ((AnnotationEMF) annotation).getAnnotates();
		} else {
			oldModelObject = ((AnnotationGeneric) annotation).getAnnotatesID();
		}
		
		linkagePolicy.setModelOnAnnotation(modelObject, annotation);
	}
	
	public void redo() {
		execute();
	}
	
	public void undo() {
		if (annotation instanceof AnnotationEMF)
			linkagePolicy.setModelOnAnnotation(oldModelObject, annotation);
		else
			((GenericAnnotationLinkagePolicy) linkagePolicy).setAnnotationID((AnnotationGeneric) annotation, (String) oldModelObject);
			
		oldModelObject = null;	// So that we don't hold onto it.
	}
	
}
