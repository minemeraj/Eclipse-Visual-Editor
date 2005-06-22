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
 *  $Revision: 1.6 $  $Date: 2005-06-22 16:24:10 $ 
 */
package org.eclipse.ve.internal.swt;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.java.core.*;

public class LinkJavaClassCellEditor extends DefaultJavaClassCellEditor {

	private String value;
	private boolean infoMessageDisplayed = false;

	public LinkJavaClassCellEditor(Composite aComposite) {
		super(aComposite);
	}

	protected String getJavaInitializationString(String aString) {
		return BeanUtilities.createStringInitString(aString);
	}

	protected String doGetString(Object value) {
		if (isInstance(value)) {
			return ((IStringBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) value, JavaEditDomainHelper.getResourceSet(fEditDomain)))
					.stringValue();
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
		try {
			link = new Link(shell, SWT.NONE);
			if ("".equals(value))value = null; //$NON-NLS-1$
			link.setText(value);
		} catch (Exception e) {
			this.infoMessageDisplayed = true;
			return SWTMessages.LinkJavaClassCellEditor_NoLink_ERROR_; 
		}
		return null;
	}

	public void setData(Object data) {
		super.setData(data);
		setJavaType(JavaRefFactory.eINSTANCE.reflectType("java.lang.String", JavaEditDomainHelper.getResourceSet(fEditDomain))); //$NON-NLS-1$
	}

	protected void fireApplyEditorValue() {
		super.fireApplyEditorValue();
		if (value != null) {
			String lowCaseValue = value.toLowerCase();
			int openA = lowCaseValue.indexOf("<a>"); //$NON-NLS-1$
			int closeA = lowCaseValue.indexOf("</a>"); //$NON-NLS-1$
			if (((openA == -1) || (closeA == -1) || (openA > closeA)) && (!infoMessageDisplayed && isDirty())) {
				infoMessageDisplayed = true;
				final Shell shell = Display.getCurrent().getActiveShell();
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						if (!shell.isDisposed()) {
							String message = MessageFormat.format(
									SWTMessages.LinkJavaClassCellEditor_NoLink_INFO_, new Object[] { value}); 
							MessageDialog.openInformation(shell, SWTMessages.LinkJavaClassCellEditor_NoLinkInfoTitle, message); 
						}
					}
				});
			}
		}
	}
}
