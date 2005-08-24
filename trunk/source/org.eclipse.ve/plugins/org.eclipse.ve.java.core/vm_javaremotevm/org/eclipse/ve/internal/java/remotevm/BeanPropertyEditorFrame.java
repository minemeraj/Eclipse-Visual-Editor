/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.remotevm;
/*
 *  $RCSfile: BeanPropertyEditorFrame.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *  This is a Dialog subclass that hosts Java Bean property editors that are AWT type.
 */
public final class BeanPropertyEditorFrame extends Frame implements IBeanPropertyEditorDialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -751707180162909504L;
	private transient java.util.List fListeners = new ArrayList(1);
	private Component propertyEditorComponent;
	private Panel propertyEditorContainer;
	private Button okButton;
	private Button cancelButton;
	//private Button applyButton;
	//private Button revertButton;
	
	private Panel buttonRow;

	public BeanPropertyEditorFrame() {
		// Right now launch this as a non-modal dialog because if we launch it modal some kind of SWT problem
		// creates a deadlock in the event queue and the desktop freezes up.
		super(RemoteVMMessages.getString("PropertyEditor.Dialog.Title")); //$NON-NLS-1$
		// set the icon on the title
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("customiz.gif"))); //$NON-NLS-1$
		setLayout(new BorderLayout());

		setBackground(SystemColor.control);

		propertyEditorContainer = new Panel();
		propertyEditorContainer.setLayout(new PropertyCustomComponentLayout());
		add(propertyEditorContainer, BorderLayout.CENTER);

		addWindowListenersToFrame(this);

	}
	/* Add the argument to the listener list for actions in this dialog
	 */
	public void addListener(IPropertyEditorDialogListener aListener) {

		fListeners.add(aListener);

	}
	/* When the frame is about to close dispose of us
	 */
	protected void addWindowListenersToFrame(Window aFrame) {

		aFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doCancel();
			}
		});
	}
	/**
	 * Close down the dialog. It is no longer needed.
	 */
	protected void closeDialog() {
		if (propertyEditorComponent != null && propertyEditorComponent instanceof IPropertyEditorDialogListener) {
			removeListener((IPropertyEditorDialogListener) propertyEditorComponent);
		}
		dispose(); // Dispose my phony parent, which will dispose me too.
	}
	/**
	 * doCancel - A cancel has been requested, revert and close.
	 */
	public void doCancel() {
		notifyRevertPropertyValue();
		closeDialog();
	}
	/**
	 * doOK - OKhas been requested, save and close.
	 */
	public void doOK() {
		notifySavePropertyValue();
		closeDialog();
	}
	/* Create the apply button if required
	 * Listen for it being pressed and update the property value if it is pressed
	 */
	/*
	protected Button getApplyButton(){
	
		if ( applyButton != null ) return applyButton;
		
		applyButton = new Button(resproxyvm.getString("apply.label")); //$NON-NLS-1$
		applyButton.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				notifySavePropertyValue();
			}
		});
	
		return applyButton;
	
	}
	*/
	/* Create the cancel button if required and dispose ourself if it is pressed
	 */
	protected Button getCancelButton() {

		if (cancelButton != null)
			return cancelButton;

		cancelButton = new Button(RemoteVMMessages.getString("PropertyEditor.Dialog.Button.Cancel")); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});

		return cancelButton;

	}
	/* Create the OK button if required and add a listener to it so that we can
	 * notify our listeners to get and apply a new value when OK is pressed
	 */
	protected Button getOKButton() {

		if (okButton != null)
			return okButton;

		okButton = new Button(RemoteVMMessages.getString("PropertyEditor.Dialog.Button.OK")); //$NON-NLS-1$
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doOK();
			}
		});

		return okButton;

	}
	/* Create the revert button if required and add a listener to it so that we can
	 * notify our listeners to revert the value to if the button is pressed
	 */
	/*
	protected Button getRevertButton(){
	
		if ( revertButton != null ) return revertButton;
		
		revertButton = new Button(resproxyvm.getString("revert.label")); //$NON-NLS-1$
		revertButton.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				notifyRevertPropertyValue();
			}
		});
	
		return revertButton;
	}
	*/

	/* Notify the listeners to revert the property value
	 */
	protected void notifyRevertPropertyValue() {

		Iterator iter = fListeners.iterator();
		while (iter.hasNext()) {
			IPropertyEditorDialogListener aListener = (IPropertyEditorDialogListener) iter.next();
			aListener.revertPropertyValue();
		}
	}
	/* Notify the listeners to apply the property value
	 */
	protected void notifySavePropertyValue() {

		Iterator iter = fListeners.iterator();
		while (iter.hasNext()) {
			IPropertyEditorDialogListener aListener = (IPropertyEditorDialogListener) iter.next();
			aListener.savePropertyValue();
		}
	}
	/* Remove the argument from the listener list for actions in this dialog
	 */
	public void removeListener(IPropertyEditorDialogListener aListener) {

		fListeners.remove(aListener);

	}
	/**
	 * Set the property editor
	 * Remove the old property editor and add the new one to the Center constraint
	 * of our border layout
	 */
	public void setPropertyEditor(Component aComponent) {

		if (propertyEditorComponent != null)
			propertyEditorContainer.remove(propertyEditorComponent);
		if (propertyEditorComponent instanceof IPropertyEditorDialogListener) {
			removeListener((IPropertyEditorDialogListener) propertyEditorComponent);
		}

		if(buttonRow != null){
			buttonRow.setBackground(null);	// Restore the button background to default.
		}
		propertyEditorComponent = aComponent;
		if (propertyEditorComponent != null) {
			propertyEditorContainer.add(propertyEditorComponent);
			if (aComponent instanceof IPropertyEditorDialogListener) {
				addListener((IPropertyEditorDialogListener) aComponent);
			}
			
			// Set the background of the button row to match the background of the propertyEditorComponent
			// so that we don't see differences at the row border. Only if not the default.
			if (propertyEditorComponent.getBackground() != propertyEditorContainer.getBackground() && buttonRow != null)
				buttonRow.setBackground(propertyEditorComponent.getBackground());
				
					
		}
	}
	public void decorateWithButtons(boolean abool) {
		if(abool){
			buttonRow = new Panel();
			buttonRow.setLayout(new FlowLayout(FlowLayout.RIGHT));

			Panel buttonGrid = new Panel();
			buttonRow.add(buttonGrid);
			buttonGrid.setLayout(new GridLayout(1, 0, 5, 0));
			buttonGrid.add(getOKButton());
			buttonGrid.add(getCancelButton());
			add(buttonRow, BorderLayout.SOUTH); //$NON-NLS-1$
		}
	}
}
