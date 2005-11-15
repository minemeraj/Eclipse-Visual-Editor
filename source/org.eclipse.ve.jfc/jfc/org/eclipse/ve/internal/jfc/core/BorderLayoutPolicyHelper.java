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
 *  $RCSfile: BorderLayoutPolicyHelper.java,v $
 *  $Revision: 1.12 $  $Date: 2005-11-15 18:53:31 $ 
 */

import java.util.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Border Layout Policy Helper.
 *
 * Note: constraints are Strings. Though they should only
 * be of the values listed here or null.
 */
public class BorderLayoutPolicyHelper extends LayoutPolicyHelper {
	protected VisualContainerPolicy policy;
	public static final int 
		LEFT_TO_RIGHT = 0,
		RIGHT_TO_LEFT = 1;
	public static final List REAL_INTERNAL_TAGS;
	public static final List DISPLAY_TAGS;
	
	private final static ParseTreeAllocation[] CODEGEN_ALLOCATIONS;
	
	private static ParseTreeAllocation createBorderAllocation(String classname, String fieldName) {
		PTExpression fieldAccess = InstantiationFactory.eINSTANCE.createPTFieldAccess(InstantiationFactory.eINSTANCE.createPTName(classname), fieldName);
		return InstantiationFactory.eINSTANCE.createParseTreeAllocation(fieldAccess);
	}
	
		
	public static final int
		NORTH_INDEX = 0,
		EAST_INDEX = 1,
		WEST_INDEX = 2,
		CENTER_INDEX = 3,
		SOUTH_INDEX = 4,
		BEFORE_INDEX = 5,
		AFTER_INDEX = 6;

