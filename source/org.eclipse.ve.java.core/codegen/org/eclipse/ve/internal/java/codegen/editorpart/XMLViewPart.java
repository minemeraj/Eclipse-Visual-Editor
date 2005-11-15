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
 *  $Revision: 1.5 $  $Date: 2005-11-15 23:11:23 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;
import org.eclipse.ve.internal.cde.core.ModelChangeController.ModelChangeEvent;

import org.eclipse.ve.internal.java.core.XMLTextPage;

/**
 * XML ViewPart to show Java Visual Editor's xml.
 * 
 * @since 1.2.0
 */
public class XMLViewPart extends ContentOutline {

	public class XMLViewTextPage extends XMLTextPage implements IContentOutlinePage {

		protected Map XML_SAVE_CACHE_OPTIONS = new HashMap(3);

		protected EditDomain domain;

		protected Job refreshJob = new Job("Refresh Visual Editor XMLView") {
		
			{
				this.setSystem(true);
				this.setPriority(Job.SHORT);
			}
			
			protected IStatus run(IProgressMonitor monitor) {
				Control c = XMLViewTextPage.this.getControl();
				if (!monitor.isCanceled() && c != null && !c.isDisposed() && domain.getDiagramData() != null) {
					Resource resource = domain.getDiagramData().eResource();
					final ByteArrayOutputStream os = new ByteArrayOutputStream();
					try {
						resource.save(os, XML_SAVE_CACHE_OPTIONS);
					} catch (Exception e) {
						os.reset();
						PrintWriter w = new PrintWriter(os);
						e.printStackTrace(w);
						w.close();
					}
					if (!monitor.isCanceled()) {
						c.getDisplay().asyncExec(new Runnable() {
							public void run() {
								setText(os.toString());
							}
						
						});
					}
				}
				return Status.OK_STATUS;
			}
		
		};
		
		protected ModelChangeController.ModelChangeListener changeListener = new ModelChangeController.ModelChangeListener() {

			public void transactionEvent(ModelChangeEvent event) {
				if (event.getEvent() == ModelChangeEvent.TRANSACTION_COMPLETED)
					refresh();
			}
		};

		public XMLViewTextPage(EditDomain anEditDomain) {
			domain = anEditDomain;
			XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
			XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));
			XML_SAVE_CACHE_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8");//$NON-NLS-1$	
			((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).addModelChangeListener(changeListener);
		}

		public void dispose() {
			((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).removeModelChangeListener(changeListener);
			refreshJob.cancel();
			super.dispose();
		}

		public void refresh() {
			refreshJob.cancel();
			refreshJob.schedule(500);
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
		if (part instanceof JavaVisualEditorPart) {
			EditDomain domain = ((JavaVisualEditorPart) part).getEditDomain();
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
