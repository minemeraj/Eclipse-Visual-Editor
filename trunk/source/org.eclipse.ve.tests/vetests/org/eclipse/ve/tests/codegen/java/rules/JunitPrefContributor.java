/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.tests.codegen.java.rules;
/*
 *  $RCSfile: JunitPrefContributor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:54:15 $ 
 */
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.java.codegen.java.rules.AbstractStyleContributor;
import org.eclipse.ve.tests.VETestsPlugin;

/**
 * @author Gili Mendel
 *
 */
public class JunitPrefContributor extends AbstractStyleContributor {

	private Image image;
	/**
	 * TODO Figure out a way the style contributor can have the ui stuff separated out. Tests shouldn't need ui just to run.
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.AbstractStyleContributor#buildUI(Composite)
	 */
	public Control buildUI(Composite parent) {
		image = CDEPlugin.getImageFromPlugin(VETestsPlugin.getPlugin(), "resources/images/whatsUp.gif"); //$NON-NLS-1$
		Control c = createLabel(parent,"JUnit's dummy Preference Contributor ...", image); //$NON-NLS-1$
		
	   return c ;
		 
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.rules.AbstractStyleContributor#disposed()
	 */
	protected void disposed() {
		image.dispose();
		image = null;
		super.disposed();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStylePrefUI#restoreDefaults()
	 */
	public void restoreDefaults() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.vce.IEditorStylePrefUI#storeUI()
	 */
	public void storeUI() {
	}

}
