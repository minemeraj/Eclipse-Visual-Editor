package org.eclipse.ve.example.customwidget.prompter;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.SWTEventListener;

public interface ButtonSelectionListener extends SWTEventListener {

	public void buttonSelected(SelectionEvent e);
		
}
