/*
 * Copyright (C) 2005 David Orme <djo@coconut-palm-software.com>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme     - Initial API and implementation
 */
package org.eclipse.ve.sweet.fieldviewer.swt.internal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ve.sweet.CannotSaveException;
import org.eclipse.ve.sweet.controllers.IMaster;
import org.eclipse.ve.sweet.fieldviewer.IFieldViewer;
import org.eclipse.ve.sweet.fieldviewer.swt.internal.ducktypes.IListIterable;
import org.eclipse.ve.sweet.hinthandler.DelegatingHintHandler;
import org.eclipse.ve.sweet.hinthandler.IHintHandler;
import org.eclipse.ve.sweet.metalogger.Logger;
import org.eclipse.ve.sweet.objectviewer.IDeleteHandler;
import org.eclipse.ve.sweet.objectviewer.IEditStateListener;
import org.eclipse.ve.sweet.objectviewer.IInputChangeListener;
import org.eclipse.ve.sweet.objectviewer.IInsertHandler;
import org.eclipse.ve.sweet.objectviewer.IObjectViewer;
import org.eclipse.ve.sweet.objectviewer.IObjectViewerFactory;
import org.eclipse.ve.sweet.objectviewer.IPropertyEditor;
import org.eclipse.ve.sweet.objectviewer.NullProperty;
import org.eclipse.ve.sweet.objectviewer.ObjectViewerFactory;
import org.eclipse.ve.sweet.reflect.DuckType;
import org.eclipse.ve.sweet.table.CompositeTable;
import org.eclipse.ve.sweet.table.IRowConstructionListener;
import org.eclipse.ve.sweet.table.IRowContentProvider;
import org.eclipse.ve.sweet.table.IRowFocusListener;

/**
 * Class CompositeTableViewer.  An IFieldViewer that binds the objects in any 
 * IListIterable (duck typed) property to the row objects in a CompositeTableViewer.
 * This object will iterate over all objects in the row object's tab order and will
 * use ObjectViewerFactory.construct() to generate editors for each of these controls.
 * It uses Java reflection to figure out the names of the corresponding property
 * for each control and binds each control to an object property of the same name.
 * 
 * @author djo
 */
public class CompositeTableViewer implements IFieldViewer, IMaster {
	private static final String COLUMN_BINDING = "ColumnBinding";

	private CompositeTable table = null;

    private IObjectViewer objectViewer;
    private IPropertyEditor property;
    private IListIterable collection;
    
    private ListIterator contents;
    int position=-1;
    
	private DelegatingHintHandler hintHandler = new DelegatingHintHandler();

	private boolean dirty = false;
	
