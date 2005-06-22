/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CBannerLayoutPolicyHelper.java,v $
 *  $Revision: 1.2 $  $Date: 2005-06-22 16:22:09 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * LayoutPolicyHelper for CBanner control to help show user feedback based on the available
 * regions in the CBanner. 
 * 
 * @since 1.1
 */
public class CBannerLayoutPolicyHelper extends LayoutPolicyHelper {
	
	protected VisualContainerPolicy policy;
	
	public static final List REAL_INTERNAL_TAGS;
	
	public static final List DISPLAY_TAGS;
		
	public static final int
		LEFT_INDEX = 0,
		RIGHT_INDEX = 1,
		BOTTOM_INDEX = 2;

	static {
		REAL_INTERNAL_TAGS = new ArrayList(3);
		REAL_INTERNAL_TAGS.add("Left"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Right"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Bottom"); //$NON-NLS-1$

		DISPLAY_TAGS= new ArrayList(3);
		DISPLAY_TAGS.add(SWTMessages.getString("CBannerLayout.Left")); //$NON-NLS-1$
		DISPLAY_TAGS.add(SWTMessages.getString("CBannerLayout.Right")); //$NON-NLS-1$
		DISPLAY_TAGS.add(SWTMessages.getString("CBannerLayout.Bottom")); //$NON-NLS-1$
	}
	
	/**
	 *  The map of tags that is displayed as feedback to the user.
	 */
	protected static HashMap getDisplayTagMap() {
			HashMap displayTagMap = new HashMap(CBannerLayoutPolicyHelper.DISPLAY_TAGS.size());
			List displayTags = CBannerLayoutPolicyHelper.DISPLAY_TAGS;
			displayTagMap.put(displayTags.get(LEFT_INDEX),displayTags.get(LEFT_INDEX));
			displayTagMap.put(displayTags.get(RIGHT_INDEX),displayTags.get(RIGHT_INDEX));
			displayTagMap.put(displayTags.get(BOTTOM_INDEX),displayTags.get(BOTTOM_INDEX));

		return displayTagMap;
	}
	
	/**
	 * Ther internal map of tags that allows for help positioning controls. 
	 */
	public static HashMap getInternalTagMap() {
		HashMap internalTagMap = new HashMap(REAL_INTERNAL_TAGS.size());
		List realInternalTags = CBannerLayoutPolicyHelper.REAL_INTERNAL_TAGS;
		internalTagMap.put(realInternalTags.get(LEFT_INDEX), realInternalTags.get(LEFT_INDEX));
		internalTagMap.put(realInternalTags.get(RIGHT_INDEX), realInternalTags.get(RIGHT_INDEX));
		internalTagMap.put(realInternalTags.get(BOTTOM_INDEX), realInternalTags.get(BOTTOM_INDEX));

		return internalTagMap;
	}
		
