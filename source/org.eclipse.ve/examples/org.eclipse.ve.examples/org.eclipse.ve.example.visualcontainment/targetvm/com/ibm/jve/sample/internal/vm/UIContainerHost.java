/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.jve.sample.internal.vm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.internal.swt.targetvm.PreventShellCloseMinimizeListener;

import com.ibm.jve.sample.core.UIContainer;

public class UIContainerHost {
	
private static Shell shell;
public static int fx = 0;
public static int fy = 0;
public static int MIN_X = 100;
public static int MIN_Y = 75;

public static Composite addContainerHost(UIContainer aUIContainer){
	
	Composite parent = new Composite(getWorkbenchShell(),SWT.NONE){
		public Point computeSize(int wHint,int hHint,boolean changed){
			Point preferredSize = super.computeSize(wHint,hHint, changed);
			Point result = new Point(
					preferredSize.x > MIN_X ? preferredSize.x : MIN_X,
					preferredSize.y > MIN_Y ? preferredSize.y : MIN_Y
					);
			return result;			
		}
	};
	parent.setLayout(new FillLayout());	
	aUIContainer.createContents(parent);
	getWorkbenchShell().layout(true);	
	return parent;

}

public static void removeContainerHost(UIContainer aContainer){
	
	aContainer.getUIContainer().dispose();
	
}

public static void layoutUIContainerComposite(Composite aComposite){
	
	aComposite.pack();
	getWorkbenchShell().layout(true);	
	
}

protected static Composite getWorkbenchShell(){
	
	if(shell == null){
//		Shell dialogParent = new Shell();
//		shell = new Shell(dialogParent,SWT.SHELL_TRIM);
		shell = new Shell();
		shell.addShellListener(new PreventShellCloseMinimizeListener());
		shell.setBounds(fx,fy,200,200);
		shell.setText("UIContainerHost");
		shell.setLayout(new RowLayout());
		shell.open();
	}
	
	return shell;
}


}
