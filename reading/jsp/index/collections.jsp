<%@ include file="/includes/common/include.jsp"%>
<c:set var="collCounts" value="${model.collCounts}"/>
<c:set var="collBars" value="${model.collBars}"/>
<c:set var="collMetadata" value="${model.collMetadata}"/>

<html>
<head><title>Perseus Collections/Texts</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="collections"/>
    </jsp:include> 	
    <div id="content" class="2column">
		<div id="side_col" style="width:33%">
		
		<c:if test="${not empty collCounts}">
		<div id="word_count" class="box_inset">
			<table>
	    	<tr><th colspan="2">Word Counts by Text Collection</th></tr>
	    	<c:forEach var="metadata" items="${collMetadata}">
				<tr>
				<td class="coll"><b><c:out value="${metadata.alternativeTitle}"/></b><br />
				(<c:out value="${collCounts[metadata.alternativeTitle]}"/> words)
				</td>
				<td class="graph"><div class="bar" style="width: <c:out value="${collBars[metadata.alternativeTitle]}"/>%;">&nbsp;</div></td>
				</tr>
	    	</c:forEach>
			</table>
		</div> <%-- word_count --%>
		</c:if>

		<div class="box_inset" id="places" style="text-align:left">
			<p><a href="/hopper/map">View a map</a> of the most frequently mentioned
			places in the Perseus Digital Library.</p>
			<p>Or by collection:
			<ul>
				<li><a href="/hopper/map?doc=Perseus:collection:Greco-Roman">Greek and Roman Materials</a></li>
				<li><a href="/hopper/map?doc=Perseus:collection:cwar">19th-Century American</a></li>
				<li><a href="/hopper/map?doc=Perseus:collection:Renaissance">Renaissance Materials</a></li>
				<li><a href="/hopper/map?doc=Perseus:collection:RichTimes">Richmond Times Dispatch</a></li>
			</ul></p>
		</div> <%-- places box --%>
		</div> <%-- side_col --%>

<div id="index_main_col" style="width:65%; font-size: 14px;">
<h3>Browse the Collections</h3>
<c:forEach var="metadata" items="${collMetadata}">
	<div class="collection">
	    <p><a href="collection?collection=<c:out value="${metadata.query}"/>"><c:out value="${metadata.title}"/></a>
	    <br><c:out value="${metadata.description}"/></p>
	</div>
	<c:if test="${metadata.documentID eq 'Perseus:collection:Greco-Roman'}">
		<p style="margin-left:30px">
		<a href="/hopper/artifactBrowser">Art & Archaeology Artifact Browser</a>
			<br>Look through a massive library of art objects, sites, and 
			buildings.  The library's catalogs document 1305 coins, 1909 vases, 
			2003 sculptures, 179 sites, 140 gems, and 424 buildings.  Each 
			catalog entry has a description of the object and its context; 
			most have images.  Descriptions and images have been produced in 
			collaboration with many museums, institutions, and scholars.  Catalog 
			information and keywords have been taken from standard sources, which 
			are cited in the entries for each object.
		</p>
		</c:if>
</c:forEach>
<br/>
<div>
	<h3>External Collections<br/>
	<span style="font-size:small">These collections are no longer hosted by the Perseus Digital Library.</span></h3>
	<div class="collection">
		<p><a href="http://papyri.info/" target="_blank" onclick="javascript: pageTracker._trackPageview('/outgoing/papyri');">Duke Databank of Documentary Papyri</a></p>
		<p><a href="http://hdl.handle.net/10427/37914" target="_blank" onclick="javascript: pageTracker._trackPageview('/outgoing/bolles');">Bolles Collection</a></p>
		<p><a href="http://dca.tufts.edu/?pid=72" target="_blank" onclick="javascript: pageTracker._trackPageview('/outgoing/tuftsHistory');">Tufts History</a></p>
	</div>
</div>
</div> <%-- index_main_col --%>

    </div> <%-- content --%>
</div> <%-- main --%>

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
