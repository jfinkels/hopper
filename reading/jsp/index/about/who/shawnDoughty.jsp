<html>
<head><title>Shawn Doughty, Information Technology</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body>
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="about"/>
	<jsp:param name="subtabActive" value="who"/>
    </jsp:include> 	
    
    <div id="content" class="2column">
        <div id="index_main_col">
		<div id="members">
		<p>		
		</p>
		</div> <!-- members div -->
	</div> <!-- main_col div -->
    
    <div id="img_side_col">    	
    </div> <!-- img_side_col -->
    
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>