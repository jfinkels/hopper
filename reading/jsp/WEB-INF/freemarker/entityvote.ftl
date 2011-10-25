<#import "common.ftl" as c>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>Entity Voting: &ldquo;${occurrence.displayText}&rdquo;</title>
    <link rel="stylesheet" type="text/css" href="/css/hopper.css">
    <link rel="stylesheet" type="text/css" href="/css/voting.css">
    <link rel="stylesheet" type="text/css" href="/css/${prefs.greekDisplay!"UnicodeC"}.css"/>
    <link type="text/javascript" href="/js/hopper.js" />

    <style type="text/css">
        body { font-size: small; }

        td.vote_count { font-style: italic; }
        tr.winner, tr.winner td { /* font-weight: bold; */ color: green; }
        tr.voted, tr.voted td { color: red; font-weight: bold; }
        td.vote_link { font-weight: bold; }

        .search_result { font-size: 80%; text-align: center; }
    </style>
</head>
<body>
	<div id="main">
		<div id="content" class="1column">
    <#if votedParam??>
        <p>Thank you for your vote!</p>
    </#if>
    <p>Statistics for occurrence #${whichOccurrence!"1"} of &ldquo;${occurrence.displayText}&rdquo; in ${header} of <a href="text?doc=${metadata.query?url}"><#if metadata.creator??>${metadata.creator}, </#if><span class="title">${metadata.title}</span></a>:</p>

    <div class="search_result">
        ${chunk_text}
    </div>


    <form class="tool" style="font-size: x-small;" action="createentity" method="post">
        <p>Don't like any of these choices? Suggest a new possibility...</p>
        <label for="name">Enter a name 
            <#if type == "Place">
                (e.g., <em>Springfield</em>; <em>Springfield, Illinois</em>)
            <#elseif type == "Person">
                (e.g., <em>John Smith</em>; <em>John Q. Public</em>)
            </#if>
        </label>
        <input type="text" name="name" value="My New ${type}" />

        <input type="hidden" name="auth" value="${authParam}" />
        <input type="hidden" name="n" value="${numParam}" />
        <input type="hidden" name="doc" value="${queryParam}" />

        <input type="hidden" name="type" value="${type}" />
        <input type="hidden" name="occurrence_id" value="#{occurrence.id}" />

        <input type="submit" value="Add and vote" />
    </form>

    <table class="data">
        <tr>
            <th colspan="2"></th>
            <th colspan="2">Max. Freq.</th>
            <th colspan="2">Min. Freq.</th>
            <th colspan="2"></th>
        </tr>
        <tr>
            <th></th>
            <th>Entity</th>
            <th>Corpus</th>
            <th>Doc</th>
            <th>Corpus</th>
            <th>Doc</th>
            <th colspan="2">&nbsp;</th>
        </tr>
        <#list entities as entity>
        <tr class="<#if entity == occurrence.entity>winner <#assign hasWinner = true><#elseif votedParam?? && entity.authorityName == votedParam>voted </#if><#if entity_index % 2 == 0>even<#else>odd</#if>">
            <td><#if entity == occurrence.entity><a href="#auto_match">&dagger;</a></#if></td>
                <td><a href="nebrowser?id=${entity.authorityName?url}&amp;mode=browse">${entity.displayName}</a></td>
                <td><a href="nebrowser?id=${entity.authorityName?url}">${entity.maxOccurrenceCount!"<em>[unavailable]</em>"}</a></td>
                <td><#if frequencies[entity_index]??>${frequencies[entity_index].maxFrequency}<#else>0</#if></td>
                <td>${entity.minOccurrenceCount!"<em>[unavailable]</em>"}</td>
                <td><#if frequencies[entity_index]??>${frequencies[entity_index].minFrequency}<#else>0</#if></td>
                <td class="vote_count">${voteCounts[entity_index]!"no"} user vote<#if voteCounts[entity_index] == 1><#else>s</#if></td>
                <td class="vote_link">
                    <#if votedParam?? && entity.authorityName == votedParam>
                        [Voted]
                    <#else>
                        <a href="recordentityvote?selection=${entity.authorityName?url}&amp;occurrence_id=${occurrence.id?url}&amp;auth=${authParam?url}&amp;n=${numParam?url}&amp;doc=${queryParam?url}">Vote</a>
                    </#if>
                    <!--
                    <form action="entityvote" method="post">
                        <input type="hidden" name="selection" value="${entity.authorityName}" />
                        <input type="hidden" name="occurrence_id" value="#{occurrence.id}" />
                        <input type="submit" value="Vote" />
                    </form>
                    -->
                </td>
            </tr>
        </#list>
    </table>
    <p id="auto_match" style="font-size: x-small;">&dagger; This entity has been selected by the automated classifier as the most likely match in this context. It may or may not be the <em>correct</em> match.</p>
    </div>
    </div>
<#include "analytics.ftl">
</body>
</html>
