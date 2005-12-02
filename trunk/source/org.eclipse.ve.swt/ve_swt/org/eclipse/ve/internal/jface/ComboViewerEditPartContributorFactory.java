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
 *  $RCSfile: ComboViewerEditPartContributorFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-12-02 21:17:45 $ 
 */
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
 * ComboViewer JFace Editpart contributor.
 * 
 * @since 1.2.0
 */
public class ComboViewerEditPartContributorFactory extends ViewerEditPartContributorFactory {

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/viewer_overlay.gif").getImageData();

	protected static class ComboViewerTreeEditPartContributor extends ViewerTreeEditPartContributor {

		/**
		 * @param beanTreeEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		protected ComboViewerTreeEditPartContributor(TreeEditPart beanTreeEditPart, EReference controlFeature) {
			super(beanTreeEditPart, controlFeature);
		}

		public ImageOverlay getImageOverlay() {
			return hasViewer ? new ImageOverlay(OVERLAY_IMAGEDATA) : null;
		}

		public String modifyText(String text) {
			return hasViewer ? MessageFormat.format("{0} (with TableViewer attached)", new Object[] { text}) : text;
		}
	}

	private static URI COMBO_FEATURE_URI = URI.createURI("java:/org.eclipse.jface.viewers#ComboViewer/combo");

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {
		return new ComboViewerTreeEditPartContributor(treeEditPart, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(EditDomain
				.getEditDomain(treeEditPart)), COMBO_FEATURE_URI));
	}

	protected static Image COMBO_VIEWER_OVERLAY_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/comboviewer_overlay.gif");

	protected static Image COMBO_VIEWER_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/comboviewer_obj.gif");

	protected static class ComboViewerGraphicalEditPartContributor extends ViewerGraphicalEditPartContributor {

		/**
		 * @param controlEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		public ComboViewerGraphicalEditPartContributor(GraphicalEditPart controlEditPart, EReference controlFeature) {
			super(controlEditPart, controlFeature);
		}

		public ToolTipProcessor getHoverOverLay() {
			if (viewer != null)
				return new ToolTipProcessor.ToolTipLabel("Select ComboViewer in action bar to show Viewer properties");
			else
				return new ToolTipProcessor.ToolTipLabel("Press action in action bar to convert to a ComboViewer");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerOverlayImage()
		 */
		protected Image getViewerOverlayImage() {
			return COMBO_VIEWER_OVERLAY_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerImage()
		 */
		protected Image getViewerImage() {
			return COMBO_VIEWER_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getActionTextForCreateViewerButton()
		 */
		protected String getActionTextForCreateViewerButton() {
			return "Press here to attach a ComboViewer";
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		return new ComboViewerGraphicalEditPartContributor(graphicalEditPart, JavaInstantiation.getReference(EMFEditDomainHelper
				.getResourceSet(EditDomain.getEditDomain(graphicalEditPart)), COMBO_FEATURE_URI));
	}

}
