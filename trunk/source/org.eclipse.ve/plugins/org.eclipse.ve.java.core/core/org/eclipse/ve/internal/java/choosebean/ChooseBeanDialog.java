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
 *  $Revision: 1.19 $  $Date: 2004-06-11 19:23:24 $ 
 */

import java.util.*;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.IBeanNameProposalRule;

/**
 * @author sri
 *
 * All: 
 * Swing Types: Subclasses of JComponent, JFrame, JDialog, JWindow, JApplet, TableColumn
 * AWT Types: Subclasses of Component, but not subclass of Swing Types.
 */
public class ChooseBeanDialog extends TypeSelectionDialog {

	public static final String JBCF_CHOOSEBEAN_SELHIST_KEY = "JBCF_CHOOSEBEAN_SELHIST_KEY"; //$NON-NLS-1$
	public static final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
	public static final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
	public static final String EmptyString = new String() ;
	
	private ResourceSet resourceSet;
	private IJavaProject project;
	private IPackageFragment pkg;
	private java.util.List selectionHistory;
	
	private IJavaSearchScope scope = null;
	private IChooseBeanContributor[] contributors = null;
	private int selectedContributor = -1;
	private Button[] typeChoices = null;
	private boolean disableOthers = false;
	
	private Text superFilterText = null;
	private Combo filterCombo = null;
	private Label className = null;
	private String beanLabelText = null;
	private Text beanLabel = null ;
	
	private EditDomain feditDomain = null ;
	
	/**
	 * ChooseBeanDialog. 
	 * @param shell parent shell
	 * @param packageFragment packageFragment to determine where the new bean will be (and to determine which classes are valid for this fragment).
	 * @param contributors list of contributors, <code>null</code> will determine list that is visible to project thru the extension point. If no contributors are desired, use <code>ChooseBeanDialog.NO_CONTRIBS</code> 
	 * @param choice index into <code>contributors</code> of preselected contributor, use <code>-1</code> for no preselection.
	 * @param disableOthers if there is a  <code>choice</code> then <code>true</code> means disable all of the other contributors.
	 * 
	 * @since 1.0.0
	 */
	public ChooseBeanDialog(Shell shell, IPackageFragment packageFragment, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		super(
			shell, 
			PlatformUI.getWorkbench().getProgressService(), 
			IJavaSearchConstants.CLASS, 
			SearchEngine.createJavaSearchScope(new IJavaElement[]{packageFragment.getJavaProject()}));
		
		this.selectedContributor = choice;
		this.pkg = packageFragment;
		this.project = packageFragment.getJavaProject();
		this.selectionHistory = new ArrayList();
		this.disableOthers = disableOthers;
		this.contributors = contributors != null ? contributors : determineContributors(project);
		setTitle(ChooseBeanMessages.getString("MainDialog.title")); //$NON-NLS-1$
		setMessage(ChooseBeanMessages.getString("MainDialog.message")); //$NON-NLS-1$
		setStatusLineAboveButtons(true);
		setMatchEmptyString(false);
		if(!anyContributors())
			selectedContributor = -1;
		else if(!isValidContributor())
			selectedContributor = 0;
		loadSelectionHistory();
	}
	 	
	public static final String PI_CONTAINER = "container"; //$NON-NLS-1$	
	public static final String PI_PLUGIN = "plugin"; //$NON-NLS-1$
	
