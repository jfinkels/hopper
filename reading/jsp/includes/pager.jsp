<%@page pageEncoding="utf-8"
        contentType="text/html;charset=utf-8"%>
<div class="pager">
<%

int hitCount = Integer.parseInt(request.getParameter("hitCount")); //numHits
int hitsPerPage = Integer.parseInt(request.getParameter("hitsPerPage"));
int firstHit = Integer.parseInt(request.getParameter("firstHit")); //currentHit
int threshold = Integer.parseInt(request.getParameter("threshold"));
String sourceURL = request.getParameter("sourceURL");

String transformedURL;

if ((hitCount-1) / hitsPerPage > 0) {
 	int currentPage = firstHit / hitsPerPage;
    int lastPage = (hitCount-1) / hitsPerPage;

 	if (currentPage > 0) {
 		transformedURL = sourceURL.replaceAll("\\$1", "0");
%>
    	<a href="<%= transformedURL %>"><img src='/img/westend.gif' border='0'/></a>
<%
    	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(firstHit-hitsPerPage));
%>
    	<a href="<%= transformedURL %>"><img src='/img/west.gif' border='0'/></a>
<%
 	}
  	for (int thisPage = 0; thisPage <= lastPage; thisPage++) {
		int distance;
		if (thisPage < currentPage) {
	    	distance = currentPage - thisPage;
		} else {
	    	distance = thisPage - currentPage;
		}
		
		if (distance == 0) {
	    	%> <%= thisPage+1 %> <%
		} else if (distance <= threshold) {
			transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(thisPage * hitsPerPage));
%>			<a href="<%= transformedURL %>"><%= thisPage+1 %></a><%
		} else if (distance == threshold+1) {
	    	%> ... <%
		}
    }
    if (currentPage < lastPage) {
    	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(firstHit+hitsPerPage));
%>
    	<a href="<%= transformedURL %>"><img src='/img/east.gif' border='0'/></a>
<% 
     	transformedURL = sourceURL.replaceAll("\\$1", String.valueOf(lastPage*hitsPerPage));
%>
    	<a href="<%= transformedURL %>"><img src='/img/eastend.gif' border='0'/></a>
<%
    }
}
%>
</div>