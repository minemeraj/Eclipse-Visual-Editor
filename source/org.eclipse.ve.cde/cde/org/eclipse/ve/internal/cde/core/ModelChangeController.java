/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ModelChangeController.java,v $
 *  $Revision: 1.1 $  $Date: 2005-02-23 23:12:41 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;


/**
 * This is used whenever the model needs to be changed. It is required
 * that changes go through here so that the model and other changes are
 * synchronized correctly.
 * <p>
 * It is stored in the  domain for usage. The key stored in this interface can be used to retrieve it
 * from the domain.
 * 
 * @since 1.1.0
 */
public abstract class ModelChangeController {

    public static final String MODEL_CHANGE_CONTROLLER_KEY = "org.eclipse.ve.internal.cde.core.ModelChangeController"; //$NON-NLS-1$

    // TODO Not happy with these being here. They are specific to the Java Visual Editor, but JFC components are referencing
    // them to not do visual updates. They really should be moved off and figured out how the JFC components know these.
    public static final String SETUP_PHASE = "SETUP_PHASE".intern();

    public static final String INIT_VIEWERS_PHASE = "INIT_VIEWERS_PHASE"
            .intern();
    
    /**
     * Phase for modelchanges being done through {@link ModelChangeController#doModelChanges(Runnable, boolean)}.
     * 
     * @since 1.1.0
     */
    public static final String MODEL_CHANGES_PHASE = "MODEL_CHANGES_PHASE".intern();

    // Access to compoundChangeCount should be synchronized so that access from
    // codegen side in inTransaction() won't collide with changes from the UI
    // thread.
    protected int compoundChangeCount = 0;

    private Map uniqueRunnables; // Runnables to be run only once per key when
                                 // all transactions are complete

    private List phases = new ArrayList();

    protected String holdMsg;

    protected int holdState = READY_STATE; // State the controller is in with
                                           // respect to being able to process
                                           // model updates

    // according to static conditions listed below
    public static final int READY_STATE = 0; // Ready to process changes to the
                                             // model

    public static final int BUSY_STATE = 1; // Busy state - will not accept
                                            // further changes to the model

    public static final int NO_UPDATE_STATE = 2; // Generic update not
                                                 // permitted. Could be
                                                 // read-only or in error
                                                 // condition
    
    /**
     * Helper class to act as a multipart key to unique identify a block of code that the caller wants to occur
     * once and only once.  The danger of the caller just using themselves as the key is that other code within themselves
     * (or possibly a subclass) may use the same key and be blocked, so a combination of object + identifier is better, e.g.
     * 		modelChangeController.asyncExec(aRunnable, new HashKey(this,"INVALIDATE"));
     * 
     * @since 1.0.2
     */
    public static class HashKey{
        Object firstObject;
        Object secondObject;
        public HashKey(Object aFirstObject, Object aSecondObject){
            firstObject = aFirstObject;
            secondObject = aSecondObject;
        }
        public boolean equals(Object anotherObject){
            if(anotherObject instanceof HashKey){
                HashKey parm = (HashKey)anotherObject;
                return parm.firstObject.equals(firstObject) && parm.secondObject.equals(secondObject);
            } else {
                return super.equals(anotherObject);
            }
        }       
        public int hashCode() {
            return firstObject.hashCode() + secondObject.hashCode();
        }
    }
    
    public static HashKey createHashKey(Object firstObject, Object secondObject){
        return new HashKey(firstObject,secondObject);
    }

