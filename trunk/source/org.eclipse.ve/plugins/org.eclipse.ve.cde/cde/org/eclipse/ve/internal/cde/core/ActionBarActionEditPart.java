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
 *  $RCSfile: ActionBarActionEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-17 21:55:16 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;

/**
 * Editpart that is a child of the Action Bar used for performing an action when pressed.
 * 
 * @since 1.2.0
 */
public abstract class ActionBarActionEditPart extends AbstractGraphicalEditPart {

	static final Image DEFAULT_IMAGE = CDEPlugin.getImageFromPlugin(CDEPlugin.getPlugin(), "images/somepart.gif");
	protected String fToolTip;
	protected Image fIcon = DEFAULT_IMAGE;
	protected List fActionListeners = null;

	public ActionBarActionEditPart() {
		super();
	}

	public ActionBarActionEditPart(String tip) {
		super();
		fToolTip = tip;
	}

	public ActionBarActionEditPart(Image icon) {
		super();
		fIcon = icon;
	}

	public ActionBarActionEditPart(Image icon, String toolTip) {
		fIcon = icon;
		fToolTip = toolTip;
	}

	public void addActionListener(ActionListener listener) {
		if (fActionListeners == null)
			fActionListeners = new ArrayList();
		if (fActionListeners.indexOf(listener) == -1)
			fActionListeners.add(listener);
	}
	public void removeActionListener(ActionListener listener) {
		if (fActionListeners != null && !fActionListeners.isEmpty() && fActionListeners.indexOf(listener) != -1)
			fActionListeners.remove(listener);
	}
	/*
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		final Label fig = new Label(fIcon);
		if (fToolTip != null)
			fig.setToolTip(new Label(fToolTip));
		fig.addMouseListener(new MouseListener.Stub() {
			// When mouse is pressed over this edit part, execute run() and notify action listeners
			public void mousePressed(MouseEvent me) {
				if (me.getSource() == fig) {
					run();
					handleActionPerformed(me.getSource());
				}
			}
		});
		return fig;
	}

	private void handleActionPerformed(Object source) {
		Iterator iter = fActionListeners.iterator();
		while (iter.hasNext()) {
			((ActionListener) iter.next()).actionPerformed(new ActionEvent(source, "Action performed on action edit part"));
		}
		
	}

	protected void createEditPolicies() {
	}

	/*
	 * Called from the figure's action listener when actionPerformed is called.
	 * 
	 * Subclasses to provide action to perform when this editpart is selected via mouse pressed event
	 */
	public abstract void run();

	public void setIcon(Image icon) {
		fIcon = icon;
	}

	public void setToolTip(String toolTip) {
		fToolTip = toolTip;
	}

}
