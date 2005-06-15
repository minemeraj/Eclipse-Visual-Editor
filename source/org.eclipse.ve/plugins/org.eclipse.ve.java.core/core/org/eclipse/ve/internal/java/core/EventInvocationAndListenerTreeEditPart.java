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
 * $RCSfile: EventInvocationAndListenerTreeEditPart.java,v $ $Revision: 1.10 $ $Date: 2005-06-15 20:19:38 $
 */

package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaEvent;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditPartRunnable;

import org.eclipse.ve.internal.jcm.*;

/**
 * This represents a listener together with the event invocations that are used by it for a JavaBean
 */
public class EventInvocationAndListenerTreeEditPart extends AbstractTreeEditPart implements CallbackEditPart, PropertyEventEditPart {

	protected IJavaObjectInstance javaBean;

	protected JavaEvent event; // hold the event in the case of only one event listener

	protected String listenerName; // classes are named, anonymous interfaces are not

	protected EventInvocationAndListener eventInvocationAndListener;

	protected boolean isShared = false;

	protected int type;

	protected static final int ANON_INTERFACE = 1;

	protected static final int ANON_CLASS = 2;

	protected static final int SINGLE_CLASS = 3;

	protected static final int MULTI_CLASS = 4;

	public EventInvocationAndListenerTreeEditPart(EventInvocationAndListener eventInvocationAndListener, IJavaObjectInstance aJavaBean, boolean shared) {
		super(eventInvocationAndListener);
		javaBean = aJavaBean;
		isShared = shared;
	}

	public void setModel(Object model) {
		eventInvocationAndListener = (EventInvocationAndListener) model;
		// We can be
		// 1) an anonymous inner class that implements an interface, e.g. implements java.awt.event.KeyListener
		// 2) an anonymous inner class that extends a class, e.g. extends java.awt.event.KeyAdapter
		// 3) a named listener class that we have a single event with
		// 3) a named listener class that we have more than one event with
		Listener listener = eventInvocationAndListener.getListener();
		ListenerType listenerType = listener.getListenerType();
		if (listenerType != null) {
			if (listenerType.isThisPart())
				listenerName = "this"; //$NON-NLS-1$
			else {
				listenerName = listenerType.getName();
				if (listenerName == null) {
					// We're anonymous. See whether we extend a class or not
					if (listenerType.getImplements().isEmpty()) {
						type = ANON_CLASS;
					} else {
						type = ANON_INTERFACE;
					}
				} else {
					// We have a named class. See whether we use it more than once
					List eventInvocations = eventInvocationAndListener.getEventInvocations();
					if (eventInvocations.size() <= 1) {
						type = SINGLE_CLASS;
					} else {
						type = MULTI_CLASS;
					}
				}
			}
		}
		// If we have a single event invocation then get its event name
		if (type != MULTI_CLASS) {
			List eventInvocations = eventInvocationAndListener.getEventInvocations();
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventInvocations.get(0);
			//TODO This is a hack to stop a class cast - it needs more thought - JRW
			if (eventInvocation instanceof EventInvocation) {
				event = ((EventInvocation) eventInvocation).getEvent();
			}
		}
		super.setModel(model);
		// Register a listener
		eventInvocationAndListener.addNotifier(new EventInvocationAndListener.Observer() {

			public void eventInvocationChanged() {
				// Where the same listener is being used more than once ( such as can occur with Style2 or 1 ) then
				// there can be two of us in the tree beneath the JavaBeanEditPart. One for the listener and the children of that
				// breaking out the actual event. The order of event firing can be that the actual event child refreshes itself after
				// it has been removed from the parent ( both of them register interest in the same EventInvocation )
				// so to stop GEF exceptions occuring we need to check to see whether we're still in the tree before we refresh ourself
				if (getParent() != null && getWidget() != null) {
					CDEUtilities.displayExec(EventInvocationAndListenerTreeEditPart.this, "REFRESH", new EditPartRunnable(EventInvocationAndListenerTreeEditPart.this) { //$NON-NLS-1$
						protected void doRun() {
							refreshVisuals();
							refreshChildren();
						}
					});
				}
			}
		});
	}

	/**
	 * Return the model children for this combination of an EventInvocation and Listener If the source JavaBean uses the listener more than once then
	 * the children are the events it uses it for otherwise the children are the methods used by the event invocation
	 */
	protected List getModelChildren() {
		if (type == MULTI_CLASS) {
			// If we have more than one event invocation for the same listener we need to break them apart for our children
			Iterator iter = eventInvocationAndListener.getEventInvocations().iterator();
			Listener listener = eventInvocationAndListener.getListener();
			List result = new ArrayList(eventInvocationAndListener.getEventInvocations().size());
			while (iter.hasNext()) {
				AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
				EventInvocationAndListener childInvocation = new EventInvocationAndListener(eventInvocation, listener);
				result.add(childInvocation);
			}
			return result;
		} else {
			List eventInvocations = eventInvocationAndListener.getEventInvocations();
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventInvocations.get(0);
			// Property events and EventSetDescriptor events have different children
			if (eventInvocation instanceof PropertyChangeEventInvocation) {
				// For a property the children are the PropertyEvent instances
				return ((PropertyChangeEventInvocation) eventInvocation).getProperties();
			} else {
				// For a regular event invocation the callbacks are the children
				return eventInvocation.getCallbacks();
			}
		}
	}

