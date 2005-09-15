package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.TreeEditPart;

public interface EditPartContributor {
	
	/**
	 * 
	 * @param treeEditPart A tree edit part that matches the filter for the extension
	 * @return a tree edit part contributor
	 * 
	 * @since 1.2.0
	 */
	TreeEditPartContributor getTreeEditPartContributor(TreeEditPart treeEditPart);
	
	GraphicalEditPartContributor getGraphicalEditPartContributor(GraphicalEditPart graphicalEditPart);	
	
}
