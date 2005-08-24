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
/*
 *  $RCSfile: ErrorFigure.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:12:50 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;


/**
 * Figure used for Graphical Editparts to put up the proper error symbol depending on the severity. It will place the
 * error notification in the upper left corner of the parent figure. 
 * <p>
 * To use this it should be added to the figure of the graphical edit part. However, if the graphical edit part can have children editparts and they
 * have figures that get added to the parent figure, you should use the {@link org.eclipse.ve.internal.cde.core.ContentPaneFigure} as the main figure
 * for the parent editpart, and then use the content pane of that figure as the content pane for the child editparts. This ErrorFigure should then be
 * added to the ContentPaneFigure itself and not to the content pane.
 * <p>
 * This figure will maintain its own bounds. It doesn't expect to be controlled by a layout manager.
 * <p>
 * This is how it is typically used. We will use an example that uses a ContentPaneFigure. This the GraphicalEditPart you are adding this to.
 * <ol>
 * <li>The process is to create it and add to the figure.
 * <li>When activating editpart, get the initial severity and set into the figure, and add a listener to set the severity whenever it changes.
 * <li>When deactivating editopart, remove the error listener.
 * </ol>
 * <pre>
 * <code>
 * 	protected IFigure createFigure() {
 *		ContentPaneFigure cfig = new ContentPaneFigure();
 *		IFigure ifig = ... your main figure ...
 *		cfig.setContentPane(ifig);
 *		errorIndicator = new ErrorFigure();
 *		cfig.add(errorIndicator);
 *		return cfig;
 *	}
 *
 *	protected void setSeverity(int severity) {
 *		errorIndicator.setSeverity(severity);
 *	}
 *
 *	public void activate() {
 *		super.activate();
 *		// Listen to the IErrorNotifier so it tells us when errors occur.
 *		// Use displayExec so that this is queued up through ModelChangeController into just one call at end of transaction.
 *		errorListener = new IErrorNotifier.ErrorListenerAdapter() {
 *			public void errorStatusChanged() {
 *				CDEUtilities.displayExec(GraphicalEditPart.this, "STATUS_CHANGED", new Runnable() {
 *					public void run() {
 *						setSeverity(getComponentProxy().getErrorStatus());
 *					}
 *				}); 
 *			}
 *		};
 *		setSeverity(getComponentProxy().getErrorStatus()); // Set the initial status
 *		errorNotifier.addErrorListener(errorListener);
 *	}
 *
 *	public void deactivate() {
 *		if (errorListener != null) {
 *			errorNotifier.removeErrorListener(errorListener);
 *			errorListener = null;
 *		}
 *		super.deactivate();
 *	}
 *
 *
 * </code>
 * </pre>
 * @see org.eclipse.ve.internal.cde.core.ContentPaneFigure
 * @since 1.1.0
 */
public class ErrorFigure extends Figure {

	protected int fSev;

	protected Image fImage;
	
	/**
	 * Construct with an initial severity of {@link IErrorHolder#ERROR_NONE}.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public ErrorFigure() {
		this(IErrorHolder.ERROR_NONE);
	}

	/**
	 * Construct with an initial severity.
	 * @param severity
	 * 
	 * @since 1.1.0
	 */
	public ErrorFigure(int severity) {
		setSeverity(severity);
	}

	protected void paintFigure(Graphics graphics) {

		super.paintFigure(graphics);
		if (fImage != null) {
			// Clear some background so the image can be seen
			graphics.drawImage(fImage, getLocation().x, getLocation().y);
		}
	}

	/**
	 * Set the severity.
	 * 
	 * @param severity Must be {@link IErrorHolder} severities.
	 * 
	 * @since 1.1.0
	 */
	public void setSeverity(int severity) {
		fSev = severity;
		switch (fSev) {
			case IErrorHolder.ERROR_SEVERE:
				fImage = IErrorHolder.ErrorType.getSevereErrorImage();
				Rectangle bounds = fImage.getBounds();
				setSize(new Dimension(bounds.width, bounds.height));
				setVisible(true);
				break;
			case IErrorHolder.ERROR_WARNING:
				fImage = IErrorHolder.ErrorType.getWarningErrorImage();
				bounds = fImage.getBounds();
				setSize(new Dimension(bounds.width, bounds.height));
				setVisible(true);
				break;
			case IErrorHolder.ERROR_INFO:
				fImage = IErrorHolder.ErrorType.getInformationErrorImage();
				bounds = fImage.getBounds();
				setSize(new Dimension(bounds.width, bounds.height));
				setVisible(true);
				break;
			default:
				setSize(0, 0);
				setVisible(false);
				break;
		}
	}
}
