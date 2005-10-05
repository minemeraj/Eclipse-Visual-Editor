package org.eclipse.ve.internal.jface;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.ImageFigure;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.JavaBeanGraphicalEditPart;

import org.eclipse.ve.internal.swt.SwtPlugin;

/**
 * Contributor factory for JFace TreeViewer.
 * 
 * @since 1.2.0
 */
public class TreeViewerEditPartContributorFactory implements AdaptableContributorFactory {

	private IJavaInstance getTreeViewer(IJavaInstance aTree) {

		// See whether this has a TreeViewer pointing to it or not
		JavaClass treeViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.TreeViewer", aTree.eResource().getResourceSet());
		return (IJavaInstance) InverseMaintenanceAdapter.getFirstReferencedBy(aTree, (EReference) treeViewerClass
				.getEStructuralFeature("tree"));
	}

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/treeviewer_overlay.gif")
			.getImageData();

	private static class TreeViewerTreeEditPartContributor implements TreeEditPartContributor {

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
	static Image noTreeViewerImage = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/full/clcl16/no_treeviewer_obj.gif");

	private static class TreeViewerGraphicalEditPartContributor implements GraphicalEditPartContributor {

		JavaBeanGraphicalEditPart treeViewerEditpart = null;

		private Object tree;
		private Object treeViewer;

		public TreeViewerGraphicalEditPartContributor(Object tree, Object treeViewer) {
			this.tree = tree;
			this.treeViewer = treeViewer;
		}

		public void dispose() {
			if (treeViewerEditpart != null)
				treeViewerEditpart.deactivate();
		}

		public IFigure getHoverOverLay() {
			if (treeViewer != null)
				return new Label("I am a Tree with a Tree Viewer");
			else
				return new Label("I am a Tree without a Tree Viewer");
		}

		public IFigure getFigureOverLay() {
			final Image image = treeViewer != null ? treeViewerOverlayImage : treeOverlayImage;
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
			fig.setVisible(true);
			return fig;
		}

		public GraphicalEditPart[] getActionBarChildren() {
			if (treeViewer != null)
				return new GraphicalEditPart[] {new JavaBeanGraphicalEditPart(treeViewer) {
					protected IFigure createFigure() {
						Label label = (Label)super.createFigure();
						ImageFigure fig = new ImageFigure();
						fig.setImage(treeViewerImage);
						fig.add(fErrorIndicator);
						fig.setToolTip(label.getToolTip());
						return fig;
					}
					protected void setupLabelProvider() {
						// don't do anything here, we'll provide our own image.
					}
				}};
			else return new GraphicalEditPart[] {new JavaBeanGraphicalEditPart(tree) {
				protected IFigure createFigure() {
					Label label = (Label)super.createFigure();
					ImageFigure fig = new ImageFigure();
					fig.setImage(noTreeViewerImage);
					fig.add(fErrorIndicator);
					label.getToolTip().add(new Label("Make me into TreeViewer"));
					fig.setToolTip(label.getToolTip());
					return fig;
				}
				protected void setupLabelProvider() {
					// don't do anything here, we'll provide our own image.
				}
			}};
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {
		Object tree = graphicalEditPart.getModel();
		return new TreeViewerGraphicalEditPartContributor(tree, getTreeViewer((IJavaInstance)tree));
	}

}