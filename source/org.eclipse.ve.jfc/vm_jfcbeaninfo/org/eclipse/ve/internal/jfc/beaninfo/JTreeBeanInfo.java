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
 *  $RCSfile: JTreeBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;

public class JTreeBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle resJTreeBundle = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtree");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTree.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, resJTreeBundle.getString("JTree.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, resJTreeBundle.getString("JTree.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtree32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtree16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			treeExpansionEventSetDescriptor(),
			treeSelectionEventSetDescriptor()
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
	    return loadImage("jtree32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtree16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"addSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("addSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("addSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// addSelectionPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"addSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("addSelectionPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("addSelectionPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// addSelectionPaths(TreePath[])
			super.createMethodDescriptor(getBeanClass(),"addSelectionPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("addSelectionPaths(TreePath[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("addSelectionPaths(TreePath[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("paths", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionPaths(TreePath[]).paths.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of node paths",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath[].class
	      		}		    		
		  	),
		  	// addSelectionRow(int)
			super.createMethodDescriptor(getBeanClass(),"addSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("addSelectionRow(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add specified row to the current selection",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// addSelectionRows(int[])
			super.createMethodDescriptor(getBeanClass(),"addSelectionRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("addSelectionRows(int[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("addSelectionRows(int[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rows", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("addSelectionRows(int[]).rows.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index array",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int[].class
	      		}		    		
		  	),
		  	// clearSelection()
			super.createMethodDescriptor(getBeanClass(),"clearSelection",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("clearSelection().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("clearSelection().Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// collapsePath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"collapsePath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("collapsePath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("collapsePath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("collapsePath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// collapseRow(int)
			super.createMethodDescriptor(getBeanClass(),"collapseRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("collapseRow(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("collapseRow(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("collapseRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// convertValueToText(Object,boolean,boolean,boolean,int,boolean) - discard
		  	// expandPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"expandPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("expandPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("expandPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("expandPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// expandRow(int)
			super.createMethodDescriptor(getBeanClass(),"expandRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("expandRow(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("expandRow(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("expandRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// fireTreeCollapsed(TreePath)
			super.createMethodDescriptor(getBeanClass(),"fireTreeCollapsed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("fireTreeCollapsed(TreePath).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Notify all listeners that node has collapsed",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("fireTreeCollapsed(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// fireTreeExpanded(TreePath)
			super.createMethodDescriptor(getBeanClass(),"fireTreeExpanded",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("fireTreeExpanded(TreePath).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Notify all listeners that node has expanded",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("fireTreeExpanded(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
			// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAnchorSelectionPath()
			super.createMethodDescriptor(getBeanClass(),"getAnchorSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getAnchorSelectionPath().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the path identified as the anchor",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellEditor()
			super.createMethodDescriptor(getBeanClass(),"getCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getCellEditor().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the editor used to edit entries",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellRenderer()
			super.createMethodDescriptor(getBeanClass(),"getCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getCellRenderer().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the current cell renderer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getClosestPathForLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"getClosestPathForLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getClosestPathForLocation(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getClosestPathForLocation(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getClosestPathForLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x in pixels from left edge",
	      				}
	      			),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getClosestPathForLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y in pixels from top edge",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// getClosestRowForLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"getClosestRowForLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getClosestRowForLocation(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getClosestRowForLocation(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getClosestRowForLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x in pixels from left edge",
	      				}
	      			),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getClosestRowForLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y in pixels from top edge",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// getEditingPath()
			super.createMethodDescriptor(getBeanClass(),"getEditingPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getEditingPath().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getEditingPath().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getExpandsSelectedPaths()
			super.createMethodDescriptor(getBeanClass(),"getExpandsSelectedPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getExpandsSelectedPaths().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getExpandsSelectedPaths().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getInvokesStopCellEditing()
			super.createMethodDescriptor(getBeanClass(),"getInvokesStopCellEditing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getInvokesStopCellEditing().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get flag indicating what happens when editing is interrupted",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getLastSelectedPathComponent()
			super.createMethodDescriptor(getBeanClass(),"getLastSelectedPathComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getLastSelectedPathComponent().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	   			OBSCURE, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the last path component in first node of selection",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLeadSelectionPath()
			super.createMethodDescriptor(getBeanClass(),"getLeadSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getLeadSelectionPath().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getLeadSelectionPath().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLeadSelectionRow()
			super.createMethodDescriptor(getBeanClass(),"getLeadSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getLeadSelectionRow().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get index of last node added to selection",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaxSelectionRow()
			super.createMethodDescriptor(getBeanClass(),"getMaxSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getMaxSelectionRow().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the last selected row",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinSelectionRow()
			super.createMethodDescriptor(getBeanClass(),"getMinSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getMinSelectionRow().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the first selected row",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the model providing the data",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPathBounds(TreePath)
			super.createMethodDescriptor(getBeanClass(),"getPathBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getPathBounds(TreePath).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the bounding rectangle for the node"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getPathBounds(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// getPathForLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"getPathForLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getPathForLocation(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get path for node at location",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getPathForLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x in pixels from left edge",
	      				}
	      			),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getPathForLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y in pixels from top edge",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// getPathForRow(int)
			super.createMethodDescriptor(getBeanClass(),"getPathForRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getPathForRow(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get path for node at location",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getPathForRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getPreferredScrollableViewportSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredScrollableViewportSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getPreferredScrollableViewportSize().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	   			OBSCURE, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the preferred viewable size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRowBounds(int)
			super.createMethodDescriptor(getBeanClass(),"getRowBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getRowBounds(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getRowBounds(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getRowBounds(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getRowCount()
			super.createMethodDescriptor(getBeanClass(),"getRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getRowCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getRowCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRowForLocation(int,int)
			super.createMethodDescriptor(getBeanClass(),"getRowForLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getRowForLocation(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get row for node at location",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("x", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getRowForLocation(int,int).x.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "x in pixels from left edge",
	      				}
	      			),
	      			createParameterDescriptor("y", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getRowForLocation(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "y in pixels from top edge",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// getRowForPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"getRowForPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getRowForPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getRowForPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("getRowForPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// getRowHeight()
			super.createMethodDescriptor(getBeanClass(),"getRowHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getRowHeight().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getRowHeight().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getScrollsOnExpand()
			super.createMethodDescriptor(getBeanClass(),"getScrollsOnExpand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getScrollsOnExpand().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getScrollsOnExpand().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionCount()
			super.createMethodDescriptor(getBeanClass(),"getSelectionCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getSelectionCount().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getSelectionCount().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionModel()
			super.createMethodDescriptor(getBeanClass(),"getSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getSelectionModel().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getSelectionModel().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionPath()
			super.createMethodDescriptor(getBeanClass(),"getSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getSelectionPath().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getSelectionPath().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionPaths()
			super.createMethodDescriptor(getBeanClass(),"getSelectionPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getSelectionPaths().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("getSelectionPaths().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionRows()
			super.createMethodDescriptor(getBeanClass(),"getSelectionRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getSelectionRows().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get an array of selected node indices",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getShowsRootHandles()
			super.createMethodDescriptor(getBeanClass(),"getShowsRootHandles",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getShowsRootHandles().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if root node handles are displayed",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getToggleClickCount()
		  	super.createMethodDescriptor(getBeanClass(),"getToggleClickCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getToggleClickCount().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if root node handles are displayed",
	      		EXPERT, Boolean.TRUE,
	      		//OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the TreeUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVisibleRowCount()
			super.createMethodDescriptor(getBeanClass(),"getVisibleRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("getVisibleRowCount().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Number of rows visible",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isCollapsed(TreePath)
			super.createMethodDescriptor(getBeanClass(),"isCollapsed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isCollapsed(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isCollapsed(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isCollapsed(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// isCollapsed(int)
			super.createMethodDescriptor(getBeanClass(),"isCollapsed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isCollapsed(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isCollapsed(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isCollapsed(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isEditable
			super.createMethodDescriptor(getBeanClass(),"isEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isEditable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isEditable.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isEditing
			super.createMethodDescriptor(getBeanClass(),"isEditing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isEditing.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isEditing.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isExpanded(TreePath)
			super.createMethodDescriptor(getBeanClass(),"isExpanded",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isExpanded(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isExpanded(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isExpanded(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// isExpanded(int)
			super.createMethodDescriptor(getBeanClass(),"isExpanded",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isExpanded(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isExpanded(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isExpanded(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isFixedRowHeight
			super.createMethodDescriptor(getBeanClass(),"isFixedRowHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isFixedRowHeight.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the row height is fixed",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isLargeModel
			super.createMethodDescriptor(getBeanClass(),"isLargeModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isLargeModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the tree is configured for a large model",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isOpaque
			super.createMethodDescriptor(getBeanClass(),"isOpaque",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isOpaque.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the tree is opaque"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isPathSelected(TreePath)
			super.createMethodDescriptor(getBeanClass(),"isPathSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isPathSelected(TreePath).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the row for path"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isPathSelected(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// isRootVisible
			super.createMethodDescriptor(getBeanClass(),"isRootVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isRootVisible.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the tree's root node is visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isRowSelected(int)
			super.createMethodDescriptor(getBeanClass(),"isRowSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isRowSelected(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("isRowSelected(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isRowSelected(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree row",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isSelectionEmpty
			super.createMethodDescriptor(getBeanClass(),"isSelectionEmpty",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isSelectionEmpty.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the selection is empty",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isVisible(TreePath)
			super.createMethodDescriptor(getBeanClass(),"isVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("isVisible(TreePath).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Is the specified node visible"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("isVisible(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// makeVisible(TreePath)
			super.createMethodDescriptor(getBeanClass(),"makeVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("makeVisible(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("makeVisible(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("makeVisible(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// removeSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"removeSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("removeSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("removeSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// removeSelectionPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"removeSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("removeSelectionPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("removeSelectionPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// removeSelectionPaths(TreePath[])
			super.createMethodDescriptor(getBeanClass(),"removeSelectionPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("removeSelectionPaths(TreePath[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("removeSelectionPaths(TreePath[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("paths", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionPaths(TreePath[]).paths.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of node paths",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath[].class
	      		}		    		
		  	),
		  	// removeSelectionRow(int)
			super.createMethodDescriptor(getBeanClass(),"removeSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("removeSelectionRow(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove specified row from the current selection",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// removeSelectionRows(int[])
			super.createMethodDescriptor(getBeanClass(),"removeSelectionRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("removeSelectionRows(int[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("removeSelectionRows(int[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rows", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("removeSelectionRows(int[]).rows.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index array",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int[].class
	      		}		    		
		  	),
		  	// scrollPathToVisible(TreePath)
			super.createMethodDescriptor(getBeanClass(),"scrollPathToVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("scrollPathToVisible(TreePath).Name"), //$NON-NLS-1$
	      	 	SHORTDESCRIPTION, resJTreeBundle.getString("scrollPathToVisible(TreePath).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("scrollPathToVisible(TreePath).path.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resJTreeBundle.getString("scrollPathToVisible(TreePath).path.Desc"), //$NON-NLS-1$
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// scrollRowToVisible(int)
			super.createMethodDescriptor(getBeanClass(),"scrollRowToVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("scrollRowToVisible(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Scroll to the specified row",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("scrollRowToVisible(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setAnchorSelectionPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"setAnchorSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setAnchorSelectionPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setAnchorSelectionPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setAnchorSelectionPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree anchor node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// setCellEditor(TreeCellEditor)
			super.createMethodDescriptor(getBeanClass(),"setCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setCellEditor(TreeCellEditor).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell editor",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellEditor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setCellEditor(TreeCellEditor).cellEditor.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree cell editor",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreeCellEditor.class
	      		}		    		
		  	),
		  	// setCellRenderer(TreeCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setCellRenderer(TreeCellRenderer).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setCellRenderer(TreeCellRenderer).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellRenderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setCellRenderer(TreeCellRenderer).cellRenderer.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree cell renderer",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreeCellRenderer.class
	      		}		    		
		  	),
		  	// setEditable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setEditable(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE makes the tree editable",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setEditable(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to make tree editable",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setExpandsSelectedPaths(boolean)
			super.createMethodDescriptor(getBeanClass(),"setExpandsSelectedPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setExpandsSelectedPaths(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE makes the selection changes result in the parent path being expanded",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setExpandsSelectedPaths(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to make tree editable",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),		  	
		  	// setInvokesStopCellEditing(boolean)
			super.createMethodDescriptor(getBeanClass(),"setInvokesStopCellEditing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setInvokesStopCellEditing(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to save data if editing is interrupted",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setInvokesStopCellEditing(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to save edit data",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setLeadSelectionPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"setLeadSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setLeadSelectionPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setLeadSelectionPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setLeadSelectionPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree lead node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// setModel(TreeModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setModel(TreeModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tree model for data",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setModel(TreeModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreeModel.class
	      		}		    		
		  	),
		  	// setRootVisible(boolean)
			super.createMethodDescriptor(getBeanClass(),"setRootVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setRootVisible(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setRootVisible(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setRootVisible(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to display root node",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setRowHeight(int)
			super.createMethodDescriptor(getBeanClass(),"setRowHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setRowHeight(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the height of each node",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("height", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setRowHeight(int).height.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row height in pixels",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setScrollsOnExpand(boolean)
			super.createMethodDescriptor(getBeanClass(),"setScrollsOnExpand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setScrollsOnExpand(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to scroll decendents when node expanded",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setScrollsOnExpand(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to scroll decendents",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setSelectionModel(TreeSelectionModel)
			super.createMethodDescriptor(getBeanClass(),"setSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionModel(SelectionModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the model representing tree node selection",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionModel(SelectionModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TreeSelection model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreeSelectionModel.class
	      		}		    		
		  	),
		  	// setSelectionPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"setSelectionPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setSelectionPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// setSelectionPaths(TreePath[])
			super.createMethodDescriptor(getBeanClass(),"setSelectionPaths",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionPaths(TreePath[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setSelectionPaths(TreePath[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("paths", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionPaths(TreePath[]).paths.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of node paths",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath[].class
	      		}		    		
		  	),
		  	// setSelectionRow(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionRow(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select specified row",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionRow(int).row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectionRows(int[])
			super.createMethodDescriptor(getBeanClass(),"setSelectionRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setSelectionRows(int[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("setSelectionRows(int[]).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("rows", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setSelectionRows(int[]).rows.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node index array",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int[].class
	      		}		    		
		  	),
		  	// setShowsRootHandles(boolean)
			super.createMethodDescriptor(getBeanClass(),"setShowsRootHandles",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setShowsRootHandles(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to show root handles",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setShowsRootHandles(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to show root handles",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setToggleClickCount()
		  	super.createMethodDescriptor(getBeanClass(),"setToggleClickCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setToggleClickCount(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Number of clicks required to toggle node",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    		    createParameterDescriptor("clickCount", new Object[] { //$NON-NLS-1$
	    		    	DISPLAYNAME, resJTreeBundle.getString("setToggleClickCount(int).count.Name"), //$NON-NLS-1$
	    		    	// SHORTDESCRIPTION, "Number of clicks",
	    		        }
	    		    )
	            },
      		    new Class[] {
      			    int.class
      		    }
		  	),
		  	// setUI(TreeUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setUI(TreeUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the tree UI",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setUI(TreeUI).aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.TreeUI.class
	      		}		    		
		  	),
		  	// setVisibleRowCount(int)
			super.createMethodDescriptor(getBeanClass(),"setVisibleRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("setVisibleRowCount(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set number of rows visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newCount", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("setVisibleRowCount(int).newCount.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New number of visible rows",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// startEditingAtPath(TreePath)
			super.createMethodDescriptor(getBeanClass(),"startEditingAtPath",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("startEditingAtPath(TreePath).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("startEditingAtPath(TreePath).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("path", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTreeBundle.getString("startEditingAtPath(TreePath).path.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tree node path",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.tree.TreePath.class
	      		}		    		
		  	),
		  	// stopEditing()
			super.createMethodDescriptor(getBeanClass(),"stopEditing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("stopEditing().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Stop editing"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// treeDidChange()
			super.createMethodDescriptor(getBeanClass(),"treeDidChange",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("treeDidChange().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Resize and repaint",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// updateUI()
			super.createMethodDescriptor(getBeanClass(),"updateUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("updateUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "update the UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
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
	    	// anchorSelectionPath
			super.createPropertyDescriptor(getBeanClass(),"anchorSelectionPath", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("anchorSelectionPath.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("anchorSelectionPath.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// cellEditor
			super.createPropertyDescriptor(getBeanClass(),"cellEditor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("cellEditor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("cellEditor.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE,
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// cellRenderer
	    	super.createPropertyDescriptor(getBeanClass(),"cellRenderer", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("cellRenderer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("cellRenderer.Desc"), //$NON-NLS-1$
	      	// PREFERRED, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE,
			EXPERT, Boolean.TRUE
				}   
	    	),
			// editable
			super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("editable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("editable.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// editing
			super.createPropertyDescriptor(getBeanClass(),"editing", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("editing.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("editing.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// editingPath
			super.createPropertyDescriptor(getBeanClass(),"editingPath", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("editingPath.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("editingPath.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// expandsSelectedPaths
			super.createPropertyDescriptor(getBeanClass(),"expandsSelectedPaths", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("expandsSelectedPaths.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("expandsSelectedPaths.Desc"), //$NON-NLS-1$
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// fixedRowHeight
			super.createPropertyDescriptor(getBeanClass(),"fixedRowHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("fixedRowHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("fixedRowHeight.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// invokesStopCellEditing
			super.createPropertyDescriptor(getBeanClass(),"invokesStopCellEditing", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("invokesStopCellEditing.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("invokesStopCellEditing.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// largeModel
			super.createPropertyDescriptor(getBeanClass(),"largeModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("largeModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("largeModel.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE,
	    		}
	    	),
	    	// lastSelectedPathComponent
			super.createPropertyDescriptor(getBeanClass(),"lastSelectedPathComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("lastSelectedPathComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("lastSelectedPathComponent.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// leadSelectionPath
			super.createPropertyDescriptor(getBeanClass(),"leadSelectionPath", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("leadSelectionPath.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("leadSelectionPath.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// leadSelectionRow
			super.createPropertyDescriptor(getBeanClass(),"leadSelectionRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("leadSelectionRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("leadSelectionRow.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// maxSelectionRow
			super.createPropertyDescriptor(getBeanClass(),"maxSelectionRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("maxSelectionRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("maxSelectionRow.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// minSelectionRow
			super.createPropertyDescriptor(getBeanClass(),"minSelectionRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("minSelectionRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("minSelectionRow.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("model.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("model.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
			EXPERT, Boolean.TRUE
	    		}
	    	),
			// opaque
			super.createPropertyDescriptor(getBeanClass(),"opaque", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("opaque.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("opaque.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// preferredScrollableViewportSize
			super.createPropertyDescriptor(getBeanClass(),"preferredScrollableViewportSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("preferredScrollableViewportSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("preferredScrollableViewportSize.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// rootVisible
			super.createPropertyDescriptor(getBeanClass(),"rootVisible", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("rootVisible.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("rootVisible.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE	      	
	    		}
	    	),
	    	// rowCount
			super.createPropertyDescriptor(getBeanClass(),"rowCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("rowCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("rowCount.Desc"),      	 //$NON-NLS-1$
	    		}
	    	),
	    	// rowHeight
			super.createPropertyDescriptor(getBeanClass(),"rowHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("rowHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("rowHeight.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE	      	
	    		}
	    	),
	    	// scrollableTracksViewportHeight
			super.createPropertyDescriptor(getBeanClass(),"scrollableTracksViewportHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("scrollableTracksViewportHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("scrollableTracksViewportHeight.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// scrollableTracksViewportWidth
			super.createPropertyDescriptor(getBeanClass(),"scrollableTracksViewportWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("scrollableTracksViewportWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("scrollableTracksViewportWidth.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectionCount
			super.createPropertyDescriptor(getBeanClass(),"selectionCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionCount.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectionEmpty
			super.createPropertyDescriptor(getBeanClass(),"selectionEmpty", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionEmpty.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionEmpty.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectionModel
			super.createPropertyDescriptor(getBeanClass(),"selectionModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionModel.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	BOUND, Boolean.TRUE,
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionPath
			super.createPropertyDescriptor(getBeanClass(),"selectionPath", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionPath.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionPath.Desc"),	      	 //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// selectionPaths
			super.createPropertyDescriptor(getBeanClass(),"selectionPaths", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionPaths.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionPaths.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectionRow
			super.createPropertyDescriptor(getBeanClass(),"selectionRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionRow.Desc"),	      	 //$NON-NLS-1$
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionRows
			super.createPropertyDescriptor(getBeanClass(),"selectionRows", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("selectionRows.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("selectionRows.Desc"),	      	 //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// showsRootHandles
			super.createPropertyDescriptor(getBeanClass(),"showsRootHandles", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("showsRootHandles.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("showsRootHandles.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// toggleClickCount
	    	super.createPropertyDescriptor(getBeanClass(),"toggleClickCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("toggleClickCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("toggleClickCount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// visibleRowCount
			super.createPropertyDescriptor(getBeanClass(),"visibleRowCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("visibleRowcount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("visibleRowCount.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,	      	
	    		}
	    	),
	    	// scrollsOnExpand
			super.createPropertyDescriptor(getBeanClass(),"scrollsOnExpand", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTreeBundle.getString("scrollsOnExpand.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTreeBundle.getString("scrollsOnExpand.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resJTreeBundle.getString("ui.Name"), //$NON-NLS-1$
			SHORTDESCRIPTION, resJTreeBundle.getString("ui.Desc"), //$NON-NLS-1$
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
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor treeExpansionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.TreeExpansionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.TreeExpansionListener.class,
				"treeExpanded",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("treeExpanded.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("treeExpanded.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("event", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resJTreeBundle.getString("treeExpansionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Tree expansion event",
	      			})
	      		},
	      		paramTypes
		  	),
		  	super.createMethodDescriptor(javax.swing.event.TreeExpansionListener.class,
				"treeCollapsed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("treeCollapsed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("treeCollapsed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("event", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resJTreeBundle.getString("treeExpansionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Tree expansion event",
	      			})
	      		},
	      		paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"treeExpanded", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, resJTreeBundle.getString("treeExpansionEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resJTreeBundle.getString("treeExpansionEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.TreeExpansionListener.class,
						"addTreeExpansionListener", "removeTreeExpansionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor treeSelectionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.TreeSelectionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.TreeSelectionListener.class,
				"valueChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTreeBundle.getString("valueChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTreeBundle.getString("valueChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("event", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resJTreeBundle.getString("treeSelectionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Tree selection changed",
	      			})
	      		},
	      		paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"treeSelection", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resJTreeBundle.getString("treeSelectionEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resJTreeBundle.getString("treeSelectionEvents.Desc"), //$NON-NLS-1$
	      				}, 
						aDescriptorList, javax.swing.event.TreeSelectionListener.class,
						"addTreeSelectionListener", "removeTreeSelectionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
