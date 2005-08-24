/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * Created on Apr 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: NullAction.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:12:49 $ 
 */

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * An action that does nothing and cannot be selected. It can be used as a spacer in a toolbar for
 * a grid type toolbar (such as used in AlignmentXYComponentPage).
 */
public class NullAction extends Action {
	public NullAction() {
		ImageDescriptor nullDescriptor =
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/elcl16/transp16.gif"); //$NON-NLS-1$
		setImageDescriptor(nullDescriptor);
		setHoverImageDescriptor(nullDescriptor);
		setDisabledImageDescriptor(nullDescriptor);
		setEnabled(false);
	}
}
