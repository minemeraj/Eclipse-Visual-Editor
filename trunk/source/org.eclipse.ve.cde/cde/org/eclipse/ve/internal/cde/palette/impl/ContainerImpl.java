package org.eclipse.ve.internal.cde.palette.impl;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ContainerImpl.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:18:00 $ 
 */
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.ve.internal.cde.palette.Container;
import org.eclipse.ve.internal.cde.palette.PalettePackage;
/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class ContainerImpl extends EObjectImpl implements Container {

	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	protected ContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return PalettePackage.eINSTANCE.getContainer();
	}

	public List getChildren() {
		return Collections.EMPTY_LIST;
	}

	public String getLabel() {
		return null;
	}
	public ImageDescriptor getLargeIcon() {
		return null;
	}
	public ImageDescriptor getSmallIcon() {
		return null;
	}
	public String getDescription() {
		return null;
	}
	
	public PaletteEntry getEntry() {
		PaletteContainer c = createPaletteContainer();
		c.setDescription(getDescription());
		c.setSmallIcon(getSmallIcon());
		c.addAll(getChildren());
		return c;
	}
	
	protected abstract PaletteContainer createPaletteContainer();

}
