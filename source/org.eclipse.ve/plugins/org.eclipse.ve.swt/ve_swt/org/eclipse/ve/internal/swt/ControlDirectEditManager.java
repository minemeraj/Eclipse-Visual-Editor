/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: ControlDirectEditManager.java,v $
 *  $Revision: 1.2 $  $Date: 2004-08-27 15:35:50 $
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IStringBeanProxy;

import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.IBeanProxyHost;

public class ControlDirectEditManager extends DirectEditManager {

	private Font scaledFont;
	private EStructuralFeature sfProperty;

	public ControlDirectEditManager(
		ControlGraphicalEditPart source,
		Class editorType,
		CellEditorLocator locator,
		EStructuralFeature property) {
		super(source, editorType, locator);
		sfProperty = property;
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown() {
		//This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown();
		if (disposeFont != null)
			disposeFont.dispose();
	}

	protected void initCellEditor() {
		String initialText = ""; //$NON-NLS-1$

		// retrieve the property's value from the model
		IJavaObjectInstance component = (IJavaObjectInstance) getEditPart().getModel();
		if (component.eIsSet(sfProperty)) {
			IJavaObjectInstance textObj = (IJavaObjectInstance) component.eGet(sfProperty);
			if (textObj != null) {
				// Get the value from the remote vm of the externalized string
				try {
					IBeanProxyHost host = BeanProxyUtilities.getBeanProxyHost(component);
					IBeanProxy propProxy = host.getBeanPropertyProxyValue(sfProperty);
					initialText = ((IStringBeanProxy) propProxy).stringValue();
				} catch (Exception e) {
				}
			}
		}
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
}
