package org.eclipse.ve.internal.java.remotevm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BeanPropertyEditorJFrame.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-16 17:46:06 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

/**
 * This is a JFrame subclass that hosts Java Bean property editors
 */
public final class BeanPropertyEditorJFrame extends JFrame implements IBeanPropertyEditorDialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -431730059961772836L;
	private transient java.util.List fListeners = new ArrayList(1);
	private JPanel propertyEditorContainer;
	private Component propertyEditorComponent;
	private JButton okButton;
	private JButton cancelButton;
	//private JButton applyButton;
	//private JButton revertButton;
	private JPanel buttonRow;
	private JPanel buttonGrid;
	
	public BeanPropertyEditorJFrame() {
		super(RemoteVMMessages.getString("PropertyEditor.Dialog.Title")); //$NON-NLS-1$
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("customiz.gif")));	//$NON-NLS-1$

		getContentPane().setLayout(new BorderLayout());

		getContentPane().setBackground(SystemColor.control);

		propertyEditorContainer = new JPanel();
		propertyEditorContainer.setLayout(new PropertyCustomComponentLayout());
		getContentPane().add(propertyEditorContainer, BorderLayout.CENTER);

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

		aFrame.addWindowListener(new java.awt.event.WindowAdapter() {
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
		dispose();
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
	protected JButton getApplyButton(){
	
		if ( applyButton != null ) return applyButton;
		
		applyButton = new JButton(resproxyvm.getString("apply.label")); //$NON-NLS-1$
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
	protected JButton getCancelButton() {

		if (cancelButton != null)
			return cancelButton;

		cancelButton = new JButton(RemoteVMMessages.getString("PropertyEditor.Dialog.Button.Cancel")); //$NON-NLS-1$
		cancelButton.setBackground(SystemColor.control);
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
	protected JButton getOKButton() {

		if (okButton != null)
			return okButton;

		okButton = new JButton(RemoteVMMessages.getString("PropertyEditor.Dialog.Button.OK")); //$NON-NLS-1$
		okButton.setBackground(SystemColor.control);
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
	protected JButton getRevertButton(){
	
		if ( revertButton != null ) return revertButton;
		
		revertButton = new JButton(resproxyvm.getString("revert.label")); //$NON-NLS-1$
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
	 * Set our title to be the class name of the component
	 *
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
			
			if(buttonRow != null){
				// 	Set the background of the button row to match the background of the propertyEditorComponent
				// 	so that we don't see differences at the row border. Only if not the default.
				if (propertyEditorComponent.getBackground() != propertyEditorContainer.getBackground())
					buttonRow.setBackground(propertyEditorComponent.getBackground());
					buttonGrid.setBackground(propertyEditorComponent.getBackground());
			}
		}
	}
	public void decorateWithButtons(boolean abool){
		if(abool){
			buttonRow = new JPanel();
			buttonRow.setLayout(new FlowLayout(FlowLayout.RIGHT));

			buttonGrid = new JPanel();
			buttonRow.add(buttonGrid);
			buttonGrid.setLayout(new GridLayout(1, 0, 5, 0));

			buttonGrid.add(getOKButton());
			//buttonGrid.add( getApplyButton() );
			//buttonGrid.add( getRevertButton() );
			buttonGrid.add(getCancelButton());

			getContentPane().add(buttonRow, BorderLayout.SOUTH); //$NON-NLS-1$
			getRootPane().setDefaultButton(getOKButton());			
		}
	}
}
