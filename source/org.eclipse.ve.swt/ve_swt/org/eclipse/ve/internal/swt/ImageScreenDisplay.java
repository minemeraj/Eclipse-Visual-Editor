/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: ImageScreenDisplay.java,v $
 *  $Revision: 1.6 $  $Date: 2005-12-12 21:29:30 $ 
 */
import java.util.ArrayList;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.ColorConstants;

/**
 * This class extends Canvas and can layout images as if it was a table
 * Every image can have a label associated with it(which goes underneath the image)
 * as well as a ToolTip.
 *
 * If either the horizontal or vertical scrollbar is not created, then
 * the number of columns and rows are calculated automatically according to the size
 * of the displaying window.
 *
 * If both the horizontal and vertical scrollbar are created, then the number of columns
 * and rows can be set.
 */
public class ImageScreenDisplay extends Canvas {
	/*
	 * Number of columns.
	 */
	private int numOfCols = 1;

	/*
	 * Number of rows.
	 */
	private int numOfRows = 1;

	/*
	 * Suggested number of cols. If there is an hscroll, it will be numOfCols when display width is less than
	 * this number. Used to hint to preferred size.
	 */
	private int suggestNumOfCols;

	/*
	 * Suggested number of rows. Used as hint to preferred size. Number of rows will grow as needed.
	 */
	private int suggestNumOfRows;

	/*
	 * Grid width -- width of a single image box (including the space 
	 * taken by text and offset) - sent in.
	 */
	private int width; //grid width and height
	/*
	 * Grid height -- height of a single image box (including the space 
	 * taken by text and offset) - sent in.
	 */
	private int height;

	/*
	 * current row (i.e. top row visible)
	 */
	private int currentRow = 0;

	/*
	 * current col (i.e. left column visible)
	 */
	private int currentCol = 0;

	/*
	 * current index of first image in image array
	 */
	private int currentImageIndex = 0;

	/*
	 * Array of visible images.
	 */
	private Image[] visibleImages = EMPTY_IMAGES;

	private static Image[] EMPTY_IMAGES = new Image[0];

	/*
	 * index of last selected image, -1 indicates none
	 */
	private int lastSelectedIndex = -1;

	/*
	 * index of last ToolTip, -1 indicates none
	 */
	private int lastToolTipIndex = -1;

	/*
	 * the spacing difference between the grid boundary and the image
	 */
	private int offset;

	/*
	 * the spacing difference between the focus border and the image
	 */
	private int borderOffset;

	/*
	 * List of IPaths for the images being displayed.
	 */
	private ArrayList paths = new ArrayList();

	/*
	 * Are the paths relative to the workspace root.
	 */
	private boolean workspacePaths;

	/*
	 * The workspace root.
	 */
	private IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

	/*
	 * boolean that indicates whether the GridTable is selectale or not
	 */
	private boolean selectable;

	public interface SelectionListener {
		/**
		 * Sent when selection occurs in the control.
		 *
		 * @param selectedIndex The index of the selected entry (-1 if none)
		 */
		public void imageSelected(int selectedIndex);

		/**
		 * Sent when default selection occurs in the control.
		 * Typically this is double-click on an image (the 
		 * imageSelected will be sent before this).
		 */
		public void imageDefaultSelected();
	}

	/*
	 * This is a one listener list. This is ok because it is only for IconController.
	 */
	private SelectionListener selectionListener;

	/*
	 * Whether tooltip text should be shown or not.
	 */
	private boolean enableToolTip;

	/**
	 * Constructs a new gridTable.
	 * @param parent the parent composite
	 * @param style the style
	 * @param cols number of columns - Used to calculate min width if no hscroll bar, else actual number of columns
	 * @param rows number of rows - Used to calculate min height. If no vscroll bar, then max number depends upon size of area.
	 * @param width width of each grid image
	 * @param height height of each grid image
	 * @param selectable makes the gridTable selectable/non selectable based on this variable
	 * @param enableToolTip enables/disables the tooltip based on this variable
	 * @param offset spacing between grid boundary and the image (?)
	 * @param borderOfset spacing between the focus border and the image
	 */
	public ImageScreenDisplay(
		Composite parent,
		int style,
		int rows,
		int cols,
		int width,
		int height,
		boolean enableSelectable,
		boolean enableToolTip,
		int offset,
		int borderOffset) {
		super(parent, style);
		this.suggestNumOfCols = cols;
		this.suggestNumOfRows = rows;
		this.height = height;
		this.width = width;
		this.selectable = enableSelectable;
		this.offset = offset;
		this.borderOffset = borderOffset;
		this.enableToolTip = enableToolTip;
		initialize();
		resetScrollBarProperties();
		addListeners();
	}

