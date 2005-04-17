<?php 
	$action = $_POST["action"] ; 
	$pass = $_POST["pass"];
	if(strlen($pass)>0 && $pass!="jve")
		exit('<h1>Incorrect password</h1>');
		
	$WRITEDIR='/home/data/httpd/www.eclipse.org/html/vep/writable/';
	$DESTDIR = $WRITEDIR . "testruns/110/M1/";
	$DESTFILE = $DESTDIR . "tests-ve1.1m1.txt";
?>
<html>
<head>
<title>Test data initalizing page</title>
</head>
<body>
10:57pm<br>
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
	$sourcefile = "tests-ve1.1m1.txt.backup";
	
	if(!file_exists($DESTDIR)){
		mkdir($WRITEDIR . "testruns");
		mkdir($WRITEDIR . "testruns/110");
		mkdir($WRITEDIR . "testruns/110/M1");
	}
	
	if(file_exists($DESTDIR)){
		echo "<li>source = $sourcefile";
		echo "<li>destination = $DESTFILE";
		echo "<li>is readble: " . is_readable($sourcefile);
		echo "<li>is writeable: " . is_writeable($DESTDIR);
		echo "<li>dest. exists?: " . file_exists($DESTFILE);
		echo "<li>src. exists?: " . file_exists($sourcefile);
		if(!copy($sourcefile, $DESTFILE)){
			exit("<h1>Cannot restore file via copy</h1>");
		}else{
			echo "<h1>Success - file restored</h1>";
		}
	}else{
		exit("<h1>Cannot find directory $DESTDIR</h1>");
	}

}elseif ($action=="view"){
	$testfile = fopen($DESTFILE, "r");
	$contents = fread($testfile, filesize($testfile));
	fclose($testfile);
	echo $contents;
}
?>
</body>
</html>