package org.eclipse.ve.internal.java.core;

import org.eclipse.ve.internal.cde.core.ICDEContextMenuContributor;

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
 *  $RCSfile: IJavaBeanTreeContextMenuContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2004-05-26 18:23:30 $ 
 */

/**
 * This interface is implemented on classes that will contribute to the eclipse
 * context sensitive menu through the extension point="org.eclipse.ui.popupMenus".
 * 
 * This allows us to filter the actions defined in the plugin.xml for this extension point
 * for editparts used in the beans viewer (see ComponentTreeEditPart).
 */
public interface IJavaBeanTreeContextMenuContributor extends ICDEContextMenuContributor {

}
