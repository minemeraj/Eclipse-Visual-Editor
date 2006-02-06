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
 *  $RCSfile: ModelChangeController.java,v $
 *  $Revision: 1.12 $  $Date: 2006-02-06 23:38:37 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ve.internal.propertysheet.IDescriptorPropertySheetEntry;


/**
 * This is used whenever the model needs to be changed. It is required
 * that changes go through here so that the model and other changes are
 * synchronized correctly.
 * <p>
 * It is stored in the  domain for usage. The key stored in this interface can be used to retrieve it
 * from the domain.
 * @since 1.1.0
 */
public abstract class ModelChangeController {
	
	/**
	 * Event object for ModelChange event.
	 * <p>
	 * This class is not meant to be implemented or subclassed by customers.
	 * @since 1.2.0
	 */
	public static class ModelChangeEvent extends EventObject {

		private static final long serialVersionUID = 1L;
		
		/**
		 * Transaction started event.
		 * @see #getEvent()
		 * @since 1.2.0
		 */
		public static final int TRANSACTION_STARTED = 0;
		
		/**
		 * Transaction completed event.
		 * @see #getEvent()
		 * @since 1.2.0
		 */
		public static final int TRANSACTION_COMPLETED = 1;

		private final int event;
		
		protected ModelChangeEvent(Object source, int event) {
			super(source);
			this.event = event;
		}
		
		
		public final int getEvent() {
			return event;
		}
		
	}
	
	/**
	 * Listener for model change events.
	 * 
	 * @since 1.2.0
	 */
	public interface ModelChangeListener {
		/**
		 * Event notification
		 * @param event event. The source is the model change controller. The {@link ModelChangeEvent#getEvent()} is the event.
		 * 
		 * @since 1.2.0
		 */
		public void transactionEvent(ModelChangeEvent event);
	}

    public static final String MODEL_CHANGE_CONTROLLER_KEY = "org.eclipse.ve.internal.cde.core.ModelChangeController"; //$NON-NLS-1$

    // TODO Not happy with these being here. They are specific to the Java Visual Editor, but JFC components are referencing
    // them to not do visual updates. They really should be moved off and figured out how the JFC components know these.
    public static final String SETUP_PHASE = "SETUP_PHASE".intern(); //$NON-NLS-1$

    public static final String INIT_VIEWERS_PHASE = "INIT_VIEWERS_PHASE" //$NON-NLS-1$
            .intern();
    
    /**
     * Phase for modelchanges being done through {@link ModelChangeController#doModelChanges(Runnable, boolean)}.
     * 
     * @since 1.1.0
     */
    public static final String MODEL_CHANGES_PHASE = "MODEL_CHANGES_PHASE".intern(); //$NON-NLS-1$

    // Access to compoundChangeCount, uniqueRunnables, inRunnables should be synchronized so that access from
    // codegen side in inTransaction() won't collide with changes from the UI
    // thread.
    protected int compoundChangeCount = 0;

    private Map uniqueRunnables; // Runnables to be run only once per key when
                                 // all transactions are complete
	
	// We are running the runnables (at end). If so then any new runnables are queued up in to the
	// unique runnables, and then at the end of the exec runnables, if not in a transaction, we will
	// run those runnables that have been queued up since the start of the running runnables. If we are
	// in a transaction by the end of the exec runnables we will not do those runnables queued because
	// they are set to go into the next transaction.
	private boolean inRunnables = false;	

    private List phases = new ArrayList();

    protected String holdMsg;
    
    protected Display display;	// If this is not null, then it means the at end runnables must be executed on the UI thread.

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
    protected static class HashKey{
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
    
    protected ListenerList listeners;

    /**
     * Default ctor.
     * 
     * 
     * @since 1.1.0
     */
    public ModelChangeController() {
    }
    
    /**
     * Create with a display. This means that the execAtEnd's runnable must be executed on the UI thread.
     * @param display
     * 
     * @since 1.1.0
     */
    public ModelChangeController(Display display) {
    	this.display = display;
    }
    
