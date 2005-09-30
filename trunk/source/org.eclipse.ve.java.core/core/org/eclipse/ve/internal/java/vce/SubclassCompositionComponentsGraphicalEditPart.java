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
package org.eclipse.ve.internal.java.vce;
/*
 * $RCSfile: SubclassCompositionComponentsGraphicalEditPart.java,v $ $Revision:
 * 1.1 $ $Date: 2005-09-30 17:36:13 $
 */
import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.jcm.BeanSubclassComposition;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.BaseJavaContainerPolicy;
import org.eclipse.ve.internal.java.core.CompositionComponentsGraphicalEditPart;
/**
 * Subclass Composition Graphical Edit Part for a bean subclass.
 */
public class SubclassCompositionComponentsGraphicalEditPart
		extends
			CompositionComponentsGraphicalEditPart
		implements IFreeFormRoot {
	private ActionBarGraphicalEditPart actionBarEditpart = null;
	static final Color VERY_LIGHT_GRAY = new Color(null, 210, 210, 210);

	public SubclassCompositionComponentsGraphicalEditPart(Object model) {
		super(model);
	}
	protected ContainerPolicy getContainerPolicy() {
		return new SubclassCompositionContainerPolicy(EditDomain
				.getEditDomain(this));
	}
	protected List getModelChildren() {
		BeanSubclassComposition comp = (BeanSubclassComposition) getModel();
		if (comp != null) {
			List children = super.getModelChildren();
			if (comp.eIsSet(JCMPackage.eINSTANCE
					.getBeanSubclassComposition_ThisPart())) {
				ArrayList newChildren = new ArrayList(children.size() + 1);
				newChildren.add(comp.getThisPart());
				newChildren.addAll(children);
				return newChildren;
			} else
				return children;
		} else
			return Collections.EMPTY_LIST;
	}
	protected Adapter compositionAdapter = new AdapterImpl() {
		public void notifyChanged(Notification msg) {
			switch (msg.getFeatureID(BeanSubclassComposition.class)) {
				case JCMPackage.BEAN_SUBCLASS_COMPOSITION__THIS_PART :
					if (!msg.isTouch())
						queueRefreshChildren();
					break;
			}
		}
	};
	public void activate() {
		super.activate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().add(
					compositionAdapter);

		// Create the action bar editpart
		if (actionBarEditpart == null) {
			actionBarEditpart = new ActionBarGraphicalEditPart();
			actionBarEditpart.setParent(getRoot());
			IFigure fig = actionBarEditpart.getFigure();	
			getLayer(LayerConstants.HANDLE_LAYER).add(fig);
			actionBarEditpart.addNotify();
			if (!actionBarEditpart.isActive())
				actionBarEditpart.activate();
			EditDomain.getEditDomain(this).setData(ActionBarGraphicalEditPart.class, actionBarEditpart);
		}
	}
	public void deactivate() {
		super.deactivate();
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(
					compositionAdapter);
		
		// Remove the action bar editpart
		if (actionBarEditpart != null) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(actionBarEditpart.getFigure());
			actionBarEditpart.deactivate();
			actionBarEditpart = null;
		}
	}
	public void setModel(Object model) {
		if (getModel() != null)
			((BeanSubclassComposition) getModel()).eAdapters().remove(compositionAdapter);		
		super.setModel(model);
	}
	
	/*
	 * Special editpart for the action bar to host the editpart contributors 
	 */
	public class ActionBarGraphicalEditPart extends AbstractGraphicalEditPart {

		List actionBarChildren = null;

		public void addActionBarChildren(List actionBarChildren) {
			this.actionBarChildren = actionBarChildren;
		}

		protected IFigure createFigure() {
			IFigure actionBarFigure = new RoundedRectangle() {
				int heightReduction = 3;
				protected void fillShape(Graphics graphics) {
					Rectangle rect = getBounds().getCopy().expand(-1, -1);
					rect.height -= heightReduction;
					graphics.fillGradient(rect, false);
				}
				protected void outlineShape(Graphics graphics) {
					graphics.setForegroundColor(ColorConstants.darkGray);
					Rectangle f = Rectangle.SINGLETON;
					Rectangle r = getBounds();
					f.x = r.x + lineWidth / 2;
					f.y = r.y + lineWidth / 2;
					f.width = r.width - lineWidth;
					f.height = r.height - lineWidth - heightReduction;
					graphics.drawRoundRectangle(f, corner.width, corner.height);
					// draw the tail to the fake bubble
					graphics.drawPolyline(new int[] { r.x + 4, r.y + r.height - heightReduction, r.x + 3, r.y + r.height, r.x + 8,
							r.y + r.height - heightReduction});
				}
			};
			FlowLayout fl = new FlowLayout();
			fl.setMajorAlignment(FlowLayout.ALIGN_CENTER);
			fl.setMinorAlignment(FlowLayout.ALIGN_RIGHTBOTTOM);
			actionBarFigure.setLayoutManager(fl);
			actionBarFigure.setForegroundColor(ColorConstants.buttonLightest);
			actionBarFigure.setBackgroundColor(VERY_LIGHT_GRAY);
			actionBarFigure.setVisible(false);
			return actionBarFigure;
		}
		
		protected void createEditPolicies() {
			installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new BaseJavaContainerPolicy(EditDomain.getEditDomain(this))));
		}

		protected void refreshChildren() {
			super.refreshChildren();
			calculateFigureSize();
		}

		private void calculateFigureSize() {
			// Calculate and set action bar size
			List children = getFigure().getChildren();
			if (children.isEmpty()) {
				return;
			}
			int abWidth = 0;
			int abHeight = 0;
			for (int i = 0; i < children.size(); i++) {
				Dimension size = ((IFigure) children.get(i)).getPreferredSize();
				abWidth += size.width + ((FlowLayout) getFigure().getLayoutManager()).getMinorSpacing();
				if (size.height > abHeight)
					abHeight = size.height;
			}
			getFigure().setSize(abWidth + 20, abHeight + 6);
		}

		protected EditPart createChild(Object model) {
			return (EditPart) model;
		}

		protected List getModelChildren() {
			return actionBarChildren == null ? Collections.EMPTY_LIST : actionBarChildren;
		}
	}
}
