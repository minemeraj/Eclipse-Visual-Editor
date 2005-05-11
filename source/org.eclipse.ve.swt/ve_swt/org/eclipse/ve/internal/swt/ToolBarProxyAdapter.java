/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolBarProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-05-11 22:41:37 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;

public class ToolBarProxyAdapter extends ItemParentProxyAdapter {

	private EReference sf_items;

	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		if (sf == sf_items && oldValue instanceof IJavaObjectInstance) {
			IBeanProxyHost itemProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) oldValue);
			itemProxyHost.releaseBeanProxy();
			revalidateBeanProxy();
		} else
			super.canceled(sf, oldValue, position);
	}

	public ToolBarProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	public void releaseBeanProxy() {
		// Need to release all of the tool items. This is because they will be implicitly disposed anyway when super
		// gets called because the target VM will dispose them as children
		if (isBeanProxyInstantiated()) {
            List items = (List) ((IJavaObjectInstance) getTarget())
                    .eGet(sf_items);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                IBeanProxyHost value = BeanProxyUtilities
                        .getBeanProxyHost((IJavaInstance) iter.next());
                if (value != null)
                    value.releaseBeanProxy();
            }
        }
		super.releaseBeanProxy();
	}

	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sf_items = JavaInstantiation.getReference((IJavaObjectInstance) newTarget, SWTConstants.SF_TOOLBAR_ITEMS);
		}
	}

}
