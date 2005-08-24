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
 *  $RCSfile: TextAreaBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;

public class TextAreaBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle restextarea = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.textarea");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.TextArea.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the TextAreaBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.TextArea.class);
		aDescriptor.setDisplayName(restextarea.getString("TextAreaDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(restextarea.getString("TextAreaSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/txtar32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/txtar16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new java.beans.EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("txtar32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("txtar16.gif");//$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create TextArea's peer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// append(String)
			super.createMethodDescriptor(getBeanClass(),"append", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "append(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("append(String)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("textParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Text to append",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	),
			// getColumns()
			super.createMethodDescriptor(getBeanClass(),"getColumns", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getColumns()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("getColumns()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get minimum dimensions",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getPreferredSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get preferred dimensions",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getMinimumSize(int,int)
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize(int,int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get minimum dimensions for given rows and columns",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("rowsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of rows",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class, int.class 
	      	}	    		
		  	),
		  	// getPreferredSize(int,int)
			super.createMethodDescriptor(getBeanClass(),"getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize(int,int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get preferred dimensions for given rows and columns",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("rowsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of rows",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class, int.class 
	      	}	    		
		  	),
		  	// getRows()
			super.createMethodDescriptor(getBeanClass(),"getRows", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getRows()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("getRows()DN"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getScrollbarVisibility()
			super.createMethodDescriptor(getBeanClass(),"getScrollbarVisibility", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getScrollbarVisibility()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("getScrollbarVisibility()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// insert(String,int)
			super.createMethodDescriptor(getBeanClass(),"insert", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "insert(String,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("insert(String,int)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("stringParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Text to insert",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("positionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Position to insert at",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class, int.class 
	      	}	    		
		  	),
		  	// replaceRange(String,int,int)
			super.createMethodDescriptor(getBeanClass(),"replaceRange", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "replaceRange(String,int,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("replaceRange(String,int,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("stringParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Text to replace with",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("startParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Position to start at",
	      			}
	      		),
	      		createParameterDescriptor("arg3", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("endParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Position to end at",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class, int.class, int.class 
	      	}	    		
		  	),
		  	// setColumns(int)
			super.createMethodDescriptor(getBeanClass(),"setColumns", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setColumns(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("setColumns(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("columnsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of columns",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
		  	// setRows(int)
			super.createMethodDescriptor(getBeanClass(),"setRows", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setRows(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextarea.getString("setRows(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextarea.getString("rowsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Number of rows",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
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
			// columns
	    	super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] {//$NON-NLS-1$
		    DISPLAYNAME, restextarea.getString("columnsDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextarea.getString("columnsSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// minimumSize
	    	super.createPropertyDescriptor(getBeanClass(),"minimumSize", new Object[] {//$NON-NLS-1$
   		    DISPLAYNAME, restextarea.getString("minimumSizeDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextarea.getString("minimumSizeSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// preferredSize
	    	super.createPropertyDescriptor(getBeanClass(),"preferredSize", new Object[] {//$NON-NLS-1$
		    DISPLAYNAME, restextarea.getString("preferredSizeDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextarea.getString("preferredSizeSD"), //$NON-NLS-1$
	    		}
	    	),
			// rows
	    	super.createPropertyDescriptor(getBeanClass(),"rows", new Object[] {//$NON-NLS-1$
		    DISPLAYNAME, restextarea.getString("rowsDN"),		    	 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextarea.getString("rowsSD"), //$NON-NLS-1$
	    		}
	    	),
			// scrollbarVisibility
			super.createPropertyDescriptor(getBeanClass(),"scrollbarVisibility", new Object[] {//$NON-NLS-1$
		    DISPLAYNAME, restextarea.getString("scrollbarVisibilityDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextarea.getString("scrollbarVisibilitySD"), //$NON-NLS-1$
	    		}
	    	)			
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}
