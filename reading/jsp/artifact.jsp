<%@ include file="includes/common/include.jsp"%>
<%@ page isELIgnored="false" %>
<c:set var="artifact" value="${model.artifact}"/>
<c:set var="propertyNames" value="${model.propertyNames}"/>
<c:set var="tableProperties" value="${model.tableProperties}"/>
<c:set var="paragraphProperties" value="${model.paragraphProperties}"/>
<c:set var="artOccs" value="${model.artOccs}"/>

<c:choose>
<c:when test="${not empty artifact}">
<c:set var="title" value="${artifact.name} (${artifact.type})"/>
</c:when>
<c:otherwise>
<c:set var="title" value="No artifact found"/>
</c:otherwise>
</c:choose>
<html>
<head>
<title>
<c:out value="${title}" escapeXml="false"/>
</title>
<link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
</head>

<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1><c:out value="${title}" escapeXml="false"/></h1>
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
<c:if test="${not empty artOccs}">
<c:set var="thumbs" value="${5}"/>
<c:if test="${fn:length(artOccs) < thumbs}">
	<c:set var="thumbs" value="${fn:length(artOccs)}"/>
</c:if>
<div style="font-size: small; text-align: center">
	<c:forEach begin="0" end="${thumbs}" step="1" var="artOcc" items="${artOccs}">
		<c:set var="img" value="${artOcc.image}"/>
		<%@ include file="/includes/thumb.jspf" %>
	</c:forEach>
</div>

<form action="/hopper/imbrowser" method="POST" style="text-align:center">
<c:forEach var="artOcc" items="${artOccs}">
	<input type="hidden" name="archiveNumber" value="${artOcc.image.archiveNumber}"/>
</c:forEach>
<input type="hidden" name="name" value="${artifact.name}"/>
<input type="submit" value="View Thumbnails (${fn:length(artOccs)})"/>
</form>
</c:if>
</div>

<div id="main_col">
<c:choose>
<c:when test="${not empty artifact}">
<p/>
<table cellspacing=5 cellpadding=2>
<c:forEach var="tableProperty" items="${tableProperties}">
<tr>
	<td><b><c:out value="${tableProperty.key}"/>:</b></td>
	<td><c:out value="${tableProperty.value}" escapeXml="false"/></td>
</tr>
</c:forEach>
</table>
<hr/>
<c:forEach var="paraProperty" items="${paragraphProperties}">
<p>
<b><c:out value="${paraProperty.key}"/>:</b> <c:out value="${paraProperty.value}" escapeXml="false"/>
</p>
</c:forEach>
</c:when>
<c:otherwise>
<p>There are no artifacts matching that name and type.</p>
</c:otherwise>
</c:choose>
</div> <%-- main_col --%>
</div> <%-- main --%>
</body>
</html>