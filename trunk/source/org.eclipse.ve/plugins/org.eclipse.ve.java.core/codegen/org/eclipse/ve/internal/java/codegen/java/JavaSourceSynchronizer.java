package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: JavaSourceSynchronizer.java,v $
 *  $Revision: 1.10 $  $Date: 2004-05-14 19:55:38 $ 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.java.codegen.core.*;
import org.eclipse.ve.internal.java.codegen.util.*;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;

public class JavaSourceSynchronizer {

 public final static		int NO_OF_UPDATE_WORKERS = 2 ;
 public final static		int DEFAULT_SYNC_DELAY = org.eclipse.ve.internal.java.vce.VCEPreferences.DEFAULT_SYNC_DELAY ;
 public       static		int L2R_DELAY_FACTOR = org.eclipse.ve.internal.java.vce.VCEPreferences.DEFAULT_L2R_FACTOR  ;  // Extra Delay if only L2R deltas
 public final static 	String RELOAD_HANDLE = "Reload Request";
 
 private                Object fSyncPoint;
 private                BackgroundThread fThread;	
 private int            fDelay= DEFAULT_SYNC_DELAY ;
 IWorkingCopyProvider    fWorkingCopyProvider  ;
 Display                 fDisplay = null ;
 WorkerPool              fStrategyWorkers = new WorkerPool(NO_OF_UPDATE_WORKERS) ;
 CodegenLockManager     lockManager = null;
 /**
  * documentEventList 
  * Contains from 0..n document events in increasing time
  * Document events are added to the end (n) 
  */
 private List documentEventList = null;
 List notifierList = null;
 
 DocListener       		fDocListener = null ;
 
 JavaSourceTranslator    fsrcTranslator = null ;  // Hack, need to provide interface.
 int					 fDelayFactor = L2R_DELAY_FACTOR ;

		
	
	/**
	 * Background thread for the periodic reconciling activity.
	 */
	class BackgroundThread extends Thread {
		
		private volatile boolean fCanceled= false;
		private volatile boolean fReset= false;
		private volatile boolean fStall=false;  // Suspend until we are told to go now
		private volatile boolean fIsDirty= false;
		private volatile boolean fIsActive= false;
		private volatile boolean fgoNow = false ; // Hack for now
		
		/**
		 * Creates a new background thread. The thread 
		 * runs with minimal priority.
		 *
		 * @param name the thread's name
		 */
		public BackgroundThread(String name) {
			super(name);
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
		}
		
		/**
		 * Returns whether a reconciling strategy is active right now.
		 *
		 * @return <code>true</code> if a activity is active
		 */
		public boolean isActive() {
			return fIsActive;
		}
		
		/**
		 * Cancels the background thread.
		 */
		public void cancel() {
			fCanceled= true;
			synchronized (fSyncPoint) {
				fSyncPoint.notifyAll();
			}
		}
		
		/**
		 * Force the synchronizer to go through the wait cycle again.
		 * Typically called when the document has changed. 
		 */
		public void reset() {			
			if (fDelay > 0) {				
				synchronized (this) {
					fIsDirty= true;
					if (!fgoNow) {					 
					  fReset= true;
					}
				}				
			} else {				
				goNow() ;
			}
		}
		/**
		 * Wake up the sync. and make him process asap
		 */
		public void goNow() {
				synchronized(this) {
					fIsDirty= true;
					fReset=false ;					
					synchronized (fSyncPoint) {
					  fgoNow=true ;
					  fStall=false;
					  fSyncPoint.notifyAll();					  
				    }
				}	
					
		}
		
		public void Stall() {
			synchronized(this) {
				fgoNow=false;
				fStall=true;				
			}
		}
		
		/**
		 * The periodic activity. Wait until there is something in the
		 * queue managing the changes applied to the text viewer. Remove the
		 * first change from the queue and process it.
		 */
		public void run() {
			while (!fCanceled) {
				synchronized (fSyncPoint) {
					try {
						if (!fgoNow)
						  if (!fStall)
						    fSyncPoint.wait(fDelay);
						  else {
						  	fStall=false;
						  	fSyncPoint.wait();
						  }
					} catch (InterruptedException x) {}
				}
				
				if (fCanceled)
					break;
				
				if (fgoNow)  
					fDelayFactor=0; 
				else
					fDelayFactor--;
															
				synchronized (this) {							
				 if (!fIsDirty && !fgoNow)
					 continue;							 
				}	
				
				// isL2ROnly aquires the lock on fList... we may cause a deadlock.
				if (fDelayFactor>0)// && isL2ROnly())
				    continue ;
				    
				fDelayFactor = L2R_DELAY_FACTOR ;
				
				
					
				if (fReset) {
					synchronized (this) {
						fReset= false;
					}
					if (!fgoNow) 
					  continue;				
				}									
				fIsActive= true;
		
				process();
				synchronized (this) {
					// Work Element may have been added between the process and now.
					// 
					fIsDirty = notifierList.size() > 0 ;
					fgoNow = fgoNow && fIsDirty;
				}
				
				fIsActive= false;
			}
		}
	};
	
