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
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

/**
 * Controls the Pause figure. <package-protected> because only JavaVisualEditorPart should call it.
 * <p>
 * All calls must be on UI thread.
 * @since 1.1.0
 */
class JavaVisualEditorReloadActionController {

	/**
	 * Call back to editor to tell it that pause or reload occurred.
	 * 
	 * @since 1.1.0
	 */
	public interface IReloadCallback {
		
		/**
		 * Pause was requested.
		 * 
		 * 
		 * @since 1.0.0
		 */
		public void pause();
		
		/**
		 * Reload was requested.
		 * @param clean true for a clean reload (e.g., remove cache)
		 * 
		 * @since 1.0.0
		 */
		public void reload(boolean clean);
		
	}
	
	// dbk save image descriptors
	private static final String JVE_STATUS_MSG_ERROR = CodegenEditorPartMessages.JVE_STATUS_MSG_ERROR; 
	private static final String JVE_STATUS_BAR_MSG_PARSE_ERROR = CodegenEditorPartMessages.JVE_STATUS_BAR_MSG_PARSE_ERROR_; 
	private static final String JVE_STATUS_MSG_PAUSE = CodegenEditorPartMessages.JVE_STATUS_MSG_PAUSE; 
	private static final String JVE_STATUS_MSG_RELOAD = CodegenEditorPartMessages.JVE_STATUS_MSG_RELOAD; 
	public static final ImageDescriptor PLAY_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/refresh_obj.gif"); //$NON-NLS-1$
	public static final ImageDescriptor PAUSE_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/pause.gif"); //$NON-NLS-1$
	public static final ImageDescriptor ERROR_IMAGE_DESCRIPTOR = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/error_obj.gif"); //$NON-NLS-1$
	
	/**
	 * Action ID for Reload action.
	 */
	public static final String RELOAD_ACTION_ID = "org.eclipse.ve.java.core.Reload"; //$NON-NLS-1$

	
	private GraphicalViewer viewer;
	private Boolean pauseType;	// True is parse error, false is holding, null is no pause at all.
	
	private Figure pauseFigure;
	private Label pauseLabelFigure;
	private IReloadCallback reloadCallback;
	
	private Action reloadAction = new Action("", Action.AS_CHECK_BOX) {
		public void run() {
			// We'll never be switching to parse error on an action ran (user clicked button). That is controlled by editor part.
			if (isChecked()) {
				// Switch to pause
				showPause();
				reloadCallback.pause();
			} else {
				// Switch to reload
				removeParseFigure();
				reloadCallback.reload(true);
			}
		}
	};

	/**
	 * Construct with reload callback.
	 * @param reloadCallback
	 * 
	 * @since 1.1.0
	 */
	public JavaVisualEditorReloadActionController(IReloadCallback reloadCallback) {
		this.reloadCallback = reloadCallback;
		// Init the reload action.
		reloadAction.setId(RELOAD_ACTION_ID);
		setupAction();
		reloadAction.setEnabled(false);	// Until everything loaded we are disabled.
	}
	
	/*
	 * Setup the action for the current state.
	 */
	private void setupAction() {
		if (pauseType == Boolean.TRUE) {
			reloadAction.setToolTipText(JVE_STATUS_BAR_MSG_PARSE_ERROR);
			reloadAction.setText(JVE_STATUS_MSG_ERROR);
			reloadAction.setHoverImageDescriptor(ERROR_IMAGE_DESCRIPTOR);
			reloadAction.setChecked(true);
		} else if (pauseType == Boolean.FALSE) {
			reloadAction.setToolTipText(JVE_STATUS_MSG_RELOAD);
			reloadAction.setText(JVE_STATUS_MSG_RELOAD);
			reloadAction.setHoverImageDescriptor(PLAY_IMAGE_DESCRIPTOR);
			reloadAction.setChecked(true);
		} else {
			reloadAction.setToolTipText(JVE_STATUS_MSG_PAUSE);
			reloadAction.setText(JVE_STATUS_MSG_PAUSE);
			reloadAction.setHoverImageDescriptor(PAUSE_IMAGE_DESCRIPTOR);
			reloadAction.setChecked(false);
		}
		reloadAction.setEnabled(false);
		reloadAction.setEnabled(true);	// Kludge. The button is not redrawing correctly. without cycling through the enabled state, it shows the new icon superimposed over the old one.
	}
	
