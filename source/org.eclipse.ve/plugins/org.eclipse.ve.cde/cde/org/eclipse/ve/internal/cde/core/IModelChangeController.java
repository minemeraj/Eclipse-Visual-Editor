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
package org.eclipse.ve.internal.cde.core;

import java.util.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;

/*
 *  $RCSfile: IModelChangeController.java,v $
 *  $Revision: 1.7 $  $Date: 2005-02-22 13:51:27 $ 
 */

/**
 * This interface is used whenever the model needs to be changed. It is required
 * that changes go through here so that the model and other changes are
 * synchronized correctly.
 * 
 * This interface is not meant to be implemented by users. It is stored in the
 * domain for usage. The key stored in this interface can be used to retrieve it
 * from the domain.
 */
public abstract class IModelChangeController {

    public static final String MODEL_CHANGE_CONTROLLER_KEY = "org.eclipse.ve.internal.cde.core.IModelChangeController"; //$NON-NLS-1$

    public static final String SETUP_PHASE = "SETUP_PHASE".intern();

    public static final String LOADING_PHASE = "LOADING_PHASE".intern();

    public static final String INIT_VIEWERS_PHASE = "INIT_VIEWERS_PHASE"
            .intern();

    // Access to compoundChangeCount should be synchronized so that access from
    // codegen side in inTransaction() won't collide with changes from the UI
    // thread.
    protected int compoundChangeCount = 0;

    private List runnables; // Runnables to be queued to run when all
                            // transactions are complete

    private Set uniqueRunnables; // Runnables to be run only once per key when
                                 // all transactions are complete

    private List phases = new Vector();

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
     * 
     * RunExceptions will not be squelched, they will be returned,
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

        if (getHoldState() != IModelChangeController.READY_STATE)
            return false; // Not in position to execute.

        try {
            startChange();
            runnable.run();
            if (updatePS && getRootPropertySheetEntry() != null)
                getRootPropertySheetEntry().refreshFromRoot();
        } finally {
            stopChange();
        }

        return true;
    }

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
    public void setHoldState(int stateFlag, String msg) {
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
     * 
     * @return current state
     * 
     * @see IModelChangeController#READY_STATE
     * @see IModelChangeController#BUSY_STATE
     * @see IModelChangeController#NO_UPDATE_STATE
     * @since 1.0.0
     */
    public abstract int getHoldState();

    /**
     * Return the hold msg associated with the current hold state, or
     * <code>null</code> if ready state.
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
    public boolean inTransaction() {
        return compoundChangeCount > 0;
    }

    /**
     * Tell the change controller that a transaction is beginning. An optional
     * name can be given
     * 
     * @since 1.0.2
     */
    public void transactionBeginning(Object phase) {
        compoundChangeCount++;
        phases.add(phase);
    }

    /**
     * Tell the change controller that a transaction has ended. An optional name
     * can be given
     * 
     * @since 1.0.2
     */
    public synchronized void transactionEnded(Object phase) {
        if (phases.indexOf(phase) != -1) {
            compoundChangeCount--;
            if (compoundChangeCount <= 0) {
                executeAsyncRunnables();
            }
            phases.remove(phase);
        }
        if (phase.equals(INIT_VIEWERS_PHASE)) {
            System.out.println("*-*-*-*-*-*-* Phase Ended" + " INIT_VIEWERS");
        } else if (phase.equals(LOADING_PHASE)) {
            System.out.println("*-*-*-*-*-*-* Phase Ended" + " LOADING");
        } else if (phase.equals(SETUP_PHASE)) {
            System.out.println("*-*-*-*-*-*-* Phase Ended" + " SETUP");
        }
    }

    protected void executeAsyncRunnables() {
        Iterator iter = getRunnables().iterator();
        while (iter.hasNext()) {
            ((Runnable) iter.next()).run();
        }
        // The unique runnables are not executed from the map
        // they are run from the List because things must be done in request
        // order so the
        // map is just a way of ensuring uniqueness
        runnables = new Vector();
        uniqueRunnables = new HashSet(50);
    }

    protected Set getUniqueRunnables() {
        if (uniqueRunnables == null) {
            uniqueRunnables = new HashSet(50);
        }
        return uniqueRunnables;
    }

    /**
     * Execute the runnable after the change controller has finished all running
     * transactions
     * 
     * @since 1.0.2
     */
    public void execAtEndOfTransaction(Runnable aRunnable) {

        if (inTransaction()) {
            getRunnables().add(aRunnable);
        } else {
            Display.getDefault().asyncExec(aRunnable);
        }
    }

    /**
     * @param runnable
     * @param once -
     *            only do the runnable once per key
     * 
     * @since 1.0.2
     */
    public void execAtEndOfTransaction(Runnable aRunnable, Object once) {

        if (inTransaction()) {
            if (!getUniqueRunnables().contains(once)) {
                getUniqueRunnables().add(once);
                getRunnables().add(aRunnable);
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
    public void execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object excludingPhase) {

        if (phases.indexOf(excludingPhase) == -1) {
            execAtEndOfTransaction(aRunnable, once);
        }
    }

    /**
     * @param runnable
     * @param once
     * @param phase
     *            to omit Run the runnable, once and only once for the 2nd
     *            argument key, and do not run it if the currently executing
     *            phases are occurring
     * 
     * @since 1.0.2
     */
    public void execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object[] excludingPhases) {

        for (int i = 0; i < excludingPhases.length; i++) {
            if (phases.indexOf(excludingPhases[i]) != -1) {
                return;
            }
        }
        execAtEndOfTransaction(aRunnable, once);
    }

    protected abstract void startChange();

    protected abstract void stopChange();

    private List getRunnables() {
        if (runnables == null) {
            runnables = new ArrayList();
        }
        return runnables;
    }

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