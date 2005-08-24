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
 *  $RCSfile: ChoiceBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;
public class ChoiceBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle reschoice = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.choice");  //$NON-NLS-1$

	
	public Class getBeanClass() {
	return java.awt.Choice.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the ChoiceBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Choice.class);
		aDescriptor.setDisplayName(reschoice.getString("ChoiceDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(reschoice.getString("ChoiceSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/choice32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/choice16.gif");//$NON-NLS-2$//$NON-NLS-1$
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
	    return loadImage("choice32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("choice16.gif");//$NON-NLS-1$
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
	   			DISPLAYNAME, "add(String)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("add(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("itemParmSD"), //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
			// addItem(String)
			super.createMethodDescriptor(getBeanClass(),"addItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addItem(String)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("addItem(String)SD") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("itemParmSD") //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",  //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		SHORTDESCRIPTION, reschoice.getString("addNotify()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// countItems - DEPRECATED
			// getItem(int)
			super.createMethodDescriptor(getBeanClass(),"getItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getItem(int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("getItem(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("indexParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("indexParmSD"), //$NON-NLS-1$
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
	   			DISPLAYNAME, "getItemCount()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("getItemCount()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedIndex()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndex", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedIndex()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("getSelectedIndex()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedItem()
			super.createMethodDescriptor(getBeanClass(),"getSelectedItem", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedItem()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("getSelectedItem()SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedObjects()
			super.createMethodDescriptor(getBeanClass(),"getSelectedObjects", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedObjects()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("getSelectedObjects()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// insert(String,int)
			super.createMethodDescriptor(getBeanClass(),"insert", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "insert(String,int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("insert(String,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("itemInsertParmSD"), //$NON-NLS-1$
	      			}
	      		),	
	    			createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("indexParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("indexInsertParmDN"), //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	new Class[] {
	      		String.class,
	      		int.class 
	      	}	    		
		  	),			
			// remove(int)
			super.createMethodDescriptor(getBeanClass(),"remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("remove(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("positionParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("positionParmSD"), //$NON-NLS-1$
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
	   			DISPLAYNAME, "remove(String)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("remove(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("itemRemoveParmSD"), //$NON-NLS-1$
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
	   			DISPLAYNAME, "removeAll()",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("removeAll()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// select(int)
			super.createMethodDescriptor(getBeanClass(),"select", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "select(int)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("select(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("positionParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("positionSelectParmDN"), //$NON-NLS-1$
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// select(String)
			super.createMethodDescriptor(getBeanClass(),"select", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "select(String)",  //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("select(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemParmDN"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, reschoice.getString("itemSelectParmSD"), //$NON-NLS-1$
	      			})
	      		},
	      		new Class[] { 
	      			String.class 
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
			DISPLAYNAME, reschoice.getString("itemCountDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reschoice.getString("itemCountSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectedIndex
			super.createPropertyDescriptor(getBeanClass(),"selectedIndex", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reschoice.getString("selectedIndexDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reschoice.getString("selectedIndexSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// selectedItem
			super.createPropertyDescriptor(getBeanClass(),"selectedItem", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, reschoice.getString("selectedItemDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reschoice.getString("selectedItemSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// selectedObjects
	   		super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, reschoice.getString("selectedObjectsDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, reschoice.getString("selectedObjectsSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
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
				"itemStateChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, reschoice.getString("itemStateChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, reschoice.getString("itemStateChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("itemEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, reschoice.getString("itemEventParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "item state changed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, reschoice.getString("itemEventDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, reschoice.getString("itemEventSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}
