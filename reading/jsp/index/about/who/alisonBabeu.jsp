<html>
<head><title>Alison Babeu, Digital Librarian</title>
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
		    Alison Babeu has served as the Digital Librarian and research coordinator for the Perseus Project
		    since 2004.    Before coming to Perseus, she worked as a librarian at both the Harvard Business School and the Boston Public Library.  
		    She has a BA in History from Mount Holyoke College and an MLS from Simmons College.  Her current projects include the development 
		    of an open source library of classical texts and a FRBR-inspired catalog as part of the Mellon funded Cybereditions Project. 
		</p>
		    <h4>Selected Publications</h4>
		    <ul>
		        <li>
		            <p>
		                Babeu, Alison. <a
		                    href="http://www.perseus.tufts.edu/~ababeu/PerseusFRBRExperiment.pdf">"Building a "FRBR-Inspired" Catalog: 
		                    The Perseus Digital Library
		                    Experience."</a>(2008).</p>
		        </li>
		        <li>
		            <p>Babeu, Alison, David Bamman, Gregory Crane, Robert Kummer, Gabriel Weaver, <a
		                href="http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.003.00001"> "Named Entity Identification and Cyberinfrastructure." </a>in
		                <em>Proceedings of the 11th European Conference on Research
		                and Advanced Technology for Digital Libraries (ECDL 2007)</em>, (2007), pp.
		                259-270.</p>
		        </li>
		        <li>
		            <p>Stewart, Gordon, Gregory Crane and Alison Babeu, <a
		                href="http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00006">"A New Generation of Textual Corpora: 
		                Mining Corpora from Very Large Collections."</a>in <em>Proceedings of the 7th ACM/IEEE-CS joint conference on Digital libraries</em>
		            , (2007), pp. 356-365.</p>
		        </li>
		        <li>
		            <p>Crane, Gregory and Alison Jones, <a
		                href="http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00007">"The Challenge
		                of Virginia Banks:  An Evaluation of Named Entity Analysis in a 19th Century
		                Newspaper Collection."</a>in <em>Proceedings of the 6th ACM/IEEE-CS joint
		                    conference on Digital libraries</em>, (2006), pp. 31-40.</p>
		        </li>
		        <li>
		            <p>Mimno, David, Alison Jones, Gregory Crane. <a
		                href="http://www.dlib.org/dlib/october05/crane/10crane.html">"Hierarchical Catalog
		            Records: Implementing a FRBR Catalog."</a> in <em>D-Lib Magazine</em>(2005), 11 (10).</p>
		        </li>
		        <li>
		            <p>Mimno, David, Alison Jones, Gregory Crane. <a
		                href="http://www.cs.umass.edu/~mimno/papers/JCDL2005/f74-mimno.pdf">"Finding a
		                Catalog:  Generating Analytical Catalog Records from Well-Structured Digital
		                Texts."</a>in <em>JCDL '05: Proceedings of the 5th ACM/IEEE-CS joint conference on
		                    Digital libraries</em> (2005), pp. 271-280.</p>
		        </li>
		</div> <!-- members div -->
	</div> <!-- main_col div -->
    
    <div id="img_side_col">
    	<!--<img src="/img/davidB.png">-->
    </div> <!-- img_side_col -->
    
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>