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
 *  $RCSfile: ContainerBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.awt.FocusTraversalPolicy;
import java.beans.*;
public class ContainerBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle rescontainer = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.container");  //$NON-NLS-1$
	
	
public java.beans.EventSetDescriptor containerEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ContainerEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ContainerListener.class,
				"componentAdded", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescontainer.getString("componentAddedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("componentAddedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("containerEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("containerEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component added event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.ContainerListener.class,
				"componentRemoved", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescontainer.getString("componentRemovedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("componentRemovedSD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("containerEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("containerEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "component removed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"container", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, rescontainer.getString("containerEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, rescontainer.getString("containerEventsSD"), //$NON-NLS-1$
						EVENTADAPTERCLASS, "java.awt.event.ContainerAdapter",	      				 //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.ContainerListener.class,
						"addContainerListener", "removeContainerListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Container.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the ContainerBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Container.class);
		aDescriptor.setDisplayName(rescontainer.getString("ContainerDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(rescontainer.getString("ContainerSD")); //$NON-NLS-1$
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
			containerEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
		   // add(Component)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(Component)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("add(Component)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to add",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Component.class 
	      	}	    		
		  	),							
			// add(Component,int)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(Component,int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add the component at position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to add",
	      			}
	      		),
	    			createParameterDescriptor("index", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Position to add at",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Component.class,
	      		int.class
	      	}	    		
		  	),
			// add(Component,Object,int)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(Component,Object,int)",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE
	      		// SHORTDESCRIPTION, "Add the component with constraints at position",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to add",
	      			}
	      		),
	      		createParameterDescriptor("constraints", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("constraintsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Layout constraints",
	      			}
	      		),
	    			createParameterDescriptor("index", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Position to add at",
	      			}
	      		),	      		
	      	},
	      	new Class[] { 
	      		java.awt.Component.class,
	      		java.lang.Object.class,
	      		int.class
	      	}	    		
		  	),
			// add(Component,Object)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(Component,Object)",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE
	      		// SHORTDESCRIPTION, "Add the component with constraints",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to add",
	      			}
	      		),
	    			createParameterDescriptor("constraints", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("constraintsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Layout constraints",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Component.class,
	      	   java.lang.Object.class
	      	}	    		
		  	),
			// add(String,Component)
			super.createMethodDescriptor(getBeanClass(), "add", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "add(String,Component)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("add(String,Component)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("constraintsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Layout constraints",
	      			}
	      		),	    			
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to add",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		String.class, java.awt.Component.class
	      	}	    		
		  	),		  	
			// addNotify()
			super.createMethodDescriptor(getBeanClass(), "addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify component has been added",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// doLayout()
			super.createMethodDescriptor(getBeanClass(), "doLayout", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "doLayout()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Lay out the components",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getComponent(int)
			super.createMethodDescriptor(getBeanClass(), "getComponent", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getComponent(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("getComponent(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "index of Component to get",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// getComponentCount()
			super.createMethodDescriptor(getBeanClass(), "getComponentCount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getComponentCount()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("getComponentCount()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getComponents()
		   super.createMethodDescriptor(getBeanClass(), "getComponents", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getComponents()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("getComponents()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getInsets()
			super.createMethodDescriptor(getBeanClass(), "getInsets", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getInsets()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the insets",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLayout()
			super.createMethodDescriptor(getBeanClass(), "getLayout", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getLayout()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the layout manager",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMaximumSize()
			super.createMethodDescriptor(getBeanClass(), "getMaximumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMaximumSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the maximum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMinimumSize()
			super.createMethodDescriptor(getBeanClass(), "getMinimumSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimumSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getPreferredSize()
			super.createMethodDescriptor(getBeanClass(), "getPreferredSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getPreferredSize()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the preferred size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// invalidate()
			super.createMethodDescriptor(getBeanClass(), "invalidate", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "invalidate()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Invalidate container and parents",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isAncestorOf(Component)
			super.createMethodDescriptor(getBeanClass(), "isAncestorOf", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isAncestorOf(Component)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("isAncestorOf(Component)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentPArmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to query",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Component.class 
	      	}	    		
		  	),
			// list(PrintStream,int)
			super.createMethodDescriptor(getBeanClass(), "list", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "list(PrintStream,int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print listing of container to stream",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("out", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("outParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Stream to print to",
	      			}
	      		),
	      		createParameterDescriptor("indent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("indentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "number of spaces to indent",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.io.PrintStream.class,
	      		int.class
	      	}   		
		  	),
			// paintComponents(Graphics)
			super.createMethodDescriptor(getBeanClass(), "paintComponents", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paintComponents(Graphics)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Paint the components",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Graphics.class
	      	}   		
		  	),
			// printComponents(Graphics)
			super.createMethodDescriptor(getBeanClass(), "printComponents", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "printComponents(Graphics)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print this container's components",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("graphics", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			}
	      		)	      		
	      	},
	      	new Class[] { 
	      		java.awt.Graphics.class
	      	}   		
		  	),
			// remove(int)
			super.createMethodDescriptor(getBeanClass(), "remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("remove(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("indexParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Index of Component to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// remove(Component)
			super.createMethodDescriptor(getBeanClass(), "remove", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "remove(Component)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("remove(Component)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("comp", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("componentParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Component to remove",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		java.awt.Component.class 
	      	}	    		
		  	),
			// removeAll()
			super.createMethodDescriptor(getBeanClass(), "removeAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeAll",//$NON-NLS-1$
	      		SHORTDESCRIPTION, rescontainer.getString("removeAll()DN"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	   new Class[] {}  		
		  	),
			// removeNotify()
			super.createMethodDescriptor(getBeanClass(), "removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Notify to remove peers",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setLayout(LayoutManager)
			super.createMethodDescriptor(getBeanClass(), "setLayout", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setLayout(LayoutManager)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the layout manager",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mgr", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, rescontainer.getString("layoutManagerParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Layout Manager",
	      			}
	      		),		      				      		
	      	},
	      	new Class[] {
	      		java.awt.LayoutManager.class, 
	      	}   		
		  	),
			// validate()
			super.createMethodDescriptor(getBeanClass(), "validate", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "validate()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Validate container and components",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFocusTraversalPolicy()
			super.createMethodDescriptor(getBeanClass(),"getFocusTraversalPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescontainer.getString("MthdDesc.getFocusTraversalPolicy.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the focus traversal policy",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setFocusTraversalPolicy(FocusTraversalPolicy)
			super.createMethodDescriptor(getBeanClass(),"setFocusTraversalPolicy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, rescontainer.getString("MthdDesc.SetFocusTraversalPolicy.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the focus traversal policy",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("focusTraversalPolicy", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, rescontainer.getString("ParamDesc.SetFocusTraversalPolicy.focusTraversalPolicy.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Focus traversal policy",
	      				})
	      		},
	      		new Class[] {
					FocusTraversalPolicy.class
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
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// componentCount()
			super.createPropertyDescriptor(getBeanClass(), "componentCount", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescontainer.getString("componentCountDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescontainer.getString("componentCountSD"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	    		}
	    	),
			// insets
			super.createPropertyDescriptor(getBeanClass(), "insets", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescontainer.getString("insetsDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescontainer.getString("insetsSD"),	      	 //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE 
	    		}
	    	),
			// layout
			super.createPropertyDescriptor(getBeanClass(), "layout", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescontainer.getString("layoutDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescontainer.getString("layoutSD"), //$NON-NLS-1$
	    		}
	    	),
			// preferredSize
			super.createPropertyDescriptor(getBeanClass(), "preferredSize", new Object[] {//$NON-NLS-1$
		   	DISPLAYNAME, rescontainer.getString("preferredSizeDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescontainer.getString("preferredSizeSD"), //$NON-NLS-1$
	    		}
	    	),
	    	// focusTraversalPolicy
			super.createPropertyDescriptor(getBeanClass(),"focusTraversalPolicy", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, rescontainer.getString("PropDesc.focusTraversalPolicy.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, rescontainer.getString("PropDesc.focusTraversalPolicy.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//PREFERRED, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}