	private IChooseBeanContributor[] determineContributors(IJavaProject project){
		Map containerIDs = new HashMap();
		Map pluginsIDs = new HashMap();
		try {
			ProxyPlugin.getPlugin().getIDsFound(project, containerIDs, new HashMap(), pluginsIDs, new HashMap());
			List contributorList = new ArrayList();
			IExtensionPoint exp = Platform.getExtensionRegistry().getExtensionPoint(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(),
					"choosebean"); //$NON-NLS-1$
			IExtension[] extensions = exp.getExtensions();

			if (extensions.length > 0) {

				// Ensure that the org.eclipse.ve.java plugins are the first in the list
				IExtension[] orderedExtensions = new IExtension[extensions.length];
				int index = 0;
				String veBaseBundleName = JavaVEPlugin.getPlugin().getBundle().getSymbolicName();
				for (int i = 0; i < extensions.length; i++) {
					if (extensions[i].getNamespace().equals(veBaseBundleName)) {
						orderedExtensions[index++] = extensions[i];
					}
				}
				// Any remaining extensions go to the end
				for (int i = 0; i < extensions.length; i++) {
					if (!extensions[i].getNamespace().equals(veBaseBundleName)) {
						orderedExtensions[index++] = extensions[i];
					}
				}

				for (int ec = 0; ec < orderedExtensions.length; ec++) {
					IConfigurationElement[] configElms = orderedExtensions[ec].getConfigurationElements();
					for (int cc = 0; cc < configElms.length; cc++) {
						IConfigurationElement celm = configElms[cc];
						if (containerIDs.get(celm.getAttributeAsIs(PI_CONTAINER)) == Boolean.TRUE
								|| pluginsIDs.get(celm.getAttributeAsIs(PI_PLUGIN)) == Boolean.TRUE) {
							try {
								IChooseBeanContributor contributor = (IChooseBeanContributor) celm.createExecutableExtension("class"); //$NON-NLS-1$
								contributorList.add(contributor);
							} catch (CoreException e) {
								JavaVEPlugin.log(e, Level.FINE);
							} catch (ClassCastException e) {
								JavaVEPlugin.log(e, Level.FINE);
							}
						}
					}
				}
			}
			return !contributorList.isEmpty() ? (IChooseBeanContributor[]) contributorList.toArray(new IChooseBeanContributor[contributorList.size()]) : NO_CONTRIBS;
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
			return NO_CONTRIBS;
		}
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
		return new QualifiedName(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), JBCF_CHOOSEBEAN_SELHIST_KEY);
	}

	public static final IChooseBeanContributor[] NO_CONTRIBS = new IChooseBeanContributor[0];
	/**
	 * ChooseBeanDialog
	 * @param shell parent shell
	 * @param file file to determine what is visible
	 * @param resourceSet resourceset where choosen beans will be added
	 * @param contributors list of contributors, <code>null</code> will determine list that is visible to project thru the extension point. If no contributors are desired, use <code>ChooseBeanDialog.NO_CONTRIBS</code> 
	 * @param choice index into <code>contributors</code> of preselected contributor, use <code>-1</code> for no preselection.
	 * @param disableOthers if there is a  <code>choice</code> then <code>true</code> means disable all of the other contributors.
	 * 
	 * @since 1.0.0
	 */
	public ChooseBeanDialog(Shell shell, IFile file, ResourceSet resourceSet, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
		this(shell, (IPackageFragment) JavaCore.create(file).getParent(), contributors, choice, disableOthers);
		this.resourceSet = resourceSet;
	}
	
	/**
	 * ChooseBeanDialog.
	 * @param shell parent shell
	 * @param ed editDomain to use
	 * @param contributors list of contributors, <code>null</code> will determine list that is visible to project thru the extension point. If no contributors are desired, use <code>ChooseBeanDialog.NO_CONTRIBS</code> 
	 * @param choice index into <code>contributors</code> of preselected contributor, use <code>-1</code> for no preselection.
	 * @param disableOthers if there is a  <code>choice</code> then <code>true</code> means disable all of the other contributors.
	 * 
	 * @since 1.0.0
	 */
	public ChooseBeanDialog(Shell shell, EditDomain ed, IChooseBeanContributor[] contributors, int choice, boolean disableOthers){
			this(shell, ((FileEditorInput)ed.getEditorPart().getEditorInput()).getFile(),
		                 JavaEditDomainHelper.getResourceSet(ed), 
		                 contributors, choice, disableOthers);
		    feditDomain = ed;
	}
	
	protected boolean anyContributors(){
		return contributors.length > 0;
	}
	
	protected boolean isValidContributor(){
		return selectedContributor > -1 && selectedContributor < contributors.length ;
	}
	
	/*
	 * @see AbstractElementListSelectionDialog#createFilteredList(Composite)
	 */
 	protected FilteredList createFilteredList(Composite parent) {
 		FilteredList list= super.createFilteredList(parent);
 		if(isValidContributor())
 			list.setFilterMatcher(contributors[selectedContributor].getFilter(project));
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
		if(isValidContributor()){
			int numEntries = contributors.length;
			Composite c = new Composite(parent, SWT.NONE);
			GridData gd= new GridData();
			gd.grabExcessHorizontalSpace= true;
			gd.horizontalAlignment= GridData.FILL;
			c.setLayoutData(gd);
			GridLayout cLayout = new GridLayout(Math.min(numEntries,4), true);
			// got from super.super.createDialoagArea()
			cLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			cLayout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
			c.setLayout(cLayout);
			
			typeChoices = new Button[numEntries];
			for(int i=0;i<typeChoices.length;i++){
				typeChoices[i] = new Button(c, SWT.RADIO);
				gd = new GridData();
				typeChoices[i].setLayoutData(gd);
				typeChoices[i].setText(contributors[i].getName());
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
											selectedContributor = i;
								updateElements();
							}else{
								selectedContributor = -1;
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
				if(disableOthers && selectedContributor!=i){
					typeChoices[i].setEnabled(false);
				}
			}
	
			typeChoices[selectedContributor].setSelection(true);
		}
	}

	private void updateElements(){
		if(isValidContributor())
			fFilteredList.setFilterMatcher(contributors[selectedContributor].getFilter(project));
		// Force the filtered list to reload
		setFilter(getFilter());
	}


	protected IJavaSearchScope getJavaSearchScope(){
		if(scope==null)
			scope = SearchEngine.createJavaSearchScope(new IJavaElement[] { ((IJavaProject)project) });
		return scope;
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
				if (results[i] instanceof IType) {
					IType type = (IType) results[i];
					if(selectionHistory!=null){
						String typeName = type.getElementName();
						if(selectionHistory.contains(typeName))
							selectionHistory.remove(typeName);
						selectionHistory.add(0, typeName);
						storeSelectionHistory();
					}
					String realFQN = type.getFullyQualifiedName('$');
					// If there is a prototype factory use this to create the default instance
					PrototypeFactory prototypeFactory = null;
					EClass eClass = (EClass) Utilities.getJavaClass(realFQN, resourceSet);					
					try {
						
						ClassDescriptorDecorator decorator =
							(ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
									eClass,
									ClassDescriptorDecorator.class,
									PrototypeFactory.PROTOTYPE_FACTORY_KEY);
						if(decorator != null){
							String prototypeFactoryName = (String)decorator.getKeyedValues().get(PrototypeFactory.PROTOTYPE_FACTORY_KEY);
							prototypeFactory = (PrototypeFactory) CDEPlugin.createInstance(null,prototypeFactoryName);
						}
					} catch (Exception e) {
						JavaVEPlugin.getPlugin().getLogger().log(Level.WARNING,e);
					}
					EObject eObject = null;
					if(prototypeFactory == null){
						eObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
					} else {
						eObject = prototypeFactory.createPrototype(eClass);
					}
					setBeanName(eObject, beanLabelText);
					newResults[(i*2)] = eObject;
					newResults[(i*2)+1] = eClass;
				}
			}
			return newResults;
		}else{
			return results;
		}
	}
	
	protected void setBeanName(EObject obj, String name){
		if(obj!=null) {
			BeanUtilities.setBeanName((IJavaInstance)obj,name);
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
	
	/**
	 * A helper method to find a decorator of a specific instance.
	 */
	protected EAnnotation findDecorator(EList decorators, Class decoratorType) {
		if (decorators == null)
			return null;
		for (int i = 0; i < decorators.size(); i++) {
			EAnnotation o = (EAnnotation) decorators.get(i);
			if (decoratorType.isInstance(o))
				return o;
		}
		return null;
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
				
				boolean isDefaultConstructorSearchRequired = true;
				// The base set of rules is that classes can only be instantiated if they have default constructors
				// Some classes however (such as SWT controls) don't conform to this, however can still be created
				// The base ClassDescriptorDecorator has a key of "org.eclipse.ve.internal.PrototypeFactory" that returns a class name
				// implementing "org.eclipse.ve.internal.PrototypeFactory" that can create the EMF model for this
				// If such a decorator exists then the selection is assumed to be valid
				EClass selectedEMFClass = (EClass) Utilities.getJavaClass(ti.getFullyQualifiedName(), resourceSet);
				ClassDescriptorDecorator decorator =
					(ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
						selectedEMFClass,
						ClassDescriptorDecorator.class,
						PrototypeFactory.PROTOTYPE_FACTORY_KEY);
				if(decorator != null) {
					isDefaultConstructorSearchRequired = false;
				}
				
				IType type = ti.resolveType(getJavaSearchScope());
				
				boolean isTypePublic = Flags.isPublic(type.getFlags());
				boolean isTypeAbstract = Flags.isAbstract(type.getFlags());
				boolean isInPresentPackage = type.getPackageFragment().getElementName().equals(pkg.getElementName());
				boolean isTypeStatic = Flags.isStatic(type.getFlags());
				boolean isTypeInner = type.getDeclaringType()!=null;
				boolean isPublicNullConstructorPresent = false;
				boolean isAnyConstructorPresent = false;
				
				// If we don't need a default constructor skip the searching of the methods for one
				if(isDefaultConstructorSearchRequired){
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
				}
 				boolean constructorError = isDefaultConstructorSearchRequired && !isPublicNullConstructorPresent && isAnyConstructorPresent;
				
				if(constructorError){
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
				isInstantiable = !constructorError && (isTypePublic || isInPresentPackage) && (!isTypeAbstract) && ((!isTypeInner) || (isTypeInner && isTypeStatic));
				if(isInstantiable)
					message = new String();
				setClassName(getFullSelectionName(ti), isInstantiable) ;
			}catch(JavaModelException e){
				t = e;
				isInstantiable = false;
				if(message.length()>0)
					message = message.concat(" : "); //$NON-NLS-1$
				message = ChooseBeanMessages.getString("SelectionAreaHelper.SecondaryMsg.Unknown_ERROR_"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		Status status = new Status(
							isInstantiable?IStatus.OK:IStatus.ERROR, 
							JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 
							IStatus.OK, 
							message, 
							t);
		return status;
	}

	protected String getFullSelectionName(TypeInfo ti){
		String falseFQN = ti.getFullyQualifiedName();
		String correctResolve = null;
		IType type = null;
		try{
			type = project.findType(falseFQN);
		}catch(JavaModelException e){
			type = null;
		}
		if(type!=null)
			correctResolve = type.getFullyQualifiedName('$');//getFQNforType(type,0);
		if(correctResolve==null)
			correctResolve = falseFQN;
		return correctResolve;
	}

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
		if(filterCombo != null){
			filterCombo.setText(filter);
		}
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
			 * @see org.ecloipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
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
