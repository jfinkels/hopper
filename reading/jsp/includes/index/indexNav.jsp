<%@page pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<% 
String tabActive = request.getParameter("tabActive");
String subtabActive = request.getParameter("subtabActive");
%>
<div id="main_top">
    <div id="tabs">
	<jsp:include page="navigation.jsp">
		<jsp:param name="tabNames" value="home,collections,research,grants,opensource,about,help"/>
		<jsp:param name="dispNames"
    value="Home,Collections/Texts,Research,Grants,Open Source,About,Help"/>
		<jsp:param name="tabActive" value="<%= tabActive %>"/>
		<jsp:param name="url" value="$1"/>	
	</jsp:include>
    </div>
    <% if (tabActive.equalsIgnoreCase("about")) { %>
    <div id="subtabs">
    <jsp:include page="navigation.jsp">
    	<jsp:param name="tabNames" value="who,publications,jobs,research"/>
    	<jsp:param name="dispNames" value="Who We Are,Publications,Job Opportunities,Research Opportunities"/>
    	<jsp:param name="tabActive" value="<%= subtabActive %>"/>
    	<jsp:param name="url" value="about/$1"/>
	</jsp:include>
    </div>
   <% } 
   	if (tabActive.equalsIgnoreCase("research")) { %>
   	<div id="subtabs">
   	<jsp:include page="navigation.jsp">
   		<jsp:param name="tabNames" value="background,current"/>
   		<jsp:param name="dispNames" value="Background,Current"/>
   		<jsp:param name="tabActive" value="<%= subtabActive %>"/>
   		<jsp:param name="url" value="research/$1"/>
   	</jsp:include>
   	</div>
   	<% } 
   	if (tabActive.equalsIgnoreCase("help")) { %>
   	<div id="subtabs">
   	<jsp:include page="navigation.jsp">
   		<jsp:param name="tabNames" value="quickstart,copyright,faq,startpoints,texts,searching,vocab,archives"/>
   		<jsp:param name="dispNames" value="Quick Start Guide,Copyright,FAQ,Starting Points,Texts Help,Search Help,Vocab Tool,Archived Help"/>
   		<jsp:param name="tabActive" value="<%= subtabActive %>"/>
   		<jsp:param name="url" value="help/$1"/>
   	</jsp:include>
   	</div>
   	<% }
   	if (tabActive.equalsIgnoreCase("collections") && subtabActive != null && !subtabActive.equals("")) { %>
   	<div id="subtabs">
   	<jsp:include page="navigation.jsp">
   		<jsp:param name="tabNames" value="Greco-Roman,Arabic,Germanic,cwar,Renaissance,RichTimes"/>
   		<jsp:param name="dispNames" value="Greek and Roman,Arabic,Germanic,19th-Century American,Renaissance,Richmond Times"/>
   		<jsp:param name="tabActive" value="<%= subtabActive %>"/>
   		<jsp:param name="url" value="collection?collection=Perseus:collection:$1"/>
   	</jsp:include>
   	</div>
   	<% } 
   	if (tabActive.equalsIgnoreCase("opensource")) { %>
   	<div id="subtabs">
   	<jsp:include page="navigation.jsp">
   		<jsp:param name="tabNames" value="download"/>
   		<jsp:param name="dispNames" value="Download"/>
   		<jsp:param name="tabActive" value="<%= subtabActive %>"/>
   		<jsp:param name="url" value="opensource/$1"/>
   	</jsp:include>
   	</div>
   	<% } %>
</div>
