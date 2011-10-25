<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
    <head>
	<title>Named Entity Browser</title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
	<script type="text/javascript" src="/js/hopper.js"></script>
    </head>
    <body>
	<div id="header">
	    <a id="logo" href="/hopper/"></a>
	    <div id="header_text">
		<h1>Named Entity Results<br/>
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
   	  </div> <#-- side_col -->
   	  <div id="main_col">
		<#if (hitCount > 0)>
		<p>Found ${hitCount} <#if (hitCount = 1)>entity<#else>entities</#if> matching your search. Click on a column heading to 
		sort by it, or to toggle ascending or descending sorting for the current column:</p>
		
		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
		<p class="help_note">Click on a number to browse all occurrences of that entity</p>
		    <table class="data">
		    <tr>
			<th><a href="${entityLink}">${entityText}</a></th>
			<th><a href="${hitsLink}">${maxHitsText}</a></th>
			<th><a href="${hitsLink}">${minHitsText}</a></th>
			<th></th>
		    </tr>
		    <#list results as entity>
		    	<#if entity.maxOccurrenceCount??>
		    		<tr <#if (entity_index % 2 != 0)>class="odd"</#if>
		    		<td>${entity.displayName}</td>
		    		<#if (entity.maxOccurrenceCount > 0)>
				    	<td><a href="nebrowser?mode=search&amp;id=${entity.authorityName?url}">${entity.maxOccurrenceCount}</a></td>
				    	<td>${entity.minOccurrenceCount}</td>
				    	<td><a href="nebrowser?mode=browse&amp;type=${objectType}&amp;id=${entity.authorityName?url}">Frequency by document</a></td>
				    <#else>
				    	<td colspan="3" style="text-align: center;"><em>no results for this entity</em></td>
				    </#if>
		    		</tr>
		    	</#if>
		    </#list>
			</table>
		<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
		<#else>
		<p>No matches were found for <b>${keyword}</b>, please try again.</p>
		</#if>
   	  </div> <#-- main_col -->
	  </div> <#-- content -->
	</div> <#-- main -->

<#include "analytics.ftl">
</body>
</html>