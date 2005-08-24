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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: CompoundBorderPropertyPage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */
 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class CompoundBorderPropertyPage
	extends AbstractBorderPropertyPage
	implements ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -4505570363226837698L;

	private boolean built = false;
	
	private Border outsideBorder = null;
    private Border insideBorder = null;
    
	private BorderEditor outsideBorderEditor = null;
	private BorderEditor insideBorderEditor = null;
	
	private JPanel outsideBorderEditPanel = null;
	private JPanel insideBorderEditPanel = null;
		
    private JTextField outsideBorderField = null;
	private JTextField insideBorderField = null;

	private JButton outsideBorderButton = null;	    
	private JButton insideBorderButton = null;
		
    public CompoundBorderPropertyPage() {
    	super();
        initialize();
    }

    public void initialize() {  
		this.setName("CompoundBorderPropertyPage"); //$NON-NLS-1$
    }
    
      
    public void buildPropertyPage() {
		if (!built) {
			this.setBackground(SystemColor.control);
			Insets labelInsets = new Insets(0, 5, 5, 5);
			Insets compInsets = new Insets(0, 5, 10, 5);

			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = 0;

			this.setLayout(gb);

			c.gridy = 0;
			c.insets = labelInsets;
	        JLabel outsideBorderLabel = new JLabel(VisualBeanInfoMessages.getString("CompoundBorder.OutsideBorder")); //$NON-NLS-1$	
			this.add(outsideBorderLabel, c);

			c.gridy = 1;
			c.insets = compInsets;
			this.add(getOutsideBorderEditPanel(), c);

	        JLabel insideBorderLabel = new JLabel(VisualBeanInfoMessages.getString("CompoundBorder.InsideBorder")); //$NON-NLS-1$
			c.gridy = 2;
			c.insets = labelInsets;
			this.add(insideBorderLabel, c);

			c.gridy = 3;
			c.insets = compInsets;
			this.add(getInsideBorderEditPanel(), c);

			c.gridy = 4;
			c.weighty = 1.0;
			JPanel filler = new JPanel();
			filler.setBackground(SystemColor.control);
			this.add(filler, c);

			updateBorder();
			built = true;
		}
    }   
	
	public JPanel getOutsideBorderEditPanel() {
		if (outsideBorderEditPanel == null) {
			outsideBorderEditPanel = new JPanel();
			outsideBorderEditPanel.setBackground(SystemColor.control);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridy = 0;
			outsideBorderEditPanel.setLayout(gb);

			c.insets = new Insets(0, 0, 0, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			outsideBorderField = new JTextField();
			outsideBorderField.setEditable(false);
			outsideBorderField.setBackground(SystemColor.control);
			gb.setConstraints(outsideBorderField, c);
			outsideBorderEditPanel.add(outsideBorderField);

			c.insets = new Insets(0, 0, 0, 0);
			c.weightx = 0;
			c.gridx = 1;
			outsideBorderButton = new JButton();
			outsideBorderButton.setText(VisualBeanInfoMessages.getString("CompoundBorder.ChooseButton")); //$NON-NLS-1$
			outsideBorderButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
			outsideBorderButton.setBackground(SystemColor.control);
			outsideBorderButton.addActionListener(this);
			gb.setConstraints(outsideBorderButton, c);
			outsideBorderEditPanel.add(outsideBorderButton);
		}
		return outsideBorderEditPanel;
	}
	
	public JPanel getInsideBorderEditPanel() { 
		if ( insideBorderEditPanel == null ) {
			insideBorderEditPanel = new JPanel();
			insideBorderEditPanel.setBackground(SystemColor.control);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridy = 0;
			insideBorderEditPanel.setLayout(gb);

			c.insets = new Insets(0, 0, 0, 5);
			c.weightx = 1.0;
			c.gridx = 0;
			insideBorderField = new JTextField();
			insideBorderField.setEditable(false);
			insideBorderField.setBackground(SystemColor.control);
			gb.setConstraints(insideBorderField, c);
			insideBorderEditPanel.add(insideBorderField);

			c.insets = new Insets(0, 0, 0, 0);
			c.weightx = 0;
			c.gridx = 1;
			insideBorderButton = new JButton();
			insideBorderButton.setText(VisualBeanInfoMessages.getString("CompoundBorder.ChooseButton")); //$NON-NLS-1$
			insideBorderButton.setMargin(new java.awt.Insets(0, 5, 0, 5));
			insideBorderButton.setBackground(SystemColor.control);
			insideBorderButton.addActionListener(this);
			gb.setConstraints(insideBorderButton, c);
			insideBorderEditPanel.add(insideBorderButton);			
		}
		return insideBorderEditPanel;
	}
		
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int state = SubEditorJDialog.CANCELLED;
		if (source == outsideBorderButton) {
			state = SubEditorJDialog.showSubEditorJDialog(getTopLevelAncestor(), getOutsideBorderEditor().getCustomEditor());
		} else if (source == insideBorderButton) {
			state = SubEditorJDialog.showSubEditorJDialog(getTopLevelAncestor(), getInsideBorderEditor().getCustomEditor());
		}
		if (state == SubEditorJDialog.OK) {
			updateBorder();
		}
	}
	
	public BorderEditor getOutsideBorderEditor() {
		if (outsideBorderEditor == null) {
			outsideBorderEditor = new BorderEditor();
			outsideBorderEditor.setValue(outsideBorder);
		}
		return outsideBorderEditor;
	}
	
	public BorderEditor getInsideBorderEditor() {
		if (insideBorderEditor == null) {
			insideBorderEditor = new BorderEditor();
			insideBorderEditor.setValue(insideBorder);
		}
		return insideBorderEditor;
	}
	
	public void updateBorder() {
		String text;
		outsideBorder = (Border) getOutsideBorderEditor().getValue();
        text = getOutsideBorderEditor().getAsText();
		outsideBorderField.setText((text == null) ? "" : text); //$NON-NLS-1$
		insideBorder = (Border) getInsideBorderEditor().getValue();
        text = getInsideBorderEditor().getAsText();
		insideBorderField.setText((text == null) ? "" : text); //$NON-NLS-1$
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}
		
	/**
	 * @see org.eclipse.ve.internal.jfc.beaninfo.IBorderPropertyPage#okToSetBorder(Border)
	 */
	public boolean okToSetBorder(Border aBorder) {
		if (aBorder instanceof CompoundBorder) {
			CompoundBorder newBorder = (CompoundBorder) aBorder;

			outsideBorder = newBorder.getOutsideBorder();
			insideBorder = newBorder.getInsideBorder();

			getOutsideBorderEditor().setValue(outsideBorder);
			getInsideBorderEditor().setValue(insideBorder);
			//updateBorder();
			
			return true;
		}
		return false;
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.beaninfo.IBorderPropertyPage#getJavaInitializationString()
	 */
	public String getJavaInitializationString() {
		String outsideString;
		String insideString;
		
		if ( getOutsideBorderEditor().getValue() == null ) {
			outsideString = "null"; //$NON-NLS-1$
		} else {
			outsideString = getOutsideBorderEditor().getJavaInitializationString();
		}

		if ( getInsideBorderEditor().getValue() == null ) {
			insideString = "null"; //$NON-NLS-1$
		} else {
			insideString = getInsideBorderEditor().getJavaInitializationString();
		}		
		
		return "javax.swing.BorderFactory.createCompoundBorder(" + outsideString + ", " + insideString + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.beaninfo.IBorderPropertyPage#getBorderValue()
	 */
	public Border getBorderValue() {
		return BorderFactory.createCompoundBorder( outsideBorder, insideBorder );
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.beaninfo.IBorderPropertyPage#getDisplayName()
	 */
	public String getDisplayName() {
		String outsideString;
		String insideString;
		
		if ( getOutsideBorderEditor().getValue() == null ) {
			outsideString = VisualBeanInfoMessages.getString("CompoundBorder.DisplayName.None"); //$NON-NLS-1$
		} else {
			outsideString = getOutsideBorderEditor().getAsText();
		}

		if ( getInsideBorderEditor().getValue() == null ) {
			insideString = VisualBeanInfoMessages.getString("CompoundBorder.DisplayName.None"); //$NON-NLS-1$
		} else {
			insideString = getInsideBorderEditor().getAsText();
		}

	    return MessageFormat.format(VisualBeanInfoMessages.getString("CompoundBorder.DisplayName.Title(Outside,Inside)"), //$NON-NLS-1$
	                                 new Object[] { outsideString, insideString } );		
	}
	
}
