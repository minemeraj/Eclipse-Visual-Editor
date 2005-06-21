/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created Jan 11, 2005 by Gili Mendel
 * 
 *  $RCSfile: ReverseParserJob.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-21 22:18:28 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jem.internal.proxy.common.ICallbackRunnable;
 

/**
 * This is a background, low priority job for reverse parsing.
 * @since 1.1.0
 */
public abstract class ReverseParserJob extends Job {
	
	public static String    REVERSE_PARSE_JOB_NAME = Messages.ReverseParserJob_0; 
	public static int       DEFAULT_PRIORITY = Thread.MIN_PRIORITY+1;	
	private Object			family;
	
	protected ICallbackRunnable callback ;
	
	public ReverseParserJob (IFile file) {
		this(file,REVERSE_PARSE_JOB_NAME);
	}
	
	public static Object getJobFamily(IFile file) {
		return file.isAccessible() ? (REVERSE_PARSE_JOB_NAME+":"+file.getLocation().toString()).intern() : null; //$NON-NLS-1$
	}
	
	public ReverseParserJob (IFile file, String name) {
		super(name);
		family = getJobFamily(file);
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
		return family==this.family;
	}
	
	/**
	 * 
	 * @return All currently running ReverseParserJobs
	 * 
	 * @since 1.1.0
	 */
	public static Job[] getReverseParserJobs(IFile file) {
        Object family = getJobFamily(file);
		return family != null ? Platform.getJobManager().find(getJobFamily(file)) : new Job[0];
	}
	
	public static void cancelJobs (IFile file) {
		Job[] jobs = getReverseParserJobs(file);
		for (int i = 0; i < jobs.length; i++) {
			jobs[i].cancel();
		}
	}
	
	public static void join(IFile file, IProgressMonitor monitor) throws OperationCanceledException, InterruptedException {
        Object family = getJobFamily(file);
        if (family != null)
		Platform.getJobManager().join(getJobFamily(file), monitor);
	}

}
