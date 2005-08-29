<?php 
	$action = $_POST["action"] ; 
	$pass = $_POST["pass"];
	if(strlen($pass)>0 && $pass!="jve")
		exit('<h1>Incorrect password</h1>');
		
	$WRITEDIR='/home/data/httpd/writable/vep/';
	$DESTDIR = $WRITEDIR . "testruns/110/final/";
	$DESTFILE = $DESTDIR . "tests-ve1.1.txt";
?>
<html>
<head>
<title>Test data initalizing page</title>
</head>
<body>
<?php
if(strlen($action)<1){
?>
<h2>Manipulate test data using below buttons.</h2>

<li>
<form method="POST" action="test-data.php">
	<input type="password" value="" name="pass">
	<input type="hidden" value="reset" name="action">
	<input type="submit" value="Reset test data">
</form>
</li>

<li>
<form method="POST" action="test-data.php">
	<input type="password" value="" name="pass">
	<input type="hidden" value="view" name="action">
	<input type="submit" value="View test data">
</form>

</li>
<?php
}elseif ($action=="reset"){
	$sourcefile = "tests-ve1.1.txt.backup";
	
	if(!file_exists($DESTDIR)){
		mkdir($WRITEDIR . "testruns");
		mkdir($WRITEDIR . "testruns/110");
		mkdir($WRITEDIR . "testruns/110/final");
	}
	
	if(file_exists($DESTDIR)){
//		echo "<li>source = $sourcefile";
//		echo "<li>destination = $DESTFILE";
//		echo "<li>is readble: " . is_readable($sourcefile);
//		echo "<li>is writeable: " . is_writeable($DESTDIR);
//		echo "<li>dest. exists?: " . file_exists($DESTFILE);
//		echo "<li>src. exists?: " . file_exists($sourcefile);
		if(file_exists($DESTFILE)){
			unlink($DESTFILE);
		}
//		echo "<li>dest. exists?: " . file_exists($DESTFILE);

		if(!copy($sourcefile, $DESTFILE)){
			exit("<h1>Cannot restore file via copy</h1>");
		}else{
			echo "<h1>Success - file restored</h1>";
		}
	}else{
		exit("<h1>Cannot find directory $DESTDIR</h1>");
	}

}elseif ($action=="view"){
	echo "<h2>File contents:</h2><br>";
	if(file_exists($DESTFILE)){
		$testfile = fopen($DESTFILE, "r");
		$contents = fread($testfile, filesize($DESTFILE));
		fclose($testfile);
		echo $contents;
	}else{
		echo "Unable to find file $DESTFILE";
	}
}
?>
</body>
</html>