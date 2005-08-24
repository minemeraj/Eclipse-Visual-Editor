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
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:09 $ 
 */

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;

public class JMenuBarRootPaneOnlyModelAdapter extends ComponentModelAdapter implements IContainmentHandler {

	public JMenuBarRootPaneOnlyModelAdapter(Object model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#isParentValid(java.lang.Object)
	 */
	public boolean isParentValid(Object parent) {
		if (parent instanceof EObject) {
			EClass parentClass = ((EObject)parent).eClass();
			if (parentClass instanceof JavaClass) {
				EStructuralFeature sf = parentClass.getEStructuralFeature("JMenuBar"); //$NON-NLS-1$
				if (sf != null) {
					return true;
				}
			}
		}
		return false;
	}
}
