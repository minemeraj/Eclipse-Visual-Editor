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
package org.eclipse.ve.internal.jfc.vm;

/*
 *  $RCSfile: ComponentManager.java,v $
 *  $Revision: 1.9 $  $Date: 2005-07-08 17:51:49 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.jfc.common.Common;

/**
 * This is the ComponentManager. It it is a listener for the component changes and it provides helper routines to quickly access information.
 * <p>
 * It also handles the freeform settings for loc and bounds since those need to be saved and returned as default values when set by freeform override.
 * <p>
 * This is not meant to be subclassed. Users should use ComponentManagerExtension for that.
 * 
 * @see ComponentManager.ComponentManagerExtension for extensions.
 * @since 1.1.0
 */
public class ComponentManager implements ComponentListener, HierarchyBoundsListener, HierarchyListener {

	protected Component fComponent;

	protected ComponentManagerFeedbackController feedbackController;
	
	protected ComponentManagerExtension[] extensions;
	
	/**
	 * All component manager extensions must be a subclass of this. They
	 * will be told of significant events and can contribute to them.
	 * 
	 * @since 1.1.0
	 */
	public abstract static class ComponentManagerExtension {
		
		private ComponentManager componentManager;
		
		void setComponentManager(ComponentManager componentManager) {
			ComponentManager old = this.componentManager;
			this.componentManager = componentManager;
			componentManagerSet(old);
		}
		
		/**
		 * Called when this extension is added or removed from a componentmanager.
		 * @param oldManager old manager or <code>null</code> if not previously in a manager.
		 * 
		 * @since 1.1.0
		 */
		protected void componentManagerSet(ComponentManager oldManager) {
			
		}
		
		/**
		 * Get the component manager this is associated with.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public final ComponentManager getComponentManager() {
			return componentManager;
		}
		
		/**
		 * Get the component being managed.
		 * @return the component being managed or <code>null</code> if not managing a component at this time.
		 * 
		 * @since 1.1.0
		 */
		public final Component getComponent() {
			return componentManager != null ? componentManager.getComponent() : null;
		}
		
		/**
		 * The component has moved, or one of its ancestors has moved. 
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void componentMoved() {
		}

		/**
		 * Component was hidden.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void componentHidden() {
		}

		/**
		 * Component was resized.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void componentResized() {
		}
		
		/**
		 * The component has been shown.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void componentShown() {
			
		}
		
		/**
		 * A refresh has been queued up.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void refreshQueued() {
			
		}
		
		/**
		 * Notification that the component is invalid.
		 * <p>
		 * This is called by the Feedback controller when it is about to validate all of the queued up windows. This will
		 * only be called for invalidations done through the client call to {@link ComponentManager#invalidate() invalidate}
		 * and to any parents of those components. It will not be called for any component that was invalidated through
		 * some other means.
		 * <p>
		 * The default is to do nothing. Subclasses may do other things. But be aware that immediately after this call
		 * the component will be validated.
		 * 
		 * @since 1.1.0
		 */
		protected void invalidated() {
			
		}
		
		/**
		 * Called when everything is all set up and ready for extensions to
		 * add any listeners that they want. These listeners must be cleaned up
		 * in the {@link ComponentManager.ComponentManagerExtension#componentSet(Component, Component)}.
		 * 
		 * 
		 * @since 1.1.0
		 */
		protected void startComponentListening() {
			
		}
		
		/**
		 * Called when a new component is set into the manager.
		 * @param oldComponent old component or <code>null</code> if no old component.
		 * @param newComponent new component or <code>null</code> if no new component.
		 * 
		 * @since 1.1.0
		 */
		protected void componentSet(Component oldComponent, Component newComponent) {
			
		}
	}

	/**
	 * This class manages the feedback from the component managers. It tries to batch them all together so that it can be sent back as one transaction
	 * There is only one per registry.
	 * 
	 * @since 1.1.0
	 */
	public static class ComponentManagerFeedbackController implements ICallback, Runnable {

