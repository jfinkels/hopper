<#import "common.ftl" as c>

<#macro vocabLangs languageList language>
<form method="get" action="">
	Show documents in <@c.langselect langs=languageList def=language name="lang" />
	<input type="submit" value="Show all" />
</form>
</#macro>
	
<#macro refineWorks texts sortMethod percentFilter usingCustomFilter language documentQuery="" workIDs=[] usingChunks=false>
<form method="get" action="" name="selectWorks">
	<#if !usingChunks>
	<fieldset id="works">
		<legend>Select one or more works:</legend>
		<select name="works" size="15" maxlength="40" multiple="multiple" style="width:100%">
			<#list texts as query>
				<option value="${query}" <#if workIDs?? && workIDs?seq_contains(query)>selected="selected"</#if>><@c.fm metadata=query.metadata ed=true /></option>
			</#list>
		</select>
	</fieldset>
	<#else>
	<input type="hidden" name="works" value="${documentQuery}">
	<input type="hidden" name="usingChunks" value="true">
	<input type="hidden" name="fullPage" value="true">
	</#if>
			
	<fieldset id="sort_by">
		<legend>Sort by</legend>
		<#if !sortMethod??>
			<#assign sortMethod = "weighted_freq">
		</#if>
		<#assign sortTitles =
			["Alphabetical Order", "Max Frequency", "Min Frequency", "Weighted Frequency",
			 "Key Term Score"]>
			 <#--, "Position in Document"]>-->
		<#assign sortValues =
			["form", "max_freq", "min_freq", "weighted_freq", "keyword_score"]>
			<#--, "position"]> -->
		<#list sortValues as value>
			<#assign title = sortTitles[value_index]>
			<input type="radio" name="sort" value="${value}"<#if value == sortMethod> checked="checked"</#if>/> ${title}<br />
		</#list>
	</fieldset>

	<fieldset id="percent">
		<legend>Show</legend>
		<#assign percentiles = [25, 50, 75, 100]>
		<#assign percentTitles = ["top 25 percent", "top 50 percent", "top 75 percent", "all words"]>
		<#list percentiles as pct>
			<input type="radio" name="filt" value="${pct}"<#if pct?string == percentFilter> checked="checked"</#if> /> ${percentTitles[pct_index]}<br />
		</#list>
		<input type="radio" id="customButton" name="filt" value="custom"<#if usingCustomFilter> checked="checked"</#if> /> top <input type="text" size="4" maxlength="3" name="filt_custom" value="<#if usingCustomFilter>${percentFilter}</#if>" onKeyUp="selectButton()"/> percent<br />
	</fieldset>
	
	<fieldset id="view">
		<legend>Output</legend>
		<input type="radio" name="output" value="table" checked="checked" /> Table<br/>
		<input type="radio" name="output" value="xml" /> XML<br/>
	</fieldset>
			
	<br style="clear: both;" />
	<input type="hidden" name="lang" value="${language.code}">
	<input id="submit" type="submit" value="Show vocabulary">
</form>
</#macro>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Perseus Vocabulary Tool - ${language.name}</title>
	<link rel="stylesheet" type="text/css" href="/css/hopper.css">
	<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
	<script type="text/javascript" src="/js/hopper.js"></script>
	<script type="text/javascript">
		function selectButton() {
			document.getElementById('customButton').checked=true;
		}
	</script>
	<style type="text/css">
		table#vocab_stats {
			text-align: center;
			font-size: 70%;
			border-spacing: 2pt;
			border-collapse: separate;
			width: 50%;
			margin: auto;
			clear: both;
		}
		
		#vocab_stats tr {
			background: #eee;
			margin-bottom: 2px;
		}
		
		#vocab_stats td, #vocab_stats th {
			padding: 4px;
		}
		
		#works_shown {
			text-align: center;
			font-size: small;
			margin-bottom: 10px;
		}
		
		#works_shown ul {
			list-style-type: none;
			margin: 0;
			padding: 0;
		}
		
		#vocab_list {
			margin: auto;
			width: 100%;
			clear: both;
		}
		
		fieldset#works {
			width: 95%;
			float: left;
		}
		
		fieldset, fieldset select {
			font-size: xx-small;
		}
		
		#sort_by, #percent, #view {
			width: 20%;
			float: left;
		}
		
		#useful_info {
			clear: both;
			margin: auto;
		}
	</style>
</head>
<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>${language.name} Vocabulary Tool</h1>
    </div>
    <div id="header_side">
    <#include "includes/head_search.ftl">
    </div>
