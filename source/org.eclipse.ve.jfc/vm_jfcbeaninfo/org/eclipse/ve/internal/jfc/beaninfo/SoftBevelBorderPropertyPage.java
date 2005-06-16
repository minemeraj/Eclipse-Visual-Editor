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
 *  $RCSfile: SoftBevelBorderPropertyPage.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;


public class SoftBevelBorderPropertyPage extends AbstractBorderPropertyPage implements  ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 5503741737244735137L;
	private boolean built = false;
	private JRadioButton raisedButton = null;
	private JRadioButton loweredButton = null;
	private ButtonGroup buttonGroup = null;
	private int bevelType = SoftBevelBorder.RAISED;
	
public SoftBevelBorderPropertyPage(){
	super();  
	initialize();
}

public int getBevelType(){
	return bevelType;
}

public JRadioButton getRaisedButton(){
	if(raisedButton == null){
		try{
			raisedButton = new JRadioButton(VisualBeanInfoMessages.getString("SoftBevelBorder.radio.Raised.Text")); //$NON-NLS-1$
			raisedButton.setBackground(SystemColor.control);
			raisedButton.setName("Raised Bevel"); //$NON-NLS-1$
			
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return raisedButton;
}

public JRadioButton getLoweredButton(){
	if (loweredButton == null){
		try{
			loweredButton = new JRadioButton(VisualBeanInfoMessages.getString("SoftBevelBorder.radio.Lowered.Text")); //$NON-NLS-1$
			loweredButton.setBackground(SystemColor.control);
			loweredButton.setName("Lowered Bevel"); //$NON-NLS-1$
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
	this.setName("SoftBevelBorderPropertyPage"); //$NON-NLS-1$
}

public void buildPropertyPage(){
	if (!built) {
		this.setBackground(SystemColor.control);
		this.setLayout(new GridLayout(8, 1, 0, 0));
		getButtonGroup();
		this.add(getRaisedButton(), getRaisedButton().getName());
		this.add(getLoweredButton(), getLoweredButton().getName());
		getRaisedButton().addItemListener(this);
		getLoweredButton().addItemListener(this);
		if ( getBevelType() == SoftBevelBorder.RAISED ) {
			getRaisedButton().setSelected(true);
		} else if ( getBevelType() == SoftBevelBorder.LOWERED ) {
			getLoweredButton().setSelected(true);
		}
		built = true;
	}
}

public Border getBorderValue(){
	Border aBorder = new SoftBevelBorder(bevelType);
	return aBorder;
}

public void itemStateChanged(ItemEvent e){
	if(e.getSource() == getRaisedButton()){
		bevelType = SoftBevelBorder.RAISED;
	}
	else if(e.getSource() == getLoweredButton()){
		bevelType = SoftBevelBorder.LOWERED;
	}
	
	firePropertyChange("borderValueChanged",null,getBorderValue()); //$NON-NLS-1$
}

public boolean okToSetBorder(Border aBorder){
	if (aBorder instanceof SoftBevelBorder){
		bevelType = ((SoftBevelBorder)aBorder).getBevelType();
		return true;
	}
	return false;

}

public String getDisplayName(){
	if ( getBevelType() == SoftBevelBorder.LOWERED ){
		return VisualBeanInfoMessages.getString("SoftBevelBorder.Lowered.DisplayName"); //$NON-NLS-1$
	} else {
		return VisualBeanInfoMessages.getString("SoftBevelBorder.Raised.DisplayName"); //$NON-NLS-1$
	}
}

public String getJavaInitializationString(){
	if (getBevelType() == SoftBevelBorder.LOWERED){
		return "new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.LOWERED)"; //$NON-NLS-1$
	}
	
	//default is Raised SoftBevelBorder
	return "new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.RAISED)"; //$NON-NLS-1$
} 

}
