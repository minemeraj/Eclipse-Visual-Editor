package org.eclipse.ve.internal.java.vce.rules;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VCEPostSetCommand.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.commands.NoOpCommand;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.cdm.CDMPackage;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.ve.internal.jcm.MemberContainer;
import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.propertysheet.common.commands.CommandWrapper;

/**
 * This is the VCE post set command. It works much like the default post set command
 * except that it knows about parent/child relationships from the BeanFeatureDecorator.
 * If this is created with isChildRule flag, then that means that we are post setting a
 * child. This means that this child must go if there are no other child relationships
 * referencing this child. And so on down to its children, etc. This will then cause
 * its properties to go away too.
 * 
 * If child rule is false, then if an object is a properties or not contained, it will go away, but 
 * if it is a member it will not away if there are any references (other than initializes/return).
 * (Subclasses can override this behavior).
 * 
 * The childRule is only followed from the top oldValue down. If it walks into another <members>
 * that is not a child, then the childRule is set back to false because it isn't a child of the
 * top object anymore, so normal rules apply. This will walk through containment features and
 * such objects will be considered automatically to be a child since they contained by the parent.
 */
public class VCEPostSetCommand extends CommandWrapper {
	
	protected EditDomain domain;
	protected EObject oldValue;
	protected List annotations;
	protected boolean childRule;
	
	protected VCEPostSetCommand(EditDomain domain, EObject oldValue, boolean childRule) {
		this.domain = domain;
		this.oldValue = oldValue;
		this.childRule = childRule;
	}

	/**
	 * @see com.ibm.etools.common.command.Command#execute()
	 */
	public void execute() {
		CommandBuilder cbld = new CommandBuilder();
		cbld.setExecuteAndAppend(true);	// So that if we revisit a child, it will be already marked and handled.
		// If not now contained, treat as if was contained, otherwise let handle value calculate it.
		// We don't know our target, but that is ok, we can handle not knowing, it just helps if we know.
		ArrayList removeValues = new ArrayList();
		handleValue(cbld, oldValue, null, null, ((EObject) oldValue).eContainer() == null, childRule, removeValues, new HashSet(10));
		
		// Now remove all of them. It is safe now since all connections within them have been handled. We can't actually
		// delete them until we've processed everything, else there could be dangling problems or codegen problems with
		// things being deleted too soon.
		for (int i=0; i<removeValues.size(); i++)
			removeValue((EObject) removeValues.get(i), cbld);
			
		if (annotations != null)
			cbld.append(AnnotationPolicy.getDeleteCommand(annotations, domain.getDiagramData()));
		command = !cbld.isEmpty() ? cbld.getCommand() : NoOpCommand.INSTANCE;	// Because of bug in undo of CommandWrapper, we must have a command.
	}
	
	private static final EReference[] EMPTY_REFS = new EReference[0];
	
