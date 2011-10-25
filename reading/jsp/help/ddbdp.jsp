<html>
  <head>
    <title>DDBDP Help</title>
    <link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
    <%@ include file="/includes/index/header.jsp" %>
    
    <div id="main">
      <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
      </jsp:include>
      
      <div id="content" class="2column">
	<div id="index_main_col">
	  <p>The Duke Databank of Documentary Papyri is no longer hosted by the Perseus Digital Library.  
	  An updated and expanded version is now located <a href="http://papyri.info/" 
	  	target="_blank" 
	  	onclick="javascript: pageTracker._trackPageview('/outgoing/papyri');">here</a>.</p>
	</div> <!-- main_col -->
      </div> <!-- 2column -->
      
    </div> <!-- main -->
    
    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>
