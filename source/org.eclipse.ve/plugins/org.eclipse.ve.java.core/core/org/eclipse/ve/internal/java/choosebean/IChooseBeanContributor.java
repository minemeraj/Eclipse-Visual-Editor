/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IChooseBeanContributor.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:23:55 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.dialogs.FilteredList;
 
/**
 * 
 * @since 1.0.0
 */
public interface IChooseBeanContributor {
public String getName();
public FilteredList.FilterMatcher getFilter(IJavaProject project);
}
