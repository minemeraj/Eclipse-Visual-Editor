/*
 * Created on 19-Sep-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.gef.commands.Command;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cdm.model.Rectangle;

import org.eclipse.jem.internal.instantiation.base.*;

class ControlModelAdapter implements IConstraintHandler {
	
	protected IJavaObjectInstance control;		

	public ControlModelAdapter(Object aControl) {
		control = (IJavaObjectInstance) aControl;
	}   	
	public void addConstraintHandlerListener(IConstraintHandlerListener listener) {
	}
	public void removeConstraintHandlerListener(IConstraintHandlerListener listener) {
	}
	public void contributeFigureSize(org.eclipse.draw2d.geometry.Rectangle figureConstraint) {
	}		
	public void contributeModelSize(Rectangle modelConstraint) {
	}
	public Command contributeOrphanChildCommand() {
		return null;
	}
	public Command contributeSizeCommand(int width, int height, EditDomain domain) {
		// We want size to be set in the size/bounds of the object.
		ApplyNullLayoutConstraintCommand cmd = new ApplyNullLayoutConstraintCommand();
		cmd.setTarget(control);
		cmd.setDomain(domain);
		cmd.setConstraint(new org.eclipse.draw2d.geometry.Rectangle(0, 0, width, height), false, true);
		return cmd;
	}		
	public boolean isResizeable() {
		return true;
	}		
}