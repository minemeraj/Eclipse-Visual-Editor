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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: CursorEditor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.awt.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (11/14/00 2:23:39 PM)
 * @author: Sri
 */
public class CursorEditor extends java.beans.PropertyEditorSupport {
	
	protected Object fCursor;
	private static Hashtable realToDisp = null;
	private static TreeMap dispToReal = null;
	private static Hashtable realToType = null;
	private static Hashtable typeToReal = null;
/**
 * Insert the method's description here.
 * Creation date: (11/17/00 2:39:34 PM)
 */
private void createHashes() {
	if( realToDisp==null || dispToReal==null || realToType==null || typeToReal==null){
		
		// using TreeMap for display names to get names displayed in alphabetical order.
		dispToReal = new TreeMap();
		realToDisp = new Hashtable(20);
		typeToReal = new Hashtable(20);
		realToType = new Hashtable(20);
		Class cClass = Cursor.class;
		java.lang.reflect.Field fields[] = cClass.getFields();
		for(int i=0; i<fields.length; i++){
			String realName = fields[i].getName();
			String dispName = fields[i].getName();
			// Don't use Cursor.CUSTOM_CURSOR since the type is set when for user defined cursors
			// See java.awt.Cursor(String).
			if (realName.equals("CUSTOM_CURSOR")) //$NON-NLS-1$
				continue;
			dispName = dispName.substring(0,1)+dispName.substring(1, dispName.indexOf("_CURSOR")).toLowerCase(); //$NON-NLS-1$
			if( dispName.indexOf('_') != -1)dispName = dispName.substring(0,dispName.indexOf('_',0)).toUpperCase() + dispName.substring( dispName.indexOf('_',0), dispName.length());
			try{
				Integer type = new Integer(fields[i].getInt(null));
				typeToReal.put(type, realName);
				realToType.put(realName, type);
				dispName = Cursor.getPredefinedCursor(type.intValue()).getName();
			}catch(Exception e){;}
			dispToReal.put(dispName, realName);
			realToDisp.put(realName, dispName);
		}
	}
}
/**
 * Use reflection to find the type of Cursors' String name.
 * Creation date: (11/14/00 2:27:02 PM)
 * @return java.lang.String
 */
public String getAsText() {
	if( fCursor != null ){
		Cursor cursor = (Cursor) fCursor;
		int type = cursor.getType();
		Class cClass = cursor.getClass();
		java.lang.reflect.Field fields[] = cClass.getFields();
		if( dispToReal==null || realToDisp==null || typeToReal==null || realToType==null ){createHashes();}
		for(int i=0; i<fields.length; i++){
			try{
				if( fields[i].getInt( cursor ) == type ){
					return (String)realToDisp.get(fields[i].getName());
				}
			}catch(Exception e){;}
		}
		return cursor.toString();
	}
	return null;
}
	//----------------------------------------------------------------------

	/**
	 * This method is intended for use when generating Java code to set
	 * the value of the property.  It should return a fragment of Java code
	 * that can be used to initialize a variable with the current property
	 * value.
	 * <p>
	 * Example results are "2", "new Color(127,127,34)", "Color.orange", etc.
	 *
	 * @return A fragment of Java code representing an initializer for the
	 *   	current value.
	 */
public String getJavaInitializationString() {
	if( fCursor == null) return ""; //$NON-NLS-1$
	String realName = (String)typeToReal.get(new Integer(((Cursor)fCursor).getType()));
	return "new java.awt.Cursor(" + "java.awt.Cursor."+ realName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
}
	public String[] getTags() {
		if( dispToReal==null || realToDisp==null || typeToReal==null || realToType==null ){createHashes();}
		Iterator itr = dispToReal.keySet().iterator();
		String res[] = new String[dispToReal.size()];
		int i=0;
		while( itr.hasNext() ){
			res[i]=(String)itr.next();
			i++;
		}
		return res;
	}
	public void setAsText(String text) throws java.lang.IllegalArgumentException {
	if (text != null) {
		
	    setValue(new Cursor(((Integer)realToType.get(dispToReal.get(text))).intValue()));
	    return;
	}
	throw new java.lang.IllegalArgumentException(text);
	}
/**
 * Insert the method's description here.
 * Creation date: (11/14/00 2:27:47 PM)
 * @param newValue java.lang.Object
 */
public void setValue(Object newValue) {
	super.setValue( newValue );
	fCursor = newValue;
	if( dispToReal==null || realToDisp==null || typeToReal==null || realToType==null ){createHashes();}
}
}
