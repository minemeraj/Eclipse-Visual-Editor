/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;
/*
 *  $RCSfile: ImageController.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-22 16:24:10 $ 
 */

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/** 
 * The IconDialog and IconController form the Icon property editor,
 * which follows the UI-delegate pattern used by several Swing components.
 * The IconDialog is the View, and the IconController is the Controller.
 * Although these two classes are separate, there is high coupling 
 * between the two (as is the case in all UI-delegate components).  
 * The file system (directory structure) is the Model in this case.
 */
public class ImageController {


	private static final String PROJECT_SCOPE_START_STRING = "getClass().getResourceAsStream("; //$NON-NLS-1$
	/** 
	 * The list of image files found in a certain directory
	 */
	private org.eclipse.swt.widgets.List fileList;

	/** 
	 * Shows iconified version of all loaded images
	 */
	private ImageScreenDisplay drawingBoard;

	/** 
	 * A flag indicating whether or not subdirectories are to be searched
	 * when looking for images 
	 */
	private boolean crawlEnabled;

	/**
	 * A flag indicating whether or not all images were loaded from the 
	 * currently selected directory, or whether some images were left out (a stop search was requested).
	 * If all images are not already loaded when the user specifies a new filter,
	 * the model (file system) is visited again to get all the images.
	 */
	private boolean allImagesLoaded;

	/**
	 * The label box where the path of the selected file is to be displayed
	 */
	private Label path;

	/**
	 * This is where the a selected image is displayed.
	 */
	private SingleImageDisplay imageCanvas;

	/**
	 * The filter for supported image types. Files need to be valid, and it will directories.
	 * Currently: VALID_EXTENSIONS static variable
	 */
	private ImageFileFilter imageFilter = new ImageFileFilter();

	/**
	 * Filter for what shows up on the tree.  Currently, only directories are allowed.
	 * But, it will probably change in the future to allow for .jar files as well.
	 */
	private DirectoryFilter df = new DirectoryFilter();

	/**
	 * This is the display for the view (i.e. the IconDialog).  The ImageLoader
	 * thread uses it to make changes to the 
	 */
	private Display dialogDisplay;

	/**
	 * This is the complete list of all the images that were loaded during the last
	 * icon/image search, irrespective of what the current specified filter is.  
	 * This is useful in the case when the user specifies a new filter.  In that case,
	 * this is the list that is refined and displayed on the IconScreenDisplay.
	 * This removes the need of revisiting the model (file system).  The only time
	 * that the directory structure is revisited is when all the images are not
	 * already loaded, and the user hits enter after specifying the new filter.
	 * 
	 * The list is of type IPath.
	 */
	private ArrayList completeFilepaths = new ArrayList();

	/**
	 * What the user specifies as the filter is stored in here.  This provides 
	 * ImageLoader thread with access to the filter.
	 */
	private String filterText = ""; //$NON-NLS-1$

	/**
	 * The IconDialog hosting this controller
	 */
	private ImageDialog dialog;

	/**
	 * This is the initial value to select. If not null, the ImageLoader thread will
	 * try and find the icon that has currently been set for the widget.
	 * If null it won't try to select.
	 */
	private IPath initialTry;

	/**
	 * This is the filter box where the user can specify a filter to be used
	 * to refine the list of images.
	 */
	private Combo filter;

	private IJavaProject jproj;

	private TreeViewer treeViewer;
	private Group locationGroup; // Group containing location controls. Entire group will be enabled/disabled during searches.
	private IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

	private static final int NO_MODE = -1;
	public static final int SEARCH_PROJECT = 0;
	public static final int SEARCH_FILE_SYSTEM = 1;

	private int currentScope = NO_MODE;

	private String initString = ""; //$NON-NLS-1$

	private ImageLoaderThread imageLoaderThread = null;

	/*
     * <package> protected so that SingleImageDisplay can access them
     */
    static Image noMemoryIcon = null ;
	static Image errorIcon = null;
	static Image warnIcon = null;	
    

