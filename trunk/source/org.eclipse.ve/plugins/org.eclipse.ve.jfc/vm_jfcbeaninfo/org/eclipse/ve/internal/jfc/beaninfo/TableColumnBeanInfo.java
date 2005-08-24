/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: TableColumnBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class TableColumnBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle TableColumnMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.tablecolumn");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.table.TableColumn.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, TableColumnMessages.getString("TableColumn.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, TableColumnMessages.getString("TableColumn.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jcolum32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jcolum16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			propertyChangeEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("jcolum32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jcolum16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getCellEditor()
			super.createMethodDescriptor(getBeanClass(),"getCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getEditor().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("getEditor().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellRenderer()
			super.createMethodDescriptor(getBeanClass(),"getCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getCellRenderer().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the cell renderer for column values"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHeaderRenderer
			super.createMethodDescriptor(getBeanClass(),"getHeaderRenderer", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getHeaderRenderer().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the renderer for the column header",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHeaderValue()
			super.createMethodDescriptor(getBeanClass(),"getHeaderValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getHeaderValue().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the value for the header",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getIdentifier()
			super.createMethodDescriptor(getBeanClass(),"getIdentifier",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getIdentifier().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the identifier for this column",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaxWidth()
			super.createMethodDescriptor(getBeanClass(),"getMaxWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getMaxWidth().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the maximum width of this column"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinWidth()
			super.createMethodDescriptor(getBeanClass(),"getMinWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getMinWidth().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum width of this column"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModelIndex()
			super.createMethodDescriptor(getBeanClass(),"getModelIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getModelIndex().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("getModelIndex().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getResizable()
			super.createMethodDescriptor(getBeanClass(),"getResizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getResizable().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("getResizable().Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getWidth()
			super.createMethodDescriptor(getBeanClass(),"getWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("getWidth().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the width of the column"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setCellEditor(TableCellEditor)
			super.createMethodDescriptor(getBeanClass(),"setCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setCellEditor(TableCellEditor).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell editor",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("editor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setCellEditor(TableCellEditor).editor.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Cell editor",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.table.TableCellEditor.class
	      		}		    		
		  	),
		  	// setCellRenderer(TableCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setCellRenderer(TableCellRenderer).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell renderer to draw column values",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellRenderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setCellRenderer(TableCellRenderer).cellRenderer.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table column cell renderer",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.table.TableCellRenderer.class
	      		}		    		
		  	),
		  	// setHeaderRenderer(TableCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setHeaderRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setHeaderRenderer(TableCellRenderer).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell renderer to draw column header",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellRenderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setHeaderRenderer(TableCellRenderer).cellRenderer.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table column cell renderer",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.table.TableCellRenderer.class
	      		}		    		
		  	),
		  	// setHeaderValue(Object)
			super.createMethodDescriptor(getBeanClass(),"setHeaderValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setHeaderValue(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the value object for the header renderer",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("value", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setHeaderValue(Object).aValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Header value",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setIdentifier(Object)
			super.createMethodDescriptor(getBeanClass(),"setIdentifier",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setIdentifier(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set an identifier for the column",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("identifier", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setIdentifier(Object).anIdentifier.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tag for column",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setMaxWidth(int)
			super.createMethodDescriptor(getBeanClass(),"setMaxWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setMaxWidth(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the maximum width",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newMaxWidth", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setMaxWidth(int).newMaxWidth.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Maximum width",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMinWidth(int)
			super.createMethodDescriptor(getBeanClass(),"setMinWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setMinWidth(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the minimum width",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newMinWidth", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setMinWidth(int).newMinWidth.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Minimum width",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setModelIndex(int)
			super.createMethodDescriptor(getBeanClass(),"setModelIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setModelIndex(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("setModelIndex(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setModelIndex(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index in TableModel",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setResizable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setResizable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setResizable(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("setResizable(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setResizable(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to make column resizable",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setWidth(int)
			super.createMethodDescriptor(getBeanClass(),"setWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("setWidth(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the width",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("width", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, TableColumnMessages.getString("setWidth(int).width.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Width of column",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// sizeWidthToFit
			super.createMethodDescriptor(getBeanClass(),"sizeWidthToFit",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("sizeWidthToFit.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("sizeWidthToFit.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// cellEditor
			super.createPropertyDescriptor(getBeanClass(),"cellEditor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("cellEditor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("cellEditor.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
	    		}
	    	),
			// cellRenderer
			super.createPropertyDescriptor(getBeanClass(),"cellRenderer", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("cellRenderer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("cellRenderer.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// headerRenderer
			super.createPropertyDescriptor(getBeanClass(),"headerRenderer", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("headerRenderer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("headerRenderer.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// headerValue
			super.createPropertyDescriptor(getBeanClass(),"headerValue", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("headerValue.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("headerValue.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE	      	
	    		}
	    	),	    	
	    	// identifier
			super.createPropertyDescriptor(getBeanClass(),"identifier", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("identifier.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("identifier.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// maxWidth
			super.createPropertyDescriptor(getBeanClass(),"maxWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("maxWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("maxWidth.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// minWidth
			super.createPropertyDescriptor(getBeanClass(),"minWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("minWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("minWidth.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// modelIndex
			super.createPropertyDescriptor(getBeanClass(),"modelIndex", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("modelIndex.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("modelIndex.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// preferredWidth
			super.createPropertyDescriptor(getBeanClass(),"preferredWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("preferredWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("preferredWidth.Desc") //$NON-NLS-1$
	    		}	   
	    	), 	
	    	// resizable
			super.createPropertyDescriptor(getBeanClass(),"resizable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("resizable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("resizable.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// width
			super.createPropertyDescriptor(getBeanClass(),"width", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, TableColumnMessages.getString("width.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, TableColumnMessages.getString("width.Desc"), //$NON-NLS-1$
	      	WRITEMETHOD , null		// Write should not be used, preferredWidth is what the user should set so make this property read only
	    		}
	    	),	
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor propertyChangeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.beans.PropertyChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.beans.PropertyChangeListener.class,
				"propertyChange",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, TableColumnMessages.getString("propertyChange.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, TableColumnMessages.getString("propertyChange.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("propertyChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, TableColumnMessages.getString("propertyChange.propertyChangeEvent.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, TableColumnMessages.getString("propertyChange.propertyChangeEvent.Desc"), //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"propertyChange", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, TableColumnMessages.getString("propertyChangeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, TableColumnMessages.getString("propertyChangeEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.beans.PropertyChangeListener.class,
						"addPropertyChangeListener", "removePropertyChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
