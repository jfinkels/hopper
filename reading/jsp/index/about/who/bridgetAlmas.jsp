<html>
<head><title>Bridget Almas, Sr. Software Developer</title>
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
		Bridget is the lead software developer for the Perseus Digital Library, and is a
		contributing developer on the Bamboo Scholarly Services and Corpora Space project groups.
		As one of the primary programmers on the open source Alpheios Project, Bridget was 
		responsible for programming the user interface and web services, contributions to the
		development of the design and architecture of the project, and served as the project manager.		 
		Bridget also has a background in the study of foreign languages, including French and Mandarin Chinese. 
		</p>
		<p>
		<a href="mailto:bridget.almas@tufts.edu">bridget.almas@tufts.edu</a>
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