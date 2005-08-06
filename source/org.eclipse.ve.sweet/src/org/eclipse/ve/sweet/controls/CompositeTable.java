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
 * Class CompositeTable.  A virtual table control that works by laying out multiple
 * copies of a programmer-supplied Composite below each other.<p>
 * 
 * Synopsis:<p>
 * 
 * In order to edit anything, one must:<p>
 * 
 * <ul>
 * <li>Set up a prototype row (and optionally header) object as specified in the CompositeTable
 * class documentation.
 * <li>Set the RunTime property to "true".
 * <li>Add a RowContentProvider that knows how to put data into a row's controls.
 * <li>Add a RowListener to validate and save changed data.
 * <li>Set the NumRowsInCollection property to the number of rows in the underlying data
 * structure.
 * </ul>
 *
 * Detailed description:<p>
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
 * TODO: eliminate flicker when scrolling backwards
 */
public class CompositeTable extends Canvas {
	
	// Property fields here
	private boolean runTime = false;
	
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

	/**
	 * Method getWeights.  Returns an array representing the percentage of the total width
	 * each column is allocated or null if no weights have been specified.  This property is
	 * ignored if the programmer has set a layout manager on the header and/or the row
	 * prototype objects.
	 * 
	 * @return the current weights array or null if no weights have been specified.
	 */
	public int[] getWeights() {
		return weights;
	}

	/**
	 * Method setWeights.  Specifies the percentage of the total width that will be allocated
	 * to each column.  This property is ignored if the programmer has set a layout manager 
	 * on the header and/or the row prototype objects.<p>
	 * 
	 * The number of elements in the array must match the number of columns and the sum of
	 * all elements must equal 100.  If either of these constraints is not true, this property
	 * will be ignored and all columns will be created equal in width.
	 * 
	 * @param weights the weights to use if the CompositeTable is automatically laying out controls.
	 */
	public void setWeights(int[] weights) {
		this.weights = weights;
		if (contentPane != null) {
			contentPane.setWeights(weights);
		}
	}

	/**
	 * Method getMaxRowsVisible.  Returns the maximum number of rows that will be permitted 
	 * in the table at once.  For example, setting this property to 1 will have the effect of 
	 * creating a single editing area with a scroll bar on the right allowing the user to scroll
	 * through all rows using either the mouse or the PgUp/PgDn keys.  The default value is
	 * Integer.MAX_VALUE.
	 * 
	 * @return the maximum number of rows that are permitted to be visible at one time, regardless
	 * of the control's size.
	 */
	public int getMaxRowsVisible() {
		return maxRowsVisible;
	}

	/**
	 * Method setMaxRowsVisible.  Sets the maximum number of rows that will be permitted 
	 * in the table at once.  For example, setting this property to 1 will have the effect of 
	 * creating a single editing area with a scroll bar on the right allowing the user to scroll
	 * through all rows using either the mouse or the PgUp/PgDn keys.  The default value is
	 * Integer.MAX_VALUE.
	 * 
	 * @param maxRowsVisible the maximum number of rows that are permitted to be visible at one time, regardless
	 * of the control's size.
	 */
	public void setMaxRowsVisible(int maxRowsVisible) {
		this.maxRowsVisible = maxRowsVisible;
		if (contentPane != null) {
			contentPane.setMaxRowsVisible(maxRowsVisible);
		}
	}
	
	/**
	 * Method getNumRowsVisible.  Returns the actual number of rows that are currently visible.
	 * Normally CompositeTable displays as many rows as will fit vertically given the control's
	 * size.  This value can be clamped to a maximum using the MaxRowsVisible property.
	 * 
	 * @return the actual number of rows that are currently visible.
	 */
	public int getNumRowsVisible() {
		if (contentPane != null)
			return contentPane.getNumRowsVisible();
		else
			return -1;
	}

	/**
	 * Method getNumRowsInCollection.  Returns the number of rows in the data structure that is
	 * being edited.
	 * 
	 * @return the number of rows in the underlying data structure.
	 */
	public int getNumRowsInCollection() {
		return numRowsInCollection;
	}

