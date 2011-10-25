<#import "common.ftl" as c>

<#macro wordfreqbox languages language word="">
<form action="" name="f" onsubmit="return validate_form(this,lookup);">
	    View frequencies for <input name="lookup" value="${word!""}"> in 
	    <@c.langselect langs=languages def=language name="lang" onchange="toggleGreekDisplay(this)"/> 
	    <input type="submit" value="View" />
	</form>
	
	<div class="enter_greek" id="enter_greek"
    <#if language?? && language.name != 'Greek'>
    	style="display: none;"
    </#if>
	>
	How to enter text in Greek:<br />
	<img src="/img/keyCaps.gif" />
    </div>
</#macro>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<title>Word Frequency Information</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css">
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<div id="header">
	  <a id="logo" href="/hopper/"></a>
	  <div id="header_text">
		<h1>Word frequency information <#if displayWord??>for ${displayWord}</#if></h1>
	  </div>
	  <div id="header_side">
	  <#include "includes/head_search.ftl">
	  </div>
	</div>
<div id="main">
	<#include "includes/indexNav.ftl">
    <div id="content" class="1column">	
    
    <#if frequencyCounts??>
    <div id="side_col">
		<@c.widget id="search" title="Search">
			<@wordfreqbox word=word languages=languages language=language />
		</@c.widget>
   		<@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
		</div>
	
	<div id="main_col">
	<#if (frequencyCounts?size > 0)>
	<#if originalForm??>
		<p style="font-size: 14px">The form you searched for, <i>${displayOriginalForm}</i>, was not found as a dictionary entry. Did you mean <i>${displayWord}</i> instead?</p>
	</#if>

	<#if isStopWord><p style="color: red; font-size: small">You are viewing frequencies for a <a href="stopwords">stop word</a>, which will have no search results.</p></#if>
	<p class="help_note">Click on a column heading to sort by that field</p>

	<#assign link = "?lookup=${word}&amp;lang=${language.code}">
	<table class="data" cellspacing="0">
	    <tr>
		<th><a href="${link}&amp;sort=total">Words in Corpus</a></th>
		<th><a href="${link}&amp;sort=max">Max</a></th>
		<th><a href="${link}&amp;sort=max10k">Max/10k</a></th>
		<th><a href="${link}&amp;sort=min">Min</a></th>
		<th><a href="${link}&amp;sort=min10k">Min/10k</a></th>
		<th><a href="${link}&amp;sort=name">Corpus Name</a></th>
	    </tr>
	<#-- this is a list of [Frequency, WordCount] tuples -->
	<#list frequencyCounts as docStats>
		<#assign frequency = docStats[0]>
		<#assign wordCount = docStats[1].wordCount>
		<#assign metadata = docStats[1].query.metadata>
		<#assign documentID = docStats[0].documentID>
	
	    <#if docStats_index % 2 == 0>
	        <#assign rowClass = "even">
	    <#else>
	        <#assign rowClass = "odd">
	    </#if>

	    <#if metadata.type?? && metadata.type != "text">
	        <#assign targetPage = "collection"
	                 targetParam = "collection">
	    <#else>
	        <#assign targetPage = "text"
	                 targetParam = "doc">
	    </#if>

	    <tr class="${rowClass}">
		<td>#{wordCount}</td>
		<td><#if !isStopWord><a href="searchresults?q=${word}&amp;target=${language.code}&amp;doc=${frequency.documentID}&amp;expand=lemma&amp;sort=docorder" target="_blank"></#if>${frequency.maxFrequency}<#if !isStopWord></a></#if></td>
		<td>${frequency.getMaxPer10K(wordCount)}</td>
		<td>${frequency.minFrequency}</td>
		<td>${frequency.getMinPer10K(wordCount)}</td>
		<td class="work">
	            <#if targetPage == "text" && metadata.creator??>${metadata.creator}, </#if>
		    <a href="${targetPage}?${targetParam}=${documentID}">${metadata.title}</a>
		</td>
	    </tr>
	</#list>
	</table>
	<#else>
	<p>Sorry, we didn't recognize the form &ldquo;${word}&rdquo;.</p>
	</#if> <#-- checking if the size of frequencyCounts is greater than 0 -->
	</div> <#-- main_col -->
	<#else>
	<div id="main_col">
			<div id="search_box" style="text-align:center; width: 70%;">
			<@wordfreqbox languages=languages language=language />
			</div>
		</div>
	</#if>
    
    </div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>