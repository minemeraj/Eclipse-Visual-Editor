<?php  																														require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/app.class.php");	require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/nav.class.php"); 	require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/menu.class.php"); 	$App 	= new App();	$Nav	= new Nav();	$Menu 	= new Menu();		include($App->getProjectCommon());    # All on the same line to unclutter the user's desktop'

	#*****************************************************************************
	#
	# template.php
	#
	# Author: 		Denis Roy
	# Date:			2005-06-16
	#
	# Description: Type your page comments here - these are not sent to the browser
	#
	#
	#****************************************************************************
	
	#
	# Begin: page-specific settings.  Change these. 
	$pageTitle 		= "Visual Editor Project";
	$pageKeywords	= "ve, vep, visual, editor, gui, builder, wysiwyg, eclipse, gui builder, GUI building, tool";
	$pageAuthor		= "Visual Editor Commiters";
	
	
	 $Nav->addCustomNav("VE Newsgroup", "http://www.eclipse.org/newsportal/thread.php?group=eclipse.tools.ve", "_self", 2);
	 $Nav->addCustomNav("VE Test Pass runs", "docs/testpass.html", "_self", 2);
	
	# End: page-specific settings
	#
		
	# Paste your HTML content between the EOHTML markers!	
	$html = <<<EOHTML

<div id="maincontent">
	<div id="midcolumn">

<table style="width: 100%;" border="0" cellpadding="2">
  <tbody>
    <tr>
      <td align="left" width="60%"><h1>$pageTitle
	    <br>
        <font size="1" color="#8080FF">First-class GUI building tools for Eclipse</font>
        </h1>
      </td>
      <td style="width: 40%; text-align: right;"><img src="images/vep.png"
 title="" alt="Visual Editor Project Logo"> <br>
      </td>
    </tr>
  </tbody>
</table>

	
		<h2>About Us</h2>
		<p>The Eclipse Visual Editor project is a vendor-neutral, open
		development platform supplying frameworks for creating GUI builders,
		and exemplary, extensible tool implementations for Swing/JFC and SWT/RCP.
		These tools are exemplary in that they verify the utility of the
		Eclipse Visual Editor frameworks, illustrate the appropriate use of
		those frameworks, and support the development and maintenance of the
		Eclipse Visual Editor Platform itself.</p>

		<p>The purpose of the Eclipse Visual Editor Project is to advance the
		creation, evolution, promotion of the Eclipse Visual Editor platform,
		and to cultivate both an open source community and an ecosystem of
		complementary products, capabilities, and services.  In particular,
		the Visual Editor Project intends to be useful for creating GUI
		builders for other languages such as C/C++ and alternate widget sets,
		including those that are not supported under Java.
		</p>
		
	
		<div class="homeitem3col">
			<h3>What's New</h3>
			<ul>
				<li><img src="http://www.eclipse.org/images/new.gif" height="14" width="31"><a href="http://download.eclipse.org/tools/ve/downloads/drops/S-1.2M1-200512210749/index.html">VE v1.2M1 released</a> <span class="dates">12/16/05</span></li>
				<li><a href="docs/v1.2/plan/vep-plan-1.2.html">VE 1.2 plan posted</a> <span class="dates">11/08/05</span></li>
				<li><a href="http://download.eclipse.org/tools/ve/downloads/drops/R-1.1.0.1-200509071822/index.html">VE v1.1.0.1 released</a> <span class="dates">09/07/05</span></li>
				<li><a href="http://download.eclipse.org/tools/ve/downloads/drops/R-1.1-200507221721/index.html">VE v1.1 released</a> <span class="dates">07/22/05</span></li>
				<li><a href="docs/v1.1/VE11grid.html">VE v1.1 Grid support</a> <span class="dates">07/13/05</span></li>
				<li><a href="docs/v1.1/VE11M2.html">VE v1.1 M2 overview</a> <span class="dates">06/10/05</span></li>
				<li><a href="http://eclipse.org/articles/Article-VE-Custom-Widget/customwidget.html">Extending the Visual Editor Tutorial: Enabling support for a custom widget</a> <span class="dates">06/15/05</span></li>
				<li><a href="docs/newsArchive/main.html">News Archive...</a></li>
			</ul>			
		</div>
		
		<div class="homeitem3col">
			<h3>Developer Resources:</h3>
			