	/**
	 * Method setNumRowsInCollection.  Sets the number of rows in the data structure that is
	 * being edited.
	 * 
	 * @param numRowsInCollection the number of rows represented by the underlying data structure.
	 */
	public void setNumRowsInCollection(int numRowsInCollection) {
		this.numRowsInCollection = numRowsInCollection;
		if (contentPane != null) {
			contentPane.setNumRowsInCollection(numRowsInCollection);
		}
	}

	/**
	 * Method getTopRow.  Return the number of the line that is being displayed in the top row
	 * of the CompositeTable editor (0-based).
	 * 
	 * @return the number of the top line.
	 */
	public int getTopRow() {
		return topRow;
	}

	/**
	 * Method setTopRow. Set the number of the line that is being displayed in the top row
	 * of the CompositeTable editor (0-based).  If the new top row is not equal to the current
	 * top row, the table will automatically be scrolled to the new position.  This number must
	 * be greater than 0 and less than NumRowsInCollection.
	 * 
	 * @param topRow the line number of the new top row.
	 */
	public void setTopRow(int topRow) {
		this.topRow = topRow;
		if (contentPane != null) {
			contentPane.setTopRow(topRow);
		}
	}

	/**
	 * Method getCurrentColumn.  Returns the column number of the currently-focused column 
	 * (0-based).
	 * 
	 * @return the column number of the currently-focused column.
	 */
	public int getCurrentColumn() {
		if (contentPane == null) {
			return -1;
		}
		return getSelection().x;
	}

	/**
	 * Method setCurrentColumn.  Sets the column number of the currently-focused column 
	 * (0-based).
	 * 
	 * @param column The new column to focus.
	 */
	public void setCurrentColumn(int column) {
		setSelection(column, getCurrentRow());
	}

	/**
	 * Method getCurrentRow.  Returns the current row number as an offset from the top of the
	 * table window.  In order to get the current row in the underlying data structure, compute
	 * getTopRow() + getCurrentRow().
	 * 
	 * @return the current row number as an offset from the top of the table window.
	 */
	public int getCurrentRow() {
		if (contentPane == null) {
			return -1;
		}
		return getSelection().y;
	}

	/**
	 * Method setCurrentRow.  Sets the current row number as an offset from the top of the
	 * table window.  In order to get the current row in the underlying data structure, compute
	 * getTopRow() + getCurrentRow().
	 * 
	 * @param row  the current row number as an offset from the top of the table window.
	 */
	public void setCurrentRow(int row) {
		setSelection(getCurrentColumn(), row);
	}
	
	/**
	 * Method getSelection.  Returns the currently-selected (column, row) pair where the row 
	 * specifies the offset from the top of the table window.  In order to get the current 
	 * row in the underlying data structure, use getSelection().y + getCurrentRow().
	 * 
	 * @return  the currently-selected (column, row) pair where the row specifies the offset 
	 * from the top of the table window.
	 */
	public Point getSelection() {
		if (contentPane == null) {
			return null;
		}
		return contentPane.getSelection();
	}
	
	/**
	 * Method setSelection.  Sets the currently-selected (column, row) pair where the row 
	 * specifies the offset from the top of the table window.  In order to get the current 
	 * row in the underlying data structure, use getSelection().y + getCurrentRow().
	 * 
	 * @param selection the (column, row) to select
	 */
	public void setSelection(Point selection) {
		setSelection(selection.x, selection.y);
	}

	/**
	 * Method setSelection.  Sets the currently-selected (column, row) pair where the row 
	 * specifies the offset from the top of the table window.  In order to get the current 
	 * row in the underlying data structure, use getSelection().y + getCurrentRow().
	 * 
	 * @param column the column to select
	 * @param row the row to select as an offset from the top of the window
	 */
	public void setSelection(int column, int row) {
		if (contentPane == null) {
			return;
		}
		contentPane.setSelection(column, row);
	}
	
	/** (non-API)
	 * Method getHeaderConstructor.  Returns the Constructor object used internally to
	 * construct the table's header or null if there is none.
	 * 
	 * @return the header's constructor.
	 */
	public Constructor getHeaderConstructor() {
		return headerConstructor;
	}

	/** (non-API)
	 * Method getRowConstructor.  Returns the Constructor object used internally to 
	 * construct each row object.
	 * 
	 * @return the rows' constructor
	 */
	public Constructor getRowConstructor() {
		return rowConstructor;
	}

