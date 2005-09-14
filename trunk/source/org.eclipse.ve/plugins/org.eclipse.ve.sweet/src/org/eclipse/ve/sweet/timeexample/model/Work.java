package org.eclipse.ve.sweet.timeexample.model;

import java.io.Serializable;

import org.eclipse.ve.sweet.validator.IValidator;

public class Work implements Serializable {
	private static final long serialVersionUID = 479560041052854892L;
	
	private JustTime startTime = new JustTime();
	private JustTime endTime = new JustTime();
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public IValidator getDescriptionValidator() {
		return new IValidator() {
			public String isValidPartialInput(String fragment) {
				// Partial input is always valid
				return null;
			}
			public String isValid(Object value) {
				int descriptionLength = value.toString().length();
				if (descriptionLength < 1)
					return getHint();
				if (descriptionLength > 40)
					return getHint();
				return null;
			}
			public String getHint() {
				return "Please enter a description between 1 and 40 characters long.";
			}
		};
	}
	public JustTime getEndTime() {
		return endTime;
	}
	public void setEndTime(JustTime endTime) {
		this.endTime = endTime;
	}
	public JustTime getStartTime() {
		return startTime;
	}
	public void setStartTime(JustTime startTime) {
		this.startTime = startTime;
	}
}
