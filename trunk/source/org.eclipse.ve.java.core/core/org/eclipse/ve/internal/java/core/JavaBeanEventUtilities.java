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
 *  $RCSfile: JavaBeanEventUtilities.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.text.Collator;
import java.util.*;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.jcm.*;

import org.eclipse.jem.java.*;

/**
 */
public class JavaBeanEventUtilities {
	
	private static Image 
		EVENT_METHOD_IMAGE,
//		EVENT_DESCRIPTOR_IMAGE,
//		EVENT_PRESENT_IMAGE,
//		EVENT_METHOD_SHARED_IMAGE,
//		EVENT_NONE_IMAGE,
//		EVENT_PRESENT_THIS_IMAGE,
		EVENT_PRESENT_ANON_IMAGE,
		EVENT_PRESENT_ANON_BW_IMAGE,		
		EVENT_PRESENT_CLASS_IMAGE,		
		EVENT_PRESENT_CLASS_BW_IMAGE,		
		EVENT_PRESENT_INTERFACE_IMAGE,
		EVENT_ARROW_IMAGE,	
	    EVENT_SHRD_ARROW_IMAGE,		
		EVENT_ARROW_DISABLED_IMAGE,
		PROPERTY_ARROW_IMAGE,	
	    PROPERTY_SHRD_ARROW_IMAGE,	
		PROPERTY_ARROW_BW_IMAGE,
		PROPERTY_ARROW_DISABLED_IMAGE,		
		EVENT_PRESENT_SHARED_IMAGE
//		EVENT_NOTTHERE_IMAGE;	// TODO Currently not used anywhere. Should it be?
        ;
	public static final String EVENTS = "events"; //$NON-NLS-1$
	public static final String REQ_DELETE_EVENT_DEPENDANT = "DELETE_EVENTS";  //$NON-NLS-1$
		
