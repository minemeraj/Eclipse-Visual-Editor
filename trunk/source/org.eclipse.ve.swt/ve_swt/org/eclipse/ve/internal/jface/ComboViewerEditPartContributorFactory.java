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
 *  $Revision: 1.1 $  $Date: 2005-10-25 19:12:43 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.ve.internal.cde.emf.EMFCreationFactory;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaBeanGraphicalEditPart;

import org.eclipse.ve.internal.swt.SwtPlugin;

public class ComboViewerEditPartContributorFactory implements AdaptableContributorFactory {

	public static IJavaInstance getComboViewer(IJavaInstance aCombo) {

		// See whether this has a TreeViewer pointing to it or not
		JavaClass comboViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.ComboViewer", aCombo.eResource().getResourceSet());
		return (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(aCombo, (EReference) comboViewerClass.getEStructuralFeature("combo"));
	}

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/viewer_overlay.gif").getImageData();
	private static Image comboViewerOverlayImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/comboviewer_overlay.gif");
	private static Image comboViewerImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/comboviewer_obj.gif");

	private static class ComboViewerTreeEditPartContributor extends AbstractEditPartContributor implements TreeEditPartContributor {

		public void dispose() {
		}

		public ImageOverlay getImageOverlay() {
			return new ImageOverlay(OVERLAY_IMAGEDATA);
		}

		public void appendToText(StringBuffer buffer) {
			buffer.append("with ComboViewer");
		}
	}

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart comboEditPart) {

		Object comboViewer = getComboViewer((IJavaInstance) comboEditPart.getModel());
		if (comboViewer != null) {
			// This Combo is pointed to by a combo viewer
			return new ComboViewerTreeEditPartContributor();
		}
		return null;
	}

	private static class ComboViewerGraphicalEditPartContributor extends AbstractEditPartContributor implements GraphicalEditPartContributor {
		private Object combo;
		private Object comboViewer;
		private GraphicalEditPart comboEditPart;

		public ComboViewerGraphicalEditPartContributor(GraphicalEditPart comboEditPart, Object comboViewer) {
			this.comboEditPart = comboEditPart;
			this.combo = comboEditPart.getModel();
			this.comboViewer = comboViewer;
		}

		public void dispose() {
		}

		public ToolTipProcessor getHoverOverLay() {
			comboViewer = getComboViewer((IJavaInstance) combo);
			if (comboViewer != null)
				return new ToolTipProcessor.ToolTipLabel("Select ComboViewer in action bar to show Viewer properties");
			else
				return new ToolTipProcessor.ToolTipLabel("Press action in action bar to convert to a ComboViewer");
		}

		/*
		 * Return an overlay image for the combo viewer only
		 */
		public IFigure getFigureOverLay() {
			comboViewer = getComboViewer((IJavaInstance) combo);
			if (comboViewer == null)
				return null;
			final Image image = comboViewerOverlayImage;
			org.eclipse.swt.graphics.Rectangle bounds = image.getBounds();
			IFigure fig = new Figure() {

				protected void paintFigure(Graphics graphics) {
					super.paintFigure(graphics);
					if (image != null) {
						// Clear some background so the image can be seen
						graphics.drawImage(image, getLocation().x + 2, getLocation().y + 2);
					}
				}
			};
			fig.setSize(new Dimension(bounds.width + 2, bounds.height + 2));
			fig.setLocation(new Point(-1, -1));
			fig.setVisible(true);
			return fig;
		}

		public GraphicalEditPart[] getActionBarChildren() {
			// If this combo has an associated ComboViewer, return an editpart with the comboviewer as it's model
			if (comboViewer != null)
				return new GraphicalEditPart[] { new JavaBeanGraphicalEditPart(comboViewer) {

					protected IFigure createFigure() {
						Label label = (Label) super.createFigure();
						ImageFigure fig = new ImageFigure();
						fig.setImage(comboViewerImage);
						fig.add(fErrorIndicator);
						fig.setToolTip(label.getToolTip());
						fig.setCursor(Cursors.HAND);
						fig.setPreferredSize(fig.getPreferredSize().width + 1, fig.getPreferredSize().height);
						return fig;
					}

					protected void setupLabelProvider() {
						// don't do anything here, we'll provide our own image.
					}
				}};

			// No Comboviewer... return an action editpart that can be selected to promote this combo to a combo viewer.
			else
				return new GraphicalEditPart[] { new ActionBarActionEditPart("Press here to convert to a ComboViewer") {

					// Create and execute commands to promote this Combo to a JFace ComboViewer
					public void run() {
						CreateRequest cr = new CreateRequest();
						cr.setFactory(new EMFCreationFactory(JavaRefFactory.eINSTANCE.reflectType("org.eclipse.jface.viewers.ComboViewer",
								(EObject) combo)));
						Command c = comboEditPart.getCommand(cr);
						if (c != null) {
							EditDomain.getEditDomain(comboEditPart).getCommandStack().execute(c);
							Display.getDefault().asyncExec(new Runnable() {

								public void run() {
									comboViewer = getComboViewer((IJavaInstance) combo);
									notifyListeners();
								}
							});
						}
					}
				}};
		}

	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		Object combo = graphicalEditPart.getModel();
		return new ComboViewerGraphicalEditPartContributor(graphicalEditPart, getComboViewer((IJavaInstance) combo));
	}

}
