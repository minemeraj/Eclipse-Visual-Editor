package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTableGraphicalEditPart.java,v $
 *  $Revision: 1.6 $  $Date: 2004-06-07 20:34:58 $ 
 */

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

public class JTableGraphicalEditPart extends ComponentGraphicalEditPart {

private EStructuralFeature sfColumns;

private IBeanProxy fHeaderProxy = null;
private IMethodProxy fGetHeaderRect = null;

public JTableGraphicalEditPart(Object aModel){
	super(aModel);
}

private JTableComponentListener fComponentListener;
private JTableImageListener fImageListener;

protected JTableComponentListener getJTableComponentListener() {
	if (fComponentListener == null)
		fComponentListener = new JTableComponentListener();
	return fComponentListener;
}
protected class JTableComponentListener implements IVisualComponentListener {
	public void componentHidden() {
	};
	public void componentMoved(int x, int y) {
		refreshColumns();		
	};
	public void componentRefreshed() {
		refreshColumns();
	};
	public void componentResized(int width, int height) {
		refreshColumns();
	};
	public void componentShown() {
		refreshColumns();
	};
}

protected JTableImageListener getJTableImageListener() {
	if (fImageListener == null)
		fImageListener = new JTableImageListener();
	return fImageListener;
}
/*
 * This is for listening for updates to the image of the table coming back from the remote vm.
 * This is important for listening for changes to the preferred size of table columns.
 * @since 1.0.0
 *
 */
protected class JTableImageListener implements IImageListener {
	public void imageChanged(ImageData imageData) {
		refreshColumns();
	}	
}

private Adapter jTableAdapter = new EditPartAdapterRunnable() {
	public void run() {
		if (isActive())
			refreshChildren();
			refreshColumns();
	}
	public void notifyChanged(Notification notification) {
		if (notification.getFeature() == sfColumns) {
			queueExec(JTableGraphicalEditPart.this);
		} 
	}
};


public void activate() {
	super.activate();
	((EObject) getModel()).eAdapters().add(jTableAdapter);
	getVisualComponent().addComponentListener(getJTableComponentListener());	
	getVisualComponent().addImageListener(getJTableImageListener());
}
public void deactivate() {
	super.deactivate();
	((EObject) getModel()).eAdapters().remove(jTableAdapter);
	getVisualComponent().removeComponentListener(getJTableComponentListener());	
	getVisualComponent().removeImageListener(getJTableImageListener());
	fHeaderProxy = null;
	fGetHeaderRect = null;
}
protected void createEditPolicies(){
	EditDomain domain = EditDomain.getEditDomain(this);	
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy(new JTableContainerPolicy(domain)));	
	super.createEditPolicies();
}


private Runnable fRefreshColumnsRunnable = new Runnable() {
	public void run() {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			TableColumnGraphicalEditPart columnEP = (TableColumnGraphicalEditPart)children.get(i);
			columnEP.setBounds(getColumnBounds(i));
			columnEP.refresh();			
		}
	}
};
private void refreshColumns(){
	// Check to see if there's no columns on the table.
	if (getChildren().size() == 0)
		return;
	// Farm this off to the display thread, if we're not already on it.
	CDEUtilities.displayExec(JTableGraphicalEditPart.this, fRefreshColumnsRunnable);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	refreshColumns();
}

protected List getModelChildren() {
	return getChildJavaBeans();
}

public List getChildJavaBeans() {
	return (List) ((EObject) getModel()).eGet(sfColumns);
}

/**
 * TableColumnGraphicalEditPart is not allowed on the free form as it is specially designed for a TableColumn
 * hosted inside a JTable.  Therefore instead of it being defined in the .override for the JTable it is instantiated
 * directly by the JTableGraphicalEditPart
 */
protected EditPart createChild(Object child) {
	TableColumnGraphicalEditPart result = new TableColumnGraphicalEditPart();
	result.setModel(child);
	result.setBounds(getColumnBounds(getChildren().size()));
	return result;
}

/*
 * @see EditPart#setModel(Object)
 */
public void setModel(Object model) {
	super.setModel(model);
	sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), JFCConstants.SF_JTABLE_COLUMNS);
}


private IBeanProxy getHeaderProxy() {
	if (fHeaderProxy == null || !fHeaderProxy.isValid()) {
		IBeanProxy tableProxy = getComponentProxy().getBeanProxy();
		IMethodProxy getTableHeader = tableProxy.getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(tableProxy.getTypeProxy().getTypeName(), "getTableHeader", null); //$NON-NLS-1$
		fHeaderProxy = getTableHeader.invokeCatchThrowableExceptions(tableProxy);
	}
	return fHeaderProxy;
}

private IMethodProxy getGetHeaderRect() {
	if (fGetHeaderRect == null) {
		fGetHeaderRect = getHeaderProxy().getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(getHeaderProxy().getTypeProxy().getTypeName(), "getHeaderRect", new String[] { "int" }); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return fGetHeaderRect;
}

private Rectangle getBounds() {
	Rectangle figBounds = getFigure().getBounds().getCopy();
	if (figBounds.height <= 0) {
		figBounds.height = getVisualComponent().getBounds().height;
	}

	return figBounds;
}

private Rectangle getColumnBounds(int index) {
	Rectangle result = null;
	if (index < 0)
		return null;
	
	try {
		IStandardBeanProxyFactory fac = getHeaderProxy().getProxyFactoryRegistry().getBeanProxyFactory();
		IIntegerBeanProxy indexProxy = fac.createBeanProxyWith(index);
		// Calls JTableHeader.getHeaderRect(column index)
		IRectangleBeanProxy rectProxy = (IRectangleBeanProxy) getGetHeaderRect().invoke(getHeaderProxy(), new IBeanProxy[] {indexProxy});
		result = new Rectangle();
		result.x = rectProxy.getX();
		result.y = rectProxy.getY();
		result.width = rectProxy.getWidth();
		result.height = rectProxy.getHeight();
		
		// Move up by the height of the header (will be 0 if header isn't showing)
		result.y = -result.height;
		// Add the size of the header to the height of the table,
		Rectangle parentBounds = getBounds();
		result.height = parentBounds.height + result.height;
		
		// Translate relative to the origin of the table
		result.x += parentBounds.x;
		result.y += parentBounds.y;
		
	} catch (ThrowableProxy e) {
		result = null;
	}
	
	return result;
}
}