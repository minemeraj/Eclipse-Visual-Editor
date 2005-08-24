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
 *  $RCSfile: AbstractEditPartFactory.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.lang.reflect.Constructor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * Base editpart factory. It is used to create editparts on a 
 * factory basis. It is used in the EMF environment.
 */

public abstract class AbstractEditPartFactory implements EditPartFactory {
	/**
	 * Subclasses can call this with the formatted class string to create
	 * the editpart. The formatted class string is the format that CDEPlugin
	 * uses to find classes, it contains the plugin, classname, and initialization data.
	 * See CDEPluging for the format.
	 *
	 * The modelObject will be set into the editpart after it is created.
	 */
	protected EditPart createEditPart(String editpartClassString, Object modelObject) {
		try {
			Class editpartClass = CDEPlugin.getClassFromString(editpartClassString);
			EditPart editpart = null;
			try {
				Constructor ctor = editpartClass.getConstructor(new Class[] { Object.class });
				editpart = (EditPart) ctor.newInstance(new Object[] { modelObject });
			} catch (NoSuchMethodException e) {
				// Use default ctor instead.
				editpart = (EditPart) editpartClass.newInstance();
				editpart.setModel(modelObject);
			}
			CDEPlugin.setInitializationData(editpart, editpartClassString, null);
			return editpart;
		} catch (Exception e) {
			String message =
				java.text.MessageFormat.format(
					CDEMessages.Object_noinstantiate_EXC_, 
					new Object[] { editpartClassString });
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;
		}
	}
}