	/**
	 * Return the edit part for the child. This can either be a method, a PropertyEvent or it can be an EventInvocationAndListener
	 */
	protected EditPart createChild(Object child) {
		if (child instanceof Callback) {
			List eventInvocations = eventInvocationAndListener.getEventInvocations();
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventInvocations.get(0);
			return new EventMethodTreeEditPart((Callback) child, eventInvocation);
		} else if (child instanceof PropertyEvent) {
			// Child properties have an edit part
			return new PropertyEventTreeEditPart((PropertyEvent) child);
		} else {
			return new EventInvocationAndListenerTreeEditPart((EventInvocationAndListener) child, javaBean, true);
		}
	}

	public String getText() {
		StringBuffer buffer = new StringBuffer();
		// Render us as follows
		// 1) For an anonymous interface show the event name and the interface it extends ( unqualified )
		// 2) For an anpnymous class show the event name and the class it extends ( unqualified )
		if (event != null) {
			// Get the NLS name for the event
			buffer.append(Utilities.getEventSetDecorator(event).getDisplayName());
			buffer.append(" - "); //$NON-NLS-1$		
		} else {
			// If this is a property change listener that is using the two argument addPropertyChange(String propertyName,PropertyChangeListener
			// listener);
			// then we want to add the property name into the description to let the user be able to distinguish this from the single argument
			// listener
			if (eventInvocationAndListener.getEventInvocations().size() == 1
					&& eventInvocationAndListener.getEventInvocations().get(0) instanceof PropertyChangeEventInvocation) {
				PropertyChangeEventInvocation propEventInvocation = (PropertyChangeEventInvocation) eventInvocationAndListener.getEventInvocations()
						.get(0);
				if (propEventInvocation.getAddMethod().getParameters().size() == 2 && propEventInvocation.getProperties().size() > 0) {
					// This is a two argument method. Get the property name and append it to the label
					PropertyEvent propEvent = (PropertyEvent) propEventInvocation.getProperties().get(0);
					buffer.append(propEvent.getPropertyName());
					buffer.append(", "); //$NON-NLS-1$			
				}
			}
		}

		if (type == ANON_INTERFACE) {
			Listener listener = eventInvocationAndListener.getListener();
			buffer.append(((JavaClass) listener.getListenerType().getImplements().get(0)).getName());
		} else if (type == ANON_CLASS) {
			Listener listener = eventInvocationAndListener.getListener();
			// Anonymous inner classes can only extend one superclass
			JavaClass x = listener.getListenerType().getExtends();
			if (x != null) {
				buffer.append(x.getName());
			}
		}
		if (listenerName != null) {
			buffer.append(" ("); //$NON-NLS-1$
			buffer.append(listenerName);
			buffer.append(")"); //$NON-NLS-1$
		}

		return buffer.toString();
	}

	public Image getImage() {
		switch (type) {
		case ANON_INTERFACE:
			return JavaBeanEventUtilities.getEvent_AnonInterface_Image();
		case ANON_CLASS:
			return JavaBeanEventUtilities.getEvent_AnonAdapter_Image();
		default:
			if (isShared) {
				return JavaBeanEventUtilities.getEventPresentInterfaceImage(); // We are a child of a listener class used more than once - draw as
																			   // the interface
			} else {
				return JavaBeanEventUtilities.getEvent_UnsharedClass_Image();
			}
		}
	}

	class EventsInvocationAndListenerEditPolicy extends AbstractEditPolicy {

