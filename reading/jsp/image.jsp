<%@ include file="includes/common/include.jsp"%>
<c:set var="img" value="${model.img}"/>
<c:set var="headerText" value="${model.headerText}"/>
<c:set var="caption" value="${model.caption}"/>
<c:set var="artifactLink" value="${model.artifactLink}"/>

<html>
<head>
<title><c:choose>
    	<c:when test="${not empty img}">
    	<c:out value="${img.archiveNumber}"/>
    	</c:when>
    	<c:otherwise>
    	No image found
    	</c:otherwise>
    	</c:choose>
    	</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script lang="javascript" src="/js/hopper.js"></script>
</head>

<body onload="checkRedirect();">
<div id="header">
     <a id="logo" href="/hopper/"></a>
     <div id="header_text">
     	  <h1><c:out value="${headerText}" escapeXml="false"/></h1>
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
    <div id="main_col">
    <c:choose>
    <c:when test="${not empty img}">
    	<c:choose>
    	<c:when test="${img.isRestricted}">
   			<p>Due to <a href="/hopper/help/copyright.jsp#images">copyright restrictions</a>, we cannot show the full size version of this image.</p>
   			<table style="margin-right:auto; margin-left:auto">
    		<tr><td>
				<img class="thumb_img" src="${img.thumbURL}">
			</td></tr>
			</table> 
    	</c:when>
    	<c:otherwise>
    	<table style="margin-right:auto; margin-left:auto">
    	<tr><td>
			<img class="standard_img" src="${img.fullURL}">
		</td></tr>
		</table>
		</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
	<p>We're sorry, but we're unable to find a matching image.</p>
	</c:otherwise>
	</c:choose>
</div> <%-- main_col --%>

<div id="side_col">
<c:if test="${not empty img}">
	<p><c:out value="${caption}" escapeXml="false"/></p>
	<p><c:out value="${img.credits}" escapeXml="false"/><c:if test="${not empty img.date}">, <c:out value="${img.date}"/></c:if></p>
	<c:if test="${not empty artifactLink}">
		<p><c:out value="${artifactLink}" escapeXml="false"/></p>
	</c:if>
</c:if>
</div> <%-- side_col --%>
</div> <%-- content --%>
</div> <%-- main --%>

<!-- Google Analytics -->
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>