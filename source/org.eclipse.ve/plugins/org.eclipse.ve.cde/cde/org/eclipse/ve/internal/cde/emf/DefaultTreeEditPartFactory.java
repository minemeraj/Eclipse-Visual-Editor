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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: DefaultTreeEditPartFactory.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.lang.reflect.Constructor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;

import org.eclipse.ve.internal.cde.core.*;

/**
 * Default factory for creating GraphicalEditParts.
 * It gets the class string out of the decorator of the EObject and
 * uses the graphviewClassname attribute.
 */
public class DefaultTreeEditPartFactory extends AbstractEditPartFactory {

	protected ClassDescriptorDecoratorPolicy policy;
	public static Class DEFAULT_EDIT_PART_CLASS = CDEDefaultTreeEditPart.class;
	private Constructor DEFAULT_EDIT_PART_CLASS_CONSTRUCTOR;	
	

	public DefaultTreeEditPartFactory(ClassDescriptorDecoratorPolicy policy) {
		this.policy = policy;
	}

	public EditPart createEditPart(EditPart parentEP, Object modelObject) {
		
		EditPart result = (modelObject instanceof EObject) ? policy.createTreeEditPart((EObject)modelObject) : null;
		if(result != null){
			return result;
		} else {
			return createDefaultEditPart(modelObject);
		}
	}
	private EditPart createDefaultEditPart(Object modelObject){
		try {		
			if (DEFAULT_EDIT_PART_CLASS_CONSTRUCTOR == null){
				DEFAULT_EDIT_PART_CLASS_CONSTRUCTOR = DEFAULT_EDIT_PART_CLASS.getConstructor(new Class[] {Object.class});
			}
			return (EditPart) DEFAULT_EDIT_PART_CLASS_CONSTRUCTOR.newInstance(new Object[] {modelObject});
		} catch (Exception exc){
			String message =
				java.text.MessageFormat.format(
					CDEMessages.Object_noinstantiate_EXC_, 
					new Object[] { DEFAULT_EDIT_PART_CLASS.getName() });
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, exc);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;				
		}
	}	
}
