/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TypeFilterMatcher.java,v $
 *  $Revision: 1.1 $  $Date: 2004-03-05 18:14:26 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jdt.internal.ui.util.StringMatcher;
import org.eclipse.ui.dialogs.FilteredList;
 

class TypeFilterMatcher implements FilteredList.FilterMatcher {

	private List yesFQNs = null;
	private List noFQNs = null;
	
	private static final char END_SYMBOL= '<';
	private static final char ANY_STRING= '*';

	private StringMatcher fMatcher;
	private StringMatcher fQualifierMatcher;
	
	/**
	 * Absolutely no yes or no preference list. Behaviour defaults
	 * to the Type selection dialogs behaviour.
	 * 
	 * @since 1.0.0
	 */
	public TypeFilterMatcher(){}
	
	/**
	 * Preference list for yes and no classes provided. Along with
	 * the normal type selection dialog behaviour, additional checks
	 * are performed based on preference lists provided. 
	 * 
	 * @param yesFQNs
	 * @param noFQNs
	 * 
	 * @since 1.0.0
	 */
	public TypeFilterMatcher(List yesFQNs, List noFQNs){
		this.yesFQNs = yesFQNs;
		this.noFQNs = noFQNs;
		if(this.yesFQNs==null)
			this.yesFQNs = new ArrayList();
		if(this.noFQNs==null)
			this.noFQNs = new ArrayList();
	}
	
	/*
	 * @see FilteredList.FilterMatcher#setFilter(String, boolean)
	 */
	public void setFilter(String pattern, boolean ignoreCase, boolean igoreWildCards) {
		int qualifierIndex= pattern.lastIndexOf("."); //$NON-NLS-1$

		// type			
		if (qualifierIndex == -1) {
			fQualifierMatcher= null;
			fMatcher= new StringMatcher(adjustPattern(pattern), ignoreCase, igoreWildCards);
			
		// qualified type
		} else {
			fQualifierMatcher= new StringMatcher(pattern.substring(0, qualifierIndex), ignoreCase, igoreWildCards);
			fMatcher= new StringMatcher(adjustPattern(pattern.substring(qualifierIndex + 1)), ignoreCase, igoreWildCards);
		}
	}

	/*
	 * @see FilteredList.FilterMatcher#match(Object)
	 */
	public boolean match(Object element) {
		if (!(element instanceof TypeInfo))
			return false;

		TypeInfo type= (TypeInfo) element;

		if (!fMatcher.match(type.getTypeName()))
			return false;
		
		if (fQualifierMatcher == null){
			if(yesFQNs==null || noFQNs==null){
				return true;
			}else{
				if (noFQNs.contains(type.getFullyQualifiedName()))
					return false;
				if(yesFQNs.contains(type.getFullyQualifiedName()))
					return true;
				return false;
			}
		}
		
		if(yesFQNs==null || noFQNs==null){
			return fQualifierMatcher.match(type.getTypeContainerName());
		}else{
			return 	fQualifierMatcher.match(type.getTypeContainerName()) &&
					!noFQNs.contains(type.getFullyQualifiedName()) &&
					yesFQNs.contains(type.getFullyQualifiedName());
		}
	}
	
	private String adjustPattern(String pattern) {
		int length= pattern.length();
		if (length > 0) {
			switch (pattern.charAt(length - 1)) {
				case END_SYMBOL:
					pattern= pattern.substring(0, length - 1);
					break;
				case ANY_STRING:
					break;
				default:
					pattern= pattern + ANY_STRING;
			}
		}
		return pattern;
	}
}