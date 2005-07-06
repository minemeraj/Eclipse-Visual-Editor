<?php 
header("Location: http://".$_SERVER['HTTP_HOST'].dirname($_SERVER['PHP_SELF'])."/index.php"); 
      if ($_POST["Email"]!= "")
          setcookie( "emailID", $_POST["Email"] , time()+60*60*24*30, "/");                       
?>
<html>
<head>
<title></title>
</head>
<body>
    <?php            
    	$datafile = "/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt";
    	$newdatafile = "/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt.new";
       if (!($f=fopen($datafile,"r"))) 
                  exit("Unable to open file.");
       if (!flock($f, LOCK_EX)) 
                  exit ("Site is busy") ;
       if (!($f2=fopen($newdatafile,"w+"))) 
                  exit("Unable to open file.");  
       while ($testinfo = fscanf ($f, "%s\t%s\t%s\t%s\t%[a-zA-Z0-9,. \\'';;~~!!@@##$$%%&&**(())--==++__]\n")) {
              list ($tst, $url, $tester, $status, $description) = $testinfo;
              // No spaces allowed in an Email ID
              list($a, $b) = sscanf($_POST["Email"],"%s %s");
              if (($tst == $_POST["Test"]) && ($tester == "None") && ($a!="") && ($b=="")) {
                  $tester = $_POST["Email"];                  
                  // ve-dev@eclipse.org 
                  mail("sgunturi@us.ibm.com, " . $_POST["Email"], $_POST["Test"] . " assigned to " . $_POST["Email"], "VisualEditor, 1.0.2 TestPass\n\tTestcase " . $_POST["Test"] . " was assigned to ". $_POST["Email"].".", "From: VEproject@{$_SERVER['SERVER_NAME']}");                  
//                  mail("ve-dev@eclipse.org, " . $_POST["Email"], $_POST["Test"] . " assigned to " . $_POST["Email"], "VisualEditor, M8 TestPass\n\tTestcase " . $_POST["Test"] . " was assigned to ". $_POST["Email"].".", "From: gmendel@us.ibm.com");                  
              }
              $out=sprintf("%s\t%s\t%s\t%s\t%s\n", $tst, $url, $tester, $status, $description);
              fwrite($f2,$out);
       }
       flock($f, LOCK_UN);
       fclose($f);
       fclose($f2);
       unlink($datafile);
//       echo "<h2>replacing tests-ve1.1.txt.new>tests-ve1.1.txt</h2><br>";
//       echo "$newdatafile exists? " . file_exists($newdatafile) . "<br>";
//       echo "$datafile exists? " . file_exists($datafile) . "<br>";
       if(!(copy($newdatafile, $datafile)))
      		exit("unable to copy");
//       echo "removing $newdatafile <br>";
       if(!unlink($newdatafile))
       	exit("unable to delete tests-ve1.1m1.txt.new");
//       echo "$newdatafile exists? " . file_exists($newdatafile) . "<br>";
//       echo "$datafile exists? " . file_exists($datafile) . ", is readable? " . is_readable($datafile) . ", file size=" . filesize($datafile) . "<br>";
     ?>			
</body>

</html>
