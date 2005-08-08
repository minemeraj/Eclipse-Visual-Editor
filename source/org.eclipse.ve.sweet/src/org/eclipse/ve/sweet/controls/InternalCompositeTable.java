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

/*
 * TODO:
 * - PgUp/PgDn don't save edited changes
 * - Insert into zeroth visible row always fails
 * - invisible inserts wind up creating "unbound" fields
 */

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
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ve.sweet.controls.internal.EmptyTablePlaceholder;
import org.eclipse.ve.sweet.controls.internal.ISelectableRegionControl;
import org.eclipse.ve.sweet.controls.internal.TableRow;
import org.eclipse.ve.sweet.metalogger.Logger;
import org.eclipse.ve.sweet.reflect.DuckType;


/** (non-API)
 * Class InternalCompositeTable.  This is the run-time CompositeTableControl.  It gets its prototype
 * row and (optional) header objects from its SWT parent, then uses them to implement an SWT table
 * control.
 * 
 * @author djo
 */
public class InternalCompositeTable extends Composite implements Listener {
	private Composite sliderHolder = null;
	private Composite controlHolder = null;
	private Slider slider = null;
	private EmptyTablePlaceholder emptyTablePlaceholder = null;
	
	private CompositeTable parent;
	
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
			TableRow row = (TableRow) rowsIter.next();
			row.dispose();
		}
	}

	// Row object layout --------------------------------------------------------------------------
	
	protected void layoutControlHolder() {
		if (myHeader != null)
			layoutChild(myHeader);
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			TableRow row = (TableRow) rowsIter.next();
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

	void updateVisibleRows() {
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
		
		// Make sure we have something to lay out to begin with
		if (numRowsInCollection > 0) {
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
		}
		
		// Lay out the header and rows correctly in the display
		int width = controlHolder.getSize().x;
		
		// First, the header...
		if (myHeader != null) {
			myHeader.setBounds(0, 0, width, headerHeight);
		}
		
		// Make sure we have rows to lay out...
		if (numRowsInCollection < 1) {
			return;
		}
		
		// Now the rows.
		int rowHeight = 50;
		rowHeight = rowControl.getSize().y;
		
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			TableRow row = (TableRow) rowsIter.next();
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

	void refreshAllRows() {
		int row=0;
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			TableRow rowControl = (TableRow) rowsIter.next();
			fireRefreshEvent(topRow + row, rowControl.getRowControl());
			++row;
		}
	}

	private void insertRowAt(int position) {
		TableRow newRow = getNewRow();
		if (position > rows.size()) {
			position = rows.size();
		}
		rows.add(position, newRow);
		fireRefreshEvent(currentVisibleTopRow + position, newRow.getRowControl());
	}
	
	private void deleteRowAt(int position) {
		TableRow row = (TableRow) rows.remove(position);
		row.setVisible(false);
		spareRows.addLast(row);
	}
	
	private TableRow getNewRow() {
		if (spareRows.size() > 0) {
			TableRow recycledRow = (TableRow) spareRows.removeFirst();
			recycledRow.setVisible(true);
			return recycledRow;
		}
		return new TableRow(this, createInternalControl(controlHolder, rowConstructor));
	}

	// Property getters/setters --------------------------------------------------------------

	public Control getHeaderControl() {
		return headerControl;
	}

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
	
	public int getTopRow() {
		return topRow;
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

	private void fireRefreshEvent(int positionInCollection, Control rowControl) {
		if (numRowsInCollection < 1) {
			return;
		}
		for (Iterator refreshListenersIter = parent.contentProviders.iterator(); refreshListenersIter.hasNext();) {
			IRowContentProvider listener = (IRowContentProvider) refreshListenersIter.next();
			listener.refresh(parent, positionInCollection, rowControl);
		}
	}
	
	// Event Handling -----------------------------------------------------------------------------

	private boolean needToRequestRC=true;
	
	public void keyPressed(TableRow sender, KeyEvent e) {
		if ((e.stateMask & SWT.CONTROL) != 0) {
			switch (e.keyCode) {
			case SWT.HOME:
				if (topRow <= 0) {
					return;
				}
				
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				deselect(e.widget);
				
				// If the focus is already in the top visible row, we will need to explicitly 
				// fire an arrive event.
				boolean needToArrive = true;
				if (currentRow > 0) {
					needToArrive = false;
				}
				
				setTopRow(0);
				
				if (needToArrive) {
					internalSetSelection(currentColumn, 0, true);
				} else {
					internalSetSelection(currentColumn, 0, false);
				}
				return;
			case SWT.END:
				if (topRow + numRowsVisible < numRowsInCollection) {
					if (!fireRequestRowChangeEvent()) {
						return;
					}
					needToRequestRC = false;
					
					deselect(e.widget);
					
					// If the focus is already in the last visible row, we will need to explicitly 
					// fire an arrive event.
					needToArrive = true;
					if (currentRow < numRowsVisible-1) {
						needToArrive = false;
					}
					
					setTopRow(numRowsInCollection - numRowsVisible);
					
					if (needToArrive) {
						internalSetSelection(currentColumn, numRowsVisible-1, true);
					} else {
						internalSetSelection(currentColumn, numRowsVisible-1, false);
					}
				}
				return;
			case SWT.DEL:
				if (fireDeleteEvent()) {
					// We know the object is gone if we made it here, so now refresh the display...
					--numRowsInCollection;
					
					// If we deleted the last row in the list
					if (currentRow >= numRowsVisible-1) {
						// If that wasn't the last row in the collection, move the focus
						if (numRowsInCollection > 0) {

							// If we're only displaying one row, scroll first
							if (currentRow < 1) {
								needToRequestRC = false;
								deleteRowAt(currentRow);
								setTopRow(topRow-1);
								internalSetSelection(currentColumn, currentRow, true);
							} else {
								needToRequestRC = false;
								internalSetSelection(currentColumn, currentRow-1, false);
								Display.getCurrent().asyncExec(new Runnable() {
									public void run() {
										deleteRowAt(currentRow+1);
										updateVisibleRows();
									}
								});
							}
						} else {
							// Otherwise, show the placeholder object and give it focus
							deleteRowAt(currentRow);
							emptyTablePlaceholder = new EmptyTablePlaceholder(controlHolder, SWT.NULL);
							emptyTablePlaceholder.setFocus();
						}
					} else {
						// else, keep the focus where it was
						deleteRowAt(currentRow);
						updateVisibleRows();
						internalSetSelection(currentColumn, currentRow, true);
					}
				}
				return;
			default:
				return;
			}
		}
		switch (e.keyCode) {
		case SWT.INSERT:
			// If no insertHandler has been registered, bail out 
			if (parent.insertHandlers.size() < 1) {
				return;
			}
			
			// Make sure we can leave the current row
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			needToRequestRC = false;
			
			// Insert the new object
			int newRowPosition = fireInsertEvent();
			if (newRowPosition < 0) {
				// This should never happen, but...
				return;
			}
			
			// If the placeholder is currently visible, remove it.
			if (emptyTablePlaceholder != null) {
				emptyTablePlaceholder.dispose();
				emptyTablePlaceholder = null;
			}
			
			// If the current widget has a selection, deselect it
			deselect(e.widget);
			
			// If the new row is in the visible space, refresh it
			if (topRow <= newRowPosition && 
					numRowsVisible > newRowPosition - topRow) {
				insertRowAt(newRowPosition - topRow);
				++numRowsInCollection;
				updateVisibleRows();
				int newRowNumber = newRowPosition - topRow;
				if (newRowNumber != currentRow) {
					internalSetSelection(currentColumn, newRowNumber, false);
				} else {
					internalSetSelection(currentColumn, newRowNumber, true);
				}
				return;
			} else {
				++numRowsInCollection;

				// If the new row is above us, scroll up to it
				if (newRowPosition < topRow + currentRow) {
					setTopRow(newRowPosition);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							updateVisibleRows();
							if (currentRow != 0) {
								internalSetSelection(currentColumn, 0, false);
							} else {
								internalSetSelection(currentColumn, 0, true);
							}
						}
					});
				} else {
					// If we're appending
					if (numRowsInDisplay > numRowsVisible) {
						updateVisibleRows();
						int newRowNumber = newRowPosition - topRow;
						if (newRowNumber != currentRow) {
							internalSetSelection(currentColumn, newRowNumber, false);
						} else {
							internalSetSelection(currentColumn, newRowNumber, true);
						}
					} else {
						// It's somewhere in the middle below us; scroll down to it
						setTopRow(newRowPosition-numRowsVisible+1);
						int newRowNumber = numRowsVisible-1;
						if (newRowNumber != currentRow) {
							internalSetSelection(currentColumn, newRowNumber, false);
						} else {
							internalSetSelection(currentColumn, newRowNumber, true);
						}
					}
				}
				
			}
			
			return;
		case SWT.ARROW_UP:
			if (currentRow > 0) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				deselect(e.widget);
				
				internalSetSelection(currentColumn, currentRow-1, false);
				return;
			}
			if (topRow > 0) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				deselect(e.widget);
				
				setTopRow(topRow - 1);
				internalSetSelection(currentColumn, currentRow, true);
				return;
			}
			return;
		case SWT.ARROW_DOWN:
			if (currentRow < numRowsVisible-1) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				deselect(e.widget);
				
				internalSetSelection(currentColumn, currentRow+1, false);
				return;
			}
			if (topRow + numRowsVisible < numRowsInCollection) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				deselect(e.widget);
				
				setTopRow(topRow + 1);
				internalSetSelection(currentColumn, currentRow, true);
				return;
			}
			return;
		case SWT.PAGE_UP:
			if (topRow > 0) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
				int newTopRow = topRow - numRowsInDisplay;
				if (newTopRow < 0) {
					newTopRow = 0;
				}
				setTopRow(newTopRow);
				internalSetSelection(currentColumn, currentRow, true);
			}
			return;
		case SWT.PAGE_DOWN:
			if (topRow + numRowsVisible < numRowsInCollection) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				needToRequestRC = false;
				
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

	public void keyTraversed(TableRow sender, TraverseEvent e) {
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

	private SelectionListener sliderSelectionListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			if (slider.getSelection() == topRow) {
				return;
			}
			
			if (!fireRequestRowChangeEvent()) {
				slider.setSelection(topRow);
				return;
			}

			deselect(getControl(currentColumn, currentRow));
			
			setTopRow(slider.getSelection());
			deferredSetFocus(getControl(currentColumn, currentRow), true);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};
	
	// Scroll wheel handling...
	public void handleEvent(Event event) {
		
		if (event.count > 0) {
			if (topRow > 0) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				deselect(getControl(currentColumn, currentRow));
				setTopRow(topRow - 1);
				deferredSetFocus(getControl(currentColumn, currentRow), true);
			}
		} else {
			if (topRow < numRowsInCollection - numRowsVisible) {
				if (!fireRequestRowChangeEvent()) {
					return;
				}
				deselect(getControl(currentColumn, currentRow));
				setTopRow(topRow + 1);
				deferredSetFocus(getControl(currentColumn, currentRow), true);
			}
		}
	}
	
	public void focusLost(TableRow sender, FocusEvent e) {
	}
	
	public void focusGained(TableRow sender, FocusEvent e) {
		boolean rowChanged = false;
		if (getRowNumber(sender) != currentRow) {
			if (needToRequestRC) {
				if (!fireRequestRowChangeEvent()) {
					// Go back if we're not allowed to be here
					deferredSetFocus(getControl(currentColumn, currentRow), false);
				}
			} else {
				needToRequestRC = true;
			}
			rowChanged = true;
		}

		currentRow = getRowNumber(sender);
		currentColumn = sender.getColumnNumber((Control)e.widget);
		
		if (rowChanged)
			fireRowArriveEvent();
	}
	
	// Event Firing -------------------------------------------------------------------------------

	private void fireRowArriveEvent() {
		if (rows.size() < 1) {
			return;
		}
		for (Iterator rowChangeListenersIter = parent.rowListeners.iterator(); rowChangeListenersIter.hasNext();) {
			IRowListener listener = (IRowListener) rowChangeListenersIter.next();
			listener.arrive(parent, topRow+currentRow, currentRow().getRowControl());
		}
	}

	private boolean fireRequestRowChangeEvent() {
		if (rows.size() < 1) {
			return true;
		}
		if (currentRow > rows.size()-1) {
			// (if the other row is already gone)
			return true;
		}
		for (Iterator rowChangeListenersIter = parent.rowListeners.iterator(); rowChangeListenersIter.hasNext();) {
			IRowListener listener = (IRowListener) rowChangeListenersIter.next();
			if (!listener.requestRowChange(parent, topRow+currentRow, currentRow().getRowControl())) {
				return false;
			}
		}
		fireRowDepartEvent();
		return true;
	}

	private void fireRowDepartEvent() {
		if (rows.size() < 1) {
			return;
		}
		for (Iterator rowChangeListenersIter = parent.rowListeners.iterator(); rowChangeListenersIter.hasNext();) {
			IRowListener listener = (IRowListener) rowChangeListenersIter.next();
			listener.depart(parent, topRow+currentRow, currentRow().getRowControl());
		}
	}

	private boolean fireDeleteEvent() {
		if (parent.deleteHandlers.size() < 1) {
			return false;
		}
		
		int absoluteRow = topRow + currentRow;
		for (Iterator deleteHandlersIter = parent.deleteHandlers.iterator(); deleteHandlersIter.hasNext();) {
			IDeleteHandler handler = (IDeleteHandler) deleteHandlersIter.next();
			if (!handler.canDelete(absoluteRow)) {
				return false;
			}
		}
		for (Iterator deleteHandlersIter = parent.deleteHandlers.iterator(); deleteHandlersIter.hasNext();) {
			IDeleteHandler handler = (IDeleteHandler) deleteHandlersIter.next();
			handler.deleteRow(absoluteRow);
		}
		return true;
	}
	
	private int fireInsertEvent() {
		if (parent.insertHandlers.size() < 1) {
			return -1;
		}
		
		for (Iterator insertHandlersIter = parent.insertHandlers.iterator(); insertHandlersIter.hasNext();) {
			IInsertHandler handler = (IInsertHandler) insertHandlersIter.next();
			int resultRow = handler.insert(topRow+currentRow);
			if (resultRow >= 0) {
				return resultRow;
			}
		}
		
		return -1;
	}

	// Event Handling, utility methods ------------------------------------------------------------
	
	private void deselect(Widget widget) {
		if (DuckType.instanceOf(ISelectableRegionControl.class, widget)) {
			ISelectableRegionControl control = (ISelectableRegionControl) DuckType.implement(ISelectableRegionControl.class, widget);
			control.setSelection(0, 0);
		}
	}
	
	private void handleNextRowNavigation(TableRow row) {
		if (currentRow < numRowsVisible-1) {
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			needToRequestRC = false;
			
			deselect(getControl(currentColumn, currentRow));
			
			deferredSetFocus(getControl(0, currentRow+1), false);
		} else {
			if (topRow + numRowsVisible >= numRowsInCollection) {
				// We're at the end; don't go anywhere
				return;
			}
			// We have to scroll forwards
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			needToRequestRC = false;
			
			deselect(getControl(currentColumn, currentRow));
			
			setTopRow(topRow+1);
			deferredSetFocus(getControl(0, currentRow), true);
		}
	}

	private void handlePreviousRowNavigation(TableRow row) {
		if (currentRow == 0) {
			if (topRow == 0) {
				// We're at the beginning of the table; don't go anywhere
				return;
			}
			// We have to scroll backwards
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			needToRequestRC = false;
			
			deselect(getControl(currentColumn, currentRow));
			
			setTopRow(topRow-1);
			deferredSetFocus(getControl(row.getNumColumns()-1, 0), true);
		} else {
			if (!fireRequestRowChangeEvent()) {
				return;
			}
			needToRequestRC = false;
			
			deselect(getControl(currentColumn, currentRow));
			
			deferredSetFocus(getControl(row.getNumColumns()-1, currentRow-1), false);
		}
	}

	private TableRow currentRow() {
		if (currentRow > rows.size()-1) {
			return null;
		}
		return (TableRow) rows.get(currentRow);
	}

	private TableRow getRowByNumber(int rowNumber) {
		if (rowNumber > rows.size()-1) {
			return null;
		}
		return (TableRow) rows.get(rowNumber);
	}
	
	private Control getControl(int column, int row) {
		TableRow rowObject = getRowByNumber(row);
		if (rowObject == null) {
			throw new IndexOutOfBoundsException("Request for a nonexistent row");
		}
		Control result = rowObject.getColumnControl(column);
		return result;
	}

	private int getRowNumber(TableRow row) {
		int rowNumber = 0;
		for (Iterator rowIter = rows.iterator(); rowIter.hasNext();) {
			TableRow candidate = (TableRow) rowIter.next();
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
					fireRowArriveEvent();
				}
			}
		});
	}

}
