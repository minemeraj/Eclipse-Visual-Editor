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
 *  $RCSfile: NamedTypeChooseBeanContributor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-15 23:23:55 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;
 
/**
 * 
 * @since 1.0.0
 */
public class NamedTypeChooseBeanContributor extends YesNoListChooseBeanContributor {
	
	public NamedTypeChooseBeanContributor(String displayName, String classPackageName, String className){
		super(displayName, new String[] {classPackageName, className},null); 		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.YesNoListChooseBeanContributor#createFilterMatcher()
	 */
	protected FilterMatcher createFilterMatcher() {
		return new TypeFilterMatcher(getYesTypeFQNs(), getNoTypeFQNs(), "*"); //$NON-NLS-1$
	}
	
}
