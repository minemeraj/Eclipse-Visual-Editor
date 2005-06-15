/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 * $RCSfile: JavaBeanTreeEditPart.java,v $ $Revision: 1.16 $ $Date: 2005-06-15 20:19:37 $
 */
package org.eclipse.ve.internal.java.core;

import java.text.Collator;
import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.ForwardedRequest;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionFilter;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.emf.DefaultTreeEditPart;
import org.eclipse.ve.internal.cde.emf.EditPartAdapterRunnable;

import org.eclipse.ve.internal.jcm.*;

/**
 * Tree Editpart for most basic JavaBeans. So that events are handled correctly, any other ones for beans should subclass this one.
 * 
 * @since 1.0.0
 */
public class JavaBeanTreeEditPart extends DefaultTreeEditPart implements IJavaBeanTreeContextMenuContributor {

	protected IErrorNotifier.ErrorListener fErrorListener = new IErrorNotifier.ErrorListenerAdapter() {

		public void errorStatusChanged() {
			CDEUtilities.displayExec(JavaBeanTreeEditPart.this, "REFRESH_VISUALS", new EditPartRunnable(JavaBeanTreeEditPart.this) { //$NON-NLS-1$

				protected void doRun() {
					refreshVisuals();
				}
			});
		}
	};

	protected IJavaObjectInstance javaModel; // Keep a typed version of the model for performance so save re-casting

	protected EStructuralFeature sfObjectEvents;

	protected Adapter listenerAdapter, eventInvocationAdapter;

	protected Adapter propertyChangeEventInvocationAdapter;

	protected List eventInvocationsListenedTo;

	protected List propertyChangeEventInvocationsListenedTo;

	public JavaBeanTreeEditPart(Object aModel) {
		super(aModel);
	}

	public void setModel(Object aModel) {
		super.setModel(aModel);
		javaModel = (IJavaObjectInstance) aModel;
		sfObjectEvents = JavaInstantiation.getSFeature(javaModel, JavaBeanEventUtilities.EVENTS);
	}

	protected EStructuralFeature getSFObjectEvents() {
		return sfObjectEvents;
	}

	public void activate() {
		super.activate();
		getErrorNotifier().addErrorListener(fErrorListener);
		((EObject) getModel()).eAdapters().add(getListenerAdapter());
	}

	protected Adapter getListenerAdapter() {
		if (listenerAdapter == null) {
			listenerAdapter = new EditPartAdapterRunnable(this) {
				protected void doRun() {
					// When we refresh our entire children we should remove any listeners that we have on the eventInvocation
					// instances
					// that we may have put there last time round when we got the children
					if (eventInvocationsListenedTo != null) {
						Iterator iter = eventInvocationsListenedTo.iterator();
						while (iter.hasNext()) {
							AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
							eventInvocation.eAdapters().remove(getEventInvocationAdapter());
						}
						eventInvocationsListenedTo.clear();
					}
					// We also remove any listening from any PropertyChangeEventInvocations
					if (propertyChangeEventInvocationsListenedTo != null) {
						Iterator iter = propertyChangeEventInvocationsListenedTo.iterator();
						while (iter.hasNext()) {
							AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
							eventInvocation.eAdapters().remove(getPropertyChangeEventInvocationAdapter());
						}
					}
					refreshChildren();
					refreshVisuals();
				}

				public void notifyChanged(Notification notification) {
					if (notification.getFeature() == getSFObjectEvents())
						queueExec(JavaBeanTreeEditPart.this, "EVENTS"); //$NON-NLS-1$
				}
			};
		}
		return listenerAdapter;
	}

	public void deactivate() {
		((EObject) getModel()).eAdapters().remove(getListenerAdapter());
		getErrorNotifier().removeErrorListener(fErrorListener);
		// Make sure we not listening to stale event invocations as these may still try and signal us when we are deactivated
		if (eventInvocationsListenedTo != null) {
			Iterator iter = eventInvocationsListenedTo.iterator();
			while (iter.hasNext()) {
				AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
				eventInvocation.eAdapters().remove(getEventInvocationAdapter());
			}
			eventInvocationsListenedTo.clear();
		}
		super.deactivate();
		if (fOverlayImage != null) {
			// d260378 - Bug in Motif in that while disposing of a TreeItem it will some
			// god-forsaken reason it tries to redisplay the item. Because of this
			// we can't dispose of the overlay image until after we put a null image into the
			// tree item for it.
			TreeItem w = (TreeItem) getWidget();
			if (!w.isDisposed())
				w.setImage((Image)null);

			fOverlayImage.dispose(); // Now we can get rid of it since tree item no longer has it.
		}
	}

