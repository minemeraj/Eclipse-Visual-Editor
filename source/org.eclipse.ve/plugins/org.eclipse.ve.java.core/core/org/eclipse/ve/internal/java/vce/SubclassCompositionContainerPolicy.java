package org.eclipse.ve.internal.java.vce;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SubclassCompositionContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
/**
 * Container Policy for Bean Subclass Compositions.
 */
public class SubclassCompositionContainerPolicy extends JavaContainerPolicy {
	
	public SubclassCompositionContainerPolicy(EditDomain domain) {
		super(JCMPackage.eINSTANCE.getBeanComposition_Components(), domain);
	}
	
	protected BeanSubclassComposition getComposition() {
		return (BeanSubclassComposition) container;
	}
	
	protected Command getDeleteDependentCommand(Object child, EStructuralFeature containmentSF) {
		if (child == getComposition().getThisPart())
			return UnexecutableCommand.INSTANCE;	// Can't delete the this part.
		// The superclass will create a command that will delete the annotation
		return super.getDeleteDependentCommand(child,containmentSF);
		
	// For now, comment out handling the backpointers. This will be handled differently in the future.
	
//		// Create a commnand that is responsible for un-wiring backpointers.
//		// An example of this is Checkbox/checkboxGroup that has a back reference of CheckboxGroup/checkbox
//		// When a CheckboxGroup is deleted we must unwire it from the checkBox that points to it
//		// This is not a containment relationship from Checkbox to checkboxGroup so refContainer won't find who points to us,
//		// to do this we must iterate over all of the meta object's references and see which ones are backpointers from 
//		// non composite ( i.e. shared ) relationships that point to us.  These are all then unwired
//
//		// Find the relationships on our metaobject that are backpointers from shared references to us
//		Iterator references = ((EObject)child).eClass().getEAllReferences().iterator();
//		CommandBuilder bldr = new CommandBuilder("Delete references to " + child);
//		while(references.hasNext()){
//			EReference referenceFromUs = (EReference)references.next();
//			// Find the opposite and make sure it is a shared relationship
//			EReference referenceToUs = referenceFromUs.getEOpposite();
//			if ( referenceToUs != null && !referenceToUs.isContainment()) {
//				// Now we have a reference to us.  Create a command to unwire this
//				// ( Following the CheckboxGroup analogy this will unwire the Checkbox/checkboxGroup )
//					Object objectsPointingToUs = ((EObject)child).eGet(referenceFromUs);
//					// If is possible that there are >1 object pointing to us ( in the case of checkboxGroup which can be pointed to by >1 checkbox )
//					if ( objectsPointingToUs instanceof List ) {
//						Iterator iter = ((List)objectsPointingToUs).iterator();
//						while(iter.hasNext()){
//							EObject objectPointingToUs = (EObject) iter.next();
//							bldr.cancelAttributeSetting(objectPointingToUs,referenceToUs,child);
//						}
//					} else if ( objectsPointingToUs != null ) {
//						bldr.cancelAttributeSetting((EObject)objectsPointingToUs,referenceToUs,child);
//					}
//				}
//		};
//		if ( result != null ){
//			bldr.getCompoundCommand().append(result);
//		}
//		return bldr.getCommand();
	}
	
	public Command getMoveChildrenCommand(List children, Object position) {
		Object thisPart = getComposition().getThisPart();
		if (thisPart != null) {
			if (children.contains(thisPart))
				return UnexecutableCommand.INSTANCE;	// Can't move the this part.
		}
		return super.getMoveChildrenCommand(children, position);
	}
	
	public Command getOrphanChildrenCommand(List children) {	
		Object thisPart = getComposition().getThisPart();
		if (thisPart != null) {
			if (children.contains(thisPart))
				return UnexecutableCommand.INSTANCE;	// Can't orphan the this part.
		}
		return super.getOrphanChildrenCommand(children);
	}


}