	/**
	 * Get the reload action to use for this editor.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Action getReloadAction() {
		return reloadAction;
	}

	/**
	 * Called once we have a viewer. Will start responding to show/remove requests.
	 * Any calls before this will simply store the state since we don't have a viewer yet.
	 * @param viewer
	 * 
	 * @since 1.1.0
	 */
	public void startListening(GraphicalViewer viewer) {
		this.viewer = viewer;
		
		pauseFigure = new Figure(){
			protected void paintFigure(Graphics graphics) {
				try {
					graphics.setAlpha(125);
					graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					graphics.fillRectangle(getClientArea());
				} catch (SWTException e) {
					// This occurs because if alpha's not available. No way to check with Graphics ahead of time.
					// In that case we paint nothing and only put up the label.
				}
			}
			Locator locator = new Locator() {
				public void relocate(IFigure target) {
					Rectangle b = getRootFigure(target).getClientArea().getCopy();
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


		pauseLabelFigure = new Label(){
			Locator locator = new Locator() {
				public void relocate(IFigure target) {
					// 	Center the figure in the middle of the canvas
					Dimension canvasSize = getRootFigure(target).getSize();
					Dimension prefSize = target.getPreferredSize();
					int newX = (canvasSize.width - prefSize.width) / 2;
					int newY = (canvasSize.height - prefSize.height) / 2;
					Rectangle b = new Rectangle(newX, newY, prefSize.width, prefSize.height).expand(new Insets(10, 25, 10, 25));
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
		
		pauseFigure.setEnabled(false);					
		pauseLabelFigure.setEnabled(true);
		pauseLabelFigure.setOpaque(true);
		
		if (pauseType != null)
			showPauseFigure(pauseType);	// We have a pending pause, show it.
	}
	
	/**
	 * Show the parse error type.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void showParseError() {
		if (pauseType != Boolean.TRUE) {
			showPauseFigure(Boolean.TRUE);
			setupAction();
		}
	}
	
	/**
	 * Show pause type only. Do not fire pause callback. 
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void showPause() {
		if (pauseType != Boolean.FALSE) {
			showPauseFigure(Boolean.FALSE);
			setupAction();
		}
	}
	
	/**
	 * Switch to pause and fire pause callback
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void runPause() {
		if (pauseType != Boolean.FALSE) {
			showPause();
			reloadCallback.pause();
		}
	}
	
	/**
	 * Dispose, no longer needed.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void dispose() {
		removeParseFigure();
		viewer = null;
	}
	
	protected Layer getLoadingLayer() {
		return (Layer) LayerManager.Helper.find(viewer.getRootEditPart()).getLayer(LayerConstants.HANDLE_LAYER);
	}
	
	private void showPauseFigure(Boolean pauseType) {
		this.pauseType = pauseType;
		// If no viewer, then we have nothing to show yet.
		if (viewer != null) {
			String label;
			Image image = pauseLabelFigure.getIcon();
			if (image != null) {
				// We are switching, so get rid of old one.
				image.dispose();
				image = null;
			}
			if (pauseType.booleanValue()) {
				image = ERROR_IMAGE_DESCRIPTOR.createImage();
				label = " Errors in source preventing parsing";				
			} else {
				image = PLAY_IMAGE_DESCRIPTOR.createImage();
				label = " Visual Editor paused";								
			}
			pauseLabelFigure.setText(label);
			pauseLabelFigure.setIcon(image);
			
			Layer loadingLayer = getLoadingLayer();
			loadingLayer.add(pauseFigure); 
			loadingLayer.add(pauseLabelFigure); 
			
			Viewport vp = getViewport(loadingLayer);
			if (vp != null) {
				vp.getHorizontalRangeModel().addPropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().addPropertyChangeListener(scrolledListener);
			}				
			getRootFigure(loadingLayer).addFigureListener(rootFigureListener);
			pauseFigure.revalidate();
			pauseLabelFigure.revalidate();					

		}
	}
	
	/**
	 * Remove the pause figure.
	 * 
	 * 
	 * @since 1.1.0
	 */
	public void removeParseFigure() {
		if (pauseType == null)
			return;	// All ready down.
		pauseType = null;
		if (viewer != null) {
			Layer layer = getLoadingLayer();
			layer.remove(pauseFigure);
			layer.remove(pauseLabelFigure);
			Image image = pauseLabelFigure.getIcon();
			pauseLabelFigure.setIcon(null);
			image.dispose();
			
			Viewport vp = getViewport(layer);
			if (vp != null) {
				vp.getHorizontalRangeModel().removePropertyChangeListener(scrolledListener);
				vp.getVerticalRangeModel().removePropertyChangeListener(scrolledListener);
			}
			getRootFigure(layer).removeFigureListener(rootFigureListener);
		}
		setupAction();
	}
		
	private Viewport getViewport(IFigure figure) {
		IFigure f = figure;
		while (f != null && !(f instanceof Viewport))
			f = f.getParent();
		return (Viewport) f;
	}
	
	private IFigure getRootFigure(IFigure target) {
		IFigure parent = target.getParent();
		while (parent.getParent() != null)
			parent = parent.getParent();
		return parent;
	}
	
	private FigureListener rootFigureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			pauseFigure.revalidate();
			pauseLabelFigure.revalidate();
		}
	};

	private PropertyChangeListener scrolledListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (RangeModel.PROPERTY_VALUE.equals(evt.getPropertyName())) {
				pauseFigure.revalidate();
				pauseLabelFigure.revalidate();
			}
		}

	};
}