	protected Image fOverlayImage;

	protected int fOverlayImageSeverity;

	public static class JavaBeansImageDescriptor extends CompositeImageDescriptor {

		Image fBaseImage;

		int fSeverity;

		public JavaBeansImageDescriptor(Image anImage, int severity) {
			fBaseImage = anImage;
			fSeverity = severity;
		}

		protected void drawCompositeImage(int width, int height) {
			ImageData bg;
			if ((bg = fBaseImage.getImageData()) == null)
				bg = DEFAULT_IMAGE_DATA;

			drawImage(bg, 0, 0);
			Point size = getSize();
			ImageData data = null;
			switch (fSeverity) {
				case IErrorHolder.ERROR_SEVERE:
					data = IErrorHolder.ErrorType.getSevereErrorImageOverlay().getImageData();
					drawImage(data, 0, size.y - data.height);
					break;
				case IErrorHolder.ERROR_WARNING:
					data = IErrorHolder.ErrorType.getWarningErrorImageOverlay().getImageData();
					drawImage(data, 0, size.y - data.height);
					break;
				case IErrorHolder.ERROR_INFO:
					data = IErrorHolder.ErrorType.getInformationErrorImageOverlay().getImageData();
					drawImage(data, 0, size.y - data.height);
					break;
			}
		}

		protected Point getSize() {
			return new Point(fBaseImage.getBounds().width, fBaseImage.getBounds().height);
		}
	}

	/**
	 * Get the error notifier to use for this editpart. Subclasses may override and supply a different one.
	 * The default is the error notifier that is on the model.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IErrorNotifier getErrorNotifier() {
		return (IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE);
	}
	
	protected Image getImage() {
		// See whether or not the JavaBean is in error
		IErrorNotifier errorNotifier = getErrorNotifier();
		int errorStatus = errorNotifier.getErrorStatus();
		// If no error then just use the image
		if (errorStatus == IErrorHolder.ERROR_NONE) {
			return super.getImage();
		} else {
			// Otherwise we use an overlay image. If we have an existing overlay image and it is for the wrong severity dispose it
			if (fOverlayImage != null && fOverlayImageSeverity != errorStatus) {
				fOverlayImage.dispose();
				fOverlayImage = null;
			}
			// Create an overlay image if required
			if (fOverlayImage == null) {
				Image anImage = super.getImage();
				if (anImage != null) {
					fOverlayImage = new Image(Display.getCurrent(), new JavaBeansImageDescriptor(anImage, errorStatus).getImageData());
					fOverlayImageSeverity = errorStatus;
				}
			}
			return fOverlayImage;
		}
	}

	// For all the subclasses that have children we wish them to be expanded by default
	// so that the user doesn't have to explicitly expand it
	protected void refreshVisuals() {
		super.refreshVisuals();
		if (checkTreeItem())
			((TreeItem) getWidget()).setExpanded(true);
	}

	/**
	 * Override this to return a list of model children
	 */
	protected List getChildJavaBeans() {
		return Collections.EMPTY_LIST;
	}

	public final List getModelChildren() {

		// During undo the order of event notification can mean that orphaned edit parts get notified of changes when they no longer have a parent
		if (getParent() == null)
			return Collections.EMPTY_LIST;
		// See whether or not we should be displaying children for events
		Integer showEvents = (Integer) ((EditDomain) getRoot().getViewer().getEditDomain()).getData(JavaVEPlugin.SHOW_EVENTS);
		// If showEvents is null then default it to be BASIC
		int intShowEvents = showEvents != null ? showEvents.intValue() : JavaVEPlugin.EVENTS_BASIC;
		if (intShowEvents != JavaVEPlugin.EVENTS_NONE) {
			// Merge the child JavaBeans with the children required to show events
			// The reason this is done by making getModelChildren() final and letting the user override getChildJavaBeans()
			// is to make sure that someone doesn't accidently forget to include the event child and it also lets us change how events are added in a
			// central way
			// rather than each EditPart subclass having to repeat the same code to include them
			List childJavaBeans = getChildJavaBeans();
			if (childJavaBeans != null) {
				List result = new ArrayList(childJavaBeans.size() + 1);
				result.addAll(getExistingEvents(intShowEvents));
				result.addAll(childJavaBeans);
				return result;
			} else {
				List result = new ArrayList(1);
				result.addAll(getExistingEvents(intShowEvents));
				return result;
			}
		} else {
			return getChildJavaBeans();
		}
	}

