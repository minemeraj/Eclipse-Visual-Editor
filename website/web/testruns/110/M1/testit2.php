<?php header("Location: http://".$_SERVER['HTTP_HOST'].dirname($_SERVER['PHP_SELF'])."/index.php"); 
      if ($_POST["Email"]!= "")
          setcookie( "emailID", $_POST["Email"] , time()+60*60*24*30, "/");                       
?>
<html>
<head>
<title></title>
</head>
<body>
    <?php            
       if (!($f=fopen("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt","r"))) 
                  exit("Unable to open file.");
       if (!flock($f, LOCK_EX)) 
                  exit ("Site is busy") ;
       if (!($f2=fopen("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt.new","w+"))) 
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
       copy("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt.new", "/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt");
       chmod ("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt","ugo+rw");
       unlink("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt.new"); 
     ?>			
</body>

</html>
