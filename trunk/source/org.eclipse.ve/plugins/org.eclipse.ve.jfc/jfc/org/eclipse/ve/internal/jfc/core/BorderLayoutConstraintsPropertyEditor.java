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

import java.text.MessageFormat;
import java.util.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.propertysheet.*;

/**
 * This property editor is used for the constraints attribute for components whose parent container has a layout manager of java.awt.BorderLayout It
 * is an example of a property editor for beans that is specialized and is part of the IDE The IDE knows to use it because the BeanType for the
 * BorderLayout bean class has the constraintsAttributePropertyEditorClass set to the name of this class It could also be done by specifying a
 * java.beans.PropertyEditor as well by setting the atttribute constraintsAttributeJavaBeansPropertyEditorClassName Creation date: (1/27/00 6:50:03
 * AM)
 * 
 * @author: Joe Winchester
 */

public class BorderLayoutConstraintsPropertyEditor extends ObjectComboBoxCellEditor implements ISourced, INeedData {

	public static class BorderLayoutConstraintsLabelProvider extends LabelProvider {

		public String getText(Object element) {
			String internalTag = null;
			if (element instanceof IJavaInstance) {
				IBeanProxy proxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) element, true);
				if (proxy == null)
					internalTag = ""; // It shouldn't be null. //$NON-NLS-1$
				else
					internalTag = proxy.toBeanString();
			} else {
				return super.getText(element);
			}
			int index = BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.indexOf(internalTag);
			return index > -1 ? (String) BorderLayoutPolicyHelper.DISPLAY_TAGS.get(index) : ""; //$NON-NLS-1$
		}
	}

	private IJavaObjectInstance fCurrentMOFConstraint;

	protected String fCurrentStringConstraint;

	protected Object[] fSources;

	protected EObject firstConstraintComponent;

	protected String[] fTags;

	protected EditDomain fEditDomain;

	protected int fOrientation = BorderLayoutPolicyHelper.LEFT_TO_RIGHT; // default component orientation

	public BorderLayoutConstraintsPropertyEditor(Composite aComposite) {
		super(aComposite);
	}

	/**
	 * Return a MOF class that represents the constraint bean
	 */
	protected Object doGetObject(int index) {
		if (index == NO_SELECTION)
			return null;
		String selectedConstraint = fTags[index];
		return BeanUtilities.createJavaObject("java.lang.String", //$NON-NLS-1$
				JavaEditDomainHelper.getResourceSet(fEditDomain), BorderLayoutPolicyHelper
						.createBorderAllocation(BorderLayoutPolicyHelper.DISPLAY_TAGS.indexOf(selectedConstraint)));
	}

	/**
	 * The value passed in is actually the MOFObject that wrappers the constraint string class.
	 * 
	 * The fValue is set to the actual constraint name.
	 */
	protected void doSetObject(Object value) {
		if (isValueValid() && value instanceof IJavaObjectInstance) {
			fCurrentMOFConstraint = (IJavaObjectInstance) value;
			// This constraint should have a string proxy. Get this and store it
			// as the current constraint
			IStringBeanProxy constraintProxy = (IStringBeanProxy) BeanProxyUtilities.getBeanProxy(fCurrentMOFConstraint);
			fCurrentStringConstraint = (String) BorderLayoutPolicyHelper.DISPLAY_TAGS.get(BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS
					.indexOf(constraintProxy.stringValue()));
		}
		// Get the component orientation from the container (firstConstraintComponent is the ConstraintComponent itself, so it's container is the
		// awt.Container).
		if (firstConstraintComponent.eContainer() != null)
			fOrientation = BorderLayoutPolicyHelper.getComponentOrientation((IJavaObjectInstance) firstConstraintComponent.eContainer());
		// The tag is null. If the combo box is not populated yet populate if
		if (fTags == null) {
			setItems(getTags());
		}
	}

	protected int doGetIndex(Object value) {
		// Return the current tag
		for (int i = 0; i < getTags().length; i++) {
			if (fTags[i].equals(fCurrentStringConstraint)) { return i; }
		}
		return NO_SELECTION;
	}

	/**
	 * Return a list of occupied tags. To do this traverse from the component ( our source ) to its parent container and then collect a list of all of
	 * the components of the container. Then iterate over these and collect all of their constraints
	 */
	protected List getOccupiedTags() {
		ArrayList result = new ArrayList(BorderLayoutPolicyHelper.DISPLAY_TAGS.size());
		IJavaObjectInstance containerBean = (IJavaObjectInstance) firstConstraintComponent.eContainer();
		if (containerBean == null)
			return result;

		EStructuralFeature constraintSF = null; // We'll get it when we first need it.
		// Find the container's components
		List containerComponents = (List) containerBean.eGet(JavaInstantiation.getSFeature(containerBean, JFCConstants.SF_CONTAINER_COMPONENTS));
		Iterator iter = containerComponents.iterator();
		while (iter.hasNext()) {
			EObject constraintComponent = (EObject) iter.next();
			if (constraintSF == null) {
				// constraintComponent better be a BorderLayoutConstraintComponent. We're not testing.
				constraintSF = constraintComponent.eClass().getEStructuralFeature("constraint"); //$NON-NLS-1$
			}
			IJavaObjectInstance constraintsValue = (IJavaObjectInstance) constraintComponent.eGet(constraintSF);
			if (constraintsValue != null) {
				// A constraint was set
				// We know the constraints value should be a bean that has a string proxy
				IBeanProxy constraintsValueProxy = BeanProxyUtilities.getBeanProxy(constraintsValue);
				if (constraintsValueProxy instanceof IStringBeanProxy)
					result.add(((IStringBeanProxy) constraintsValueProxy).stringValue());
			} else {
				// It is null, so it is center.
				result.add("Center"); //$NON-NLS-1$
			}
		}
		// Reprocess result to add tags for ones that are equivalent based on component orientation.
		// (i.e. If orientation is left to right, "West" and "BEFORE_LINE_BEGINS" are the same
		HashMap tapmap = BorderLayoutPolicyHelper.getInternalTagMap(fOrientation);
		for (int i = 0; i < result.size(); i++) {
			String constraint = (String) result.get(i);
			String equalConstraint = (String) tapmap.get(constraint);
			if (!result.contains(equalConstraint))
				result.add(equalConstraint);
		}
		return result;
	}

	/**
	 * Return the list of allowable tags for border layout These are cached in fTags and calculated lazily The tags are the unoccopied constraints. To
	 * see which are unoccupied we must get the occupied constraints and then remove these from the list of occupied ones, adding back in our own
	 * constraint ,e.g. the getOccupiedConstraints() could return "North" "Center" "East" and we then create a list that includes "West" and "South"
	 * because these are valid and available. However our own current constraint ( e.g. "North" ) is also available so we add it back in
	 */
	protected String[] getTags() {
		if (fTags != null)
			return fTags;
		ArrayList potentialTags = new ArrayList(BorderLayoutPolicyHelper.DISPLAY_TAGS.size());
		for (int i = 0; i < BorderLayoutPolicyHelper.DISPLAY_TAGS.size(); i++) {
			potentialTags.add(BorderLayoutPolicyHelper.DISPLAY_TAGS.get(i));
		}
		Iterator occupiedTags = getOccupiedTags().iterator();
		while (occupiedTags.hasNext()) {
			int index = BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.indexOf(occupiedTags.next());
			if (index > -1)
				potentialTags.remove(BorderLayoutPolicyHelper.DISPLAY_TAGS.get(index));
		}
		// Add back in the constraint for us.. get this from fValue which is
		// the same as the fLabel in the comboBox list.
		if (fCurrentStringConstraint != null) {
			potentialTags.add(fCurrentStringConstraint);
			// Also add back the corresponding equivalent tag based on the component orientation
			// (i.e. If orientation is left to right, "West" and "BEFORE_LINE_BEGINS" are the same
			String correspondingTag = (String) BorderLayoutPolicyHelper.getDisplayTagMap(fOrientation).get(fCurrentStringConstraint);
			if (!potentialTags.contains(correspondingTag))
				potentialTags.add(correspondingTag);
		}
		// Convert the list to an array for the result
		fTags = new String[potentialTags.size()];
		for (int i = 0; i < potentialTags.size(); i++) {
			fTags[i] = (String) potentialTags.get(i);
		}
		return fTags;
	}

	/**
	 * setSource method comment.
	 */
	public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
		fSources = sources;
		firstConstraintComponent = (EObject) sources[0];
		fTags = null;
	}

	public String isCorrectObject(Object anObject) {
		String ansString = null;
		if (anObject instanceof IJavaObjectInstance) {
			IBeanProxy constraintProxy = BeanProxyUtilities.getBeanProxy((IJavaObjectInstance) anObject);
			if (constraintProxy instanceof IStringBeanProxy) {
				String str = ((IStringBeanProxy) constraintProxy).stringValue();
				if (!BorderLayoutPolicyHelper.REAL_INTERNAL_TAGS.contains(str))
					ansString = str;
			} else if (ansString != null)
				ansString = constraintProxy.toBeanString();
			else
				ansString = ""; // It shouldn't be null or non string, means error in instantiation //$NON-NLS-1$
		} else if (anObject != null)
			ansString = anObject.toString();

		return ansString == null ? null : MessageFormat.format(JFCMessages.BorderLayoutConstraintsPropertyEditor_InvalidConstraint_Msg, new Object[] { ansString});
	}

	public void setData(Object data) {
		fEditDomain = (EditDomain) data;
	}
}
