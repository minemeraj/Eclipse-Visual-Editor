package org.eclipse.ve.internal.jfc.beaninfo;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IconPropertyEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */


import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JPanel;

public class IconPropertyEditor extends JPanel implements PropertyChangeListener {
	private Icon fIcon = null;
	
public IconPropertyEditor() {
	super();
	this.initialize();
}

public IconPropertyEditor(Icon anIcon) {
	super();
	this.initialize();
	setIconValue(anIcon);
}

public Icon getIconValue() {
	return fIcon;
}


private void initConnections() {
	
}


private void initialize() {
	setName("IconPropertyEditor");//$NON-NLS-1$
	setLayout(new GridBagLayout());
	setBackground(SystemColor.control);
	
	initConnections();
	
}

public void itemStateChanged(ItemEvent e) {
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("IconValue")) {//$NON-NLS-1$
	}
}


public void setIconValue(Icon newIcon) {
	fIcon = newIcon;
	//display preview
}

public Dimension getPreferredSize(){
	return new Dimension(450,250);
}

}