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
 *  $RCSfile: JTextAreaBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JTextAreaBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JTextAreaMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtextarea");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTextArea.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JTextAreaMessages.getString("JTextArea.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JTextAreaMessages.getString("JTextArea.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/txtar32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/txtar16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	return( new EventSetDescriptor[0]);
}
/**
  * @return an icon of the specified kind for JButton
  */
public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("txtar32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("txtar16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// append(String)
			super.createMethodDescriptor(getBeanClass(),"append",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("append(String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("append(String).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("string", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("append(String).string.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "String to append",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// getColumns()
			super.createMethodDescriptor(getBeanClass(),"getColumns",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getColumns().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getColumns().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLineCount()
			super.createMethodDescriptor(getBeanClass(),"getLineCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getLineCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getLineCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLineWrap()
			super.createMethodDescriptor(getBeanClass(),"getLineWrap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getLineWrap().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getLineWrap().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(),"getMinimumSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getMinimumSize().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRows()
			super.createMethodDescriptor(getBeanClass(),"getRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getRows().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getRows().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTabSize()
			super.createMethodDescriptor(getBeanClass(),"getTabSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getTabSize().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getTabSize().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getWrapStyleWord()
			super.createMethodDescriptor(getBeanClass(),"getWrapStyleWord",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("getWrapStyleWord().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("getWrapStyleWord().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// insert(String,int)
			super.createMethodDescriptor(getBeanClass(),"insert",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("insert(String,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("insert(String,int).Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("string", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("insert(String,int).text.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text to insert",
	      				}),
	      			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("insert(String,int).pos.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "position",
	      				})	
	      		},
	      		new Class[] {
	      			String.class, int.class
	      		}		    		
		  	),
		  	// setColumns(int)
			super.createMethodDescriptor(getBeanClass(),"setColumns",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("setColumns(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the number of columns",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("num", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("setColumns(int).columns.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Number of columns",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setLineWrap(boolean)
			super.createMethodDescriptor(getBeanClass(),"setLineWrap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("setLineWrap(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextAreaMessages.getString("setLineWrap(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("setLineWrap(boolean).bool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to wrap",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setRows(int)
			super.createMethodDescriptor(getBeanClass(),"setRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("setRows(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the number of rows",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rows", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("setRows(int).rows.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Number of rows",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setTabSize(int)
			super.createMethodDescriptor(getBeanClass(),"setTabSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("setTabSize(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tab size",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("size", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("setTabSize(int).size.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Size",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setWrapStyleWord(boolean)
			super.createMethodDescriptor(getBeanClass(),"setWrapStyleWord",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextAreaMessages.getString("setWrapStyleWord(boolean).Name"), //$NON-NLS-1$
	   			SHORTDESCRIPTION, JTextAreaMessages.getString("setWrapStyleWord(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextAreaMessages.getString("setWrapStyleWord(boolean).Boolean.Name"), //$NON-NLS-1$
	      				})
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
		PropertyDescriptor aDescriptorList[] = {
	    	// columns
			super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("columns.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("columns.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// lineCount
			super.createPropertyDescriptor(getBeanClass(),"lineCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("lineCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("lineCount.Desc"), //$NON-NLS-1$
	    	}
	    	),
	    	// lineWrap
			super.createPropertyDescriptor(getBeanClass(),"lineWrap", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("lineWrap.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("lineWrap.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// rows
			super.createPropertyDescriptor(getBeanClass(),"rows", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("rows.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("rows.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// tabSize
			super.createPropertyDescriptor(getBeanClass(),"tabSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("tabSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("tabSize.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE
	    		}
	    	),	    	
	    	// wrapStyleWord
			super.createPropertyDescriptor(getBeanClass(),"wrapStyleWord", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextAreaMessages.getString("wrapStyleWord.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextAreaMessages.getString("wrapStyleWord.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	BOUND, Boolean.TRUE
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
