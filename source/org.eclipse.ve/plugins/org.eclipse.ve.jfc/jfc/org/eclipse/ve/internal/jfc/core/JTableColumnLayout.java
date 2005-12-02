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
 *  $RCSfile: JTableColumnLayout.java,v $
 *  $Revision: 1.4 $  $Date: 2005-12-02 18:41:28 $ 
 */

import org.eclipse.draw2d.*;
import java.util.*;
import org.eclipse.draw2d.geometry.*;

/**
 * This class implements the <pre>ILayoutManager</pre> interface 
 * using the XY Layout algorithm. This lays out the components 
 * using the layout constraints as defined by each component.
 */

public class JTableColumnLayout extends AbstractLayout
{
protected Map constraints = new HashMap();

/**
 * Sets the layout constraint of the given figure.
 * The constraints can only be of type Integer which is the width.
 */
public void setConstraint(IFigure figure, Object newConstraint) {
	super.setConstraint(figure, newConstraint);
	if (newConstraint != null)
		constraints.put(figure, newConstraint);
}

/**
 * Calculates and returns the preferred size of the input figure.
 * Since in XYLayout the location of the child should be preserved,
 * the preferred size would be a region which would hold all the
 * children of the input figure. If no constraint is set, that
 * child is ignored for calculation. If width and height are not
 * positive, the preferred dimensions of the child are taken.
 * 
 * @param f  Figure for which the preferred size is required.
 * @return  Preferred size of the input figure.
 */
protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint){
	Dimension dim = new Dimension(0,f.getBounds().height);
	ListIterator children = f.getChildren().listIterator();
	while (children.hasNext()){
		IFigure child = (IFigure)children.next();
		Integer i = (Integer)constraints.get(child);
		if (i == null)
			continue;
		dim.width = dim.width + i.intValue();
	}
	Insets insets = f.getInsets();	
	return new Dimension(dim.width + insets.getWidth(), dim.height + insets.getHeight()).
		union(getBorderPreferredSize(f));
}

/**
 * Implements the algorithm to layout the components of the given container figure.
 * Each component is laid out using it's own layout constraint specifying it's width
 */
public void layout(IFigure parent) {
	Iterator children = parent.getChildren().iterator();
	IFigure f;
	Rectangle bounds = new Rectangle(0,0,0,parent.getBounds().height);
	while (children.hasNext()) {
		f = (IFigure)children.next();
		Integer widthI  = (Integer) getConstraint(f);
		if (widthI == null) continue;
		int width = widthI.intValue();
		bounds.width = bounds.width + width;
		f.setBounds(bounds.getCopy());
		bounds.x = bounds.x + width;
	}
}

/**
 * Returns the layout constraint for the given figure.
 * If no constraint has been set for the receiver so far,
 * set the default constraints based on the figure.
 *
 * @param figure   Figure whose constraint is desired.
 * @see  #setConstraint(IFigure, Object)
 */
public Object getConstraint(IFigure figure) {
	return constraints.get(figure);
}

/**
 * Removes the input figure from the list of constraints.
 *
 * @param figure  Figure which should be removed from the list. 
 */
public void remove(IFigure figure){
	super.remove(figure);
	constraints.remove(figure);
}

}
