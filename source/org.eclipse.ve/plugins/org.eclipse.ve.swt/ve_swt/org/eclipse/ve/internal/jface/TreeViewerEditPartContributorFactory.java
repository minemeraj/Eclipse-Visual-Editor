package org.eclipse.ve.internal.jface;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.swt.graphics.*;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

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

	private static ImageData OVERLAY_IMAGEDATA = CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/test_overlay.gif")
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

	static Image fImage = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/test_overlay.gif");

	private static class TreeViewerGraphicalEditPartContributor implements GraphicalEditPartContributor {

		public void dispose() {
		}

		public IFigure getHoverOverLay() {
			return new Label("I am a Tree with a JFace Tree Viewer");
		}

		public IFigure getFigureOverLay() {
			Rectangle bounds = fImage.getBounds();
			IFigure fig = new Figure() {

				protected void paintFigure(Graphics graphics) {
					super.paintFigure(graphics);
					if (fImage != null) {
						// Clear some background so the image can be seen
						graphics.drawImage(fImage, getLocation().x + 2, getLocation().y + 2);
					}
				}
			};
			fig.setSize(new Dimension(bounds.width + 2, bounds.height + 2));
			fig.setVisible(true);
			return fig;
		}
	}

	public GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart) {

		Object treeViewer = getTreeViewer((IJavaInstance) graphicalEditPart.getModel());

		if (treeViewer != null) {
			return new TreeViewerGraphicalEditPartContributor();
		} else {
			return null;
		}
	}

}