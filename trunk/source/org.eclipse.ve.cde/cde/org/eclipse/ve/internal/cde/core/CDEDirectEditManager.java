/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDEDirectEditManager.java,v $
 *  $Revision: 1.1 $  $Date: 2005-03-21 22:48:08 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;

public abstract class CDEDirectEditManager extends DirectEditManager {

	private Font scaledFont;
	private EStructuralFeature sfProperty;

	public CDEDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, EStructuralFeature property) {
		super(source, editorType, locator);
		sfProperty = property;
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown() {
		// This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown();
		if (disposeFont != null)
			disposeFont.dispose();
	}

	protected void initCellEditor() {
		String initialText = ""; //$NON-NLS-1$
		if (sfProperty != null)
			initialText = getPropertyValue(sfProperty);
		getCellEditor().setValue(initialText);
		Text text = (Text) getCellEditor().getControl();
		IFigure figure = ((GraphicalEditPart) getEditPart()).getFigure();
		scaledFont = figure.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());
		getEditPart().getFigure().translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		scaledFont = new Font(null, data);

		text.setFont(scaledFont);
		text.selectAll();

	}

	/**
	 * <P>
	 * Gets the string value of the property specified by the structure feature sfProperty.
	 * @param sfProperty
	 * @return a String value 
	 * 
	 * @since 1.1.0
	 */
	protected abstract String getPropertyValue(EStructuralFeature sfProperty);

}
