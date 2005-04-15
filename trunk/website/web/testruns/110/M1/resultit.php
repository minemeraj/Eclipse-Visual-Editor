<html>

<head>
<meta http-equiv="Content-Language" content="en-us">
<title>Test it</title>
</head>

<body>

<form method="POST" action="resultit2.php">
    
	<p>If you intend to publish test results for the<b><i> <?php echo $_POST["Test"] ; ?> </i>
	</b>test case, please fill out your email address, select Pass/Fail, record your defects in the comments area and press the
	<i>Submit</i> button.<br>Note that an email will be sent to you and the 
	Visual Editor project mailing list.<br>Thank 
	you for your time.</p>
	<?php $val = $_COOKIE["emailID"]; ?>
	<input type="text" name="Email" value="<?php echo $val; ?>" size="50">
	<p><input type="radio" name="Result" value="Passed"><b><font color="#0000FF">Passed</font></b></p>
	<p><input type="radio" name="Result" value="Failed"><b><font color="#FF0000">Failed</font></b></p>
	<input type="hidden" value="<?php echo $_POST["Test"] ; ?>" name="Test">
	<blockquote>
		<p>&nbsp;<i>Enter any comments or defects opened here</i>:<br>
		<input type="text" name="Comments" size="50"></p>
	</blockquote>
	<p><input type="submit" value="Submit"></p>
</form>
</body>

</html>