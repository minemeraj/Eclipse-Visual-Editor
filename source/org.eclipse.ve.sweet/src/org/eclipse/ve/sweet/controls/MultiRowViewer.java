/*
 * Copyright (C) 2005 David Orme <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme     - Initial API and implementation
 */
package org.eclipse.ve.sweet.controls;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;


public class MultiRowViewer extends Canvas {
	
	// Property fields here
	private boolean runTime = false;
	
	private boolean drawingLines = false;
	private int[] weights = new int[0];
	
	private int topRow = 0;
	private int numRowsInCollection = 0;
	private int maxRowsVisible = Integer.MAX_VALUE;
	
	// Private fields here
	private Constructor headerConstructor = null;
	private Control headerControl = null;
	private Constructor rowConstructor = null;
	private Control rowControl = null;
	
	private InternalMRViewer contentPane = null;
	
	public MultiRowViewer(Composite parent, int style) {
		super(parent, style);
		setLayout(new Layout() {
			protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
				return computeTheSize(wHint, hHint, flushCache);
			}
			protected void layout(Composite composite, boolean flushCache) {
				resize();
			}
		});
	}
	
	protected final Point computeTheSize(int wHint, int hHint, boolean changed) {
		return super.computeSize(wHint, hHint, changed);
	}
	
	private int numChildrenLastTime = 0;
	
	protected final void resize() {
		if (isRunTime()) {
			int childrenLength = getChildren().length;
			if (numChildrenLastTime != childrenLength) {
				resizeAndRecordPrototypeRows();
				showPrototypes(false);
				numChildrenLastTime = childrenLength;
				contentPane.removeHandlers();
				contentPane.dispose();
				contentPane = new InternalMRViewer(this, SWT.NULL);
			}
			updateVisibleRows();
		} else {
			resizeAndRecordPrototypeRows();
		}
	}

	protected void updateVisibleRows() {
		if (contentPane == null) {
			switchToRunMode();
		}
		Point size = getSize();
		contentPane.setBounds(0, 0, size.x, size.y);
	}

	private void switchToRunMode() {
		showPrototypes(false);
		contentPane = new InternalMRViewer(this, SWT.NULL);
	}
	
	private void switchToDesignMode(){
		contentPane.removeHandlers();
		contentPane.dispose();
		contentPane = null;
		showPrototypes(true);
		resizeAndRecordPrototypeRows();
	}

	private void showPrototypes(boolean newValue) {
		if (headerControl != null) {
			headerControl.setVisible(newValue);
		}
		if (rowControl != null) {
			rowControl.setVisible(newValue);
		}
	}

	protected void resizeAndRecordPrototypeRows() {
		Control[] children = getChildren();
		ArrayList realChildren = new ArrayList();
		Control[] finalChildren = children;
		
		// Find first two prototypes
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof InternalMRViewer) {
				continue;
			}
			if (realChildren.size() < 2) {
				realChildren.add(children[i]);
			}
		}
		finalChildren = (Control[]) realChildren.toArray(new Control[realChildren.size()]);
		
		if (finalChildren.length == 0) {
			headerConstructor = null;
			headerControl = null;
			rowConstructor = null;
			rowControl = null;
			return;
		}

		// Get a constructor for the header and/or the row prototype
		headerConstructor = null;
		headerControl = null;
		rowConstructor = null;
		rowControl = null;
		
		if (finalChildren.length == 1) {
			try {
				rowControl = (Composite) finalChildren[0];
				rowConstructor = finalChildren[0].getClass().getConstructor(new Class[] {Composite.class, Integer.TYPE});
			} catch (Exception e) {
			}
		} else {
			try {
				headerConstructor = finalChildren[0].getClass().getConstructor(new Class[] {Composite.class, Integer.TYPE});
				headerControl = finalChildren[0];
				rowConstructor = finalChildren[1].getClass().getConstructor(new Class[] {Composite.class, Integer.TYPE});
				rowControl = finalChildren[1];
			} catch (Exception e) {
			}
		}
		
		// Now actually resize the children
		int top=0;
		int width = getSize().x;
		for (int i=0; i < finalChildren.length; ++i) {
			int height = 50;
			if (finalChildren[i] instanceof Composite) {
				Composite child = (Composite) finalChildren[i];
				if (child.getLayout() == null) {
					height = layoutHeaderOrRow(child);
				} else {
					height = finalChildren[i].computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
				}
			} else {
				height = finalChildren[i].computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
			}
			
			finalChildren[i].setBounds(0, top, width, height);
			top += height;
		}
	}
	
	/**
	 * @param child
	 * @return the height of the header or row
	 */
	int layoutHeaderOrRow(Composite child) {
		Control[] children = child.getChildren();
		if (children.length == 0) {
			return 50;
		}
		int[] weights = this.weights;
		weights = checkWeights(weights, children.length);
		
		int maxHeight = 0;
		for (int i = 0; i < children.length; i++) {
			int height = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
			if (maxHeight < height) {
				maxHeight = height;
			}
		}
		++maxHeight;
		
		int widthRemaining = child.getParent().getSize().x;
		int totalSize = widthRemaining;
		for (int i = 0; i < children.length-1; i++) {
			int left = totalSize - widthRemaining;
			int desiredHeight = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
			int top = maxHeight - desiredHeight;
			int width = (int)(((float)weights[i])/100 * totalSize);
			children[i].setBounds(left, top, width-2, desiredHeight);
			widthRemaining -= width;
		}
		
		int left = totalSize - widthRemaining;
		int desiredHeight = children[children.length-1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
		int top = maxHeight - desiredHeight;
		children[children.length-1].setBounds(left, top, widthRemaining, desiredHeight);
		
		return maxHeight;
	}

	private int[] checkWeights(int[] weights, int numChildren) {
		if (weights.length == numChildren) {
			int sum = 0;
			for (int i = 0; i < weights.length; i++) {
				sum += weights[i];
			}
			if (sum == 100) {
				return weights;
			}
		}
		
		// Either the number of weights doesn't match or they don't add up.
		// Compute something sane and return that instead.
		int[] result = new int[numChildren];
		int weight = 100 / numChildren;
		int extra = 100 % numChildren;
		for (int i = 0; i < result.length-1; i++) {
			result[i] = weight;
		}
		result[numChildren-1] = weight + extra;
		return result;
	}

	private ControlListener childResizeListener = new ControlAdapter() {
		public void controlResized(ControlEvent e) {
			if (e.widget instanceof Composite) {
				Composite item = (Composite) e.widget;
				if (item.getLayout() == null) {
					layoutHeaderOrRow(item);
				}
			}
		}
	};
	
	public boolean isRunTime() {
		return runTime;
	}

	public void setRunTime(boolean runTime) {
		if (this.runTime != runTime) {
			this.runTime = runTime;
			if (runTime) {
				if (rowControl == null) {
					resizeAndRecordPrototypeRows();
				}
				switchToRunMode();
			} else {
				switchToDesignMode();
			}
		}
	}

	public boolean isDrawingLines() {
		return drawingLines;
	}

	public void setDrawingLines(boolean drawingLines) {
		this.drawingLines = drawingLines;
		if (contentPane != null) {
			contentPane.setDrawingLines(drawingLines);
		}
	}

	public int[] getWeights() {
		return weights;
	}

	public void setWeights(int[] weights) {
		this.weights = weights;
		if (contentPane != null) {
			contentPane.setWeights(weights);
		}
	}

	public int getMaxRowsVisible() {
		return maxRowsVisible;
	}

	public void setMaxRowsVisible(int maxRowsVisible) {
		this.maxRowsVisible = maxRowsVisible;
		if (contentPane != null) {
			contentPane.setMaxRowsVisible(maxRowsVisible);
		}
	}
	
	public int getNumRowsVisible() {
		if (contentPane != null)
			return contentPane.getNumRowsVisible();
		else
			return -1;
	}

	public int getNumRowsInCollection() {
		return numRowsInCollection;
	}

	public void setNumRowsInCollection(int numRowsInCollection) {
		this.numRowsInCollection = numRowsInCollection;
		if (contentPane != null) {
			contentPane.setNumRowsInCollection(numRowsInCollection);
		}
	}

	public int getTopRow() {
		return topRow;
	}

	public void setTopRow(int topRow) {
		this.topRow = topRow;
		if (contentPane != null) {
			contentPane.setTopRow(topRow);
		}
	}
	
	public Control getHeaderRow() {
		return headerControl;
	}

	public Control[] getVisibleChildRows() {
		return contentPane.getVisibleChildRows();
	}

	public Constructor getHeaderConstructor() {
		return headerConstructor;
	}

	public Constructor getRowConstructor() {
		return rowConstructor;
	}

	public Control getHeaderControl() {
		return headerControl;
	}

	public Control getRowControl() {
		return rowControl;
	}

	LinkedList refreshListeners = new LinkedList();
	
	public void addRefreshContentProvider(IRefreshContentProvider listener) {
		refreshListeners.add(listener);
	}

	public void removeRefreshContentProvider(IRefreshContentProvider listener) {
		refreshListeners.remove(listener);
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