		protected IVMCallbackServer fServer;	// This will be set to null after shutdown called. Access is thru sync(tranactions).

		protected int fCallbackID;
		
		private boolean changesHeld;	// Changes are being queued.

		private List transactions = new ArrayList(); // List of transactions to send.
		private Map uniquesMap = new HashMap();	// Map of unique transactions (ComponentManager,ID, index in transactions array). (key and value the same object). Used to keep only the last unique one in list.
		/*
		 * The Unique set object for the uniquesSet. The hash key for it will be for (ComponentManagerFeedbackControllerNotifier,ID). It will also
		 * contain the index, which will be changed if a new unique comes along.
		 */
		private static class UniqueEntry {
			protected final Object notifier;
			protected final int ID;
			private int index;
			
			/**
			 * Construct with cm and ID.
			 * @param notifier
			 * @param ID
			 * 
			 * @since 1.1.0
			 */
			public UniqueEntry(Object notifier, int ID) {
				this.notifier = notifier;
				this.ID = ID;
			}

			public int hashCode() {
				return 31*(31 + notifier.hashCode())+ID;
			}
			
			public boolean equals(Object obj) {
				if (obj == this)
					return true;
				else {
					try {
						UniqueEntry ue = (UniqueEntry) obj;
						return (this.notifier == ue.notifier && this.ID == ue.ID);
					} catch (ClassCastException e) {
						// We should never get this because this set will only have UniqueEntry as entries.
						return false;
					}
				}
			}
			
			/**
			 * @param index The index to set.
			 * 
			 * @since 1.1.0
			 */
			public void setIndex(int index) {
				this.index = index;
			}

			/**
			 * @return Returns the index.
			 * 
			 * @since 1.1.0
			 */
			public int getIndex() {
				return index;
			}
		}

		private List queuedRefreshRequests;

		private Set queuedInvalidImages; // The components that have invalid images will be stored in here.

		private Set queuedInvalidWindows; // The awt.Windows (top-most component) that have been invalidated.

		private Map componentToComponentManagers = new HashMap(); // Map of component's to component managers.

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.common.ICallback#initializeCallback(org.eclipse.jem.internal.proxy.common.IVMServer, int)
		 */
		public void initializeCallback(IVMCallbackServer server, int callbackID) {
			fServer = server;
			fCallbackID = callbackID;
			server.getIVMServer().addShutdownListener(new Runnable() {
			
				public void run() {
					synchronized (transactions) {
						if (fServer != null) {
							// We're going down and there is no way to tell AWT Event queue to go away. So we just say we are shutting down.
							fServer = null;
						}
					}
				}
			
			});
		}

		/**
		 * Register that we have a component to manager relationship.
		 * 
		 * @param component
		 * @param manager
		 * 
		 * @since 1.1.0
		 */
		public void registerComponentManager(Component component, ComponentManager manager) {
			componentToComponentManagers.put(component, manager);
		}

		/**
		 * Deregister the component to manager relationship.
		 * 
		 * @param component
		 * 
		 * @since 1.1.0
		 */
		public void deregisterComponentManager(Component component) {
			componentToComponentManagers.remove(component);
		}

		/**
		 * Queue up for this component and its parents that have registered component managers that their images are invalid. The client will
		 * eventually call and have all queued up sent back.
		 * 
		 * @param component
		 * 
		 * @since 1.1.0
		 */
		public void invalidateImage(Component component) {
			// We will gather the top-most windows from all of the intermediate components.
			// This is so that when asked to send back all invalid images we can validate the
			// awt.Windows that are invalid.
			if (queuedInvalidImages == null) {
				queuedInvalidImages = new HashSet();
				queuedInvalidWindows = new HashSet();
			}
			for (; component != null; component = component.getParent()) {
				queuedInvalidImages.add(component);
				if (component instanceof Window) {
					queuedInvalidWindows.add(component);
					break;
				}
					
			}
		}

