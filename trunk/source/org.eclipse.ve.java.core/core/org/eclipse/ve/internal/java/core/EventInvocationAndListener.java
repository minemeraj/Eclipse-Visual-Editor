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
 *  $RCSfile: EventInvocationAndListener.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */

import java.util.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.ve.internal.jcm.*;

public class EventInvocationAndListener{
	protected List eventInvocations;
	protected Listener listener;
	private List notifiers;
	private Adapter eventInvocationAdapter;
	public interface Observer{
		void eventInvocationChanged();	 
	}
	public EventInvocationAndListener(AbstractEventInvocation anEventInvocation, Listener aListener){
		eventInvocations = new ArrayList(1);
		eventInvocations.add(anEventInvocation);		
		registerInterestInEventInvocation(anEventInvocation);
		listener = aListener;
	}
	void addEventInvocation(AbstractEventInvocation anEventInvocation){
		eventInvocations.add(anEventInvocation);
		registerInterestInEventInvocation(anEventInvocation);		
	}
	private void registerInterestInEventInvocation(AbstractEventInvocation anEventInvocation){
		if (eventInvocationAdapter == null) {
			eventInvocationAdapter = new Adapter() {
				public void notifyChanged(Notification notification) {
					// If the eventInvocation fires its callback event then we fire an event
					//TODO Always fire it for now, later optimize to only be when the callback changes 
					notifyObservers();
				}
				public Notifier getTarget() {
					return null;
				}
				public void setTarget(Notifier newTarget) {
				}
				public boolean isAdapterForType(Object type) {
					return false;
				}
			};
		};
		anEventInvocation.eAdapters().add(eventInvocationAdapter);
	}
	private void notifyObservers(){
		if(notifiers != null){
			Iterator iter = notifiers.iterator();
			while(iter.hasNext()){
				Observer observer = (Observer)iter.next();
				observer.eventInvocationChanged();
			}
		}
	}
	protected Listener getListener(){
		return listener;
	}
	public List getEventInvocations(){
		return eventInvocations;
	}
	public void addNotifier(Observer notifier){
		if(notifiers == null) notifiers = new ArrayList(1);
		notifiers.add(notifier);
	}
	public void dispose(){
		
	}
}
