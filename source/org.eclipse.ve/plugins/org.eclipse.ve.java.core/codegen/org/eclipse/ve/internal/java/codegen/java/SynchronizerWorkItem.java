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
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: SynchronizerWorkItem.java,v $
 *  $Revision: 1.9 $  $Date: 2005-09-20 14:28:34 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.DocumentEvent;

import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;



/**
 *  This is the work element context information that is used by the Java Source Synchronizer 
 *  as an input to a strategy routine.
 *  It is an independent snap shot of the current content !!
 *  All Elements (Methods/Fields) are available, and a single element is designated
 *  as the working element.
 */
public class SynchronizerWorkItem {
	
	    final public static String   UNRESOLVED_HANDLE = "<UNRESOLVED>" ; //$NON-NLS-1$
	    final public static String  RELOAD_HANDLE = "<RELOAD>" ; //$NON-NLS-1$
	    final public static String  THIS_ANNOTATION_HANDLE = "_this_Annotation_handle"; //$NON-NLS-1$
	    final public static String  INSTANCE_ANNOTATION_HANDLE = "_instance_Annotation_handle"; //$NON-NLS-1$
	    final public static String  CLASS_IMPLEMENT_EXTENDS_HANDLE = "_class_ImplementsExtends_handle"; //$NON-NLS-1$

		private final static int TYPE_WI_PACKAGE = 0x01;
		private final static int TYPE_WI_IMPORT = 0x01<<1;
//		private final static int TYPE_WI_TYPE = 0x01<<2;
		private final static int TYPE_WI_FIELD = 0x01<<3;
		private final static int TYPE_WI_METHOD = 0x01<<4;
		private final static int TYPE_WI_INNERTYPE = 0x01<<5;
		
		private String        packageName = null;
		private String        extendsClass = null;
		private String[]      implementsInterafces = new String[] {} ;
		private ISourceRange        packages = null ;
		private ISourceRange[]      imports = new ISourceRange[] {} ;
		private ISourceRange[]      fields = new ISourceRange[] {} ;		
		private ISourceRange[]      methods = new ISourceRange[] {} ;
		private ISourceRange[]      innerTypes = new ISourceRange[] {};
		private String        PHandle  = null ;
		private String[]      IHandles = null ;
		private String[]      Fhandles = null ;
		private String[]      Mhandles = null;
		private String[]      MSkeletons = null;
		private String        entireClassCode = null;
		private String        elementPrevContent = null ;
		private String[]     innerTypeHandles = null;
		private String[]     innerTypeSkeletons = null;

		private String        _handle=null ;
		private int           elementIndex = -1 ;		
		
		private ICompilationUnit referenceCU = null;
		private int              deltaOff = -1;
		private int              deltaLen = -1;
		private int              type;
		private boolean         sharedToLocalUpdate ;
		private boolean         handleOnly ;
		
		
		/**
		 *   Handle only - no need to compile
		 */
		public SynchronizerWorkItem(String handle, boolean isDirectionToLocal){
			_handle = handle;
			this.sharedToLocalUpdate = isDirectionToLocal;
			handleOnly = true ;
		}
		
		
		private boolean isIntersect (int cX, int cY, int dX, int dY) {
			// Intersect
        	if (cX >= dX && cX <= dY) return true ;
			if (cY >= dX && dY >= cY) return true ;
			// SubSet
			if (cX <= dX && cY >= dY) return true ;
			if (dX <= cX && dY >= cY) return true ;
			return false ;
		}

		/**
		 * Determine if delta intersect a field. If it is, set _handle
		 */
		private boolean isIntersectFields(IField[] f, ICompilationUnit cu, int len,DocumentEvent docDelta) {
			for (int i=0; i<f.length; i++) 
			try {
				  // Check to see if it is the field's decleration
				  if (isIntersect (f[i].getSourceRange().getOffset(),
					  f[i].getSourceRange().getOffset()+f[i].getSourceRange().getLength(),
				   	  docDelta.getOffset(),
				   	  docDelta.getOffset()+len)) {
					    _handle = f[i].getHandleIdentifier() ;
					    return true ;
				  }
				  // Check to see if it is the FF annotation comment
				  int rangeFrom = f[i].getSourceRange().getOffset()+f[i].getSourceRange().getLength();
				  int rangeTo = cu.getSource().indexOf('\r', rangeFrom);
				  if(i<f.length-1 && rangeTo>f[i+1].getSourceRange().getOffset())
					 rangeTo = f[i].getSourceRange().getOffset();
				  if(rangeFrom<0 || rangeTo<0 || rangeFrom>cu.getSource().length() || rangeTo>=cu.getSource().length())
								continue;
				  if(isIntersect(rangeFrom, rangeTo, docDelta.getOffset(), docDelta.getOffset()+len))  {
						  _handle = INSTANCE_ANNOTATION_HANDLE;
						  return true ;
				  }				  
			}
			catch (JavaModelException e) {}			
			return false ;			
		}
		
