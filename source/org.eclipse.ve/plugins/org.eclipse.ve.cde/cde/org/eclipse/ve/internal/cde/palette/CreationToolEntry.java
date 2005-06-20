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
 *  $RCSfile: CreationToolEntry.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-20 23:54:40 $ 
 */
package org.eclipse.ve.internal.cde.palette;

import org.eclipse.ve.internal.cdm.KeyedValueHolder;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Creation Tool Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Creation Tool
 * <p>
 * This is abstract.
 * <p>
 * If there are any annotation values, then an annotation will be created for the object that the factory returns. The annotation values come from the mixin KeyedValueHolder.keyedValues.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.ve.internal.cde.palette.PalettePackage#getCreationToolEntry()
 * @model abstract="true"
 * @generated
 */
public interface CreationToolEntry extends AbstractToolEntry, KeyedValueHolder{
} // CreationToolEntry
