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
 *  $RCSfile: EditPartContributionChangeListener.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-17 21:55:16 $ 
 */
package org.eclipse.ve.internal.cde.core;

public interface EditPartContributionChangeListener {

	public void contributionChanged(EditPartContributor editpartContributor);

}