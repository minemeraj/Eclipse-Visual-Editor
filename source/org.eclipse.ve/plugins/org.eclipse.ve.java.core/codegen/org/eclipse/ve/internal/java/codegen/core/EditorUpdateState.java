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
/*
 *  $RCSfile: EditorUpdateState.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;

/**
 * 
 * @since 1.0.0
 */
public class EditorUpdateState implements IEditorUpdateState {

	private int bottomUpProcessing = 0;
	private boolean collectingDeltas = false;  // source update deltas
	private boolean topDownProcessing = false;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#setGUIReadonly(boolean)
	 */
	public synchronized void setBottomUpProcessing(boolean flag) {
		if(flag){
			bottomUpProcessing ++;
		}else{
			if(--bottomUpProcessing<0)
				bottomUpProcessing = 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#isGUIReadonly()
	 */
	public synchronized boolean isBottomUpProcessing() {
		return bottomUpProcessing > 0;
	}
	
	public synchronized int getButtomUpProcessingCount() {
		return bottomUpProcessing;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#setThreadScheduled(boolean)
	 */
	public synchronized void setCollectingDeltas(boolean flag) {		
		collectingDeltas = flag;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#isThreadScheduled()
	 */
	public synchronized boolean isCollectingDeltas() {
		return collectingDeltas;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#isGUIUpdating()
	 */
	public boolean isTopDownProcessing() {
		return topDownProcessing;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.core.IEditorUpdateState#setGUIUpdating(boolean)
	 */
	public void setTopDownProcessing(boolean updating) {
		topDownProcessing = updating;
	}
	
	public String toString() {
		return "["+hashCode()+"] ButtomUp="+bottomUpProcessing+", isCollecting="+collectingDeltas+", TopDown="+topDownProcessing; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
