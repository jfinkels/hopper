<%@ include file="includes/common/include.jsp"%>
<c:set var="artifactCount" value="${model.artifactCount}"/>
<c:set var="artifacts" value="${model.artifacts}"/>
<c:set var="artifactOccs" value="${model.artifactOccs}"/>
<c:set var="pageSize" value="${model.pageSize}"/>
<c:set var="page" value="${model.page}"/>
<c:set var="randomArtifacts" value="${model.randomArtifacts}"/>
<c:set var="randArtifactImages" value="${model.randArtifactImages}"/>
<c:set var="object" value="${model.object}"/>
<c:set var="field" value="${model.field}"/>
<c:set var="keyclass" value="${model.keyclass}"/>
<c:set var="keyword" value="${model.keyword}"/>
<c:set var="value" value="${model.value}"/>

<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Art and Archaeology Artifact Browser</title>
<link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
<link href="/css/catalog.css" type="text/css" rel="stylesheet"/>
<link href="/css/ArtifactBrowser.css" type="text/css" rel="stylesheet"/>
<script src="/js/hopper.js" type="text/javascript"></script>
</head>
<body onload="checkRedirect();">
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Art & Archaeology Artifact Browser</h1>
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
<c:choose>
<c:when test="${ empty object }">
	<div class="box">
	<h3 style="text-align:center">Examples from the Art & Archaeology Artifact Browser</h3>
	<c:forEach var="randArt" items="${randomArtifacts}">
	<table style="text-align:center; margin-left:auto; margin-right:auto" rules="rows" border="1" bordercolor="black" width="94%" vspace="5">
	<tbody>
	<tr>
	<td width="50%">
	<c:url value="artifact" var="artifactURL">
		<c:param name="name" value="${randArt.name}"/>
		<c:param name="object" value="${randArt.type}"/>
	</c:url>
	        <a href="${artifactURL}"><c:out value="${randArt.name}" escapeXml="false"/></a>
		<br>
		<span style="font-size:small">from the Perseus <c:out
	value="${randArt.type}"/> Catalog</span>
		</td>
		<td width="50%">
		<div style="padding:2px">
		<c:choose>
			<c:when test="${randArtifactImages[randArt] != null}">
				<c:set var="img" value="${randArtifactImages[randArt].image}"/>
				<%@ include file="/includes/thumb.jspf" %>
		    </c:when>
		    <c:otherwise>
		    <span style="font-size:small"><i>[No image available]</i></span>
		    </c:otherwise>
		</c:choose>
		</div>
		      </td>
		      </tr>
		  </tbody>
	      </table>
	      </c:forEach>
	</div>
</c:when>
<c:when test="${ not empty art_browser.object && empty art_browser.field }">
	<div class="box">
	  <h3 style="text-align:center">Choose a different artifact type:</h3>
	  <ul>	
	  <c:if test="${object != 'Building'}">
	    <li><a href="artifactBrowser?object=Building">Building</a></li>
	  </c:if>
	  <c:if test="${object != 'Coin'}">
	    <li><a href="artifactBrowser?object=Coin">Coin</a></li>
	  </c:if>
	  <c:if test="${object != 'Gem'}">
	    <li><a href="artifactBrowser?object=Gem">Gem</a></li>
	  </c:if>
	  <c:if test="${object != 'Sculpture'}">
	    <li><a href="artifactBrowser?object=Sculpture">Sculpture</a></li>
	  </c:if>
	  <c:if test="${object != 'Site'}">
	    <li><a href="artifactBrowser?object=Site">Site</a></li>
	  </c:if>
	  <c:if test="${object != 'Vase'}">
	    <li><a href="artifactBrowser?object=Vase">Vase</a></li>
	  </c:if>	
	  </ul>	  
	</div>		
</c:when>
<%-- <c:when test="${ not empty art_browser.object && not empty art_browser.field && empty art_browser.value }">

<c:if test="${not empty field_counts}">
	<div class="box">
	  <h3 style="text-align:center">Browse another artifact by <c:out value="${art_browser.field}"/>:</h3>
	  <ul>	
	  <c:forEach var="field_count" items="${field_counts}">
	    <c:set var="artifact_type" value="${field_count.key}"/>
	    <c:set var="count" value="${field_count.value}"/>
	    <c:if test="${ ((artifact_type.displayName ne
	  art_browser.object)) }">
	   	    <li><a href="artifactBrowser?object=${artifact_type}&field=${art_browser.field}"><c:out value="${artifact_type}"/></a></li>	    
	    </c:if>
	  </c:forEach>
	  </ul>
	</div>