	/**
	 * Constructor CompositeTableViewer.  Construct a CompositeTableViewer.
	 * 
	 * @param control The CompositeTable object to automate.
	 * @param objectViewer The IObjectViewer encapsulating the object we're editing
	 * @param propertyEditor The property of the object we're editing
	 */
	public CompositeTableViewer(Object control, IObjectViewer objectViewer, IPropertyEditor propertyEditor) {
		table = (CompositeTable) control;
		this.objectViewer = objectViewer;
		
		table.addInsertHandler(controllerInsertHandler);
		table.addDeleteHandler(controllerDeleteHandler);
		table.addRowConstructionListener(rowConstructionListener);
		table.addRowContentProvider(rowContentProvider);
		table.addRowFocusListener(rowFocusListener);
		
		try {
			setInput(propertyEditor);
		} catch (CannotSaveException e) {
			throw new RuntimeException("Should not get CannotSaveException on construction!");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setInput(org.eclipse.ve.sweet.objectviewer.IPropertyEditor)
	 */
	public void setInput(IPropertyEditor input) throws CannotSaveException {
		property = input;
		
		if (input instanceof NullProperty) {
			return;
		}

		collectionInsertHandler = property.getInsertHandler();
		collectionDeleteHandler = property.getDeleteHandler();
		
		Object propValue = property.get();
		if (!DuckType.instanceOf(IListIterable.class, propValue)) {
			throw new RuntimeException("Unable to use a CompositeTableViewer to edit a " + propValue.getClass().getName());
		}
		collection = (IListIterable) DuckType.implement(IListIterable.class, propValue);
		newIterator();
		table.setNumRowsInCollection(collection.size());
	}
	
	// Iterator utility methods ----------------------------------------------------------------

	private void newIterator() {
		contents = collection.listIterator();
		position=0;
	}
	
	private Object next() {
		Object result = contents.next();
		++position;
		return result;
	}
	
	private Object previous() {
		Object result = contents.previous();
		--position;
		return result;
	}
	
	/**
	 * Returns the object at newPosition, leaving the iterator one past newPosition.
	 * 
	 * @param newPosition The 0-based position of the object to return.
	 * @return The object at newPosition
	 */
	private Object objectAt(int newPosition) {
		Object result = null;
		if (position == newPosition) {
			return next();
		}
		if (position < newPosition) {
			while (position <= newPosition) {
				result = next();
			}
			return result;
		}
		if (position > newPosition) {
			while (position > newPosition) {
				result = previous();
			}
			next();
			return result;
		}
		// Never executed...
		return result;
	}
	
	// More property methods --------------------------------------------------------------

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#getInput()
	 */
	public IPropertyEditor getInput() {
		return property;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#getPropertyName()
	 */
	public String getPropertyName() {
		return property.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setHintHandler(org.eclipse.ve.sweet.hinthandler.IHintHandler)
	 */
	public void setHintHandler(IHintHandler hintHandler) {
		this.hintHandler.delegate = hintHandler;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#isDirty()
	 */
	public boolean isDirty() {
		return dirty || getCurrentRowObjectViewer().isDirty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#setDirty(boolean)
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		if (!dirty) {
			getCurrentRowObjectViewer().rollback();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#undo()
	 */
	public void undo() {
		getCurrentRowObjectViewer().rollback();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#save()
	 */
	public void save() throws CannotSaveException {
		getCurrentRowObjectViewer().commit();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.fieldviewer.IFieldViewer#validate()
	 */
	public String validate() {
		if (table.getNumRowsInCollection() < 1) {
			return null;
		}
		
		IObjectViewer currentRowObjectViewer = getCurrentRowObjectViewer();
		if (currentRowObjectViewer == null) {
			return null;
		}
		String error = currentRowObjectViewer.validateAndSaveEditedFields();
		if (error != null) {
			return error;
		}
		error = currentRowObjectViewer.validateAndSaveObject();
		return error;
	}
	
	// Master/detail utility methods -----------------------------------------------------------

    private LinkedList detailObjects = new LinkedList();

    /*
     * Tries to save all detail objects.  Returns null if they could all be saved; an error message if any
     * could not be saved.
     */
    private String canChangeDetailObjects() {
    	for (Iterator detailObjectsIter = detailObjects.iterator(); detailObjectsIter.hasNext();) {
			IObjectViewer objectViewer = (IObjectViewer) detailObjectsIter.next();
			String error = objectViewer.validateAndSaveObject();
			if (error != null) {
				return error;
			}
		}
    	return null;
    }
    
    /*
     * Call setInput() on all detail objects, passing the current input object.
     */
    private void changeDetailObjects(Object detailObject) {
    	for (Iterator detailObjectsIter = detailObjects.iterator(); detailObjectsIter.hasNext();) {
			IObjectViewer objectViewer = (IObjectViewer) detailObjectsIter.next();
			objectViewer.setInput(detailObject);
		}
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IMaster#addDetailViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void addDetailViewer(IObjectViewer detailViewer) {
		detailObjects.add(detailViewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.sweet.controllers.IMaster#removeDetailViewer(org.eclipse.ve.sweet.objectviewer.IObjectViewer)
	 */
	public void removeDetailViewer(IObjectViewer detailViewer) {
		detailObjects.remove(detailViewer);
	}

	// Event handler utility methods -----------------------------------------------------------

	private static final String DATA_KEY = "IObjectViewer";

	private IObjectViewer theNullObjectViewer = new IObjectViewer() {

		public boolean setInput(Object input) {
			return false;
		}

		public Object getInput() {
			return "";
		}

		public IPropertyEditor getProperty(String name) throws NoSuchMethodException {
			return new NullProperty(name);
		}

		public IFieldViewer bind(Object control, String propertyName) {
			return null;
		}

		public IObjectViewer bind(String propertyName) {
			return this;
		}

		public IObjectViewer bind(String propertyName, IObjectViewerFactory factory) {
			return this;
		}

		public String validateAndSaveEditedFields() {
			return null;
		}

		public String validateAndSaveObject() {
			return null;
		}

		public void addObjectListener(IEditStateListener listener) {
		}

		public void removeObjectListener(IEditStateListener listener) {
		}

		public void fireObjectListenerEvent() {
		}

		public void addInputChangeListener(IInputChangeListener listener) {
		}

		public void removeInputChangeListener(IInputChangeListener listener) {
		}

		public void setHintHandler(IHintHandler hintHandler) {
		}

		public boolean isDirty() {
			return false;
		}

		public void commit() throws CannotSaveException {
		}

		public void refresh() {
		}

		public void rollback() {
		}

		public void delete() {
		}
		
	};
    

	private IObjectViewer getCurrentRowObjectViewer() {
		Control rowControl = table.getCurrentRowControl();
		if (rowControl == null) {
			return theNullObjectViewer;
		}
		return (IObjectViewer) rowControl.getData(DATA_KEY);
	}
	
	protected void bindFields(Control rowControl, IObjectViewer objectViewer) {
		Control[] columns;
		Control[] prototypeCols;
		if (rowControl instanceof Composite) {
			Composite rowComposite = (Composite) rowControl;
			columns = rowComposite.getChildren();
			prototypeCols = ((Composite)table.getRowControl()).getChildren();
		} else {
			columns = new Control[] {rowControl};
			prototypeCols = new Control[] {table.getRowControl()};
		}
		
		int numColumns = columns.length;

		for (int i=0; i < numColumns; ++i) {
			String binding = (String) prototypeCols[i].getData(COLUMN_BINDING);
			if (binding != null) {
				objectViewer.bind(columns[i], binding);
			}
		}
	}

	// Event handlers --------------------------------------------------------------------------

	private IRowConstructionListener rowConstructionListener = new IRowConstructionListener() {
		public void rowConstructed(Control newRow) {
			IObjectViewer objectViewer = ObjectViewerFactory.construct();
			objectViewer.setHintHandler(hintHandler);
			newRow.setData(DATA_KEY, objectViewer);
			bindFields(newRow, objectViewer);
		}
	};
	
	private IRowContentProvider rowContentProvider = new IRowContentProvider() {
		public void refresh(CompositeTable table, int currentObjectOffset, Control row) {
			IObjectViewer objectViewer = (IObjectViewer) row.getData(DATA_KEY);
			objectViewer.setInput(objectAt(currentObjectOffset));
		}
	};
	
	
	private IRowFocusListener rowFocusListener = new IRowFocusListener() {
		public boolean requestRowChange(CompositeTable sender, int currentObjectOffset, Control row) {
			String error = canChangeDetailObjects();
			if (error != null) {
				hintHandler.setMessage(error);
				return false;
			}
			IObjectViewer objectViewer = (IObjectViewer) row.getData(DATA_KEY);
			if (objectViewer.validateAndSaveObject() == null)
				return true;
			else
				return false;
		}
		public void depart(CompositeTable sender, int currentObjectOffset, Control row) {
			IObjectViewer objectViewer = (IObjectViewer) row.getData(DATA_KEY);
			try {
				objectViewer.commit();
			} catch (CannotSaveException e) {
				Logger.log().error(e, "We just successfully saved, so a CannotSaveException makes no sense");
			}
		}
		public void arrive(CompositeTable sender, int currentObjectOffset, Control row) {
			changeDetailObjects(objectAt(currentObjectOffset));
		}
	};

	protected IInsertHandler collectionInsertHandler;
	
	private IInsertHandler controllerInsertHandler = new IInsertHandler() {
		public int insert(int positionHint) {
			if (collectionInsertHandler == null) {
				return -1;
			}
			int result = collectionInsertHandler.insert(positionHint);
			if (result >= 0) {
				dirty = true;
				newIterator();
			}
			return result;
		}
	};

	protected IDeleteHandler collectionDeleteHandler;
	
	private IDeleteHandler controllerDeleteHandler = new IDeleteHandler() {
		public boolean canDelete(int rowInCollection) {
			if (collectionDeleteHandler == null) {
				return false;
			}
			return collectionDeleteHandler.canDelete(rowInCollection);
		}
		public void deleteRow(int rowInCollection) {
			collectionDeleteHandler.deleteRow(rowInCollection);
			dirty = true;
			newIterator();
			try {
				objectViewer.commit();
			} catch (CannotSaveException e) {
				throw new RuntimeException("controllerInsertHandler: Should always be able to save a new object");
			}
		}
	};


}
