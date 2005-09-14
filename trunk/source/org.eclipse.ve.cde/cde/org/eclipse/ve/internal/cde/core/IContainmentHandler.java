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
package org.eclipse.ve.internal.cde.core;

import java.util.List;

/*
 *  $RCSfile: IContainmentHandler.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-14 23:30:22 $ 
 */

/**
 * This is used to determine containment questions
 * about a child about to be added to a parent.
 *
 * This will be returned from the IModelAdapterFactory.getAdapter.
 * If the child doesn't care, then there is no need of the
 * handler, and it should return null.
 */
public interface IContainmentHandler {

	/**
	 * Return whether the parent is valid for this child.
	 * It is possible that the child is valid for the parent,
	 * but the parent is not acceptable to the child.
	 * <p>
	 * This will be called if the child is of a valid type for the parent.
	 * 
	 * @param parent
	 * @return 
	 * 
	 * @since 1.0.0
	 */
	public boolean isParentValid(Object parent);
	
	/**
	 * Get child to use if not itself. 
	 * <p>
	 * This will be called if {@link #isParentValid(Object)} returns false. It will
	 * ask for what should be used as the child instead of itself. It should return the
	 * child or <code>null</code> if no child is valid.
	 * <p>
	 * Some things to think of, if the selected feature is a containment feature, then there needs to be some setting in 
	 * the new child referring to the original child. Otherwise the original child will not get dragged in. Otherwise
	 * the child must be a valid setting on the original child so that the original child will get dragged in.
	 * @param parent
	 * @param features
	 * @return the ChildFeature containing the child and feature to use or <code>null</code> if no child can be given up.
	 * 
	 * @since 1.2.0
	 */
	public ChildFeature getChildToAdd(Object parent, List features);
	
	/**
	 * The child/feature to use instead of itself. When this object is returned from {@link IContainmentHandler#getChildToAdd(Object, List)}, the
	 * child in here must be a setting on the original child. Otherwise the original child will not be dragged in.
	 * 
	 * @since 1.2.0
	 */
	public class ChildFeature {
		private final Object child;
		private final Object childFeature;

		/**
		 * Construct with child and feature
		 * @param child to add
		 * @param childFeature to use to add child to parent (i.e. the parent's feature). It must be one that was sent into {@link IContainmentHandler#getChildToAdd(Object, List)}.
		 * @since 1.2.0
		 */
		public ChildFeature(Object child, Object childFeature) {
			this.child = child;
			this.childFeature = childFeature;
			
		}

		/**
		 * @return Returns the child.
		 * 
		 * @since 1.2.0
		 */
		public Object getChild() {
			return child;
		}

		/**
		 * @return Returns the child feature of the parent to use for the child.
		 * 
		 * @since 1.2.0
		 */
		public Object getChildFeature() {
			return childFeature;
		}
	}
	
}
