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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ve.sweet.metalogger.Logger;


public class InternalMRViewer extends Composite {

	private static final class SliderHolderLayout extends Layout {
		private final Point ZERO = new Point(1, 1);
		
		private Point size = null;

		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			if (flushCache) {
				size = null;
			}
			if (size == null) {
				if (!composite.getVisible()) {
					size = ZERO;
				} else {
					size = composite.getChildren()[0].computeSize(wHint, hHint);
					size.y = composite.getParent().getSize().y;
				}
			}
			return size;
		}

		protected void layout(Composite composite, boolean flushCache) {
			computeSize(composite, SWT.DEFAULT, SWT.DEFAULT, true);
			composite.getChildren()[0].setSize(size);
		}
	}

	private Composite sliderHolder = null;
	private Composite controlHolder = null;
	private Slider slider = null;
	
	private MultiRowViewer mrv;
	
	private boolean drawingLines;		// TODO: Not implemented yet
	private int[] weights;
	private int maxRowsVisible;
	private int numRowsInCollection;
	private int topRow;
	
	private int currentVisibleTopRow = 0;
	private int numRowsVisible = 0;
	private LinkedList rows = new LinkedList();
	private LinkedList spareRows = new LinkedList();
	
	private Constructor headerConstructor;
	private Constructor rowConstructor;
	private Control headerControl;
	private Control myHeader = null;
	private Control rowControl;
	
	public InternalMRViewer(Composite parent, int style) {
		super(parent, style);
		initialize();
		this.mrv = (MultiRowViewer) parent;

		drawingLines = mrv.isDrawingLines();
		weights = mrv.getWeights();
		maxRowsVisible = mrv.getMaxRowsVisible();
		numRowsInCollection = mrv.getNumRowsInCollection();
		topRow = mrv.getTopRow();
		
		headerConstructor = mrv.getHeaderConstructor();
		rowConstructor = mrv.getRowConstructor();
		headerControl = mrv.getHeaderControl();
		rowControl = mrv.getRowControl();
		
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
		sliderHolder.setLayout(new SliderHolderLayout());
		sliderHolder.setLayoutData(gd);
	}

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
		sliderHolder.getParent().layout(true);
	}
	
	public boolean isSliderVisible() {
		return sliderVisible;
	}
	
	public void removeHandlers() {
		// TODO Auto-generated method stub
		
	}

	protected void layoutControlHolder() {
		if (myHeader != null)
			layoutChild(myHeader);
		for (Iterator rowsIter = rows.iterator(); rowsIter.hasNext();) {
			Control row = (Control) rowsIter.next();
			layoutChild(row);
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
				return mrv.layoutHeaderOrRow(composite);
			}
			composite.layout(true);
			return composite.getSize().y;
		}
		return child.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
	}

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
		
		numRowsVisible = clientAreaHeight / rowControl.getSize().y;
		
		int displayableRows = numRowsInCollection - topRow;
		if (numRowsVisible > displayableRows) {
			numRowsVisible = displayableRows;
		}
		if (numRowsVisible > maxRowsVisible) {
			numRowsVisible = maxRowsVisible;
		}

		// Scroll the view so that the right number of row
		// objects are showing and they have the right data
		if (rows.size() - Math.abs(currentVisibleTopRow - topRow) > 0) {
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
			Control row = (Control) rowsIter.next();
			row.setBounds(0, topPosition, width, rowHeight);
			layoutChild(row);
			topPosition += rowHeight;
		}
	}

	private void scrollTop() {
		while (currentVisibleTopRow < topRow) {
			deleteRowAt(0);
			++currentVisibleTopRow;
		}
		while (currentVisibleTopRow > topRow) {
			insertRowAt(0);
			--currentVisibleTopRow;
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
			Control control = (Control) rowsIter.next();
			fireRefreshEvent(row, control);
			++row;
		}
	}

	private void insertRowAt(int position) {
		Control newControl = getNewControl();
		if (position > rows.size()) {
			position = rows.size();
		}
		rows.add(position, newControl);
		fireRefreshEvent(position, newControl);
	}
	
	private Control getNewControl() {
		if (spareRows.size() > 0) {
			Control recycledControl = (Control) spareRows.removeFirst();
			recycledControl.setVisible(true);
			return recycledControl;
		}
		return createInternalControl(controlHolder, rowConstructor);
	}

	private void deleteRowAt(int position) {
		Control control = (Control) rows.remove(position);
		control.setVisible(false);
		spareRows.addLast(control);
	}
	
	// Property getters/setters --------------------------------------------------------------

	public void setDrawingLines(boolean drawingLines) {
		this.drawingLines = drawingLines;
		controlHolder.redraw();
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

	public void setWeights(int[] weights) {
		this.weights = weights;
		layoutControlHolder();
	}

	public Control[] getVisibleChildRows() {
		return (Control[]) rows.toArray(new Control[rows.size()]);
	}

	public int getNumRowsVisible() {
		return numRowsVisible;
	}
	
	public void addRefreshContentProvider(IRefreshContentProvider listener) {
		mrv.refreshListeners.add(listener);
	}

	public void removeRefreshContentProvider(IRefreshContentProvider listener) {
		mrv.refreshListeners.remove(listener);
	}

	private void fireRefreshEvent(int offsetFromTopRow, Control rowControl) {
		for (Iterator refreshListenersIter = mrv.refreshListeners.iterator(); refreshListenersIter.hasNext();) {
			IRefreshContentProvider listener = (IRefreshContentProvider) refreshListenersIter.next();
			listener.refresh(mrv, offsetFromTopRow, rowControl);
		}
	}

}