	/** (non-API)
	 * Method getHeaderControl.  Returns the prototype header control.
	 * 
	 * @return the prototype header control.
	 */
	public Control getHeaderControl() {
		return headerControl;
	}

	/** (non-API)
	 * Method getRowControl.  Returns the prototype row control.
	 * 
	 * @return the prototype row control.
	 */
	public Control getRowControl() {
		return rowControl;
	}

	LinkedList contentProviders = new LinkedList();
	
	/**
	 * Method addRowContentProvider.  Adds the specified content provider to the list of
	 * content providers that will be called when a row needs to be filled with data.
	 * Most of the time it only makes sense to add a single one.
	 * 
	 * @param contentProvider The content provider to add.
	 */
	public void addRowContentProvider(IRowContentProvider contentProvider) {
		contentProviders.add(contentProvider);
	}

	/**
	 * Method removeRowContentProvider.  Removes the specified content provider from the list of
	 * content providers that will be called when a row needs to be filled with data.
	 * 
	 * @param contentProvider The content provider to remove.
	 */
	public void removeRowContentProvider(IRowContentProvider contentProvider) {
		contentProviders.remove(contentProvider);
	}
	
	LinkedList rowListeners = new LinkedList();
	
	/**
	 * Method addRowListener.  Adds the specified listener to the set of listeners that 
	 * will be notified when the user wishes to leave a row and when the user has already
	 * left a row.  If any listener vetos leaving a row, the focus remains in the row.
	 * 
	 * @param listener The listener to add.
	 */
	public void addRowListener(IRowListener listener) {
		rowListeners.add(listener);
	}
	
	/**
	 * Method removeRowListener.  Removes the specified listener from the set of listeners that 
	 * will be notified when the user wishes to leave a row and when the user has already
	 * left a row.  If any listener vetos leaving a row, the focus remains in the row.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeRowListener(IRowListener listener) {
		rowListeners.remove(listener);
	}
	
	LinkedList insertHandlers = new LinkedList();
	
	/**
	 * Method addInsertHandler.  Adds the specified insertHandler to the set of objects that
	 * will be used to handle insert requests.
	 * 
	 * @param insertHandler the insertHandler to add.
	 */
	public void addInsertHandler(IInsertHandler insertHandler) {
		insertHandlers.add(insertHandler);
	}
	
	/**
	 * Method removeInsertHandler.  Removes the specified insertHandler from the set of objects that
	 * will be used to handle insert requests.
	 * 
	 * @param insertHandler the insertHandler to remove.
	 */
	public void removeInsertHandler(IInsertHandler insertHandler) {
		insertHandlers.remove(insertHandler);
	}
	
	LinkedList deleteHandlers = new LinkedList();
	
	/**
	 * Method addDeleteHandler.  Adds the specified deleteHandler to the set of objects that
	 * will be used to handle delete requests.
	 * 
	 * @param deleteHandler the deleteHandler to add.
	 */
	public void addDeleteHandler(IDeleteHandler deleteHandler) {
		deleteHandlers.add(deleteHandler);
	}
	
	/**
	 * Method removeDeleteHandler.  Removes the specified deleteHandler from the set of objects that
	 * will be used to handle delete requests.
	 * 
	 * @param deleteHandler the deleteHandler to remove.
	 */
	public void removeDeleteHandler(IDeleteHandler deleteHandler) {
		deleteHandlers.remove(deleteHandler);
	}
	
	boolean deleteEnabled = true;

	/**
	 * Method isDeleteEnabled.  Returns if delete is enabled.  Deletions are only processed if
	 * the DeleteEnabled property is true and at least one delete handler has been registered.<p>
	 * 
	 * The default value is true.
	 * 
	 * @return true if delete is enabled.  false otherwise.
	 */
	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}

	/**
	 * Method setDeleteEnabled.  Sets if delete is enabled.  Deletions are only processed if
	 * the DeleteEnabled property is true and at least one delete handler has been registered.<p>
	 * 
	 * The default value is true.
	 * 
	 * @param deleteEnabled true if delete should be enabled.  false otherwise.
	 */
	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"


