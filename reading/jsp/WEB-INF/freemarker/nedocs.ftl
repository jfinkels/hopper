<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
    <head>
	<title>Named Entity Browser, ${title}</title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css">
    </head>
    <body>
    <div id="header">
	    <a id="logo" href="/hopper/"></a>
	    <div id="header_text">
		<h1>Named Entity Results, ${title}</h1>
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
   	  		<#if entityID??>
   	  			<p>Found occurrences of <strong>${entity.displayName}</strong> in ${hitCount} document<#if (hitCount > 1)>s</#if> in 
   	  			the collection. Click on a column heading to sort by it, or to toggle ascending or descending sorting for the 
   	  			current column:</p>
   	  		<#else>
   	  			<p>Found occurrences of ${hitCount} entity<#if (hitCount > 1)>s</#if> in this document. Click on a column 
   	  			heading to sort by it, or to toggle ascending or descending sorting for the current column:</p>
   	  		</#if>
   	  	<#else>
   	  		<p>Found no matches for this keyword. Please <a href="nebrowser">try again</a>.</p>
   	  	</#if>
   	  	<@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
			<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
		</@c.pager>
		<p class="help_note">Click on a number to view all occurrences of the entity in the selected document</p>
		    <table class="data" style="width:100%">
		    <tr>
		    <th><a href="${documentLink}">${documentText}</a></th>
			<th><a href="${hitsLink}">${maxHitsText}</a></th>
			<th><a href="${hitsLink}">${minHitsText}</a></th>
			<th colspan="2"></th>
		    </tr>
		    <#list frequencies as freq>
		    <#if entityID??>
		    	<#assign displayText>
		    	<@c.fm metadata=frequencyMetadata[freq_index] link="text?doc=${freq.documentID}"/>
		    	</#assign>
		    <#else>
		    	<#assign displayText=freq.entity.displayName>
		    </#if>
		    <tr <#if (freq_index % 2 != 0)>class="odd"</#if>>
				    <td>${displayText}</td>
				    <td><a href="nebrowser?query=${freq.documentID?url}&amp;id=<#if entityID??>${entityID?url}<#else>${freq.entity.authorityName?url}</#if>">${freq.maxFrequency}</a></td>
				    <td>${freq.minFrequency}</td>
				    <td><a href="nebrowser?mode=browse&amp;<#if entityID??>query=${freq.documentID?url}<#else>id=${freq.entity.authorityName?url}</#if>"><#if entityID??>Browse 
				    	all entities in this document<#else>Browse in all documents</#if></a></td>
				</tr>
		    </#list>
		    </table>
		    <@c.pager hits=pager.totalHits curpage=pager.currentPage pagesize=pager.pageSize ; thispage, text>
				<#if pager.currentPage != thispage><a href="${urlBuilder.withParameter("start", thispage).toString()}"></#if>${text}<#if pager.currentPage != thispage></a></#if>
			</@c.pager>
   	  </div> <#-- main_col -->
   	  </div> <#-- content -->
	</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>
