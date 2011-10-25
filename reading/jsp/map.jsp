<%@ include file="includes/common/include.jsp"%>
<c:set var="mapID" value="${model.mapID}"/>
<c:set var="loadTextMap" value="${model.loadTextMap}"/>
<c:set var="creator" value="${model.creator}"/>
<c:set var="title" value="${model.title}"/>
<c:set var="language" value="${model.language}"/>
<html>
<head><title>Locations in 
<c:choose><c:when test="${loadTextMap}"><c:out value="${title}"/></c:when>
<c:otherwise>the Perseus Digital Library</c:otherwise></c:choose></title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAmVfH9mfXIUDXF0tz6IVxNhSjn5ApKtUPEJlMaMN-PCKfbc5DuRT-cN8ndmPHBpeUU7DlQF4QPRNmXA&sensor=false"
            type="text/javascript">
</script>

<script type="text/javascript" src="/js/map.js"></script>

</head>
<body onload="initialize('${mapID}'); checkRedirect();" onunload="GUnload()">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Most frequently mentioned places in<br/>
    	<c:choose>
    	<c:when test="${loadTextMap}">
    		<c:if test="${not empty creator}">
    		<c:out value="${creator}"/>, 
			</c:if>
			<span class="title"><c:out value="${title}"/></span>
			<c:if test="${not empty language}">
				(<c:out value="${language}"/>)
			</c:if>
    	</c:when>
    	<c:otherwise>
    		the Perseus Digital Library 	
    	</c:otherwise>
    	</c:choose></h1>
    </div>
<div id="header_side">
	<%@ include file="/includes/head_search.html" %>
    </div>
</div>
<div id="main">
    <jsp:include page="includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
    </jsp:include> 	
    <div id="content" class="1column">
    	<div style="text-align:center">
    	<br/>
    	<div id="map_canvas" style="width: 700px; height: 500px; margin-left:auto; margin-right:auto"></div>
    	</div> <%-- main_col --%>
    </div> <%-- content --%>
    </div> <%-- main --%>
    
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>