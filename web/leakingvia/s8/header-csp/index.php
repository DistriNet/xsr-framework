<?php

$type = htmlspecialchars($_GET["type"]);

echo "<iframe id='iframe1' src='https://leak.test/s4/header2?type=$type' style='position: relative; height: 100%; width: 100%;'></iframe>";

?>
