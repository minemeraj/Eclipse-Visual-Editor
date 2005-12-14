/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ItemParentGraphicalEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-12-14 21:44:40 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditPartRunnable;
 
/**
 * swt Graphical Editpart for widgets that are item parents (e.g. ToolBar). This
 * is for simple item parents. Others, like TabFolder are more complicated and
 * handle things themselves.
 * 
 * @since 1.1.0
 */
public abstract class ItemParentGraphicalEditPart extends CompositeGraphicalEditPart {

	public ItemParentGraphicalEditPart(Object model) {
		super(model);
	}
	
	/**
	 * Called by the item editparts when something has changed on them. It will
	 * cause all children to refresh their visuals. 
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void refreshItems() {
		CDEUtilities.displayExec(this, "ITEM VISUALS", new EditPartRunnable(this) { //$NON-NLS-1$
			protected void doRun() {
				List children = getChildren();
				for (int i = 0; i < children.size(); i++) {
					((ItemGraphicalEditPart) children.get(i)).refreshVisuals();
				}
			}
		});
	}

}
