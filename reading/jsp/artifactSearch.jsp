<%@ include file="includes/common/include.jsp"%>
<c:set var="query" value="${model.query}"/>

<c:set var="artifactResults" value="${model.artifactResults}"/>
<c:set var="totalArtifactResults" value="${artifactResults.totalHitCount}"/>
<c:set var="artifactResultOccs" value="${model.artifactResultOccs}"/>
<c:set var="artifactPage" value="${model.artifactPage}"/>
<c:set var="searchArtifact" value="${model.searchArt}"/>
<c:set var="artifactTypes" value="${model.artifactTypes}"/>
<c:set var="artifactTypeParam" value=""/>
<c:forEach var="artifactType" items="${artifactTypes}">
	<c:if test="${artifactType.value}">
	<c:set var="artifactTypeParam" value="${artifactTypeParam}&artifactType=${artifactType.key}"/>
	</c:if>
</c:forEach>

<c:set var="imagePage" value="${model.imagePage}"/>
<c:set var="imgResults" value="${model.imgResults}"/>
<c:set var="totalImageResults" value="${imgResults.totalHitCount}"/>
<c:set var="archiveNumbers" value="${model.archiveNumbers}"/>
<c:set var="searchImage" value="${model.searchImg}"/>

<c:set var="pageSize" value="${model.pageSize}"/>

<html>
<head>
<title>
Art & Archaeology Search Results
</title>
<link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
<link href="/css/ArtifactBrowser.css" type="text/css" rel="stylesheet"/>
<script src="/js/hopper.js" type="text/javascript"></script>
</head>
<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Art & Archaeology Search Results<br>
	for <em><c:out value="${query}"/></em></h1>
    </div>
<div id="header_side">
	<%@ include file="/includes/head_search.html" %>
    </div>
</div>
<div id="main">
<jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
</jsp:include> 
<div id="content" class="2column">
<div id="side_col">
	<div id="artifact_search" class="widget secondary">
	<div class="header">
		<a id="artifact_search-link" href="javascript:toggle('artifact_search')" class="toggle">hide</a>
		Search Options
	</div>
	<div id="artifact_search-contents" class="contents">
	<form action="artifactSearch" method="get" onsubmit="return validate_form(this,q);">
		Search for a new term: <input type="text" name="q" value="<c:out value="${query}"/>"/><br><br>
		
		<input type="checkbox" name="artifact" value="yes" <c:if test="${searchArtifact}">checked</c:if>/>Search artifacts
		<input type="checkbox" name="image" value="yes" <c:if test="${searchImage}">checked</c:if>/>Search image captions
		<br><br>
		<input type="submit" value="Search"/>
		</form>
</div>
</div> <%-- widget --%>

<c:if test="${not empty artifactResults}">
<div id="artifact_refine" class="widget secondary">
<div class="header">
	<a id="artifact_refine-link" href="javascript:toggle('artifact_refine')" class="toggle">hide</a>
	Narrow Artifact Results
</div>
<div id="artifact_refine-contents" class="contents">
	<form action="artifactSearch" method="get" name="artifactTypes">
	<input type="hidden" name="q" value="<c:out value="${query}"/>"/>
	<input type="hidden" name="artifact" value="yes"/>
	<c:forEach var="entry" items="${artifactResults.artifactCounts}">
		<input type="checkbox" name="artifactType" value="${entry.key}" <c:if test="${artifactTypes[entry.key]}">checked</c:if>/><c:out value="${entry.key} (${entry.value})"/>
	</c:forEach>
	<br/><br/>
	<input type="button" name="checkall" value="Check All" onclick="checkAll(document.artifactTypes.artifactType)"/>
	<input type="button" name="uncheckall" value="Uncheck All" onclick="uncheckAll(document.artifactTypes.artifactType)"/>
	<input type="submit" value="View"/>
	</form>
</div>
</div> <%-- widget --%>
</c:if>
</div>

<div id="main_col">

