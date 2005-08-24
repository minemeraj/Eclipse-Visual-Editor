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
 *  $RCSfile: IconPropertyEditor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */


import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JPanel;

public class IconPropertyEditor extends JPanel implements PropertyChangeListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 8983305473804120771L;
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
