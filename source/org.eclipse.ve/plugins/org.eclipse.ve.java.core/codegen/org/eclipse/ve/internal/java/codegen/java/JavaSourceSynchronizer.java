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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: JavaSourceSynchronizer.java,v $
 *  $Revision: 1.20 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.java.codegen.core.*;
import org.eclipse.ve.internal.java.codegen.util.IWorkingCopyProvider;
import org.eclipse.ve.internal.java.codegen.util.ReverseParserJob;

public class JavaSourceSynchronizer implements ISynchronizable{

 public final static		int DEFAULT_SYNC_DELAY = org.eclipse.ve.internal.java.vce.VCEPreferences.DEFAULT_SYNC_DELAY ;
 public final static		String RELOAD_HANDLE = "Reload Request"; //$NON-NLS-1$
 
 
 private int				snippetDelay= DEFAULT_SYNC_DELAY ;
 IWorkingCopyProvider		workingCopyProvider  ;
 Display					curDisplay = null ;
 EditorUpdateState			updateStatus = null;
 Object						bdmLock = null;
 
  //documentEventList 
  //Contains from 0..n document events in increasing time
  //Document events are added to the end (n)  
 private List 				documentEventList = null;

 DocListener				docListener = null ;
 JavaSourceTranslator		srcTranslator = null ;  // Hack, need to provide interface.
 boolean				    stalled = false;        // On a rename, we hold off buttom up

		
	
	/**
	 *  The Listener will convert document text changes to JavaElement changes.
	 *  To do so, it need to be able to handle with a deletion of JavaElement (in which case
	 *  It need to get the pre Delta content), or with new JavaElements in which case the content
	 *  is based on the document after the deltal is apllied.
	 *
	 *  
	 */
	class DocListener implements IDocumentListener {
		/**
		 * Document is about to be changed - set the GUI to be in read only.
		 *  
		 * @see org.eclipse.jface.text.IDocumentListener#documentAboutToBeChanged(org.eclipse.jface.text.DocumentEvent)
		 */
		
		public void documentAboutToBeChanged(DocumentEvent event) {
			// We dont need to busy wait becuase updates in the code should never happen when 
			// GUI to source changes are being performed. We will be here only when no Top-Down
			// changes are being performed.
			updateStatus.setBottomUpProcessing(true);  
		}

		/**
		 *  
		 * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
		 */
		public void documentChanged(final DocumentEvent event) {
			driveNewEvent(event);
		}			
	}
	
	/**
	 * Creates a new reconciler with the following configuration: it is
	 * an incremental reconciler which kicks in every 500 ms. There are no
	 * predefined reconciling strategies.
	 */ 
	public JavaSourceSynchronizer(IWorkingCopyProvider wcp, JavaSourceTranslator st) {
		super();
		    
		workingCopyProvider = wcp ;
        srcTranslator = st ;        		
		curDisplay = getDisplay();	
		updateStatus = new EditorUpdateState();
		documentEventList = new ArrayList();
		connect() ;		
	}
	
	
    protected void driveNewEvent(Object event) { 
    	// Synchronize with potential Jobs that are driving snippets
		synchronized (updateStatus) {
			if (event!=null)
			    documentEventList.add(event);
			if (updateStatus.isCollectingDeltas()) {
				updateStatus.setBottomUpProcessing(false); // decrement the counter and go away
				// Reset current outstanding snippet job's schedcule time
				Job[] jobs = ReverseParserJob.getReverseParserJobs(workingCopyProvider.getFile());
				for (int i = 0; i < jobs.length; i++) {
					if (jobs[i].getState() != Job.RUNNING && jobs[i] instanceof SnippetParseJob) {
						jobs[i].sleep();
						jobs[i].wakeUp(getDelay());
					}
				}
				return;
			}
			else {
				if (!stalled && !documentEventList.isEmpty()) {
					// Go for it, be the one to drive the deltas
				    updateStatus.setCollectingDeltas(true); 
				}
				else {
					updateStatus.setBottomUpProcessing(false);
					// do nothing
					return ;
				}
			}
		}
		srcTranslator.fireSnippetProcessing(true);
		SnippetParseJob job = new SnippetParseJob(
		        workingCopyProvider.getFile(), 
		        srcTranslator.createSharedToLocalUpdater(), 
		        getDisplay(),
				workingCopyProvider.getWorkingCopy(false), 
				updateStatus, 
				documentEventList);
		job.setEditDomain(srcTranslator.getEditDomain());
		job.schedule(getDelay());
	}
			