</c:if>
</c:when>
<c:when test="${ not empty art_browser.object && not empty art_browser.field && not empty art_browser.value }">
	<div class="box">
	<h3 style="text-align:center">Choose the information to display:</h3>	
	<form action="setCheckboxQuery" method="GET">
	  <input type="hidden" name="object" value="${art_browser.object}"/>
	  <input type="hidden" name="field" value="${art_browser.field}"/>
	  <input type="hidden" name="value" value="<c:out
	  value="${art_browser.value}"/>"/>
	  <input type="hidden" name="page" value="<c:out value="${page}"/>"/>
	  <c:forEach var="checkbox_item" items="${sessionScope.checkbox_query}">
	     <c:set var="checked" value="${checkbox_item.value}"/>
	     <c:choose>
		<c:when test="${checked}">
		  <input type="checkbox" name="recordSubjectName" value="<c:out value="${checkbox_item.key}"/>" checked="checked"/>
		</c:when>
		<c:otherwise>
		  <input type="checkbox" name="recordSubjectName" value="<c:out value="${checkbox_item.key}"/>"/>		
		</c:otherwise>
	     </c:choose>
	     <label for="recordSubjectName"><c:out value="${checkbox_item.key}"/></label><br/>	     
	  </c:forEach>
	  <input type="submit"/>
	</form>
	</div>
</c:when> --%>
</c:choose>
</div>

