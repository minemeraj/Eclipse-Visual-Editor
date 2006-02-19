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
/*
 *  $RCSfile: CardLayoutEditPolicy.java,v $
 *  $Revision: 1.12 $  $Date: 2006-02-19 14:09:11 $ 
 */
import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IMethodProxy;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;

/**
 * Edit policy for handling a java.awt.CardLayout in the Graph viewer.
 * 
 * Since we can't really select the individual cards on container that uses
 * this layout, we depend on the tree editpart in the outline viewer for
 * selecting the cards or components on those cards. 
 * 
 * This edit policy is added as an edit part listener to it's host (the container)
 * to be notified when a card is selected. 
 * When this occurs, the previously viewed card figures are hidden and the 
 * currently selected card is made visible. If we didn't hide the editparts 
 * from the other pages, the figure rectangle outlines will "bleed" through 
 * and show on the top card.
 * It also has an internal CardLayoutComponentListener that implements IVisualComponentListener
 * which is added to the proxy adapter to listen for component refreshes on the cards only,
 * not it's children. This is to re-select the card and force a refresh when the
 * constraint is changed. Without the re
 */
public class CardLayoutEditPolicy extends ConstrainedLayoutEditPolicy implements IActionFilter {
	
	public final static String LAYOUT_ID = "java.awt.CardLayout"; //$NON-NLS-1$
	
	private EditPartListener cardListener;
	protected CardLayoutPolicyHelper helper = new CardLayoutPolicyHelper();
	protected VisualContainerPolicy containerPolicy;		// Handles the containment functions
	protected EditPart fSelectedCard;
	protected EReference sfConstraintConstraint, sfConstraintComponent, sfName;
	protected CardLayoutComponentListener fComponentListener;

	public CardLayoutEditPolicy(VisualContainerPolicy containerPolicy) {
		this.containerPolicy = containerPolicy;
		helper.setContainerPolicy(containerPolicy);
	}
	
