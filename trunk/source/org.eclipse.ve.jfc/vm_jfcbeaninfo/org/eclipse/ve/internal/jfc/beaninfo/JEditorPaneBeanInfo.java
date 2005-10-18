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
 *  $RCSfile: JEditorPaneBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class JEditorPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JEditorPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jeditorpane");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JEditorPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JEditorPaneMessages.getString("JEditorPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JEditorPaneMessages.getString("JEditorPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jedtpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jedtpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			hyperlinkEventSetDescriptor()
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
	    return loadImage("jedtpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jedtpn16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessibility context",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// createEditorKitForContentType(String)
			super.createMethodDescriptor(getBeanClass(),"createEditorKitForContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("createEditorKitForContentType(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create an editor kit for the specified type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("type", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("createEditorKitForContentType(String).type.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Type of content",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// getContentType()
			super.createMethodDescriptor(getBeanClass(),"getContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("getContentType().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("getContentType().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getEditorKit()
			super.createMethodDescriptor(getBeanClass(),"getEditorKit",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("getEditorKit().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("getEditorKit().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getEditorKitForContentType(String)
			super.createMethodDescriptor(getBeanClass(),"getEditorKitForContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("getEditorKitForContentType(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create an editor kit for the specified type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("type", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("getEditorKitForContentType(String).type.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Type of content",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
			// getPage()
			super.createMethodDescriptor(getBeanClass(),"getPage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("getPage().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("getPage().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// registerEditorKitForContentType(String,String)
			super.createMethodDescriptor(getBeanClass(),"registerEditorKitForContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("registerEditorKitForContentType(String,String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Register an editor kit for the specified type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("type", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("registerEditorKitForContentType(String,String).type.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Type of content",
	      				}),
	    			createParameterDescriptor("classname", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("registerEditorKitForContentType(String,String).classname.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Editor Kit",
	      				}),
	    			
	      		},
	      		new Class[] {
	      			String.class, String.class
	      		}		    		
		  	),
		  	// setContentType(String)
			super.createMethodDescriptor(getBeanClass(),"setContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("setContentType(String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("setContentType(String).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("type", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setContentType(String).type.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Content type",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
			// setEditorKit(EditorKit)
			super.createMethodDescriptor(getBeanClass(),"setEditorKit",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("setEditorKit(EditorKit).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the editor kit for the editor",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("kit", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setEditorKit(EditorKit).kit.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Editor kit",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.EditorKit.class
	      		}		    		
		  	),
			// setEditorKitForContentType(String,EditorKit)
			super.createMethodDescriptor(getBeanClass(),"setEditorKitForContentType",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("setEditorKitForContentType(String,EditorKit).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the editor kit for the specified type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("type", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setEditorKitForContentType(String,EditorKit).type.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Type of content",
	      				}),
	    			createParameterDescriptor("classname", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setEditorKitForContentType(String,EditorKit).classname.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Editor Kit",
	      				}),
	    			
	      		},
	      		new Class[] {
	      			String.class, javax.swing.text.EditorKit.class
	      		}		    		
		  	),
			// setPage(String)
			super.createMethodDescriptor(getBeanClass(),"setPage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("setPage(String).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("setPage(String).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("url", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setPage(String).url.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Current url",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
			// setPage(URL)
			super.createMethodDescriptor(getBeanClass(),"setPage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("setPage(URL).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("setPage(URL).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("url", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JEditorPaneMessages.getString("setPage(URL).url.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Current url",
	      				})
	      		},
	      		new Class[] {
	      			java.net.URL.class
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
	    	// contentType
			super.createPropertyDescriptor(getBeanClass(),"contentType", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JEditorPaneMessages.getString("contentType.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JEditorPaneMessages.getString("contentType.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,      	
			IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
					"text/plain", new Integer(0), "\"text/plain\"", //$NON-NLS-1$ //$NON-NLS-2$
					"text/html", new Integer(1), "\"text/html\"", //$NON-NLS-1$ //$NON-NLS-2$
					"text/rtf", new Integer(2), "\"text/rtf\"" //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
	    	),
			// editorKit
			super.createPropertyDescriptor(getBeanClass(),"editorKit", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JEditorPaneMessages.getString("editorKit.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JEditorPaneMessages.getString("editorKit.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// page
			super.createPropertyDescriptor(getBeanClass(),"page", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JEditorPaneMessages.getString("page.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JEditorPaneMessages.getString("page.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	PREFERRED, Boolean.TRUE	      	
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
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor hyperlinkEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.HyperlinkEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.HyperlinkListener.class,
				"hyperlinkUpdate",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JEditorPaneMessages.getString("hyperlinkUpdate.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JEditorPaneMessages.getString("hyperlinkUpdate.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("hyperlinkEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JEditorPaneMessages.getString("hyperlinkUpdate.hyperlinkEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on activating link",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"hyperlink", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JEditorPaneMessages.getString("hyperlinkEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JEditorPaneMessages.getString("hyperlinkEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.HyperlinkListener.class,
						"addHyperlinkListener", "removeHyperlinkListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
