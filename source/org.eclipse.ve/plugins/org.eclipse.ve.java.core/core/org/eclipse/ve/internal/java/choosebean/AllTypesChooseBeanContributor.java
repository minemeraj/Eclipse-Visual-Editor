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
 *  $RCSfile: AllTypesChooseBeanContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-05 18:14:26 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;
 
/**
 * 
 * @since 1.0.0
 */
public class AllTypesChooseBeanContributor implements IChooseBeanContributor {
	
	private FilteredList.FilterMatcher filter = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getName()
	 */
	public String getName() {
		return ChooseBeanMessages.getString("AllTypesChooseBeanContributor.Name"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getFilter(org.eclipse.jdt.core.search.IJavaSearchScope)
	 */
	public FilterMatcher getFilter(IJavaProject javaProject) {
		if(filter==null){
			filter = new TypeFilterMatcher();
		}
		return filter;
	}

}
