package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: EMFAnnotationLinkagePolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:58 $ 
 */

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.AnnotationGeneric;
/**
 * This is used as an annotation linkage helper when the model is an EMF
 * model. In those cases we don't need anything special. EMF handles it all
 * for us.
 */
public final class EMFAnnotationLinkagePolicy extends AnnotationLinkagePolicy {


	public void dispose() {
		// Nothing needs to be done.
	}
	
	// This shouldn't even be called in this case.	
	protected void setModelOnAnnotationGeneric(Object model, AnnotationGeneric annotation) {
	}

	// This shouldn't even be called in this case.		
	protected Annotation getAnnotationFromGeneric(Object model) {
		return null;
	}
	
	// This shouldn't even be called in this case.		
	protected void initializeAnnotationGeneric(AnnotationGeneric annotation) {
	}

	// This shouldn't even be called in this case.			
	protected boolean isAnnotationValidGeneric(AnnotationGeneric annotation) {
		return true;
	}

}