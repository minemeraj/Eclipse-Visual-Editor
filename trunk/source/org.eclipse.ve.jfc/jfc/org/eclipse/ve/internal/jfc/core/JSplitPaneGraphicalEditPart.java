/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: JSplitPaneGraphicalEditPart.java,v $ $Revision: 1.3 $ $Date: 2004-05-24 17:56:08 $
 */

package org.eclipse.ve.internal.jfc.core;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.VisualComponentsLayoutPolicy;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

/**
 * Graphical edit part for handling JSplitPane in the Graph viewer
 */
public class JSplitPaneGraphicalEditPart extends ComponentGraphicalEditPart {

	private Adapter containerAdapter = new EditPartAdapterRunnable() {
		public void run() {
			if (isActive())
				refreshChildren();
		}

		public void notifyChanged(Notification msg) {
			Object feature = msg.getFeature();
			if (feature == sfLeftComponent || feature == sfRightComponent || feature == sfTopComponent || feature == sfBottomComponent
					|| feature == sf_containerComponents)
				queueExec(JSplitPaneGraphicalEditPart.this);
		}
	};

	protected EStructuralFeature sfLeftComponent, sfRightComponent, sfTopComponent, sfBottomComponent, sf_constraintComponent,
			sf_containerComponents;

	public JSplitPaneGraphicalEditPart(Object model) {
		super(model);
	}

	protected EditPart createChild(Object model) {
		EditPart ep = super.createChild(model);
		((ComponentGraphicalEditPart) ep).setPropertySource(new NonBoundsBeanPropertySource((EObject) model));
		((ComponentGraphicalEditPart) ep).setTransparent(true); // So that it doesn't create an image, we subsume it here.
		return ep;
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(VisualComponentsLayoutPolicy.LAYOUT_POLICY, new VisualComponentsLayoutPolicy());
		// This is a special policy that just handles the size/position of visual components wrt/the figures. It does not handle changing
		// size/position.
		createLayoutEditPolicy();
	}

	protected void createLayoutEditPolicy() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new JSplitPaneLayoutEditPolicy(EditDomain.getEditDomain(this)));
	}

	protected IFigure createFigure() {
		IFigure fig = super.createFigure();
		fig.setLayoutManager(new XYLayout());
		return fig;
	}
	class TabLabel extends Figure{
		private String string;
		TabLabel(String aString){
			string=aString;
//			setBorder(new LineBorder(ColorConstants.black));
		}
		protected void paintClientArea(Graphics graphics) {
			Rectangle bounds = getBounds();
			Rectangle innerBounds = getBounds().getCopy();
			innerBounds.width = innerBounds.width - 7;
			innerBounds.x = innerBounds.x + 7; 
			Display display = Display.getCurrent();
			graphics.setForegroundColor(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			graphics.setBackgroundColor(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			// Fill the square rectangle for the inner bounds and also a triangle on its left
			graphics.fillRectangle(innerBounds);
			graphics.fillPolygon(new int[]{
					bounds.getBottomLeft().x,bounds.getBottomLeft().y,
					innerBounds.getTopLeft().x,innerBounds.getTopLeft().y,
					innerBounds.getBottomLeft().x,innerBounds.getBottomLeft().y});
			// Draw some text inside the inner rectangle			
			graphics.drawText(string,innerBounds.x,innerBounds.y);
			// Draw a tab folder shape around the text
			graphics.drawLine(bounds.getBottomRight().translate(-1,0),bounds.getTopRight().translate(-1,0));
			graphics.drawLine(bounds.getTopRight(),innerBounds.getTopLeft());
			graphics.drawLine(innerBounds.getTopLeft(),bounds.getBottomLeft());
		}
	}
	
	class BindingLauncher extends Clickable{
		TabLabel tabLabel;
		BindingLauncher(){
			tabLabel = new TabLabel("Bind...");			
			setContents(tabLabel);
			setSize(37,13);
		}
	}
	private BindingLauncher labelFigure;
	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(containerAdapter);
		
		// Test code
		Layer handleLayer = (Layer)getLayer(LayerConstants.HANDLE_LAYER);
		
		labelFigure = new BindingLauncher();
		getFigure().addAncestorListener(new AncestorListener(){
			public void ancestorAdded(IFigure ancestor) {
				ancestor.toString();
			}
			public void ancestorMoved(IFigure ancestor) {
				Rectangle parentBounds = getFigure().getBounds();
				labelFigure.setLocation(new Point(
						parentBounds.x + parentBounds.width - labelFigure.getSize().width,
						getFigure().getBounds().y-labelFigure.getSize().height));
			}
			public void ancestorRemoved(IFigure ancestor) {
				ancestor.toString();
			}
		});
		handleLayer.add(labelFigure);
		
	}

	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(containerAdapter);
		Layer handleLayer = (Layer)getLayer(LayerConstants.HANDLE_LAYER);
		handleLayer.remove(labelFigure);
	}

	/**
	 * JSplitPane's children are really set as the left or top component and the right or bottom component. If the JSplitPane's orientation is
	 * HORIZONTAL_SPLIT, the components will be set left and right. If the JSplitPane's orientation is VERTICAL_SPLIT, the components will be set top
	 * and bottom. We'll check for both since we don't know the orientation... and even if we did, Sun lets you set the components regardless of what
	 * the orientation is set to. In other words, you can set the left and right components even though the orientation is vertical... and the
	 * opposite is true as well.
	 */
	public List getModelChildren() {
		ArrayList result = new ArrayList();
		Object leftComponent = ((EObject) getModel()).eGet(sfLeftComponent);
		Object topComponent = ((EObject) getModel()).eGet(sfTopComponent);
		Object rightComponent = ((EObject) getModel()).eGet(sfRightComponent);
		Object bottomComponent = ((EObject) getModel()).eGet(sfBottomComponent);
		if (leftComponent != null) {
			result.add(leftComponent);
		} else if (topComponent != null) {
			result.add(topComponent);
		}
		if (rightComponent != null) {
			result.add(rightComponent);
		} else if (bottomComponent != null) {
			result.add(bottomComponent);
		}
		List componentList = getConstraintComponentsModelChildren();
		if (!componentList.isEmpty()) {
			for (int i = 0; i < componentList.size(); i++) {
				result.add(componentList.get(i));
			}
		}
		return result;
	}

	/**
	 * Sun let's you set the children in JSplitPane with just an add(aComponent, "left"), etc., so we have to handle the cases whereas the components
	 * of the JSplitPane are constraint components.
	 */
	protected List getConstraintComponentsModelChildren() {
		// Model children is the components feature.
		// However, this returns the constraint components, but we want to return instead
		// the components themselves. They are the "model" that gets sent to the createChild and
		// component edit part.
		List constraintChildren = (List) ((EObject) getModel()).eGet(sf_containerComponents);
		ArrayList children = new ArrayList(constraintChildren.size());
		Iterator itr = constraintChildren.iterator();
		while (itr.hasNext()) {
			EObject con = (EObject) itr.next();
			IJavaInstance component = (IJavaInstance) con.eGet(sf_constraintComponent);
			// See whether the component is in severe error. If so then exlude if from the list of children
			if (BeanProxyUtilities.getBeanProxyHost(component).getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
				children.add(con.eGet(sf_constraintComponent)); // Get the component out of the constrain
			}
		}
		return children;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		ResourceSet rset = ((EObject) model).eResource().getResourceSet();
		sfLeftComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT);
		sfRightComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT);
		sfBottomComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		sfTopComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT);
		sf_constraintComponent = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sf_containerComponents = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
	}
}