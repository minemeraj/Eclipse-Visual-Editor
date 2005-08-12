/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.proxy.common.IVMServer;

/**
 * Environment. There is one per display. Subclasses are used for individual OS's.
 * 
 * @since 1.1.0
 */
public abstract class Environment {

	private Display display;
	private Thread t;
	private Shell freeFormHost;
	private int freeFormHostRefCount = 0;	// ReferenceCount on freeFormHost. Since access can only be through UI thread we don't need to worry about synchronization.
	
	public Color lightGray;
	
	private static final String ENVIRONMENT_KEY = "ve.swt.Environment";
	
	/**
	 * Get the Environment associated with the given display.
	 * @param display
	 * @return the associated Environment or <code>null</code> if not associated with an environment.
	 * 
	 * @since 1.1.0
	 */
	public static Environment getEnvironment(Display display) {
		return (Environment) display.getData(ENVIRONMENT_KEY);
	}

	
	public Environment(IVMServer vmserver) {
		initialize(vmserver);
	}
	
	private boolean shutdownRequested;
	
	private void initialize(IVMServer vmserver) {
		t = new Thread("SWT UI Thread for VE") { //$NON-NLS-1$
			public void run() {
				synchronized (Thread.currentThread()) {
					display = new Display();
					display.setData(ENVIRONMENT_KEY, Environment.this);
					lightGray = new Color(display, 220, 220, 220);
					Thread.currentThread().notifyAll();
				}
				while (true) {
					try {
						if (shutdownRequested)
							break;
						if (!display.readAndDispatch())
							display.sleep();
					} catch (RuntimeException e) {
						e.printStackTrace();
						// We don't want this to end because of some user error. It will stay
						// running until vm is killed.
					}
				}
				display.dispose();
			}
		};
		
		vmserver.addShutdownListener(new Runnable() {
		
			public void run() {
				shutdownRequested = true;
				display.asyncExec(null);	// Wake it up
				try {
					t.join(10000);
				} catch (InterruptedException e) {
				}
			}
		
		});

		synchronized (t) {
			t.start();
			while (true) {
				try {
					t.wait();
					break;
				} catch (InterruptedException e) {
					continue;
				}
			}
		}
	}

	/**
	 * Get the display used by this environment.
	 * @param vmserver The IVMServer for this registry.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Display getDisplay() {
		return display;
	}
	
	/**
	 * Get the freeform host if created. If not created, then return null.
	 * This is only used to access the shell, it should be used to create the
	 * shell. Use the {@link Environment#addToFreeFormHost(Control, boolean)} to do that.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Shell getFreeFormHost() {
		return freeFormHost;
	}
	
	/**
	 * Return the offscreen location for windows for the display associated with this environment.
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public Point getOffScreenLocation() {
		Rectangle bounds = getDisplay().getBounds();
		return new Point(bounds.width+1000, bounds.height+1000);
	}
	/**
	 * Initialize the freeform host used by this environment.
	 * This must be called on the UI thread. There must be a cooresponding disposeFreeFormHost because the ff host
	 * is reference counted and not disposed until all references are gone.
	 * @param x
	 * @param y
	 * @return the freeform host. Only used to know when that it is initialized and when it is gone.
	 * 
	 * @since 1.1.0
	 */
	public Shell initializeFreeFormHost(int x, int y) {
		freeFormHostRefCount++;
		if (freeFormHost == null) {
			Shell dialogParent = new Shell(display);
			freeFormHost = new Shell(dialogParent, SWT.SHELL_TRIM );
			freeFormHost.setLocation(x, y);
			freeFormHost.setLayout(new RowLayout());
			freeFormHost.addShellListener(getPreventShellCloseListener());
			freeFormHost.open();
		}
		return freeFormHost;
	}
	
	/**
	 * Dispose the free form host.
	 * This doesn't need to be called in UI thread.
	 * 
	 * @since 1.1.0
	 */
	public void disposeFreeFormHost() {
		if (freeFormHost != null && --freeFormHostRefCount <= 0) {
			final Shell ffh = freeFormHost;
			freeFormHost = null;
			display.asyncExec(new Runnable() {
				public void run() {
					if (freeFormHostRefCount <= 0) {
						// Still want dispose, nobody came along and asked for it.
						freeFormHostRefCount = 0;
						Composite parent = ffh.getParent();
						ffh.dispose();
						parent.dispose();
					}
				}
			});
		}
	}
	
