<html>
<head>
<title>TestPass</title>
</head>
<body>
<table border="0" cellpadding="2" style="width: 100%;">
  <tbody>
    <tr>
      <td align="left" width="60%"> <b> <font class="indextop" size="7">Visual
Editor Project</font></b><br>
      <font color="#6699FF">
      <font class="indexsub"><font size="2">First-class GUI building tools for Eclipse</font></font><font size="2">
		</font></font> </td>
      <td style="width: 40%; text-align: right;"> <img src="vep.png"
 title="" alt="Visual Editor Project Logo"> <br>
      </td>
    </tr>
  </tbody>
</table>
<table border="0" cellpadding="2" width="100%">
  <tbody>
    <tr>
      <td align="left" valign="top" bgcolor="#0070a0"> <b>
		<font
 color="#ffffff" face="Arial,Helvetica"><font size="4">Testpass&nbsp; <i>VE 1.1 final regression</i> - July 13th, 2005</font></font></b></td>
    </tr>
  </tbody>
</table>
<?php
   $Left=0;
   $InProgress=0;
   $Done=0;
?>
<blockquote>
	<ul>
		<li><b><font size="4" color="#0000FF">Test Cases not assigned to anyone yet</font></b><ul>
		     <?php
              if (!($f=fopen("/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt","r"))) 
                  exit("Unable to open file.");
              while ($testinfo = fscanf ($f, "%s\t%s\t%s\t%s\t%[a-zA-Z0-9,. \\'';;~~!!@@##$$%%&&**(())--==++__]\n")) {
                list ($tst, $url, $tester, $status, $description) = $testinfo;
                if ($tester == "None") {
                  echo "<li> <form method=\"POST\" action=\"testit.php\">" ;
                  echo "  <input type=\"hidden\" value=\"$tst\" name=\"Test\">";
                  echo "  <input type=\"submit\" value=\"Test it\">";
                  echo "  (<a href=\"$url\">$tst</a>)\t$description ";
                  echo "</form></li>";
                  $Left=$Left+1;
                }
              }
              fclose($f);
             ?>			
            </ul>
		<hr>			            
	</ul>
	<ul>
		<li><font size="4" color="#0000FF"><b>Test Cases assigned and in 
		progress</b></font><ul>
		<?php
              if (!($f=fopen("/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt","r"))) 
                  exit("Unable to open file.");
              while ($testinfo = fscanf ($f, "%s\t%s\t%s\t%s\t%[a-zA-Z0-9,. \\'';;~~!!@@##$$%%&&**(())--==++__]\n")) {
                list ($tst, $url, $tester, $status, $description) = $testinfo;
                if (($tester != "None") && ($status=="NoResult")){
                  echo "<li><form method=\"POST\" action=\"resultit.php\">" ;
                  echo "  <input type=\"hidden\" value=\"$tst\" name=\"Test\">";
                  echo "  <input type=\"submit\" value=\"Submit Results\">";
                  echo "  (<a href=\"$url\">$tst</a>) is being tested by $tester";
                  echo "</form></li>";
                  $InProgress=$InProgress+1;
                }
              }
              fclose($f);
        ?>			
		</ul>
		<hr>
	</ul>	
	<ul>
		<li><font size="4" color="#0000FF"><b>Test Cases Completed</b></font><br>
		<?php
              if (!($f=fopen("/home/data/httpd/writable/vep/testruns/110/M1/tests-ve1.1.txt","r"))) 
                  exit("Unable to open file.");
              echo "<table border=\"0\" width=\"100%\" id=\"t3\">";
              while ($testinfo = fscanf ($f, "%s\t%s\t%s\t%s\t%[a-zA-Z0-9,. \\'';;~~!!@@##$$%%&&**(())--==++__]\n")) {
                list ($tst, $url, $tester, $status, $description) = $testinfo;
                if (($tester != "None") && ($status!="NoResult")){
                  if ($status == "Failed")
                     $color="#FF0000";
                  else
                     $color="#0000FF";
                  echo "  <tr>";
                  echo "<td width=\"100\"> (<a href=\"$url\">$tst</a>)</td>";
                  echo "<td width=\"220\">was completed with status [<font color=\"$color\">$status</font>]</td>";
                  echo "<td width=\"240\"> by $tester </td>";
                  echo "<td>$description</td>";
                  echo "</tr>";
                  $Done=$Done+1;
                }
              }
              fclose($f);
              echo "</table> ";
        ?>			
		
		<hr>
	</ul>
</blockquote>

<p>
<table border="0" cellpadding="2" width="100%">
  <tbody>
    <tr>
      <td align="left" valign="top" bgcolor="#0070a0"> <b>
		<font
 color="#ffffff" face="Arial,Helvetica"><font size="4">Total of  <?php $total=$Left+$InProgress+$Done; echo "$total" ?> test cases
 </font></font></b><font size="4"> </font> </td>
    </tr>
  </tbody>
</table>

<blockquote>
	<table border="0" width="100%" id="table1">
		<tr>
			<td width="37"><b><font size="4">  <?php echo "$InProgress" ?> 
			</font></b></td>
			<td><b><font size="4">in progress</font></b></td>
		</tr>
		<tr>
			<td width="37"><b><font size="4"> <?php echo "$Done" ?> </font></b>
			</td>
			<td><b><font size="4">completed</font></b></td>
		</tr>
		<tr>
			<td width="37"><b><font size="4" color="#0000FF"> <?php echo "$Left" ?> </font></b>
			</td>
			<td><i><b><font size="4">to Go...</font></b></i></td>
		</tr>
	</table>
</blockquote>
</body>

</html>
