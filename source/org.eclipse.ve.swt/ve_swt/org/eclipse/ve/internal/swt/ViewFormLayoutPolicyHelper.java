/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewFormLayoutPolicyHelper.java,v $
 *  $Revision: 1.7 $  $Date: 2006-08-17 15:32:01 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IRectangleBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 
/**
 * LayoutPolicyHelper for ViewForm to help provide feedback to user about what cells
 * are occupied and what cells are available for dropping.
 * 
 * @since 1.1
 */
public class ViewFormLayoutPolicyHelper extends LayoutPolicyHelper {
	
	protected VisualContainerPolicy policy;
	
	public static final List REAL_INTERNAL_TAGS;
	
	public static final List DISPLAY_TAGS;
		
	public static final int
		LEFT_INDEX = 0,
		RIGHT_INDEX = 1,
		CENTER_INDEX = 2,
		CONTENT_INDEX = 3;

	static {
		REAL_INTERNAL_TAGS = new ArrayList(4);
		REAL_INTERNAL_TAGS.add("Top Left"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Top Right"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Top Center"); //$NON-NLS-1$
		REAL_INTERNAL_TAGS.add("Content"); //$NON-NLS-1$

		DISPLAY_TAGS= new ArrayList(4);
		DISPLAY_TAGS.add(SWTMessages.ViewFormLayout_TopLeft); 
		DISPLAY_TAGS.add(SWTMessages.ViewFormLayout_TopRight); 
		DISPLAY_TAGS.add(SWTMessages.ViewFormLayout_TopCenter); 
		DISPLAY_TAGS.add(SWTMessages.ViewFormLayout_Content); 
	}
	
	/**
	 *  The map of tags that is displayed as feedback to the user.
	 */
	protected static HashMap getDisplayTagMap() {
			HashMap displayTagMap = new HashMap(ViewFormLayoutPolicyHelper.DISPLAY_TAGS.size());
			List displayTags = ViewFormLayoutPolicyHelper.DISPLAY_TAGS;
			displayTagMap.put(displayTags.get(LEFT_INDEX),displayTags.get(LEFT_INDEX));
			displayTagMap.put(displayTags.get(RIGHT_INDEX),displayTags.get(RIGHT_INDEX));
			displayTagMap.put(displayTags.get(CENTER_INDEX),displayTags.get(CENTER_INDEX));
			displayTagMap.put(displayTags.get(CONTENT_INDEX),displayTags.get(CONTENT_INDEX));

		return displayTagMap;
	}
	
	/**
	 * Ther internal map of tags that allows for help positioning controls. 
	 */
	public static HashMap getInternalTagMap() {
		HashMap internalTagMap = new HashMap(REAL_INTERNAL_TAGS.size());
		List realInternalTags = ViewFormLayoutPolicyHelper.REAL_INTERNAL_TAGS;
		internalTagMap.put(realInternalTags.get(LEFT_INDEX), realInternalTags.get(LEFT_INDEX));
		internalTagMap.put(realInternalTags.get(RIGHT_INDEX), realInternalTags.get(RIGHT_INDEX));
		internalTagMap.put(realInternalTags.get(CENTER_INDEX), realInternalTags.get(CENTER_INDEX));
		internalTagMap.put(realInternalTags.get(CONTENT_INDEX), realInternalTags.get(CONTENT_INDEX));

		return internalTagMap;
	}
		
	public ViewFormLayoutPolicyHelper(VisualContainerPolicy ep) {
		super(ep);
	}
	
