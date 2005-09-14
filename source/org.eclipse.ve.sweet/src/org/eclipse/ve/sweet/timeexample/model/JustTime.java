package org.eclipse.ve.sweet.timeexample.model;

import java.io.Serializable;
import java.util.Date;

public class JustTime implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date time;
	
	public JustTime() {
		time = new Date();
	}

	public JustTime(Date date) {
		this.time = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