    static{
    	noMemoryIcon = CDEPlugin.getImageFromPlugin(SwtPlugin.getDefault(), "icons/nomemory.gif" ); //$NON-NLS-1$
        	
		errorIcon = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJS_ERROR_TSK);        	
		warnIcon = PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJS_WARN_TSK);        			
    }


	public ImageController(
		ImageDialog dialog,
		IProject proj,
		Tree t,
		org.eclipse.swt.widgets.List l,
		ImageScreenDisplay g,
		Label p,
		SingleImageDisplay c,
		Display screen,
		Button crawlButton,
		Group lg,
		Combo imgFilter) {
		this.dialog = dialog;
		jproj = JavaCore.create(proj);
		fileList = l;
		drawingBoard = g;
		path = p;
		imageCanvas = c;
		dialogDisplay = screen;
		locationGroup = lg;
		filter = imgFilter;
		allImagesLoaded = true;

		fileList.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				listSelected();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				closeOKDialog();
			}
		});

		drawingBoard.addSelectionListener(new ImageScreenDisplay.SelectionListener() {
			public void imageSelected(int index) {
				updateScreen(index);
			}

			public void imageDefaultSelected() {
				closeOKDialog();
			}

			/*
			 * This is a helper method that is called when an interesting mouse event has
			 * been fired.  Based on what was selected/deselected on the drawingBoard
			 * (IconScreenDisplay) due to the mouse event, it updates the rest of the
			 * View (IconDialog) -- the list of files and the Selected Image group.
			 *
			 * @param	index		The index of the selected image on the drawingBoard
			 */
			private void updateScreen(int index) {
				if (drawingBoard.isValidIndex(index)) {
					if (fileList.getSelectionIndex() != index) {
						fileList.setSelection(index);
						fileList.showSelection();
					}
				} else {
					fileList.deselectAll();
				}

				IPath selectedPath = drawingBoard.getSelectedImagePath();
				ImageController.this.dialog.getStatusLineManager().setErrorMessage("");				 //$NON-NLS-1$
				if (selectedPath != null) {
					updateSelectedImage(selectedPath);
				} else {
					clearDisplay();
				}
			}
		});

		crawlButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				crawlEnabled = ((Button) event.widget).getSelection();
				rebuildList();
			}
		});


		filter.addModifyListener(new ModifyListener() {
		
			public void modifyText(ModifyEvent e) {
				selectionChanged();
			}
					
			/*
			 * This is the function that handles what needs to be done when something is
			 * typed or selected in the filter box.  
			 * 
			 */
			private void selectionChanged() {
				String choice = filter.getText().trim();
				if (!filterText.equals(choice)) {
					filterText = choice;
					if (allImagesLoaded)
						refineList();
					else
						rebuildList(); // The list was not complete from last time, need to get it again to apply filter.
				}
			}
	
			/*
			 * This is a helper method that refines completeList and origImages based on the
			 * currently specified filter, and displays these new lists of Strings and Images
			 * in the fileList and on drawingBoard respectively.
			 * 
			 * Note: It is assumed this is in the UI thread because the key stroke is also in the UI thread.
			 */
			private void refineList() {
				if (completeFilepaths.size() != 0) {
					fileList.removeAll();
					drawingBoard.removeAll();
					clearDisplay();
					int size = completeFilepaths.size();
					for (int i = 0; i < size; i++) {
						final IPath entry = (IPath) completeFilepaths.get(i);
						if (meetsFilter(entry)) {
							drawingBoard.add(entry);
							fileList.add(entry.lastSegment());
						}
					}
	
					listSelected();
				}
			}		
		});

		treeViewer = new TreeViewer(t);
		treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				treeSelected((IStructuredSelection) event.getSelection());
			}
		});
	}

	/**
	 * Set the initial value. This must be called only after the entire IconDialog has been created because
	 * it needs things like StatusLineManager.
	 * 
	 * @param value The init path of the current value from the property editor. (i.e. the parm to the ImageIcon).
	 * @return int The search scope selected. Caller should then select the appropriate radio button so that it matches.
	 *              The static values in this class can be used for this.
	 * 
	 * NOTE: This must be called within the UI thread.
	 */
	public int setInitialValue(String value) {
		// First determine the starting scope from the input value.
		if (value != null && value.length() > 0) {
			if (value.startsWith(PROJECT_SCOPE_START_STRING)) {
				IPackageFragment startSelectionFragment = null;	// Fragment to select
				int firstQuoteIndex = value.indexOf('"', PROJECT_SCOPE_START_STRING.length());
				if (firstQuoteIndex != -1) {
					int lastQuoteIndex = value.indexOf('"', firstQuoteIndex + 1);
					if (lastQuoteIndex != -1) {
						String pathString = value.substring(firstQuoteIndex+1, lastQuoteIndex).trim();
						try {
							IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
							// Need to look in the roots to find first one.
							IPackageFragmentRoot[] roots = jproj.getAllPackageFragmentRoots();
							for (int i = 0; i < roots.length; i++) {
								IResource res = workspaceRoot.findMember(roots[i].getPath().append(pathString));
								if (res instanceof IFile) {
									startSelectionFragment = jproj.findPackageFragment(res.getFullPath().removeLastSegments(1));
									initialTry = res.getFullPath();	// It will be the one to finally select
									break;
								}
							}
						} catch (JavaModelException e) {
						}
					}
				}
				
				setSearchScope(SEARCH_PROJECT);
				if (startSelectionFragment != null)
					treeViewer.setSelection(new StructuredSelection(startSelectionFragment), true);
			} else if (value.length() > 3 && value.charAt(0) == '"' && value.charAt(value.length()-1) == '"') {
				IPath path = new Path(value.substring(1, value.length()-1));
				File startSelectionDirectory = path.toFile();
				if (startSelectionDirectory .canRead() && !startSelectionDirectory.isHidden())
					startSelectionDirectory = startSelectionDirectory.getParentFile();	// Actually want the directory the file is in.
				setSearchScope(SEARCH_FILE_SYSTEM);
				if (startSelectionDirectory != null) {
					initialTry = path;
					treeViewer.setSelection(new StructuredSelection(startSelectionDirectory), true);
				}
			} else {
				setSearchScope(SEARCH_PROJECT);	// Can't figure out, do a default
			}
		} else {
			setSearchScope(SEARCH_PROJECT);	// Can't figure out, do a default
		}
		
		return currentScope;
	}

	protected void rebuildList() {
		// Rebuild the list
		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		if (!selection.isEmpty()) {
			treeSelected(selection);
		}
	}

	private Button getOKButton() {
		return dialog.getButton(IDialogConstants.OK_ID);
	}


	private void closeOKDialog() {
		// Will close if ok is enabled. If not enabled it won't close.
		getOKButton().getDisplay().asyncExec(new Runnable() {
			public void run() {
				getOKButton().getShell().traverse(SWT.TRAVERSE_RETURN); // This will cause the default button to be clicked.
			}
		});
	}

	/*
	 * Content provider for java for our requirements.
	 * We extend the standard because it does some important things. We will
	 * just fine tune it here.
	 */	 
	private static class JavaContentProvider extends StandardJavaElementContentProvider {
				
		private boolean validRoot(IPackageFragmentRoot root) {
			return !root.isExternal() && !root.isArchive();
		}
		
		/*
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements(Object inputElement) {
			// This would be a project, and since it is the root, we want all packageFragmentRoots, including from required projects.
			try {
				if (inputElement instanceof IJavaProject) {
					IPackageFragmentRoot[] roots = ((IJavaProject) inputElement).getAllPackageFragmentRoots();
					ArrayList newRoots = new ArrayList(roots.length);
					for (int i = 0; i < roots.length; i++) {
						if (validRoot(roots[i]))
							newRoots.add(roots[i]);
					}
					
					return newRoots.toArray();
				}
			} catch (JavaModelException e) {
			}
			return StandardJavaElementContentProvider.NO_CHILDREN;			
		}
						
		/*
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
		 */
		public Object[] getChildren(Object parentElement) {
			try {
				// I don't need all types, I'm looking specifically only for package fragment roots. They are the only
				// element in this environment that can have children.
				if (parentElement instanceof IPackageFragmentRoot) 
					return ((IPackageFragmentRoot) parentElement).getChildren();	// Get just the package fragments.
			} catch (JavaModelException e) {
			}

			return StandardJavaElementContentProvider.NO_CHILDREN;	
		}
		
		/*
		 * @see org.eclipse.jdt.ui.StandardJavaElementContentProvider#hasChildren(Object)
		 */
		public boolean hasChildren(Object element) {
			// For package fragments, we don't want to show that it has children. It justs puts a plus sign that is not needed.
			if (element instanceof IPackageFragment)
				return false;
			else
				return super.hasChildren(element);
		}
		
		protected Object skipProjectPackageFragmentRoot(IPackageFragmentRoot root) {
			// We don't want to skip roots that are the project. We don't show the project in
			// our tree. Because of this we can never programatically select fragments.
			// So instead we return the root as is.
			return root;	
		}
	
	}
	
	private static final Object[] NO_CHILDREN = new Object[0];	
	private class FileContentProvider implements ITreeContentProvider {
		public Object[] getChildren(Object o) {
			Object[] children = null;
			if (o == File.class) {
				// Root, so get all roots (note this won't return any UNC files, only drives and actual attached dirs (UNIX)
				children = File.listRoots();
			} else if (o instanceof File) {
				children = ((File) o).listFiles(df);
			}
			if (children != null) {
				Arrays.sort(children, new Comparator() {
					public int compare(Object o1, Object o2) {
						return (((File) o1).getName().compareToIgnoreCase(((File) o2).getName()));
					}
				});
			} else
				children = NO_CHILDREN;
			return children;
		}
		public boolean hasChildren(Object o) {
			if (o == File.class)
				return true; // For the root, there has to be something, or we wouldn't be running
			else if (o instanceof File) {
				File[] lf = ((File) o).listFiles(df);
				return (lf != null && lf.length > 0);
			} else
				return false;
		}

		public Object getParent(Object element) {
			if (element == File.class)
				return null;
			else if (element instanceof File)
				return ((File) element).getParentFile();
			else
				return null;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}
	}
	
	/**
	 * Set the search scope to use.
	 * 
	 * NOTE: This must be called within the UI thread.
	 */
	public void setSearchScope(int newMode) {
		boolean changedScope = currentScope != newMode;
		currentScope = newMode;
		if (changedScope) {
			clearDisplay();
			fileList.removeAll();
			drawingBoard.removeAll();
			dialog.getStatusLineManager().setErrorMessage("");							 //$NON-NLS-1$
			switch (currentScope) {
				case SEARCH_PROJECT:
					ITreeContentProvider contentProvider = new JavaContentProvider();
					JavaElementLabelProvider labelProvider = new JavaElementLabelProvider() {
						/**
						 * @see org.eclipse.jdt.ui.JavaElementLabelProvider#getText(Object)
						 */
						public String getText(Object element) {
							// Need a slight override, for source roots, want to show the entire path so that the project shows up.
							// It doesn't look as good because you can't tell what project the root is in when there are required projects.
							String result = super.getText(element);
							if (element instanceof IPackageFragmentRoot) 
								result = MessageFormat.format(SWTMessages.ImageController_RootLabel, new Object[] {result, ((IPackageFragmentRoot) element).getJavaProject().getElementName()}); 
							return result;
						}
					};
					treeViewer.setContentProvider(contentProvider);
					treeViewer.setLabelProvider(labelProvider);
					treeViewer.setInput(jproj);
					break;
				case SEARCH_FILE_SYSTEM:
					// The content provider we're providing will only return directories, so we don't need a viewer filter.
					treeViewer.setContentProvider(new FileContentProvider());		
					treeViewer.setLabelProvider(new LabelProvider() {		
						Image folderImage = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
						public String getText(Object element) {
							if (element instanceof File) {
								File f = (File) element;
								if (f.getParent() == null)
									return f.toString(); // It's a root, so it has no name, use full path
								else
									return f.getName();
							} else
								return super.getText(element);
						}
		
						public Image getImage(Object element) {
							return element instanceof File ? folderImage : super.getImage(element);
						}
					});
		
					treeViewer.setInput(File.class); // Now kick off the new content
					break;
			}
		}
	}

	public String getInitString() {
		return (initString);
	}

	private void setInitString(String newVal) {
		initString = newVal;
	}

	/*
	 * This is a helper method that is responsible for updating the SelectedImage
	 * group.  Whenever a selection occurs, this method is called so
	 * that it can change the Selected Image group to reflect the change.
	 *
	 * @param	f	The File representing the newly selected image
	 * 
	 * NOTE: This should be called from UI Thread
	 */
	private void updateSelectedImage(IPath fpath) {
		path.setToolTipText(fpath.toString());
		path.setText(fpath.toString());
		imageCanvas.setImage(drawingBoard.getSelectedImagePath(), currentScope == SEARCH_PROJECT ? workspaceRoot : null);
		
		if (imageCanvas.getImageMessage() != null) {
			// We had a warning, just put up the warning, but allow them to select anyhow.
			dialog.getStatusLineManager().setErrorMessage(warnIcon, imageCanvas.getImageMessage());
		}

		if (currentScope == ImageController.SEARCH_FILE_SYSTEM) {
			getOKButton().setEnabled(true);			
			setInitString(BeanUtilities.createStringInitString(fpath.toString()));
		} else {
			IPackageFragmentRoot[] roots = null;
			try {
				roots = jproj.getAllPackageFragmentRoots();
			} catch (JavaModelException e) {
			}
			
			IPath relativePath = null;
			int rootIndex = -1;
			for (int i = 0; i < roots.length; i++) {
				IPath rootPath = roots[i].getPath();
				if (rootPath.isPrefixOf(fpath)) {
					// This is the root of the desired file
					// Remove the root so we have just from the root down as the name
					relativePath = fpath.removeFirstSegments(rootPath.segmentCount());
					rootIndex = i;
					break;
				}
			}
			
			// Bit of a kludge, but there may be duplicates, but under different roots. Need to go through again and
			// see if the relative path is found under other roots. If so, and the other path is ahead of the found one,
			// then this is a dup and not allowed.
			for (int i = 0; i<rootIndex; i++) {
				IPath rootPath = roots[i].getPath();
				IResource res = workspaceRoot.findMember(rootPath.append(relativePath));
				if (res instanceof IFile) {
					// A duplicate found.
					getOKButton().setEnabled(false);
					dialog.getStatusLineManager().setErrorMessage(MessageFormat.format(SWTMessages.ImageController_HiddenImage_ERROR_, new Object[] {res.getFullPath().toString()})); 
					return;
				}
			}
			getOKButton().setEnabled(true);
			relativePath = relativePath.makeAbsolute();	// Need to make it absolute so that the getClass().getResource() will find it relative to the classpath roots and not relative to the class.
			setInitString("getClass().getResourceAsStream(" + BeanUtilities.createStringInitString(relativePath.toString()) + ")"); //$NON-NLS-1$ //$NON-NLS-2$								
		}
	}

	/*
	 * This is a helper method that is called when a selection event occurs on 
	 * the list.  This method synchronizes the rest of the View to reflect the
	 * newly made selection.
	 */
	private void listSelected() {
		drawingBoard.setSelected(fileList.getSelectionIndex());
		drawingBoard.showSelection();
	}

	/*
	 * This is a helper method that is called when the Model (file system) 
	 * needs to be searched for images.  This method initiates the process
	 * by enabling/disabling various View components as needed, and then
	 * dispatches a separate thread (ImageLoaderThread) that handles the
	 * actual class of querying the directory.  
	 *
	 * @param	tie	The TreeItem representing the folder that has to
	 *			searched for images.
	 */
	private void treeSelected(IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		File f = null;
		IPath path = null;
		if (element instanceof IJavaElement && (crawlEnabled || !(element instanceof IPackageFragmentRoot))) {
			// IPackageFragmentRoot files are taken care of by the default package entry instead.
			// Except if crawlEnabled. In that case, let package root to the guy so that it will
			// crawl through it to the packages themselves.
			try {
				IResource res = ((IJavaElement) element).getUnderlyingResource();
				f = res.getLocation().toFile();
				path = res.getFullPath(); // So that it is local to workspace
				// In this case it would be a package fragment or package fragment root.
			} catch (JavaModelException e) {
			}
		} else if (element instanceof File) {
			f = (File) element;
			path = new Path(f.getPath());
		}

		// We know the old thread (if there is one) is finished because we couldn't get
		// here otherwise because the entire browsein group and filter text is disabled during loading.
		imageLoaderThread = new ImageLoaderThread(f, path);
		imageLoaderThread.setPriority(Thread.NORM_PRIORITY);
		imageLoaderThread.start();
	}

	/**
	 * This method disposes all the resources (mainly, the various images) that
	 * were being used by this class.  This class is not a widget, and this 
	 * method is not to be confused with the dispose() method of a widget (albeit
	 * it serves a similar purpose).  This method should be called when an instance 
	 * of this class is no longer required.
	 */
	public void dispose() {
		if (imageLoaderThread != null)
			imageLoaderThread.terminateThread();
	}

	/*
	 * This method is similar to updateSelectedImage, except that this is called 
	 * whenever an image is deselected.  This method simply "clears" the 
	 * Selected Image group to reflect the chage.
	 */
	private void clearDisplay() {
		getOKButton().setEnabled(false);
		path.setText(""); //$NON-NLS-1$
		path.setToolTipText(""); //$NON-NLS-1$
		imageCanvas.setImage(null, null);
	}

	/*
	 * This is a helper method that determines whether or not the given filename
	 * meets the filter specified in the filter box, in addition to the image filter.  This method parses the 
	 * filterText (to get all specified filters, which are separated by commas) 
	 * and delegates the actual task of comparing it to the given
	 * filename to a helper method -- meetsFilterHelper.
	 *
	 * @param	path	Path of the file
	 *
	 * @return	boolean	A flag indicating whether or not the given
	 *				filename meets the specified filter.
	 */
	private boolean meetsFilter(IPath path) {

		String name = path.lastSegment();
		StringTokenizer stk = new StringTokenizer(filterText, ","); //$NON-NLS-1$
		boolean result = false;
		if (stk.hasMoreTokens()) {
			while (!result && stk.hasMoreTokens()) {
				result = meetsFilterHelper(name, stk.nextToken().trim());
			}
		} else {
			result = meetsFilterHelper(name, filterText);
		}
		return result;
	}

	/*
	 * This is a helper for a helper method -- meetsFilter().  It checks to
	 * see if the given filename meets the given filter.
	 *
	 * @param 	name			A String representing a filename
	 * @param	afilterText		A String representing one of the 
	 *					specified filters
	 *
	 * @return	boolean		A flag indicating whether or not the given
	 *					filename meets the given filter.
	 */
	private boolean meetsFilterHelper(String name, String afilterText) {
		String start = ""; //$NON-NLS-1$
		String end = ""; //$NON-NLS-1$
		int index = afilterText.indexOf("*"); //$NON-NLS-1$
		if (index >= 0) {
			start = afilterText.substring(0, index);
			end = afilterText.substring(index + 1, afilterText.length());
		} else {
			start = afilterText;
		}
		return (name.toUpperCase().startsWith(start.toUpperCase()) && name.toUpperCase().endsWith(end.toUpperCase()));
	}

	//========================================================================

	/**
	 * List of current valid extensions that Java can handle.
	 */
	public static final String[] VALID_EXTENSIONS = new String[] {".gif", ".jpg", ".png", ".bmp", ".ico"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	
	/**
	 * An inner class that filters images.  Currently it allows the extensions
	 * in VALID_EXTENSIONS. Or it accepts directories.
	 * It will not allow files that can't be read.
	 * 
	 * This allows one pass through a directory for children.
	 */
	private static class ImageFileFilter implements FileFilter {

		public boolean accept(File file) {
			if (file.isDirectory())
				return file.canRead() && !file.isHidden();

			String name = file.getName().toLowerCase();
			for (int i = 0; i < VALID_EXTENSIONS.length; i++) {
				if (name.endsWith(VALID_EXTENSIONS[i]))
					return file.canRead() && !file.isHidden();
			}
			
			return false;
		}

	}

	/**
	 * An inner class that acts as a directory filter.  
	 * This will probably be changed in the future so that it allows
	 * Jar files as well.
	 */
	private static class DirectoryFilter implements FileFilter {

		public boolean accept(File f) {
			return (f.isDirectory() && f.canRead() && !f.isHidden());
		}

	}

	//=======================================================================

	/**
	 * This is the ImageLoaderThread which queries the Model (directory
	 * structure) and loads images.  Depending on the number of directories
	 * to search, and the number of images found, this process could be 
	 * time consuming.  That is why it is being launched in a separate thread. 
	 * Doing so also allows the user to cancel the search at any time should
	 * that be desirable.  This class also takes care of 
	 * enabling/disabling/updating the various View (IconDialog) components
	 * throughtout the process.  
	 */
	private class ImageLoaderThread extends Thread {

		private boolean terminateThread;
		private StatusLineManager manager;
		private IProgressMonitor monitor;

		private File f; // The File (directory) to start loading
		private IPath path; // The path for this directory
		
		private Control inLocation;	// Kludge needed to restore focus correctly.

		public ImageLoaderThread(File f, IPath path) {
			this.f = f;
			this.path = path;
		}

		/*
		 * This must be called only within UI thread. This makes
		 * sure sync. is correct in that if anything is disposed, this will be called.
		 */
		public void terminateThread() {
			terminateThread = true;
		}

		/*
		 * Kludge: ProgressMonitor can't handle not being on UI thread. Eclipse
		 * creates a ProgressMonitorPart to get around this, but the StatusLineManager
		 * doesn't use this guy.
		 */
		private void beginTask(final IProgressMonitor pm, final String task, final int ticks) {
			dialogDisplay.syncExec(new Runnable() {
				public void run() {
					if (!terminateThread)
						pm.beginTask(task, ticks);
				}
			});
		}

		/*
		 * Kludge: ProgressMonitor can't handle not being on UI thread. Eclipse
		 * creates a ProgressMonitorPart to get around this, but the StatusLineManager
		 * doesn't use this guy.
		 */
		private void worked(final IProgressMonitor pm, final int worked) {
			dialogDisplay.asyncExec(new Runnable() {
				public void run() {
					if (!terminateThread)
						pm.worked(worked);
				}
			});
		}

		/*
		 * Kludge: ProgressMonitor can't handle not being on UI thread. Eclipse
		 * creates a ProgressMonitorPart to get around this, but the StatusLineManager
		 * doesn't use this guy.
		 */
		private void done(final IProgressMonitor pm) {
			dialogDisplay.syncExec(new Runnable() {
				public void run() {
					if (!terminateThread)
						pm.done();
				}
			});
		}

		private void setEnabledForLocGroup(boolean enabled) {
			// KLUDGE WARNING:
			// There is a problem with this in that if something in the location group was in focus, and
			// we then disable the control, the focus goes somewhere else, and this
			// then means when we re-enable it, it won't have focus.
			// In this case it goes to the composite surrounding the filter
			// because that is the next focus control (the filter will already
			// have been disabled, so the composite around it will be the next
			// focus).
			// This is annoying if you are using keyboard access.
			// So if we are disabling, and the control is in focus, then we
			// need to know that.
			Control[] children = locationGroup.getChildren();			
			if (!enabled) {
				// First find which one, if any, is in focus
				inLocation = null;
				for (int i = 0; i < children.length; i++) {
					if (children[i].isFocusControl()) {
						inLocation = children[i];
						break;
					}
				}	
			}			

			// want to do all children because it looks better. Disabling
			// just the group works, but the children still look enabled, and this is confusing.
			locationGroup.setEnabled(enabled);
			for (int i = 0; i < children.length; i++) {
				children[i].setEnabled(enabled);
			}
			
			if (enabled) {
				// We are now re-enabling, but we don't want to set focus to the
				// control if something else (other than the filter composite).
				if (inLocation != null && filter.getParent().isFocusControl()) 
					inLocation.forceFocus();	// Make sure it has focus again if it had it before AND we are in filter composite as focus.	
				inLocation = null;
			}
		}

		public void run() {
			try {
				manager = dialog.getStatusLineManager();
				monitor = manager.getProgressMonitor();
				dialogDisplay.syncExec(new Runnable() {
					public void run() {
						if (terminateThread)
							return;
						manager.setCancelEnabled(true);
						monitor.setCanceled(false); // Doesn't reset when begin task called again.
						filter.setEnabled(false);
						setEnabledForLocGroup(false);
						clearDisplay();
						fileList.removeAll();
						drawingBoard.removeAll();
						drawingBoard.setIsWorkspacePaths(currentScope == SEARCH_PROJECT);
						completeFilepaths.clear();
						allImagesLoaded = false;
						ImageController.this.dialog.getStatusLineManager().setErrorMessage("");							 //$NON-NLS-1$
					}
				});
				beginTask(monitor, SWTMessages.ImageController_Status_SearchingForImages, 1000); 

				addImages(f, path, monitor, 1000);
				allImagesLoaded = !monitor.isCanceled();
			} catch (Exception e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (SWTError err) {
				JavaVEPlugin.log(err, Level.WARNING);
			} finally {
				if (terminateThread)
					return;

				final String text = (monitor.isCanceled() ? SWTMessages.ImageController_Status_SearchStopped : SWTMessages.ImageController_Status_SearchDone); 
				done(monitor);

				// Run sync so that we don't leave until all done.
				dialogDisplay.syncExec(new Runnable() {
					public void run() {
						if (terminateThread)
							return;
						setEnabledForLocGroup(true);
						filter.setEnabled(true);
						manager.setMessage(text);
						if (initialTry != null) {
							// Try to select initial value.
							IPath path = initialTry;
							initialTry = null;
							drawingBoard.setSelected(path);
						}
					}
				});				
			}
		}

		private void addImages(File file, IPath dirPath, IProgressMonitor pm, int ticksAvailable) {
			if (f == null || terminateThread || pm.isCanceled())
				return;
			File[] inArray = file.listFiles(imageFilter);

			IProgressMonitor subpm = new SubProgressMonitor(pm, ticksAvailable);
			beginTask(subpm, null, inArray.length * 100);
			try {
				if (inArray != null) {
					// inArray should only be null if file was not a directory (which should be impossible) or if an IOException occurred in the listFiles()
					// which I don't know why that would occur, but sometimes it does.
					for (int j = 0; j < inArray.length; j++) {
						if (terminateThread || subpm.isCanceled()) {
							return;
						}
						final File jfile = inArray[j];
						if (jfile.isDirectory()) {
							if (crawlEnabled) {
								IPath path = dirPath.append(jfile.getName());
								addImages(jfile, path, subpm, 100);
							} else
								worked(subpm, 100); // Count the directory as worked
						} else {
							final IPath path = dirPath.append(jfile.getName());
	
							completeFilepaths.add(path);
	
							if (meetsFilter(path)) {
								if (!terminateThread) {
									worked(subpm, 100);
	
									dialogDisplay.asyncExec(new Runnable() {
										public void run() {
											if (terminateThread)
												return; // This could already be on the queue when closed requested.
											drawingBoard.add(path);
											fileList.add(jfile.getName());
										}
									});
								}
							}
						}
					}
				}
			} finally {
				if (!terminateThread)
					done(subpm);
			}
		}

	}

}
