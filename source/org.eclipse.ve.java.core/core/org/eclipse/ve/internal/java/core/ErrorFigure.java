package org.eclipse.ve.internal.java.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ErrorFigure.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;

public class ErrorFigure extends Figure {
	
	int fSev;
	Image fImage;
	
	public ErrorFigure(int severity){
		sevSeverity(severity);
		// Make our bounds be the size of the icon we will display
	}
	
	protected void paintFigure(Graphics graphics){
		
		super.paintFigure(graphics);
		if ( fImage != null){
			// Clear some background so the image can be seen			
			graphics.drawImage(fImage,getLocation().x,getLocation().y);
		}
	}
	
	public void sevSeverity(int severity){
		fSev = severity;
		switch(fSev){
			case IBeanProxyHost.ERROR_SEVERE:
				fImage = IBeanProxyHost.ErrorType.getSevereErrorImage();
				setSize(new Dimension(fImage.getBounds().width,fImage.getBounds().height));
			break;
			case IBeanProxyHost.ERROR_WARNING:
				fImage = IBeanProxyHost.ErrorType.getWarningErrorImage();
				setSize(new Dimension(fImage.getBounds().width,fImage.getBounds().height));
				break;
			case IBeanProxyHost.ERROR_INFO:
				fImage = IBeanProxyHost.ErrorType.getInformationErrorImage();
				setSize(new Dimension(fImage.getBounds().width,fImage.getBounds().height));
				break;				
			default:
				setSize(0,0);
		}				
	}
}
