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
 *  $RCSfile: LocaleEditor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.Locale;

public class LocaleEditor extends java.beans.PropertyEditorSupport {
	protected java.lang.Object fLocale = null;
	protected LocalePropertyEditor fCustomEditor = null;
	
public String getAsText(){
	if (fLocale != null)
		return ((java.util.Locale)fLocale).getDisplayName();
	return null;
}

public java.awt.Component getCustomEditor(){
	if(fCustomEditor == null){
		Locale aLocale = (Locale)getValue();
		if (aLocale == null){
			fCustomEditor = new LocalePropertyEditor();
		}
		else{
			fCustomEditor = new LocalePropertyEditor(aLocale);
		}
	}
	return fCustomEditor;
}

public String getJavaInitializationString(){
	Locale locale = (Locale) fLocale;
	if (fCustomEditor != null){
		return fCustomEditor.getLocaleInitializationString();
	}
	else{
		return"new java.util.Locale(\" " + locale.getLanguage() + "\", \"" + locale.getCountry() + "\", \"" + locale.getVariant() + "\")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}

public Object getValue(){
	if (fCustomEditor != null){
		return fCustomEditor.getLocaleValue();
	}else{
		return super.getValue();
	}
}

public void setValue(Object newValue){
	fLocale = newValue;
	if(fCustomEditor != null){
		fCustomEditor.setLocaleValue((Locale)newValue);
	}
	super.setValue(newValue);
}

public boolean supportsCustomEditor(){
	return true;
}

}
