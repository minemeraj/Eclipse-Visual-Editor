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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */

import java.util.*;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.java.vce.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.java.codegen.core.ICodeGenStatus;
import org.eclipse.ve.internal.java.codegen.core.JavaSourceTranslator;
import org.eclipse.ve.internal.java.codegen.editorpart.IJVEStatus;
import org.eclipse.ve.internal.java.codegen.util.*;

public class JavaSourceSynchronizer {


 public final static		int NO_OF_UPDATE_WORKERS = 2 ;
 public final static		int DEFAULT_SYNC_DELAY = 500 ;
 public       static		int L2R_DELAY_FACTOR = org.eclipse.ve.internal.java.vce.VCEPreferences.DEFAULT_L2R_FACTOR  ;  // Extra Delay if only L2R deltas
	
 private                Object fSyncPoint;
 private                BackgroundThread fThread;	
 private int            fDelay= DEFAULT_SYNC_DELAY ;
 IWorkingCopyProvider    fWorkingCopyProvider  ;
 volatile Vector        fList = new Vector () ;
 Display                 fDisplay = null ;
 IBackGroundWorkStrategy fSharedUpdatingLocalStrategy  = null,
                         fLocalUpdatingSharedStrategy = null ;
 WorkerPool              fStrategyWorkers = new WorkerPool(NO_OF_UPDATE_WORKERS) ;
 Hashtable               fSharedUpdatingLocalMonitor = new Hashtable() ,
                         fLocalUpdatingSharedMonitor = new Hashtable() ;
 
 SharedDocListener       fSharedDocListener = null ;
 
 JavaSourceTranslator    fsrcTranslator = null ;  // Hack, need to provide interface.
 ICodeGenStatus			 fStatus = null ;
 int					 fDelayFactor = L2R_DELAY_FACTOR ;

		
	
	/**
	 * Background thread for the periodic reconciling activity.
	 */
	class BackgroundThread extends Thread {
		
		private volatile boolean fCanceled= false;
		private volatile boolean fReset= false;
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
		 * Reset the background thread 
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
		
		public void goNow() {
				synchronized(this) {
					fIsDirty= true;
					fReset=false ;					
					synchronized (fSyncPoint) {
					  fgoNow=true ;
					  fSyncPoint.notifyAll();					  
				    }
				}	
					
		}
		
		/**
		 * Scan the List to see if there are elements that are not Left to Right 
		 * work items.
		 */
		boolean isL2ROnly() {
			if (fList == null || fList.isEmpty())
				return true;
			synchronized (fList) {
				Iterator itr = fList.iterator();
				while (itr.hasNext()) {
					if (!(itr.next() instanceof SynchronizerWorkItem))
						return false;
				}
			}
			return true ;
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
						  fSyncPoint.wait(fDelay);
					} catch (InterruptedException x) {}
				}
				
				if (fCanceled)
					break;
				
				fDelayFactor-- ;
					