    /**
     * Call this method with a runnable. The runnable will do the actual update
     * of the model. This method will make sure that the updates are blocked
     * correctly to the text editor, for instance.
     * <p>
     * RunExceptions will not be squelched, they will be returned.
     * 
     * @param runnable -
     *            The runnable to execute.
     * @param updatePS -
     *            Whether the property sheet should be updated when this is done
     *            executing successfully. When being called from the command
     *            stack, this should be false.
     * @return It will return whether the runnable could be executed. For
     *         example if the file was read-only and could not be checked out,
     *         the update won't occur.
     */
    public boolean doModelChanges(Runnable runnable, boolean updatePS) {

        if (getHoldState() != ModelChangeController.READY_STATE)
            return false; // Not in position to execute.

        boolean nested = !phases.contains(MODEL_CHANGES_PHASE);	// Is this a nested model changes call.
        try {
        	transactionBeginning(MODEL_CHANGES_PHASE);
            startChange(nested);
            runnable.run();
            if (updatePS) {
            	IDescriptorPropertySheetEntry ps = getRootPropertySheetEntry();
            	if (ps != null)
            		ps.refreshFromRoot();
            }
        } finally {
            stopChange(nested);
            transactionEnded(MODEL_CHANGES_PHASE);
        }

        return true;
    }

    /**
     * Answet the root property sheet entry. This is used for model changes result of {@link ModelChangeController#doModelChanges(Runnable, boolean)} call
     * if the updatePS parameter is <code>true</code>.  
     * @return root property sheet entry or <code>null</code> if none.
     * 
     * @since 1.1.0
     */
    protected abstract IDescriptorPropertySheetEntry getRootPropertySheetEntry();

    /**
     * To set it to a particular hold state.
     * 
     * @param stateFlag
     *            state to set it to. If <code>READY_STATE</code>, the msg
     *            will be ignored and will be reset to <code>null</code>.
     * @param msg
     *            a msg to associate with the hold state. If <code>null</code>,
     *            then use a default msg.
     * 
     * @since 1.0.0
     */
    public synchronized void setHoldState(int stateFlag, String msg) {
        holdState = stateFlag;
        if (holdState != READY_STATE) {
            if (msg != null) {
                holdMsg = msg;
            } else {
                holdMsg = CDEMessages.getString("ModelChangeController.EditorCannotBeChangedNow"); //$NON-NLS-1$
            }
        } else {
            holdMsg = null;
        }
    }

    /**
     * Get the hold state. There are some states that are provided by the
     * standard interface, but the model controller implementation can provide
     * more.
     * <p>
     * Subclasses should return the {@link ModelChangeController#holdState} if not
     * one of their special states that they query in a different way.
     * 
     * @return current state
     * 
     * @see ModelChangeController#READY_STATE
     * @see ModelChangeController#BUSY_STATE
     * @see ModelChangeController#NO_UPDATE_STATE
     * @since 1.0.0
     */
    public abstract int getHoldState();

    /**
     * Return the hold msg associated with the current hold state, or
     * <code>null</code> if ready state.
     * <p>
     * Subclasses should return the {@link ModelChangeController#holdMsg} if not
     * one of their special messages that they query in a different way.
     * 
     * @return msg or <code>null</code> if in ready state.
     * 
     * @since 1.0.0
     */
    public abstract String getHoldMsg();

    /**
     * Tests to see if the model controller is currently processing a
     * transaction
     * 
     * @return true if in a transaction
     */
    public synchronized boolean inTransaction() {
        return compoundChangeCount > 0;
    }

    /**
     * Tell the change controller that a transaction is beginning. An optional
     * name can be given
     * @param phase Optional phase flag. It is up to the callers to determine what phase is. <code>null</code> if no phase indication needed.
     * @since 1.0.2
     */
    public synchronized void transactionBeginning(Object phase) {
        compoundChangeCount++;
        if (phase != null)
        	phases.add(phase);
    }

    /**
     * Tell the change controller that a transaction has ended. An optional phase key
     * can be given
     * @param phase the optional phase key of the transaction being ended. If that phase isn't active (i.e. begin done on it) then 
     * the call is ignored and the transaction is not ended. If <code>null</code> then the transaction counter is always deactivated.
     * @since 1.0.2
     */
    public synchronized void transactionEnded(Object phase) {
        if (phase == null || phases.remove(phase)) {
            compoundChangeCount--;
            if (compoundChangeCount <= 0) {
            	compoundChangeCount = 0;
            	phases.clear();	// Can't have any phases waiting, and if we did, then there is a nesting problem, so just clear the list.
                executeAsyncRunnables();
            }
        }
    }

