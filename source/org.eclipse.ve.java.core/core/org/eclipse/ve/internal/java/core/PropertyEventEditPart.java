/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * This allows edit parts to be polymorphically selected for a given property event
 * and propertyEventSelected(PropertyEvent propertyEvent) return true if the child edit part was selected
 */
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: PropertyEventEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.ve.internal.jcm.PropertyEvent;

public interface PropertyEventEditPart {
	
	boolean selectPropertyEvent(PropertyEvent aPropertyEvent);

}
