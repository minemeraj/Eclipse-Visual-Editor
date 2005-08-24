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
 *  $RCSfile: JSplitPaneManagerExtension.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:38:13 $ 
 */
package org.eclipse.ve.internal.jfc.vm;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JSplitPane;


 
/**
 * JSplitPane manager extension.
 * 
 * @since 1.1.0
 */
public class JSplitPaneManagerExtension extends ComponentManager.ComponentManagerExtension {
	
	protected static final int DIVIDER_NOT_SET = Integer.MIN_VALUE;
	protected int setDividerLocation = DIVIDER_NOT_SET;
	
	/**
	 * Get the component as a split pane.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected JSplitPane getSplitPane() {
		return (JSplitPane) getComponent();
	}
	
	protected PropertyChangeListener dividerListener = new PropertyChangeListener() {
		boolean imSetting = false;	// Am I in the process of setting it here in the property change listener.
		/*
		 * 
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			boolean reallyIamSetting;
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
				synchronized (dividerListener) {
					imSetting = true;
				}
				try {
					getSplitPane().setDividerLocation(setDividerLocation);
					// Note: The following may or may not be necessary in the new component manager scheme. Anything that would cause
					// the divider location to change, such as actual setting or it, or a repaint due to a resize, should still be within
					// a transaction, and such will get the correct image. But to be safe we will invalidate here.
					getComponentManager().invalidate();	// Now tell it to re-get the image. This is because sometimes these changes happen after the image has already been captured.
				} finally {
					synchronized (dividerListener) {
						imSetting = false;
					}
				}
			}
		}
	};

	protected void componentResized() {
		if (setDividerLocation == DIVIDER_NOT_SET) {
			getSplitPane().resetToPreferredSizes();
			getComponentManager().invalidate();	// We've got a new size with no divider. So we need to reset to the preferred sizes for the new size and schedule an image.
		}
	}	

	protected void componentSet(Component oldComponent, Component newComponent) {
		if (oldComponent != null) {
			oldComponent.removePropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
		}
	}
	
	/**
	 * Set the divider location.
	 * @param dividerLocation
	 * @return return previous location
	 * @since 1.1.0
	 */
	public int setDividerLocation(int dividerLocation) {
		int oldLoc = getSplitPane().getDividerLocation();
		if (setDividerLocation != DIVIDER_NOT_SET && dividerLocation == -1) {
			// We're setting to default, and didn't had it set before, so remove our listener.
			getSplitPane().removePropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
		} else if (setDividerLocation == DIVIDER_NOT_SET && dividerLocation != -1) {
			// We're setting to something explicit, and we weren't set before, so add our listener
			getSplitPane().addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, dividerListener);
		}
		
		if (dividerLocation == -1)
			setDividerLocation = DIVIDER_NOT_SET;
		else
			setDividerLocation = dividerLocation;
		
		getSplitPane().setDividerLocation(dividerLocation);
		return oldLoc;
	}
	
	
	protected void invalidated() {
		// It was invalidated. (Either explicitly or through a child). Do we have the divider not set? If so, then we should do reset to preferred size before validating.
		if (setDividerLocation == DIVIDER_NOT_SET)
			getSplitPane().resetToPreferredSizes();
	}	
}