	/*
	 * @return true if this value is being removed. false if it stays around. 
	 */
	protected boolean handleValue(CommandBuilder cbld, EObject oldValue, EReference refby, EObject refTarget, boolean contained, boolean child, Collection removedValues, Set processed) {
		// To unset, see if it is contained by the <properties> feature on member container, and if so remove itself.
		// If oldValue is contained by some other feature (such as <members>) then try to determine what to do.
		EStructuralFeature initSF = JCMPackage.eINSTANCE.getJCMMethod_Initializes();
		EStructuralFeature returnSF = JCMPackage.eINSTANCE.getJCMMethod_Return();
		InverseMaintenanceAdapter ai = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(oldValue, InverseMaintenanceAdapter.ADAPTER_KEY);
		EReference[] features = ai != null ? ai.getFeatures() : EMPTY_REFS;
		
		EStructuralFeature containmentFeature = oldValue.eContainmentFeature();
		boolean delayDelete = false;	// Delay delete until entire post set is complete. Initially this is don't delay.
		boolean property = false;	// Is this contained as a property.
		
		// It won't be added to the processed set until we determine we should go on.
		// If we don't go and we leave it unprocessed, it can be processed again by another
		// reference to it within the delete set. That will allow one pass processing through the
		// tree. We may determine that is processed though won't be removed. This means we have
		// a final determination and no need to go on.
		if (property = (containmentFeature == JCMPackage.eINSTANCE.getMemberContainer_Properties()))
			;	// Continue, it will go away.
		else if (contained)
			;	// Continue, it will go away. If it is contained, it can't have a method.
		else if (containmentFeature == JCMPackage.eINSTANCE.getMemberContainer_Members()) {
			delayDelete = true;	// We are a member of something, delay the delete.
			if (child) {
				// force it to go away if no other child relationship references it.
				if (ai != null) {
					// There are references.
					for (int i = 0; i < features.length; i++) {
						EReference feature = features[i];
						if (feature != initSF && feature != returnSF && feature != refby && isChildRelationShip(feature)) {
							// It won't go away. Somebody else references thru child relationship. We leave it here.
							return false;
						} 
					}
				}				
			} else {
				// It can't go away if any other references
				if (ai != null) {
					// There are references, check if any other than init/return.
					for (int i = 0; i < features.length; i++) {
						EReference feature = features[i];
						if (feature != initSF && feature != returnSF && feature != refby) {
							// It won't go away. Somebody else references. We leave it here.
							return false;
						} 
					}
				}				
			}
			
			if (!allowDeletion(oldValue)) {
				processed.add(oldValue);	// We processed it and we aren't allowing deletion.
				return false;	// Didn't allow deletion for some other reason
			}
		} else
			return false;	// Contained by something else than the reference that calls this and not properties or members. Don't go away.
		
		processed.add(oldValue);		
		// Now walk through the children to have them also removed if necessary.
		Iterator refsItr = oldValue.eClass().getEAllReferences().iterator();
		List linkRemoveList = null;	// A list for links to remove, used in the loop only.
		while (refsItr.hasNext()) {
			EReference ref = (EReference) refsItr.next();
			if (ref.isChangeable() && oldValue.eIsSet(ref)) {
				boolean continueChild = continueAsChild(child, ref);
				if (ref.isMany()) {
					Iterator kids = ((List) oldValue.eGet(ref)).iterator();
					while (kids.hasNext()) {
						Object kid = kids.next();
						if (kid instanceof EObject && !processed.contains(kid)) {
							boolean handled = handleValue(cbld, (EObject) kid, ref, oldValue, ref.isContainment(), continueChild, removedValues, processed);
							if (!handled) {
								// Not handled, so we add a break of the link to the linkRemoveList so that we can
								// remove this link since kid may not be going away and we don't want dangling
								// back references from kid to oldValue in kid's InverseMaintenanceAdapter. It could be processed
								// again later in the processing tree of this request, so it may be an unnecessary
								// link break, but that is ok.
								if (!okToKeep(ref, (EObject) kid)) {
									if (linkRemoveList == null)
										linkRemoveList = new ArrayList(2);
									linkRemoveList.add(kid);
								}
							}
						}
					}
					if (linkRemoveList != null && !linkRemoveList.isEmpty()) {
						// Now remove the non-handled links. Couldn't do it at the time because of the iterator.
						cbld.cancelAttributeSettings(oldValue, ref, linkRemoveList);
						linkRemoveList.clear();
					}
				} else {
					Object kid = oldValue.eGet(ref);
					if (kid instanceof EObject && !processed.contains(kid))
						if (!handleValue(cbld, (EObject) kid, ref, oldValue, ref.isContainment(), continueChild, removedValues, processed)) {
							// Not handled, so we add a break of the link to the command builder so that we can
							// remove this link since kid may not be going away and we don't want dangling
							// back references from kid to oldValue in kid's InverseMaintenanceAdapter. It could be processed
							// again later in the processing tree of this request, so it may be an unnecessary
							// link break, but that is ok.
							if (!okToKeep(ref, (EObject) kid))
								cbld.cancelAttributeSetting(oldValue, ref);
						}
				}
			
			}
		}			

		// We are going away, so get rid of any references to us, except the init and return, those must be handled at the very end when we handle all at once.
		features = ai != null ? ai.getFeatures() : EMPTY_REFS;	// Reget the list so that we only see what are there (some may of been removed in subprocessing).
		for (int i = 0; i < features.length; i++) {
			EReference feature = features[i];
			EObject[] refs = ai.getReferencedBy(feature);
			if (feature != initSF && feature != returnSF) {
				// Delete all references other than the refBy feature from our target that is being removed that caused us to be called,
				// because that will be handled by the caller to handleValue.
				for (int j = 0; j < refs.length; j++)
					if (feature != refby || refs[j] != refTarget)
						cbld.cancelAttributeSetting(refs[j], feature, oldValue);
			}
		}
	
		handleAnnotation(oldValue);
		if (delayDelete)
			removedValues.add(oldValue);	// Add to the collection to be removed at the very end. It goes after any children have been added so that list is from the deepest to the shallowest.	
		else if (property)
			cbld.cancelAttributeSetting(oldValue.eContainer(), oldValue.eContainmentFeature(), oldValue);	// It is a property, get rid of it now.
			
		return true;
	}
	