	static {
		REAL_INTERNAL_TAGS = new ArrayList(7);
		REAL_INTERNAL_TAGS.add("North"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("East"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("West"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Center"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("South"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Before"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("After"); //$NON-NLS-1$

		DISPLAY_TAGS= new ArrayList(7);
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_North); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_East); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_West); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_Center); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_South); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_BEFORE_LINE_BEGINS); 
		DISPLAY_TAGS.add(JFCMessages.BorderLayout_AFTER_LINE_ENDS); 
		
		CODEGEN_ALLOCATIONS = new ParseTreeAllocation[7];
		CODEGEN_ALLOCATIONS[0] = createBorderAllocation("java.awt.BorderLayout", "NORTH"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[1] = createBorderAllocation("java.awt.BorderLayout", "EAST"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[2] = createBorderAllocation("java.awt.BorderLayout", "WEST"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[3] = createBorderAllocation("java.awt.BorderLayout", "CENTER"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[4] = createBorderAllocation("java.awt.BorderLayout", "SOUTH"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[5] = createBorderAllocation("java.awt.BorderLayout", "BEFORE_LINE_BEGINS"); //$NON-NLS-1$
		CODEGEN_ALLOCATIONS[6] = createBorderAllocation("java.awt.BorderLayout", "AFTER_LINE_ENDS"); //$NON-NLS-1$ 
	}
	
/**
 * Return a new allocation for the given constraint index.
 * @param index
 * @return
 * 
 * @since 1.2.0
 */
public static JavaAllocation createBorderAllocation(int index) {
	return (JavaAllocation) EcoreUtil.copy(CODEGEN_ALLOCATIONS[index]);
}

/**
 * Helper method to return a table that maps BorderLayout constraint display names with their
 * corresponding constraints... based on the componentOrientation.
 * (e.g. If orientation is left to right, "West" and "BEFORE_LINE_BEGINS" are equivalent
 */
protected static HashMap getDisplayTagMap(int componentOrientation) {
		HashMap displayTagMap = new HashMap(BorderLayoutPolicyHelper.DISPLAY_TAGS.size());
		List displayTags = BorderLayoutPolicyHelper.DISPLAY_TAGS;
		displayTagMap.put(displayTags.get(CENTER_INDEX),displayTags.get(CENTER_INDEX));
		displayTagMap.put(displayTags.get(NORTH_INDEX),displayTags.get(NORTH_INDEX));
		displayTagMap.put(displayTags.get(SOUTH_INDEX),displayTags.get(SOUTH_INDEX));
		if (componentOrientation == LEFT_TO_RIGHT) {
			displayTagMap.put(displayTags.get(EAST_INDEX),displayTags.get(AFTER_INDEX));
			displayTagMap.put(displayTags.get(WEST_INDEX),displayTags.get(BEFORE_INDEX));
			displayTagMap.put(displayTags.get(BEFORE_INDEX),displayTags.get(WEST_INDEX));
			displayTagMap.put(displayTags.get(AFTER_INDEX),displayTags.get(EAST_INDEX));
		} else if (componentOrientation == RIGHT_TO_LEFT) {
			displayTagMap.put(displayTags.get(EAST_INDEX),displayTags.get(BEFORE_INDEX));
			displayTagMap.put(displayTags.get(WEST_INDEX),displayTags.get(AFTER_INDEX));
			displayTagMap.put(displayTags.get(BEFORE_INDEX),displayTags.get(EAST_INDEX));
			displayTagMap.put(displayTags.get(AFTER_INDEX),displayTags.get(WEST_INDEX));
		}
	return displayTagMap;
}
/**
 * Helper method to returns a table that maps BorderLayout constraint internal names with their
 * corresponding constraints... based on the componentOrientation.
 * (e.g. If orientation is left to right, "West" and "Before" are equivalent
 */
public static HashMap getInternalTagMap(int componentOrientation) {
	HashMap internalTagMap = new HashMap(REAL_INTERNAL_TAGS.size());
	List realInternalTags = BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS;
	internalTagMap.put(realInternalTags.get(CENTER_INDEX), realInternalTags.get(CENTER_INDEX));
	internalTagMap.put(realInternalTags.get(NORTH_INDEX), realInternalTags.get(NORTH_INDEX));
	internalTagMap.put(realInternalTags.get(SOUTH_INDEX), realInternalTags.get(SOUTH_INDEX));
	if (componentOrientation == LEFT_TO_RIGHT) {
		internalTagMap.put(realInternalTags.get(EAST_INDEX), realInternalTags.get(AFTER_INDEX));
		internalTagMap.put(realInternalTags.get(WEST_INDEX), realInternalTags.get(BEFORE_INDEX));
		internalTagMap.put(realInternalTags.get(BEFORE_INDEX), realInternalTags.get(WEST_INDEX));
		internalTagMap.put(realInternalTags.get(AFTER_INDEX), realInternalTags.get(EAST_INDEX));
	} else if (componentOrientation == RIGHT_TO_LEFT) {
		internalTagMap.put(realInternalTags.get(EAST_INDEX), realInternalTags.get(BEFORE_INDEX));
		internalTagMap.put(realInternalTags.get(WEST_INDEX), realInternalTags.get(AFTER_INDEX));
		internalTagMap.put(realInternalTags.get(BEFORE_INDEX), realInternalTags.get(EAST_INDEX));
		internalTagMap.put(realInternalTags.get(AFTER_INDEX), realInternalTags.get(WEST_INDEX));
	}
	return internalTagMap;
}
/**
 * Helper method to return the componentOrientation of a component. 
 * Note: Although the actual method getComponentOrientation() returns a ComponentOrientation object,
 * 		 this method gets that and determines if it is left to right or right to left and returns
 * 		 a constant instead... LEFT_TO_RIGHT or RIGHT_TO_LEFT.
 */
protected static int getComponentOrientation(IJavaObjectInstance aComponent) {
	int result = LEFT_TO_RIGHT;
	IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost(aComponent);
	IBeanProxy aBeanProxy = componentProxyHost.getBeanProxy();
	IBooleanBeanProxy booleanProxy = BeanAwtUtilities.invoke_getComponentOrientation_isLeftToRight(aBeanProxy);
	if (booleanProxy != null)
		if (booleanProxy.booleanValue())
			result = LEFT_TO_RIGHT;
		else
			result = RIGHT_TO_LEFT;
	return result;
}
	
public BorderLayoutPolicyHelper(VisualContainerPolicy ep) {
	super(ep);
}

public BorderLayoutPolicyHelper() {
}	

protected IJavaObjectInstance convertConstraint(Object constraint) {
	if (constraint instanceof String) {
		int ndx = REAL_INTERNAL_TAGS.indexOf(constraint);
		return (IJavaObjectInstance) (ndx > -1 ?
			BeanUtilities.createJavaObject("java.lang.String", getContainer().eResource().getResourceSet(), createBorderAllocation(ndx)) :	// $NONS-NLS-1$ //$NON-NLS-1$
			BeanUtilities.createString(getContainer().eResource().getResourceSet(), (String) constraint));
	} else
		return null;
}


private Rectangle boundingRectangle() {
	if (getContainer() != null){
		IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(getContainer());
		IRectangleBeanProxy rectangleProxy = BeanAwtUtilities.invoke_getBounds(containerProxy);
		Rectangle r = new Rectangle(rectangleProxy.getX(),rectangleProxy.getY(),rectangleProxy.getWidth(),rectangleProxy.getHeight());
		return r;
	}
	return null;	
}


// return a point in the container's parent coordinates
public Point convertLocation(Point p) {
	if (p == null)
		return null;
	Rectangle r = boundingRectangle();
	return new Point(p.x + r.x, p.y + r.y);
}
/**
 * Return a vector of all regions
 */
public List getAllRegions() {
	return REAL_INTERNAL_TAGS;
}
/**
 * Return a list of occupied regions
 * Collect a list of all of the components of the container.
 * Then iterate over these and collect all of their constraints
 * and remove from available ones
 */
public String[] getAvailableRegions() {
	// Get a COPY of all the regions, else result.remove() will
	// remove from the original AllRegions()
	EStructuralFeature sfConstraintConstraint = JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT);
	EStructuralFeature sfComponents = JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONTAINER_COMPONENTS);
	List result = new ArrayList(getAllRegions().size());
	result.addAll(getAllRegions());
	Iterator containerComponents =  ((List)getContainer().eGet(sfComponents)).iterator();
	HashMap tagMap = getInternalTagMap(getComponentOrientation(getContainer()));
	while (containerComponents.hasNext()) {
		EObject constraintsValue = (EObject) containerComponents.next();
		// The constraintsValue is the BorderComponent.  This holds the component and the constraints
		IJavaObjectInstance constraintString = (IJavaObjectInstance)constraintsValue.eGet(sfConstraintConstraint);
		// We know the constraints value should be a bean so we can use its toString to get the string value
		IBeanProxy cp = BeanProxyUtilities.getBeanProxy(constraintString, true);
		String constraint = cp != null ? cp.toBeanString() : ""; //$NON-NLS-1$
		result.remove(constraint);
		// Also need to remove the constraint's equivalent constraint if it's there
		//  (i.e. If orientation is left to right, "West" and "BEFORE_LINE_BEGINS" are the same and needs to be removed)
		if (result.contains(tagMap.get(constraint)))
			result.remove(tagMap.get(constraint));
	}
	if (result.size() <= 0)
		return null;
	// Convert the vector to an array for the result
	String[] tags = new String[result.size()];
	for (int i = 0; i < result.size(); i++) {
		tags[i] = (String) result.get(i);
	}
	return tags;
}
	public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List childrenComponents, List constraints, Object position) {
		/**
		 * Defect 203278/199705 - if any of the constraints are null such as when the regions are all
		 * occupied, return the UnexecutableCommand.
		 */
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			if (itr.next() == null)
				return VisualContainerPolicy.createUnexecutableResult(childrenComponents, constraints);
		}
	return super.getAddChildrenCommand(childrenComponents, constraints, position);
}
public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object childComponent, Object constraint, Object position) {
	/**
	 * Defect 199705 - if the constraint is null such as when the regions are all
	 * occupied, return the UnexecutableCommand.
	 */
	if (constraint == null)
		return VisualContainerPolicy.createUnexecutableResult(childComponent, constraint);
	return super.getCreateChildCommand(childComponent, constraint, position);
}
public String getCurrentConstraint(Point p) {
	if (p == null)
		return null;
	Rectangle r = boundingRectangle();
	Rectangle nRect = new Rectangle(r.x, r.y, r.width, r.height / 3);
	Rectangle wRect = new Rectangle(r.x, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle cRect = new Rectangle(r.x + r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle sRect = new Rectangle(r.x, r.y + 2 * r.height / 3, r.width, r.height / 3);
	Rectangle eRect = new Rectangle(r.x + 2 * r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	if (nRect.contains(p))
		return (String) REAL_INTERNAL_TAGS.get(NORTH_INDEX);
	if (wRect.contains(p))
		return (String) REAL_INTERNAL_TAGS.get(WEST_INDEX);
	if (cRect.contains(p))
		return (String) REAL_INTERNAL_TAGS.get(CENTER_INDEX);
	if (eRect.contains(p))
		return (String) REAL_INTERNAL_TAGS.get(EAST_INDEX);
	if (sRect.contains(p))
		return (String) REAL_INTERNAL_TAGS.get(SOUTH_INDEX);
	return null;
}
public Rectangle getCurrentRectangle(Point p) {
	if (p == null)
		return null;
	Rectangle r = boundingRectangle();
	Rectangle nRect = new Rectangle(r.x, r.y, r.width, r.height / 3);
	Rectangle wRect = new Rectangle(r.x, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle cRect = new Rectangle(r.x + r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	Rectangle sRect = new Rectangle(r.x, r.y + 2 * r.height / 3, r.width, r.height / 3);
	Rectangle eRect = new Rectangle(r.x + 2 * r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3);
	if (nRect.contains(p))
		return nRect;
	if (wRect.contains(p))
		return wRect;
	if (cRect.contains(p))
		return cRect;
	if (eRect.contains(p))
		return eRect;
	if (sRect.contains(p))
		return sRect;
	return null;
}
/**
 * Determine what regions are still available in the BorderLayout and then assign a region
 * to each child. 
 * Return a Vector containing the constraints corresponding to each child.
 */
public List getDefaultConstraint(List children) {
	ArrayList constraints = new ArrayList(children.size());
	String[] regions = getAvailableRegions();
	// If no regions available or there is not enough to go around, 
	// return a collection with no constraints and later when it does the 
	// getCreateChildCommand, return the UnexecutableCommand command.
	if (regions == null || regions.length < children.size())
		constraints.addAll(Collections.nCopies(children.size(), null));
	else {
		// otherwise add the regions to a Vector.
		for (int i = 0; i < children.size(); i++)
			constraints.add(regions[i]);
	}
	return constraints;
}
/**
 * Return a list of occupied regions.
 * Collect a list of all of the components of the container.
 * Then iterate over these and collect all of their constraints
 * and remove from available ones
 */
public String[] getFilledRegions() {
	EStructuralFeature sfConstraintComponent = JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONSTRAINT_CONSTRAINT);
	ArrayList result = new ArrayList();
	if (getContainer() != null) {
		Iterator containerComponents = ((List)getContainer().eGet(JavaInstantiation.getSFeature(getContainer(), JFCConstants.SF_CONTAINER_COMPONENTS))).iterator();
		while (containerComponents.hasNext()) {
			EObject borderComponent = (EObject)containerComponents.next();
			// The border component has the constraint set onto it - turn this into a java.lang.String
			IJavaObjectInstance constraintsValue = (IJavaObjectInstance)borderComponent.eGet(sfConstraintComponent);
			if (constraintsValue != null) {
				// A constraint was set - get the string from it
				IBeanProxy cProxy = BeanProxyUtilities.getBeanProxy(constraintsValue, true);
				if (cProxy instanceof IStringBeanProxy)
					result.add( ((IStringBeanProxy)cProxy).stringValue() );
			}
		}
	}
	// Reprocess result to add tags for ones that are equivalent based on component orientation.
	//  (i.e. If orientation is left to right, "West" and "BEFORE_LINE_BEGINS" are the same
	HashMap tagMap = getInternalTagMap(getComponentOrientation(getContainer()));
	for (int i = 0; i < result.size(); i++) {
		String constraint = (String) result.get(i);
		String equalConstraint = (String) tagMap.get(constraint);
		if (!result.contains(equalConstraint))
			result.add(equalConstraint);
	}
	if (result.size() <= 0)
		return null;
	// Convert the vector to an array for the result
	String[] tags = new String[result.size()];
	for (int i = 0; i < result.size(); i++) {
		tags[i] = (String) result.get(i);
	}
	return tags;
}
/**
 * Check if given constraint is in the available set
 */
public boolean isRegionAvailable(String constraint) {
	String[] tags = getAvailableRegions();
	if (tags != null) {
		for (int i = 0; i < tags.length; i++)
			if (tags[i].equals(constraint))
				return true;
	}
	return false;
}
}
