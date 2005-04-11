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
 *  $RCSfile: LinkJavaClassCellEditor.java,v $
 *  $Revision: 1.1 $  $Date: 2005-04-11 17:31:49 $ 
 */
package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;
import org.eclipse.jem.java.JavaRefFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.DefaultJavaClassCellEditor;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

public class LinkJavaClassCellEditor extends DefaultJavaClassCellEditor {
	
	private String value;
	private boolean infoMessageDisplayed = false;

	public LinkJavaClassCellEditor(Composite aComposite){
		super(aComposite);
	}
	
	protected String getJavaInitializationString(String aString) {
		return BeanUtilities.createStringInitString(aString);
	}

	protected String doGetString(Object value) {
		if (isInstance(value)) {
			return ((IStringBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain))).stringValue();
		} else if (value != null) {
			// Since this is not a string value, the text widget is disabled, 
			// just return the toString of this object.
			return BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain)).toBeanString();
		} else
			return null;
	}

	protected String isCorrectString(String value) {
		this.infoMessageDisplayed = false;
		this.value = value;
		Shell shell = new Shell();
		Link link = null;
		try{
			link = new Link(shell, SWT.NONE);
			if("".equals(value)) value = null;
			link.setText(value);
		}
		catch(Exception e){
			this.infoMessageDisplayed = true;
			return SWTMessages.getString("LinkJavaClassCellEditor.NoLink_ERROR_");
		}
		return null;
	}
	
	public void setData(Object data) {
		super.setData(data);
		setJavaType(JavaRefFactory.eINSTANCE.reflectType("java.lang.String", JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
	}

	protected void fireApplyEditorValue() {
		super.fireApplyEditorValue();
		if( !this.infoMessageDisplayed && this.isDirty() && this.value != null){
			this.infoMessageDisplayed = true;
			int openA = this.value.indexOf("<a>");
			int closeA = this.value.indexOf("</a>");
			if( (openA == -1) || (closeA == -1) || (openA > closeA) ){
				String message = MessageFormat.format(SWTMessages.getString("LinkJavaClassCellEditor.NoLink_INFO_"), new Object[] {this.value}); //$NON-NLS-1$
				MessageDialog.openInformation(null,SWTMessages.getString("LinkJavaClassCellEditor.NoLinkInfoTitle"), message); //$NON-NLS-1$
			}
		}
	}
}