		private Runnable validateImages = new Runnable() {

			public void run() {
				// Make sure we still have image to go.
				if (queuedInvalidImages != null && !queuedInvalidImages.isEmpty()) {
					// Send out invalidated notification to all of the invalid components.
					for (Iterator itr = queuedInvalidImages.iterator(); itr.hasNext();) {
						Component component = (Component) itr.next();
						ComponentManager cmanager = (ComponentManager) componentToComponentManagers.get(component);
						if (cmanager != null)
							cmanager.invalidated();
					}
					// Validate the awt.Windows so that we can get the correct size/loc changes being sent.
					for (Iterator itr = queuedInvalidWindows.iterator(); itr.hasNext();) {
						Window window = (Window) itr.next();
						window.validate();
					}
					queuedInvalidWindows.clear();
					// Now send the component manager invalid images transactions.
					synchronized (transactions) {
						if (fServer == null)
							return;	// We are shut down.
						for (Iterator itr = queuedInvalidImages.iterator(); itr.hasNext();) {
							Component component = (Component) itr.next();
							ComponentManager cmanager = (ComponentManager) componentToComponentManagers.get(component);
							if (cmanager != null) {
								appendTransaction(cmanager, Common.CL_IMAGEINVALID, null, true);
							}
						}
					}
					queuedInvalidImages.clear();
					ComponentManagerFeedbackController.this.run();
				}
			}
		};

		/**
		 * Called from the client to post back all of the invalid images.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public void postInvalidImages() {
			if (queuedInvalidImages != null && !queuedInvalidImages.isEmpty()) {
				// We have something to do, but queue it off to the AWT UI thread so that the validates are done on the UI thread.
				// This will make sure most (but unforunately not all) size/location changes are grouped together and batched rather
				// than happening sporadically.
				EventQueue.invokeLater(validateImages);
			}
		}

		/**
		 * Add the transaction to the feedback controller and queue it up. These are to be called from ComponentManager and ComponentManager
		 * subclasses only.
		 * 
		 * @param notifier
		 *            notifier that this transaction is from. This notifier must be registered on the IDE side to permit notification to work. It will
		 *            be ignored if not registered.
		 * @param callbackID
		 *            the if of the callback.
		 * @param parms
		 *            array of parms for the callback. <code>null</code> if there are no parms.
		 * @param unique
		 *            <code>true</code> if this is a unique transaction and should replace previous one of same manager/id.
		 * @since 1.1.0
		 */
		public void addTransaction(Object notifier, int callbackID, Object[] parms, boolean unique) {
			appendTransaction(notifier, callbackID, parms, unique);
			boolean queueNow;
			synchronized (this) {
				queueNow = !changesHeld;
			}
			if (queueNow)
				EventQueue.invokeLater(this); // Queue up for later execution.
		}

		/*
		 * Append the transaction to the transaction list.
		 * 
		 * @since 1.1.0
		 */
		private void appendTransaction(Object notifier, int callbackID, Object[] parms, boolean unique) {
			synchronized (transactions) {
				if (fServer == null)
					return;	// We are shut down.
				if (unique) {
					UniqueEntry newUE = new UniqueEntry(notifier, callbackID);
					UniqueEntry ue = (UniqueEntry) uniquesMap.get(newUE);
					if (ue != null) {
						// Clear out the old transaction.
						int index = ue.getIndex();
						transactions.set(index++, null);
						transactions.set(index++, null);
						transactions.set(index++, null);
					} else {
						ue = newUE;
						uniquesMap.put(ue, ue);
					}
					ue.setIndex(transactions.size());
				}
				transactions.add(notifier);
				transactions.add(new Integer(callbackID));
				transactions.add(parms != null ? new ICallbackHandler.TransmitableArray(parms) : (Object) parms);
			}
		}

