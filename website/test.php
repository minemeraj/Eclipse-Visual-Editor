<?php
/*
 * Created on Jan 6, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */


echo "Hello, world!";

if (!($f2=fopen("public/tests.txt","w+")))
                  exit("Unable to open file.");
fwrite($f2,"Testing\n");
fclose($f2);


if (!($f2=fopen("public/tests.txt","r")))
                  exit("Unable to open file.");
$string = fread($f2,8);
fclose($f2);

echo "File read: " . $string;

?>