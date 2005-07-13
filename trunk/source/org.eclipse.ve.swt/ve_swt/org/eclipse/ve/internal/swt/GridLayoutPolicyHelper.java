/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridLayoutPolicyHelper.java,v $
 *  $Revision: 1.17 $  $Date: 2005-07-13 22:16:44 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * 
 * @since 1.0.0
 */
public class GridLayoutPolicyHelper extends LayoutPolicyHelper implements IActionFilter {

	protected ResourceSet rset;
	protected EReference sfLayoutData, sfCompositeControls;
	protected EStructuralFeature sfHorizontalSpan, sfVerticalSpan, sfNumColumns, sfCompositeLayout, sfLabelText;
	protected int defaultHorizontalSpan, defaultVerticalSpan;
	protected EObject[][] layoutTable = null;
	protected Rectangle[] childrenDimensions = null;
	protected int numColumns = -1;
	private IBeanProxy fContainerBeanProxy = null;
	private IBeanProxy fLayoutManagerBeanProxy = null;
	protected JavaClass classLabel = null;

	protected IBeanProxy getContainerBeanProxy() {
		if (fContainerBeanProxy == null) {
			fContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		}
		return fContainerBeanProxy;
	}

	protected IBeanProxy getLayoutManagerBeanProxy() {
		if (fLayoutManagerBeanProxy == null) {
			if (getContainerBeanProxy() != null)
				fLayoutManagerBeanProxy = BeanSWTUtilities.invoke_getLayout(getContainerBeanProxy());
		}
		return fLayoutManagerBeanProxy;
	}