		/**
		 * Queue up the initial refresh request for a manager. The client will call postInitialRefresh when all of the components are initialized.
		 * That will then take these queued requests and send them.
		 * 
		 * @param manager
		 * 
		 * @since 1.1.0
		 */
		void queueInitialRefresh(ComponentManager manager) {
			if (queuedRefreshRequests == null)
				queuedRefreshRequests = new ArrayList();
			queuedRefreshRequests.add(manager);
		}

		/**
		 * This is called by the client. It will take the queued up initial refresh requests and post them to be done.
		 * 
		 * @since 1.1.0
		 */
		public void postInitialRefresh() {
			if (queuedRefreshRequests != null && !queuedRefreshRequests.isEmpty()) {
				final List queued = queuedRefreshRequests;
				EventQueue.invokeLater(new Runnable() {

					public void run() {
						synchronized (transactions) {
							if (fServer == null)
								return;	// We are shutdown
							for (int i = 0; i < queued.size(); i++) {
								ComponentManager manager = (ComponentManager) queued.get(i);
								if (manager.fComponent != null) {
									appendTransaction(manager, Common.CL_REFRESHED, manager.getRefreshParms(), true);
									manager.startComponentListening(); // Start the component listening now.
								}
							} 
						}

						ComponentManagerFeedbackController.this.run(); // Now send all of these back at one time.
					}
				});
			}
			;
			queuedRefreshRequests = null;
		}
		
		/**
		 * Starting changes. When called by client, it will set the addTransaction request to not do an invokeLater but
		 * instead just queue the transaction. Eventually a postChanges() request will be made and at
		 * that time any queued up transaction will be sent. This allows the client to tell us that there
		 * may be multiple changes coming other than new components or invalid images. Such as a size change.
		 * This will allow all size changes to be queued up until the end.
		 * 
		 * @since 1.1.0
		 */
		public synchronized void startingChanges() {
			changesHeld = true;
		}
		
