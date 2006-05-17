/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
 *  $RCSfile: AddEventWizard.java,v $
 *  $Revision: 1.15 $  $Date: 2006-05-17 20:14:52 $ 
 */

import java.text.MessageFormat;
import java.util.*;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.wizard.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.beaninfo.*;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;

import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;

import org.eclipse.ve.internal.jcm.*;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

public class AddEventWizard extends Wizard {
	
	private IJavaObjectInstance javaBean;
	private EStructuralFeature sfObjectEvents;	
	private EventSetDecorator eventSetDecorator;
	private PropertyDecorator propDecor;
	private Method beanMethod;
	private boolean useAdapterClass, useExistingEventInvocation;
	private AbstractEventInvocation existingEventInvocation;
	private JavaClass listenerImplements;
	private JavaClass listenerExtends;
	private EditPart editPart;
	private AddEventWizardPage eventPage;
	private Image javaBeanImage;
	private Method propertyTwoArgumentMethod; // The two argument method if one exists on the JavaBean: "addPropertyChangeListener(String propertyName, PropertyChangeListener listener)";
	private Method propertyAddMethodToUse;
		
	private static final Object[] EMPTY_STRING_MSG_PARM = new Object[] {""};	// For use when parms to format should be an empty string. //$NON-NLS-1$
			
	class AddEventWizardPage extends WizardPage {		
		private Button c1createNewListenerTwoArgumentMethod, c1createNewListenerSingleArgumentMethod, c1useExistingListener;
		private Composite stackComposite ,  propertyComposite , listenerComposite  ;	
		private StackLayout stackLayout;	
		private Tree tree;
		private Label label;	
		private Button c0useExistingListener, c0createNewListener, c0extendAdapter, c0implenentInterface;
		private String c0adapterName;
		// The data on the TreeItem for an event is the event it's for, together with any existing event that we detect
		class EventData{
			public EventSetDecorator eventSetDecor;
			public AbstractEventInvocation eventInvocation;
			EventData(EventSetDecorator anEventSetDecor, AbstractEventInvocation anEventInvocation){
				eventSetDecor = anEventSetDecor;
				eventInvocation = anEventInvocation;
			}
		}
		// The data on the TreeItem for a method is the method proxy, together with any existing callback we detect 
		class MethodData{
			public MethodProxy methodProxy;
			public Callback callback;
			MethodData(MethodProxy aMethodProxy){
				methodProxy = aMethodProxy;
			}
			MethodData(MethodProxy aMethodProxy, Callback aCallback){
				methodProxy = aMethodProxy;
				callback = aCallback;
			}
		}
		// The data on the TreeItem for a Property is the PropertyDecorator, together with any existing PropertyEvent we detect 
			class PropertyData{
				public PropertyDecorator propDecor;
				public PropertyEvent propEvent;
				PropertyData(PropertyDecorator aPropDecor){
					propDecor = aPropDecor;
				}
				PropertyData(PropertyDecorator aPropDecor, PropertyEvent aPropEvent){
					propDecor = aPropDecor;
					propEvent = aPropEvent;
				}
			}		
		
