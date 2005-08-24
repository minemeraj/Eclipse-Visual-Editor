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
 *  $RCSfile: JComboBoxBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JComboBoxBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JComboBoxMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jcombobox");  //$NON-NLS-1$

/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("actionPerformed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("actionPerformed.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComboBoxMessages.getString("actionPerformed.actionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on making selection",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, JComboBoxMessages.getString("actionEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JComboBoxMessages.getString("actionEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JComboBox.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JComboBoxMessages.getString("JComboBox.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JComboBoxMessages.getString("JComboBox.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jcombo32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jcombo16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			actionEventSetDescriptor(),
			itemEventSetDescriptor()
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
	    return loadImage("jcombo32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jcombo16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// addItem(Object)
			super.createMethodDescriptor(getBeanClass(),"addItem",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("addItem(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add the item to the list",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("item", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("addItem(Object).item.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// configureEditor(ComboBoxEditor,Object)
			super.createMethodDescriptor(getBeanClass(),"configureEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("configureEditor(ComboBoxEditor,Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Initialize the editor with the item",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("editor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("configureEditor(ComboBoxEditor,Object).anEditor.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Editor to configure"
	      				}
	      			),
	    			createParameterDescriptor("item", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("configureEditor(ComboBoxEditor,Object).item.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object to edit",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ComboBoxEditor.class,
	      			java.lang.Object.class
	      		}		    		
		  	),
			// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getActionCommand()
			super.createMethodDescriptor(getBeanClass(),"getActionCommand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getActionCommand().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("getActionCommand().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getEditor()
			super.createMethodDescriptor(getBeanClass(),"getEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getEditor().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the editor for items",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getItemAt(int)
			super.createMethodDescriptor(getBeanClass(),"getItemAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getItemAt(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the item at specified index"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("getItemAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "index of item",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getItemCount()
			super.createMethodDescriptor(getBeanClass(),"getItemCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getItemCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("getItemCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getKeySelectionManager()
			super.createMethodDescriptor(getBeanClass(),"getKeySelectionManager",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getKeySelectionManager().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the key selection manager",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaximumRowCount()
			super.createMethodDescriptor(getBeanClass(),"getMaximumRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getMaximumRowCount().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the maximum number of items displayed",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the model providing the data",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRenderer
			super.createMethodDescriptor(getBeanClass(),"getRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getRenderer.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("getRenderer.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedIndex()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getSelectedIndex().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get index of selected item",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedItem()
			super.createMethodDescriptor(getBeanClass(),"getSelectedItem",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getSelectedItem().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the selected item",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedObjects()
			super.createMethodDescriptor(getBeanClass(),"getSelectedObjects",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getSelectedObjects().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get an array with selected item",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ComboBoxUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// hidePopup()
			super.createMethodDescriptor(getBeanClass(),"hidePopup",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("hidePopup().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Close the popup window",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// insertItemAt(Object,int)
			super.createMethodDescriptor(getBeanClass(),"insertItemAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("insertItemAt(Object,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert item at given index"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("object", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("insertItemAt(Object,int).item.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Item to insert",
	      				}),
	      			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("insertItemAt(Object,int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of item",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Object.class, int.class
	      		}		    		
		  	),
		  	// isEditable
			super.createMethodDescriptor(getBeanClass(),"isEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("isEditable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("isEditable.Desc")	    		},  //$NON-NLS-1$
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isLightWeightPopupEnabled
			super.createMethodDescriptor(getBeanClass(),"isLightWeightPopupEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("isLightWeightPopupEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("isLightWeightPopupEnabled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isPopupVisible
			super.createMethodDescriptor(getBeanClass(),"isPopupVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("isPopupVisible.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("isPopupVisible.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// removeAllItems
			super.createMethodDescriptor(getBeanClass(),"removeAllItems",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("removeAllItems.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove all items"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// removeItem(Object)
			super.createMethodDescriptor(getBeanClass(),"removeItem",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("removeItem(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove specified item from list"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("object", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("removeItem(Object).item.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Item to remove",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// removeItemAt(int)
			super.createMethodDescriptor(getBeanClass(),"removeItemAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("removeItemAt(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove item at specified index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("removeItemAt(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of item",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// selectWithKeyChar(char)
			super.createMethodDescriptor(getBeanClass(),"selectWithKeyChar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("selectWithKeyChar(char).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the item matching character",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyChar", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("selectWithKeyChar(char).keyChar.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Character typed",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			char.class
	      		}		    		
		  	),
		  	// setActionCommand(String)
			super.createMethodDescriptor(getBeanClass(),"setActionCommand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setActionCommand(String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("setActionCommand(String).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aCommand", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setActionCommand(String).aString.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Command string",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setEditor(ComboBoxEditor)
			super.createMethodDescriptor(getBeanClass(),"setEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setEditor(ComboBoxEditor).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the editor",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("editor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setEditor(ComboBoxEditor).editor.Desc"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Combo box item editor",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ComboBoxEditor.class
	      		}		    		
		  	),
		  	// setRenderer(ListCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setRenderer(ListCellRenderer).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell renderer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellRenderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setRenderer(ListCellRenderer).cellRenderer.Desc"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List cell renderer",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ListCellRenderer.class
	      		}		    		
		  	),
		  	// setEditable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setEditable(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("setEditable(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setEditable(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to make combo box editable",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setKeySelectionManager(KeySelectionManager)
			super.createMethodDescriptor(getBeanClass(),"setKeySelectionManager",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setKeySelectionManager(KeySelectionManager).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the key selection manager",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aManager", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setKeySelectionManager(KeySelectionManager).aManager.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Key selection manager",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.JComboBox.KeySelectionManager.class
	      		}		    		
		  	),
		  	// setLightWeightPopupEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setLightWeightPopupEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setLightWeightPopupEnabled(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "When displaying the popup, JComboBox choose to use a light weight popup if it fits.",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setLightWeightPopupEnabled(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable lightweight",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMaximumRowCount(int)
			super.createMethodDescriptor(getBeanClass(),"setMaximumRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setMaximumRowCount(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the number of items displayed",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("count", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setMaximumRowCount(int).count.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "number of items",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setModel(ComboBoxModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setModel(ComboBoxModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the combo model for data",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setModel(ComboBoxModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Combo box model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ComboBoxModel.class
	      		}		    		
		  	),
		  	// setPopupVisible(boolean)
			super.createMethodDescriptor(getBeanClass(),"setPopupVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setPopupVisible(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Disable or enable the combo box",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setPopupVisible(boolean).aBool.Name") //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setSelectedIndex(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setSelectedIndex(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the item at index",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setSelectedIndex(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of item",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectedItem(Object)
			super.createMethodDescriptor(getBeanClass(),"setSelectedItem",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setSelectedItem(Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the specified item",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("item", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setSelectedItem(Object).item.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Item to select",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// setUI(ComboBoxUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("setUI(ComboBoxUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the combo box UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JComboBoxMessages.getString("setUI(ComboBoxUI).aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Combo box UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ComboBoxUI.class
	      		}		    		
		  	),
		  	// showPopup
			super.createMethodDescriptor(getBeanClass(),"showPopup",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("showPopup.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Show the popup window",
	      		EXPERT, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
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
			// actionCommand
			super.createPropertyDescriptor(getBeanClass(),"actionCommand", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("actionCommand.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("actionCommand.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// editable
			super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("editable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("editable.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	    		}
	    	),
	    	// editor
			super.createPropertyDescriptor(getBeanClass(),"editor", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("editor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("editor.Dec"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// focusTraversable
			super.createPropertyDescriptor(getBeanClass(),"focusTraversable", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("focusTraversable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("focusTraversable.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("itemCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("itemCount.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// keySelectionManager
			super.createPropertyDescriptor(getBeanClass(),"keySelectionManager", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("keySelectionManager.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("keySelectionManager.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// lightWeightPopupEnabled
			super.createPropertyDescriptor(getBeanClass(),"lightWeightPopupEnabled", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("lightWeightPopupEnabled.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("lightWeightPopupEnabled.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	    		}
	    	),
	    	// maximumRowCount
			super.createPropertyDescriptor(getBeanClass(),"maximumRowCount", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("maximumRowCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("maximumRowCount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("model.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("model.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// popupVisible
			super.createPropertyDescriptor(getBeanClass(),"popupVisible", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("popupVisible.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("popupVisible.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// renderer
			super.createPropertyDescriptor(getBeanClass(),"renderer", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("renderer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("renderer.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// selectedIndex
			super.createPropertyDescriptor(getBeanClass(),"selectedIndex", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("selectedIndex.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("selectedIndex.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// selectedItem
			super.createPropertyDescriptor(getBeanClass(),"selectedItem", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("selectedItem.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("selectedItem.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// selectedObjects
			super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("selectedObjects.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("selectedObjects.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// action
			super.createPropertyDescriptor(getBeanClass(),"action", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("action.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("action.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
	    		}
	    	),
	    	// UI
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JComboBoxMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JComboBoxMessages.getString("ui.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,	      	
			BOUND, Boolean.TRUE
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
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor itemEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ItemEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ItemListener.class,
				"itemStateChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JComboBoxMessages.getString("itemStateChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JComboBoxMessages.getString("itemStateChanged.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("itemEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JComboBoxMessages.getString("itemStateChanged.itemEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event when selected item changes",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, JComboBoxMessages.getString("itemEvent.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JComboBoxMessages.getString("itemEvent.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
