<?php 
header("Location: http://".$_SERVER['HTTP_HOST'].dirname($_SERVER['PHP_SELF'])."/index.php"); 
?>
<html>
<head>
<title></title>
</head>
<body>AA
    <?php        
    	$datafile = "/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt";
    	$newdatafile = "/home/data/httpd/writable/vep/testruns/110/final/tests-ve1.1.txt.new";
       if (!($f=fopen($datafile,"r")))
                  exit("Unable to open original file.");
       if (!flock($f, LOCK_EX)) 
                  exit ("Site is busy") ;
       if (!($f2=fopen($newdatafile,"w+"))) 
                  exit("Unable to open temp file.");  
       while ($testinfo = fscanf ($f, "%s\t%s\t%s\t%s\t%[a-zA-Z0-9,. \\'';;~~!!@@##$$%%&&**(())--==++__]\n")) {
              list ($tst, $url, $tester, $status, $description) = $testinfo;
              if (($tst == $_POST["Test"]) && ($_POST["Result"]!="")) {
                  $status = $_POST["Result"];
                  $description=$_POST["Comments"];
                  // ve-dev@eclipse.org 
                  mail("ve-dev@eclipse.org, ".$_POST["Email"], $_POST["Test"].": ".$_POST["Result"].".", "VisualEditor, 1.1 final TestPass\n\tTestcase " . $_POST["Test"] . " status=".$_POST["Result"]." by ". $_POST["Email"]."\nComments:\n".$description.".", "From: gmendel@us.ibm.com");                  
//                  mail("walkerp@us.ibm.com, ".$_POST["Email"], $_POST["Test"].": ".$_POST["Result"].".", "VisualEditor, 1.1 final TestPass\n\tTestcase " . $_POST["Test"] . " status=".$_POST["Result"]." by ". $_POST["Email"]."\nComments:\n".$description.".", "From: VEproject@{$_SERVER['SERVER_NAME']}");                  
              }
              $out=sprintf("%s\t%s\t%s\t%s\t%s\n", $tst, $url, $tester, $status, $description);
              fwrite($f2,$out);
       }
       flock($f, LOCK_UN);
       fclose($f);
       fclose($f2);
       if(!unlink($datafile))
       		exit("unable to link $datafile");
       if(!copy($newdatafile, $datafile))
       		exit("unable to copy $newdatafile > $datafile");
       if(!unlink($newdatafile))
       		exit("unable to remove $newdatafile");
     ?>
</body>

</html>
