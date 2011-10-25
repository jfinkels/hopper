<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
<head>
<title>
<#if metadata.creator??>${metadata.creator}, </#if>
${metadata.title},
${head}
</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/css/hopper-print.css" media="print"/>
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
<link rel="stylesheet" type="text/css" href="/css/sidetoc.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/css/navbar.css" media="screen"/>
<script type="text/javascript" src="/js/hopper.js"></script>
<script type="text/javascript" src="/js/text.js"></script>
</head>
<body onload="checkRedirect();">

<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>
	<#if metadata.creator??>${metadata.creator}, </#if>
	<span class="title">${metadata.title}</span>
	<br />
	<#if metadata.contributor??>${metadata.contributor}, Ed.</#if>
	</h1>
    </div>
    <div id="header_side">
	  <#include "includes/head_search.ftl">
    </div>
</div>
<div id="main">
	<#include "includes/indexNav.ftl">
	<#assign showBrowseBar = (prefs.navbar == "show")>
	<div id="content" class="3column" style="margin-right:0px;">
	<div id="navbar_wrapper" style="width:100%" <#if !showBrowseBar>class="nodisplay"</#if>>
	    <p id="navbar_help" style="background: #ccc;">
		<span style="float: right; font-size: x-small; margin-right:5px;"><a href="javascript:hideNavbar();">Hide browse bar</a></span>
		<span id="navbar_title" style="font-size: x-small;">Your current position in the text is marked in blue. Click anywhere in the 
		line to jump to another position:</span>
	    </p>
	    ${navbar}
	</div> <#-- navbar_wrapper -->
	<div id="left_col">
	    <div class="sidebox" id="collections">
		<h5>This text is part of:</h5>
		<ul>
		<#list collections as collection>
		<li><a href="collection?collection=${collection?url}">${collection.metadata.title}</a></li>
		</#list>
		</ul>
		</div> <#-- collection -->
		<#if displayChunkSchemes>
			<div class="sidebox" id="chunking">
			<h5>View text chunked by:</h5>
			<ul>
			<#list chunkSchemes?keys as scheme>
				<li>
				<#list chunkSchemes[scheme] as type>
				<a href="disppref?url=/hopper/text?doc=${query.innermostDocumentID?url}&amp;default.scheme=${scheme?url}&amp;default.type=${type?url}">${displayNames[type]}</a>
				<#if (type_index < chunkSchemes[scheme]?size-1)>
				:
				</#if>
				</#list>
				</li>
			</#list>
			</ul>
			</div> <#-- chunking -->
		</#if>
		<div class="sidebox" id="toc">
		<h5>Table of Contents:</h5>
		${styledToc}
		<a class="xml" href="xmltoc?doc=${query?url}" onclick="javascript: pageTracker._trackPageview('/hopper/xmltoc?doc=${query?url}');">
		    <img src="/img/xml.gif" alt="view as XML" border="0" />
		</a>
		</div> <#-- toc -->
	</div> <#-- left_col -->
	
	<div id="center_col">
	    <div id="header_nav">
	    <#if previousChunk??>
	    	<a class="arrow" href="text?doc=${previousChunk.query?replace("+","%2B")?url}">
				<img src="/img/prev.gif" alt="previous" border="0" />
   		 	</a>
	    </#if>
	    <#if nextChunk??>
	    	<a class="arrow" href="text?doc=${nextChunk.query?replace("+","%2B")?url}">
				<img src="/img/next.gif" alt="next" border="0" />
   		 	</a>
	    </#if>
	    <form id="header_jump_form" action="">
		    <input name="doc" value="${query.displayCitation}" onmouseover="showJumpHelp();" onmouseout="hideJumpHelp();" onclick="hideJumpHelp();"/>
		    <div id="jumphelp" style="display:none">
		    	Current location in this text.  Enter a Perseus citation to go to another section or work. Full search 
		    	options are on the right side and top of the page.
		    </div>
		    <input type="hidden" name="fromdoc" value="${query.documentID}" />
		</form>
	    </div> <#-- header_nav -->
	    <div id="text_main">
	    <script type="text/javascript">
			addDocument('${query}');
		</script>
		<#if lang.hasMorphData>
			<p class="help_note">Click on a word to bring up parses, dictionary entries, and frequency statistics</p>
		</#if>
		<#if truncateChunk && !force??>
			<p class="help_note">${percentOfText}% of the text is displayed below. If you wish to
		view the entire text, please click <a href="text?doc=${matchingChunk.query?url}&force=y">here</a></p>
		</#if>
		<#if rightJustify>
			<div align='right'>
		</#if>
		${renderedTokens}
		<#if rightJustify>
			</div>
		</#if>
	    </div> <#-- text_main -->
	    <div id="text_footer">
		<div id="text_desc">
			<#list metadata.sourceList as source>
				<#if !source?starts_with("Perseus:bib")>
					${source}
				</#if>
			</#list>		
			<#if metadata.funder??>
				<p>${metadata.funder} provided support for entering this text.</p>
			</#if>
			<#if metadata.segmentation??>
			<p>${metadata.segmentation}</p>
			</#if>
			<#if metadata.correctionLevel??>
				<#if metadata.method??>
					<p>This text was converted to electronic form by ${metadata.method} and has been proofread to a <span class="correction_level">${metadata.correctionLevel}</span> level of accuracy.</p>
				<#else>
					<p>This text has been proofread to a <span class="correction_level">${metadata.correctionLevel}</span> level of accuracy.</p>
				</#if>
			<#elseif metadata.method??>
				<p>This text was converted to electronic form by ${metadata.method}.</p>
			</#if>
			<#if truncateChunk && !force??>
			<p class="help_note">${percentOfText}% of the text is displayed below. If you wish to
			view the entire text, please click <a href="text?doc=${matchingChunk.query?url}&force=y">here</a></p>
			</#if>
			<#if metadata.ISBN??>
			<p>Purchase a copy of this text (not necessarily the same edition) from 
      <a href="http://www.amazon.com/gp/product/${metadata.ISBN?url}?ie=UTF8&tag=theperseusprojec&linkCode=as2&camp=1789&creative=9325&creativeASIN=${metadata.ISBN?url}">Amazon.com</a>
		<img src="http://www.assoc-amazon.com/e/ir?t=theperseusprojec&l=as2&o=1&a=${metadata.ISBN}"
		width="1" height="1" border="0" alt="" style="border:none
		!important; margin:0px !important;" /></p>
			</#if>
		</div> <#-- text_desc -->
		<div id="footer_nav">
		    <a class="xml" href="xmlchunk?doc=${query?url}" onclick="javascript: pageTracker._trackPageview('/hopper/xmlchunk?doc=${query?url}');">
			<img src="/img/xml.gif" alt="view as XML" border="0" />
		    </a>
		    <#if previousChunk??>
	    	<a class="arrow" href="text?doc=${previousChunk.query?url}">
				<img src="/img/prev.gif" alt="previous" border="0" />
   		 	</a>
	   	 	</#if>
	    	<#if nextChunk??>
	    	<a class="arrow" href="text?doc=${nextChunk.query?url}">
				<img src="/img/next.gif" alt="next" border="0" />
   		 	</a>
	    	</#if>
		</div> <#-- footer_nav -->
		</div> <#-- text_footer -->
		<#if metadata.isDownloadable()>
			<div class="rights_info">
			<#--Creative Commons License-->
			<p class="cc_rights">
				<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/us/">
				<img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png" />
				</a><br />This work is licensed under a 
				<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/us/">Creative Commons Attribution-ShareAlike 3.0 United States License</a>.
			</p>
		<p class="xml_download">An <a href="dltext?doc=${query.documentID?url}">XML version</a> of this text is available for download, 
		with the additional restriction that you offer Perseus any modifications you make. Perseus provides credit for all accepted 
		changes, storing new additions in a versioning system.</p>
	    </div> <#-- rights_info -->
		</#if>
	</div> <#-- center_col -->
	
	<div id="right_col">
	    <div class="widget secondary<#if showBrowseBar> nodisplay</#if>" id="navbar_placeholder">
		<div class="header">
		    <a class="toggle" href="javascript:showNavbar();">show</a>
		    Browse Bar
		</div>
		<div class="contents" style="display: none;">
		    &nbsp;
		</div>
	    </div>
	    <#if isLexicon>
	    	<@c.widget id="entrylookup" title="Dictionary Entry Lookup">
	    		Use this tool to search for dictionary entries in all lexica.
	    		<@c.resolveformbox languages=lookupLangs language=lexLang matchMode="exact"/>
	    	</@c.widget>
	    </#if>
	    <#-- start at one because we've already added main text to documents array -->
	    <#assign resourcesDisplayed = 1>
	    <#list resources?keys as type>
	    	<#assign resourceChunks = resources[type]>
	    	<#assign idPrefix = type?lower_case>
	    	<#list resourceChunks as resource>
	    	<#assign resourceID = idPrefix+resource_index>
	    		<script type="text/javascript">
					addDocument('${resource.query}');
				</script>
				<div class="widget primary" id="${resourceID}">
	    			<div class="header">
	    			<a class="toggle" href="javascript:loadXMLDoc('${resourceID}', '${resource.query}', ${resourcesDisplayed});" 
	    			id="${resourceID}-link">load</a>
					<a class="toggle" href="text?doc=${resource.query?url}">focus</a>
					${formattedResourceChunks[resource]}
					</div> <#-- header -->
					<div class="contents" id="${resourceID}-contents" style="display: none">
						<div class="text" id="${resourceID}-text">
						</div> <#-- text -->
					</div> <#-- contents -->
				</div> <#-- widget -->
				<#assign resourcesDisplayed = resourcesDisplayed + 1>
	    	</#list>
	    </#list>
	    <#-- Entities -->
	    <#if docHasPlaces>
	    <@c.widget id="places" title="Places (automatically extracted)">
					<p><a href="/hopper/map?doc=${query.documentID?url}">View a map</a> of the most frequently mentioned places in this document.</p>
				<#assign placeCounts = entityCounts["Place"]![]>
				<#if (placeCounts?size > 0)>
					<@c.entitydisplay entities=placeCounts query=query text1="Sort places" text2="Click on a place to search for it in this document." />
				</#if>
			</@c.widget>
	    </#if> <#-- Places -->
	    
	    <#assign personCounts = entityCounts["Person"]![]>
	    <#if (personCounts?size > 0)>
	    	<@c.widget id="people" title="People (automatically extracted)">
				<@c.entitydisplay entities=personCounts query=query text1="Sort people" text2="Click on a person to search for him/her in this document." />
			</@c.widget>
	    </#if> <#-- People -->
	    
	    <#assign dateCounts = entityCounts["Date"]![]>
	    <#if (dateCounts?size > 0)>
	    	<@c.widget id="dates" title="Dates (automatically extracted)">
				<@c.entitydisplay entities=dateCounts query=query text1="Sort dates" text2="Click on a date to search for it in this document." />
			</@c.widget>
	    </#if> <#-- Dates -->
	    
	    <#if (citationCount > 0)>
	    	<@c.widget id="cits" title="References (${citationCount} total)">
	    		${citationText}
	    	</@c.widget>
	    </#if> <#-- Citations -->

	    <#if lang.hasMorphData>
	    	<div class="widget secondary" id="vocab">
	    		<div class="header">
	    			<a class="toggle" href="javascript:loadVocabWgt('${query}');" id="vocab-link">load</a>
	    		Vocabulary Tool
	    		</div> <#-- header -->
	    		<div class="contents" id="vocab-contents" style="display: none">
	    		</div> <#-- contents -->
	    	</div> <#-- widget -->
	    </#if> <#-- Vocab -->
	    
	    <@c.widget id="search" title="Search">
	    	<form action="searchresults" class="search_form" onsubmit="return validate_form(this,q);">
	    	
	    	<input type="hidden" name="target" value="${metadata.language.abbreviation}" />
	    	<input type="hidden" name="inContent" value="true" />
			<input name="q" size="30" />
	    	
	    	<input type="submit" value="Search" /><br />
	    	Searching in ${metadata.language.name}.
	    	<a href="search?doc=${query?url}">More search options</a><br />
	    	
	    	Limit Search to:<br />
			&nbsp;<input class="search_doc" type="checkbox" name="doc" value="${query.innermostDocumentID}" 
				checked="checked" /><span class="title">${metadata.title}</span> (this document)<br />
		
			<#if metadata.language.hasMorphData>
				<input type="radio" name="expand" value="yes" checked="checked" />Search for all inflected forms<br />
					(search for "amo" returns "amo", "amas", "amat", etc.)<br />
				<input type="radio" name="expand" value="no" />Search for exact forms only
			</#if>
			</form>
	    </@c.widget> <#-- Search -->
	    
	    <#--<@c.preferences prefs=prefs prefsStatics=prefsStatics url="text?doc=${query}"/>-->
	    <@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
	</div> <#-- right_col -->
	</div> <#-- content -->
</div> <#-- main -->

<#-- Amazon -->
<script type="text/javascript"
    src="http://www.assoc-amazon.com/s/link-enhancer?tag=theperseusprojec&o=1">
</script>
<noscript>
    <img
    src="http://www.assoc-amazon.com/s/noscript?tag=theperseusprojec"
    alt="" />
</noscript>
<#include "analytics.ftl">
</body>
</html>
