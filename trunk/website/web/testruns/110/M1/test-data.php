<?php 
	$action = $_POST["action"] ; 
	$pass = $_POST["pass"];
	if(strlen($pass)>0 && $pass!="jve")
		exit('<h1>Incorrect password</h1>');
		
	$WRITEDIR='/home/data/httpd/www.eclipse.org/html/vep/writable';
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
	if(chdir($WRITEDIR)){
		if(!file_exists('testruns/110/M1')){
			mkdir('testruns');
			mkdir('testruns/110');
			mkdir('testruns/110/M1');
		}
		if(chdir($WRITEDIR . "/testruns/110/M1")){
			echo 'reset:WORKED'.getcwd();
		}else{
			exit("<h1>Cannot chdir to $WRITEDIR/testruns/110/M1");
		}
	}else{
		exit("<h1>Unable to change to $WRITEDIR</h1>");
	}
}elseif ($action=="view"){
}
?>
</body>
</html>