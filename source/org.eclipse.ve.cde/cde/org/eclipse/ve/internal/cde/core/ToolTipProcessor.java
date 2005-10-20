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
 *  $RCSfile: ToolTipProcessor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-20 19:34:44 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;

/**
 * Provides Tool tip information for CDE graphical edit parts
 *
 * Originally an inner class inside ToolTipAssistFactory called TooltipDetails which is part of org.eclipse.ve.java.core. 
 * Moved up to CDE to be used in a general purpose manner.
 * 
 * @since 1.2.0
 */
public interface ToolTipProcessor {

	/**
	 * Return the figure to use. This will be called only once, the first time the assist is used. This may not occur for awhile. It will occur on the
	 * first hover over the tooltip.
	 * 
	 * @return IFigure
	 * 
	 * @since 1.2.0
	 */
	IFigure createFigure();

	/**
	 * The processor is being activated. This will be called the first time after the figure is created, and whenever the details is reactivated at a
	 * later time. It should do things like start listening if it needs to listen for anything.
	 * 
	 * 
	 * @since 1.2.0
	 */
	void activate();

	/**
	 * The processor is being deactivated. It should do things like stop listening if it is listening.
	 * 
	 * 
	 * @since 1.2.0
	 */
	void deactivate();
}
