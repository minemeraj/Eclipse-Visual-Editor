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
 *  $RCSfile: AbstractBorderPropertyPage.java,v $
 *  $Revision: 1.4 $  $Date: 2008-05-29 20:10:14 $ 
 */
import javax.swing.JPanel;
import javax.swing.border.Border;

public abstract class AbstractBorderPropertyPage extends JPanel {



   /**
	 * 
	 */
	private static final long serialVersionUID = -5503489435066594295L;

// called to initialize gui when added to the editor
public abstract void buildPropertyPage();

public abstract boolean okToSetBorder(Border aBorder);

public abstract String getJavaInitializationString();

public abstract Border getBorderValue();

// Return the display name for the property sheet for the current border
public abstract String getDisplayName();

}
