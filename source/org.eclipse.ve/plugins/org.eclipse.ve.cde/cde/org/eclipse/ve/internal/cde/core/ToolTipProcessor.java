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
 *  $Revision: 1.3 $  $Date: 2005-12-14 21:27:11 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * Provides Tool tip information for CDE graphical edit parts
 * 
 * Originally an inner class inside ToolTipAssistFactory called TooltipDetails which is part of org.eclipse.ve.java.core. Moved up to CDE to be used
 * in a general purpose manner.
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

	public class ToolTipLabel implements ToolTipProcessor {
		String text = ""; //$NON-NLS-1$
		static final Font labelFont = new Font(Display.getDefault(), "Arial", 8, SWT.BOLD); //$NON-NLS-1$

		public ToolTipLabel(String text) {
			this.text = text;
		}

		public IFigure createFigure() {
			Label l = new Label(text);
			l.setFont(labelFont);
			return l;
		}

		public void activate() {
		}

		public void deactivate() {
		}

	}

	public class ToolTipSeparator implements ToolTipProcessor {

		public IFigure createFigure() {
			return new Figure() {

				public void paint(Graphics graphics) {
					Rectangle bounds = getBounds();
					graphics.setForegroundColor(ColorConstants.lightGray);
					graphics.drawLine(bounds.x, bounds.y + 1, bounds.x + bounds.width, bounds.y + 1);
				}

				public Dimension getPreferredSize(int wHint, int hHint) {
					return new Dimension(getParent().getBounds().width, 2);
				}
			};
		}

		public void activate() {
		}

		public void deactivate() {
		}

	}
}