		/**
		 * Determine if delta intersect FF annotation area of a This part
		 */
		private boolean isIntersectThisFFAnnotation (ICompilationUnit cu, int len, DocumentEvent docDelta) {			
			ICodeGenSourceRange thisFF = FreeFormThisAnnotationDecoder.getDesignatedAnnotationRange(cu) ;
			if (thisFF != null && isIntersect(thisFF.getOffset(),
				thisFF.getOffset()+thisFF.getLength(),
				docDelta.getOffset(),
				docDelta.getOffset()+len)) {
						                 
                  _handle = THIS_ANNOTATION_HANDLE;
                  return true ;
			}						                
			return false ;
		}
		
		/**
		 * Determin if delta intersect the extends clause
		 */
		private boolean isIntersectSuperImplements (ICompilationUnit cu, DocumentEvent docDelta) {
			IType mainType = CodeGenUtil.getMainType(cu);
			try {
				if (mainType != null) {
					String superName = mainType.getSuperclassName();
					String[] superInterfaces = mainType.getSuperInterfaceNames();
					if ((superName != null && superName.length() > -1) || (superInterfaces != null && superInterfaces.length > 0)) {
						// Type extends something
						ISourceRange typeNameRange = mainType.getNameRange();
						// Note: Changes which are made from the NEWLINE to the end
						//       of the class just extended.
						int from = typeNameRange.getOffset();
						int to = typeNameRange.getOffset() + typeNameRange.getLength();
				
						from = cu.getSource().lastIndexOf('\n', from) < 0 ? from : cu.getSource().lastIndexOf('\n', from);
				
						if (superName != null && superName.length() > 0) {
							to = cu.getSource().indexOf(superName, to);
							to += superName.length();
						}
						if (superInterfaces != null)
							for (int cc = 0; cc < superInterfaces.length; cc++) {
								if (cu.getSource().indexOf(superInterfaces[cc], to) > to) {
									to = cu.getSource().indexOf(superInterfaces[cc], to);
									to += superInterfaces[cc].length();
								}
							}
						if (isIntersect(from, to, docDelta.getOffset(), docDelta.getOffset() + docDelta.getLength())) {
							_handle = CLASS_IMPLEMENT_EXTENDS_HANDLE ;
							return true ;
						}
					}
				}
			}
			catch (JavaModelException e) {}	
			return false ;
			
		}
		
		/**
		 * 
		 * @param t  Has to be a SourceType
		 * @return
		 */
		protected boolean isMainType(IType t) {
			if (t == null || !isSourceIType(t)) return false ;
			IType mainT = CodeGenUtil.getMainType(t.getCompilationUnit()) ;
			return mainT.equals(t) ;
		}
		
		protected IJavaElement promoteToInnerTypeElementIfNeeded(IJavaElement elm) {
			if (elm == null) return null ;
			
			IJavaElement result = elm ;
			
			while (!(isSourceIType(elm)) && elm != null)
			   elm = elm.getParent() ;
			 // Only overide result, if we found a parent type that is NOT the main type
			if (elm != null) {
				if (!isMainType((IType)elm)) 
				   result = elm ; 
			}			
			return result ;
		}
		
		protected boolean isSourceIType(IJavaElement element){
			boolean isSourceType = false;
			if (element instanceof IType) {
				IType iType = (IType) element;
				isSourceType = !iType.isBinary();
			}
			return isSourceType;
		}
		
		/**
		 *  Create a snapshot of the first JavaElement ;
		 */
		public SynchronizerWorkItem(DocumentEvent docDelta,ICompilationUnit cu, boolean isDirectionToLocal, boolean isBeforeActualChange){
			try {
				sharedToLocalUpdate = isDirectionToLocal ;
				referenceCU = cu;
				setSourceCode(cu.getSource());
				if (!cu.isConsistent())
					cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;			    			 
				IJavaElement elm = null;
				int changeFrom = docDelta.getOffset();
				int changeTo = docDelta.getOffset();
				int docDeltaTextLength = (docDelta.getText()==null) ? 0 : docDelta.getText().length();
				if (isBeforeActualChange)
				   changeTo += docDelta.getLength();
				else
				   changeTo += docDeltaTextLength;
				for(int off=changeFrom;off<changeTo;off++){
					elm = cu.getElementAt(off);
					if (isSourceIType(elm)) 
						continue;
					if(elm!=null)
						break;
				}
				if(elm==null)
					elm = promoteToInnerTypeElementIfNeeded(cu.getElementAt(docDelta.getOffset()));
					
				int len = isBeforeActualChange?docDelta.getLength():docDeltaTextLength;
				if (isSourceIType(elm) && isMainType((IType)elm)) {
					// JDT does not designate fields
					IType st = (IType) elm ;
					IField[] f = st.getFields() ;
					// The following will try to set _handle
					
					if (!isIntersectFields(f,cu, len,docDelta)) 
						if (!isIntersectThisFFAnnotation(cu,len,docDelta)) 
							isIntersectSuperImplements(cu,docDelta) ;																			
				}else{
					if (elm != null){
						if (elm instanceof IField) {
							/*
							 * The purpose of SyncWI is to map from document model to 
							 * codegens model. Hence when a change is made in the inner
							 * field it is reflected as a change in the outer field.
							 */
							// TODO  Presently inner fields are transparent. This should not be so later on.
							IType mainType = CodeGenUtil.getMainType(cu);
							IField field = (IField) elm;
							IField ff = getEncompassingField(mainType.getFields(), field);
							if(ff!=null)
								elm = ff;
						}
					    _handle = elm.getHandleIdentifier() ;
					}else { // null element, maybe a change after the classes def. 
						  isIntersectThisFFAnnotation(cu,len,docDelta) ;
					} 
				}
			}catch (JavaModelException e) {}
			// TODO  This is an overkill - delta may not impact BDM
			if (_handle == null)  { // Structural Change.
				_handle = UNRESOLVED_HANDLE ;
				return ;		
			}
			processPackage(cu);
			processMethods(docDelta,cu) ;
			processInnerTypes(cu);
			processFields(cu) ;
			processImports(cu) ;
			processExtends(cu) ;
			processImplements(cu) ;
			deltaOff = docDelta.getOffset() ;
			
			if (isBeforeActualChange)
				deltaLen = docDelta.getLength();
			else {
				deltaLen =
					(docDelta.getText() == null) ? 0 : docDelta.getText().length();
			}
		
			handleOnly = false ;
		}
		