	/**
	 * Get the composite to add the control to. Since we can't
	 * always reparent a control, we need to instead return the
	 * composite which will be the host for the control when
	 * on the freeform. 
	 * The freeform host must already of been initialized.
	 * This must be called on the UI thread.
	 * @param useSetSize
	 * @return the composite to be the parent for the control.
	 * @since 1.1.0
	 */
	public Composite addToFreeFormParent(boolean useSetSize) {
		Composite container = new Composite(freeFormHost, SWT.NONE);
		// We are using data on the container because we can't set the
		// layoutdata on the child because the child would then show
		// this to the IDE. We don't want that.
		container.setData(LAYOUT_DATA, Boolean.valueOf(useSetSize));
		container.setLayout(FF_LAYOUT);
		return container;
	}
	
	/**
	 * Set the use set size flag for the control. This must be called after
	 * addToFreeFormParent was called.
	 * This must be called on the UI thread.
	 * @param control
	 * @param useSetSize
	 * 
	 * @since 1.1.0
	 */
	public void setUseSetSize(Control control, boolean useSetSize) {
		Composite parent = control.getParent();
		if (parent.getData(LAYOUT_DATA) != null) {
			// We have a valid parent.
			parent.setData(LAYOUT_DATA, Boolean.valueOf(useSetSize));
		}
	}
	
	private final static String LAYOUT_DATA = "ve.LAYOUT_DATA";
	private final static String DISPOSE_ADDED = "ve.DISPOSE ADDED";
	private final static Layout FF_LAYOUT = new Layout() {
		
		private boolean getUseSetSize(Composite composite) {
			return ((Boolean) composite.getData(LAYOUT_DATA)).booleanValue();
		}
		
		protected void layout(Composite composite, boolean flushCache) {
			Control[] children = composite.getChildren();
			if (children.length == 0)
				return;
			if (composite.getData(DISPOSE_ADDED) == null) {
				// On the first layout that it has a child we can add our dispose listener.
				// This listener will clean us up and remove us if the child goes away.
				children[0].addDisposeListener(new FreeFormDisposeListener(composite));
				composite.setData(DISPOSE_ADDED, Boolean.TRUE);
			}			
			if (!getUseSetSize(composite)) {
				Point cSize = composite.getSize();
				children[0].setBounds(0,0,cSize.x,cSize.y);	// Set to size of this composite.
			} else 
				children[0].setLocation(0,0);
		}

		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			Control[] children = composite.getChildren();
			if (children.length != 1)
				return new Point(0,0);
			return getUseSetSize(composite) ? children[0].getSize() : children[0].computeSize(wHint, hHint);
		}		
	};
	
	private static class FreeFormDisposeListener implements DisposeListener {
		private final Composite ffContainer;
		public FreeFormDisposeListener(Composite ffContainer) {
			this.ffContainer = ffContainer;
		}
		
		public void widgetDisposed(DisposeEvent e) {
			if (!ffContainer.isDisposed()) {
				// Need to put to async because at this time the child is still considered a child
				// even though it is disposed. I think this is more of a notification of disposal
				// sent before actual dispose.
				ffContainer.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (!ffContainer.isDisposed()) {
							// The child has been disposed, so the freeform container can be disposed.
							ffContainer.dispose();
						}
					}
				});
			}
		}
	};
	
	/**
	 * Subclasses need to return the appropriate prevent shell close listener for the current os.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected abstract ShellListener getPreventShellCloseListener();
	
	/**
	 * Get the font label for this font.
	 * @param aFont
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static String getFontLabel(Font aFont) {

		FontData fontData = aFont.getFontData()[0];
		StringBuffer fontLabelBuffer = new StringBuffer();
		fontLabelBuffer.append(fontData.getName());
		fontLabelBuffer.append(',');
		// Style is a bitmask of NORMAL , BOLD and ITALIC
		boolean styleUsed = false;
		if ((fontData.getStyle() & SWT.BOLD) != 0) {
			fontLabelBuffer.append(TargetVMMessages.getString("Environment.FontLabel.Style.Bold")); //$NON-NLS-1$
			styleUsed = true;
		}
		if ((fontData.getStyle() & SWT.ITALIC) != 0) {
			if (styleUsed)
				fontLabelBuffer.append(' ');
			fontLabelBuffer.append(TargetVMMessages.getString("Environment.FontLabel.Style.Italic")); //$NON-NLS-1$
			styleUsed = true;
		}
		if (styleUsed)
			fontLabelBuffer.append(',');
		fontLabelBuffer.append(String.valueOf(fontData.getHeight()));
		return fontLabelBuffer.toString();
	}
}
