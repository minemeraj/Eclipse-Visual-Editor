/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaObjectEmiter.java,v $
 *  $Revision: 1.4 $  $Date: 2004-08-27 15:34:10 $ 
 */
package org.eclipse.ve.internal.java.vce.templates;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author Gili Mendel
 * 
 *  This class attemps to load the latest Object that is associated with a given class name.
 *  If one is not available, and source code is available, it will compile the source code and 
 *  load the new object.
 * 
 *  This class initializes lazily..  It will not handle concecutive calls with a different
 *  class loader.  It is expected that this class will be cached, and reused for a given class
 *  and class loader.
 *  
 */
public class JavaObjectEmiter {

	//	String					fEncodeing = System.getProperty("file.encoding", "Cp1252" );
	String 	fEncodeing = System.getProperty("Cp1252"); //$NON-NLS-1$
	char[]	fSrc = null;
	String 	fClassName = null;
	String 	fClassPackage = null;
	IPath 	fDestinationPath = null;
	Class	ftheClass = null;
	Class   fsecondBestClass = null ;
	ClassLoader fClassLoader = null ;
	long	fSrcTimeStamp = -1 ;
	int   	index = 0 ;

	/**
	 * Utility Class to support the compiler
	 */
	protected class EmiterCompilerRequestor implements ICompilerRequestor {
		protected IProblem[] fProblems = new IProblem[0];
		protected int ErrCount = 0, WarnCount = 0;

		public IProblem[] getProblems() {
			return fProblems;
		}

