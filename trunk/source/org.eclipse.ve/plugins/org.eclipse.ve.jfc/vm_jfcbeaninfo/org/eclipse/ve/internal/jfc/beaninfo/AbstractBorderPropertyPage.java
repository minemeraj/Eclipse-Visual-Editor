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
 *  $RCSfile: AbstractBorderPropertyPage.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:11 $ 
 */
import javax.swing.JPanel;
import javax.swing.border.Border;

public abstract class AbstractBorderPropertyPage extends JPanel {



// called to initialize gui when added to the editor
public abstract void buildPropertyPage();

public abstract boolean okToSetBorder(Border aBorder);

public abstract String getJavaInitializationString();

public abstract Border getBorderValue();

// Return the display name for the property sheet for the current border
public abstract String getDisplayName();

}