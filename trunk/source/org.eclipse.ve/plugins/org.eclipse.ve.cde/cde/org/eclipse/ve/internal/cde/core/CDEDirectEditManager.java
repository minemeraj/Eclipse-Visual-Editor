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
 *  $Revision: 1.3 $  $Date: 2005-06-15 20:19:34 $ 
 */
package org.eclipse.ve.internal.cde.core;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public abstract class CDEDirectEditManager extends DirectEditManager {

	private Font scaledFont;
	private IPropertyDescriptor sfProperty;

	public CDEDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, IPropertyDescriptor property) {
		super(source, editorType, locator);
		sfProperty = property;
	}
	
	protected DirectEditRequest createDirectEditRequest() {
		DirectEditRequest req = super.createDirectEditRequest();
		req.setDirectEditFeature(sfProperty);
		return req;
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
		IFigure figure = getEditPart().getFigure();
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
	 * Gets the string value of the property specified by the property.
	 * @param property
	 * @return a String value 
	 * 
	 * @since 1.1.0
	 */
	protected abstract String getPropertyValue(IPropertyDescriptor property);

}
