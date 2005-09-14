package org.eclipse.ve.internal.jface;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.*;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.DefaultTreeEditPart;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

public class TreeViewerEditPartContributor implements EditPartContributor {
	static final Image fImage = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/test_overlay.gif");

	public void treeEditPartActivated(DefaultTreeEditPart treeEditPart) {

		Object model = treeEditPart.getModel();
		if(model instanceof IJavaInstance){
			IJavaInstance javaModel = (IJavaInstance)model;
			if(javaModel.getJavaType().getQualifiedName().equals("org.eclipse.swt.widgets.Tree")){
				// See whether this has a TreeViewer pointing to it or not
				JavaClass treeViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.TreeViewer",javaModel.eResource().getResourceSet());
				Object treeViewer = InverseMaintenanceAdapter.getFirstReferencedBy(javaModel,(EReference) treeViewerClass.getEStructuralFeature("tree"));
				if(treeViewer != null){
					// This tree is pointed to by a tree viewer
					treeEditPart.addEditPartContributor(new TreeEditPartContributor(){
						public ImageOverlay getImageOverlay() {
							ImageOverlay result = new ImageOverlay();
							result.fImageData = fImage.getImageData(); //$NON-NLS-1$
							result.location = new Point(result.fImageData.width,result.fImageData.height);
							return result;
						}
						public void appendToText(StringBuffer buffer) {
							buffer.append("with TreeViewer");
						}
					});
				}
			}
		}
	}

	public void graphicalEditPartActivated(CDEAbstractGraphicalEditPart graphicalEditPart) {
		Object model = graphicalEditPart.getModel();
		if(model instanceof IJavaInstance){
			IJavaInstance javaModel = (IJavaInstance)model;
			if(javaModel.getJavaType().getQualifiedName().equals("org.eclipse.swt.widgets.Tree")){
				// See whether this has a TreeViewer pointing to it or not
				JavaClass treeViewerClass = Utilities.getJavaClass("org.eclipse.jface.viewers.TreeViewer",javaModel.eResource().getResourceSet());
				Object treeViewer = InverseMaintenanceAdapter.getFirstReferencedBy(javaModel,(EReference) treeViewerClass.getEStructuralFeature("tree"));
				if(treeViewer != null){
					// This tree is pointed to by a tree viewer
					graphicalEditPart.addEditPartContributor(new GraphicalEditPartContributor(){
						public IFigure getHoverOverLay() {
							return new Label("I am a Tree with a JFace Tree Viewer");
						}

						public IFigure getFigureOverLay() {
							Rectangle bounds = fImage.getBounds();
							IFigure fig = new Figure () {
								protected void paintFigure(Graphics graphics) {
									super.paintFigure(graphics);
									if (fImage != null) {
										// Clear some background so the image can be seen
										graphics.drawImage(fImage, getLocation().x+2, getLocation().y+2);
								}
							}};
							fig.setSize(new Dimension(bounds.width + 2, bounds.height + 2));
							fig.setVisible(true);
							
							return fig;
						}
					});
				}
			}
		}
		
	}
}