    protected void executeAsyncRunnables() {
        Iterator iter = getUniqueRunnables().values().iterator();
        // Create a safe runnable so that we can run the at End's and not worry about exceptions stopping other important ones from running.
        class SafeRunnable implements ISafeRunnable {

        	public Runnable runnable;
        	
			/* (non-Javadoc)
			 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable)
			 */
			public void handleException(Throwable exception) {
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getBundle().getSymbolicName(), 0, "", exception));
			}

			/* (non-Javadoc)
			 * @see org.eclipse.core.runtime.ISafeRunnable#run()
			 */
			public void run() {
				runnable.run();
			}
        }
        SafeRunnable sr = new SafeRunnable();
        while (iter.hasNext()) {
        	sr.runnable = (Runnable) iter.next();
        	Platform.run(sr);
        }
        
        // The unique runnables are not executed from the map
        // they are run from the List because things must be done in request
        // order so the
        // map is just a way of ensuring uniqueness
        if (uniqueRunnables != null)
        	uniqueRunnables.clear();
    }

    protected Map getUniqueRunnables() {
        if (uniqueRunnables == null) {
            uniqueRunnables = new LinkedHashMap(50);
        }
        return uniqueRunnables;
    }

    /**
     * Execute the runnable after the change controller has finished all running
     * transactions
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable) {
    	execAtEndOfTransaction(aRunnable, new Object());	// Give it a unique key.
    }

    /**
     * @param runnable
     * @param once -
     *            only do the runnable once per key
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable, Object once) {

        if (inTransaction()) {
            if (!getUniqueRunnables().containsKey(once)) {
                getUniqueRunnables().put(once, aRunnable);
            }
        } else {
            Display.getDefault().asyncExec(aRunnable);
        }

    }

    /**
     * @param runnable
     * @param once
     * @param phase
     *            to omit Run the runnable, once and only once for the 2nd
     *            argument key, and do not run it if the currently executing
     *            phase is occuring
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object excludingPhase) {

        if (!phases.contains(excludingPhase)) {
            execAtEndOfTransaction(aRunnable, once);
        }
    }

    /**
     * @param runnable
     * @param once
     * @param phases run except if any these phases are in progress. May be <code>null</code> if no excluding phases.
     *            
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object[] excludingPhases) {

        if (excludingPhases != null) {
			for (int i = 0; i < excludingPhases.length; i++) {
				if (phases.contains(excludingPhases[i])) { return; }
			}
		}
        execAtEndOfTransaction(aRunnable, once);
    }

    /**
     * This will be called when changes are about to be made through the {@link ModelChangeController#doModelChanges(Runnable, boolean)} method.
     * A beginTransaction(MODEL_CHANGES) will be called first to indicate within a model changes transaction.
     * @param nested <code>false</code> if this is the outer most call to doModelChanges.
     * 
     * @since 1.1.0
     */
    protected abstract void startChange(boolean nested);

    /**
     * This will be called when changes are done being made through the {@link ModelChangeController#doModelChanges(Runnable, boolean)} methods.
     * An endTransaction(MODEL_CHANGES) will be called after this to indicate exiting a model changes transaction.
     * 
     * @param nested <code>false</code> if this is the outer most call to doModelChanges.
     * 
     * @since 1.1.0
     */
    protected abstract void stopChange(boolean nested);

    //------------------------------ @deprecated methods to be removed
    // ------------------------------

    /**
     * @deprecated
     */
    public void setHoldChanges(boolean flag, String reasonMsg) {
        // DO NOTHING @deprecated
    }

    /**
     * @deprecated get hold state instead
     */
    public boolean isHoldChanges() {
        return false;
    }

}