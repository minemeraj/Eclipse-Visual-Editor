<?php 
	$action = $_POST["action"] ; 
	$pass = $_POST["pass"];
	
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
?>
<h1>RESET</h1>
<?php
}elseif ($action=="view"){
?>
<h1>VIEW</h1>
<?php
}
?>
</body>
</html>