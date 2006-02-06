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
 *  $Revision: 1.50 $  $Date: 2006-02-06 17:14:41 $
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
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
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ContainerPolicy.Result;
import org.eclipse.ve.internal.cde.core.IContainmentHandler.StopRequestException;
import org.eclipse.ve.internal.cde.emf.AbstractEMFContainerPolicy.CorelatedResult;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.impl.KeyedInstanceLocationImpl;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.vce.rules.VCEPreSetCommand;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Layout policy helper for SWT Grid Layout.
 * 
 * @since 1.2.0
 */
public class GridLayoutPolicyHelper extends LayoutPolicyHelper implements IActionFilter {

	protected ResourceSet rset;

	protected EReference sfLayoutData, sfCompositeControls;

	protected EStructuralFeature sfHorizontalSpan, sfVerticalSpan, sfNumColumns, sfCompositeLayout, sfLabelText;

	protected int defaultHorizontalSpan, defaultVerticalSpan;

	protected GridComponent[][] glayoutTable;
	
	protected int numColumns = -1;
	protected int originalNumColumns;

	private IBeanProxy fContainerBeanProxy;

	private CompositeProxyAdapter containerProxyAdapter;

	private IBeanProxy fLayoutManagerBeanProxy;

	protected JavaClass classLabel;
	protected JavaClass classControl;
	
	private static final String NO_MODS = "NOMODS"; //$NON-NLS-1$
	private static final int NOT_MODIFIED_SPAN = -1;
	private static final int SET_TO_DEFAULT_SPAN = -2;
	public class GridComponent {
		
		public GridComponent(Object component, EObject componentEObject) {
			this.component = component;
			this.componentEObject = componentEObject;
		}
		
		public GridComponent(Object component, EObject componentEObject, Object requestType) {
			this(component, componentEObject);
			this.requestType = requestType;
			setupUseGriddata(componentEObject, requestType);
		}
		
		private void setupUseGriddata(EObject componentEObject, Object requestType) {
			useGriddata = null;
			if (RequestConstants.REQ_CREATE.equals(requestType) || RequestConstants.REQ_ADD.equals(requestType)) {
				// For add/create we need to see if there is an existing griddata. If so, then we need to 
				// set it as use griddata AND we need to seet the span to cancel span because it may
				// already of had a span set.
				if (componentEObject.eIsSet(sfLayoutData)) {
					IJavaObjectInstance gridData = (IJavaObjectInstance) componentEObject.eGet(sfLayoutData);
					JavaHelpers griddataType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.layout.GridData", rset);	//$NON-NLS-1$
					if (griddataType.isInstance(gridData)) {
						// We have one to copy over. If it was bad this is ok because for an add it would be orphaned and not actually retained.
						// For a create, that shouldn't occur, but if it does we'll leave it there.
						useGriddata = gridData;
						modSpanHeight = modSpanWidth = SET_TO_DEFAULT_SPAN;	// Need to cancel out any current settings
					}
				}
			}
		}
		
		public GridComponent(EObject child, boolean filler) {
			this(child, child, RequestConstants.REQ_CREATE);
			this.filler = filler;
		}
		
		public void setComponent(Object component, EObject componentEObject, Object requestType) {
			this.component = component;
			this.componentEObject = componentEObject;
			this.requestType = requestType;
			modSpanWidth = modSpanHeight = NOT_MODIFIED_SPAN;
			filler = false;
			setupUseGriddata(componentEObject, requestType);
		}
		
		/**
		 * Set into this component the moved component passed in. This is used for moved components.
		 * It is assumed that this GridComponent is the new GridComponent for the moved component
		 * sent in on the request. This "this" cannot be an existing component.
		 * @param gc
		 * 
		 * @since 1.2.0
		 */
		void setMovedComponent(GridComponent gc) {
			// If the moved component was not just added/created, (i.e. it was here before we started), then set to move state
			// so that it will be picked up as needing to be moved in the stopRequest. Else leave as add/create/move so that
			// it will be processed correctly for that appropriate state. We should not change an add/create to a move. It won't
			// work correctly in the container policy.
			requestType = NO_MODS.equals(gc.requestType) ? RequestConstants.REQ_MOVE : gc.requestType;
			modSpanWidth = modSpanHeight = SET_TO_DEFAULT_SPAN;	// Also cancel the span for the moved child.
			filler = gc.filler;
			useGriddata = gc.useGriddata;
		}
		
		public void setGriddata(IJavaObjectInstance griddata) {
			if (useGriddata == null)
				useGriddata = griddata;
		}
		
		GridComponent prev, next;
		boolean filler;
		Object component;
		EObject componentEObject;
		Object requestType = NO_MODS;
		int modSpanWidth = NOT_MODIFIED_SPAN;
		int modSpanHeight = NOT_MODIFIED_SPAN;
		Rectangle gridDimension;
		IJavaObjectInstance useGriddata;
		public boolean isFillerLabel() {
			return filler;
		}
		
		public void setSpanWidth(int spanWidth) {
			gridDimension.width = modSpanWidth = spanWidth;
			if (spanWidth == 1)
				modSpanWidth = SET_TO_DEFAULT_SPAN;
		}
		
		public void setSpanHeight(int spanHeight) {
			gridDimension.height = modSpanHeight = spanHeight;
			if (spanHeight == 1)
				modSpanHeight = SET_TO_DEFAULT_SPAN;
		}

	}
	protected GridComponent first,last;
	protected List deletedComponents;
	protected List orphanedComponents;
	
	/**
	 * Start processing a set of requests.
	 * Call {@link #stopRequest()} when finished.
	 * 
	 * @since 1.2.0
	 */
	public void startRequest() {
		getLayoutTable();
	}
	
