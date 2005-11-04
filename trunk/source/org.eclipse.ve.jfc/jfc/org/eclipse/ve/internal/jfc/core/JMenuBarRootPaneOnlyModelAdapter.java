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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JMenuBarRootPaneOnlyModelAdapter.java,v $
 *  $Revision: 1.7 $  $Date: 2005-11-04 17:30:48 $ 
 */

import org.eclipse.emf.ecore.*;

import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class JMenuBarRootPaneOnlyModelAdapter extends AbstractComponentModelContainmentHandler {

	public JMenuBarRootPaneOnlyModelAdapter(Object model) {
		super(model);
	}
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		if (parent instanceof EObject) {
			EClass parentClass = ((EObject)parent).eClass();
			if (parentClass instanceof JavaClass) {
				EStructuralFeature sf = parentClass.getEStructuralFeature("JMenuBar"); //$NON-NLS-1$
				if (sf != null) {
					return child;
				}
			}
		}
		throw new StopRequestException("This child can only be dropped on a class that takes a JMenuBar.");
	}

}
