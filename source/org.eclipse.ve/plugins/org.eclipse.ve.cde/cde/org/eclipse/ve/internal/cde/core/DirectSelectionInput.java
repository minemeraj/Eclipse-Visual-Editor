package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DirectSelectionInput.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:17:59 $ 
 */

/**
 * This is a hack interface.  It is there so that the editor part can be located and given its new model to select
 * It is used by jcm so that after an event is created the source range can be selected.  This works right now
 * with edit parts, but for the graph viewer there is no edit part, hence this interface
 */
public interface DirectSelectionInput {
	
	void modelSelected(Object model);

}