	/**
	 * End the set of requests and get the command that
	 * the request has built up. 
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public Command stopRequest() {
		CommandBuilder cb = new CommandBuilder();
		
		if (numColumns != originalNumColumns)
			createNumColumnsCommand(numColumns, cb);
		
		// There is actually one optimization we can do and that is remove all trailing fillers. They don't
		// add anything. They won't create extra columns and they could create empty rows at the end, but we don't
		// want those anyway.

		GridComponent end = last;
		while (last != null && (last == EMPTY_GRID || last.isFillerLabel())) {
			GridComponent next = last.prev;
			if (last != EMPTY_GRID) {
				removeGridComponent(end);
				deleteComponent(end);
				glayoutTable[end.gridDimension.x][end.gridDimension.y] = EMPTY_GRID;				
			}
			end = next;
		}
		
		Object beforeComp = null;
		Object currentModState = null;
		List currentComponentGCs = new ArrayList();
		Object prevComp = beforeComp;
		// We build up from the end instead of from the beginning because we need to have the prevComp in place before we can put something
		// in front of it. If we went from the beginning, something may of been moved to a later spot and it would not be in the correct 
		// order in the real list.
		for(GridComponent gc = last; gc != null; gc = gc.prev) {
			if (gc.requestType != NO_MODS) {
				if (!gc.requestType.equals(currentModState)) {
					// We are switching to a new type, send out the old group.
					if (!currentComponentGCs.isEmpty()) {
						getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
						currentComponentGCs.clear();
					}
					beforeComp = prevComp;	// This new guy will now go before the latest prev component.
					currentModState = gc.requestType;
				}
				currentComponentGCs.add(0, gc);	// Since we build up backwards, we insert from the front so that it results in forward.
			} else {
				// Switch to no change, so put what we have.
				if (!currentComponentGCs.isEmpty()) {
					getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
					currentComponentGCs.clear();
					currentModState = null;
				}
			}
			
			handleSpanAtEnd(gc, cb);	// Handle if the span had changed.
			prevComp = gc.component;
		}

		// Do last group.
		if (!currentComponentGCs.isEmpty()) {
			getCommandForAddCreateMoveChildren(currentModState, currentComponentGCs, beforeComp, cb);
		}
		
		if (deletedComponents != null)
			cb.append(policy.getDeleteDependentCommand(deletedComponents).getCommand());
		
		if (orphanedComponents != null)
			cb.append(policy.getOrphanChildrenCommand(orphanedComponents).getCommand());
		
		refresh();
		return cb.getCommand();
	}
	
	private void handleSpanAtEnd(GridComponent gc, CommandBuilder cb) {
		if (gc.modSpanWidth != NOT_MODIFIED_SPAN || gc.modSpanHeight != NOT_MODIFIED_SPAN) {
			IJavaObjectInstance gridData = gc.useGriddata == null ? (IJavaObjectInstance) gc.componentEObject.eGet(sfLayoutData) : gc.useGriddata;
			JavaHelpers griddataType = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.layout.GridData", rset);	//$NON-NLS-1$
			boolean badGridData = !griddataType.isInstance(gridData);
			
			RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
			switch (gc.modSpanWidth) {
				case SET_TO_DEFAULT_SPAN:
					// If we have no griddata, no need to do a cancel.
					if (gridData != null) {
						if (badGridData)
							gc.useGriddata = gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(griddataType, rset, (String) null); //$NON-NLS-1$
						componentCB.cancelAttributeSetting(gridData, sfHorizontalSpan);
					}
					break;
				case NOT_MODIFIED_SPAN:
					// Do nothing, not modified.
					break;
				default:
					// Set to a specific span.
					if (gridData == null || badGridData)
						gc.useGriddata = gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(griddataType, rset, (String) null); //$NON-NLS-1$
					Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gc.modSpanWidth)); //$NON-NLS-1$
					componentCB.applyAttributeSetting(gridData, sfHorizontalSpan, widthObject);
					break;
			}
			switch (gc.modSpanHeight) {
				case SET_TO_DEFAULT_SPAN:
					// If we have no griddata, no need to do a cancel.
					if (gridData != null) {
						if (badGridData)
							gc.useGriddata = gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(griddataType, rset, (String) null); //$NON-NLS-1$
						componentCB.cancelAttributeSetting(gridData, sfVerticalSpan);
					}
					break;
				case NOT_MODIFIED_SPAN:
					// Do nothing, not modified.
					break;
				default:
					// Set to a specific span.
					if (gridData == null || badGridData)
						gc.useGriddata = gridData = (IJavaObjectInstance) BeanUtilities.createJavaObject(griddataType, rset, (String) null); //$NON-NLS-1$
					Object widthObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(gc.modSpanHeight)); //$NON-NLS-1$
					componentCB.applyAttributeSetting(gridData, sfVerticalSpan, widthObject);
					break;
			}
			
			if (gridData != null) {
				componentCB.applyAttributeSetting(gc.componentEObject, sfLayoutData, gridData);
				cb.append(componentCB.getCommand());
			}
		}
	}
	
	protected void deleteComponent(GridComponent gcomp) {
		if (!gcomp.requestType.equals(RequestConstants.REQ_ADD) && !gcomp.requestType.equals(RequestConstants.REQ_CREATE)) {
			// If it was create or add, then it wasn't here to begin with so no need to add to deleted list.
			addToDeleted(gcomp.component);
		}
	}
	
	protected void addToDeleted(Object child) {
		if (deletedComponents == null)
			deletedComponents = new ArrayList();
		deletedComponents.add(child);		
	}
	
	protected void addToOrphaned(Object child) {
		if (orphanedComponents == null)
			orphanedComponents = new ArrayList();
		orphanedComponents.add(child);		
	}

	protected IBeanProxy getContainerBeanProxy() {
		if (fContainerBeanProxy == null || !fContainerBeanProxy.isValid()) {
			fContainerBeanProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		}
		return fContainerBeanProxy;
	}

	protected CompositeProxyAdapter getContainerProxyAdapter() {
		if (containerProxyAdapter == null) {
			try {
				containerProxyAdapter = (CompositeProxyAdapter) BeanProxyUtilities.getBeanProxyHost(getContainer());
			} catch (ClassCastException e) {
				// Ignore this. Means it was wrong.
			}
		}
		return containerProxyAdapter;
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
		// Nothing extra to cancel.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.visual.ILayoutPolicyHelper#getDefaultConstraint(java.util.List)
	 */
	public List getDefaultConstraint(List children) {
		return Collections.nCopies(children.size(), null);
	}

	public final GridComponent EMPTY_GRID = new GridComponent(null, null);

	private static class AnyFeatureSetVisitor implements Visitor {

		public Object isSet(EStructuralFeature feature, Object value) {
			if (feature.getName().equals(JavaInstantiation.ALLOCATION))
				return null;
			else if (feature.isMany() && ((List) value).isEmpty())
				return null;
			return Boolean.TRUE;
		}
	}

	private static final AnyFeatureSetVisitor anyFeatureSetVisitor = new AnyFeatureSetVisitor();

	private void removeGridComponent(GridComponent gc) {
		if (first == gc)
			first = gc.next;
		else
			gc.prev.next = gc.next;
		if (last == gc)
			last = gc.prev;
		else
			gc.next.prev = gc.prev;
	}
	
	protected void insertGridGomponentAtBeginning(GridComponent gc) {
		gc.next = first;
		gc.prev = null;
		if (first == null)
			last = gc;
		else
			first.prev = gc;
		first = gc;
	}
	
	protected void insertGridComponentBefore(GridComponent gc, GridComponent before) {
		if (before != null) {
			if (before == first) {
				first = gc;
			} else
				before.prev.next = gc;
			gc.next = before;
			gc.prev = before.prev;
			before.prev = gc;
		} else
			addGridComponent(gc);
	}
	
	private void addGridComponent(GridComponent gc) {
		if (last == null)
			insertGridGomponentAtBeginning(gc);
		else {
			last.next = gc;
			gc.prev = last;
			gc.next = null;
			last = gc;
		}
	}
	
