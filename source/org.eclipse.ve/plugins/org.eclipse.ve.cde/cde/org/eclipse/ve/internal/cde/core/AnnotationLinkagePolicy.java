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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: AnnotationLinkagePolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cdm.*;
import org.eclipse.gef.commands.Command;
/**
 * Policy for handling annotations.
 * Because accessing the annotation/viewinfo from the
 * model and visa-versa is model dependent and is not
 * always an EMF model, custom code is required to handle
 * the transformations.
 *
 * If the model is entirely EMF, then use DefaultAnnotationLinkageHelper.
 * Otherwise, subclass GenericAnnotationLinkageHelper and implement the required methods.
 *
 * Note:
 *   It is important that when the DiagramData has been read in and the
 *   model is ready, that the intializeLinkages method is called. This
 *   is so that the linkages to the model are correctly set up. Also
 *   read the other caveats for the initializeLinkages method.
 */
public abstract class AnnotationLinkagePolicy {
			
	/**
	 * Return the annotation for this model object, if any.
	 */
	public Annotation getAnnotation(Object modelObject) {
		if (modelObject == null)
			return null;
		if (modelObject instanceof EObject) {
			EObject mo = (EObject) modelObject;
			AnnotationEMF.ParentAdapter a = (AnnotationEMF.ParentAdapter) EcoreUtil.getExistingAdapter(mo, AnnotationEMF.ParentAdapter.PARENT_ANNOTATION_ADAPTER_KEY);
			return a != null ? a.getParentAnnotation() : null;
		} else {
			// Get it from the generic implementer.
			return getAnnotationFromGeneric(modelObject);
		}
	}
	
	/**
	 * Subclasses must handle this. IT means clean up any listeners and go away.
	 */
	public abstract void dispose();
	
	protected abstract Annotation getAnnotationFromGeneric(Object model);	
	
	/**
	 * Set the model for the annotation. This should only be called if
	 * the model is a live model object.
	 *
	 * If this is EMF, then the model should be live and not a proxy.
	 *
	 * If this is not EMF, then the model should be live and in the Model.
	 * If it isn't, then this cannot be done because the ID might not
	 * be found, and in that case we can't set the id onto the annotation.
	 * If the annotation is not initialized, then it won't be initialized
	 * after this call. It doesn't change that state.
	 */
	public void setModelOnAnnotation(Object model, Annotation annotation) {
		if (annotation instanceof AnnotationEMF)
			((AnnotationEMF) annotation).setAnnotates((EObject) model);
		else
			setModelOnAnnotationGeneric(model, (AnnotationGeneric) annotation);	
	}
	
	protected abstract void setModelOnAnnotationGeneric(Object model, AnnotationGeneric annotation);
	
	
	protected abstract void initializeAnnotationGeneric(AnnotationGeneric annotation);
	
	
	/**
	 * Initialize an annotation. What this does is make sure that
	 * the annotation is connected and notifying. It makes the
	 * connection to the model object (or ID in case of EMF) bi-directional.
	 */
	public void initializeAnnotation(Annotation annotation) {
			if (annotation instanceof AnnotationEMF)
				((AnnotationEMF) annotation).getAnnotates();	// This will resolve the proxy if needed.
			else
				initializeAnnotationGeneric((AnnotationGeneric) annotation);	// Let the subclass handle it.
	}
	
	/**
	 * Initialize linkages for a DiagramData.
	 *
	 * When we have a diagram, we need to call this so that the
	 * linkages from annotation to model are set up. If we don't do that
	 * then we won't be able to find annotations from model objects except in
	 * the rare case that the model is part of the DiagramData itself. Only in
	 * that case would the bi-directional linkages be set up.
	 *
	 * One should also never just throw away the ResourceSet containing the
	 * diagram without also throwing away the model and this linkage helper.
	 * Otherwise this helper will cause everything to be held until the
	 * model is thrown away.
	 *
	 * If you really wish to remove the DiagramData without removing the model,
	 * then you must do an unload() of the Diagram's resource. That will
	 * cause it to be cleaned up in here too.
	 *
	 */
	public void initializeLinkages(DiagramData data) {
		Iterator annotations = data.getAnnotations().iterator();
		while (annotations.hasNext()) {
			initializeAnnotation((Annotation) annotations.next());
		}
	}


	protected abstract boolean isAnnotationValidGeneric(AnnotationGeneric annotation);
	
	/**
	 * Clean up DiagramData. This goes through and see if annotations are left dangling.
	 * I.e. They have no model for them. It will also remove dangling VisualInfos.
	 *
	 * This can occur because of mistakes in processing and in the non-EMF case because
	 * we can't reliably find the children of a model object to add/remove any annotations.
	 * Also if the Annotation is removed, but the Visual Info is not removed from the diagram
	 * would also result in an error.
	 *
	 * It returns it as a command. It needs to be executed. It can be executed on the stack or
	 * off of the command stack. Executing it on the stack means that the Annotations will
	 * come back and be made available on undo. Otherwise they would be lost.
	 *
	 * It will return null if no cleanup is needed.
	 */
	public Command cleanupDiagramData(DiagramData data) {
		List delAnn = new ArrayList();		
		Iterator annotations = data.getAnnotations().iterator();
		while (annotations.hasNext()) {
			Annotation annotation = (Annotation) annotations.next();
			
			boolean remove = false;
			if (annotation instanceof AnnotationEMF) {
				EObject model = ((AnnotationEMF) annotation).getAnnotates();
				if (model != null) {
					// Not null, if it is a proxy, then not resolved, or if not proxy, then still attached.
					// If it is a proxy, remove this annotation because the model couldn't be found.
					if (model.eIsProxy())
						remove = true;
					else if (model.eResource() == null) {
						// But it is possible that the object is not in any resource. In that case this annotation needs to go too.						
						// This is because someone removed model from full model but didn't remove annotation.
						remove = true;
					}
				} else
					remove = true;	// Couldn't find any model, so it should go.
			} else {
				remove = !isAnnotationValidGeneric((AnnotationGeneric) annotation);	// Only the implementation knows
			}
			
			if (remove) {
				// We will need to remove it and any visualinfos.
				delAnn.add(annotation);
			}
		}
		
		CommandBuilder cmdBldr = new CommandBuilder(null);
		cmdBldr.append(AnnotationPolicy.getDeleteCommand(delAnn, data));
		
		// Go through the VisualInfos in all of the Diagrams, remove any that are not
		// in a resource. These are in error.
		List delVI = new ArrayList();
		Iterator diagrams = data.getDiagrams().iterator();
		while (diagrams.hasNext()) {
			Diagram diagram = (Diagram) diagrams.next();
			Iterator vis = diagram.getVisualInfos().iterator();
			while (vis.hasNext()) {
				VisualInfo vi = (VisualInfo) vis.next();
				if (vi.eResource() == null)
					delVI.add(vi);	// Not contained in any resource, needs to be removed.
			}
		}
		
		if (!delVI.isEmpty()) {
			cmdBldr.cancelGroupAttributeSetting(delVI, CDMPackage.eINSTANCE.getVisualInfo_Diagram());
		}
		
		return cmdBldr.getCommand();
	}
		
}
