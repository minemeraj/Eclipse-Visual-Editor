package org.eclipse.ve.internal.cde.core;

import org.eclipse.gef.EditPart;

public abstract class EditPartContributorRegistry {
	
	public abstract void treeEditPartActivated(EditPart anEditPart);
	
	public abstract void graphicalEditPartActivated(EditPart anEditPart);

}
