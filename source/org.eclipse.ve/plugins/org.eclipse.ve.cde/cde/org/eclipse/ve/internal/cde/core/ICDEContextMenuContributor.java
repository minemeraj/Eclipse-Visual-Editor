package org.eclipse.ve.internal.cde.core;
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
 *  $RCSfile: ICDEContextMenuContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-26 18:23:27 $ 
 */

import java.util.List;

/**
 * 
 * This interface is implemented on classes that will contribute to the eclipse
 * context sensitive menu through the extension point="org.eclipse.ui.popupMenus".
 * 
 * This allows us to have both GraphicalEditPart and TreeEditPart
 * filter the same actions defined in the plugin.xml for this extension point
 * without having to specify a common class in their hierarchy (in this case, AbstractEditPart),
 * or having to define the actions twice... once for the graph viewer and once for
 * the Beans viewer.
 * 
 */
public interface ICDEContextMenuContributor {
	
	/*
	 * This should not be needed but GEF doesn't make the edit policies public
	 * except by a specific key. It is used by the CDEActionFilter to pass
	 * filter requests to the edit policies.
	 */
	public List getEditPolicies();	

}
