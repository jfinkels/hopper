<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
    <head>
	<title>Named Entity Browser, <@c.fm metadata=query.metadata/></title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
	<script type="text/javascript" src="/js/hopper.js"></script>
    </head>
    <body>
	<div id="header">
	    <a id="logo" href="/hopper/"></a>
	    <div id="header_text">
		<h1>Named Entity Results<br/>
		<@c.fm metadata=query.metadata/>
		</h1>
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
   	 	<#if (resultCount > 0)>
   	 	<@c.widget id="sorting" title="Sorting">
   	 		<p>You can sort these results in two ways:</p>
			<dl>
			<#list sortMethods as method>
			<dt>
			<#if sortMethod == method>
				${sortTitles[method_index]} (current method)
			<#else>
				<a href="nebrowser?query=${queryString}&amp;sort=${method}&amp;order=${sortOrder}">${sortTitles[method_index]}</a>
			</#if>
			</dt>
			<dd>${sortDescriptions[method_index]}</dd>
			</#list>
			</dl>
			<#if (sortOrder == "asc")>
				<p>You are currently sorting in ascending order. <a href="nebrowser?query=${queryString}&amp;sort=${sortMethod}&amp;order=desc">Sort in descending order.</a></p>
			<#else>
				<p>You are currently sorting in descending order. <a href="nebrowser?query=${queryString}&amp;sort=${sortMethod}&amp;order=asc">Sort in ascending order.</a></p>
			</#if>
   	 	</@c.widget>
   	 	<@c.widget id="unique" title="Most Frequent Entities">
   	 	<p>The entities that appear most frequently in this document are shown below.</p>
   	 	<table class="data">
			<tr>
				<th>Entity</th>
				<th>Max. Freq</th>
				<th>Min. Freq</th>
				<th colspan="2"></th>
			</tr>
			<#list frequencies as freq>
			<#assign entity = freq.entity>
			<tr <#if (freq_index % 2 != 0)>class="odd"</#if>>
			    <td>${entity.displayName}</td>
			    <td><a href="nebrowser?id=${entity.authorityName?url}&amp;query=${freq.documentID}">${freq.maxFrequency}</a></td>
			    <td>${freq.minFrequency}</td>
			    <td><a href="nebrowser?mode=browse&amp;id=${entity.authorityName?url}">Browse</a></td>
			    <td><a href="nebrowser?id=${entity.authorityName?url}">Search</a></td>
			</tr>
			</#list>
			<tr class="view_all">
				<td colspan="5"><a href="nebrowser?mode=browse&amp;query=${queryString}">View all entities in this document...</a></td>
			</tr>
		</table>
   	 	</@c.widget>
   	 	</#if>
   	  </div> <#-- side_col -->
   	  
   	  <div id="main_col">
   	  <#if !query.isJustDocumentID()>
		<p>Browsing named entities in a specific section of <@c.fm metadata=query.metadata/>. 
			<a href="nebrowser?query=${query.documentID}&amp;sort=${sortMethod}&amp;order=${sortOrder}">Search the whole document.</a></p>
	  <#else>
			<p>Browsing named entities in <@c.fm metadata=query.metadata link="text?doc=${query?url}"/>.</p>
	  </#if>
	  <#if (resultCount > 0)>
	  	<p>Found ${resultCount} total hit<#if (resultCount > 1)>s</#if> in ${tupleCount} result<#if (tupleCount > 0)>s</#if>.</p>
	  	<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
		<div class="search_results">
	  	<#list tuples as tuple>
	  	<div class="search_widget primary">
		 		<div class="neheader">
		 		${tuple.entity.displayName} (<a href="nebrowser?id=${tuple.entity.authorityName?url}">search for this</a>): <a href="text?doc=${tuple.chunk.query}&amp;highlightauth=${tuple.entity.authorityName?url}#match1">${tuple.chunk.query.displayQuery}</a>
		 		</div> <#-- neheader -->
				<div class="contents">
					${renderer.render(tuple.snippet)}
				</div> <#-- contents -->
		</div> <#-- search_widget -->
	  	</#list>
	  	</div> <#-- search_results -->
	  	<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
	  <#else>
	  <p>Found no matches in this document. Please try again.</p>
	  </#if>
   	  </div> <#-- main_col -->
	  </div> <#-- content -->
	</div> <#-- main -->
	
<#include "analytics.ftl">
</body>
</html>
