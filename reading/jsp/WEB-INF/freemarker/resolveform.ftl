<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<#assign title="${language.name} Dictionary Headword Search Results"/>

<html>
  <head>
	<title>${title}</title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
	<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
	<script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
	<div id="header">
	  <a id="logo" href="/hopper/"></a>
	  <div id="header_text">
		<h1>${title}</h1>
	  </div>
	  <div id="header_side">
	  <#include "includes/head_search.ftl">
	  </div>
	</div>
	<div id="main">
		  <#include "includes/indexNav.ftl">
	  <div id="content" class="1column">
	  <#if results??>
		<div id="side_col">
		<@c.widget id="search" title="Search">
		<@c.resolveformbox lookupForm=lookupForm languages=languages language=language matchMode=matchMode/>
		</@c.widget>
   	<@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
		</div>
	<div id="main_col">
		  <div id="results">
			<#if results?size &gt; 0>
				<p>Your search returned ${totalResults} <#if totalResults == 1>result<#else>results</#if>.</p>
		    		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
					<#if pager.currentPage != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		    		</@c.pager>
			  <table class="data">
				<tr>
				  <th>Lemma</th>
				  <th>Dictionaries</th>
				  <th>Max. Freq.</th>
				  <th>Min. Freq.</th>
				  <th>Short Definition</th>
				</tr>
				<#list results as lemma>
				    <tr <#if lemma_index % 2 != 0>class="odd"</#if>>
					<td>
					<#if (lemma.sequenceNumber > 1)>
						<#assign seq=lemma.sequenceNumber>
						<#else>
						<#assign seq="">
					</#if>
					${renderer.renderText(lemma.headword+seq)}
					</td>
					<td>
					  <#if lemma.lexiconQueries?size &gt; 0>
						<#list lemma.lexiconQueries as query><a href="text?doc=${query?url('utf-8')}">${query.metadata.alternativeTitle}</a><#if query_has_next>, </#if></#list>
					  </#if>
					</td>
					<td><a href="searchresults?all_words=${lemma.displayForm}&amp;all_words_expand=yes&amp;la=${lemma.language.code}">${lemma.maxOccurrenceCount}</a></td>
					<td>${lemma.minOccurrenceCount}</td>
					<td>${lemma.shortDefinition!"<em>[unavailable]</em>"}</td>
				  </tr>
				</#list>
			  </table>
		    		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
					<#if pager.currentPage != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		    		</@c.pager>
			  <#else>
				<p>Your search for <strong>${lookupForm}</strong> returned no results.</p>
				<p>This may be an inflected form of the word. To find the possible dictionary entry forms of this word, check the word study tool for <a target="_blank" href="morph?l=${lookupForm}&la=${language.code}">${lookupForm}</a>.
			  </#if>
			</div> <#-- results -->
		</div> <#-- main_col -->
		<#else>
		<div id="main_col">
			<div id="search_box" style="text-align:center; width: 70%;">
			<@c.resolveformbox languages=languages language=language matchMode=matchMode />
			</div>
		</div>
		</#if>
	  </div> <#-- content -->
	</div> <#-- main -->
	
<#include "analytics.ftl">
  </body>
</html>
