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
 *  $RCSfile: EMFCreationFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
/**
 * Creation factory for EMF objects. It is given the EClass itself.
 * This is useful when there are small set of EClasses that will always be loaded.
 * If there are large set of classes in the palette and not all are to be loaded at 
 * the start, then use EMFClassCreationFactory. Then the eclass won't be loaded until needed.
 */
public class EMFCreationFactory implements CreationFactory {
	protected EClass fClass;

	public EMFCreationFactory(EClass aClass) {
		setObjectType(aClass);
	}

	public void setObjectType(EClass aClass) {
		fClass = aClass;
	}

	/**
	 * getNewObject method comment. 
	 */
	public Object getNewObject() {
		if (fClass == null)
			return null;
		try {
			return fClass.getEPackage().getEFactoryInstance().create(fClass);
		} catch (Exception e) {
			Status st = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", e); //$NON-NLS-1$
			CDEPlugin.getPlugin().getLog().log(st);
		}
		return null;
	}

	public Object getObjectType() {

		return fClass;

	}

}
