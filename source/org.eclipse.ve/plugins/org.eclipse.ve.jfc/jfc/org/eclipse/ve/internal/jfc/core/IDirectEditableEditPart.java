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
 *  $RCSfile: IDirectEditableEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-07 20:34:58 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EStructuralFeature;
 

/**
 * @since 1.0.0
 *
 */
public interface IDirectEditableEditPart {
	public EStructuralFeature getSfDirectEditProperty();
}
