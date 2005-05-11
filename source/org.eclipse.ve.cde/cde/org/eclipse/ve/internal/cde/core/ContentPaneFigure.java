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
 *  $RCSfile: ContentPaneFigure.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:26 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
 

/**
 * This is used by Graphical Editparts that have two kinds of children figures. This allows 
 * separation of the content pane figure (i.e. the figure where all of the EditPart child figures will
 * go) from other figures that participate in the main figure but aren't the child editpart figures.
 * This is needed because if a child editpart is reordered, it uses the index of the child editpart to
 * determine where to put it back in the list of figures. But that index does not take into account
 * any other figures that may of been added, and so it can added back in at wrong index.
 * <p>
 * Typically the content pane will be the normal main drawing figure for the editpart. The other 
 * children of the ContentPaneFigure are usually auxiliary figures that paint on top of the content pane
 * and they usually control their own bounds.
 * <p>
 * Umless the layout manager of the content pane figure is changed, by default the inner class ContentPaneLayout
 * will be used as the layout manager. This manager basically will size only the content pane and leave the other children alone, and
 * the preferred size will be that of the content pane.
 * A subclass of ContentPaneLayout can be created that would layout other children in addition to the content pane.
 * <p>
 * This is usually used in the following manner in a Graphical Editpart.
 * <pre>
 * <code>
 * 	protected IFigure createFigure() {
 *		ContentPaneFigure cfig = new ContentPaneFigure();
 *		IFigure ifig = ... your main figure ...
 *		cfig.setContentPane(ifig);
 *		return cfig;
 *	}
 *
 *	public IFigure getContentPane() {
 *		// Override getContentPane so that it uses the ContentPaneFigure's content pane as the figure
 *		// to add child editparts' figures into.
 *		return getContentPaneFigure().getContentPane();
 *	}
 *
 * </code>
 * </pre>
 * @see ContentPaneFigure.ContentPaneLayout 
 * @see org.eclipse.ve.internal.cde.core.ErrorFigure another figure often used in conjuction with the content pane figure.
 * @since 1.1.0
 */
public class ContentPaneFigure extends Figure {
	private IFigure contentPane;
	
	/**
	 * Layout manager used by default in the ContentPaneFigure.
	 * <p>
	 * It only manages the figure set as the ContentPane. It will 
	 * use its preferred size and it will size it to be the same
	 * size as the ContentPaneFigure's clientArea.
	 * <p>
	 * In general there is a singleton of this used by all ContentPaneFigure's
	 * since it does not need to keep state. 
	 * @since 1.1.0
	 */
	public static class ContentPaneLayout extends AbstractHintLayout {

		protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
			ContentPaneFigure cf = (ContentPaneFigure) container; 
			return cf.getContentPane() != null ? cf.getContentPane().getPreferredSize(wHint, hHint) : new Dimension();
		}

		public void layout(IFigure container) {
			ContentPaneFigure cf = (ContentPaneFigure) container;
			Rectangle r = cf.getClientArea();			
			if (cf.getContentPane() != null)
				cf.getContentPane().setBounds(r);
		}
		
	}
	private static final ContentPaneLayout DEFAULT_LAYOUT = new ContentPaneLayout();
	
	public ContentPaneFigure() {
		setLayoutManager(DEFAULT_LAYOUT);
	}

	/**
	 * This is the figure that is the content pane, where all of the children from
	 * editparts will be placed. It is typically the main drawing figure for the
	 * editpart.
	 * <p>
	 * If the contentPane is not already a child of the ContentPaneFigure, it will
	 * be added at index 0 to the ContentPaneFigure.
	 * 
	 * @param contentPane The contentPane to set.
	 * 
	 * @since 1.1.0
	 */
	public void setContentPane(IFigure contentPane) {
		this.contentPane = contentPane;
		if (contentPane.getParent() != this)
			add(contentPane, 0);
	}

	/**
	 * Get the content pane.
	 * @return Returns the contentPane.
	 * 
	 * @see ContentPaneFigure#setContentPane(IFigure)
	 * @since 1.1.0
	 */
	public IFigure getContentPane() {
		return contentPane;
	}
}