<div id="main_col">

  <c:choose>
  	<%-- have nothing, first page of browser --%>
    <c:when test="${ empty object }">
		<c:set var="artifactTypes" value="${model.artifactTypes}"/>
    <h3>Browse one of the following artifact types:</h3>
        <ul>
	<c:forEach var="artifactType" items="${artifactTypes}">	
   	  <li><a href="artifactBrowser?object=<c:out value="${artifactType.displayName}"/>"><c:out value="${artifactType.displayName}"/></a></li>	
	</c:forEach>
	</ul>
    </c:when>
    <c:otherwise>
    	<%-- have artifact type --%>
    	<c:choose>
    	<%-- user hasn't chosen field --%>
    	<c:when test="${ empty field }">
   			<c:set var="propertyDisplayNames" value="${model.propertyDisplayNames}"/>
			<h3>Choose a property of the <c:out value="${object}"/> artifact:</h3>
			<h4>or choose <a href="artifactBrowser">a different artifact type</a></h4>	
        	<ul>
			<c:forEach var="property" items="${propertyDisplayNames}">	
 	  		<li><a href="artifactBrowser?object=<c:out value="${object}"/>&field=<c:out value="${property}"/>">		
	   		<c:out value="${property}"/>
	  		</a></li>
			</c:forEach>
			</ul>
   		</c:when>
   		<c:otherwise>
   			<%-- have artifact type and field, now need to diverge depending on whether or not user chose Keyword --%>
   			<c:choose>
   			<c:when test="${field eq 'Keyword'}">
   				<c:choose>
   					<%-- user hasn't chosen keyclass --%>
   					<c:when test="${ empty keyclass }">
   					<c:set var="keyclasses" value="${model.keyclasses}"/>
   					<h3>View <c:out value="${object}"/>s whose <c:out value="${field}"/> category is...</h3>
   						<c:url value="artifactBrowser" var="artBrowserURL">
							<c:param name="object" value="${object}"/>					
   						</c:url>
						<h4>or choose <a href="${artBrowserURL}">a different artifact property</a></h4>	
	  					<ul>   
						<c:forEach var="keyclass" items="${keyclasses}">	
							<c:if test="${not empty keyclass.key}">
							<li>
							<c:url value="artifactBrowser" var="artBrowserURL">
	      						<c:param name="object" value="${object}"/>
	      						<c:param name="field" value="${field}"/>
	      						<c:param name="keyclass" value="${keyclass.key}"/>
	      					</c:url>
    		  				<a href="${artBrowserURL}">     
		    				<c:out value="${keyclass.key}" escapeXml="false"/> [<c:out value="${keyclass.value}"/> keyword<c:if test="${keyclass.value > 1}">s</c:if>]
	  	  					</a>
							</li>
							</c:if>
						</c:forEach>
						</ul>
   					</c:when>
   					<c:otherwise>
   						<c:choose>
   							<%-- user hasn't chosen keyword --%>
   							<c:when test="${ empty keyword }">
   								<c:set var="keywords" value="${model.keywords}"/>
   								<h3>View <c:out value="${object}"/>s with keyword category: <c:out value="${keyclass}"/>...</h3>
   								<c:url value="artifactBrowser" var="artBrowserURL">
									<c:param name="object" value="${object}"/>					
									<c:param name="field" value="${field}"/>					
   								</c:url>
								<h4>or choose <a href="${artBrowserURL}">a different keyword category</a></h4>	
	  							<ul>   
									<c:forEach var="keyword" items="${keywords}">	
									<c:if test="${not empty keyword.key}">
									<li>
									<c:url value="artifactBrowser" var="artBrowserURL">
	      								<c:param name="object" value="${object}"/>
	      								<c:param name="field" value="${field}"/>
	      								<c:param name="keyclass" value="${keyclass}"/>
	      								<c:param name="keyword" value="${keyword.key}"/>
	      							</c:url>
    		  						<a href="${artBrowserURL}">     
		    						<c:out value="${keyword.key}" escapeXml="false"/> [<c:out value="${keyword.value}"/> <c:out value="${object}"/><c:if test="${keyword.value > 1}">s</c:if>]
	  	  							</a>
								</li>
								</c:if>
								</c:forEach>
							</ul>
   							</c:when>
   							<%-- user has object, field=Keyword, keyclass and keyword --%>
   							<c:otherwise>
   								<h3><c:out value="${artifactCount}"/> <c:out value="${object}"/><c:if test="${artifactCount > 1}">s</c:if> 
								whose <c:out value="${field}"/> is <c:out value="${keyword}" escapeXml="false"/></h3>	     
								<h4>You may also <a href="artifactBrowser?object=<c:out	value="${object}"/>&field=<c:out value="${field}"/>">
	  							choose a different Keyword</a></h4>
	  							
	  							<c:import url="includes/pager.jsp">
	   								<c:param name="hitCount" value="${artifactCount}"/>
	    							<c:param name="hitsPerPage" value="${pageSize}"/>
	    							<c:param name="firstHit" value="${page}"/>
	    							<c:param name="threshold" value="3"/>
	    							<c:param name="sourceURL" value="artifactBrowser?object=${object}&field=${field}&keyclass=${keyclass}&keyword=${keyword}&page=$1"/>
	  							</c:import>
	  							
	  							<%@ include file="/includes/artifactRecs.jspf" %>
	  							
	  							<c:import url="includes/pager.jsp">
	   								<c:param name="hitCount" value="${artifactCount}"/>
	    							<c:param name="hitsPerPage" value="${pageSize}"/>
	    							<c:param name="firstHit" value="${page}"/>
	    							<c:param name="threshold" value="3"/>
	    							<c:param name="sourceURL" value="artifactBrowser?object=${object}&field=${field}&keyclass=${keyclass}&keyword=${keyword}&page=$1"/>
	  							</c:import>
   							</c:otherwise>
   						</c:choose>
   					</c:otherwise>
   				</c:choose>
   			</c:when>
   			<c:otherwise>
   				<%-- user did not choose Keyword --%>
   				<c:choose>
   					<%-- no value --%>
   				    <c:when test="${ empty value }">
    					<c:set var="fieldValues" value="${model.fieldValues}"/>
						<h3>View <c:out value="${object}"/>s whose <c:out value="${field}"/> is...</h3>
						<c:url value="artifactBrowser" var="artBrowserURL">
							<c:param name="object" value="${object}"/>					
   						</c:url>
						<h4>or choose <a href="${artBrowserURL}">a different artifact property</a></h4>	     
						<ul>   
						<c:forEach var="fieldValue" items="${fieldValues}">	
							<c:if test="${not empty fieldValue.key}">
							<li>
    		  				<a href="artifactBrowser?object=<c:out value="${object}"/>&field=<c:out
		  					value="${field}"/>&value=<str:encodeUrl><c:out value="${fieldValue.key}" escapeXml="false"/></str:encodeUrl>">     
		    				<c:out value="${fieldValue.key}" escapeXml="false"/> [<c:out value="${fieldValue.value}"/> <c:out value="${object}"/><c:if test="${fieldValue.value > 1}">s</c:if>]
	  	  					</a>
							</li>
							</c:if>
						</c:forEach>
						</ul>
   					</c:when>
   					<%-- have object, field and value --%>
   					<c:otherwise>
						<h3><c:out value="${artifactCount}"/> <c:out value="${object}"/>s 
						whose <c:out value="${field}"/> is <c:out value="${value}" escapeXml="false"/></h3>	     
						<h4>You may also <a href="artifactBrowser?object=<c:out
 	  					value="${object}"/>&field=<c:out value="${field}"/>">
	  					choose a different <c:out value="${field}"/>
	  					</a></h4>
						<c:set var="valueEncoded">
							<str:encodeUrl><c:out value="${value}" escapeXml="false"/></str:encodeUrl>
						</c:set>
	  					<c:import url="includes/pager.jsp">
	   						<c:param name="hitCount" value="${artifactCount}"/>
	    					<c:param name="hitsPerPage" value="${pageSize}"/>
	    					<c:param name="firstHit" value="${page}"/>
	    					<c:param name="threshold" value="3"/>
	    					<c:param name="sourceURL" value="artifactBrowser?object=${object}&field=${field}&value=${valueEncoded}&page=$1"/>
	  					</c:import>

						<%@ include file="/includes/artifactRecs.jspf" %>
						
	  					<c:import url="includes/pager.jsp">
	    					<c:param name="hitCount" value="${artifactCount}"/>
	  	  					<c:param name="hitsPerPage" value="${pageSize}"/>
	    					<c:param name="firstHit" value="${page}"/>
	    					<c:param name="threshold" value="3"/>
	    					<c:param name="sourceURL" value="artifactBrowser?object=${object}&field=${field}&value=${valueEncoded}&page=$1"/>
	  					</c:import>
   					</c:otherwise> <%-- have object, field and value --%>
   				</c:choose>
   			</c:otherwise> <%-- user did not choose keyword --%>
   			</c:choose>
   		</c:otherwise> <%-- have artifact type and field, now need to diverge depending on whether or not user chose Keyword --%>
   		</c:choose>
    </c:otherwise>	<%-- have artifact type --%>		
  </c:choose>	
</div>

</div>
</div>
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