		/**
		 * Stopping changes. Called by client to indicate all changes held should now be sent.
		 * 
		 * @since 1.1.0
		 */
		public void postChanges() {
			synchronized (this) {
				changesHeld = false;
			}
			EventQueue.invokeLater(this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			// This should only be called through the event queue. It should not be called directly.
			final Object[] trans;
			IVMCallbackServer server;
			synchronized (transactions) {
				if (fServer == null)
					return;	// We are shutdown, don't process.
				if (transactions.isEmpty())
					return; // Nothing to do.
				if (componentToComponentManagers.isEmpty()) {
					// There is nobody listening, so clear and do nothing.
					transactions.clear();
					return;
				}
				trans = transactions.toArray();
				transactions.clear();
				uniquesMap.clear();
				server = fServer;
			}
			try {
				server.doCallback(new ICallbackRunnable() {

					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.CL_TRANSACTIONS, trans);
					}
				});
			} catch (CommandException exp) {
				exp.printStackTrace();
			}
		}
	}


	/**
	 * Add the extension. No affect if already in list.
	 * @param extension
	 * 
	 * @since 1.1.0
	 */
	public void addExtension(ComponentManagerExtension extension) {
		if (extensions == null) {
			extensions = new ComponentManagerExtension[] {extension};
			extension.setComponentManager(this);
		} else {
			for (int i = 0; i < extensions.length; i++) {
				if (extensions[i] == extension)
					return;
			}
			ComponentManagerExtension[] newExtensions = new ComponentManagerExtension[extensions.length+1];
			System.arraycopy(extensions, 0, newExtensions, 0, extensions.length);
			newExtensions[newExtensions.length-1] = extension;
			extensions = newExtensions;
			extension.setComponentManager(this);				
		}
	}
	
	/**
	 * Remove the extension. No affect if not in list.
	 * @param extension
	 * 
	 * @since 1.1.0
	 */
	public void removeExtension(ComponentManagerExtension extension) {
		if (extensions != null) {
			for (int i = 0; i < extensions.length; i++) {
				if (extensions[i] == extension) {
					if (extensions.length > 1) {
						ComponentManagerExtension[] newExtensions = new ComponentManagerExtension[extensions.length-1];
						System.arraycopy(extensions, 0, newExtensions, 0, i);
						int left = newExtensions.length-i;
						if (left > 0)
							System.arraycopy(extensions, i+1, newExtensions, i, left);
						extensions = newExtensions;
					} else
						extensions = null;
					extension.setComponentManager(null);
					return;
				}
			}
		}
	}
	
	/**
	 * Set the component that this manager is managing.
	 * 
	 * @param aComponent
	 * 
	 * @since 1.1.0
	 */
	public void setComponent(Component aComponent, ComponentManagerFeedbackController feedbackController) {
		this.feedbackController = feedbackController;
		if (fComponent != null) {
			feedbackController.deregisterComponentManager(fComponent);
			fComponent.removeComponentListener(this);
			fComponent.removeHierarchyBoundsListener(this);
			fComponent.removeHierarchyListener(this);
			locOverridden = false; // Since changing components we should reset to not overridden.
		}
		Component oldComponent = fComponent;
		fComponent = aComponent;
		if (fComponent != null) {
			feedbackController.registerComponentManager(fComponent, this);
			feedbackController.queueInitialRefresh(this); // Queue up the initial refresh request.
		}
		
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].componentSet(oldComponent, fComponent);
			}
		}
	}
	
	/**
	 * Get the component.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected Component getComponent() {
		return fComponent;
	}
	
	/**
	 * Return the feedback controller. Used by extensions so they can add transactions.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public ComponentManagerFeedbackController getFeedbackController() {
		return feedbackController;
	}

	/**
	 * Called from the client side to invalidate the component. It will also tell the feedback controller about this and the controller will remember
	 * this for it and its parents for marking as an invalid image. The client side will then eventually ask for all invalid images at once.
	 * 
	 * @since 1.1.0
	 */
	public void invalidate() {
		fComponent.invalidate();
		feedbackController.invalidateImage(fComponent);
	}
	
	/**
	 * Notification that the component is invalid.
	 * <p>
	 * This is called by the Feedback controller when it is about to validate all of the queued up windows. This will
	 * only be called for invalidations done through the client call to {@link ComponentManager#invalidate() invalidate}
	 * and to any parents of those components. It will not be called for any component that was invalidated through
	 * some other means.
	 * <p>
	 * The default is to do nothing. Subclasses may do other things. But be aware that immediately after this call
	 * the component will be validated.
	 * 
	 * @since 1.1.0
	 */
	protected void invalidated() {
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].invalidated();
			}
		}
	}
	
	/**
	 * Called by Feedback controller. This says we've received the initial refresh request, so we can now turn on listening so that we don't miss
	 * anything. This way until this point in time we don't send out extra notifications. Notifications between the setting of the component and the
	 * initial refresh are extraneous and should not be sent since the initial refresh groups them all together.
	 * 
	 * @since 1.1.0
	 */
	protected void startComponentListening() {
		fComponent.addComponentListener(this);
		// Add hierarchy listener so we know when it is showing because any bounds changes will not be sent
		// until then. This is different than componentVisible because component can be set to "visible" even though
		// not showing (i.e. the flag is visible, but one of the parents is not). In those case notifications
		// may not necessarily be sent.
		fComponent.addHierarchyListener(this);
		// Add a hierarchy bounds listener so we know when any parent has changed.
		fComponent.addHierarchyBoundsListener(this);
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].startComponentListening();
			}
		}
	}

	/**
	 * This will return the location relative to "awt.Window" parent (such as Frame) (This is the "absolute" location for components).
	 * If this is a awt.Window, then the absolute location will be (0,0).
	 * <p>
	 * This is necessary because sometimes (e.g add to JFrame actually adds to the RootPanel) the parent that we add to is not the the real parent. We
	 * expect the location to be relative to that parent and not the real parent, which we don't know about. So instead we are reporting location
	 * relative to the constant of the top parent. The other side will then use that and the appropriate parent windows location relative to top
	 * parent to place this child window relative to it.
	 * <p>
	 * These coordinates can be compared only if they are from the root window. They don't make sense relative to each other otherwise.
	 * 
	 * @since 1.0.0
	 */
	public Object[] getLocation() {
		if (fComponent != null) {
			int x = 0;
			int y = 0;
			for (Component comp = fComponent; comp != null && !(comp instanceof Window); comp = comp.getParent()) {
				x += comp.getX();
				y += comp.getY();
			}
			return new Object[] { new Integer(x), new Integer(y)};
		} else
			return null;
	}

	/**
	 * This will return the bounds relative to the relative parent. The result is a four element array that is broken down into x,y,width and height.
	 * <p>
	 * The location has the same definition of (x,y) as getLocation().
	 * 
	 * @see ComponentManager#getLocation()
	 * @since 1.0.0
	 */
	public Object[] getBounds() {

		if (fComponent != null) {
			Object[] location = getLocation();
			Dimension size = fComponent.getSize();
			return new Object[] { location[0], location[1], new Integer(size.width), new Integer(size.height)};
		} else
			return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		Component c = e.getComponent();
		feedbackController.addTransaction(this, Common.CL_RESIZED, new Object[] { new Integer(c.getWidth()), new Integer(c.getHeight())}, true);
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].componentResized();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		fireMoved();
	}

	/**
	 * Fire component moved.
	 * 
	 * 
	 * @since 1.1.0
	 */
	protected void fireMoved() {
		feedbackController.addTransaction(this, Common.CL_MOVED, getLocation(), true);
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].componentMoved();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(final ComponentEvent e) {
		feedbackController.addTransaction(this, Common.CL_SHOWN, null, true);
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].componentShown();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(final ComponentEvent e) {
		feedbackController.addTransaction(this, Common.CL_HIDDEN, null, true);
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].componentHidden();
			}
		}
	}

	/**
	 * Get the refresh parms. This is used by both ComponentManager and ComponentManagerFeedbackController to get the refresh parms. This is because
	 * either one can request a refresh but the manager want's to do them all at once.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private Object[] getRefreshParms() {
		return getBounds();
	}

	/**
	 * Queue up onto the AWT event queue a refresh request. This gives the component a chance to layout before the refresh request is sent back. It
	 * can be initiated from the client side and queued up here.
	 * 
	 * @since 1.1.0
	 */
	public void queueRefresh() {
		feedbackController.addTransaction(this, Common.CL_REFRESHED, getRefreshParms(), true);
		// We are queueing up the request BEFORE queueing it up again so that if there are any layout's pending
		// they will be processed before the transaction can be queued up and get the parms at that time
		// instead of now.
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				feedbackController.addTransaction(ComponentManager.this, Common.CL_REFRESHED, getRefreshParms(), true);
			}
		});
		
		if (extensions != null) {
			ComponentManagerExtension[] lcl = extensions;	// In case it changes to a new array while we are walking it.
			for (int i = 0; i < lcl.length; i++) {
				lcl[i].refreshQueued();
			}
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.HierarchyBoundsListener#ancestorMoved(java.awt.event.HierarchyEvent)
	 */
	public void ancestorMoved(HierarchyEvent e) {
		// One of our ancestors moved.
		// So we need to know one of those in-between has changed so that we can say we moved.
		Component cMoved = e.getChanged(); // The component that moved.
		// If the component moved is us, no need to signal because we are listening to ourselves.
		if (cMoved != fComponent) {
			// It wasn't us, so see if it is one that is in-between us and the root window.
			for (Component cntr = fComponent.getParent(); cntr != null && !(cntr instanceof Window); cntr = cntr.getParent()) {
				if (cntr == cMoved) {
					fireMoved();
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.HierarchyBoundsListener#ancestorResized(java.awt.event.HierarchyEvent)
	 */
	public void ancestorResized(HierarchyEvent e) {
		// We don't care if a parent resized.
	}

	
	private static int INTERESTED_HIERARCHYCHANGES = HierarchyEvent.PARENT_CHANGED | HierarchyEvent.SHOWING_CHANGED;
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.HierarchyListener#hierarchyChanged(java.awt.event.HierarchyEvent)
	 */
	public void hierarchyChanged(HierarchyEvent e) {
		if (e.getID() == HierarchyEvent.HIERARCHY_CHANGED && (e.getChangeFlags() & INTERESTED_HIERARCHYCHANGES) != 0 && fComponent.isShowing()) {
			// We now became visible (or one of our parents), we weren't before, so queue a refresh so that IDE can get current
			// bounds since those notifications probably didn't go out. Or we changed parent. That would affect location too.
			queueRefresh();
		}
	}

	// ---------------- Part that handles the default loc when on freeform
	protected Point overPoint; // The last applied location when we are in override mode.

	protected boolean locOverridden;

	/**
	 * Apply bounds. If the loc was overridden, this will not apply bounds, just the size part of it. It will fill the oldRect, if not null, with the
	 * before set value. If it was overridden, then the loc will be the previous override loc.
	 * 
	 * @param rect
	 * @param oldRect
	 *            if not null it will be set with the previous bounds.
	 * 
	 * @since 1.1.0
	 */
	public void applyBounds(Rectangle rect, Rectangle oldRect) {
		if (!locOverridden) {
			if (oldRect != null)
				fComponent.getBounds(oldRect);
			fComponent.setBounds(rect);
		} else {
			if (oldRect != null) {
				oldRect.setLocation(overPoint);
				oldRect.setSize(fComponent.getWidth(), fComponent.getHeight());
			}
			fComponent.setSize(rect.width, rect.height); // Overriding, but we still need to set the size part.
			// We are overriding, so save the rect's location so that it will look like this spot when queried.
			overPoint.x = rect.x;
			overPoint.y = rect.y;
		}
	}

	/**
	 * Apply location, and return previous value. If loc was overridden, this will not apply loc. It will fill the oldPoint, if not null, with the
	 * before set value. If it was overridden, then the loc will be the previous override loc.
	 * 
	 * @param point
	 * @param oldPoint
	 *            if not null it will be set with the previous loc.
	 * 
	 * @since 1.1.0
	 */
	public void applyLocation(Point point, Point oldPoint) {
		if (!locOverridden) {
			if (oldPoint != null)
				fComponent.getLocation(oldPoint);
			fComponent.setLocation(point);
		} else {
			if (oldPoint != null) {
				oldPoint.setLocation(overPoint);
			}
			// We are overriding, so save the point's location so that it will look like this spot when queried.
			overPoint.x = point.x;
			overPoint.y = point.y;
		}
	}

	/**
	 * Force an override of loc so that it will go to specified loc but it will get the current loc and store it as the overridden value for later
	 * retrieval.
	 * 
	 * @param x
	 * @param y
	 * 
	 * @since 1.1.0
	 */
	public void overrideLoc(int x, int y) {
		if (!locOverridden) {
			overPoint = fComponent.getLocation(overPoint);
			locOverridden = true;
		}
		fComponent.setLocation(x, y);
	}

	/**
	 * Remove the override of the loc.
	 * 
	 * @since 1.1.0
	 */
	public void removeOverrideLoc() {
		if (locOverridden) {
			locOverridden = false;
			if (overPoint != null)
				fComponent.setLocation(overPoint);
		}
	}

	/**
	 * Query the default loc. Which is the current loc if not override, or the last set loc if override applied.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getDefaultLocation() {
		if (locOverridden)
			return overPoint;
		else
			return fComponent.getLocation();
	}

	/**
	 * Query the default bounds. Which is the current bounds if not override, or the last set loc with the current size if override applied.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Rectangle getDefaultBounds() {
		if (locOverridden)
			return new Rectangle(overPoint.x, overPoint.y, fComponent.getWidth(), fComponent.getHeight());
		else
			return fComponent.getBounds();
	}

}