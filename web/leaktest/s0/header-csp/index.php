<?php

/*  header('Link: <http://adition.com/report/?leak=' . $type . '>; rel="stylesheet" ', false);*/

  //header('Report-To: { "url": "https://adition.com/report/?leak=x-csp-report-to","group": "endpoint-2","max-age": 10886400 }', false);

  $type = htmlspecialchars($_GET["type"]);
  switch ($type) {
    case 'csp-report-uri':
      header("Content-Security-Policy: default-src 'none'; report-uri https://adition.com/report/?leak=csp-report-uri");
      break;
    case 'csp-report-uri-2':
      header("Content-Security-Policy-Report-Only: default-src 'none'; report-uri https://adition.com/report/?leak=csp-report-uri-2");
      break;
    case 'csp-report-to':
      header('Report-To: { "url": "https://adition.com/report/?leak=csp-report-to","group": "endpoint-1","max-age": 10886400 }', false);
      header("Content-Security-Policy: script-src 'self'; report-to=endpoint-1");
      break;
    case 'x-csp-report-uri':
      header("X-Content-Security-Policy: default-src 'none'; report-uri https://adition.com/report/?leak=x-csp-report-uri");
      break;
    case 'x-csp-report-uri-2':
      header("X-Content-Security-Policy-Report-Only: default-src 'none'; report-uri https://adition.com/report/?leak=x-csp-report-uri-2");
      break;
    case 'x-csp-report-to':
      header('Report-To: { "url": "https://adition.com/report/?leak=x-csp-report-to","group": "endpoint-2","max-age": 10886400 }', false);
      header("X-Content-Security-Policy: default-src 'none'; report-to=endpoint-2");
      break;
    case 'x-webkit-csp-report-uri':
      header("X-Webkit-CSP: default-src 'none'; report-uri https://adition.com/report/?leak=x-webkit-csp-report-uri");
      break;
    case 'nel-report':
      header('Report-To: { "url": "https://adition.com/report/?leak=csp-report-to","group": "network-errors","max-age": 10886400 }', false);
      header('NEL: {"report_to": "network-errors", "max_age": 2592000}');
      break;
    default: echo "NO USABLE TYPE!!!";
  }
 ?>
<script src="https://other-domain.com/script.js"></script>
<p id="message">header2</p>