<%-- Artifact Results --%>
<div>
<c:if test="${not empty artifactResults}">
<a name="artifact">
	<c:choose>
		<c:when test="${totalArtifactResults > 0}">
			<p>Your search for <strong><c:out value="${query}"/></strong> returned <c:out value="${totalArtifactResults}"/>
			<c:choose>
				<c:when test="${totalArtifactResults == 1}">artifact.</c:when>
				<c:otherwise>artifacts.</c:otherwise>
			</c:choose>
			<c:if test="${not empty imgResults}">
				<a href="#image" style="font-size:small">(Jump to Image Results)</a>
			</c:if>
			</p>
	  		<c:import url="includes/pager.jsp">
	    		<c:param name="hitCount" value="${totalArtifactResults}"/>
	    		<c:param name="hitsPerPage" value="${pageSize}"/>
	    		<c:param name="firstHit" value="${artifactPage}"/>
	    		<c:param name="threshold" value="3"/>
	    		<c:param name="sourceURL" value="artifactSearch?q=${fn:escapeXml(query)}&artifact=yes&artifactPage=$1${artifactTypeParam}"/>
	  		</c:import>
	
			<c:forEach var="result" items="${artifactResults.hits}">
			<c:set var="artifact" value="${result.content}"/>
				<table class="artifact-record">      
	  				<tr><td class="subject-list" style="width:40%">
	    				<div style="margin-left:2%">
	      				<div class="artifact-record-title">
	      				<c:url value="artifact" var="artifactURL">
	      					<c:param name="name" value="${artifact.name}"/>
	      					<c:param name="object" value="${artifact.type}"/>
	      				</c:url>
	      				<a href="${artifactURL}">${artifact.name}</a> <em>[<c:out value="${artifact.type}"/>]</em>
	      				</div> <%-- artifact-record-title --%>
	      				
	      				<c:if test="${not empty artifactResultOccs[artifact]}">
	      					<br>
	      					<form action="/hopper/imbrowser" method="POST">
	      					<c:forEach var="artifactOcc" items="${artifactResultOccs[artifact]}">
	      						<input type="hidden" name="archiveNumber" value="${artifactOcc.image.archiveNumber}"/>
	      					</c:forEach>
	      					<input type="hidden" name="name" value="${artifact.name}"/>
	      					<input type="submit" value="View Thumbnails (${fn:length(artifactResultOccs[artifact])})"/>
	      					</form>
	      				</c:if> 
	      				
	    				</div>
	    			</td> <%-- subject-list --%>
	    
	    			<td class="artifact-occurrence">
	    			<c:choose>
	    				<c:when test="${not empty artifactResultOccs[artifact]}">
	    				<table align="center" style="font-size: small; text-align: center" cellpadding="5">
	    				<tr>
	    				<c:forEach begin="0" end="1" step="1" var="artifactOcc" items="${artifactResultOccs[artifact]}">
	    					<c:set var="img" value="${artifactOcc.image}"/>
	    					<td>
	    					<%@ include file="/includes/thumb.jspf" %>
		     				</td>
		     			</c:forEach>
		     			</tr>
		     			</table>
		    			</c:when>
		    			<c:otherwise>
		    				<i>[No images available]</i>
		    			</c:otherwise>
					</c:choose>
	    			</td> <%-- artifact-occurrence --%>
	  				</tr>
				</table>
			</c:forEach> <%-- for each artifact result --%>

	  		<c:import url="includes/pager.jsp">
	    		<c:param name="hitCount" value="${totalArtifactResults}"/>
	    		<c:param name="hitsPerPage" value="${pageSize}"/>
	    		<c:param name="firstHit" value="${artifactPage}"/>
	    		<c:param name="threshold" value="3"/>
	    		<c:param name="sourceURL" value="artifactSearch?q=${fn:escapeXml(query)}&artifact=yes&artifactPage=$1${artifactTypeParam}"/>
	  		</c:import>
	
		</c:when>
		<c:otherwise>
			<p>Your search for <strong><c:out value="${query}"/></strong> returned no artifact results.</p>
		</c:otherwise>
	</c:choose>
</c:if>
</div>

<%-- Image Results --%>
<div>
<c:if test="${not empty imgResults}">
<a name="image">
	<c:choose>
		<c:when test="${totalImageResults > 0}">
			<p>
			<form action="/hopper/imbrowser" method="POST" style="font-size: medium">
			<table width="100%">
			<tr>
			<td>
				Your search for <strong><c:out value="${query}"/></strong> returned <c:out value="${totalImageResults}"/> 
				<c:choose>
					<c:when test="${totalImageResults == 1}">image.</c:when>
					<c:otherwise>images.</c:otherwise>
				</c:choose>
			<c:if test="${not empty artifactResults}">
				<a href="#artifact" style="font-size:small">(Jump to Artifact Results)</a>
			</c:if>
			</td>
			<td align="right">
				<c:if test="${totalImageResults > pageSize}">
			      <c:forEach var="archiveNum" items="${archiveNumbers}">
			      		 <input type="hidden" name="archiveNumber" value="${archiveNum}"/>
			      </c:forEach>
			      <input type="hidden" name="q" value="<c:out value="${query}"/>"/>
			      <input type="submit" value="Browse all image results."/>
			    </c:if>
			</td>
			</tr>
			</table>
			</form>
			</p>
			<c:forEach var="result" items="${imgResults.hits}">
			<c:set var="img" value="${result.content}"/>
				<%@ include file="/includes/thumb.jspf" %>
			</c:forEach> <%-- for each image result --%>
		</c:when>
		<c:otherwise>
			<p>Your search for <strong><c:out value="${query}"/></strong> returned no image results.</p>
		</c:otherwise>
	</c:choose>
</c:if>
</div>

</div>
</div>
</html>