<%@ include file="includes/common/include.jsp"%>
<c:set var="title" value="${model.title}"/>
<c:set var="language" value="${model.language}"/>
<c:set var="documentID" value="${model.documentID}"/>
<c:set var="langCounts" value="${model.langCounts}"/>
<c:set var="langBars" value="${model.langBars}"/>
<c:set var="allDocuments" value="${model.allDocuments}"/>
<c:set var="hasSubDocs" value="${model.hasSubDocs}"/>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>
<c:out value="${title}"/>
</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
<script type="text/javascript" src="/js/collection.js"></script>
</head>
<body onload="checkRedirect();">

<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>
	    Perseus Collection<br />
	    <c:out value="${title}"/>
	</h1>
    </div>
    <div id="header_side">
	<form id="head_search" action="searchresults" onsubmit="return validate_form(this,q);">
	    <input name="q" size="30" />

	    <input type="submit" value="Search" /><br />

		<c:set var="targetIndex" value="${language.name}" />
		<c:if test="${empty targetIndex}">
			<c:set var="targetIndex" value="English"/>
		</c:if>
	    <span class="sidetext">
	    Search for documents in
	    <select name="target">
	    <option value="en" <c:if test="${targetIndex eq 'English'}">selected="selected"</c:if>>English</option>
	    <option value="greek" <c:if test="${targetIndex eq 'Greek'}">selected="selected"</c:if>>Greek</option>
	    <option value="la" <c:if test="${targetIndex eq 'Latin'}">selected="selected"</c:if>>Latin</option>
	    <option value="ang" <c:if test="${targetIndex eq 'Old English'}">selected="selected"</c:if>>Old English</option>
	    <option value="non" <c:if test="${targetIndex eq 'Old Norse'}">selected="selected"</c:if>>Old Norse</option>
	    </select>
	    <br />
	    <input type="radio" name="collections" value="<c:out value="${documentID}"/>" checked="checked"/> Search only in <i><c:out value="${title}"/></i>
    <p><a href="/hopper/search">All Search Options</a> [<a href="javascript:abbrev_help()">view abbreviations</a>]</p>
	    </span>
	</form>
    </div> <%-- header_side --%>
</div> <%-- header --%>
<div id="main">
	<c:set var="subtab" value="''"/>
	<c:choose>
		<c:when test="${fn:startsWith(title, 'Greek')}">
			<c:set var="subtab" value="Greco-Roman"/>
		</c:when>
		<c:when test="${fn:startsWith(title, 'Arabic')}">
			<c:set var="subtab" value="Arabic"/>
		</c:when>
		<c:when test="${fn:startsWith(title,'Germanic')}">
			<c:set var="subtab" value="Germanic"/>
		</c:when>
		<c:when test="${fn:startsWith(title,'19th-Century')}">
			<c:set var="subtab" value="cwar"/>
		</c:when>
		<c:when test="${fn:startsWith(title,'Renaissance')}">
			<c:set var="subtab" value="Renaissance"/>
		</c:when>
		<c:when test="${fn:startsWith(title,'Richmond')}">
			<c:set var="subtab" value="RichTimes"/>
		</c:when>
	</c:choose>
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="collections"/>
	<jsp:param name="subtabActive" value="${subtab}"/>
    </jsp:include> 

    <div id="content" class="2column">
	<div id="side_col" style="width:33%">
	
	<c:if test="${not empty langCounts}">
	<div class="box_inset" id="word_count">
	    <table>
	    <tr><th colspan="2">Word Counts by Language</th></tr>
	    <c:forEach var="count" items="${langCounts}">
        <tr>
        <td style="coll"><b><c:out value="${count.key}"/></b><br />
        (<c:out value="${count.value}"/>)
        </td>
        <td class="graph"><div class="bar" style="width: <c:out value="${langBars[count.key]}"/>%;">&nbsp;</div></td>
        </tr>
        </c:forEach>

	    </table>
	</div> <%-- box_inset --%>
	</c:if>
	</div>
	<div id="index_main_col" style="width:65%">
