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
 *  $Revision: 1.3 $  $Date: 2005-05-27 12:48:47 $ 
 */
package org.eclipse.ve.internal.jface;

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPart;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;

import org.eclipse.ve.internal.swt.ControlGraphicalEditPart;
import org.eclipse.ve.internal.swt.SwtPlugin;

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

	public ViewPartGraphicalEditPart(Object model) {
		super(model);
	}
	
	public void setModel(Object aModel) {
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
		// Out child edit part is transparent as we are the top level one on the free form
		EditPart ep = super.createChild(model);
		if (ep instanceof ControlGraphicalEditPart) {
			((ControlGraphicalEditPart) ep).setTransparent(true);
		}
		return ep;
	}

	public Object getAdapter(Class type) {
		if(type==IConstraintHandler.class){
			if(constraintHandler==null)
				constraintHandler =new ViewPartVisualModelAdapter(getModel());;
			return constraintHandler;
		}
		return super.getAdapter(type);
	}	
}