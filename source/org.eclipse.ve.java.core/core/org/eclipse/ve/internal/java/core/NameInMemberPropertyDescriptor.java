/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NameInMemberPropertyDescriptor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:54 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ICellEditorValidator;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.MemberContainer;
 

/**
 * This is a composition name property descriptor that makes the name unique for the MemberContainer
 * it is in, and not unique within the whole composition, like the superclass does.
 * 
 * @since 1.0.0
 */
public class NameInMemberPropertyDescriptor extends NameInCompositionPropertyDescriptor {


	/**
	 * Member based name validator. It will validate name within the member container
	 * of the source.
	 * 
	 * @since 1.0.0
	 */
	public static class MemberBasedNameValidator extends NameValidator {

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor.NameValidator#getSuggestedName(java.lang.String)
		 */
		protected String getSuggestedName(String name) {
			Object source = pos[0].getEditableValue();
			if (!(source instanceof EObject))
				return super.getSuggestedName(name);	// Don't know what it is, do unique in composition.
			EObject container = ((EObject) source).eContainer();
			if (!(container instanceof MemberContainer))
				return super.getSuggestedName(name);	// Don't know what it is, do unique in composition.
			return getUniqueNameInMember(domain, (MemberContainer) container, name); 
		}
	}
	
	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor() {
		super();
	}
	
	/**
	 * @param displayNameToUse
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor(String displayNameToUse) {
		super(displayNameToUse);
	}
	
	/**
	 * @param displayNameToUse
	 * @param additionalValidator
	 * 
	 * @since 1.0.0
	 */
	public NameInMemberPropertyDescriptor(String displayNameToUse, ICellEditorValidator additionalValidator) {
		super(displayNameToUse, additionalValidator);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor#getNameValidator()
	 */
	protected NameValidator getNameValidator() {
		return new MemberBasedNameValidator();
	}

	/**
	 * Get a unique name in member using the given base name.
	 * @param domain
	 * @param container the membercontainer to look into.
	 * @param name base name to start with
	 * @return unique name in member container based upon starting name
	 * 
	 * @since 1.0.0
	 */
	public static String getUniqueNameInMember(EditDomain domain, MemberContainer container, String name) {
		return getUniqueNameInMember(domain, container, name, null);
	}
	
	/**
	 * Get a unique name in composition using the given base name. It will also
	 * look in the Set of other names if the set is not null. This allows for checking
	 * for an add of group at once, so that they also don't duplicate themselves.
	 * 
	 * @param domain
	 * @param container the membercontainer to look into.
	 * @param name base name to start with
	 * @param otherNames set of other names (names not yet in member, but will be, so don't duplicate) <code>null</code> if no other names.
	 * @return unique name in member container based upon starting name
	 * 
	 * @since 1.0.0
	 */
	public static String getUniqueNameInMember(EditDomain domain, MemberContainer container, String name, Set otherNames) {
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		String baseName = null;
		if (name != null)
			baseName = name;
		else
			baseName = CDEMessages.getString("PropertyDescriptor.NameInComposition.Default"); // Use a default. //$NON-NLS-1$
		String componentName = baseName;
		int incr = 0;
		main : while (true) {
			if (otherNames != null && otherNames.contains(componentName)) {
				componentName = baseName + ++incr;
				continue;
			}
			Iterator itr = container.getMembers().iterator();
			while (itr.hasNext()) {
				Annotation an = (Annotation) policy.getAnnotation(itr.next());
				if (an != null) {
					BasicEMap.Entry ks = getMapEntry(an, NAME_IN_COMPOSITION_KEY);
					if (ks != null && componentName.equals(ks.getValue())) {
						componentName = baseName + ++incr;
						continue main;
					}
				}
			}
			break;
		}

		return componentName;
	}

}
