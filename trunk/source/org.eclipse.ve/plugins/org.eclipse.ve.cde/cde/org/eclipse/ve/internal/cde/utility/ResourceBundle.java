/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.utility;
/*
 *  $RCSfile: ResourceBundle.java,v $
 *  $Revision: 1.3 $  $Date: 2004-08-27 15:35:35 $ 
 */


import org.eclipse.emf.ecore.EObject;
/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Bundle</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getResourceBundle()
 * @model abstract="true"
 * @generated
 */
public interface ResourceBundle extends EObject{


	public java.util.ResourceBundle getBundle();	// Return the Resource bundle
	
}
