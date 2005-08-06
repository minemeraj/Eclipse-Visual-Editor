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


/**
 * Class CompositeTable.  A virtual table control that works by stacking copies of a
 * programmer-supplied Composite below each other.<p>
 * 
 * This control is designed to work inside of the Eclipse Visual Editor.  To use it,
 * drop one on the design surface.  (Even though it extends Canvas, it does not make
 * sense to put a layout manager on it.)<p>
 * 
 * Next create one or two new custom controls by using the Visual Editor to extend
 * Composite.  If you create one custom control, it will be used as the prototype
 * for all rows that will be displayed in the table.  If you create two, the first
 * one will be used as a prototype for the header and the second one will be used 
 * as a prototype for the rows.<p>
 * 
 * If these custom controls are not given layout managers (null layout), then 
 * CompositeTable will automatically detect this situation and will supply its own
 * layout manager that will automatically lay out the children of these controls in 
 * columns to form a table.  However, if you supply layout managers for your header 
 * prototype and row prototype objects, CompositeTable will respect your choice.
 * If you use CompositeTable's built-in layout manager, then the weights property
 * will be used to determine what percentage of the total width will be allocated
 * to each column.  If this property is not set or if the sum of their elements
 * does not equal 100, the columns are created as equal sizes.<p>
 * 
 * Once you have created your (optional) Header and Row custom controls, simply drop
 * them onto your CompositeTable control in VE.  The first of these two custom
 * controls to be instantiated in your code will be interpreted by the CompositeTable
 * as the header control and the second will be interpreted as the row control.<p>
 * 
 * Now that you have defined the (optional) header and row, you can switch your 
 * CompositeTable into run mode and use it.  This is done by switching the RunTime 
 * property to true.<p>
 * 
 * Once in run mode, all of the CompositeTable's properties will be active.  In order
 * to use it, set the NumRowsInCollection property to the number of rows in the 
 * collection you want to display.  And add a RefreshContentProvider, which will
 * be called whenever CompositeTable needs to refresh a particular row.<p>
 * 
 * Please refer to the remainder of the JavaDoc for information on the remaining
 * properties and events.
 * 
 * @author djo
 * @since 3.1
 * TODO: implement scroll wheel events on 3.1; eliminate flicker when scrolling backwards
 */
public class CompositeTable extends Canvas {
	
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
	
	private InternalCompositeTable contentPane = null;
	
	/**
	 * Constructor CompositeTable.  Construct a CompositeTable control.  CompositeTable
	 * accepts the same style bits as the SWT Canvas.
	 * 
	 * @param parent The SWT parent control.
	 * @param style Style bits.  These are the same as Canvas
	 */
	public CompositeTable(Composite parent, int style) {
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
				contentPane.dispose();
				contentPane.dispose();
				contentPane = new InternalCompositeTable(this, SWT.NULL);
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
		contentPane = new InternalCompositeTable(this, SWT.NULL);
	}
	
	private void switchToDesignMode(){
		contentPane.dispose();
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
			if (children[i] instanceof InternalCompositeTable) {
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
	
	/**
	 * Method isRunTime.  Returns if the CompositeTable is in run time mode as opposed
	 * to design time mode.  In design time mode, the only permitted operations are to
	 * add or remove child Composites to be used as the header and/or row prototype objects.
	 * 
	 * @return true if this CompositeTable is in run mode.  false otherwise.
	 */
	public boolean isRunTime() {
		return runTime;
	}

	/**
	 * Method setRunTime.  Turns run-time mode on or off.  When run-time mode is off, 
	 * CompositeTable ignores most property operations and will accept prototype child
	 * controls to be added.  When run-time mode is on, the prototype controls are 
	 * interpreted and all properties become active.
	 * 
	 * @param runTime true if run-time mode should be enabled; false otherwise.
	 */
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

	public int getCurrentColumn() {
		if (contentPane == null) {
			return -1;
		}
		return getSelection().x;
	}

	public void setCurrentColumn(int column) {
		setSelection(column, getCurrentRow());
	}

	public int getCurrentRow() {
		if (contentPane == null) {
			return -1;
		}
		return getSelection().y;
	}

	public void setCurrentRow(int row) {
		setSelection(getCurrentColumn(), row);
	}
	
	public Point getSelection() {
		if (contentPane == null) {
			return null;
		}
		return contentPane.getSelection();
	}
	
	public void setSelection(Point selection) {
		setSelection(selection.x, selection.y);
	}

	public void setSelection(int column, int row) {
		if (contentPane == null) {
			return;
		}
		contentPane.setSelection(column, row);
	}
	
	public Control[] getRowControls() {
		if (contentPane == null) {
			return null;
		}
		return contentPane.getRowControls();
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
	
	public void addRefreshContentProvider(IRowContentProvider listener) {
		refreshListeners.add(listener);
	}

	public void removeRefreshContentProvider(IRowContentProvider listener) {
		refreshListeners.remove(listener);
	}
	
	LinkedList rowListeners = new LinkedList();
	
	public void addRowListener(IRowListener listener) {
		rowListeners.add(listener);
	}
	
	public void removeRowListener(IRowListener listener) {
		rowListeners.remove(listener);
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