	/**
	 * @param ep
	 * 
	 * @since 1.0.0
	 */
	public GridLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
		initializeDefaults();
	}

	/**
	 * 
	 * 
	 * @since 1.0.0
	 */
	public GridLayoutPolicyHelper() {
		initializeDefaults();
	}

	protected void initializeDefaults() {
		GridData gd = new GridData();
		defaultHorizontalSpan = gd.horizontalSpan;
		defaultVerticalSpan = gd.verticalSpan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#cancelConstraints(org.eclipse.ve.internal.cde.commands.CommandBuilder, java.util.List)
	 */
	protected void cancelConstraints(CommandBuilder commandBuilder, List children) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getDefaultConstraint(java.util.List)
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), null);
	}
	public static final EObject EMPTY = EcoreFactory.eINSTANCE.createEObject();
	/**
	 * Get a representation of the grid. The grid is indexed by [column][row]. The value at each position is the child located at that position. Empty
	 * cells will have null values.
	 * 
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public EObject[][] getLayoutTable() {
		if (layoutTable == null) {
			int[][] dimensions = getContainerLayoutDimensions();
			layoutTable = new EObject[dimensions[0].length][dimensions[1].length];
			numColumns = dimensions[0].length;
			// If empty container, don't continue.
			if (layoutTable.length < 1 || layoutTable[0].length < 1)
				return layoutTable;

			int row = 0;
			int col = 0;
			int horizontalSpan;
			int verticalSpan;

			List children = (List) getContainer().eGet(sfCompositeControls);
			childrenDimensions = new Rectangle[children.size()];
			int childNum = 0;
			Iterator itr = children.iterator();

			while (itr.hasNext()) {
				IJavaObjectInstance child = (IJavaObjectInstance) itr.next();
				IJavaObjectInstance childData = (IJavaObjectInstance) child.eGet(sfLayoutData);
				if (childData != null) {
					horizontalSpan = getIntValue(sfHorizontalSpan, childData);
					verticalSpan = getIntValue(sfVerticalSpan, childData);
				} else {
					horizontalSpan = defaultHorizontalSpan;
					verticalSpan = defaultVerticalSpan;
				}

				Rectangle r = new Rectangle();

				// Find the next un-occupied cell
				while (layoutTable[col][row] != null && row < layoutTable[0].length) {
					col += 1;
					if (col >= numColumns) {
						row += 1;
						col = 0;
					}
				}
				// if there's not enough columns left for the horizontal span, go to
				// next row
				if (col != 0 && (col + horizontalSpan - 1) >= numColumns) {
					layoutTable[col][row] = EMPTY;
					row += 1;
					col = 0;
				}

				// Add the child to the table in all spanned cells
				for (int i = 0; i < horizontalSpan; i++) {
					for (int j = 0; j < verticalSpan; j++) {
						layoutTable[col + i][row + j] = child;
					}
				}

				r.x = col;
				r.y = row;
				r.width = horizontalSpan;
				r.height = verticalSpan;
				childrenDimensions[childNum] = r;
				childNum++;

				// Add the spanned columns to the column position
				col += horizontalSpan - 1;
			}
			// If the last row isn't full, fill it with EMPTY objects
			if (layoutTable.length > 0 && layoutTable[0].length > 0) {
				int lastRow = layoutTable[0].length - 1;
				for (int i = 0; i < layoutTable.length; i++) {
					if (layoutTable[i][lastRow] == null)
						layoutTable[i][lastRow] = EMPTY;
				}
			}

		}
		return layoutTable;
	}

	private int getIntValue(EStructuralFeature sf, IJavaObjectInstance object) {
		int value = 1;

		IJavaInstance valueObject = (IJavaInstance) object.eGet(sf);
		if (valueObject != null) {
			IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(valueObject, rset);
			if (intProxy != null) {
				value = intProxy.intValue();
			}
		}

		return value;
	}

	/**
	 * Get the dimensions of all the children of this container.  The array is indexed by the Z-order 
	 * of the children.  The dimensions are packed into a Rectangle according to the following rules:
	 * 
	 * rect.x = column position
	 * rect.y = row position
	 * rect.width = horizontal span
	 * rect.height = vertical span
	 * 
	 * @return array of children dimensions.
	 * 
	 * @since 1.0.0
	 */
	public Rectangle[] getChildrenDimensions() {
		if (childrenDimensions == null) {
			getLayoutTable();
		}
		return childrenDimensions;
	}

	/**
	 * Get the number of columns in the container's grid layout.
	 * @return number of columns
	 * 
	 * @since 1.0.0
	 */
	public int getNumColumns() {
		if (numColumns == -1) {
			getLayoutTable();
		}
		return numColumns;
	}

	/**
	 * Get the index of the child occupying the given cell.
	 * @param cell Cell location to check
	 * @return the index of the child, or -1 if cell is unoccupied, or an invalid position.
	 * 
	 * @since 1.0.0
	 */
	public int getChildIndexAtCell(Point cell) {
		int value = -1;

		EObject[][] table = getLayoutTable();
		// Check to make sure the cell position is within the grid
		if (cell.x < 0 || cell.x >= getNumColumns() || cell.y < 0 || cell.y >= table[0].length)
			return -1;

		EObject childAtCell = table[cell.x][cell.y];
		// If the cell is empty, try to find the last occupied cell
		if (childAtCell == null) {
			int x = cell.x;
			int y = cell.y;
			while (childAtCell == null && !(x == 0 && y == 0)) {
				if (x == 0) {
					y -= 1;
					x = getNumColumns();
				}
				x -= 1;

				childAtCell = table[x][y];
			}
		}

		List children = (List) getContainer().eGet(sfCompositeControls);
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).equals(childAtCell)) {
				value = i;
				break;
			}
		}

		return value;
	}

	int[] expandableColumns, expandableRows;
	/**
	 * Return the GridLayout dimensions which is 2 dimensional array that contains 2 arrays:
	 *  1. an int array of all the column widths
	 *  2. an int array of all the row heights
	 */
	public int[][] getContainerLayoutDimensions() {
		int[] columnWidths = null, rowHeights = null;
		int[][] result = new int[2][];
		result[0] = new int[0];
		result[1] = new int[0];

		// Hack to grab the column/row information from the private fields of a GridLayout
		// The helper class org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper is used to calculate the column widths and row heights
		// Prior to 3.1 these were in package protected fields on GridLayout but these are no longer available so the helper class
		// computes the values
		IBeanTypeProxy gridLayoutHelperType = getLayoutManagerBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
				"org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper"); //$NON-NLS-1$
		IBeanProxy gridLayoutHelperProxy = null;
		try {
			gridLayoutHelperProxy = gridLayoutHelperType.newInstance();
			final IBeanProxy gridLayoutHelperProxyFinal = gridLayoutHelperProxy;
			final IMethodProxy setCompositeMethodProxy = gridLayoutHelperType.getMethodProxy("setComposite", "org.eclipse.swt.widgets.Composite"); //$NON-NLS-1$ //$NON-NLS-2$
			DisplayManager.DisplayRunnable runnable = new DisplayManager.DisplayRunnable() {

				public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
					setCompositeMethodProxy.invoke(gridLayoutHelperProxyFinal, getContainerBeanProxy());
					return null;
				}
			};

			JavaStandardSWTBeanConstants.invokeSyncExec(getContainerBeanProxy().getProxyFactoryRegistry(), runnable);

		} catch (Exception e) {
			e.printStackTrace();
		}

		IFieldProxy getColumnWidthsFieldProxy = gridLayoutHelperType.getDeclaredFieldProxy("widths"); //$NON-NLS-1$
		IFieldProxy getRowHeightsFieldProxy = gridLayoutHelperType.getDeclaredFieldProxy("heights"); //$NON-NLS-1$
		try {
			// Get the column widths and row heights from the target VM helper
			IArrayBeanProxy arrayProxyColumnWidths = (IArrayBeanProxy) getColumnWidthsFieldProxy.get(gridLayoutHelperProxy);
			if (arrayProxyColumnWidths != null) {
				columnWidths = new int[arrayProxyColumnWidths.getLength()];
				for (int i = 0; i < arrayProxyColumnWidths.getLength(); i++) {
					columnWidths[i] = ((IIntegerBeanProxy) arrayProxyColumnWidths.get(i)).intValue();
				}
				result[0] = columnWidths;
			}
			IArrayBeanProxy arrayProxyRowHeights = (IArrayBeanProxy) getRowHeightsFieldProxy.get(gridLayoutHelperProxy);
			if (arrayProxyRowHeights != null) {
				rowHeights = new int[arrayProxyRowHeights.getLength()];
				for (int i = 0; i < arrayProxyRowHeights.getLength(); i++) {
					rowHeights[i] = ((IIntegerBeanProxy) arrayProxyRowHeights.get(i)).intValue();
				}
				result[1] = rowHeights;
			}
		} catch (ThrowableProxy exc) {
			return null;
		}
		
		return result;
	}

	/**
	 * Return the spacing information for the GridLayout.  
	 * This information is packed into a Rectangle object, as follows:
	 * 
	 * Rectangle.x = RowLayout.marginWidth
	 * Recatngle.y = RowLayout.marginHeight
	 * Recatngle.width = RowLayout.horizontalSpacing
	 * Recatngle.height = RowLayout.verticalSpacing
	 * 
	 * @return Rectangle representing the GridLayout's spacing
	 * 
	 * @since 1.0.0
	 */
	public Rectangle getContainerLayoutSpacing() {
		Rectangle result = null;

		// Grab the spacing information from the fields of a GridLayout
		IFieldProxy getMarginHeightFieldProxy = getLayoutManagerBeanProxy().getTypeProxy().getDeclaredFieldProxy("marginHeight"); //$NON-NLS-1$
		IFieldProxy getMarginWidthFieldProxy = getLayoutManagerBeanProxy().getTypeProxy().getDeclaredFieldProxy("marginWidth"); //$NON-NLS-1$
		IFieldProxy getHorizontalSpacingFieldProxy = getLayoutManagerBeanProxy().getTypeProxy().getDeclaredFieldProxy("horizontalSpacing"); //$NON-NLS-1$
		IFieldProxy getVerticalSpacingFieldProxy = getLayoutManagerBeanProxy().getTypeProxy().getDeclaredFieldProxy("verticalSpacing"); //$NON-NLS-1$
		try {
			result = new Rectangle();
			IIntegerBeanProxy intProxyMarginHeight = (IIntegerBeanProxy) getMarginHeightFieldProxy.get(getLayoutManagerBeanProxy());
			if (intProxyMarginHeight != null) {
				result.y = intProxyMarginHeight.intValue();
			}
			IIntegerBeanProxy intProxyMarginWidth = (IIntegerBeanProxy) getMarginWidthFieldProxy.get(getLayoutManagerBeanProxy());
			if (intProxyMarginWidth != null) {
				result.x = intProxyMarginWidth.intValue();
			}
			IIntegerBeanProxy intProxyHorizontalSpacing = (IIntegerBeanProxy) getHorizontalSpacingFieldProxy.get(getLayoutManagerBeanProxy());
			if (intProxyHorizontalSpacing != null) {
				result.width = intProxyHorizontalSpacing.intValue();
			}
			IIntegerBeanProxy intProxyVerticalSpacing = (IIntegerBeanProxy) getVerticalSpacingFieldProxy.get(getLayoutManagerBeanProxy());
			if (intProxyVerticalSpacing != null) {
				result.height = intProxyVerticalSpacing.intValue();
			}
		} catch (ThrowableProxy exc) {
			return null;
		}
		return result;
	}

	/**
	 * Return the GridLayout origin
	 */
	public Rectangle getContainerClientArea() {
		if (getContainerBeanProxy() != null) {
			try {
				// This needs to be done in a syncExec because it needs to access the SWT display thread
				IRectangleBeanProxy result = (IRectangleBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExec(getContainerBeanProxy()
						.getProxyFactoryRegistry(), new DisplayManager.DisplayRunnable() {

					public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
						IBeanProxy aContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
						IMethodProxy getContainerClientArea = aContainerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(
								aContainerBeanProxy.getTypeProxy().getTypeName(), "getClientArea", null); //$NON-NLS-1$
						if (getContainerClientArea != null) {
							IRectangleBeanProxy rectangleProxy = (IRectangleBeanProxy) getContainerClientArea
									.invokeCatchThrowableExceptions(aContainerBeanProxy);

							// Check to see if this is a container that extends Decorations (Shell, Dialog, etc)
							IBeanTypeProxy decorationsType = displayProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Decorations"); //$NON-NLS-1$
							if (aContainerBeanProxy.getTypeProxy().isKindOf(decorationsType)) {
								IMethodProxy getDecorationsComputeTrim = aContainerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aContainerBeanProxy.getTypeProxy().getTypeName(), "computeTrim", new String[] { "int", "int", "int", "int" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
								if (getDecorationsComputeTrim != null) {
									// Create the parameters for the computeTrim method
									IStandardBeanProxyFactory fac = aContainerBeanProxy.getProxyFactoryRegistry().getBeanProxyFactory();
									IIntegerBeanProxy xProxy = fac.createBeanProxyWith(rectangleProxy.getX());
									IIntegerBeanProxy yProxy = fac.createBeanProxyWith(rectangleProxy.getY());
									IIntegerBeanProxy widthProxy = fac.createBeanProxyWith(rectangleProxy.getWidth());
									IIntegerBeanProxy heightProxy = fac.createBeanProxyWith(rectangleProxy.getHeight());

									IRectangleBeanProxy trimProxy = (IRectangleBeanProxy) getDecorationsComputeTrim.invoke(aContainerBeanProxy,
											new IBeanProxy[] { xProxy, yProxy, widthProxy, heightProxy});
									if (trimProxy != null) {
										IStandardSWTBeanProxyFactory fac2 = (IStandardSWTBeanProxyFactory) aContainerBeanProxy
												.getProxyFactoryRegistry().getBeanProxyFactoryExtension(IStandardSWTBeanProxyFactory.REGISTRY_KEY);
										IRectangleBeanProxy newRectProxy = fac2.createBeanProxyWith(trimProxy.getX() * -1, trimProxy.getY() * -1,
												rectangleProxy.getWidth(), rectangleProxy.getHeight());
										return newRectProxy;
									}
								}
							}
							return rectangleProxy;
						} else {
							return null;
						}
					}
				});
				if (result != null) { return new Rectangle(result.getX(), result.getY(), result.getWidth(), result.getHeight()); }
			} catch (ThrowableProxy e) {
			} catch (RunnableException e) {
			}
		}
		return null;
	}

	/*
	 * Return true if the container has no children, false if it does.
	 * Since Swing's GridBagLayout doesn't refresh it's layout information if all the components 
	 * have been removed, we can't rely on the getLayoutDimensions() call to return the correct information. 
	 * Instead we need to first query the parent container to see if it has any children.
	 */
	public boolean isContainerEmpty() {
		if (getContainerBeanProxy() != null) {
			try {
				// This needs to be done in a syncExec because it needs to access the SWT display thread
				IArrayBeanProxy result = (IArrayBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExec(getContainerBeanProxy()
						.getProxyFactoryRegistry(), new DisplayManager.DisplayRunnable() {

					public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
						IBeanProxy aContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
						if (aContainerBeanProxy != null) {
							IMethodProxy getChildrenMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory()
									.getMethodProxy(aContainerBeanProxy.getTypeProxy().getTypeName(), "getChildren", null); //$NON-NLS-1$
							if (getChildrenMethodProxy != null) {
								IArrayBeanProxy childrenProxy = (IArrayBeanProxy) getChildrenMethodProxy
										.invokeCatchThrowableExceptions(aContainerBeanProxy);
								return childrenProxy;
							}
						}
						return null;
					}
				});
				if (result != null) { return result.getLength() <= 0; }
			} catch (ThrowableProxy e) {
			} catch (RunnableException e) {
			}
		}
		return true;
	}

	public boolean isOnSameRow(int child1, int child2) {
		Rectangle[] children = getChildrenDimensions();

		Rectangle r1 = children[child1];
		Rectangle r2 = children[child2];

		return (r1.y == r2.y);
	}

	public boolean isCellEmptyBefore(int index) {
		if (index == 0) { return false; }
		Rectangle r = getChildrenDimensions()[index];
		if (r.y == 0) { return false; }
		EObject cell;
		if (r.x != 0) {
			cell = getLayoutTable()[r.x - 1][r.y];
		} else {
			cell = getLayoutTable()[getNumColumns() - 1][r.y - 1];
		}
		return (cell == null);
	}

	/**
	 * Create a child span command
	 * 
	 * @param childEditPart
	 * @param childCellLocation
	 * @param endCellLocation
	 * @param spanDirection
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public Command getSpanChildrenCommand(EditPart childEditPart, Point childCellLocation, Point endCellLocation, int spanDirection) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject) childEditPart.getModel();
		if (control != null) {
			IJavaObjectInstance gridData = (IJavaObjectInstance) control.eGet(sfLayoutData);
			if (gridData == null) {
				// Create a new grid data if one doesn't already exist.
				gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(
						"org.eclipse.swt.layout.GridData", rset, "new org.eclipse.swt.layout.GridData()"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (gridData != null) {
				List children = (List)getContainer().eGet(sfCompositeControls);
				int index = children.indexOf(control);
				Rectangle rect = getChildrenDimensions()[index];
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(childEditPart), null, false);
				if (spanDirection == PositionConstants.EAST) {
					int gridWidth = endCellLocation.x - childCellLocation.x + 1;
					if (gridWidth != rect.width) {
						Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridWidth)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfHorizontalSpan, widthObject);
						if (gridWidth > rect.width) {
							// Increase the horizontalSpan
							componentCB.append(createNumColumnsCommand(numColumns + gridWidth - rect.width));
							componentCB.append(createFillerLabelCommands(rect.y + 1));
						} else {
							// Decrease the horizontal span and put a filler label in the columns it was decreased by
							if (index + 1 < children.size())
								for (int i = 0; i < rect.width - gridWidth; i++)
									componentCB.append(policy.getCreateCommand(createFillerLabelObject(), children.get(index + 1)));
						}
					}
				}
				if (spanDirection == PositionConstants.SOUTH) {
					int gridHeight = endCellLocation.y - childCellLocation.y + 1;
					if (gridHeight != rect.height) {
						Object heightObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridHeight)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfVerticalSpan, heightObject);
						if (gridHeight > rect.height) {
							// increase the verticalSpan
							for (int i = 0; i < gridHeight - rect.height; i++) {
								componentCB.append(createFillerLabelsForSpannedRowCommand(rect.y + rect.height, endCellLocation.x));
							}
						} else {
							// decrease the verticalSpan
							CommandBuilder rowcb = new CommandBuilder();
							for (int i = 0; i < rect.height - gridHeight; i++) {
								rowcb.append(createRemoveRowCommand(rect.y + rect.height -1 - i, control));
							}
							if (!rowcb.isEmpty())
								// If all that is left on the row(s) is filler labels, remove them.
								componentCB.append(rowcb.getCommand());
							else if (index + 1 < children.size()) {
								// otherwise put a filler label in place of the cell the is left open by decrementing the vertical span
								EObject beforeObject = findNextValidObject(rect.x, rect.y + rect.height - 1);
								componentCB.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
							}
						}
					}
				}
				componentCB.applyAttributeSetting(control, sfLayoutData, gridData);
				cb.append(componentCB.getCommand());
			}
		}
		if (cb.isEmpty()) { return UnexecutableCommand.INSTANCE; }
		return cb.getCommand();
	}

	public IJavaInstance createFillerLabelObject() {
		return BeanUtilities.createJavaObject("org.eclipse.swt.widgets.Label",rset,(JavaAllocation)null); //$NON-NLS-1$
	}
	
	public Command createNumColumnsCommand(int numCols) {
		CommandBuilder cb = new CommandBuilder();
		EObject parent = (EObject) policy.getContainer();
		if (parent != null) {
			IJavaInstance gridLayout = (IJavaInstance) parent.eGet(sfCompositeLayout);
			if (gridLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
				Object intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(numCols)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridLayout, sfNumColumns, intObject);
				componentCB.applyAttributeSetting(parent, sfCompositeLayout, gridLayout);
				cb.append(componentCB.getCommand());
			} else {
				// this shouldn't happen
				return UnexecutableCommand.INSTANCE;
			}
			return cb.getCommand();
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}
	
	/*
	 * To add a row, we must add the filler labels and the requested control prior to the control
	 * in the first column on the row after the insertion point. Add filler except at the column position.
	 */
	public Command createFillerLabelsForNewRowCommand (Object addedControl, int atRow, int atColumn, Request request) {
		EObject[][] table = getLayoutTable();
		if (table[0].length < 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;

		if (atColumn == -1)
			atColumn = numColumns - 1;
		
		// Do not allow adding the row if inserting through a control that spans vertically
		if (atRow < table[0].length) {
			EObject child = table[atColumn][atRow];
			if (child != null && !isFillerLabel(child)) {
				int index = children.indexOf(table[atColumn][atRow]);
				if (index != -1 && childrenDimensions[index].y != atRow)
					return UnexecutableCommand.INSTANCE;
			}
		}
		
		CommandBuilder cb = new CommandBuilder();
		EObject beforeObject = findNextValidObject(0, atRow);
		// Add the row by adding filler labels and inserting the control at the specific column position.
		// If any of the controls spans vertically, don't add filler, just expand it one more row.
		for (int i = 0; i < numColumns; i++) {
			boolean addFiller = true;
			if (i == atColumn) {
				// This is the column where the new control is put
				if (request instanceof CreateRequest)
					cb.append(policy.getCreateCommand(addedControl, beforeObject));
				else 
					cb.append(policy.getMoveChildrenCommand(Collections.singletonList(addedControl), beforeObject));
				addFiller = false;
			} else if (atRow < table[0].length) {
				EObject child = table[i][atRow];
				if (child != null && !isFillerLabel(child) && child != EMPTY) {
					int index = children.indexOf(child);
					// If the row is going through a control that is spanning more than one row,
					// we need to expand it by one instead of adding filler.
					if (index != -1 && childrenDimensions[index].y != atRow) {
						cb.append(createVerticalSpanCommand(child, childrenDimensions[index].height + 1));
						addFiller = false;
					}
				}
			}
			if (addFiller)
				// These are the columns where the empty labels are put
				cb.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
		}
		return cb.getCommand();
	}

	/*
	 * To add a row, we must add the filler labels before the first object on the next row
	 * except for the column the spanned control is spanning into.
	 */
	private Command createFillerLabelsForSpannedRowCommand (int atRow, int atColumn) {
		EObject[][] table = getLayoutTable();
		if (table[0].length < 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;

		if (atColumn == -1)
			atColumn = numColumns - 1;
		
		CommandBuilder cb = new CommandBuilder();
		EObject beforeObject = findNextValidObject(0, atRow);
		// Add the row by adding filler labels in all columns except the column the control is spanning into.
		// If any of the controls spans vertically, don't add filler, just expand it one more row.
		for (int i = 0; i < numColumns; i++) {
			boolean addFiller = true;
			if (!(i == atColumn)) {
				if (atRow < table[0].length) {
					EObject child = table[i][atRow];
					if (child != null && !isFillerLabel(child) && child != EMPTY) {
						int index = children.indexOf(child);
						// If the row is going through a control that is spanning more than one row,
						// we need to expand it by one instead of adding filler.
						if (index != -1 && childrenDimensions[index].y != atRow) {
							cb.append(createVerticalSpanCommand(child, childrenDimensions[index].height + 1));
							addFiller = false;
						}
					}
				}
				if (addFiller)
					// These are the columns where the empty labels are put
					cb.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
			}
		}
		return cb.getCommand();
	}

	/*
	 * Put a new control into a cell that is EMPTY. 
	 */
	public Command createAddToEmptyCellCommand (Object addedControl, Point cell, Request request) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table.length == 0 || table[0].length == 0 || cell.x >= table.length || cell.y >= table[0].length)
			return null;

		// Find the next occupied cell to be used as the before object. 
		EObject beforeObject = findNextValidObject(cell.x, cell.y);

		// Now go through the row and replace the empty cell with the new object... also
		// create filler labels in other empty cells as we go.
		for (int i = 0; i < table.length; i++) {
			if (i == cell.x)
				// This is the column where the new control is put
				if (request instanceof CreateRequest)
					cb.append(policy.getCreateCommand(addedControl, beforeObject));
				else 
					cb.append(policy.getMoveChildrenCommand(Collections.singletonList(addedControl), beforeObject));
			else if (table[i][cell.y] == EMPTY)
				// These are the columns where the empty labels are put
				cb.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
		}
		return cb.getCommand();
	}

	/*
	 * Insert filler labels at the end of each row except the one the control was added to.
	 */
	public Command createFillerLabelCommands (int exceptRow) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table[0].length <= 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;
		// Add a filler label prior to each object that is in the first position of each row. 
		// This will in effect add a label to end of the previous row.
		// This must be done for each row except the row where the actual control has been added.
		for (int i = 1; i < table[0].length; i++) {
			if (i != exceptRow) {
				if (table[0][i] != EMPTY) {
					cb.append(policy.getCreateCommand(createFillerLabelObject(), table[0][i]));
				}
			}
		}
		// Need to add a final label to the end unless the last row was where the actual control was added
		if (exceptRow != table[0].length)
			cb.append(policy.getCreateCommand(createFillerLabelObject(), null));
		return cb.getCommand();
	}

	/*
	 * Insert filler labels in each row at a specific column position in order to move the controls
	 * over one column yet maintain all other row/column positions before that column.
	 * The row the control was added is not processed.
	 */
	public Command createFillerLabelCommands (int atColumn, int exceptRow, boolean isLastColumn) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table[0].length <= 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;
		// Add a filler label prior to each object that is in the atColumn of each row. 
		// This will in effect add a label to end of the previous row.
		// This must be done for each row except the row where the actual control has been added.
		for (int i = 0; i < table[0].length; i++) {
			if (isLastColumn && i == 0) continue;
			if (i != exceptRow) {
				if (table[atColumn][i] != EMPTY) {
					int index = children.indexOf(table[atColumn][i]);
					// If the column is going through a control that is spanning more than one column,
					// we need to expand it by one instead of adding filler.
					if (index != -1 && childrenDimensions[index].width > defaultHorizontalSpan)
						cb.append(createHorizontalSpanCommand(table[atColumn][i], childrenDimensions[index].width + 1));
					else
						cb.append(policy.getCreateCommand(createFillerLabelObject(), table[atColumn][i]));
				}
			}
		}
		return cb.getCommand();
	}

	/*
	 * Create the command to set the horizontalSpan value of the GridData for a child control.
	 */
	private Command createHorizontalSpanCommand(EObject control, int gridWidth) {
		CommandBuilder cb = new CommandBuilder();
		if (control != null) {
			IJavaObjectInstance gridData = (IJavaObjectInstance) control.eGet(sfLayoutData);
			if (gridData == null) {
				// Create a new grid data if one doesn't already exist.
				gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(
						"org.eclipse.swt.layout.GridData", rset, "new org.eclipse.swt.layout.GridData()"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (gridData != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
				Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridWidth)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridData, sfHorizontalSpan, widthObject);
				componentCB.applyAttributeSetting(control, sfLayoutData, gridData);
				cb.append(componentCB.getCommand());
			}
		}
		return cb.getCommand();
	}

	/*
	 * Create the command to set the horizontalSpan value of the GridData for a child control.
	 */
	private Command createVerticalSpanCommand(EObject control, int gridHeight) {
		CommandBuilder cb = new CommandBuilder();
		if (control != null) {
			IJavaObjectInstance gridData = (IJavaObjectInstance) control.eGet(sfLayoutData);
			if (gridData == null) {
				// Create a new grid data if one doesn't already exist.
				gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(
						"org.eclipse.swt.layout.GridData", rset, "new org.eclipse.swt.layout.GridData()"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (gridData != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
				Object heightObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridHeight)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridData, sfVerticalSpan, heightObject);
				componentCB.applyAttributeSetting(control, sfLayoutData, gridData);
				cb.append(componentCB.getCommand());
			}
		}
		return cb.getCommand();
	}

	public void refresh() {
		layoutTable = null;
		childrenDimensions = null;
		numColumns = -1;
	}

	public void setContainerPolicy(VisualContainerPolicy policy) {
		super.setContainerPolicy(policy);

		if (policy != null) {
			// Eventually we will be set with a policy. At that time we can compute these.
			rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
			sfCompositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
			sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
			sfHorizontalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_SPAN);
			sfVerticalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_VERTICAL_SPAN);
			sfNumColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_NUM_COLUMNS);
			sfCompositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
			classLabel = Utilities.getJavaClass("org.eclipse.swt.widgets.Label", rset); //$NON-NLS-1$
			sfLabelText = classLabel.getEStructuralFeature("text"); //$NON-NLS-1$

		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 * Enable the Show/Hide Grid action on the Beans viewer depending on the layout EditPolicy
	 * on the graphical viewer side.
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof EditPart) {
			EditDomain ed = EditDomain.getEditDomain((EditPart) target);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(((EditPart) target).getModel());
				if (ep != null && ep.getEditPolicy(EditPolicy.LAYOUT_ROLE) instanceof IActionFilter) { return ((IActionFilter) ep
						.getEditPolicy(EditPolicy.LAYOUT_ROLE)).testAttribute(target, name, value); }
			}
		}
		return false;
	}
	public boolean isFillerLabel(Object control) {
		return classLabel.isInstance(control) && !((EObject)control).eIsSet(sfLabelText);
	}
	/*
	 * If the objects in a row are all filler labels, except the ignoreObject, remove them 
	 */
	public Command createRemoveRowCommand (int atRow, EObject ignoreObject) {
		EObject[][] table = getLayoutTable();
		CommandBuilder cb = new CommandBuilder();
		boolean empty = true;
		for (int i = 0; i < table.length; i++) {
			if (!isFillerLabel(table[i][atRow]) && !(table[i][atRow] == EMPTY) && !(ignoreObject == table[i][atRow])) {
				empty = false;
				break;
			}
		}
		if (empty) {
			for (int i = 0; i < table.length; i++) {
				if (!(table[i][atRow] == EMPTY) && !(ignoreObject == table[i][atRow]))
					cb.append(policy.getDeleteDependentCommand(table[i][atRow]));
			}
			return cb.getCommand();
		}
		return null;
	}
	/*
	 * If the objects in a column are all filler labels, except the ignoreObject, remove them 
	 */
	public Command createRemoveColumnCommand (int atColumn, EObject ignoreObject, int projectNumColumns) {
		EObject[][] table = getLayoutTable();
		CommandBuilder cb = new CommandBuilder();
		boolean empty = true;
		for (int i = 0; i < table[0].length; i++) {
			if (!isFillerLabel(table[atColumn][i]) && !(table[atColumn][i] == EMPTY) && !(ignoreObject == table[atColumn][i])) {
				empty = false;
				break;
			}
		}
		if (empty) {
			// Remove the columns and reduce the numColumns by 1
			for (int i = 0; i < table[0].length; i++) {
				if (!(table[atColumn][i] == EMPTY) && !(ignoreObject == table[atColumn][i]))
					cb.append(policy.getDeleteDependentCommand(table[atColumn][i]));
			}
			if (projectNumColumns != 1)
				cb.append(createNumColumnsCommand(projectNumColumns - 1));
			return cb.getCommand();
		}
		return null;
	}
	
	/*
	 * Find next object in the table that is not EMPTY and doesn't vertically span more than one row.
	 * Note: filler labels are valid
	 */
	private EObject findNextValidObject (int columnStart, int rowStart) {
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table.length == 0 || table[0].length == 0)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;

		// Find the next occupied cell to be used as the before object. 
		EObject foundObject = null;
		int col = columnStart, row = rowStart;
		boolean firstpass = true;
		for (int i = row; i < table[0].length && foundObject == null; i++) {
			for (int j = col; j < table.length; j++) {
				if (table[j][i] != EMPTY) {
					int index = children.indexOf(table[j][i]);
					// If the row is going through a control that is spanning vertically more than one
					// row, skip it. This is checked by comparing this control's starting y (row) value
					// with where we are in the table lookup
					if (index != -1 && childrenDimensions[index].y == i) {
						foundObject = table[j][i];
						break;
					}
				}
			}
			if (firstpass)
				col = 0;
		}
		return foundObject;
	}
}
