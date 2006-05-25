<?php  																												

	require_once($_SERVER['DOCUMENT_ROOT'] . "/projects/common/project-info.class.php");
	$p_name = "eclipse";
	if(isset($_REQUEST['project']) && strlen($_REQUEST['project']) > 0)
	{
		$p_name = $_REQUEST['project'];
		$p_name = strtolower($p_name);		
	}
	if(isset( $_REQUEST['fetch']))
		$pi = new ProjectInfo($p_name, $_REQUEST['fetch']);
	else
  		$pi = new ProjectInfo($p_name, 0);
	
	require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/app.class.php");
	require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/nav.class.php");
	require_once($_SERVER['DOCUMENT_ROOT'] . "/eclipse.org-common/system/menu.class.php");
	$App 	= new App();	
	$Nav	= new Nav();	
	$Menu 	= new Menu();		
	include($App->getProjectCommon());    # All on the same line to unclutter the user's desktop'

	# Begin: page-specific settings.  Change these. 
	$pageTitle 		= $pi->getname();
	$pageKeywords	= "Automatically Generated from poject-info.xml";
	$pageAuthor		= "Eduardo A. Romero Gomez";
	$projectKey = $p_name;
	# Add page-specific left nav menu here
	#
	#$Nav->addNavSeparator("Cool Links", 	"#");
	#$Nav->addCustomNav("OCM", 	"http://www.openmex.com", "_blank", 1);
  	
	ob_start();
