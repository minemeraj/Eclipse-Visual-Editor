/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComboButton.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-12 18:50:01 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
 
/**
 * 
 * @since 1.0.0
 */
public class ComboButton extends Composite {
	
	private Button button;
	private CCombo combo;
	
	public ComboButton(Composite parent, int styleBits){
		super(parent,styleBits);
		createContents();
	}
	private void createContents(){
		combo = new CCombo(this,SWT.NONE);
		button = new Button(this,SWT.DOWN);
		button.setText("...");
		addControlListener(new ControlListener(){
			public void controlResized(ControlEvent e) {
				internalLayout();
			}
			public void controlMoved(ControlEvent e) {				
			}
		});
	}
	
	private void internalLayout(){
		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;
		Point buttonSize = button.computeSize(SWT.DEFAULT, height);
		Point comboSize = combo.computeSize(SWT.DEFAULT,height);
		combo.setBounds (0, 0, width - buttonSize.x, height);
		button.setBounds (width - buttonSize.x, 0, buttonSize.x, comboSize.y);
		
	}
	
	public Point computeSize (int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 0, height = 0;
		Point comboSize = combo.computeSize (wHint, SWT.DEFAULT, changed);
		Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		int borderWidth = getBorderWidth();
		
		height = Math.max (hHint, comboSize.y + 2*borderWidth);
		width = Math.max (wHint, Math.max(comboSize.x + buttonSize.x + 2*borderWidth, buttonSize.x + 2)  );
		return new Point (width, height);
	}
	
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell();
		shell.setSize(400,200);
		shell.setLayout(new FillLayout());
		ComboButton comboButton = new ComboButton(shell,SWT.NONE);
		new CCombo(shell,SWT.NONE);
		shell.setVisible(true);
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())display.sleep();
		}
		display.dispose();
	}
	
}