	/**
	 * Adds an WorkItem with the RELOAD_HANDLE in the Local to Shared updater.
	 * 
	 * @param additionalRequests  A set of additional requests which should be added prior to the RELOAD_HANDLE. 
	 * Could be set to null if only RELOAD_HANDLE required. 
	 */
	public void appendReloadRequest(){
		driveNewEvent(RELOAD_HANDLE);		
	}		
	
	private Display getDisplay() {
		if (curDisplay != null) return curDisplay ;
		
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows() ;
		if (windows != null && windows.length>0) {			
			for (int i=0; i<windows.length; i++) {
				if (windows[i].getShell() != null && windows[i].getShell().getDisplay() != null) {
					 curDisplay = windows[i].getShell().getDisplay() ;
					 break ;
				}
			}
		}
		return curDisplay ;
	}
	

	
	/**
	 * Tells the reconciler how long it should collect text changes before
	 * it activates the appropriate reconciling strategies.
	 *
	 * @param delay the duration in milli seconds of a change collection period.
	 */
	public void setDelay(int delay) {
	    if (delay >= DEFAULT_SYNC_DELAY) 
		   snippetDelay = delay;
	    else
	    	throw new IllegalArgumentException ("invalid delay value"); //$NON-NLS-1$
	}
	public int getDelay() {
		return snippetDelay ;
	}
	
    public boolean isWorkQueued() {        
        boolean result = false ;
		if(documentEventList.size()>0)
			result=true;
        return result ;
    }
    
    /**
     * The Assumption here is that this will be called when the UI (top down) is driving
     * a change, and does not want a feedback from the CU document.
     * If there were any outstanding background work, than the UI would be in read only and could
     * not call here.
     * 
     * @since 1.0.0
     */
    public void suspendDocListener() {
    	if (docListener != null) {    		    
    		    workingCopyProvider.getDocument().removeDocumentListener(docListener) ;
        }
    }
    
    public void resumeDocListener() {
    	if (docListener != null) {
    		workingCopyProvider.getDocument().addDocumentListener(docListener) ;
    	}
    }
    
    public synchronized void disconnect() {    	
        if (docListener != null) {
            workingCopyProvider.getDocument().removeDocumentListener(docListener) ;
            docListener = null ;
            bdmLock=null;
        }        
        clearOutstandingWork();
    }	
    
    public synchronized void connect() {
        if (docListener == null) {
          docListener = new DocListener() ;
          workingCopyProvider.getDocument().addDocumentListener(docListener) ;
          bdmLock = new Object();
        }
    }
    
    /**
     * This method will clear outstanding Events, and cancel
     * a worker thread
     */
    protected void clearOutstandingWork() {
    	synchronized (updateStatus) {
    	   documentEventList.clear();
    	   updateStatus.setCollectingDeltas(false);    	   
    	}
 	   Job[] jobs = ReverseParserJob.getReverseParserJobs(workingCopyProvider.getFile());
	   for (int i = 0; i < jobs.length; i++) 
		if (jobs[i] instanceof SnippetParseJob) {
			jobs[i].cancel();
		}
	  
    }

public IEditorUpdateState getUpdateStatus() {
	return updateStatus;
}

/**
 * Continue collecting document change deltas, but do not spawn any new snippet jobs
 * e.g., a rename transaction is about to occur... let it finish
 */
public void stallProcessing() {	
	Job[] jobs;
	synchronized (this) {
	   stalled=true;
	   jobs = ReverseParserJob.getReverseParserJobs(workingCopyProvider.getFile());
	}
	for (int i = 0; i < jobs.length; i++) {
		if (jobs[i] instanceof SnippetParseJob) {
			try {
				jobs[i].join();
			} catch (InterruptedException e) {}
		}
	}
}

/**
 * Processing will be started immediately
 * 
 */
public synchronized void resumeProcessing() {
	stalled=false;
	updateStatus.setBottomUpProcessing(true);  
	driveNewEvent(null);
}


/* (non-Javadoc)
 * @see org.eclipse.jface.text.ISynchronizable#setLockObject(java.lang.Object)
 */
public void setLockObject(Object lockObject) {
	bdmLock = lockObject;	
}


/* (non-Javadoc)
 * @see org.eclipse.jface.text.ISynchronizable#getLockObject()
 */
public Object getLockObject() {
	return bdmLock;
}

public String toString() {
	String listener = docListener==null?"null":"listening"; //$NON-NLS-1$ //$NON-NLS-2$
	return "Stalled="+stalled+", updateStatus="+updateStatus+", delay="+getDelay()+", docListener="+listener; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
}

}


