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
 *  $RCSfile: UnExecutableCommandData.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-27 22:29:27 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.swt.graphics.Image;
 

public class UnExecutableCommandData {
private String message = null;
private Image image = null;

public UnExecutableCommandData(String message){
	setMessage(message);
}
public UnExecutableCommandData(String message, Image image){
	setMessage(message);
	setImage(image);
}
public Image getImage() {
	return image;
}

public void setImage(Image image) {
	this.image = image;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}
}
