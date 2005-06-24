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
 *  $RCSfile: WorkbenchPartLabelProvider.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.swt.graphics.Image;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.cde.emf.DefaultLabelProvider;

import org.eclipse.ve.internal.java.vce.PDEUtilities;

/**
 * RCP WorkbenchPart label provider (gets image/label from plugin.xml in project).
 * 
 * @since 1.1.0
 */
public class WorkbenchPartLabelProvider extends DefaultLabelProvider{
	
	private Image image;
	
	public void dispose() {
		if(image != null){
			image.dispose();
		}
		super.dispose();
	}
	
	public Image getImage(Object element) {
		String viewClassName = ((IJavaInstance)element).getJavaType().getQualifiedName();
		String iconPath = PDEUtilities.getUtilities(domain).getIconPath(viewClassName);
		if(iconPath != null){
			try{
				image = new Image(null,iconPath);
				return image;
			} catch (Exception exc){
			}
		}
		return super.getImage(element);
	}
	
	public String getText(Object element) {
		String viewClassName = ((IJavaInstance)element).getJavaType().getQualifiedName();
		String viewNameInPDE = PDEUtilities.getUtilities(domain).getViewName(viewClassName);
		return viewNameInPDE == null ? super.getText(element): super.getText(element) + "-\"" + viewNameInPDE + "\"";
	}
}