	private boolean okToKeep(EReference ref, EObject kid) {
		// Check to see if the kid doesn't have an InverseMaintenanceAdapter AND the ref is not bi-directional.
		// In that case there is no dangling reference from kid back to old value. So it can be kept and not canceled.
		return (ref.getEOpposite() == null && EcoreUtil.getExistingAdapter(kid, InverseMaintenanceAdapter.ADAPTER_KEY) == null);
	}
	
	protected void removeValue(EObject value, CommandBuilder cbld) {
		// Get rid of it if contained by a method now that we got rid of references to it. If contained by something else, that
		// will take care of itself automatically.
		if (value.eContainer() instanceof MemberContainer)
			cbld.cancelAttributeSetting(value.eContainer(), value.eContainmentFeature(), value);
		
		// Now we need to see if we get rid of the methods themselves and the init/return settings		
		EReference initSF = JCMPackage.eINSTANCE.getJCMMethod_Initializes();
		EReference returnSF = JCMPackage.eINSTANCE.getJCMMethod_Return();
		InverseMaintenanceAdapter ia = (InverseMaintenanceAdapter) EcoreUtil.getExistingAdapter(value, InverseMaintenanceAdapter.ADAPTER_KEY);
		JCMMethod initMethod = ia != null ? (JCMMethod) ia.getFirstReferencedBy(initSF) : null;
		JCMMethod returnMethod = ia != null ? (JCMMethod) ia.getFirstReferencedBy(returnSF) : null;		
		if (initMethod != null) {
			cbld.cancelAttributeSetting(initMethod, initSF, value);
			if (initMethod.getInitializes().isEmpty())
				if (!initMethod.eIsSet(returnSF))
					cbld.cancelAttributeSetting(initMethod.eContainer(), initMethod.eContainmentFeature(), initMethod);
		}
		
		if (returnMethod != null) {
			cbld.cancelAttributeSetting(returnMethod, returnSF, value);
			if (returnMethod.getInitializes().isEmpty())
				cbld.cancelAttributeSetting(returnMethod.eContainer(), returnMethod.eContainmentFeature(), returnMethod);
		}	
	}
	
	protected boolean continueAsChild(boolean currentChild, EReference feature) {
		// See if we should pass on as a child
		if (currentChild)
			if (feature.isContainment())
				return true;
			else
				return isChildRelationShip(feature);

		return false;
	}
	
	public static boolean isChildRelationShip(EReference feature) {
		// Implicit child relationship is <components>. 
		if (feature == JCMPackage.eINSTANCE.getBeanComposition_Components())
			return true;
			
		Iterator decorItr = feature.getEAnnotations().iterator();
		while (decorItr.hasNext()) {
			EAnnotation o = (EAnnotation) decorItr.next();
			if (o instanceof BeanFeatureDecorator) 
				return ((BeanFeatureDecorator) o).isChildFeature();
		}
		return false;	
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
	
	/*
	 * Override to prevent deletion for other reasons. This is called if we think we should
	 * delete it.
	 */
	protected boolean allowDeletion(EObject value) {
		return true;
	}

}
