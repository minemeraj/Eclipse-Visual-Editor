/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Environment {

	private static Display display;
	private static Thread t;
	private static Shell freeFormHost;

	private static void initialize() {
		if (display != null)
			return;

		t = new Thread("SWT UI Thread for VE") {
			public void run() {
				synchronized (Thread.currentThread()) {
					display = new Display();
					Thread.currentThread().notifyAll();
				}
				while (true) {
					try {
						if (!display.readAndDispatch())
							display.sleep();
					} catch (RuntimeException e) {
						e.printStackTrace();
						// We don't want this to end because of some user error. It will stay
						// running until vm is killed.
					}
				}
			}
		};

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

	public static Display getDisplay() {
		initialize();
		return display;
	}
	public static Shell getFreeFormHost() {
		if (freeFormHost == null) {
			freeFormHost = new Shell(display);
			freeFormHost.setBounds(0, 0, 100, 100);
			freeFormHost.setLocation(-10000, -10000);
			freeFormHost.open();
		}
		return freeFormHost;
	}
	public static String getFontLabel(Font aFont) {

		FontData fontData = aFont.getFontData()[0];
		StringBuffer fontLabelBuffer = new StringBuffer();
		fontLabelBuffer.append(fontData.getName());
		fontLabelBuffer.append(',');
		// Style is a bitmask of NORMAL , BOLD and ITALIC
		boolean styleUsed = false;
		if ((fontData.getStyle() & SWT.BOLD) != 0) {
			fontLabelBuffer.append("Bold");
			styleUsed = true;
		}
		if ((fontData.getStyle() & SWT.ITALIC) != 0) {
			if (styleUsed)
				fontLabelBuffer.append(' ');
			fontLabelBuffer.append("Italic");
			styleUsed = true;
		}
		if (styleUsed)
			fontLabelBuffer.append(',');
		fontLabelBuffer.append(String.valueOf(fontData.getHeight()));
		return fontLabelBuffer.toString();
	}
}
