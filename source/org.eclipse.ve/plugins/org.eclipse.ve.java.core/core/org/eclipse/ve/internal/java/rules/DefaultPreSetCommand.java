/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.rules;
/*
 *  $RCSfile: DefaultPreSetCommand.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.MemberContainer;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * This is the default preSet command returned by the Default Property Rule.
 * This should not be instantiated directly. The rule is the one who should
 * instantiate it. That is why the ctor is protected so that only the default
 * rule can instantiate it.
 */
public class DefaultPreSetCommand extends CommandWrapper {
	
	protected EditDomain domain;
	protected EObject target;
	protected EObject newValue;
	protected boolean containment;
	protected List annotations;
	
	protected DefaultPreSetCommand(EditDomain domain, EObject target, EObject newValue, EReference feature) {
		this.domain = domain;
		this.target = target;
		this.newValue = newValue;
		this.containment = feature != null ? feature.isContainment() : false;
	}
	
	protected boolean prepare() {
		// Need to override prepare because prepare expects to have a command
		// create, and at the time of prepare being called, we don't have a command yet.
		return target != null && domain != null;
	}
	
	public void execute() {
		MemberContainer mc = findMemberContainer();
		CommandBuilder cbld = new CommandBuilder();
		cbld.setExecuteAndAppend(true);	// Execute as we append so that if we point back to a value more than once, it will be marked as already handled.
		handleValue(cbld, mc, newValue, containment);
		if (annotations != null)
			cbld.append(AnnotationPolicy.getDefaultAddAnnotationsCommand(annotations, domain));
		if (!cbld.isEmpty())
			command = cbld.getCommand();
		else
			command = NoOpCommand.INSTANCE;	// Because of bug in CommandWrapper, we must have a command.
	}

	/*
	 * Find what member container the new value needs to be added to.
	 */
	protected MemberContainer findMemberContainer() {
		EObject parent = null;
		if (!containment && newValue.eContainer() instanceof MemberContainer) {
			// Already contained and feature not containment. Subproperties will be in this parent.
			parent = newValue.eContainer();
		} else {		
			// For just JBCF, any properties, and subproperties, will be placed into <properties> of first memberContainer found in
			// hierarchy of target.
			parent = target.eContainer();
			while (parent != null && !(parent instanceof MemberContainer))
				parent = parent.eContainer();
		}
			
		if (parent == null) {
			// Shouldn't be here, but just in case, need the DiagramData from domain. This will be a MemberContainer.
			parent = domain.getDiagramData();
		}
		
		return (MemberContainer) parent;
	}
	
	protected void handleValue(CommandBuilder cbld, MemberContainer mc, EObject value, boolean containment) {
		// If the type is containment, we go on because we don't want to stick into properties in that case.
		if (!containment && value.eContainer() == null) {
			// Not yet in a container. If in a container, that is ok, it is in right place.
			cbld.applyAttributeSetting(mc, JCMPackage.eINSTANCE.getMemberContainer_Properties(), value);
			handleAnnotation(value);
		} else if (containment)
			handleAnnotation(value);		
		
		// Now walk through the children to have them also made into properties, if not already.
		Iterator refs = value.eClass().getEAllReferences().iterator();
		while (refs.hasNext()) {
			EReference ref = (EReference) refs.next();
			if (value.eIsSet(ref)) {
				if (ref.isMany()) {
					Iterator kids = ((List) value.eGet(ref)).iterator();
					while (kids.hasNext()) {
						Object kid = kids.next();
						if (kid instanceof EObject)
							handleValue(cbld, mc, (EObject) kid, ref.isContainment());
					}
				} else {
					Object kid = value.eGet(ref);
					if (kid instanceof EObject)
						handleValue(cbld, mc, (EObject) kid, ref.isContainment());
				}
			}
		}
	}
	
	/*
	 * Called to handle annotation on this child. It just accumulates
	 * them. Children of children will be handled individually.
	 */
	protected void handleAnnotation(EObject child) {
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(child);
		if (annotation != null && annotation.eContainmentFeature() == null) {
			// Annotation exists and not yet added anywhere.
			if (annotations == null)
				annotations = new ArrayList();
			annotations.add(annotation);
		}
	}
		
}
