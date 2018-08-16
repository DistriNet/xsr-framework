<?php

$type = htmlspecialchars($_GET["type"]);

echo "<iframe id='iframe1' src='https://adition.com/s3/redirect?type=" . $type . "' style='position: relative; height: 100%; width: 100%;'></iframe>";

?>