		/**
		 *  Duplicate a WorkElement context, using  different element, typically constructed by
		 *  getNextMethodWorkItem()
		 */
		public SynchronizerWorkItem(SynchronizerWorkItem origin,int Index, int wiType, ICompilationUnit cu) {
		   handleOnly = origin.isHandleOnly() ;		   	
		   sharedToLocalUpdate = origin.isSharedToLocalUpdate() ;		   
		   referenceCU = origin.getCompilationUnit();
		   
		   elementIndex = Index ;		   	
		   setType(wiType);
		   
			packageName = origin.getPackageName();
			packages = origin.getPackage();
			extendsClass = origin.getExtends();
			implementsInterafces = origin.getImplements();
			imports = origin.getImports();
			fields = origin.getFields();
			methods = origin.getMethods();
			PHandle = origin.getPackageHandle();
			IHandles = origin.getImportHandles();
			Fhandles = origin.getFieldshandles();
			Mhandles = origin.getMethodsHandles();
			MSkeletons = origin.getMethodSkeletons();
			entireClassCode = origin.getSourceCode();
			deltaOff = origin.getdeltaOff();
			deltaLen = origin.getdeltaLen();
			innerTypeHandles = origin.getInnerTypeHandles();
			innerTypes = origin.getInnerTypes();
			innerTypeSkeletons = origin.getInnerTypeSkeletons();
		}
		
		public boolean isEquivalent(SynchronizerWorkItem wi) {
			if (getChangedElementHandle() == null && wi.getChangedElementHandle() != null) return false ;
			if (getChangedElementHandle() != null && wi.getChangedElementHandle() == null) return false ;
			
			return isSharedToLocalUpdate() == wi.isSharedToLocalUpdate() &&
			       getChangedElementHandle().equals(wi.getChangedElementHandle()) ;
		}
		
		
		public static SynchronizerWorkItem getMatchingElement(List list, SynchronizerWorkItem wi) {
			if (list == null) return null ;
			Iterator itr = list.iterator() ;
			while (itr.hasNext()) {
				SynchronizerWorkItem w = (SynchronizerWorkItem) itr.next() ;
				if (w.isEquivalent(wi)) return w ;
			}
			return null ;
		}
		
		
		private static int getNextField (ISourceReference m, IField[]f,int deltaEnd) {
			if(m==null || f==null)
				return -1;
			try {
			 for (int i=0; i<f.length; i++)
			   if (f[i].getSourceRange().getOffset() > m.getSourceRange().getOffset() &&
			       f[i].getSourceRange().getOffset()<deltaEnd)
			      return (i) ;
			}
			catch (JavaModelException e) {}
			return -1 ;
		}

		private static int getNextMethod (ISourceReference m, IMethod[]mtd,int deltaEnd) {
			if(m==null || mtd==null)
				return -1;
			try {
			 for (int i=0; i<mtd.length; i++)
			   if (mtd[i].getSourceRange().getOffset() > m.getSourceRange().getOffset()+m.getSourceRange().getLength() &&
			       mtd[i].getSourceRange().getOffset() < deltaEnd)
			      return (i) ;
			}
			catch (JavaModelException e) {}
			return -1 ;
		}
		
		private static int getNextImport (ISourceReference m, IImportDeclaration[] imps, int deltaEnd){
			if(m==null || imps==null)
				return -1;
			try {
			 for (int i=0; i<imps.length; i++)
			   if (imps[i].getSourceRange().getOffset() > m.getSourceRange().getOffset() &&
			       imps[i].getSourceRange().getOffset()<deltaEnd)
			      return (i) ;
			}
			catch (JavaModelException e) {}
			return -1 ;
		}