	/**
	 * Set whether workspace relative or file-system for images.
	 */
	public void setIsWorkspacePaths(boolean workspacePaths) {
		this.workspacePaths = workspacePaths;
	}

	/**
	 * Adds an ImageItem to the GridTable
	 */
	public void add(final IPath filepath) {
		paths.add(filepath);
		resetRowsAndCols();

		int index = paths.size() - 1;
		if (verticalIndexVisible(index) || horizontalIndexVisible(index)) {
			getDisplay().syncExec(new Runnable() {
				public void run() {
					try{
					setupImages();
					}
					catch(SWTException e){						
					}
				}
			});
		}
	}
	/**
	 * Adds various listeners to this widget
	 */
	private void addListeners() {

		//Adding horizontal scrollbar selection listener if it exists
		final ScrollBar hBar = getHorizontalBar();
		if (hBar != null) {
			hBar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int newCol = hBar.getSelection();
					if (newCol != currentCol)
						scrollTo(currentRow, newCol, false);
				}
			});
		}

		//Adding vertical scrollbar selection listener if it exists
		final ScrollBar vBar = getVerticalBar();
		if (vBar != null) {
			vBar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int newRow = vBar.getSelection();
					if (newRow != currentRow) {
						scrollTo(newRow, currentCol, false);
					}
				}
			});
		}

		//Adding resize listeners, required because when the widget resizes, number of rows/col
		//needs to be recalculated
		addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				resetRowsAndCols();
				setupImages();
			}
		});

		//Adding the paint listener
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				drawImagesAndLabels(event);
			}
		});

		// Add the focus listener
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				redraw(); // So we have the focus indicator
			}

			public void focusLost(FocusEvent e) {
				redraw(); // So we get rid of the focus indicator
			}
		});

		//adding the mouse listener if the GridTable is set to be selectable	
		if (selectable)
			addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				int imageIndex = findImageIndexFromPosition(e.x, e.y);
				if (imageIndex >= 0) {
					//select current selection
					setSelected(imageIndex);
				}
			}

			public void mouseDoubleClick(MouseEvent e) {
				if (selectionListener != null)
					selectionListener.imageDefaultSelected();
			}
		});

		//adding mouse movement listener for showing tooltip if tooltip is enabled
		if (enableToolTip)
			addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				int imageIndex = findImageIndexFromPosition(e.x, e.y);
				// if the ToolTip was already set on the same grid index, return
				// don't want to reset the ToolTip with every mouse movement
				if (lastToolTipIndex != imageIndex) {
					if (imageIndex == -1) //remove the tooltip
						setToolTipText(""); //$NON-NLS-1$
					else
						setToolTipText(((IPath) paths.get(imageIndex)).toString());
					lastToolTipIndex = imageIndex;
				}
			}
		});

		// adding the key listener for accessibility purposes (thus allowing navigation)
		// of the different images displayed through the keyboard
		addKeyListener(new KeyboardNavigationListener());

		// Add this to tell it that we want escape and return, tab, and backtab to continue being sent up
		// the heirarchy so that the shell can handle them.  
		addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
					|| e.detail == SWT.TRAVERSE_RETURN
					|| e.detail == SWT.TRAVERSE_TAB_NEXT
					|| e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = true;
				}
			}
		});

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (visibleImages != null) {
					disposeImages(0, visibleImages.length);
					visibleImages = EMPTY_IMAGES;
				}
			}
		});
	}

	/*
	 * Setup the images for the current row/col and visible area
	 * NOTE: This should be called only from within UI thread.
	 */
	private void setupImages() {
		int firstVisIndex = getFirstVisibleIndex();
		int visCols = getNumOfVisibleCols();
		int visRows = getNumOfVisibleRows();
		int newVisSize = visCols * visRows;
		Image[] newImages = null;
		int index = 0; // Used index within newImages
		int imageIndex = firstVisIndex; // Use index with path list.

		Rectangle redrawRect = null;

		Display display = getDisplay();

		// Find the first visible null, this way we won't access beyond it.
		int firstVisibleNull = 0;
		for (; firstVisibleNull < visibleImages.length; firstVisibleNull++)
			if (visibleImages[firstVisibleNull] == null)
				break;

		// When done, the completion state will be:
		//	newImages will be created if needed
		//	index will be the next spot in newImages that needs to be filled into
		//	imageIndex will be the next spot from the paths list to load from
		//	currentImageIndex will be the new first visible index
		//	visibleImages will be the new array of images if changed
		//	redraw will be set the area to redraw if any change was made
		if (firstVisIndex != currentImageIndex) {
			newImages = new Image[newVisSize];
			// We've actually have moved so do complicated stuff.
			if (firstVisIndex < currentImageIndex) {
				// Moved back, so fill with new up to currentImageIndex or end of current visible, whichever is first
				for (; imageIndex < currentImageIndex && index < newImages.length; imageIndex++, index++) {
					newImages[index] = getImage(display, imageIndex);
				}

				if (imageIndex == currentImageIndex) {
					// We've reached the currentImageIndex, so move what we can reuse out of it.
					int move = Math.min(newImages.length - index, firstVisibleNull);
					System.arraycopy(visibleImages, 0, newImages, index, move);
					index += move;
					imageIndex += move;
					disposeImages(move, firstVisibleNull); // Get rid of the unused ones.
				} else
					disposeImages(0, firstVisibleNull); // Didn't use any of the old, get rid of them.

				visibleImages = newImages;
				currentImageIndex = firstVisIndex;
				redrawRect = getClientArea();	// Everything has moved, so redraw entire area.
			} else {
				// Moved forward, so fill with new up to currentImageIndex or end of current visible, whichever is first	
				int startOld = firstVisIndex - currentImageIndex; // Where in old list can we reuse for start of new list
				if (startOld < firstVisibleNull) {
					int move = Math.min(firstVisibleNull - startOld, newImages.length);
					System.arraycopy(visibleImages, startOld, newImages, index, move);
					disposeImages(0, startOld); // Get rid of those before.
					disposeImages(startOld + move, firstVisibleNull); // Get rid of those after.
					index += move;
					imageIndex += move;
				} else
					disposeImages(0, firstVisibleNull);
				visibleImages = newImages;
				currentImageIndex = firstVisIndex;
				redrawRect = getClientArea();	// Everything has moved, so redraw entire area.
			}
		} else {
			// Didn't move, but may have changed visible range.
			if (visibleImages.length != newVisSize) {
				newImages = new Image[newVisSize];
				int move = Math.min(firstVisibleNull, newVisSize);
				System.arraycopy(visibleImages, index, newImages, index, move);
				index += move;
				imageIndex += move;
				if (index < firstVisibleNull)
					disposeImages(index, firstVisibleNull);
				visibleImages = newImages;
				currentImageIndex = firstVisIndex;
				redrawRect = getClientArea();	// Everything may have moved, so redraw entire area.
			} else {
				// Didn't change size, but we may have added some paths. We never remove paths piece-meal, they always completely
				// removed through removeAll.
				// So find first null image, this is where we start filling in new ones.
				index += firstVisibleNull;
				imageIndex += firstVisibleNull;
			}
		}

		// Now in all three cases, there may be news ones to add.
		// This is done via imageIndex < paths length and index < visibleImages length
		int pathsLength = itemsSize();
		for (; index < visibleImages.length && imageIndex < pathsLength; index++, imageIndex++) {
			visibleImages[index] = getImage(display, imageIndex);
			// Add in each of the bounds of the image at the specified index to the redraw area.
			Rectangle imageBounds = getImageBounds(imageIndex);
			if (redrawRect == null)
				redrawRect = imageBounds;
			else
				redrawRect.add(imageBounds);	// Add the image bounds into the redraw area.
		}

		if (redrawRect != null)
			redraw(redrawRect.x, redrawRect.y, redrawRect.width, redrawRect.height, false);
	}

	private Image getImage(Display display, int imageIndex) {
		String filename = getFileName((IPath) paths.get(imageIndex));
		if (filename != null)
			try {
				Image img = new Image(display, filename);
				Rectangle bounds = img.getBounds();
				int newWidth = (bounds.width < width) ? bounds.width : width;
				int newHeight = (bounds.height < height) ? bounds.height : height;
				if (newWidth != bounds.width || newHeight != bounds.height) {
					Image tnImage = new Image(display, newWidth, newHeight);
					GC tnGC = new GC(tnImage);
					tnGC.drawImage(img, 0, 0, bounds.width, bounds.height, 0, 0, newWidth, newHeight);
					img.dispose();
					img = tnImage;
					tnGC.dispose();
				}
				return img;
			} catch (SWTException e) {
				return ImageController.warnIcon;
			} catch (OutOfMemoryError e) {
				return ImageController.noMemoryIcon;
			}
		return null;
	}

	private String getFileName(IPath path) {
		if (workspacePaths) {
			IResource res = workspaceRoot.findMember(path);
			if (res != null)
				return res.getLocation().toOSString();
		} else
			return path.toOSString();

		return null;
	}

	/*
	 * Scroll to the row/col sent it
	 */
	private void scrollTo(int row, int col, boolean holdSetup) {
		if (currentRow != row || currentCol != col) {
			currentRow = row;
			currentCol = col;
			resetScrollBarProperties();
			if (!holdSetup)
				setupImages();
		}
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when the receiver's selection changes, by sending
	 * it one of the messages defined in the <code>SelectionListener</code>
	 * interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the selection changes.
	 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
	 * </p>
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		selectionListener = listener;
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when the receiver's selection changes.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		selectionListener = null;
	}

	/**
	 * Calculates the grid index, grid index starts at zero and increments horizontally
	 */
	public static int calculateGridIndex(int row, int col, int numberOfCols) {
		return row * numberOfCols + col;
	}

	/**
	 * draws the image stored in ImageItem at the specified grid index
	 */
	private void drawImage(int gridIndex, GC gc) {

		int imageIndex = gridIndex - currentImageIndex;
		if (currentImageIndex != -1 && imageIndex < visibleImages.length) {
			Rectangle imageDrawingBounds = getImageBounds(gridIndex);
			if (imageDrawingBounds == null || !gc.getClipping().intersects(imageDrawingBounds)) {
				return; // No image at this index or we aren't within the clipping region of the gc, so don't bother painting.
			}

			Image img = visibleImages[imageIndex];
			if (img != null)
				gc.drawImage(
					img,
					0,
					0,
					img.getBounds().width,
					img.getBounds().height,
					imageDrawingBounds.x,
					imageDrawingBounds.y,
					imageDrawingBounds.width,
					imageDrawingBounds.height);
		}
	}
	/**
	 * Draw images and texts that should be displayed in the  GridTable
	 * Invoked by the SWT.Paint listener
	 */
	private void drawImagesAndLabels(PaintEvent event) {
		// Technically we should only paint within the area given by the event.
		GC gc = event.gc;
		int lastRow = getLastVisibleRow();
		int lastCol = getLastVisibleCol();
		//traverse through the grid indexes of grids that are currently visible
		//and draw the images and labels that correspond to the grid index
		for (int row = getFirstVisibleRow(); row <= lastRow; row++) {
			for (int col = getFirstVisibleCol(); col <= lastCol; col++) {
				//index of the grid
				int index = row * getNumOfCols() + col;
				if (index >= itemsSize())
					break;
				//draw image
				drawImage(index, gc);
			}
		}
		//set focus to what was selected before
		drawCursor(lastSelectedIndex, gc);
	}

	/**
	 * determins which image is selected based on the x and y component of the mouse click
	 */
	public int findImageIndexFromPosition(int posX, int posY) {
		//determine the row and column of which the mouse click took place
		int col = currentCol + ((posX - borderOffset) / width);
		int row = currentRow + ((posY - borderOffset) / height);
		//index of the grid which the mouse click took place, starts at 0
		int index = calculateGridIndex(row, col, numOfCols);
		if (index < itemsSize()) {
			if (getDisplayBounds(index).contains(new Point(posX, posY)))
				return index;
		}
		return -1; // no image found
	}

	/**
	 * Returns the bounds for drawing the image (the max bounds for the image in grid)
	 */
	private Rectangle getDisplayBounds(int index) {
		Point pos = getGridPosition(index);
		return new Rectangle(pos.x + offset, pos.y + offset, width - 2 * offset, height - 2 * offset);
	}
	/**
	 * Gets the first visible column number in the display area, column number starts at 0.
	 */
	public int getFirstVisibleCol() {
		return currentCol;
	}
	/**
	 * Gets the first visible row number in the display area, row number starts at 0.
	 */
	public int getFirstVisibleRow() {
		return currentRow;
	}
	/**
	 * returns the position of the grid according to the index
	 */
	private Point getGridPosition(int gridIndex) {
		// if gridIndex is greater or equal to the number of grids available
		if (gridIndex >= getNumOfGrids())
			gridIndex = 0;

		int rowNumber = gridIndex / numOfCols; //row number starts at 0;
		int colNumber = gridIndex % numOfCols; //col number starts of at 0

		int xPosition = (width * (colNumber - currentCol)) + borderOffset; // To adjust for left border of the focus box
		int yPosition = (height * (rowNumber - currentRow)) + borderOffset; // To adjust for top border of the focus box
		return new Point(xPosition, yPosition);
	}
	/**
	 * 
	 * returns the size of the grid in GridTable
	 */
	public Point getGridSize() {
		return new Point(width, height);
	}
	/**
	 * gets the bounds of the actual image to be displayed
	 * The new image size will always be smaller or equal to the original image size.
	 * The new image size is calculated as follows.
	 * if image width <= display width and image height <= display height then ratio = 1,
	 * otherwise, 	ratio1 = image width / display width
	 * 				ratio2 = image height / display height
	 * the bigger of the two ratios will determine the final ratio of how much the
	 * image is going to be shrinked by.
	 * 
	 * It will be a minimum of 5 pixels.
	 */
	private Rectangle getImageBounds(int index) {
		int visibleIndex = index - currentImageIndex;
		if (0 <= visibleIndex && visibleIndex < visibleImages.length) {
			Image img = visibleImages[visibleIndex];
			if (img != null) {
				Rectangle bounds = img.getBounds();
				Point imageSize = new Point(bounds.width, bounds.height);

				//the bounds for the displayable image area
				Rectangle displayBounds = getDisplayBounds(index);

				double ratio;
				if (imageSize.x <= displayBounds.width && imageSize.y <= displayBounds.height) {
					ratio = 1;
				} else {
					double hRatio = imageSize.x / (double) displayBounds.width;
					double vRatio = imageSize.y / (double) displayBounds.height;
					ratio = (hRatio > vRatio) ? hRatio : vRatio;
				}

				//calculates the new sizes using the ratio
				int newWidth = Math.max((int) Math.floor(imageSize.x / ratio), 5);	// A min size of 5.
				int newHeight = Math.max((int) Math.floor(imageSize.y / ratio), 5);
				//calculates the new x,y positon so that the image is place in the middle
				// of the grid
				int newXPosition = displayBounds.x + (displayBounds.width - newWidth) / 2;
				int newYPosition = displayBounds.y + (displayBounds.height - newHeight) / 2;
				return new Rectangle(newXPosition, newYPosition, newWidth, newHeight);
			}
		}

		return null;
	}
	/**
	 * 
	 * returns the displayed image size (area for the grid for the image)
	 */
	public Point getImageSize() {
		return new Point(width - 2 * offset, height - 2 * offset);
	}
	/**
	 * Gets the last visible column number in the display area(index starts at 0)
	 */
	private int getLastVisibleCol() {
		return getFirstVisibleCol() + getNumOfVisibleCols() - 1;
	}
	/**
	 * Gets the last visible row number in the display area(index starts at 0)
	 */
	private int getLastVisibleRow() {
		return getFirstVisibleRow() + getNumOfVisibleRows() - 1;
	}
	/**
	 * gets the current number of columns
	 */
	public int getNumOfCols() {
		return numOfCols;
	}
	/**
	 * gets the current number of grids
	 */
	public int getNumOfGrids() {
		return numOfCols * numOfRows;
	}
	/**
	 * gets the current numer of rows
	 */
	public int getNumOfRows() {
		return numOfRows;
	}
	/**
	 * Gets the current number of visible columns in the displayable area
	 */
	public int getNumOfVisibleCols() {
		double cols = (getClientArea().width - borderOffset) / (double) width;
		return (getHorizontalBar() != null) ? (int) Math.ceil(cols) : (int) cols; // If no hbar, then don't allow partials
	}
	/**
	 * Gets the current number of visible rows in the displayable area
	 */
	public int getNumOfVisibleRows() {
		double rows = (getClientArea().height - borderOffset) / (double) height;
		return (getVerticalBar() != null) ? (int) Math.ceil(rows) : (int) rows; // If no vbar, then don't allow partials
	}

	public IPath getSelectedImagePath() {
		if (lastSelectedIndex < 0)
			return null;
		return (IPath) paths.get(lastSelectedIndex);
	}

	/**
	 * gets the total height the grid table including the area that isn't currently showing
	 */
	private int getTotalGridHeight() {
		return numOfRows * height;
	}
	/**
	 * gets the total width the grid table including the areas not currently showing
	 */
	private int getTotalGridWidth() {
		return numOfCols * width;
	}

	/**
	 * performs certain initilizations on the GridTable
	 */
	private void initialize() {
		setBackground(ColorConstants.white);
	}

	/**
	 * removes all ImageItems
	 */
	public void removeAll() {
		paths.clear();
		lastSelectedIndex = -1;
		ScrollBar vBar = getVerticalBar();
		if (vBar != null) {
			vBar.setSelection(0);
		}
		currentRow = 0;

		ScrollBar hBar = getHorizontalBar();
		if (hBar != null) {
			hBar.setSelection(0);
		}
		currentCol = 0;
		currentImageIndex = 0;
		disposeImages(0, visibleImages.length);
		visibleImages = EMPTY_IMAGES;

		resetRowsAndCols();
		setupImages();
	}

	/*
	 * dispose of the images in the specified range, not including endIndex
	 */
	private void disposeImages(int startIndex, int endIndex) {
		for (int i = startIndex; i < endIndex && i < visibleImages.length; i++) {
			Image img = visibleImages[i];
			if (img != null) {
				if (img != ImageController.noMemoryIcon && img != ImageController.warnIcon && img != ImageController.errorIcon)
					visibleImages[i].dispose();
				visibleImages[i] = null;
			}
		}
	}
	/**
	 * resets the horizontal bar properties 
	 */
	private void resetHScrollBarProperties() {
		ScrollBar hBar = getHorizontalBar();
		if (hBar == null)
			return;

		int displayWidth = getClientArea().width;
		// if the width of the visible displaying area is greater than the
		// maximum width of images to be displayed, then horizontal scrollbar
		// should be disabled
		if (displayWidth - borderOffset >= getTotalGridWidth()) {
			hBar.setEnabled(false);
			return;
		}

		// otherwise
		hBar.setEnabled(true);
		// Select to current left col, min of 0, max of number of cols, thumb is number of visible cols, increment is one col, page
		// increment is number of visible cols -1 , so that a page right down will bring the right column to the left and not loose context..	
		int numVisibleCols = getNumOfVisibleCols();
		hBar.setValues(currentCol, 0, numOfCols + 1, numVisibleCols, 1, numVisibleCols - 1);
	}
	/**
	 * recalculates the number of rows and columns
	 */
	private void resetRowsAndCols() {
		boolean hBar = getHorizontalBar() != null;
		boolean vBar = getVerticalBar() != null;
		int rows, cols;
		if (!hBar) {
			cols = Math.max(getNumOfVisibleCols(), 1); // Always at least one
		} else
			cols = suggestNumOfCols;

		if (!vBar) {
			rows = Math.max(getNumOfRows() / height, 1); // Always at least one
			if (hBar) {
				cols = (int) Math.ceil(paths.size() / (double) numOfRows);
				// In case of no vbar, but an hbar, override suggested num of cols to what is needed.
			}
		} else
			rows = Math.max((int) Math.ceil(paths.size() / (double) numOfCols), 1); // Aways at least one

		if (cols != numOfCols) {
			numOfCols = cols;
			resetHScrollBarProperties();
		}

		if (rows != numOfRows) {
			numOfRows = rows;
			resetVScrollBarProperties();
		}
	}
	/**
	 * resets horizontal and vertical scrollbar properties
	 */
	private void resetScrollBarProperties() {
		resetHScrollBarProperties();
		resetVScrollBarProperties();
	}
	/**
	 * resets the vertical scrollbar properties 
	 */
	private void resetVScrollBarProperties() {
		ScrollBar vBar = getVerticalBar();
		if (vBar == null)
			return;

		int displayHeight = getClientArea().height;

		// if the height of the visible displaying area is greater than the
		// maximum height of images to be displayed, then vertical scrollbar
		// should be disabled
		if (displayHeight - borderOffset >= getTotalGridHeight()) {
			vBar.setEnabled(false);
			return;
		}

		vBar.setEnabled(true);
		// Select to current top row, min of 0, max of number of rows, thumb is number of visible rows, increment is one row, page
		// increment is number of visible rows -1 , so that a page will down will bring the bottom row to the top, so as not to loose context.
		int numVisRows = getNumOfVisibleRows();
		vBar.setValues(currentRow, 0, numOfRows + 1, numVisRows, 1, numVisRows - 1);
	}
	/**
	 * sets/unsets the focus of the grid of this index
	 */
	private void drawCursor(int index, GC gc) {
		if (index < 0) {
			// Just draw a focus around the first one, but not selection.
			Rectangle bounds = getDisplayBounds(0);
			Rectangle focusBounds =
				new Rectangle(
					bounds.x - borderOffset,
					bounds.y - borderOffset,
					bounds.width + borderOffset * 2,
					bounds.height + borderOffset * 2);
			if (focusBounds.intersects(getClientArea()) && focusBounds.intersects(gc.getClipping()))
				drawFocus(focusBounds, gc);
			return;
		}

		Rectangle bounds = getDisplayBounds(index);
		Rectangle focusBounds =
			new Rectangle(
				bounds.x - borderOffset,
				bounds.y - borderOffset,
				bounds.width + borderOffset * 2,
				bounds.height + borderOffset * 2);
		if (focusBounds.intersects(getClientArea()) && focusBounds.intersects(gc.getClipping())) {
			drawFocus(focusBounds, gc);

			// Now draw the selection box
			Color oldBackground = gc.getBackground();
			gc.setBackground(ColorConstants.menuBackgroundSelected);
			focusBounds.x += 2;
			focusBounds.y += 2;
			focusBounds.height -= 3;
			focusBounds.width -= 3;
			gc.fillRectangle(focusBounds);
			drawImage(index,gc );
			gc.setBackground(oldBackground);
		}
	}

	private void drawFocus(Rectangle focusBounds, GC gc) {
		if (isFocusControl()) {
			// Draw the focus border
			//stores the old foreground color
			Color oldForeground = gc.getForeground();
			//set the border color
			gc.setForeground(ColorConstants.gray);
			int oldLS = gc.getLineStyle();
			gc.setLineStyle(SWT.LINE_DOT);
			//draw the focus rectangle
			gc.drawRectangle(focusBounds);
			//reset gc to old foreground color
			gc.setForeground(oldForeground);
			gc.setLineStyle(oldLS);
		}
	}

	public Point computeSize(int wHint, int hHint, boolean change) {
		int tempWidth = (width * suggestNumOfCols) + borderOffset;
		int tempHeight = (height * suggestNumOfRows) + borderOffset;

		if (wHint != SWT.DEFAULT) {
			tempWidth = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			tempHeight = hHint;
		}
		Rectangle rect = computeTrim(0, 0, tempWidth, tempHeight);
		return new Point(rect.width, rect.height);
	}

	public int getNumOfImages() {
		return itemsSize();
	}
	public int getNumOfVisibleImages() {
		return (getNumOfVisibleCols() * getNumOfVisibleRows());
	}
	public int getSelectedColNum() {
		int result;
		if (isValidIndex(lastSelectedIndex)) {
			result = lastSelectedIndex % numOfCols;
		} else {
			result = lastSelectedIndex;
		}
		return result;
	}
	public int getSelectedRowNum() {
		int result;
		if (isValidIndex(lastSelectedIndex)) {
			result = lastSelectedIndex / numOfCols;
		} else {
			result = lastSelectedIndex;
		}
		return result;
	}

	/**
	 * Deselects the currently selected Image
	 */
	public void deselect() {
		if (lastSelectedIndex >= 0 && lastSelectedIndex < itemsSize()) {
			lastSelectedIndex = -1;
			fireSelection(-1);
			redraw(); // Cause selection to be appropriatly changed
		}
	}

	private void fireSelection(int index) {
		if (selectionListener != null)
			selectionListener.imageSelected(index);
	}

	/**
	 * Selects the Image at the given index.  Does nothing if index not valid.
	 */
	public boolean setSelected(int index) {
		boolean success = false;
		if (selectable && isValidIndex(index) && index != lastSelectedIndex) {
			//select current selection
			lastSelectedIndex = index;
			success = true;
			redraw();
			// Cause selection to be appropriatly changed. Always redraw, even if not visible. Easier then seeing if old visible or new visibile and then redraw.
			fireSelection(index);
		}
		return success;
	}

	/**
	 * Sets the image to the passed in path.
	 */
	public boolean setSelected(IPath path) {
		int index = paths.indexOf(path);
		return (index != -1) ? setSelected(index) : false;
	}

	public void showSelection() {
		boolean needToSetupImages = false;
		if (!verticalIndexVisible(lastSelectedIndex)) {
			showVerticalSelection();
			needToSetupImages = true;
		}
		if (!horizontalIndexVisible(lastSelectedIndex)) {
			showHorizontalSelection();
			needToSetupImages = true;
		}
		if (needToSetupImages) {
			setupImages();
		}
	}

	/*
	 * Automatically scrolls the canvas, if it has to, so that the selected image
	 * is visible
	 */
	private void showVerticalSelection() {
		if (isValidIndex(lastSelectedIndex)) {
			int newRow = lastSelectedIndex / numOfCols;
			scrollTo(newRow, currentCol, true);
		}
	}

	public boolean isValidIndex(int index) {
		return (index >= 0 && index < itemsSize());
	}

	public int itemsSize() {
		return paths.size();
	}

	private void showHorizontalSelection() {
		if (isValidIndex(lastSelectedIndex)) {
			int newCol = lastSelectedIndex % numOfCols;
			scrollTo(currentRow, newCol, true);
		}
	}

	private boolean horizontalIndexVisible(int index) {
		if (getHorizontalBar() == null) {
			return true;
		}
		int col = index % numOfCols;
		return (getFirstVisibleCol() <= col && col <= getLastVisibleCol());

	}

	private boolean verticalIndexVisible(int index) {
		if (getVerticalBar() == null) {
			return true;
		}
		int row = index / numOfCols;
		return (getFirstVisibleRow() <= row && row <= getLastVisibleRow());
	}

	public int getFirstVisibleIndex() {
		return (getFirstVisibleRow() * numOfCols) + getFirstVisibleCol();
	}

	public int getSelectedIndex() {
		return selectable ? lastSelectedIndex : -1;
	}

	//=======================================================================

	private class KeyboardNavigationListener implements KeyListener {
		public void keyPressed(KeyEvent kee) {
			if (selectable && !isValidIndex(getSelectedIndex())) {
				select(getFirstVisibleIndex());
				return;
			}
			switch (kee.keyCode) {
				case SWT.ARROW_UP :
					select(lastSelectedIndex - numOfCols);
					break;
				case SWT.ARROW_DOWN :
					select(lastSelectedIndex + numOfCols);
					break;
				case SWT.ARROW_RIGHT :
					select(lastSelectedIndex + 1);
					break;
				case SWT.ARROW_LEFT :
					select(lastSelectedIndex - 1);
					break;
				case SWT.PAGE_UP :
					if (!select(lastSelectedIndex - (numOfCols * (getNumOfVisibleRows() - 1)))) {
						select(0);
					}
					break;
				case SWT.PAGE_DOWN :
					if (!select(lastSelectedIndex + (numOfCols * (getNumOfVisibleRows() - 1)))) {
						select(itemsSize() - 1);
					}
					break;
				case SWT.HOME :
					if (kee.stateMask == SWT.CTRL) {
						select(0);
					} else {
						select(lastSelectedIndex - getSelectedColNum());
					}
					break;
				case SWT.END :
					if (kee.stateMask == SWT.CTRL) {
						select(itemsSize() - 1);
					} else if (!select(lastSelectedIndex - 1 - getSelectedColNum() + numOfCols)) {
						select(itemsSize() - 1);
					}
					break;
			}
		}

		public void keyReleased(KeyEvent kee) {
		}

		private boolean select(int index) {
			boolean success = false;
			if (isValidIndex(index)) {
				setSelected(index);
				showSelection();
				success = true;
			}
			return success;
		}
	}

}