	public Object getAdapter(Class aKey) {
		Object result = super.getAdapter(aKey);
		if (result != null) {
			return result;
		} else if (aKey == IActionFilter.class) {
			return getActionFilter();
		} else if (aKey == IErrorHolder.class) {
			return getErrorNotifier();
		} else {
			// See if any of the MOF adapters on our target can return a value for the request
			Iterator mofAdapters = ((IJavaInstance) getModel()).eAdapters().iterator();
			while (mofAdapters.hasNext()) {
				Object mofAdapter = mofAdapters.next();
				if (mofAdapter instanceof IAdaptable) {
					Object mofAdapterAdapter = ((IAdaptable) mofAdapter).getAdapter(aKey);
					if (mofAdapterAdapter != null) { return mofAdapterAdapter; }
				}
			}
		}
		return null;
	}

	protected IActionFilter getActionFilter() {
		return JavaBeanActionFilter.INSTANCE;
	}

	public List getEditPolicies() {
		List result = new ArrayList();
		EditPolicyIterator i = super.getEditPolicyIterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}

	/**
	 * Return the existing events for our model. This is the relationship Object/events and will be a list of Events.xmi/EventInvocation objects
	 */
	protected List getExistingEvents(int eventDepth) {
		List eventInvocations = (List) javaModel.eGet(getSFObjectEvents());
		// There are two ways that events are shown, BASIC or EXPERT
		// Basic shows one child for each method invocation, and Expert breaks it down into more depth showing
		// the actual listeners in the tree
		switch (eventDepth) {
		case JavaVEPlugin.EVENTS_BASIC:
			return getBasicEvents(eventInvocations);
		case JavaVEPlugin.EVENTS_EXPERT:
			return getExpertEvents(eventInvocations);
		}
		return Collections.EMPTY_LIST;
	}

	protected Adapter getEventInvocationAdapter() {
		if (eventInvocationAdapter == null) {
			eventInvocationAdapter = new EditPartAdapterRunnable(this) {

				protected void doRun() {
					refreshChildren();
					refreshVisuals();
				}
				
				public void notifyChanged(Notification notification) {
					if (notification.getFeatureID(AbstractEventInvocation.class) == JCMPackage.ABSTRACT_EVENT_INVOCATION__CALLBACKS)
						queueExec(JavaBeanTreeEditPart.this, "CALLBACKS"); //$NON-NLS-1$
				}
			};
		}
		return eventInvocationAdapter;
	}

	protected Adapter getPropertyChangeEventInvocationAdapter() {
		if (propertyChangeEventInvocationAdapter == null) {
			propertyChangeEventInvocationAdapter = new EditPartAdapterRunnable(this) {
				protected void doRun() {
					refreshChildren();
					refreshVisuals();
				}

				public void notifyChanged(Notification notification) {
					if (notification.getFeatureID(PropertyChangeEventInvocation.class) == JCMPackage.PROPERTY_CHANGE_EVENT_INVOCATION__PROPERTIES)
						queueExec(JavaBeanTreeEditPart.this, "EVENT_PROPERTIES"); //$NON-NLS-1$
				}
			};
		}
		return propertyChangeEventInvocationAdapter;
	}

