/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: TypeResolver.java,v $
 *  $Revision: 1.5 $  $Date: 2006-09-14 18:31:07 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.jem.workbench.utility.JavaModelListener;


import com.ibm.icu.util.StringTokenizer;
 

/**
 * Resolve types for codegen. Based upon a compilation unit, and not a type. This means resolve will not properly 
 * handle simple names found within an inner class if the simple name was for a type that was an inner class of a 
 * supertype/interface of the inner class. This is because we are not getting the type hierarchy of all of the inner
 * classes, nor are we given the inner class as the context during the resolve. This should be ok since we don't
 * support inner classes other than the events anyway.
 * <p>
 * Note: This doesn't handle ambiguous. It will just return first one found. The compiler will mark it as ambiguous
 * and it is the responsibility of the user to correct it there.
 * 
 * @since 1.1.0
 */
public class TypeResolver {
	
	/**
	 * Key for data in edit domain to find the current type resolver. It is so that anyone in the domain can do type resolving efficiently.
	 * The value of the entry in the edit domain will be an instance of {@link TypeResolverRetriever}. The accessor method
	 * can then be used to retrieve the resolver. It is done this way so that the resolver can be recreated as needed without
	 * continually updating the edit domain.
	 * @since 1.2.0
	 */
	public static final Object TYPE_RESOLVER_EDIT_DOMAIN_KEY = TypeResolver.class;
	
	/**
	 * Interface for resolver retriever. An implementation of this should be placed into the edit domain under the
	 * key {@link TypeResolver#TYPE_RESOLVER_EDIT_DOMAIN_KEY}. It should return the current resolver using the
	 * accessor method {@link #getResolver()}.
	 * 
	 * @since 1.2.0
	 */
	public interface TypeResolverRetriever {
		
		/**
		 * Return the current resolver.
		 * @return the current resolver or <code>null</code> if there isn't any.
		 * 
		 * @since 1.2.0
		 */
		public TypeResolver getResolver();
	}
	
	/**
	 * The base resolved return. The subclasses provide the actual
	 * results.
	 * 
	 * @since 1.0.0
	 */
	public static abstract class Resolved {
		/**
		 * Anwser the fully-qualified resolved name of the type.
		 * 
		 * @return the fully-qualified name.
		 * 
		 * @since 1.0.0
		 */
		public abstract String getName();
	}
	
	/*
	 * Indicator that was resolved through main type. This allows it to be cleared out if main type is changed
	 * drastically where we couldn't determine if it was gone by itself. Used as setting in ResolvedType.resolvedThru.
	 * 
	 * @since 1.0.0
	 */
	private static final Object MAIN_TYPE = new Object();
	
	/*
	 * Indicator that was resolved through super type or interface. This allows it to be cleared out if super type heirarchy is changed.
	 * Used as setting in ResolvedType.resolvedThru.
	 * 
	 * @since 1.0.0
	 */
	private static final Object SUPER_TYPE = new Object();
	
	
	/**
	 * Used to store the resolved itype and fully-qualified name so that they
	 * aren't continually retrieved. This is returned by resolve for use of
	 * the users to get to either fields. Primitives won't have <code>type</code>
	 * set because there are no IType's for primitives.
	 * 
	 * @since 1.0.0
	 */
	public static class ResolvedType extends Resolved {
		ResolvedType(IType type, String name) {
			this.type = type;
			this.name = name;
		}
		
		ResolvedType(IType type) {
			this.type = type;
			this.name = type.getFullyQualifiedName();
		}		
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved#getName()
		 */
		public final String getName() {
			return this.name;
		}
		
		/**
		 * Return the type
		 * @return the type or <code>null</code> if a primitive. The name of the primitive will be in the name.
		 * 
		 * @see #getName()
		 * @since 1.0.0
		 */
		public final IType getIType() {
			return this.type;
		}
			
		/*
		 * The IType for this resolution. If <code>null</code>, then it is for a primitive. The name of the
		 * primitive will be in "name" field.
		 * @see ResolvedType#name
		 */
		private final IType type;
		
		/*
		 * Fully-qualified name in reflection format (i.e. '$' instead '.' for inner classes).
		 */
		private final String name;
		
		/*
		 * How was this resolved. It can be:
		 * <dl>
		 * <li>ImportDecl: The import decl used, either directly or indirectly through another resolved type that was resolved
		 * through this import decl. It is accessed through <package-protected> access within the TypeResolver.
		 * <li>MAIN_TYPE: Was resolved as an inner class of the maintype.
		 * <li>SUPER_TYPE: Was resolved thru one of the super types or interfaces.
		 * <li><code>null</code>: Was resolved through same package.
		 * </dl>
		 */
		Object resolvedThru;
	}
	
	/**
	 * This is a resolved Array Type. In this case the <code>type</code> field is the
	 * final component type. I.e. if it is <code>String[][]</code> the final type is
	 * <code>java.lang.String</code> IType.
	 * 
	 * @since 1.0.0
	 */
	public static class ResolvedArrayType extends Resolved {

		ResolvedArrayType(ResolvedType finalType, int dimensions, String name) {
			this.finalType = finalType;
			this.dimensions = dimensions;
			this.name = name;
		}
		
		
		/* (non-Javadoc)
		 * @see org.eclipse.ve.internal.java.codegen.util.TypeResolver.Resolved#getName()
		 */
		public final String getName() {
			return name;
		}
		
		/**
		 * Return the final resolved type.
		 * @return final resolved type.
		 * 
		 * @since 1.0.0
		 */
		public final ResolvedType getFinalType() {
			return finalType;
		}
		
		/**
		 * The dimensions of the array.
		 * @return array dimensions.
		 * 
		 * @since 1.0.0
		 */
		public final int getDimensions() {
			return dimensions;
		}
		
		/*
		 * The final resolved type.
		 */
		private final ResolvedType finalType;
	
		/*
		 * The fully-qualified name of the array type.
		 */
		private final String name;
		
		/*
		 * The dimensions of the array.
		 */
		private final int dimensions;
	}
	
