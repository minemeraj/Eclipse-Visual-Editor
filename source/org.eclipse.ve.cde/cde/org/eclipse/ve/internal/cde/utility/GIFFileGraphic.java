/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.utility;
/*
 *  $RCSfile: GIFFileGraphic.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:12:49 $ 
 */

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GIF File Graphic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class caches the Image from a resource name of a gif file.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.ve.internal.cde.utility.GIFFileGraphic#getResourceName <em>Resource Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getGIFFileGraphic()
 * @model 
 * @generated
 */
public interface GIFFileGraphic extends Graphic{


	/**
	 * Returns the value of the '<em><b>Resource Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is the resource name of the gif file. It is of the form of a URL. This means that to access GIF's within a plugin, use the format of "platform:/plugin/PLUGINID/GIFILENAME".
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resource Name</em>' attribute.
	 * @see #setResourceName(String)
	 * @see org.eclipse.ve.internal.cde.utility.UtilityPackage#getGIFFileGraphic_ResourceName()
	 * @model 
	 * @generated
	 */
	String getResourceName();

	/**
	 * Sets the value of the '{@link org.eclipse.ve.internal.cde.utility.GIFFileGraphic#getResourceName <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Name</em>' attribute.
	 * @see #getResourceName()
	 * @generated
	 */
	void setResourceName(String value);

}
