package org.eclipse.ve.internal.java.choosebean;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChooseBeanDialog.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-13 21:11:52 $ 
 */

import java.util.*;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.corext.util.*;
import org.eclipse.jdt.internal.ui.util.StringMatcher;
import org.eclipse.jdt.internal.ui.util.TypeInfoLabelProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.*;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.ve.internal.cdm.AnnotationEMF;
import org.eclipse.ve.internal.cdm.CDMFactory;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.IBeanNameProposalRule;

/**
 * @author sri
 *
 * All: 
 * Swing Types: Subclasses of JComponent, JFrame, JDialog, JWindow, JApplet, TableColumn
 * AWT Types: Subclasses of Component, but not subclass of Swing Types.
 * Web Proxy Types: Classes which are in packages called 'soap.proxy'.
 */
public class ChooseBeanDialog extends TwoPaneElementSelector {

	public static final String JBCF_CHOOSEBEAN_SELHIST_KEY = "JBCF_CHOOSEBEAN_SELHIST_KEY"; //$NON-NLS-1$
	public static final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
	public static final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
	public static int CHOICE_ALL = 0;
	public static int CHOICE_SWING = 1;
	public static int CHOICE_AWT = 2;
	public static int CHOICE_WEBPROXY = 3;
	public static int CHOICE_DEFAULT = CHOICE_ALL;
	public static final String EmptyString = new String() ;
	private String[] choices = {	ChooseBeanMessages.getString("SelectionAreaHelper.SelectType.ALL"),  //$NON-NLS-1$
												ChooseBeanMessages.getString("SelectionAreaHelper.SelectType.SWING"),  //$NON-NLS-1$
												ChooseBeanMessages.getString("SelectionAreaHelper.SelectType.AWT"),  //$NON-NLS-1$
												ChooseBeanMessages.getString("SelectionAreaHelper.SelectType.WebProxy")}; //$NON-NLS-1$
	
	public static String WEB_SERVICE_PROXY_PACKAGE = "proxy.soap";//$NON-NLS-1$
	
	private static TypeInfoLabelProvider classLabelProvider = new TypeInfoLabelProvider(TypeInfoLabelProvider.SHOW_TYPE_ONLY);
	private static TypeInfoLabelProvider packageLabelProvider = new TypeInfoLabelProvider(TypeInfoLabelProvider.SHOW_TYPE_CONTAINER_ONLY + TypeInfoLabelProvider.SHOW_ROOT_POSTFIX);
	private ResourceSet resourceSet;
	private IJavaProject project;
	private IPackageFragment pkg;
	private java.util.List selectionHistory;
	
	private IJavaSearchScope scope = null;
	private WebServicesHelper wsHelper = null;
	
	private Button[] typeChoices = null;
	private int selectedChoice = 0;
	private boolean disableOthers = false;
	