	private int getSetNumColumns() {
		try {
			// Get the actual set number of columns.
			IBeanProxy layoutProxy = getLayoutManagerBeanProxy();
			return ((IIntegerBeanProxy)layoutProxy.getTypeProxy().getFieldProxy("numColumns").get(layoutProxy)).intValue();
		} catch (ThrowableProxy e) {
		} catch (NullPointerException e) {
		} catch (ClassCastException e) {
		}
		return 0;
	}
	/**
	 * Get a representation of the grid. The grid is indexed by [column][row]. The value at each position is the child located at that position. Empty
	 * cells will have null values.
	 * 
	 * @return the layout table. This table and its contents must not be modified.
	 * 
	 * @since 1.0.0
	 */
	public GridComponent[][] getLayoutTable() {
		if (glayoutTable == null) {
			int[][] dimensions = getContainerLayoutDimensions();
			if (dimensions == null)
				return null;
			glayoutTable = new GridComponent[dimensions[0].length][dimensions[1].length];
			// Original num columns is the actual setting in the layout.
			originalNumColumns = getSetNumColumns();
			numColumns = dimensions[0].length;
			// If empty container, don't continue.
			if (glayoutTable.length < 1 || glayoutTable[0].length < 1) {
				return glayoutTable;
			}

			int row = 0;
			int col = 0;
			int horizontalSpan;
			int verticalSpan;

			int childNum = 0;
			List children = (List) getContainer().eGet(sfCompositeControls);
			Iterator itr = children.iterator();

			while (itr.hasNext()) {
				IJavaObjectInstance child = (IJavaObjectInstance) itr.next();
				GridComponent gcomp = new GridComponent(child, child);
				addGridComponent(gcomp);
				IJavaObjectInstance childData = (IJavaObjectInstance) child.eGet(sfLayoutData);
				if (childData != null) {
					try {
						horizontalSpan = getIntValue(sfHorizontalSpan, childData);
						verticalSpan = getIntValue(sfVerticalSpan, childData);
					} catch (IllegalArgumentException e) {
						// Not a grid data.
						horizontalSpan = defaultHorizontalSpan;
						verticalSpan = defaultVerticalSpan;
					}
				} else {
					horizontalSpan = defaultHorizontalSpan;
					verticalSpan = defaultVerticalSpan;
				}

				Rectangle r = new Rectangle();

				// Find the next un-occupied cell
				while (row < glayoutTable[0].length && glayoutTable[col][row] != null) {
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

				// Add the child to the table in all spanned cells. Handle users coding mistakes by not spanning past the end of the table.
				int maxColSpan = Math.min(col+horizontalSpan, glayoutTable.length);
				int maxRowSpan = Math.min(row+verticalSpan, glayoutTable[col].length);
				for (int coli = col; coli < maxColSpan; coli++) {
					for (int rowj = row; rowj < maxRowSpan; rowj++) {
						if (classLabel.isInstance(child) && (FeatureValueProviderHelper.visitSetFeatures(child, anyFeatureSetVisitor) == null)
								&& isNoStyleSet(child)) {
							gcomp.filler = true;
						}
						glayoutTable[coli][rowj] = gcomp;
					}
				}

				r.x = col;
				r.y = row;
				r.width = maxColSpan-col;	// True horizontal span
				r.height = maxRowSpan-row;	// True vertical span
				gcomp.gridDimension = r;
				if (r.width != horizontalSpan)
					gcomp.setSpanWidth(r.width);
				if (r.height != verticalSpan)
					gcomp.setSpanHeight(r.height);
				childNum++;

				// Add the spanned columns to the column position
				col += r.width - 1;
			}
			// Now change all null entries to be EMPTY.
			for (int i = 0; i < glayoutTable.length; i++) {
				for (int j = 0; j < glayoutTable[i].length; j++) {
					if (glayoutTable[i][j] == null) {
						glayoutTable[i][j] = EMPTY_GRID;
					}
				}
			}
		}
		return glayoutTable;
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
	 * Get the child dimensions for the child that occupies the cell.
	 * 
	 * @param cell
	 * @return rectangle of dimensions or <code>null</code> if no child at that cell. This rectangle must not be modified.
	 *
	 * @see #getChildrenDimensions()
	 * @since 1.2.0
	 */
	public Rectangle getChildDimensions(Point cell) {
		getLayoutTable();
		if (cell.x < 0 || cell.x >= glayoutTable.length || cell.y < 0 || cell.y >= glayoutTable[0].length)
			return null;
		GridComponent gc = glayoutTable[cell.x][cell.y];
		if (gc != EMPTY_GRID)
			return gc.gridDimension;
		else
			return null;
	}
	
	/**
	 * Get the child dimensions for the child.
	 * @param child child to find dimensions of.
	 * @return rectangle of dimensions or <code>null</code> if child not a component. This rectangle must not be modified.
	 * 
	 * @since 1.2.0
	 */
	public Rectangle getChildDimensions(EObject child) {
		getLayoutTable();
		GridComponent gc = getComponent(child);
		if (gc != null)
			return gc.gridDimension;
		else
			return null;
	}

	/**
	 * Get the number of columns in the container's grid layout.
	 * 
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
	 * Get the number of rows int he container's grid layout.
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public int getNumRows() {
		getLayoutTable();
		return glayoutTable.length != 0 ? glayoutTable[0].length : 0;
	}

	/*
	 * Return true if a filler label occupies this cell location
	 */
	public boolean isFillerLabelAtCell(Point cell) {
		// Check to make sure the cell position is within the grid
		if (cell.x >= 0 && cell.x < glayoutTable.length && cell.y >= 0 && cell.y < glayoutTable[0].length)
			return glayoutTable[cell.x][cell.y].isFillerLabel();
		return false;
	}
	
	public boolean isEmptyAtCell(Point cell) {
		getLayoutTable();
		// Check to make sure the cell position is within the grid
		if (cell.x >= 0 && cell.x < glayoutTable.length && cell.y >= 0 && cell.y < glayoutTable[0].length)
			return glayoutTable[cell.x][cell.y] == EMPTY_GRID;
		return false;		
	}

	int[] expandableColumns, expandableRows;

	private static final String TARGET_VM_VERSION_KEY = "TARGET_VM_VERSION"; //$NON-NLS-1$

	private int targetVMVersion = -1;

	private EditDomain fEditDomain;

	/**
	 * Return the GridLayout dimensions which is 2 dimensional array that contains 2 arrays: 1. an int array of all the column widths 2. an int array
	 * of all the row heights
	 */
	public int[][] getContainerLayoutDimensions() {
		int[][] result = new int[2][];
		result[0] = result[1] = new int[0];

		if (isContainerBeanInvalid())
			return null;

		// Hack to grab the column/row information from the private fields of a GridLayout
		// The helper class org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper is used to calculate the column widths and row heights
		// Prior to 3.1 these were in package protected fields on GridLayout but these are no longer available so the helper class
		// computes the values
		String targetVMHelperTypeName = null;
		if (getTargetVMSWTVersion() >= 3100) {
			targetVMHelperTypeName = "org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper"; //$NON-NLS-1$
		} else {
			targetVMHelperTypeName = "org.eclipse.ve.internal.swt.targetvm.GridLayoutHelper_30"; //$NON-NLS-1$
		}
		IBeanTypeProxy gridLayoutHelperType = getLayoutManagerBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
				targetVMHelperTypeName); //$NON-NLS-1$
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
				IBeanProxy[] columnWidths = arrayProxyColumnWidths.getSnapshot();
				int resultWidths[] = new int[columnWidths.length];
				for (int i = 0; i < columnWidths.length; i++) {
					resultWidths[i] = ((IIntegerBeanProxy) columnWidths[i]).intValue();
				}
				result[0] = resultWidths;
			}
			IArrayBeanProxy arrayProxyRowHeights = (IArrayBeanProxy) getRowHeightsFieldProxy.get(gridLayoutHelperProxy);
			if (arrayProxyRowHeights != null) {
				IBeanProxy[] rowHeights = arrayProxyRowHeights.getSnapshot();
				int resultHeights[] = new int[rowHeights.length];
				for (int i = 0; i < rowHeights.length; i++) {
					resultHeights[i] = ((IIntegerBeanProxy) rowHeights[i]).intValue();
				}
				result[1] = resultHeights;
			}
		} catch (ThrowableProxy exc) {
			return null;
		}

