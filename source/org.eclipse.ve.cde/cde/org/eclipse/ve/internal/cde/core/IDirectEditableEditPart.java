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
 *  $RCSfile: IDirectEditableEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-03-21 22:48:08 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.emf.ecore.EStructuralFeature;
 

/**
 * @since 1.0.0
 *
 */
public interface IDirectEditableEditPart {
	public EStructuralFeature getSfDirectEditProperty();
}
