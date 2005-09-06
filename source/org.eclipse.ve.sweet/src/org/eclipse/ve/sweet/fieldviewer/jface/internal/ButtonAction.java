/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Aug 30, 2005 by Gili Mendel
 * 
 *  $RCSfile: ButtonAction.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-06 19:44:29 $ 
 */
package org.eclipse.ve.sweet.fieldviewer.jface.internal;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
 

public class ButtonAction {
	
	private Button  button = null;	
	private IAction action = null;
	private Listener buttonListener;

	public ButtonAction(Composite parent, int styles) {		
		button = new Button(parent,styles);
		hookControl(button);
		setDefaults();
	}
		
	public ButtonAction(Button aButton){
		button = aButton;		
		setDefaults();
		hookControl(button);
	}
	
	public ButtonAction(Composite parent, IAction anAction) {
		int flags = SWT.PUSH;
        if (anAction.getStyle() == IAction.AS_CHECK_BOX)
               flags = SWT.TOGGLE;
        if (anAction.getStyle() == IAction.AS_RADIO_BUTTON)
               flags = SWT.RADIO;
        
		button = new Button(parent, flags);
		hookControl(button);	
		setAction(anAction);
	}
		
    protected void hookControl(Button button) {
    	button.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                handleDispose(event);
            }
        });
    	button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent event) {
				handleSelection(event);
			}
			public void widgetSelected(SelectionEvent event) {
				handleSelection(event);
			}
		});
    }
    
	private void setDefaults(){
		button.setEnabled(false);			
	}
	
	public Control getControl() {
		return button;
	}
	

	
	/**
	 * 
	 * @param anAction to be used by the Button. Note that anAction will
	 *        not override existing settings (like text).  It will only contribute
	 *        properties that are not already set.  Use the constructor to create ButtonAction
	 *        that reflects completely the action.
	 * 	 
	 */
	public void setAction(IAction anAction) {
		
		//TODO: the case of an existing action
		action = anAction;		


		if (button.getText()!=null) {
          String text = action.getText();
          if (text == null)
              text = ""; //$NON-NLS-1$
          else
              text = Action.removeAcceleratorText(text);
		}
			
		 if (button.getToolTipText()!=null)
		     button.setToolTipText(action.getToolTipText());
		 
		 button.setEnabled(action.isEnabled());		 
		 button.setSelection(action.isChecked());
		 
		 action.addPropertyChangeListener(new IPropertyChangeListener() {		
			public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
				update();		
			}		
		   });
	}
	
	protected void update() {
		//TODO: refresh potentially image/text
		button.setEnabled(action.isEnabled());		 
		button.setSelection(action.isChecked());
	}
    
    protected void handleDispose(DisposeEvent event) {
    }
    
    protected void handleSelection(SelectionEvent event) {
    	if (action!=null)
    		action.run();    	
    }
}
