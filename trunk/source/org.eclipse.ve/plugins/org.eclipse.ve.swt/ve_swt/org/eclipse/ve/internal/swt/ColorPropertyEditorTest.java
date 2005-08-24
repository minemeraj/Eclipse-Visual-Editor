/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * @author jmyers
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ColorPropertyEditorTest {
	
	private static ColorPropertyEditor cc;

	public static void main(String[] args) {
			Display display = new Display ();
			Shell shell = new Shell(display);
			shell.setText(ColorPropertyEditorMessages.ColorPropertyEditorTest_Shell_title); 
			
			GridLayout grid = new GridLayout();
			grid.numColumns = 1;
			grid.verticalSpacing = 5;
			shell.setLayout(grid);
					
			cc = new ColorPropertyEditor();
			Control c = cc.createControl(shell, SWT.NONE);
			GridData gd1 = new GridData();
			gd1.grabExcessHorizontalSpace = true;
			gd1.grabExcessVerticalSpace = true;
			gd1.horizontalAlignment = GridData.FILL;
			gd1.verticalAlignment = GridData.FILL;
			c.setLayoutData(gd1);
			
			Composite okCancel = new Composite(shell, SWT.NONE);
			okCancel.setLayout(new FillLayout());
			GridData gd2 = new GridData();
			gd2.grabExcessHorizontalSpace = true;
			gd2.grabExcessVerticalSpace = false;
			gd2.horizontalAlignment = GridData.END;
			gd2.verticalAlignment = GridData.FILL;
			okCancel.setLayoutData(gd2);
			
			Button ok = new Button(okCancel, SWT.PUSH);
			ok.setText(ColorPropertyEditorMessages.ColorPropertyEditorTest_OK); 
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println(cc.getJavaInitializationString());
					e.widget.getDisplay().dispose();
					System.exit(0);
				}
			});
			
			Button cancel = new Button(okCancel, SWT.PUSH);
			cancel.setText(ColorPropertyEditorMessages.ColorPropertyEditorTest_Cancel); 
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					e.widget.getDisplay().dispose();
					System.exit(0);
				}
			});
			
			shell.pack();
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			display.dispose ();
		}
}
