package org.eclipse.ve.sweet.timeexample.model;

import java.io.Serializable;
import java.util.LinkedList;

import org.eclipse.ve.sweet.objectviewer.IDeleteHandler;
import org.eclipse.ve.sweet.objectviewer.IInsertHandler;

public class Workday implements Serializable {
	private JustDate date = new JustDate();
	
	public JustDate getDate() {
		return date;
	}

	public void setDate(JustDate date) {
		this.date = date;
	}
	
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private LinkedList work = new LinkedList();

	public LinkedList getWork() {
		return work;
	}

	public IDeleteHandler getWorkDeleteHandler() {
		return new IDeleteHandler() {
			public boolean canDelete(int rowInCollection) {
				return true;
			}

			public void deleteRow(int rowInCollection) {
				work.remove(rowInCollection);
			}
		};
	}
	
	public IInsertHandler getWorkInsertHandler() {
		return new IInsertHandler() {
			public int insert(int positionHint) {
				Work workTime = new Work();
				work.addLast(workTime);
				return work.size()-1;
			}
		};
	}

}