		public static SynchronizerWorkItem getNextMethodWorkItem(SynchronizerWorkItem current,ICompilationUnit cu) {
			try {
			  ISourceReference member=null ;
			  
			  IMethod[] mtds = CodeGenUtil.getMethods(cu) ;			
			  IType mainType = CodeGenUtil.getMainType(cu);
			  IField[]  flds = mainType==null?null:mainType.getFields() ;
			  IImportDeclaration[] imprts = cu.getImports();
			  IPackageDeclaration[] pkgs = cu.getPackageDeclarations();
			
			// Package
			for(int i=0; pkgs!=null&&i<pkgs.length ;i++){
				if(pkgs[i].getHandleIdentifier().equals(current.getChangedElementHandle())){
					member = pkgs[i];
					break;
				}
			}
			// JCMMethod		       
			for (int i=0; mtds!=null&&i<mtds.length; i++) {
				// TODO  Consider Cycles, where duplicate methods flip flop
				if (mtds[i].getHandleIdentifier().equals(current.getChangedElementHandle())) {
					member = mtds[i] ;
					break ;
				}
			}
			// Field
			for (int i=0; flds!=null&&i<flds.length; i++){
				if (flds[i].getHandleIdentifier().equals(current.getChangedElementHandle())) {
					member=flds[i] ;
					break ;
				}
			}
		  	// Import
			for(int i=0;imprts!=null&&i<imprts.length;i++){
				if(imprts[i].getHandleIdentifier().equals(current.getChangedElementHandle())){
					member = imprts[i];
					break;
				}
			}
			  
			  if (member == null) return null ;
			  int nextF = getNextField(member,flds,current.getdeltaOff()+current.getdeltaLen()) ;
			  int nextM = getNextMethod(member,mtds,current.getdeltaOff()+current.getdeltaLen()) ;
			  int nextI = getNextImport(member,imprts,current.getdeltaOff()+current.getdeltaLen());
			  
			  ISourceReference firstMember = null;
			  if(nextI>-1 && nextI<imprts.length){
			  	if(firstMember==null){
			  		firstMember = imprts[nextI];
			  	}else{
			  		if(firstMember.getSourceRange().getOffset()>imprts[nextI].getSourceRange().getOffset())
			  			firstMember = imprts[nextI];
			  	}
			  }
			  if(nextF>-1 && nextF<flds.length){
			  	if(firstMember==null){
			  		firstMember = flds[nextF];
			  	}else{
			  		if(firstMember.getSourceRange().getOffset()>flds[nextF].getSourceRange().getOffset())
			  			firstMember = flds[nextF];
			  	}
			  }
			  if(nextM>-1 && nextM<mtds.length){
			  	if(firstMember==null){
			  		firstMember = mtds[nextM];
			  	}else{
			  		if(firstMember.getSourceRange().getOffset()>mtds[nextM].getSourceRange().getOffset())
			  			firstMember = mtds[nextM];
			  	}
			  }
			  if(firstMember==null) return null;
			  
			  
			  if(firstMember instanceof IImportDeclaration){
			  	return new SynchronizerWorkItem(current,nextI,TYPE_WI_IMPORT, cu);
			  }else if(firstMember instanceof IMethod){
			  	return new SynchronizerWorkItem(current,nextM,TYPE_WI_METHOD,cu) ;
			  }else if(firstMember instanceof IField){
			  	return new SynchronizerWorkItem(current,nextF,TYPE_WI_FIELD,cu) ;
			  }else
			  	return null;
			  //if (nextF>=0 && nextF < nextM) 
			  //   return new SynchronizerWorkItem(current,nextF,false,cu) ;
			  //else if (nextM>=0)
			  //   return new SynchronizerWorkItem(current,nextM,true,cu) ;
			  //else
			  //   return null ;
			}
			catch (JavaModelException e) {
				// TODO  
				return null ;
			}
		}
				
		public static List getWorkItemList (DocumentEvent docDelta,ICompilationUnit cu, boolean isDirectionToLocal, boolean isBeforeActualChange) {
			ArrayList list = new ArrayList() ;
			try{
				SynchronizerWorkItem wi = new SynchronizerWorkItem(docDelta,cu,isDirectionToLocal, isBeforeActualChange) ;					
				int totals = 0 ;
				if (wi != null) {
				   if (wi.getFields() != null) totals+=wi.getFields().length ;
				   if (wi.getMethods() != null) totals+=wi.getMethods().length ;
				   if (wi.getImports() != null) totals+=wi.getImports().length ;
			    }
				while (wi != null) {
				    if (list.size()>totals) {
				        JavaVEPlugin.log("SynchronizerWorkItem.getWorkItemList(): looping.", Level.WARNING) ; //$NON-NLS-1$
				        break ;
				    }
					list.add(wi) ;
					wi = SynchronizerWorkItem.getNextMethodWorkItem(wi,cu) ;
				}
			}catch(Exception e){
				JavaVEPlugin.log(e,Level.WARNING);
			}
			return list ;			
		}

