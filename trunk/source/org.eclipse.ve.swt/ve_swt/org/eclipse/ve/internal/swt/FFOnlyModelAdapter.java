/*
 * Created on 19-Sep-03
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.core.IContainmentHandler;
;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FFOnlyModelAdapter extends ControlModelAdapter implements IContainmentHandler {
	
	public FFOnlyModelAdapter(Object model) {
		super(model);
	}
	
	public boolean isParentValid(Object parent) {
		// return true only for parents that are the freeform surface
		return parent instanceof DiagramData;
	}	

}
