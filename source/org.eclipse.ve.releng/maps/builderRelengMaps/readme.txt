What do the files in this directory mean?

baseTag: Contains one line. The content of the line is the CVS Tag for the org.eclipse.releng.basebuilder project to download for building.

basePDE.properties: A properties file. It contains the pde build scripts directory out of org.eclipse.releng.basebuilder to use. It needs to be
changed when a new basebuilder is used, and that new basebuilder is stepping up to a new version of pde scripts, such as going from 3.0.0 to 3.1.0.

veTag: Contains one line. The content of the line is the CVS Tag for the org.eclipse.ve.releng.builder project to download for building.

