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
 *  $RCSfile: LineBorderPropertyPage.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

public class LineBorderPropertyPage extends AbstractBorderPropertyPage implements DocumentListener, ChangeListener {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -7180063690548288954L;

	private boolean built = false;
	
	private static final Color DEFAULT_COLOR = Color.gray;

	private JTextField widthField = null; 
	
	private ColorPropertyEditor cpe = null;
	private int borderWidth = 5;
		
public LineBorderPropertyPage(){
	super();
    initialize();
}

public void initialize() {
	Dimension size = new Dimension( 445, 250 );
	this.setMinimumSize( size );
	cpe = new ColorPropertyEditor();
	cpe.setValue(DEFAULT_COLOR);
    cpe.addChangeListener(this);
}

public void buildPropertyPage(){
	if (!built) {
		cpe.setPreviewEnabled(false);
		cpe.initialize();

		setName("LineBorderPropertypage"); //$NON-NLS-1$
		setLayout(new BorderLayout());
		setBackground(SystemColor.control);

		widthField = new JTextField(String.valueOf(getBorderWidth()));
		widthField.getDocument().addDocumentListener(this);

		JPanel p1 = new JPanel();
		p1.setBackground(SystemColor.control);
		p1.setLayout(new BorderLayout());
		p1.add(new JLabel(VisualBeanInfoMessages.getString("LineBorder.Width.Label.Text")), BorderLayout.WEST); //$NON-NLS-1$
		p1.add(widthField, BorderLayout.CENTER);
		this.add(p1, BorderLayout.NORTH);

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(new JLabel(VisualBeanInfoMessages.getString("LineBorder.Color.Label.text")), BorderLayout.NORTH); //$NON-NLS-1$
		p2.setBackground(SystemColor.control);
		p2.add(cpe, BorderLayout.CENTER);
		this.add(p2, BorderLayout.CENTER);

		cpe.autoSelectTab();
		built = true;
	}
}


public String getName(){
	return "LineBorderPropertyPage"; //$NON-NLS-1$
}

public String getDisplayName(){
	return MessageFormat.format(VisualBeanInfoMessages.getString("LineBorder.DisplayName(width,Color)"), new Object[]{ new Integer(getBorderWidth()), cpe.getAsText()}); //$NON-NLS-1$
}

public String getJavaInitializationString(){
	return "javax.swing.BorderFactory.createLineBorder(" + cpe.getJavaInitializationString() + "," + getBorderWidth() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}

public void handleException(Throwable exception){
	exception.printStackTrace();
}

public int getBorderWidth(){
	return borderWidth;
}

public Color getBorderColor(){
	return (Color)cpe.getValue();
}

public Border getBorderValue(){
	Border aLineBorder = BorderFactory.createLineBorder(getBorderColor(), getBorderWidth());
	return aLineBorder;	
}

public void stateChanged( ChangeEvent e ) {
	firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
}

public void updateHandle(DocumentEvent e){
	if (e.getDocument() == widthField.getDocument()){
		try {
		    borderWidth = Integer.parseInt(widthField.getText());
		    firePropertyChange("borderValueChanged", null, getBorderValue()); //$NON-NLS-1$
	    } catch ( NumberFormatException nfexc ) { }
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

public boolean okToSetBorder(Border aBorder){
	if (aBorder instanceof LineBorder){
		LineBorder fBorder = (LineBorder) aBorder;
		borderWidth = fBorder.getThickness();
		cpe.setValue(fBorder.getLineColor());
		return true;
	} else {
	    return false;
	}
}

}

