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
 *  $RCSfile: PrototypeFactory.java,v $
 *  $Revision: 1.2 $  $Date: 2004-06-03 14:38:53 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.*;

 /**
 * Returns a default instance based on the class argument.  Used by ChooseBean and other places where
 * a non-default EMF instance should be created (such as SWT)  
 *
 * @since 1.0.0
 */
public interface PrototypeFactory {
	
	public static final String PROTOTYPE_FACTORY_KEY = "org.eclipse.ve.internal.prototypefactory";	 //$NON-NLS-1$
	
EObject createPrototype(EClass aClass);
	
}
