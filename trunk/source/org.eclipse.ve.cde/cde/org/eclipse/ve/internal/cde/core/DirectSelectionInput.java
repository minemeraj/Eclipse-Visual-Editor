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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: DirectSelectionInput.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */

/**
 * This is a hack interface.  It is there so that the editor part can be located and given its new model to select
 * It is used by jcm so that after an event is created the source range can be selected.  This works right now
 * with edit parts, but for the graph viewer there is no edit part, hence this interface
 */
public interface DirectSelectionInput {
	
	void modelSelected(Object model);

}
