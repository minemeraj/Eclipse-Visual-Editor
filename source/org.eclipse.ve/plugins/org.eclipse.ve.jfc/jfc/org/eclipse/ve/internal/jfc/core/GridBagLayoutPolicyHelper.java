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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagLayoutPolicyHelper.java,v $
 *  $Revision: 1.14 $  $Date: 2005-07-11 20:59:42 $ 
 */

import java.util.*;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.ve.internal.jcm.BeanDecorator;
/**
 * GridBag layout policy helper.
 *
 * Note: the constraint is a GridBagConstraint. It will be converted
 * to the appropriate java object instance when necessary.
 */
public class GridBagLayoutPolicyHelper extends LayoutPolicyHelper implements IActionFilter {
	static Integer ZERO_INTEGER = new Integer(0);
	protected ResourceSet rset;
	List fComponents = null;
	ArrayList childrenXYBounds,
		columnDividerPositions,
		rowDividerPositions,
		sortedLeftEdges,
		sortedRightEdges,
		sortedTopEdges,
		sortedBottomEdges = null;

	private EStructuralFeature sfGridX, sfGridY, sfGridWidth, sfGridHeight, sfWeightX, sfWeightY, sfFill, sfInsets, sfIPadX, sfIPadY;

	private JavaHelpers primInt, primDouble, gridBagConstraints;

	private EObject layoutTable[][];

	protected static String[] fillInitStrings = new String[] { "java.awt.GridBagConstraints.NONE", //$NON-NLS-1$
		"java.awt.GridBagConstraints.BOTH", //$NON-NLS-1$
		"java.awt.GridBagConstraints.HORIZONTAL", //$NON-NLS-1$
		"java.awt.GridBagConstraints.VERTICAL" //$NON-NLS-1$
	};

