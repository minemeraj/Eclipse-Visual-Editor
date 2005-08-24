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
/*
 *  $RCSfile: YesNoListChooseBeanContributor.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.IFilter;
 
/**
 * 
 * @since 1.0.0
 */
public abstract class YesNoListChooseBeanContributor implements IChooseBeanContributor {

	private String name = null;
	private String[] yesTypes = null;
	private String[] noTypes = null;
	
	public YesNoListChooseBeanContributor(String name, String[] yesTypes, String[] noTypes){
		this.name = name;
		this.yesTypes = yesTypes;
		this.noTypes = noTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getFilter(org.eclipse.jdt.core.search.IJavaSearchScope)
	 */
	public IFilter getFilter(IPackageFragment pkg, IProgressMonitor monitor) {
		return new YesNoFilter(yesTypes, noTypes, pkg, monitor);		
	}
}
