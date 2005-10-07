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
 *  $RCSfile: CDEAbstractGraphicalEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2005-10-07 16:30:36 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;
/**
 * Base edit part that handles edit part contributors the the graphical edit part.
 * 
 * @since 1.2
 */
public abstract class CDEAbstractGraphicalEditPart extends AbstractGraphicalEditPart {

	protected List fEditPartContributors;
	private ActionBarMouseMotionListener fMouseMotionListener = null;
	private ActionBarEditPartListener fEditPartListener = null;
	private ActionBarFigureListener fHostFigureListener = null;
	private boolean actionBarEditpartSelected = false;

	private void addEditPartContributor(GraphicalEditPartContributor anEditPartContributor) {
		if (fEditPartContributors == null) {
			fEditPartContributors = new ArrayList(1);
		}
		fEditPartContributors.add(anEditPartContributor);
	}
	
	protected EditDomain getEditDomain(){
		return EditDomain.getEditDomain(this);
	}
	
	public void activate() {
		super.activate();
		List editPartContributors = getEditDomain().getContributors(this);
		if(editPartContributors != null){
			Iterator iter = editPartContributors.iterator();
			while(iter.hasNext()){
				GraphicalEditPartContributor graphicalEditPartContributor = ((AdaptableContributorFactory)iter.next()).getGraphicalEditPartContributor(this);
				if(graphicalEditPartContributor != null){
					addEditPartContributor(graphicalEditPartContributor);
				}
			}
		}
		// If there are any graphical editpart contributors, add the figures to the main and tooltip figure
		if (fEditPartContributors != null) {
			Iterator iter = fEditPartContributors.iterator();
			IFigure contentPane = getFigure();
			IFigure toolTipFigure = getFigure().getToolTip();
			while (iter.hasNext()) {
				GraphicalEditPartContributor contrib = (GraphicalEditPartContributor) iter.next();
				IFigure figOverlay = contrib.getFigureOverLay();
				if (figOverlay != null)
					contentPane.add(figOverlay);
				IFigure hoverFig = contrib.getHoverOverLay();
				if (hoverFig != null)
					toolTipFigure.add(hoverFig);
			}
			getFigure().addMouseMotionListener(this.fMouseMotionListener = new ActionBarMouseMotionListener());
			addEditPartListener(fEditPartListener = new ActionBarEditPartListener());
			getFigure().addFigureListener(this.fHostFigureListener = new ActionBarFigureListener());
		}
	
	}
	
	public void deactivate() {
		if (fEditPartContributors != null) {
			for (Iterator itr = fEditPartContributors.iterator(); itr.hasNext();) {
				EditPartContributor contributor = (EditPartContributor) itr.next();
				contributor.dispose();
				
			}
			getFigure().removeMouseMotionListener(this.fMouseMotionListener);
			removeEditPartListener(this.fEditPartListener);
			getFigure().removeFigureListener(fHostFigureListener);

			fEditPartContributors = null;
		}
		super.deactivate();
	}
	/*
	 * Mouse motion listener for the Action Bar
	 * Listens for mouse movement over the host figure and displays action bar editpart and its children
	 */
	private class ActionBarMouseMotionListener extends MouseMotionListener.Stub {
		ActionBarGraphicalEditPart actionBarEditPart = null;
		public List actionBarChildren = Collections.EMPTY_LIST;
		public List actionBarFigures = Collections.EMPTY_LIST;
		IFigure actionBarFigure = null;
		boolean mouseInsideActionBar = false;
		boolean mouseInsideControlFigure = false;
		boolean actionBarVisible = false;

		private ActionBarGraphicalEditPart getActionBarEditPart () {
			if (actionBarEditPart == null)
				actionBarEditPart = (ActionBarGraphicalEditPart) getEditDomain().getData(ActionBarGraphicalEditPart.class);
			return actionBarEditPart;
		}
		public void mouseEntered(MouseEvent me) {
			if (me.getSource() == actionBarFigure || mouseWithinActionBar(me)) {
				mouseInsideActionBar = true;
			}
			if (me.getSource() == getFigure()) {
				mouseInsideControlFigure = true;
			}
			if (mouseInsideActionBar || mouseInsideControlFigure)
				Display.getCurrent().timerExec(1000, showActionBarRunnable);
		}

