<?php
  $type = htmlspecialchars($_GET["type"]);

  switch ($type) {
    case "meta-refresh":
      echo '<meta http-equiv="refresh" content="0; url=https://adition.com/report/?leak=meta-refresh">';
      echo 'test';
      break;
    case "header-300":
      header('HTTP/1.1 300 Multiple Choices');
      header('Location: https://adition.com/report/?leak=header-300');
      break;
    case "header-301":
      header('HTTP/1.1 301 Moved Permanently');
      header('Location: https://adition.com/report/?leak=header-301');
      break;
    case "header-302":
      header('HTTP/1.1 302 Found');
      header('Location: https://adition.com/report/?leak=header-302');
      break;
    case "header-303":
      header('HTTP/1.1 303 See Other');
      header('Location: https://adition.com/report/?leak=header-303');
      break;
    case "header-304":
      header('HTTP/1.1 304 Not Modified');
      header('Location: https://adition.com/report/?leak=header-304');
      break;
    case "header-305":
      header('HTTP/1.1 305 Use Proxy');
      header('Location: https://adition.com/report/?leak=header-305');
      break;
    case "header-306":
      header('HTTP/1.1 306 Switch Proxy');
      header('Location: https://adition.com/report/?leak=header-306');
      break;
    case "header-307":
      header('HTTP/1.1 307 Temporary Redirect');
      header('Location: https://adition.com/report/?leak=header-307');
      break;
    case "header-308":
      header('HTTP/1.1 308 Permanent Redirect');
      header('Location: https://adition.com/report/?leak=header-308');
      break;
    case "window-location":
      echo "<script>";
      echo "window.location='https://adition.com/report/?leak=redirect-winloc'";
      echo "</script>";
      break;
    case "form-GET":
      echo "<form id='toSubmit' action='https://adition.com/report/?leak=form-GET' method='get'>";
      echo "<input name='leak' value='form-GET' type='hidden'>";
      echo "</form>";
      echo "<script>";
      echo "document.getElementById('toSubmit').submit()";
      echo "</script>";
      break;
    case "form-POST":
      echo "<form id='toSubmit' action='https://adition.com/report/?leak=form-POST' method='post'>";
      echo "<input name='leak' value='form-POST' type='hidden'>";
      echo "</form>";
      echo "<script>";
      echo "document.getElementById('toSubmit').submit()";
      echo "</script>";
      break;
    default:
      echo "No type parameter found.";
  }

    echo "<p id='message'>To be redirected</p>";
?>
redirect
