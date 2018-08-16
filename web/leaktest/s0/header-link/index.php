<?php
    $type = $_GET["type"];
    header('Link: ' . base64_decode($type));
    echo $type;
    echo base64_decode($type);

    /*header('Link: <https://adition.com/report/?leak=link-stylesheet>; rel="stylesheet" ', false);
    header('Link: <https://adition.com/report/?leak=link-icon>; rel="icon" ', false);
    header('Link: <https://adition.com/report/?leak=link-shortcut-icon>; rel="shortcut icon" ', false);
    header('Link: <https://adition.com/report/?leak=link-import>; rel="import" ', false);
    header('Link: <https://adition.com/report/?leak=link-dns-prefetch>; rel="dns-prefetch" ', false);
    header('Link: <https://adition.com/report/?leak=link-preconnect>; rel="preconnect" ', false);
    header('Link: <https://adition.com/report/?leak=link-prefetch>; rel="prefetch" ', false);
    header('Link: <https://adition.com/report/?leak=link-preload>; rel="preload" ', false);
    header('Link: <https://adition.com/report/?leak=link-prerender>; rel="prerender" ', false);
    header('Link: <https://adition.com/report/?leak=link-preload-as-font>; rel="preload" ; as="font"', false);
    header('Link: <https://adition.com/report/?leak=link-preload-as-image>; rel="preload" ; as="image"', false);
    header('Link: <https://adition.com/report/?leak=link-preload-as-style>; rel="preload" ; as="style"', false);
    header('Link: <https://adition.com/report/?leak=link-preload-as-script>; rel="preload" ; as="script"', false);
    header('Link: <https://adition.com/report/?leak=link-search>; rel="search" ', false);
    header('Link: <https://adition.com/report/?leak=link-alternate>; rel="alternate" ', false);
    header('Link: <https://adition.com/report/?leak=link-alternate-atom>; rel="alternate" ; type="application/atom+xml"', false);
    header('Link: <https://adition.com/report/?leak=link-alternate-stylesheet>; rel="alternate stylesheet" ', false);
    header('Link: <https://adition.com/report/?leak=link-amphtml>; rel="amphtml" ', false);
    header('Link: <https://adition.com/report/?leak=link-appendix>; rel="appendix" ', false);
    header('Link: <https://adition.com/report/?leak=link-apple-touch-icon-precomposed>; rel="apple-touch-icon-precomposed" ', false);
    header('Link: <https://adition.com/report/?leak=link-apple-touch-icon>; rel="apple-touch-icon" ', false);
    header('Link: <https://adition.com/report/?leak=link-apple-touch-startup-image>; rel="apple-touch-startup-image" ', false);
    header('Link: <https://adition.com/report/?leak=link-archives>; rel="archives" ', false);
    header('Link: <https://adition.com/report/?leak=link-author>; rel="author" ', false);
    header('Link: <https://adition.com/report/?leak=link-bookmark>; rel="bookmark" ', false);
    header('Link: <https://adition.com/report/?leak=link-canonical>; rel="canonical" ', false);
    header('Link: <https://adition.com/report/?leak=link-chapter>; rel="chapter" ', false);
    header('Link: <https://adition.com/report/?leak=link-chrome-webstore-item>; rel="chrome-webstore-item" ', false);
    header('Link: <https://adition.com/report/?leak=link-contents>; rel="contents" ', false);
    header('Link: <https://adition.com/report/?leak=link-copyright>; rel="copyright" ', false);
    header('Link: <https://adition.com/report/?leak=link-entry-content>; rel="entry-content" ', false);
    header('Link: <https://adition.com/report/?leak=link-external>; rel="external" ', false);
    header('Link: <https://adition.com/report/?leak=link-feedurl>; rel="feedurl" ', false);
    header('Link: <https://adition.com/report/?leak=link-first>; rel="first" ', false);
    header('Link: <https://adition.com/report/?leak=link-glossary>; rel="glossary" ', false);
    header('Link: <https://adition.com/report/?leak=link-help>; rel="help" ', false);
    header('Link: <https://adition.com/report/?leak=link-index>; rel="index" ', false);
    header('Link: <https://adition.com/report/?leak=link-last>; rel="last" ', false);
    header('Link: <https://adition.com/report/?leak=link-manifest>; rel="manifest" ', false);
    header('Link: <https://adition.com/report/?leak=link-mask-icon>; rel="mask-icon" ', false);
    header('Link: <https://adition.com/report/?leak=link-next>; rel="next" ', false);
    header('Link: <https://adition.com/report/?leak=link-offline>; rel="offline" ', false);
    header('Link: <https://adition.com/report/?leak=link-P3Pv1>; rel="P3Pv1" ', false);
    header('Link: <https://adition.com/report/?leak=link-pingback>; rel="pingback" ', false);
    header('Link: <https://adition.com/report/?leak=link-prev>; rel="prev" ', false);
    header('Link: <https://adition.com/report/?leak=link-publisher>; rel="publisher" ', false);
    header('Link: <https://adition.com/report/?leak=link-search-2>; rel="search" ; type="application/opensearchdescription+xml"', false);
    header('Link: <https://adition.com/report/?leak=link-sidebar>; rel="sidebar" ', false);
    header('Link: <https://adition.com/report/?leak=link-start>; rel="start" ', false);
    header('Link: <https://adition.com/report/?leak=link-section>; rel="section" ', false);
    header('Link: <https://adition.com/report/?leak=link-subsection>; rel="subsection" ', false);
    header('Link: <https://adition.com/report/?leak=link-subresource>; rel="subresource" ', false);
    header('Link: <https://adition.com/report/?leak=link-tag>; rel="tag" ', false);
    header('Link: <https://adition.com/report/?leak=link-up>; rel="up" ', false);*/
?>

<p id="message">header</p>
