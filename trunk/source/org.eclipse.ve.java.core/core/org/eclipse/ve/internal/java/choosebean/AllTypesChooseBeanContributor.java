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
 *  $RCSfile: AllTypesChooseBeanContributor.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.graphics.Image;
 
/**
 * 
 * @since 1.0.0
 */
public class AllTypesChooseBeanContributor implements IChooseBeanContributor {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getName()
	 */
	public String getName() {
		return ChooseBeanMessages.AllTypesChooseBeanContributor_Name; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.choosebean.IChooseBeanContributor#getFilter(org.eclipse.jdt.core.search.IJavaSearchScope)
	 */
	public IFilter getFilter(IPackageFragment pkg, IProgressMonitor monitor) {
		return null;
	}

	public Image getImage() {
		return null;
	}

}
