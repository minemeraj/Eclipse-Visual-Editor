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
package org.eclipse.ve.internal.java.remotevm;
/*
 *  $RCSfile: WindowLauncher.java,v $
 *  $Revision: 1.10 $  $Date: 2005-12-13 01:05:41 $ 
 */

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;

import org.eclipse.jem.internal.proxy.common.*;

import org.eclipse.ve.internal.java.common.Common;
import org.eclipse.ve.internal.java.remotevm.macosx.ProcessManager;


public class WindowLauncher implements ICallback {
	
	protected IVMCallbackServer fServer;
	protected int fCallbackID;	
	Component fComponent;
	IBeanPropertyEditorDialog fDialog;
	Window fWindow;
	java.util.List fWindowListeners = new ArrayList(1);
	int windowState = Common.WIN_OPENED;
	private boolean explicitPropertyChange = false;
	
	private static final boolean isMacOSX = System.getProperty("os.name").equalsIgnoreCase("Mac OS X");
	
public WindowLauncher(Component aComponent){
	fComponent = aComponent;
	launchEditor();
	listenToComponent();
}
public WindowLauncher(Component aComponent, boolean aBoolean){
	explicitPropertyChange = aBoolean;
	fComponent = aComponent;
	launchEditor();
	listenToComponent();	
}
/**
 * The listener initialize for callback server.
 */
public void initializeCallback(IVMCallbackServer server, int callbackID){
	fServer = server;
	fCallbackID = callbackID;
}
/**
 * Calls the IDE-side WindowLauncher that Cancel was pressed
 */
protected void callbackRevertValue(){
	windowState = Common.DLG_CANCEL;
}

/**
 * Calls the IDE-side WindowLauncher that OK was pressed
 */
protected void callbackSaveValue(){
	windowState = Common.DLG_OK;
}

/**
 * Calls the IDE-side WindowLauncher that window was closed
 */
protected void callbackWindowClosed(){	
	// Prevents race conditions, where OK was pressed, and window was closed later.
	if(windowState == Common.WIN_OPENED) {
		windowState = Common.WIN_CLOSED;
		fDialog.setPropertyEditor(null);	// To release the component
		fWindow = null;
		fDialog = null;
	}
	if (isMacOSX) {
		ProcessManager.processToBack();
	}
}

public int getWindowState(){
	return windowState;
}

/**
 * Test to see whether the component is AWT or Swing.  
 * this determines what kind of dialog we will use to host the component
 * Component itself is the superclass of Frame, JFrame, Dialog, JDialog, Window and JWindow
 * All of these need special coding by us as they are shells and different launching
 * This will be coded later - for now just worry about non-shell components
 */
void launchEditor(){
	if ( fComponent instanceof JComponent) {
		fDialog = new BeanPropertyEditorJFrame();
	} else { 
		fDialog = new BeanPropertyEditorFrame();
	}
	if(!explicitPropertyChange){
		fDialog.decorateWithButtons(true);
	}
	fDialog.setPropertyEditor(fComponent);
	fWindow = (Window)fDialog;
	fWindow.pack();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	fWindow.setLocation( 
		(screenSize.width - fWindow.getWidth()) /2,
		(screenSize.height - fWindow.getHeight()) / 2);
	fWindow.setVisible(true);
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			toFront();
			if (isMacOSX) {
				ProcessManager.processToFront();
			}
		}
	});
	windowState = Common.WIN_OPENED;
}

void listenToComponent() {
		fWindow.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {
				Iterator iter = fWindowListeners.iterator();
				while (iter.hasNext()) {
					((WindowListener) iter.next()).windowClosed();
				}
				callbackWindowClosed();
			}
		});
		fDialog.addListener(new IPropertyEditorDialogListener() {
			public void revertPropertyValue() {
				callbackRevertValue();
			}
			public void savePropertyValue() {
				callbackSaveValue();
			}
		});

		// Because of problem with timing we can get property change events even as we are being
		// disposed.
		// So to get around that we will listen only when it is actually added as a child. If not
		// parented, we won't listen.
		fComponent.addHierarchyListener(new HierarchyListener() {

			// Add ourself as a listener to the component
			// This is required for customizers that use this as a means to signal the they may
			// need repainting of the bean
			PropertyChangeListener pcl = new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					try {
						fServer.doCallback(new ICallbackRunnable() {
							public Object run(ICallbackHandler handler) throws CommandException {
								return handler.callbackWithParms(fCallbackID, Common.PROP_CHANGED, new Object[] { evt.getPropertyName()});
							}
						});
					} catch (CommandException exp) {
					}
				}
			};

			boolean hasParent;
			{
				hasParent = fComponent.getParent() != null;
				if (hasParent)
					fComponent.addPropertyChangeListener(pcl);
			}

			// KLUDGE We're listening for displayability change because that is the only thing that
			// occurs
			// in the addNotify/removeNotify. And we need to do it then because if we don't, the
			// JComponent removeNotify goes
			// on and signals out propertyChange event, but it happens in the middle of the
			// removeNotify, which causes a lockup
			// because we callback to the IDE, which then comes back on a different thread to
			// invalidate. The problem is since we
			// are in the middle of a remove, we've locked the AWT treelock, and the invalidate
			// also tries to lock the treelock. So
			// we get a deadlock situation. By removing the property listener during the
			// removenotify we won't get the propertychange event
			// sent.
			public void hierarchyChanged(HierarchyEvent e) {
				if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
					hasParent = !hasParent;
					if (hasParent)
						fComponent.addPropertyChangeListener(pcl);
					else
						fComponent.removePropertyChangeListener(pcl);
				}
			}
		});
	}

public void toFront(){
	fWindow.toFront();
	fWindow.requestFocus();
}
public void show(){
	fWindow.setVisible(true);
}
public void hide(){
	fWindow.setVisible(false);
}
public boolean isVisible(){
	return fWindow.isVisible();
}
public void addListener(WindowListener aListener){
	fWindowListeners.add(aListener);
}
public void removeListener(WindowListener aListener){
	fWindowListeners.remove(aListener);
}
}
