<#macro fm metadata ed=true link="" fmt=true lang=false head="">
<#if metadata.title??>
<#if metadata.creator??>${metadata.creator}, </#if><#if fmt==true><span class="title"></#if><#if link != ""><a href="${link}"></#if>${metadata.title}<#if head != "">, ${head}</#if><#if link != ""></a></#if><#if fmt==true></span></#if><#if lang==true && metadata.language??> (${metadata.language})</#if><#if ed==true && metadata.contributor??> (ed. ${metadata.contributor})</#if><#rt>
</#if>
</#macro>

<#macro widget id title=id?capitalize hidden=false>
    <div class="widget secondary" id="${id}">
	<div class="header">
	    <a class="toggle" href="javascript:toggle('${id}')" id="${id}-link"><#if hidden==true>show<#else>hide</#if></a>
	    ${title}
	</div>
	<div class="contents" id="${id}-contents"<#if hidden==true>style="display:none"</#if>>
	    <#nested>
	</div>
    </div>
</#macro>

<#macro pager hits pagesize=25 curpage=1 threshold=5>
    <#if hits &gt; pagesize>
	<#assign pagecount = ((hits - 1) / pagesize + 1)?int>
	<div class="pager">
	    <#if curpage &gt; 1>
		<#nested 1, "<img src='/img/westend.gif' border='0'/>">
		<#nested (curpage-1), "<img src='/img/west.gif' border='0'/>">
	    </#if>

            <#assign start><#if curpage-threshold &lt;= 0>1<#else>${(curpage-threshold)}</#if></#assign>
            <#assign start = start?number>

            <#assign end><#if curpage+threshold &gt;= pagecount>${(pagecount)}<#else>${(curpage+threshold)}</#if></#assign>
            <#assign end = end?number>

            <#if start &gt; 1> ... </#if>
	    <#list start..end as page>
                <#nested page page>
	    </#list>
            <#if end &lt; pagecount> ... </#if>
	    <#if curpage &lt; pagecount>
		<#nested (curpage+1), "<img src='/img/east.gif' border='0'/>">
		<#nested pagecount, "<img src='/img/eastend.gif' border='0'/>">
	    </#if>
	</div>
    </#if>
</#macro>

<#macro join l=[""] sep=" "><#list l as x>${x}<#if x != l?last>${sep}</#if></#list></#macro>

<#macro langselect langs def=langs?first name="target" onchange="">
    <select name="${name}" onchange="${onchange}">
	<#list langs as lang>
	<option value="${lang.code}" <#if lang = def || (lang.code?? && lang.code = def)>selected="selected"</#if>>${lang.name}</option>
	</#list>
    </select>
</#macro>

<#macro entitydisplay entities query text1 text2>
	<div>
	${text1}
	<a href="text?doc=${query}&amp;ie_sort=display">alphabetically</a>,
	<a href="text?doc=${query}&amp;ie_sort=token">as they appear on the page</a>,
	<a href="text?doc=${query}&amp;ie_sort=freq">by frequency</a><br />
	${text2}
	</div>
	<#list entities as entity>
		<a href="nebrowser?id=${entity["authorityName"]}&amp;query=${query.documentID}" target="_blank">${entity["displayName"]}</a> (${entity["count"]})<br />
	</#list>
</#macro>

<#macro preferences prefs prefsStatics url>
	<@c.widget id="disppref" title="Display Preferences">
		<form action="disppref">
	    	<table><tr>
				<td>Greek Display:</td>
				<td><select name="${prefsStatics.GREEK_DISPLAY_KEY}">
					<@c.prefoptions knownValues=prefs.greekKnownValues displayValues=prefs.greekDisplayValues selected=prefs.greekDisplay />
				</select></td>
				</tr>
				<tr>
				<td>Arabic Display:</td>
				<td><select name="${prefsStatics.ARABIC_DISPLAY_KEY}">
					<@c.prefoptions knownValues=prefs.arabicKnownValues displayValues=prefs.arabicDisplayValues selected=prefs.arabicDisplay />
				</select></td>
				</tr>
				<tr>
				<td>View by Default:</td>
				<td><select name="${prefsStatics.LANGUAGE_KEY}">
					<@c.prefoptions knownValues=prefs.languageKnownValues displayValues=prefs.languageDisplayValues selected=prefs.language />
				</select></td>
				</tr>
				<tr>
				<td>Browse Bar:</td>
				<td><select name="${prefsStatics.NAVBAR_KEY}">
					<@c.prefoptions knownValues=prefs.navbarKnownValues displayValues=prefs.navbarDisplayValues selected=prefs.navbar />
				</select></td>
				</tr>
				<tr><td colspan="2">
				<input type="hidden" name="url" value="${url}"/>
				<input type="submit" value="Update Preferences" />
				</td></tr>
			</table>
		</form>
	</@c.widget> <#-- Display Preferences -->
</#macro>

<#macro prefoptions knownValues displayValues selected>
	<#list knownValues as knownValue>
		<option value="${knownValue}" 
			<#if selected == knownValue>selected="selected"</#if>>${displayValues[knownValue_index]}</option>
	</#list>
</#macro>

<#macro resolveformbox languages language matchMode="exact" lookupForm="">
<form action="resolveform" method="get" onsubmit="return validate_form(this,lookup);">
	<#assign types = ["start", "end", "substring", "exact"]>
	<#assign typeTitles = ["words starting with", "words ending with", "words containing", "the exact word"]>
		  Search for
		  <select name="type">
		  	<#list types as typeVal>
		  		<option value="${typeVal}"<#if typeVal == matchMode> selected="selected"</#if>>${typeTitles[typeVal_index]}</option>
		  	</#list>
		  </select>
		  <input type="text" name="lookup" value="<#if lookupForm??>${lookupForm}</#if>" />
		   in 
		  <@c.langselect langs=languages def=language name="lang" onchange="toggleGreekDisplay(this)"/>
		  <input type="submit" value="Search" />
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