	private String[] awtBaseClasses = {"java.awt", "Component"}; //$NON-NLS-1$ //$NON-NLS-2$
	private String[] swingBaseClasses = {"javax.swing", "JComponent", "javax.swing", "JFrame", "javax.swing", "JDialog", "javax.swing", "JWindow", "javax.swing", "JApplet", "javax.swing.table", "TableColumn"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
	// TypeInfos for the above mentioned base classes
	private List awtTypeInfos = new ArrayList();
	private List swingTypeInfos = new ArrayList();
	// List of strings
	private List webServicesTypes = null;
	private List swingTypes = null;
	private List awtTypes = null;
	private List allTypes = new ArrayList();
	
	private Text superFilterText = null;
	private Combo filterCombo = null;
	private Label className = null;
	private String beanLabelText = null;
	private Text beanLabel = null ;

	private FilteredList.FilterMatcher currentFilterMatcher = null;
	private TypeFilterMatcher anTypeFilterMatcher = new TypeFilterMatcher();
	private ShowAllTypeChoiceFilter anShowAllTypeChoiceFilter = new ShowAllTypeChoiceFilter();
// TODO TimerStep comment	private int originalTestId;	// used for performance measurements
	
	private EditDomain feditDomain = null ;
	
	public static class WebServicesHelper{
		public static String[] SERVICE_INTERFACE = {"javax.xml.rpc", "Service"}; //$NON-NLS-1$ //$NON-NLS-2$
		public static String[] REMOTE_INTERFACE = {"java.rmi","Remote"}; //$NON-NLS-1$ //$NON-NLS-2$
		protected IJavaProject javaProject = null;
		protected IType serviceType = null;
		protected IType remoteType = null;
		protected ITypeHierarchy serviceTypeHierarchy = null;
		protected ITypeHierarchy remoteTypeHierarchy = null;
		
		public WebServicesHelper(IJavaProject javaProject){
			try {
				init(javaProject.findType(SERVICE_INTERFACE[0], SERVICE_INTERFACE[1]), 
					javaProject.findType(REMOTE_INTERFACE[0], REMOTE_INTERFACE[1]),
					javaProject);
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			}
		}
		
		protected void init(IType serviceType, IType remoteType, IJavaProject javaProject){
			this.serviceType = serviceType;
			this.remoteType = remoteType;
			this.javaProject = javaProject;
			if(javaProject!=null && serviceType!=null)
				try {
					serviceTypeHierarchy = serviceType.newTypeHierarchy(javaProject, null);
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
				}
			if(javaProject!=null && remoteType!=null)
				try {
					remoteTypeHierarchy = remoteType.newTypeHierarchy(javaProject, null);
				} catch (JavaModelException e1) {
					JavaVEPlugin.log(e1, MsgLogger.LOG_FINE);
				}
		}
		
		/**
		 * Valid services are Classes which do all of the following: 
		 *  (*) Extends/Implements the interface javax.xml.rpc.Service.
		 *  (*) Returns an Interface which has extended the interface java.rmi.Remote 
		 *  
		 * @return
		 */
		public List getValidServices(){
			List wsTypes = new ArrayList();
			if(remoteType!=null && remoteTypeHierarchy!=null && serviceType!=null && serviceTypeHierarchy!=null){
				List serviceClasses = getSubTypes(serviceTypeHierarchy, serviceType, true);
				if(serviceClasses!=null){
					for(int i=0;i<serviceClasses.size();i++){
						IType serviceClass = (IType) serviceClasses.get(i);
						if(isValidService(serviceClass)){
							if(!wsTypes.contains(serviceClass.getFullyQualifiedName()))
								wsTypes.add(serviceClass.getFullyQualifiedName());
						}
					}
				}
			}
			return wsTypes;
		}
		
		public boolean isValidProxySoapService(IType type){
			if(type.getPackageFragment().getElementName().equals(WEB_SERVICE_PROXY_PACKAGE))
				return true;
			return false;
		}
		
		public boolean isValidJAXRPCService(IType type){
			return isExtendingService(type) && getRemoteReturnedByService(type)!=null;
		}
		
		public boolean isValidService(IType type){
			return isValidProxySoapService(type) || isValidJAXRPCService(type);
		}
		
		public boolean isExtendingService(IType type){
			if(serviceTypeHierarchy!=null){
				IType[] subTypes = serviceTypeHierarchy.getAllSubtypes(serviceType);
				for(int i=0;i<subTypes.length;i++)
					if(subTypes[i].equals(type))
						return true;
			}
			return false;
		}
		
		public IType getRemoteReturnedByService(IType inputService){
			try {
				List remoteExtendingInterfaces = getSubTypes(remoteTypeHierarchy, remoteType, false);
				ITypeHierarchy typeHierarchy = inputService.newSupertypeHierarchy(null);
				IType[] allSuperClasses = typeHierarchy.getAllSuperclasses(inputService);
				IType[] superClasses = new IType[allSuperClasses.length+1];
				System.arraycopy(allSuperClasses,0,superClasses,1,allSuperClasses.length);
				superClasses[0] = inputService;
				for(int sc=0;sc<superClasses.length;sc++){
					for(int mc=0;mc<superClasses[sc].getMethods().length;mc++){
						IMethod method = superClasses[sc].getMethods()[mc];
						if(method.getParameterTypes().length>0 ||
							Signature.SIG_VOID.equals(method.getReturnType()) ||
							method.getReturnType().indexOf(Signature.C_ARRAY)>-1 ||
							!Flags.isPublic(method.getFlags()))
							continue;
						String retType = Signature.toString(Signature.getReturnType(method.getSignature()));
						String[][] resolvedTypes = superClasses[sc].resolveType(retType);
						if(resolvedTypes!=null && resolvedTypes.length>0){
							for(int rtc=0;rtc<resolvedTypes.length;rtc++){
								String pkg = resolvedTypes[rtc][0];
								String cls = resolvedTypes[rtc][1];
								for(int reic=0;reic<remoteExtendingInterfaces.size();reic++){
									IType remoteExtendingInterface = (IType) remoteExtendingInterfaces.get(reic);
									if(remoteExtendingInterface.getElementName().equals(cls) &&
										remoteExtendingInterface.getPackageFragment().getElementName().equals(pkg))
											return remoteExtendingInterface;
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			} catch (IllegalArgumentException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_FINE);
			}		
			return null;
		}
	}
	
	private static class TypeFilterMatcher implements FilteredList.FilterMatcher {

		private static final char END_SYMBOL= '<';
		private static final char ANY_STRING= '*';

		private StringMatcher fMatcher;
		private StringMatcher fQualifierMatcher;
		
		/*
		 * @see FilteredList.FilterMatcher#setFilter(String, boolean)
		 */
		public void setFilter(String pattern, boolean ignoreCase, boolean igoreWildCards) {
			int qualifierIndex= pattern.lastIndexOf("."); //$NON-NLS-1$

			// type			
			if (qualifierIndex == -1) {
				fQualifierMatcher= null;
				fMatcher= new StringMatcher(adjustPattern(pattern), ignoreCase, igoreWildCards);
				
			// qualified type
			} else {
				fQualifierMatcher= new StringMatcher(pattern.substring(0, qualifierIndex), ignoreCase, igoreWildCards);
				fMatcher= new StringMatcher(adjustPattern(pattern.substring(qualifierIndex + 1)), ignoreCase, igoreWildCards);
			}
		}

		/*
		 * @see FilteredList.FilterMatcher#match(Object)
		 */
		public boolean match(Object element) {
			if (!(element instanceof TypeInfo))
				return false;

			TypeInfo type= (TypeInfo) element;

			if (!fMatcher.match(type.getTypeName()))
				return false;

			if (fQualifierMatcher == null)
				return true;

			return fQualifierMatcher.match(type.getTypeContainerName());
		}
		
		private String adjustPattern(String pattern) {
			int length= pattern.length();
			if (length > 0) {
				switch (pattern.charAt(length - 1)) {
					case END_SYMBOL:
						pattern= pattern.substring(0, length - 1);
						break;
					case ANY_STRING:
						break;
					default:
						pattern= pattern + ANY_STRING;
				}
			}
			return pattern;
		}
	}
	
	/*
	 * A string comparator which is aware of obfuscated code
	 * (type names starting with lower case characters).
	 */
	private static class StringComparator implements Comparator {
	    public int compare(Object left, Object right) {
	     	String leftString= (String) left;
	     	String rightString= (String) right;
	     		     	
	     	if (Strings.isLowerCase(leftString.charAt(0)) &&
	     		!Strings.isLowerCase(rightString.charAt(0)))
	     		return +1;

	     	if (Strings.isLowerCase(rightString.charAt(0)) &&
	     		!Strings.isLowerCase(leftString.charAt(0)))
	     		return -1;
	     	
			int result= leftString.compareToIgnoreCase(rightString);			
			if (result == 0)
				result= leftString.compareTo(rightString);

			return result;
	    }
	}
	
	protected final class ShowAllTypeChoiceFilter implements FilteredList.FilterMatcher{

		private StringMatcher fMatcher;
		private StringMatcher fQualifierMatcher;
		
		/*
		 * @see FilteredList.FilterMatcher#setFilter(String, boolean)
		 */
		public void setFilter(String pattern, boolean ignoreCase, boolean igoreWildCards) {
			int qualifierIndex= pattern.lastIndexOf("."); //$NON-NLS-1$

			// type			
			if (qualifierIndex == -1) {
				fQualifierMatcher= null;
				fMatcher= new StringMatcher(pattern + '*', ignoreCase, igoreWildCards);
				
			// qualified type
			} else {
				fQualifierMatcher= new StringMatcher(pattern.substring(0, qualifierIndex), ignoreCase, igoreWildCards);
				fMatcher= new StringMatcher(pattern.substring(qualifierIndex + 1), ignoreCase, igoreWildCards);
			}
		}

		/*
		 * @see FilteredList.FilterMatcher#match(Object)
		 */
		public boolean match(Object element) {
			if (!(element instanceof TypeInfo))
				return false;
			TypeInfo typeInfo = (TypeInfo) element;
			if (!fMatcher.match(typeInfo.getTypeName()))
				return false;
			if (fQualifierMatcher!=null && !fQualifierMatcher.match(typeInfo.getTypeContainerName()))
				return false;
			boolean matched = false;
			switch (selectedChoice) {
				case 0 :
					matched = true;
					break;
				case 1 :
					if(swingTypes!=null){
						if(swingTypes.contains(typeInfo.getFullyQualifiedName()))
							matched = true;
					}
					break;
				case 2 :
					if(awtTypes!=null){
						if(awtTypes.contains(typeInfo.getFullyQualifiedName()) &&
						   !swingTypes.contains(typeInfo.getFullyQualifiedName()))
							matched = true;
					}
					break;
				case 3 :
					if(typeInfo.getPackageName().equals(WEB_SERVICE_PROXY_PACKAGE))
						matched = true;
					if(webServicesTypes.contains(typeInfo.getFullyQualifiedName()))
						matched = true;
				break;
				default :
					break;
			}
			return matched;
		}
	}
	
	public ChooseBeanDialog(Shell shell, IPackageFragment packageFragment, int choice, boolean disableOthers){
		super(shell, classLabelProvider, packageLabelProvider);
		// Use TimerStep APIs for performance measurements. Save the original test id to restore it later.
// TODO Remove all timerstep comments		originalTestId = TimerStep.instance().getTestd();
//		TimerStep.instance().setTestd(137);
//		TimerStep.instance().writeCounters2(100);
		this.pkg = packageFragment;
		this.project = packageFragment.getJavaProject();
		this.selectedChoice = choice;
		this.selectionHistory = new ArrayList();
		this.disableOthers = disableOthers;
		setTitle(ChooseBeanMessages.getString("MainDialog.title")); //$NON-NLS-1$
		setMessage(ChooseBeanMessages.getString("MainDialog.message")); //$NON-NLS-1$
		setStatusLineAboveButtons(true);
		loadSelectionHistory();
		wsHelper = new WebServicesHelper(this.project);
	}
	
	private void loadSelectionHistory(){
		try {
			String selhists = project.getProject().getPersistentProperty(getQualifiedName());
			if(selhists!=null){
				StringTokenizer spacer = new StringTokenizer(selhists, " ", false); //$NON-NLS-1$
				while(spacer.hasMoreTokens()){
					this.selectionHistory.add(spacer.nextToken());
				}
			}
		} catch (CoreException e) {
		}
	}
	
	private void storeSelectionHistory(){
		try {
			StringBuffer buff = new StringBuffer();
			for(int i=0;i<selectionHistory.size() && i<10;i++){
				buff.append(selectionHistory.get(i)+" "); //$NON-NLS-1$
			}
			project.getProject().setPersistentProperty(getQualifiedName(), buff.toString());
		} catch (CoreException e) {
		}
	}
	
	private QualifiedName getQualifiedName(){
		return new QualifiedName(JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), JBCF_CHOOSEBEAN_SELHIST_KEY);
	}
	
	public ChooseBeanDialog(Shell shell, IFile file, ResourceSet resourceSet, int choice, boolean disableOthers){
		this(shell, (IPackageFragment) JavaCore.create(file).getParent(), choice, disableOthers);
		this.resourceSet = resourceSet;
	}
	
	public ChooseBeanDialog(Shell shell, EditDomain ed, int choice, boolean disableOthers){
			this(shell, ((FileEditorInput)ed.getEditorPart().getEditorInput()).getFile(),
		                 JavaEditDomainHelper.getResourceSet(ed), 
		                 choice, disableOthers);
		    feditDomain = ed;
	}
	
	/*
	 * @see AbstractElementListSelectionDialog#createFilteredList(Composite)
	 */
 	protected FilteredList createFilteredList(Composite parent) {
 		final FilteredList list= super.createFilteredList(parent);
 		currentFilterMatcher = anTypeFilterMatcher;
		list.setFilterMatcher(currentFilterMatcher);
		list.setComparator(new StringComparator());
		return list;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(Composite)
	 */
	public Control createDialogArea(Composite parent) {
		Composite topComponent = new Composite(parent, SWT.NONE);
		GridLayout topComponentLayout = new GridLayout();
		topComponent.setLayout(topComponentLayout);
		
		Group classGroup = new Group(topComponent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		classGroup.setLayoutData(gd);
		classGroup.setText(ChooseBeanMessages.getString("SelectionAreaHelper.GroupTitle")); //$NON-NLS-1$
		classGroup.setLayout(new GridLayout());
		Composite c = (Composite)super.createDialogArea(classGroup);
		if (c.getLayout() instanceof GridLayout) {
			GridLayout gl = (GridLayout) c.getLayout();
			gl.marginHeight=0;
			gl.marginWidth=0;
		}
		
		createClassArea(classGroup);
		
		createBeanLabelArea(topComponent);
		
		className = new Label(topComponent, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		className.setLayoutData(gd);
				
		
//		Deemed unnecessary for now.		
//		Group fileGroup = new Group(topComponent, SWT.NONE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		fileGroup.setLayoutData(gd);
//		fileGroup.setText(ChooseBeanMessages.getString("SerializedFileHelper.GroupTitle")); //$NON-NLS-1$
//		fileGroup.setLayout(new GridLayout(2, false));
//		createFileArea(fileGroup);

//		TimerStep.instance().writeCounters2(101);	// take another snapshot after filling in the areas
//		TimerStep.instance().setTestd(originalTestId);	// restore to the original test id
		return topComponent;
	}
	
	private void createBeanLabelArea(Composite parent){
		int numCols = 2;
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(numCols,false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText(ChooseBeanMessages.getString("ChooseBeanDialog.Group.Properties.Title")); //$NON-NLS-1$
		
		Label label = new Label(group, SWT.NONE);
		GridData gd = new GridData();
		gd.horizontalSpan=1;
		label.setLayoutData(gd);
		label.setText(ChooseBeanMessages.getString("ChooseBeanDialog.Group.Properties.VariableName.text")); //$NON-NLS-1$
		
		beanLabel = new Text(group, SWT.SINGLE|SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=numCols-1;
		beanLabel.setLayoutData(gd);
		beanLabel.addModifyListener(new ModifyListener() {
			/**
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				if (e.widget instanceof Text) {
					Text text = (Text) e.widget;
					beanLabelText = text.getText();
					updateOkState();
				}
			}
		});
		if(resourceSet==null){
			group.setEnabled(false);
			label.setEnabled(false);
			beanLabel.setEnabled(false);
		}
	}

	protected void createClassArea(Composite parent){
		int numCols = 4;
		Composite c = new Composite(parent, SWT.NONE);
		GridData gd= new GridData();
		gd.grabExcessHorizontalSpace= true;
		gd.horizontalAlignment= GridData.FILL;
		c.setLayoutData(gd);
		GridLayout cLayout = new GridLayout(numCols, true);
		// got from super.super.createDialoagArea()
		cLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		cLayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		c.setLayout(cLayout);
		
		typeChoices = new Button[4];
		for(int i=0;i<typeChoices.length;i++){
			typeChoices[i] = new Button(c, SWT.RADIO);
			gd = new GridData();
			gd.horizontalSpan=numCols/4;
			typeChoices[i].setLayoutData(gd);
			typeChoices[i].setText(choices[i]);
			typeChoices[i].addSelectionListener(new SelectionListener() {
				/**
				 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
				 */
				public void widgetSelected(SelectionEvent e) {
					if(e.widget instanceof Button){
						Button choice = (Button) e.widget;
						if(choice.getSelection()){
							if(typeChoices!=null)
								for(int i=0;i<typeChoices.length;i++)
									if(choice.equals(typeChoices[i]))
										selectedChoice = i;
							updateElements();
						}else{
							selectedChoice = -1;
						}
					}
				}

				/**
				 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
				 */
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
			if(disableOthers && selectedChoice!=i){
				typeChoices[i].setEnabled(false);
			}
		}

		typeChoices[selectedChoice].setSelection(true);
		updateElements();
	}
	
	protected void createFileArea(Composite parent){
//		Text text = new Text(parent, SWT.BORDER|SWT.SINGLE);
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		text.setLayoutData(gd);
//		
//		Button b = new Button(parent, SWT.PUSH);
//		b.setText(ChooseBeanMessages.getString("SerializedFileHelper.Button.SelectFile"));  //$NON-NLS-1$
//		b.setEnabled(false);
//		gd = new GridData();
//		b.setLayoutData(gd);
	}
	
	private void updateElements(){

		if(typeChoices[0].getSelection()){
			if(!(currentFilterMatcher instanceof TypeFilterMatcher)){
				currentFilterMatcher = anTypeFilterMatcher;
				fFilteredList.setFilterMatcher(currentFilterMatcher);
			}
		}
		
		if(typeChoices[1].getSelection()){
			if(!(currentFilterMatcher instanceof ShowAllTypeChoiceFilter)){
				currentFilterMatcher = anShowAllTypeChoiceFilter;
				fFilteredList.setFilterMatcher(currentFilterMatcher);
			}
			if(swingTypes==null)
				BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
					public void run() {
						swingTypes= getSubTypes(swingTypeInfos);
					}
				});
		}

		if(typeChoices[2].getSelection()){
			if(!(currentFilterMatcher instanceof ShowAllTypeChoiceFilter)){
				currentFilterMatcher = anShowAllTypeChoiceFilter;
				fFilteredList.setFilterMatcher(currentFilterMatcher);
			}
			if(awtTypes==null)
				BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
					public void run() {
						awtTypes= getSubTypes(awtTypeInfos);
						swingTypes= getSubTypes(swingTypeInfos);
					}
				});
		}
		
		if(typeChoices[3].getSelection()){
			if(!(currentFilterMatcher instanceof ShowAllTypeChoiceFilter)){
				currentFilterMatcher = anShowAllTypeChoiceFilter;
				fFilteredList.setFilterMatcher(currentFilterMatcher);
			}
			if(webServicesTypes==null)
				BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
					public void run() {
						webServicesTypes = getWebServicesTypes();
					}
				});
		}

		// Force the filtered list to reload
		setFilter(getFilter());
	}

	protected List getAllElements(){
		final TypeInfoFactory typeInfoFactory = new TypeInfoFactory();
		TypeInfoRequestor requestor = new TypeInfoRequestor(allTypes){
			private void check(char[] packageName, char[] typeName, char[][] enclosingTypeNames, String path, boolean isInterface){
				for(int i=0;i<swingBaseClasses.length;i+=2)
					if(new String(packageName).equals(swingBaseClasses[i]) && new String(typeName).equals(swingBaseClasses[i+1]))
						swingTypeInfos.add(typeInfoFactory.create(packageName, typeName, enclosingTypeNames, isInterface, path));
				for(int i=0;i<awtBaseClasses.length;i+=2)
					if(new String(packageName).equals(awtBaseClasses[i]) && new String(typeName).equals(awtBaseClasses[i+1]))
						awtTypeInfos.add(typeInfoFactory.create(packageName, typeName, enclosingTypeNames, isInterface, path));
			}
			public void acceptClass(char[] packageName, char[] typeName, char[][] enclosingTypeNames, String path) {
				super.acceptClass(packageName, typeName, enclosingTypeNames, path);
				check(packageName, typeName, enclosingTypeNames, path, false);
			}
		};
		try {
			new SearchEngine().searchAllTypeNames(ResourcesPlugin.getWorkspace(),
				null,
				null,
				IJavaSearchConstants.PATTERN_MATCH,
				IJavaSearchConstants.CASE_INSENSITIVE,
				IJavaSearchConstants.CLASS,
				getJavaSearchScope(),
				requestor,
				IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				null);
		} catch (JavaModelException e) {
		}
		return allTypes;
	}

	private List getSubTypes(List tis){
		List list = new ArrayList();
		for(int i=0;i<tis.size();i++){
			try {
				TypeInfo ti = (TypeInfo) tis.get(i);
				IType type = ti.resolveType(getJavaSearchScope());
				ITypeHierarchy th = type.newTypeHierarchy(null);
				IType[] types = th.getAllSubtypes(type);
				list.add(type.getFullyQualifiedName());
				for(int j=0;j<types.length;j++)
					list.add(types[j].getFullyQualifiedName());
			} catch (JavaModelException e) {
			}
		}
		return list;
	}
	
	protected static List getSubTypes(ITypeHierarchy th, IType fromType, boolean isClass){
		IType[] types = th.getAllSubtypes(fromType);
		List list = new ArrayList();
		for(int i=0;i<types.length;i++){
			try {
				if(isClass && types[i].isClass())
					list.add(types[i]);
				if(!isClass && types[i].isInterface())
					list.add(types[i]);
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, MsgLogger.LOG_INFO);
			}
		}
		return list;
	}
	
	private List getWebServicesTypes(){
		if(wsHelper!=null)
			return wsHelper.getValidServices();
		return new ArrayList();
	}

	protected IJavaSearchScope getJavaSearchScope(){
		if(scope==null)
			scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { ((IJavaProject)project) });
		return scope;
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open() {
		setElements(getAllElements().toArray());
		return super.open();
	}

	/**
	 * Returns in ChooseBeanSelector format if ResourceSet speciofied.
	 * Returns the TypeInfos if ResourceSet is null.
	 * @see org.eclipse.ui.dialogs.SelectionDialog#getResult()
	 */
	public Object[] getResult(){
		Object[] results = super.getResult();
		if(resourceSet!=null){
			Object[] newResults = new Object[results.length*2];
			for(int i=0;i<results.length;i++){
				TypeInfo ti = (TypeInfo) results[i];
				if(selectionHistory!=null){
					if(selectionHistory.contains(classLabelProvider.getText(ti)))
						selectionHistory.remove(classLabelProvider.getText(ti));
					selectionHistory.add(0, classLabelProvider.getText(ti));
					storeSelectionHistory();
				}
				String realFQN = getFullSelectionName(ti);
				EClass eclass = (EClass) Utilities.getJavaClass(realFQN, resourceSet);
				EObject eObject = eclass.getEPackage().getEFactoryInstance().create(eclass);
				setBeanName(eObject, beanLabelText);
				newResults[(i*2)] = eObject;
				newResults[(i*2)+1] = eclass;
			}
			return newResults;
		}else{
			return results;
		}
	}
	
	protected void setBeanName(EObject obj, String name){
		if(obj!=null) {
			CDMFactory fact = CDMFactory.eINSTANCE;
			AnnotationEMF an = fact.createAnnotationEMF();
			if(an!=null){
				EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
				sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
				sentry.setValue(name);
				CDEUtilities.putEMapEntry(an.getKeyedValues(), sentry);
				an.setAnnotates(obj);
			}
		}
	}
	
	/**
	 * 
	 * This is a temporary solution, we need to have a name proposal provider - not
	 * hard coded access to CodeGen.
	 */
	protected String getFieldProposal (String className) {
		
		if (feditDomain != null && resourceSet != null) {
			EObject o = feditDomain.getDiagramData();
			IType javaElement = null;
			// CodeGen will have an adapter on the diagram to resolve the IType
			for (Iterator iter = o.eAdapters().iterator(); iter.hasNext();) {
				Object a = iter.next();
				if (a instanceof IAdaptable) {
					Object je = ((IAdaptable) a).getAdapter(IJavaElement.class);
					// The rule needs the IType for a JavaBased editor to ensure
					// no instance variable duplication
					if (je instanceof IType) {
						javaElement = (IType) je;
						break;
					}
				}
			}
			if (javaElement != null) {
				IBeanNameProposalRule pp = (IBeanNameProposalRule) feditDomain.getRuleRegistry().getRule(IBeanNameProposalRule.RULE_ID); 
				String result = pp.getProspectInstanceVariableName(className, new Object[] { javaElement }, resourceSet);
				return result == null ? EmptyString : result;
			}
		}
		return EmptyString;
	}
	
	protected void setClassName (String name, boolean validClass) {
		if (validClass) {
			className.setForeground(green) ;
		}
		else {
			className.setForeground(red) ;
		}
		
		if (name == null || name.length()==0) {
			String t = name==null? EmptyString : name ;		
		    className.setText(t) ;
		    beanLabel.setText(t) ;		    
		}
		else {
			if (validClass)
			   beanLabel.setText(getFieldProposal (name)) ;
			else
			   beanLabel.setText(EmptyString) ;
			className.setText(name) ;
		}
		  
		
	}
	
	protected IStatus getClassStatus(Object selected){
		Throwable t = null;
		String message = new String(); 
		boolean isInstantiable = true;
		if(selected==null){
			isInstantiable=false;
			setClassName(null,false);
			message = ChooseBeanMessages.getString("SelectionAreaHelper.SecondaryMsg.NoSelectionMade"); //$NON-NLS-1$
		}else{
			try{
				TypeInfo ti = (TypeInfo) selected;
				IType type = ti.resolveType(getJavaSearchScope());
				
				boolean isTypePublic = Flags.isPublic(type.getFlags());
				boolean isTypeAbstract = Flags.isAbstract(type.getFlags());
				boolean isInPresentPackage = type.getPackageFragment().getElementName().equals(pkg.getElementName());
				boolean isTypeStatic = Flags.isStatic(type.getFlags());
				boolean isTypeInner = type.getDeclaringType()!=null;
				boolean isPublicNullConstructorPresent = false;
				boolean isAnyConstructorPresent = false;
				
				IMethod[] methods = type.getMethods();
				for(int m=0;m<methods.length;m++){
					if(methods[m].isConstructor() &&
					   methods[m].getParameterTypes().length<1 &&
					   Flags.isPublic(methods[m].getFlags())){
					   	isPublicNullConstructorPresent = true;
					   	//break;
					}
					if(methods[m].isConstructor())
						isAnyConstructorPresent=true;
				}
				if(!isPublicNullConstructorPresent && isAnyConstructorPresent){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.getString("SelectionAreaHelper.SecondaryMsg.NoPublicNullConstructor")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(!isTypePublic){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.getString("SelectionAreaHelper.SecondaryMsg.TypeNonPublic")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(isTypeAbstract){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.getString("ChooseBeanDialog.Message.AbstractType"));  //$NON-NLS-1$
				}
				if(isTypeInner && (!isTypeStatic)){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.getString("ChooseBeanDialog.Message.NonStaticType"));  //$NON-NLS-1$
				}
				// Instantiable if all are true:
				//		+ No Constructor is present OR public null constructor present
				//		+ Type is public OR type is in present package
				//		+ Type is not abstract
				//		+ Enclosing type is not IType OR Enclosing type is IType and this type is static
				isInstantiable = ((!isAnyConstructorPresent) || isPublicNullConstructorPresent) && (isTypePublic || isInPresentPackage) && (!isTypeAbstract) && ((!isTypeInner) || (isTypeInner && isTypeStatic));
				if(isInstantiable)
					message = new String();
				setClassName(getFullSelectionName(ti), isInstantiable) ;
			}catch(JavaModelException e){
				t = e;
				isInstantiable = false;
				if(message.length()>0)
					message = message.concat(" : "); //$NON-NLS-1$
				message = ChooseBeanMessages.getString("SelectionAreaHelper.SecondaryMsg.UnknownError"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		Status status = new Status(
							isInstantiable?IStatus.OK:IStatus.ERROR, 
							JavaVEPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 
							IStatus.OK, 
							message, 
							t);
		return status;
	}

	protected String getFullSelectionName(TypeInfo ti){
		String falseFQN = ti.getFullyQualifiedName();
		String correctResolve = null;
//		if(file!=null && file instanceof org.eclipse.jdt.internal.core.CompilationUnit){
//			String[][] ret = null;
//			try{
//				IType[] types =((org.eclipse.jdt.internal.core.CompilationUnit)file).getTypes();
//				if(types!=null && types.length>0)
//					ret = types[0].resolveType(falseFQN);
//			}catch(JavaModelException e){}
//			if(ret!=null && ret.length>0){
//				correctResolve = ret[0][0]+"."+ret[0][1].replace('.','$'); //$NON-NLS-1$
//			}
//		}
//		if(correctResolve==null){
			IType type = null;
			try{
				type = project.findType(falseFQN);
			}catch(JavaModelException e){
				type = null;
			}
			if(type!=null)
				correctResolve = type.getFullyQualifiedName('$');//getFQNforType(type,0);
//		}
//		if(correctResolve==null){
//			String packageName = ti.getPackageName();
//			String className = new TypeInfoLabelProvider(TypeInfoLabelProvider.SHOW_TYPE_ONLY).getText(ti);
//			boolean isContainerPackage = ti.getPackageName().equals(ti.getTypeContainerName());
//			String fullName = null;
//			if(isContainerPackage){
//				if(packageName.length()==0) 
//					fullName = className;
//				else
//					fullName = packageName + "." + className; //$NON-NLS-1$
//			}else{
//				packageName = ti.getTypeContainerName();
//				fullName = packageName + "$" + className; //$NON-NLS-1$
//			}
//			correctResolve = fullName;
//		}
		if(correctResolve==null)
			correctResolve = falseFQN;
		return correctResolve;
	}

//	/*
//	 * elm - for level of 0, the input SHOULD be an IType
//	 * got from CodegenTypeResolver.getFQNforType()
//	 */
//	public static String getFQNforType(IJavaElement elm, int level){
//		if(elm instanceof IClassFile){
//			IClassFile cf = (IClassFile) elm;
//			String className = elm.getElementName();
//			className = className.substring(0,className.lastIndexOf(".class")); //$NON-NLS-1$
//			return getFQNforType(elm.getParent(),level+1) + className;
//		}else if(elm instanceof ICompilationUnit){
//			return getFQNforType(elm.getParent(),level+1);
//		}else if(elm instanceof IPackageFragment){
//			return elm.getElementName()+"."; //$NON-NLS-1$
//		}else if(elm instanceof IPackageFragmentRoot){
//			return ""; //$NON-NLS-1$
//		}else if(elm instanceof BinaryType){
//			return getFQNforType(elm.getParent(), level+1);
//		}else if(elm instanceof SourceType){
//			if(level==0)
//				return getFQNforType(elm.getParent(), level+1) + elm.getElementName();
//			else
//				return getFQNforType(elm.getParent(), level+1) + elm.getElementName()+"$"; //$NON-NLS-1$
//		}else if(elm instanceof IJavaProject){
//			return ""; //$NON-NLS-1$
//		}
//		return ""; //$NON-NLS-1$
//	}

	
	/**
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#createFilterText(Composite)
	 */
	protected Text createFilterText(Composite parent) {

		Composite c = new Composite(parent, SWT.NONE);
		GridData data = new GridData();
		data.heightHint=0;
		data.widthHint=0;
		c.setLayoutData(data);
		superFilterText = super.createFilterText(c);

		filterCombo = new Combo(parent, SWT.DROP_DOWN);

		data= new GridData();
		data.grabExcessVerticalSpace= false;
		data.grabExcessHorizontalSpace= true;
		data.horizontalAlignment= GridData.FILL;
		data.verticalAlignment= GridData.BEGINNING;
		filterCombo.setLayoutData(data);

		filterCombo.setText(getFilter()==null?"":getFilter()); //$NON-NLS-1$

		for(int i=0;i<selectionHistory.size();i++)
			if(selectionHistory.get(i)!=null)
				filterCombo.add((String)selectionHistory.get(i));

		// [259715] Need to do the listeners last because Linux filterCombo.add() causes modify listener
		// to fire, which does a setText on superFilterText which causes another event to fire,
		// which somewhere above causes the filteredList to be accessed, which has not yet been created.
		
		filterCombo.addModifyListener(new ModifyListener() {
			/**
			 * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				superFilterText.setText(filterCombo.getText());
			}
		});
		
		filterCombo.addSelectionListener(new SelectionListener() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				handleDefaultSelected();
			}
		});
		return superFilterText;
	}

	/**
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#setFilter(String)
	 */
	public void setFilter(String filter) {
		super.setFilter(filter);
		filterCombo.setText(filter);
	}

	/**
	 * @see org.eclipse.jface.window.Window#create()
	 */
	public void create() {
		super.create();
		if(filterCombo!=null)
			filterCombo.setFocus();
	}

	/**
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#handleEmptyList()
	 */
	protected void handleEmptyList() {
		super.handleEmptyList();
		filterCombo.setEnabled(false);
	}

	/**
	 * @see org.eclipse.ui.dialogs.TwoPaneElementSelector#createLowerList(Composite)
	 */
	protected Table createLowerList(Composite parent) {
		Table table = super.createLowerList(parent);
		table.addSelectionListener(new SelectionListener() {
			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				updateStatus(getClassStatus(getLowerSelectedElement()));
			}

			/**
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				updateStatus(getClassStatus(getLowerSelectedElement()));
			}
		});
		return table;
	}

	/**
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#handleSelectionChanged()
	 */
	protected void handleSelectionChanged() {
		super.handleSelectionChanged();
		updateStatus(getClassStatus(getLowerSelectedElement()));
	}
	
	protected void updateOkState() {
		// Ensure a valid instanceVariable name first
		if (beanLabelText!=null && beanLabelText.length()!=0) {
			if (!JavaConventions.validateFieldName(beanLabelText).isOK()) {
				Button okButton = getOkButton();
			    if (okButton != null)
					okButton.setEnabled(false);
			    return ;
			}			
		}
		super.updateOkState() ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#handleDefaultSelected()
	 */
	protected void handleDefaultSelected() {
		// Need to override because the default function doesn't see that the 
		// ok button may already be disabled. It assumes that the validateCurrentSelection() method 
		// does everything to make sure that everything is ok.
		if (getOkButton().isEnabled())
			super.handleDefaultSelected();
	}

}
