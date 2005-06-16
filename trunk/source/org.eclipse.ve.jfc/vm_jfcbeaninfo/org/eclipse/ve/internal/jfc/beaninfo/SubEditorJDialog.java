package org.eclipse.ve.internal.jfc.beaninfo;
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
 *  $RCSfile: SubEditorJDialog.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Usage note: reusing the same SubEditorJDialog can cause modal problems.
 * For best usability, construct a new SubEditorJDialog whenever it is shown
 */
public class SubEditorJDialog extends JDialog implements ActionListener {

	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -8129844147597559027L;
	public static final int OK = 0;
	public static final int CANCELLED = 1;

	private int closedState = CANCELLED;

	JPanel mainPanel = null;
	JButton okButton = null;
	JButton cancelButton = null;
		 	
	public SubEditorJDialog(Frame owner) {
	    super(owner, VisualBeanInfoMessages.getString("SubEditorJDialog.title"), true); //$NON-NLS-1$
	    initialize();
	}
	
	public SubEditorJDialog(Dialog owner) {
	    super(owner, VisualBeanInfoMessages.getString("SubEditorJDialog.title"), true); //$NON-NLS-1$
   	    initialize();
	}

	/**
	 * Show a sub editor dialog and return it's result.
	 * If the container is not a Dialog or Frame, the default window is used.
	 * 
	 * @param owner    the container which owns this dialog
	 * @param customEditor    the custom editor to put in the dialog
	 * @return the state showing how the dialog was disposed
	 */
	public static int showSubEditorJDialog( Container owner, Component customEditor ) {
		SubEditorJDialog subEditor;
		
		if ( owner instanceof Dialog ) {
			subEditor = new SubEditorJDialog((Dialog)owner);
		} else if ( owner instanceof Frame ) {
			subEditor = new SubEditorJDialog((Frame)owner);
		} else {
			subEditor = new SubEditorJDialog((Frame)null);
		}
		subEditor.setCustomEditor( customEditor );
		return subEditor.showEditor();
	}
	
	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setBackground(SystemColor.control);
			okButton.setText(VisualBeanInfoMessages.getString("SubEditorJDialog.ok")); //$NON-NLS-1$
			okButton.addActionListener(this);
		}
		return okButton;
	}

	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setBackground(SystemColor.control);
			cancelButton.setText(VisualBeanInfoMessages.getString("SubEditorJDialog.cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

	public void initialize() {
		mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.control);
		mainPanel.setLayout(new BorderLayout());

		JPanel buttonRow = new JPanel();
		buttonRow.setBackground(SystemColor.control);
		buttonRow.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JPanel buttonGrid = new JPanel();
		buttonGrid.setBackground(SystemColor.control);
		buttonGrid.setLayout(new GridLayout(1, 0, 5, 0));
		buttonGrid.add(getOkButton());
		buttonGrid.add(getCancelButton());

		buttonRow.add(buttonGrid);

		mainPanel.add(buttonRow, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
		getRootPane().setDefaultButton(getOkButton());
	}

	public void setCustomEditor(Component customEditor) {
		mainPanel.add(customEditor, BorderLayout.CENTER);
		pack();
	}

	public int showEditor() {
		setVisible(true);
		return closedState;
	}

	public int getState() {
		return closedState;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getOkButton()) {
			closedState = OK;
		} else {
			closedState = CANCELLED;
		}
		setVisible(false);
	}
}
