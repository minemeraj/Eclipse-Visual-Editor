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
 *  $RCSfile: NoBorderSelectedPropertyPage.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
 */

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.Border;
 
public class NoBorderSelectedPropertyPage extends AbstractBorderPropertyPage {
	private boolean built = false;
	public NoBorderSelectedPropertyPage(){
		super();
		initialize();
	}
	
	public String getDisplayName(){
		return ""; //$NON-NLS-1$
	}
	
	public void initialize(){
		this.setName("NoBorderSelectedPropertyPage"); //$NON-NLS-1$
	}
	
	public void buildPropertyPage(){
		if (!built) {
			setBackground(SystemColor.control);
			setLayout(new BorderLayout());
			JPanel p1 = new JPanel();
			p1.setBackground(SystemColor.control);
			add(p1, BorderLayout.CENTER);
			built = true;
		}
	}
	public String getJavaInitializationString(){
		return "null"; //$NON-NLS-1$
	}
	
	public Border getBorderValue(){
		return null;
	}
	
	public boolean okToSetBorder(Border aBorder) {
		return (aBorder == null);
	}
}