	private static final Map STANDARD;
	static {
		STANDARD = new HashMap();
		STANDARD.put("int", new ResolvedType(null, "int")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("float", new ResolvedType(null, "float")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("double", new ResolvedType(null, "double")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("short", new ResolvedType(null, "short")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("long", new ResolvedType(null, "long")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("boolean", new ResolvedType(null, "boolean")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("byte", new ResolvedType(null, "byte")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("char", new ResolvedType(null, "char")); //$NON-NLS-1$ //$NON-NLS-2$
		STANDARD.put("void", new ResolvedType(null, "void")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/*
	 * Resolver's import decl.
	 * 
	 * @since 1.0.0
	 */
	private static class ImportDecl {
		/*
		 * The fully-qualified name from the import.
		 */
		private final String fullyQualifiedName;
		
		/*
		 * The last qualifier of the name, (i.e. for "x.y.z", the last qualifier is "z"). Only set for not onDemand types.
		 */
		private final String lastQualifier;
		
		/*
		 * This will be the resolved type. This will only be set for an onDemand import of a class. E.g.
		 * "import x.y.z.Q.*", then this will be the IType of "x.y.z.Q". If this is not an onDemand, or if
		 * this is an onDemand for a package (e.g. "import x.y.z.*") then this will be <code>null</code>.
		 * It will be set in resolveIt(IJavaProject).
		 */
		private IType resolvedType;
		
		/*
		 * Has this been resolved yet. Even if it resolve fails, it is still consider hasResolved.
		 */
		private boolean hasResolved;
		
		/**
		 * Create using an IImportDeclaration.
		 * 
		 * @param importDecl
		 * 
		 * @since 1.0.0
		 */
		public ImportDecl(IImportDeclaration importDecl, IJavaProject javaproject) {
			String fq = importDecl.getElementName();
			if (importDecl.isOnDemand()) {
				fullyQualifiedName = fq.substring(0, fq.length()-2);	// Strip off trailing ".*"
				lastQualifier = null;
				resolveIt(javaproject);
			} else {
				fullyQualifiedName = fq;
				int ndx = fq.lastIndexOf('.');
				lastQualifier = fq.substring(ndx+1);
			}
		}
		
		/**
		 * Create using an ImportDeclaration.
		 * 
		 * @param importDecl
		 * 
		 * @since 1.0.0
		 */
		public ImportDecl(ImportDeclaration importDecl, IJavaProject javaProject) {
			fullyQualifiedName = importDecl.getName().getFullyQualifiedName();
			if (importDecl.isOnDemand()) {
				lastQualifier = null;
				resolveIt(javaProject);
			} else
				lastQualifier = ((QualifiedName) importDecl.getName()).getName().getIdentifier();
		}
		
		/**
		 * Special one used to generate the default "java.lang.*". It will always be an ondemand package type.
		 * 
		 * @param fullyQualified fully-qualified without the ".*" on it. Must be a package.
		 * 
		 * @since 1.0.0
		 */
		ImportDecl(String fullyQualified) {
			this(fullyQualified, true, null);
		}

		/**
		 * Special one used to add one via the API instead of coming through reconcile. This
		 * allows VE to add some while still generating code and have resolves work correctly
		 * without having a reconcile done.
		 * 
		 * @param fullyQualified fullyqualified without ".*" if on demand.
		 * @param onDemand
		 * @param javaProject
		 * 
		 * @since 1.0.0
		 */
		ImportDecl(String fullyQualified, boolean onDemand, IJavaProject javaProject) {
			fullyQualifiedName = fullyQualified;
			if (onDemand) {
				lastQualifier = null;
				if (javaProject != null)
					resolveIt(javaProject);
			} else {
				int ndx = fullyQualified.lastIndexOf('.');
				lastQualifier = fullyQualified.substring(ndx+1);
			}
		}
		
		/**
		 * Is this an onDemand entry. I.e. one that ends in ".*".
		 * @return onDemand
		 * 
		 * @since 1.0.0
		 */
		public boolean isOnDemand() {
			// We're currently only storing a lastQualifier if not onDemand. We only need it then. So we're using
			// that as a flag to determine if onDemand. If in the future we need the lastQualifier even when onDemand,
			// then we will need to add a boolean field for this.
			return lastQualifier == null;
		}
		
		/**
		 * Get the fully-qualified name of the import. (i.e. "import x.y.z" will answer "x.y.z").
		 * 
		 * @return the fully-qualified name of the import.
		 * 
		 * @since 1.0.0
		 */
		public String getFullyQualifiedName() {
			return fullyQualifiedName;
		}
		
		/**
		 * Get the last qualifier. (i.e. if "import x.y.z" it will return "z". Currently will
		 * return <code>null</code> if this is an onDemand import.
		 * @return last qualifier or <code>null</code> if an onDemand import.
		 * 
		 * @see #isOnDemand()
		 * @since 1.0.0
		 */
		public String getLastQualifier() {
			return lastQualifier;
		}
		
		/**
		 * Resolve it now. This should not be called on non-onDemand declarations.
		 * @param javaProject
		 * @throws JavaModelException
		 * 
		 * @since 1.0.0
		 */
		public void resolveIt(IJavaProject javaProject) {
			try {
				// First see if a type. If not a type, then it is a package (or not found, treat as a package).
				hasResolved = true;
				IType type = javaProject.findType(fullyQualifiedName);
				if (type != null) {
					// It is a type.
					resolvedType = type;
				}
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}
		
		/**
		 * This is used to clear the resolved if we done a clear all.
		 * 
		 * @since 1.0.0
		 */
		public void clearResolved() {
			resolvedType = null;
			hasResolved = false;
		}
		
		/**
		 * Has this been resolved, or at least tried.
		 * 
		 * @return <code>true</code> if resolve had been tried.
		 * 
		 * @since 1.0.0
		 */
		public boolean isResolved() {
			return hasResolved;
		}
		
		/**
		 * Get the resolved type. It will be <code>null</code> if not yet resolved, or if this is for package
		 * type import. It should not be called on a non-onDemand declaration.
		 * @return
		 * 
		 * @see #isResolved()
		 * @since 1.0.0
		 */
		public IType getResolvedType() {
			return resolvedType;
		}
	}
	
	/**
	 * This is a list of import declarations (ImportDecl), where each entry in the list will be an import declaration.
	 */
	protected List importDecls; 
		
	/**
	 * Map of the resolved names so far resolved to the ResolvedType. This is to make reuses of the same
	 * name faster. (Included in the map are the standard (with primitives) because they may come in also by codegen callers).
	 */
	protected Map namesToResolvedTypes = new HashMap(STANDARD);
	
	/**
	 * Type hierarchy of the main type of the class. Used to to cache the superClasses/Interfaces so that
	 * they are available for the lookup of inner classes of them since they simplenames of inner classes 
	 * can be found without imports when they are in the maintype or one of the superclass/interfaces.
	 * 
	 * This hierarchy will turn on its monitoring of changes so that it can be refreshed as needed.
	 */
	private ITypeHierarchy mainTypeHierarchy;
	
	private ResolvedType resolvedMain;
	
	/**
	 * The main type for this resolver.
	 */
	protected IType mainType;
	
	private static final IType[] EMPTY_ALL_SUPER_TYPES = new IType[0];
	
	/**
	 * Cached copy of all super types, i.e. the super classes and the super interfaces in bottom-up order.
	 * This is cached because the list is referenced a lot and is expensive to build.
	 */
	private IType[] allSuperTypes;
	
	/**
	 * Package name of the main type.
	 */
	protected String mainPackageName;
	
	/**
	 * The java project of the main type.
	 */
	protected IJavaProject javaProject;
	

	/*
	 * Listen for changes from the hierarchy and mark refresh needed.
	 */
	protected ITypeHierarchyChangedListener hierarchyChangedListener = new ITypeHierarchyChangedListener() {

		public void typeHierarchyChanged(ITypeHierarchy typeHierarchy) {
			markRefreshHierarchyNeeded();
		}
	};
	
	/*
	 * Mark that need hierarchy refresh. 
	 * 
	 * @since 1.0.0
	 */
	private void markRefreshHierarchyNeeded() {
		synchronized(triedHierarchyCreate) {
			needHierarchyRefresh = true;
		}
	}
	
	/*
	 * Listen to the java model for changes. 
	 */
	protected JavaModelListener modelListener;
	
	
	/**
	 * Construct using IImportDeclarations and a maintype.
	 * @param importDecls the array that comes from ICompilationUnit.
	 * @param mainType the type of the ICompilationUnit.
	 * 
	 * @since 1.0.0
	 */
	public TypeResolver(IImportDeclaration[] importDecls, IType mainType) {
		this(mainType);
		this.importDecls = new ArrayList(importDecls.length+1);
		boolean javalangExists = false;
		for (int i = 0; i < importDecls.length; i++) {
			if (importDecls[i].getElementName().equals("java.lang.*")) //$NON-NLS-1$
				javalangExists = true;
			this.importDecls.add(new ImportDecl(importDecls[i], javaProject));
		}
		if (!javalangExists) {
			// Finally add the java.lang.* implicit.
			this.importDecls.add(new ImportDecl("java.lang")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Construct using ImportDeclarations and a maintype.
	 * @param importDecls the list of IImportDeclarations that comes from ICompilationUnit.
	 * @param mainType the type of the ICompilationUnit.
	 * 
	 * @since 1.0.0
	 */
	public TypeResolver(List importDecls, IType mainType) {
		this(mainType);
		int size = importDecls.size();
		this.importDecls = new ArrayList(size+1);
		boolean javalangExists = false;
		for (int i = 0; i < size; i++) {
			ImportDeclaration importDeclaration = (ImportDeclaration) importDecls.get(i);
			if (importDeclaration.isOnDemand() && importDeclaration.getName().getFullyQualifiedName().equals("java.lang")) //$NON-NLS-1$
				javalangExists = true;
			this.importDecls.add(new ImportDecl(importDeclaration, javaProject));
		}
		if (!javalangExists) {
			// Finally add the java.lang.* implicit.
			this.importDecls.add(new ImportDecl("java.lang")); //$NON-NLS-1$
		}
	}	
	
	/*
	 * Internal one used by others.
	 */
	protected TypeResolver(IType mainType) {
		this.mainType = mainType;
		this.resolvedMain = new ResolvedType(mainType, mainType.getFullyQualifiedName());
				
		// Put maintype into pre-resolved list.
		namesToResolvedTypes.put(mainType.getElementName(), resolvedMain);
		mainPackageName = mainType.getPackageFragment().getElementName();
		javaProject = mainType.getJavaProject();
		modelListener = new JavaModelListener(ElementChangedEvent.POST_CHANGE | ElementChangedEvent.POST_RECONCILE) {

			protected IJavaProject getJavaProject() {
				return javaProject;
			}

			
			public void processDelta(IJavaElementDelta delta) {
				IJavaElement element = delta.getElement();
				
				switch (element.getElementType()) {
					case IJavaElement.IMPORT_CONTAINER:
						processJavaElementChanged((IImportContainer) element, delta);
						break;
					case IJavaElement.IMPORT_DECLARATION:
						processJavaElementChanged((IImportDeclaration) element, delta);
						break;
					default:
						super.processDelta(delta);
						break;
				}				
			}
			
			protected void processJavaElementChanged(IImportContainer element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.REMOVED:
						clearAllImports();	// The container is gone, so get rid of imports.
						break;
					case IJavaElementDelta.ADDED:
						refreshImportContainer();	// We now have an import container, so build it up.
						break;
					case IJavaElementDelta.CHANGED:
						processChildren(element, delta);	// We have something changed. It will be thru children.
						break;
				}
			}

			protected void processJavaElementChanged(IImportDeclaration element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.REMOVED:
						getRemovedImportDecls().add(element);
						break;
					case IJavaElementDelta.ADDED:
						addImport(element);	// We don't bother accumulating these like we do for remove because no clearing of current resolves needed, so no need to group together.
						break;
					case IJavaElementDelta.CHANGED:
						// This would be a reorder, which doesn't affect the outcome.
						break;
				}
			}
			
			protected void processJavaElementChanged(ICompilationUnit element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.ADDED:
						break;	// On add don't need to do anything
					case IJavaElementDelta.REMOVED:
						getRemovedElements().add(element);
						break;
					case IJavaElementDelta.CHANGED:
						if ((delta.getFlags() & IJavaElementDelta.F_CHILDREN) > 0)
							processChildren(element, delta);
						else if ((delta.getFlags() & (IJavaElementDelta.F_CONTENT | IJavaElementDelta.F_FINE_GRAINED)) == IJavaElementDelta.F_CONTENT) {
							getRemovedElements().add(element);	// It is a blast change of the source and jdt couldn't determine what changed.
							if (element.equals(TypeResolver.this.mainType.getCompilationUnit())) {
								markRefreshHierarchyNeeded();	// Also it was the main type, maybe super changed, don't know so clear the hierarchy too.
								refreshImportContainer();	// Also we don't know about the import container. Recreate it.
							}
						}
						break;
				}
			}

			protected void processJavaElementChanged(IJavaProject element, IJavaElementDelta delta) {
				if (isBlastAll())
					return;	// Don't bother, we are going to clear everything anyway.
				if (isInClasspath(element)) {
					switch (delta.getKind()) {
						case IJavaElementDelta.REMOVED:
							// The project has been removed. For now we'll just blast everything and reresolve.
							// In the future we can see if we should only remove those resolved in this project.
							setBlastAll();
							return;
						case IJavaElementDelta.CHANGED:
							if ((delta.getFlags() & IJavaElementDelta.F_CLOSED) != 0) {
								// The project has been closed. For now we'll just blast everything and reresolve.
								// In the future we can see if we should only remove those resolved in this project.
								setBlastAll();
								return;
							}
							if (isClasspathResourceChange(delta)) {
								// The classpath of the project has been changed. For now we will blast all.
								// In the future we could use the fragment root added/removed/changed notifications
								// instead and remove only those within the fragment root. However, removal of
								// a project doesn't have any children, just classpath changed flag.
								setBlastAll();
								return;
							}
							processChildren(element, delta);
							break;
						case IJavaElementDelta.ADDED:
							return;	// Project was added, this doesn't cause any existing to change, hopefully.
					}
				}
			}
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.adapters.jdom.JavaModelListener#processJavaElementChanged(org.eclipse.jdt.core.IPackageFragmentRoot, org.eclipse.jdt.core.IJavaElementDelta)
			 */
			protected void processJavaElementChanged(IPackageFragmentRoot element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.CHANGED:
						// If the archive content changed, then add to removedElements.
						if ((delta.getFlags() & IJavaElementDelta.F_ARCHIVE_CONTENT_CHANGED) != 0) {
							getRemovedElements().add(element);
						} else 
							processChildren(element, delta);
						break;
					case IJavaElementDelta.ADDED:
						break;	// Don't need to do anything on an add.
					case IJavaElementDelta.REMOVED:
						// Probably won't get here, but if removed, blast all (could actually use removed elements).
						setBlastAll();
					break;
				}
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.adapters.jdom.JavaModelListener#processJavaElementChanged(org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core.IJavaElementDelta)
			 */
			protected void processJavaElementChanged(IPackageFragment element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.ADDED:
						break;	// On add don't need to do anything
					case IJavaElementDelta.REMOVED:
						getRemovedElements().add(element);
						break;
					case IJavaElementDelta.CHANGED:
						processChildren(element, delta);
						break;
				}
			}
			
			protected void processJavaElementChanged(IClassFile element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.ADDED:
						break;	// On add don't need to do anything
					case IJavaElementDelta.REMOVED:
						getRemovedElements().add(element);
						break;
					case IJavaElementDelta.CHANGED:
						processChildren(element, delta);
						break;
				}
			}
			
			protected void processJavaElementChanged(IType element, IJavaElementDelta delta) {
				switch (delta.getKind()) {
					case IJavaElementDelta.ADDED:
						break;	// On add don't need to do anything
					case IJavaElementDelta.REMOVED:
						getRemovedElements().add(element);
						break;
					case IJavaElementDelta.CHANGED:
						if ((delta.getFlags() & IJavaElementDelta.F_SUPER_TYPES) != 0 && element.equals(TypeResolver.this.mainType))
							markRefreshHierarchyNeeded();	// The super type of the main type has changed, so we need to refresh the hierarchy too.
						processChildren(element, delta);
						break;
				}
			}
			
			private boolean isBlastAll() {
				return blastAll.get() != null;
			}
			
			private void setBlastAll() {
				blastAll.set(Boolean.TRUE);
				removedElements.set(null);	// If we blast all, we don't need any accumulated removed elements.
			}
			
			private IRegion getRemovedElements() {
				IRegion region = (IRegion) removedElements.get();
				if (region == null)
					removedElements.set(region = JavaCore.newRegion());
				return region;
			}
			
			private List getRemovedImportDecls() {
				List removed = (List) removedImportDecls.get();
				if (removed == null)
					removedImportDecls.set(removed = new ArrayList(1));
				return removed;
			}
			
			// Blast everything. Using ThreadLocal because changes delta's can come on any thread at any time.
			private ThreadLocal blastAll = new ThreadLocal();
			
			// Region of removed elements. These are the elements that have been removed. Will be used to remove
			// any types that are from these elements.
			private ThreadLocal removedElements = new ThreadLocal();
			
			// Removed import declarations, gather and then done all at once at the end.
			private ThreadLocal removedImportDecls = new ThreadLocal();
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.jem.internal.adapters.jdom.JavaModelListener#elementChanged(org.eclipse.jdt.core.ElementChangedEvent)
			 */
			public void elementChanged(ElementChangedEvent event) {
				blastAll.set(null);
				removedElements.set(null);
				removedImportDecls.set(null);
				super.elementChanged(event);
				if (isBlastAll()) {
					clearAllResolved();
				} else {
					if (removedElements.get() != null)
						clearRegion((IRegion) removedElements.get());
					if (removedImportDecls.get() != null)
						processRemovedImports(getRemovedImportDecls());
				}
				blastAll.set(null);
				removedElements.set(null);
				removedImportDecls.set(null);
			}
		};
	}
	
	
	/*
	 * Add an import decl. Make sure we don't already have it to be safe.
	 */
	private synchronized void addImport(IImportDeclaration iid) {
		String importName = iid.isOnDemand() ? iid.getElementName().substring(0, iid.getElementName().length()-2) : iid.getElementName();
		if (iid.isOnDemand() && importName.equals("java.lang")) //$NON-NLS-1$
			return;	// We already have java.lang.
		for (int i = 0; i < importDecls.size(); i++) {
			ImportDecl id = (ImportDecl) importDecls.get(i);
			if (iid.isOnDemand() == id.isOnDemand() && id.getFullyQualifiedName().equals(importName))
				return;	// We already have it.
		}
		importDecls.add(new ImportDecl(iid, javaProject));
	}
	
	/**
	 * Add an import before the next reconcile. This is so that VE can add an import during code generation
	 * and see the resolve works correctly before a reconcile is done. The following reconcile will see that
	 * the added import is already added and will do nothing.
	 * <p>
	 * Note: It is very important that you don't add and then REMOVE the same import before the reconcile 
	 * occurs. Because if that was done then the reconcile will not see the import was removed and
	 * won't tell the TypeResolver to remove it. So it would stay.
	 * 
	 * @param importName fully import name e.g. <code>x.y.z</code>. Do not include ".*" if on demand. The ondemand flag will tell you that.
	 * @param onDemand <code>true</code> if on demand (i.e. <code>import x.y.z.*;</code>).
	 * 
	 * @since 1.0.0
	 */
	public synchronized void addImport(String importName, boolean onDemand) {
		if (onDemand && importName.equals("java.lang")) //$NON-NLS-1$
			return;	// We already have java.lang.
		for (int i = 0; i < importDecls.size(); i++) {
			ImportDecl id = (ImportDecl) importDecls.get(i);
			if (onDemand == id.isOnDemand() && id.getFullyQualifiedName().equals(importName))
				return;	// We already have it.
		}
		importDecls.add(new ImportDecl(importName, onDemand, javaProject));
	}
	
	/*
	 * Clear out everything.
	 */
	private synchronized void clearAllResolved() {
		namesToResolvedTypes.clear();
		// Add primitives back.
		namesToResolvedTypes.putAll(STANDARD);
		for (int i = 0; i < importDecls.size(); i++) {
			ImportDecl id = (ImportDecl) importDecls.get(i);
			if (id.isResolved())
				id.clearResolved();
		}
	}
	
	/*
	 * Clear out anything under the given region.
	 */
	private synchronized void clearRegion(IRegion region) {
		for (Iterator itr = namesToResolvedTypes.values().iterator(); itr.hasNext();) {
			ResolvedType rt = (ResolvedType) itr.next();
			// rt.type could be null if this is for a primitive.
			if (rt.type != null && region.contains(rt.type))
				itr.remove();
		}
		
		// Should also clear the resolved of any import decl that is within this region. That
		// way they will be re-resolved next time needed, and may be not found, which is good.
		for (int i = 0; i < importDecls.size(); i++) {
			ImportDecl id = (ImportDecl) importDecls.get(i);
			if (id.isResolved() && id.getResolvedType() != null && region.contains(id.getResolvedType()))
				id.clearResolved();
		}
	}
	
	/*
	 * Completely refresh the import container. Clear out any imports that are removed.
	 */
	private synchronized void refreshImportContainer() {
		IImportContainer ic = mainType.getCompilationUnit().getImportContainer();
		if (!ic.exists()) {
			clearAllImports();
		} else {
			try {
				// There is a container, so move over those found, create for any new ones, and then
				// any left in old container should be cleaned up.
				IJavaElement[] imports = ic.getChildren();	// The children will be ImportDeclarations.
				List newContainer = new ArrayList(imports.length+1);
				boolean javalangExists = false;
				for (int i = 0; i < imports.length; i++) {
					IImportDeclaration iid = (IImportDeclaration) imports[i];
					// Get the element name, strip off ".*" if necessary.
					String importName = iid.isOnDemand() ? iid.getElementName().substring(0, iid.getElementName().length()-2) : iid.getElementName();
					if (iid.isOnDemand() && importName.equals("java.lang")) //$NON-NLS-1$
						javalangExists = true;
					// See if this import is currently in the list.
					boolean movedit = false;
					for (int j = 0; j < importDecls.size(); j++) {
						ImportDecl id = (ImportDecl) importDecls.get(j);
						if (iid.isOnDemand() == id.isOnDemand() && importName.equals(id.getFullyQualifiedName())) {
							// It matches
							newContainer.add(id);	// Move it over
							importDecls.remove(j);	// Take it out because we moved it.
							movedit = true;
							break;
						}
					}
					if (!movedit) {
						// Brand new one, add to list.
						newContainer.add(new ImportDecl(iid, javaProject));
					}
				}
				if (!javalangExists) {
					// It wasn't explicit in the new container so we need to move it over.
					for (int i = 0; i < importDecls.size(); i++) {
						ImportDecl id = (ImportDecl) importDecls.get(i);
						if (id.isOnDemand() && id.getFullyQualifiedName().equals("java.lang")) { //$NON-NLS-1$
							newContainer.add(id);
							importDecls.remove(i);
							break;
						}
						
					}
				}
				if (!importDecls.isEmpty())
					clearSpecificImports(importDecls);	// Get rid of those left.
				importDecls = newContainer;
			} catch (JavaModelException e) {
				JavaVEPlugin.log(e,Level.WARNING);
				clearAllImports();
			}
		}
	}
	
	/*
	 * Given the list of IImportDeclarations, process it, and clear the import decls.
	 */
	private synchronized void processRemovedImports(List removedImports) {
		List toClear = new ArrayList(removedImports.size());
		for (Iterator itr = importDecls.iterator(); itr.hasNext() && !removedImports.isEmpty();) {
			ImportDecl id = (ImportDecl) itr.next();
			for (Iterator itrr = removedImports.iterator(); itrr.hasNext();) {
				IImportDeclaration iid = (IImportDeclaration) itrr.next();
				if (id.isOnDemand() == iid.isOnDemand()) {
					boolean foundit = false;
					if (id.isOnDemand())
						if (id.getFullyQualifiedName().equals(iid.getElementName().substring(0, iid.getElementName().length()-2)))
							foundit = true;
						else
							;
					else if (id.getFullyQualifiedName().equals(iid.getElementName()))
							foundit = true;
					if (foundit) {
						toClear.add(id);
						itr.remove();
						itrr.remove();
						break;
					}
				}
			}
		}
		
		clearSpecificImports(toClear);
	}
	
	/*
	 * Clear a specific list of imports.
	 */
	private void clearSpecificImports(List toClear) {
		for (Iterator itr = namesToResolvedTypes.values().iterator(); itr.hasNext();) {
			ResolvedType rt = (ResolvedType) itr.next();
			if (rt.resolvedThru instanceof ImportDecl) {
				if (toClear.contains(rt.resolvedThru))
					itr.remove();	// This was resolved thru a specific import decl that is going away, so it goes away.
			}
		}
	}

	/*
	 * Clear all imports. Used if there is no import container or if there was an error
	 * interpreting the import container. We want to clear in that case because it is
	 * better to get unresolved than to have bad resolves.
	 * 
	 * @since 1.0.0
	 */
	private void clearAllImports() {
		// There is no import container now, so clear everything from the old except java.lang.
		for (Iterator itr = namesToResolvedTypes.values().iterator(); itr.hasNext();) {
			ResolvedType rt = (ResolvedType) itr.next();
			if (rt.resolvedThru instanceof ImportDecl && !((ImportDecl) rt.resolvedThru).getFullyQualifiedName().equals("java.lang")) //$NON-NLS-1$
				itr.remove();	// This was resolved thru an import decl, so it goes away.
		}
		// Find the java.lang, keep it out, then clear and add it back.
		ImportDecl jl = null;
		for (int i = 0; i < importDecls.size(); i++) {
			if (((ImportDecl) importDecls.get(i)).getFullyQualifiedName().equals("java.lang")) { //$NON-NLS-1$
				jl = (ImportDecl) importDecls.get(i);
				break;
			}
		}
		importDecls.clear();
		importDecls.add(jl);
	}

	// Have we tried at least once. If it failed we don't want to try again?
	// We also use it as something to sync on to change needHierarchyRefresh. This
	// is because we want to hold up getting maintype hierarchy while refreshing it,
	// BUT we don't want to hold up the notification that a refresh is needed. So
	// we need two semaphores. We use "this" for the refresh and we use triedHierarchyCreate for changing the 
	// refresh flag.
	private boolean[] triedHierarchyCreate = new boolean[1];
	
	private boolean needHierarchyRefresh = true;	// Do we need a hierarchy refresh? Must be changed under sync(this) control.
	
	/**
	 * Answer the super type hierarchy for the main type. This is here both for usage within
	 * the resolver, but also as a utility for the rest of codegen. These are expensive so only
	 * use the one.
	 * 
	 * @return Returns the mainTypeHierarchy or <code>null</code> if not created for some reason.
	 */
	public synchronized ITypeHierarchy getMainTypeHierarchy() {
		boolean doRefresh = false;
		synchronized (triedHierarchyCreate) {
			// Sync on this so that if a refresh needed notification comes in it will wait to we got the snapshot of the refresh flag.
			doRefresh = needHierarchyRefresh;
			needHierarchyRefresh = false;
		}
		while (doRefresh) {
			allSuperTypes = EMPTY_ALL_SUPER_TYPES;			
			if (!triedHierarchyCreate[0]) {
				try {
					triedHierarchyCreate[0] = true;
					mainTypeHierarchy = mainType.newSupertypeHierarchy(new NullProgressMonitor());
					mainTypeHierarchy.addTypeHierarchyChangedListener(hierarchyChangedListener);
					allSuperTypes = mainTypeHierarchy.getAllSupertypes(mainType);
				} catch (JavaModelException e) {
					JavaVEPlugin.log(e, Level.WARNING);
				}
			} else {
				if (mainTypeHierarchy != null) {
					try {
						// It had been created ok originally.
						mainTypeHierarchy.refresh(new NullProgressMonitor());
						allSuperTypes = mainTypeHierarchy.getAllSupertypes(mainType);	// Get the new list. It may be different.
						// Now clear out any resolves thru super types so they get re-resolved.
						for (Iterator resolvedItr = namesToResolvedTypes.values().iterator(); resolvedItr.hasNext();) {
							ResolvedType rt = (ResolvedType) resolvedItr.next();
							if (rt.resolvedThru == SUPER_TYPE)
								resolvedItr.remove();
						}
					} catch (JavaModelException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			}
			
			synchronized (triedHierarchyCreate) {
				// A sanity check. did it go stale while refreshing?
				doRefresh = needHierarchyRefresh;
				needHierarchyRefresh = false;
			}		
		}
		
		return mainTypeHierarchy;

	}

	/**
	 * Dispose when done. This is important because some system listeners are added in here and they
	 * will not GC by themselves without dispose.
	 * 
	 * 
	 * @since 1.0.0
	 */
	public void dispose() {
		if (mainTypeHierarchy != null) {
			mainTypeHierarchy.removeTypeHierarchyChangedListener(hierarchyChangedListener);
			mainTypeHierarchy = null;	// This prevents an accidental re-resolve. It won't try resolving if no main type hierarchy.
		}
		
		if (modelListener != null)
			JavaCore.removeElementChangedListener(modelListener);
	}
	
	/**
	 * Resolve a simple name. This can't have any '.' in it.
	 * 
	 * @param simpleName
	 * @return either resolved name, or <code>null</code> if unresolved.
	 * 
	 * @since 1.0.0
	 */
	protected ResolvedType resolveSimpleType(String simpleName) {
		try {
			ResolvedType resolved = (ResolvedType) namesToResolvedTypes.get(simpleName);
			if (resolved != null)
				return resolved;
			
			if (getMainTypeHierarchy() == null)
				return null;	// We don't have a valid hierarchy. So can only return what we got.

			// Need to go through the algorithm to see what it could be.
			// 1) Is it an inner class of this main type.
			IType innerClass = findType(mainType, simpleName);
			if (innerClass != null)
				return addToResolvedMain(simpleName, innerClass);

			// 2) Is it an inner class of a super type or super interface.
			IType[] lclAllSuperTypes = getAllSuperTypes();
			for (int i = 0; i < lclAllSuperTypes.length; i++) {
				innerClass = findType(lclAllSuperTypes[i], simpleName);
				if (innerClass != null)
					return addToResolvedSuperList(simpleName, innerClass);
			}

			// 3) Is it matching an exact match (i.e. same as last qualifier on import declaration).
			int s = importDecls.size();
			for (int i = 0; i < s; i++) {
				ImportDecl id = (ImportDecl) importDecls.get(i);
				if (!id.isOnDemand()) {
					// This is not a "*" type import. So see if name matches last part. You cannot have a not onDemand that is a package.
					if (id.getLastQualifier().equals(simpleName)) {
						// Create the itype for this. It will be cached so we only need to worry about it once.
						IType resolvedType = javaProject.findType(id.getFullyQualifiedName());
						return resolvedType != null ? addToResolved(id, id.getLastQualifier(), resolvedType) : null;
					}
				}
			}
			
			// 4) Is it in the same package as the maintype.
			IType type = javaProject.findType(mainPackageName, simpleName);
			if (type != null)
				return addToResolved(simpleName, type);
			
			// 5) Is it in an on-demand import.
			for (int i = 0; i < s; i++) {
				ImportDecl id = (ImportDecl) importDecls.get(i);
				if (id.isOnDemand()) {
					if (!id.isResolved())
						id.resolveIt(javaProject);
					// See if it can be found. There will be a resolver if the import is of a type, i.e. import x.y.Z.*;
					IType resolver = id.getResolvedType();
					if (resolver != null) {
						 type = findType(resolver, simpleName);
						 if (type == null)
						 	continue;
					} else {
						// The import decl is probably of a package type, i.e. import x.y.z.*;
						type = javaProject.findType(id.getFullyQualifiedName(), simpleName);
						if (type == null)
							continue;
					}
					return addToResolved(id, simpleName, type);
				}
			}
			
			// Not found
			return null;
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return null;
		}
	}
	

	private IType findType(IType outerType, String innerTypeName) throws JavaModelException {
	IJavaElement[] children = outerType.getChildren();
	for (int i = 0; i < children.length; i++) {
		if (children[i].getElementType() == IJavaElement.TYPE) {
			IType t = (IType) children[i];
			if (t.getElementName().equals(innerTypeName))
				return t;
		}
	}
	return null;
	}
	
	private ResolvedType addToResolved(String simpleName, IType resolvedType) {
		ResolvedType result = new ResolvedType(resolvedType);
		namesToResolvedTypes.put(simpleName, result);
		return result;
	}
	
	private ResolvedType addToResolved(NamesList namesList, IType resolvedType) {
		ResolvedType result = new ResolvedType(resolvedType);
		// Turn the names list into a left to right string (i.e. the true name) so that it can be looked up
		// later. (But also for now just for test purposes we will put both in so that resolve(nameslist) doesn't
		// require major change. See if problem is lookup of already resolved types.
		namesToResolvedTypes.put(new NamesList(namesList), result);
		return result;
	}	
	
	/*
	 * Found from an exact import statement for the type.
	 */
	private ResolvedType addToResolved(ImportDecl importDecl, String simpleName, IType resolvedType) {
		ResolvedType rt = addToResolved(simpleName, resolvedType);
		rt.resolvedThru = importDecl;
		return rt;
	}
	
	/*
	 * Found as inner class of another resolved type.
	 */
	private ResolvedType addToResolved(ResolvedType resolvedFrom, NamesList namesList, IType resolvedType) {
		ResolvedType rt = addToResolved(namesList, resolvedType);
		rt.resolvedThru = resolvedFrom.resolvedThru;
		return rt;
	}
	
	private ResolvedType addToResolvedMain(String simpleName, IType type) {
		ResolvedType rt = addToResolved(simpleName, type);
		rt.resolvedThru = MAIN_TYPE;
		return rt;
	}

	private ResolvedType addToResolvedSuperList(String simpleName, IType type) {
		ResolvedType rt = addToResolved(simpleName, type);
		rt.resolvedThru = SUPER_TYPE;
		return rt;
	}

	
	private final static String[] EMPTY_FIELD_ACCESSORS = new String[0];
	
	/**
	 * This is the field resolved type from a QualifiedName. The split is between the type name part and where field
	 * access starts. It will return the type, and the array of parts of all that is left. 
	 * <p>
	 * For example, if we have "Foo.Bar.f1.f2" we would return the resolved type as "x.y.z.Foo$Bar" and the field part
	 * as "f1.f2". If the entire name was a type, the field part will be null.
	 * <p>
	 * Note: "f1" is not guarenteed to be a field of "x.y.z.Foo$Bar" it is just the part of the name that is not a type.
	 * 
	 * @since 1.0.0
	 */
	public static class FieldResolvedType {
		
		FieldResolvedType(ResolvedType resolvedType, String[] fieldAccessors) {
			this.resolvedType = resolvedType;
			this.fieldAccessors = fieldAccessors;
		}
		
		/**
		 * The resolved type.
		 */
		public final ResolvedType resolvedType;
		
		/**
		 * The array of field accessors left over. They will be in correct order of left to right.
		 * For example: "x.z.A.B.f.g" will have a resolved type of "x.z.A.B" and the field accessor 
		 * array will be {"f", "g"}. It will be zero-length if nothing left.
		 */
		public final String[] fieldAccessors;
	}
	
	/**
	 * Resolve the AST name. This is used to resolve if there is a possibility of a field access involved.
	 * It is assumed that the field access WILL NOT be the first name. That must be checked before coming in
	 * here.
	 * <p>
	 * If this is known to be a type, then use <code>resolveType</code> instead.
	 * @param name
	 * @return the resolvedtype and field part, or <code>null</code> if not resolvable.
	 * 
	 * @see #resolveType(Name)
	 * @since 1.0.0
	 */
	public FieldResolvedType resolveWithPossibleField(Name name) {
		getMainTypeHierarchy();	// Force a refresh if needed.
		
		if (name.isSimpleName()) {
			ResolvedType rt = resolveSimpleType(((SimpleName) name).getIdentifier());
			return rt != null ? new FieldResolvedType(rt, EMPTY_FIELD_ACCESSORS) : null;
		}
		
		ResolvedType rt = (ResolvedType) namesToResolvedTypes.get(name.getFullyQualifiedName());
		if (rt != null) {
			return new FieldResolvedType(rt, EMPTY_FIELD_ACCESSORS);
		}
		

		// Else resolve as complex
		NamesList names = decomposeName(name);
		return resolveWithPossibleField(names);
	}
	
	/**
	 * Resolve the string name. This is used to resolve if there is a possibility of a field access involved.
	 * It is assumed that the field access WILL NOT be the first name. That must be checked before coming in
	 * here.
	 * <p>
	 * If this is known to be a type, then use <code>resolveType</code> instead.
	 * 
	 * @param name
	 * @return the resolvedtype and field part, or <code>null</code> if not resolvable.
	 * 
	 * @see #resolveType(String)
	 * @since 1.0.0
	 */
	public FieldResolvedType resolveWithPossibleField(String name) {
		getMainTypeHierarchy();	// Force a refresh if needed.
		
		if (name.indexOf('.') == -1) {
			ResolvedType rt = resolveSimpleType(name);
			return rt != null ? new FieldResolvedType(rt, EMPTY_FIELD_ACCESSORS) : null;
		}

		ResolvedType rt = (ResolvedType) namesToResolvedTypes.get(name);
		if (rt != null) {
			return new FieldResolvedType(rt, EMPTY_FIELD_ACCESSORS);
		}

		// Else resolve as complex
		NamesList names = decomposeName(name);
		return resolveWithPossibleField(names);
	}	
	
	/*
	 * Common resolve with possible field for use from name or string.
	 */
	private FieldResolvedType resolveWithPossibleField(NamesList names) {
		ResolvedType rt = resolve(names);
		String[] fieldAccessors = EMPTY_FIELD_ACCESSORS;
		if (rt != null && names.getTestSize() != names.size()) {
			// There was some left over in names. These would be the field accessors
			// Need to build in reverse order. Want the fieldAccessors to be left to right.
			fieldAccessors = new String[names.size()-names.getTestSize()];
			int fromIndex = fieldAccessors.length;
			for (int i = 0; i < fieldAccessors.length; i++) {
				fieldAccessors[i] = (String) names.get(--fromIndex);
			}
		}
		return rt != null ? new FieldResolvedType(rt, fieldAccessors) : null;
	}

	/*
	 * This is used so that we can have a quick compare using equals in 
	 * the resolved names list. It has a setting called testSize. This size is 
	 * how far from the right to compare. The equals will be done from the right
	 * to the left. That is because when decomposed, a qualified name will be right to left.
	 * So to compare from the beginning of a name we compare from the right. 
	 * The testsize setting will allow the "size" of the list to be "changed"
	 * without actually removing any of the names. This is useful for the algorithm we
	 * will be using. We actually need to go through the list twice so we don't want to
	 * loose any of it and have to reconstruct it.
	 * <p>
	 * This will be what is used both as a key in the resolved names list and
	 * as the decomposed name.
	 * 
	 * @since 1.0.0
	 */
	private static class NamesList extends ArrayList {
		
		/**
		 * Comment for <code>serialVersionUID</code>
		 * 
		 * @since 1.1.0
		 */
		private static final long serialVersionUID = -7972116865333630531L;
		int testSize;
		
		public NamesList(int initialCapacity) {
			super(initialCapacity);
		}
		
		public NamesList(NamesList c) {
			super(c.getTestSize());
			int size = c.size();
			for(int index=size-c.getTestSize(); index<size; index++)
				add(c.get(index));
			setTestSize(c.getTestSize());
		}
		
		/* (non-Javadoc)
		 * @see java.util.AbstractList#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof NamesList))
				return super.equals(o);
			NamesList other = (NamesList) o;
			if (testSize != other.testSize)
				return false;
			
			// We can be assured because this is our list that we will not have any null entries.
			int thisIndex = size();
			int thisStop = thisIndex-testSize;
			int otherIndex = other.size();
			if (thisStop < 0 || other.testSize > otherIndex)
				return false;	// Some testSize set error.
			while(--thisIndex >= thisStop) {
				if (!this.get(thisIndex).equals(other.get(--otherIndex)))
					return false;
			}
			return true;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			int hashCode = 1;
			int testIndex = size();
			int testStop = testIndex-testSize;
			if (testStop < 0)
				testStop = 0;
		
		   	while (--testIndex >= testStop) {
			    hashCode = 31*hashCode + get(testIndex).hashCode();	// We know it will never be null because of the tight control of it.
			}
			return hashCode;
		}
		
		public int getTestSize() {
			return this.testSize;
		}
		
		public void setTestSize(int testSize) {
			this.testSize = testSize;
		}
	}
	
	/*
	 * Decompose the name into a list of strings. It is backwards in that the most left will be last
	 * and the most right will be first.
	 */
	private NamesList decomposeName(Name name) {
		NamesList names = new NamesList(3);
		return decomposeName(name, names);
	}
	
	/*
	 * Decompose into the given names list.
	 */
	private NamesList decomposeName(Name name, NamesList names) {
		while (true) {
			if (name.isSimpleName()) {
				names.add(((SimpleName) name).getIdentifier());
				break;
			} else {
				QualifiedName qn = (QualifiedName) name;
				names.add(qn.getName().getIdentifier());
				name = qn.getQualifier(); 
			}
		}
		names.setTestSize(names.size());
		return names;
	}	
	
	/*
	 * Decompose the string into a list of strings. It is backwards in that the most left will be last
	 * and the most right will be first.
	 */
	private NamesList decomposeName(String name) {
		StringTokenizer st = new StringTokenizer(name, "."); //$NON-NLS-1$
		NamesList names = new NamesList(st.countTokens());
		while (st.hasMoreTokens()) {
			names.add(0, st.nextToken());
		}
		names.setTestSize(names.size());
		return names;
	}
	
	/*
	 * Decompose the QualifiedType into a list of strings. It is backwards in that the most left will be last
	 * and the most right will be first.
	 */
	private NamesList decomposeType(QualifiedType qtype) {
		NamesList names = new NamesList(3);
		Type type = qtype;
		while (true) {
			if (type.isSimpleType()) {
				decomposeName(((SimpleType) type).getName(), names);
				break;
			} else if (type.isQualifiedType()){
				QualifiedType qt = (QualifiedType) type;
				names.add(qt.getName().getIdentifier());
				type = qt.getQualifier(); 
			}
		}
		names.setTestSize(names.size());
		return names;
	}	
	
	/**
	 * This is used when the name is known to be a type. If it could be field access then
	 * use <code>resolveWithPossibleField(Name)</code> instead. If it doesn't match resolve entirely, ie. it
	 * doesn't have a part that couldn't be resolved, it will return <code>null</code>.
	 * 
	 * @param name
	 * @return the resolved type or <code>null</code> if not resolvable.
	 * 
	 * @see #resolveWithPossibleField(Name)
	 * @since 1.0.0
	 */
	public ResolvedType resolveType(Name name) {
		getMainTypeHierarchy();	// Force a refresh if needed.
		
		if (name.isSimpleName()) { return resolveSimpleType(((SimpleName) name).getIdentifier()); }

		ResolvedType rt = (ResolvedType) namesToResolvedTypes.get(name.getFullyQualifiedName());
		if (rt != null) {
			return rt;
		}
		
		// Else resolve as complex
		NamesList names = decomposeName(name);
		return resolveType(names);
	}
	
	/**
	 * Resolve the type from the given AST Type.
	 * 
	 * @param type
	 * @return the resolved type or resolved array type or <code>null</code> if not resolvable.
	 * 
	 * @see ResolvedType
	 * @see ResolvedArrayType
	 * @since 1.0.0
	 */
	public Resolved resolveType(Type type) {
		getMainTypeHierarchy();	// Force a refresh if needed.
		
		if (type.isSimpleType())
			return resolveType(((SimpleType) type).getName());
		else if (type.isPrimitiveType())
			return resolveSimpleType(((PrimitiveType) type).getPrimitiveTypeCode().toString());
		else if (type.isArrayType()) {
			ArrayType at = (ArrayType) type;
			// Resolve the final type. We know it won't be an array type, so we won't recurse.
			// Then append "dims" number of "[]" to make a formal name.
			// Note: We know it must resolve to a ResolvedType because element type is never an array.
			ResolvedType finalType = (ResolvedType) resolveType(at.getElementType());
			if (finalType == null)
				return null;
			StringBuffer st = new StringBuffer(finalType.getName());
			int dims = at.getDimensions();
			int rdims = dims;
			while (dims-- > 0) {
				st.append("[]"); //$NON-NLS-1$
			}
			return new ResolvedArrayType(finalType, rdims, st.toString());
		} else if (type.isQualifiedType()) {
			// A qualified type means the qualifier is a type, while the name is an inner class of that type.
			// You would think it would be easier to take the qualifier and resolve it and then take the simple
			// name and resolve it as an inner class of that type. But we would like to do a lookup of already resolved
			// so as not to go through the resolve again, but this requires us to recurse through the qualifier and add name and
			// form a nameslist to see if already resolved, but if you have gone that far, just send the nameslist into
			// resolve and let it handle it entirely. Also, to resolve the qualifier would require recursion to form a 
			// names list too. So just do it once.
			return resolveType(decomposeType((QualifiedType) type));
		} else
			return null;
	}
	
	/**
	 * This is used when the string is known to be a type. If it could be field access then use <code>resolveWithPossibleField(String)</code>
	 * instead. If it doesn't match resolve entirely, ie. it doesn't have a part that couldn't be resolved, it will return <code>null</code>.
	 * 
	 * @param name
	 * @return the resolved type or <code>null</code> if not resolvable.
	 * 
	 * @see #resolveWithPossibleField(String)
	 * @since 1.0.0
	 */
	public ResolvedType resolveType(String name) {
		getMainTypeHierarchy();	// Force a refresh if needed.
		
		if (name.indexOf('.') == -1) {
			return resolveSimpleType(name);
		}

		ResolvedType rt = (ResolvedType) namesToResolvedTypes.get(name);
		if (rt != null) {
			return rt;
		}
		
		// Else resolve as complex
		NamesList names = decomposeName(name);
		return resolveType(names);
	}	
	
	/*
	 * Common resolveType for Name or string.
	 */
	private ResolvedType resolveType(NamesList names) {
		ResolvedType rt = resolve(names);
		if (rt != null && names.getTestSize() != names.size())
			return null;	// There was some left over in names. This is wrong for resolve type.
		return rt;
	}

	/*
	 * We need to recurse all of the way to the left. Unfortunately the QualifiedName tree structure
	 * is inconvienently backwards. We want to go from left-to-right, but tree is right-to-left.
	 * We would not of gotton here if there was only one name. We would of process that separately first.
	 * <p>
	 * When we leave we will return the ResolvedType and names' testsize will be set to the size to cover that
	 * resolved type. Any left over in names will be fields or something else. But they are not inner classes.
	 * If the return is null, then it couldn't be found and names should be ignored.
	 */
	private ResolvedType resolve(NamesList names) {
		try {
			// First see, starting from the right to the left, if the names have been resolved.
			// For example for "x.y.z.A.B" we will see if "x.y.z.A.B" was already resolved, if so we
			// found it. Then check "x.y.z.A", if that is found then we found the first part. Then we
			// need to see if "B" is inner to "A".
			int namesSize = names.size();
			int tSize = namesSize;
			names.setTestSize(tSize);
			ResolvedType rt = (ResolvedType) namesToResolvedTypes.get(names);
			if (rt != null) {
				// We found it on the full size. No need to go further.
				return rt;
			}
			
			// Else find largest match. But we will stop when we tested only two names. That is because one name would be stored as
			// a string instead of a nameslist. wW will test for that one name in the next section.
			while (rt == null && --tSize > 1) {
				names.setTestSize(tSize);
				rt = (ResolvedType) namesToResolvedTypes.get(names);
			}
			
			if (rt == null) {
				// No match, so start building up from first one on left. First we see if the first one is a simple type.
				// If so, we found it. Else after that we treat the first as a package fragment and the next as a type.
				// We keep building up until we find a type.
				int namesIndex = namesSize-1;
				names.setTestSize(1);	// Matching first one.
				rt = resolveSimpleType((String) names.get(namesIndex));
				if (rt == null) {
					// First one not a simple type, so start building up package name and type.
					StringBuffer pkgBuild = new StringBuffer();
					// We can go down to index 1, because then we will have one more left for type. If we went to index 0 then
					// there would be no type left to search for.
					int namesTestSize = 1;	// Starting test size because we already tested the first one.
					while (namesIndex>=1) {
						pkgBuild.append((String) names.get(namesIndex));
						IType type = javaProject.findType(pkgBuild.toString(), (String) names.get(--namesIndex));
						++namesTestSize;
						if (type != null) {
							// We found the first type, cache this so that we don't need to search again if this is found.
							names.setTestSize(namesTestSize);	// include previous pkg portion plus new type.
							rt = addToResolved(names, type);	// It didn't come from a import decl or any existing resolved type in this case, so we don't add through one.
							break;	// We found it, so from here we look within the IType.
						}
						pkgBuild.append('.');	// Add the separator for ext package fragment.
					}
					
					if (rt == null) 
						return null;	// We went all of the way and never found a type.
				}				
			}
			
			// If we got here we found the minimum type and have cached it, or it was cached. The nameslist testsize will be
			// set to this length. Now from here we see how far we can go and find inner classes. We will do this by increasing
			// the testsize one at a time until we don't find a match. When we are done, testsize in names will be the max match.
			int namesTestSize = names.getTestSize(); 
			for (int namesIndex = namesSize-namesTestSize-1; namesIndex>=0; --namesIndex) {
				String typeName = (String) names.get(namesIndex);
				IType type = findType(rt.type, typeName);
				if (type != null) {
					// We found this as type, add to the cache, but go on. Tell it what import decl it came from.
					names.setTestSize(++namesTestSize);
					rt = addToResolved(rt, names, type);
				} else {
					// That's it, that is as far as we can go. names has the correct test size for what was found.
					break;
				}
			}

			return rt;
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.WARNING);
			return null;
		}	
	}

	/**
	 * Get the resolved main. 
	 * @return the resolved main. This will never be <code>null</code>.
	 * 
	 * @since 1.0.0
	 */
	public ResolvedType resolveMain() {
		return resolvedMain;
	}
	/**
	 * @return Returns the allSuperTypes.
	 */
	protected IType[] getAllSuperTypes() {
		getMainTypeHierarchy();	// This forces the wait for it to complete so that we get a new set of supertypes if necessary.
		return allSuperTypes;
	}

	
	public IType getMainType() {
		return mainType;
	}
	
}
 