		AddEventWizardPage(){
			super("Add Event"); //$NON-NLS-1$
			setImageDescriptor(JavaVEPlugin.getWizardTitleImageDescriptor());
			setTitle(JavaMessages.AddEventWizard_Title); 
		}
		public IWizardPage getNextPage() {
			return null;
		}
		public void createControl(Composite parent){
				Composite c = new Composite(parent,SWT.NONE);
				setControl(c);			
				GridData cData = new GridData(GridData.FILL_BOTH);
				c.setLayoutData(cData);
				c.setLayout(new GridLayout(2,false));
							
				// The events are shown in an SWT tree
				tree = new Tree(c,SWT.BORDER);
				GridData tData = new GridData(GridData.FILL_BOTH);
				tData.heightHint = 200;		
				tData.widthHint = 200;				
				tree.setLayoutData(tData);		
		
				// To the right of the tree is a Composite in GridLayout with two columns
				Composite labelComposite = new Composite(c,SWT.NONE);
				GridData lData = new GridData(GridData.FILL_BOTH);		
				labelComposite.setLayoutData(lData);
				labelComposite.setLayout(new GridLayout(1,false));
				
				tree.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						TreeItem treeItem = (TreeItem)e.item;
						Object data = treeItem.getData();
						if ( data instanceof EventData ) {
							eventSetDecoratorItemSelected(treeItem,false);
						} else if ( data instanceof MethodData ) { 
							// Get the method decorator
							methodProxyItemSelected(treeItem,true);	
						} else if (data instanceof PropertyData ) {
							propertyDecoratorItemSelected(treeItem);						
						} else { 
							propertyComposite.setEnabled(false);
							listenerComposite.setEnabled(false);
							label.setText(JavaMessages.AddEventWizard_Tree_TreeItem_Unknown); 
							setPageComplete(false);	
							adjustSize(false);				
						}
					}
				});
							
				// To the right of the tree is a label
				label = new Label(labelComposite, SWT.WRAP);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				gridData.widthHint = 185;
				label.setLayoutData(gridData);
				
				// Under this is a separator label
				Label sep = new Label(labelComposite,SWT.SEPARATOR | SWT.HORIZONTAL);
				sep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
				// and beneath this a stack composite with three stacks
				stackComposite = new Composite(labelComposite,SWT.NONE);
				stackLayout = new StackLayout();
				stackComposite.setLayout(stackLayout);
				
				// Listener composite controls the listener creation
				listenerComposite = new Composite(stackComposite,SWT.NONE);
				
				// The stacks are for whether an event is selected, a method or a property
				propertyComposite = new Composite(stackComposite,SWT.NONE);
			    			    
			   	populateListenerComposite();
			   	populatePropertyComposite();
				populateTree();
				
				stackLayout.topControl = listenerComposite;
				stackComposite.layout();				
						
		}
		private void populateListenerComposite(){
			listenerComposite.setLayout(new GridLayout());
			GridData listenerCompositeData = new GridData(GridData.FILL_BOTH);
			listenerComposite.setLayoutData(listenerCompositeData);
			// Radio button says whether or not an existing listener should be used, or a new one
			c0useExistingListener = new Button(listenerComposite,SWT.RADIO);
			c0useExistingListener.setText(JavaMessages.AddEventWizard_API_Msg_UseExistingListener); 
			c0useExistingListener.setEnabled(false);
			c0createNewListener = new Button(listenerComposite,SWT.RADIO);
			c0createNewListener.setText(JavaMessages.AddEventWizard_API_Msg_CreateNewListener); 
			c0createNewListener.setEnabled(false);
			
			Composite extendImplementComposite = new Composite(listenerComposite,SWT.NONE);
			GridData eiCompData = new GridData();
			eiCompData.horizontalIndent = 20;
			extendImplementComposite.setLayoutData(eiCompData);
			
			extendImplementComposite.setLayout(new GridLayout(1,false));
			c0extendAdapter = new Button(extendImplementComposite,SWT.RADIO);
			c0extendAdapter.setText(MessageFormat.format(JavaMessages.AddEventWizard_Extends_Label, EMPTY_STRING_MSG_PARM)); 
			c0extendAdapter.setEnabled(false);
			c0implenentInterface = new Button(extendImplementComposite,SWT.RADIO);
			c0implenentInterface.setText(MessageFormat.format(JavaMessages.AddEventWizard_Implements_Label, EMPTY_STRING_MSG_PARM)); 
			c0implenentInterface.setEnabled(false);			
			
			// When a new listener is selected, we enable the radio choice for whether it's going to implement the interface
			// or extend the adapter
			c0createNewListener.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					// If the create new listener is chosen we enable the choices 
					createNewListenerSelected(c0createNewListener.getSelection());
				}
			});		
			
			c0extendAdapter.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					// If the extend adapter radio choice is selected then we should indicate this so that the performFinish
					// knows that an adapter has been chosen
					useAdapterClass = c0extendAdapter.getSelection();
				}
			});
			
			c0useExistingListener.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					// Record the fact the existing listener should be used
					useExistingEventInvocation = c0useExistingListener.getSelection();
				}				
			});
			
		}
		private void populatePropertyComposite(){
			
			propertyComposite.setLayout(new GridLayout(1,false));
			GridData propertyCompositeData = new GridData(GridData.FILL_BOTH);
			propertyComposite.setLayoutData(propertyCompositeData);			
			
			Label useExistingListenerMethodLabel = new Label(propertyComposite,SWT.NONE);
			useExistingListenerMethodLabel.setText(JavaMessages.AddEventWizard_compositeProperty_label_UseExistingListenerMethod); 
			
			c1useExistingListener = new Button(propertyComposite,SWT.RADIO);
			GridData useExistingListenerMethodButtonData = new GridData();
			useExistingListenerMethodButtonData.horizontalIndent = 20;
			c1useExistingListener.setLayoutData(useExistingListenerMethodButtonData);
			c1useExistingListener.setText("&addPropertyChange(PropertyChangeListener listener)");  //$NON-NLS-1$
			c1useExistingListener.setEnabled(false);
			// When the radio button to use the existing listener is selected or deselected this affects whether or not we can use it
			c1useExistingListener.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if(c1useExistingListener.getSelection()) {
						useExistingEventInvocation = true;
						propertyAddMethodToUse = null;						
					}					
				}
			});
			Label createNewListenerLabel = new Label(propertyComposite,SWT.NONE);
			createNewListenerLabel.setText(JavaMessages.AddEventWizard_compositeProperty_label_CreateNewListenerMethod); 
			
			c1createNewListenerSingleArgumentMethod = new Button(propertyComposite,SWT.RADIO);
			GridData createNewListenerSingleArgButtonData = new GridData();
			createNewListenerSingleArgButtonData.horizontalIndent = 20;
			// When the radio button for this is selected the propertyAddMethodToUse is nulled out.  The default single argument
			// "addPropertyChangeListener(PropertyChangeListener listener)"; will be used in performFinish();
			c1createNewListenerSingleArgumentMethod.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if(c1createNewListenerSingleArgumentMethod.getSelection()) {
						useExistingEventInvocation = false;						
						propertyAddMethodToUse = null;						
					}					
				}
			});			
			c1createNewListenerSingleArgumentMethod.setLayoutData(createNewListenerSingleArgButtonData);
			c1createNewListenerSingleArgumentMethod.setText("a&ddPropertyChange(PropertyChangeListener listener)");  //$NON-NLS-1$
			c1createNewListenerSingleArgumentMethod.setSelection(true);
						
			c1createNewListenerTwoArgumentMethod = new Button(propertyComposite,SWT.RADIO);
			GridData createNewListenerTwoArgButtonData = new GridData();
			createNewListenerTwoArgButtonData.horizontalIndent = 20;
			c1createNewListenerTwoArgumentMethod.setLayoutData(createNewListenerTwoArgButtonData);			
			c1createNewListenerTwoArgumentMethod.setText("add&PropertyChange(String propertyName, PropertyChangeListener listener)");  //$NON-NLS-1$
			c1createNewListenerTwoArgumentMethod.setEnabled(false);
			// When the radio button for this is selected the propertyAddMethodToUse is the two argument method
			// "addPropertyChangeListener(String propertyName, PropertyChangeListener listener)"; will be used in performFinish();
			// This method is searched for when the tree is built ( in populateTree ), and only if it is found is the radio button enabled
			c1createNewListenerTwoArgumentMethod.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if(c1createNewListenerTwoArgumentMethod.getSelection()) {
						useExistingEventInvocation = false;						
						propertyAddMethodToUse = propertyTwoArgumentMethod;						
					}			
				}
			});
		}
		
		/**
		 * Add items to the tree, the first level of which are all of the available eventDescriptors
		 * and beneath them all the methodDescriptors
		 */
		private void populateTree(){
			tree.removeAll();
			java.util.List sortedEvents = JavaBeanEventUtilities.getSortedEvents((JavaClass)javaBean.getJavaType());
			// Now we have a list of EventSetDescriptors sorted alphabetically
			Iterator iter = sortedEvents.iterator();
			while(iter.hasNext()){
				BeanEvent event = (BeanEvent)iter.next();
				EventSetDecorator eventSetDecor = Utilities.getEventSetDecorator(event);				
				TreeItem eventTreeItem = new TreeItem(tree,SWT.NONE);
				eventTreeItem.setText(eventSetDecor.getDisplayName());
				// See whether the event has an adapter we can use.  This affects the icon so the user knows
				// whether their new class will implement the interface or extend the adapter
				// We must see whether there is an existing event invocation that matches the event that can code generate onto
				AbstractEventInvocation existingInvocation = null;
				boolean existingEventInvocationIsAdapter = false;
				Iterator eventInvocations = ((List) javaBean.eGet(sfObjectEvents)).iterator();
				while(eventInvocations.hasNext()){
					AbstractEventInvocation eventInvocation = (AbstractEventInvocation)eventInvocations.next();
					// If the existing event invocation is for "propertyChange" then it can only be used if the add method
					// that introduces it to the JavaBean is single argument ( i.e., it's gonna get called back for all property events )
					if (eventInvocation instanceof PropertyChangeEventInvocation && ((PropertyChangeEventInvocation)eventInvocation).getAddMethod().getParameters().size() == 2) {
						// This property listener is specific to a particular property ( the one that's the argument value for the "addPropertyChangeListener(String propName, PropertyChangeListener listener)"
						// There maybe another listener that can be used however ( one that uses "addPropertyChangeListener(PropertyChangeListener listener)"
						// so we continue around the loop iterating the eventInvocations 
						continue; 
					}
					if ( eventInvocation.getListener().getListenedBy().size() == 1) {						
						ListenerType listenerType = eventInvocation.getListener().getListenerType();
						// TODO For now we are using listenerType.getName() to determine if non-anonymous inner class
						// Currently can't handle non-anon classes as an existing invocation in the add dialog, so
						// we won't say there is an existing invocation. However, in future, for style 2 we should,
						// in that case using name is not sufficient because it could be non-inner class and those
						// we can't do adds to.
						// NOTE: Also for style2: the test above for listenedBy.size == 1 would be invalid because
						// it would be listened to by more than one, but we should still allow adds.
						if (listenerType.getName() == null) {
							if (listenerType.getExtends() != null
								&& listenerType.getExtends().equals(eventSetDecor.getEventAdapterClass())) {
								// The listener extends the adapter, so it's the existingEventInvocation
								existingInvocation = eventInvocation;
								existingEventInvocationIsAdapter = true;
								break;
							} else if (listenerType.getImplements().indexOf(eventSetDecor.getListenerType()) != -1) {
								// The listener implements the interface, so it's the existingEventInvocation
								existingInvocation = eventInvocation;
								break;
							}
						}
					}
				}
				// The graphic to use depends on whether the listener exists or not
				// Also, the text used on the tree is different so that it is closer to the JavaBeans tree for existing listeners
				if (existingInvocation != null ){
					if (existingEventInvocationIsAdapter) {
						eventTreeItem.setImage(JavaBeanEventUtilities.getEvent_AnonAdapter_Image());
						eventTreeItem.setText( eventTreeItem.getText() + " - " + eventSetDecor.getEventAdapterClass().getName() ); //$NON-NLS-1$
					} else {
						eventTreeItem.setImage(JavaBeanEventUtilities.getEvent_AnonInterface_Image());
						eventTreeItem.setText( eventTreeItem.getText() + " - " + eventSetDecor.getListenerType().getName() );						 //$NON-NLS-1$
					}
				} else {
					// there is no existing event invocation, so we use gray images depending on whether
					// there is an adapter class to extend or not
					if (eventSetDecor.getEventAdapterClass() == null) {
						eventTreeItem.setImage(JavaBeanEventUtilities.getEvent_AnonInterface_BW_Image());
					} else {
						eventTreeItem.setImage(JavaBeanEventUtilities.getEvent_AnonAdapter_BW_Image());					
					}
				}
				// Add children to the tree item for the event callback methods themselves
				int firstPreferredChild = 0;
				int loopCounter = 0;
				if (event.getName().equals("propertyChange")) {  //$NON-NLS-1$
					// The JavaBeans spec states that there will be the method "addPropertyChangeListener(PropertyChangeListener listener)";
					// however there may also be a two argument method, "addPropertyChangeListener(String propertyName, PropertyChangeListener listener)";
					// We need to see whether this two argument method exists because this affects the radio button enablement and code generation that can occur
					ArrayList parmTypes = new ArrayList(2);
					parmTypes.add("java.lang.String"); //$NON-NLS-1$					
					parmTypes.add("java.beans.PropertyChangeListener"); //$NON-NLS-1$										
					propertyTwoArgumentMethod = ((JavaClass)javaBean.getJavaType()).getMethodExtended("addPropertyChangeListener",parmTypes); //$NON-NLS-1$
					c1createNewListenerTwoArgumentMethod.setEnabled(propertyTwoArgumentMethod != null);
					// Property change events are handled differently from regular EventSetDescriptors
					// Each java.beans.PropertyDescriptor that answers true to isBound is a candidate propertyChangeEvent
					Iterator boundProperties = JavaBeanEventUtilities.getBoundProperties((JavaClass)javaBean.getJavaType(),true).iterator();
					while(boundProperties.hasNext()){
						PropertyDecorator propDecor = (PropertyDecorator)boundProperties.next();
						TreeItem propertyTreeItem = new TreeItem(eventTreeItem,SWT.NONE);
						propertyTreeItem.setText(propDecor.getDisplayName());
						// There might be an existing property change listener
						if ( existingInvocation != null ) { 
							// For the existing invocation it is possible that it has an existing PropertyEvent for the property
							// This affects whether it is available or not, and a different graphic is used
							Iterator existingPropertyEvents = ((PropertyChangeEventInvocation)existingInvocation).getProperties().iterator();
							while(existingPropertyEvents.hasNext()){
								PropertyEvent existingPropertyEvent = (PropertyEvent)existingPropertyEvents.next();
								if(existingPropertyEvent.getPropertyName().equals(propDecor.getName())) {
									propertyTreeItem.setData(new PropertyData(propDecor,existingPropertyEvent));
								}  
							}	
						}
						// If we have no data for the TreeItem then no existing PropertyEvent was found
						if (propertyTreeItem.getData() == null) {
							propertyTreeItem.setData(new PropertyData(propDecor));				
							// Show the available arrow 
							propertyTreeItem.setImage(JavaBeanEventUtilities.getPropertyArrow_Available_Image());							
						} else {
							// Show the used arrow
							propertyTreeItem.setImage(JavaBeanEventUtilities.getPropertyArrowImage());							
						}
					}
					// For a property we don't store the event set decorator, but we do store the existing eventInvocation so that new PropertyEvents can be added to it
					eventTreeItem.setData(new EventData(eventSetDecor,existingInvocation));					
				} else {
					if ( eventSetDecor != null ) {
						eventTreeItem.setData(new EventData(eventSetDecor,existingInvocation));
						List listenerMethodDecs = new ArrayList(eventSetDecor.getListenerMethods().size());
						Iterator listenerMethodsIter = eventSetDecor.getListenerMethods().iterator();
						while(listenerMethodsIter.hasNext()){
							listenerMethodDecs.add(Utilities.getMethodDecorator((MethodProxy)listenerMethodsIter.next()));
						}
						
						Collections.sort(listenerMethodDecs, new Comparator() {
							Collator coll = Collator.getInstance(ULocale.getDefault().toLocale());
							public int compare(Object o1, Object o2) {
								return coll.compare(((MethodDecorator) o1).getDisplayName(), ((MethodDecorator) o2).getDisplayName());
							}
						});
						
						listenerMethodsIter = listenerMethodDecs.iterator();
						while (listenerMethodsIter.hasNext()) {
							MethodDecorator methodDecr = (MethodDecorator)listenerMethodsIter.next();
							MethodProxy methodProxy = (MethodProxy) methodDecr.getEModelElement();
							TreeItem methodTreeItem = new TreeItem(eventTreeItem,SWT.NONE);					
							methodTreeItem.setText(methodDecr.getDisplayName());
							// If the listener itself isn't being used, then the graphic must be a black and white event arrow
							if ( existingInvocation !=null ) {
								// It' possible that the method is already being used by the event invocation, in which case it becomes green
								Iterator existingCallbacks = existingInvocation.getCallbacks().iterator();
								while(existingCallbacks.hasNext()){
									Callback existingCallback = (Callback)existingCallbacks.next();
									if ( existingCallback.getMethod() == methodProxy.getMethod() ) {
										methodTreeItem.setImage(JavaBeanEventUtilities.getEventArrowImage());		
										methodTreeItem.setData(new MethodData(methodProxy, existingCallback));
										break;										
									}
								}																		
							}							
							// If the data is unset then it's not an event that's not used.  The image shows this, and the method data has just the methodProxy in it
							if ( methodTreeItem.getData() == null) {
								methodTreeItem.setImage(JavaBeanEventUtilities.getEventArrow_Available_Image());			
								methodTreeItem.setData(new MethodData(methodProxy));						
							}									
							// If the event is preferred it will be expanded.  Record this 
							if ( firstPreferredChild == 0 && eventSetDecor.isPreferred() ) {
								MethodDecorator methodDecor = Utilities.getMethodDecorator(methodProxy);
								if ( methodDecor.isPreferred() ) {
									firstPreferredChild = loopCounter;
								}
							}
							loopCounter ++;
						}
					}	
				}	
//				tree.pack();
				// The preferred events are expanded by default
				if ( eventSetDecor.isPreferred() ) {
					eventTreeItem.setExpanded(true);
					// If no-one is selected in the tree so far then select the first expanded item by default
					if ( tree.getSelectionCount() == 0 && eventTreeItem.getItemCount() > 0 ) { 
						// The event may have several children.  Go for the first preferred one
						TreeItem preferredMethod = eventTreeItem.getItems()[firstPreferredChild];
						tree.setSelection(new TreeItem[] { preferredMethod });
						// Having selected the child we need to update the radio choices and the label
						methodProxyItemSelected(preferredMethod,false);
					}
				}
			}												
		}
		protected void propertyDecoratorItemSelected(TreeItem treeItem){
			
			propertyComposite.setEnabled(true);
			c1createNewListenerSingleArgumentMethod.setEnabled(true);
			stackLayout.topControl = propertyComposite;
			stackComposite.layout();			
			beanMethod = null;			
						
			PropertyData propData = (PropertyData)treeItem.getData();
			propDecor = propData.propDecor;
			if(propDecor.getShortDescription() != null)
				label.setText(propDecor.getShortDescription().trim());
			else
				label.setText(JavaMessages.AddEventWizard_Label_NoDescription); 
			
			// If there is no existing PropertyEvent, and there is an existing listener then it can be re-used
			boolean canUseExistingListener = false;	
			if(propData.propEvent == null){
				// The parent of the tree item will have the existing listener if there is one
				EventData eventData = (EventData) treeItem.getParentItem().getData();
				existingEventInvocation = eventData.eventInvocation;
				if (existingEventInvocation != null){
					canUseExistingListener = true;	 
				} 
			}
			if (canUseExistingListener) {
				c1useExistingListener.setEnabled(true);				
				c1useExistingListener.setSelection(true);
				useExistingEventInvocation = true;
				c1createNewListenerSingleArgumentMethod.setSelection(false);
				c1createNewListenerTwoArgumentMethod.setSelection(false);				
			} else {
				c1useExistingListener.setSelection(false);
				c1useExistingListener.setEnabled(false);
				// If there is a two argument method available we use it by default, otherwise the single argumet one
				if(propertyTwoArgumentMethod == null){
					c1createNewListenerSingleArgumentMethod.setSelection(true);
					useExistingEventInvocation = false;
					c1createNewListenerTwoArgumentMethod.setSelection(false);					
				} else {
					c1createNewListenerTwoArgumentMethod.setEnabled(true);
					c1createNewListenerSingleArgumentMethod.setSelection(false);										
					c1createNewListenerTwoArgumentMethod.setSelection(true);	
					propertyAddMethodToUse = propertyTwoArgumentMethod;
					useExistingEventInvocation = false;
				}
			}
			 
			setPageComplete(true);					
		}
		
		protected void methodProxyItemSelected(TreeItem treeItem,boolean adjustSize){
			propDecor = null;
			listenerComposite.setEnabled(true);
			stackLayout.topControl = listenerComposite;
			stackComposite.layout();							
			
			eventSetDecoratorItemSelected(treeItem.getParentItem(),true);
			MethodData methodData = (MethodData)treeItem.getData();			
			// Enable the correct radio choices for whether a new listener will be created or an existing one used
			// We are only able to use a listener if the tree edit part's parent has an existing listener and the method isn't already used by an existing callback
			EventData eventData = (EventData) treeItem.getParentItem().getData();
			existingEventInvocation = eventData.eventInvocation;
			if (eventData != null && eventData.eventInvocation != null && methodData.callback == null) {
				// We have an event invocation and the method isn't already used
				c0useExistingListener.setEnabled(true);
				c0useExistingListener.setSelection(true);	
				useExistingEventInvocation = true;
				c0createNewListener.setSelection(false);
				c0createNewListener.setEnabled(true);
				createNewListenerSelected(false);
			} else {
				// We have an event invocation, however the method is already used so we can't allow it to be re-used
				c0createNewListener.setEnabled(true);
				if(!c0createNewListener.getSelection()) {
					c0createNewListener.setSelection(true); 
					createNewListenerSelected(true);
				}
				createNewListenerSelected(true);  // This method call updates the radio choices for whether we're going to extend the adapter or implement the interface				
				c0useExistingListener.setSelection(false);
				useExistingEventInvocation = false;				
				c0useExistingListener.setEnabled(false);
			}				
						
			MethodProxy methodProxy = methodData.methodProxy;
			MethodDecorator methodDecor = Utilities.getMethodDecorator(methodProxy);
			if (methodDecor.isSetShortDescription())
			   label.setText(methodDecor.getShortDescription().trim());
			else
			   label.setText(JavaMessages.AddEventWizard_Label_NoDescription); 
			// Set the BeanEvent and Method so the callback can be created
			beanMethod = methodProxy.getMethod();
						
			if (adjustSize) adjustSize(false) ;
			setPageComplete(true);
		}
		
		private void createNewListenerSelected(boolean aBoolean){
			if (aBoolean) {
				if (c0adapterName != null && c0adapterName.length() > 1) {
					c0extendAdapter.setEnabled(true);
				}
				c0implenentInterface.setEnabled(true);
			} else {
				// The create listener is not selected, so disable everything that allows it
				c0extendAdapter.setEnabled(false);
				c0implenentInterface.setEnabled(false);
			}			
		}
		
		protected void eventSetDecoratorItemSelected(TreeItem treeItem, boolean isMethod){
			propDecor = null;
			EventData eventData = (EventData)treeItem.getData();
			eventSetDecorator = eventData.eventSetDecor;
			// If the event is a propertyChange then enable the property controls and exit
			if(eventSetDecorator.getName().equals("propertyChange")){ //$NON-NLS-1$
				label.setText(eventSetDecorator.getShortDescription().trim()); //$NON-NLS-1$
				stackLayout.topControl = propertyComposite;
				c1useExistingListener.setEnabled(false);
				c1createNewListenerSingleArgumentMethod.setEnabled(false);
				c1createNewListenerTwoArgumentMethod.setEnabled(false);
				stackComposite.layout();
				return;
			}		
			// The listenerComposite should be the top layout.  If a method we selected don't both doing the layout()
			// cause it's going to be done by the methodProxyItemSelected method that called this
			
			if (!isMethod) {
				stackLayout.topControl = listenerComposite;
				listenerComposite.setEnabled(false);
				stackComposite.layout();
			}			
			// Show who the interface implements
			listenerImplements = eventSetDecorator.getListenerType();		
			listenerExtends = eventSetDecorator.getEventAdapterClass();	
			// If there is an adapter class then we extend this byu default
			useAdapterClass = listenerExtends != null;
			// There is also a label that shows which add method will be used to register the listener with the JavaBean
			// Update the radio buttons.  For just a selected event we can't do anything so they are all disabled
			c0createNewListener.setEnabled(false);
			// 
			c0useExistingListener.setEnabled(false);
			c0extendAdapter.setEnabled(false);
			
			 c0implenentInterface.setEnabled(false);
			// If an adapter is available we use it by default
			if (listenerExtends != null ){
				c0adapterName = listenerExtends.getQualifiedName();
				c0extendAdapter.setText(MessageFormat.format(JavaMessages.AddEventWizard_Extends_Label, new Object[] {c0adapterName}));	
				c0extendAdapter.setSelection(true);
				c0implenentInterface.setSelection(false);
			} else {
				c0adapterName = null;
				c0extendAdapter.setText(MessageFormat.format(JavaMessages.AddEventWizard_Extends_Label, EMPTY_STRING_MSG_PARM)); 
				c0extendAdapter.setSelection(false);
				c0implenentInterface.setSelection(true);		
			}
			c0implenentInterface.setText(MessageFormat.format(JavaMessages.AddEventWizard_Implements_Label, new Object[] {listenerImplements.getQualifiedName()}));	
			listenerComposite.layout(true);	// Force a new and calculated layout.			
			// Set the label to the short description and set the beanMethod to null
			// as the user can only add an event if a bean method has been set
			beanMethod = null;
			setPageComplete(false);
			if (eventSetDecorator.isSetShortDescription())
			    label.setText(eventSetDecorator.getShortDescription().trim());
			else
				label.setText(JavaMessages.AddEventWizard_Label_NoDescription); 
			if (!isMethod) { 
				adjustSize(false);
			}			
		}
		private void adjustSize(boolean force) {
			// Having changed the text for the implements: and extends: labels we may need to increase the dialog size if the labels need more width
			if (stackLayout.topControl != null) {
				stackComposite.layout();
				Point existingSize = stackLayout.topControl.getSize();
				Point preferredSize = stackLayout.topControl.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				if (preferredSize.x > existingSize.x || force) {
					Composite compositeToPack = stackLayout.topControl.getParent();
					while (compositeToPack != null) {
						compositeToPack.pack();
						if (compositeToPack instanceof Shell)
							break;
						compositeToPack = compositeToPack.getParent();
					}
				} else {
					// Even though the dialog doesn't need making larger, we should pack the control to make sure that any updated labels are shown correctly
					stackLayout.topControl.pack();
				}
			}
		}
		public void setVisible(boolean aBoolean){
			super.setVisible(aBoolean);
			if (aBoolean){
//				adjustSize(true);
				tree.setFocus();
				tree.getDisplay().asyncExec(new Runnable() {
					public void run() {
						// Needs to be in an async because the first time set visible the 
						// tree has not yet been layed out. So it can't determine whether
						// it is showing or not. Need to async it so that it gets layed out
						// and displayed so that the actual selection location can be brought
						// into view.
						tree.showSelection();	// Bring the selection into view.
					}
				});					
			}
		}
	}
	private String javaBeanDescription;
	public AddEventWizard(IJavaObjectInstance aJavaBean,EditPart anEditPart){
		javaBean = aJavaBean;
		sfObjectEvents = JavaInstantiation.getSFeature(javaBean, JavaBeanEventUtilities.EVENTS);		
		editPart = anEditPart;
		ILabelProvider provider = ClassDescriptorDecoratorPolicy.getPolicy(editPart).getLabelProvider(javaBean.eClass());		
		if ( provider != null && javaBean != null ) {
			javaBeanDescription = provider.getText(javaBean);
			setWindowTitle(JavaMessages.AddEventWizard_addEventTitle + " - " + javaBeanDescription);  //$NON-NLS-1$
			javaBeanImage = provider.getImage(javaBean);
		} else {
			setWindowTitle(JavaMessages.AddEventWizard_addEventTitle); 
		}
	}
	public Image getJavaBeanImage(){
		return javaBeanImage;
	}
	public void addPages(){
		eventPage =new AddEventWizardPage();
		addPage(eventPage);
	}
	public boolean performFinish() {
		// Create the listener based on the wizard preferences
		if (propDecor != null) {
			// Add a listener for property change
			PropertyEvent newPropertyEvent = null;
			if ( useExistingEventInvocation ) {
				// If the listener is to be added to we can just create the property event and add it right here
				newPropertyEvent = JCMFactory.eINSTANCE.createPropertyEvent() ;
				newPropertyEvent.setPropertyName(propDecor.getName());
				newPropertyEvent.setUseIfExpression(true);
				 ((PropertyChangeEventInvocation)existingEventInvocation).getProperties().add(newPropertyEvent);				
			} else {
				// The propertyAddMethod to use is either null ( in which case the static helper method finds the single argument method for us )
				// or else it is the two argument method
				newPropertyEvent = JavaBeanEventUtilities.addPropertyChange(javaBean,propDecor.getName(),propertyAddMethodToUse);								
			}
			// If our edit part is a tree then iterate its children and try and select the child edit part that is for the newly created property event
			JavaBeanEventUtilities.selectPropertyEventChildEditPart(editPart,newPropertyEvent);
		} else if (beanMethod != null) {
			// If we are going to use an existing listener we create a new callback and add it to the event invocation
			Callback callback = null;
			if ( useExistingEventInvocation ) {
				// The data is the method proxy of the event to be added to the existing listener
			 	callback = JCMFactory.eINSTANCE.createCallback();
				callback.setMethod(beanMethod);
				existingEventInvocation.getCallbacks().add(callback);				
			} else {
				// For a new class we can either extend the adapter or use the listener
				if ( useAdapterClass ) {
					callback = JavaBeanEventUtilities.addCallback(javaBean,eventSetDecorator,beanMethod,null,listenerExtends);
				} else {
					callback =JavaBeanEventUtilities.addCallback(javaBean,eventSetDecorator,beanMethod,listenerImplements,null);
				}
			}	
			// Now we've create the new callback we should select it so the JVE drives to the newly added line of code
			// This only works for the tree right now
			JavaBeanEventUtilities.selectCallbackChildEditPart(editPart,callback);
		}
		return true;
	}
}
