/*
 * Created on 28-Feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.ve.example.customwidget.prompter;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.SWTEventListener;
;

/**
 * @author JoeWinchester
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ButtonSelectionListener extends SWTEventListener {

	public void buttonSelected(SelectionEvent e);
	
	
}
