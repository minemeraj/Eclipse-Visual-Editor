package org.eclipse.ve.internal.jfc.vm;

import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSplitPane;

import org.eclipse.ve.internal.jfc.common.Common;
import org.eclipse.jem.internal.proxy.common.*;

/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JSplitPaneManager.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:44:12 $ 
 */
 
/**
 * This class manages the splitpane.
 * @author richkulp
 */
public class JSplitPaneManager implements ICallback {
	
	protected IVMServer fServer;
	protected int fCallbackID;	
	protected JSplitPane splitpane;
	protected static final int DIVIDER_NOT_SET = Integer.MIN_VALUE;
	protected int setDividerLocation = DIVIDER_NOT_SET;
	protected PropertyChangeListener dividerListener = new PropertyChangeListener() {
		boolean imSetting = false;	// Am I in the process of setting it here in the property change listener.
		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			boolean reallyIamSetting = false;
			synchronized (dividerListener) {
				reallyIamSetting = imSetting;
			}
			// If I'm (the propertyChange listener) setting it, I don't care to listen for that particular change, because
			// it either accepted or it didn't. If it accepted it, then it would match, and if it didn't accept it, it wouldn't
			// match. But if it didn't accept it, I don't want to go ahead and try to set it back because it would fail again and again.
			// A case of not being accepted is if divider location is outside the max/min divider location. The splitpane will always
			// reset it back.
			if (!reallyIamSetting && ((Integer) evt.getNewValue()).intValue() != setDividerLocation) {
				// It got changed to something else other than us explicitly setting it. This
				// occurs due to resizes, or any relayout. We want to maintain our set size.
				// Queue it off. We can't do it here because we can get into a deadlock
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						synchronized (dividerListener) {
							imSetting = true;
						}
						try {
							splitpane.setDividerLocation(setDividerLocation);
							splitpane.revalidate();
							invalidateImage();	// Now tell it to re-get the image. This is because sometimes these changes happen after the image has already been captured.
						} finally {
							synchronized (dividerListener) {
								imSetting = false;
							}
						}
					}
				});
			}
		}
	};
	
	protected ComponentListener componentListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			if (setDividerLocation == DIVIDER_NOT_SET) {
				resetToPreferredSizes();
				invalidateImage();	// We've got a new size with no divider. So we need to reset to the new size and get a new image.
			}
		}	
	};
	
	/**
	 * The listener initialize for callback server.
	 */
	public void initializeCallback(IVMServer server, int callbackID){
		fServer = server;
		fCallbackID = callbackID;
	}	
		
	public void setSplitPane(JSplitPane splitpane) {
		if (this.splitpane != null) {
			this.splitpane.removePropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
			this.splitpane.removeComponentListener(componentListener);
		}
			
		this.splitpane = splitpane;
		if (this.splitpane != null)
			this.splitpane.addComponentListener(componentListener);		
	}
	
	public void setDividerLocation(int dividerLocation) {
		if (setDividerLocation != DIVIDER_NOT_SET && dividerLocation == -1) {
			// We're setting to default, and didn't had it set before, so remove our listener.
			splitpane.removePropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
		} else if (setDividerLocation == DIVIDER_NOT_SET && dividerLocation != -1) {
			// We're setting to something explicit, and we weren't set before, so add our listener
			splitpane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
		}
		
		if (dividerLocation == -1)
			setDividerLocation = DIVIDER_NOT_SET;
		else
			setDividerLocation = dividerLocation;
		
		splitpane.setDividerLocation(dividerLocation);
	}
	
	public void resetToPreferredSizes() {
		splitpane.resetToPreferredSizes();
	}
	
	/**
	 * Send back a invalidate image notification so that IDE can ask for a new image.
	 */
	public void invalidateImage() {
		if (fServer != null) {	
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, Common.JSP_INVALIDATE, null);						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}	
	
}
