package org.eclipse.ve.internal.cde.palette;

import org.eclipse.gef.Tool;

/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractToolEntry.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * </p>
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getAbstractToolEntry()
 * @model abstract="true"
 * @generated
 */
public interface AbstractToolEntry extends Entry {


	
	public Tool getTool();
}
