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
 *  $RCSfile: JavaVisualEditorModelChangeController.java,v $
 *  $Revision: 1.5 $  $Date: 2005-02-21 14:41:41 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.IRewriteTarget;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.cde.core.IModelChangeController;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelBuilder;
 
/**
 * Change controller for the JVE. 
 * It is <package> protected because only the JavaVisualEditorPart should access it.
 * 
 * @since 1.0.0
 */
class JavaVisualEditorModelChangeController implements IModelChangeController {
	
	private JavaVisualEditorPart part;
	private IDiagramModelBuilder modelBuilder;

	// Note any access to compoundChangeCount is synchronized so that access from codegen side in inTransaction()
	// won't collide with changes from the UI thread.
	private int compoundChangeCount = 0;
	private int holdState = READY_STATE;
	private String holdMsg = null;
    private List runnables;		// Runnables to be queued to run when all transactions are complete
    private Map uniqueRunnables;	// Runnables to be run only once per key when all transactions are complete
    private List phases = new Vector(); 

	public JavaVisualEditorModelChangeController(JavaVisualEditorPart part, IDiagramModelBuilder modelBuilder) {
		this.modelBuilder = modelBuilder;
		this.part = part;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#inTransaction()
	 */
	public boolean inTransaction() {
		return compoundChangeCount > 0;
	}
	
	public void setHoldChanges(boolean flag, String msg) {
		// TODO deprecated - remove when ready
	}

	public boolean isHoldChanges() {
		return false;	// TODO deprecated - remove when ready.
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#getHoldState()
	 */	
	public int getHoldState() {
		Assert.isTrue(Display.getCurrent() != null);
		
		if (modelBuilder.isBusy())
			return BUSY_STATE;
		else if (holdState != READY_STATE)
			return holdState;
		else {
			synchronized (this) {
				if (compoundChangeCount == 0 && !part.validateEditorInputState())	// Not in transaction and not valid to change
					return NO_UPDATE_STATE;
				else
					return READY_STATE;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#run(Runnable, boolean)
	 */	
	public boolean run(Runnable runnable, boolean updatePS) {
		
		if (getHoldState() != IModelChangeController.READY_STATE)
			return false;	// Not in position to execute.
		
		try {
			startChange();
			runnable.run();
			if (updatePS && this.part.rootPropertySheetEntry != null)
				this.part.rootPropertySheetEntry.refreshFromRoot();
		} finally {
			stopChange();
		}

		return true;
	}

	private synchronized void startChange() {
		if (compoundChangeCount++ == 0) {
			// The undomanager doesn't handle nesting of compound changes, so we need to do it here.
			IRewriteTarget rewriteTarget = (IRewriteTarget) this.part.getAdapter(IRewriteTarget.class);
			rewriteTarget.beginCompoundChange();
			modelBuilder.startTransaction();
		}
	}

	private synchronized void stopChange() {
		if (--compoundChangeCount <= 0) {
			compoundChangeCount = 0; // In case we get out of sync.
			try {
				modelBuilder.commit();
			} finally {
				// this must be done or undo stack will get messed up. Just in case an error in commit.
				// Also must be done AFTER commit in case commit changes some more of the code and that
				// needs to be under compound change too.
				IRewriteTarget rewriteTarget = (IRewriteTarget) this.part.getAdapter(IRewriteTarget.class);
				rewriteTarget.endCompoundChange();
				executeAsyncRunnables();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#getHoldMsg()
	 */
	public String getHoldMsg() {
		if (holdMsg != null)
			return holdMsg;
		
		switch (getHoldState()) {
			case BUSY_STATE:
				return CodegenEditorPartMessages.getString("JavaVisualEditorModelChangeController.EditorBusyAndCannotChangeNow"); //$NON-NLS-1$
			case NO_UPDATE_STATE:
				return CodegenEditorPartMessages.getString("JavaVisualEditorModelChangeController.EditorCannotBeChangedNow"); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IModelChangeController#setHoldState(int, java.lang.String)
	 */
	public void setHoldState(int stateFlag, String msg) {
		holdState = stateFlag;
		if (holdState != READY_STATE)
			if (msg != null)
				holdMsg = msg;
			else
				holdMsg = CodegenEditorPartMessages.getString("JavaVisualEditorModelChangeController.EditorCannotBeChangedNow"); //$NON-NLS-1$
		else
			holdMsg = null;
	}
	
	private List getRunnables(){
	    if(runnables == null){
	        runnables = new ArrayList();
	    }
	    return runnables;
	}

    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#asyncExec(java.lang.Runnable)
     */
    public void asyncExec(Runnable aRunnable) {
        
        if (inTransaction()){
            getRunnables().add(aRunnable);
        } else {
            Display.getDefault().asyncExec(aRunnable);
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#beginTransaction()
     */
    public void beginTransaction(Object phase) {
        compoundChangeCount++;
        phases.add(phase);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#endTransaction()
     */
    public synchronized void endTransaction(Object phase) {
        if(phases.indexOf(phase) != -1){
            compoundChangeCount--;
            if(compoundChangeCount <= 0){
                executeAsyncRunnables();
            }
            phases.remove(phase);
        }
    }
    
    private void executeAsyncRunnables(){
        Iterator iter = getRunnables().iterator();
        while(iter.hasNext()){
            ((Runnable)iter.next()).run();
        }        
		// The unique runnables are not executed from the map
        // they are run from the List because things must be done in request order so the
        // map is just a way of ensuring uniqueness
        runnables = new Vector();
        uniqueRunnables = new HashMap();
    }
    
    private Map getUniqueRunnables(){
        if(uniqueRunnables == null){
            uniqueRunnables = new HashMap(50);
        }
        return uniqueRunnables;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#asyncExec(java.lang.Runnable, java.lang.Object)
     */
    public void asyncExec(Runnable aRunnable, Object once) {
      
        if (inTransaction()){
            if (getUniqueRunnables().get(once) == null){
                getUniqueRunnables().put(once,aRunnable);
                getRunnables().add(aRunnable);
            }
        } else {
            Display.getDefault().asyncExec(aRunnable);
        }        
       
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#asyncExec(java.lang.Runnable, java.lang.Object)
     */
    public void asyncExec(Runnable aRunnable, Object once, Object excludingPhase) {
        
        if(phases.indexOf(excludingPhase) == -1){
            asyncExec(aRunnable,once);
        }       
    }

    /* (non-Javadoc)
     * @see org.eclipse.ve.internal.cde.core.IModelChangeController#asyncExec(java.lang.Runnable, java.lang.Object, java.lang.Object[])
     */
    public void asyncExec(Runnable aRunnable, Object once, Object[] excludingPhases) {

        for (int i = 0; i < excludingPhases.length; i++) {
            if(phases.indexOf(excludingPhases[i]) != -1){
                return;
            }
        }
        asyncExec(aRunnable,once);        
    }    
}