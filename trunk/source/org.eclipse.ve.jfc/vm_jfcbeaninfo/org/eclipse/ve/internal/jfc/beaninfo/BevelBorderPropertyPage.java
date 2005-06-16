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
 *  $RCSfile: BevelBorderPropertyPage.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class BevelBorderPropertyPage extends AbstractBorderPropertyPage implements ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 8205118731737645116L;
	private boolean built = false;
	private JRadioButton raisedButton = null;
	private JRadioButton loweredButton = null;
	private ButtonGroup buttonGroup = null;
	private int bevelType = BevelBorder.RAISED;
	
public BevelBorderPropertyPage(){
	super();  
	initialize();
}

public String getDisplayName(){
	if ( getBevelType() == BevelBorder.LOWERED ) {
		return VisualBeanInfoMessages.getString("BevelBorder.Lowered.DisplayName"); //$NON-NLS-1$
	} else {
		return VisualBeanInfoMessages.getString("BevelBorder.Raised.DisplayName"); //$NON-NLS-1$
	}
}

public int getBevelType(){
	return bevelType;
}

public JRadioButton getRaisedButton(){
	if(raisedButton == null){
		try{
			raisedButton = new JRadioButton(VisualBeanInfoMessages.getString("BevelBorder.radio.Raised.Text"));  //$NON-NLS-1$
			raisedButton.setBackground(SystemColor.control);
			raisedButton.setName("Raised Bevel"); //$NON-NLS-1$
			raisedButton.addItemListener(this);
			
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return raisedButton;
}

public JRadioButton getLoweredButton() {
	if (loweredButton == null){
		try{
			loweredButton = new JRadioButton(VisualBeanInfoMessages.getString("BevelBorder.radio.Lowered.Text"));  //$NON-NLS-1$
			loweredButton.setBackground(SystemColor.control);
			loweredButton.setName("Lowered Bevel"); //$NON-NLS-1$
			loweredButton.addItemListener(this);
		}catch(Throwable e){
			e.printStackTrace();
		}
	
	}
	return loweredButton;
}

public ButtonGroup getButtonGroup(){
	if(buttonGroup == null){
		try{
			buttonGroup = new ButtonGroup();
			buttonGroup.add(getRaisedButton());
			buttonGroup.add(getLoweredButton());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return buttonGroup;
}

public void initialize(){
	this.setName("BevelBorderPropertyPage"); //$NON-NLS-1$
}

public void buildPropertyPage() {
	if (!built) {
		this.setBackground(SystemColor.control);
		this.setLayout(new GridLayout(8, 1, 0, 0));
		getButtonGroup();
		this.add(getRaisedButton(), getRaisedButton().getName());
		this.add(getLoweredButton(), getLoweredButton().getName());
		if ( getBevelType() == BevelBorder.RAISED ) { 
		    getRaisedButton().setSelected(true);
		} else if ( getBevelType() == BevelBorder.LOWERED ) {
			getLoweredButton().setSelected(true);
		}
		built = true;
	}
}

public Border getBorderValue(){
	Border aBorder = BorderFactory.createBevelBorder(getBevelType());
	return aBorder;
}

public void itemStateChanged(ItemEvent e){
	if(e.getSource() == getRaisedButton()){
		bevelType = BevelBorder.RAISED;
	}
	else if(e.getSource() == getLoweredButton()){
		bevelType = BevelBorder.LOWERED;
	}
	
	firePropertyChange("borderValueChanged",null,getBorderValue()); //$NON-NLS-1$
}

public String getJavaInitializationString(){		
	if (getBevelType() == BevelBorder.LOWERED){
		return "javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED)"; //$NON-NLS-1$
	}
	
	//default is Raised BevelBorder
	return "javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)"; //$NON-NLS-1$
}

public boolean okToSetBorder(Border aBorder) {
	
	if ( aBorder instanceof BevelBorder ) {
		BevelBorder bevelBorder = (BevelBorder)aBorder;	
		bevelType = bevelBorder.getBevelType();
		return true;
	} 
	return false;
}


}