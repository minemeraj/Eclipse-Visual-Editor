<?php  																														
require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/app.class.php");	
require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/nav.class.php"); 	
require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/menu.class.php"); 	
$App = new App(); $Nav = new Nav(); $Menu = new Menu(); include($App->getProjectCommon()); 	
	#
	# Begin: page-specific settings.  Change these. 
	$pageTitle 		= "Visual Editor Project";
	$pageKeywords	= "visual, editor, java, eclipse, graphical, ui, swing, swt, jfc, rcp";
	$pageAuthor		= "Nick Boldt";
	
	# Paste your HTML content between the EOHTML markers!	
	$html = <<<EOHTML

	<div id="midcolumn">

	    <table border="0" cellpadding="5">
	      <tr>
	        <td valign="top">

	        <h1>$pageTitle</h1>
		<h3>First-class GUI building tools for Eclipse</h3>

		<p>&nbsp;</p>
		<div class="homeitem3col">
			<p>The Eclipse Visual Editor project is a vendor-neutral, open development platform supplying frameworks for 
			creating GUI builders, and exemplary, extensible tool implementations for Swing/JFC and SWT/RCP. 
			These tools are exemplary in that they verify the utility of the Eclipse Visual Editor frameworks, 
			illustrate the appropriate use of those frameworks, and support the development and maintenance of the 
			Eclipse Visual Editor Platform itself.</p> 
			
			<p>The purpose of the Eclipse Visual Editor Project is to advance the creation, evolution, promotion of the Eclipse Visual Editor platform, 
			and to cultivate both an open source community and an ecosystem of complementary products, capabilities, and services. In particular, the 
			Visual Editor Project intends to be useful for creating GUI builders for other languages such as C/C++ and alternate widget sets, 
			including those that are not supported under Java. </p>

	          <table border="0" cellpadding="5">

	            <!-- Downloads -->
	            <tr>
	              <td width=20></td>
	              <td><a href="downloads/"><img border=0 src="images/downloads.gif"></a></td>
	              <td>
	                <table border="0" cellpadding="0">
	                  <tr>
	                    <td><font size=+1><a href="downloads/">Downloads</a></font></td>
	                  </tr>
	                  <tr>
	                    <td>Get our latest <b>1.4.0</b> nightly build!</td>
	                  </tr>
	                </table>
	              </td>
	            </tr>

	            <!-- Get Involved -->
	            <tr>
	              <td width=20></td>
	              <td><a href="http://wiki.eclipse.org/VE"><img border=0 src="images/community.jpg"></a></td>
	              <td>
	                <table border="0" cellpadding="0">
	                  <tr>
	                    <td><font size=+1><a href="http://wiki.eclipse.org/VE">Get Involved</a></font></td>
	                  </tr>
	                  <tr>
	                    <td>Find out how you can get involved with the project</td>
	                  </tr>
	                </table>
	              </td>
	            </tr>

	            <!-- Plans - should be http://www.eclipse.org/projects/project-plan.php?projectid=tools.ve -->
	            <tr>
	              <td width=20></td>
	              <td><a href="http://wiki.eclipse.org/VE"><img border=0 src="images/reference.png"></a></td>
	              <td>
	                <table border="0" cellpadding="0">
	                  <tr>
	                    <td><font size=+1><a href="http://wiki.eclipse.org/VE">Plans</a></font></td>
	                  </tr>
	                  <tr>
	                    <td>See the project plans</td>
	                  </tr>
	                </table>
	              </td>
	            </tr>

	          </table>
	        </td>
	      </tr>
	    </table>
	    <p align="right"><i><a href="http://eclipse.org/vep/WebContent/main.php">Old VE Homepage</a></i></p>
	</div>
           
	<div id="rightcolumn">
		<div class="sideitem">
			<h6>VEP Components</h6>
			<ul>
				<li><a href="WebContent/docs/components/cde/cde.html"> CDE </a></li>
				<li><a href="WebContent/docs/components/core/core.html"> Core </a></li>
				<li><a href="WebContent/docs/components/jfc/jfc.html"> JFC/Swing</a> </li>
				<li><a href="WebContent/docs/components/javamodel/javaModel.html"> Java Model</a></li>
				<li><a href="WebContent/docs/components/swt/swt.html">SWT</a></li>
			</ul>
		</div>

		<div class="sideitem">
			<h6>Related links</h6>
			<ul>
				<li><a href="http://www.eclipse.org/galileo">Eclipse 3.5 Galileo Release</a></li>
				<li><a href="http://www.eclipse.org/gef/">Graphical Editing Framework (GEF)</a></li>
				<li><a href="http://www.eclipse.org/modeling/emf/">Eclipse Modeling Framework (EMF)</a> </li>
				<li><a href="http://wiki.eclipse.org/index.php/JFace_Data_Binding ">JFace Data Binding</a></li>				
			</ul>
		</div>
	</div>
	
	<!--
	<div id="rightcolumn">
		<div class="sideitem">
			<h6>News</h6>
			<ul>
				<li><a href="?">news</a></li> 
			</ul>
		</div>
	</div>
	-->

EOHTML;


	# Generate the web page
	$App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html);
?>
