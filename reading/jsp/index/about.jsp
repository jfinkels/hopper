<html>
<head><title>About the Perseus Digital Library</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="about"/>
    </jsp:include> 	
    
    <div id="content" class="2column">
        <div id="index_main_col">
		<div id="about">
			
<p>
Since planning began in 1985, the Perseus Digital Library Project has explored what happens 
when libraries move online. Two decades later, as new forms of publication emerge and millions 
of books become digital, this question is more pressing than ever. Perseus is a practical 
experiment in which we explore possibilities and challenges of digital collections in a 
networked world. For the mission of Perseus and its current research, see 
<a href="/hopper/research">here</a>.
</p>
<p>
Perseus maintains a web site that showcases collections and services developed as a part of 
our research efforts over the years.  The code for the digital library system and many of the 
collections that we have developed are now available.  For more information, please go 
<a href="/hopper/opensource">here</a>. If you are interested in running a mirror of the Perseus 
Digital Library, please contact the <a href="mailto:webmaster@perseus.tufts.edu">webmaster</a>.
</p>
<p>
Our flagship collection, under development since 1987, covers the history, literature and 
culture of the Greco-Roman world. We are applying what we have learned from Classics to other 
subjects within the humanities and beyond. We have studied many problems over the past two 
decades, but our <a href="/hopper/research/current">current research</a> centers on personalization: 
organizing what you see to meet your needs.
</p>
		</div> <!-- about div -->
	</div> <!-- main_col div -->
        
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
