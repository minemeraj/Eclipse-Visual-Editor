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
 *  $RCSfile: GraphicalEditPartContributor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-09-14 23:18:16 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
 

public interface GraphicalEditPartContributor {
	IFigure getHoverOverLay();
	IFigure getFigureOverLay();
}
