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
 *  $Revision: 1.1 $  $Date: 2005-01-13 21:02:40 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jem.internal.proxy.common.ICallbackRunnable;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
 

/**
 * This is a background, low priority job for reverse parsing.
 * @since 1.1.0
 */
public abstract class ReverseParserJob extends Job {
	
	public static String    REVERSE_PARSE_JOB_NAME = "Visual Editor Reverse Parser";
	public static int       DEFAULT_PRIORITY = Thread.MIN_PRIORITY+1;
	
	protected ICallbackRunnable callback ;
	
	public ReverseParserJob () {
		this(REVERSE_PARSE_JOB_NAME);
	}
	
	public ReverseParserJob (String name) {
		super(REVERSE_PARSE_JOB_NAME);
		setPriority(Job.DECORATE);		
	}
	protected IStatus run(IProgressMonitor monitor) {
		Thread myThread = getThread();
		int prevPriority = myThread.getPriority();
		try {					
			myThread.setPriority(DEFAULT_PRIORITY);
			return doRun(monitor);
		} catch (Exception e) {
			return new Status(Status.ERROR,CDEPlugin.getPlugin().getPluginID(), 0, "",e);
		}
		finally {
			myThread.setPriority(prevPriority);
		}		
	}	
	protected abstract IStatus doRun(IProgressMonitor monitor); 	
}
