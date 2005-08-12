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
 *  $RCSfile: FreeFormAWTDialog.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-12 17:43:04 $ 
 */

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
/**
 * Dialog to host freeform AWT components.
 * Creation date: (12/01/00 4:14:56 PM)
 * @author: Joe Winchester
 */
public class FreeFormAWTDialog extends Dialog {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = 2173221199247584523L;
	protected Frame fHostingFrame;
	protected boolean isValidating;
	protected Dimension minSize;
	protected boolean isDisposing = false;
	
	protected static FreeFormAWTDialog AWT_DIALOG;
	
	/**
	 * Get the off-screen location. It will get the screen size and go 1000 more
	 * off of the lower-right corner.
	 * 
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public static Point getOffScreenLocation() {
		Rectangle virtualBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//		The following should be the true way, but getConfigurations() is very very slow (it is about a second elapsed time).
//		So we're relying on getMaxBounds to work with multi-screen virtual device. 
//		GraphicsConfiguration[] configurations = screenDevice.getConfigurations();
//		Rectangle virtualBounds = configurations[0].getBounds();
//		for (int i = 1; i < configurations.length; i++) {
//			virtualBounds = virtualBounds.union(configurations[i].getBounds());
//		}
		return new Point(virtualBounds.width+1000, virtualBounds.height+1000);
	}
	
	/**
	 * Get the one awt dialog in the jvm.
	 * @param x
	 * @param y
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static FreeFormAWTDialog getFreeFormDialog(int x, int y) {
		// Note: We have only one for the entire jvm's life. First this is more efficient.
		// Everytime we did a reload from scratch we used to dispose of the dialog and then
		// re-get it when needed. But this actually showed up a problem. On some jvm
		// implementations if you have two dialogs open, the close
		// of the second dialog causes a problem. This way we only have one open.
		if (AWT_DIALOG == null)
			AWT_DIALOG = new FreeFormAWTDialog(x,y);
		else
			AWT_DIALOG.setLocation(x, y);
		return AWT_DIALOG;
	}
/**
 * FreeFormDialog constructor comment.
 */
public FreeFormAWTDialog(int x, int y) {
	super(new Frame(VisualVMMessages.getString("FreeForm.Frame.Title"))); //$NON-NLS-1$
	setLayout(new FlowLayout(0,0,0));
	fHostingFrame = (Frame) getParent();
	setLocation(x,y);
	fHostingFrame.setLocation(x, y);
	setTitle(VisualVMMessages.getString("FreeForm.Dialog.Title")); //$NON-NLS-1$
	
	// Need to set hosting frame visible so that we can get font, colors out of it. We want our dialog to look like
	// a frame for the children since that is the most common usage. We ourselves can't be a frame because then we
	// would show up on the taskbar, which would be annoying.
	fHostingFrame.setVisible(true);
	setFont(fHostingFrame.getFont());
	setBackground(fHostingFrame.getBackground());
	fHostingFrame.setVisible(false);

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
public FreeFormAWTDialog(java.awt.Frame parent) {
	super(parent);
}
/**
 * When a component is added to the free form shell it is always placed inside a special container
 * This container ensures that each component occupies its specified size
 */
public Component add(Component aComponent){
	FreeFormAWTContainer aContainer = new FreeFormAWTContainer();
	aContainer.add(aComponent);
	super.add(aContainer);
	pack();
	return aComponent;
}
/**
 * Disposes the parent (a Frame) that was instantiated at construction time.
 */
public void dispose() {
	// Need to check if we are already in the process of the hosting frame
	// disposing it's children (including this dialog). This is a hack to 
	// avoid an possible infinite loop since the Frame doesn't know this dialog
	// has already been disposed.
	if (!isDisposing) {
		isDisposing = true;
		super.dispose();
	    if (fHostingFrame != null)
    		fHostingFrame.dispose();
    	isDisposing = false;
	}
}
protected FreeFormAWTContainer findContainer(Component aComponent) {
	for (int i = getComponentCount()-1; i >= 0 ; i--){
		FreeFormAWTContainer freeFormContainer = (FreeFormAWTContainer) getComponent(i);
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
	FreeFormAWTContainer f = findContainer(aComponent);
	if (f != null) {
		f.setUseComponentSize(useComponentSize);
		invalidate();	// this could cause size changes.
	}
}

/**
 * Find the container whose component matches the argument and then remove it
 */
public void remove(Component aComponent){
	FreeFormAWTContainer f = findContainer(aComponent);
	if (f != null) {
		super.remove(f);
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
//	System.err.println("Free Form AWT Dialog validated");
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
				if (minSize != null)
					pack();
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