</div>
<div id="main">
	<#include "includes/indexNav.ftl">
	<div id="content" class="2column">
	
	<#if frequencies??>
		<#-- move to side column when we have results -->
		<div id="side_col">
		<@c.widget id="refine" title="Refine Display">
		<#if !usingChunks>
			<@vocabLangs languageList=languageList language=language />
		</#if>
	    <@refineWorks texts=texts sortMethod=sortMethod percentFilter=percentFilter 
	    	usingCustomFilter=usingCustomFilter language=language workIDs=workIDs 
	    	usingChunks=usingChunks documentQuery=documentQuery />
	    </@c.widget>
	    <@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
		</div> <#-- side_col -->

		<div id="main_col">
		<#if !usingChunks>
			<div id="useful_info">
				<table id="vocab_stats">
					<tr>
						<th>Total word count</th>
						<th>Unique word count</th>
						<th><a href="/hopper/help/vocab#vocabdensity">Vocabulary density</a></th>
						<th>Words occurring only once</th>
					</tr>
					<tr>
						<td>${totalCount}</td>
						<td>${uniqueCount}</td>
						<td>${totalCount / uniqueCount}</td>
						<td>${onceCount}</td>
					</tr>
				</table>

				<div id="works_shown">
					<p>Showing frequencies for:</p>
					<ul>
						<#list queryList as query>
							<li><@c.fm metadata=query.metadata link="text?doc=${query}" ed=true />
						</#list>
					</ul>
				</div>
			</div>
		<#else>
		<div id="works_shown">
			<p>Showing frequencies for <@c.fm metadata=documentQuery.metadata link="text?doc=${documentQuery}" ed=false head=documentQuery.getDisplayQuery() /></p>
		</div>
		</#if>

		<#assign runningWeighted = 0.0>
		<table class="data" id="vocab_list">
			<tr>
				<th colspan="4">&nbsp;</th>
				<th colspan="2"><a href="/hopper/help/vocab#weight" style="text-decoration:underline">Weighted Freq.</a></th>
				<#--
				<th colspan="2">% of Total</th>
				-->
				<th colspan="3">&nbsp;</th>
			</tr>
			<tr>
				<th>Count</th>
				<th>Word</th>
				<th><a href="/hopper/help/vocab#max" style="text-decoration:underline">Max. Freq.</a></th>
				<th>Min. Freq.</th>
				<th>This Word</th>
				<th>Total</th>
				<#--
				<th colspan="2">% of Total</th>
				-->
				<th><a href="/hopper/help/vocab#tfidf" style="text-decoration:underline">Key Term Score</a></th>
				<th><a href="/hopper/help/vocab#defs" style="text-decoration:underline">Definition</a></th>
				<th>Lexicon Entries</th>
			</tr>
			<#assign classes = ["even", "odd"]>
			<#list frequencies as freq>
				<#assign word = freq.entity.displayForm>
				<#assign runningWeighted = runningWeighted + freq.weightedFrequency>
				<tr <#if freq_index % 2 != 0>class="odd"</#if>>
					<td><#if (freq_index+1) % 10 == 0>${freq_index+1}</#if></td>
					<td>${renderer.render(freq.entity.displayName)}</td>
					<td><a href="searchresults?all_words=${word}&amp;la=${language.code}&amp;all_words_expand=yes" target="_new">${freq.maxFrequency}</a></td>
					<td>${freq.minFrequency}</td>
					<td>#{freq.weightedFrequency; M2}</td>
					<td>#{runningWeighted; M2}</td>
					<td>#{freq.tfidf; M4}</td>
					<td>${freq.entity.shortDefinition!"<em>[unavailable]</em>"}</td>
                    <td><#list freq.entity.lexiconQueries as lexQuery><a href="text?doc=${lexQuery?url}&amp;highlight=${word}" target="_blank" onclick="openPopupWindow(this); return false">${lexQuery.metadata.alternativeTitle}</a><#if lexQuery_has_next>, </#if></#list></td>
				</tr>
			</#list>
		</table>
		<br/>
	</div> <#-- main_col -->
	<#else>
	<div id="main_col">
	<@vocabLangs languageList=languageList language=language />
	<@refineWorks texts=texts sortMethod=sortMethod percentFilter=percentFilter 
		usingCustomFilter=usingCustomFilter language=language />
	</div> <#-- main_col -->
	</#if>
</div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>
