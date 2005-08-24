/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;

/**
 * Interface to use to register notifiers for callbacks. Extensions that want
 * to be called back should implement this interface and should register/deregister
 * on the {@link org.eclipse.ve.internal.jfc.core.ComponentManager.ComponentManagerExtension#primSetExtensionProxy(IBeanProxy)}.
 * 
 * @since 1.1.0
 */
public interface ComponentManagerFeedbackControllerNotifier {
	/**
	 * This will be called from the feedback controller when a transaction for the registered notifier comes in.
	 * 
	 * @param msgID
	 * @param parms
	 * 
	 * @since 1.1.0
	 */
	public void calledBack(int msgID, Object[] parms);
}