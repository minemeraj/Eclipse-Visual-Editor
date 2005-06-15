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
 *  $RCSfile: ControlManagerFeedbackController_30.java,v $
 *  $Revision: 1.1 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.util.*;

import org.eclipse.swt.widgets.*;

/**
 * This is the version of the feedback controller for SWT 3.0.x
 * 
 * @since 1.1.0
 */
public class ControlManagerFeedbackController_30 extends ControlManager.ControlManagerFeedbackController {

	/**
	 * Create it.
	 * 
	 * @param environment
	 * 
	 * @since 1.1.0
	 */
	public ControlManagerFeedbackController_30(Environment environment) {
		super(environment);
	}

	private static class CompositeTree {

		public final Composite composite;

		public CompositeTree[] children;

		/**
		 * Construct with a composite
		 * 
		 * @param composite
		 * 
		 * @since 1.1.0
		 */
		public CompositeTree(Composite composite) {
			this.composite = composite;
		}

		/**
		 * Add a child composite tree. Return whether it was added or was already there.
		 * 
		 * @param child
		 * @return <code>true</code> if it was added, <code>false</code> if it was already there.
		 * 
		 * @since 1.1.0
		 */
		public boolean addChild(CompositeTree child) {
			if (children == null) {
				children = new CompositeTree[5]; // Give some room.
				children[0] = child;
				return true;
			} else {
				for (int i = 0; i < children.length; i++) {
					CompositeTree ct = children[i];
					if (ct == child)
						return false; // Already there.
					else if (ct == null) {
						// We can go here, a free spot.
						children[i] = child;
						return true;
					}
				}
				// Neither found nor was there a free spot, need to reallocate.
				CompositeTree[] newGuy = new CompositeTree[children.length * 2];
				System.arraycopy(children, 0, newGuy, 0, children.length);
				newGuy[children.length] = child;
				children = newGuy;
				return true;
			}
		}
	}

	protected Collection validateControls(Map invalidControls) {
		// We could take the simple approach that we did in the past of for each invalid control we
		// walk up the tree and relayout each composite. But that means each composite could be layed
		// out over and over even though it hasn't changed. Instead we will try to be smarter by
		// building a tree of composites, and then just walking once.

		// Set of controls/composites/shells that are invalid, including those that had children invalid.
		// This will actually be dual-purposed. The keys will be everything that is invalid. But the entry
		// will be either "null" if it is a control, or a CompositeTree otherwise. That way we can kill
		// two birds with one map.
		Map invalidatedControls = new HashMap(100);

		Shell freeformHost = environment.getFreeFormHost();
		// Map is shell->Set(invalid controls in shell)
		// For each still active shell, build the tree of composites that need layout and
		// lay them out.
		Iterator shellEntries = invalidControls.entrySet().iterator();
		while (shellEntries.hasNext()) {
			Map.Entry shellEntry = (Map.Entry) shellEntries.next();
			Shell shell = (Shell) shellEntry.getKey();

			// Now we walk all of the invalid controls to build up a tree of composites that need to be layed out.
			Iterator invalids = ((Collection) shellEntry.getValue()).iterator();
			while (invalids.hasNext()) {
				Control control = (Control) invalids.next();
				// invalidated instead.
				CompositeTree childCompositeTree = null;
				if (!(control instanceof Composite)) {
					// It doesn't have children. It is a leaf.
					invalidatedControls.put(control, null);
					sendAboutToValidateToManager(control);
				} else {
					childCompositeTree = (CompositeTree) invalidatedControls.get(control);
					if (childCompositeTree == null) {
						invalidatedControls.put(control, childCompositeTree = new CompositeTree((Composite) control));
						sendAboutToValidateToManager(control);
					}
				}
				// Now we walk up the parents until we get one that has a composite tree and the child composite is in it, creating them as we go.
				Composite parent = control.getParent();
				while (parent != null) {
					CompositeTree parentTree = (CompositeTree) invalidatedControls.get(parent);
					if (parentTree == null) {
						invalidatedControls.put(parent, parentTree = new CompositeTree(parent));
						sendAboutToValidateToManager(parent);
					}
					if (childCompositeTree != null) {
						// Add a child into the list. If already in the list we can stop because the rest of the tree parents have already been added.
						if (!parentTree.addChild(childCompositeTree))
							break; // It was already there, so we can stop.
					}
				}
			}

			try {
				// Now we layout from the bottom to the top. A depth-first walk.
				layoutTree((CompositeTree) invalidControls.get(shell));
				if (shell == freeformHost)
					shell.pack();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}

		return invalidatedControls.keySet(); // These are the invalid controls/composites that need their images refreshed.
	}

	private void layoutTree(CompositeTree tree) {
		if (tree == null)
			return;
		// First layout all of the children, then layout self.
		if (tree.children != null) {
			for (int i = 0; i < tree.children.length; i++) {
				layoutTree(tree.children[i]);
			}
		}
		tree.composite.layout(true);
	}

}
