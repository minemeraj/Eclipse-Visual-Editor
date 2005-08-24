/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
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
 *  $Revision: 1.29 $  $Date: 2005-08-24 23:52:55 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.instantiation.base.FeatureValueProvider.FeatureValueProviderHelper;
import org.eclipse.jem.internal.instantiation.base.FeatureValueProvider.Visitor;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener;
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
	
	/*
	 * Wrapper class for the filler label.
	 * This is put in the layoutTable at the specific cell location and created when the layoutTable
	 * is first created. That way we only have to go throught the elaborate checks up front and not
	 * every time we want to check if it's a filler label.
	 */
	static class FillerLabel extends EObjectImpl {
		EObject realObject;

		public FillerLabel(EObject realObject) {
			this.realObject = realObject;
		}
	}

	protected IBeanProxy getContainerBeanProxy() {
		if (fContainerBeanProxy == null || !fContainerBeanProxy.isValid()) {
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

	private static class AnyFeatureSetVisitor implements Visitor {

		public Object isSet(EStructuralFeature feature, Object value) {
			if (feature.getName().equals(JavaInstantiation.ALLOCATION))
				return null;
			else if (feature.isMany() && ((List)value).isEmpty())
				return null;
			return Boolean.TRUE;
		}
	}
	private static final AnyFeatureSetVisitor anyFeatureSetVisitor = new AnyFeatureSetVisitor();
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
						if (classLabel.isInstance(child) && (FeatureValueProviderHelper.visitSetFeatures(child, anyFeatureSetVisitor) == null) && isNoStyleSet(child))
							layoutTable[col + i][row + j] = new FillerLabel(child);
						else
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

	/*
	 * Return true if a filler label occupies this cell location
	 */
	public boolean isFillerLabelAtCell(Point cell) {
		EObject[][] table = getLayoutTable();
		// Check to make sure the cell position is within the grid
		if (cell.x >= 0 && cell.x < table.length && cell.y >= 0 && cell.y < table[0].length)
			return table[cell.x][cell.y] instanceof FillerLabel;
		return false;
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

		if (isFillerLabel(childAtCell))
			childAtCell = ((FillerLabel)childAtCell).realObject;
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
	private static final String TARGET_VM_VERSION_KEY = "TARGET_VM_VERSION";
	private int targetVMVersion = -1;
	private EditDomain fEditDomain;
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
		String targetVMHelperTypeName = null;
		if(getTargetVMSWTVersion() >= 3100){
			targetVMHelperTypeName = "org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper";
		} else {
			targetVMHelperTypeName = "org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper_30";		
		}		
		IBeanTypeProxy gridLayoutHelperType = getLayoutManagerBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(targetVMHelperTypeName); //$NON-NLS-1$
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
				List children = (List) getContainer().eGet(sfCompositeControls);
				int index = children.indexOf(control);
				Rectangle rect = getChildrenDimensions()[index];
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(childEditPart), null, false);
				if (spanDirection == PositionConstants.EAST) {
					int newgridDataWidth = endCellLocation.x - childCellLocation.x + 1;
					if (newgridDataWidth != rect.width) {
						Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(newgridDataWidth)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfHorizontalSpan, widthObject);
						if (newgridDataWidth > rect.width) {
							// Increase the horizontalSpan
							// but first see if we can expand into empty cells without increasing the number of columns
							int numColsIncrement = createHorizontalSpanWithEmptyColumnCommands(componentCB, rect.y, rect.x + rect.width - 1,
									rect.height, newgridDataWidth - rect.width);
							if (numColsIncrement > 0) {
								// Need to expand the number of columns and add fillers in each row
								componentCB.append(createNumColumnsCommand(numColumns + numColsIncrement));
								for (int i = 0; i < numColsIncrement; i++) {
									componentCB.append(createInsertColumnWithinRowCommands(endCellLocation.x, rect.y, null, null));
								}
							}
						} else {
							CommandBuilder columncb = new CommandBuilder();
							// First look through the columns were spanning out of and see if there empty columns
							int projectedNumCols = numColumns;
							for (int i = 0; i < rect.width - newgridDataWidth; i++) {
								Command cmd = createRemoveColumnCommand(rect.x + rect.width - 1 - i, control, projectedNumCols);
								if (cmd != null) {
									columncb.append(cmd);
									projectedNumCols--;
								}
							}
							if (!columncb.isEmpty())
								// If all that is left in the column(s) is filler labels, remove them.
								componentCB.append(columncb.getCommand());
							else {
								// Decrease the horizontal span and put filler labels in the columns it was decreased by
								Rectangle childRect = rect.getCopy();
								// Need to put filler labels in the more than one row if the child spans vertically.
								for (int i = 0; i < rect.height; i++) {
									// otherwise put a filler label in place of the cell the is left open by decrementing the vertical span
									EObject beforeObject = findNextValidObject(childRect.x + childRect.width, childRect.y + childRect.height - 1);
									for (int j = 0; j < rect.width - newgridDataWidth; j++) {
										componentCB.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
									}
									childRect.y--;
								}
							}
						}
					}
				}
				if (spanDirection == PositionConstants.SOUTH) {
					int newgridDataHeight = endCellLocation.y - childCellLocation.y + 1;
					if (newgridDataHeight != rect.height) {
						Object heightObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(newgridDataHeight)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfVerticalSpan, heightObject);
						if (newgridDataHeight > rect.height) {
							// Increase the verticalSpan
							// but first see if we can expand into empty cells without increasing the number of columns
							int numRowsIncrement = createVerticalSpanWithEmptyRowCommands(componentCB, rect.y + rect.height - 1, rect.x, rect.width,
									newgridDataHeight - rect.height);
							if (numRowsIncrement > 0) {
								List childCols = new ArrayList(rect.width);
								for (int i = 0; i < rect.width; i++) {
									childCols.add(new Integer(rect.x + i));
								}
								// For adding a row, add filler labels in cells where the child is not occupied 
								for (int i = 0; i < numRowsIncrement; i++) {
									componentCB.append(createFillerLabelsForSpannedRowCommand(rect.y + rect.height, childCols));
								}
							}
						} else {
							// decrease the verticalSpan
							CommandBuilder rowcb = new CommandBuilder();
							for (int i = 0; i < rect.height - newgridDataHeight; i++) {
								rowcb.append(createRemoveRowCommand(rect.y + rect.height - 1 - i, control));
							}
							if (!rowcb.isEmpty())
								// If all that is left on the row(s) is filler labels, remove them.
								componentCB.append(rowcb.getCommand());
							else {
								Rectangle childRect = rect.getCopy();
								for (int i = 0; i < rect.height - newgridDataHeight; i++) {
									// otherwise put a filler label in place of the cell the is left open by decrementing the vertical span
									EObject beforeObject = findNextValidObject(childRect.x + 1, childRect.y + childRect.height - 1);
									for (int j = 0; j < rect.width; j++) {
										componentCB.append(policy.getCreateCommand(createFillerLabelObject(), beforeObject));
									}
									childRect.y--;
								}
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
		if (atRow < table[0].length - 1) {
			EObject child = table[atColumn][atRow];
			if (child != null && !isFillerLabel(child)) {
				int index = children.indexOf(table[atColumn][atRow]);
				if (index != -1 && childrenDimensions[index].y != atRow)
					return UnexecutableCommand.INSTANCE;
			}
		}
		
		CommandBuilder cb = new CommandBuilder();
		// if this the last row, we must check to see if any cells are empty and replace them
		// with filler labels so the new object falls into the correct cell location in the new row.
		if (atRow >= table[0].length) {
			for (int i = 0; i < table.length; i++) {
				if (table[i][table[0].length-1] == EMPTY)
					cb.append(policy.getCreateCommand(createFillerLabelObject(), null));
			}
		}
		
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
	private Command createFillerLabelsForSpannedRowCommand (int atRow, List atColumns) {
		EObject[][] table = getLayoutTable();
		if (table[0].length < 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;

		CommandBuilder cb = new CommandBuilder();
		EObject beforeObject = findNextValidObject(0, atRow);
		// Add the row by adding filler labels in all columns except the columns the control is spanning into.
		// If any of the controls spans vertically, don't add filler, just expand it one more row.
		for (int i = 0; i < numColumns; i++) {
			boolean addFiller = true;
			if (!(atColumns.contains(new Integer(i)))) {
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
	public Command createInsertColumnWithinRowCommands (int atColumn, int atRow, Object addedControl, Request request) {
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
		// Also have to handle the case in which the before control spans vertically.
		EObject beforeObject = table[atColumn][atRow];
		if (isFillerLabel(beforeObject))
			beforeObject = ((FillerLabel)beforeObject).realObject;
		for (int i = 0; i < table[0].length; i++) {
			EObject child = table[atColumn][i];
			if (isFillerLabel(child))
				child = ((FillerLabel)child).realObject;
			int index = children.indexOf(child);

			// This is the row where the new control is put
			if (i == atRow && addedControl != null && index != -1) {
				Rectangle rect = childrenDimensions[index];
				if (rect.height != defaultVerticalSpan && rect.y != i) {
					if (atColumn + 1 < numColumns)
						child = findNextValidObject(atColumn + 1, i);
					else if (i + 1 < table[0].length)
						child = findNextValidObject(0, i + 1);
					else
						child = null;
				}
				if (request instanceof CreateRequest)
					cb.append(policy.getCreateCommand(addedControl, child));
				else
					cb.append(policy.getMoveChildrenCommand(Collections.singletonList(addedControl), child));
					
			}

			// This is other rows that need have a filler label added to the end
			if (i != atRow && index != -1) {
				Rectangle rect = childrenDimensions[index];
				// If the before object spans vertically, find the next valid object to insert filler
				if (rect.height != defaultVerticalSpan && child == beforeObject) {
					// if the spanned control doesn't start in this row, find next valid object to insert filler
					if (rect.y != i) {
						if (atColumn + 1 < numColumns)
							child = findNextValidObject(atColumn + 1, i);
						else if (i + 1 < table[0].length)
							child = findNextValidObject(0, i + 1);
						else
							child = null;
					}
				} else if (i + 1 < table[0].length && table[0][i+1] != EMPTY) {
					child = table[0][i+1];
					if (isFillerLabel(child))
						child = ((FillerLabel)child).realObject;
				} else 
					child = null;
				cb.append(policy.getCreateCommand(createFillerLabelObject(), child));
			}
		}
		return cb.getCommand();
	}

	/*
	 * Insert filler labels in each row at a specific column position in order to move the controls
	 * over one column yet maintain all other row/column positions before that column.
	 */
	public Command createInsertColumnCommands (Object addedControl, Request request, int atColumn, int atRow, boolean isLastColumn) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
//		if (table[0].length <= 1)
//			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;
		// Add a filler label prior to each object that is in the atColumn of each row. 
		// This must be done for each row except the row where the actual control has been added.
		for (int i = 0; i < table[0].length; i++) {
			if (isLastColumn && i == 0)
				continue;
			// If this the last row, we must check to see if any cells are empty and replace them
			// with filler labels so the new object falls into the correct cell location.
			if (i == table[0].length - 1) {
				for (int j = 0; j < table.length; j++) {
					if (table[j][i] == EMPTY)
						cb.append(policy.getCreateCommand(createFillerLabelObject(), null));
				}
			}
			if (table[atColumn][i] != EMPTY) {
				EObject child = table[atColumn][i];
				if (isFillerLabel(child))
					child = ((FillerLabel)child).realObject;
				if (i == atRow) {
					// This is the row where the new control is put
					int index = children.indexOf(child);
					if (index != -1) {
						Rectangle rect = childrenDimensions[index];
						//Handle case where the before child is spanned vertically and the starting
						// row is not this row. Need to get the next valid child.
						if (rect.height != defaultVerticalSpan && rect.y != i){
							if (atColumn + 1 < numColumns)
								child = findNextValidObject(atColumn + 1, i);
							else if (i + 1 < table[0].length)
								child = findNextValidObject(0, i + 1);
							else
								child = null;
						}
						if (request instanceof CreateRequest)
							cb.append(policy.getCreateCommand(addedControl, child));
						else
							cb.append(policy.getMoveChildrenCommand(Collections.singletonList(addedControl), child));
					}
				} else {
					if (isFillerLabel(child))
						child = ((FillerLabel) child).realObject;
					int index = children.indexOf(child);
					if (index != -1) {
						Rectangle rect = childrenDimensions[index];
						// Just a control that doesn't span either way or
						// a control that spans horizontal, not vertically, and starts atColumn.
						if ((rect.width == defaultHorizontalSpan && rect.height == defaultVerticalSpan)
								|| (rect.width > defaultHorizontalSpan && rect.height == defaultVerticalSpan && rect.x == atColumn))
							cb.append(policy.getCreateCommand(createFillerLabelObject(), child));

						// If the column is going through a control that is spanning more than one column,
						// we need to expand it by one instead of adding filler. Only do this if
						// this control starts in the row in case it spans vertically too.
						else if (rect.width > defaultHorizontalSpan && rect.x != atColumn && rect.y == i)
							cb.append(createHorizontalSpanCommand(child, rect.width + 1));

						// If the control spans vertically, add a filler before this if it's the row it
						// starts in otherwise we need to add a filler before the
						// the control after this one... or at the end (nextChild=null)
						else if (rect.height > defaultVerticalSpan) {
							if (rect.y == i)
								cb.append(policy.getCreateCommand(createFillerLabelObject(), child));
							else {
								EObject nextChild = null;
								if (atColumn + 1 < numColumns)
									nextChild = findNextValidObject(atColumn + 1, i);
								else if (i + 1 < table[0].length)
									nextChild = findNextValidObject(0, i + 1);
								cb.append(policy.getCreateCommand(createFillerLabelObject(), nextChild));
							}
						}
					} else
						cb.append(policy.getCreateCommand(createFillerLabelObject(), child));
				}
			}
		}
		// Handle when adding control to the last column on the last row which is basically
		// adding to the end of the container
		if (isLastColumn && atRow >= table[0].length) {
			if (request instanceof CreateRequest)
				cb.append(policy.getCreateCommand(addedControl, null));
			else
				cb.append(policy.getMoveChildrenCommand(Collections.singletonList(addedControl), null));
		}
		return cb.getCommand();
	}
	public Command createFillerLabelsForDeletedControlCommands (EObject deletedChild) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table[0].length <= 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;
		int index = children.indexOf(deletedChild);
		if (index != -1) {
			Rectangle rect = childrenDimensions[index];
			EObject nextChild = null;
			// Iterate through the rows and columns where the child resides and replace with
			// filler labels where it spans horizontally and vertically.
			for (int i = rect.y; i < rect.y + rect.height && i < table[0].length; i++) {
				if (rect.x + 1 < numColumns)
					nextChild = findNextValidObject(rect.x + 1, i);
				else if (i + 1 < table[0].length)
					nextChild = findNextValidObject(0, i + 1);
				// If the deleted child spans horizontally, loop through and create appropriate
				// number of filler labels.
				for (int j = 0; j < rect.width; j++) {
					cb.append(policy.getCreateCommand(createFillerLabelObject(), nextChild));
				}
			}
		}

		return cb.getCommand();
	}

	public Command createFillerLabelsForMovedControlCommands (EObject movedChild, EObject beforeChild) {
		CommandBuilder cb = new CommandBuilder();
		EObject[][] table = getLayoutTable();
		// If there is only one row (or none), no need to add empty labels.
		if (table[0].length <= 1)
			return null;
		List children = (List) getContainer().eGet(sfCompositeControls);
		if (children.isEmpty())
			return null;
		int index = children.indexOf(movedChild);
		if (index != -1) {
			Rectangle rect = childrenDimensions[index];
			EObject nextChild = null;
			// Iterate through the rows and columns where the child resides and replace with
			// filler labels where it spans horizontally and vertically.
			for (int i = rect.y; i < rect.y + rect.height && i < table[0].length; i++) {
				if (rect.x + 1 < numColumns)
					nextChild = findNextValidObject(rect.x + 1, i);
				else if (i + 1 < table[0].length)
					nextChild = findNextValidObject(0, i + 1);
				// If the deleted child spans horizontally, loop through and create appropriate
				// number of filler labels.
				if (nextChild == beforeChild)
					nextChild = movedChild;
				for (int j = 0; j < rect.width; j++) {
					cb.append(policy.getCreateCommand(createFillerLabelObject(), nextChild));
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
	 * Create the command to set the horizontalSpan value to the default value.
	 */
	public Command createHorizontalSpanDefaultCommand(EObject control) {
		return createHorizontalSpanCommand(control, defaultHorizontalSpan);
	}


	/*
	 * Create the command to set the verticalSpan value of the GridData for a child control.
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

	/*
	 * Create the command to set the verticalSpan value to the default value.
	 */
	public Command createVerticalSpanDefaultCommand(EObject control) {
		return createVerticalSpanCommand(control, defaultVerticalSpan);
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
	/*
	 * Return true if the object is a filler label.
	 * This helper class should only be used when the object passed in (i.e. control) is an object of 
	 * the internal layoutTable. 
	 */
	private boolean isFillerLabel(Object control) {
		return control instanceof FillerLabel;
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
				if (isFillerLabel(table[i][atRow]))
					cb.append(policy.getDeleteDependentCommand(((FillerLabel)table[i][atRow]).realObject));
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
				if (isFillerLabel(table[atColumn][i]))
					cb.append(policy.getDeleteDependentCommand(((FillerLabel)table[atColumn][i]).realObject));
			}
			if (projectNumColumns != 1)
				cb.append(createNumColumnsCommand(projectNumColumns - 1));
			return cb.getCommand();
		}
		return null;
	}
	
	/*
	 * For spanning horizontally, walk through the row starting atColumn and delete empty or 
	 * filler labels so we can expand into the empty columns. If no empty cells, numColIncrement
	 * is returned so the number of columns can be incremented on the overall grid.
	 */
	private int createHorizontalSpanWithEmptyColumnCommands(CommandBuilder cb, int atRow, int atColumn, int childHeight, int numColsIncrement) {
		EObject[][] table = getLayoutTable();
		if (atColumn < table.length && atRow < table[0].length) {
			for (int i = atColumn; i < table.length && numColsIncrement != 0; i++) {
				if (isHorizontalSpaceAvailable(i, atRow, atRow + childHeight - 1)) {
					for (int j = atRow; j < atRow + childHeight; j++) {
						if (isFillerLabel(table[i][j]))
							cb.append(policy.getDeleteDependentCommand(((FillerLabel)table[i][j]).realObject));
					}
					numColsIncrement--;
				}
			}
		}
		return numColsIncrement;
	}
	/*
	 * For spanning vertically, walk through the rows starting atRow and atColumn and delete empty or 
	 * filler labels so we can expand into the empty rows. If no empty cells, numRowIncrement
	 * is returned so the number of rows can be incremented on the overall grid by creating additional
	 * filler labels for the additional rows.
	 */
	private int createVerticalSpanWithEmptyRowCommands(CommandBuilder cb, int atRow, int atColumn, int childWidth, int numRowsIncrement) {
		EObject[][] table = getLayoutTable();
		if (atColumn < table.length && atRow < table[0].length && (atRow + numRowsIncrement) < table[0].length) {
			for (int i = atRow; i < table[0].length && numRowsIncrement != 0; i++) {
				if (isVerticalSpaceAvailable(i, atColumn, atColumn + childWidth - 1)) {
					for (int j = atColumn; j < atColumn + childWidth; j++) {
						if (isFillerLabel(table[j][i]))
							cb.append(policy.getDeleteDependentCommand(((FillerLabel)table[j][i]).realObject));
					}
					numRowsIncrement--;
				}
			}
		}
		return numRowsIncrement;
	}

	/*
	 * Find next object in the table that is not EMPTY and doesn't vertically span more than one row.
	 * Note: filler labels are valid
	 */
	private EObject findNextValidObject (int columnStart, int rowStart) {
		EObject[][] table = getLayoutTable();
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
					EObject child = table[j][i];
					if (isFillerLabel(child))
						child = ((FillerLabel)child).realObject;
					int index = children.indexOf(child);
					// If the row is going through a control that is spanning vertically more than one
					// row, skip it. This is checked by comparing this control's starting y (row) value
					// with where we are in the table lookup
					if (index != -1 && childrenDimensions[index].y == i) {
						foundObject = child;
						break;
					}
				}
			}
			if (firstpass)
				col = 0;
		}
		return foundObject;
	}
	/*
	 * Return true if the cells atRow from columnStart to columnEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isVerticalSpaceAvailable(int atRow, int columnStart, int columnEnd) {
		boolean result = true;
		EObject[][] table = getLayoutTable();
		if (table.length == 0 || table[0].length == 0 || columnStart >= table.length || columnEnd >= table.length || atRow >= table[0].length)
			return false;
		for (int i = columnStart; i <= columnEnd; i++) {
			if (table[i][atRow] != EMPTY && !isFillerLabel(table[i][atRow])) {
				result = false;
				break;
			}
		}
		return result;
	}
	/*
	 * Return true if the cells atCol from rowStart to rowEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isHorizontalSpaceAvailable(int atCol, int rowStart, int rowEnd) {
		boolean result = true;
		EObject[][] table = getLayoutTable();
		if (table.length == 0 || table[0].length == 0 || rowStart >= table[0].length || rowEnd >= table[0].length || atCol >= table.length)
			return false;
		for (int i = rowStart; i <= rowEnd; i++) {
			if (table[atCol][i] != EMPTY && !isFillerLabel(table[atCol][i])) {
				result = false;
				break;
			}
		}
		return result;
	}
	/*
	 * Helper method to determine if there are any styles set for this label.
	 * Look for SWT.NONE as the second argument.
	 */
	private boolean isNoStyleSet(IJavaObjectInstance child) {
		if (child != null && child.getAllocation() instanceof ParseTreeAllocation) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) child.getAllocation();
			PTExpression allocationExpression = ptAlloc.getExpression();
			if (allocationExpression instanceof PTClassInstanceCreation) {
				PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExpression;
				if (classInstanceCreation.getArguments().size() == 2) {
					PTExpression secondArg = (PTExpression) classInstanceCreation.getArguments().get(1);
					if (secondArg instanceof PTFieldAccess && ((PTFieldAccess) secondArg).getField().equals("NONE"))
						return true;
				}
			}
		}
		return false;
	}
	private int getTargetVMSWTVersion(){
		
		if(targetVMVersion != -1) return targetVMVersion;
		// This is cache'd on the edit domain for performance
		Integer editDomainTargetVMVersion = (Integer) fEditDomain.getData(TARGET_VM_VERSION_KEY);
		if(editDomainTargetVMVersion != null){
			targetVMVersion = editDomainTargetVMVersion.intValue();
			return targetVMVersion;
		}
		// Get the target VM version for the first time for the edit domain from the target VM itself 
		ProxyFactoryRegistry proxyFactoryRegistry = getLayoutManagerBeanProxy().getProxyFactoryRegistry();		
		IExpression expression = proxyFactoryRegistry.getBeanProxyFactory().createExpression();		
		// Evaluate the expression "org.eclipse.swt.SWT.getVersion()";
		IProxyBeanType swtBeanTypeProxy = proxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy(expression,"org.eclipse.swt.SWT");
		IProxyMethod getVersionMethodProxy = swtBeanTypeProxy.getMethodProxy(expression,"getVersion");
		ExpressionProxy proxy = expression.createSimpleMethodInvoke(getVersionMethodProxy,swtBeanTypeProxy,null,true);
		proxy.addProxyListener(new ProxyListener(){
			public void proxyResolved(ProxyEvent event) {	
				targetVMVersion = ((IIntegerBeanProxy)event.getProxy()).intValue();
				fEditDomain.setData(TARGET_VM_VERSION_KEY,new Integer(targetVMVersion));
			}
			public void proxyNotResolved(ProxyEvent event) {				
			}
			public void proxyVoid(ProxyEvent event) {				
			}
		});	
		try {
			expression.invokeExpression();
		} catch (Exception e) {
			JavaVEPlugin.log("Unable to work out target SWT version for GridLayoutHelper", Level.WARNING);	
			targetVMVersion = 3100;
		}
		if(targetVMVersion == -1){
			JavaVEPlugin.log("Unable to work out target SWT version for GridLayoutHelper", Level.WARNING);
			targetVMVersion = 3100;			
		}
		return targetVMVersion;
	}

	public void setEditDomain(EditDomain editDomain) {
		fEditDomain = editDomain;
	}

	
	public int getDefaultHorizontalSpan() {
		return defaultHorizontalSpan;
	}

	
	public int getDefaultVerticalSpan() {
		return defaultVerticalSpan;
	}
	
}
