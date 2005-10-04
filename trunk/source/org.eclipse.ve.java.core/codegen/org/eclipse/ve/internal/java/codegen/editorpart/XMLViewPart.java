/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: XMLViewPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-04 17:04:41 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.io.ByteArrayOutputStream;
import java.util.*;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.XMLTextPage;
 

public class XMLViewPart extends ContentOutline {
	
	public class XMLViewTextPage extends XMLTextPage implements IContentOutlinePage {
		protected Map XML_SAVE_CACHE_OPTIONS = new HashMap(3);
		protected EditDomain domain;
		protected CommandStackListener stackListener = new CommandStackListener(){
			public void commandStackChanged(EventObject event) {
				refresh();
			}	        	
        };
        
		public XMLViewTextPage(EditDomain anEditDomain) {
			domain = anEditDomain;
			XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
			XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));
	        XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8");//$NON-NLS-1$	
	        domain.getCommandStack().addCommandStackListener(stackListener);
		}
		
		public void dispose() {
			domain.getCommandStack().removeCommandStackListener(stackListener);
			super.dispose();
		}
		public void refresh() {
			if(domain.getDiagramData() != null){
				Resource resource = domain.getDiagramData().eResource(); 
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					resource.save(os, XML_SAVE_CACHE_OPTIONS);
					setText(os.toString());
				} catch (Exception e) {
					setText(""); //$NON-NLS-1$
				}
			}
		}
		public void addSelectionChangedListener(ISelectionChangedListener listener) {			
		}
		public ISelection getSelection() {
			return null;
		}
		public void removeSelectionChangedListener(ISelectionChangedListener listener) {			
		}
		public void setSelection(ISelection selection) {			
		}
	}

	protected PageRec doCreatePage(IWorkbenchPart part) {
		// Try to get a resource
		if(part instanceof JavaVisualEditorPart){
			EditDomain domain = ((JavaVisualEditorPart)part).getEditDomain();
			// Create an XMLTextPage
			XMLViewTextPage page = new XMLViewTextPage(domain);
			page.refresh();
 			initPage(page);
			page.createControl(getPageBook());
			return new PageRec(part, page);
		}
		// There is no beans list
		return null;
	}
	
	protected IPage createDefaultPage(PageBook book) {
		MessagePage msgPage = (MessagePage) super.createDefaultPage(book);
		msgPage.setMessage("For use with the Visual Editor"); 
		return msgPage;
	}	

}