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
package org.eclipse.ve.internal.cdm.model;
/*
 *  $RCSfile: CDMModelConstants.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */


public interface CDMModelConstants {
	
	public static final String
		// This is the key in the visual info for the visual constraint.
		// The constraint could be a Point, Dimension, or Rectangle from com.ibm.etools.ocm.model.
		VISUAL_CONSTRAINT_KEY = "org.eclipse.ve.internal.cdm.model.visualconstraintkey", //$NON-NLS-1$
		
		// This is the key in the visual info for the bendPoints of a connection.
		VISUAL_BENDPOINTS_KEY = "org.eclipse.ve.internal.cdm.model.visualbendpointskey"; //$NON-NLS-1$


}
