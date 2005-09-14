package org.eclipse.ve.sweet.timeexample.model;

import java.io.Serializable;
import java.util.Date;

public class JustDate implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date date;
	
	public JustDate() {
		date = new Date();
	}

	public JustDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
