package org.eclipse.ve.internal.java.remotevm;
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
 *  $RCSfile: XMLHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.beans.*;
import java.io.*;

public class XMLHelper {

public static Object getDecodedObject(String xmlString){
	XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
	Object ret = decoder.readObject();
	return ret;
}

public static String getEncodedString(Object bean){
	ByteArrayOutputStream stringCollecter = new ByteArrayOutputStream();
	XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(stringCollecter));
	encoder.writeObject(bean);
	encoder.close();
	return stringCollecter.toString();
}

}