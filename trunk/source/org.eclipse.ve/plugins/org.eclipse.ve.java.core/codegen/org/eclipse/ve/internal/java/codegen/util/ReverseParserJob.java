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
 *  $Revision: 1.5 $  $Date: 2005-02-16 21:12:28 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jem.internal.proxy.common.ICallbackRunnable;
 

/**
 * This is a background, low priority job for reverse parsing.
 * @since 1.1.0
 */
public abstract class ReverseParserJob extends Job {
	
	public static String    REVERSE_PARSE_JOB_NAME = Messages.getString("ReverseParserJob.0"); //$NON-NLS-1$
	public static int       DEFAULT_PRIORITY = Thread.MIN_PRIORITY+1;	
	private Object			family;
	
	protected ICallbackRunnable callback ;
	
	public ReverseParserJob (IFile file) {
		this(file,REVERSE_PARSE_JOB_NAME);
	}
	
	public static Object getJobFamily(IFile file) {
		return (REVERSE_PARSE_JOB_NAME+":"+file.getLocation().toString()).intern(); //$NON-NLS-1$
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
		return InternalPlatform.getDefault().getJobManager().find(getJobFamily(file));
	}
	
	public static void cancelJobs (IFile file) {
		Job[] jobs = getReverseParserJobs(file);
		for (int i = 0; i < jobs.length; i++) {
			jobs[i].cancel();
		}
	}
	
	public static void join(IFile file, IProgressMonitor monitor) throws OperationCanceledException, InterruptedException {		
		InternalPlatform.getDefault().getJobManager().join(getJobFamily(file), monitor);
	}

}