	protected List getBasicEvents(List eventInvocations) {
		if (eventInvocations.size() == 0) { return Collections.EMPTY_LIST; }
		Iterator iter = eventInvocations.iterator();
		List result = new ArrayList(3);
		while (iter.hasNext()) {
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
			if (eventInvocation instanceof EventInvocation) {
				// For an event invocation the children are the callbacks, one per event
				Iterator callbacks = eventInvocation.getCallbacks().iterator();
				while (callbacks.hasNext()) {
					Callback callback = (Callback) callbacks.next();
					result.add(new EventCallback(eventInvocation, callback));
				}
				// Having iterated over the eventInvocations and added their callbacks as our children we need to listener to notifications
				// of when their callbacks change so that we can refresh ourself
				eventInvocation.eAdapters().add(getEventInvocationAdapter());
				// Lazy initialize the list that records which event invocations we are listenening to so we can remove the listeners later
				if (eventInvocationsListenedTo == null) {
					eventInvocationsListenedTo = new ArrayList(eventInvocations.size());
				}
				eventInvocationsListenedTo.add(eventInvocation);
			} else if (eventInvocation instanceof PropertyChangeEventInvocation) {
				// For a property event invocation the children are the properties
				PropertyChangeEventInvocation propChangeEventInvocation = (PropertyChangeEventInvocation) eventInvocation;
				Iterator properties = propChangeEventInvocation.getProperties().iterator();
				while (properties.hasNext()) {
					PropertyEvent propEvent = (PropertyEvent) properties.next();
					result.add(propEvent);
				}
				// Listen to the PropertyChangeEventInvocation so that we know when new properties are added or removed and can update ourself
				propChangeEventInvocation.eAdapters().add(getPropertyChangeEventInvocationAdapter());
				if (propertyChangeEventInvocationsListenedTo == null)
					propertyChangeEventInvocationsListenedTo = new ArrayList(1);
				propertyChangeEventInvocationsListenedTo.add(propChangeEventInvocation);
			}
		}
		// Sort the result. Events go before properties, and they both get sorted by name
		Collections.sort(result, new Comparator() {

			Collator coll = Collator.getInstance(Locale.getDefault());

			public int compare(Object o1, Object o2) {
				// EventCallback gets sorted before properties and by method name within itself
				if (o1 instanceof EventCallback) {
					if (o2 instanceof PropertyEvent)
						return -1;
					return coll.compare(((EventCallback) o1).getCallback().getMethod().getName(), ((EventCallback) o2).getCallback().getMethod()
							.getName());
				} else {
					// PropertyEvent. - get sorted after EventCallback or else by name
					if (o2 instanceof EventCallback) {
						return 1;
					} else {
						return coll.compare(((PropertyEvent) o1).getPropertyName(), ((PropertyEvent) o2).getPropertyName());
					}
				}
			}
		});

		return result;
	}

	protected List getExpertEvents(List eventInvocations) {
		List result = new ArrayList(3);
		// If there is only one event then crete a single EventInvocationAndListener
		if (eventInvocations.isEmpty()) {
			return eventInvocations;
		} else if (eventInvocations.size() == 1) {
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventInvocations.get(0);
			Listener listener = eventInvocation.getListener();
			result.add(new EventInvocationAndListener(eventInvocation, listener));
			return result;
		} else {
			// If there is more than one event invocation we need to turn things around so that
			// the child in the tree is the actual listener itself - and its children will represent each of the JavaEvents that we use to point to
			// it
			Iterator iter = eventInvocations.iterator();
			while (iter.hasNext()) {
				AbstractEventInvocation eventInvocation = (AbstractEventInvocation) iter.next();
				Listener listener = eventInvocation.getListener();
				// See whether any of the existing EventInvocationListeners already use this listener
				Iterator iter1 = result.iterator();
				boolean foundExistingListener = false;
				while (iter1.hasNext()) {
					EventInvocationAndListener existing = (EventInvocationAndListener) iter1.next();
					if (existing.getListener() == listener) {
						foundExistingListener = true;
						existing.addEventInvocation(eventInvocation);
					}
				}
				if (!foundExistingListener)
					result.add(new EventInvocationAndListener(eventInvocation, listener));
			}
			return result;
		}
	}

	protected EditPart createChildEditPart(Object model) {
		return super.createChild(model);
	}

	protected final EditPart createChild(Object model) {
		// This method is final so that subclasses do not override and forget how to create the edit part of the JavaBeanEvents
		// Instead of overriding this subclasses should override createChildEditPart(Object);
		if (model instanceof EventInvocationAndListener) {
			return new EventInvocationAndListenerTreeEditPart((EventInvocationAndListener) model, javaModel, false);
		} else if (model instanceof EventCallback) {
			return new EventMethodTreeEditPart((EventCallback) model);
		} else if (model instanceof PropertyEvent) {
			return new PropertyEventTreeEditPart((PropertyEvent) model);
		} else {
			return createChildEditPart(model);
		}
	}

	class JavaBeanEventsEditPolicy extends AbstractEditPolicy {

		public Command getCommand(Request request) {
			if (JavaBeanEventUtilities.REQ_DELETE_EVENT_DEPENDANT.equals(request.getType())) {
				Object childToDelete = ((ForwardedRequest) request).getSender().getModel();
				// There are two types of children that could be being deleted. In Basic mode the child is the Callback, and
				// in expert mode it's an EventInvocationAndListener
				if (childToDelete instanceof Callback) {
					// if this callback is shared ... do not enable its delete
					if (((Callback) childToDelete).isSharedScope())
						return null;
					else
						return new DeleteEventCallbackCommand((Callback) childToDelete);
				} else if (childToDelete instanceof PropertyEvent) {
					return new DeletePropertyEventCommand((PropertyEvent) childToDelete);
				} else if (childToDelete instanceof EventInvocationAndListener) { return new DeleteEventInvocationAndListenerCommand(
						(EventInvocationAndListener) childToDelete); }
			}
			return null;
		}
	}

