/*
 * Created on May 14, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.cheatsheets;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CreateWebServiceClientAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.PlatformUI;

/**
 * @author sri
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CreateWebServiceClientAction
	extends AbstractWizardInvocationAction {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.cheatsheets.AbstractWizardInvocationAction#createWizard()
	 */
	protected IWizard createWizard() {
		String pluginID = "com.ibm.etools.webservice.consumption.ui"; //$NON-NLS-1$
		String wizardClassName = "com.ibm.etools.webservice.consumption.ui.wizard.client.WebServiceClientWizard"; //$NON-NLS-1$
		IWizard w =  (IWizard) super.createInstanceOf(pluginID, wizardClassName);
		super.invoke("init", new Object[]{PlatformUI.getWorkbench(), new StructuredSelection()}, w); //$NON-NLS-1$
		return w;
	}

}
