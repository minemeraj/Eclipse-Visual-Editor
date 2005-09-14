package org.eclipse.ve.sweet.timeexample.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.ve.sweet.converter.ConverterRegistry;
import org.eclipse.ve.sweet.objectviewer.IDeleteHandler;
import org.eclipse.ve.sweet.objectviewer.IInsertHandler;
import org.eclipse.ve.sweet.validator.ValidatorRegistry;
import org.eclipse.ve.sweet.validators.DateValidator;

public class Model implements Serializable {
	
	private static final String filename = "/home/djo/consulting/timetracking.cps";
	
	private static Model model = null;
	public static Model getDefault() {
		if (model == null) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(filename));
				model = (Model) in.readObject(); 
			} catch (FileNotFoundException e) {
				model = new Model();
			} catch (IOException e) {
				e.printStackTrace();
				model = new Model();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				model = new Model();
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			registerDateTimeHandlers();
			sortWorkdays(model);
		}
		return model;
	}
	
	private static void sortWorkdays(Model model) {
		
		Workday[] workdays = (Workday[]) model.workdays.toArray(new Workday[model.workdays.size()]);
		Arrays.sort(workdays, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Workday w1 = (Workday) arg0;
				Workday w2 = (Workday) arg1;
				if (w1.getDate().getDate().equals(w2.getDate().getDate())) {
					return 0;
				}
				if (w1.getDate().getDate().before(w2.getDate().getDate())) {
					return -1;
				}
				if (w1.getDate().getDate().after(w2.getDate().getDate())) {
					return 1;
				}
				return 0;
			}
		});
		model.workdays = new LinkedList();
		for (int i = 0; i < workdays.length; i++) {
			model.workdays.addLast(workdays[i]);
		}
	}

	private static void registerDateTimeHandlers() {
		ValidatorRegistry.associate(JustDate.class.getName(), new DateValidator());
		ConverterRegistry.associate(String.class.getName(), JustDate.class.getName(), new ConvertString2JustDate());
		ConverterRegistry.associate(JustDate.class.getName(), String.class.getName(), new ConvertJustDate2String());

		ValidatorRegistry.associate(JustTime.class.getName(), new DateValidator());
		ConverterRegistry.associate(String.class.getName(), JustTime.class.getName(), new ConvertString2JustTime());
		ConverterRegistry.associate(JustTime.class.getName(), String.class.getName(), new ConvertJustTime2String());
	}

	public void save() {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(this);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = -9211800295949749545L;

	private LinkedList workdays = new LinkedList();

	public LinkedList getWorkdays() {
		return workdays;
	}

	public IDeleteHandler getWorkdaysDeleteHandler() {
		return new IDeleteHandler() {
			public boolean canDelete(int rowInCollection) {
				Workday day = (Workday) workdays.get(rowInCollection);
				if (day.getWork().isEmpty())
					return true;
				else
					return false;
			}

			public void deleteRow(int rowInCollection) {
				workdays.remove(rowInCollection);
			}
		};
	}
	
	public IInsertHandler getWorkdaysInsertHandler() {
		return new IInsertHandler() {
			public int insert(int positionHint) {
				Workday newDay = new Workday();
				workdays.addLast(newDay);
				return workdays.size()-1;
			}
		};
	}
}
