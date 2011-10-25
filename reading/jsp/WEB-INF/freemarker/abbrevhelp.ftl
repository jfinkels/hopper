<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Abbreviation Help</title>
</head>

<body>
<h4>Help with Abbreviations</h4>

<p>We reference documents using the standard scholarly abbreviations. Use any of the abbreviations below to go to the associated work. You will be taken to the document in the original language or in translation, depending on your settings.</p>

<p>To access a specific part of a document, add the relevant section/chapter/book information after the abbreviation (thus, to access the fifth chapter of the fourth book of Thucydides, enter <b>Thuc. 5.4</b>).</p>

<ul>
	<#list abbrevs as abbrev>
		<#if abbrev.ABO.metadata.title??>
		<li><a target="_new" href="text?doc=${abbrev.displayForm?url}">${abbrev.displayForm}</a>: <#if abbrev.ABO.metadata.creator??>${abbrev.ABO.metadata.creator}, </#if><cite>${abbrev.ABO.metadata.title}</cite></li>
	    </#if>
	</#list>
</ul>
<#include "analytics.ftl">
</body>
</html>
