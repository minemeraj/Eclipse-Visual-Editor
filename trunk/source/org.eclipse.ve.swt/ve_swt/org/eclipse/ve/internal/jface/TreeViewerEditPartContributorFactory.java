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
package org.eclipse.ve.internal.jface;

import java.text.MessageFormat;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.swt.SwtPlugin;

/**
 * Contributor factory for JFace TreeViewer.
 * 
 * @since 1.2.0
 */
public class TreeViewerEditPartContributorFactory extends ViewerEditPartContributorFactory {

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/viewer_overlay.gif")
			.getImageData();

	protected static class TreeViewerTreeEditPartContributor extends ViewerTreeEditPartContributor {

		/**
		 * @param beanTreeEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		protected TreeViewerTreeEditPartContributor(TreeEditPart beanTreeEditPart, EReference controlFeature) {
			super(beanTreeEditPart, controlFeature);
		}

		public ImageOverlay getImageOverlay() {
			return hasViewer ? new ImageOverlay(OVERLAY_IMAGEDATA) : null;
		}

		public String modifyText(String text) {
			return hasViewer ? MessageFormat.format("{0} (with TreeViewer attached)", new Object[] {text}) : text;
		}
	}
	
	private static URI TREE_FEATURE_URI = URI.createURI("java:/org.eclipse.jface.viewers#TreeViewer/tree");

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {
		return new TreeViewerTreeEditPartContributor(treeEditPart, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(treeEditPart)), TREE_FEATURE_URI));
	}

	protected static Image TREE_VIEWER_OVERLAY_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer2_overlay.gif");
	protected static Image TREE_VIEWER_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer_obj.gif");

	protected static class TreeViewerGraphicalEditPartContributor extends ViewerGraphicalEditPartContributor {
		/**
		 * @param controlEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		public TreeViewerGraphicalEditPartContributor(GraphicalEditPart controlEditPart, EReference controlFeature) {
			super(controlEditPart, controlFeature);
		}

		public ToolTipProcessor getHoverOverLay() {
			if (viewer != null)
				return new ToolTipProcessor.ToolTipLabel("Select TreeViewer in action bar to show Viewer properties");
			else
				return new ToolTipProcessor.ToolTipLabel("Press action in action bar to convert to a TreeViewer");
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerOverlayImage()
		 */
		protected Image getViewerOverlayImage() {
			return TREE_VIEWER_OVERLAY_IMAGE;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerImage()
		 */
		protected Image getViewerImage() {
			return TREE_VIEWER_IMAGE;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getActionTextForCreateViewerButton()
		 */
		protected String getActionTextForCreateViewerButton() {
			return "Press here to attach a TreeViewer";
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		return new TreeViewerGraphicalEditPartContributor(graphicalEditPart, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(graphicalEditPart)), TREE_FEATURE_URI));
	}

}