/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;


/*
 *  $RCSfile: IJavaBeanGraphicalContextMenuContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:10 $ 
 */

/**
 * This interface is implemented on classes that will contribute to the eclipse
 * context sensitive menu through the extension point="org.eclipse.ui.popupMenus".
 * 
 * This allows us to filter the actions defined in the plugin.xml for this extension point
 * for editparts used in the graph viewer (see ComponentGraphicalEditPart).
 */
public interface IJavaBeanGraphicalContextMenuContributor extends IJavaBeanContextMenuContributor {

}
