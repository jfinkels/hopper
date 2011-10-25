<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
    <head>
	<title>Named Entity Browser, ${entity.displayName}</title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
	<script type="text/javascript" src="/js/hopper.js"></script>
    </head>
    <body>
	<div id="header">
	    <a id="logo" href="/hopper/"></a>
	    <div id="header_text">
		<h1>Named Entity Results, ${entity.displayName}</h1>
	    </div>
	        <div id="header_side">
			<#include "includes/head_search.ftl">
    </div>
	</div>
	<div id="main">
	  <#include "includes/indexNav.ftl">
	  <div id="content" class="2column">
    <div id="side_col">
    <#include "includes/nesearch.ftl">
    <@c.widget id="unique" title="Matching Documents">
    <p>The documents where this entity occurs most often are shown below.  Click on a document to open it. </p>
		<table class="data">
			<tr>
				<th>Document</th>
				<th>Max. Freq</th>
				<th>Min. Freq</th>
				<th colspan="2"></th>
			</tr>
			<#list frequencies as freq>
				<tr <#if freq_index % 2 != 0>class="odd"</#if>>
				<td>
					<@c.fm metadata=frequencyMetadata[freq_index] link="text?doc=${freq.documentID}"/>
				</td>
				<td><a href="nebrowser?id=${entity.authorityName?url}&amp;query=${freq.documentID}">${freq.maxFrequency}</a></td>
				<td>${freq.minFrequency}</td>
				<td><a href="nebrowser?mode=browse&amp;query=${freq.documentID}">Browse</a></td>
				<td><a href="nebrowser?query=${freq.documentID}">Search</td>
				</tr>
			</#list>
			<tr class="view_all">
				<td colspan="5"><a href="nebrowser?mode=browse&amp;id=${id?url}">View all matching documents...</a></td>
			</tr>   
		</table> 
    </@c.widget>
    </div> <#-- side_col -->
    
   	<div id="main_col">
   		<#if restrictingQuery?? && !restrictingQuery.isJustDocumentID()>
   			<p>Browsing named entities in a specific section of <@c.fm metadata=restrictingQuery.metadata/>. You can search for ${entity.displayName} 
				<a href="nebrowser?&amp;query=${restrictingQuery.documentID}&amp;id=${entity.authorityName?url}&amp;sort=${sortMethod}&amp;order=${sortOrder}">in 
   				the whole document</a> or <a href="nebrowser?id=${entity.authorityName?url}">in all documents</a>.</p>
   		<#elseif restrictingQuery??>
   			<p>Browsing named entities in <@c.fm metadata=restrictingQuery.metadata link="text?doc=${restrictingQuery}"/>. You can also <a href="nebrowser?id=${entity.authorityName?url}&amp;mode=browse">browse the 
				collection</a> for ${entity.displayName} or search for ${entity.displayName} <a href="nebrowser?id=${entity.authorityName?url}">in 
				all documents</a>.
			</p>
   		</#if>
   		<#if (sectionCount > 0)>
   			<p>Your search returned ${resultCount} result<#if (resultCount > 1)>s</#if> in ${sectionCount} document 
   			section<#if (sectionCount > 1)>s</#if>:</p>
   		<#else>
   			<p>Your search returned no results. Please try again.</p>
   		</#if>
   		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
		<div class="search_results">
		 <#list tuples as tuple>
		 	<#assign query=tuple.chunk.query>
		 	<div class="search_widget primary">
		 		<div class="neheader">
		 		<@c.fm metadata=query.metadata link="text?doc=${query}"/>, ${tuple.chunk.containingHeads} (<a href="nebrowser?query=${query}">search</a>)
				</div>
				<div class="contents">
					${renderer.render(tuple.snippet)}
				</div>
			</div> <#-- search_result -->
		 </#list>
		</div> <#-- search_results -->
		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
   	</div> <#-- main_col -->
   	</div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>
    