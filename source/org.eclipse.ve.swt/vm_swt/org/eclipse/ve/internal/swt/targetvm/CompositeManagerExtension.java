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
 *  $Revision: 1.3 $  $Date: 2005-07-08 17:51:50 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.util.*;
import java.util.List;

import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.swt.common.Common;
import org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerExtension;
 

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
				// If layout data type is null, then we don't know the type. So we leave alone and hope for the best.
				if (layoutDataType != null) {
					List invalidChildren = new ArrayList();
					Control[] children = parent.getChildren();
					for (int i = 0; i < children.length; i++) {
						Control child = children[i];
						// Restore to what it should be, either the explicit setting or the default one at construction.
						Object data = child.getData(ControlManager.LAYOUT_DATA_KEY);	// Use what it thinks it should be.
						child.setLayoutData(data);	// Restore it to test it. It may of been changed to a good value from a previous pass through this code, we want to retest to make sure it is still bad.
						if (data != null) {
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
				} else
					getControlManager().getFeedbackController().addTransaction(this, Common.CMPL_INVALID_LAYOUT, null, false);	// Tell it all are valid.
			}
		}
		super.aboutToValidate();
	}
}
