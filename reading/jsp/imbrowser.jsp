<%@ include file="includes/common/include.jsp"%>
<%@ page isELIgnored="false" %>
<c:set var="title" value="${model.title}"/>
<c:set var="archNumbers" value="${model.archNumbers}"/>
<c:set var="hitCount" value="${model.hitCount}"/>
<c:set var="hitsPerPage" value="${model.hitsPerPage}"/>
<c:set var="numPages" value="${model.numPages}"/>
<c:set var="startPage" value="${model.startPage}"/>
<c:set var="images" value="${model.images}"/>
<c:set var="searchTerm" value="${model.searchTerm}"/>
<c:set var="name" value="${model.name}"/>

<html>
<head>
<title>${title}</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
</head>
<body>

<div id="header">
	<a id="logo" href="/hopper/"></a>
	<div id="header_text">
		<h1>Perseus Image Browser</h1>
		<h4>${title}</h4>
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
	<div id="imcoll" style="margin-left:1%">
	<c:if test="${hitCount > hitsPerPage}">
		<div style="text-align:right">
		<form action="/hopper/imbrowser" method="POST">
		<c:forEach var="image" items="${archNumbers}">
			<input type="hidden" name="archiveNumber" value="${image}"/>
		</c:forEach>
		<input type="hidden" name="q" value="${searchTerm}"/>
		<input type="hidden" name="name" value="${name}"/>
			page <select name="start">
			<c:forEach begin="1" end="${numPages}" step="1" varStatus="page">
				<c:choose>
					<c:when test="${page.count eq startPage}">
						<option value="${page.count}" selected="selected">${page.count}</option>					
					</c:when>
					<c:otherwise>
						<option value="${page.count}">${page.count}</option>										
					</c:otherwise>
				</c:choose>
			</c:forEach>
			</select>
			of ${numPages}
	 <input type="submit" value="View"/>
	</form>
	</div>
	</c:if>

<table class="images" style="width:100%" cellspacing="0" cellpadding="0" border="0">
<tbody>
<c:set var="imagesPerRow" value="5"/>
<c:forEach begin="0" end="${fn:length(images)-1}" step="${imagesPerRow}" items="${images}" varStatus="topCounter">
<tr>
<c:set var="endValue" value="${topCounter.index + imagesPerRow}"/>
<c:if test="${fn:length(images) < endValue}">
	<c:set var="endValue" value="${fn:length(images)}"/>
</c:if>
	<c:forEach begin="${topCounter.index}" end="${endValue-1}" step="1" varStatus="insideCounter">
		<c:set var="img" value="${images[insideCounter.index]}"/>
		<td>
			<%@ include file="/includes/thumb.jspf" %>
		</td>
	</c:forEach>
</tr>
</c:forEach>
</tbody>
</table>
<br>
 
	</div> <%-- imcoll --%>
	
    </div> <%-- content --%>
</div> <%-- main --%>

<%-- Google Analytics --%> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