	class DeleteEventInvocationAndListenerCommand extends Command {

		EventInvocationAndListener eventInvocationAndListener;

		DeleteEventCallbackCommand deleteEventCallbackCommand;

		DeleteEventInvocationAndListenerCommand(EventInvocationAndListener anEventInvocationAndListener) {
			eventInvocationAndListener = anEventInvocationAndListener;
		}

		public void execute() {
			// Code this to remove the event invocation
			Iterator eventInvocations = eventInvocationAndListener.getEventInvocations().iterator();
			while (eventInvocations.hasNext()) {
				AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventInvocations.next();
				removeEventInvocation(javaModel, eventInvocation);
			}
		}
	}

	class DeleteEventCallbackCommand extends Command {

		Callback eventCallback;

		DeleteEventCallbackCommand(Callback aCallback) {
			eventCallback = aCallback;
		}

		public void execute() {
			//TODO Rich is going to get upset if I don't do this properly using command builders - Need to change later, including all of the other
			// event command execute() methods, JRW
			// Delete the child event
			// This cleans up the chain deleting the EventInvocation, Listener and Listener type as required
			//TODO This needs optimizing for VAJStyle 1 and 2 where we may want to get rid of implements clauses on shared listeners
			AbstractEventInvocation eventInvocation = (AbstractEventInvocation) eventCallback.eContainer();
			// First remove the callback from the event invocation
			eventInvocation.getCallbacks().remove(eventCallback);
			// Now if there are no more callbacks on the event invocation we should remove it from the JavaBean
			if (eventInvocation.getCallbacks().isEmpty()) {
				removeEventInvocation(javaModel, eventInvocation);
			}
		}
	}

	class DeletePropertyEventCommand extends Command {

		PropertyEvent propertyEvent;

		DeletePropertyEventCommand(PropertyEvent aPropertyEvent) {
			propertyEvent = aPropertyEvent;
		}

		public void execute() {
			PropertyChangeEventInvocation propertyChangeEventInvocation = (PropertyChangeEventInvocation) propertyEvent.eContainer();
			// First remove the propertyEvent from the PropertyChangeEvent invocation
			propertyChangeEventInvocation.getProperties().remove(propertyEvent);
			// Now if there are no more propertyEvents on the event invocation we should remove it from the JavaBean
			// and also remove the callback from it
			if (propertyChangeEventInvocation.getProperties().isEmpty()) {
				removeEventInvocation(javaModel, propertyChangeEventInvocation);
			}
		}
	}

	private void removeEventInvocation(IJavaObjectInstance aJavaModel, AbstractEventInvocation anEventInvocation) {
		// To remove an event invocation
		// 1) Remove all of its callbacks. This helps to ensure that any code in methods used by the callback ( such as
		// 	   if ( event.getSource() == ... ) {} type blocks in VAJava styles 1 or 2 get removed
		// 2) Take the eventInvocation off the JavaBean
		// 3) also unset the reference between it and the listener ( which may be shared )
		// 3) If having done this the listener is no longer used, then we should remove it

		// Step 1. Iterate over the callbacks removing them
		anEventInvocation.getCallbacks().clear();
		if (anEventInvocation instanceof PropertyChangeEventInvocation)
			((PropertyChangeEventInvocation) anEventInvocation).getProperties().clear();
		// Step 2. Take the eventInvocation of the javaBean itself
		((EList) javaModel.eGet(getSFObjectEvents())).remove(anEventInvocation);
		// Step 3. Unset the reference between the event invocation and the listener
		Listener listener = anEventInvocation.getListener();
		anEventInvocation.setListener(null);
		// If there are no callbacks remaining registered against the listener we should also remove the listener itself
		if (listener != null && listener.getListenedBy().isEmpty()) {
			// The Listener is owned by the BeanComposition
			ListenerType listenerType = listener.getListenerType();
			listenerType.getListeners().remove(listener);
			// It is now possible that the listener type has no more instances, in which case clean up by deleteing it
			if (listenerType.getListeners().isEmpty()) {
				//TODO Talk to Gili about whether he has a code gen adapter actually listening for this. I don't think there is right now
				BeanComposition beanComposition = JavaBeanEventUtilities.getBeanComposition(javaModel);
				beanComposition.getListenerTypes().remove(listenerType);
			}
		}
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("JAVABEANS_EVENTS", new JavaBeanEventsEditPolicy()); //$NON-NLS-1$
		installEditPolicy(CopyAction.REQ_COPY,new DefaultCopyEditPolicy());		
	}
}
