/*
 * Created on Apr 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: NullAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * An action that does nothing and cannot be selected. It can be used as a spacer in a toolbar for
 * a grid type toolbar (such as used in AlignmentXYTabPage).
 */
public class NullAction extends Action {
	public NullAction() {
		ImageDescriptor nullDescriptor =
			CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/clcl16/transp16.gif"); //$NON-NLS-1$
		setImageDescriptor(nullDescriptor);
		setHoverImageDescriptor(nullDescriptor);
		setDisabledImageDescriptor(nullDescriptor);
		setEnabled(false);
	}
}