				// Want to update the status asap.												
				synchronized (this) {			
				 if (fStatus != null) {
				 	if (fList.isEmpty() &&
				 	    (fStrategyWorkers.getAvailWorkers() == fStrategyWorkers.getNoOfWorkers())) {
				 	    	fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_OUTOFSYNC,false) ;
				 	    	fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_SYNCHING,false) ;
				 	    	fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL,false) ;
				 	    	fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_SOURCE,false) ;				 	   				 	    
				 	    }
				 	    
				 }						
				 if (!fIsDirty && !fgoNow)
					 continue;							 
				}	
				
				// isL2ROnly aquires the lock on fList... we may cause a deadlock.
				if (fDelayFactor>0 && isL2ROnly())
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
					fIsDirty= fList.size() > 0 ;
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
	class SharedDocListener implements IDocumentListener {
		
		List prev = null ;
		DocumentEvent prevEvent = null ;
		long          prevEventTime = System.currentTimeMillis() ;
				        
				private boolean isRepeatedKey(DocumentEvent event,boolean update) {
					long curTime = System.currentTimeMillis() ;
					boolean result = false ;
					
					if (prevEvent!=null)  {
		            	int delta = event.getOffset()-prevEvent.getOffset()  ;
		            	if (delta>=0 && delta <=2 && curTime-prevEventTime<450)
		            	   result = true ;
		            	   fThread.reset() ;
					}
					if (update)
					  prevEventTime = curTime ;				  
					return result ;
				}
				public void documentAboutToBeChanged(DocumentEvent event){	
					try{
						if (isRepeatedKey(event,false))  return ;
						if (fStatus!=null)
						   fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_OUTOFSYNC,true) ;
						
						prev = SynchronizerWorkItem.getWorkItemList(event,fWorkingCopyProvider.getSharedWorkingCopy(false),true,true) ;
						if (prev.size()>100)
						  JavaVEPlugin.log("JavaSourceSynchronizer$SharedDocListener.docChanged(): elements: "+prev.size(), //$NON-NLS-1$
						                 org.eclipse.jem.internal.core.MsgLogger.LOG_WARNING) ;
					}catch(Throwable e){
						JavaVEPlugin.log("Not processing documentAboutToBeChanged(DocumentEvent) due to exception.",MsgLogger.LOG_WARNING); //$NON-NLS-1$
						JavaVEPlugin.log(e,MsgLogger.LOG_WARNING);
					}
				}
				public void documentChanged(DocumentEvent event){
					try{
	                    if (isRepeatedKey(event,true)) {
							prevEvent = event ;
							return ;
						}
						prevEvent = event ;
						
						List elements = SynchronizerWorkItem.refreshWorkItemList(prev,event,fWorkingCopyProvider.getSharedWorkingCopy(),true,false) ;
						if (elements.size()>100)
						  JavaVEPlugin.log("JavaSourceSynchronizer$SharedDocListener.docChanged(): elements: "+elements.size(), //$NON-NLS-1$
						                 org.eclipse.jem.internal.core.MsgLogger.LOG_WARNING) ;
						prev = null ;
						// There is a delta change,  if we are already in the process of reloading from scratch,
						// put on the list that we need a "newer" reload from sctrach action, which will force the current
						// running reload from stractch to stop
						if (fStatus.isStatusSet(IJVEStatus.JVE_CODEGEN_STATUS_RELOAD_PENDING)|| fStatus.isStatusSet(IJVEStatus.JVE_CODEGEN_STATUS_RELOAD_IN_PROGRESS)) {
							fStatus.setReloadPending(true) ;
							appendReloadRequest(elements);
						}else{
							updateLocalFromShared(elements);
						}
					}catch(Throwable e){
						JavaVEPlugin.log("Not processing documentChanged(DocumentEvent) due to exception.",MsgLogger.LOG_WARNING); //$NON-NLS-1$
						JavaVEPlugin.log(e,MsgLogger.LOG_WARNING);
					}
				}

			}
			
	/**
	 * Adds an WorkItem with the RELOAD_HANDLE in the Local to Shared updater.
	 * 
	 * @param additionalRequests  A set of additional requests which should be added prior to the RELOAD_HANDLE. 
	 * Could be set to null if only RELOAD_HANDLE required. 
	 */
	public void appendReloadRequest(List additionalRequests){
		SynchronizerWorkItem wi = new SynchronizerWorkItem(SynchronizerWorkItem.RELOAD_HANDLE,true) ;
		List elements = new ArrayList() ;
		if(additionalRequests!=null && additionalRequests.size()>0)
			elements.addAll(additionalRequests);
		elements.add(wi);
		updateLocalFromShared(elements);
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
		install () ;
	}
	
	public void setSharedUpdatingLocalStrategy(IBackGroundWorkStrategy strategy) {
		fSharedUpdatingLocalStrategy = strategy ;		
	}
	
	public void setLocalUpdatingSharedStrategy(IBackGroundWorkStrategy strategy) {
		fLocalUpdatingSharedStrategy = strategy ;		
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
	
	private void driveStrategy(List workElements, Hashtable monitors, IBackGroundWorkStrategy strategy) throws InterruptedException {
		if (workElements.isEmpty()) return ;
		
		if (fStatus!=null) {
		   fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_SYNCHING,true) ;
		   if (fLocalUpdatingSharedStrategy == strategy)
		      fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_SOURCE,true) ;
		   else if (fSharedUpdatingLocalStrategy == strategy)
		      fStatus.setStatus(IJVEStatus.JVE_CODEGEN_STATUS_UPDATING_JVE_MODEL,true) ;		  
		}
		
		if (strategy == null) {
			JavaVEPlugin.log ("JavaSourceSynchronizer.driveStrategy() - no strategy", MsgLogger.LOG_WARNING) ; //$NON-NLS-1$
			return ;
		}
		
		Iterator itr = workElements.iterator() ;
		CancelMonitor newMon = new CancelMonitor() ;
		while (itr.hasNext()) {
			SynchronizerWorkItem wi = (SynchronizerWorkItem) itr.next() ;
		    CancelMonitor m = (CancelMonitor) monitors.get(wi.getChangedElementHandle()) ;
		    if (m != null) {
		      // TODO fixMe() ;
		      m.setCancel(false) ;		     // do not want to get collision error
		    }
		    monitors.put(wi.getChangedElementHandle(),newMon) ;
		}
			
		StrategyWorker w = fStrategyWorkers.grabWorker() ;		  	
		w.assignStrategy(strategy,workElements.toArray(new SynchronizerWorkItem[workElements.size()]),
		  	           fSharedDocListener, getDisplay(),
		  	           newMon) ;		  								
	}
	
	/**	 
	 */
	private void process() {
		
		// TODO Collision Logic needed
		while (fList.size()>0) {
		  final ArrayList LocalToSharedDelta = new ArrayList () ;
		  final ArrayList SharedToLocalDelta = new ArrayList () ;
		  final ArrayList NotificationList = new ArrayList() ;		  
		  
		  synchronized (fList) {
		  	if (fList.size()>0) 
		  	 for (int i=fList.size()-1; i>=0; i--) {
		  	   // TODO Need a generic interface for work items
		  	   if( fList.get(i) instanceof SynchronizerWorkItem){
		  	   	  SynchronizerWorkItem we = (SynchronizerWorkItem) fList.get(i);
		  	   	  if(we.isSharedToLocalUpdate())
		  	   		SharedToLocalDelta.add(fList.remove(i));
		  	   	  else
		  	   		LocalToSharedDelta.add(fList.remove(i));
		  	   }
		  	   else if(fList.get(i) instanceof NotifierElement) {
		  	       NotificationList.add(fList.remove(i)) ;		  	       
		  	   }
		  	 }		      
		  }	
		  
		  try {			   
		   // right -> left
		   driveStrategy(LocalToSharedDelta,fLocalUpdatingSharedMonitor,fLocalUpdatingSharedStrategy) ;
		   // left -> right
		   driveStrategy(SharedToLocalDelta,fSharedUpdatingLocalMonitor,fSharedUpdatingLocalStrategy) ;
		   if (fStrategyWorkers != null && !NotificationList.isEmpty())
		      fStrategyWorkers.waitForAllWorkersToComplete() ;
		   
		   for (int i = NotificationList.size()-1; -1 < i; i--) {
		    try {
		     NotifierElement elm = (NotifierElement) NotificationList.get(i) ;
             elm.getListener().markerProcessed(elm.getMarker()) ;
		    }
		    catch (Throwable t) {
		        org.eclipse.ve.internal.java.core.JavaVEPlugin.log(t) ;
		    }
            
           }
		   
		  }
		  catch (InterruptedException e) {}
		 
		}
		    
	}
	
	/**
	 * A listener can set a marker on the work queue, and get notify when the
	 * synchronizer processed everything prior to the marker.
	 */
	public void notifyOnMarker(ISynchronizerListener listener, String marker, boolean immediate) {
	    if (listener == null || fThread == null) return ;
	    
	    NotifierElement elm = new NotifierElement(listener,marker,immediate) ;    
	    synchronized (fList) {
	        fList.add(elm) ;	        
	    }
	    if (immediate)
	      fThread.goNow() ;
	    else
	      fThread.reset() ;	    
	}
	
	/**
	 *  
	 */
	 public void updateSharedFromLocal(String elementHandle) {
	 	synchronized (fList) {
	 		SynchronizerWorkItem we = new SynchronizerWorkItem(elementHandle, false);
	 		Enumeration e = fList.elements() ;	 		
	 		SynchronizerWorkItem oldElement = null ;                		 		
	 		while (e.hasMoreElements()) {
                   Object o = e.nextElement() ;
                   if (!(o instanceof SynchronizerWorkItem)) continue ;
                	SynchronizerWorkItem w = (SynchronizerWorkItem) o ;                	
                	if (w.isSharedToLocalUpdate() == we.isSharedToLocalUpdate() &&
                	    w.getChangedElementHandle().equals(we.getChangedElementHandle())) {
                	    oldElement = w ;
                	    break ;
                	}
            }
            if (oldElement != null) {
//JavaVEPlugin.log("Sync.updateSharedFromLocal: Replacing "+oldElement) ;
                fList.remove(oldElement) ;
            }	 		
	 		fList.add(we);
//JavaVEPlugin.log("Sync.updateSharedFromLocal: "+elementHandle+" - "+fList.size()) ;		 				 			 		
	 	}
	 	fThread.reset() ;	 	
	 }
	
	 public void updateLocalFromShared(SynchronizerWorkItem we){
	 	if (we == null) {
	 		return ;
	 	}
	 	synchronized (fList) {	 		
                Enumeration e = fList.elements() ;
                SynchronizerWorkItem oldElement = null ;
                while (e.hasMoreElements()) {
                   Object o = e.nextElement() ;
                   if (!(o instanceof SynchronizerWorkItem)) continue ;
                   SynchronizerWorkItem w = (SynchronizerWorkItem) o ;      
                	
                	if (w.isEquivalent(we)) {
                	    oldElement = w ;
                	    break ;
                	}
                }
                if (oldElement != null) {
                   // This is a continuing change, remember the original content
//JavaVEPlugin.log("Sync.updateLocalFromShared: Replacing "+oldElement) ;
                   fList.remove(oldElement) ;
                   if (oldElement.getChangedElementHandle().equals(SynchronizerWorkItem.RELOAD_HANDLE))
                      fStatus.setReloadPending(false) ;  // decrement the reload count counter.
                   else
                      we.setChangeElementPrevContent(oldElement.getChangeElementPrevContent()) ;
                }
	 			fList.add(we);
                fsrcTranslator.fireSnippetProcessing(true) ;
//JavaVEPlugin.log("Sync.updateLocalFromShared: "+ we+" - "+fList.size()) ;	 		 		
	 	}
	 	fThread.reset() ;	 	
	 }
	 
	 public void updateLocalFromShared(List workElements){
	 	if (workElements == null || workElements.isEmpty()) return ;
	 	synchronized (fList) {
	 		Iterator itr = workElements.iterator() ;
	 		while (itr.hasNext()) {
	 			updateLocalFromShared((SynchronizerWorkItem)itr.next()) ;
	 		}
	 	}
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
        synchronized (fList) {
            Iterator itr = fList.iterator() ;
            while (itr.hasNext()) {
                Object element = itr.next() ;
                if (element instanceof SynchronizerWorkItem) {
                    result = true ;
                    break ;
                }                
            }
        }
        return result ;
    }
    public synchronized void disconnect() {
        if (fSharedDocListener != null) {
            fWorkingCopyProvider.getSharedDocument().removeDocumentListener(fSharedDocListener) ;
            fSharedDocListener = null ;
        }
        synchronized (fList) {
			for (int i=0; i<fList.size(); i++) {
			  if(fList.get(i) instanceof NotifierElement) {
			  	NotifierElement elm = (NotifierElement) fList.get(i) ;
			  	try {
					elm.getListener().markerProcessed(elm.getMarker()) ;
			  	}
			  	catch (Throwable e) {}
			  }
			}		      
        	fList.clear() ;   
        }
    }	public synchronized void connect() {
        if (fSharedDocListener == null) {
          fSharedDocListener = new SharedDocListener() ;
          fWorkingCopyProvider.getSharedDocument().addDocumentListener(fSharedDocListener) ;        
        }
    }
/**
 * Sets the fStatus.
 * @param fStatus The fStatus to set
 */
public void setStatus(ICodeGenStatus fStatus) {
	this.fStatus = fStatus;
}

}


