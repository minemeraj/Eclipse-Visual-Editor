/**
 * 
 */
package org.eclipse.jface.binding.internal;

import org.eclipse.jface.binding.DatabindingService;
import org.eclipse.jface.binding.IChangeEvent;
import org.eclipse.jface.binding.IChangeListener;
import org.eclipse.jface.binding.IConverter;
import org.eclipse.jface.binding.IUpdatableValue;
import org.eclipse.jface.binding.IValidator;

public class ValueBinding implements IChangeListener {
	/**
	 * 
	 */
	private final DatabindingService context;

	private final IUpdatableValue target;

	private final IUpdatableValue model;

	private final IConverter modelToTargetConverter;

	private final IValidator validator;

	private final IConverter targetToModelConverter;

	public ValueBinding(DatabindingService context, IUpdatableValue target,
			IUpdatableValue model, IConverter targetToModelConverter,
			IConverter modelToTargetConverter, IValidator validator) {
		super();
		this.context = context;
		this.target = target;
		this.model = model;
		this.targetToModelConverter = targetToModelConverter;
		this.modelToTargetConverter = modelToTargetConverter;
		this.validator = validator;
	}

	public void handleChange(IChangeEvent changeEvent) {
		if (changeEvent.getUpdatable() == target) {
			if (changeEvent.getChangeType() == IChangeEvent.VERIFY) {
				// we are notified of a pending change, do validation
				// and
				// veto the change if it is not valid
				Object value = changeEvent.getNewValue();
				String partialValidationError = validator
						.isPartiallyValid(value);
				context.updatePartialValidationError(this,
						partialValidationError);
				if (partialValidationError != null) {
					changeEvent.setVeto(true);
				}
			} else {
				// the target (usually a widget) has changed, validate
				// the
				// value and update the source
				updateModelFromTarget();
			}
		} else {
			updateTargetFromModel();
		}
	}

	/**
	 * This also does validation.
	 */
	public void updateModelFromTarget() {
		Object value = target.getValue();
		String validationError = doValidateTarget(value);
		if (validationError == null) {
			try {
				model.setValue(targetToModelConverter.convertModel(value));
			} catch (Exception ex) {
				context.updateValidationError(this,
						"An error occurred while setting the value.");
			}
		}
	}

	public void validateTarget() {
		Object value = target.getValue();
		doValidateTarget(value);
	}

	private String doValidateTarget(Object value) {
		String validationError = validator.isValid(value);
		context.updatePartialValidationError(this, null);
		context.updateValidationError(this, validationError);
		return validationError;
	}

	public void updateTargetFromModel() {
		target.setValue(targetToModelConverter.convertTarget(model.getValue()));
	}
}