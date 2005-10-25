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
 *  $Revision: 1.2 $  $Date: 2005-10-25 19:12:43 $ 
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

public class TableViewerEditPartContributorFactory implements AdaptableContributorFactory {

	public static IJavaInstance getTableViewer(IJavaInstance aTable) {

		// See whether this has a TreeViewer pointing to it or not
		JavaClass tableViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.TableViewer", aTable.eResource().getResourceSet());
		return (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(aTable, (EReference) tableViewerClass.getEStructuralFeature("table"));
	}

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(),
			"icons/full/clcl16/viewer_overlay.gif").getImageData();
	private static Image tableViewerOverlayImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/tableviewer_overlay.gif");
	private static Image tableViewerImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/tableviewer_obj.gif");

	private static class TableViewerTreeEditPartContributor extends AbstractEditPartContributor implements TreeEditPartContributor {

		public void dispose() {
		}

		public ImageOverlay getImageOverlay() {
			return new ImageOverlay(OVERLAY_IMAGEDATA);
		}

		public void appendToText(StringBuffer buffer) {
			buffer.append("with TableViewer");
		}
	}

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart tableEditPart) {

		Object tableViewer = getTableViewer((IJavaInstance) tableEditPart.getModel());
		if (tableViewer != null) {
			// This Table is pointed to by a table viewer
			return new TableViewerTreeEditPartContributor();
		}
		return null;
	}

	private static class TableViewerGraphicalEditPartContributor extends AbstractEditPartContributor implements GraphicalEditPartContributor {
		private Object table;
		private Object tableViewer;
		private GraphicalEditPart tableEditPart;

		public TableViewerGraphicalEditPartContributor(GraphicalEditPart tableEditPart, Object tableViewer) {
			this.tableEditPart = tableEditPart;
			this.table = tableEditPart.getModel();
			this.tableViewer = tableViewer;
		}

		public void dispose() {
		}

		public ToolTipProcessor getHoverOverLay() {
			tableViewer = getTableViewer((IJavaInstance) table);
			if (tableViewer != null)
				return new ToolTipProcessor.ToolTipLabel("Select TableViewer in action bar to show Viewer properties");
			else
				return new ToolTipProcessor.ToolTipLabel("Press action in action bar to convert to a TableViewer");
		}

		/*
		 * Return an overlay image for the table viewer only
		 */
		public IFigure getFigureOverLay() {
			tableViewer = getTableViewer((IJavaInstance) table);
			if (tableViewer == null)
				return null;
			final Image image = tableViewerOverlayImage;
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
			// If this table has an associated TableViewer, return an editpart with the tableviewer as it's model
			if (tableViewer != null)
				return new GraphicalEditPart[] { new JavaBeanGraphicalEditPart(tableViewer) {

					protected IFigure createFigure() {
						Label label = (Label) super.createFigure();
						ImageFigure fig = new ImageFigure();
						fig.setImage(tableViewerImage);
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

			// No Tableviewer... return an action editpart that can be selected to promote this table to a table viewer.
			else
				return new GraphicalEditPart[] { new ActionBarActionEditPart("Press here to convert to a TableViewer") {

					// Create and execute commands to promote this Table to a JFace TableViewer
					public void run() {
						CreateRequest cr = new CreateRequest();
						cr.setFactory(new EMFCreationFactory(JavaRefFactory.eINSTANCE.reflectType("org.eclipse.jface.viewers.TableViewer",
								(EObject) table)));
						Command c = tableEditPart.getCommand(cr);
						if (c != null) {
							EditDomain.getEditDomain(tableEditPart).getCommandStack().execute(c);
							Display.getDefault().asyncExec(new Runnable() {

								public void run() {
									tableViewer = getTableViewer((IJavaInstance) table);
									notifyListeners();
								}
							});
						}
					}
				}};
		}

	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		Object table = graphicalEditPart.getModel();
		return new TableViewerGraphicalEditPartContributor(graphicalEditPart, getTableViewer((IJavaInstance) table));
	}
}
