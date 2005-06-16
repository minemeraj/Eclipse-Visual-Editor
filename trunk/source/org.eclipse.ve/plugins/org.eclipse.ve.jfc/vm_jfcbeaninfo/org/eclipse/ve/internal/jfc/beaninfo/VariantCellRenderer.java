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
 *  $RCSfile: VariantCellRenderer.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.Component;

import javax.swing.*;

/**
 * This type was generated by a SmartGuide.
 */
public class VariantCellRenderer extends DefaultListCellRenderer {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 3722430592554750562L;
	String variantNames[];

public VariantCellRenderer(String[] variants){
	super();
	variantNames = variants;
}
	
public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

	String text = variantNames[index];
	super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
	setText(text);
	setVerticalTextPosition(SwingConstants.CENTER);
	return this;
	
}

}
