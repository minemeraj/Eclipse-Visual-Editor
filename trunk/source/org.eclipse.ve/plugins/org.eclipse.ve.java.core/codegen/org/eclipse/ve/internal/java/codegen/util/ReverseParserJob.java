/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Jan 11, 2005 by Gili Mendel
 * 
 *  $RCSfile: ReverseParserJob.java,v $
 *  $Revision: 1.3 $  $Date: 2005-01-19 19:20:43 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jem.internal.proxy.common.ICallbackRunnable;
 

/**
 * This is a background, low priority job for reverse parsing.
 * @since 1.1.0
 */
public abstract class ReverseParserJob extends Job {
	
	public static String    REVERSE_PARSE_JOB_NAME = "Visual Editor reverse parser";
	public static int       DEFAULT_PRIORITY = Thread.MIN_PRIORITY+1;	
	
	protected ICallbackRunnable callback ;
	
	public ReverseParserJob () {
		this(REVERSE_PARSE_JOB_NAME);
	}
	
	public ReverseParserJob (String name) {
		super(name);
		setPriority(Job.DECORATE);				
	}
	protected IStatus run(IProgressMonitor monitor) {
		Thread myThread = getThread();
		int prevPriority = myThread.getPriority();
		try {					
			myThread.setPriority(DEFAULT_PRIORITY);
			return doRun(monitor);
		}
		finally {
			myThread.setPriority(prevPriority);
		}		
	}	
	protected abstract IStatus doRun(IProgressMonitor monitor);
	
	public boolean belongsTo(Object family) {		
		return family==REVERSE_PARSE_JOB_NAME;
	}
	
	/**
	 * 
	 * @return All currently running ReverseParserJobs
	 * 
	 * @since 1.1.0
	 */
	public static Job[] getReverseParserJobs() {
		return InternalPlatform.getDefault().getJobManager().find(REVERSE_PARSE_JOB_NAME);
	}
	
	public static void cancelJobs () {
		Job[] jobs = getReverseParserJobs();
		for (int i = 0; i < jobs.length; i++) {
			jobs[i].cancel();
		}
	}
	
	public static void join(IProgressMonitor monitor) throws OperationCanceledException, InterruptedException {		
		InternalPlatform.getDefault().getJobManager().join(REVERSE_PARSE_JOB_NAME, monitor);
	}

}
