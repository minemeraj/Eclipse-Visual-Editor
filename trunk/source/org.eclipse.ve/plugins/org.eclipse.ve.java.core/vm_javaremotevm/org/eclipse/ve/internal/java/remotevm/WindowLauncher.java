package org.eclipse.ve.internal.java.remotevm;
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
 *  $RCSfile: WindowLauncher.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;

import org.eclipse.ve.internal.java.common.Common;
import org.eclipse.jem.internal.proxy.common.*;
import org.eclipse.jem.internal.proxy.common.ICallback;
import org.eclipse.jem.internal.proxy.common.IVMServer;


public class WindowLauncher implements ICallback {
	
	protected IVMServer fServer;
	protected int fCallbackID;	
	Component fComponent;
	IBeanPropertyEditorDialog fDialog;
	Window fWindow;
	java.util.List fWindowListeners = new ArrayList(1);
	int windowState = Common.WIN_OPENED;
	
public WindowLauncher(Component aComponent){
	fComponent = aComponent;
	launchEditor();
	listenToComponent();
}
/**
 * The listener initialize for callback server.
 */
public void initializeCallback(IVMServer server, int callbackID){
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
	} else if ( fComponent instanceof Component ) { 
		fDialog = new BeanPropertyEditorFrame();
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
		}
	});
	windowState = Common.WIN_OPENED;
}

void listenToComponent(){
	fWindow.addWindowListener(new WindowAdapter(){
		public void windowClosed(WindowEvent event){
			Iterator iter = fWindowListeners.iterator();
			while(iter.hasNext()){
				((WindowListener)iter.next()).windowClosed();
			}
			callbackWindowClosed();
		}
	});
	fDialog.addListener(new IPropertyEditorDialogListener(){
		public void revertPropertyValue(){callbackRevertValue();}
		public void savePropertyValue(){callbackSaveValue();}
	});
	
	// Add ourself as a listener to the component
	// This is required for customizers that use this as a means to signal the they may
	// need repainting of the bean
	fComponent.addPropertyChangeListener(
		new PropertyChangeListener(){
			public void propertyChange(final PropertyChangeEvent evt){
				try {
					fServer.doCallback(new ICallbackRunnable() {
						public Object run(ICallbackHandler handler) throws CommandException {
							return handler.callbackWithParms(fCallbackID, Common.PROP_CHANGED, new Object[] {evt.getPropertyName()});
						}
					});
				} catch (CommandException exp) {
				}				
			}
		}
	);	
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