		public static List refreshWorkItemList(List oList,DocumentEvent docDelta,ICompilationUnit cu, boolean isDirectionToLocal, boolean isBeforeActualChange) {
			List list = getWorkItemList(docDelta,cu,isDirectionToLocal, isBeforeActualChange) ;
			ArrayList nList = new ArrayList() ;
			try{
				// Go through and update prev. Content
				Iterator itr = list.iterator() ;
				while (itr.hasNext()) {
					SynchronizerWorkItem current = (SynchronizerWorkItem) itr.next() ;
					SynchronizerWorkItem older = getMatchingElement(oList,current) ;
					if (older != null) {
						current.setChangeElementPrevContent(older.getChangedElementContent()) ;
					}
					nList.add(current) ;
				}
				// Go through and add removed elements
				if (oList != null) {
	 			 itr = oList.iterator() ;			
				 while (itr.hasNext()) {
					SynchronizerWorkItem older = (SynchronizerWorkItem) itr.next() ;
					SynchronizerWorkItem current = getMatchingElement(list,older) ;
					if (current == null) {
						older.refreshContents(cu) ;
						nList.add(older) ;
					}
				 }
				}
				// If no resolveable WE, return an invalid one so that it is known that 
				// the structure has changed.
	            if (nList.isEmpty()) 
	              nList.add(new SynchronizerWorkItem(docDelta,cu,isDirectionToLocal,isBeforeActualChange)) ;					
			}catch(Exception e){
				JavaVEPlugin.log(e,Level.WARNING);
			}
			return nList ;
		}
		
		private void processPackage(ICompilationUnit cu){
			try{
				if(cu.getPackageDeclarations()!=null && cu.getPackageDeclarations().length>0){
					packageName = cu.getPackageDeclarations()[0].getElementName();
					packages = cu.getPackageDeclarations()[0].getSourceRange();
					PHandle = cu.getPackageDeclarations()[0].getHandleIdentifier();
					if(PHandle!=null && PHandle.equals(_handle)){
						setType(TYPE_WI_PACKAGE);
						elementIndex = 0;
					}
				}else{
					packageName = null;
				}
			}catch(JavaModelException e){
				packageName=null;
			}
		}
		
		private void processMethods(DocumentEvent docDelta,ICompilationUnit cu) {
			try {
				IMethod[] all = CodeGenUtil.getMethods(cu) ;
				if (all == null)
					return ;
				methods = new ISourceRange[all.length] ;
				Mhandles = new String[all.length] ;
				MSkeletons = new String[all.length];
				for (int i=0; i<methods.length; i++) {
					methods[i] = all[i].getSourceRange();
					Mhandles[i]=all[i].getHandleIdentifier() ;
					if (all[i].getHandleIdentifier().equals(_handle)) {
						setType(TYPE_WI_METHOD);
						elementIndex = i ;
			 		}
			 		MSkeletons[i] = createSkeleton(all[i]);
				}			 
			}catch (JavaModelException e) {}
		}
		
		private void processInnerTypes(ICompilationUnit cu){
			try {
				IType[] innTypes = CodeGenUtil.getMainType(cu).getTypes();
				if(innTypes!=null){
					innerTypes = new ISourceRange[innTypes.length];
					innerTypeHandles = new String[innTypes.length];
					innerTypeSkeletons = new String[innTypes.length];
					for(int i=0;i<innTypes.length;i++){
						innerTypes[i] = innTypes[i].getSourceRange();
						innerTypeHandles[i] = innTypes[i].getHandleIdentifier();
						innerTypeSkeletons[i] = createSkeleton(innTypes[i]);
						// Element inside the inner class (like a method) count as well.
						if(_handle.indexOf(innTypes[i].getHandleIdentifier()) >=0){						
							setType(TYPE_WI_INNERTYPE);
							elementIndex = i;
						}
					}
				}
			} catch (JavaModelException e) {	}
		}
				
