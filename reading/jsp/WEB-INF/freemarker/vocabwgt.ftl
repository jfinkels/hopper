<#if (frequencies?? && frequencies?size > 0)>
<p>Currently showing the top ${percentFilter}% of words sorted by weighted frequency. <a target="_blank"
href="vocablist?works=${documentQuery?url}&amp;usingChunks=true&amp;fullPage=true">Study 
all vocabulary in this passage.</a>
		</p>
<table class="data">
<tr>
<th>Word</th>
<th>Definition</th>
<th>Key Term Score</th>
<th>Min Freq</th>
<th>Max Freq</th>
<th>Weighted Freq</th>
</tr>
<#list frequencies as freq>
	<#assign word = freq.entity.displayForm>
 	<tr <#if freq_index % 2 != 0>class="odd"</#if>>
	<td>${renderer.render(freq.entity.displayName)}</td>
	<td>${freq.entity.shortDefinition!"<em>[unavailable]</em>"}</td>
	<td>#{freq.tfidf; M4}</td>
	<td>${freq.minFrequency}</td>
	<td>${freq.maxFrequency}</td>
	<td>#{freq.weightedFrequency; M2}</td>
	</tr>
</#list>
</table>
<#else>
 No vocab found - try viewing the text at a <a href="text?doc=${documentQuery.getContainingQuery()?url}">broader level</a>.
</#if>