/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.util;
/*
 *  $RCSfile: StrategyWorker.java,v $
 *  $Revision: 1.5 $  $Date: 2004-12-16 18:36:14 $ 
 */

import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.java.codegen.core.ICodegenLockManager;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;


/**
 * The Worker class is responsible for running and controlling connections. <br>
 * A worker runs in its own thread and waits for connections that need to be performed. <br>
 * A WorkerPool object owning a reference to a Worker object can assign a connection to it,
 * and also shut it down in an orderly manner.
 */
public class StrategyWorker implements Runnable{
	

  /**
   * the worker pool owning this worker
   */
  private WorkerPool              fPool;
  
  private IBackGroundWorkStrategy fStrategy = null ;
  private ICancelMonitor          fMonitor = null ;
  private Display                 fdisplay = null ;
  private ICompilationUnit       fWorkingCopy = null ;
  private ICodegenLockManager fLockManager = null;
  private List fAllEvents = null;
  
  /**
   * the thread running this object
   */
  private Thread           workerThread = null;

  /**
   * loop continuation flag
   */
  private volatile boolean cont = true;
  //
  // For graceful cancelling
  //
  boolean fCancel = false;

  /**
   * Creates a worker owned to a specified worker pool.
   */
  public StrategyWorker (WorkerPool pool) {
	fPool = pool;
  }  
  
  /**
   * Assigns a connection to this worker and signals it to perform it.
   * @param connection the connection to be performed.
   */
  public synchronized void assignStrategy(IBackGroundWorkStrategy strat, ICodegenLockManager lockManager, List allDocEvents, ICompilationUnit workingCopy, Display disp, ICancelMonitor monitor) {
  	fStrategy = strat ;
  	fMonitor = monitor ;
  	fAllEvents = allDocEvents ;
  	fLockManager = lockManager;
  	fdisplay = disp ;
  	fWorkingCopy = workingCopy;
  	notify();
  }
  
  void cancel() {
  	fCancel = true;
  }  
  /**
   * Shuts down this worker. <br>
   * If this worker is currently running a conneection, it's notified that it should finish as soon as possible.
   */
  public void finish() {
	cont = false;	
	if (workerThread != null) workerThread.interrupt();
  }  
  boolean isCancel() {return fCancel;}  
  /**
   * Runs the worker thread. <br>
   * Initially, the worker is in an idle mode, and waits until a connection is assigned to it. <br>
   * When a connection has been assigned to it, the worker invokes its <code>perform</code> method. <br>
   * When the connection is done, the worker returns itself to the worker pool and goes back to wait
   * for a new connection.
   */
public void run() {

	workerThread = Thread.currentThread();
	mainloop: while (cont) {

		try {

			waitForStrategy();
			/* save a reference to this thread */

			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log("StrategyWorker running: " + fStrategy.getClass().getName(), Level.FINE); //$NON-NLS-1$
			if (fMonitor == null || !fMonitor.isCanceled()) {
				fStrategy.run(fdisplay, fWorkingCopy, fLockManager, fAllEvents, fMonitor);
			}

		} catch (InterruptedException e) {
		} catch (Throwable t) {
			try {
				JavaVEPlugin.log(t, Level.WARNING);
			} catch (Throwable tt) {
			}
		} finally {
			fStrategy = null;
			fWorkingCopy = null;
			fAllEvents = null;
			fLockManager = null;
			if (fMonitor != null)
				fMonitor.setCompleted();
			fMonitor = null;
			fPool.returnWorker(this);
		}
	}
}  
  
  private synchronized void waitForStrategy() throws InterruptedException {
	while(fStrategy == null)
	  wait(); 
  }  
                        	
	
}
