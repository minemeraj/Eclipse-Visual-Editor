/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormsConstants.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:41 $ 
 */
package org.eclipse.ve.internal.forms;

import org.eclipse.emf.common.util.URI;
 

/**
 * Constants used by Forms
 * @since 1.2.0
 */
public class FormsConstants {

	private FormsConstants() {
		
	}
	
	public static final URI
		SF_EXPANDABLECOMPOSITE_CLIENT,
		SF_FORM_BODY;
	
	static {
		SF_EXPANDABLECOMPOSITE_CLIENT = URI.createURI("java:/org.eclipse.ui.forms.widgets#ExpandableComposite/client");		 //$NON-NLS-1$
		SF_FORM_BODY = URI.createURI("java:/org.eclipse.ui.forms.widgets#Form/body");		 //$NON-NLS-1$
	}
}
