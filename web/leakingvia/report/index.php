<?php

header("Set-Cookie: generic=1; Expires=Wed, 30 May 2019 07:28:00 GMT", true);
header("Set-cookie: secure=1; Expires=Wed, 30 May 2019 07:28:00 GMT; Secure", false);
header("Set-cookie: httpOnly=1; Expires=Wed, 30 May 2019 07:28:00 GMT; HttpOnly", false);
header("Set-Cookie: lax=1; Expires=Wed, 30 May 2019 07:28:00 GMT; SameSite=lax", false);
header("Set-Cookie: strict=1; Expires=Wed, 30 May 2019 07:28:00 GMT; SameSite=strict", false);

?>

<html>

<head>
</head>

<body>

<?php

echo 'Report: ' . htmlspecialchars($_GET["leak"]);

?>

</body>
</html>