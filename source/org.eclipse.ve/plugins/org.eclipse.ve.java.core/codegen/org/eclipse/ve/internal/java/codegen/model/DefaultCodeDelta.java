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
package org.eclipse.ve.internal.java.codegen.model;
/*
 *  $RCSfile: DefaultCodeDelta.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */

import java.util.*;

/**
 * This CodeDelta stores changes only to ONE method and ONE field.
 * It also stores the states of all the expressions inside the 
 * updated method.
 */
public class DefaultCodeDelta 
	implements ICodeDelta {

protected CodeMethodRef method = null;
protected BeanPart beanPart = null;
protected Hashtable table = null;

public DefaultCodeDelta(){
	beanPart = null;
	method = null;
	table = new Hashtable();
}

public void addDeltaMethod(CodeMethodRef method){
	this.method = method;
	setElementStatus(method, ICodeDelta.ELEMENT_NO_CHANGE);
}

public void addDelataField(BeanPart beanPart){
	this.beanPart = beanPart;
	setElementStatus(this.beanPart, ICodeDelta.ELEMENT_NO_CHANGE);
}

public List getDeletedElements(AbstractCodeRef element){
	List deleted = new ArrayList();
	if(table.containsKey(element)){
		// The method is in the table.. hence return entered deleted items.
		Enumeration keys = table.keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			if(key instanceof BeanPart){
				;
			}
			if(key instanceof AbstractCodeRef){
				if(getElementStatus((AbstractCodeRef)key)==ICodeDelta.ELEMENT_DELETED)
					deleted.add(key);
			}
		}
	}
	return deleted;
}

public CodeMethodRef getDeltaMethod (){
	return method;
}

public BeanPart  getDeltaField(){
	return beanPart;
}

public int getElementStatus(AbstractCodeRef element){
	return getStatus(element);
}

public int getElementStatus(BeanPart element){
	return getStatus(element);
}

protected int getStatus(Object key){
	if(table.containsKey(key)){
		Integer status = (Integer) table.get(key);
		return status.intValue();
	}else{
		return -1;
	}
}

public void setElementStatus(AbstractCodeRef code, int status){
	setStatus(code, status);
}

public void setElementStatus(BeanPart bean, int status){
	setStatus(bean, status);
}

protected void setStatus(Object object, int status){
	table.put(object, new Integer(status));
}

public String toString() {
	StringBuffer sb = new StringBuffer() ;
	sb.append(beanPart+" : "+method+"\n") ; //$NON-NLS-1$ //$NON-NLS-2$
	Enumeration e = table.keys();
	while (e.hasMoreElements()) {
		Object key = e.nextElement();
		sb.append(key) ;
		String status ;
		switch (getStatus(key)) {
			case ICodeDelta.ELEMENT_NO_CHANGE:  status = " NO Change" ;//$NON-NLS-1$
			                                   	break ;
			case ICodeDelta.ELEMENT_DELETED:  	status = " Deleted" ;//$NON-NLS-1$
			                                   	break ;
			case ICodeDelta.ELEMENT_ADDED:  	status = " Added" ;//$NON-NLS-1$
			                                   	break ;			                                   
			case ICodeDelta.ELEMENT_CHANGED:  	status = " Changed" ;//$NON-NLS-1$
			                                   	break ;			                                   
			case ICodeDelta.ELEMENT_UPDATED_OFFSETS:  status = " Offset Changed" ;//$NON-NLS-1$
			                                   break ;				                                   		                                   
			case ICodeDelta.ELEMENT_UNDETERMINED:  status = " Undetermined" ;//$NON-NLS-1$
			                                   	break ;	
			default: status ="???" ; //$NON-NLS-1$
			                                   			
		}
		sb.append(status) ;
		
	}
	return sb.toString() ;
	
}

}
