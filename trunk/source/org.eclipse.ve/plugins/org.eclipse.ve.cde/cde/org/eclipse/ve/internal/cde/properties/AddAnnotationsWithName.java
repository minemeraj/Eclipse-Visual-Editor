package org.eclipse.ve.internal.cde.properties;
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
 *  $RCSfile: AddAnnotationsWithName.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
import java.util.*;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.gef.commands.Command;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.commands.AddAnnotationsCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cdm.Annotation;
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

		HashSet addedNames = new HashSet(annotations.size());
		Iterator anns = annotations.iterator();
		while (anns.hasNext()) {
			Annotation ann = (Annotation) anns.next();
			Object ks = ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if (ks instanceof String) {
				String newName = NameInCompositionPropertyDescriptor.getUniqueNameInComposition(domain, (String) ks, addedNames);
				if (!newName.equals(ks)) {
					// We need to apply the new unique name
					addedNames.add(newName); // So that it won't be used again.
					// We always apply an entire new keyvalue, not just modify the setting.
					EStringToStringMapEntryImpl newKs =
						(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
					newKs.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
					newKs.setValue(newName);
					cb.applyAttributeSetting(ann, newKs);
				}
			}
		}

		CompoundCommand cmd = cb.getCompoundCommand();
		if (cmd.isEmpty())
			return null; // Nothing changed
		else
			return cmd.unwrap();
	}

}
