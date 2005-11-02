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
 *  $RCSfile: AbstractEditPartContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-02 18:48:26 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

/**
 * Abstract implementation of EditPartContributor that handles the contribution change listeners
 * 
 * @since 1.2.0
 */
public abstract class AbstractEditPartContributor implements EditPartContributor {

	List contributionChangeListeners;

	public void addContributionChangeListener(EditPartContributionChangeListener listener) {
		getContributionChangeListeners().add(listener);
	}

	public void removeContributionChangeListener(EditPartContributionChangeListener listener) {
		getContributionChangeListeners().remove(listener);
	}

	private List getContributionChangeListeners() {
		if (contributionChangeListeners == null)
			contributionChangeListeners = new ArrayList();
		return contributionChangeListeners;
	}

	/*
	 * Helper method to notify contribution change listeners.
	 */
	public void notifyContributionChanged() {
		Iterator iterator = getContributionChangeListeners().iterator();
		while (iterator.hasNext()) {
			((EditPartContributionChangeListener) iterator.next()).contributionChanged(this);
		}
	}
}