	class NotifierElement {
	    ISynchronizerListener fListener ;
	    String                fMarker ;
	    boolean              fImmediate ;
	    public NotifierElement (ISynchronizerListener sl, String marker, boolean immediate) {
	        fListener = sl ;
	        fMarker = marker ;
	        fImmediate = immediate ;
	    }
	    public ISynchronizerListener getListener() {
	        return fListener ;
	    }
	    public String getMarker() {
	        return fMarker ;
	    }
	    public boolean isImmediate() {
	    	return fImmediate ;
	    }
	}
	
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
			lockManager.setGUIReadonly(true);  
		}

		/**
		 *  
		 * @see org.eclipse.jface.text.IDocumentListener#documentChanged(org.eclipse.jface.text.DocumentEvent)
		 */
		public void documentChanged(final DocumentEvent event) {
			documentEventList.add(event);
			fsrcTranslator.fireSnippetProcessing(true);
			fThread.reset();
		}
	}
			
	/**
	 * Adds an WorkItem with the RELOAD_HANDLE in the Local to Shared updater.
	 * 
	 * @param additionalRequests  A set of additional requests which should be added prior to the RELOAD_HANDLE. 
	 * Could be set to null if only RELOAD_HANDLE required. 
	 */
	public void appendReloadRequest(){
		documentEventList.add(RELOAD_HANDLE);
		fThread.reset();
	}
	
	class  CancelMonitor implements ICancelMonitor {
		    boolean fCancel = false ;
		    boolean fCompleted = false ;
		
		    public boolean isCanceled() { return fCancel; }
		    public void setCancel(boolean flag) {
		    	fCancel = flag ;
		    }
		    public synchronized void setCompleted() {
		    	fCompleted = true ;
		    	notifyAll() ;
		    }
		    public synchronized boolean isCompleted(boolean wantToWait) {
		    	if (wantToWait && !fCompleted)
		    	try {
		    	   wait() ;
		    	}
		    	catch (Exception e) {}
		    	return fCompleted ;
		    }
	}
		

	
	/**
	 * Creates a new reconciler with the following configuration: it is
	 * an incremental reconciler which kicks in every 500 ms. There are no
	 * predefined reconciling strategies.
	 */ 
	public JavaSourceSynchronizer(IWorkingCopyProvider wcp, JavaSourceTranslator st) {
		super();
		Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
		int newFactor = -1;
		// The following is a last moment hack ...
		try {
			newFactor = store.getInt(VCEPreferences.SOURCE_DELAY_FACTOR);
		 if (newFactor >= 0)
		    L2R_DELAY_FACTOR = newFactor ;
		}
		catch (Exception e) {
			L2R_DELAY_FACTOR = VCEPreferences.DEFAULT_L2R_FACTOR ;
		}
		    
		fWorkingCopyProvider = wcp ;
        fsrcTranslator = st ;
        // May or may not have one		
		fDisplay = Display.getCurrent() ;
		
		lockManager = new CodegenLockManager();
		documentEventList = new ArrayList();
		notifierList = new ArrayList();
		
		install () ;
	}
	
	/**
	 * Start the background thread, and add this synchroniser as 
	 * the listener to the Shared document provider.
	 */
	private synchronized void install() {
		if (fThread != null) return ;
				
		fSyncPoint= new Object();
		fThread= new BackgroundThread("CodeGen::"+getClass().getName()+"["+fWorkingCopyProvider.getFile().getName()+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		fThread.start();
		connect() ;
	}
	
	
	private Display getDisplay() {
		if (fDisplay != null) return fDisplay ;
		
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows() ;
		if (windows != null && windows.length>0) {			
			for (int i=0; i<windows.length; i++) {
				if (windows[i].getShell() != null && windows[i].getShell().getDisplay() != null) {
					 fDisplay = windows[i].getShell().getDisplay() ;
					 break ;
				}
			}
		}
		return fDisplay ;
	}
	
	private void driveStrategy(List allDocEvents, IBackGroundWorkStrategy strategy) throws InterruptedException {
		if (allDocEvents.isEmpty()) return ;
		
		fsrcTranslator.fireStatusChanged("Synchronizing") ;  
		
		if (strategy == null) {
			JavaVEPlugin.log ("JavaSourceSynchronizer.driveStrategy() - no strategy", Level.WARNING) ; //$NON-NLS-1$
			return ;
		}
		
		CancelMonitor newMon = new CancelMonitor() ;
		StrategyWorker w = fStrategyWorkers.grabWorker() ;		  	
		w.assignStrategy(strategy,lockManager, allDocEvents, 
		  	           fWorkingCopyProvider.getWorkingCopy(true), getDisplay(), newMon) ;
	}
	
	/**	 
	 */
	private void process() {
		
		if(documentEventList.size()>0 && !getLockMgr().isGUIUpdating()){
			try {
				// Drive source to GUI update stratergy
				if(!lockManager.isThreadScheduled()){
					lockManager.setThreadScheduled(true);
					driveStrategy( documentEventList, fsrcTranslator.createSharedToLocalUpdater()) ;
				}
			}catch(InterruptedException ie){}
		}

		// TODO Collision Logic needed
		while (notifierList.size() > 0) {
			final ArrayList NotificationList = new ArrayList();
			synchronized (notifierList) {
				if (notifierList.size() > 0)
					for (int i = notifierList.size() - 1; i >= 0; i--) {
						NotificationList.add(notifierList.remove(i));
					}
			}

			try {
				if (fStrategyWorkers != null && !NotificationList.isEmpty())
					fStrategyWorkers.waitForAllWorkersToComplete();
				for (int i = NotificationList.size() - 1; i>-1; i--) {
					try {
						NotifierElement elm = (NotifierElement) NotificationList.get(i);
						elm.getListener().markerProcessed(elm.getMarker());
					} catch (Throwable t) {
						org.eclipse.ve.internal.java.core.JavaVEPlugin.log(t);
					}
				}
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * A listener can set a marker on the work queue, and get notify when the
	 * synchronizer processed everything prior to the marker.
	 */
	public void notifyOnMarker(ISynchronizerListener listener, String marker, boolean immediate) {
	    if (listener == null || fThread == null) return ;
	    
	    NotifierElement elm = new NotifierElement(listener,marker,immediate) ;    
	    synchronized (notifierList) {
	        notifierList.add(elm) ;	        
	    }
	    if (immediate)
	      fThread.goNow() ;
	    else
	      fThread.reset() ;	    
	}
	
	/**
	 * Tells the reconciler how long it should collect text changes before
	 * it activates the appropriate reconciling strategies.
	 *
	 * @param delay the duration in milli seconds of a change collection period.
	 */
	public void setDelay(int delay) {
	    if (delay > DEFAULT_SYNC_DELAY) 
		   fDelay= delay;
	}
	public int getDelay() {
		return fDelay ;
	}
	
	
	/**
	 *
	 */
	public synchronized void uninstall() {
		disconnect() ;
		if (fThread != null) {
			fThread.cancel();
			fThread= null;
		}
        if (fStrategyWorkers != null) 
           fStrategyWorkers.finish() ;
        fStrategyWorkers = null ;
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
    	if (fDocListener != null) {    		    
    		    fWorkingCopyProvider.getDocument().removeDocumentListener(fDocListener) ;
        }
    }
    
    public void resumeDocListener() {
    	if (fDocListener != null) {
    		fWorkingCopyProvider.getDocument().addDocumentListener(fDocListener) ;
    	}
    }
    
    public synchronized void disconnect() {    	
        if (fDocListener != null) {
            fWorkingCopyProvider.getDocument().removeDocumentListener(fDocListener) ;
            fDocListener = null ;
        }        
        synchronized (notifierList) {
        	for (int i=0; i<notifierList.size(); i++) {
        		NotifierElement elm = (NotifierElement) notifierList.get(i) ;
        		if( elm != null )
        			try {
        			elm.getListener().markerProcessed(elm.getMarker()) ;
        		}
        		catch (Throwable e) {}
        	}		      
        	notifierList.clear() ;   
        }
        clearOutstandingWork();
    }	
    
    public synchronized void connect() {
        if (fDocListener == null) {
          fDocListener = new DocListener() ;
          fWorkingCopyProvider.getDocument().addDocumentListener(fDocListener) ;        
        }
    }
    
    /**
     * This method will clear outstanding Events, and cancel
     * a worker thread
     */
    protected void clearOutstandingWork() {
    	documentEventList.clear();
    	lockManager.resetGUIReadOnly();
    }

public ICodegenLockManager getLockMgr() {
	return lockManager;
}

/**
 * The next cycle around, the thread will wait, untill a resume is called
 */
public void stallProcessing() {
	fThread.Stall();
}

/**
 * Processing will be started immediately
 * 
 */
public void resumeProcessing() {
	fThread.goNow();
}

}


