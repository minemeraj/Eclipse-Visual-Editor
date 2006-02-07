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
package org.eclipse.ve.internal.cde.utility.impl;
/*
 *  $RCSfile: GraphicImpl.java,v $
 *  $Revision: 1.4 $  $Date: 2006-02-07 17:21:33 $ 
 */
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.swt.graphics.Image;

import org.eclipse.ve.internal.cde.utility.Graphic;
import org.eclipse.ve.internal.cde.utility.UtilityPackage;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graphic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class GraphicImpl extends EObjectImpl implements Graphic {

	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected GraphicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return UtilityPackage.Literals.GRAPHIC;
	}

	public Image getImage() {
		return null;
	}

}