	public ViewFormLayoutPolicyHelper() {
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
	
	/**
	* return a point in the container's parent coordinates
	*/
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
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPLEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPRIGHT);
		EStructuralFeature sfCenterControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPCENTER);
		EStructuralFeature sfContentControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_CONTENT);

		List result = new ArrayList(getAllRegions().size());
		result.addAll(getAllRegions());
		
		IJavaObjectInstance viewFormBean = getContainer();
		
		if(viewFormBean != null) {
			IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
			IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
			IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
			IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		
			if(content != null)
				result.remove(CONTENT_INDEX);
			if(center != null)
				result.remove(CENTER_INDEX);
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
	public VisualContainerPolicy.CorelatedResult getAddChildrenCommand(List children, List constraints, Object position) {
		Iterator itr = constraints.iterator();
		while (itr.hasNext()) {
			Object next = itr.next();
			if (next == null) 
				return VisualContainerPolicy.createUnexecutableResult(children, constraints);
			String constraint = ((ViewFormLayoutEditPolicy.ViewFormConstraintWrapper) next).getViewFormConstraint();
			if (!REAL_INTERNAL_TAGS.contains(constraint))
				return VisualContainerPolicy.createUnexecutableResult(children, constraints); 
		}
		Object child = children.get(0);
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPLEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPRIGHT);
		EStructuralFeature sfCenterControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPCENTER);
		EStructuralFeature sfContentControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_CONTENT);
		
		IJavaObjectInstance viewFormBean = getContainer();
		IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
				
		VisualContainerPolicy.CorelatedResult result = super.getAddChildrenCommand(Collections.singletonList(child), Collections.singletonList(null), position);
		if (!result.getChildren().isEmpty()) {
			EObject parent = getContainer();
			child = result.getChildren().get(0);
			CommandBuilder cBld = new CommandBuilder();
			cBld.append(result.getCommand());
			String constraint = ((ViewFormLayoutEditPolicy.ViewFormConstraintWrapper) result.getCorelatedList().get(0)).getViewFormConstraint();
			if(left == null && ((String) REAL_INTERNAL_TAGS.get(LEFT_INDEX)).equals(constraint))
				cBld.applyAttributeSetting(parent, sfLeftControl, child, position);
			else if(right == null && ((String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX)).equals(constraint))
				cBld.applyAttributeSetting(parent, sfRightControl, child, position);
			else if(center == null && ((String) REAL_INTERNAL_TAGS.get(CENTER_INDEX)).equals(constraint))
				cBld.applyAttributeSetting(parent, sfCenterControl, child, position);
			else if(content == null && ((String) REAL_INTERNAL_TAGS.get(CONTENT_INDEX)).equals(constraint))
				cBld.applyAttributeSetting(parent, sfContentControl, child, position);
			result.setCommand(cBld.getCommand());
		}
		return result;
		
	}
	
	/**
	 * If the constraint is null such as when the regions are all
	 * occupied, return the UnexecutableCommand.
	 */
	public VisualContainerPolicy.CorelatedResult getCreateChildCommand(Object child, Object constraint, Object position) {
		if (constraint == null) {
			return VisualContainerPolicy.createUnexecutableResult(child, constraint);
		}
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPLEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPRIGHT);
		EStructuralFeature sfCenterControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPCENTER);
		EStructuralFeature sfContentControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_CONTENT);
		
		IJavaObjectInstance viewFormBean = getContainer();
		IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
		IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
		IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
		IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		
		VisualContainerPolicy.CorelatedResult result = super.getCreateChildCommand(child, constraint, position);
		if (!result.getChildren().isEmpty()) {
			EObject parent = getContainer();
			child = result.getChildren().get(0);
			CommandBuilder cBld = new CommandBuilder();
			cBld.append(result.getCommand());
			String realConstraint = ((ViewFormLayoutEditPolicy.ViewFormConstraintWrapper) constraint).getViewFormConstraint();
			if(left == null && ((String) REAL_INTERNAL_TAGS.get(LEFT_INDEX)).equals(realConstraint))
				cBld.applyAttributeSetting(parent, sfLeftControl, child, position);
			else if(right == null && ((String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX)).equals(realConstraint))
				cBld.applyAttributeSetting(parent, sfRightControl, child, position);
			else if(center == null && ((String) REAL_INTERNAL_TAGS.get(CENTER_INDEX)).equals(realConstraint))
				cBld.applyAttributeSetting(parent, sfCenterControl, child, position);
			else if(content == null && ((String) REAL_INTERNAL_TAGS.get(CONTENT_INDEX)).equals(realConstraint))
				cBld.applyAttributeSetting(parent, sfContentControl, child, position);
			result.setCommand(cBld.getCommand());
		}
		return result;
	}

	public String getCurrentConstraint(Point p) {
		if (p == null)
			return null;
		Rectangle r = boundingRectangle();
		Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 3, r.height / 3);
		Rectangle centerRect = new Rectangle(r.x + r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle rightRect = new Rectangle( r.x + 2 * r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle contentRect = new Rectangle( r.x, r.y + r.height / 3, r.width, 2 * r.height / 3);

		if (leftRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(LEFT_INDEX);
		if (rightRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(RIGHT_INDEX);
		if (centerRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(CENTER_INDEX);
		if (contentRect.contains(p))
			return (String) REAL_INTERNAL_TAGS.get(CONTENT_INDEX);
		return null;
	}
	
	public Rectangle getCurrentRectangle(Point p) {
		if (p == null)
			return null;
		Rectangle r = boundingRectangle();
		Rectangle leftRect = new Rectangle( r.x, r.y, r.width / 3, r.height / 3);
		Rectangle centerRect = new Rectangle(r.x + r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle rightRect = new Rectangle( r.x + 2 * r.width / 3, r.y, r.width / 3, r.height / 3);
		Rectangle contentRect = new Rectangle( r.x, r.y + r.height / 3, r.width, 2 * r.height / 3);

		if (leftRect.contains(p))
			return leftRect;
		if (rightRect.contains(p))
			return rightRect;
		if (centerRect.contains(p))
			return centerRect;
		if (contentRect.contains(p))
			return contentRect;
		return null;
	}
	
	/**
	 * Determine what regions are still available in the ViewForm and then assign a region
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
	 */
	public String[] getFilledRegions() {
		
		EStructuralFeature sfLeftControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPLEFT);
		EStructuralFeature sfRightControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPRIGHT);
		EStructuralFeature sfCenterControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_TOPCENTER);
		EStructuralFeature sfContentControl = JavaInstantiation.getSFeature(getContainer(), SWTConstants.SF_VIEWFORM_CONTENT);

		ArrayList result = new ArrayList();

		IJavaObjectInstance viewFormBean = getContainer();
		
		if(viewFormBean != null) {
			IJavaInstance left = (IJavaInstance) viewFormBean.eGet(sfLeftControl);
			IJavaInstance right = (IJavaInstance) viewFormBean.eGet(sfRightControl);
			IJavaInstance center = (IJavaInstance) viewFormBean.eGet(sfCenterControl);
			IJavaInstance content = (IJavaInstance) viewFormBean.eGet(sfContentControl);
		
			if(left != null)
				result.add(REAL_INTERNAL_TAGS.get(LEFT_INDEX));
			if(right != null)
				result.add(REAL_INTERNAL_TAGS.get(RIGHT_INDEX));
			if(center != null)
				result.add(REAL_INTERNAL_TAGS.get(CENTER_INDEX));
			if(content != null)
				result.add(REAL_INTERNAL_TAGS.get(CONTENT_INDEX));
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
