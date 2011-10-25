<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<#assign title = "English-to-${language.name} Word Search Results">
<html>
<head>
<title>${title}</title>
<link type="text/css" rel="stylesheet" href="/css/hopper.css" />
<link type="text/css" rel="stylesheet" href="/css/${prefs.greekDisplay}.css" />
<script type="text/javascript" src="/js/hopper.js"></script>
</head>

<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <h1>${title}
    <#if term??><br>for ${term}</#if></h1>
    <div id="header_side">
    <#include "includes/head_search.ftl">
    </div>
</div>
<div id="main">
	  <#include "includes/indexNav.ftl">
	  <div id="content" class="2column">
    <div id="side_col">
    <@c.widget id="search" title="Search">
    <form id="deflookup" action="" onsubmit="return validate_form(this,q,2);">
    <#assign types = ["begin", "end","exact", "substring"]>
	<#assign typeTitles = ["words beginning with", "words ending with", "the exact word", "words containing"]>
        Search for <select name="type">
        <#list types as typeVal>
		  		<option value="${typeVal}"<#if typeVal == searchType> selected="selected"</#if>>${typeTitles[typeVal_index]}</option>
		  	</#list>
        </select>
        <input name="q" value="${term!""}">
         in 
        <@c.langselect langs=languages def=language name="target" /> dictionary definitions
        <input type="submit" value="Search" />
    </form>
    </@c.widget>
   	
   	<@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
    </div>
    <div id="main_col">
    <#if results??>
        <div id="search_results">
            <#if results.totalHitCount == 0>
                <p>Your search for <strong>${term}</strong> returned no results.</p>
            <#else>
                <p>Your search returned ${results.totalHitCount} matching entries.</p>
		    <@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		    </@c.pager>
                <table class="data">
                    <tr>
                        <#assign lang = language.code>
                        <th><a href="?q=${term?url}&amp;sort=alpha&amp;target=${lang?url}">Headword</a></th>
                        <th>Dictionaries</th>
                        <th><a href="?q=${term?url}&amp;sort=freq&amp;target=${lang?url}">Max. Inst.</a></th>
                        <th><a href="?q=${term?url}&amp;sort=freq&amp;target=${lang?url}">Min. Inst.</a></th>
                        <th><a href="?q=${term?url}&amp;sort=freq&amp;target=${lang?url}"># Documents</a></th>
                        <th>Short Definition</th>
                    </tr>
                    <#list results.hits as result>
                        <#assign lemma = result.content>
                        <#if (lemma.sequenceNumber > 1)>
							<#assign seq=lemma.sequenceNumber>
						<#else>
							<#assign seq="">
						</#if>
                        <tr <#if result_index % 2 != 0>class="odd"</#if>>
                            <td>${renderer.renderText(lemma.headword+seq)}</td>
                            <td><#list result.content.lexiconQueries as lexQuery><a href="text?doc=${lexQuery?url}&amp;highlight=${term}">${lexQuery.metadata.alternativeTitle}</a><#if lexQuery_has_next>, </#if></#list></td>
			    			<td><a href="searchresults?all_words=${lemma.displayForm?url}&amp;all_words_expand=yes&amp;la=${lemma.language.code?url}">${lemma.maxOccurrenceCount}</a></td>
			    			<td>${lemma.minOccurrenceCount}</td>
			    			<td>${lemma.documentCount}</td>
                            <td>${result.content.shortDefinition!"<em>[unavailable]</em>"}</td>
                        </tr>
                    </#list>
                </table>
                <@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
                    <#if pager.currentPage != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
                </@c.pager>
            </#if>
        </div> <#-- search_results -->
    </#if>
    </div> <#-- main_col -->
    </div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>
