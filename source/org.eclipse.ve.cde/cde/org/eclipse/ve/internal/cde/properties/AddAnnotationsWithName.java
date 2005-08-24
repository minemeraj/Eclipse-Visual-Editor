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
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: AddAnnotationsWithName.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:12:48 $ 
 */
import java.util.*;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.commands.AddAnnotationsCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

/**
 * This checks the name in composition and makes sure that
 * they are valid, and will change them if they are not.
 * @version 	1.0
 * @author
 */
public class AddAnnotationsWithName extends AddAnnotationsCommand {

	/**
	 * Constructor for AddAnnotationsWithName.
	 */
	public AddAnnotationsWithName() {
		super();
	}

	/**
	 * @see AddAnnotationsCommand#getAdditionalCommands(List, EditDomain)
	 */
	protected Command getAdditionalCommands(List annotations, EditDomain domain) {
		CommandBuilder cb = new CommandBuilder();
		
		// First get the appropriate NameInComposition Property Descriptor.
		// It could be that other property descritors want to do something special - like rename the name

		NameInCompositionPropertyDescriptor pd = 
			(NameInCompositionPropertyDescriptor) domain.getKeyedPropertyDescriptor(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
		
		Annotation[] annotationsArray = new Annotation[annotations.size()];
		String[] names = new String[annotationsArray.length];
		for(int aCount=0; aCount<annotations.size(); aCount++){
			annotationsArray[aCount] = (Annotation) annotations.get(aCount);
			Object ks = annotationsArray[aCount].getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if(ks instanceof String)
				names[aCount] = (String)ks;
			else
				names[aCount] = null;
		}
		
		String[] uniqueNames = pd.getUniqueNamesInComposition(domain, names, annotationsArray);
		
		for (int unCount = 0; unCount < uniqueNames.length; unCount++) {
			if (!uniqueNames[unCount].equals(names[unCount])) {
				// We always apply an entire new keyvalue, not just modify the setting.
				EStringToStringMapEntryImpl newKs =
					(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
				newKs.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
				newKs.setValue(uniqueNames[unCount]);
				cb.applyAttributeSetting(annotationsArray[unCount], newKs);
			}
		}

		CompoundCommand cmd = cb.getCompoundCommand();
		if (cmd.isEmpty())
			return null; // Nothing changed
		else
			return cmd.unwrap();
	}

}
