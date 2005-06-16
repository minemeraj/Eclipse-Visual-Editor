package org.eclipse.ve.internal.jfc.vm;
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
 *  $RCSfile: FreeFormSwingDialog.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
/**
 * Dialog to host Freefrom Swing components
 * Creation date: (12/01/00 4:14:56 PM)
 * @author: Joe Winchester
 */
public class FreeFormSwingDialog extends JDialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -1700965019233269959L;
	protected boolean isValidating;
	protected Dimension minSize;
	protected boolean isDisposing = false;
	
	protected static FreeFormSwingDialog SWING_DIALOG;
	
	/**
	 * Get the one swing dialog in the jvm.
	 * @param x
	 * @param y
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static FreeFormSwingDialog getFreeFormDialog(int x, int y) {
		// Note: We have only one for the entire jvm's life. First this is more efficient.
		// Everytime we did a reload from scratch we used to dispose of the dialog and then
		// re-get it when needed. But this actually showed up a problem. On some jvm
		// implementations if you have two dialogs open, the close
		// of the second dialog causes a problem. This way we only have one open.
		if (SWING_DIALOG == null)
			SWING_DIALOG = new FreeFormSwingDialog(x,y);
		else
			SWING_DIALOG.setLocation(x, y);
		return SWING_DIALOG;
	}
	
/**
 * FreeFormDialog constructor comment.
 */
public FreeFormSwingDialog(int x, int y) {
	super();
	getContentPane().setLayout(new FlowLayout(0,0,0));
	setLocation(x,y);
	setTitle(VisualVMMessages.getString("FreeForm.Dialog.Title")); //$NON-NLS-1$
	setVisible(true);
	
	// We need to get a true min size before any components have been added.
	addComponentListener(new ComponentAdapter() {
		/**
		 * @see java.awt.event.ComponentAdapter#componentResized(ComponentEvent)
		 */
		public void componentResized(ComponentEvent e) {
			removeComponentListener(this);
			minSize = getSize();	// This is true min size now.
		}
		
	});
}
/**
 * FreeFormDialog constructor comment.
 * @param parent java.awt.Frame
 */
public FreeFormSwingDialog(java.awt.Frame parent) {
	super(parent);
}
/**
 * When a component is added to the free form shell it is always placed inside a special container
 * This container ensures that each component occupies its specified size
 */
public Component add(Component aComponent){
	FreeFormSwingContainer aContainer = new FreeFormSwingContainer();
	aContainer.add(aComponent);
	getContentPane().add(aContainer);
	pack();
	return aComponent;
}

protected FreeFormSwingContainer findContainer(Component aComponent) {
	Container contentPane = getContentPane();
	for (int i = contentPane.getComponentCount()-1; i >= 0 ; i--){
		FreeFormSwingContainer freeFormContainer = (FreeFormSwingContainer) contentPane.getComponent(i);
		if ( freeFormContainer.getComponentCount() == 0) {
			// The component has actually been removed by some other means (such as it
			// was assigned to a new parent before being cancelled from the free form).
			// This can happen when doing changes via java editor instead of via visual editor.
			// So we will just remove the freeFormContainer.
			super.remove(freeFormContainer);
		} else {
			if ( freeFormContainer.getComponent(0) == aComponent )
				return freeFormContainer;
		}
	}
	return null;
}

/**
 * Set it so that the component will be the specified size.
 */
public void setUseComponentSize(Component aComponent, boolean useComponentSize) {
	FreeFormSwingContainer f = findContainer(aComponent);
	if (f != null) {
		f.setUseComponentSize(useComponentSize);
		invalidate();	// this could cause size changes.
	}}

/**
 * Find the container whose component matches the argument and then remove it
 */
public void remove(Component aComponent){
	FreeFormSwingContainer f = findContainer(aComponent);
	if (f != null) {
		getContentPane().remove(f);
	}
	pack();	// Just to be safe, because component could of been removed separately
	return;
}
/**
 * When one of our components changes its appearance it will invalidate us.
 * We should also perform a pack so that the frame of the dialog always
 * fits tightly around the contents.
 */
public void validate(){
	boolean valid = isValid();
//	System.err.println("Free Form Swing Dialog validated: "+valid);
	super.validate();
	if (!valid) {
		synchronized (getTreeLock()) {
			if (!isValidating) {
				isValidating = true;
				// KLUDGE:
				// On Windows, and maybe other systems, you can't size a java.awt.Window below a certain minimum size that is determined
				// by the AWT peer. However, this is not reflected in the pack() and getPreferredSize() or getMinimumSize() calculations.
				// They return the ideal. So, if you try to set it smaller than this minimum, it will resize it back up.
				// 
				// The problem we are having is if there is a ComponentListener attached to a Window, it will perform a validate when component resized is
				// received. Which means it comes in here and does a pack. But the peer resize above is considered a component resize too,
				// so the first pack makes it smaller than the min size, the peer makes it bigger, which comes back through and tries
				// to size it back down to the small size, and so on.
				//
				// The kludge is that until we receive the first true minimum size (which we submitted through pack in the ctor)
				// is received, we can't perform a new pack here. getPreferredSize is overridden to not allow smaller than
				// this true minimum size.
				if (minSize != null) {
					pack();
//					System.err.println("Freeform packed to "+getSize());
				}
			}
			isValidating = false;
		}
	}
}
	/**
	 * @see java.awt.Component#preferredSize()
	 * @deprecated
	 */
	public Dimension getPreferredSize() {
		Dimension calc =  super.getPreferredSize();
		if (minSize != null) {
			calc.width = Math.max(minSize.width, calc.width);
			calc.height = Math.max(minSize.height, calc.height);			
		}
		return calc;
	}

}
