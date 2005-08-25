package org.eclipse.ve.sweet.objecteditor.test.model;

import java.util.Date;

public class Project {
	private String name;
	private Date startDate;
	private Date endDate;
	private boolean active;
	private Contact primaryContact;
	private Contact billingContact;
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Contact getBillingContact() {
		return billingContact;
	}
	public void setBillingContact(Contact billingContact) {
		this.billingContact = billingContact;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Contact getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(Contact primaryContact) {
		this.primaryContact = primaryContact;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