		private static String createSkeleton(IType type){
			StringBuffer sb = new StringBuffer();
			try {
				sb.append(Flags.toString(type.getFlags()));
			} catch (JavaModelException e3) {}
			try {
				if(type.isClass())
					sb.append(" class "); //$NON-NLS-1$
			} catch (JavaModelException e4) {}
			try {
				if(type.isInterface())
					sb.append(" interface "); //$NON-NLS-1$
			} catch (JavaModelException e5) {}
			sb.append(type.getElementName());
			try {
				if(type.getSuperclassName()!=null && type.getSuperclassName().length()>0){
					sb.append(" extends " + type.getSuperclassName()+" "); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} catch (JavaModelException e) {}
			try {
				if(type.getSuperInterfaceNames()!=null && type.getSuperInterfaceNames().length>0){
					sb.append(" implements "); //$NON-NLS-1$
					for(int i=0;i<type.getSuperInterfaceNames().length;i++){
						sb.append(type.getSuperInterfaceNames()[i]);
						if(i<type.getSuperInterfaceNames().length-1)
							sb.append(". "); //$NON-NLS-1$
						else
							sb.append(" "); //$NON-NLS-1$
					}
				}
			} catch (JavaModelException e1) {}
			sb.append("{\n"); //$NON-NLS-1$
			try {
				if(type.getMethods()!=null && type.getMethods().length>0){
					for(int i=0;i<type.getMethods().length;i++){
						sb.append(createSkeleton(type.getMethods()[i]));
					}
				}
			} catch (JavaModelException e2) {}
			sb.append("}\n"); //$NON-NLS-1$
			return sb.toString();
		}
		
		
		/*
		 * Brought from Java Language Specification
		 * http://java.sun.com/docs/books/jls/second_edition/html/jTOC.doc.html
		 */
		private static String createSkeleton(IMethod method){
			StringBuffer buff = new StringBuffer();
			try{
				int methodFlags = method.getFlags();
				
				// Modifiers
				if(Flags.isPublic(methodFlags))
					buff.append(" public "); //$NON-NLS-1$
				if(Flags.isProtected(methodFlags))
					buff.append(" protected "); //$NON-NLS-1$
				if(Flags.isPrivate(methodFlags))
					buff.append(" private ");	 //$NON-NLS-1$
				if(Flags.isAbstract(methodFlags))
					buff.append(" abstract "); //$NON-NLS-1$
				if(Flags.isStatic(methodFlags))
					buff.append(" static "); //$NON-NLS-1$
				if(Flags.isFinal(methodFlags))
					buff.append(" final "); //$NON-NLS-1$
				if(Flags.isSynchronized(methodFlags))
					buff.append(" synchronized "); //$NON-NLS-1$
				if(Flags.isNative(methodFlags))
					buff.append(" native "); //$NON-NLS-1$
				if(Flags.isStrictfp(methodFlags))
					buff.append(" strictfp "); //$NON-NLS-1$
				
				// JCMMethod name
				String methodSig = method.getSignature();
				String methodName = method.getElementName();
				String[] argNames = method.getParameterNames();
				buff.append(Signature.toCharArray(methodSig.toCharArray(), methodName.toCharArray(), createChars(argNames), true, true));
				
				// Throws
				String[] exceptSigs = method.getExceptionTypes();
				if(exceptSigs.length>0){
					buff.append(" throws "); //$NON-NLS-1$
					for(int i=0;i<exceptSigs.length;i++){
						buff.append(Signature.toCharArray(exceptSigs[i].toCharArray()));
						if(i<exceptSigs.length-1)
							buff.append(", "); //$NON-NLS-1$
					}
				}
				
				// Brackets and return type
				String retSig = method.getReturnType();
				buff.append(" { \n"); //$NON-NLS-1$
				if(Signature.SIG_VOID.equals(retSig)){
					// do nothing since it is void;
				}else{
					buff.append("\t return ("); //$NON-NLS-1$
					buff.append(Signature.toCharArray(retSig.toCharArray()));
					buff.append(") "); //$NON-NLS-1$
//					String returnType = new String(Signature.toCharArray(retSig.toCharArray()));
					if(Signature.SIG_BYTE.equals(retSig) ||
					   Signature.SIG_CHAR.equals(retSig) ||
					   Signature.SIG_DOUBLE.equals(retSig) ||
					   Signature.SIG_FLOAT.equals(retSig) ||
					   Signature.SIG_INT.equals(retSig) ||
					   Signature.SIG_LONG.equals(retSig) ||
					   Signature.SIG_SHORT.equals(retSig)){
					   	buff.append("0"); //$NON-NLS-1$
					}else{
						if(Signature.SIG_BOOLEAN.equals(retSig)){
							buff.append("true"); //$NON-NLS-1$
						}else{
							buff.append("null"); //$NON-NLS-1$
						}
					}
				}
				buff.append(";\n}\n"); //$NON-NLS-1$
			}catch(JavaModelException e){
				JavaVEPlugin.log("SyncWI: Error when constructing skeleton",Level.WARNING); //$NON-NLS-1$
				buff.setLength(0);
			}
			return buff.toString();
		}

		private static char[][] createChars(String[] strs){
			char[][] ret = new char[strs.length][];
			for(int i=0;i<strs.length;i++){
				ret[i] = strs[i].toCharArray();
			}
			return ret;
		}
		
		/**
		 * Fields could be of the form "String a, b;" on the same line. In these
		 * cases the sourcerange of a is [0,9] and of b is [0,12]. Hence we
		 * return only b, since a is wrapped in it. 
		 * 
		 * @return   newField    newField is not being encompassed by any other.
		 *                 otherField  otherField is encompassing newField.
		 *                 null           invalid state. 
		 */
		private static IField getEncompassingField(IField[] fieldsList, IField newField){
			if(newField==null || fieldsList==null || fieldsList.length<1)
				return newField;
			IField gtField = newField;
			try{
				int from = newField.getSourceRange().getOffset();
				int to = from + newField.getSourceRange().getLength();
				for(int i=0;i<fieldsList.length;i++){
					int aFrom = fieldsList[i].getSourceRange().getOffset();
					int aTo = aFrom + fieldsList[i].getSourceRange().getLength();
					if(aFrom<=from && aTo>=to)
							gtField = fieldsList[i];
				}
			}catch(JavaModelException e){}
			return gtField;
		}
		
		private void processFields(ICompilationUnit cu) {
			try {
				IType  t = CodeGenUtil.getMainType(cu) ;							
				IField[] f = t.getFields() ;
				if (f == null || f.length==0) 
					return ;
				fields = new ISourceRange[f.length] ;
				Fhandles = new String[f.length] ;
				int actualFieldCount = 0;
				for (int i=0; i<f.length; i++) {
					IField encompassingField = getEncompassingField(f, f[i]);
					if(encompassingField==null){
						// invalid state
					}else{
						if(encompassingField.equals(f[i])){
							fields[i] = f[i].getSourceRange() ;
							Fhandles[i]=f[i].getHandleIdentifier() ;
							if (f[i].getHandleIdentifier().equals(_handle)) {
								setType(TYPE_WI_FIELD);
								elementIndex = actualFieldCount ;
							}
							actualFieldCount ++;
						}else{
							// There is some other field wrapping this - so forget it.
						}
					}
				}
				if(actualFieldCount!=f.length){
					String[] actualFHandles = new String[actualFieldCount];
					ISourceRange[] actualSrcRanges = new ISourceRange[actualFieldCount];
					int cnt = 0;
					for(int i=0;i<fields.length;i++){
						if(fields[i] == null)
							continue;
						actualSrcRanges[cnt] = fields[i];
						actualFHandles[cnt] = Fhandles[i];
						cnt++;
					}
					fields = actualSrcRanges;
					Fhandles = actualFHandles;
				}
			}catch (JavaModelException e) {}
		}
		
		private void processImports (ICompilationUnit cu) {
			try {
				IImportDeclaration imp[] = cu.getImports() ;
				if (imp != null && imp.length>0) {
					imports = new ISourceRange[imp.length] ;
					IHandles = new String[imp.length];
					for (int i=0; i<imp.length; i++) {
						if(imp[i].getHandleIdentifier().equals(_handle)){
							setType(TYPE_WI_IMPORT);
							elementIndex = i;
						}
						imports[i] = imp[i].getSourceRange() ;
						IHandles[i] = imp[i].getHandleIdentifier() ;
					}
			 	}
			} catch (JavaModelException e) {}
		}
		
		private void processExtends (ICompilationUnit cu) {
			try{
				IType mainType = CodeGenUtil.getMainType(cu);
				if(mainType!=null)
					extendsClass = mainType.getSuperclassName();
			}catch(JavaModelException e) {}
		}

		private void processImplements (ICompilationUnit cu) {
			try{
				IType mainType = CodeGenUtil.getMainType(cu);
				if(mainType!=null)
					implementsInterafces = mainType.getSuperInterfaceNames();
			}catch(JavaModelException e) {}
		}
		
		/**
		 * Refreshes the source locations of the elements in
		 * this SyncWI. If the elements are not there, then 
		 * their sourceRanges are set to null.
		 */
		public void refreshContents (ICompilationUnit cu) {
			try {
				elementPrevContent = getChangedElementContent();
				referenceCU = cu;
				setSourceCode(cu.getSource());
				if (!cu.isConsistent())
					cu.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor()) ;			    			 
				IType t = CodeGenUtil.getMainType(cu) ;			 
				// TODO  Need to consider new methods as well
				if (t == null) {
					// Major parer failure ... reload, and lock the editor
					_handle = RELOAD_HANDLE ;
					handleOnly = true ;
					return ;
				}
				
				
				// All Methods.
				for (int i=0; i<methods.length; i++) {
					IMethod m = CodeGenUtil.getMethod(t,Mhandles[i]) ;
			 		if (m != null)
			 		   methods[i] = m.getSourceRange() ;
			 		else
			 		   methods[i] = null ;
				}
				
				// All Fields
				IField[] flds = t.getFields() ;
				Hashtable fieldTable = new Hashtable() ;
				for (int i=0; i<flds.length; i++) 
					fieldTable.put(flds[i].getHandleIdentifier(),flds[i]) ;
             
				for (int i=0; i<fields.length; i++) {			 	
					if (Fhandles[i]!=null && fieldTable.get(Fhandles[i]) != null)
						fields[i] = ((IField)fieldTable.get(Fhandles[i])).getSourceRange() ;
					else
						fields[i] = null ;
				}
				
				// All Imports
				IImportDeclaration[] imps = cu.getImports();
				Hashtable importTable = new Hashtable() ;
				for (int i=0; i<imps.length; i++) 
					importTable.put(imps[i].getHandleIdentifier(),imps[i]) ;
             
				for (int i=0; i<imports.length; i++) {			 	
					if (importTable.get(IHandles[i]) != null)
						imports[i] = ((IImportDeclaration)importTable.get(IHandles[i])).getSourceRange() ;
					else
						imports[i] = null ;
				}

				// All Packages
				IPackageDeclaration[] pkgsD = cu.getPackageDeclarations();
				Hashtable pkgTable = new Hashtable() ;
				for (int i=0; i<pkgsD.length; i++) 
					pkgTable.put(pkgsD[i].getHandleIdentifier(),pkgsD[i]) ;
             	if(packages!=null){
             		if(pkgTable.get(PHandle) != null){
             			packages = ((IPackageDeclaration)pkgTable.get(PHandle)).getSourceRange();
             		}else{
             			packages = null;
             		}
             	}

			}			
			catch (JavaModelException e) {
				JavaVEPlugin.log(e, Level.WARNING) ;
			}
		}
		