	public CBannerLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}
	
	public CBannerLayoutPolicyHelper() {
	}	
	
	private Rectangle boundingRectangle() {
		if (getContainer() != null){
			IBeanProxy containerProxy = BeanProxyUtilities.getBeanProxy(getContainer());
			IRectangleBeanProxy rectangleProxy = BeanSWTUtilities.invoke_getBounds(containerProxy);
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
	 */
	public String[] getAvailableRegions() {
		// Get a COPY of all the regions, else result.remove() will
		// remove from the original AllRegions()
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_LEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_RIGHT);
		EStructuralFeature sfBottomControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_BOTTOM);

		List result = new ArrayList(getAllRegions().size());
		result.addAll(getAllRegions());
		
		IJavaObjectInstance cBannerBean = getContainer();
		
		if(cBannerBean != null) {
			IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
			IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
			IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
			if(bottom != null)
				result.remove(BOTTOM_INDEX);
			if(right != null)
				result.remove(RIGHT_INDEX);
			if(left != null)
				result.remove(LEFT_INDEX);
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
	 * If any of the constraints are null such as when the regions are all
	 * occupied, return the UnexecutableCommand.
	 */
	public Command getAddChildrenCommand(List children, List constraints, Object position) {
		Object constraint = null;
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			if (next == null)
				return UnexecutableCommand.INSTANCE;
			if (REAL_INTERNAL_TAGS.contains(next))
				constraint = next;
		}
		Object child = children.get(0);
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_LEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_RIGHT);
		EStructuralFeature sfBottomControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_BOTTOM);
		
		IJavaObjectInstance cBannerBean = getContainer();
		IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
		Command setAsContent;
		EObject parent = getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left == null && ((String) REAL_INTERNAL_TAGS.get(LEFT_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfLeftControl, child, position);
		else if(right == null && ((String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfRightControl, child, position);
		else if(bottom == null && ((String) REAL_INTERNAL_TAGS.get(BOTTOM_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfBottomControl, child, position);
		
		setAsContent = cBld.getCommand();
		return super.getCreateChildCommand(child, constraint, position).chain(setAsContent);
	}
	
	/**
	 * If the constraint is null such as when the regions are all
	 * occupied, return the UnexecutableCommand.
	 */
	public Command getCreateChildCommand(Object child, Object constraint, Object position) {
		if (constraint == null)
			return UnexecutableCommand.INSTANCE;
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_LEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_RIGHT);
		EStructuralFeature sfBottomControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_BOTTOM);
		
		IJavaObjectInstance cBannerBean = getContainer();
		IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
		IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
		Command setAsContent;
		EObject parent = getContainer();
		CommandBuilder cBld = new CommandBuilder(""); //$NON-NLS-1$
		
		if(left == null && ((String) REAL_INTERNAL_TAGS.get(LEFT_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfLeftControl, child, position);
		else if(right == null && ((String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfRightControl, child, position);
		else if(bottom == null && ((String) REAL_INTERNAL_TAGS.get(BOTTOM_INDEX)).equals(constraint))
			cBld.applyAttributeSetting(parent, sfBottomControl, child, position);
		
		setAsContent = cBld.getCommand();
		return super.getCreateChildCommand(child, constraint, position).chain(setAsContent);
	}

	public String getCurrentConstraint(Point p) {
		if (p == null)
			return null;
		Rectangle r = boundingRectangle();
		Rectangle leftRect = new Rectangle(r.x, r.y, r.width / 2, r.height / 2);
		Rectangle rightRect = new Rectangle(r.x + r.width / 2, r.y, r.width / 2, r.height / 2);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height / 2, r.width, r.height / 2);

		if (leftRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(LEFT_INDEX);
		if (rightRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX);
		if (bottomRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(BOTTOM_INDEX);
		return null;
	}
	
	public Rectangle getCurrentRectangle(Point p) {
		if (p == null)
			return null;
		Rectangle r = boundingRectangle();
		Rectangle leftRect = new Rectangle(r.x, r.y, r.width / 2, r.height / 2);
		Rectangle rightRect = new Rectangle(r.x + r.width / 2, r.y, r.width / 2, r.height / 2);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height / 2, r.width, r.height / 2);

		if (leftRect.contains(p))
			return leftRect;
		if (rightRect.contains(p))
			return rightRect;
		if (bottomRect.contains(p))
			return bottomRect;
		return null;
	}
	
	/**
	 * Determine what regions are still available in the CBannerLayout and then assign a region
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
			return Collections.nCopies(children.size(), null);
		else
			// otherwise add the regions to a Vector.
			for (int i = 0; i < children.size(); i++)
				constraints.add(regions[i]);
		return constraints;
	}
	
	/**
	 * Return a list of occupied regions.
	 */
	public String[] getFilledRegions() {
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_LEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_RIGHT);
		EStructuralFeature sfBottomControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_CBANNER_BOTTOM);

		ArrayList result = new ArrayList();

		IJavaObjectInstance cBannerBean = getContainer();
		
		if(cBannerBean != null) {
			IJavaInstance left = (IJavaInstance) cBannerBean.eGet(sfLeftControl);
			IJavaInstance right = (IJavaInstance) cBannerBean.eGet(sfRightControl);
			IJavaInstance bottom = (IJavaInstance) cBannerBean.eGet(sfBottomControl);
		
			if(left != null)
				result.add(REAL_INTERNAL_TAGS.get(LEFT_INDEX));
			if(right != null)
				result.add(REAL_INTERNAL_TAGS.get(RIGHT_INDEX));
			if(bottom != null)
				result.add(REAL_INTERNAL_TAGS.get(BOTTOM_INDEX));
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
	
	protected void cancelConstraints(CommandBuilder commandBuilder, List children) {
		// No action required.
	}
	
}
