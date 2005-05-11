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
 *  $RCSfile: ViewPartTreeEditPart.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-11 22:41:38 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaBeanTreeEditPart;

import org.eclipse.ve.internal.swt.SwtPlugin;


public class ViewPartTreeEditPart extends JavaBeanTreeEditPart {
	
	public ViewPartTreeEditPart(Object aModel) {
		super(aModel);
	}
	
	protected List getChildJavaBeans() {
		List result = new ArrayList(1);
		IJavaInstance delegate_control = BeanUtilities.getFeatureValue((IJavaInstance)getModel(),SwtPlugin.DELEGATE_CONTROL);
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}
	}

}
