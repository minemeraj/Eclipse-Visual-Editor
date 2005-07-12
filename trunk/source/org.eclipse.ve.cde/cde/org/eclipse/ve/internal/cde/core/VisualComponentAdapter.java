/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: VisualComponentAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-07-12 21:08:25 $ 
 */
package org.eclipse.ve.internal.cde.core;
 

/**
 * Adapter for IVisualComponentListener. It provides default do nothing implementations
 * for the methods. Subclasses can override specific methods of interest.
 * 
 * @since 1.1.0
 */
public class VisualComponentAdapter implements IVisualComponentListener {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentHidden()
	 */
	public void componentHidden() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentMoved(int, int)
	 */
	public void componentMoved(int x, int y) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentRefreshed()
	 */
	public void componentRefreshed() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentResized(int, int)
	 */
	public void componentResized(int width, int height) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponentListener#componentShown()
	 */
	public void componentShown() {
	}

	public void componentValidated() {
	}

}
