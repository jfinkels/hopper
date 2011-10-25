<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#import "common.ftl" as c>
<html>
<head>
<title>Submit Vote</title>
<link rel="stylesheet" type="text/css" href="/css/voting.css" />
<link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay}.css"/>
</head>

<body>
<div id="main">
    <div id="content" class="1column" style="margin-top: 5px; font-size:small">
<#if recordedVote>
	<h3>Thank you!</h3>
	<p>Your vote has been recorded. Thank you for your input!</p>
<#elseif badRequest>
	<p>There was a problem recording your vote!</p>
	<p>Please email us with the URL you were attempting to access, and we'll look into it.</p>
</#if>
<#if hasCounts>
	<p>Current results for occurrence #${which} of <q>${renderer.render(form)}</q> 
	in <#if query.metadata.creator??>${query.metadata.creator}, </#if><em>${query.metadata.title}</em>, ${query.displayQuery}:</p>
	<table class="vote_results">
	<tr><th>Meaning</th><th>Votes</th></tr>
	<#-- this is redundant - nothing to do about it though -->
	<#if hasSense>
		<#list senses as sense>
		<tr <#if sense_index % 2 != 0>class="odd"</#if>>
		<td class="meaning">
		<div style="margin-left: ${sense.level * 20}px;">${sense.sense}. 
		<#assign def = sense.shortDefinition>
		<#if (def?length > 50)>${def?substring(0,50)}...<#else>${def}</#if>
		</div>
		</td>
		<#assign key=sense.lexiconID+sense.lemma+sense.sense>
		<td class="graph">
		<#if (senseCounts[key]?int > 0)>
			<div class="bar" style="width: ${senseCounts[key]?int/totalVotes*100}%;">${senseCounts[key]?int}</div>
		</#if>
		</td> 
		</tr>
		</#list>
	<#elseif hasMorph>
		<#list parses as parse>
		<tr <#if parse_index % 2 != 0>class="odd"</#if>>
		<td class="meaning">
			<b>${renderer.render(parse.lemma.displayForm)}</b> ${parse}
		</td>
		<#assign key=parse+parse.lemma.authorityName>
		<td class="graph">
		<#if (morphCounts[key]?int > 0)>
			<div class="bar" style="width: ${morphCounts[key]?int/totalVotes*100}%;">${morphCounts[key]?int}</div>
		</#if>
		</td>
		</tr>
		</#list>
	</#if>
	<tr class="total"><th colspan="2">Total: ${totalVotes}</th></tr>
	</table>
<p><a href="javascript:history.back()">Back</a> (Note that you may need to refresh the previous page to see the effect of your vote.)</p>
<#else>
<h4>No results for this word yet.</h4>
<p>Please try again later!</p>
</#if>

    </div> <#-- content -->
</div> <#-- main -->
<#include "analytics.ftl">
</body>
</html>