<table style="border-collapse: collapse;" border="0"
 bordercolor="#111111" cellpadding="3" cellspacing="0" width="100%">
  <tbody>    
    <tr>
      <td rowspan="2" width="48"><a href="http://download.eclipse.org/tools/ve/downloads/index.php"><img src="images/download.gif" border="0"></a></td>
      <td colspan="2"><a href="http://download.eclipse.org/tools/ve/downloads/index.php">Download Page </a>, <a href="docs/translations/translation.html">Translations</a>,
      <A href="http://archive.eclipse.org/tools/ve/downloads/">Archives	Download Page</A></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>Get the packages from the <a href="http://download.eclipse.org/tools/ve/downloads/index.php">download page</a> and the translations from the <a href="docs/translations/translation.html">translations page</a>.
        <br>
      </td>
    </tr>
    <tr>
      <td rowspan="2"><a href="http://bugs.eclipse.org/bugs"> <img src="images/bugzilla.gif" border="0"> </a></td>
      <td colspan="2" bgcolor="#ffffbb"><a href="http://bugs.eclipse.org/bugs"> Bugzilla</a></td>
    </tr>
    <tr>
      <td bgcolor="#ffffbb">&nbsp;</td>
      <td bgcolor="#ffffbb">Find/report bugs and feature requests <br>
      <a href="http://dev.eclipse.org/bugs/buglist.cgi?bug_status=NEW&amp;bug_status=ASSIGNED&amp;bug_status=REOPENED&amp;email1=&amp;emailtype1=substring&amp;emailassigned_to1=1&amp;email2=&amp;emailtype2=substring&amp;emailreporter2=1&amp;bugidtype=include&amp;bug_id=&amp;changedin=&amp;votes=&amp;chfieldfrom=&amp;chfieldto=Now&amp;chfieldvalue=&amp;product=VE&amp;short_desc=&amp;short_desc_type=allwordssubstr&amp;long_desc=&amp;long_desc_type=allwordssubstr&amp;keywords=&amp;keywords_type=anywords&amp;field0-0-0=noop&amp;type0-0-0=noop&amp;value0-0-0=&amp;cmdtype=doit&amp;order=Reuse+same+sort+as+last+time">All open</a>, <a  href="http://bugs.eclipse.org/bugs/buglist.cgi?short_desc_type=allwordssubstr&amp;short_desc=&amp;product=VE&amp;long_desc_type=allwordssubstr&amp;long_desc=&amp;bug_file_loc_type=allwordssubstr&amp;bug_file_loc=&amp;keywords_type=allwords&amp;keywords=&amp;bug_status=RESOLVED&amp;bug_status=VERIFIED&amp;bug_status=CLOSED&amp;emailtype1=substring&amp;email1=&amp;emailtype2=substring&amp;email2=&amp;bugidtype=include&amp;bug_id=&amp;votes=&amp;changedin=7&amp;chfieldfrom=&amp;chfieldto=Now&amp;chfieldvalue=&amp;cmdtype=doit&amp;order=Reuse+same+sort+as+last+time&amp;field0-0-0=noop&amp;type0-0-0=noop&amp;value0-0-0=">Recently closed</a>, 
      <a href="https://bugs.eclipse.org/bugs/enter_bug.cgi?product=VE">Open a new bug</a>, 
      <a href="https://bugs.eclipse.org/bugs/enter_bug.cgi?product=VE&bug_severity=enhancement">Open a feature request</a></td>
    </tr>
    <tr>
      <td rowspan="2"><a href="faq.html"> <img src="images/faq.gif" border="0"> </a></td>
      <td colspan="2"><a href="faq.html">FAQ </a></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>Frequently Asked Questions</td>
    </tr>
    <tr>
      <td rowspan="2"><a href="news://news.eclipse.org/eclipse.tools.vep"> <img src="images/news.gif" border="0"> </a></td>
      <td colspan="2" bgcolor="#ffffbb"><a target="_top" href="news://news.eclipse.org/eclipse.tools.ve" name="newsgroup">Newsgroup: eclipse.tools.ve </a> 
         (<a href="http://www.eclipse.org/newsportal/thread.php?group=eclipse.tools.ve">web interface</a><a>) </a></td>
    </tr>
    <tr>
      <td bgcolor="#ffffbb">&nbsp;</td>
      <td bgcolor="#ffffbb">Place to ask questions on how to use the Visual Editor</td>
    </tr>
    <tr>
      <td rowspan="2"><a href="docs/doc.html"> <img src="images/reference.gif" border="0"> </a></td>
      <td colspan="2"><a href="docs/doc.html">Documents</a>, <A	href="docs/migration/migration.html">Migration Documents</A></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>white papers, articles, user guides, reference manuals, test scenarios, tutorials, Javadoc...</td>
    </tr>
    <tr>
      <td rowspan="2"><a href="news://news.eclipse.org/eclipse.tools.vep"> <img src="images/OK.gif" border="0" height="39" width="38"> </a></td>
      <td colspan="2" bgcolor="#ffffbb"><a target="_top" href="news://news.eclipse.org/eclipse.tools.ve"> </a><a href="docs/testpass.html">Visual Editor Test Pass runs</a></td>
    </tr>
    <tr>
      <td bgcolor="#ffffbb">&nbsp;</td>
      <td bgcolor="#ffffbb">Formal external test passes</td>
    </tr>
    <tr>
      <td rowspan="2"><a href="http://dev.eclipse.org/mailman/listinfo/vep-dev"> <img src="images/mail.gif" border="0"> </a></td>
      <td colspan="2"><a target="_top" href="http://dev.eclipse.org/mailman/listinfo/ve-dev"> Mailing List </a></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>If you have questions about VE or why something doesn't work in VE, please don't ask in the mailing list.<a href="#newsgroup"
 target="_self"><b>Please use the Newsgroup above.</b></a>. This