?>
<div id="maincontent">
	<style type="text/css">
	h4 {
	font-weigth: normal;
	border-bottom: thin dotted;
	}
	hr
	{
		border-bottom: solid;
	}
	</style>	
	<div id="midcolumn">
	<h1>Name: <a href="<?= $pi->getUrlIndex(); ?>" target="_blank"><?= $pi->getname();?></a></h1>
    <h2>Project Info</h2>
	<p> 
	<div align="justify"><?= $pi->getParagraph() ?></div><br />
	<a href="<?= $pi->getUrlIndex();?>">project home page &raquo;</a>
	<br/>	
	<table width="100%">
	<tr>
		<td valign="top" width="50%">
		<h3>Releases:</h3> 
		  <?php
		  $num_shipps = $pi->hasshippings();
		  for($i=0; $i<$num_shipps; $i++)
		  {
			$shipp = $pi->getshipping($i);
			$name = $shipp->getName();
			$url =  $shipp->getDownload();
			$date = $shipp->getDate();
			?>
			<a href="<?= $url ?>"><?= $name ?></a> (complete) - <?= $date ?><br/>
			<?  
		  }
		  ?>
		<?php
		if($pi->hasreleases())
		{
		?>
		  <?php
		  $num_rels = $pi->hasreleases();
		  for($r=0; $r<$num_rels;$r++)
		  {
			$rel = $pi->getrelease($r);
			if($rel)
			{
				$name = $rel->getName();
				$date = $rel->getDate();
				$status = $rel->getStatus();
				$download = $rel->getDownload();
				$plan = $rel->getPlan();	
				?>
				 <b><?= $name ?></b>  (<?= $status ?>) - <span class="dates"><?= $date ?></span> 
				 <?php
				if($download != "")
				{?>
				<a href="<?= ( ($download == "") ? "#" : $download  )?>">Download</a>
				<?}?>
				 
				<?php
				if($plan != "")
				{
					?>
					<a href="<?= ( ($plan == "") ? "#" : $plan  )?>">Plan</a>
					<?
				}
				?>
				<br/>
			  <?
			  }
		  }
		  ?>
		<?
		}
		?>
	</td>
	<td valign="top" width="50%">
	<h3>Other Information:</h3>
	<?
	if($pi->hasBugzilla() <= 1)
	{
		?>
	<a href="<?=$pi->bugs_url()?>">Bug Reports</a><br/><br/>
	  <?php
	}
	else if($pi->hasbugzilla())
	{
	?>

	<b>Bugzilla Products</b> (<?= $pi->hasbugzilla()?>):<br/>
	  <?php
	  $num_bugzilla = $pi->hasbugzilla();
	  for($i=0; $i<$num_bugzilla; $i++)
	  {  	
		$name =  $pi->getbugzillaproduct($i);
		$url =  $pi->getbugzilla($i);
		?>
		  &nbsp;&nbsp;<a href="<?= $url ?>" target="_blank"><?= $name ?></a><br/>
	  	<?  
	  }
	  ?>
	<?
	} // End lists
	?>	
	<?php
	$cvs = $pi->cvs_url();
	if(strlen($cvs) > 0)
	{
	?>
	<a href="<?= $cvs?>" target="_blank">CVS Repository</a><br/>
	<?php
		if($pi->hascvsmodules())
		{
			?>
		<b>CVS Modules:</b><br/>
		<?
			for($m =0; $m<$pi->hascvsmodules(); $m++)
			{
				?>
				&nbsp;&nbsp;&nbsp;&nbsp;<?= $pi->getcvsmodule($m); ?><br/>
				<?	
			}
		?>
		<?
		}
		echo "<br/>";
	}
	?>
	<?php
	if($pi->hasblogs())
	{
	?>
	<b>Blogs:</b><br/>
	  <?php
	  $num_blogs = $pi->hasblogs(); 
	  for($i=0; $i<$num_blogs; $i++)
	  {  	
		$blog = $pi->getBlog($i);
		$name = $blog->getName();
		$url = $blog->getURL();		
		?>
		&nbsp;&nbsp;&nbsp;&nbsp;<a href="<?= $url?>" target="_blank"><?= $name ?></a><br/>
	  <?  
	  }
	  ?>
	<?
	} // End Blogs
	?>
	<?php
	if($pi->hasArticles())
	{
	?>
	<b>Articles:</b><br/>	
	 <?php
	  $num_articles = $pi->hasArticles(); 
	  for($i=0; $i<$num_articles; $i++)
	  {  	
		$url = $pi->getArticleAt($i);
		?>
		&nbsp;&nbsp;&nbsp;&nbsp;<a href="<?= $url ?>" target="_blank"><?= $url ?></a><br/>
	  <?  
	  }
	  ?>
	<?
	} // End Articles
	?>	
	</td>
	</tr>
	<tr>
	<td valign="top"  width="50%">	
	<?php
	if($pi->haslists())
	{
	  $url = $pi->mailing_lists_url();
	?>
	<h3><a href="<?= $url ?>" target="_blank">Mailing Lists:</a></h3>
	<ul>

	  <?php
	  $num_lists = $pi->haslists();
	  for($i=0; $i<$num_lists; $i++)
	  {  	
		$name =  $pi->getlist($i);
		?>
		<li><?= $name ?></li>
	  <?  
	  }
	  ?>
	</ul>
	<?
	} // End lists
	?>	
	<td valign="top" width="50%">
	  <?php
	if($pi->hasnewsgroups())
	{
	  $url = $pi->newsgroups_url();
	?>
	<h3><a href="<?= $url ?>" target="_blank">Newsgroups Categories:</a></h3>
	<ul>
	  <?php
	  $num_news = $pi->hasnewsgroups();
	  for($i=0; $i<$num_news; $i++)
	  {  	
		$name =  $pi->getnewsgroup($i);
		?>
		<li>
		  <?= $name ?>
		</li>
	  <?  
	  }
	  ?>
	</ul>
	<?
	} // End lists
	?>	
	</td>
	</tr>
	</table>
	<br/><br/>
	<p>See <a href="/projects/common/doc/howstuffworks/how-things-work_projectinfo.php">How Things Work</a> for how this page is generated.
    <br/>
    </p>
    
		
	<br/><br/><br/>

    </div>	
    </div>
	<div id="rightcolumn">
		<div class="sideitem">
		<h6>Project Info</h6>
		<ul>
			<li><b>Parent Project:</b> <?= $pi->getParentProjectID() ?><br></li>
			<li><b>Level:</b> <?= $pi->getLevel() ?><br></li>
		</ul>
		</div>
	<div class="sideitem">
		<h6>Related links</h6>
		<ul>
				<?if(strlen($pi->contributors_url())>0)
				{ ?>
				<li><a href="<?= $pi->contributors_url() ?>">Contributors</a></li>
				<?
				}
				?>
				<?if(strlen($pi->committers_url())>0)
				{ ?>
				<li><a href="<?= $pi->committers_url() ?>">Committers</a></li>
				<?
				}
				?>
				<?if(strlen($pi->getting_started_url())>0)
				{ ?>
				<li><a href="<?= $pi->getting_started_url() ?>">Getting Started</a></li>
				<?
				}
				?>		
		</ul>
		<br/>

		<h6>Dashboard Information</h6>
		<table width="100%" align="center">
		<tr>
			<td>Bugs: <?= $pi->dashboard_bugs_lights(); ?></td>
		</tr>
		<tr>
			<td>News: <?= $pi->dashboard_news_lights(); ?></td>
		</tr>
		</table>	
	</div>
	</div>


		
	
</div>
<?php
	$html = ob_get_contents();
	ob_end_clean();

	# Generate the web page
	$App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html);
?>
