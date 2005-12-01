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
 *  $RCSfile: GridLayoutAddedCellFeedbackFigure.java,v $
 *  $Revision: 1.1 $  $Date: 2005-12-01 20:19:43 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.SWT;

/**
 * Feedback figure to show outer line of an added cell outside the grid. 
 * @since 1.2.0
 */
public class GridLayoutAddedCellFeedbackFigure extends RectangleFigure {


	public GridLayoutAddedCellFeedbackFigure() {
		super();
		setLineStyle(SWT.LINE_DASH);
		setLineWidth(2);
		setForegroundColor(ColorConstants.yellow);
		setFill(false);
	}

}
