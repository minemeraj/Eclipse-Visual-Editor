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
 * 1.1 $ $Date: 2005-10-06 21:59:20 $
 */
import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
	static final Color VERY_LIGHT_YELLOW = new Color(null, 255, 255, 220);
	static final int ACTIONBAR_FIGURE_MARGIN = 5;

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
		IFigure actionBarFigure = null;
		Polyline connectionFigure = null;
		RectangleFigure anchorFigure = null;
		List actionBarChildren = null;

		public void activate() {
			super.activate();
			getLayer(LayerConstants.HANDLE_LAYER).add(getFigure());
		}
		public void deactivate() {
			super.deactivate();
			getLayer(LayerConstants.HANDLE_LAYER).remove(getFigure());
			if (connectionFigure != null) {
				getLayer(LayerConstants.HANDLE_LAYER).remove(connectionFigure);
				getLayer(LayerConstants.HANDLE_LAYER).remove(anchorFigure);
			}
		}
		public void addActionBarChildren(List actionBarChildren) {
			this.actionBarChildren = actionBarChildren;
		}

		public void show(Rectangle hostBounds, int orientation) {
			Rectangle bounds = actionBarFigure.getBounds();
			actionBarFigure.setLocation(new Point(hostBounds.x + hostBounds.width / 2 + 3, hostBounds.y - bounds.height - 7));
			actionBarFigure.setVisible(true);
			if (connectionFigure == null && anchorFigure == null) {
				connectionFigure = new Polyline() {

					protected void outlineShape(Graphics g) {
						try {
							g.setAntialias(SWT.ON);
						} catch (SWTException e) {
							// For OS platforms that don't support Antialias
						}
						super.outlineShape(g);
					}
				};
				connectionFigure.setLineWidth(1);
				anchorFigure = new RectangleFigure();
				anchorFigure.setSize(5, 5);
				anchorFigure.setBackgroundColor(ColorConstants.lightGreen);
				getLayer(LayerConstants.HANDLE_LAYER).add(connectionFigure);
				getLayer(LayerConstants.HANDLE_LAYER).add(anchorFigure);
			}
			PointList pl = new PointList(new int[] { hostBounds.x + hostBounds.width / 5, hostBounds.y + hostBounds.height / 5, bounds.x - 8,
					bounds.y + bounds.height / 2 + 5, bounds.x, bounds.y + bounds.height / 2 + 5});
			connectionFigure.setPoints(pl);
			connectionFigure.setVisible(true);
			anchorFigure.setLocation(new Point(hostBounds.x + hostBounds.width / 5 - 3, hostBounds.y + hostBounds.height / 5 + 1));
			anchorFigure.setVisible(true);
		}
		public void hide() {
			actionBarFigure.setVisible(false);
			connectionFigure.setVisible(false);
			anchorFigure.setVisible(false);
		}
		protected IFigure createFigure() {
			actionBarFigure = new RoundedRectangle() {
				protected void fillShape(Graphics graphics) {
					Rectangle rect = getBounds().getCopy().expand(-1, -1);
					graphics.fillGradient(rect, false);
				}
				protected void outlineShape(Graphics graphics) {
					graphics.setForegroundColor(ColorConstants.darkGray);
					Rectangle f = Rectangle.SINGLETON;
					Rectangle r = getBounds();
					f.x = r.x + lineWidth / 2;
					f.y = r.y + lineWidth / 2;
					f.width = r.width - lineWidth;
					f.height = r.height - lineWidth;
					graphics.drawRoundRectangle(f, corner.width, corner.height);
				}
			};
			// Use XY positioning
			XYLayout xylayout = new XYLayout();
			actionBarFigure.setLayoutManager(xylayout);

			// For the fill gradient, left to right go from white to very light yellow to show depth
			actionBarFigure.setForegroundColor(ColorConstants.buttonLightest);
			actionBarFigure.setBackgroundColor(VERY_LIGHT_YELLOW);

			actionBarFigure.setVisible(false);
			return actionBarFigure;
		}
		
		protected void createEditPolicies() {
			// Need a layout policy in order to provide selection capability on the action edit parts
			installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new BaseJavaContainerPolicy(EditDomain.getEditDomain(this)) {

				protected boolean isValidChild(Object child, EStructuralFeature containmentSF) {
					return false;
				}
			}) {

				protected void showLayoutTargetFeedback(Request request) {
					// Don't do anything... don't want to move around the action bar editparts
				}

				protected boolean isHorizontal() {
					return true;
				}
			});
		}

		protected void refreshChildren() {
			super.refreshChildren();
			setChildrenConstraints();
			calculateFigureSize();
		}

		/*
		 * Calculate and set the action bar size
		 */
		private void calculateFigureSize() {
			List children = getFigure().getChildren();
			if (children.isEmpty()) {
				return;
			}
			int abWidth = ACTIONBAR_FIGURE_MARGIN;
			int abHeight = 0;
			for (int i = 0; i < children.size(); i++) {
				Dimension size = ((IFigure) children.get(i)).getPreferredSize();
				abWidth += size.width + ACTIONBAR_FIGURE_MARGIN;
				if (size.height > abHeight)
					abHeight = size.height;
			}
			getFigure().setSize(abWidth + 25, abHeight + ACTIONBAR_FIGURE_MARGIN*2);
		}
		
		private void setChildrenConstraints() {
			List children = getFigure().getChildren();
			LayoutManager lm = getFigure().getLayoutManager();
			if (children.isEmpty() || lm == null) { return; }
			int abWidth = ACTIONBAR_FIGURE_MARGIN;
			for (int i = 0; i < children.size(); i++) {
				IFigure childFigure = (IFigure) children.get(i);
				if (lm.getConstraint(childFigure) == null)
					getFigure().setConstraint(
							childFigure,
							new Rectangle(abWidth, ACTIONBAR_FIGURE_MARGIN, childFigure.getPreferredSize().width,
									childFigure.getPreferredSize().height));
				abWidth += childFigure.getPreferredSize().width + ACTIONBAR_FIGURE_MARGIN;
			}
		}

		protected EditPart createChild(Object model) {
			return (EditPart) model;
		}

		protected List getModelChildren() {
			return actionBarChildren == null ? Collections.EMPTY_LIST : actionBarChildren;
		}
	}
}
