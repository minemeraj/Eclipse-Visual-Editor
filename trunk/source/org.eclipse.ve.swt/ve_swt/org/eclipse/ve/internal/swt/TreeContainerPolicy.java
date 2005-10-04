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
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: TreeContainerPolicy.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-04 15:41:48 $ 
 */


import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
/**
 * Container Edit Policy for Trees/Columns.
 */
public class TreeContainerPolicy extends SWTWidgetContainerPolicy {
	protected EStructuralFeature sfTreeItem;
	
	public TreeContainerPolicy(EditDomain domain) {
		super(JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_TREE_COLUMNS), domain);
		sfTreeItem = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), SWTConstants.SF_TREE_ITEMS);
	}
	
	protected EStructuralFeature getContainmentSF(Object child, Object positionBeforeChild, int requestType) {
		return isTreeItem(child) ? sfTreeItem : super.getContainmentSF(child, positionBeforeChild, requestType);
	}
	
	protected EStructuralFeature getContainmentSF(List children, Object positionBeforeChild, int requestType) {
			// It could be add/move/orphan. In any of those cases we can't handle a mixture of children.
			if (children.isEmpty())
				return null;
			else if (children.size() == 1) {
				// When only one it can be either a tree item or something else. 
				return isTreeItem(children.get(0)) ? sfTreeItem: super.getContainmentSF(children, positionBeforeChild, requestType);	
			} else {
				// If there is more than one, if they are not all treeitems or not treeitems then we can't handle it. That is because that would
				// require using more than one feature to the apply and we can't handle that.
				Iterator iter = children.iterator();
				boolean isTreeItems = isTreeItem(iter.next());
				while(iter.hasNext()) {
					Object child = iter.next();
					if (isTreeItems != isTreeItem(child))
						return null;	// Invalid, we have a mixture.
				}
				return isTreeItems ? sfTreeItem : super.getContainmentSF(children, positionBeforeChild, requestType);
			}
		}
	
	protected boolean isTreeItem(Object child) {
		return sfTreeItem.getEType().isInstance(child);
	}

}
