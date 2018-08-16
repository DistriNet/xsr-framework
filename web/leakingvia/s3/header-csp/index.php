<?php

$type = htmlspecialchars($_GET["type"]);

echo "<iframe id='iframe1' src='https://leak.test/s0/header2?type=$type' style='position: relative; height: 100%; width: 100%;'></iframe>";

?>