		public void createNewProblem(char[] originatingFileName, String message, int id, String[] stringArguments, int severity, int startPosition, int endPosition, int line) {
			IProblem newProblem = new DefaultProblem(originatingFileName, message, id, stringArguments, severity, startPosition, endPosition, line);
			if (fProblems == null) {
				fProblems = new IProblem[1];
				fProblems[0] = newProblem;
			}
			else {
				IProblem[] temp = new IProblem[fProblems.length + 1];
				System.arraycopy(fProblems, 0, temp, 0, fProblems.length);
				temp[fProblems.length] = newProblem;
				fProblems = temp;
			}
			if (fProblems[fProblems.length - 1].isError())
				ErrCount++;
			else
				WarnCount++;

		}
		public void createNewProblem(char[] originatingFileName, String message) {
			createNewProblem(originatingFileName, message, 0, null, 0, 0, 0, 0);
		}
		public void acceptResult(CompilationResult compilationResult) {
			try {
				if (compilationResult.hasErrors()) {
					StringBuffer problem = new StringBuffer();
					String newLine = System.getProperties().getProperty("line.separator"); //$NON-NLS-1$
					problem.append(newLine);
					fProblems = compilationResult.getProblems();
					for (int i = 0; i < fProblems.length; i++) {
						if (fProblems[i].isError())
							ErrCount++;
						else
							WarnCount++;
					}
				}
				else {
					ClassFile[] compiledFiles = compilationResult.getClassFiles();
					ClassFile classFile = compiledFiles[0];

					// Before writing out the class file, compare it to the previous file					
					String classfilename = new String(classFile.fileName()); // the qualified type name "p1/p2/A"
					IPath classfilepath = new Path(classfilename);
					IPath tempDir = fDestinationPath ;

					// create the directories first
					IPath filepath = tempDir.append(classfilepath);
					IPath directory = filepath.removeLastSegments(1);
					File actualDir = directory.toFile();

					if (actualDir.exists() || actualDir.mkdirs()) {
						File actualfile = tempDir.append(classfilepath).addFileExtension("class").toFile(); //$NON-NLS-1$
						if (actualfile.exists())
							actualfile.delete();

						byte[] bytes = classFile.getBytes();
						FileOutputStream output = new FileOutputStream(actualfile);
						output.write(bytes);
						output.close();
					}
					else
						createNewProblem(directory.toOSString().toCharArray(), VCETemplatesMessages.getString("JavaObjectEmiter.Problem.DirectoryDoesntExist_ERROR_"), 0, null, org.eclipse.jdt.internal.compiler.problem.ProblemSeverities.Error, 0, 0, 0); //$NON-NLS-1$

				}
			}
			catch (SecurityException se) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(se);
				createNewProblem(getClassName().toCharArray(), VCETemplatesMessages.getString("JavaObjectEmiter.Problem.Security_EXC_") + se.getMessage(), 0, null, org.eclipse.jdt.internal.compiler.problem.ProblemSeverities.Error, 0, 0, 0); //$NON-NLS-1$
			}
			catch (java.io.IOException ioe) {
				org.eclipse.ve.internal.java.core.JavaVEPlugin.log(ioe);
				createNewProblem(getClassName().toCharArray(), VCETemplatesMessages.getString("JavaObjectEmiter.Problem.IO_EXC_") + ioe.getMessage(), 0, null, org.eclipse.jdt.internal.compiler.problem.ProblemSeverities.Error, 0, 0, 0); //$NON-NLS-1$
			}
		}
		/**
		 * Returns the errCount.
		 * @return int
		 */
		public int getErrCount() {
			return ErrCount;
		}

	}

	/**
	 * Utility class to support the compiler.
	 */
	protected class EmitterCompilationUnit implements org.eclipse.jdt.internal.compiler.env.ICompilationUnit {
		/**
		* @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getContents()
		*/
		public char[] getContents() {
			return fSrc;
		}

		/**
		 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getMainTypeName()
		 */
		public char[] getMainTypeName() {
			return getClassName().toCharArray();
		}

		/**
		 * @see org.eclipse.jdt.internal.compiler.env.ICompilationUnit#getPackageName()
		 */
		public char[][] getPackageName() {
			return CharOperation.splitOn('.', fClassPackage.toCharArray());
		}

		/**
		 * @see org.eclipse.jdt.internal.compiler.env.IDependent#getFileName()
		 */
		public char[] getFileName() {
			IPath fileName = fDestinationPath.append(fClassName) ;
			return fileName.toOSString().toCharArray();
		}

	}

	public JavaObjectEmiter(char[] Src, String className, String pkgName, IPath destinationPath, long srcTimeStamp) {
		fSrc = Src;
		fClassName = className;
		fClassPackage = pkgName;
		fDestinationPath = destinationPath;
		fSrcTimeStamp = srcTimeStamp ;
	}
	
	public JavaObjectEmiter(String className, String pkgName, IPath destinationPath, long srcTimeStamp) {
		this(null,className,pkgName,destinationPath,srcTimeStamp) ;
	}

	protected void tick(int i, IProgressMonitor pm) {
		if (pm != null)
			pm.worked(i);
	}
	protected void tick(IProgressMonitor pm) {
		tick(1, pm);
	}
	
	public String getClassName() {
		if (index == 0) return fClassName ;
		else return fClassName+Integer.toString(index) ;
	}

	/**
	 * @return package/ClassName
	 */
	public String getFullClassName() {
		return fClassPackage.length() == 0 ? getClassName() : fClassPackage + "." + getClassName() ; //$NON-NLS-1$
	}
	
	

    /**
     * Create a new class loader that includes our destination path
     */
    protected ClassLoader getClassLoader(ClassLoader cl) throws TemplatesException {
    	
    	if (fClassLoader != null) return fClassLoader ;
    	
    	URL[] destUrl = null;
		try {
			destUrl = new URL[] { fDestinationPath.toFile().toURL() };
		}
		catch (MalformedURLException e) {
			throw new TemplatesException(e);
		}    	
		fClassLoader = java.net.URLClassLoader.newInstance(destUrl, cl);		
		return fClassLoader ;
    }    
       
    
    protected Class getExistingClass (ClassLoader cl) {
    	
    	if (ftheClass != null) return ftheClass ;
		do {
			try {
				Class clazz = cl.loadClass(getFullClassName());
				// Remember a fallback class, just in case
				fsecondBestClass = clazz ;
				long ts = TemplateUtil.getTimeStamp(clazz) ;
				if (ts >= fSrcTimeStamp) {					
					ftheClass = clazz ;
					return clazz ;				
				}
				else {
					JavaVEPlugin.log("JavaObjectEmitter: Did not found found existing class: "+clazz.getName(),Level.FINEST) ; //$NON-NLS-1$
				}
				index++ ;
			}
			catch (ClassNotFoundException e) {
				return null;
			}
		}
		while (true);	
    	
    }
    /**
	 * @return Class if it could be found with no compile
	 */
	public Class getExistingClass(ClassLoader cl, IProgressMonitor pm) throws TemplatesException {
		if (ftheClass != null)
			return ftheClass;

		File dest = fDestinationPath.toFile();
		if (!dest.exists())
			dest.mkdir();
			
		ClassLoader urlCl = getClassLoader(cl) ;
		// May not need to compile, the latest/greated class
		// can already be there
		ftheClass = getExistingClass(urlCl) ;	
		return ftheClass ;
	}
    
    /**
     * When a class' name has changed (index has changed), we need
     * to change the source to reflect this
     */
    public void refreshSourceClassname() throws TemplatesException {
    	if (fSrc == null) return ;
    	IScanner scanner = org.eclipse.jdt.core.ToolFactory.createScanner(false,false,false,false) ;
    	scanner.setSource(fSrc) ;
    	try {
			int token = scanner.getNextToken() ;
			// Look for the class keyword
			while (token != ITerminalSymbols.TokenNameclass && token != ITerminalSymbols.TokenNameEOF)
			   token = scanner.getNextToken() ;
			// Look for the classes name 
			token = scanner.getNextToken() ;
			while (token != ITerminalSymbols.TokenNameIdentifier && token != ITerminalSymbols.TokenNameEOF)
			   token = scanner.getNextToken() ;
			if (token == ITerminalSymbols.TokenNameEOF) throw new TemplatesException("Could not find class's name") ; //$NON-NLS-1$
			
			// Replace the class Name
			int left = scanner.getCurrentTokenStartPosition() ;
			int right = scanner.getCurrentTokenEndPosition()+1 ;
			
			StringBuffer buf = new StringBuffer() ;
			buf.append(scanner.getSource()) ;
			buf.replace(left,right,getClassName()) ;
			fSrc = buf.toString().toCharArray() ;
		}
		catch (InvalidInputException e) {
			throw new TemplatesException (e.getMessage()) ;
		}
    	
    }
    
	/**
	 * getClass() will try to load the class first.
	 * (one that has a timesamp>fSrcTimestamp)
	 * If one was not found, and the source exists, it will compile and load
	 * the new class
	 */
	public Class getClass(String[] classPath, ClassLoader cl, IProgressMonitor pm) throws TemplatesException {
		if (ftheClass != null)
			return ftheClass;

		if (getExistingClass(cl,pm) != null) 
		   return ftheClass ;
		   
		// Could not find a good class  
	    if (fSrc == null) return null ;	
	    	    
	    if (index>0) refreshSourceClassname() ;	   	    
		   
        // Compile it
        JavaVEPlugin.log("JavaObjectEmitter: Compiling a new class: "+getClassName(),Level.FINEST) ; //$NON-NLS-1$
		EmitterCompilationUnit[] cu = new EmitterCompilationUnit[] { new EmitterCompilationUnit()};
		INameEnvironment env = new FileSystem(classPath, new String[0], fEncodeing);
		tick(pm);
		IErrorHandlingPolicy errorPolicy = DefaultErrorHandlingPolicies.exitOnFirstError();
		IProblemFactory problemFactory = org.eclipse.jdt.internal.core.builder.ProblemFactory.getProblemFactory(java.util.Locale.getDefault());
		EmiterCompilerRequestor requestor = new EmiterCompilerRequestor();
		org.eclipse.jdt.internal.compiler.Compiler cmp = new org.eclipse.jdt.internal.compiler.Compiler(env, errorPolicy, JavaCore.getOptions(), requestor, problemFactory);
		tick(pm);

		cmp.compile(cu);
		if (requestor.getErrCount() > 0)
			throw new TemplatesException("SyntaxError: " + requestor.getProblems()[0].getMessage()); //$NON-NLS-1$
		tick(pm);
		env.cleanup();
		// Load it
		try {
			fClassLoader = null ; // force a new URL class loader so that the "new" .class is already there
			ftheClass = getClassLoader(cl).loadClass(getFullClassName());
			tick(pm);
		}
		catch (Exception e) {
			throw new TemplatesException(e);
		}
		return ftheClass;
	}
	/**
	 * Will create a new Object that is associated with this Emitter's class name.
	 * If a class is not available, or a class could not be compiled From the sorce associated 
	 * with this class, an exception will be thrown.
	 * 
	 * @param classPath is the compile class path, in the case that the source needs to be compiled
	 * @param classloader to use to try and load an existing class
	 * @param pm optional progress monitor
	 */
	public Object generateObject(String[] classPath, ClassLoader cl, IProgressMonitor pm) throws TemplatesException {
		Object newInstance=null;
		try {
			newInstance = getClass(classPath, cl, pm).newInstance();
			tick(pm) ;
		}
		catch (TemplatesException e) {
			throw e ;
		}
		catch (Exception ex) {
			throw new TemplatesException(ex);
		}
		return newInstance;
	}
	/**
	 * If the class is loadable, and its time stamp is valid, a new Object will be created
	 */
	public Object generateObjectFromExisting(ClassLoader cl, IProgressMonitor pm) throws TemplatesException {
		Object newInstance;
		try {
			Class clazz = getExistingClass(getClassLoader(cl)); 
			newInstance = (clazz == null) ? null : clazz.newInstance() ;
			tick(pm) ;
		}
		catch (Exception e) {
			throw new TemplatesException(e);
		}
		return newInstance;
	}
	/**
	 * Returns the encodeing.
	 * @return String
	 */
	public String getEncodeing() {
		return fEncodeing;
	}

	/**
	 * Sets the encodeing.
	 * @param encodeing The encodeing to set
	 */
	public void setEncodeing(String encodeing) {
		fEncodeing = encodeing;
	}

	/**
	 * Returns the secondBestClass
	 * @return a class the matches the class name, but whose time stamp
	 *          is older from the required time stamp
	 */
	public Class getSecondBestClass() {
		return fsecondBestClass;
	}

	/**
	 * The reason for this API, is for users to be able to try and load existing
	 * class, without the need to "generate" the source, unless have to do so.
	 * @param src The src to set
	 */
	public void setSrc(char[] src) throws TemplatesException {
		if (fSrc != null) throw new TemplatesException ("Can not overide existing source") ; //$NON-NLS-1$
		fSrc = src;
	}
	public String toString() {
		return "JavaObjectEmitter("+getClassName()+","+fSrcTimeStamp+")" ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Returns the srcTimeStamp.
	 * @return long
	 */
	public long getSrcTimeStamp() {
		return fSrcTimeStamp;
	}

}
