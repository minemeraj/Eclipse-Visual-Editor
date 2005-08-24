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
 *  $RCSfile: EmptyBorderPropertyPage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.awt.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class EmptyBorderPropertyPage extends AbstractBorderPropertyPage implements DocumentListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -6584341079896760410L;
	private boolean built = false;
	private JLabel topLabel = new JLabel(VisualBeanInfoMessages.getString("Top")); //$NON-NLS-1$
	private JTextField topField = new JTextField();
	private JLabel leftLabel = new JLabel(VisualBeanInfoMessages.getString("Left")); //$NON-NLS-1$
	private JTextField leftField = new JTextField();
	private JLabel bottomLabel = new JLabel(VisualBeanInfoMessages.getString("Bottom")); //$NON-NLS-1$
	private JTextField bottomField = new JTextField();
	private JLabel rightLabel = new JLabel(VisualBeanInfoMessages.getString("Right")); //$NON-NLS-1$
	private JTextField rightField = new JTextField();
	
		                                             
public EmptyBorderPropertyPage(){
	super();  
	initialize();
}

public String getName(){
	return "EmptyBorderPropertyPage"; //$NON-NLS-1$
}

public void initialize(){
	this.setName("EmptyBorderPropertyPage"); //$NON-NLS-1$
}

public void buildPropertyPage() {
	if (!built) {
		this.setBackground(SystemColor.control);
		setLayout(new GridLayout(16, 1, 0, 0));
		add(topLabel);
		add(topField);
		topField.getDocument().addDocumentListener(this);
		add(leftLabel);
		add(leftField);
		leftField.getDocument().addDocumentListener(this);
		add(bottomLabel);
		add(bottomField);
		bottomField.getDocument().addDocumentListener(this);
		add(rightLabel);
		add(rightField);
		rightField.getDocument().addDocumentListener(this);
		built = true;
	}
}
protected int getIntValue(String aString){
	try {
		return Integer.parseInt(aString);
	} catch ( NumberFormatException nfexc ) {
		return 0;
	}	
}

public int getTop(){
	return getIntValue(topField.getText());
}

public int getLeft(){
	return getIntValue(leftField.getText());
}

public int getBottom(){
	return getIntValue(bottomField.getText());
}

public int getRight(){
	return getIntValue(rightField.getText());
}

public Border getBorderValue(){
	Border aBorder = BorderFactory.createEmptyBorder(getTop(), getLeft(), getBottom(), getRight());
	return aBorder;
}

public void updateHandle(DocumentEvent e){
	if (e.getDocument() == topField.getDocument() || e.getDocument() == leftField.getDocument() || 
	e.getDocument() == bottomField.getDocument() || e.getDocument() == rightField.getDocument()){
		firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	}
}

public void insertUpdate(DocumentEvent e){
	updateHandle(e);
}

public void removeUpdate(DocumentEvent e){
	updateHandle(e);
}

public void changedUpdate(DocumentEvent e){
	updateHandle(e);
}

public String getDisplayName(){
	return MessageFormat.format(VisualBeanInfoMessages.getString("EmptyBorder.Empty(top,left,bottom,right)"), //$NON-NLS-1$
								new Object[]{ 	new Integer(getTop()), 
												new Integer(getLeft()), 
												new Integer(getBottom()),
												new Integer(getRight())});
}

public String getJavaInitializationString(){
	return "javax.swing.BorderFactory.createEmptyBorder(" + getTop() + "," + getLeft() + //$NON-NLS-1$ //$NON-NLS-2$
	            "," + getBottom() + "," + getRight() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

public boolean okToSetBorder(Border aBorder){
	if (aBorder instanceof EmptyBorder && !(aBorder instanceof MatteBorder)){
		Insets fInsets = ((EmptyBorder) aBorder).getBorderInsets();
		topField.setText(String.valueOf(fInsets.top));
		leftField.setText(String.valueOf(fInsets.left));
		bottomField.setText(String.valueOf(fInsets.bottom));
		rightField.setText(String.valueOf(fInsets.right));
		return true;
	}
	
	return false;
}

}
