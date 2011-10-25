<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
<head>
<title>Perseus Search Results</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css">
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
<script type="text/javascript" src="/js/searchresults.js"></script>
</head>
<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Search Results</h1>
    </div>
    <div id="header_side">
	<#include "includes/head_search.ftl">
    </div>
</div>  <#-- header -->
<div id="main">
	  <#include "includes/indexNav.ftl">
      <div id="content" class="2column">
	<div id="side_col">
	<#if metadataSearch>
		<@c.widget id="metadata" title="Refine This Search">
			<form action="?" method="get">
			Search for a new term: <input type="text" name="q" value="${searchTerm!""}"/>
			<br/>
			<input type="submit" value="Refine search" />
			</form>
		</@c.widget>
	<#else>
    <@c.widget id="refine" title="Refine This Search">
		<form action="?" method="get">
		    <#assign sr = corpusResults.searcher>

		    <table>
			<tr><td>Language:</td><td><@c.langselect langs=languages def=sr.language /></td></tr>
                        <tr><td>Required words:</td><td><input name="all_words" type="text" value="<@c.join l=sr.requiredWords />" /></td><td><input type="checkbox" name="all_words_expand"<#if sr.expandRequiredWords>checked="checked" </#if>/> Expand</td></tr>
			<tr><td>Required phrase:</td><td><input name="phrase" type="text" value="${sr.requiredPhrase!""}" /></td></tr>
			<tr><td>Allowed words:</td><td><input name="any_words" type="text" value="<@c.join l=sr.allowedWords />" /></td><td><input type="checkbox" name="any_words_expand" <#if sr.expandAllowedWords>checked="checked" </#if>/> Expand</td></tr>
			<tr><td>Excluded words:</td><td><input name="exclude_words" type="text" value="<@c.join l=sr.excludedWords />" /></td><td><input type="checkbox" name="exclude_words_expand" <#if sr.expandExcludedWords>checked="checked" </#if>/> Expand</td></tr>
		    </table>

		    <input type="hidden" name="documents" value="<@c.join l=sr.targetDocuments />" />
		    <input type="submit" value="Refine search" />
			<br />
			<#if corpusResults?? && corpusResults.totalHitCount &gt; 0>
			    (This searches within the currently selected documents. To search within all documents, <a href="#frequencies">use the form below</a>.)
			</#if>
		</form>
	    </@c.widget>

	    <#-- <#if corpusResults.totalHitCount &gt; 0>
		<@c.widget id="sorting" title="Sorting">
		    <p>You can sort search results in following ways:</p>
		    <ul>
			<#--
			<li>Group by work - returns all matching works and a sample of several chunks from each one.</li>
			-->
			<#-- <#assign rel = corpusResults.searcher.sortByRelevance>
			<li><strong><#if !rel><a href="${builder.withParameter("sort", "rel")}"></#if>"Relevance"<#if !rel></a></#if></strong> - sorts the document sections that matched your query by the proportion of the words in that section that are search terms.  Sorting by this measure can help identify passages that are particularly relevant to your query, but can disproportionally benefit short sections. This is a good choice if your query returns many documents.</li>

			<li><strong><#if rel><a href="${builder.withParameter("sort", "docorder")}"></#if>Document order<#if rel></a></#if></strong> - sorts the document sections that matched your query by the order in which they appear in the collection. Sorting by document order works best when you want to systematically scan every instance of a word in the collection.</li>
		    </ul>
		</@c.widget>
	    </#if> -->

            <#if metadataResults?? && metadataResults?size &gt; 0>
            <@c.widget id="metadata" title="Relevant Works (${metadataResults?size})" hidden=true>
                    <ul>
                        <#list metadataResults as match>
                            <li><@c.fm metadata=match ed=true lang=true link="text?doc=${match.documentID}" /></li>
                        </#list>
                    </ul>
                </@c.widget>
            </#if>

	    <#if corpusResults.documentFrequencies?? && corpusResults.documentFrequencies?size &gt; 0>
		<@c.widget id="frequencies" title="All Matching Documents (${corpusResults.documentFrequencies?size})" hidden=true>
		    <form action="#" method="get">
			<p>You can narrow your query by clicking on the texts you wish to search below. Alternatively, <a href="${builder.withParameter("documents", "")}">search all documents</a>.</p>
			<table class="data">
			    <tr>
				<th>Search?</th>
				<th>Text</th>
				<th>Results</th>
			    </tr>
			    <#list corpusResults.documentFrequencies?keys?sort as docID>
					<#assign query = idsToQueries[docID]>
					<#assign metadata = query.metadata>
				<tr class="<#if docID_index % 2 == 0>odd<#else>even</#if>">
				    <td>
					<input type="checkbox" name="documents" value="${query}"<#if (corpusResults.targetDocuments?seq_contains(query))> checked="checked"</#if> />
				    </td>
				    <td><@c.fm metadata=metadata ed=false /></td>
				    <td><a href="${builder.withParameter("documents", query.toString()).withParameter("page", "1")}">${corpusResults.documentFrequencies[docID].get()}</a></td>
				</tr>
			    </#list>
			</table>
			<input type="hidden" name="language" value="${corpusResults.searcher.language.code}" />
			<input type="hidden" name="inContent" value="true" />
			<input name="all_words" type="hidden" value="<@c.join l=sr.requiredWords />" /><input type="submit" value="Search only selected texts" />
			<#if sr.expandRequiredWords>
				<input type="hidden" name="all_words_expand" value="true">
			</#if>

			<#if sr.requiredPhrase?? && sr.requiredPhrase != "">
				<input name="phrase" type="hidden" value="${sr.requiredPhrase!""}" />
			</#if>

			<input name="any_words" type="hidden" value="<@c.join l=sr.allowedWords />" />
			<#if sr.expandAllowedWords>
				<input type="hidden" name="any_words_expand" value="true">
			</#if>

			<input name="bad_words" type="hidden" value="<@c.join l=sr.excludedWords />" />
			<#if sr.expandExcludedWords>
				<input type="hidden" name="bad_words_expand" value="true">
			</#if>
			</form>
			
			<form action="#" method="get">
				<input type="hidden" name="language" value="${corpusResults.searcher.language.code}" />
				<input type="hidden" name="inContent" value="true" />
				<input name="all_words" type="hidden" value="<@c.join l=sr.requiredWords />" />
				<#if sr.expandRequiredWords>
					<input type="hidden" name="all_words_expand" value="true">
				</#if>

				<#if sr.requiredPhrase?? && sr.requiredPhrase != "">
					<input name="phrase" type="hidden" value="${sr.requiredPhrase!""}" />
				</#if>

				<input name="any_words" type="hidden" value="<@c.join l=sr.allowedWords />" />
				<#if sr.expandAllowedWords>
					<input type="hidden" name="any_words_expand" value="true">
				</#if>

				<input name="bad_words" type="hidden" value="<@c.join l=sr.excludedWords />" />
				<#if sr.expandExcludedWords>
					<input type="hidden" name="bad_words_expand" value="true">
				</#if>
				<input type="submit" value="Search all documents" />
			</form>

		</@c.widget>
	    </#if>

	    <#if lemmaResults?? && lemmaResults.totalHitCount &gt; 0>
		<@c.widget id="lemma" title="Matching Lemmas (${lemmaResults.totalHitCount})">
		    <ul>
			<#list lemmaResults.hits as result>
			<li>${result.title}<#if result.content.shortDefinition??>: "${result.content.shortDefinition}"</#if> (entry in <#list result.lexiconQueries as lexQuery> <a href="text?doc=${lexQuery}">${lexQuery.metadata.alternativeTitle}</a></#list>)</li>
			</#list>
		    </ul>
		</@c.widget>
	    </#if>

	    <#if entityResults?? && entityResults.totalHitCount &gt; 0>
		<@c.widget id="entity" title="Matching Entities (${entityResults.totalHitCount})">
		    <table class="data">
			<tr>
			    <th>Entity</th>
			    <th># Hits</th>
			    <th>&nbsp;</th>
			</tr>
			<#list entityResults.hits as result>
			<tr>
			    <td>${result.title}</td>
			    <td><a href="nebrowser?id=${result.identifier}">${result.content.maxOccurrenceCount}</a></td>
			    <td><a href="nebrowser?mode=browse&amp;id=${result.identifier}">Browse</a></td>
			</tr>
			</#list>
		    </table>
		    <#if entityResults.totalHitCount &gt; entityResults.returnedHitCount>
		    <a class="view_rest" href="nebrowser?keyword=${searchTerm}">View all ${entityResults.totalHitCount}...</a>
		    </#if>
		</@c.widget>
	    </#if>
	    
	    <#if artifactResults?? && (artifactResults.totalHitCount > 0)>
	    <@c.widget id="artifact" title="Matching Artifacts (${artifactResults.totalHitCount})">
	    	<#list artifactResults.hits as result>
	    	<a href="artifact?name=${result.content.name?url}&amp;object=${result.content.type?url}">${result.content.name}</a> 
	    		<em>[${result.content.type}]</em><br/>
	    	</#list>
	    	<br/>
	    	<form action="artifactSearch" method="get" target="_blank">
	    	<input type="hidden" name="q" value="${searchTerm}"/>
	    	<input type="hidden" name="artifact" value="yes"/>
				<#list artifactResults.artifactCounts?keys?sort as artifactType>
					<input type="checkbox" name="artifactType" value="${artifactType}" checked/>${artifactType} (${artifactResults.artifactCounts[artifactType]})
				</#list>
				<br/><input type="submit" value="View"/>
	    	</form>
	    </@c.widget>
	    </#if>
	    
	    <#if imgResults?? && (imgResults.totalHitCount > 0)>
	    <@c.widget id="image" title="Matching Images (${imgResults.totalHitCount})">
	    <table width="100%"><tr>
	    	<#list imgResults.hits as result>
	    	<#assign img=result.content>
	    	<td>
	    	<div class="thumbnail">
			<p>
			<#if img.status == 5>
				<i>[Image not available]</i>
			<#else>
				<#if img.isRestricted>
					<img class="thumb_img" src="${img.thumbURL}">
				<#else>
					<a href="/hopper/image?img=${img.archiveNumber?url}"><img class="thumb_img" src="${img.thumbURL}"></a>
				</#if>
			</#if>
			</p>
				<div class="caption">
				<#if img.isRestricted>
					<a href="/hopper/help/copyright.jsp#images">Image access restricted</a><br>
				</#if>
				<#assign imgCaption=img.caption>
				<#assign captionLength=75>
				<#if (imgCaption?length > captionLength)>
					<#assign imgCaption=imgCaption?substring(0, captionLength)+"...">
				</#if>
				${imgCaption}
				</div> <#-- caption -->
			</div><#-- thumbnail -->
			</td>
	    	</#list>
	    </tr></table>
	    <br/><a href="artifactSearch?q=${searchTerm}&amp;image=yes">View more images...</a>
	    </@c.widget>
	    </#if>

	    <#if lexiconResults?? && lexiconResults.totalHitCount &gt; 0>
		<@c.widget id="lexicon" title="Matching Dictionary Entries (${lexiconResults.totalHitCount})">
		    <ul>
			<#list lexiconResults.hits as result>
			<li>${result.title}<#if result.content.shortDefinition??>: "${result.content.shortDefinition}"</#if> (entry in <#list result.content.lexiconQueries as lexQuery> <a href="text?doc=${lexQuery}">${lexQuery.metadata.alternativeTitle}</a></#list>)</li>
			</#list>
		    </ul>
		    <#if lexiconResults.totalHitCount &gt; lexiconResults.returnedHitCount>
		    <a href="definitionlookup?q=${searchTerm}">View all ${lexiconResults.totalHitCount}...</a>
		    </#if>
		</@c.widget>
	    </#if>
	</#if>
	</div> <#-- side_col -->
	<div id="main_col">
	<#if metadataSearch>
		<b>Your search returned ${metadataResults?size} author and title results.</b>
		<br/>
		<span style="font-size:small">If you would like to search the content of all documents, please click <a href="searchresults?q=${searchTerm}&amp;inContent=true&amp;language=${language}">here</a>.</span>
		<br/>
		<ul>
        	 <#list metadataResults as match>
             	<li><@c.fm metadata=match ed=true lang=true link="text?doc=${match.documentID}" /></li>
             </#list>
        </ul>
	<#else>
		<#if corpusResults.targetDocuments?size &gt; 0>
			<div id="currently_searching" class="info">
				Currently searching the following texts in ${sr.language.name}:
				<ul>
					<#list corpusResults.targetDocuments as doc>
						<li><@c.fm metadata=doc.metadata link="text?doc=${doc}" ed=true /></li>
					</#list>
				</ul>
			</div>
		<#elseif corpusResults.totalHitCount &gt; 0>
		</#if>
		
        <#if corpusResults.totalHitCount &gt; 0>
		<#-- To prevent it from saying results 1500 - 1700 of 1642 results... -->
			<#if page*pagesize < corpusResults.documentFrequencies?size>
        		<p>Showing <b>${(page-1)*pagesize +1} - ${page*pagesize}</b> of <b>${corpusResults.documentFrequencies?size}</b> document results in <b>${sr.language.name}</b>.</p>
			<#else>
				<p>Showing <b>${(page-1)*pagesize +1} - ${corpusResults.documentFrequencies?size}</b> of <b>${corpusResults.documentFrequencies?size}</b> document results in <b>${sr.language.name}</b>.</p>
			</#if>
            <@c.pager hits=corpusResults.documentFrequencies?size curpage=page pagesize=pagesize ; thispage, text>
            	<#if page != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if page != thispage></a></#if>
            </@c.pager>
		<#else>
			<p>Your search returned no results.</p>
            <p>Not sure why?</p>
            <ul>
            	<li>Make sure all words are spelled correctly. If you're searching for all instances of a particular dictionary entry, and not just a specific form, make sure you checked the "Expand" checkbox.</li>
                <li>You may have searched for one or more "stop words"--words that are considered too common to be indexed. <a href="stopwords">View all stop words...</a></li>
            </ul>
       	</#if>
	<#-- Only print table if results greater than 0 -->
	<#if corpusResults.totalHitCount &gt; 0>
	<#assign entryCount=0 >
	<table cellpadding="2" cellspacing="0" style="width:100%">
		<#list allResults as similarResults>
			<#assign escapedDocID=(similarResults[0].query.containingQuery?replace(":","")?replace(".",""))>
			<#if (similarResults?size < 2) >
			<#-- If there is only one entry, then print it out alone -->
				<tr class="trResultTitleBar">
					<td><b><@c.fm metadata=similarResults[0].query.metadata /></b></td>
					<td style="text-align:right"></td>
				</tr>
				<tr class="trResultEditorBar">
					<td colspan="2"><b>(${similarResults[0].query.metadata.language.name})</b>
		    			<#list similarResults[0].translations as trans>
						(<a href="text?doc=${trans.query}">${trans.metadata.language.name}<#if trans.metadata.contributor??>, ed. ${trans.metadata.contributor}</#if></a>)
					</#list></td>
				</tr>
				<tr height="8px" style="background-color:#eeeeee;"><td colspan="2"></td></tr>
				<tr class="trSearchResults">
					<td colspan="2"><a href="text?doc=${similarResults[0].identifier}&amp;highlight=${similarResults[0].matchingTokenString}">${similarResults[0].title}</a>:&nbsp;${similarResults[0].renderedText}</td>
				</tr>
			<#else>
			<#-- Otherwise, print the first one, and hide the rest of the results -->			
				<tr class="trResultTitleBar">
					<td><b><#if similarResults[0].query.metadata.creator??>${similarResults[0].query.metadata.creator},&nbsp;</#if><i>${similarResults[0].query.metadata.title}</i></b></td> 
					<td style="text-align:right; font-size:12px"><span id="${escapedDocID},0,${similarResults?size}" 
						class="spanExpander" onclick="expandContract('${escapedDocID}',${similarResults?size})">More(${similarResults?size})</span></td>
				</tr>
				<tr class="trResultEditorBar">
					<td colspan="2"><b>(${similarResults[0].query.metadata.language.name})</b>
		    			<#list similarResults[0].translations as trans>
						(<a href="text?doc=${trans.query}">${trans.metadata.language.name}<#if trans.metadata.contributor??>, ed. ${trans.metadata.contributor}</#if></a>)
					</#list></td>
				</tr>
				<tr height="8px" style="background-color:#eeeeee;"><td colspan="2"></td></tr>
				<tr id="${escapedDocID},0,0" class="trSearchResults">
					<td colspan="2"><a href="text?doc=${similarResults[0].identifier}&amp;highlight=${similarResults[0].matchingTokenString}">${similarResults[0].title}</a>:&nbsp;${similarResults[0].renderedText}</td>
				</tr>
				<#-- Rest of the Results -->
				<#list similarResults as entry>
				<#assign entryCount=entryCount + 1>
				<tr id="${escapedDocID},${entryCount},0" class="trHiddenSearchResults" style="display:none">
					<td colspan="2"><a href="text?doc=${entry.identifier}&amp;highlight=${entry.matchingTokenString}">${entry.title}</a>:&nbsp;${entry.renderedText}</td>
				</tr>
				</#list>
			</#if>
			<tr class="trBottom"><td colspan="2"></td></tr>
			<tr height="10px"><td colspan="2"></td></tr>
			<#assign entryCount=0>
		</#list>
		</table>
	</#if>
		<#if corpusResults.totalHitCount &gt; 0>
			<@c.pager hits=corpusResults.documentFrequencies?size curpage=page pagesize=pagesize ; thispage, text>
			<#if page != thispage><a href="${builder.withParameter("page", thispage).toString()}"></#if>${text}<#if page != thispage></a></#if>
			</@c.pager>
        </#if>
   	</#if>
	</div> <#-- main_col -->
    </div> <#-- content -->
</div> <#-- main -->

<#include "analytics.ftl">
</body>
</html>
