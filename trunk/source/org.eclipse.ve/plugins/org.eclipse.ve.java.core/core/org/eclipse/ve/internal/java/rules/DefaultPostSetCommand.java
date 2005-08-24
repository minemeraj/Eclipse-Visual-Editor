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
 *  $RCSfile: DefaultPostSetCommand.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:30:48 $ 
 */

import java.util.*;

import org.eclipse.emf.ecore.*;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * This is the default postSet command returned by the Default Property Rule.
 * This should not be instantiated directly. The rule is the one who should
 * instantiate it. That is why the ctor is protected so that only the default
 * rule and subclasses can instantiate it.
 */
public class DefaultPostSetCommand extends CommandWrapper {
	
	protected EditDomain domain;
	protected EObject oldValue;
	protected List annotations;
	
	protected DefaultPostSetCommand(EditDomain domain, EObject oldValue) {
		this.domain = domain;
		this.oldValue = oldValue;
	}

	/**
	 * @see com.ibm.etools.common.command.Command#execute()
	 */
	public void execute() {
		CommandBuilder cbld = new CommandBuilder();
		cbld.setExecuteAndAppend(true);	// So that if we revisit a child, it will be already marked and handled.
		handleValue(cbld, oldValue, oldValue.eContainer() == null);	// If not now contained, treat as if was contained, otherwise let handle value calculate it.
		if (annotations != null)
			cbld.append(AnnotationPolicy.getDeleteCommand(annotations, domain.getDiagramData()));
		command = !cbld.isEmpty() ? cbld.getCommand() : NoOpCommand.INSTANCE;	// Because of bug in undo of CommandWrapper, we must have a command.
	}
	
	protected void handleValue(CommandBuilder cbld, EObject oldValue, boolean contained) {
		// To unset, see if it is contained by the <properties> feature on member container, and if so remove itself.
		// If oldValue is contained by some other feature (such as <members>) then don't touch it or its properties.
		EStructuralFeature containmentFeature = oldValue.eContainmentFeature();
		if (containmentFeature == JCMPackage.eINSTANCE.getMemberContainer_Properties()) {
			cbld.cancelAttributeSetting(oldValue.eContainer(), JCMPackage.eINSTANCE.getMemberContainer_Properties(), oldValue);
			contained = true;
			handleAnnotation(oldValue);
		} else if (contained)
			handleAnnotation(oldValue);		
		
		if (contained) {
			// Now walk through the children to have them also removed from <properties> setting.
			Iterator refs = oldValue.eClass().getEAllReferences().iterator();
			while (refs.hasNext()) {
				EReference ref = (EReference) refs.next();
				if (ref.isChangeable() && oldValue.eIsSet(ref)) {
					if (ref.isMany()) {
						Iterator kids = ((List) oldValue.eGet(ref)).iterator();
						while (kids.hasNext()) {
							Object kid = kids.next();
							if (kid instanceof EObject)
								handleValue(cbld, (EObject) kid, ref.isContainment());
						}
					} else {
						Object kid = oldValue.eGet(ref);
						if (kid instanceof EObject)
							handleValue(cbld, (EObject) kid, ref.isContainment());
					}
				}
			}			
		}
	}
	
	/*
	 * Called to handle annotation on this child. It just accumulates
	 * them. Children of children will be handled individually.
	 * 
	 * This should be called if child is being removed completely.
	 */
	protected void handleAnnotation(EObject child) {
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(child);
		if (annotation != null && annotation.eContainmentFeature() == CDMPackage.eINSTANCE.getDiagramData_Annotations()) {
			// Annotation exists and is added to the diagram data.
			if (annotations == null)
				annotations = new ArrayList();
			annotations.add(annotation);
		}
	}	

	/**
	 * @see com.ibm.etools.common.command.AbstractCommand#prepare()
	 */
	protected boolean prepare() {
		return oldValue != null;
	}

}
