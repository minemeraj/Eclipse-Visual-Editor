/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: NullLayoutSwitcher.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-11 15:27:25 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * 
 * @since 1.0.0
 */
public class NullLayoutSwitcher extends LayoutSwitcher {
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutSwitcher#getChangeConstraintsCommand(java.util.List)
	 */
	protected NullLayoutPolicyHelper helper;
	
	/**
	 * NullLayoutConstraintConverter constructor comment.
	 */
	public NullLayoutSwitcher(VisualContainerPolicy cp) {
		super(cp);
		helper = new NullLayoutPolicyHelper(cp);
	}
	protected Command getChangeConstraintsCommand(List children) {
		return null;
	}
}
