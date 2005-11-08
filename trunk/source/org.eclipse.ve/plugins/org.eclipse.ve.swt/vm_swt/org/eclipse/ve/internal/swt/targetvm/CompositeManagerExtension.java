/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositeManagerExtension.java,v $
 *  $Revision: 1.6 $  $Date: 2005-11-08 22:33:17 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.swt.common.Common;
import org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerExtension;
import org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerFeedbackController;
 

/**
 * Composite manager extension for handling the composite.
 * @since 1.1.0
 */
public class CompositeManagerExtension extends ControlManagerExtension {
	
	protected static LayoutDataTypeCallback layoutDataTypeCallback;
	
	/**
	 * Callback for handling the request of valid layout data per layout type. There
	 * is only one. It is constructed from the IDE side.
	 * 
	 * @since 1.1.0
	 */
	public static class LayoutDataTypeCallback implements ICallback {

		private IVMCallbackServer vmServer;
		private int callbackID;

		public void initializeCallback(IVMCallbackServer vmServer, int callbackID) {
			this.vmServer = vmServer;
			this.callbackID = callbackID;
			layoutDataTypeCallback = this;
		}
		
		private Map layoutToDataType;
		
		/**
		 * Get the valid layout data type from the given layout type.
		 * 
		 * @param layout layout type. Do not call with <code>null</code>
		 * @return the valid class or <code>null</code> if unknown.
		 * 
		 * @since 1.1.0
		 */
		public Class getLayoutDataType(final Class layout) {
			if (layoutToDataType != null) {
				if (layoutToDataType.containsKey(layout))
					return (Class) layoutToDataType.get(layout);
			} else {
				layoutToDataType = new HashMap();
			}

			Class dataType;
			try {
				dataType = (Class) vmServer.doCallback(new ICallbackRunnable() {
				
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(callbackID, 0, new Object[] {layout});
					}
				
				});
			} catch (CommandException e) {
				e.printStackTrace();
				dataType = null;
			}
			layoutToDataType.put(layout, dataType);
			return dataType;
		}
		
	}
	/**
	 * Flag indicating layoutData needs to be checked on the about to validate
	 * so that no errors are produced on layout. And the bad layoutdata errors
	 * can be sent back to the IDE.
	 */
	protected boolean layoutDataCheckRequired = true;
	
	
	/**
	 * Called by the client to indicate the check of layout data against the
	 * layout type required.
	 * 
	 * @since 1.1.0
	 */
	public void setVerifyLayoutData() {
		layoutDataCheckRequired = true;
	}
	
	protected void aboutToValidate() {
		if (layoutDataCheckRequired) {
			layoutDataCheckRequired = false;
			Composite parent = (Composite) getControl();
			Layout layout = parent.getLayout();
			// If layout is null, then no check needs to be made.
			if (layout != null) {
				Class layoutDataType = layoutDataTypeCallback.getLayoutDataType(layout.getClass());
				List invalidChildren = new ArrayList();
				Control[] children = parent.getChildren();
				for (int i = 0; i < children.length; i++) {
					Control child = children[i];
					// Restore to what it should be, either the explicit setting or the default one at construction.
					Object data = ControlManager.getStoredLayoutData(child);	// Use what it thinks it should be.
					if (data != ControlManager.LAYOUT_DATA_NOTSTORED) {
						// We have tested once the data, so we will restore it.
						// Restore it to test it. It may of been changed to a good value from a previous pass through this code, we want to retest to make sure it is still bad.
						child.setLayoutData(data);
					} else {
						// We have never queried this value. Probably came from a subclass of Composite that had some already defined.
						// So grab the current value and use it as the initial data like we had done.
						ControlManager.storeLayoutData(child, data = child.getLayoutData());
					}
					// layoutDataType null means we don't know the type, so no test is done, data == null is considered to be always good. So far that seems to be true.
					if (layoutDataType != null && data != null) {
						// It should of been null (Void.TYPE - isInstance is always false against that) or it is not an instance of the type.
						if (!layoutDataType.isInstance(data)) {
							// This is invalid.
							child.setLayoutData(null);		// We set to null. Hoping all layout editors can handle null. They will probably put a default value into the layout data.
							invalidChildren.add(child);
							invalidChildren.add(data.getClass().getName());
						}
					}
				}
				// Signal even if all good so that it knows that all is good.
				if (!invalidChildren.isEmpty()) {
					invalidChildren.add(0, layoutDataType.getName());	// Want the data type first so we can produce a nice message on the host.
					getControlManager().getFeedbackController().addTransaction(this, Common.CMPL_INVALID_LAYOUT, invalidChildren.toArray(), false);
				} else
					getControlManager().getFeedbackController().addTransaction(this, Common.CMPL_INVALID_LAYOUT, null, false);	// Tell it all are valid.
			}
		}
		super.aboutToValidate();
	}
	
	protected Integer[] clientAreaInfo;
	
	/**
	 * This will handle seeing if origin offset has changed. If it has it will send back new offset and firemoved to all of the children
	 * to indicate they have moved their absolute location. If fireMoved is true on entry, then whether the offset has changed or not
	 * the children will all be told they were moved. This is so that offset can be handled if move is still required.
	 * 
	 * @param fireMoved
	 * 
	 * @since 1.2.0
	 */
	protected void handleOriginOffset(boolean fireMoved) {
		// Get the offset between the upper-left corner of the control and the origin (0,0) of the control.
		// For most controls this is (0,0). But for Shell it is not, because (0,0) on the shell actually puts
		// you down and to the right. Need to know this offset to make appropriate coordinate calculations on
		// the GraphViewer. The offset will be in the orientation of the control. For example if Right-to-Left,
		// then visually it is from the upper right, but logically it is still upper-left.

		Composite parent = getControl().getParent();
		Control control = getControl();
		ControlManagerFeedbackController feedbackController = getControlManager().getFeedbackController();
		Rectangle controlBounds = feedbackController.getDisplay().map(parent, control, control.getBounds());
		// Flip from corner-offset-from-origin to origin-offset-from-corner by negating.
		controlBounds.x = -controlBounds.x;
		controlBounds.y = -controlBounds.y;	
		Rectangle clientArea = ((Scrollable) control).getClientArea();
		if (clientAreaInfo == null || clientAreaInfo[0].intValue() != controlBounds.x || clientAreaInfo[1].intValue() != controlBounds.y ||
				clientAreaInfo[2].intValue() != clientArea.x || clientAreaInfo[3].intValue() != clientArea.y || clientAreaInfo[4].intValue() != clientArea.width || clientAreaInfo[5].intValue() != clientArea.height) {
			fireMoved = true;	// We will fire no matter what.
			if (clientAreaInfo == null)
				clientAreaInfo = new Integer[6];
			clientAreaInfo[0] = new Integer(controlBounds.x);
			clientAreaInfo[1] = new Integer(controlBounds.y);
			clientAreaInfo[2] = new Integer(clientArea.x);
			clientAreaInfo[3] = new Integer(clientArea.y);
			clientAreaInfo[4] = new Integer(clientArea.width);
			clientAreaInfo[5] = new Integer(clientArea.height);
			feedbackController.addTransaction(this, Common.CMPL_CLIENTAREA_CHANGED, clientAreaInfo, true);
		}
		
		if (fireMoved) {
			// Fire moved on all children managers.
			Control[] children = ((Composite) control).getChildren();
			for (int i = 0; i < children.length; i++) {
				ControlManager cm = feedbackController.getControlManager(children[i]);
				if (cm != null)
					cm.fireMoved();	// This is necessary because if the origin offset changed, the absolute location of the children will be different.
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerExtension#hasBeenValidated()
	 */
	protected void hasBeenValidated() {
		super.hasBeenValidated();
		handleOriginOffset(false);	// Handle the offset, but only fire moved if it changed.
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerExtension#controlResized()
	 */
	protected void controlResized() {
		super.controlResized();
		int style = getControl().getStyle();
		// Handle the origin offset. It may change due to a resize (such as a shell with a menubar and it was made smaller. This would cause the
		// menubar to flow to a new line and that would change the origin offset).
		// However, if the style right to left, no matter whether the origin offset changed or not, we still need to fire move to all children
		// because a resize of the parent will cause them to be at a different absolute location, even though they still have the same bounds.
		handleOriginOffset((style & SWT.RIGHT_TO_LEFT) != 0);
	}
}
