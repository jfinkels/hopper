<#import "common.ftl" as c>

<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Perseus Stop Words</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css">
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
<link type="text/javascript" href="/js/hopper.js" />
</head>
<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Stop Words</h1>
    </div>
    <div id="header_side">
	<#include "includes/head_search.ftl">
    </div>
</div>
<div id="main">
	<#include "includes/indexNav.ftl">
    <div id="content" class="2column">
	<div id="side_col">
	</div>
	<div id="index_main_col">
            <p>The following words will not return any results:
            <ul>
                <#list languages as lang>
                    <#if lang.hasStoplist()>
                        <li><strong>${lang.name}</strong>: <@c.join l=lang.stoplist sep=", " /></li><br>
                    </#if>
                </#list> 
            </ul>
	</div>
    </div>
</div>
<#include "analytics.ftl">
</body>
</html>
