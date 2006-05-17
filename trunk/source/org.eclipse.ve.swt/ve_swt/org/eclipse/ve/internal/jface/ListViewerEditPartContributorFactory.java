/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ListViewerEditPartContributorFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2006-05-17 20:15:54 $ 
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
 * Listviewer JFace edit part contributor.
 * 
 * @since 1.2.0
 */
public class ListViewerEditPartContributorFactory extends ViewerEditPartContributorFactory {

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/viewer_overlay.gif").getImageData(); //$NON-NLS-1$

	protected static class ListViewerTreeEditPartContributor extends ViewerTreeEditPartContributor {

		/**
		 * @param beanTreeEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		protected ListViewerTreeEditPartContributor(TreeEditPart beanTreeEditPart, EReference controlFeature) {
			super(beanTreeEditPart, controlFeature);
		}

		public ImageOverlay getImageOverlay() {
			return hasViewer ? new ImageOverlay(OVERLAY_IMAGEDATA) : null;
		}

		public String modifyText(String text) {
			return hasViewer ? MessageFormat.format(JFaceMessages.ListViewerEditPartContributorFactory_WithViewer_Msg, new Object[] { text}) : text;
		}
	}

	private static URI LIST_FEATURE_URI = URI.createURI("java:/org.eclipse.jface.viewers#ListViewer/list"); //$NON-NLS-1$

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {
		return new ListViewerTreeEditPartContributor(treeEditPart, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(EditDomain
				.getEditDomain(treeEditPart)), LIST_FEATURE_URI));
	}

	protected static Image LIST_VIEWER_OVERLAY_IMAGE = CDEPlugin
			.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/listviewer_overlay.gif"); //$NON-NLS-1$

	protected static Image LIST_VIEWER_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/listviewer_obj.gif"); //$NON-NLS-1$

	protected static class ListViewerGraphicalEditPartContributor extends ViewerGraphicalEditPartContributor {

		/**
		 * @param controlEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		public ListViewerGraphicalEditPartContributor(GraphicalEditPart controlEditPart, EReference controlFeature) {
			super(controlEditPart, controlFeature);
		}

		public ToolTipProcessor getHoverOverLay() {
			if (viewer != null)
				return new ToolTipProcessor.ToolTipLabel(JFaceMessages.ListViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg);
			else
				return new ToolTipProcessor.ToolTipLabel(JFaceMessages.ListViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerOverlayImage()
		 */
		protected Image getViewerOverlayImage() {
			return LIST_VIEWER_OVERLAY_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerImage()
		 */
		protected Image getViewerImage() {
			return LIST_VIEWER_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getActionTextForCreateViewerButton()
		 */
		protected String getActionTextForCreateViewerButton() {
			return JFaceMessages.ListViewerEditPartContributorFactory_Button_AttachViewer_Text;
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		return new ListViewerGraphicalEditPartContributor(graphicalEditPart, JavaInstantiation.getReference(EMFEditDomainHelper
				.getResourceSet(EditDomain.getEditDomain(graphicalEditPart)), LIST_FEATURE_URI));
	}

}
