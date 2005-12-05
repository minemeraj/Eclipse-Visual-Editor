<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<title>Test it</title>
</head>

<body>

<form method="POST" action="testit2.php">
    
	<p>If you intend to test the<b><i> <?php echo $_POST["Test"] ; ?> </i>
	</b>test case, please fill out your email address and press the 
	<i>Testit</i> button.<br>Note that an email will be sent to you and the 
	Visual Editor project mailing list to confirm your testing effort.<br>Thank 
	you for your time.</p>
	<?php $val = $_COOKIE["emailID"]; ?>
	<input type="text" name="Email" value="<?php echo $val; ?>" size="50"><p>&nbsp;</p>
	<input type="hidden" value="<?php echo $_POST["Test"] ; ?>" name="Test"><p>&nbsp;</p>
	<p><input type="submit" value="Test it" name="B1"></p>
</form>

</body>

</html>
