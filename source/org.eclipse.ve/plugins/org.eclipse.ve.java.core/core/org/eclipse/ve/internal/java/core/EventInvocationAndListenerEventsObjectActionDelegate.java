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
 *  $RCSfile: EventInvocationAndListenerEventsObjectActionDelegate.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.ModelChangeController;
import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.Listener;

import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
 
public class EventInvocationAndListenerEventsObjectActionDelegate implements IObjectActionDelegate, IMenuCreator {
	
	private EventInvocationAndListener eventInvocationAndListener;
	private EditPart editPart;
	
	protected void fillMenu(Menu menu) {
		// The menus items allow adding of the methods supported by the event invocation's listener's protocol
		// allowing new callbacks to be added, unless 
		//     1)  The listener is shared ( i.e. it's a style 2, or 3 ).  We don't generate these as they would require a case block in the callback
		//	        that has to determine the source of the event
		//     2)  The listener already has a callback for that method.  For these we grey the menu option out by disabling it
		// ---------------
		// First, get the available methods that the listener implements 
		final Listener listener = eventInvocationAndListener.getListener(); 
		ListenerType listenerType = listener.getListenerType();
		// We need to find the listener methods that the listener implements
		// This is actually tricky, because not all methods the class implements are necessarily listener ones
		// so we actually need to look at who it implements and extends and compare these against the known types and adapter types
		// of the available events to see the event, and then get the methods from there
		JavaClass listenerImplements = null;
		if ( listenerType.getImplements().size() == 1 ) listenerImplements = (JavaClass)listenerType.getImplements().get(0);  
		JavaClass listenerExtends = listenerType.getExtends();
		// Now we know who the listener extends and also implements
		// We can't handle it if there is more than one listener.  If that's the case in expert mode our children will be for each interface and the Events menu works from there
		final AbstractEventInvocation eventInvocation =  (AbstractEventInvocation) eventInvocationAndListener.getEventInvocations().get(0);
		// Get the JavaBean class by back traversing from the event invocation to the class of the JavaBean that owns it
		final IJavaObjectInstance javaBean =(IJavaObjectInstance) eventInvocation.eContainer();		
		JavaClass javaBeanClass = (JavaClass) javaBean.getJavaType();
		// If the existing listener is for a PropertyChangeEventInvocation then the menu lists the available properties, greying out ones already used
		// otherwise it lists the available events, also greying out the ones already used
		if (eventInvocation instanceof PropertyChangeEventInvocation ) { 
			final PropertyChangeEventInvocation propEventInvocation = (PropertyChangeEventInvocation)eventInvocation;
			// A single listener is used for all menu items
			SelectionListener menuPropertyItemSelectionListener = new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent e) {
					// TODO This should be a command.
					EditDomain domain = EditDomain.getEditDomain(editPart);
					((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).doModelChanges(new Runnable() {
						public void run() {									
							// The data is the method proxy of the event to be added to the existing listener
							PropertyDecorator propDecor = (PropertyDecorator)e.widget.getData(); 
							// Create a PropertyEvent and add it to the PropertyChangeEvent invocation
							PropertyEvent propertyEvent = JCMFactory.eINSTANCE.createPropertyEvent();
							propertyEvent.setUseIfExpression(true);
							propertyEvent.setPropertyName(propDecor.getName());
							propEventInvocation.getProperties().add(propertyEvent);
							// If we are opened over a tree edit part then find the child for the newly create event and select it so we drive to the source range in the editor part
							JavaBeanEventUtilities.selectPropertyEventChildEditPart(editPart,propertyEvent);
						}
					}, false);
				}
			};
			// See whether the add method for the PropertyChangeListener is "addPropertyChangeListener(String propertyName,PropertyChangeListener listener)";
			boolean isTwoArgAddMethod = propEventInvocation.getAddMethod().getParameters().size() == 2;
			// Get all of the available properties that are bound			
			Iterator boundProperties = JavaBeanEventUtilities.getBoundProperties(javaBeanClass,true).iterator();	
			while(boundProperties.hasNext()) {
				PropertyDecorator propDecor = (PropertyDecorator)boundProperties.next();
				//	Create a MenuItem for each one, disabling any that are already used
				MenuItem menuItem = new MenuItem(menu,SWT.PUSH);
				menuItem.setData(propDecor);
				menuItem.setText(propDecor.getDisplayName());
				// If the method that adds the listener is a two argument one then we can't re-use it for any other properties
				if (isTwoArgAddMethod) {
					menuItem.setEnabled(false);
				} else {
					// For an existing invocation added with a single argument
					// it is possible that it has an existing PropertyEvent for the property, in which case it is disabled
					Iterator existingPropertyEvents = propEventInvocation.getProperties().iterator();
					while(existingPropertyEvents.hasNext()){
					PropertyEvent existingPropertyEvent = (PropertyEvent)existingPropertyEvents.next();
					if(existingPropertyEvent.getPropertyName().equals(propDecor.getName())) {
						menuItem.setEnabled(false);
						break;
					}
				}
			}
			// If the menu item is enabled then add the listener to it
			if (menuItem.isEnabled()) {
				menuItem.addSelectionListener(menuPropertyItemSelectionListener);		
				menuItem.setImage(JavaBeanEventUtilities.getPropertyArrow_Available_Image());
				} else {
					menuItem.setImage(JavaBeanEventUtilities.getPropertyArrowDisabledImage());
				}
			}
		} else {
			final BeanEvent beanEvent = JavaBeanEventUtilities.getEvent(javaBeanClass,listenerImplements,listenerExtends);
			// If we don't get a beanEvent that means we are style1/2 since in those cases listenerImplements/extends will be null.
			if (beanEvent != null) {
				// Now we have the BeanEvent, get its methods
				EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(beanEvent);
				Iterator eventMethods = eventSetDecor.getListenerMethods().iterator();
				// Use a single listener for all of the menu events
				SelectionListener menuItemSelectionListener = new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent e) {
						// TODO This should be a command.
						EditDomain domain = EditDomain.getEditDomain(editPart);
						((ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).doModelChanges(new Runnable() {
							public void run() {									
								// The data is the method proxy of the event to be added to the existing listener
								MethodProxy methodProxy = (MethodProxy) e.widget.getData();
								// Create a callback and add it to the event invocation
								Callback callback = JCMFactory.eINSTANCE.createCallback();
								callback.setMethod(methodProxy.getMethod());
								eventInvocation.getCallbacks().add(callback);
								// Having added the callback we should select the edit part so the JVE editor drives to selection correctly
								// This only works for the tree right now as the canvas has no children for edit parts
								JavaBeanEventUtilities.selectCallbackChildEditPart(editPart, callback);
							}
						}, false);
					}
				};
				while (eventMethods.hasNext()) {
					MethodProxy methodProxy = (MethodProxy) eventMethods.next();
					MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
					// For the menu description use the NLS label from the bundle
					MethodDecorator methodDecor = Utilities.getMethodDecorator(methodProxy);
					menuItem.setText(methodDecor.getDisplayName());
					menuItem.setData(methodProxy);
					menuItem.addSelectionListener(menuItemSelectionListener);
					menuItem.setImage(JavaBeanEventUtilities.getEventArrow_Available_Image());
					// We only want to enable those nethods that don't already have callbacks. This is because code generation allows the creation of new methods only
					Iterator existingCallbacks = eventInvocation.getCallbacks().iterator();
					while (existingCallbacks.hasNext()) {
						Callback existingCallback = (Callback) existingCallbacks.next();
						if (existingCallback.getMethod() == methodProxy.getMethod()) {
							// The method is already used.  Disable the menu item
							menuItem.setEnabled(false);
							menuItem.setImage(JavaBeanEventUtilities.getEventArrowDisabledImage());
							break;
						}
					}
				}
			}
		}
	}	
	
	public void run(IAction action){
		// Never called because we are a menu action that just create a cascaded menu with the list of available events
	}
	public void dispose() {
	}
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// We don't have a need for the active part.
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
	
	private Action delegateAction;	// This is the delegate action that is wrappering this one.	
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
					// Get the EventInvocationAndListener from the edit part 
					List editParts = ((IStructuredSelection)selection).toList();
					editPart = (EditPart)editParts.get(0);
					eventInvocationAndListener = (EventInvocationAndListener)editPart.getModel(); 					
					// We disable the action if the listener is shared by more than one JavaBean, because adding is too hard to code-gen for this style					
					// Also, only enable the action if there is one EventInvocation.  If there is more than one then we also can't cope
					// TODO Also disable for style1/2. This is done by checking for listenerType name not null. In future
					// this won't be sufficient to distinquish style 1 from 2.
					action.setEnabled(eventInvocationAndListener.getListener().getListenerType().getName() == null && eventInvocationAndListener.getListener().getListenedBy().size() == 1 && eventInvocationAndListener.getEventInvocations().size() == 1);					
				} else {
					action.setEnabled(false);
				}
			}
		}

}
