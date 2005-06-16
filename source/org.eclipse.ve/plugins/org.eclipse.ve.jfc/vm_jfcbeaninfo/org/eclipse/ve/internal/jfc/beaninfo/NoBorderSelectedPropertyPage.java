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
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.BorderLayout;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.Border;
 
public class NoBorderSelectedPropertyPage extends AbstractBorderPropertyPage {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -3187739663659179508L;
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