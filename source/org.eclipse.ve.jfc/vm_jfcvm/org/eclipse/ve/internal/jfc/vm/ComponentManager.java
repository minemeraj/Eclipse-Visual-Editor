package org.eclipse.ve.internal.jfc.vm;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ComponentManager.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:35 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import org.eclipse.ve.internal.jfc.common.Common;
import org.eclipse.jem.internal.proxy.common.*;
/**
 * This is the ComponentManager used by the JCF.
 * It it is a listener for the component changes and
 * it provides helper routines to quickly
 * access information.
 */
public class ComponentManager implements ICallback, ComponentListener, HierarchyBoundsListener, HierarchyListener {

	protected IVMServer fServer;
	protected int fCallbackID;
	protected Component fComponent;
	protected Container fParent;

	/**
	 * The listener initialize for callback server.
	 */
	public void initializeCallback(IVMServer server, int callbackID){
		fServer = server;
		fCallbackID = callbackID;
	}
	
	public void setComponent(Component aComponent) {
		if (fComponent != null) {
			fComponent.removeComponentListener(this);
			fComponent.removeHierarchyBoundsListener(this);
			fComponent.removeHierarchyListener(this);
		}
		fComponent = aComponent;
		fParent = null;
		if (fComponent != null) {
			fComponent.addComponentListener(this);
			// Add hierarchy listener so we know when it is showing because any bounds changes will not be sent
			// until then. This is different than componentVisible because component can be set to "visible" even though
			// not showing (i.e. the flag is visible, but one of the parents is not). In those case notifications
			// may not necessarily be sent.			
			fComponent.addHierarchyListener(this);
			// Queue up a refresh to get the bounds, even if not yet showing, get something at least.
			queueRefresh();	
		}
	}
	
	public void setRelativeParentComponent(Container aContainer) {
		fComponent.removeHierarchyBoundsListener(this);
		fParent = aContainer;
		
		// Only add if container is not our direct parent. We don't care about ancestor movements if it is our direct parent.
		if (aContainer != null && aContainer != fComponent.getParent())
			fComponent.addHierarchyBoundsListener(this);		
		queueRefresh();	// So now we can get bounds relative to our relative parent.
	}
	
	/**
	 * This will return the location relative to the relative parent.
	 * This is necessary because sometimes (e.g add to JFrame actually
	 * adds to the RootPanel) the parent that we add to is not the
	 * the real parent. We expect the location to be relative to that
	 * parent and not the real parent, which we don't know about.
	 */
	public Object[] getLocation() {
		int x = 0;
		int y = 0;
		Component c = fComponent;
		x = c.getX();
		y = c.getY();
		if (fParent != null) {
			for (Container cntr = c.getParent(); cntr != null && cntr != fParent; cntr = cntr.getParent()) {
				x += cntr.getX();
				y += cntr.getY();
			}
		}		
		return new Object[] {new Integer(x), new Integer(y)};
	}
	
	/**
	 * java.awt.event.ComponentListener interface
	 */
	 
	/**
	 * Invoked when the component's size changes.
	 */
	public void componentResized(final ComponentEvent e) {
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						Component c = e.getComponent();
						return handler.callbackWithParms(fCallbackID, Common.CL_RESIZED, new Object[] {new Integer(c.getWidth()), new Integer(c.getHeight())});						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}
	
	/**
	 * Invoked when the component's position changes.
	 */
	public void componentMoved(final ComponentEvent e) {
		fireMoved();
	}

	protected void fireMoved() {
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.CL_MOVED, getLocation());						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}
	
	/**
	 * Invoked when the component has been made visible.
	 */
	public void componentShown(final ComponentEvent e) {
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.CL_SHOWN, null);						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}		
	
	/**
	 * Invoked when the component has been made invisible.
	 */
	public void componentHidden(final ComponentEvent e) {
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.CL_HIDDEN, null);						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}
	
	/**
	 * Queue up onto the AWT event queue a refresh request. This gives the
	 * component a chance to layout before the refresh request is sent back.
	 * It is initiated from the client side and queued up here.
	 */
	public void queueRefresh() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if (fServer != null) {
					try {
						fServer.doCallback(new ICallbackRunnable() {
							public Object run(ICallbackHandler handler) throws CommandException {
								return handler.callbackWithParms(fCallbackID, Common.CL_REFRESHED, null);						
							}
						});
					} catch (CommandException exp) {
					}
				}
			}
		});
	}
	
	/**
	 * java.awt.event.HierarchyBoundsListener interface
	 */

	/**
	 * @see java.awt.event.HierarchyBoundsListener#ancestorMoved(HierarchyEvent)
	 */
	public void ancestorMoved(HierarchyEvent e) {
		// One of our ancestors moved, and we are listening Which means are relative parent component is not our direct parent,
		// so we need to know one of those in-between has changed so that we can say we moved. However, it could of been the
		// parent component itself, in which case we don't care because he would also being listened to directly.
		// Any above the parent should also be ignored because they are handled by other listeners.
		Component cMoved = e.getChanged();	// The component that moved.
		// If the component moved is us, no need to signal because we are listening to ourselves.
		if (cMoved != fComponent) {
			// It wasn't us, so see if it is one that is in-between us and the parent.
			for (Container cntr = fComponent.getParent(); cntr != null && cntr != fParent; cntr = cntr.getParent()) {
				if (cntr == cMoved) {										
					fireMoved();
					break;
				}
			}
		}
	}

	/**
	 * @see java.awt.event.HierarchyBoundsListener#ancestorResized(HierarchyEvent)
	 */
	public void ancestorResized(HierarchyEvent e) {
		// We don't care if a parent resized.
	}
	
	/**
	 * java.awt.event.HierarchyListener interface
	 */	

	/**
	 * @see java.awt.event.HierarchyListener#hierarchyChanged(HierarchyEvent)
	 */
	public void hierarchyChanged(HierarchyEvent e) {		
		if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && fComponent.isShowing()) {
			// We now became visible (or one of our parents), we weren't before, so queue a refresh so that IDE can get current
			// bounds since those notifications probably didn't go out.					
			queueRefresh();
		}
	}

}