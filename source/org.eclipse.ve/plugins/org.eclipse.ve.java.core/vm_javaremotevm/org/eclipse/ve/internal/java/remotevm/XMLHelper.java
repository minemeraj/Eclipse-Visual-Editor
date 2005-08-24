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
package org.eclipse.ve.internal.java.remotevm;
/*
 *  $RCSfile: XMLHelper.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:49 $ 
 */

import java.io.*;
import java.lang.reflect.*;

public class XMLHelper {

	static boolean LOOKEDUP_DECODER;	
	static Class XML_DECODER_CLASS;
	static Method DECODER_READOBJECT_METHOD;
	static Constructor DECODER_CONSTRUCTOR;
	
	static boolean LOOKEDUP_ENCODER;
	static Class XML_ENCODER_CLASS;
	static Method ENCODER_WRITEOBJECT_METHOD;	
	static Method ENCODER_CLOSE_METHOD;
	static Constructor ENCODER_CONSTRUCTOR;	

public static Object getDecodedObject(String xmlString){
	// Cache and lookup the java.beans.XMLDecoder class and the readObject() method
	if (!LOOKEDUP_DECODER){
		LOOKEDUP_DECODER = true;
		try {
			XML_DECODER_CLASS = Class.forName("java.beans.XMLDecoder"); //$NON-NLS-1$
			DECODER_READOBJECT_METHOD = XML_DECODER_CLASS.getMethod("readObject",null); //$NON-NLS-1$
			DECODER_CONSTRUCTOR = XML_DECODER_CLASS.getConstructor(new Class[]{ByteArrayInputStream.class});
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {		
		}
	}
	
	// Create a Decoder instance and invoke the readObject() method to evaluate the xmlString argument	
	if (XML_DECODER_CLASS != null){	
		try {
			Object xmlDecoder = DECODER_CONSTRUCTOR.newInstance(new Object[]{new ByteArrayInputStream(xmlString.getBytes())});
			return DECODER_READOBJECT_METHOD.invoke(xmlDecoder,null);
		} catch (InstantiationException e) {			
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
	}
	return null;
}

public static String getEncodedString(Object bean){
	
	// Cache and lookup the java.beans.XMLEncoder class and the writeObject(Object javaBean) method
	if (!LOOKEDUP_ENCODER){
		LOOKEDUP_ENCODER = true;
		try {
			XML_ENCODER_CLASS = Class.forName("java.beans.XMLEncoder"); //$NON-NLS-1$
			ENCODER_WRITEOBJECT_METHOD = XML_ENCODER_CLASS.getMethod("writeObject",new Class[] {Object.class}); //$NON-NLS-1$
			ENCODER_CLOSE_METHOD = XML_ENCODER_CLASS.getMethod("close",null);			 //$NON-NLS-1$
			ENCODER_CONSTRUCTOR = XML_ENCODER_CLASS.getConstructor(new Class[]{BufferedOutputStream.class});
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {		
		}
	}
	
	// Create an Encoder instance and invoke the writeObject() method to encode the argument	
	try{
		ByteArrayOutputStream stringCollecter = new ByteArrayOutputStream();
		Object encoder = ENCODER_CONSTRUCTOR.newInstance(new Object[]{new BufferedOutputStream(stringCollecter)}); 
		ENCODER_WRITEOBJECT_METHOD.invoke(encoder,new Object[] {bean});
		ENCODER_CLOSE_METHOD.invoke(encoder,null);
		return stringCollecter.toString();
	} catch (InstantiationException e) {			
	} catch (IllegalArgumentException e) {
	} catch (IllegalAccessException e) {
	} catch (InvocationTargetException e) {
	}
	return null;
}

}
