<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>

<#macro morphbox languages language word="">
<form action="" name="f" onsubmit="return validate_form(this,l);">
	    Get Info for <input name="l" value="<#if word??>${word}</#if>"/> in 

		<@c.langselect langs=languages def=language name="la" onchange="toggleGreekDisplay(this)" />
	    <input type="submit" value="Go" />
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

<html>
<head>
<title>${language.name} Word Study Tool</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
<script type="text/javascript" src="/js/hopper.js"></script> 
<script type="text/javascript" src="/js/xmlhttp.js"></script>
<script type="text/javascript" src="/js/morph.js"></script>
</head>

<body onload="document.f.l.focus(); checkRedirect();">
<div id="header">
	  <a id="logo" href="/hopper/"></a>
	  <div id="header_text">
		<h1>${language.name} Word Study Tool</h1>
	  </div>
	  <div id="header_side">
	  <#include "includes/head_search.ftl">
	  </div>
	</div>
<div id="main">
	<#include "includes/indexNav.ftl">
	<div id="content" class="1column">
    <#if lemmas??>
    	<div id="side_col">
		<@c.widget id="search" title="Search">
			<@morphbox word=word languages=languages language=language />
		</@c.widget>
   		<@c.preferences prefs=prefs prefsStatics=prefsStatics url=url/>
		</div>
		
		<div id="main_col">
    	<#if (lemmas?size > 0)>
    	<#list lemmas as lemma>
    	<div class="analysis">
    	<div class="lemma" id="${lemma.displayForm}">
    	<div class="lemma_header">
		<h4 class="${language.code}">${parseRenderer.render(lemma.displayForm)}</h4>
		<span class="lemma_definition">
		${lemma.shortDefinition!"<em>[definition unavailable]</em>"}
    	</span>
    	</div>
    	<p>
    	<#if lemma.lexiconQueries??>
    		(Show lexicon entry in 
    		<#list lemma.lexiconQueries as lexQuery>
    			<#assign onclick>
    				doXMLHttpRequest(loadLexiconPreCallback, loadLexiconCallback, {'doc':'${lexQuery}'<#if query??>,'form':'${word}','which':'${which}','source':'${query}','lexquery':'${lexQuery}'</#if>});showEntry('${lexQuery}-contents');
    			</#assign>
    			<a id="${lexQuery}-link" href="#lexicon" onclick="${onclick}">${lexQuery.metadata.alternativeTitle}</a></#list>) <#-- need to keep end list tag here so there is no space before the end parentheses -->
    	</#if>
    	(<a href="searchresults?q=${word?url}&amp;target=${language.code?url}<#if query??>&amp;doc=${query.documentID?url}</#if>" target="_blank">search</a>)
    	</p>
   		<#assign hasWinner=false>
    	<table>
    	<#list lemmaParsesByAuthName[lemma.authorityName] as parse>
    		<#assign parseWinner = false>
    		<#-- Comparing current parse to winning parse is proving to be very annoying -->
    		<tr <#if winningParse??><#if parse.compareTo(winningParse) == 0 && parse.code == winningParse.code><#assign hasWinner=true parseWinner=true>class="winner"</#if></#if>>
    		<td class="${language.code}">${parseRenderer.render(parse.orth)}<#if parseWinner> &dagger;</#if></td>
    		<td>${parse}</td>
    		<td style="font-size: x-small">
    		<#if doVoting>
    			<#assign key=parse+parse.lemma.authorityName >
    			<#if (morphVotesByParse[key]??)>${morphVotesByParse[key]}
    				<#if (morphVotesByParse[key]?int > 1)> user votes
    				<#else> user vote
    				</#if>
    			<#else><em>no user votes</em>
    			</#if>
    		</td> 
    		<td>${votingResultsByParse[key]}</td>
    		<td>
    		<#if which?? && hasMultipleParses>
    		[<a href="submitvote?type=morph&amp;doc=${query.documentID?url}&amp;subquery=${query.query?url}&amp;form=${word?url}&amp;which=${which}&amp;lemma=${parse.lemma.displayForm?url}&amp;code=${parse.code?url}&amp;lang=${language.code}">vote</a>]
    		</#if>
    		</td>
    		</#if>
    		</tr>
    	</#list>
    	</table>
    	</div>
    	<#if hasWinner>
    	<p class="help_note" style="font-weight: normal;">&dagger; This form has been selected using statistical methods as the most likely one in this context. It may or may not be the <em>correct</em> form. (<a href="#votes" onclick="javascript:showVotes();">More info</a>)</p>
    	</#if>
    	<#if lemmaStats?? && lemmaStats[lemma.authorityName]??>
    		<#assign lemmaStat = lemmaStats[lemma.authorityName]>
    		<#assign frequency = lemmaStat[0]>
    		<#assign wordCount = lemmaStat[1].wordCount>
    		
    		<p style="font-size:x-small" class="word_freq">Word Frequency Statistics (<a href="wordfreq?lang=${language.code?url}&amp;lookup=${lemma.displayForm?url}">more statistics</a>)</p>
			<table class="data">
			<tr>
			<th>Words in Corpus</th>
			<th>Max</th>
			<th>Max/10k</th>
			<th>Min</th>
			<th>Min/10k</th>
			<th>Corpus Name</th>
			</tr>
			<tr>
			<td>${wordCount}</td>
			<td><a href="searchresults?q=${lemma.headword?url}&amp;target=${language.code?url}&amp;doc=${query.documentID?url}&amp;expand=lemma&amp;sort=docorder" target="_blank">${frequency.maxFrequency}</a></td>
			<td>${frequency.getMaxPer10K(wordCount)}</td>
			<td>${frequency.minFrequency}</td>
			<td>${frequency.getMinPer10K(wordCount)}</td>
			<td class="work">
	            <#if query.metadata.creator??>${query.metadata.creator}, </#if>
		    <a href="text?doc=${query.documentID?url}">${query.metadata.title}</a>
			</td>
			</tr>
			</table>
		<#else>
		<p class="word_freq" style="font-size: x-small"><a href="wordfreq?lang=${language.code?url}&amp;lookup=${lemma.displayForm?url}">Word frequency statistics</a></p>
    	</#if>
    	</div>
    	<br/>
    </#list>
    
    <#-- xml link -->
    <a href="xmlmorph?lang=${language.code?url}&lookup=${word?url}" onclick="javascript: pageTracker._trackPageview('/hopper/xmlmorph?lang=${language.code?url}&lookup=${word?url}');"><img src="/img/xml.gif" alt="view as XML" border="0"/></a><br/><br/>
    
    <#-- lexica entries -->
    <div id="lexicon">
    	<#list allLexQueries as lexQuery>
    		<div class="lexicon_entry" style="display: none" id="${lexQuery}-contents">
    		<p class="new_window_link"><a href="text?doc=${lexQuery?url}" 
    			target="_blank">View this entry in a new window</a> / <a href="#">back to top</a></p>
    		<#if doSenseVoting[lexQuery]>
    		<div class="note">
		    <h4>Help us make this tool more useful!</h4>

		    <p>You've looked up a word that has more than one possible meaning. Please tell us which meaning you think is being used 
		    in this context by clicking <b>[select]</b> next to the appropriate meaning in the definition below. We hope to use the 
		    information we gather from this tool to provide automatic disambiguation services in the future.</p>

		    <p>Not sure which sense is being used here? View 
		    <a href="submitvote?type=sense&amp;lexquery=${lexQuery?url}&amp;doc=${query.documentID?url}&amp;subquery=${query.query?url}&amp;form=${word?url}&amp;which=${which?url}&amp;lang=${language.code?url}">what 
		    other readers have selected</a>.</p>
		    </div> <#-- note -->
    		</#if> <#-- senseVoting -->
    		</div> <#-- lexicon_entry -->
    	</#list>
    </div> <#-- lexicon -->
    
    <#-- voting data -->
    <#if doVoting>
    <div id="votes">
    	<table>
    		<caption>Statistical votes for form <q>${parseRenderer.render(word)}</q></caption>
    	<tr>
    	<th>Results</th>
    	<#list votingResults.evaluators as evaluator>
    		<th>${evaluator.description}</th>
    	</#list>
    	</tr>
    	<#list votingResults.parses as parse>
    	<#if parse.compareTo(winningParse) == 0 && parse.code == winningParse.code>
    	<tr class="winner">
    	<#else>
    	<tr>
    	</#if>
    	<td class="${language.code}"><b>${parseRenderer.render(parse.lemma.headword)}</b> ${parse}</td>
    	<#list votingResults.evaluators as evaluator>
    		<#assign key=parse+parse.lemma.authorityName+evaluator.description>
			<#if votingScoresByParse[key]??>				
				<td style="text-align:center">${votingScoresByParse[key]}</td>
			<#else>
			<td>&nbsp;</td>
			</#if>
    	</#list>
    	</tr>
    	</#list>
    	</table>
    	<p>The possible parses for this word have been evaulated by an experimental system that attempts to determine which 
    	parse is correct in this context. The system is composed of a number of "evaluators"--each of which uses different 
    	criteria to score the possibilities--whose votes are weighted to determine the best answer. The percentages in the 
    	table above show each evaluator's score for each form, which are then combined to determine each form's overall score.</p>
		<p>This selection used the following evaluators:</p>
		<ul>
		<#list votingResults.evaluators as evaluator>
		 <li><strong>${evaluator.description}</strong>: ${evaluator.longDescription}</li>
		</#list>
		</ul>
		<p>User votes are weighted more heavily than the other methods, which are all treated equally.</p>
		<p>Don't agree with the results? Cast your vote for the correct form by clicking on the <b>[vote]</b> link to the right 
		of the form above!</p>
    </div>
    </#if>
    <#else>
    <p>Sorry, no information was found for <span class="${language.code}">${parseRenderer.render(word)}</span>.</p>
    </#if> <#-- checking if the size of lemmas is greater than 0 -->
    </div> <#-- main_col -->
    <#else> <#-- no lemma results -->
    	<div id="main_col">
			<div id="search_box" style="text-align:center; width: 70%;">
			<@morphbox languages=languages language=language />
			</div>
		</div>
    </#if> <#-- checking if there are lemmas -->
    
    </div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>
