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

public class DatabindingService {

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
			} else {
				Pair other = (Pair) obj;
				return a.equals(other.a) && b.equals(other.b);
			}
		}

		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}
	}

	private SettableValue combinedValidationMessage = new SettableValue(
			String.class);

	private Map converters = new HashMap();

	private List createdUpdatables = new ArrayList();

	private Map tableFactories = new HashMap();

	private Map valueFactories = new HashMap();

	private List validationMessages = new ArrayList();

	private List partialValidationMessages = new ArrayList();

	private SettableValue validationMessage = new SettableValue(String.class);

	private SettableValue partialValidationMessage = new SettableValue(
			String.class);

	private DatabindingService parent;

	public DatabindingService(DatabindingService parent) {
		this.parent = parent;
		registerValueFactories();
		registerConverters();
	}

	public DatabindingService() {
		this(null);
	}

	public void addUpdatableTableFactory(Class clazz,
			IUpdatableTableFactory factory) {
		tableFactories.put(clazz, factory);
	}

	public void addUpdatableValueFactory(Class clazz,
			IUpdatableValueFactory factory) {
		valueFactories.put(clazz, factory);
	}

	public void bindTable(IUpdatableTable targetTable,
			IUpdatableTable modelTable) throws BindingException {
		if (!targetTable.getElementType().isAssignableFrom(
				modelTable.getElementType())) {
			throw new BindingException("no converter from "
					+ modelTable.getElementType() + " to "
					+ targetTable.getElementType());
		}
		Class[] modelColumnTypes = modelTable.getColumnTypes();
		if (targetTable.getColumnTypes().length != modelColumnTypes.length) {
			throw new BindingException("column counts don't match");
		}
		IConverter modelToTargetElementConverter = new IdentityConverter(
				targetTable.getElementType());
		IConverter[] modelToTargetLabelConverters = new IConverter[modelColumnTypes.length];
		for (int i = 0; i < modelToTargetLabelConverters.length; i++) {
			modelToTargetLabelConverters[i] = new IdentityConverter(
					modelColumnTypes[i]);
		}
		bindTable(targetTable, modelTable, modelToTargetElementConverter,
				modelToTargetLabelConverters);
	}

	public void bindTable(final IUpdatableTable targetTable,
			final IUpdatableTable modelTable,
			final IConverter modelToTargetElementConverter,
			final IConverter[] modelToTargetValueConverters)
			throws BindingException {
		final int columnCount = targetTable.getColumnTypes().length;
		if (modelTable.getColumnTypes().length != columnCount) {
			throw new BindingException("number of columns doesn't match");
		}
		checkConverterTypes(modelToTargetElementConverter, modelTable
				.getElementType(), targetTable.getElementType());
		for (int i = 0; i < columnCount; i++) {
			checkConverterTypes(modelToTargetValueConverters[i], modelTable
					.getColumnTypes()[i], targetTable.getColumnTypes()[i]);
		}

		IChangeListener listener = new IChangeListener() {
			public void handleChange(IChangeEvent changeEvent) {
				if (changeEvent.getUpdatable() == targetTable) {
					if (changeEvent.getChangeType() == ChangeEvent.CHANGE) {
						int row = changeEvent.getPosition();
						modelTable.setElementAndValues(row, targetTable
								.getElement(row), getConvertedModelValues(
								targetTable, modelToTargetValueConverters,
								columnCount, row));
					}
					// TODO ADD case, REMOVE case
				} else {
					if (changeEvent.getChangeType() == ChangeEvent.CHANGE) {
						int row = changeEvent.getPosition();
						targetTable.setElementAndValues(row,
								modelToTargetElementConverter
										.convert(changeEvent.getNewValue()),
								getConvertedModelValues(modelTable,
										modelToTargetValueConverters,
										columnCount, row));
					} else if (changeEvent.getChangeType() == ChangeEvent.ADD) {
						int row = changeEvent.getPosition();
						targetTable.addElementWithValues(row,
								modelToTargetElementConverter
										.convert(changeEvent.getNewValue()),
								getConvertedModelValues(modelTable,
										modelToTargetValueConverters,
										columnCount, row));
					} else if (changeEvent.getChangeType() == ChangeEvent.REMOVE) {
						int row = changeEvent.getPosition();
						targetTable.removeElement(row);
					}
				}
			}
		};
		targetTable.addChangeListener(listener);
		modelTable.addChangeListener(listener);
		// TODO filling a table for the first time is not efficient - need one
		// call for filling the table, or some beginChange/endChange scheme
		// TODO don't forget support for virtual tables!
		while (targetTable.getSize() > modelTable.getSize()) {
			targetTable.removeElement(0);
		}
		for (int i = 0; i < targetTable.getSize(); i++) {
			targetTable.setElementAndValues(i, modelToTargetElementConverter
					.convert(modelTable.getElement(i)),
					getConvertedModelValues(modelTable,
							modelToTargetValueConverters, columnCount, i));
		}
		while (targetTable.getSize() < modelTable.getSize()) {
			int index = targetTable.getSize();
			targetTable.addElementWithValues(index,
					modelToTargetElementConverter.convert(modelTable
							.getElement(index)), getConvertedModelValues(
							modelTable, modelToTargetValueConverters,
							columnCount, index));
		}
	}

	public void bindTable(Object target, Object targetFeature, Object model,
			Object modelFeature) throws BindingException {
		bindTable(createUpdatableTable(target, targetFeature),
				createUpdatableTable(model, modelFeature));
	}

	public void bindTable(Object target, Object targetFeature, Object model,
			Object modelFeature,
			final IConverter modelToTargetElementConverter,
			final IConverter[] modelToTargetValueConverters)
			throws BindingException {
		bindTable(createUpdatableTable(target, targetFeature),
				createUpdatableTable(model, modelFeature),
				modelToTargetElementConverter, modelToTargetValueConverters);
	}

	public void bindTable(Object target, Object targetFeature,
			IUpdatableTable model,
			final IConverter modelToTargetElementConverter,
			final IConverter[] modelToTargetValueConverters)
			throws BindingException {
		bindTable(createUpdatableTable(target, targetFeature), model,
				modelToTargetElementConverter, modelToTargetValueConverters);
	}

	/**
	 * Convenience method for binding the given target to the given model value,
	 * using converters obtained by calling getConverter().
	 * 
	 * @param target
	 *            the target value
	 * @param model
	 *            the model value
	 */
	public void bindValue(final IUpdatableValue target,
			final IUpdatableValue source) throws BindingException {
		final IConverter targetToModelConverter = getConverter(target
				.getValueType(), source.getValueType());
		final IConverter modelToTargetConverter = getConverter(source
				.getValueType(), target.getValueType());
		bindValue(target, source, targetToModelConverter,
				modelToTargetConverter);
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
	 * @param targetToModelConverter
	 *            the converter for converting from target values to model
	 *            values
	 * @param modelToTargetConverter
	 *            the converter for converting from model values to target
	 *            values
	 */
	public void bindValue(final IUpdatableValue target,
			final IUpdatableValue model,
			final IConverter targetToModelConverter,
			final IConverter modelToTargetConverter) {
		final IValidator targetValidator = getValidator(targetToModelConverter);
		bindValue(target, model, targetToModelConverter,
				modelToTargetConverter, targetValidator);
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
	 * @param source
	 *            the model value
	 * @param targetToModelConverter
	 *            the converter for converting from target values to model
	 *            values
	 * @param modelToTargetConverter
	 *            the converter for converting from model values to target
	 *            values
	 * @param targetValidator
	 *            the validator for validating updated target values
	 */
	public void bindValue(final IUpdatableValue target,
			final IUpdatableValue model,
			final IConverter targetToModelConverter,
			final IConverter modelToTargetConverter,
			final IValidator targetValidator) {
		ValueBinding valueBinding = new ValueBinding(this, target, model,
				targetToModelConverter, modelToTargetConverter, targetValidator);
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
	 */
	public void bindValue(Object targetObject, Object targetFeature,
			IUpdatableValue modelValue) throws BindingException {
		bindValue(createUpdatableValue(targetObject, targetFeature), modelValue);
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
	 */
	public void bindValue(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature) throws BindingException {
		bindValue(createUpdatableValue(targetObject, targetFeature),
				createUpdatableValue(modelObject, modelFeature));
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
	 * @param targetToModelConverter
	 *            the converter for converting from target values to model
	 *            values
	 * @param modelToTargetConverter
	 *            the converter for converting from model values to target
	 *            values
	 */
	public void bindValue(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature,
			final IConverter targetToModelConverter,
			final IConverter modelToTargetConverter) throws BindingException {
		bindValue(createUpdatableValue(targetObject, targetFeature),
				createUpdatableValue(modelObject, modelFeature),
				targetToModelConverter, modelToTargetConverter);
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
	 * @param targetToModelConverter
	 *            the converter for converting from target values to model
	 *            values
	 * @param modelToTargetConverter
	 *            the converter for converting from model values to target
	 *            values
	 * @param targetValidator
	 *            the validator for validating updated target values
	 */
	public void bindValue(Object targetObject, Object targetFeature,
			Object modelObject, Object modelFeature,
			IConverter targetToModelConverter,
			IConverter modelToTargetConverter, IValidator targetValidator)
			throws BindingException {
		bindValue(createUpdatableValue(targetObject, targetFeature),
				createUpdatableValue(modelObject, modelFeature),
				targetToModelConverter, modelToTargetConverter, targetValidator);
	}

	private void checkConverterTypes(
			final IConverter modelToTargetElementConverter, Class fromType,
			Class toType) throws BindingException {
		if (!modelToTargetElementConverter.getFromType().isAssignableFrom(
				fromType)
				|| !toType.isAssignableFrom(modelToTargetElementConverter
						.getToType())) {
			throw new BindingException(
					"converter from/to types don't match element types");
		}
	}

	public IUpdatableTable createUpdatableTable(Object object, Object feature)
			throws BindingException {

		Class clazz = object.getClass();
		IUpdatableTableFactory tableFactory = null;
		while (tableFactory == null && clazz != Object.class) {
			tableFactory = (IUpdatableTableFactory) tableFactories.get(clazz);
			clazz = clazz.getSuperclass();
		}
		IUpdatableTable result = null;
		if (tableFactory != null) {
			result = tableFactory.createUpdatableTable(object, feature);
		}
		if (result == null && parent != null) {
			result = parent.createUpdatableTable(object, feature);
		}
		if (result != null) {
			createdUpdatables.add(result);
			return result;
		}
		throw new BindingException(
				"Couldn't create an updatable table for object=" + object
						+ ", feature=" + feature);
	}

	/**
	 * Creates an updatable value from the given object and feature ID. This
	 * method looks up a factory registered for the given object's type. I the
	 * given object is itself an IUpdatableValue, this method creates a derived
	 * updatable value.
	 * 
	 * @param object
	 * @param featureID
	 * @return
	 * @throws BindingException
	 */
	public IUpdatableValue createUpdatableValue(Object object, Object featureID)
			throws BindingException {

		Class clazz = object.getClass();

		if (object instanceof IUpdatableValue) {
			return new DerivedUpdatableValue(this, ((IUpdatableValue) object),
					featureID);
		}

		IUpdatableValueFactory valueFactory = null;
		while (valueFactory == null && clazz != Object.class) {
			valueFactory = (IUpdatableValueFactory) valueFactories.get(clazz);
			clazz = clazz.getSuperclass();
		}

		IUpdatableValue result = null;
		if (valueFactory != null) {
			result = valueFactory.createUpdatableValue(object, featureID);
		}
		if (result == null && parent != null) {
			result = parent.createUpdatableValue(object, featureID);
		}
		if (result != null) {
			createdUpdatables.add(result);
			return result;
		}
		throw new BindingException(
				"Couldn't create an updatable value for object=" + object
						+ ", feature=" + featureID);
	}

	public void dispose() {
		for (Iterator it = createdUpdatables.iterator(); it.hasNext();) {
			IUpdatable updatable = (IUpdatable) it.next();
			updatable.dispose();
		}
	}

	public IUpdatableValue getCombinedValidationMessage() {
		return combinedValidationMessage;
	}

	private Object[] getConvertedModelValues(final IUpdatableTable modelTable,
			final IConverter[] modelToTargetValueConverters,
			final int columnCount, int index) {
		Object[] modelValues = modelTable.getValues(index);
		Object[] convertedValues = new Object[columnCount];
		for (int i = 0; i < columnCount; i++) {
			convertedValues[i] = modelToTargetValueConverters[i]
					.convert(modelValues[i]);
		}
		return convertedValues;
	}

	public IConverter getConverter(Class fromType, Class toType)
			throws BindingException {
		if (fromType == toType) {
			return new IdentityConverter(fromType, toType);
		} else {
			IConverter converter = (IConverter) converters.get(new Pair(
					fromType, toType));
			if (converter == null) {
				throw new BindingException("no converter");
			}
			return converter;
		}
	}

	public IUpdatableValue getPartialValidationMessage() {
		return partialValidationMessage;
	}

	public IConverter getStringToDoubleConverter() {
		IConverter doubleConverter = new IConverter() {
			public Object convert(Object object) {
				return new Double((String) object);
			}

			public Class getFromType() {
				return String.class;
			}

			public Class getToType() {
				return double.class;
			}
		};
		return doubleConverter;
	}

	public IConverter getToStringConverter(final Class fromClass) {
		IConverter toStringConverter = new IConverter() {
			public Object convert(Object object) {
				return object.toString();
			}

			public Class getFromType() {
				return fromClass;
			}

			public Class getToType() {
				return String.class;
			}
		};
		return toStringConverter;
	}

	public IUpdatableValue getValidationMessage() {
		return validationMessage;
	}

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

	protected void registerConverters() {
		IConverter doubleConverter = getStringToDoubleConverter();
		converters.put(new Pair(String.class, Double.class), doubleConverter);
		converters.put(new Pair(String.class, double.class), doubleConverter);
		IConverter integerConverter = new IConverter() {
			public Object convert(Object object) {
				return new Integer((String) object);
			}

			public Class getFromType() {
				return String.class;
			}

			public Class getToType() {
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
			validationSettableMessage.setValueAndNotify("");
		} else {
			validationSettableMessage.setValueAndNotify(((Pair) listOfPairs
					.get(listOfPairs.size() - 1)).b);
		}
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

}
