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
 *  $Revision: 1.9 $  $Date: 2005-06-06 22:34:20 $
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager.DisplayRunnable.RunnableException;

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
	protected EStructuralFeature sfHorizontalSpan, sfVerticalSpan;
	protected int defaultHorizontalSpan, defaultVerticalSpan;
	protected EObject[][] layoutTable = null;
	protected Rectangle[] childrenDimensions = null;
	protected int numColumns = -1;
	private IBeanProxy fContainerBeanProxy = null;
	private IBeanProxy fLayoutManagerBeanProxy = null;

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
				while (layoutTable[col][row] != null) {
					col += 1;
					if (col >= numColumns) {
						row += 1;
						col = 0;
					}
				}
				// if there's not enough columns left for the horizontal span, go to
				// next row
				if (col != 0 && (col + horizontalSpan - 1) >= numColumns) {
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
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(childEditPart), null, false);
				if (spanDirection == PositionConstants.EAST) {
					int gridWidth = endCellLocation.x - childCellLocation.x + 1;
					Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridWidth)); //$NON-NLS-1$
					componentCB.applyAttributeSetting(gridData, sfHorizontalSpan, widthObject);
				}
				if (spanDirection == PositionConstants.SOUTH) {
					int gridHeight = endCellLocation.y - childCellLocation.y + 1;
					Object heightObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridHeight)); //$NON-NLS-1$
					componentCB.applyAttributeSetting(gridData, sfVerticalSpan, heightObject);
				}
				componentCB.applyAttributeSetting(control, sfLayoutData, gridData);
				cb.append(componentCB.getCommand());
			}
		}
		if (cb.isEmpty()) { return UnexecutableCommand.INSTANCE; }
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
}
