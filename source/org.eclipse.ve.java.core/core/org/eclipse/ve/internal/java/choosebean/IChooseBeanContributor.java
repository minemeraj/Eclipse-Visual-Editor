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
 *  $RCSfile: IChooseBeanContributor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-12-02 20:22:23 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.dialogs.ITypeInfoFilterExtension;
import org.eclipse.jface.resource.ImageDescriptor;
 
/**
 * 
 * @since 1.0.0
 */
public interface IChooseBeanContributor {
public String getName();
public ImageDescriptor getImage();
public ITypeInfoFilterExtension getFilter(IPackageFragment pkg, IProgressMonitor monitor);
}