		private String[] getIRangeSources(ISourceRange[] ranges){
			String[] sources = new String[ranges.length];
			for(int i=0;i<ranges.length;i++){
				ISourceRange range = ranges[i];
				sources[i] = getIRangeSource(range);
			}
			return sources;
		}
		
		private String getIRangeSource(ISourceRange range){
		    if (range == null || entireClassCode==null) return null ;
			return getSourceCode().substring(range.getOffset(), range.getOffset()+range.getLength());
		}
		
		public String [] getImplements() {
			return implementsInterafces;
		}
		public String getExtends(){
			return extendsClass;
		}
		public ISourceRange[] getImports() {
			return imports;
		}
		public String[] getImportSources(){
			return getIRangeSources(getImports());
		}
		public ISourceRange[] getMethods() {
			return methods;
		}
		public ISourceRange[] getInnerTypes() {
			return innerTypes;
		}
		public ISourceRange getPackage(){
			return packages;
		}
		public String[] getMethodSources(){
			return getIRangeSources(getMethods());
		}
		public String[] getInnerTypeSources() {
			return getIRangeSources(getInnerTypes());
		}
		public String[] getMethodSkeletons(){
			return MSkeletons;
		}
		public String[] getInnerTypeSkeletons(){
			return innerTypeSkeletons;
		}
		public String[] getInnerTypeHandles() {
			return innerTypeHandles;
		}
		public String [] getMethodsHandles() {
			return Mhandles ;
		}
		public String getPackageHandle(){
			return PHandle;
		}
		public String getPackageSource(){
			return getIRangeSource(getPackage());
		}
		public String getPackageName(){
			return packageName;
		}
		public String getSourceCode(){
			return entireClassCode;
		}
		
