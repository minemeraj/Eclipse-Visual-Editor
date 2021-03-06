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
package org.eclipse.ve.internal.jfc.vm;

/*
 *  $RCSfile: FreeFormSwingContainer.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:38:13 $ 
 */

import javax.swing.JPanel;

import org.eclipse.ve.internal.jfc.vm.FreeFormAWTContainer.FreeFormLayoutManager;

/**
 * This panel excepts to only have one component in it. It will use normal preferred size unless the container has been told to use the component's
 * actual size instead.
 * 
 * This allows for the component to either change with the preferred size of the child, or the child's actual size.
 */
public class FreeFormSwingContainer extends JPanel {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 2159900672755043960L;
	boolean useComponentSize = false;

	public FreeFormSwingContainer() {
		super(new FreeFormAWTContainer.FreeFormLayoutManager());
	}

	public void setUseComponentSize(boolean useSize) {
		((FreeFormLayoutManager) getLayout()).useComponentSize = Boolean.valueOf(useSize);
	}

}
