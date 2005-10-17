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
 *  $Revision: 1.7 $  $Date: 2005-10-17 21:55:16 $ 
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
	private ActionBarController fActionBarController = null;
	private ActionBarEditPartListener fActionBarEditPartListener = null;
	private ActionBarFigureListener fHostFigureListener = null;
	private ActionBarActionListener fActionListener = null;
	private EditPartContributionChangeListener fContributionChangeListener;
	private boolean actionBarEditpartSelected = false;
	private List actionBarChildren = Collections.EMPTY_LIST;
	private List figureOverlayCache;
	private List hoverOverlayCache;

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
		List editPartContributorFactories = getEditDomain().getContributors(this);
		if(editPartContributorFactories != null){
			fContributionChangeListener = new EditPartContributionChangeListener () {
				public void contributionChanged(EditPartContributor editpartContributor) {
					refreshContributors();
				}
			};
			Iterator iter = editPartContributorFactories.iterator();
			while(iter.hasNext()){
				GraphicalEditPartContributor graphicalEditPartContributor = ((AdaptableContributorFactory)iter.next()).getGraphicalEditPartContributor(this);
				if(graphicalEditPartContributor != null){
					addEditPartContributor(graphicalEditPartContributor);
					graphicalEditPartContributor.addContributionChangeListener(fContributionChangeListener);
				}
			}
			getFigure().addMouseMotionListener(this.fActionBarController = new ActionBarController());
			addEditPartListener(fActionBarEditPartListener = new ActionBarEditPartListener());
			getFigure().addFigureListener(this.fHostFigureListener = new ActionBarFigureListener());
			fActionListener = new ActionBarActionListener();
			addContributors();
		}
	}
	
	public void deactivate() {
		if (fEditPartContributors != null) {
			for (Iterator itr = fEditPartContributors.iterator(); itr.hasNext();) {
				EditPartContributor contributor = (EditPartContributor) itr.next();
				contributor.dispose();
				
			}
			getFigure().removeMouseMotionListener(this.fActionBarController);
			removeEditPartListener(this.fActionBarEditPartListener);
			getFigure().removeFigureListener(fHostFigureListener);

			fEditPartContributors = null;
		}
		super.deactivate();
	}
	/*
	 * Action Bar Controller
	 * Listens for mouse movement over the host figure and displays the action bar editpart and its children
	 */
	private class ActionBarController extends MouseMotionListener.Stub {
		ActionBarGraphicalEditPart actionBarEditPart = null;
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
//				System.out.println("mouse entered ActionBarFigure");
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
			if (!mouseWithinActionBar(me)) {
				mouseInsideActionBar = false;
//				System.out.println("mouse exited ActionBarFigure");
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

		private void highlightActionBarChildren(MouseEvent me) {
			if (actionBarFigure == null) return;
			List children = actionBarEditPart.getChildren();
			for (int i = 0; i < children.size(); i++) {
				IFigure child = ((GraphicalEditPart)children.get(i)).getFigure();
				Rectangle bounds = child.getBounds();
				if (bounds.x < me.x && bounds.y < me.y && bounds.x + bounds.width > me.x && bounds.y + bounds.height > me.y) {
					child.setBorder(new LineBorder(ColorConstants.darkGreen));
					child.repaint();
				}
				else if (child.getBorder() != null) {
					child.setBorder(null);
					child.repaint();
				}
			}
		}
		/*
		 * If hovering over one of the action bar editparts, draw a focus rectangle around the child
		 * 
		 * @see org.eclipse.draw2d.MouseMotionListener#mouseHover(org.eclipse.draw2d.MouseEvent)
		 */
		public void mouseMoved(MouseEvent me) {
			if (mouseWithinActionBar(me)) {
				highlightActionBarChildren(me);
			}
		}
		
		public void showActionBar() {
			if (actionBarFigure == null) {
				actionBarFigure = getActionBarEditPart().getFigure();
				populateActionBar();
				if (actionBarChildren != null && !actionBarChildren.isEmpty()) {
					actionBarFigure.addMouseMotionListener(fActionBarController);
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
			mouseInsideActionBar = false;
			if (actionBarVisible) {
				actionBarFigure.removeMouseMotionListener(fActionBarController);
				getActionBarEditPart().removeEditPartListener(fActionBarEditPartListener);
				for (int i = 0; i < actionBarChildren.size(); i++) {
					((EditPart)actionBarChildren.get(i)).removeEditPartListener(fActionBarEditPartListener);
				}
				getActionBarEditPart().hide();
				actionBarVisible = false;
				mouseInsideActionBar = false;
				getActionBarEditPart().addActionBarChildren((Collections.EMPTY_LIST));
				getActionBarEditPart().refresh();
				actionBarFigure = null;
				actionBarEditPart = null; // clear the cache of this so we can re-get the next time we need it
			}
		}
		private void populateActionBar() {
			if (!actionBarChildren.isEmpty()) {
				getActionBarEditPart().addEditPartListener(fActionBarEditPartListener);
				getActionBarEditPart().addActionBarChildren(actionBarChildren);
				getActionBarEditPart().refresh();
			}
		}
	}

	/*
	 * Editpart listener that listens for selection of action bar editparts and adds listeners
	 * to the editparts depending on whether they action editparts or just selectable editparts.
	 */
	class ActionBarEditPartListener extends EditPartListener.Stub {

		public void childAdded(EditPart editpart, int arg1) {
			if (editpart != null) {
				if (editpart instanceof ActionBarActionEditPart)
					((ActionBarActionEditPart)editpart).addActionListener(fActionListener);
				editpart.addEditPartListener(fActionBarEditPartListener);
			}
		};

		public void removingChild(EditPart child, int index) {
			if (child instanceof ActionBarActionEditPart)
				((ActionBarActionEditPart)child).removeActionListener(fActionListener);
			child.removeEditPartListener(fActionBarEditPartListener);
		}

		public void selectedStateChanged(EditPart part) {
			if (part != null && part.getSelected() == EditPart.SELECTED_PRIMARY
					&& (actionBarChildren.contains(part) || part == CDEAbstractGraphicalEditPart.this)) {
				Display.getCurrent().asyncExec(fActionBarController.showActionBarRunnable);
				if (actionBarChildren.contains(part))
					actionBarEditpartSelected = true;
			} else {
				actionBarEditpartSelected = false;
				Display.getCurrent().asyncExec(fActionBarController.hideActionBarRunnable);
			}
		}
	};
	/*
	 * Listens for figure movements of the host figure so the action bar can be moved to a relative location
	 * to the host figure.
	 */
	class ActionBarFigureListener implements FigureListener {

		public void figureMoved(IFigure source) {
			fActionBarController.hideActionBar();
			Display.getCurrent().asyncExec(fActionBarController.showActionBarRunnable);
		}
		
	}

	/*
	 * Listens for when the mouse is pressed on the action type edit parts.
	 * When this occurs the action bar should be dismissed.
	 */
	class ActionBarActionListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			fActionBarController.hideActionBar();
		}
		
	}
	public void refresh() {
		super.refresh();
	}

	/*
	 * From the editpart contributores, add the figure overlays, hover overlays, and action bar children
	 */
	private void addContributors() {
		if (fEditPartContributors != null) {
			Iterator iter = fEditPartContributors.iterator();
			figureOverlayCache = new ArrayList();
			hoverOverlayCache = new ArrayList();
			IFigure contentPane = getFigure();
			IFigure toolTipFigure = getFigure().getToolTip();
			while (iter.hasNext()) {
				GraphicalEditPartContributor contrib = (GraphicalEditPartContributor) iter.next();
				// Contribute the figure overlay
				IFigure figOverlay = contrib.getFigureOverLay();
				if (figOverlay != null) {
					contentPane.add(figOverlay);
					figureOverlayCache.add(figOverlay);
				}
				// Contribute the hover overlay
				IFigure hoverFig = contrib.getHoverOverLay();
				if (hoverFig != null) {
					toolTipFigure.add(hoverFig);
					hoverOverlayCache.add(hoverFig);
				}
				actionBarChildren = new ArrayList();
				GraphicalEditPart[] children = contrib.getActionBarChildren();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						actionBarChildren.add(children[i]);
					}
				}
			}
		}
	}
	
	/*
	 * Clear out the figure overlays, hover overlays, and action bar children
	 */
	private void removeContributors() {
		if (fEditPartContributors != null) {
			IFigure fig = getFigure();
			if (figureOverlayCache != null) {
				Iterator iterator = figureOverlayCache.iterator();
				while (iterator.hasNext())
					fig.remove((IFigure) iterator.next());
				figureOverlayCache = null;
			}
			if (hoverOverlayCache != null) {
				fig = getFigure().getToolTip();
				Iterator iterator = hoverOverlayCache.iterator();
				while (iterator.hasNext())
					fig.remove((IFigure) iterator.next());
				hoverOverlayCache = null;
			}
			actionBarChildren = Collections.EMPTY_LIST;
		}
	}
	
	private void refreshContributors() {
		removeContributors();
		addContributors();
	}
}
