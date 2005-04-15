<?php header("Location: http://".$_SERVER['HTTP_HOST']
                       .dirname($_SERVER['PHP_SELF'])
                       ."/index.php"); ?>
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
              if (($tst == $_POST["Test"]) && ($_POST["Result"]!="")) {
                  $status = $_POST["Result"];
                  $description=$_POST["Comments"];
                  // ve-dev@eclipse.org 
//                  mail("ve-dev@eclipse.org, ".$_POST["Email"], $_POST["Test"].": ".$_POST["Result"].".", "VisualEditor, M8 TestPass\n\tTestcase " . $_POST["Test"] . " status=".$_POST["Result"]." by ". $_POST["Email"]."\nComments:\n".$description.".", "From: VEproject@{$_SERVER['SERVER_NAME']}");                  
                  mail("sgunturi@us.ibm.com, ".$_POST["Email"], $_POST["Test"].": ".$_POST["Result"].".", "VisualEditor, M8 TestPass\n\tTestcase " . $_POST["Test"] . " status=".$_POST["Result"]." by ". $_POST["Email"]."\nComments:\n".$description.".", "From: gmendel@us.ibm.com");                  
              }
              $out=sprintf("%s\t%s\t%s\t%s\t%s\n", $tst, $url, $tester, $status, $description);
              fwrite($f2,$out);
       }
       flock($f, LOCK_UN);
       fclose($f);
       fclose($f2);
       copy("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt.new", "/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt");
       unlink("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt.new");
       chmod ("/home/data/httpd/download.eclipse.org/tools/ve/web/testruns/110/M1/tests-ve1.1m1.txt","ugo+rw");      
     ?>			
</body>

</html>
