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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JDesktopPaneGraphicalEditPart.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

public class JDesktopPaneGraphicalEditPart extends JLayeredPaneGraphicalEditPart {

	protected EditPart fSelectedFrame;
	private EditPartListener frameListener;
	protected JDesktopPaneProxyAdapter desktopPaneAdapter;

	public JDesktopPaneGraphicalEditPart(Object model) {
		super(model);
	}
	public void activate() {
		super.activate();
		setListener(createFrameListener());
	}
	
	protected void addFrameListenerToChildren(EditPart ep) {
		ep.addEditPartListener(frameListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			addFrameListenerToChildren((EditPart) childen.next());
	}
	
	public void deactivate() {
		setListener(null);
		super.deactivate();
	}
	
	protected void removeFrameListenerFromChildren(EditPart ep) {
		ep.removeEditPartListener(frameListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			removeFrameListenerFromChildren((EditPart) childen.next());
	}
	/*
	 * Create an EditPartListener for itself and its JInternalFrames so it knows 
	 * when a frame has been selected.
	 */
	protected EditPartListener createFrameListener() {
		return new EditPartListener.Stub() {
			public void childAdded(EditPart editpart, int index) {
				addFrameListenerToChildren(editpart);
			}
			public void removingChild(EditPart editpart, int index) {
				removeFrameListenerFromChildren(editpart);
			}
			public void selectedStateChanged(EditPart editpart) {
				if (editpart == null || editpart == JDesktopPaneGraphicalEditPart.this)
					return;
				// Find the JInternalFrame where this editpart resides and select the frame
				// if isn't already.
				if ((editpart != null)
					&& (editpart.getSelected() == EditPart.SELECTED
						|| editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
					EditPart frame = getFrameOfSelectedEditpart(editpart);
					if (frame != null && frame != fSelectedFrame)
						selectFrame(frame);
				}
			}
		};
	}

	/*
	 * If the parent of this editpart is the JDesktopPane, we're on the JInternalFrame.
	 * If not recursely call up through the parent chain until we find the 
	 * editpart (JInternalFrame) that the original editpart was found in.
	 */
	protected EditPart getFrameOfSelectedEditpart(EditPart ep) {
		if (ep == null || ep.getParent() == this)
			return ep;
		return getFrameOfSelectedEditpart(ep.getParent());
	}
	
	/*
	 * Return the proxy adapter associated with this JTabbedPane.
	 */
	protected JDesktopPaneProxyAdapter getJDesktopPaneProxyAdapter() {
		if (desktopPaneAdapter == null) {
			IBeanProxyHost desktopPaneProxyHost =
				BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getModel());
			desktopPaneAdapter = (JDesktopPaneProxyAdapter) desktopPaneProxyHost;
		}
		return desktopPaneAdapter;
	}
	
	/*
	 * The selected JInternalFrame of the JDesktopPane has changed. 
	 */
	protected void selectFrame(EditPart frame) {
		if (frame != fSelectedFrame) {
			fSelectedFrame = frame;
			getJDesktopPaneProxyAdapter().activateFrame((IJavaObjectInstance) frame.getModel());
			refreshChildren();	// Need to refresh the children because we've changed order.
		}
	}
	
	protected void setListener(EditPartListener listener) {
		if (this.frameListener != null)
			removeFrameListenerFromChildren(this);
		this.frameListener = listener;
		if (this.frameListener != null)
			addFrameListenerToChildren(this);
	}
}
