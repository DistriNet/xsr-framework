<?php

$type = htmlspecialchars($_GET["type"]);

echo "<iframe id='iframe1' src='https://attacker.test/s1/redirect?type=" . $type . "' style='position: relative; height: 100%; width: 100%;'></iframe>";

?>