		public void mouseExited(MouseEvent me) {
			if (me.getSource() == getFigure()) {
				mouseInsideControlFigure = false;
			}
			if (me.getSource() == actionBarFigure && !mouseWithinActionBar(me)) {
				mouseInsideActionBar = false;
			}
			if (!mouseInsideActionBar && !mouseInsideControlFigure  && !actionBarEditpartSelected) {
				Display.getCurrent().timerExec(1000, hideActionBarRunnable);
			}
		}
		private boolean mouseWithinActionBar(MouseEvent me) {
			if (actionBarFigure == null) return false;
			Rectangle bounds = actionBarFigure.getBounds();
			return (bounds.x < me.x && bounds.y < me.y && bounds.x + bounds.width > me.x && bounds.y + bounds.height > me.y);
		}

		public void showActionBar() {
			if (actionBarFigure == null) {
				actionBarFigure = getActionBarEditPart().getFigure();
				populateActionBar();
				if (actionBarChildren != null && !actionBarChildren.isEmpty()) {
					actionBarFigure.addMouseMotionListener(fMouseMotionListener);
					if (!actionBarVisible) {
						getActionBarEditPart().show(getFigure().getBounds().getCopy(), 0);
						actionBarVisible = true;
					}
				}
			}
		}
		/** the <code>Runnable</code> used for showing the action bar with a delay timer */
		public Runnable showActionBarRunnable = new Runnable() {
			public void run() {
				if (mouseInsideActionBar || mouseInsideControlFigure) {
					showActionBar();
				}
			}
		};
		/** the <code>Runnable</code> used for removing the action bar with a delay timer */
		public Runnable hideActionBarRunnable = new Runnable() {
			public void run() {
				if (!mouseInsideActionBar && !mouseInsideControlFigure && !actionBarEditpartSelected) {
					hideActionBar();
				}
			}
		};
		public void hideActionBar() {
			if (actionBarVisible) {
				getActionBarEditPart().hide();
				actionBarVisible = false;
				actionBarFigure.removeMouseMotionListener(fMouseMotionListener);
				getActionBarEditPart().removeEditPartListener(fEditPartListener);
				for (int i = 0; i < actionBarChildren.size(); i++) {
					((EditPart)actionBarChildren.get(i)).removeEditPartListener(fEditPartListener);
				}
				getActionBarEditPart().addActionBarChildren((Collections.EMPTY_LIST));
				getActionBarEditPart().refresh();
				actionBarFigure = null;
				actionBarEditPart = null; // clear the cache of this so we can re-get the next time we need it
			}
		}
		private void populateActionBar() {
			if (actionBarChildren.isEmpty()) {
				actionBarChildren = new ArrayList();
				actionBarFigures = new ArrayList();
				Iterator iter = fEditPartContributors.iterator();
				while (iter.hasNext()) {
					GraphicalEditPartContributor contrib = (GraphicalEditPartContributor) iter.next();
					GraphicalEditPart [] children = contrib.getActionBarChildren();
					if (children != null) {
						for (int i = 0; i < children.length; i++) {
							actionBarChildren.add(children[i]);
							actionBarFigures.add(children[i].getFigure());
						}
					}
				}
			}
			if (!actionBarChildren.isEmpty()) {
				getActionBarEditPart().addEditPartListener(fEditPartListener);
				getActionBarEditPart().addActionBarChildren(actionBarChildren);
				getActionBarEditPart().refresh();
			}
		}
	}

	/*
	 * Editpart listener that listens for selection of action bar editparts
	 */
	class ActionBarEditPartListener extends EditPartListener.Stub {

		public void childAdded(EditPart editpart, int arg1) {
			if (editpart != null) {
				editpart.addEditPartListener(fEditPartListener);
			}
		};

		public void removingChild(EditPart child, int index) {
			child.removeEditPartListener(fEditPartListener);
			super.removingChild(child, index);
		}

		public void selectedStateChanged(EditPart part) {
			if (part != null && part.getSelected() == EditPart.SELECTED_PRIMARY
					&& (fMouseMotionListener.actionBarChildren.contains(part) || part == CDEAbstractGraphicalEditPart.this)) {
				Display.getCurrent().asyncExec(fMouseMotionListener.showActionBarRunnable);
				if (fMouseMotionListener.actionBarChildren.contains(part))
					actionBarEditpartSelected = true;
			} else {
				actionBarEditpartSelected = false;
				Display.getCurrent().asyncExec(fMouseMotionListener.hideActionBarRunnable);
			}
		}
	};
	/*
	 * Listens for figure movements of the host figure so the action bar can be moved to a relative location
	 * to the host figure.
	 */
	class ActionBarFigureListener implements FigureListener {

		public void figureMoved(IFigure source) {
			fMouseMotionListener.hideActionBar();
			Display.getCurrent().asyncExec(fMouseMotionListener.showActionBarRunnable);
		}
		
	}
}