		public Command getCommand(Request request) {
			if (JavaBeanEventUtilities.REQ_DELETE_EVENT_DEPENDANT.equals(request.getType())) {
				final Object childToDelete = ((ForwardedRequest) request).getSender().getModel();
				// If the child is a Callback impl then it's just a callback that we can remove from the listener
				if (childToDelete instanceof Callback) {
					// If the callback is shared (inner listener used by multiple objects), do not enable any action
					if (((Callback) childToDelete).isSharedScope())
						return null;

					Command result = new Command() {

						Callback eventCallback = (Callback) childToDelete;

						public void execute() {
							// Remove the callback, but don't do any more cleanup. This way the listener will remain, because in the
							// JavaBeans viewer the user explicitly selected the method child of the callback in expert mode to delete, so we
							// don't cascade and delete the tree item parent. For this basic mode should be used.
							EventInvocation eventInvocation = (EventInvocation) eventCallback.eContainer();
							// Remove the callback from the event invocation
							// We don't delete the event invocation, and this way the listener being added still remains in the model, tree and
							// JavaSource
							// (During some cascaded or multiple delete scenarios it is possible the invocation is already orphaned, so we need to
							// chek to avoid a NPE)
							if (eventInvocation != null) {
								eventInvocation.getCallbacks().remove(eventCallback);
							}
						}
					};
					return result;
				} else if (childToDelete instanceof EventInvocationAndListener) {
					// In expert mode if the JavaBean uses the same listner for more than one event, then we have both of the event invocations
					// but our children split them out. This occurs with VAJ style 2 where the IvjEventHandler is shared across all JavaBeans
					// The deletion is done by removing basically forwarding the request to our parent, which will be the JavaBeanTreeEditPart
					// so that our child is deleted as though it were a direct child of the JavaBean, which would be the case if only one event were
					// being used by the JavaBean's shared listener
					return getParent().getCommand(request);
				} else if (childToDelete instanceof PropertyEvent) {
					try {
						// If we could not parse the if statements ... it is considered a shared property callback
						// Do not enable any action on it.
						if (!((PropertyEvent) childToDelete).isUseIfExpression())
							return null;
					} catch (Exception e) {
					}
					Command result = new Command() {

						PropertyEvent propertyEvent = (PropertyEvent) childToDelete;

						public void execute() {
							// Remove the callback, but don't do any more cleanup. This way the listener will remain, because in the
							// JavaBeans viewer the user explicitly selected the method child of the callback in expert mode to delete, so we
							// don't cascade and delete the tree item parent. For this basic mode should be used.
							PropertyChangeEventInvocation propChangeEventInvocation = (PropertyChangeEventInvocation) propertyEvent.eContainer();
							// Remove the PropertyEvent from the event invocation
							// We don't delete the event invocation, and this way the listener being added still remains in the model, tree and
							// JavaSource
							// Bugzilla Bug 47367 - If there is more than one property event for the same listener being deleted together
							// this caused an NPE so we need to check for null
							if (propChangeEventInvocation != null) {
								propChangeEventInvocation.getProperties().remove(propertyEvent);
							}
						}
					};
					return result;
				}
			}
			return null;
		}
	}

	/**
	 * Treat this as a child of the parent so that delete requests are forwarded to the parent
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		// Install an edit policy to forward delete requests to our parent. Instead of a regular DefaultComponentEditPolicy
		// we use a special request type, so that the container delete doesn't collide with event deletion
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {

			protected Command createDeleteCommand(GroupRequest deleteRequest) {
				ForwardedRequest forwardedRequest = new ForwardedRequest(JavaBeanEventUtilities.REQ_DELETE_EVENT_DEPENDANT, getHost());
				return getHost().getParent().getCommand(forwardedRequest);
			}
		});
		installEditPolicy("JAVABEANS_EVENTS", new EventsInvocationAndListenerEditPolicy()); //$NON-NLS-1$		
	}

	/*
	 * We represent a listener, so we must iterate through our children to see if any of them can select the callback
	 */
	public boolean selectCallback(Callback aCallback) {
		Iterator childEditParts = getChildren().iterator();
		while (childEditParts.hasNext()) {
			Object childEditPart = childEditParts.next();
			if (childEditPart instanceof CallbackEditPart) {
				boolean childSelectedCallback = ((CallbackEditPart) childEditPart).selectCallback(aCallback);
				if (childSelectedCallback)
					return true;
			}
		}
		return false;
	}

	public boolean selectPropertyEvent(PropertyEvent aPropertyEvent) {
		Iterator childEditParts = getChildren().iterator();
		while (childEditParts.hasNext()) {
			Object childEditPart = childEditParts.next();
			if (childEditPart instanceof PropertyEventEditPart) {
				boolean childSelectedPropertyEvent = ((PropertyEventEditPart) childEditPart).selectPropertyEvent(aPropertyEvent);
				if (childSelectedPropertyEvent)
					return true;
			}
		}
		return false;
	}

	public void activate() {
		super.activate();
		((TreeItem) getWidget()).setExpanded(true);
	}

	public void deactivate() {
		super.deactivate();
		// The edit part attaches event listeners to the eventInvocationAndListener. We should unregister these
		// when we are deactivated to allow GC of the edit parts
		eventInvocationAndListener.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IJavaElement.class) {
		// KLUDGE: Need to go to the Composition EP to get the JE. If we don't do this then the
		// JavaBrowsing perspective will go blank when this EP is selected. This is annoying.
		return getRoot().getContents().getAdapter(key); }
		return super.getAdapter(key);
	}

}