mailing list is used to participate in discussions on the <b>implementation</b>
of the Visual Editor itself, not usage of it.<br>
      <a href="http://dev.eclipse.org/mhonarc/lists/ve-dev/maillist.html">Archives</a>,<a href="mailto:ve-dev@eclipse.org">Send message</a></td>
    </tr>
    <tr>
      <td rowspan="2"><a href="http://dev.eclipse.org/viewcvs/index.cgi/?cvsroot=Tools_Project">
      <img src="images/cvs.gif" border="0"> </a></td>
      <td colspan="2" bgcolor="#ffffbb"><a href="http://dev.eclipse.org/viewcvs/index.cgi/?cvsroot=Tools_Project">CVS Repository</a></td>
    </tr>
    <tr>
      <td bgcolor="#ffffbb">&nbsp;</td>
      <td bgcolor="#ffffbb">Web Interface to Visual Editor's Source Code</td>
    </tr>
  </tbody>
</table>			
			
		</div>

		
	</div>
	<div id="rightcolumn">
		<div class="sideitem">
			<h6>VEP Components</h6>
			<ul>
				<li><a href="docs/components/cde/cde.html"> CDE </a></li>
				<li><a href="docs/components/core/core.html"> Core </a></li>
				<li><a href="docs/components/jfc/jfc.html"> JFC/Swing</a> </li>
				<li><a href="docs/components/javamodel/javaModel.html"> Java Model</a></li>
				<li><a href="docs/components/swt/swt.html">SWT</a></li>
			</ul>
		</div>

		<div class="sideitem">
			<h6>Related links</h6>
			<ul>
				<li><a href="http://eclipse.org/tools/index.html">Eclipse Tools Project</a></li>
				<li><a href="http://www.eclipse.org/gef">GEF Tools Subproject</a></li>
				<li><a href="http://www.eclipse.org/emf">EMF Tools Subproject</a> </li>
				<li><a href="http://wiki.eclipse.org/index.php/JFace_Data_Binding ">JFace Data Binding</a></li>				
			</ul>
		</div>
	</div>
</div>


EOHTML;


	# Generate the web page
	$App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html);
?>
