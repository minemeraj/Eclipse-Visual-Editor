/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jface.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.binding.internal.DerivedUpdatableValue;
import org.eclipse.jface.binding.internal.ValueBinding;

/**
 * @since 3.2
 */
public class DatabindingService {

	protected static class CollectionChangeListener implements IChangeListener {

		IUpdatableCollection source, target;

		/**
		 * @param source
		 * @param target
		 */
		public CollectionChangeListener(IUpdatableCollection source,
				IUpdatableCollection target) {
			this.source = source;
			this.target = target;
		}

		public void handleChange(IChangeEvent changeEvent) {
			if (changeEvent.getUpdatable() == source) {
				int row = changeEvent.getPosition();
				if (changeEvent.getChangeType() == IChangeEvent.CHANGE)
					target.setElement(row, changeEvent.getNewValue());
				else if (changeEvent.getChangeType() == IChangeEvent.ADD)
					target.addElement(changeEvent.getNewValue(), row);
				else if (changeEvent.getChangeType() == IChangeEvent.REMOVE)
					target.removeElement(row);
			}
		}
	}

	private static class Pair {

		private final Object a;

		private Object b;

		Pair(Object a, Object b) {
			this.a = a;
			this.b = b;
		}

		public boolean equals(Object obj) {
			if (obj.getClass() != Pair.class) {
				return false;
			}
			Pair other = (Pair) obj;
			return a.equals(other.a) && b.equals(other.b);
		}

		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}
	}

	private Map converters = new HashMap();

	private Map updatableFactories = new HashMap(50);

	private List createdUpdatables = new ArrayList();

	private boolean defaultIdentityConverter = true;

	private DatabindingService parent;

	private List partialValidationMessages = new ArrayList();

	private List validationMessages = new ArrayList();

	private SettableValue partialValidationMessage = new SettableValue(
			String.class, ""); //$NON-NLS-1$

	private SettableValue validationMessage = new SettableValue(String.class,
			""); //$NON-NLS-1$

	private SettableValue combinedValidationMessage = new SettableValue(
			String.class, ""); //$NON-NLS-1$

	/**
	 * 
	 */
	public DatabindingService() {
		this(null);
	}

	/**
	 * @param parent
	 */
	public DatabindingService(DatabindingService parent) {
		this.parent = parent;
		registerValueFactories();
		registerConverters();
	}

	/**
	 * @param clazz
	 * @param factory
	 */
	public void addUpdatableFactory(Class clazz, IUpdatableFactory factory) {
		updatableFactories.put(clazz, factory);
	}

	/**
	 * Convenience method for binding the given target to the given model value,
	 * using converters obtained by calling getConverter().
	 * 
	 * @param target
	 *            the target value
	 * @param model
	 *            the model value
	 * @throws BindingException
	 */
	public void bind(final IUpdatable target, final IUpdatable model)
			throws BindingException {
		IConverter converter = null;
		if (target instanceof IUpdatableValue)
			if (model instanceof IUpdatableValue) {
				IUpdatableValue tgt = (IUpdatableValue) target, mdl = (IUpdatableValue) model;
				converter = getConverter(tgt.getValueType(),
						mdl.getValueType(), isDefaultIdentityConverter());
			} else
				throw new BindingException(
						"Incompatible instances of IUpdatable");
		else if (target instanceof IUpdatableCollection)
			if (model instanceof IUpdatableCollection) {
				IUpdatableCollection tgt = (IUpdatableCollection) target, mdl = (IUpdatableCollection) model;
				converter = getConverter(tgt.getElementType(), mdl
						.getElementType(), isDefaultIdentityConverter());
			} else
				throw new BindingException(
						"Incompatible instances of IUpdatable");

		bind(target, model, converter);
	}

	/**
	 * Convenience method for binding the given target to the given model value,
	 * using the given converters to convert between target values and model
	 * values, and a validator obtained by calling
	 * getValidator(target.getValueType(), model.getValueType(),
	 * targetToModelValidator).
	 * 
	 * @param target
	 *            the target value
	 * @param model
	 *            the model value
	 * @param converter
	 *            the converter for converting from target values to model
	 *            values
	 * @throws BindingException
	 */
	public void bind(final IUpdatable target, final IUpdatable model,
			final IConverter converter) throws BindingException {
		final IValidator targetValidator = getValidator(converter);
		bind(target, model, converter, targetValidator);
	}

	/**
	 * @param target
	 * @param model
	 * @param converter
	 * @param targetValidator
	 * @throws BindingException
	 */
	public void bind(final IUpdatable target, final IUpdatable model,
			final IConverter converter, final IValidator targetValidator)
			throws BindingException {
		if (target instanceof IUpdatableValue)
			if (model instanceof IUpdatableValue) {
				IUpdatableValue tgt = (IUpdatableValue) target, mdl = (IUpdatableValue) model;
				bind(tgt, mdl, converter, targetValidator);
			} else
				throw new BindingException(
						"Incompatible instances of IUpdatable");
		else if (target instanceof IUpdatableCollection)
			if (model instanceof IUpdatableCollection) {
				IUpdatableCollection tgt = (IUpdatableCollection) target, mdl = (IUpdatableCollection) model;
				bind(tgt, mdl, converter, targetValidator);
			} else
				throw new BindingException(
						"Incompatible instances of IUpdatable");
	}

	/**
	 * 
	 * Binds two {@link org.eclipse.jface.binding.IUpdatableCollection}
	 * 
	 * @param targetCollection
	 * @param modelCollection
	 * @param converter
	 * @param validator
	 * @throws BindingException
	 * 
	 */
	public void bind(IUpdatableCollection targetCollection,
			IUpdatableCollection modelCollection, IConverter converter,
			final IValidator validator) throws BindingException {

		// TODO use a ValueBindings, and deal with validator

		// Verify element conversion types
		Class convertedClass = converter.getTargetType();
		if (!targetCollection.getElementType().isAssignableFrom(convertedClass)) {
			throw new BindingException("no converter from "
					+ convertedClass.getName() + " to "
					+ targetCollection.getElementType().getName());

		}
		convertedClass = converter.getModelType();
		if (!modelCollection.getElementType().isAssignableFrom(convertedClass)) {
			throw new BindingException("no converter from "
					+ convertedClass.getName() + " to "
					+ modelCollection.getElementType().getName());

		}

		// Start listening, giving that target the ability to "refresh" as we
		// set
		// it up with model values
		targetCollection.addChangeListener(new CollectionChangeListener(
				targetCollection, modelCollection));
		modelCollection.addChangeListener(new CollectionChangeListener(
				modelCollection, targetCollection));

		// Remove old, if any
		while (targetCollection.getSize() > 0)
			targetCollection.removeElement(0);

		// Set the target List with the content of the Model List
		for (int i = 0; i < modelCollection.getSize(); i++) {
			targetCollection.addElement(modelCollection.getElement(i), i);
		}

	}

	/**
	 * Binds the given target to the given model value, using the given
	 * converters to convert between target values and model values, and the
	 * given validator to validate target values. First, the target value will
	 * be set to the current model value, using the modelToTargetConverter.
	 * Subsequently, whenever one of the values changes, the other value will be
	 * updated, using the matching converter. The model value will only be
	 * updated if the given validator successfully validates the current target
	 * value.
	 * 
	 * @param target
	 *            the target value
	 * @param model
	 *            the model value
	 * @param converter
	 *            the converter for converting from target values to model
	 *            values
	 * @param targetValidator
	 *            the validator for validating updated target values
	 */
	public void bind(final IUpdatableValue target, final IUpdatableValue model,
			final IConverter converter, final IValidator targetValidator) {
		ValueBinding valueBinding = new ValueBinding(this, target, model,
				converter, targetValidator);
		target.addChangeListener(valueBinding);
		model.addChangeListener(valueBinding);
		valueBinding.updateTargetFromModel();
	}

	/**
	 * Convenience method for binding the given target object's feature to the
	 * given model value. This method uses createUpdatableValue() to obtain the
	 * IUpdatableValue objects used for the binding.
	 * 
	 * @param targetObject
	 *            the target object
	 * @param targetFeature
	 *            the feature identifier for the target object
	 * @param modelValue
	 *            the model value
	 * @throws BindingException
	 */
	public void bind(Object targetObject, Object targetFeature,
			IUpdatable modelValue) throws BindingException {
		bind(createUpdatable(targetObject, targetFeature), modelValue);
	}

	/**
	 * Convenience method for binding the given target object's feature to the
	 * given model object's feature. This method uses createUpdatableValue() to
	 * obtain the IUpdatableValue objects used for the binding.
	 * 
	 * @param targetObject
	 *            the target object
	 * @param targetFeature
	 *            the feature identifier for the target object
	 * @param modelObject
	 *            the model object
	 * @param modelFeature
	 *            the feature identifier for the model object
	 * @throws BindingException
	 */
	public void bind(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature) throws BindingException {
		bind(createUpdatable(targetObject, targetFeature), createUpdatable(
				modelObject, modelFeature));
	}

	/**
	 * Convenience method for binding the given target object's feature to the
	 * given model object's feature. This method uses createUpdatableValue() to
	 * obtain the IUpdatableValue objects used for the binding.
	 * 
	 * @param targetObject
	 *            the target object
	 * @param targetFeature
	 *            the feature identifier for the target object
	 * @param modelObject
	 *            the model object
	 * @param modelFeature
	 *            the feature identifier for the model object
	 * @param converter
	 *            the converter for converting from target values to model
	 *            values
	 * @throws BindingException
	 */
	public void bind(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature, final IConverter converter)
			throws BindingException {
		bind(createUpdatable(targetObject, targetFeature), createUpdatable(
				modelObject, modelFeature), converter);
	}

	/**
	 * Convenience method for binding the given target object's feature to the
	 * given model object's feature. This method uses createUpdatableValue() to
	 * obtain the IUpdatableValue objects used for the binding.
	 * 
	 * @param targetObject
	 *            the target object
	 * @param targetFeature
	 *            the feature identifier for the target object
	 * @param modelObject
	 *            the model object
	 * @param modelFeature
	 *            the feature identifier for the model object
	 * @param converter
	 *            the converter for converting from target values to model
	 *            values
	 * @param validator
	 * @throws BindingException
	 */
	public void bind(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature, IConverter converter,
			IValidator validator) throws BindingException {
		bind(createUpdatable(targetObject, targetFeature), createUpdatable(
				modelObject, modelFeature), converter, validator);
	}

	/**
	 * Creates an updatable value from the given object and feature ID. This
	 * method looks up a factory registered for the given object's type. I the
	 * given object is itself an IUpdatableValue, this method creates a derived
	 * updatable value.
	 * 
	 * @param object
	 *            is the instance we need an updatable for
	 * @param featureID
	 *            is the property designated the updatable object.
	 * @return updatable for the given object
	 * @throws BindingException
	 */

	public IUpdatable createUpdatable(Object object, Object featureID)
			throws BindingException {
		if (object instanceof IUpdatableValue)
			return new DerivedUpdatableValue(this, ((IUpdatableValue) object),
					featureID);
		else if (object instanceof IUpdatableCollection)
			throw new BindingException("TODO: need to implement this"); // TODO:

		return primCreateUpdatable(object.getClass(), object, featureID);
	}

	/**
	 * 
	 */
	public void dispose() {
		for (Iterator it = createdUpdatables.iterator(); it.hasNext();) {
			IUpdatable updatable = (IUpdatable) it.next();
			updatable.dispose();
		}
	}

	/**
	 * @return
	 */
	public IUpdatableValue getCombinedValidationMessage() {
		return combinedValidationMessage;
	}

	/**
	 * Get a registered converter between teh fromType and the toType
	 * 
	 * @param fromType
	 * @param toType
	 * @param createIdentity
	 *            if set to true, and no registered converter is found, create
	 *            an Identity one
	 * @return registered converter, Identity (if create Identity is true)
	 * @throws BindingException
	 *             if no converter is found
	 * 
	 */
	public IConverter getConverter(Class fromType, Class toType,
			boolean createIdentity) throws BindingException {
		if (fromType == toType) {
			return new IdentityConverter(fromType, toType);
		}
		IConverter converter = (IConverter) converters.get(new Pair(fromType,
				toType));
		if (converter == null && createIdentity)
			converter = new IdentityConverter(fromType, toType);
		else
			throw new BindingException("no converter");

		return converter;
	}

	/**
	 * @return the validation updatable
	 */
	public IUpdatableValue getPartialValidationMessage() {
		return partialValidationMessage;
	}

	/**
	 * @return the converter
	 */
	public IConverter getStringToDoubleConverter() {
		IConverter doubleConverter = new IConverter() {

			public Object convertTargetToModel(Object object) {
				return new Double((String) object);
			}

			public Object convertModelToTarget(Object aDouble) {
				return aDouble.toString();
			}

			public Class getModelType() {
				return String.class;
			}

			public Class getTargetType() {
				return double.class;
			}
		};
		return doubleConverter;
	}

	/**
	 * @param fromClass
	 * @return the converter
	 */
	public IConverter getToStringConverter(final Class fromClass) {
		IConverter toStringConverter = new IConverter() {

			public Object convertTargetToModel(Object object) {
				return object.toString();
			}

			public Object convertModelToTarget(Object aString) {
				return aString;
			}

			public Class getModelType() {
				return fromClass;
			}

			public Class getTargetType() {
				return String.class;
			}
		};
		return toStringConverter;
	}

	/**
	 * @return the validation updatable
	 */
	public IUpdatableValue getValidationMessage() {
		return validationMessage;
	}

	/**
	 * @param sourceConverter
	 * @return the validator
	 */
	public IValidator getValidator(IConverter sourceConverter) {
		return new IValidator() {

			public String isPartiallyValid(Object value) {
				return null;
			}

			public String isValid(Object value) {
				return null;
			}
		};
	}

	/**
	 * @return true if ...
	 */
	public boolean isDefaultIdentityConverter() {
		return defaultIdentityConverter;
	}

	/**
	 * 
	 * This create an adaptable for Object, using clazz and its supers for
	 * factories.
	 * 
	 * @param clazz
	 *            is the root class from which to look for registered updatable
	 *            factories
	 * @param object
	 *            is the instance we need an updatable for
	 * @param featureID
	 *            is the property designated the updatable object.
	 * @return updatable for the given object
	 * @throws BindingException
	 * 
	 */
	private IUpdatable primCreateUpdatable(Class clazz, Object object,
			Object featureID) throws BindingException {
		IUpdatableFactory factory = null;
		while (factory == null && clazz != Object.class) {
			factory = (IUpdatableFactory) updatableFactories.get(clazz);
			clazz = clazz.getSuperclass();
		}

		IUpdatable result = null;
		if (factory != null) {
			result = factory.createUpdatable(object, featureID);
			// It is possible that this (class) level's factory can not provide
			// an Updatable for featureID
			if (result == null) {
				result = createUpdatable(clazz, featureID);
			}
		}
		if (result == null && parent != null) {
			result = parent.createUpdatable(object, featureID);
		}
		if (result != null) {
			createdUpdatables.add(result);
			return result;
		}
		throw new BindingException(
				"Couldn't create an updatable value for object=" + object
						+ ", feature=" + featureID);
	}

	protected void registerConverters() {
		IConverter doubleConverter = getStringToDoubleConverter();
		converters.put(new Pair(String.class, Double.class), doubleConverter);
		converters.put(new Pair(String.class, double.class), doubleConverter);
		IConverter integerConverter = new IConverter() {

			public Object convertTargetToModel(Object aString) {
				return new Integer((String) aString);
			}

			public Object convertModelToTarget(Object anInteger) {
				return anInteger.toString();
			}

			public Class getModelType() {
				return String.class;
			}

			public Class getTargetType() {
				return int.class;
			}
		};
		converters.put(new Pair(String.class, Integer.class), integerConverter);
		converters.put(new Pair(String.class, int.class), integerConverter);
		converters.put(new Pair(Double.class, String.class),
				getToStringConverter(Double.class));
		converters.put(new Pair(double.class, String.class),
				getToStringConverter(double.class));
		converters.put(new Pair(Object.class, String.class),
				getToStringConverter(Object.class));
		converters.put(new Pair(Integer.class, String.class),
				getToStringConverter(Object.class));
		converters.put(new Pair(Integer.class, int.class),
				new IdentityConverter(Integer.class, int.class));
		converters.put(new Pair(int.class, Integer.class),
				new IdentityConverter(int.class, Integer.class));
	}

	protected void registerValueFactories() {
	}

	private void removeValidationListenerAndMessage(List listOfPairs,
			Object first) {
		for (int i = listOfPairs.size() - 1; i >= 0; i--) {
			Pair pair = (Pair) listOfPairs.get(i);
			if (pair.a.equals(first)) {
				listOfPairs.remove(i);
				return;
			}
		}
		return;
	}

	/**
	 * @param defaultIdentityConverter
	 */
	public void setDefaultIdentityConverter(boolean defaultIdentityConverter) {
		this.defaultIdentityConverter = defaultIdentityConverter;
	}

	/**
	 * @param listener
	 * @param partialValidationErrorOrNull
	 */
	public void updatePartialValidationError(IChangeListener listener,
			String partialValidationErrorOrNull) {
		removeValidationListenerAndMessage(partialValidationMessages, listener);
		if (partialValidationErrorOrNull != null) {
			partialValidationMessages.add(new Pair(listener,
					partialValidationErrorOrNull));
		}
		updateValidationMessage(
				combinedValidationMessage,
				partialValidationMessages.size() > 0 ? partialValidationMessages
						: validationMessages);
		updateValidationMessage(partialValidationMessage,
				partialValidationMessages);
	}

	/**
	 * @param listener
	 * @param validationErrorOrNull
	 */
	public void updateValidationError(IChangeListener listener,
			String validationErrorOrNull) {
		removeValidationListenerAndMessage(validationMessages, listener);
		if (validationErrorOrNull != null) {
			validationMessages.add(new Pair(listener, validationErrorOrNull));
		}
		updateValidationMessage(
				combinedValidationMessage,
				partialValidationMessages.size() > 0 ? partialValidationMessages
						: validationMessages);
		updateValidationMessage(validationMessage, validationMessages);
	}

	private void updateValidationMessage(
			SettableValue validationSettableMessage, List listOfPairs) {
		if (listOfPairs.size() == 0) {
			validationSettableMessage.setValueAndNotify(""); //$NON-NLS-1$
		} else {
			validationSettableMessage.setValueAndNotify(((Pair) listOfPairs
					.get(listOfPairs.size() - 1)).b);
		}
	}

}
