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
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ve.sweet.metalogger.Logger;


public class InternalCompositeTable extends Composite implements Listener {
	private Composite sliderHolder = null;
	private Composite controlHolder = null;
	private Slider slider = null;
	
	private CompositeTable parent;
	
	private int[] weights;
	private int maxRowsVisible;
	private int numRowsInDisplay;
	private int numRowsInCollection;

	private int topRow;
	private int currentRow;
	private int currentColumn;

	private int currentVisibleTopRow = 0;
	private int numRowsVisible = 0;
	private LinkedList rows = new LinkedList();
	private LinkedList spareRows = new LinkedList();
	
	private Constructor headerConstructor;
	private Constructor rowConstructor;
	private Control headerControl;
	private Control myHeader = null;
	private Control rowControl;
	
	public InternalCompositeTable(Composite parentControl, int style) {
		super(parentControl, style);
		initialize();
		this.parent = (CompositeTable) parentControl;
		controlHolder.addListener(SWT.MouseWheel, this);

		weights = parent.getWeights();
		maxRowsVisible = parent.getMaxRowsVisible();
		numRowsInCollection = parent.getNumRowsInCollection();
		topRow = parent.getTopRow();
		
		headerConstructor = parent.getHeaderConstructor();
		rowConstructor = parent.getRowConstructor();
		headerControl = parent.getHeaderControl();
		rowControl = parent.getRowControl();
		
		currentVisibleTopRow = topRow;
		showHeader();
		updateVisibleRows();
	}

	private void initialize() {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.verticalSpacing = 0;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.horizontalSpacing = 0;
		this.setLayout(gl);
		createControlHolder();
		createSliderHolder();
	}

