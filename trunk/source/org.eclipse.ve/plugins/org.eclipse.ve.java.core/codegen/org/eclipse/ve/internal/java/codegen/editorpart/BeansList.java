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
package org.eclipse.ve.internal.java.codegen.editorpart;
/*
 *  $RCSfile: BeansList.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:47 $ 
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
	msgPage.setMessage(CodegenEditorPartMessages.BeansList_DefaultPageMessage); 
	return msgPage;
}
}
