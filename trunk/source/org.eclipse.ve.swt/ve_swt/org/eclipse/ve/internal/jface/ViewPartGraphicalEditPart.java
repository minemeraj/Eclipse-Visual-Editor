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
 *  $RCSfile: ViewPartGraphicalEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2005-06-07 13:38:08 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
import org.eclipse.ve.internal.swt.*;

public class ViewPartGraphicalEditPart extends ControlGraphicalEditPart {
	
	public class ViewPartVisualModelAdapter extends ControlVisualModelAdapter{

		public ViewPartVisualModelAdapter(Object aControl) {
			super(aControl);
		}

		public boolean isResizeable() {
			return false;
		}
		
	}
	
	protected IBeanProxyHost compositeProxyHost;
	private EStructuralFeature sf_delegate_control;

	public ViewPartGraphicalEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object aModel) {
		sf_delegate_control = ((EObject)aModel).eClass().getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL);		
		super.setModel(aModel);
	}
	
	protected IBeanProxyHost getControlProxy() {

		if(compositeProxyHost == null){
			// Get the composite
			ViewPartProxyAdapter viewPartProxyAdapter = (ViewPartProxyAdapter) BeanProxyUtilities.getBeanProxyHost(getBean());
			compositeProxyHost = viewPartProxyAdapter;
		}
		return compositeProxyHost;
		
	}
	
	protected IFigure createFigure() {
		ContentPaneFigure cf = (ContentPaneFigure) super.createFigure();
		cf.getContentPane().setLayoutManager(new XYLayout());
		return cf;
	}	
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy(true));
		removeEditPolicy(CopyAction.REQ_COPY); // A ViewPart cannot be copied		
	}
		
	public List getModelChildren() {
		// The "delegate_control" is our child
		List result = new ArrayList(1);
		IJavaInstance delegate_control = getDelegateComposite();
		if(delegate_control != null){
			result.add(delegate_control);
			return result;			
		} else {
			return Collections.EMPTY_LIST;
		}	
	}
	
	private IJavaInstance getDelegateComposite(){
		return BeanUtilities.getFeatureValue((IJavaInstance)getModel(),SwtPlugin.DELEGATE_CONTROL);		
	}
	
	protected EditPart createChild(Object model) {		
		// The child will be a CompositeTreeEditPart if we have a single child composite.  The problem is that we cannot allow any more children to be dropped onto it
		// as currently this creates bad code
		CompositeGraphicalEditPart result = new CompositeGraphicalEditPart(model){			
			protected VisualContainerPolicy getContainerPolicy() {
				return new CompositeNoOpContainerPolicy(EditDomain.getEditDomain(this));
			}
		};
		// Our child edit part is transparent as we are the top level one on the free form			
		result.setTransparent(true);
		return result;
	}

	public Object getAdapter(Class type) {
		if(type==IConstraintHandler.class){
			if(constraintHandler==null)
				constraintHandler =new ViewPartVisualModelAdapter(getModel());;
			return constraintHandler;
		}
		return super.getAdapter(type);
	}	
	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(viewPartAdapter);
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(viewPartAdapter);
	}	
	
	protected Adapter viewPartAdapter = new EditPartAdapterRunnable(this) {
		public void doRun() {
			refreshChildren();
		}

		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sf_delegate_control) {
				queueExec(ViewPartGraphicalEditPart.this, "DELEGATE_CONTROL"); //$NON-NLS-1$
			}
		}
	};	
}