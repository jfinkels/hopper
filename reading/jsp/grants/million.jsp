<html>
<head><title>Grants</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="grants"/>
    </jsp:include> 	
    
    <div id="content" class="2column">
        <div id="index_main_col">
		<div id="grants">
	<h3>Mining a Million Scanned Books: Linguistic and Structure Analysis, Fast Expanded Search, and Improved OCR</h3>
	
	<p>A collaborative project with <a target="_blank" href="http://ciir.cs.umass.edu/index.html" 
	onclick="javascript: pageTracker._trackPageview('/outgoing/Alpheios');">UMass Amherst</a> and the 
	<a target="_blank" href="http://www.archive.org/" 
	onclick="javascript: pageTracker._trackPageview('/outgoing/InternetArchive');">Internet Archive</a>
	<br/>National Science Foundation Award Number: IIS - <a target="_blank" 
	href="http://www.nsf.gov/awardsearch/showAward.do?AwardNumber=0910165"
	onclick="javascript: pageTracker._trackPageview('/outgoing/NSF0910165');">0910165</a>
	<br/>Data-intensive Computing</p>
	
	<p><a href="/hopper/about/who/gregoryCrane"><b>Gregory Crane, PI</b></a>
	<br/>Perseus Project/Classics Department
	<br/>134C Eaton Hall
	<br/>Tufts University
	<br/>Medford, MA 02155</p>
	
	<p><a href="/hopper/about/who/davidBamman"><b>David Bamman</b></a>
	<br/>Perseus Project
	<br/>134C Eaton Hall
	<br/>Tufts University
	<br/>Medford, MA 02155</p>
	
	<p><b>Project Summary</b></p>
	<p>The Center for Intelligent Information Retrieval at UMass Amherst, the Perseus Digital Library Project at 
	Tufts, and the Internet Archive are investigating large-scale information extraction and retrieval technologies 
	for digitized book collections.</p>

	<p>To provide effective analysis and search for scholars and the general public, and to handle the diversity 
	and scale of these collections, this project focuses on improvements in seven interlocking technologies: improved 
	OCR accuracy through word spotting, creating probabilistic models using joint distributions of features, and 
	building topic-specific language models across documents; structural metadata extraction, to mine headers, chapters, 
	tables of contents, and indices; linguistic analysis and information extraction, to perform syntactic analysis and 
	entity extraction on noisy OCR output; inferred document relational structure, to mine citations, quotations, 
	translations, and paraphrases; latent topic modeling  through time, to improve language modeling for OCR and 
	retrieval, and to track the spread of ideas across periods and genres; query expansion for relevance models, to 
	improve relevance in information retrieval by offline pre-processing of document comparisons; and interfaces for 
	exploratory data analysis, to provide users of the document collection with efficient tools to update complex models 
	of important entities, events, topics, and linguistic features.</p>

	<p>When applied across large corpora, these technologies reinforce each other: improved topic modeling enables more 
	targeted language models for OCR; extracting structural metadata improves citation analysis; and entity extraction 
	improves topic modeling and query expansion.</p>

	<p>The testbed for this project is the growing corpus of over one million open-access books from the Internet Archive.</p>
	
	<p>Undergraduate students involved in this project:
	<ul>
	<li>John Frederick Owen</li>
	<li>Erin Shanahan</li>
	</ul>
	</p>
	
	<p><b>Publications:</b>
	<ul class="publications">
	<li id="pub-bamman10-jcdl">David Bamman, Alison Babeu, Gregory Crane. Transferring Structural Markup Across 
	Translations Using Multilingual Alignment and Projection. In <cite>Proceedings of the 10th ACM/IEEE-CS Joint 
	Conference on Digital libraries (JCDL 2010)</cite>, pages 11-20, Australia : ACM Digital Library, 2010-06. 
	(<a href="http://www.perseus.tufts.edu/publications/jcdl27-bamman.pdf">Full text</a>)</li>
	<li id="pub-crane10-mellon">Gregory Crane. Give us editors! Re-inventing the edition and re-thinking the humanities. 
	In <cite>Online Humanities Scholarship: The Shape of Things to Come</cite>, University of Virgnia : Mellon 
	Foundation, 2010-03. (<a target="_blank" href="http://cnx.org/content/m34316/1.1/">Full text</a>)</li>
	</ul>
	</p>
		</div> <!-- grants div -->
	</div> <!-- main_col div -->
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
