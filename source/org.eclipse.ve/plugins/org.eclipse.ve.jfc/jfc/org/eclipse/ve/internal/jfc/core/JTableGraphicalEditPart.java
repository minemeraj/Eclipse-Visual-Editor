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
 *  $Revision: 1.2 $  $Date: 2004-04-20 09:13:12 $ 
 */

import java.util.*;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.CDELayoutEditPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;

public class JTableGraphicalEditPart extends ComponentGraphicalEditPart {

private EStructuralFeature sfColumns;

private Adapter jTableAdapter = new EditPartAdapterRunnable() {

	public void run() {
		if (isActive())
			refreshChildren();		
	}
	
	public void notifyChanged(Notification notification) {
		if (notification.getFeature() == getSFColumns()) {
			queueExec(JTableGraphicalEditPart.this);
		} 
	}
};

private JTableComponentListener fComponentListener;


public JTableGraphicalEditPart(Object aModel){
	super(aModel);
}
public void activate() {
	super.activate();
	((EObject) getModel()).eAdapters().add(jTableAdapter);
	getVisualComponent().addComponentListener(getJTableComponentListener());	
}
public void deactivate() {
	super.deactivate();
	((EObject) getModel()).eAdapters().remove(jTableAdapter);
}
protected void createEditPolicies(){
	EditDomain domain = EditDomain.getEditDomain(this);
	EStructuralFeature SF_TABLE_COLUMNS = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain), JFCConstants.SF_JTABLE_COLUMNS);	
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(new JavaContainerPolicy(SF_TABLE_COLUMNS,domain))); //$NON-NLS-1$	
	super.createEditPolicies();
}
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

	};
	public void componentResized(int width, int height) {
		refreshColumns();
	};
	public void componentShown() {
	};
}
private void refreshColumns(){
	Iterator iter = getChildren().iterator();
	while(iter.hasNext()){
		TableColumnGraphicalEditPart columnEP = (TableColumnGraphicalEditPart)iter.next();
		columnEP.refresh();
	}
}

private EStructuralFeature getSFColumns(){
	if(sfColumns == null){
		sfColumns = JavaInstantiation.getSFeature(getBean().eResource().getResourceSet(), JFCConstants.SF_JTABLE_COLUMNS);
	}
	return sfColumns;
}

protected List getModelChildren() {
	return Collections.EMPTY_LIST;
//  TODO - TableColumn graphical edit part not completed yet - JRW
//	return (List) getBean().eGet(getSFColumns());
}
/**
 * TableColumnGraphicalEditPart is not allowed on the free form as it is specially designed for a TableColumn
 * hosted inside a JTable.  Therefore instead of it being defined in the .override for the JTable it is instantiated
 * directly by the JTableGraphicalEditPart
 */
protected EditPart createChild(Object child) {
	TableColumnGraphicalEditPart result = new TableColumnGraphicalEditPart();
	result.setModel(child);
	return result;
}
}