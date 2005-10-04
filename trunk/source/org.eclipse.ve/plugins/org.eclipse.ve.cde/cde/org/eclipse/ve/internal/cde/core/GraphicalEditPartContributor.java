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
 *  $Revision: 1.3 $  $Date: 2005-09-29 15:06:59 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
 
/**
 * Graphical Editpart Contributor inteface.
 * 
 * @since 1.2.0
 */
public interface GraphicalEditPartContributor extends EditPartContributor {
	public IFigure getHoverOverLay();
	public IFigure getFigureOverLay();
	public GraphicalEditPart [] getActionBarChildren();
}