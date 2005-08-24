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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: HoverFeedbackEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:50 $ 
 */



import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

public class HoverFeedbackEditPolicy extends GraphicalEditPolicy implements FigureListener {
	protected Figure feedbackFigure = null;

public void deactivate(){
	super.deactivate();
	if (feedbackFigure != null)
		stopListening();
}
public void eraseTargetFeedback(Request request){
	if(feedbackFigure!=null)
		stopListening();
}
protected void followFigure(IFigure figure){
	Rectangle bounds = figure.getBounds();
	getSelectionFeedback().setBounds(bounds.getExpanded(1,1));
}
public Command getCommand(Request request){return null;}
public void figureMoved(IFigure figure){
	followFigure(figure);
}
protected IFigure getSelectionFeedback(){
	if (feedbackFigure == null){
		feedbackFigure = new Figure();
		feedbackFigure.setBorder(new LineBorder(ColorConstants.darkBlue,2));
		feedbackFigure.setOpaque(false);
		addFeedback(feedbackFigure);
		startListening();
	}
	return feedbackFigure;
}
public EditPart getTargetEditPart(Request request){
	return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ?
		getHost() : null;
}
protected void showSelectionFeedback(){
	getSelectionFeedback();
}
public void showTargetFeedback(Request request){
	if(request.getType().equals(RequestConstants.REQ_SELECTION_HOVER))
		showSelectionFeedback();
}
protected void startListening(){
	IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
	figure.addFigureListener(this);
	followFigure(figure);
}
protected void stopListening(){
	IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
	figure.removeFigureListener(this);
	removeFeedback(feedbackFigure);
	feedbackFigure = null;
}
}