<div id="documents">
	<h2>Documents:</h2>	
	<table class="tResults" cellspacing="5">
	<c:forEach var="docList" items="${allDocuments}">
		<c:set var="docMetadata" value="${docList[0]}"/>
		<c:set var="listSize" value="${fn:length(docList)}"/>
		<tr id="<c:out value="${docMetadata.creator}"/>,0,<c:out value="${listSize}"/>+" class="trResults">
		<c:choose>
		<c:when test="${listSize eq 1}">
				<td class='tdExpand'></td>
				<td class='tdAuthor' colspan='2'>
				<c:if test="${not empty docMetadata.creator}"><c:out value="${docMetadata.creator}" escapeXml="false"/>.</c:if>
		</c:when>
		<c:otherwise>
			<td class="tdExpand" id="<c:out value="${docMetadata.creator}"/>,0,<c:out value="${listSize}"/>" 
				onclick="expandContract('<c:out value="${docMetadata.creator}"/>',<c:out value="${listSize}"/>)" 
				onmouseover='changeCursor()' onmouseout='revertCursor()'><img src='/img/east.gif'></td>
			<td class='tdAuthor' colspan='2'>
			<c:out value="${docMetadata.creator}" escapeXml="false"/>.
		</c:otherwise>
		</c:choose>
		<c:forEach var="metadata" items="${docList}" varStatus="status">
			<c:if test="${listSize > 1}">
			</td></tr>
			<tr id="<c:out value="${metadata.creator}"/>,<c:out value="${status.count}"/>,0" class="trHiddenResults" style="display:none">
			<td width="15"/>
			<td class="tdExpand"/>
			<td class="tdAuthor">
			</c:if>
			<%-- title (and link if no subdocs) --%>
			<c:choose>
			<c:when test="${not empty hasSubDocs[metadata]}">
				<c:out value="${metadata.title}" escapeXml="false"/>.
			</c:when>
			<c:otherwise>
				<c:set var="docParam"  value="${metadata.documentID}"/>
				<c:url value="text" var="textURL">
					<c:param name="doc" value="${docParam}"/>
				</c:url>
				<a href="<c:out value="${textURL}"/>" class="aResultsHeader"><c:out value="${metadata.title}" escapeXml="false"/></a>.
			</c:otherwise>
			</c:choose>
			<%-- editor --%>
			<c:if test="${not empty metadata.contributor}">
				<c:out value="${metadata.contributor}" escapeXml="false"/>.
			</c:if>
			<%-- language --%>
			<c:if test="${not empty metadata.language}">
				(<c:out value="${metadata.language}"/>)
			</c:if>
			<%-- citation --%>
			<c:if test="${empty hasSubDocs[metadata]}">
				<c:if test="${not empty metadata.query.displayCitation}">
					[<c:out value="${fn:trim(metadata.query.displayCitation)}" escapeXml="false"/>]
				</c:if>
			</c:if>
			<%-- search link --%>
			<c:url value="search" var="searchURL">
				<c:param name="doc" value="${metadata.documentID}"/>
			</c:url>
			<a href="<c:out value="${searchURL}"/>">search this work</a><br/>
			<%-- description --%>
			<c:if test="${not empty metadata.description}">
				<c:out value="${metadata.description}" escapeXml="false"/>
			</c:if>
			<%-- subdocs --%>
			<c:if test="${not empty hasSubDocs[metadata]}">
				<ul class="subdoc">
					<c:forEach var="subdoc" items="${hasSubDocs[metadata]}">
					<c:url value="text" var="subdocTextURL">
						<c:param name="doc" value="${subdoc}"/>
					</c:url>
					<li><a class="aResultsHeader" href="<c:out value="${subdocTextURL}"/>"><c:out value="${subdoc.metadata.title}" escapeXml="false"/></a>
					<c:if test="${not empty subdoc.displayCitation}">
					[<c:out value="${fn:trim(subdoc.displayCitation)}" escapeXml="false"/>]
					</c:if>
					</li>
					</c:forEach>
				</ul>
			</c:if>
		</c:forEach>
			
		</td>
		</tr>
		<tr height='05px'><td colspan='2'></td></tr>
	</c:forEach>
	
</table>
</div>
	</div> <%-- documents --%>
    </div> <%-- content --%>
</div> <%-- main --%>

<!-- Google Analytics --> 
	<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
