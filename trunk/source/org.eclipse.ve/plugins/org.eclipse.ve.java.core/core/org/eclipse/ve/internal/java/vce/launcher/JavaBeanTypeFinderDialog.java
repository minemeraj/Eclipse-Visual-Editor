package org.eclipse.ve.internal.java.vce.launcher;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaBeanTypeFinderDialog.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 13:00:22 $ 
 */

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

/**
 * A dialog to select a bean type from a list of types. The dialog allows
 * multiple selections.
 */
public class JavaBeanTypeFinderDialog extends TwoPaneElementSelector {

	private IRunnableContext fRunnableContext;
	private IJavaElement fElem;
	private int fStyle;
	
	private static class PackageRenderer extends JavaElementLabelProvider {
		public PackageRenderer() {
			super(JavaElementLabelProvider.SHOW_PARAMETERS | JavaElementLabelProvider.SHOW_POST_QUALIFIED | JavaElementLabelProvider.SHOW_ROOT);	
		}

		public Image getImage(Object element) {
			return super.getImage(((IType)element).getPackageFragment());
		}
		
		public String getText(Object element) {
			return super.getText(((IType)element).getPackageFragment());
		}
	}
	
	/**
	 * Constructor.
	 */
	public JavaBeanTypeFinderDialog(Shell shell, IRunnableContext context,
		IJavaElement elem, int style)
	{
		super(shell, new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_BASICS | JavaElementLabelProvider.SHOW_OVERLAY_ICONS), 
			new PackageRenderer());

		Assert.isNotNull(context);

		fRunnableContext= context;
		fElem= elem;
		fStyle= style;
	}

	/*
	 * @see Window#open()
	 */
	public int open() {
		JavaBeanSearchEngine engine= new JavaBeanSearchEngine();
		IType[] types;
		try {
			types= engine.searchJavaBeans(fRunnableContext, fElem, fStyle);
		} catch (InterruptedException e) {
			return CANCEL;
		} catch (InvocationTargetException e) {
			ExceptionHandler.handle(e, VCELauncherMessages.ErrorDialog_Title, e.getMessage()); 
			return CANCEL;
		}
		
		setElements(types);
		return super.open();
	}
	
}