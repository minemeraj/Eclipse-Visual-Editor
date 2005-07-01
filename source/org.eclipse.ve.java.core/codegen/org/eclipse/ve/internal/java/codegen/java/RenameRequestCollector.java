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
 *  $RCSfile: RenameRequestCollector.java,v $
 *  $Revision: 1.1 $  $Date: 2005-07-01 20:21:04 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;

 

public class RenameRequestCollector implements Runnable {
	private List renameRequests = null;
	private IBeanDeclModel bdm = null;
	public RenameRequestCollector(IBeanDeclModel bdm){
		this.bdm = bdm;
	}
	public void run() {
		if(renameRequests!=null && renameRequests.size()>0){
			bdm.suspendSynchronizer();
			try{
				for (int requestSize = 0; requestSize < renameRequests.size(); requestSize++) {
					((Runnable)renameRequests.get(requestSize)).run();
				}
			}finally{
				renameRequests.clear();
				bdm.resumeSynchronizer();
			}
		}
		AnnotationDecoderAdapter.renameCollector = null;
	}

	public void addRequest(Runnable runnable){
		if(renameRequests==null)
			renameRequests = new ArrayList();
		renameRequests.add(runnable);
	}
}