	public void activate() {
		containerPolicy.setContainer(getHost().getModel());
		sfConstraintConstraint =
			JavaInstantiation.getReference(
				(IJavaObjectInstance) getHost().getModel(),
				JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		sfConstraintComponent =
			JavaInstantiation.getReference(
				(IJavaObjectInstance) getHost().getModel(),
				JFCConstants.SF_CONSTRAINT_COMPONENT);
		sfName =
			JavaInstantiation.getReference(
				(IJavaObjectInstance) getHost().getModel(),
				JFCConstants.SF_COMPONENT_NAME);			
				
		super.activate();
		/*
		 * If there are children, spawn a separate thread to process through
		 * the children, adding the card and component listener, selecting each one, and making it invisible. 
		 * Only the first card is made visible.
		 */
		if (!getHost().getChildren().isEmpty()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					List children = getHost().getChildren();
					for (int i = 0; i < children.size(); i++) {
						EditPart child = (EditPart) children.get(i);
						addCardListenerToChildren(child);
						addComponentListener(child);
						if (i > 0 ) {
							setCardVisible(child, false);
						}
					}
					GraphicalEditPart ep = (GraphicalEditPart) getHost().getChildren().get(0);
					setCardVisible(ep, true);
					selectCard(ep);
				}
			});
		}
		CustomizeLayoutWindowAction.addLayoutCustomizationPage(getHost().getViewer(), CardLayoutLayoutPage.class);
	}
	
	protected void addCardListenerToChildren(EditPart ep) {
		ep.addEditPartListener(cardListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			addCardListenerToChildren((EditPart) childen.next());
	}
	/* Add a listener to the visual component, the visual component is retrieved through the 
	 * getAdapter interface from the editpart. Refresh the editpart when the card
	 * component has been refreshed.
	 */
	protected void addComponentListener(EditPart child) {
		if (child.getParent() == getHost()) {
			if (child instanceof ComponentGraphicalEditPart) {
				IVisualComponent visualComponent =
					(IVisualComponent) ((ComponentGraphicalEditPart) child).getAdapter(IVisualComponent.class);
				if (visualComponent != null) {
					visualComponent.addComponentListener(getCardLayoutComponentListener());
				}
			}
		}
	}
	
	/*
	 * @see ConstrainedLayoutEditPolicy#getAddCommand(Request generic)
	 */
	protected Command getAddCommand(Request generic){
		ChangeBoundsRequest request = (ChangeBoundsRequest)generic;
		List editParts = request.getEditParts();
		List childrenComponents = new ArrayList(editParts.size());
		Iterator iter = editParts.iterator();
		while (iter.hasNext())
			childrenComponents.add(((EditPart) iter.next()).getModel());
		List constraints = helper.getDefaultConstraint(childrenComponents);
		// Graphically we always add at the end.
		return helper.getAddChildrenCommand(childrenComponents, constraints, null).getCommand();
	}

	/*
	 * Create an EditPartListener for itself and its cards so it knows 
	 * when a card has been added, removed, or selected
	 */
	protected EditPartListener createListener() {
		cardListener = new EditPartListener.Stub() {
			public void childAdded(EditPart child, int index) {
				decorateChild(child);
				addCardListenerToChildren(child);
				addComponentListener(child);
//				setCardVisible(child, true);
//				selectCard(child);
			}
			public void removingChild(EditPart child, int index) {
				removeCardListenerFromChildren(child);
				removeComponentListener(child);
				if (!getHost().getChildren().isEmpty()) {
					EditPart card = (EditPart) getHost().getChildren().get(0);
					setCardVisible(card, true);
					selectCard(card);
				}
			}
			public void selectedStateChanged(EditPart editpart) {
				if (editpart == null || editpart == getHost())
					return;
				// Find the card where this editpart resides and bring the card to the
				// front if isn't already.
				if ((editpart != null)
					&& (editpart.getSelected() == EditPart.SELECTED
						|| editpart.getSelected() == EditPart.SELECTED_PRIMARY)) {
					EditPart card = getCardOfSelectedEditpart(editpart);
					if (card != null && card != fSelectedCard) {
						setCardVisible(fSelectedCard, false);
						BeanAwtUtilities.hideGrids(fSelectedCard);
						setCardVisible(card, true);
						selectCard(card);
					}
				}
			}
		};
		return cardListener;
	}
	protected CardLayoutComponentListener getCardLayoutComponentListener() {
		if (fComponentListener == null)
			fComponentListener = new CardLayoutComponentListener();
		return fComponentListener;
	}
	/* This listener is added to the visual component, which is retrieved through the 
	 * getAdapter interface from the editpart. Refresh the editpart when the card
	 * component has been refreshed.
	 */
	protected class CardLayoutComponentListener extends VisualComponentAdapter {
		
		public void componentRefreshed() {
			selectCard(fSelectedCard);
		};
	}
	/*
	 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart, Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return null;
	}
	public void deactivate() {
		Iterator children = getHost().getChildren().iterator();
		while (children.hasNext()) {
			GraphicalEditPart child = (GraphicalEditPart) children.next();
			removeCardListenerFromChildren(child);
			removeComponentListener(child);
			// We must set the visibility back otherwise the figure will not be selectable in the new layout manager - Bugzilla 58447
			setCardVisible(child, true);				
		}
		super.deactivate();
	}
	protected void removeCardListenerFromChildren(EditPart ep) {
		ep.removeEditPartListener(cardListener);
		Iterator childen = ep.getChildren().iterator();
		while (childen.hasNext())
			removeCardListenerFromChildren((EditPart) childen.next());
	}
	/*
	 * If the parent of this editpart is the host container, we're on the card.
	 * If not, recursely call up through the parent chain until we find the 
	 * editpart (card) that the original editpart was found in.
	 */
	protected EditPart getCardOfSelectedEditpart(EditPart ep) {
		EditPart container = getHost();
		if (ep == null || ep.getParent() == container)
			return ep;
		return getCardOfSelectedEditpart(ep.getParent());
	}

	/*
	 * @see ConstrainedLayoutEditPolicy#getConstraintFor(Point)
	 */
	protected Object getConstraintFor(Point point) {
		return null;
	}

	/*
	 * @see ConstrainedLayoutEditPolicy#getConstraintFor(Rectangle)
	 */
	protected Object getConstraintFor(Rectangle rect) {
		return null;
	}

	/*
	 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Object child = request.getNewObject();
		
		List constraints = helper.getDefaultConstraint(Collections.singletonList(child));
		
		return helper.getCreateChildCommand(child, constraints.get(0), null).getCommand();
	}

	/*
	 * @see LayoutEditPolicy#createChildEditPolicy(EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new NonResizableEditPolicy();
	}
	/*
	 * @see LayoutEditPolicy#getDeleteDependantCommand(Request)
	 */
	protected Command getDeleteDependantCommand(Request aRequest) {
		Command deleteContributionCmd = containerPolicy.getCommand(aRequest);
		if ( deleteContributionCmd == null )
			return UnexecutableCommand.INSTANCE;	// It can't be deleted
	
		// Note: If there is any annotation, that will be deleted too by the
		// container policy, and that will then also delete all of the view info.
		// So we don't need to handle viewinfo here.
			
		return deleteContributionCmd;		
	}
	protected Command getOrphanChildrenCommand(Request aRequest) {
		return helper.getOrphanChildrenCommand(ContainerPolicy.getChildren((GroupRequest) aRequest)).getCommand();
	} 
	/* Add a listener to the visual component, the visual component is retrieved through the 
	 * getAdapter interface from the editpart. Refresh the editpart when the card
	 * component has been refreshed.
	 */
	protected void removeComponentListener(EditPart child) {
		if (child.getParent() == getHost()) {
			if (child instanceof ComponentGraphicalEditPart) {
				IVisualComponent visualComponent =
					(IVisualComponent) ((ComponentGraphicalEditPart) child).getAdapter(IVisualComponent.class);
				if (visualComponent != null) {
					visualComponent.removeComponentListener(getCardLayoutComponentListener());
				}
			}
		}
	}
	/*
	 * The selected card of this container has changed. Bring this card to the front.
	 */
	protected void selectCard(EditPart card) {
		fSelectedCard = card; // save for later checks... see createCardListener()
		if (fSelectedCard == null) return;
		// need to get the proxyhost for this container and execute a show method
		// on the card that is selected.

		IJavaObjectInstance component = (IJavaObjectInstance) card.getModel();
		IBeanProxyHost componentProxy = BeanProxyUtilities.getBeanProxyHost(component);
		if (!componentProxy.isBeanProxyInstantiated())
			return;	// component not valid for some reason.
			
		IBeanProxyHost containerProxyHost =
			BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getHost().getModel());
		IBeanProxy containerProxy = containerProxyHost.getBeanProxy();
		EObject constraintComponent = InverseMaintenanceAdapter.getFirstReferencedBy(component, sfConstraintComponent);
		IJavaInstance javaStringConstraint = null;
		if (constraintComponent.eIsSet(sfConstraintConstraint)) {
			javaStringConstraint = (IJavaInstance) constraintComponent.eGet(sfConstraintConstraint);
		} else {
			// If the constraint is not set, use the components name.
			IBeanProxyHost componentProxyHost =	BeanProxyUtilities.getBeanProxyHost(component);
			javaStringConstraint = componentProxyHost.getBeanPropertyValue(sfName);
		}
		IBeanProxy layoutProxy = BeanAwtUtilities.invoke_getLayout(containerProxy);
		// Slight possibility that we are in the process of switching layout managers.
		if (layoutProxy != null) {
			IMethodProxy showMethodProxy = layoutProxy.getTypeProxy().getMethodProxy("show", //$NON-NLS-1$
					new String[] { "java.awt.Container", "java.lang.String"}); //$NON-NLS-1$ //$NON-NLS-2$
			if (showMethodProxy != null && javaStringConstraint != null) {
				showMethodProxy.invokeCatchThrowableExceptions(layoutProxy, new IBeanProxy[] { containerProxy,
						BeanProxyUtilities.getBeanProxy(javaStringConstraint)});
			}
			((ContainerProxyAdapter) containerProxyHost).revalidateBeanProxy();
		}
	}
	/*
	 * This card has been selected or deselected. 
	 * Make it and all it's children visible or invisible.
	 */
	protected void setCardVisible(EditPart card, boolean bool) {
		if (card != null) {
			((GraphicalEditPart) card).getFigure().setVisible(bool);
			IFigure fig = ((GraphicalEditPart) card).getFigure();
			Iterator children = card.getChildren().iterator();
			while (children.hasNext())
				 ((GraphicalEditPart) children.next()).getFigure().setVisible(bool);
			fig.revalidate();
		}
	}
	/**
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(EditPart, Object)
	 */
	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;	// Never called. We override getAddCommand.
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object, java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (name.startsWith(CustomizeLayoutPage.LAYOUT_POLICY_KEY) && value.equals(LAYOUT_ID))
			return true;
		
		return false;
	}
}