		public void setSourceCode(String code){
		    if (code == null && JavaVEPlugin.isLoggingLevel(Level.FINE))
		    		JavaVEPlugin.log("SynchronizerWorkItem: null code", Level.FINE) ; //$NON-NLS-1$
			entireClassCode = code;
		}
		
		public String  getElementContent(String handle) {
			if (handle == null) 
				return null ;
			for (int i=0; i<Mhandles.length; i++) {
				if (handle.equals(Mhandles[i])) 
					return getMethodSources()[i];
			}
			for (int i=0; i<Fhandles.length; i++) {
				if (handle.equals(Fhandles[i])) 
					return getFieldSources()[i];
			}
			for (int i=0; i<IHandles.length; i++) {
				if(handle.equals(IHandles[i]))
					return getImportSources()[i];
			}
			{
				if(handle.equals(PHandle))
					return getPackageSource();
			}
			return null ;
		}
		
		public ISourceRange[] getFields() {
			return fields ;
		}
		public String[] getFieldSources(){
			return getIRangeSources(getFields());
		}
		public String [] getFieldshandles() {
			return Fhandles ;
		}
		public String [] getImportHandles() {
			return IHandles;
		}
		public String getChangedElementHandle () {
			if (elementIndex<0) return _handle ;
			switch(type){
				case TYPE_WI_FIELD: 
					return Fhandles[elementIndex];
				case TYPE_WI_IMPORT:
					return IHandles[elementIndex];
				case TYPE_WI_METHOD:
					return Mhandles[elementIndex];
				case TYPE_WI_PACKAGE:
					return PHandle;
			    case TYPE_WI_INNERTYPE:
			        return innerTypeHandles[elementIndex] ;			        
				default:
					return null;
			}
		}
		public boolean isSharedToLocalUpdate() {
			return sharedToLocalUpdate ;
		}
		public String toString() {
			return getChangedElementHandle() ;
		}
		public ICompilationUnit getCompilationUnit(){
			return referenceCU;
		}
		public boolean isHandleOnly() {
			return handleOnly ;
		}
		public int getdeltaOff()  {
			return deltaOff ;
		}
		public int getdeltaLen() {
			return deltaLen ;
		}
		public boolean isPackage() {
			return isType(TYPE_WI_PACKAGE);
		}
		public boolean isMethod() {
			return isType(TYPE_WI_METHOD);
		}
	    public boolean isInnerClass() {
				return isType(TYPE_WI_INNERTYPE);
		}
		public boolean isField() {
			return isType(TYPE_WI_FIELD);
		}
		public boolean isImport(){
			return isType(TYPE_WI_IMPORT);
		}
		public void setType(int newType){
			type=newType;
		}
		private boolean isType(int ofType){
			return ofType == type;
		}
		public String getChangedElementContent() {
			switch(type){
				case TYPE_WI_FIELD:
					return getFieldSources()[elementIndex];
				case TYPE_WI_METHOD:
					return getMethodSources()[elementIndex];
				case TYPE_WI_IMPORT:
					return getImportSources()[elementIndex];	
				case TYPE_WI_PACKAGE:
					return getPackageSource();				
			}
			return null;
		}
		public int getChangedIndex() {
			return elementIndex ;
		}
		public void setChangeElementPrevContent(String s) {
			elementPrevContent = s ;
		}
		public String getChangeElementPrevContent() {
			return elementPrevContent ;
		}
		
			
}
