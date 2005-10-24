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
 *  $Revision: 1.12 $  $Date: 2005-10-24 15:21:09 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
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
	private IFigure figureOverlayPanel;		// Figure panel to display the figure overlay contributions

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
		// Add tooltip processors for tooltip capability
		ToolTipProcessor [] processors = createToolTipProcessors();
		if (processors != null && processors.length > 0) {
			ToolTipContentHelper contentHelper = new ToolTipContentHelper(processors);
			getFigure().setToolTip(contentHelper);
		}
		List editPartContributorFactories = getEditDomain().getContributors(this);
		if(editPartContributorFactories != null){
			figureOverlayPanel  = new Panel();
			figureOverlayPanel.setLayoutManager(new FlowLayout());
			figureOverlayPanel.getBounds().translate(3, 3);
			((ContentPaneFigure)getFigure()).getContentPane().add(figureOverlayPanel);
			fContributionChangeListener = new EditPartContributionChangeListener () {
				public void contributionChanged(EditPartContributor editpartContributor) {
					refreshContributions();
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
			addContributions();
			if (getFigure().getToolTip() != null && getFigure().getToolTip() instanceof ToolTipContentHelper)
				((ToolTipContentHelper)getFigure().getToolTip()).activate();
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
		if (getFigure().getToolTip() != null && getFigure().getToolTip() instanceof ToolTipContentHelper)
			((ToolTipContentHelper)getFigure().getToolTip()).deactivate();
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
			if (actionBarFigure == me.getSource() || mouseWithinActionBar(me)) {
				mouseInsideActionBar = true;
			}
			if (me.getSource() == getFigure()) {
				mouseInsideControlFigure = true;
			}
			if (mouseInsideActionBar || mouseInsideControlFigure)
				Display.getCurrent().timerExec(500, showActionBarRunnable);
		}

		public void mouseExited(MouseEvent me) {
			if (me.getSource() == getFigure()) {
				mouseInsideControlFigure = false;
			}
			if (!mouseWithinActionBar(me)) {
				mouseInsideActionBar = false;
			}
			if (!mouseInsideActionBar && !mouseInsideControlFigure  && !actionBarEditpartSelected) {
				Display.getCurrent().timerExec(500, hideActionBarRunnable);
			}
		}
		
		public void mouseMoved(MouseEvent me) {
			if (mouseWithinActionBar(me)) {
				highlightActionBarChildren(me);
			}
		}

		/*
		 * Return true if the mouse is moving/hovering in the action bar area
		 */
		private boolean mouseWithinActionBar(MouseEvent me) {
			if (actionBarFigure == null) return false;
			Rectangle bounds = actionBarFigure.getBounds();
			return (bounds.x < me.x && bounds.y < me.y && bounds.x + bounds.width > me.x && bounds.y + bounds.height > me.y);
		}

		/*
		 * If hovering over one of the action bar action editparts, draw a focus rectangle around the child
		 * 
		 * @see org.eclipse.draw2d.MouseMotionListener#mouseHover(org.eclipse.draw2d.MouseEvent)
		 */
		private void highlightActionBarChildren(MouseEvent me) {
			if (actionBarFigure == null) return;
			List children = actionBarEditPart.getChildren();
			for (int i = 0; i < children.size(); i++) {
				if (!(children.get(i) instanceof ActionBarActionEditPart))
					continue;
				IFigure child = ((GraphicalEditPart)children.get(i)).getFigure();
				Rectangle bounds = child.getBounds();
				if (bounds.x < me.x && bounds.y < me.y && bounds.x + bounds.width > me.x && bounds.y + bounds.height > me.y) {
					if (child.getBorder() == null) {
						child.setBorder(new Border() {

							public Insets getInsets(IFigure figure) {
								return new Insets();
							}

							public Dimension getPreferredSize(IFigure figure) {
								return null;
							}

							public boolean isOpaque() {
								return false;
							}

							public void paint(IFigure figure, Graphics graphics, Insets insets) {
								graphics.setForegroundColor(ColorConstants.black);
								Rectangle rect = figure.getBounds().getCopy();
								rect.width -= 1;
								rect.height -= 1;
								graphics.drawRectangle(rect);

							}

						});
						child.repaint();
					}
				}
				else if (child.getBorder() != null) {
					child.setBorder(null);
					child.repaint();
				}
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
				actionBarVisible = false;
				getActionBarEditPart().hide();
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
	 * to the editparts depending on whether they are action editparts or just selectable editparts.
	 */
	class ActionBarEditPartListener extends EditPartListener.Stub {

		public void childAdded(EditPart editpart, int arg1) {
			if (editpart != null) {
				if (editpart instanceof ActionBarActionEditPart) {
					// Listen for when the button is pressed
					((ActionBarActionEditPart)editpart).addActionListener(fActionListener);
					// Listen for mouse movement over this action to highlight it
					((ActionBarActionEditPart)editpart).getFigure().addMouseMotionListener(fActionBarController);
				}
				editpart.addEditPartListener(fActionBarEditPartListener);
			}
		};

		public void removingChild(EditPart child, int index) {
			if (child instanceof ActionBarActionEditPart) {
				((ActionBarActionEditPart)child).removeActionListener(fActionListener);
				((ActionBarActionEditPart)child).getFigure().removeMouseMotionListener(fActionBarController);
			}
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
	 * From the editpart contributors, add the figure overlays, hover overlays, and action bar children
	 */
	private void addContributions() {
		if (fEditPartContributors != null) {
			Iterator iter = fEditPartContributors.iterator();
			figureOverlayCache = new ArrayList();
			hoverOverlayCache = new ArrayList();
			// For tooltip contributions, add a separator between the main tooltip and the contributions
			IFigure toolTipFigure = getFigure().getToolTip();
			if (toolTipFigure instanceof ToolTipContentHelper) {
				ToolTipProcessor separator = new ToolTipProcessor.ToolTipSeparator();
				((ToolTipContentHelper)toolTipFigure).addToolTipProcessor(separator);
				hoverOverlayCache.add(separator);
			}
			while (iter.hasNext()) {
				GraphicalEditPartContributor contrib = (GraphicalEditPartContributor) iter.next();
				// Contribute the figure overlays
				IFigure figOverlay = contrib.getFigureOverLay();
				if (figOverlay != null) {
					figureOverlayPanel.add(figOverlay);
					figureOverlayPanel.setSize(figureOverlayPanel.getPreferredSize());
					figureOverlayCache.add(figOverlay);
				}
				// Contribute the hover overlays
				ToolTipProcessor processor = contrib.getHoverOverLay();
				if (processor != null && toolTipFigure instanceof ToolTipContentHelper) {
					((ToolTipContentHelper)toolTipFigure).addToolTipProcessor(processor);
					hoverOverlayCache.add(processor);
				}
				actionBarChildren = new ArrayList();
				GraphicalEditPart[] children = contrib.getActionBarChildren();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						actionBarChildren.add(children[i]);
					}
				}
			}
			// If we have new contributions to the hover help, refresh the tooltip content helper
			if (hoverOverlayCache != null && toolTipFigure instanceof ToolTipContentHelper)
				((ToolTipContentHelper)toolTipFigure).refresh();
		}
	}
	
	/*
	 * Clear out the figure overlays, hover overlays, and action bar children
	 */
	private void removeContributions() {
		if (fEditPartContributors != null) {
			if (figureOverlayCache != null && figureOverlayPanel != null) {
				Iterator iterator = figureOverlayCache.iterator();
				while (iterator.hasNext())
					figureOverlayPanel.remove((IFigure) iterator.next());
				figureOverlayCache = null;
			}
			if (hoverOverlayCache != null && getFigure().getToolTip() instanceof ToolTipContentHelper) {
				ToolTipContentHelper contentHelper = (ToolTipContentHelper) getFigure().getToolTip();
				Iterator iterator = hoverOverlayCache.iterator();
				while (iterator.hasNext())
					contentHelper.removeToolTipProcessor((ToolTipProcessor) iterator.next());
				hoverOverlayCache = null;
			}
			actionBarChildren = Collections.EMPTY_LIST;
		}
	}
	
	// The contributions have changed... remove the old and re-add the contributions to pickup any changes
	private void refreshContributions() {
		removeContributions();
		addContributions();
	}

	/*
	 * Return array of tooltip processors to be added to the tooltip content helper in activation
	 */
	abstract protected ToolTipProcessor [] createToolTipProcessors ();
	
}
