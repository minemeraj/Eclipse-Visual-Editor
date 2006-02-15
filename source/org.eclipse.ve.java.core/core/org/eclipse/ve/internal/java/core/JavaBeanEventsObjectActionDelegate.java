/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: JavaBeanEventsObjectActionDelegate.java,v $
 *  $Revision: 1.14 $  $Date: 2006-02-15 16:11:47 $ 
 */

import java.util.*;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;

import org.eclipse.ve.internal.jcm.Callback;
import org.eclipse.ve.internal.jcm.PropertyEvent;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * @author pwalker
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JavaBeanEventsObjectActionDelegate implements IObjectActionDelegate, IMenuCreator {
	

	/**
	 * Use this flag in object contributions to indicate that the contribution is only for editors that
	 * support standard events. This is placed into the domain during the editor setup.
	 * <pre>
	 *    <visibility>
     *      <and>
     *        <objectState
     *              name="DOMAIN"
     *              value="STANDARD EVENTS"/>
     *        <objectState
     *              name="CHANGEABLE"
     *              value="true"/>
     *      </and>
     *   </visibility> 
	 * </pre>
	 */
	public static final String STANDARD_EVENT_CAPABLE = "STANDARD EVENTS"; //$NON-NLS1$ //$NON-NLS-1$
	
	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// We don't have a need for the active part.
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		// Never called because we become a menu.
	}

	private List editParts;
	private void setEditParts(List editParts) {
		this.editParts = editParts;
	}

	private List getEditParts() {
		return editParts;
	}

	private Action delegateAction;	// This is the delegate action that is wrappering this one.
	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// Test to see if action is enabled, if it is, that means we've already passed the enablesFor test.
		if (!action.isEnabled())
			return;

		if ( selection == null || 
				!(selection instanceof IStructuredSelection) || 
				selection.isEmpty() || 
				((IStructuredSelection)selection).size() != 1)	// only handle one edit part for now.
				
			action.setEnabled(false);
			
		else {
			if (action instanceof Action) {
				if (delegateAction != action) {
					delegateAction = (Action) action;
					delegateAction.setMenuCreator(this);
				}
				action.setEnabled(true);
				setEditParts(((IStructuredSelection)selection).toList());
			} else {
				action.setEnabled(false);
			}
		}
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Control)
	 */
	public Menu getMenu(Control parent) {
		return null;	// Should never be on a control, should be a submenu of a popup menu
	}

	/**
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Menu)
	 */
	public Menu getMenu(Menu parent) {
		// Create the new menu. The menu will get filled when it is about to be shown. see fillMenu(Menu).
		Menu menu = new Menu(parent);
		/**
		 * Add listener to repopulate the menu each time
		 * it is shown because MenuManager.update(boolean, boolean) 
		 * doesn't dispose pulldown ActionContribution items for each popup menu.
		 */
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu m = (Menu)e.widget;
				MenuItem[] items = m.getItems();
				for (int i=0; i < items.length; i++) {
					items[i].dispose();
				}
				fillMenu(m);
			}
		});
		return menu;	
	}
	/**
	 * Fill pull down menu with the event listeners for this component
	 */
	protected void fillMenu(Menu menu) {
		if (getEditParts() != null && getEditParts().size() == 1) {
			final EditPart editPart = (EditPart)getEditParts().get(0);	// just handle the first for now. handle multiple editparts later on
			if ( editPart.getModel() instanceof IJavaObjectInstance ) {
				final IJavaObjectInstance javaBean = (IJavaObjectInstance)editPart.getModel();
				// The first item in the menu allows a wizard dialog to be used to add events
				MenuItem openDialogItem = new MenuItem(menu,SWT.PUSH);
				openDialogItem.setText(JavaMessages.JavaBeanEventsObjectActionDelegate_MenuItem_AddEvents_Text); 
				openDialogItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// TODO Needs to run through commands and also we need to have general purpose no commands so disable menu items
						EditDomain domain = EditDomain.getEditDomain(editPart);
						((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).doModelChanges(new Runnable() {
							public void run() {					
								Shell shell = new Shell(Display.getDefault().getActiveShell());	
								AddEventWizard addEventWizard = new AddEventWizard(javaBean,editPart);			
								WizardDialog wizardDialog = new WizardDialog(shell,addEventWizard);
								shell.setImage(addEventWizard.getJavaBeanImage());
								wizardDialog.create();
								// Attempt to center the dialog on the screen.  This should occur in the default WizardDialog behavior
								// but for some reason it doesn't occur correctly
								wizardDialog.getShell().setLocation(
									(Display.getDefault().getBounds().width - wizardDialog.getShell().getSize().x) / 2,
									(Display.getDefault().getBounds().height - wizardDialog.getShell().getSize().y) / 2 );
								wizardDialog.open();
								shell.dispose();
							}
						}, false);
					}
				});							
				// Use a general listener for the events on all menu items
				final SelectionListener addMethodProxyListener = new SelectionAdapter() {				
					public void widgetSelected(SelectionEvent e) {
						Object[] widgetData = (Object[]) e.widget.getData();
						final EventSetDecorator eventDecor = (EventSetDecorator)widgetData[0];						
						final MethodProxy methodProxy = (MethodProxy)widgetData[1];
						EditDomain domain = EditDomain.getEditDomain(editPart);
						((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).doModelChanges(new Runnable() {
							public void run() {
								// TODO This should really return a command which we then put on the command stack.
								Callback newlyCreatedCallback = JavaBeanEventUtilities.addCallback(javaBean,eventDecor,methodProxy.getMethod());
								// Select the newly created edit part for the callback
								// For this we must find the tree edit part, as graph viewer edit parts have no children representing the events
								JavaBeanEventUtilities.selectCallbackChildEditPart(editPart, newlyCreatedCallback);
							}
						}, false);
					}
				};
				// Use a general listener for the properties on all menu items
				final SelectionListener addPropertyDecoratorListener = new SelectionAdapter() {				
					public void widgetSelected(SelectionEvent e) {
						final PropertyDecorator propDecor = (PropertyDecorator)e.widget.getData();
						EditDomain domain = EditDomain.getEditDomain(editPart);
						((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).doModelChanges(new Runnable() {
							public void run() {
								// TODO This should really return a command which we then put on the command stack.
								PropertyEvent propertyEvent = JavaBeanEventUtilities.addPropertyChange(javaBean,propDecor.getName());
								// If we are a tree edit part then iterate our children and select the newly created edit part
								JavaBeanEventUtilities.selectPropertyEventChildEditPart(editPart, propertyEvent);
							}
						}, false);
					}
				};		
				
				class MethodAndEventSetDecorator{
						public EventSetDecorator eventSetDecor;
						public MethodDecorator methodDecor;		
					MethodAndEventSetDecorator(EventSetDecorator anEventSetDecor, MethodDecorator aMethodDecor){
						eventSetDecor = anEventSetDecor;
						methodDecor = aMethodDecor;
					}
				}
								
				// Now add the preferred methods on preferred events as short cuts							
				// Iterate over the preferred events to see which ones have preferred method descriptors
				// Accumulate them all so that they can be sorted by method name independent of eventset.
				List methodAndEventsList = new ArrayList();				
				Iterator iter = Utilities.getEventSetsIterator(((JavaClass)javaBean.getJavaType()).getAllEvents());		
				while(iter.hasNext()){
					EventSetDecorator eventDecor = (EventSetDecorator)iter.next();
					List methodsEList = eventDecor.getListenerMethods();
					// Sort the preferred methods by their display name.
					Iterator methods = methodsEList.iterator();
					while(methods.hasNext()){
						MethodProxy methodProxy = (MethodProxy)methods.next();
						// Get the method decorator which is holds the preferred flag that comes
						// from the BeanInfo java.beans.MethodDescriptor
						MethodDecorator methodDecor = Utilities.getMethodDecorator(methodProxy);
						if ( methodDecor.isPreferred() ) {
							methodAndEventsList.add(new MethodAndEventSetDecorator(eventDecor,methodDecor));
						}
					}
				}
							
				if (!methodAndEventsList.isEmpty()) {
					// Sort it and build the menu.
					if (methodAndEventsList.size() > 1) {
						Collections.sort(methodAndEventsList, new Comparator() {
							Collator coll = Collator.getInstance(ULocale.getDefault().toLocale());
							public int compare(Object o1, Object o2) {
								return coll.compare(((MethodAndEventSetDecorator) o1).methodDecor.getDisplayName(), ((MethodAndEventSetDecorator) o2).methodDecor.getDisplayName());
							}
						}); 
					}	
											
					new MenuItem(menu,SWT.SEPARATOR);
					int l = methodAndEventsList.size();
					for (int i = 0; i < l; i++) {
						MethodAndEventSetDecorator methodAndEventSetDecor = (MethodAndEventSetDecorator)methodAndEventsList.get(i);
						MethodDecorator methodDecor = methodAndEventSetDecor.methodDecor;
						MenuItem item = new MenuItem(menu,SWT.PUSH);
						item.setText(methodDecor.getDisplayName());
						item.setData(new Object[] { methodAndEventSetDecor.eventSetDecor , methodDecor.getEModelElement() });
						item.addSelectionListener(addMethodProxyListener);
						item.setImage(JavaBeanEventUtilities.getEventArrow_Available_Image());
					}
				}
				
				// Iterate over the preferred properties looking for all that are preferred and bound
				Iterator propIter = Utilities.getPropertiesIterator(((JavaClass)javaBean.getJavaType()).getAllProperties());
				ArrayList boundPreferredProperties = new ArrayList();
				while (propIter.hasNext()){
					PropertyDecorator propDecor = (PropertyDecorator)propIter.next();
					// It is possible to be a preferred property, but not bound.  We only are interested in bound properties ( because only these will fire a propertyChangeEvent )
					if (propDecor.isBound() && propDecor.isPreferred()) {
						boundPreferredProperties.add(propDecor);
					}
				}
				if (!boundPreferredProperties.isEmpty()) {
					if (boundPreferredProperties.size() > 1) {
						// Now we have the list of bounds and preferred properties sort them by display name
						Collections.sort(boundPreferredProperties, new Comparator() {
							Collator coll = Collator.getInstance(ULocale.getDefault().toLocale());
							public int compare(Object o1, Object o2) {
								return coll.compare(((PropertyDecorator) o1).getDisplayName(), ((PropertyDecorator) o2).getDisplayName());
							}
						}); 
					}	
					// Iterate over the sorted bound preferred properties creating menu items for each one
					new MenuItem(menu,SWT.SEPARATOR);					
					propIter = boundPreferredProperties.iterator();
					while(propIter.hasNext()){ 					
						PropertyDecorator propDecor = (PropertyDecorator)propIter.next();
						MenuItem item = new MenuItem(menu,SWT.PUSH);
						item.setText(propDecor.getDisplayName());
						item.setData(propDecor);
						item.addSelectionListener(addPropertyDecoratorListener);			
						item.setImage(JavaBeanEventUtilities.getPropertyArrow_Available_Image());			
					}
				}
			}
		}
	}	
}
