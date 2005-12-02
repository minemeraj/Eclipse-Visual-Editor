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
 *  $RCSfile: NamedTypeChooseBeanContributor.java,v $
 *  $Revision: 1.9 $  $Date: 2005-12-02 20:22:22 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import org.eclipse.jface.resource.ImageDescriptor;
 
/**
 * 
 * @since 1.0.0
 */
public class NamedTypeChooseBeanContributor extends YesNoListChooseBeanContributor {
	//TODO - have to fix this too
	public NamedTypeChooseBeanContributor(String displayName, String classPackageName, String className){
		super(displayName, new String[] {classPackageName, className},null); 		
	}

	public ImageDescriptor getImage() {
		return null;
	}
	
}
