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
 *  $RCSfile: TableViewerEditPartContributorFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-12-14 19:23:39 $ 
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
 * Tableviewer JFace edit part contributor.
 * 
 * @since 1.2.0
 */
public class TableViewerEditPartContributorFactory extends ViewerEditPartContributorFactory {

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/viewer_overlay.gif").getImageData(); //$NON-NLS-1$

	protected static class TableViewerTreeEditPartContributor extends ViewerTreeEditPartContributor {

		/**
		 * @param beanTreeEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		protected TableViewerTreeEditPartContributor(TreeEditPart beanTreeEditPart, EReference controlFeature) {
			super(beanTreeEditPart, controlFeature);
		}

		public ImageOverlay getImageOverlay() {
			return hasViewer ? new ImageOverlay(OVERLAY_IMAGEDATA) : null;
		}

		public String modifyText(String text) {
			return hasViewer ? MessageFormat.format(JFaceMessages.TableViewerEditPartContributorFactory_WithTableViewer_Msg, new Object[] { text}) : text;
		}
	}

	private static URI TABLE_FEATURE_URI = URI.createURI("java:/org.eclipse.jface.viewers#TableViewer/table"); //$NON-NLS-1$

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {
		return new TableViewerTreeEditPartContributor(treeEditPart, JavaInstantiation.getReference(EMFEditDomainHelper.getResourceSet(EditDomain
				.getEditDomain(treeEditPart)), TABLE_FEATURE_URI));
	}

	protected static Image TABLE_VIEWER_OVERLAY_IMAGE = CDEPlugin
			.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/tableviewer_overlay.gif"); //$NON-NLS-1$

	protected static Image TABLE_VIEWER_IMAGE = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/tableviewer_obj.gif"); //$NON-NLS-1$

	protected static class TableViewerGraphicalEditPartContributor extends ViewerGraphicalEditPartContributor {

		/**
		 * @param controlEditPart
		 * @param controlFeature
		 * 
		 * @since 1.2.0
		 */
		public TableViewerGraphicalEditPartContributor(GraphicalEditPart controlEditPart, EReference controlFeature) {
			super(controlEditPart, controlFeature);
		}

		public ToolTipProcessor getHoverOverLay() {
			if (viewer != null)
				return new ToolTipProcessor.ToolTipLabel(JFaceMessages.TableViewerEditPartContributorFactory_TooltipLabel_SelectViewer_Msg);
			else
				return new ToolTipProcessor.ToolTipLabel(JFaceMessages.TableViewerEditPartContributorFactory_TooltipLabel_ConvertToViewer_Msg);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerOverlayImage()
		 */
		protected Image getViewerOverlayImage() {
			return TABLE_VIEWER_OVERLAY_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getViewerImage()
		 */
		protected Image getViewerImage() {
			return TABLE_VIEWER_IMAGE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ve.internal.jface.ViewerEditPartContributorFactory.ViewerGraphicalEditPartContributor#getActionTextForCreateViewerButton()
		 */
		protected String getActionTextForCreateViewerButton() {
			return JFaceMessages.TableViewerEditPartContributorFactory_Button_AttachViewer_Text;
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		return new TableViewerGraphicalEditPartContributor(graphicalEditPart, JavaInstantiation.getReference(EMFEditDomainHelper
				.getResourceSet(EditDomain.getEditDomain(graphicalEditPart)), TABLE_FEATURE_URI));
	}

}
