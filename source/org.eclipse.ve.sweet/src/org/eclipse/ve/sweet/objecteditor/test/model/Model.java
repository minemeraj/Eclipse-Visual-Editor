package org.eclipse.ve.sweet.objecteditor.test.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Model implements Serializable {
	private LinkedList clients = new LinkedList();
	private LinkedList staff = new LinkedList();
	private LinkedList work = new LinkedList();

	public LinkedList getClients() {
		return clients;
	}

	public void setClients(LinkedList clients) {
		this.clients = clients;
	}

	public LinkedList getStaff() {
		return staff;
	}

	public void setStaff(LinkedList staff) {
		this.staff = staff;
	}

	public LinkedList getWork() {
		return work;
	}

	public void setWork(LinkedList work) {
		this.work = work;
	}
}