	public static Image getEventArrow_Available_Image(){
		if ( EVENT_METHOD_IMAGE == null ) {
			// Grey arrow
			EVENT_METHOD_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/unevent_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_METHOD_IMAGE;
	}	
	public static Image getPropertyArrowImage() {
		if ( PROPERTY_ARROW_IMAGE == null ) {
			// Blue VAJ style event arrow
			PROPERTY_ARROW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/p2pconn_obj.gif"); //$NON-NLS-1$
		}	
		return PROPERTY_ARROW_IMAGE;
	}	
	public static Image getPropertyShrdArrowImage() {
			if ( PROPERTY_SHRD_ARROW_IMAGE == null ) {
				// Blue VAJ style event arrow
				PROPERTY_SHRD_ARROW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(),"icons/full/obj16/p2pconn_shrd_obj.gif"); //$NON-NLS-1$
			}	
			return PROPERTY_SHRD_ARROW_IMAGE;
		}	
	public static Image getPropertyArrowDisabledImage() {
			if ( PROPERTY_ARROW_DISABLED_IMAGE == null ) {
				// Blue VAJ style event arrow
				PROPERTY_ARROW_DISABLED_IMAGE = new Image(Display.getDefault(),getPropertyArrowImage(),SWT.IMAGE_DISABLE);
			}	
			return PROPERTY_ARROW_DISABLED_IMAGE;
		}		
	public static Image getPropertyArrow_Available_Image() {
		if ( PROPERTY_ARROW_BW_IMAGE == null ) {
			// Blue VAJ style event arrow
			PROPERTY_ARROW_BW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/unproperty_obj.gif"); //$NON-NLS-1$
		}	
		return PROPERTY_ARROW_BW_IMAGE;
	}		
 	public static Image getEventArrowImage(){
		if ( EVENT_ARROW_IMAGE == null ) {
			// Green VAJ style event arrow
			EVENT_ARROW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/e2mconn_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_ARROW_IMAGE;
	}	
	public static Image getEventSharedArrowImage(){
			if ( EVENT_SHRD_ARROW_IMAGE == null ) {
				// Green VAJ style event arrow
				EVENT_SHRD_ARROW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(),"icons/full/obj16/e2mconn_shrd_obj.gif"); //$NON-NLS-1$
			}	
			return EVENT_SHRD_ARROW_IMAGE;
		}	
	public static Image getEventArrowDisabledImage(){
		if ( EVENT_ARROW_DISABLED_IMAGE == null ) {
			// Green VAJ style event arrow
			EVENT_ARROW_DISABLED_IMAGE = new Image(Display.getDefault(),getEventArrowImage(),SWT.IMAGE_DISABLE);
		}	
		return EVENT_ARROW_DISABLED_IMAGE;
	}	
	public static Image getEvent_UnsharedClass_Image(){
		if ( EVENT_PRESENT_SHARED_IMAGE == null ) {
			// Empty white circles
			EVENT_PRESENT_SHARED_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/shrdclass_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_PRESENT_SHARED_IMAGE;		
	}	
	public static Image getEventPresentInterfaceImage(){
		if ( EVENT_PRESENT_INTERFACE_IMAGE == null ) {
			// Interface symbol
			EVENT_PRESENT_INTERFACE_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/cview16/eventpresent_interface_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_PRESENT_INTERFACE_IMAGE;		
	}		
	public static Image getEvent_AnonAdapter_Image(){
		if ( EVENT_PRESENT_CLASS_IMAGE == null ) {
			// Event for a listener used once by the JavaBean
			EVENT_PRESENT_CLASS_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/anonclass_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_PRESENT_CLASS_IMAGE;		
	}
	public static Image getEvent_AnonAdapter_BW_Image(){
		if ( EVENT_PRESENT_CLASS_BW_IMAGE == null ) {
			// Event for a listener used once by the JavaBean
			EVENT_PRESENT_CLASS_BW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/listenerclass_obj.gif"); //$NON-NLS-1$
		}	
		return EVENT_PRESENT_CLASS_BW_IMAGE;		
	}		
	public static Image getEvent_AnonInterface_Image(){
		if ( EVENT_PRESENT_ANON_IMAGE == null ) {
			// Empty white circles
			EVENT_PRESENT_ANON_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/anoninter_obj.gif"); //$NON-NLS-1$
		}			
		return EVENT_PRESENT_ANON_IMAGE;		
	}	
	public static Image getEvent_AnonInterface_BW_Image(){
			if ( EVENT_PRESENT_ANON_BW_IMAGE == null ) {
				// Empty white circles
				EVENT_PRESENT_ANON_BW_IMAGE = CDEPlugin.getImageFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/obj16/listenerinterface_obj.gif"); //$NON-NLS-1$
			}	
			return EVENT_PRESENT_ANON_BW_IMAGE;		
		}	
	/**
	 * Return a List of all bound properties on the argument sorted by name
	 */
	public static List getBoundProperties(JavaClass aClass, boolean sort){
		List result = new ArrayList();
		// Iterate over the properties of the class and see which ones are bound
		Iterator allReferences = aClass.getAllProperties().iterator();
		while(allReferences.hasNext()){
			EStructuralFeature reference = (EStructuralFeature) allReferences.next();
			PropertyDecorator propDecor = Utilities.getPropertyDecorator(reference);		
			if ( propDecor != null && propDecor.isBound() ) {
				result.add(propDecor);
			}
		}
		if (sort) {
			Collections.sort(result,new Comparator() {
				Collator coll = Collator.getInstance(Locale.getDefault());
				public int compare(Object o1, Object o2) {
					return coll.compare(((PropertyDecorator)o1).getDisplayName(), ((PropertyDecorator)o2).getDisplayName());
				}
			});
		}
		return result;
	}	
	/**
	 * Return a list of MethodProxy instances for the methods that are part of the JavaEvent callback
	 */
	public static List getMethods(JavaEvent event){
		EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(event);
		if ( eventSetDecor != null ) {
			// propertyChange is different to regular event sets.  For regular event set the children
			// are the listener methods, e.g. key has keyPreesed and keyReleased which are the listener methods
			// however propertyChange has children that represent the bound properties
			if ( eventSetDecor.getName().equals("propertyChange") ) { //$NON-NLS-1$
				// TODO Do property change support later"); //$NON-NLS-1$
				return Collections.EMPTY_LIST;
//				return JavaBeanEventUtilities.getBoundProperties(javaClass);
			} else {
				return eventSetDecor.getListenerMethods();
			}
		} else { 
			return Collections.EMPTY_LIST;
		}
	}
	public static Callback addCallback(IJavaObjectInstance aJavaBean, EventSetDecorator anEventSetDecorator , Method aMethod){
		// See whether an existing listener exists.  If so we may be able to reuse it
		JavaClass eventListenerType = anEventSetDecorator.getListenerType();
		Iterator eventInvocations = ((List) aJavaBean.eGet( JavaInstantiation.getSFeature(aJavaBean, JavaBeanEventUtilities.EVENTS))).iterator();
		JavaClass eventAdapterClass = anEventSetDecorator.getEventAdapterClass();
		while(eventInvocations.hasNext()){
			AbstractEventInvocation existingEventInvocation = (AbstractEventInvocation) eventInvocations.next();
			// For the existing event invocation we can re-use it if
			// a) Its listener is anonymous and
			// b) The listener implements the interface, or extends the adapter class for the event we're trying to add
			// c) The listener doesn't already have a callback for the method we are adding
			boolean canReuseListener = false;
			ListenerType existingListenerType = existingEventInvocation.getListener().getListenerType();
			JavaClass existingListenerClass = null ;
			if (existingListenerType.getExtends() != null) 
			    existingListenerClass = existingListenerType.getExtends() ;
			else if (existingListenerType.getImplements().size()>0)
				existingListenerClass = (JavaClass) existingListenerType.getImplements().get(0) ;
			else
			    existingListenerClass = existingListenerType.getIs() ; 
			// Test a).  Anonymous inner classes have no name.  As a double backup check make sure the listener isn't shared ( although a listnerType with no name by definition can't be shared )
			if (existingListenerType.getName() == null && existingEventInvocation.getListener().getListenedBy().size() == 1) {
				// Test b).  The listenerType either extends the adapter or implements the interface  
				if (existingListenerType.getExtends() == eventAdapterClass || existingListenerType.getImplements().indexOf(eventListenerType) != -1) {
					// test c). make sure that this ListenerType is the one we need	
					if (eventListenerType.equals(existingListenerClass) || (eventAdapterClass != null &&
					    eventAdapterClass.equals(existingListenerClass))) {
						// Test d).  Check its existing callbacks
						Iterator callbacks = existingEventInvocation.getCallbacks().iterator();
						canReuseListener = true;
						while (callbacks.hasNext()) {
							Callback existingCallback = (Callback) callbacks.next();
							if (existingCallback.getMethod() == aMethod) {
								// The method is already used
								canReuseListener = false;
								break;
							}
						}
					}
				}
				// If we can reuse the listener then just create the callback and exit the loop
				if ( canReuseListener ) {
					Callback newCallback = JCMFactory.eINSTANCE.createCallback();
					newCallback.setMethod(aMethod);
					existingEventInvocation.getCallbacks().add(newCallback);
					return newCallback;
				}				
			};
		}
		// The fact we are here means we didn't find an existing listener to use so create a new Listener Type 
		// If there is an adapter then extend this by defualt, otherwise implements the ListenerType JavaClass of the EventSetDescriptor
		if ( eventAdapterClass != null ){
			// The method call takes either an adapter or an interface in separate arguments, and handles things accordingly
			return addCallback(aJavaBean,anEventSetDecorator,aMethod,null,eventAdapterClass);				
		} else {
			return addCallback(aJavaBean,anEventSetDecorator,aMethod,eventListenerType,null);				
		}		
	}
	
	public static Callback addCallback(IJavaObjectInstance aJavaBean, EventSetDecorator anEventSetDecorator , Method aMethod, JavaClass implementsJavaInterface, JavaClass extendsJavaAdapter){
		// This should probably be defered to some kind of rule, but for now just add a style with 
		// a new anonymous inner class listener, and event invocation that points to this
		BeanEvent beanEvent = (BeanEvent)anEventSetDecorator.eContainer();
		
		// Create a new Listener Type and implements the ListenerType JavaClass of the EventSetDescriptor
		ListenerType listenerType = JCMFactory.eINSTANCE.createListenerType();
		if ( implementsJavaInterface != null ) {
			listenerType.getImplements().add(implementsJavaInterface);
		}
		if ( extendsJavaAdapter != null ) {
			listenerType.setExtends(extendsJavaAdapter);
		}
		// The listener type is owned by the BeanComposition
		BeanComposition beanComposition = getBeanComposition(aJavaBean);
		beanComposition.getListenerTypes().add(listenerType);
		
		// Create a listener that points to the listener type		
		Listener listener = JCMFactory.eINSTANCE.createListener();
		listener.setListenerType(listenerType);
		
		return addCallback(aJavaBean,listener,beanEvent,aMethod);
		
	}
	
	public static Callback addCallback(IJavaObjectInstance aJavaBean, Listener aListener, BeanEvent beanEvent , Method aMethod ){
		
		// Create an event invocation		
		EventInvocation eventInvocation = JCMFactory.eINSTANCE.createEventInvocation();
		eventInvocation.setEvent(beanEvent);
		// Create a callback
		Callback callback = JCMFactory.eINSTANCE.createCallback();
		callback.setMethod(aMethod);
		// The event invocation references the listener and owns the callback
		eventInvocation.getCallbacks().add(callback);
		eventInvocation.setListener(aListener);
		// The event invocation should be added to the JavaBean
		EList eventInvocations = (EList)aJavaBean.eGet(
			JavaInstantiation.getSFeature(aJavaBean,JavaBeanEventUtilities.EVENTS));		
		eventInvocations.add(eventInvocation);	
		return callback;	
		
	}
	
	public static BeanComposition getBeanComposition(IJavaObjectInstance aJavaBean){
		EObject nextContainer = aJavaBean.eContainer();
		while( nextContainer != null ) {
			if ( nextContainer instanceof BeanComposition ) {
				return (BeanComposition)nextContainer;
			} else {
				nextContainer = nextContainer.eContainer();
			}
		}
		return null;
	}
	/**
	 * Get preferred EventSetDecorators
	 */
	public static List getPreferredBeanEventSetDescriptors(JavaClass javaClass) {
		// The list of our chidlren is the available events as defined by our JavaClass		
		EList javaEvents = javaClass.getAllEvents();
		// Now filter this list to only include those that are preferred.
		// The preferred flag comes from the BeanInfo java.beans.EventSetDescriptor
		// and is stored on the EventSetDecorator
		ArrayList result = new ArrayList(3);
		Iterator iter = javaEvents.iterator();
		while(iter.hasNext()){
			BeanEvent beanEvent = (BeanEvent)iter.next();
			EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(beanEvent);
			if ( eventSetDecor != null && eventSetDecor.isPreferred() ) {
				result.add(eventSetDecor);
			}
		}
		return result;
	}
	/**
	 * Return the events sorted alphabetically
	 */
	public static List getSortedEvents(JavaClass javaClass) {
		// The list of our chidlren is the available events as defined by our JavaClass		
		EList javaEvents = javaClass.getAllEvents();
		// Because the AllEvents is an EList we don't want to manipulate it directly
		java.util.List sortedEvents = new ArrayList(javaEvents);
		Collections.sort(sortedEvents,new Comparator() {
			Collator coll = Collator.getInstance(Locale.getDefault());
			public int compare(Object o1, Object o2) {
				EventSetDecorator e1 = Utilities.getEventSetDecorator((BeanEvent) o1);
				EventSetDecorator e2 = Utilities.getEventSetDecorator((BeanEvent) o2);				
				return coll.compare(e1.getDisplayName(), e2.getDisplayName());
			}
		});
		return sortedEvents;
	}
	
	public static BeanEvent getEvent(JavaClass javaBeanClass, JavaClass implementing, JavaClass extendingAdapter){
		// Get all the events and check each one to see if it is for the interface or adatper 
		Iterator javaEvents = javaBeanClass.getAllEvents().iterator();
		while (javaEvents.hasNext()){
			BeanEvent beanEvent = (BeanEvent)javaEvents.next();
			EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(beanEvent);
			// If the listener type of the event matches the argument or its adapter class then return it			
			if ( eventSetDecor.getListenerType() == implementing ) { 
				return beanEvent;				
			}
			if ( extendingAdapter != null && extendingAdapter == eventSetDecor.getEventAdapterClass()){
				return beanEvent;			
			}
		}
		return null;		
	}
    /**
     * Add a property change listener.  This will be an anonymous inner class that extends PropertyChangeListener
     * There are three ways that property change listeners can be added
     * 1) - addPropertyChangeListener(PropertyChangeListener listener)
     *       In this case a single listener is used for all events, and the code must look at the PropertyChangeEvent.getName to determine which property changed
     * 2) - addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
     *       In this case the listener is called back when the specific named property is called
     * 3) - not supported yet
     * If the method argument is null it is assumed that style1 is being used
     */
	public static PropertyEvent addPropertyChange(IJavaObjectInstance javaBean, String propertyName, Method javaMethod) {
	
		// Create a new Listener Type
		ListenerType listenerType = JCMFactory.eINSTANCE.createListenerType();
		// This implements PropertyChangeListener
		JavaClass propertyChangeListener = Utilities.getJavaClass("java.beans.PropertyChangeListener",javaBean.eResource().getResourceSet()); //$NON-NLS-1$
		listenerType.getImplements().add(propertyChangeListener);

		// The listener type is owned by the BeanComposition
		BeanComposition beanComposition = getBeanComposition(javaBean);
		beanComposition.getListenerTypes().add(listenerType);
		
		// Create a listener that points to the listener type		
		Listener listener = JCMFactory.eINSTANCE.createListener();
		listener.setListenerType(listenerType);

		// Find the javaMethod if it is null
		if (javaMethod == null){
			javaMethod = getAddPropertyChangeListenerMethod((JavaClass)javaBean.getJavaType());
		}		
		// Create a PropertyChange event invocation		
		PropertyChangeEventInvocation propertyChangeEventInvocation = JCMFactory.eINSTANCE.createPropertyChangeEventInvocation();
		PropertyEvent pe = JCMFactory.eINSTANCE.createPropertyEvent() ;
		// If the method is the single argument add then the useCaseBlock flag should be set
		pe.setUseIfExpression(true);
		
		pe.setPropertyName(propertyName);
		propertyChangeEventInvocation.getProperties().add(pe);
		propertyChangeEventInvocation.setAddMethod(javaMethod);
		// The event invocation references the listener
		listener.getListenedBy().add(propertyChangeEventInvocation);
		// and owns a callback
		Callback callback = JCMFactory.eINSTANCE.createCallback();
		// The callback method is always the method  "public void propertyChange(PropertyChangeEvent evt);" which is explicitly retrieved and put into the model
		List parmTypes = new ArrayList(1);
		parmTypes.add("java.beans.PropertyChangeEvent"); //$NON-NLS-1$		
		Method propertyChangeEventMethod = propertyChangeListener.getMethodExtended("propertyChange",parmTypes);		 //$NON-NLS-1$
		callback.setMethod(propertyChangeEventMethod);
		// Now add the callback to the event invocation
		propertyChangeEventInvocation.getCallbacks().add(callback);
		
		// The event invocation should be added to the JavaBean
		EList eventInvocations = (EList)javaBean.eGet(
			JavaInstantiation.getSFeature(javaBean,JavaBeanEventUtilities.EVENTS));		
		eventInvocations.add(propertyChangeEventInvocation);	
		// Return the property event
		return pe;	
	}
	public static Method getAddPropertyChangeListenerMethod(JavaClass javaClass){
		List parmTypes = new ArrayList(1);
		parmTypes.add("java.beans.PropertyChangeListener"); //$NON-NLS-1$
		// The parm types are the qualified class names are the strings
		return javaClass.getMethodExtended("addPropertyChangeListener",parmTypes);		 //$NON-NLS-1$
	}
	/**
	 * Add a property decorator to the javaBean.  This method finds an existing PropertyListener if there is one that doesn't already have a PropertyChange
	 * on it for the argument, so it tries to avoid the same property change being used on the same PropertyChangeListener
	 */
	public static PropertyEvent addPropertyChange(IJavaObjectInstance javaBean, String propertyName) {
		
		// Find an existing propertyChangeListener
		EList eventInvocations = (EList) javaBean.eGet(JavaInstantiation.getSFeature(javaBean,JavaBeanEventUtilities.EVENTS));
		Iterator iter = eventInvocations.iterator();
		while(iter.hasNext()){
			Object eventInvocation = iter.next();
			if (eventInvocation instanceof PropertyChangeEventInvocation) {
				// See whether this listener can be used, which is the case if it doesn't have a PropertyEvent for the one being created
				PropertyChangeEventInvocation propChangeEventInvocation = (PropertyChangeEventInvocation)eventInvocation;
				// A propertyChangeEventInvocation that is added with the two argument method "addPropertyChangeListener(String propertyName, PropertyChangeListener listener)";
				// can't be reused
				if (propChangeEventInvocation.getAddMethod().getParameters().size() == 2) continue;
				
				Iterator propEvents = propChangeEventInvocation.getProperties().iterator();
				boolean propertyAlreadyUsed = false;
				while(propEvents.hasNext()) {
					PropertyEvent propEvent = (PropertyEvent)propEvents.next();
					if(propEvent.getPropertyName().equals(propertyName)){
						// This property listener can't be used
						propertyAlreadyUsed = true;
						break;
					}
				}
				// If the property isn't used then we can re-use the listener
				if(!propertyAlreadyUsed){
					PropertyEvent newPropertyEvent = JCMFactory.eINSTANCE.createPropertyEvent() ;
					newPropertyEvent.setUseIfExpression(true);
					newPropertyEvent.setPropertyName(propertyName);
					propChangeEventInvocation.getProperties().add(newPropertyEvent);
					return newPropertyEvent;
				}
			}
		}
		// Having got here there we no property listener that could be used.  Create a new one
		return addPropertyChange(javaBean,propertyName,null);
	}
	/**
	 * Select the child edit part of the argument for the given property event
	 */
	protected static void selectPropertyEventChildEditPart(EditPart editPart, PropertyEvent propertyEvent) {
		if(editPart instanceof TreeEditPart){
			Iterator childEditParts = editPart.getChildren().iterator();
			while(childEditParts.hasNext()){
				Object childEditPart = childEditParts.next();
				if(childEditPart instanceof PropertyEventEditPart){
					boolean childSelected = ((PropertyEventEditPart)childEditPart).selectPropertyEvent(propertyEvent);
					// If a child edit part was selected successfully then break
					if(childSelected) break;	
				}
			}
		} else {
			// We need to drive selection right to the editor part
			((EditDomain) editPart.getViewer().getEditDomain() ).selectModel(propertyEvent);
		}
	}
	/**
	 * Select the child edit part of the argument for the given callback
	 */
	public static void selectCallbackChildEditPart(EditPart editPart, Callback callback) {
		if(editPart instanceof TreeEditPart){
			Iterator childEditParts = editPart.getChildren().iterator();
			while(childEditParts.hasNext()){
				Object childEditPart = childEditParts.next();
				if(childEditPart instanceof CallbackEditPart){
					boolean callbackEditPartSelected = ((CallbackEditPart)childEditPart).selectCallback(callback);
					// If the callback child has been found and selected then break out of the loop
					if(callbackEditPartSelected) break;
				}
			}
		} else {
			// We need to drive selection right to the editor part
			((EditDomain) editPart.getViewer().getEditDomain() ).selectModel(callback);
		}
	}
}