	public GridBagLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}

	public GridBagLayoutPolicyHelper() {
	}

	/**
	 * Assign the fill value from a fill preference based on the component type.
	 * Assign the weightx and weighty based on the fill preference.
	 */
	protected void adjustForFillPreferences(IJavaObjectInstance component, GridBagConstraint constraint) {

		int fillPref = getGridBagConstraintsFillPreference(component);

		if (fillPref == GridBagConstraint.NONE)
			return; // no need to adjust if the fill preference is NONE

		constraint.fill = fillPref;
		if (fillPref == GridBagConstraint.HORIZONTAL) {
			constraint.weightx = 1.0d;
		} else if (fillPref == GridBagConstraint.VERTICAL) {
			constraint.weighty = 1.0d;
		} else if (fillPref == GridBagConstraint.BOTH) {
			constraint.weightx = 1.0d;
			constraint.weighty = 1.0d;
		}
	}

	/**
	 * Convert the IDE GridBagConstraint to the java object instance constraint
	 */
	protected IJavaObjectInstance convertConstraint(Object constraint) {
		GridBagConstraint gridBagConstraint = (GridBagConstraint) constraint;
		GridBagConstraint defaultConstraint = new GridBagConstraint(); //  used to compare default values
		IJavaObjectInstance javaGridBagConstraint = (IJavaObjectInstance) BeanUtilities.createJavaObject("java.awt.GridBagConstraints", rset, (String)null); //$NON-NLS-1$
		if (gridBagConstraint.gridx != defaultConstraint.gridx) {
			Object gridx = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.gridx));
			javaGridBagConstraint.eSet(sfGridX, gridx);
		}
		if (gridBagConstraint.gridy != defaultConstraint.gridy) {
			Object gridy = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.gridy));
			javaGridBagConstraint.eSet(sfGridY, gridy);
		}
		if (gridBagConstraint.gridwidth != defaultConstraint.gridwidth) {
			Object gridwidth = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.gridwidth));
			javaGridBagConstraint.eSet(sfGridWidth, gridwidth);
		}
		if (gridBagConstraint.gridheight != defaultConstraint.gridheight) {
			Object gridheight = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.gridheight));
			javaGridBagConstraint.eSet(sfGridHeight, gridheight);
		}
		if (gridBagConstraint.weightx != defaultConstraint.weightx) {
			Object weightx = BeanUtilities.createJavaObject(primDouble, rset, String.valueOf(gridBagConstraint.weightx));
			javaGridBagConstraint.eSet(sfWeightX, weightx);
		}
		if (gridBagConstraint.weighty != defaultConstraint.weighty) {
			Object weighty = BeanUtilities.createJavaObject(primDouble, rset, String.valueOf(gridBagConstraint.weighty));
			javaGridBagConstraint.eSet(sfWeightY, weighty);
		}
		if (gridBagConstraint.fill != defaultConstraint.fill) {
			Object fill = BeanUtilities.createJavaObject(primInt, rset, fillInitStrings[gridBagConstraint.fill]);
			javaGridBagConstraint.eSet(sfFill, fill);
		}
		// The insets value is a class, not a primitive.
		if (!gridBagConstraint.insets.equals(defaultConstraint.insets)) {
			Insets insets = gridBagConstraint.insets;
			IJavaObjectInstance insetsBean = (IJavaObjectInstance) BeanUtilities.createJavaObject("java.awt.Insets", rset, InsetsJavaClassCellEditor.getJavaInitializationString(insets)); //$NON-NLS-1$ 
			javaGridBagConstraint.eSet(sfInsets, insetsBean);
		}
		if (gridBagConstraint.ipadx != defaultConstraint.ipadx) {
			Object ipadx = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.ipadx));
			javaGridBagConstraint.eSet(sfIPadX, ipadx);
		}
		if (gridBagConstraint.ipady != defaultConstraint.ipady) {
			Object ipady = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(gridBagConstraint.ipady));
			javaGridBagConstraint.eSet(sfIPadY, ipady);
		}

		return javaGridBagConstraint;
	}

	/**
	 * Determine what the default constraint(s) are for this layout manager
	 * and assign a constraint to each child.
	 * Return a List with a constraint for each child.
	 */
	public List getDefaultConstraint(List children) {
		ArrayList constraints = new ArrayList(children.size());

		// We are going to iterate through the children and generate a new GridBagConstraints bean
		for (int i = 0; i < children.size(); i++) {
			Object child = children.get(i);
			if (child instanceof IJavaObjectInstance)
				constraints.add(getDefaultConstraint((IJavaObjectInstance) child));
		}
		return constraints;
	}
	/**
	 * Return a default GridBagConstaints constructed with a null ctor
	 */
	public GridBagConstraint getDefaultConstraint(IJavaObjectInstance component) {
		GridBagConstraint constraint = new GridBagConstraint();
		adjustForFillPreferences(component, constraint);
		return constraint;
	}
	/**
	 * Return a GridBagConstaints with the specific gridx and gridy if the cell is empty.
	 * Return null if the cell is already occupied.
	 */
	public GridBagConstraint getConstraint(IJavaObjectInstance component, int gridx, int gridy) {
		GridBagConstraint constraint = new GridBagConstraint();
		constraint.gridx = gridx;
		constraint.gridy = gridy;
		adjustForFillPreferences(component, constraint);
		return constraint;
	}
	/**
	 * Return a list of gridBag constraints to be assigned to each of the components.
	 */
	protected List getGridBagConstraints(List components) {
		resetLists();
		fComponents = components;
		ArrayList constraints = new ArrayList(components.size());
		for (int i = 0; i < components.size(); i++) {
			// Find the nearby Column and Row dividers
			int nearestLeftColumnDivider = 0,
				nearestRightColumnDivider = 0,
				nearestAboveRowDivider = 0,
				nearestBelowRowDivider = 0,
				position = 0;
			Rectangle childBounds = getBounds((IJavaObjectInstance) components.get(i));
			// find the nearest left column divider
			for (int j = 0; j < getColumnDividerPositions().size(); j++) {
				position = ((Integer) getColumnDividerPositions().get(j)).intValue();
				if (position <= childBounds.x) {
					nearestLeftColumnDivider = j;
				}
			}
			// find the nearest right column divider
			for (int j = 0; j < getColumnDividerPositions().size(); j++) {
				position = ((Integer) getColumnDividerPositions().get(j)).intValue();
				if (position >= (childBounds.x + childBounds.width)) {
					nearestRightColumnDivider = j;
					break;
				}
			}
			// find the nearest top row divider
			for (int j = 0; j < getRowDividerPositions().size(); j++) {
				position = ((Integer) getRowDividerPositions().get(j)).intValue();
				if (position <= childBounds.y) {
					nearestAboveRowDivider = j;
				}
			}
			// find the nearest bottom row divider
			for (int j = 0; j < getRowDividerPositions().size(); j++) {
				position = ((Integer) getRowDividerPositions().get(j)).intValue();
				if (position >= (childBounds.y + childBounds.height)) {
					nearestBelowRowDivider = j;
					break;
				}
			}
			// Calulate the Insets
			Insets insets =
				new Insets(
					childBounds.y - ((Integer) getRowDividerPositions().get(nearestAboveRowDivider)).intValue(),
					childBounds.x - ((Integer) getColumnDividerPositions().get(nearestLeftColumnDivider)).intValue(),
					((Integer) getRowDividerPositions().get(nearestBelowRowDivider)).intValue() - (childBounds.y + childBounds.height),
					((Integer) getColumnDividerPositions().get(nearestRightColumnDivider)).intValue()
						- (childBounds.x + childBounds.width));
			// Calculate the padding.
			Dimension dim = getPreferredSize((IJavaObjectInstance) components.get(i));
			int ipadx = childBounds.width - dim.width;
			int ipady = childBounds.height - dim.height;

			// Create a new GridBagConstaints and assign the values that we calculated previously.
			GridBagConstraint constraint = new GridBagConstraint();
			constraint.gridx = nearestLeftColumnDivider;
			constraint.gridy = nearestAboveRowDivider;
			constraint.gridwidth = nearestRightColumnDivider - nearestLeftColumnDivider;
			constraint.gridheight = nearestBelowRowDivider - nearestAboveRowDivider;
			constraint.insets = insets;
			constraint.ipadx = ipadx;
			constraint.ipady = ipady;
			// Adjust for fill preference and set the weightx and weighty
			adjustForFillPreferences((IJavaObjectInstance) components.get(i), constraint);
			// Add this constraint to the list of constraints
			constraints.add(constraint);
		}
		return constraints;
	}
	/**
	 * Adds additional column or row dividers based on the position of each child in the container
	 */
	protected void addInternalDividerPositionsTo(List dividerPositions, List near, List far) {
		int farIndex = 0, nearIndex = 0, thisEdge = 0;
		int prevEdge = ((Integer) near.get(0)).intValue();
		List prevEdgeSource = near;
		List thisEdgeSource = null;
		while (nearIndex < near.size() && farIndex < far.size()) {
			if (((Integer) near.get(nearIndex)).intValue() < ((Integer) far.get(farIndex)).intValue()) {
				thisEdge = ((Integer) near.get(nearIndex)).intValue();
				nearIndex++;
				thisEdgeSource = near;
			} else {
				thisEdge = ((Integer) far.get(farIndex)).intValue();
				farIndex++;
				thisEdgeSource = far;
			}
			if (prevEdgeSource.equals(far) && thisEdgeSource.equals(near)) {
				// Creates divider using middle of near and far
				dividerPositions.add(new Integer((thisEdge + prevEdge) / 2));
			}
			prevEdge = thisEdge;
			prevEdgeSource = thisEdgeSource;
		}
	}
	protected List getChildrenXYBounds() {
		if (childrenXYBounds == null) {
			childrenXYBounds = new ArrayList(fComponents.size());
			// create a list of the xy constraints for each child component
			for (int i = 0; i < fComponents.size(); i++) {
				IJavaObjectInstance comp = (IJavaObjectInstance) fComponents.get(i);
				IBeanProxy compProxy = BeanProxyUtilities.getBeanProxy(comp);
				IRectangleBeanProxy rectangleProxy = BeanAwtUtilities.invoke_getBounds(compProxy);
				Rectangle r =
					new Rectangle(rectangleProxy.getX(), rectangleProxy.getY(), rectangleProxy.getWidth(), rectangleProxy.getHeight());
				childrenXYBounds.add(i, r);
			}
		}
		return childrenXYBounds;
	}
	/**
	 * Answer a collection of start x positions for each column in the gridbag layout
	 */
	protected List getColumnDividerPositions() {
		if (columnDividerPositions == null) {
			Insets containerInsets = getContainerInsets();
			columnDividerPositions = new ArrayList();
			// Now add the left-most edge, either the container's left edge (0) or the leftest left edge of a component.
			Integer leftMostEdge = (Integer) getSortedLeftEdges().get(0);
			Integer leftOfContainer = new Integer(containerInsets.left);
			if (leftOfContainer.compareTo(leftMostEdge) <= 0)
				columnDividerPositions.add(leftOfContainer);
			else
				columnDividerPositions.add(leftMostEdge);
			// Add the internal boundaries based on the right and left edges
			addInternalDividerPositionsTo(columnDividerPositions, getSortedLeftEdges(), getSortedRightEdges());
			// Add the right-most boundary, either the containers right edge or the rightest-right of a component.
			Integer rightMostEdge = (Integer) getSortedRightEdges().get(getSortedRightEdges().size() - 1);
			Rectangle containerBounds = getBounds(getContainer());
			Integer containerWidth = new Integer(containerBounds.width - containerInsets.right);
			if (containerWidth.compareTo(rightMostEdge) >= 0)
				columnDividerPositions.add(containerWidth);
			else
				columnDividerPositions.add(rightMostEdge);
			Collections.sort(columnDividerPositions);
		}
		return columnDividerPositions;
	}
	/**
	 * Return a Rectangle containing the bounds of a bean.
	 */
	protected Rectangle getBounds(IJavaObjectInstance aBean) {
		IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy(aBean);
		IRectangleBeanProxy rectangleProxy = BeanAwtUtilities.invoke_getBounds(beanProxy);
		Rectangle r = new Rectangle(rectangleProxy.getX(), rectangleProxy.getY(), rectangleProxy.getWidth(), rectangleProxy.getHeight());
		return r;
	}

	/**
	 * Return the GridBagLayout origin
	 */
	public org.eclipse.draw2d.geometry.Point getContainerLayoutOrigin() {
		IBeanProxy aContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		IBeanProxy aLayoutManagerBeanProxy = BeanAwtUtilities.invoke_getLayout(aContainerBeanProxy);
			IMethodProxy getLayoutOrigin = aLayoutManagerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aLayoutManagerBeanProxy.getTypeProxy().getTypeName(), "getLayoutOrigin", //$NON-NLS-1$
	null);
		IPointBeanProxy pointProxy = (IPointBeanProxy) getLayoutOrigin.invokeCatchThrowableExceptions(aLayoutManagerBeanProxy);
		return new org.eclipse.draw2d.geometry.Point(pointProxy.getX(), pointProxy.getY());
	}
	/*
	 * Return true if the container has no children, false if it does.
	 * Since Swing's GridBagLayout doesn't refresh it's layout information if all the components 
	 * have been removed, we can't rely on the getLayoutDimensions() call to return the correct information. 
	 * Instead we need to first query the parent container to see if it has any children.
	 */
	public boolean isContainerEmpty() {
		IBeanProxy aContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		if (aContainerBeanProxy != null) {
			IMethodProxy getComponentCountMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aContainerBeanProxy.getTypeProxy().getTypeName(), "getComponentCount", null); //$NON-NLS-1$
			if (getComponentCountMethodProxy != null) {
				IIntegerBeanProxy countProxy =
					(IIntegerBeanProxy) getComponentCountMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
				if (countProxy != null && countProxy.intValue() > 0)
					return false;
			}
		}
		return true;
	}
	/**
	 * Return the GridBagLayout dimensions which is 2 dimensional array that contains 2 arrays:
	 *  1. an int array of all the column widths
	 *  2. an int array of all the row heights
	 */
	public int[][] getContainerLayoutDimensions() {
		int[] columnWidths = null, rowHeights = null;
		int[][] result = new int[2][];
		result[0] = new int[0];
		result[1] = new int[0];
		IBeanProxy aContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		IBeanProxy aLayoutManagerBeanProxy = BeanAwtUtilities.invoke_getLayout(aContainerBeanProxy);
		IMethodProxy getLayoutDimensions = aLayoutManagerBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aLayoutManagerBeanProxy.getTypeProxy().getTypeName(), "getLayoutDimensions", null); //$NON-NLS-1$
		IArrayBeanProxy arrayProxy = (IArrayBeanProxy) getLayoutDimensions.invokeCatchThrowableExceptions(aLayoutManagerBeanProxy);
		if (arrayProxy != null) {
			try {
				IArrayBeanProxy arrayProxyColumnWidths = (IArrayBeanProxy) arrayProxy.get(0);
				if (arrayProxyColumnWidths != null) {
					columnWidths = new int[arrayProxyColumnWidths.getLength()];
					for (int i = 0; i < arrayProxyColumnWidths.getLength(); i++) {
						columnWidths[i] = ((IIntegerBeanProxy) arrayProxyColumnWidths.get(i)).intValue();
					}
					result[0] = columnWidths;
				}
				IArrayBeanProxy arrayProxyRowHeights = (IArrayBeanProxy) arrayProxy.get(1);
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
		}
		return result;
	}

	/**
	 * Key for accessing the gridbar fill preference on a per class basis. Stored on BeanDecorator.
	 */
	public static final String GRIDBAG_FILL_PREFERENCE_KEY = "org.eclipse.ve.internal.jfc.core.gridbagfilepreferencekey"; //$NON-NLS-1$

	/**
	 * Get the fill preference for a specific component type... i.e. for JTextField, GridBagConstraints.HORIZONTAL
	 */
	protected int getGridBagConstraintsFillPreference(IJavaObjectInstance component) {

		BeanDecorator bd =
			(BeanDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
				component.getJavaType(),
				BeanDecorator.class,
				GRIDBAG_FILL_PREFERENCE_KEY);
		if (bd != null) {
			return ((EEnumLiteral) bd.getKeyedValues().get(GRIDBAG_FILL_PREFERENCE_KEY)).getValue();
		}

		return GridBagConstraint.NONE; // default for Component and all others
	}
	/*
	 * Return the Insets of a bean.
	 * We need this in order to include the insets of the parent container in calculating the child's insets when switching. 
	 */
	protected Insets getContainerInsets() {
		IBeanProxy aBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		IMethodProxy getInsetsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aBeanProxy.getTypeProxy().getTypeName(), "getInsets", null); //NON-NLS-1$ //$NON-NLS-1$
		IBeanProxy insetsProxy = getInsetsMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
		int top = 0, left = 0, bottom = 0, right = 0;
		if (insetsProxy != null) {
			IIntegerBeanProxy intProxy = null;
			try {
				IFieldProxy fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("top"); //$NON-NLS-1$
				intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
				if (intProxy != null) {
					top = intProxy.intValue();
				}
				fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("left"); //$NON-NLS-1$
				intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
				if (intProxy != null) {
					left = intProxy.intValue();
				}
				fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("bottom"); //$NON-NLS-1$
				intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
				if (intProxy != null) {
					bottom = intProxy.intValue();
				}
				fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("right"); //$NON-NLS-1$
				intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
				if (intProxy != null) {
					right = intProxy.intValue();
				}
			} catch (ThrowableProxy e) {
				// Do nothing. The default Insets(0,0,0,0) will be returned.
			}
		}
		//	return new Insets();
		return new Insets(top, left, bottom, right);
	}

	/*
	 * Create and return an internal 2 dimensional array in which each position reflects the cell 
	 * location of the gridbag layout and contains the constraint component that occupies that cell.
	 * If a component has a gridwidth and/or gridheight, the component will be stored in
	 * multiple positions of the table to reflect cell occupation.
	 */
	protected EObject[][] getLayoutTable() {
		if (layoutTable == null) {
			int[][] dimensions = getContainerLayoutDimensions();
			layoutTable = new EObject[dimensions[0].length][dimensions[1].length];
			if (getContainer() != null) {
				IJavaInstance intValue = null;
				Iterator containerComponents = ((List) getContainer().eGet(sfComponents)).iterator();
				while (containerComponents.hasNext()) {
					boolean isDefault = false;
					EObject gridbagComponent = (EObject) containerComponents.next();
					// The component has the constraint set onto it. Get the gridx and gridy properties and set
					// them into the layout table at the appropriate cell location
					IJavaObjectInstance gridbagconstraint = (IJavaObjectInstance) gridbagComponent.eGet(sfConstraintConstraint);
					if (gridbagconstraint != null) {
						if(gridbagconstraint.eIsSet(sfGridX))
							intValue = (IJavaInstance) gridbagconstraint.eGet(sfGridX);
						else
							isDefault = true;
						if (intValue != null || isDefault) {
							int x = -1;
							if(!isDefault && intValue != null)
								x = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(intValue)).intValue();
							if(gridbagconstraint.eIsSet(sfGridY)){
								intValue = (IJavaInstance) gridbagconstraint.eGet(sfGridY);
								isDefault = false;
							} else
								isDefault = true;
							if (intValue != null || isDefault) {
								int y = -1;
								if(!isDefault && intValue != null)
									y = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(intValue)).intValue();
								if (x < layoutTable.length && y < layoutTable[0].length){
									if(x == -1){
										x = getGridXOfDefaultConstraint(gridbagComponent, dimensions);
									}
									if(y == -1){
										y = getGridYOfDefaultConstraint(gridbagComponent, dimensions);
									}
									if(x >= 0 && y >= 0)
										layoutTable[x][y] = gridbagComponent;
								}
								// If the gridWidth and gridHeight values are greater than 1, store this same
								// component in the table as well so the cell location will indicate it is occupied.
								intValue = (IJavaInstance) gridbagconstraint.eGet(sfGridWidth);
								int gridWidth = 1;
								if (intValue != null && x >= 0 && y >= 0) {
									gridWidth = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(intValue)).intValue();
									if (gridWidth > 1) {
										for (int i = 1; i < gridWidth && (x + i) < layoutTable.length; i++) {
											layoutTable[x + i][y] = gridbagComponent;
										}
									}
								}
								intValue = (IJavaInstance) gridbagconstraint.eGet(sfGridHeight);
								if (intValue != null && x >= 0 && y >= 0) {
									int gridHeight = ((IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy(intValue)).intValue();
									if (gridHeight > 1) {
										for (int i = 1; i < gridHeight && (y + i) < layoutTable[0].length; i++) {
											if(gridWidth > 1){
												for (int j = 0; j < gridWidth 
													&& (x + j) < layoutTable.length 
													&& (y + i) < layoutTable[0].length; j++){
													
													layoutTable[x+j][y+i] = gridbagComponent;
												}
											} else if ((y + i) < layoutTable[0].length){
												layoutTable[x][y + i] = gridbagComponent;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return layoutTable;
	}
	/*
	 * Return the commands that will adjust the constraints of any components affected
	 * by inserting a new column or new row (or both) into the GridBagLayout
	 */
	public Command adjustConstraintsCommand(IJavaObjectInstance child, Point cellLocation, boolean insertNewColumn, boolean insertNewRow) {
		CommandBuilder cb = new CommandBuilder();
		// First get the components table that shows the positions of the components on the grid
		EObject table[][] = getLayoutTable();
		if (table.length != 0 && table[0].length != 0) {
			/* Loop through the table and check to see if the gridx and/or gridy should be changed
			 * based on whether a column and/or row was inserted and whether this component's 
			 * position is at or after the cell position where this occurred.
			 */
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[0].length; j++) {
					if (table[i][j] != null) {
						EObject gridbagConstraintComponent = table[i][j];
						/* 
						 * If this component at this location is the same as the component in the
						 * cell location to its left or above, don't adjust because it's already
						 * been processed and has gridx or gridy greater than 1 (i.e. spans more than 1 cell).
						 */
						if ((i - 1 >= 0 && gridbagConstraintComponent == table[i - 1][j])
							|| (j - 1 >= 0 && gridbagConstraintComponent == table[i][j - 1]))
							continue;
						/*
						 * If this component at this location is the actual component that is the target of this
						 * request, do not adjust it since it's being moved and it's apply command is handled elsewhere.
						 */
						IJavaObjectInstance gridbagcomponent = (IJavaObjectInstance) gridbagConstraintComponent.eGet(sfConstraintComponent);
						if (gridbagcomponent != null && gridbagcomponent == child)
							continue;
						// If this component is affected based on it's position to the new column and/or row, increment it's gridx and/or gridy	
						IJavaObjectInstance gridbagconstraint =
							(IJavaObjectInstance) gridbagConstraintComponent.eGet(sfConstraintConstraint);
						if (gridbagconstraint != null) {
							RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
							boolean constraintChanged = false;
							if (insertNewColumn && (i >= cellLocation.x)) {
								constraintChanged = true;
								Object gridx = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(i + 1));
								componentCB.applyAttributeSetting(gridbagconstraint, sfGridX, gridx);
							}
							if (insertNewRow && (j >= cellLocation.y)) {
								constraintChanged = true;
								Object gridy = BeanUtilities.createJavaObject(primInt, rset, String.valueOf(j + 1));
								componentCB.applyAttributeSetting(gridbagconstraint, sfGridY, gridy);
							}
							/* 
							 * If the constraint was changed due to the gridx and/or gridy adjustments,
							 * reapply the constraint again to the component constraint and add the 
							 * ruled command builder to the other main command builder so the canvas is refreshed.
							 */
							if (constraintChanged) {
								componentCB.applyAttributeSetting(gridbagConstraintComponent, sfConstraintConstraint, gridbagconstraint);
								cb.append(componentCB.getCommand());
							}
						}
					}
				}
			}
		}
		return cb.getCommand();
	}
	/**
	 * Return a Dimension containing the preferred size of a bean.
	 */
	protected Dimension getPreferredSize(IJavaObjectInstance aBean) {
		IBeanProxy aBeanProxy = BeanProxyUtilities.getBeanProxy(aBean);
			IMethodProxy getPreferredSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aBeanProxy.getTypeProxy().getTypeName(), "getPreferredSize", //$NON-NLS-1$
	null);
		IDimensionBeanProxy dimensionProxy = (IDimensionBeanProxy) getPreferredSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
		Dimension dim = new Dimension(dimensionProxy.getWidth(), dimensionProxy.getHeight());
		return dim;
	}

	/**
	 * Answer a collection of start y positions for each row in the gridbag layout
	 */
	protected List getRowDividerPositions() {
		Insets containerInsets = getContainerInsets();
		if (rowDividerPositions == null) {
			rowDividerPositions = new ArrayList();
			// Now add the left-most edge, either the container's left edge (0) or the leftest left edge of a component.
			Integer topMostEdge = (Integer) getSortedTopEdges().get(0);
			Integer topOfContainer = new Integer(containerInsets.top);
			if (topOfContainer.compareTo(topMostEdge) <= 0)
				rowDividerPositions.add(topOfContainer);
			else
				rowDividerPositions.add(topMostEdge);
			// Add the internal boundaries based on the top and bottom edges
			addInternalDividerPositionsTo(rowDividerPositions, getSortedTopEdges(), getSortedBottomEdges());
			// Add the right-most boundary, either the containers right edge or the rightest-right
			Integer bottomMostEdge = (Integer) getSortedBottomEdges().get(getSortedBottomEdges().size() - 1);
			Rectangle containerBounds = getBounds(getContainer());
			Integer containerHeight = new Integer(containerBounds.height - containerInsets.bottom);
			if (containerHeight.compareTo(bottomMostEdge) >= 0)
				rowDividerPositions.add(containerHeight);
			else
				rowDividerPositions.add(bottomMostEdge);
			Collections.sort(rowDividerPositions);
		}
		return rowDividerPositions;
	}
	/**
	 *Return a sorted collection of ints representing the bottom edges of all components.
	 */
	protected List getSortedBottomEdges() {
		if (sortedBottomEdges == null) {
			sortedBottomEdges = new ArrayList(getChildrenXYBounds().size());
			for (int i = 0; i < getChildrenXYBounds().size(); i++) {
				Rectangle rect = (Rectangle) getChildrenXYBounds().get(i);
				sortedBottomEdges.add(new Integer(rect.y + rect.height));
			}
			Collections.sort(sortedBottomEdges);
		}
		return sortedBottomEdges;
	}
	/**
	 *Return a sorted collection of ints representing the left edges of all components.
	 */
	protected List getSortedLeftEdges() {
		if (sortedLeftEdges == null) {
			sortedLeftEdges = new ArrayList(getChildrenXYBounds().size());
			for (int i = 0; i < getChildrenXYBounds().size(); i++) {
				Rectangle rect = (Rectangle) getChildrenXYBounds().get(i);
				sortedLeftEdges.add(new Integer(rect.x));
			}
			Collections.sort(sortedLeftEdges);
		}
		return sortedLeftEdges;
	}
	/**
	 *Return a sorted collection of ints representing the right edges of all components.
	 */
	protected List getSortedRightEdges() {
		if (sortedRightEdges == null) {
			sortedRightEdges = new ArrayList(getChildrenXYBounds().size());
			for (int i = 0; i < getChildrenXYBounds().size(); i++) {
				Rectangle rect = (Rectangle) getChildrenXYBounds().get(i);
				sortedRightEdges.add(new Integer(rect.x + rect.width));
			}
			Collections.sort(sortedRightEdges);
		}
		return sortedRightEdges;
	}
	/**
	 *Return a sorted collection of ints representing the top edges of all components.
	 */
	protected List getSortedTopEdges() {
		if (sortedTopEdges == null) {
			sortedTopEdges = new ArrayList(getChildrenXYBounds().size());
			for (int i = 0; i < getChildrenXYBounds().size(); i++) {
				Rectangle rect = (Rectangle) getChildrenXYBounds().get(i);
				sortedTopEdges.add(new Integer(rect.y));
			}
			Collections.sort(sortedTopEdges);
		}
		return sortedTopEdges;
	}
	public boolean isCellEmpty(int x, int y) {
		EObject[][] table = getLayoutTable();
		if (table.length != 0 && table[0].length != 0 && x < table.length && y < table[0].length)
			return table[x][y] == null;
		return true;
	}
	/*
	 * Return true if it's okay to move constraintComponent into cell x,y.
	 * In order for this be valid, the cell x,y must be either empty or if not empty,
	 * the cell can be occupied by the same component whose gridwidth and gridheight are greater
	 * than one thus allowing it to occupy more than one cell.  
	 */
	public boolean isCellValidForMove(Point targetCellLocation, EObject constraintComponent) {
		int x = targetCellLocation.x;
		int y = targetCellLocation.y;

		Dimension dim = getComponentGridDimensions(constraintComponent);
		if (dim != null) {
			for (int i = 0; i < dim.width; i++) {
				if (!isCellEmpty(x + i, y) && getCellOccupant(x + i, y) != constraintComponent)
					return false;
			}
			for (int i = 0; i < dim.height; i++) {
				if (!isCellEmpty(x, y + i) && getCellOccupant(x, y + i) != constraintComponent)
					return false;
			}
		}
		return true;
	}
	/*
	 * Return the actual number of columns (Dimension.width value) and rows (Dimension.height value)
	 * that a component occupies in the gridbag layout. 
	 */
	protected Dimension getComponentGridDimensions(EObject component) {
		EObject table[][] = getLayoutTable();
		int startx = 0, starty = 0;
		// First walk through the table to find the starting cell of the constraint component
		if (table.length != 0 && table[0].length != 0) {
			int i = 0, j = 0;
			boolean foundit = false;
			for (i = 0; i < table.length && !foundit; i++) {
				for (j = 0; j < table[0].length && !foundit; j++) {
					if (table[i][j] != null && table[i][j] == component) {
						// Found the starting cell location for this component
						foundit = true;
						startx = i;
						starty = j;
						break;
					}
				}
			}
			// Now that we found it, walk through the remaining cells in the x and y direction
			// to determine the gridwidth and gridheight.
			if (foundit) {
				int gridWidth = 1, gridHeight = 1;
				for (i = startx + 1; i < table.length; i++) {
					if (table[i][j] != null && table[i][j] == component)
						gridWidth++;
				}
				for (i = startx, j = starty + 1; j < table[0].length; j++) {
					if (table[i][j] != null && table[i][j] == component)
						gridHeight++;
				}
				// Return the gridwidth and gridheight values in a draw2d Dimension object
				return new Dimension(gridWidth, gridHeight);
			}
		}

		return null;
	}
	// Return the constraint component that occupies a specific cell location
	protected EObject getCellOccupant(int x, int y) {
		EObject[][] table = getLayoutTable();
		if (table.length != 0 && table[0].length != 0 && x >= 0 && x < table.length && y >= 0 && y < table[0].length)
			return table[x][y];
		return null;
	}
	private void resetLists() {
		childrenXYBounds = null;
		columnDividerPositions = null;
		rowDividerPositions = null;
		sortedLeftEdges = null;
		sortedRightEdges = null;
		sortedTopEdges = null;
		sortedBottomEdges = null;
		layoutTable = null;
	}
	public void refresh() {
		resetLists();
	}
	public void setContainerPolicy(VisualContainerPolicy policy) {
		super.setContainerPolicy(policy);

		if (policy != null) {
			// Eventually we will be set with a policy. At that time we can compute these.
			rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
			sfGridX = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDX);
			sfGridY = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDY);
			sfGridWidth = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDWIDTH);
			sfGridHeight = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_GRIDHEIGHT);
			sfWeightX = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_WEIGHTX);
			sfWeightY = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_WEIGHTY);
			sfFill = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_FILL);
			sfInsets = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_INSETS);
			sfIPadX = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_IPADX);
			sfIPadY = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_IPADY);

			primInt = Utilities.getJavaType("int", rset); //$NON-NLS-1$
			primDouble = Utilities.getJavaType("double", rset); //$NON-NLS-1$
			gridBagConstraints = Utilities.getJavaClass("java.awt.GridBagConstraints", rset); //$NON-NLS-1$
		}
		resetLists();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ILayoutPolicyHelper#getAddChildrenCommand(java.util.List, java.util.List, java.lang.Object)
	 */
	public Command getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		if (childrenComponents.isEmpty() || constraints.isEmpty() || (childrenComponents.size() > 1))
			return UnexecutableCommand.INSTANCE;

		EObject constraintComponent = visualFact.create(classConstraintComponent);
		List componentConstraints = Collections.singletonList(constraintComponent);
		GridBagConstraint gridBagConstraint = (GridBagConstraint) constraints.get(0);
		IJavaObjectInstance component = (IJavaObjectInstance) childrenComponents.get(0);
		// If this component is added or moved from within this or another gridbag layout, use it's existing constraint so
		// we don't lose the original settings... we just want to set the gridx, gridy settings.
		CommandBuilder cb = new CommandBuilder();
		EObject cc = InverseMaintenanceAdapter.getIntermediateReference(component, sfComponents, sfConstraintComponent, component);
		if (cc != null) {
			IJavaObjectInstance constraintObject = (IJavaObjectInstance) cc.eGet(sfConstraintConstraint);
			if (constraintObject != null && gridBagConstraints.isInstance(constraintObject)) {
				// This is a GridBagConstraints object. Just change the gridx and gridy, then apply the constraint to the ConstraintComponent
				RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
				Object intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridBagConstraint.gridx)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(constraintObject, sfGridX, intObject);
				intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridBagConstraint.gridy)); //$NON-NLS-1$
				componentCB.applyAttributeSetting(constraintObject, sfGridY, intObject);
				componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, constraintObject);
				cb.append(componentCB.getCommand());
			}
		}
		// No commands created so the constraint didn't exist or wasn't a GridBagConstraint. Just create the default. 
		if (cb.isEmpty())
			constraintComponent.eSet(sfConstraintConstraint, gridBagConstraint != null ? convertConstraint(gridBagConstraint) : null);
		// Put the constraint into the constraint component.

		cb.append(policy.getAddCommand(componentConstraints, childrenComponents, position));
		return cb.getCommand();
	}
	public Command getSpanChildrenCommand(EditPart childEditPart, Point childCellLocation, Point endCellLocation, int spanDirection) {
		CommandBuilder cb = new CommandBuilder();
		EObject constraintComponent =
			InverseMaintenanceAdapter.getIntermediateReference(
				(EObject) childEditPart.getParent().getModel(),
				sfComponents,
				sfConstraintComponent,
				(IJavaObjectInstance) childEditPart.getModel());
		if (constraintComponent != null) {
			// First be sure the intermediate cells are unoccupied or are occupied by the same component
			if (checkIntermediateCellsOccupied(constraintComponent, childCellLocation, endCellLocation)) {
				IJavaObjectInstance gridbagconstraint = (IJavaObjectInstance) constraintComponent.eGet(sfConstraintConstraint);
				if (gridbagconstraint != null) {
					RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(childEditPart), null, false);
					if (spanDirection == PositionConstants.EAST) {
						int gridWidth = endCellLocation.x - childCellLocation.x + 1;
						Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridWidth)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridbagconstraint, sfGridWidth, widthObject);
					}
					if (spanDirection == PositionConstants.SOUTH) {
						int gridHeight = endCellLocation.y - childCellLocation.y + 1;
						Object heightObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gridHeight)); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridbagconstraint, sfGridHeight, heightObject);
					}
					componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, gridbagconstraint);
					cb.append(componentCB.getCommand());
				}
			}
		}
		if (cb.isEmpty()) {
			return UnexecutableCommand.INSTANCE;
		}
		return cb.getCommand();
	}

	/**
	 * Check that all the cells between the child location and the end location
	 * are either empty, or occupied by the given constraintComponent.
	 * 
	 * @param constraintComponent
	 * @param childCellLocation
	 * @param endCellLocation
	 * @return  true if no other constraints are occupying the intermediate cells, false otherwise
	 * 
	 * @since 1.0.0
	 */
	protected boolean checkIntermediateCellsOccupied(EObject constraintComponent, Point childCellLocation, Point endCellLocation) {
		boolean valid = true;
		// Check horizontally oriented differences
		for (int i = childCellLocation.x; valid && i <= endCellLocation.x; i++) {
			valid = isCellEmpty(i, endCellLocation.y)
				|| constraintComponent == getCellOccupant(i, endCellLocation.y);
		}
		// Check vertically oriented differences
		for (int i = childCellLocation.y; valid && i <= endCellLocation.y; i++) {
			valid = isCellEmpty(endCellLocation.x, i)
				|| constraintComponent == getCellOccupant(endCellLocation.x, i);
		}
		return valid;
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
				if (ep != null && ep.getEditPolicy(EditPolicy.LAYOUT_ROLE) instanceof IActionFilter) {
					return ((IActionFilter) ep.getEditPolicy(EditPolicy.LAYOUT_ROLE)).testAttribute(target, name, value);
				}
			}
		}
		return false;
	}

	/**
	 * Helper for the layout table to determine what the GridX is of a component with the 
	 * default constraint of -1.
	 * 
	 * @since 1.1
	 */
	private int getGridXOfDefaultConstraint(EObject gridbagComponent, int[][] dimensions) {
		int x = -1;
		IJavaObjectInstance component = 
			(IJavaObjectInstance) gridbagComponent.eGet(sfConstraintComponent);
		IBeanProxy compProxy = BeanProxyUtilities.getBeanProxy(component);
		IRectangleBeanProxy rectangleProxy = BeanAwtUtilities.invoke_getBounds(compProxy);
		int componentX = rectangleProxy.getX();
		int[] columnPositions = new int[dimensions[0].length + 1];
		int layoutOriginXMargin = getContainerLayoutOrigin().x;
		//adjust for left margin
		columnPositions[0] = layoutOriginXMargin;
		for(int i = 1; i < columnPositions.length; i++){
			columnPositions[i] = columnPositions[i - 1] + dimensions[0][i - 1];
		}
		int gridx = -1;
		
		for (int i = 0; i < columnPositions.length-1; i++) {
			int xpos = columnPositions[i];
			if (componentX >= xpos && componentX < columnPositions[i+1]) {
				gridx = i;

				/*
				 * Since column positions can be equal if there are columns that don't contain components,
				 * iterate back throught the columns positions to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && columnPositions[i] == columnPositions[j]; j--){
					gridx = j;
				}
				break;
			}
		}
		if(columnPositions.length == 1)
			x = 0;
		else
			x = gridx;
		
		return x;
	}
	
	/**
	 * Helper for the layout table to determine what the GridY is of a component with the 
	 * default constraint of -1.
	 * 
	 * @since 1.1
	 */
	private int getGridYOfDefaultConstraint(EObject gridbagComponent, int[][] dimensions) {
		int y = -1;
		
		IJavaObjectInstance component = 
			(IJavaObjectInstance) gridbagComponent.eGet(sfConstraintComponent);
		IBeanProxy compProxy = BeanProxyUtilities.getBeanProxy(component);
		IRectangleBeanProxy rectangleProxy = BeanAwtUtilities.invoke_getBounds(compProxy);
		int componentY = rectangleProxy.getY();
		int[] rowPositions = new int[dimensions[1].length + 1];
		int layoutOriginYMargin = getContainerLayoutOrigin().y;
		//adjust for top margin
		rowPositions[0] = layoutOriginYMargin;
		for(int i = 1; i < rowPositions.length; i++){
			rowPositions[i] = rowPositions[i - 1] + dimensions[1][i - 1];
		}
		int gridy = -1;
		
		for (int i = 0; i < rowPositions.length-1; i++) {
			int ypos = rowPositions[i];
			if (componentY >= ypos && componentY < rowPositions[i+1]) {
				gridy = i;

				/*
				 * Since row positions can be equal if there are rows that don't contain components,
				 * iterate back throught the row positions to get the first one with this position.
				 */
				int j;
				for (j = i; j >= 0 && rowPositions[i] == rowPositions[j]; j--){
					gridy = j;
				}
				break;
			}
		}
		if(rowPositions.length == 1)
			y = 0;
		else
			y = gridy;
		
		return y;
	}
}