		return result;
	}

	/**
	 * @return
	 * 
	 * @since 1.2.0
	 */
	protected boolean isContainerBeanInvalid() {
		return getContainerBeanProxy() == null || !getContainerBeanProxy().isValid();
	}

	/**
	 * Return the spacing information for the GridLayout. This information is packed into a Rectangle object, as follows:
	 * 
	 * Rectangle.x = RowLayout.marginWidth Recatngle.y = RowLayout.marginHeight Recatngle.width = RowLayout.horizontalSpacing Recatngle.height =
	 * RowLayout.verticalSpacing
	 * 
	 * @return Rectangle representing the GridLayout's spacing
	 * 
	 * @since 1.0.0
	 */
	public Rectangle getContainerLayoutSpacing() {

		if (isContainerBeanInvalid())
			return null;
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
		if (getContainerProxyAdapter() != null) { return getContainerProxyAdapter().getClientArea().getCopy(); }
		return null;
	}

	/**
	 * Span the child. 
	 * @param child
	 * @param newSpan
	 * @param spanDirection
	 * @param griddata use <code>null</code> if should use griddata from the child or create one if it doesn't have one. Supply
	 * an explicit griddata here in the case of building up from an implicit and we can't fluff one up because there is
	 * one being created before this, but not yet applied.
	 * 
	 * @since 1.2.0
	 */
	public void spanChild(EObject child, Point newSpan, int spanDirection, IJavaObjectInstance griddata) {
		GridComponent gc = getComponent(child);
		if (gc == null)
			return;
		gc.setGriddata(griddata);
		
		if (spanDirection == PositionConstants.EAST || spanDirection == PositionConstants.WEST) {
			int newgridDataWidth = newSpan.x;
			if (newgridDataWidth != gc.gridDimension.width) {
				if (newgridDataWidth > gc.gridDimension.width) {
					// Increase the horizontalSpan
					// but first see if we can expand into empty cells without increasing the number of columns
					int numColsIncrement = spanHorizontalIntoEmptyColumns(gc, gc.gridDimension.y, gc.gridDimension.x+gc.gridDimension.width,
							gc.gridDimension.height, newgridDataWidth - gc.gridDimension.width);
					if (numColsIncrement > 0) {
						// We now need to insert at this point this number of columns so that we can span into them.
						int insertColAt = gc.gridDimension.x+gc.gridDimension.width;	// Insert just after current end of control.
						while (numColsIncrement-- > 0)
							createNewCol(insertColAt);
						spanHorizontalIntoEmptyColumns(gc, gc.gridDimension.y, gc.gridDimension.x+gc.gridDimension.width,
							gc.gridDimension.height, newgridDataWidth - gc.gridDimension.width);	// Now span into these new ones.
					}
				} else {
					// Shrink by one column at a time from the right. Fill with filler and then see if column can go away.
					if (gc.gridDimension.x == 0 && gc.gridDimension.y > 0) {
						// However, there is a problem need to worry about. If this control is the first in the row and the previous
						// row ends in at least the number of the new smaller span empty cells, then we need to fill in those empty
						// with fillers. Otherwise the control will flow back onto the previous row.
						//
						// If we have too many empties, need to fill with filler. We'll do a little optimization here. We will actually
						// leave one less empty than the new span width. 
						// If we simply instead fill the last empty in the row, then all of the row will be filled with fillers
						// and this would create extra unneeded fillers.
						//
						// For example say we need to have no more than two empties in the prev row (i.e. span down to two for
						// the control in the next row), and we had the following layout:
						//
						// C C C E E E E
						//
						// The most efficient is to change to:
						//
						// C C C F F F E
						boolean allEmpties = true;
						int prevRow = gc.gridDimension.y-1;
						for (int numCols = newgridDataWidth, colToTest = glayoutTable.length-1; numCols > 0; numCols--, colToTest--) {
							if (glayoutTable[colToTest][prevRow] != EMPTY_GRID) {
								allEmpties = false;
								break;
							}
						}
						if (allEmpties) {
							// So replace the first one in the span of empties of the size we needed so that there will no longer
							// be enough room for the next control.
							replaceEmptyCell(createFillerComponent(), glayoutTable.length-newgridDataWidth, prevRow);
						}
					}
					int colToSpanOutOf = gc.gridDimension.x+gc.gridDimension.width-1;
					while (gc.gridDimension.width > newgridDataWidth) {
						gc.setSpanWidth(gc.gridDimension.width-1);
						int nextRow = gc.gridDimension.y+gc.gridDimension.height;
						Arrays.fill(glayoutTable[colToSpanOutOf], gc.gridDimension.y, nextRow, EMPTY_GRID);
						for (int row = gc.gridDimension.y; row < nextRow; row++) {
							replaceEmptyCell(createFillerComponent(), colToSpanOutOf, row);
						}
						removeColIfEmpty(colToSpanOutOf);
						colToSpanOutOf--;
					}
				}
			}
		} else if (spanDirection == PositionConstants.SOUTH) {
			int newgridDataHeight = newSpan.y;
			if (newgridDataHeight != gc.gridDimension.height) {
				if (newgridDataHeight > gc.gridDimension.height) {
					// Increase the horizontalSpan
					// but first see if we can expand into empty cells without increasing the number of columns
					int numRowsIncrement = spanVerticalIntoEmptyRows(gc, gc.gridDimension.y+gc.gridDimension.height, gc.gridDimension.x,
							gc.gridDimension.width, newgridDataHeight - gc.gridDimension.height);
					if (numRowsIncrement > 0) {
						// We now need to insert at this point this number of columns so that we can span into them.
						int insertRowAt = gc.gridDimension.y+gc.gridDimension.height;	// Insert just after current end of control.
						while (numRowsIncrement-- > 0)
							createNewRow(insertRowAt);
						spanVerticalIntoEmptyRows(gc, gc.gridDimension.y+gc.gridDimension.height, gc.gridDimension.x,
								gc.gridDimension.width, newgridDataHeight - gc.gridDimension.height);	// Now span into these new ones.
					}
				} else {
					// Shrink by one row at a time from the bottom. Fill with filler and then see if row can go away.
					int rowToSpanOutOf = gc.gridDimension.y+gc.gridDimension.height-1;
					while (gc.gridDimension.height > newgridDataHeight) {
						gc.setSpanHeight(gc.gridDimension.height-1);
						int nextCol = gc.gridDimension.x+gc.gridDimension.width;
						for (int col = gc.gridDimension.x; col < nextCol; col++) {
							glayoutTable[col][rowToSpanOutOf] = EMPTY_GRID;
						}
						for (int col = gc.gridDimension.x; col < nextCol; col++) {
							replaceEmptyCell(createFillerComponent(), col, rowToSpanOutOf);
						}
						removeRowIfEmpty(rowToSpanOutOf);
						rowToSpanOutOf--;
					}
				}
			}
		}
	}

	/*
	 * Create the filler label object used to help keep the positioning of the controls within the composite container
	 * 
	 * Note: Since this is a special label with no text (to keep it invisible in the layout), we'll create the allocation here so it will cause the
	 * LabelContainmentHandler NOT to set the text property.
	 */
	protected IJavaInstance createFillerLabelObject() {
		PTClassInstanceCreation ic = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation();
		ic.setType("org.eclipse.swt.widgets.Label"); //$NON-NLS-1$

		// set the arguments
		PTInstanceReference ir = InstantiationFactory.eINSTANCE.createPTInstanceReference();
		ir.setReference((IJavaInstance) policy.getContainer());
		PTFieldAccess fa = InstantiationFactory.eINSTANCE.createPTFieldAccess();
		PTName name = InstantiationFactory.eINSTANCE.createPTName("org.eclipse.swt.SWT"); //$NON-NLS-1$
		fa.setField("NONE"); //$NON-NLS-1$
		fa.setReceiver(name);
		ic.getArguments().add(ir);
		ic.getArguments().add(fa);
		
		JavaAllocation alloc = InstantiationFactory.eINSTANCE.createParseTreeAllocation(ic);
		IJavaInstance filler = BeanUtilities.createJavaObject("org.eclipse.swt.widgets.Label", rset, alloc); //$NON-NLS-1$
		AnnotationLinkagePolicy policy = fEditDomain.getAnnotationLinkagePolicy();
		Annotation annotation = AnnotationPolicy.createAnnotation(filler);
		
		KeyedInstanceLocationImpl instLoc = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
		instLoc.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
		instLoc.setValue(InstanceLocation.LOCAL_LITERAL);
		annotation.getKeyedValues().add(instLoc);
		
		EStringToStringMapEntryImpl nameEntry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
		nameEntry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
		nameEntry.setValue(NameInMemberPropertyDescriptor.FORCE_NAME_IN_COMPOSITION_PREFIX+"filler"); //$NON-NLS-1$
		annotation.getKeyedValues().add(nameEntry);
		
		policy.setModelOnAnnotation(filler, annotation);
		return filler;
	}

	private void createNumColumnsCommand(int numCols, CommandBuilder cb) {
		EObject parent = (EObject) policy.getContainer();
		IJavaInstance gridLayout = (IJavaInstance) parent.eGet(sfCompositeLayout);
		RuledCommandBuilder componentCB = new RuledCommandBuilder(policy.getEditDomain(), null, false);
		if (numCols > 0) {
			Object intObject = BeanUtilities.createJavaObject("int", rset, String.valueOf(numCols)); //$NON-NLS-1$
			componentCB.applyAttributeSetting(gridLayout, sfNumColumns, intObject);
		} else {
			componentCB.cancelAttributeSetting(gridLayout, sfNumColumns);
		}
		// Need to touch gridLayout on composite so that composite knows it has a changed grid layout.
		componentCB.applyAttributeSetting(parent, sfCompositeLayout, gridLayout);
		cb.append(componentCB.getCommand()); 
	}


	public void refresh() {
		glayoutTable = null;
		deletedComponents = orphanedComponents = null;
		first = last = null;
		numColumns = originalNumColumns = -1;
	}

	public void setContainerPolicy(VisualContainerPolicy policy) {
		super.setContainerPolicy(policy);

		if (policy != null) {
			fEditDomain = policy.getEditDomain();
			// Eventually we will be set with a policy. At that time we can compute these.
			rset = JavaEditDomainHelper.getResourceSet(policy.getEditDomain());
			sfCompositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
			sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
			sfHorizontalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_SPAN);
			sfVerticalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_VERTICAL_SPAN);
			sfNumColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_NUM_COLUMNS);
			sfCompositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
			classLabel = Utilities.getJavaClass("org.eclipse.swt.widgets.Label", rset); //$NON-NLS-1$
			classControl = Utilities.getJavaClass("org.eclipse.swt.widgets.Control", rset); //$NON-NLS-1$
			sfLabelText = classLabel.getEStructuralFeature("text"); //$NON-NLS-1$

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String) Enable the Show/Hide Grid action on the
	 *      Beans viewer depending on the layout EditPolicy on the graphical viewer side.
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
	 * For spanning horizontally, walk through the row starting atColumn and delete empty or filler labels so we can expand into the empty columns. If
	 * no empty cells, numColIncrement is returned so the number of columns can be incremented on the overall grid.
	 */
	private int spanHorizontalIntoEmptyColumns(GridComponent spanGC, int atRow, int atColumn, int childHeight, int numColsIncrement) {
		// Span as far as we can with empty vertical columns, move the spanGC into the new columns as we go.
		if (atColumn < glayoutTable.length && atRow < glayoutTable[0].length) {
			for (int col = atColumn; col < glayoutTable.length && numColsIncrement != 0; col++) {
				int toRow = atRow + childHeight -1;
				if (isHorizontalSpaceAvailable(col, atRow, toRow)) {
					for (int row = atRow; row <= toRow; row++) {
						GridComponent gc = glayoutTable[col][row];
						if (gc.isFillerLabel()) {
							removeGridComponent(gc);
							deleteComponent(gc);
						}
						glayoutTable[col][row] = spanGC;
					}
					spanGC.setSpanWidth(spanGC.gridDimension.width+1);
					numColsIncrement--;
				} else
					break;	// We hit our first non-empty column. Can't span any further.
			}
		}
		return numColsIncrement;
	}
	
	/*
	 * For spanning vertically, walk through the col starting atRow and delete empty or filler labels so we can expand into the empty rows. If
	 * no empty cells, numRowIncrement is returned so the number of rows can be incremented on the overall grid.
	 */
	private int spanVerticalIntoEmptyRows(GridComponent spanGC, int atRow, int atColumn, int childWidth, int numRowsIncrement) {
		// Span as far as we can with empty horizontal rows, move the spanGC into the new rows as we go.
		if (atColumn < glayoutTable.length && atRow < glayoutTable[0].length) {
			for (int row = atRow; row < glayoutTable[0].length && numRowsIncrement != 0; row++) {
				int toCol = atColumn + childWidth -1;
				if (isVerticalSpaceAvailable(row, atColumn, toCol)) {
					for (int col = atColumn; col <= toCol; col++) {
						GridComponent gc = glayoutTable[col][row];
						if (gc.isFillerLabel()) {
							removeGridComponent(gc);
							deleteComponent(gc);
						}
						glayoutTable[col][row] = spanGC;
					}
					spanGC.setSpanHeight(spanGC.gridDimension.height+1);
					numRowsIncrement--;
				} else
					break;	// We hit our first non-empty row. Can't span any further.
			}
		}
		return numRowsIncrement;
	}	

	private GridComponent findNextValidGC(int columnStart, int rowStart) {


		if (glayoutTable.length == 0 || glayoutTable[0].length == 0)
			return null;

		// Find the next occupied cell to be used as the before object.
		int col = columnStart, row = rowStart;
		for (int i = row; i < glayoutTable[0].length; i++) {
			for (int j = col; j < glayoutTable.length; j++) {
				GridComponent child = glayoutTable[j][i];
				if (child != EMPTY_GRID) {
					// If the row is going through a control that is spanning vertically more than one
					// row, skip it. This is checked by comparing this control's starting y (row) value
					// with where we are in the table lookup. If it doesn't span vertically, then its gridY will be the same as the row.
					if (child.gridDimension.y == i) {
						return child;
					}
				}
			}
			col = 0;	// Reset so that we now start from beginning of all subsequent rows.
		}
		return null;		
	
	}

	/*
	 * Return true if the cells atRow from columnStart to columnEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isVerticalSpaceAvailable(int atRow, int columnStart, int columnEnd) {
		if (glayoutTable.length == 0 || glayoutTable[0].length == 0 || columnStart >= glayoutTable.length || columnEnd >= glayoutTable.length || atRow >= glayoutTable[0].length)
			return false;
		for (int col = columnStart; col <= columnEnd; col++) {
			if (glayoutTable[col][atRow] != EMPTY_GRID && !glayoutTable[col][atRow].isFillerLabel()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Return true if the cells atCol from rowStart to rowEnd have either an EMPTY object or is a filler label.
	 */
	private boolean isHorizontalSpaceAvailable(int atCol, int rowStart, int rowEnd) {
		if (glayoutTable.length == 0 || glayoutTable[0].length == 0 || rowStart >= glayoutTable[0].length || rowEnd >= glayoutTable[0].length || atCol >= glayoutTable.length)
			return false;
		for (int row = rowStart; row <= rowEnd; row++) {
			if (glayoutTable[atCol][row] != EMPTY_GRID && !glayoutTable[atCol][row].isFillerLabel()) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Helper method to determine if there are any styles set for this label. Look for SWT.NONE as the second argument.
	 */
	private boolean isNoStyleSet(IJavaObjectInstance child) {
		if (child != null && child.isParseTreeAllocation()) {
			ParseTreeAllocation ptAlloc = (ParseTreeAllocation) child.getAllocation();
			PTExpression allocationExpression = ptAlloc.getExpression();
			if (allocationExpression instanceof PTClassInstanceCreation) {
				PTClassInstanceCreation classInstanceCreation = (PTClassInstanceCreation) allocationExpression;
				if (classInstanceCreation.getArguments().size() == 2) {
					PTExpression secondArg = (PTExpression) classInstanceCreation.getArguments().get(1);
					if (secondArg instanceof PTFieldAccess && ((PTFieldAccess) secondArg).getField().equals("NONE")) //$NON-NLS-1$
						return true;
				}
			}
		}
		return false;
	}

	private int getTargetVMSWTVersion() {

		if (targetVMVersion != -1)
			return targetVMVersion;
		// This is cache'd on the edit domain for performance
		Integer editDomainTargetVMVersion = (Integer) fEditDomain.getData(TARGET_VM_VERSION_KEY);
		if (editDomainTargetVMVersion != null) {
			targetVMVersion = editDomainTargetVMVersion.intValue();
			return targetVMVersion;
		}
		// Get the target VM version for the first time for the edit domain from the target VM itself
		ProxyFactoryRegistry proxyFactoryRegistry = getLayoutManagerBeanProxy().getProxyFactoryRegistry();
		IExpression expression = proxyFactoryRegistry.getBeanProxyFactory().createExpression();
		// Evaluate the expression "org.eclipse.swt.SWT.getVersion()";
		IProxyBeanType swtBeanTypeProxy = proxyFactoryRegistry.getBeanTypeProxyFactory().getBeanTypeProxy(expression, "org.eclipse.swt.SWT"); //$NON-NLS-1$
		IProxyMethod getVersionMethodProxy = swtBeanTypeProxy.getMethodProxy(expression, "getVersion"); //$NON-NLS-1$
		ExpressionProxy proxy = expression.createSimpleMethodInvoke(getVersionMethodProxy, swtBeanTypeProxy, null, true);
		proxy.addProxyListener(new ProxyListener() {

			public void proxyResolved(ProxyEvent event) {
				targetVMVersion = ((IIntegerBeanProxy) event.getProxy()).intValue();
				fEditDomain.setData(TARGET_VM_VERSION_KEY, new Integer(targetVMVersion));
			}

			public void proxyNotResolved(ProxyEvent event) {
			}

			public void proxyVoid(ProxyEvent event) {
			}
		});
		try {
			expression.invokeExpression();
		} catch (Exception e) {
			JavaVEPlugin.log("Unable to work out target SWT version for GridLayoutHelper", Level.WARNING); //$NON-NLS-1$	
			targetVMVersion = 3100;
		}
		if (targetVMVersion == -1) {
			JavaVEPlugin.log("Unable to work out target SWT version for GridLayoutHelper", Level.WARNING); //$NON-NLS-1$
			targetVMVersion = 3100;
		}
		return targetVMVersion;
	}

	/**
	 * This only needs to be called if there is no container policy set. The container policy will override and place its edit domain in instead.
	 * @param editDomain
	 * 
	 * @since 1.2.0
	 */
	public void setEditDomain(EditDomain editDomain) {
		fEditDomain = editDomain;
	}

	private void getCommandForAddCreateMoveChildren(Object requestType, List childrenGC, Object beforeObject, CommandBuilder cb) {
		List children = new ArrayList(childrenGC.size());
		List constraints = new ArrayList(childrenGC.size());
		for (int i = 0; i < childrenGC.size(); i++) {
			GridComponent gc = (GridComponent) childrenGC.get(i);
			children.add(gc.component);
			constraints.add(gc.useGriddata);
		}
		// of it will mess up the policy commands created..
		// Create the appropriate set of constraints to apply with.
		if (RequestConstants.REQ_CREATE.equals(requestType))
			cb.append(policy.getCreateCommand(constraints, children, beforeObject).getCommand());
		else if (RequestConstants.REQ_ADD.equals(requestType))
			cb.append(policy.getAddCommand(constraints, children, beforeObject).getCommand());
		else
			cb.append(policy.getMoveChildrenCommand(children, beforeObject));
	}	
	
	/**
	 * Replace the filler at the given cell with the new child.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param childObject
	 * @param requestType
	 * @param cell
	 * 
	 * @since 1.2.0
	 */
	public void replaceFiller(EObject child, Object childObject, Object requestType, Point cell) {
		if (!isFillerLabelAtCell(cell))
			return;	// Invalid request.
		GridComponent movedComponent = getComponentIfMove(child, requestType);
		GridComponent compAtCell = glayoutTable[cell.x][cell.y];
		deleteComponent(compAtCell);	// Delete the filler.
		if (movedComponent == null)
			compAtCell.setComponent(childObject, child, requestType);	// Just set in new stuff for same index position.
		else {
			// This is a move, so we will instead set into this filler component the moved child. This will be a copy and then
			// the old child will be removed.
			compAtCell.setComponent(childObject, child, requestType);	// First make it new move component
			compAtCell.setMovedComponent(movedComponent);	// Then copy what is needed from old.
			removeChild(movedComponent, false, true);	// Need to force removal because if it is a filler, it is now in a new place.
		}
	}
	
	/**
	 * Used by callers to delete the child from the layout. It will clean up and remove now empty columns and rows that the child used to cover.
	 * It will also call the appropriate delete from the container policy.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param delete
	 * 
	 * @since 1.2.0
	 */
	public void deleteChild(EObject child) {
		GridComponent gc = getComponent(child);
		if (gc != null)
			removeChild(gc, true, false);	// Don't force removal of fillers. That is because if it would be replaced by a filler the user would get confused and think nothing happened. Instead they will get a not sign.
		else
			addToDeleted(child);	// May be some other non-grid child. Let it get deleted.
			
	}
	
	/**
	 * Same as {@link #deleteChild(EObject)} except it does it for each child in the list.
	 * @param children
	 * 
	 * @since 1.2.0
	 */
	public void deleteChildren(List children) {
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EObject child = (EObject) iter.next();
			deleteChild(child);
		}
	}

	/**
	 * Used by callers to orphan the child from the layout. It will clean up and remove now empty columns and rows that the child used to cover.
	 * It will also call the appropriate orphan from the container policy.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param delete
	 * 
	 * @since 1.2.0
	 */
	public void orphanChild(EObject child) {
		GridComponent gc = getComponent(child);
		if (gc != null)
			removeChild(gc, false, true);	// Need to force removal because it is going away to a new parent.
		addToOrphaned(child);
	}
	
	/**
	 * Same as {@link #orphanChild(EObject)} except it does it for each child in the list.
	 * @param children
	 * 
	 * @since 1.2.0
	 */
	public void orphanChildren(List children) {
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EObject child = (EObject) iter.next();
			orphanChild(child);
		}
	}

	/**
	 * Remove the gridcomponent from the table and clean up rows/cols if needed. Also set it as a child to delete from the container if the delete
	 * flag is true.
	 * @param oldChild
	 * @param delete
	 * @param forceRemove force the remove. If the oldChild was a filler and this is <code>false</code> it doesn't actually remove it. That is because
	 * if it was removed, it would just put a filler back in its place, and then see if row/col should be deleted. If using <code>true</code>
	 * then it will force a removal of the filler. Even though a filler will go back in its place, this may still be necessary because
	 * the actual eobject has trully been orphaned or moved. And in that case we need to remove it.
	 * @since 1.2.0
	 */
	protected void removeChild(GridComponent oldChild, boolean delete, boolean forceRemove) {
		// First replace the child squares with Empties, then fill in with fillers.
		// Remove the child from the linked list and delete it if requested
		// And finally clear out now exposed empty rows/cols.
		
		int toCol = oldChild.gridDimension.x + oldChild.gridDimension.width - 1;
		int toRow = oldChild.gridDimension.y + oldChild.gridDimension.height - 1;
		if (!oldChild.isFillerLabel() || forceRemove) {
			removeGridComponent(oldChild);
			if (delete)
				deleteComponent(oldChild);

			for (int col = oldChild.gridDimension.x; col <= toCol; col++) {
				for (int row = oldChild.gridDimension.y; row <= toRow; row++) {
					glayoutTable[col][row] = EMPTY_GRID;
				}
			}
			for (int col = oldChild.gridDimension.x; col <= toCol; col++) {
				for (int row = oldChild.gridDimension.y; row <= toRow; row++) {
					replaceEmptyCell(createFillerComponent(), col, row);
				}
			}
		}
		
		// Now remove empty rows/cols. 
		for(int remRow=toRow; remRow >= oldChild.gridDimension.y; remRow--)
			removeRowIfEmpty(remRow);
		for(int remCol=toCol; remCol >= oldChild.gridDimension.x; remCol--)
			removeColIfEmpty(remCol);		
	}
	
	/**
	 * Change the number of columns to the new size if possible.
	 * If it is larger than the current it will add as many empty columns
	 * on the right as needed. If it less than the current it remove
	 * as many empty columns from the right as possible. But it cannot
	 * remove a column that has something other than fillers in it,
	 * so it won't get any further than that.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 *  
	 * @param newNumCols
	 * @return the actual new number of columns. It may be a different number than sent in if it could not remove a col.
	 * 
	 * @since 1.2.0
	 */
	public int changeNumberOfColumns(int newNumCols) {
		int delta = newNumCols - numColumns;
		if (delta > 0) {
			// Add columns.
			while(--delta>=0) {
				createNewCol(numColumns);
			}
		} else if (delta < 0) {
			// Remove columns;
			while(++delta<=0) {
				if (!removeColIfEmpty(numColumns-1))
					break;	// Couldn't go any further.
			}
		}
		
		return numColumns;
	}
	
	/**
	 * Remove this row if contains nothing but empties, or fillers.
	 * @param row
	 * @return <code>true</code> if the row was removed.
	 * @since 1.2.0
	 */
	protected boolean removeRowIfEmpty(int row) {
		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else if (gc.isFillerLabel())
				continue;
			else 
				return false;	// We have one that starts on this row
		}
		
		// We have an empty row. Now go through and remove all of the fillers, decrease by one any vertical spans, and then just
		// move up the entire layout one row.
		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else {
				// It must be a filler. already verified that above.
				removeGridComponent(gc);
				deleteComponent(gc);
			}
		}
		
		for (int col = 0; col < glayoutTable.length; col++) {
			GridComponent[] oldCol = glayoutTable[col];
			GridComponent[] newCol = glayoutTable[col] = new GridComponent[oldCol.length-1];
			System.arraycopy(oldCol, 0, newCol, 0, row);
			System.arraycopy(oldCol, row+1, newCol, row, newCol.length-row);
			// And finally! update the grid dimensions of all of the moved controls.
			for (int rrow = row; rrow < newCol.length; rrow++) {
				GridComponent gc = newCol[rrow];
				if (gc != EMPTY_GRID) {
					if (gc.gridDimension.x == col && gc.gridDimension.y == rrow+1) {
						gc.gridDimension.y = rrow;
					}
					// Skip over control to get to next filled row.
					rrow = (gc.gridDimension.y+gc.gridDimension.height-1);
				}
			}
		}
		
		
		return true;
	}

	/**
	 * Remove this col if contains nothing but empties, ir fillers.
	 * @param col
	 * @return <code>true</code> if the col was removed.
	 * @since 1.2.0
	 */
	protected boolean removeColIfEmpty(int col) {
		for (int row = 0; row < glayoutTable[col].length; row++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else if (gc.isFillerLabel())
				continue;
			else
				return false;	// We have one that starts on this col
		}
		
		// We have an empty col. Now go through and remove all of the fillers, decrease by one any horizontal spans, and then just
		// move up the entire layout one col.
		for (int row = 0; row < glayoutTable[col].length; row++) {
			GridComponent gc = glayoutTable[col][row];
			if (gc == EMPTY_GRID)
				continue;
			else {
				// Must be a filler. Already verified that above.
				removeGridComponent(gc);
				deleteComponent(gc);
			}
		}
		
		GridComponent[][] oldLayout = glayoutTable;
		glayoutTable = new GridComponent[glayoutTable.length-1][];
		System.arraycopy(oldLayout, 0, glayoutTable, 0, col);
		System.arraycopy(oldLayout, col+1, glayoutTable, col, glayoutTable.length-col);
		numColumns = glayoutTable.length;
		// And finally! update the grid dimensions of all of the moved controls.
		for (int rcol = col; rcol < glayoutTable.length; rcol++) {
			// Need to rcol in the next for loop because we modify rcol within the for loop.
			for (int rrow = 0; rcol < glayoutTable.length && rrow < glayoutTable[rcol].length; rrow++) {
				GridComponent gc = glayoutTable[rcol][rrow];
				if (gc != EMPTY_GRID) {
					if (gc.gridDimension.x == rcol+1 && gc.gridDimension.y == rrow) {
						gc.gridDimension.x = rcol;
					}
					// Skip over control to get to next filled col.
					rcol = (gc.gridDimension.x+gc.gridDimension.width-1);
				}
			}
		}

		return true;
	}

	private GridComponent getComponentIfMove(EObject childEObject, Object requestType) {
		if (RequestConstants.REQ_CREATE.equals(requestType) || RequestConstants.REQ_ADD.equals(requestType))
			return null;
		return getComponent(childEObject);
	}

	/**
	 * Get the component for the given child.
	 * @param childEObject
	 * @return the component or <code>null</code> if not in the layout.
	 * 
	 * @since 1.2.0
	 */
	protected GridComponent getComponent(EObject childEObject) {
		if (childEObject == null)
			return null;
		GridComponent gc = first;
		while (gc != null) {
			if (gc.componentEObject == childEObject)
				return gc;
			gc = gc.next;
		}
		return null;
	}
	
	
	/**
	 * Replace the filler or empty at the given cell with the new child.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param childObject
	 * @param requestType
	 * @param cell
	 * 
	 * @since 1.2.0
	 */
	public void replaceFillerOrEmpty(EObject child, Object childObject, Object requestType, Point cell) {
		if (isFillerLabelAtCell(cell))
			replaceFiller(child, childObject, requestType, cell);
		else if (isEmptyAtCell(cell))
			replaceEmptyCell(child, childObject, requestType, cell);		
	}
	
	/**
	 * Insert a col within just one row. This will not do anything if there are
	 * any vertically spanned children in that row. 
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * @param cell
	 * @return <code>true</code> if the insert could be done, <code>false</code> if the insert failed. (e.g. a vertical span encountered).
	 * @since 1.2.0
	 */
	public boolean insertColWithinRow(Point cell) {
		if (cell.x < 0 || cell.x >= glayoutTable.length || cell.y < 0 || cell.y >= glayoutTable[0].length)
			return false;
		
		// Create a column at the end if this row doesn't end with a filler or empty. None of the children of any row other than the one with the column being
		// inserted will enter this column. Then the row starting with the insert col will be shifted left one column.
		// If any one spans vertically then we will not allow the move. This is a restriction for now because
		// it becomes very difficult to figure out what needs to move because the vertical span would push
		// other components that would not normally be involved to also be shifted. It could cause shifting
		// both above and below the new row.
		
		// First test to see if a vertical span is involved. Don't do anything if it is.
		for (int col = cell.x; col < glayoutTable.length; col++) {
			GridComponent gc = glayoutTable[col][cell.y];
			if (gc != EMPTY_GRID && gc.gridDimension.height > 1)
				return false;	// We have a vertical span, can't handle these at this time.
		}
	
		GridComponent lastCol = glayoutTable[numColumns-1][cell.y];
		if (lastCol != EMPTY_GRID && !last.isFillerLabel())
			createNewCol(numColumns);
		
		// Now start the needed shifting. Remove the new filler component just added in the new column and move over by one each until
		// we reach the insert column. 
		GridComponent newFiller = glayoutTable[numColumns-1][cell.y];
		if (newFiller != EMPTY_GRID) {
			removeGridComponent(newFiller);
			deleteComponent(newFiller);
		}
		for (int col = glayoutTable.length-2, toCol = col+1; col >= cell.x; col--, toCol--) {
			glayoutTable[toCol][cell.y] = glayoutTable[col][cell.y];
		}
		// Now put an empty in the col spot and replace with the new child.
		glayoutTable[cell.x][cell.y] = EMPTY_GRID;
		replaceEmptyCell(createFillerComponent(), cell.x, cell.y);
		return true;
	}
	
	/**
	 * Create a new row. Controls can then be placed in afterwards.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param newRow insert new row at this point. This must be >= 0 and <= number of rows in current layout. If equal to number of rows, then
	 * this means add one row to the bottom.
	 * 
	 * @since 1.2.0
	 */
	public void createNewRow(int newRow) {
		if (newRow < 0 || (glayoutTable.length == 0 && newRow > 0) || newRow > glayoutTable[0].length)
			return;
		
		if (glayoutTable.length == 0) {
			glayoutTable = new GridComponent[numColumns = 1][1];
			glayoutTable[0][0] = EMPTY_GRID;
			replaceEmptyCell(createFillerComponent(), 0, 0);
			return;
		}

		GridComponent[][] newLayoutTable = new GridComponent[glayoutTable.length][glayoutTable[0].length+1];
		if (newRow > 0) {
			// We need to create the new table. First for each column, copy up to but not including the new row as is since those won't change.
			for (int col = 0; col < glayoutTable.length; col++) {
				System.arraycopy(glayoutTable[col], 0, newLayoutTable[col], 0, newRow);
			}
		}

		// First fill new row in with Empty so we don't get errors later accessing null slots.
		for (int col = 0; col < glayoutTable.length; col++) {
			newLayoutTable[col][newRow] = EMPTY_GRID;
		}
		
		GridComponent[][] oldLayoutTable = glayoutTable;
		glayoutTable = newLayoutTable;	// Now have a valid table up to the prev row. We make it the table so that findNext, etc. will work.
		if (newRow == oldLayoutTable[0].length) {
			// It is the last row, use replace empty on first col to fill it correctly.
			replaceEmptyCell(createFillerComponent(), 0, newRow);
		} else {
			// Now comes the hard part, we need to fill in the new row, but we have to be careful because of vertical spans.
			// First move all from old row to the last row down one row and updating their dimensions to their new row.
			// This is needed so that when we create the new row after this we have valid "next valid objects".
			// Note: The reason we are coming from the bottom up is because that way we can tell when we hit the
			// top-left of a a component. If we went from top down we would keep incrementing the y coor (because
			// we hit it, we increment it, and then on the next row we hit it again and would increment it again).
			for (int row = oldLayoutTable[0].length-1, toRow = row+1; row >= newRow; row--, toRow--) {
				for (int col = 0; col < oldLayoutTable.length; col++) {				
					GridComponent cellEntry = oldLayoutTable[col][row];
					if (cellEntry == EMPTY_GRID)
						glayoutTable[col][toRow] = EMPTY_GRID;
					else {
						if (cellEntry.gridDimension.y == row && cellEntry.gridDimension.x == col) {
							cellEntry.gridDimension.y = toRow;	// It is being moved down one.
						}
						int spanx = cellEntry.gridDimension.width;
						// This will increment col for us to so we pick in the for loop with the next col after the span.
						col--;
						while(spanx-- > 0) {
							glayoutTable[++col][toRow] = cellEntry;
						}
					}
				}
			}
			// Now go through old row, and find any that are vertical spanned but start before that row, increase their's by one because they now span new row.
			// For those that don't, put in a filler instead. (Note: we could try to get smart and figure out if they should
			// be empties or not, but that gets real complicated. Leave for a later exercise. :-) )
			for (int col = 0; col < oldLayoutTable.length; col++) {
				GridComponent gc = oldLayoutTable[col][newRow];
				if (gc != EMPTY_GRID && gc.gridDimension.y < newRow) {
					gc.setSpanHeight(gc.gridDimension.height+1);
					int spanWidth = gc.gridDimension.width;
					// This will increment col for us to so we pick in the for loop with the next col after the span.
					col--;
					while(spanWidth-- > 0) {
						glayoutTable[++col][newRow] = gc;
					}
				} else
					replaceEmptyCell(createFillerComponent(), col, newRow);
			}
		}
	}

	/**
	 * Create a new col. Controls can then be placed in afterwards.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * @param newCol insert new column at this point. This must be >= 0 and <= number of columns in current layout. If equal to number of columns, then
	 * this means add one column to the end.
	 * 
	 * @since 1.2.0
	 */
	public void createNewCol(int newCol) {
		if (newCol < 0 || newCol > glayoutTable.length)
			return;
		if (glayoutTable.length == 0) {
			glayoutTable = new GridComponent[numColumns = 1][1];
			glayoutTable[0][0] = EMPTY_GRID;
			replaceEmptyCell(createFillerComponent(), 0, 0);
			return;
		}
		
		GridComponent[][] newLayoutTable = new GridComponent[numColumns = glayoutTable.length+1][glayoutTable[0].length];
				
		// We need to create the new table. First copy each column up to but including new col as is since those won't change.
		for (int col = 0; col < newCol; col++) {
			System.arraycopy(glayoutTable[col], 0, newLayoutTable[col], 0, glayoutTable[col].length);
		}

		// First fill new col in with Empty so we don't get errors later accessing null slots.
		Arrays.fill(newLayoutTable[newCol], EMPTY_GRID);
		
		GridComponent[][] oldLayoutTable = glayoutTable;
		glayoutTable = newLayoutTable;	// Now have a valid table up to the prev col. We make it the table so that findNext, etc. will work.
		// Now comes the hard part, we need to fill in the new col, but we have to be careful because of horizontal spans.
		// First move all from old col to the last col over one col and updating their dimensions to their new col.
		// This is needed so that when we create the new row after this we have valid "next valid objects".
		// Note: The reason we are coming from the right is because that way we can tell when we hit the
		// top-left of a a component. If we went from left to right we would keep incrementing the x coor (because
		// we hit it, we increment it, and then on the next col we hit it again and would increment it again).
		for (int col = oldLayoutTable.length-1, toCol = col+1; col >= newCol; col--, toCol--) {
			for (int row = 0; row < oldLayoutTable[0].length; row++) {				
				GridComponent cellEntry = oldLayoutTable[col][row];
				if (cellEntry == EMPTY_GRID)
					glayoutTable[toCol][row] = EMPTY_GRID;
				else {
					if (cellEntry.gridDimension.x == col && cellEntry.gridDimension.y == row) {
						cellEntry.gridDimension.x = toCol;	// It is being moved over one.
					}
					int spanHeight = cellEntry.gridDimension.height;
					// This will increment row for us to so we pick in the for loop with the next row after the span.
					row--;
					while(spanHeight-- > 0) {
						glayoutTable[toCol][++row] = cellEntry;
					}
				}
			}
		}
		int lastRow = oldLayoutTable[0].length - 1;
		if (newCol != oldLayoutTable.length) {
			// Now go through old col, and find any that are horizontal spanned but start before that col, increase their's by one because they now span new col.
			// For those that don't, put in a filler instead. (Note: we could try to get smart and figure out if they should
			// be empties or not, but that gets real complicated. Leave for a later exercise. :-) )
			for (int row = 0; row <= lastRow; row++) {
				GridComponent gc = oldLayoutTable[newCol][row];
				if (gc != EMPTY_GRID && gc.gridDimension.x < newCol) {
					gc.setSpanWidth(gc.gridDimension.width + 1);
					int spanHeight = gc.gridDimension.height;
					// This will increment col for us to so we pick in the for loop with the next col after the span.
					row--;
					while (spanHeight-- > 0) {
						glayoutTable[newCol][++row] = gc;
					}
				} else
					replaceEmptyCell(createFillerComponent(), newCol, row);
			}
		} else {
			// Last col. Just replace with fillers
			for (int row = 0; row < glayoutTable[0].length; row++) {
				replaceEmptyCell(createFillerComponent(), newCol, row);
			}
		}
	}

	/**
	 * Insert the component in the ordered list before the given component, and fill in the layout table with the new component. It is assumed
	 * that the slots taken by the new component are empty (or the current contents have been handled and can be replaced by this new component).
	 * 
	 * @param gc
	 * @param beforeComponent component to be ordered before, <code>null</code> for add to end.
	 * @param x
	 * @param y
	 * @param spanX
	 * @param spanY
	 * 
	 * @since 1.2.0
	 */
	protected void insertComponent(GridComponent gc, GridComponent beforeComponent, int x, int y, int spanX, int spanY) {
		gc.gridDimension = new Rectangle(x, y, spanX, spanY);
		insertGridComponentBefore(gc, beforeComponent);
		// Now fill in the slots
		int stopCol = x+spanX;
		int stopRow = y+spanY;
		for (int col = x; col < stopCol; col++) {
			for (int row = y; row < stopRow; row++)
				glayoutTable[col][row] = gc;
		}
	}
	
	private GridComponent createFillerComponent() {
		return new GridComponent(createFillerLabelObject(), true);
	}

	/**
	 * Replace empty cell with the given child.
	 * <p>
	 * Must call {@link #startRequest()} before this method can be called.
	 * 
	 * @param child
	 * @param childObject
	 * @param requestType
	 * @param cell
	 * 
	 * @since 1.2.0
	 */
	public void replaceEmptyCell(EObject child, Object childObject, Object requestType, Point cell) {
		if (glayoutTable[cell.x][cell.y] != EMPTY_GRID)
			return;	// Invalid request.
		replaceEmptyCell(new GridComponent(childObject, child, requestType), cell.x, cell.y);
	}
	
	/**
	 * Replace the empty cell. The child must be a new grid component. It cannot be one that already exists.
	 * It could be modified during the execution.
	 * @param child
	 * @param cellCol
	 * @param cellRow
	 * 
	 * @since 1.2.0
	 */
	protected void replaceEmptyCell(GridComponent child, int cellCol, int cellRow) {
		if (glayoutTable[cellCol][cellRow] != EMPTY_GRID)
			return;	// Invalid request.
		
		GridComponent movedComponent = getComponentIfMove(child.componentEObject, child.requestType);
		
		// Find the next occupied cell to be used as the before object.
		GridComponent before = findNextValidGC(cellCol, cellRow);

		// Need to find all empties between the object BEFORE the beforeObject (i.e. the object we are going AFTER)
		// and our position. They need to be replaced with filler. Otherwise ours will not be placed correctly.
		GridComponent componentBeforeUs = before != null ? before.prev : last;
		Rectangle beforeUsDim = componentBeforeUs != null ? componentBeforeUs.gridDimension : new Rectangle();
		int startCol = beforeUsDim.x;
		for (int row = beforeUsDim.y; row <= cellRow; row++) {
			int endCol = row != cellRow ? glayoutTable.length : cellCol;	// Stop at our cell only on our row, else do entire row.
			for (int col = startCol; col < endCol; col++) {
				if (glayoutTable[col][row] == EMPTY_GRID) {
					insertComponent(createFillerComponent(), before, col, row, 1, 1);
				}
			}
			startCol = 0;	// After the first row we want to start in first col.
		}
		
		if (movedComponent == null)
			insertComponent(child, before, cellCol, cellRow, 1, 1);
		else {
			child.setMovedComponent(movedComponent);
			insertComponent(child, before, cellCol, cellRow, 1, 1);
			removeChild(movedComponent, false, true);	// Need to force removal because if it was a filler it is now in a new place.
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getAddChildrenCommand(java.util.List, java.util.List, java.lang.Object)
	 */
	public CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		return processAddMoveCreate(childrenComponents, constraints, position, RequestConstants.REQ_ADD);
	}

	/*
	 * @param childrenComponents
	 * @param constraints
	 * @param position
	 * @return
	 * 
	 * @since 1.2.0
	 */
	private CorelatedResult processAddMoveCreate(List childrenComponents, List constraints, Object position, Object requestType) {
		CorelatedResult result = new CorelatedResult(childrenComponents, constraints);
		// Currently, because it is too hard to figure out the correct corelated result, we will only allow add of one child.
		// This is what happens graphically too anyway.
		if (childrenComponents.size()>1 || childrenComponents.isEmpty()) {
			result.setCommand(UnexecutableCommand.INSTANCE);
			return result;
		} else {
			EObject trueEObject;
			Object child;
			// Need to figure out the true child so that we can put it in our layout correctly.
			try {
				// First see if the true child is a control. If not, then let normal create processing handle it.
				child = childrenComponents.get(0);
				Object trueChild = policy.getTrueChild(child, policy.getPolicyRequestType(requestType), new CommandBuilder(), new CommandBuilder());
				if (trueChild == null || !classControl.isInstance(trueChild))
					return policy.getAddCommand(constraints, childrenComponents, position);	// Do normal, the container policy will handle it.
				trueEObject = (EObject) trueChild;
			} catch (StopRequestException e) {
				result.setCommand(UnexecutableCommand.INSTANCE);
				return result;
			}
			
			startRequest();
			GridComponent before = getComponent((EObject) position);
			if (before == null) {
				// Get last guy, and just put in next available cell. That includes fillers.
				if (last == null) {
					// The table is empty.
					createNewCol(0);
					replaceFiller(trueEObject, child, requestType, new Point(0,0));
				} else {
					// See if there is a col after this last guy, and it is an empty. If so, just replace it.
					// If there is no col, then create one, and put the last guy there. There can't be any
					// non-empty after it. If there was then it wouldn't be last guy.
					int nextCol = last.gridDimension.x+last.gridDimension.width;
					if (nextCol >= glayoutTable.length) {
						// Need a new col.
						nextCol = glayoutTable.length;	// To be safe. This shouldn't happen that next col is greater than length.
						createNewCol(glayoutTable.length);
						replaceFillerOrEmpty(trueEObject, child, requestType, new Point(nextCol, last.gridDimension.y));
					} else if (glayoutTable[nextCol][last.gridDimension.y] == EMPTY_GRID) {
						replaceEmptyCell(trueEObject, child, requestType, new Point(nextCol, last.gridDimension.y));
					} else {
						// This shouldn't happen. There should be an empty here. If not then there is something wrong.
						result.setCommand(UnexecutableCommand.INSTANCE);
						return result;
					}
				}
			} else {
				// First try create column within row. If that doesn't work then we want to create a column before this guy and just put the new guy there.
				Point cell = before.gridDimension.getLocation();
				if (!insertColWithinRow(cell)) {
					createNewCol(cell.x);
				}
				replaceFillerOrEmpty(trueEObject, child, requestType, cell);
			}
			
			// If the constraint is set, then in case the constraint needs to be changed by the stop request, we will replace the constraint
			// in the grid component with the constraint set in. That way they work on the same griddata.
			if (constraints.get(0) != null) {
				GridComponent newGuy = getComponent(trueEObject);
				newGuy.useGriddata = (IJavaObjectInstance) constraints.get(0);
			}
			
			result.setCommand(stopRequest());
			return result;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getCreateChildCommand(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
		return processAddMoveCreate(Collections.singletonList(childComponent), Collections.singletonList(constraint), position, RequestConstants.REQ_CREATE);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getOrphanChildrenCommand(java.util.List)
	 */
	public Result getOrphanChildrenCommand(List children) {
		CorelatedResult result = new CorelatedResult(children);
		startRequest();
		orphanChildren(children);
		result.setCommand(stopRequest());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getDeleteDependentCommand(java.util.List)
	 */
	public Command getDeleteDependentCommand(List children) {
		startRequest();
		deleteChildren(children);
		return stopRequest();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getDeleteDependentCommand(java.lang.Object)
	 */
	public Command getDeleteDependentCommand(Object child) {
		startRequest();
		deleteChild((EObject) child);
		return stopRequest();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.LayoutPolicyHelper#getMoveChildrenCommand(java.util.List, java.lang.Object)
	 */
	public Command getMoveChildrenCommand(List children, Object beforeChild) {
		return processAddMoveCreate(children, Collections.singletonList(null), beforeChild, RequestConstants.REQ_MOVE_CHILDREN).getCommand();
	}
	
}
