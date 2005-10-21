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

/**
 * Contributor factory for JFace TreeViewer.
 * 
 * @since 1.2.0
 */
public class TreeViewerEditPartContributorFactory implements AdaptableContributorFactory {

	public static IJavaInstance getTreeViewer(IJavaInstance aTree) {

		// See whether this has a TreeViewer pointing to it or not
		JavaClass treeViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.TreeViewer", aTree.eResource().getResourceSet());
		return (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(aTree, (EReference) treeViewerClass
				.getEStructuralFeature("tree"));
	}

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer_overlay.gif")
			.getImageData();

	private static class TreeViewerTreeEditPartContributor extends AbstractEditPartContributor implements TreeEditPartContributor {

		public void dispose() {
		}

		public ImageOverlay getImageOverlay() {
			return new ImageOverlay(OVERLAY_IMAGEDATA);
		}

		public void appendToText(StringBuffer buffer) {
			buffer.append("with TreeViewer");
		}
	}

	public TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart) {

		Object treeViewer = getTreeViewer((IJavaInstance) treeEditPart.getModel());
		if (treeViewer != null) {
			// This tree is pointed to by a tree viewer
			return new TreeViewerTreeEditPartContributor();
		}
		return null;
	}

	static Image treeViewerOverlayImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer_overlay.gif");
	static Image treeOverlayImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/tree_overlay.gif");
	static Image treeViewerImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer_obj.gif");
	static Image noTreeViewerImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/no_treeviewer_obj2.gif");

	private static class TreeViewerGraphicalEditPartContributor extends AbstractEditPartContributor implements GraphicalEditPartContributor {

		private Object tree;
		private Object treeViewer;
		private GraphicalEditPart treeEditPart;

		public TreeViewerGraphicalEditPartContributor(GraphicalEditPart treeEditPart, Object treeViewer) {
			this.treeEditPart = treeEditPart;
			this.tree = treeEditPart.getModel();
			this.treeViewer = treeViewer;
		}

		public void dispose() {
		}

		public ToolTipProcessor getHoverOverLay() {
			treeViewer = getTreeViewer((IJavaInstance)tree);
			if (treeViewer != null)
				return new ToolTipProcessor.ToolTipLabel("Select TreeViewer in action bar to show Viewer properties");
			else
				return new ToolTipProcessor.ToolTipLabel("Press action in action bar to convert to a TreeViewer");
		}

		/*
		 * Return an overlay image for the tree viewer only
		 */
		public IFigure getFigureOverLay() {
			treeViewer = getTreeViewer((IJavaInstance)tree);
			if (treeViewer == null)
				return null;
			final Image image = treeViewerOverlayImage;
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
			// If this tree has an associated TreeViewer, return an editpart with the treeviewer as it's model
			if (treeViewer != null)
				return new GraphicalEditPart[] { new JavaBeanGraphicalEditPart(treeViewer) {

					protected IFigure createFigure() {
						Label label = (Label) super.createFigure();
						ImageFigure fig = new ImageFigure();
						fig.setImage(treeViewerImage);
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
			
			// No Treeviewer... return an action editpart that can be selected to promote this tree to a tree viewer.
			else
				return new GraphicalEditPart[] { new ActionBarActionEditPart("Press here to convert to a TreeViewer") {

					// Create and execute commands to promote this Tree to a JFace TreeViewer
					public void run() {
						CreateRequest cr = new CreateRequest();
						cr.setFactory(new EMFCreationFactory(JavaRefFactory.eINSTANCE.reflectType("org.eclipse.jface.viewers.TreeViewer",
								(EObject) tree)));
						Command c = treeEditPart.getCommand(cr);
						if (c != null) {
							EditDomain.getEditDomain(treeEditPart).getCommandStack().execute(c);
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									treeViewer = getTreeViewer((IJavaInstance) tree);
									notifyListeners();
								}
							});
						}
					}
				}};
		}

	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		Object tree = graphicalEditPart.getModel();
		return new TreeViewerGraphicalEditPartContributor(graphicalEditPart, getTreeViewer((IJavaInstance)tree));
	}

}