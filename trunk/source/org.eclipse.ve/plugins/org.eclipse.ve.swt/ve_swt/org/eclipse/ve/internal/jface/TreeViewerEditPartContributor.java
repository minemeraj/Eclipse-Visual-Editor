package org.eclipse.ve.internal.jface;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.graphics.Point;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.DefaultTreeEditPart;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

public class TreeViewerEditPartContributor implements EditPartContributor {

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
							result.fImageData = CDEPlugin.getImageDescriptorFromPlugin(CDEPlugin.getPlugin(), "icons/full/cview16/test_overlay.gif").getImageData(); //$NON-NLS-1$
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
}
