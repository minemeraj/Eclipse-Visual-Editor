<?php

	# Set the theme for your project's web pages.
	# See the Committer Tools "How Do I" for list of themes
	# https://dev.eclipse.org/committers/
	# Optional: defaults to system theme 
	$theme = "";

	# Define your project-wide Nav bars here.
	# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
	# these are optional
	$Nav->addNavSeparator("VE Home", 	"http://eclipse.org/vep");
	
	$Nav->addCustomNav("VE Downloads", 		"http://download.eclipse.org/tools/ve/downloads/index.php", 	"_self", 2);
	$Nav->addCustomNav("VE Download Archives", 	"http://archive.eclipse.org/tools/ve/downloads/", 	"_self", 3);
	
	$Nav->addCustomNav("VE FAQ", "faq.html", "_self", 2);
	
	$Nav->addCustomNav("Bugzilla", "http://bugs.eclipse.org/bugs", "_blank", 2);
	$Nav->addCustomNav("Open a new bug", "https://bugs.eclipse.org/bugs/enter_bug.cgi?product=VE", "_blank", 3);
	$Nav->addCustomNav("Open a feature request", "https://bugs.eclipse.org/bugs/enter_bug.cgi?product=VE&bug_severity=enhancement", "_blank", 3);
	$Nav->addCustomNav("Recently closed bugs", "http://bugs.eclipse.org/bugs/buglist.cgi?short_desc_type=allwordssubstr&amp;short_desc=&amp;product=VE&amp;long_desc_type=allwordssubstr&amp;long_desc=&amp;bug_file_loc_type=allwordssubstr&amp;bug_file_loc=&amp;keywords_type=allwords&amp;keywords=&amp;bug_status=RESOLVED&amp;bug_status=VERIFIED&amp;bug_status=CLOSED&amp;emailtype1=substring&amp;email1=&amp;emailtype2=substring&amp;email2=&amp;bugidtype=include&amp;bug_id=&amp;votes=&amp;changedin=7&amp;chfieldfrom=&amp;chfieldto=Now&amp;chfieldvalue=&amp;cmdtype=doit&amp;order=Reuse+same+sort+as+last+time&amp;field0-0-0=noop&amp;type0-0-0=noop&amp;value0-0-0=", "_blank", 3);



?>
