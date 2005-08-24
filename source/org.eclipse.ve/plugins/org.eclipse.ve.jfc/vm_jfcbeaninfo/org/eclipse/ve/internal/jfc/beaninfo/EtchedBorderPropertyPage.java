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
 *  $RCSfile: EtchedBorderPropertyPage.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class EtchedBorderPropertyPage extends AbstractBorderPropertyPage implements ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -2992784274154986792L;
	private boolean built = false;
	private JRadioButton raisedButton = null;
	private JRadioButton loweredButton = null;
	private ButtonGroup buttonGroup = null;
	private int etchedType = EtchedBorder.RAISED;
	
public EtchedBorderPropertyPage(){
	super();  
	initialize();
}

public String getDisplayName(){
	if ( getEtchedType() == EtchedBorder.LOWERED ){
		return VisualBeanInfoMessages.getString("EtchedBorder.DisplayName.Lowered"); //$NON-NLS-1$
	} else {
		return VisualBeanInfoMessages.getString("EtchedBorder.DisplayName.Raised"); //$NON-NLS-1$
	}
}

public String getName(){
	return "EtchedBorderPropertyPage"; //$NON-NLS-1$
}

public int getEtchedType(){
	return etchedType;
}

public JRadioButton getRaisedButton(){
	if(raisedButton == null){
		try{
			raisedButton = new JRadioButton(VisualBeanInfoMessages.getString("EtchedBorder.radio.Raised.Text")); //$NON-NLS-1$
			raisedButton.setBackground(SystemColor.control);
			raisedButton.setName("Raised Etched"); //$NON-NLS-1$
			
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	return raisedButton;
}

public JRadioButton getLoweredButton(){
	if (loweredButton == null){
		try{
			loweredButton = new JRadioButton(VisualBeanInfoMessages.getString("EtchedBorder.radio.Lowered.Text")); //$NON-NLS-1$
			loweredButton.setBackground(SystemColor.control);
			loweredButton.setName("Lowered Etched"); //$NON-NLS-1$
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
	this.setName("EtchedBorderPropertyPage"); //$NON-NLS-1$
}

public void buildPropertyPage() {
	if (!built) {
		this.setBackground(SystemColor.control);
		this.setLayout(new GridLayout(8, 1, 0, 0));
		getButtonGroup();
		this.add(getRaisedButton(), getRaisedButton().getName());
		this.add(getLoweredButton(), getLoweredButton().getName());
		getRaisedButton().addItemListener(this);
		getLoweredButton().addItemListener(this);
		if (getEtchedType() == EtchedBorder.RAISED ) {
			getRaisedButton().setSelected(true);
		} else if ( getEtchedType() == EtchedBorder.LOWERED ) {
			getLoweredButton().setSelected(true);
		}
		built = true;
	}
}

public Border getBorderValue(){
	Border aBorder = BorderFactory.createEtchedBorder(getEtchedType());
	return aBorder;
}

public void itemStateChanged(ItemEvent e){
	if(e.getSource() == getRaisedButton()){
		etchedType = EtchedBorder.RAISED;
	}
	else if(e.getSource() == getLoweredButton()){
		etchedType = EtchedBorder.LOWERED;
	}
	firePropertyChange("borderValueChanged",null,getBorderValue()); //$NON-NLS-1$
}

public boolean okToSetBorder(Border aBorder){
	if (aBorder instanceof EtchedBorder){
		etchedType = ((EtchedBorder)aBorder).getEtchType();
		return true;
	}
	
	return false;
}

public String getJavaInitializationString(){
	if (getEtchedType() == EtchedBorder.LOWERED){
		return "javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED)"; //$NON-NLS-1$
	}
	
	//default is Raised EtchedBorder
	return "javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED)"; //$NON-NLS-1$
}


}
