/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JaveVisualEditorLoadingFigureController.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

import org.eclipse.ve.internal.java.codegen.core.CodegenMessages;
 
/*
 * JVE Loading Figure Controller. Used by JavaVisualEditorPart. 
 * <package> protected because not to be used by anyone else.
 * 
 * Note: All calls to this must be on the Display thread.
 * 
 * @since 1.0.0
 */
class JaveVisualEditorLoadingFigureController {
	protected static final Insets INSETS = new Insets(10, 25, 10, 25);

	protected GraphicalViewer viewer;
	protected Label loadingFigure;
	protected boolean showingLoadingFigure = true;
	
	public IFigure getRootFigure(IFigure target) {
		IFigure parent = target.getParent();
		while (parent.getParent() != null)
			parent = parent.getParent();
		return parent;
	}		

	public JaveVisualEditorLoadingFigureController() {
	}

	/**
	 * Call when we have a viewer to actually work with.
	 * At this point in time we can now display the current loading status.
	 * 
	 * This allows us to start listening before we have a viewer.
	 * This should only be called once.
	 */
	public void startListener(GraphicalViewer viewer) {
		this.viewer = viewer;
		loadingFigure = new Label(CodegenMessages.CodeGenVisualGraphicalEditorPart_StatusChangeListener_loading) {
			Locator locator = new Locator() {
				public void relocate(IFigure target) {
					// Center the figure in the middle of the canvas
					Dimension canvasSize = getRootFigure(target).getSize();
					Dimension prefSize = target.getPreferredSize();
					int newX = (canvasSize.width - prefSize.width) / 2;
					int newY = (canvasSize.height - prefSize.height) / 2;
					Rectangle b = new Rectangle(newX, newY, prefSize.width, prefSize.height).expand(INSETS);
					target.translateToRelative(b);
					target.setBounds(b);
				}
			};
			public void validate() {
				if (!isValid())
					locator.relocate(this);
				super.validate();
			}
		};
		loadingFigure.setEnabled(true);
		loadingFigure.setOpaque(true);
		loadingFigure.setBorder(SimpleEtchedBorder.singleton);
		if (showingLoadingFigure)
			showLoadingFigure();
	}

	public void showLoadingFigure(boolean show) {
		if (show) {
			if (!showingLoadingFigure)
				showLoadingFigure();
		} else if (showingLoadingFigure)
			removeLoadingFigure();
	}

	protected Layer getLoadingLayer() {
		return (Layer) LayerManager.Helper.find(viewer.getRootEditPart()).getLayer(LayerConstants.HANDLE_LAYER);
	}

	private FigureListener rootFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			loadingFigure.revalidate();
		}
	};

	private PropertyChangeListener scrolledListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (RangeModel.PROPERTY_VALUE.equals(evt.getPropertyName()))
				loadingFigure.revalidate();	// Scrollbar has moved, so revalidate.
		}

	};
	
	protected void removeLoadingFigure() {
		showingLoadingFigure = false;
		if (viewer != null) {
			Layer layer = getLoadingLayer();
			if (layer.getChildren().contains(loadingFigure)) {
				layer.remove(loadingFigure);
			}
			Viewport vp = getViewport(layer);
			if (vp != null) {
				vp.getHorizontalRangeModel().removePropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().removePropertyChangeListener(scrolledListener);
			}
			getRootFigure(layer).removeFigureListener(rootFigureListener);
		}
	}
	
	protected Viewport getViewport(IFigure figure) {
		IFigure f = figure;
		while (f != null && !(f instanceof Viewport))
			f = f.getParent();
		return (Viewport) f;
	}

	protected void showLoadingFigure() {
		showingLoadingFigure = true;
		if (viewer != null) {
			Layer layer = getLoadingLayer();
			layer.add(loadingFigure);
			Viewport vp = getViewport(layer);
			if (vp != null) {
				vp.getHorizontalRangeModel().addPropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().addPropertyChangeListener(scrolledListener);
			}				
			getRootFigure(layer).addFigureListener(rootFigureListener);
			loadingFigure.revalidate();
		}
	}
}