    /**
     * Add a model change listener. If the listener already is added, it won't be added again.
     * @param listener
     * 
     * @since 1.2.0
     */
    public void addModelChangeListener(ModelChangeListener listener) {
    	if (listeners == null)
    		listeners = new ListenerList(ListenerList.IDENTITY);
    	listeners.add(listener);
    }
    
    /**
     * Remove a model change listener. If the listener is not registered then there will be no effect.
     * The listener may be removed during noticiation of an event.
     * @param listener
     * 
     * @since 1.2.0
     */
    public void removeModelChangeListener(ModelChangeListener listener) {
    	if (listener != null)
    		listeners.remove(listener);
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

        boolean nested = phases.contains(MODEL_CHANGES_PHASE);	// Is this a nested model changes call.
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
     * Answer the root property sheet entry. This is used for model changes result of {@link ModelChangeController#doModelChanges(Runnable, boolean)} call
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
                holdMsg = CDEMessages.ModelChangeController_EditorCannotBeChangedNow; 
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
    public void transactionBeginning(Object phase) {
    	boolean starting;
        synchronized (this) {
			starting = (0 == compoundChangeCount++);
			if (phase != null)
				phases.add(phase);
		}
        if (starting)
        	fireModelChangeEvent(new ModelChangeEvent(this, ModelChangeEvent.TRANSACTION_STARTED));
    }
    
    protected void fireModelChangeEvent(final ModelChangeEvent event) {
    	if (listeners != null) {
			final Object[] ll = listeners.getListeners();
			final int[] index = new int[1];
			ISafeRunnable safeRunnable = new ISafeRunnable() {

				public void run() throws Exception {
					((ModelChangeListener) ll[index[0]]).transactionEvent(event);
				}

				public void handleException(Throwable exception) {
				}

			};
			for (int i = 0; i < ll.length; i++) {
				index[0] = i;
				Platform.run(safeRunnable);
			}
		}
    }

    /**
     * Tell the change controller that a transaction has ended. An optional phase key
     * can be given.
     *  * <p>
     * <b>NOTE:</b>If there are no more phases and this is the end of all nested transactions, the execAtEnd
     * runnables will be executed. However, these are required to be run on the UI thread, so transactionEnded
     * will call Display.syncExec() in this case. Therefor it is very important that any callers that are not
     * on the UI thread know about the possibility of this and so they must make sure that a syncExec at this
     * point in their code will not cause a deadlock. This could happen in the case that the UI thread is
     * waiting on something and the display thread is not being processed, and it is waiting for something
     * from this thread. 
     *
     * @param phase the optional phase key of the transaction being ended. If that phase isn't active (i.e. begin done on it) then 
     * the call is ignored and the transaction is not ended. If <code>null</code> then the transaction counter is always deactivated.
     * @since 1.0.2
     */
    public void transactionEnded(Object phase) {
    	boolean executeRunnables = false;
    	boolean transactionEnded = false;
        synchronized (this) {
			if (phase == null || phases.remove(phase)) {
				compoundChangeCount--;
				if (compoundChangeCount <= 0) {
					compoundChangeCount = 0;
					phases.clear(); // Can't have any phases waiting, and if we did, then there is a nesting problem, so just clear the list.
					executeRunnables = uniqueRunnables != null && !uniqueRunnables.isEmpty();
					transactionEnded = true;
				}
			}
		}
        if (executeRunnables) {
        	// This must be done outside of the synchronized because it would be possible that 
        	// UI thread is trying to do something with the model controller at the same time, 
        	// and so it would be locked. That would prevent the executeAsyncRunnables from running.
			// If we have a display, and we are currently not on the display's thread, then do
			// an asyncExec for it to get processed over there. Else we have no display, or
			// we are on the display's thread, so we can execute directly.
        	if (display != null && Display.getCurrent() != display)
        		display.asyncExec(new Runnable() {
					public void run() {
						executeAsyncRunnables();
					}
				});
        	else
        		executeAsyncRunnables();
        }
        
        if (transactionEnded) {
        	// Execute async if not on ui thread so that this notification occurs
        	// after ALL asyncrunnables have run.
        	if (display != null && Display.getCurrent() != display)
        		display.asyncExec(new Runnable() {
					public void run() {
						fireModelChangeEvent(new ModelChangeEvent(this, ModelChangeEvent.TRANSACTION_COMPLETED));
					}
				});
        	else
        		fireModelChangeEvent(new ModelChangeEvent(this, ModelChangeEvent.TRANSACTION_COMPLETED));
        	
        }
    }

    protected void executeAsyncRunnables() {
        // Create a safe runnable so that we can run the at End's and not worry about exceptions stopping other important ones from running.
        class SafeRunnable implements ISafeRunnable {

        	public Runnable runnable;
        	
			/* (non-Javadoc)
			 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable)
			 */
			public void handleException(Throwable exception) {
			}

			/* (non-Javadoc)
			 * @see org.eclipse.core.runtime.ISafeRunnable#run()
			 */
			public void run() {
				runnable.run();
			}
        }
		SafeRunnable sr = new SafeRunnable();
		
        while (true) {
			Runnable[] runnables;
			synchronized (this) {
				// Make a copy so that we can clear the list.
				Collection ur = getUniqueRunnables().values();
				runnables = (Runnable[]) ur.toArray(new Runnable[ur.size()]);
				ur.clear();
				inRunnables = true;
			}
			
			for (int i = 0; i < runnables.length; i++) {
				sr.runnable = runnables[i];
				Platform.run(sr);
			}

			synchronized (this) {
				if (inTransaction() || getUniqueRunnables().isEmpty()) {
					inRunnables = false;
					break;
				}
				// Not in transaction and we have more runnables queued up since we last grabbed them at the top
				// we will go again and send these.
			}
		}
    }

    protected Map getUniqueRunnables() {
        if (uniqueRunnables == null) {
            uniqueRunnables = new LinkedHashMap(50);
        }
        return uniqueRunnables;
    }

    /**
     * Execute the runnable after the change controller has finished all running
     * transactions. If constructed with a display, then these will be executed on the display thread.
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable) {
    	execAtEndOfTransaction(aRunnable, new Object());	// Give it a unique key.
    }

    /**
     * Execute at the end, but do not queue up more than the first runnable with the given once key.
     * If constructed with a display, then these will be executed on the display thread.
     * @param runnable
     * @param once -
     *            only do the runnable once per key
     * 
     * @since 1.0.2
     */
    public synchronized void execAtEndOfTransaction(Runnable aRunnable, Object once) {

        if (inTransaction() || inRunnables) {
            if (!getUniqueRunnables().containsKey(once)) {
                getUniqueRunnables().put(once, aRunnable);
            }
        } else {
            Display.getDefault().asyncExec(aRunnable);
        }

    }

    /**
     * Execute at the end, but do not queue up more than the first runnable with the given once key.
     * Do not queue up if excludingPhase is currently one of the current phases.
     * If constructed with a display, then these will be executed on the display thread.

     * @param runnable
     * @param once
     * @param excludingPhase
     *            to omit Run the runnable, once and only once for the 2nd
     *            argument key, and do not run it if the currently executing
     *            phase is occuring
     * @return <code>true</code> if not within excluding phase, <code>false</code> if excluding due to excluding phase.
     * 
     * @since 1.0.2
     */
    public synchronized boolean execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object excludingPhase) {

        if (!phases.contains(excludingPhase)) {
            execAtEndOfTransaction(aRunnable, once);
            return true;
        } else
        	return false;
    }

    /**
     * Execute at the end, but do not queue up more than the first runnable with the given once key.
     * Do not queue up if any of the excludingPhase's are currently one of the current phases.
     * If constructed with a display, then these will be executed on the display thread.
     * @param runnable
     * @param once
     * @param excludingPhases run except if any these phases are in progress. May be <code>null</code> if no excluding phases.
     * @return <code>true</code> if it was not within excluding phases, <code>false</code> if it was excluded due to excluding phases.           
     * 
     * @since 1.0.2
     */
    public synchronized boolean execAtEndOfTransaction(Runnable aRunnable, Object once,
            Object[] excludingPhases) {

        if (excludingPhases != null) {
			for (int i = 0; i < excludingPhases.length; i++) {
				if (phases.contains(excludingPhases[i])) { return false; }
			}
		}
        execAtEndOfTransaction(aRunnable, once);
        return true;
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
