package org.eclipse.ve.internal.java.vce.launcher;
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
 *  $RCSfile: JavaBeanLauncherTabGroup.java,v $
 *  $Revision: 1.3 $  $Date: 2005-02-15 23:23:55 $ 
 */
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import org.eclipse.debug.ui.*;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;

public class JavaBeanLauncherTabGroup extends AbstractLaunchConfigurationTabGroup {

	/**
	 * @see ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
	 */
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
			new JavaBeanTab(),
			new AppletParametersTab(),
			new JavaArgumentsTab(),			
			new JavaJRETab(),			
			new JavaClasspathTab(),
			new SourceLookupTab(),
			new CommonTab()
		};
		setTabs(tabs);
	}	
}
