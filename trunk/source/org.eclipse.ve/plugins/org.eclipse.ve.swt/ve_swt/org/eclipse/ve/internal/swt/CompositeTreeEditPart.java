package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.java.core.*;
/**
 * TreeEditPart for a SWT Container.
 */
public class CompositeTreeEditPart extends JavaBeanTreeEditPart {
	
	private EReference 
			sf_compositeLayout,
			sf_compositeControls;	

	public CompositeTreeEditPart(Object model) {
		super(model);
	}

	protected TreeContainerEditPolicy treeContainerPolicy;
	
	protected ContainerPolicy getContainerPolicy() {
		return new JavaContainerPolicy(sf_compositeControls,EditDomain.getEditDomain(this));	// SWT standard Contained Edit Policy
	}	
		
	protected List getChildJavaBeans() {
		return (List) ((EObject) getModel()).eGet(sf_compositeControls);
	}

	protected Adapter compositeAdapter = new Adapter() {
		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_compositeControls)
				refreshChildren();
			else if (notification.getFeature() == sf_compositeLayout)
				createLayoutPolicyHelper();
		}

		public Notifier getTarget() {
			return null;
		}

		public void setTarget(Notifier newTarget) {
		}

		public boolean isAdapterForType(Object type) {
			return false;
		}
	};
	
	public void activate() {
		super.activate();	
		((EObject) getModel()).eAdapters().add(compositeAdapter);
	}
	
	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(compositeAdapter);
	}
		
		protected void createEditPolicies() {
		super.createEditPolicies();
		treeContainerPolicy = new TreeContainerEditPolicy(getContainerPolicy());
		installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, treeContainerPolicy);
		createLayoutPolicyHelper();
	}
	
	protected void createLayoutPolicyHelper() {
		//TODO
	}
	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		
		ResourceSet rset = ((IJavaObjectInstance) model).eResource().getResourceSet();
		sf_compositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
		sf_compositeControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);					
	}

}
