package org.eclipse.ve.internal.java.codegen.core;
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
 *  $RCSfile: TransientErrorEvent.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import org.eclipse.emf.ecore.EObject;

/**
 * @version 	1.0
 * @author
 */
public class TransientErrorEvent implements ITransientErrorEvent{
	
	private String code = null;
	private String handle = null;
	private String type = null;
	private EObject refObject = null;
	private int sourceStart = -1;
	private int sourceEnd = -1;
	private String message = null;
	
	public TransientErrorEvent(
				String type, 
				EObject refObject, 
				int srcStart, 
				int srcEnd, 
				String message, 
				String handle, 
				String code){
		setErrorType(type);
		setRefObject(refObject);
		setSourceStart(srcStart);
		setSourceEnd(srcEnd);
		setMessage(message);
		setElementSource(code);
		setElementHandle(handle);
	}

	public TransientErrorEvent(
				String type, 
				EObject refObject, 
				String message){
		this(type, refObject, -1, -1, message, null, null);
	}

	public String getErrorType(){
		return type;
	}
	
	public String getElementHandle(){
		return handle;
	}
	
	public String getElementSource(){
		return code;
	}
	
	public EObject getRefObject(){
		return refObject;
	}
	
	public int getSourceStart(){
		return sourceStart;
	}
	
	public int getSourceEnd(){
		return sourceEnd;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getSource(){
		if(getElementSource()==null)
			return null;
		if(getSourceStart()>-1 && getSourceEnd()<getElementSource().length())
			return getElementSource().substring(getSourceStart(), getSourceEnd());
		return null;
	}
	
	public void setErrorType(String type){
		this.type = type;
	}
	
	public void setRefObject(EObject refObject){
		this.refObject = refObject;
	}
	
	public void setSourceStart(int sourceStart){
		this.sourceStart= sourceStart;
	}
	
	public void setSourceEnd(int sourceEnd){
		this.sourceEnd = sourceEnd;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void setElementSource(String code){
		this.code = code;
	}
	
	public void setElementHandle(String handle){
		this.handle = handle;
	}
}
