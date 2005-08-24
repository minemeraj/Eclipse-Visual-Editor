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
 *  $RCSfile: ListBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class ListBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle reslist = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.list");  //$NON-NLS-1$
	
	
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reslist.getString("actionPerformedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("actionPerformedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("actionEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on making a selection",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, reslist.getString("actionEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, reslist.getString("actionEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.List.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the ListBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.List.class);
		aDescriptor.setDisplayName(reslist.getString("ListDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(reslist.getString("ListSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/list32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/list16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			actionEventSetDescriptor()
			,itemEventSetDescriptor()
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
	    return loadImage("list32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("list16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// add(String)
			super.createMethodDescriptor(getBeanClass(),"add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("add(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item to add",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
		  	// add(String,int)
		  	super.createMethodDescriptor(getBeanClass(),"add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(String,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("add(String,int)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item to add",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "zero-based position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class,
	      		int.class 
	      	}	    		
		  	),
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the list peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// deselect(int)
			super.createMethodDescriptor(getBeanClass(),"deselect", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "deselect(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Deselect item at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item to deselect",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// getItem(int)
			super.createMethodDescriptor(getBeanClass(),"getItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItem(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the item at index",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item to get",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// getItemCount()
			super.createMethodDescriptor(getBeanClass(),"getItemCount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItemCount()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("getItemCount()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getItems()
			super.createMethodDescriptor(getBeanClass(),"getItems", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItems()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("getItems()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get minimum dimensions of list",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getMinimumSize(int)
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum dimensions for given rows",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("rowsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "number of rows",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// getPreferredSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get preferred dimensions of list",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getPreferredSize(int)
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the preferred dimensions for given rows",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("rowsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "number of rows",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// getRows()
			super.createMethodDescriptor(getBeanClass(),"getRows", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getRows()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("getRows()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedIndex()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndex", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedIndex()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get index of selected item",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getSelectedIndexes()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndexes", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedIndexes()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get indexes of selected items",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedItem()
			super.createMethodDescriptor(getBeanClass(),"getSelectedItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedItem()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("getSelectedItem()SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getSelectedItems()
			super.createMethodDescriptor(getBeanClass(),"getSelectedItems", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedItems()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get array of selected items",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedObjects()
			super.createMethodDescriptor(getBeanClass(),"getSelectedObjects", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedObjects()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get array with selected item",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getVisibleIndex()
			super.createMethodDescriptor(getBeanClass(),"getVisibleIndex", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getVisibleIndex()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get index of item last made visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// isIndexSelected(int)
			super.createMethodDescriptor(getBeanClass(),"isIndexSelected", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isIndexSelected(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("isIndexSelected(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// isMultipleMode()
			super.createMethodDescriptor(getBeanClass(),"isMultipleMode", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isMultipleMode()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("isMultipleMode()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// makeVisible(int)
			super.createMethodDescriptor(getBeanClass(),"makeVisible", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "makeVisible(int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Make item at index visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),				
			// remove(int)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("remove(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("positionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// remove(String)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("remove(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("itemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
			// removeAll()
			super.createMethodDescriptor(getBeanClass(),"removeAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeAll()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("removeAll()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// removeNotify()
			super.createMethodDescriptor(getBeanClass(),"removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove the peer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// select(int)
			super.createMethodDescriptor(getBeanClass(),"select", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "select(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("select(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("positionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of item to select",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// replaceItem(String,int)
		  	super.createMethodDescriptor(getBeanClass(),"replaceItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "replaceItem(String,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("replaceItem(String,Int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("newItemParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new item to replace",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "zero-based position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class,
	      		int.class 
	      	}	    		
		  	),
		  	// setMultipleMode(boolean)
			super.createMethodDescriptor(getBeanClass(),"setMultipleMode", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setMultipleMode(boolean)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("setMultipleMode(boolean)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("modeParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE to allow multiple selection",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
	      	}	    		
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// itemCount
			super.createPropertyDescriptor(getBeanClass(),"itemCount", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("itemCountDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("itemCountSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// items
			super.createPropertyDescriptor(getBeanClass(),"items", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("itemsDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("itemsSD"), //$NON-NLS-1$
	    		}
	    	),
			// minimumSize
			super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("minimumSizeDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("minimumSizeSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// multipleMode
			super.createPropertyDescriptor(getBeanClass(),"multipleMode", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("multipleModeDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("multipleModeSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// preferredSize
			super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("preferredSizeDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("preferredSizeSD"), //$NON-NLS-1$
	    		}
	    	),
			// rows
			super.createPropertyDescriptor(getBeanClass(),"rows", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("rowsDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("rowsSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectedIndexes
			super.createPropertyDescriptor(getBeanClass(),"selectedIndexes", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("selectedIndexesDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("selectedIndexesSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectedIndex
			super.createPropertyDescriptor(getBeanClass(),"selectedIndex", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("selectedIndexDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("selectedIndexSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectedItem
			super.createPropertyDescriptor(getBeanClass(),"selectedItem", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("selectedItemDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("selectedItemSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// selectedItems
			super.createPropertyDescriptor(getBeanClass(),"selectedItems", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("selectedItemsDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("selectedItemsSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectedObjects
			super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("selectedObjectsDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("selectedObjectsSD"), //$NON-NLS-1$
	    		}
	    	),
			// visibleIndex
			super.createPropertyDescriptor(getBeanClass(),"visibleIndex", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reslist.getString("visibleIndexDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, reslist.getString("visibleIndexSD"), //$NON-NLS-1$
	    		}
	    	)
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
				"itemStateChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reslist.getString("itemStateChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reslist.getString("itemStateChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("itemEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reslist.getString("itemEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item state changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, reslist.getString("itemEventDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, reslist.getString("itemEventSD"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}
