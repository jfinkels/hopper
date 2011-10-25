<%@ include file="/includes/common/include.jsp"%>
<c:set var="dateSort" value="${model.dateSort}"/>
<c:set var="output" value="${model.output}"/>
<html>
<head><title>Publications</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
    <jsp:param name="tabActive" value="about"/>
	<jsp:param name="subtabActive" value="publications"/>
    </jsp:include> 	
    <div id="content" class="2column">
        <div id="index_main_col">
		<div id="publications">

			<h4>Perseus Articles and Publications</h4>

			<c:choose>
				<c:when test="${dateSort}">
			    	<p><a href="?sort=name">Sort by name</a> | Sorted by date</p>
			    </c:when>
				<c:otherwise>
			   		<p>Sorted by name | <a href="?sort=date">Sort by date</a></p>
				</c:otherwise>
			</c:choose>
			
			<c:out value="${output}" escapeXml="false"/>
		</div> <!-- Publications div -->	
	</div> <!-- main_col div -->
        
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
