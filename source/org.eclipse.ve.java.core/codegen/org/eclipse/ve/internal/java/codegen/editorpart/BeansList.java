package org.eclipse.ve.internal.java.codegen.editorpart;
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
 *  $RCSfile: BeansList.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class BeansList extends ContentOutline {
	

protected PageRec doCreatePage(IWorkbenchPart part) {
	// Try to get a beans list
	Object obj = part.getAdapter(this.getClass());
	if (obj instanceof IContentOutlinePage) {
		IContentOutlinePage page = (IContentOutlinePage)obj;
		if (page instanceof IPageBookViewPage) 
			initPage((IPageBookViewPage)page);
		page.createControl(getPageBook());
		return new PageRec(part, page);
	}
	// There is no beans list
	return null;
}
protected IPage createDefaultPage(PageBook book) {
	MessagePage msgPage = (MessagePage) super.createDefaultPage(book);
	msgPage.setMessage(CodegenEditorPartMessages.getString("BeansList.DefaultPageMessage")); //$NON-NLS-1$
	return msgPage;
}
}