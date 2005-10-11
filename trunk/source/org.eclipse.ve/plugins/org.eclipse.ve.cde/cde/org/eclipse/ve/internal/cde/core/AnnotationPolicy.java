/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: AnnotationPolicy.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-11 21:26:01 $ 
 */

import java.util.*;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.ve.internal.cde.commands.*;
import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;
/**
 * This is a static policy for dealing with annotations.
 * It provides utility methods so that annotations are handled
 * consistently.
 */
public class AnnotationPolicy
 {


	/**
	 * Create an annotation. It will return the new annotation.
	 * This allows the caller to customize the annotation that will be added when
	 * commands are executed. Since the annotation is new, there is no need for the
	 * customization to be done through commands too.
	 *
	 * Then the annotation needs to be passed into the createAnnotationCommand method
	 * so that a command is created to add it into the system.
	 */
	public static Annotation createAnnotation(Object modelObject) {
		CDMFactory fact = CDMPackage.eINSTANCE.getCDMFactory();
		if (modelObject instanceof EObject)
			return fact.createAnnotationEMF();
		else
			return fact.createAnnotationGeneric();
	}

	/**
	 * This will return a command to add the annotations into the system.
	 * It will return null if there are no annotations to add. This will
	 * use the default add annotations command that was in the EditDomain.
	 *
	 * Note: This is not to be used in CreateRequest in ContainerEditPolicy,
	 * use the getCreateRequestCommand() in this class to do it, so that the
	 * order is preserved correctly. This is to be used only when annotations
	 * by themselves are being added, and the model object is already in the model.
	 */
	public static org.eclipse.gef.commands.Command getDefaultAddAnnotationsCommand(List annotations, EditDomain domain) {
		if (annotations.isEmpty())
			return null;

		try {
			AddAnnotationsCommand applyCmd = (AddAnnotationsCommand) domain.getDefaultAddAnnotationsCommandClass().newInstance();
			applyCmd.setDomain(domain);
			applyCmd.setAnnotations(annotations);
			return applyCmd;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This will return a command to apply a new model object to the annotation.
	 * The model should be live so that the connection can be succesfully made.
	 */
	public static Command getApplyModelToAnnotationCommand(Object modelObject, Annotation annotation, AnnotationLinkagePolicy linkage) {
		return new ApplyModelToAnnotationCommand(modelObject, annotation, linkage);
	}

	/**
	 * Return a command to delete the annotations from the DiagramData that
	 * they are in. It will also delete any VisualInfos associated with the
	 * annotations. It is assumed the list contains only Annotations.
	 *
	 * It will return null if there are no annotations to delete.
	 *
	 * NOTE: This is not to be used in DeleteDependentRequest in ContainerEditPolicy,
	 * use the getDeleteDependentRequest() in this class to do it, so that the order
	 * is preserved correctly. This is to be used only when annotations by themselves
	 * are being deleted, but the model object is still in the model (i.e. the model object
	 * no longer has an annotation).
	 */
	public static Command getDeleteCommand(List annotations, DiagramData diagramData) {
		if (annotations.isEmpty())
			return null;
		CommandBuilder cBldr = new CommandBuilder(""); //$NON-NLS-1$

		List vis = new ArrayList();
		Iterator itr = annotations.iterator();
		while (itr.hasNext()) {
			Annotation annotation = (Annotation) itr.next();

			if (annotation instanceof AnnotationEMF) {
				// We want to unset the annotates field. This is so that the annotation is no longer linked to the
				// model object.
				cBldr.cancelAttributeSetting(annotation, CDMPackage.eINSTANCE.getAnnotationEMF_Annotates());
			}
			// We don't do anything like this for AnnotationGeneric because not needed. When removed from the DiagramData it will be un-linked.

			// Add the VisualInfos to the vis list so that they can be removed from the diagrams later.
			vis.addAll(annotation.getVisualInfos());
		}

		// Now remove the annotations. In non-EMF case, this will un-register the ID, and anyone listening will stop listening.
		cBldr.cancelAttributeSettings(diagramData, CDMPackage.eINSTANCE.getDiagramData_Annotations(), annotations);

		// Now remove all of the VisualInfos from the Diagrams.
		cBldr.cancelGroupAttributeSetting(vis, CDMPackage.eINSTANCE.getVisualInfo_Diagram());

		return cBldr.getCommand();
	}

	/**
	 * This command is used in the DeleteDependentRequest of the ContainerEditPolicy. It makes sure
	 * that the annotations and visual infos are deleted at the right time.
	 */
	public static Command getDeleteDependentCommand(List annotations, Command deleteDependentCommand, DiagramData diagramData) {
		CompoundCommand c = new CompoundCommand();
		c.append(deleteDependentCommand);
		c.append(getDeleteCommand(annotations, diagramData));
		return c.unwrap();
	}

	/**
	 * Update the list with all annotations that this model object and children have attached to them.
	 *
	 * For the non-EMF case, this may not return anything because the connections between model and 
	 * annotation may not yet be live, nor may it be possible to find the children of the model object.
	 * The developer in the non-EMF case will have to know this and use another mechanism to find all
	 * of the children.
	 *
	 * It will return the same list, but it allows for a simple top level usage of 
	 * creating the list in the call itself (e.g. List a = getAllAnnotations(new ArrayList(), ...))
	 */
	public static List getAllAnnotations(List annotations, Object modelObject, AnnotationLinkagePolicy policy) {
		Annotation ann = policy.getAnnotation(modelObject);
		if (ann != null)
			annotations.add(ann);

		if (modelObject instanceof EObject) {
			EObject eo = (EObject) modelObject;
			Iterator itr = eo.eContents().iterator();
			while (itr.hasNext()) {
				Object n = itr.next();
				if (n instanceof EObject)
					getAllAnnotations(annotations, n, policy);
			}
		} else {
			// We will try to find containment.
			GenericAnnotationLinkagePolicy gPolicy = (GenericAnnotationLinkagePolicy) policy;
			Iterator itr = gPolicy.getContainedChildren(modelObject).iterator();
			while (itr.hasNext()) {
				getAllAnnotations(annotations, itr.next(), policy);
			}
		}

		return annotations;
	}
	
	/**
	 * Get all annotations for all of the model objects.
	 * @param annotations
	 * @param modelObjects
	 * @param policy
	 * @return
	 * 
	 * @see #getAllAnnotations(List, Object, AnnotationLinkagePolicy)
	 * @since 1.2.0
	 */
	public static List getAllAnnotations(List annotations, List modelObjects, AnnotationLinkagePolicy policy) {
		for (Iterator itr = modelObjects.iterator(); itr.hasNext();) {
			Object modelObject = itr.next();
			annotations = getAllAnnotations(annotations, modelObject, policy);
		}
		return annotations;
	}

	/**
	 * This command is used in the CreateRequest of the ContainerEditPolicy. It makes sure that
	 * the annotations are added at the right time.
	 * @param annotations list of annotations to create
	 * @param childCreateCommand the current set of commands to create the child
	 * @param domain the editdomain
	 * @return the command to do both the annotations and the child create.
	 * 
	 * @since 1.2.0
	 */
	public static Command getCreateRequestCommand(List annotations, Command childCreateCommand, EditDomain domain) {
		CompoundCommand c = new CompoundCommand(childCreateCommand.getLabel());
		c.append(getDefaultAddAnnotationsCommand(annotations, domain));
		c.append(childCreateCommand);
		return c.unwrap();
	}

	/**
	 * This will return the command to delete the VisualInfo passed in, and if there are no other VisualInfos
	 * or KeyedValues for the annotation, delete the annotation too.
	 *
	 * NOTE: It is important that this command is used to cancel the VisualInfo because it will make sure
	 * that linkages are correctly cleaned up.
	 */
	public static Command cancelVisualInfo(VisualInfo vi) {
		Annotation annotation = (Annotation) vi.eContainer();
		if (annotation.getVisualInfos().size() == 1 && annotation.getKeyedValues().size() == 0) {
			// This is the only Visual Info, and there are no key values, then get rid of the
			// entire annotation.
			return getDeleteCommand(Collections.singletonList(annotation), (DiagramData) annotation.eContainer());
		}

		// So just cancel the visual info.
		CommandBuilder cmdBldr = new CommandBuilder(null);
		cmdBldr.cancelAttributeSetting(annotation, CDMPackage.eINSTANCE.getAnnotation_VisualInfos(), vi);
		cmdBldr.cancelAttributeSetting(vi, CDMPackage.eINSTANCE.getVisualInfo_Diagram());
		return cmdBldr.getCommand();
	}

	/**
	 * This will return the command to delete the keyed valued from the annotation, and if there are no VisualInfos,
	 * and no other keyed values, it will also cancel the annotation.
	 *
	 * Note: It is important that this command is used to cancel settings on the annotation because it will
	 * cleanup correctly.
	 */
	public static Command cancelAnnotationSetting(Annotation ann, String key) {
		if (!ann.getKeyedValues().containsKey(key))
			return null; // Not set, so can't cancel.

		if (ann.getKeyedValues().size() == 1)
			return getDeleteCommand(Collections.singletonList(ann), (DiagramData) ann.eContainer()); // Cancel the entire visual info

		// There is more than one, so only cancel the one setting.
		CancelKeyedValueCommand cmd = new CancelKeyedValueCommand();
		cmd.setTarget(ann);
		cmd.setKey(key);
		return cmd;
	}

	/**
	 * Create the appropriate command to apply the Annotation setting.
	 * It will create the Annotation if necessary.
	 *
	 * Note: This is important to be used if the annotation may not yet be available. It will make
	 * sure that it is created correctly. If the annotation already exists, then a straight ApplyKeyedValueCommand
	 * could be used, however this is simpler in that it can be applied without worrying if it exists or not.
	 */
	public static Command applyAnnotationSetting(Object modelObject, BasicEMap.Entry newValue, EditDomain domain) {
		return new ApplyAnnotationCommand(modelObject, newValue, domain);
	}

	/**
	 * Return the IPropertyDescriptors that are applicable to the annotation on the given model object.
	 */
	public static List getAnnotationPropertyDescriptors(Object modelObject, EditDomain domain) {
		AnnotationLinkagePolicy annotationLinkagePolicy = domain.getAnnotationLinkagePolicy();
		if (annotationLinkagePolicy != null) {
			Annotation annotation = annotationLinkagePolicy.getAnnotation(modelObject);
			if (annotation != null) {
				ArrayList descs = new ArrayList(2);
				Iterator itr = annotation.getKeyedValues().iterator();
				while (itr.hasNext()) {
					BasicEMap.Entry kv = (BasicEMap.Entry) itr.next();
					IPropertyDescriptor desc = domain.getKeyedPropertyDescriptor(kv.getKey());
					if (desc != null)
						descs.add(desc);
				}
				return descs;
			}
		}
		return Collections.EMPTY_LIST;
	}

	public static final EObject SF_PARENT_ANNOTATION;
	static {
		SF_PARENT_ANNOTATION = CDMPackage.eINSTANCE.getEObject_ParentAnnotation();
	}

	/**
	 * A compound listener that listens for Annotation changes. It is abstract. Users should implement the two abstract methods
	 * to get notification of annotation being added/removed, or the annotation being modified.
	 *
	 * It notifies:
	 *   a) Annotation added/removed for the model of interest. This will be sent to notifyAnnotation.
	 *   b) Any Annotation changes, including KeyValues. Notifier will be Annotation. This will be sent to notifyAnnotationChanges.
	 */
	public static abstract class AnnotationListener implements GenericAnnotationLinkagePolicy.AnnotationLinkageListener {
		protected Object model;
		protected String modelID;
		protected GenericAnnotationLinkagePolicy linkage;
		protected Annotation annotation;
		protected Adapter modelAdapter, // When EMF model
		annotationAdapter;

		public AnnotationListener(Object model, EditDomain domain) {
			this.model = model;
			if (model instanceof EObject)
				setupModelAdapter(domain.getAnnotationLinkagePolicy());
			else
				setupModelListener((GenericAnnotationLinkagePolicy) domain.getAnnotationLinkagePolicy());
		}

		/**
		 * Annotation has been changed for the model being listened to.
		 * 
		 * @param eventType
		 * @param oldAnnotation
		 * @param newAnnotation
		 * 
		 * @since 1.0.0
		 */
		public abstract void notifyAnnotation(int eventType, Annotation oldAnnotation, Annotation newAnnotation);
		
		/**
		 * An annotation setting has been changed. If the notification is for a KeyedValues, then it will be broken up
		 * into individual notifications, one for each keyedvalue (if there were more than one) so that it can be treated
		 * as each being an individual notification.
		 * 
		 * @param msg
		 * 
		 * @since 1.0.0
		 */
		public abstract void notifyAnnotationChanges(Notification msg);

		/**
		 * Remove all listening.
		 */
		public void removeListening() {
			if (model != null) {
				if (model instanceof EObject) {
					((EObject) model).eAdapters().remove(modelAdapter);
					modelAdapter = null;
				} else {
					linkage.removeAnnotationLinkageListener(modelID, this);
					modelID = null;
					linkage = null;
				}
				removeAnnotationAdapter();
				model = null;
			}
		}

		private void setupModelAdapter(AnnotationLinkagePolicy link) {
			Notifier ro = (Notifier) model;
			modelAdapter = new AdapterImpl() {
				public void notifyChanged(Notification msg) {
					if (msg.getEventType() == Notification.REMOVING_ADAPTER)
						removeListening(); // My adapter is being removed from the model, so remove all listening.
					else if (
						msg.getEventType() == AnnotationEMF.ParentAdapter.PARENT_ANNOTATION_NOTIFICATION_TYPE
							&& msg.getFeature() == SF_PARENT_ANNOTATION) {
						switch (msg.getPosition()) {
							case Notification.SET :
								if (!msg.isReset()) {
									if (!msg.isTouch())
										setupAnnotationAdapter((Annotation) msg.getNewValue());
									notifyAnnotation(Notification.SET, (Annotation) msg.getOldValue(), (Annotation) msg.getNewValue());
									break;
								}	// else flow into unset.
							case Notification.UNSET :
								setupAnnotationAdapter(null);
								notifyAnnotation(Notification.UNSET, (Annotation) msg.getOldValue(), (Annotation) msg.getNewValue());
								break;
						}
					}

				}

			};
			ro.eAdapters().add(modelAdapter);
			setupAnnotationAdapter(link.getAnnotation(model));
		}

		private void setupModelListener(GenericAnnotationLinkagePolicy linkage) {
			this.linkage = linkage;
			modelID = linkage.getIDFromModel(model);
			linkage.addAnnotationLinkageListener(modelID, this);
			setupAnnotationAdapter(linkage.getAnnotation(model));
		}

		private void setupAnnotationAdapter(Annotation annotation) {
			if (annotationAdapter != null) {
				removeAnnotationAdapter();
			}

			this.annotation = annotation;
			if (annotation != null) {
				annotationAdapter = new AdapterImpl() {
					public void notifyChanged(Notification msg) {
						if (msg.getEventType() == Notification.REMOVING_ADAPTER)
							removeAnnotationAdapter(); // My adapter is being removed from the annotation, so remove listening
						else {
							processAnnotationChanges(msg); // Pass the changes on
						}
					}
				};
				annotation.eAdapters().add(annotationAdapter);
			}
		}
		
		private void processAnnotationChanges(Notification msg) {
			Notification[] keyedMsgs = KeyedValueNotificationHelper.notifyChanged(msg);
			if (keyedMsgs == null)
				notifyAnnotationChanges(msg);	// Not a keyed value.
			else {
				for (int i = 0; i < keyedMsgs.length; i++) {
					notifyAnnotationChanges(keyedMsgs[i]);
				}
			}
				
		}

		private void removeAnnotationAdapter() {
			if (annotationAdapter != null) {
				Adapter a = annotationAdapter;
				annotationAdapter = null; // So we don't get a loop on remove.
				annotation.eAdapters().remove(a);
				annotation = null;
			}
		}

		public void annotationLinkageChanged(GenericAnnotationLinkagePolicy.AnnotationLinkageEvent event) {
			if (event.getType() == GenericAnnotationLinkagePolicy.AnnotationLinkageEvent.REMOVING_LISTENER)
				removeListening(); // My adapter is being removed from the model, so remove all listening.
			else {
				switch (event.getType()) {
					case GenericAnnotationLinkagePolicy.AnnotationLinkageEvent.SET :
						setupAnnotationAdapter(((GenericAnnotationLinkagePolicy.AnnotationLinkageChangeEvent) event).getNewAnnotation());
						// Now drop into touch to send notification
					case GenericAnnotationLinkagePolicy.AnnotationLinkageEvent.TOUCH :
						notifyAnnotation(
							Notification.SET,
							((GenericAnnotationLinkagePolicy.AnnotationLinkageChangeEvent) event).getOldAnnotation(),
							((GenericAnnotationLinkagePolicy.AnnotationLinkageChangeEvent) event).getNewAnnotation());
						break;
					case GenericAnnotationLinkagePolicy.AnnotationLinkageEvent.UNSET :
						setupAnnotationAdapter(null);
						notifyAnnotation(
							Notification.UNSET,
							((GenericAnnotationLinkagePolicy.AnnotationLinkageChangeEvent) event).getOldAnnotation(),
							((GenericAnnotationLinkagePolicy.AnnotationLinkageChangeEvent) event).getNewAnnotation());
						break;
					case GenericAnnotationLinkagePolicy.AnnotationLinkageEvent.SET_ID :
						modelID = (String) event.getSource(); // Get the new id and save it.
						break;
				}
			}
		}
	}

}
