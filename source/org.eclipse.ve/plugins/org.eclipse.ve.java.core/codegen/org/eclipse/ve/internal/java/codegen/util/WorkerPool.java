package org.eclipse.ve.internal.java.codegen.util;
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
 *  $RCSfile: WorkerPool.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.Stack;


/**
 * The WorkerPool class maintains a predefined number of worker objects, each running in a different thread. <br>
 * the pool consists of an array which keeps references to all workers in the pool and a stack from which avaiable
 * workers are taken and to which idle workers are returned.
 */

public class WorkerPool {

 


  /**
   * array of references to this pool's worker objects 
   */
  StrategyWorker[] workers;

  /**
   * a stack from which wokers are taken and returned
   */
  Stack    stack;

  /**
   * Creates a worker pool. <br>
   * The workers are created and run within separate threads. <br>
   * After creation, all workers are waiting for work.
   * @param numberOfWorkers number of workers in the pool
   */
  public WorkerPool (int numberOfWorkers) {
	workers = new StrategyWorker[numberOfWorkers];
	stack = new Stack();
	for (int i=0;i<numberOfWorkers;i++) {
	  StrategyWorker worker = new StrategyWorker(this);
	  workers[i] = worker;
	  stack.push(worker);
	  Thread t = new Thread(worker,"CodeGen::"+worker.getClass().getName()) ; //$NON-NLS-1$
	  t.setDaemon(true) ;
	  t.setPriority(Thread.MIN_PRIORITY);
	  t.start() ;
	}
  }  
  /**
   * Tells the worker pool to finish. <br>
   * The <code>Worker finish</code> method is invoked on every worker in the pool (idle or busy).
   */
  public void finish() {
	for (int i=0;i<workers.length;i++)
	  workers[i].finish();
  }  
  /**
   * Retrieves an idle worker waiting for work. <br>
   * This method blocks until a idle worker is available.
   * @return the idle worker object
   * @exception InterruptedException if another thread interrupted this one during wait.
   */
  public synchronized StrategyWorker grabWorker() throws InterruptedException {
	while (stack.empty())
	  wait();
	return (StrategyWorker)stack.pop();
  }  
  /**
   * Returns an idle worker to the worker pool. <br>
   * Also notifies the worker pool thread that a new idle worker is available.
   * @param worker idle worker to return to the pool
   */
  public synchronized void returnWorker (StrategyWorker worker) {
	stack.push(worker);
	
	notifyAll();
  }
  
  /**
   *  This synchroneous method will return when all workers have finished their job
   */  
  public synchronized void waitForAllWorkersToComplete() throws InterruptedException {
      // TODO  We can be smarter here and wait once for each worker
      // Actually this is a bad way to wait, as it can introduce deadlocks easily, sync we 
      // are holding up the synchronizer's main thread -- disabling any future completion 
      // of commit and flush
      while (stack.size()<workers.length)
        wait() ;
  }
  public synchronized int getAvailWorkers() {
  	return stack.size() ;
  }
  public int getNoOfWorkers() {
  	return workers.length ;
  }
}

