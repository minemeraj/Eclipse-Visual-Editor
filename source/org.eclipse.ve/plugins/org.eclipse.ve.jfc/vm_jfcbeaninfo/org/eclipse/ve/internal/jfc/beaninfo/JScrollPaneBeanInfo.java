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
 *  $RCSfile: JScrollPaneBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JScrollPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JScrollPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jscrollpane");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JScrollPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JScrollPaneMessages.getString("JScrollPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JScrollPaneMessages.getString("JScrollPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/scrpne32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/scrpne16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("scrpne32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("scrpne16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// createHorizontalScrollBar()
			super.createMethodDescriptor(getBeanClass(),"createHorizontalScrollBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("createHorizontalScrollBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create horizontal scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// createVerticalScrollBar()
			super.createMethodDescriptor(getBeanClass(),"createVerticalScrollBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("createVerticalScrollBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create vertical scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible text",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getColumnHeader()
			super.createMethodDescriptor(getBeanClass(),"getColumnHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getColumnHeader().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("getColumnHeader().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCorner(String)
			super.createMethodDescriptor(getBeanClass(),"getCorner",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getCorner(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the corner component at key",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("getCorner(String).key.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Key of component",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      			}		    		
		  	),
		  	// getHorizontalScrollBar()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalScrollBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getHorizontalScrollBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the horizontal scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalScrollBarPolicy()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalScrollBarPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getHorizontalScrollBarPolicy().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("getHorizontalScrollBarPolicy().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getRowHeader()
			super.createMethodDescriptor(getBeanClass(),"getRowHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getRowHeader().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the row header viewport",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the UI",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalScrollBar()
			super.createMethodDescriptor(getBeanClass(),"getVerticalScrollBar",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getVerticalScrollBar().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the vertical scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalScrollBarPolicy()
			super.createMethodDescriptor(getBeanClass(),"getVerticalScrollBarPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getVerticalScrollBarPolicy().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("getVerticalScrollBarPolicy().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getViewport()
			super.createMethodDescriptor(getBeanClass(),"getViewport",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getViewport().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the viewport",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getViewportBorder()
			super.createMethodDescriptor(getBeanClass(),"getViewportBorder",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("getViewportBorder().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the viewport border",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setColumnHeader(JViewport)
			super.createMethodDescriptor(getBeanClass(),"setColumnHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setColumnHeader(JViewport).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("setColumnHeader(JViewport).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("viewport", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setColumnHeader(JViewport).viewport.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Viewport to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.JViewport.class
	      		}		    		
		  	),
			// setColumnHeaderView(Component)
			super.createMethodDescriptor(getBeanClass(),"setColumnHeaderView",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setColumnHeaderView(Component).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("setColumnHeaderView(Component).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setColumnHeaderView(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to view",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setCorner(String,Component)
			super.createMethodDescriptor(getBeanClass(),"setCorner",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setCorner(String,Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the corner component at key",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("key", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setCorner(String,Component).key.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "key string",
	      				}
	      			),
	      			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setCorner(String,Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class, java.awt.Component.class
	      		}		    		
		  	),
		  	// setHorizontalScrollBarPolicy(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalScrollBarPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setHorizontalScrollBarPolicy(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the horizontal scroll bar policy",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setHorizontalScrollBarPolicy(int).anInteger.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "How scroll bar appears",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setRowHeader(JViewport)
			super.createMethodDescriptor(getBeanClass(),"setRowHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setRowHeader(JViewport).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the Row header viewport",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("viewport", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setRowHeader(JViewport).viewport.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Viewport to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.JViewport.class
	      		}		    		
		  	),
			// setRowHeaderView(Component)
			super.createMethodDescriptor(getBeanClass(),"setRowHeaderView",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setRowHeaderView(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the viewport's view on specified component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setRowHeaderView(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to view",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setUI(ScrollPaneUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setUI(ScrollPaneUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the scroll pane UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setUI(ScrollPaneUI).aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Scroll pane UI",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ScrollPaneUI.class
	      		}		    		
		  	),
		  	// setVerticalScrollBarPolicy(int)
			super.createMethodDescriptor(getBeanClass(),"setVerticalScrollBarPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setVerticalScrollBarPolicy(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the vertical scroll bar policy",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setVerticalScrollBarPolicy(int).anInteger.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "How scroll bar appears",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setViewport(JViewport)
			super.createMethodDescriptor(getBeanClass(),"setViewport",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setViewport(JViewport).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the viewport",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("viewport", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setViewport(JViewport).viewport.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Viewport to add",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.JViewport.class
	      		}		    		
		  	),
			// setViewportView(Component)
			super.createMethodDescriptor(getBeanClass(),"setViewportView",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setViewportView(Component).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the viewport's view on specified component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setViewportView(Component).component.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to view",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// setViewportBorder(Border)
			super.createMethodDescriptor(getBeanClass(),"setViewportBorder",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollPaneMessages.getString("setViewportBorder(Border).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the viewport's border",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("border", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollPaneMessages.getString("setViewportBorder(Border).border.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Border",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.border.Border.class
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
			// columnHeader
			super.createPropertyDescriptor(getBeanClass(),"columnHeader", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("columnHeader.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("columnHeader.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// columnHeaderView
			super.createPropertyDescriptor(getBeanClass(),"columnHeaderView", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("columnHeaderView.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("columnHeaderView.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// horizontalScrollBar
			super.createPropertyDescriptor(getBeanClass(),"horizontalScrollBar", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("horizontalScrollBar.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("horizontalScrollBar.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// horizontalScrollBarPolicy
			super.createPropertyDescriptor(getBeanClass(),"horizontalScrollBarPolicy", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("horizontalScrollBarPolicy.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("horizontalScrollBarPolicy.Desc"), //$NON-NLS-1$
	      		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JScrollPaneMessages.getString("ScrollBarPolicy.AS_NEEDED"), new Integer(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED", //$NON-NLS-1$
	      			JScrollPaneMessages.getString("ScrollBarPolicy.NEVER"), new Integer(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER", //$NON-NLS-1$
	      			JScrollPaneMessages.getString("ScrollBarPolicy.ALWAYS"), new Integer(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS", //$NON-NLS-1$

	    			}
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// rowHeader
			super.createPropertyDescriptor(getBeanClass(),"rowHeader", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("rowHeader.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("rowHeader.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				
	    		}
	    	),
			// rowHeaderView
			super.createPropertyDescriptor(getBeanClass(),"rowHeaderView", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("rowHeaderView.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("rowHeaderView.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),	    	
	    	// validateRoot
			super.createPropertyDescriptor(getBeanClass(),"validateRoot", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("validateRoot.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("validateRoot.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE     	
	    		}
	    	),
	    	// verticalScrollBar
			super.createPropertyDescriptor(getBeanClass(),"verticalScrollBar", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("verticalScrollBar.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("verticalScrollBar.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// verticalScrollBarPolicy
			super.createPropertyDescriptor(getBeanClass(),"verticalScrollBarPolicy", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("verticalScrollBarPolicy.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("verticalScrollBarPolicy.Desc"), //$NON-NLS-1$
	      		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JScrollPaneMessages.getString("ScrollBarPolicy.AS_NEEDED"), new Integer(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED", //$NON-NLS-1$
	      			JScrollPaneMessages.getString("ScrollBarPolicy.NEVER"), new Integer(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER", //$NON-NLS-1$
	      			JScrollPaneMessages.getString("ScrollBarPolicy.ALWAYS"), new Integer(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS), //$NON-NLS-1$
	      				"javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS", //$NON-NLS-1$

	    			}
	    		}
	    	),
	    	// viewport
			super.createPropertyDescriptor(getBeanClass(),"viewport", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("viewport.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("viewport.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// viewportBorder
			super.createPropertyDescriptor(getBeanClass(),"viewportBorder", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("viewportBorder.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("viewportBorder.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// viewportView
			super.createPropertyDescriptor(getBeanClass(),"viewportView", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("viewportView.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("viewportView.Desc"), //$NON-NLS-1$
		      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollPaneMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollPaneMessages.getString("ui.Desc"), //$NON-NLS-1$
		      	//DESIGNTIMEPROPERTY, Boolean.FALSE
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

/* (non-Javadoc)
 * @see org.eclipse.ve.internal.jfc.beaninfo.IvjBeanInfo#overridePropertyDescriptors(java.beans.PropertyDescriptor[])
 */
protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
	PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();
		
	replacePropertyDescriptor(newPDs, "border", null, new Object[] { //$NON-NLS-1$
		EXPERT, Boolean.FALSE,
		}
	);

	return newPDs;
}
}