	/**
	 * This method initializes controlHolder	
	 *
	 */
	private void createControlHolder() {
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		controlHolder = new Composite(this, SWT.NONE);
		controlHolder.setLayoutData(gridData);
		controlHolder.setLayout(new Layout() {
			protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
				if (rowControl != null) {
					int height = 0;
					int width = 0;
					if (headerControl != null) {
						Point headerSize = headerControl.getSize();
						width = headerSize.x;
						height = headerSize.y;
					}
					Point rowSize = rowControl.getSize();
					height += rowSize.y * 2;
					if (width < rowSize.x) {
						width = rowSize.x;
					}
					return new Point(height, width);
				}
				return new Point(50, 50);
			}
			protected void layout(Composite composite, boolean flushCache) {
				layoutControlHolder();
			}
		});
	}

	/**
	 * This method initializes sliderHolder	
	 *
	 */
	private void createSliderHolder() {
		GridData gd = getSliderGridData();
		sliderHolder = new Composite(this, SWT.NONE);
		slider = new Slider(sliderHolder, SWT.VERTICAL);
		slider.addSelectionListener(sliderSelectionListener);
		sliderHolder.setLayout(new FillLayout());
		sliderHolder.setLayoutData(gd);
		sliderHolder.setTabList(new Control[] {});
	}

	// Slider utility methods ---------------------------------------------------------------------

	private GridData getSliderGridData() {
		GridData gd = new org.eclipse.swt.layout.GridData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gd.verticalSpan = 1;
		if (!sliderVisible) {
			gd.widthHint = 0;
		}
		gd.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		return gd;
	}
	
	private boolean sliderVisible = false;
	
	public void setSliderVisible(boolean visible) {
		this.sliderVisible = visible;
		sliderHolder.setLayoutData(getSliderGridData());
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				sliderHolder.getParent().layout(true);
				sliderHolder.layout(true);
				Point sliderHolderSize = sliderHolder.getSize();
				slider.setBounds(0, 0, sliderHolderSize.x, sliderHolderSize.y);
			}
		});
	}
	
	public boolean isSliderVisible() {
		return sliderVisible;
	}
	
	public void dispose() {
		disposeRows(rows);
		disposeRows(spareRows);
		super.dispose();
	}

	private void disposeRows(LinkedList rowsCollection) {
		for (Iterator rowsIter = rowsCollection.iterator(); rowsIter.hasNext();) {
			InternalRow row = (InternalRow) rowsIter.next();
			row.dispose();
		}
	}

	// Row object layout --------------------------------------------------------------------------
	
	protected void layoutControlHolder() {
		if (myHeader != null)
			layoutChild(myHeader);
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			InternalRow row = (InternalRow) rowsIter.next();
			layoutChild(row.getRowControl());
		}
		updateVisibleRows();
	}

	/**
	 * @param child
	 * @return height of child
	 */
	private int layoutChild(Control child) {
		if (child instanceof Composite) {
			Composite composite = (Composite) child;
			if (composite.getLayout() == null) {
				return parent.layoutHeaderOrRow(composite);
			}
			composite.layout(true);
			return composite.getSize().y;
		}
		return child.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
	}

	// Table control layout -- utility methods ----------------------------------------------------

	private Control createInternalControl(Composite parent, Constructor constructor) {
		Control result = null;
		try {
			result = (Control) constructor.newInstance(new Object[] {parent, new Integer(SWT.NULL)});
		} catch (Exception e) {
			Logger.log().error(e, "Unable to construct control");
		}
		return result;
	}
	
	private void showHeader() {
		if (myHeader == null && headerConstructor != null) {
			myHeader = createInternalControl(controlHolder, headerConstructor);
			layoutChild(myHeader);
		}
	}
	
	// Table control layout -- main refresh algorithm ---------------------------------------------

	private void updateVisibleRows() {
		// If we don't have our prototype row object yet, bail out
		if (rowControl == null) {
			return;
		}
		
		// Figure out how many rows we can stack vertically
		int clientAreaHeight = controlHolder.getSize().y;
		if (clientAreaHeight <= 0) {
			return;
		}
		
		int topPosition = 0;
		
		int headerHeight = 0;
		if (myHeader != null) {
			headerHeight = headerControl.getSize().y + 1;
			clientAreaHeight -= headerHeight;
			topPosition += headerHeight;
		}
		
		numRowsInDisplay = clientAreaHeight / rowControl.getSize().y;
		numRowsVisible = numRowsInDisplay;
		
		int displayableRows = numRowsInCollection - topRow;
		if (numRowsVisible > displayableRows) {
			numRowsVisible = displayableRows;
		}
		if (numRowsVisible > maxRowsVisible) {
			numRowsVisible = maxRowsVisible;
		}
		if (numRowsVisible < 1) {
			numRowsVisible = 1;
		}

		// Scroll the view so that the right number of row
		// objects are showing and they have the right data
		if (rows.size() - Math.abs(currentVisibleTopRow - topRow) > 0) {
			if (currentRow >= numRowsVisible) {
				deleteRowAt(0);
				++currentVisibleTopRow;
				++topRow;
				--currentRow;
			}
			scrollTop();
			fixNumberOfRows();
		} else {
			currentVisibleTopRow = topRow;
			
			// The order of number fixing/refresh is important in order to
			// minimize the number of screen redraw operations
			if (rows.size() > numRowsVisible) {
				fixNumberOfRows();
				refreshAllRows();
			} else {
				refreshAllRows();
				fixNumberOfRows();
			}
		}
		
		// Lay out the header and rows correctly in the display
		int width = controlHolder.getSize().x;
		
		// First, the header...
		if (myHeader != null) {
			myHeader.setBounds(0, 0, width, headerHeight);
		}
		
		// Now the rows.
		int rowHeight = 50;
		rowHeight = rowControl.getSize().y;
		
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			InternalRow row = (InternalRow) rowsIter.next();
			Control rowControl = row.getRowControl();
			rowControl.setBounds(0, topPosition, width, rowHeight);
			layoutChild(rowControl);
			topPosition += rowHeight;
		}
		
		// Show, hide, reset the scroll bar
		if (numRowsVisible < numRowsInCollection) {
			int extra = numRowsInCollection - numRowsVisible;
			int pageIncrement = numRowsVisible;
			if (pageIncrement > extra)
				pageIncrement = extra;
			
			slider.setMaximum(numRowsInCollection);
			slider.setMinimum(0);
			slider.setIncrement(1);
			slider.setPageIncrement(pageIncrement);
			slider.setThumb(numRowsInCollection - (numRowsInCollection - numRowsVisible));
			
			slider.setSelection(topRow);
			
			if (!isSliderVisible()) {
				setSliderVisible(true);
			}
		} else {
			setSliderVisible(false);
		}
	}

	private void scrollTop() {
		while (currentVisibleTopRow < topRow) {
			deleteRowAt(0);
			++currentVisibleTopRow;
		}
		while (currentVisibleTopRow > topRow) {
			--currentVisibleTopRow;
			insertRowAt(0);
		}
	}

	private void fixNumberOfRows() {
		int numRows = rows.size();
		while (numRows > numRowsVisible) {
			deleteRowAt(numRows-1);
			numRows = rows.size();
		}
		while (numRows < numRowsVisible) {
			insertRowAt(numRows);
			numRows = rows.size();
		}
	}

	private void refreshAllRows() {
		int row=0;
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			InternalRow rowControl = (InternalRow) rowsIter.next();
			fireRefreshEvent(topRow + row, rowControl.getRowControl());
			++row;
		}
	}

	private void insertRowAt(int position) {
		InternalRow newRow = getNewRow();
		if (position > rows.size()) {
			position = rows.size();
		}
		rows.add(position, newRow);
		fireRefreshEvent(currentVisibleTopRow + position, newRow.getRowControl());
	}
	
	private void deleteRowAt(int position) {
		InternalRow row = (InternalRow) rows.remove(position);
		row.setVisible(false);
		spareRows.addLast(row);
	}
	
	private InternalRow getNewRow() {
		if (spareRows.size() > 0) {
			InternalRow recycledRow = (InternalRow) spareRows.removeFirst();
			recycledRow.setVisible(true);
			return recycledRow;
		}
		return new InternalRow(this, createInternalControl(controlHolder, rowConstructor));
	}

	// Property getters/setters --------------------------------------------------------------

	public void setMaxRowsVisible(int maxRowsVisible) {
		this.maxRowsVisible = maxRowsVisible;
		updateVisibleRows();
	}

	public void setNumRowsInCollection(int numRowsInCollection) {
		this.numRowsInCollection = numRowsInCollection;
		updateVisibleRows();
	}

	public void setTopRow(int topRow) {
		this.topRow = topRow;
		updateVisibleRows();
	}

	public Point getSelection() {
		return new Point(currentColumn, currentRow);
	}
	
	public void setSelection(int column, int row) {
		if (row == currentRow)
			internalSetSelection(column, row, false);
		else {
			if (fireRequestRowChangeEvent())
				internalSetSelection(column, row, true);
		}
	}

	public void setWeights(int[] weights) {
		this.weights = weights;
		layoutControlHolder();
	}

	public int getNumRowsVisible() {
		return numRowsVisible;
	}
	
	// Refresh Event API --------------------------------------------------------------------------

	public void addRefreshContentProvider(IRowContentProvider listener) {
		parent.contentProviders.add(listener);
	}

	public void removeRefreshContentProvider(IRowContentProvider listener) {
		parent.contentProviders.remove(listener);
	}

	private void fireRefreshEvent(int offsetFromTopRow, Control rowControl) {
		for (Iterator refreshListenersIter = parent.contentProviders.iterator(); refreshListenersIter.hasNext();) {
			IRowContentProvider listener = (IRowContentProvider) refreshListenersIter.next();
			listener.refresh(parent, topRow, offsetFromTopRow, rowControl);
		}
	}
	
	// Event Handling, navigation -----------------------------------------------------------------

	void keyPressed(InternalRow sender, KeyEvent e) {
		if ((e.stateMask & SWT.CONTROL) != 0) {
			switch (e.keyCode) {
			case SWT.HOME:
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				setTopRow(0);
				internalSetSelection(currentColumn, 0, true);
				return;
			case SWT.END:
				if (topRow + numRowsVisible < numRowsInCollection) {
					if (!fireRequestRowChangeEvent()) {
						return;
					}
					setTopRow(numRowsInCollection - numRowsVisible);
					internalSetSelection(currentColumn, numRowsVisible, true);
				}
				return;
			default:
				return;
			}
		}
		switch (e.keyCode) {
		case SWT.ARROW_UP:
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			if (currentRow > 0) {
				internalSetSelection(currentColumn, currentRow-1, true);
				return;
			}
			if (topRow > 0) {
				setTopRow(topRow - 1);
				internalSetSelection(currentColumn, currentRow, true);
				return;
			}
			return;
		case SWT.ARROW_DOWN:
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			if (currentRow < numRowsVisible-1) {
				internalSetSelection(currentColumn, currentRow+1, true);
				return;
			}
			if (topRow + numRowsVisible < numRowsInCollection) {
				setTopRow(topRow + 1);
				internalSetSelection(currentColumn, currentRow, true);
				return;
			}
			return;
		case SWT.PAGE_UP:
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			if (topRow > 0) {
				int newTopRow = topRow - numRowsInDisplay;
				if (newTopRow < 0) {
					newTopRow = 0;
				}
				setTopRow(newTopRow);
				internalSetSelection(currentColumn, currentRow, true);
			}
			return;
		case SWT.PAGE_DOWN:
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			if (topRow + numRowsVisible < numRowsInCollection) {
				int newTopRow = topRow + numRowsVisible;
				if (newTopRow >= numRowsInCollection - 1) {
					newTopRow = numRowsInCollection - 1;
				}
				setTopRow(newTopRow);
				if (currentRow < numRowsVisible) {
					internalSetSelection(currentColumn, currentRow, true);
				} else {
					internalSetSelection(currentColumn, numRowsVisible-1, true);
				}
			}
			return;
		}
	}
	
	public void keyTraversed(InternalRow sender, TraverseEvent e) {
		if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
			if (currentColumn >= sender.getNumColumns() - 1) {
				e.detail = SWT.TRAVERSE_NONE;
				handleNextRowNavigation(sender);
			}
		} else if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			if (currentColumn == 0) {
				e.detail = SWT.TRAVERSE_NONE;
				handlePreviousRowNavigation(sender);
			}
		} else if (e.detail == SWT.TRAVERSE_RETURN) {
			e.detail = SWT.TRAVERSE_NONE;
			if (currentColumn >= sender.getNumColumns() - 1) {
				handleNextRowNavigation(sender);
			} else {
				deferredSetFocus(getControl(currentColumn+1, currentRow), false);
			}
		}
	}

	void focusLost(InternalRow sender, FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	void focusGained(InternalRow sender, FocusEvent e) {
		if (getRowNumber(sender) != currentRow) {
			if (!fireRequestRowChangeEvent()) {
				// Go back if we're not allowed to be here
				deferredSetFocus(getControl(currentColumn, currentRow), false);
			}
		}
		currentRow = getRowNumber(sender);
		currentColumn = sender.getColumnNumber((Control)e.widget);
		fireRowChangedEvent();
	}
	
	private SelectionListener sliderSelectionListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			if (!fireRequestRowChangeEvent()) {
				slider.setSelection(topRow);
				return;
			}
			
			setTopRow(slider.getSelection());
			deferredSetFocus(getControl(currentColumn, currentRow), true);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};
	
	public void handleEvent(Event event) {
		if (!fireRequestRowChangeEvent()) {
			return;
		}

		if (event.count > 0) {
			if (topRow > 0)
				setTopRow(topRow - 1);
		} else {
			if (topRow < numRowsInCollection - numRowsVisible)
				setTopRow(topRow + 1);
		}
		deferredSetFocus(getControl(currentColumn, currentRow), true);
	}
	
	// Event Firing -------------------------------------------------------------------------------

	private void fireRowChangedEvent() {
		for (Iterator rowChangeListenersIter = parent.rowListeners.iterator(); rowChangeListenersIter.hasNext();) {
			IRowListener listener = (IRowListener) rowChangeListenersIter.next();
			listener.rowChanged(parent);
		}
	}

	private boolean fireRequestRowChangeEvent() {
		for (Iterator rowChangeListenersIter = parent.rowListeners.iterator(); rowChangeListenersIter.hasNext();) {
			IRowListener listener = (IRowListener) rowChangeListenersIter.next();
			if (!listener.requestRowChange(parent)) {
				return false;
			}
		}
		return true;
	}

	// Event Handling, utility methods ------------------------------------------------------------
	
	private void handleNextRowNavigation(InternalRow row) {
		if (!fireRequestRowChangeEvent()) {
			deferredSetFocus(getControl(currentRow, currentColumn), false);
			return;
		}
		
		if (currentRow < numRowsVisible-1) {
			deferredSetFocus(getControl(0, currentRow+1), true);
		} else {
			if (topRow + numRowsVisible >= numRowsInCollection) {
				// We're at the end; don't go anywhere
				return;
			}
			// We have to scroll forwards
			setTopRow(topRow+1);
			deferredSetFocus(getControl(0, currentRow), true);
		}
	}

	private void handlePreviousRowNavigation(InternalRow row) {
		if (!fireRequestRowChangeEvent()) {
			deferredSetFocus(getControl(currentRow, currentColumn), false);
			return;
		}
		
		if (currentRow == 0) {
			if (topRow == 0) {
				// We're at the beginning of the table; don't go anywhere
				return;
			}
			// We have to scroll backwards
			setTopRow(topRow-1);
			deferredSetFocus(getControl(row.getNumColumns()-1, 0), true);
		} else {
			deferredSetFocus(getControl(row.getNumColumns()-1, currentRow-1), true);
		}
	}

	private InternalRow currentRow() {
		InternalRow result = null;;
		Iterator rowsIter = rows.iterator();
		for (int i=0; i < currentRow && rowsIter.hasNext(); ++i) {
			result = (InternalRow) rowsIter.next();
		}
		return result;
	}

	private InternalRow getRowByNumber(int rowNumber) {
		InternalRow result = null;;
		Iterator rowsIter = rows.iterator();
		for (int i=0; i <= rowNumber && rowsIter.hasNext(); ++i) {
			result = (InternalRow) rowsIter.next();
		}
		return result;
	}
	
	private Control getControl(int column, int row) {
		InternalRow rowObject = getRowByNumber(row);
		if (rowObject == null) {
			throw new IndexOutOfBoundsException("Request for a nonexistent row");
		}
		Control result = rowObject.getColumnControl(column);
		return result;
	}

	private int getRowNumber(InternalRow row) {
		int rowNumber = 0;
		for (Iterator rowIter = rows.iterator(); rowIter.hasNext();) {
			InternalRow candidate = (InternalRow) rowIter.next();
			if (candidate == row) {
				return rowNumber;
			}
			++rowNumber;
		}
		return -1;
	}

	private void internalSetSelection(int column, int row, boolean rowChange) {
		Control toFocus = getControl(column, row);
		deferredSetFocus(toFocus, rowChange);
	}

	private void deferredSetTopRow(final int newTopRow) {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				setTopRow(newTopRow);
			}
		});
	}
	
	private void deferredSetFocus(final Control toFocus, final boolean rowChange) {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				toFocus.setFocus();
				if (rowChange) {
					fireRowChangedEvent();
				}
			}
		});
	}

}
