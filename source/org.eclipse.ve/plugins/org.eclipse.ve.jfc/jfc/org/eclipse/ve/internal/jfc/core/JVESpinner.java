/*
 * Created on Jun 3, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JVESpinner.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author pwalker
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JVESpinner extends Composite {
	static final int BUTTON_WIDTH = 16;

	protected Text text;
	protected Button up, down;
	protected int minimum, maximum;
	protected int fValue = 0;
	protected boolean commandInProcess = false;
	private boolean settingText = false;

	public JVESpinner(Composite parent, int style, int initialValue) {

		super(parent, style);

		text = new Text(this, style | SWT.SINGLE | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);
		text.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e) {		
				try {
					setValue(Integer.parseInt(text.getText()));
				} catch (NumberFormatException ex) {
					setTextField();
				}
			}
		});
		text.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				traverse(e);
			}
		});
		text.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				text.selectAll();
			}
		});
		text.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				text.selectAll();
			}
		});
		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				up();
			}
		});
		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				down();
			}
		});
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				resize();
			}
		});
		minimum = 0;
		maximum = 999;
		text.setFont(getFont());
		fValue = initialValue;
		setTextField();
	}
	void traverse(Event e) {
		switch (e.detail) {
			case SWT.TRAVERSE_ARROW_PREVIOUS :
				if (e.keyCode == SWT.ARROW_UP) {
					e.doit = true;
					e.detail = SWT.NULL;
					up();
				}
				break;
			case SWT.TRAVERSE_ARROW_NEXT :
				if (e.keyCode == SWT.ARROW_DOWN) {
					e.doit = true;
					e.detail = SWT.NULL;
					down();
				}
				break;
		}
	}
	void up() {
		setValue(getValue() + 1);
	}
	void down() {
		setValue(getValue() - 1);
	}
	void focusIn() {
		text.setFocus();
	}
	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}
	public void setValue(int value) {
		// Don't process anything if we are still in the middle of processing previous commands 
		if (commandInProcess) return;
		try {
			Integer.parseInt(String.valueOf(value));
			if (value < minimum || value > maximum) {
				setTextField();
				return;
			} else if (fValue == value){
				return;
			}
			fValue = value;	
			setTextField();
			/*
			 * A real hack for now. Need to handle the case where notification has been
			 * sent out and commands are processing. To avoid a ConcurrentModificationException,
			 * we need to set a flag to prevent accepting any more input from the user.
			 */ 
			commandInProcess = true;
			notifyListeners(SWT.Modify, new Event());
		} catch (NumberFormatException ex) {
			setTextField();
		}
	}
	public int getValue() {
		return Integer.parseInt(text.getText());
	}
	protected void setTextField() {
		// Added this  check to prevent Linux GTK stack overflow problem
		if (!settingText) {
			settingText = true;
			String valueString = String.valueOf(fValue);
			text.setText(valueString);
			text.setSelection(valueString.length());
			settingText = false;
		}
	}
	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMinimum() {
		return minimum;
	}
	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - BUTTON_WIDTH;
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH, buttonHeight);
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent(String.valueOf(maximum));
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);
		int width = pt.x + BUTTON_WIDTH;
		int height = pt.y;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width, height);
	}
	public void addModifyListener(Listener listener) {
		if (listener != null)
			addListener(SWT.Modify, listener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		if (text != null && text.getEnabled() != enabled) 
			text.setEnabled(enabled);
		if (up != null && up.getEnabled() != enabled)
			up.setEnabled(enabled);
		if (down != null && down.getEnabled()!= enabled) down.setEnabled(enabled);
		
		// re-enable the notification process and allow user input if enabled
		if (enabled) {
			commandInProcess = false;
		} 
	}
}