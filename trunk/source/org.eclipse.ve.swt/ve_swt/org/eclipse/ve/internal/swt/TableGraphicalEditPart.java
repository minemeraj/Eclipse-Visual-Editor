/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TableGraphicalEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-08 15:03:04 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;
import org.eclipse.ve.internal.java.core.JavaContainerPolicy;
import org.eclipse.ve.internal.java.visual.VisualContainerPolicy;
 

/**
 * 
 * @since 1.0.0
 */
public class TableGraphicalEditPart extends CompositeGraphicalEditPart {

	public TableGraphicalEditPart(Object aModel){
		super(aModel);
	}
	
	private EStructuralFeature sfColumns;
	
	protected VisualContainerPolicy getContainerPolicy() {
		return new TableContainerPolicy(EditDomain.getEditDomain(this)); // SWT standard Composite/Container Edit Policy
	}



	private Adapter tableAdapter = new EditPartAdapterRunnable() {

		public void run() {
			if (isActive())
				refreshChildren();		
		}
		
		public void notifyChanged(Notification notification) {
			if (notification.getFeature() == sfColumns) {
				queueExec(TableGraphicalEditPart.this);
			} 
		}
	};

	private TableComponentListener fComponentListener;

	public void activate() {
		super.activate();
		((EObject) getModel()).eAdapters().add(tableAdapter);
		getVisualComponent().addComponentListener(getTableComponentListener());	
	}
	public void deactivate() {
		super.deactivate();
		((EObject) getModel()).eAdapters().remove(tableAdapter);
	}
	protected void createEditPolicies(){
		super.createEditPolicies();
		EditDomain domain = EditDomain.getEditDomain(this);		
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new CDELayoutEditPolicy(new JavaContainerPolicy(sfColumns,domain)));;
	}
	protected TableComponentListener getTableComponentListener() {
		if (fComponentListener == null)
			fComponentListener = new TableComponentListener();
		return fComponentListener;
	}
	protected class TableComponentListener implements IVisualComponentListener {
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

	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
//	  TODO - TableColumn graphical edit part not completed yet - JRW
//		return (List) getBean().eGet(getSFColumns());
	}
	/**
	 * TableColumnGraphicalEditPart is not allowed on the free form as it is specially designed for a TableColumn
	 * hosted inside a Table.  Therefore instead of it being defined in the .override for the Table it is instantiated
	 * directly by the TableGraphicalEditPart
	 */
	protected EditPart createChild(Object child) {
		TableColumnGraphicalEditPart result = new TableColumnGraphicalEditPart();
		result.setModel(child);
		return result;
	}

	/*
	 * @see EditPart#setModel(Object)
	 */
	public void setModel(Object model) {
		super.setModel(model);
		JavaClass modelType = (JavaClass) ((EObject) model).eClass();
		sfColumns = JavaInstantiation.getSFeature(((EObject) model).eClass().eResource().getResourceSet(), SWTConstants.SF_TABLE_COLUMNS);
	}
	}
