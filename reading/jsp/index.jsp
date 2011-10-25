<html>
<head><title>Perseus Digital Library</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="home"/>
    </jsp:include> 	
<div id="content" class="2column">
        <div id="main_col">
	    <div id="home">
	    <div align="center"><b>Welcome to Perseus 4.0</b>, also known as the <b>Perseus Hopper</b>. <br>
	    <font size="-1">Read more on the <a href="/hopper/help/versions.jsp">Perseus version history.</a>
	    <br/>New to Perseus? Click <a href="/hopper/help/quickstart.jsp">here</a> for a short tutorial.
	    </font></div>
	        <div id="news">
		    <h4>Announcements</h4>
		    <ul>
		     <li>May 23, 2011:
		     <p>Thanks to a grant from the Google Digital Humanities program, Perseus is pleased to publish TEI XML digital editions of 
		     <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0567">Dio Chrysostom</a> and 
		     <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0572">Dionysius</a>.
		     </p>
             <p>Support from the Mellon Foundation has allowed us to add TEI XML digital editions for  
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0566">Jerome</a>,  
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0569">Minucius Felix</a>, and 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0570">Tertullian</a>.
             </p>                            
	     <p>This release also includes fixes for line number displays in recently released texts, typos in Elegy &amp; Iambus, the citation scheme in <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0405">Athenaeus</a> and entity voting errors.</p>
            </li> 
		    <li>April 14, 2011:
             <p>Thanks to a grant from the Google Digital Humanities program, 
             Perseus is pleased to publish a TEI XML digital edition of 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0540">Diodorus</a>. 
             Support for the Cybereditions project from the Mellon Foundation 
             has allowed us to add as well TEI XML digital editions for  
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0536">Curtius</a>,  
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0537">Horace</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0543">Cicero</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0548">Ovid</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0549">Sidonius</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0554">Prudentius</a> and 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0562">Seneca the Elder</a>.                             
             </p>
             <p>Thanks to support from the U.S. Department of Education and the Max Planck Society
             we have also been able to publish Edward William Lane's 
             <a href="/hopper/text?doc=Perseus%3atext%3a2002.02.0015">Arabic-English Lexicon</a>.</p>                            
            </li> 
		     <li>March 14, 2011:
		     <p>Thanks to a grant from the Google Digital Humanities program, 
		     Perseus is pleased to publish a TEI XML digital edition of 
		     <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0530">Arrian</a>  
		     and to complete the TEI XML digital edition of <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0511">Lucian</a>, initially 
		     published in December. Support for the Cybereditions project from the Mellon Foundation 
		     and Tufts University has allowed us to add as well TEI XML digital editions for  
		     <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0496">Florus</a>,  
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0497">Persius</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0498"> Statius</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0501">Apuleius</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0504">Columella</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0506">Martial</a>, 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0507">Tibullus</a> and 
             <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0508:work=1">the Scriptores Historiae Augustae</a>.               
             </p>               
            </li> 
            <li>January 24, 2011:
            <p>Thanks to a grant from the Google Digital Humanities program, the Perseus Digital Library is pleased to publish TEI XML digital editions for 
            the Greek Poets <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0483">Aratus Solensis</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0495">Colluthus</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0485">Nonnus of Panopolis</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0481">Callimachus</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0484">Lycophron</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0488">Oppian</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0489">Oppian of Apamea</a>, 
            <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0490">Quintus Smyrnaeus</a> 
            and <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0491">Tryphiodorus</a>.
            </p>		    
		    </li> 
		    <li>December 13, 2010:
		    <ul>Thanks to NEH support for the PhiloGrid Project, the Perseus Digital Library is pleased to publish TEI XML digital editions for 
		    <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0324">Plutarch</a>, 
		    <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0405">Athenaeus</a>, 
		    the <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0472"><span style="font-style:italic;">Greek Anthology</span></a>, 
		    <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0477"><span style="font-style:italic;">Elegy and Iambus</span></a> and 
		    for most of <a href="/hopper/text?doc=Perseus%3atext%3a2008.01.0471">Lucian</a>. 
		    This increases the available Plutarch from roughly 100,000 to the surviving 1,150,000 
		    words. Athenaeus and the <span style="font-style:italic;">Greek Anthology</span> 
		    are new within the Perseus Digital Library, with roughly 270,000 and 160,000 words 
		    of Greek. The 13,000 words for J.M. Edmonds <span style="font-style:italic;">Elegy 
		    and Iambus</span> include both the surviving poetic quotations and major contexts 
		    in which these poems are quoted. The 200,000 words of Lucian represent roughly 70% 
		    of the surviving works attributed to that author. In all, this places more than 1.6 
		    million words of Greek in circulation. With this release, we have also changed the 
		    license for opensource texts to <a href="http://creativecommons.org/licenses/by-sa/3.0/us/" 
		    onclick="javascript: pageTracker._trackPageview('/outgoing/creativeCommons');">Creative Commons Attribution-ShareAlike</a>,
		    removing the non-commercial restriction that we adopted in March 2006 when we first 
		    began making our XML source texts available under a CC license. See our post on the
		    <a href="http://www.stoa.org/archives/1332" onclick="javascript: pageTracker._trackPageview('/outgoing/stoa">Stoa 
		    Consortium</a> blog for full details on the release.
		    </ul>
		    </li>
		    <li>October 22, 2010:
		    <ul>
		    <li><b>Updates to Perseus Digital Library:</b> A
		    new release of the website is now available.  We
		    have added new texts for current authors, such as
		    Plutarch and Servius.
		    </li>
		    </ul>
		    </li>
		    
		    <li>February 5, 2010:
		    <ul>
		    <li>We have fixed the problem with viewing full-size images in IE7 and 8.
		    </li>
		    </ul>
		    </li>
		    
		    <li>February 4, 2010:
		    <ul>
		    <li>A new release of our source code is now available on <a href="http://sourceforge.net/projects/perseus-hopper" 
onclick="javascript: pageTracker._trackPageview('/outgoing/sourceForgeHopper');">SourceForge</a>. Updated data and text files are also available <a href="/hopper/opensource/download">here</a>.
		    </li>
		    </ul>
		    </li>
		    
		    <li>February 1, 2010:
		    <ul>
		    <li>Become a fan of Perseus on <a target="_blank" 
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Facebook');" 
		    href="http://www.facebook.com/perseusproject">Facebook</a>!
		    </li>
		    <li><b>Problems viewing images on IE 7 and 8:</b> We are aware of the problem with viewing full-size images through IE 7 or 8 and 
		    are working to fix it. For the best experience on Perseus, we always recommend using the latest version of <a target="_blank" 
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Firefox');" 
		    href="http://www.mozilla.com/firefox/">Firefox</a>.</li>
		    </ul>
		    </li>
		    
		    <li>December 15, 2009:
		    <ul>
		    <li><b>Updates to Perseus Digital Library:</b> The <a target="_blank" href="/hopper/vocablist">Vocabulary Tool</a> is now 
		    available. For more information about this tool, please see the <a target="_blank" href="/hopper/help/vocab">help page</a>.
		    </li>
		    </ul>
		    </li>
		    
		    <li>October 7, 2009:
		    <ul>
		    <li><b>Updates to Perseus Digital Library:</b> We have added many new authors and texts 
		    to our collection, including <a href="/hopper/searchresults?q=seneca">Seneca</a>, 
		    <a href="/hopper/searchresults?q=quintilian">Quintilian</a>, 
		    <a href="/hopper/searchresults?q=flaccus">Flaccus</a>, 
		    <a href="/hopper/searchresults?q=cicero">Cicero</a>, 
		    <a href="/hopper/searchresults?q=gellius">Aulus Gellius</a>, 
		    <a href="/hopper/searchresults?q=ammianus">Ammianus</a>
		     and <a href="/hopper/searchresults?q=petronius">Petronius</a>.
		    </li>
		    </ul>
		    </li>
		    
		     <li>March 31, 2009:
		    <ul>
		    <li><b>Updates to Perseus Digital Library:</b> You can now view the places mentioned
		    in the Perseus Digital Library by <a href="/hopper/collections">collection</a> or text 
		    through the Google Maps interface. Links to view places in a text can be found on a 
		    text page, in the Places box. Places are automatically extracted, so please email the 
		    <a href="mailto:webmaster@perseus.tufts.edu">webmaster</a> if you notice errors.
		    </li>
		    </ul>
		    </li>
		    
		    <li>March 16, 2009:
		    <ul><li><b>New job announcement:</b> Perseus is seeking a <b><a href="/hopper/about/jobs">Greek Treebank 
		    Editor</a></b> to supervise the creation of a syntactic database for classical Greece with 
		    1,000,000 words - one of the most promising instruments ever produced for the study of Greek 
		    linguistics, literary style, and lexicography.  We encourage graduate students who could build 
		    their dissertation work on this project, as well as classicists with PhD in hand, to consider 
		    applying.
		    
		    </li>
		    <li><b>Updates to Perseus Digital Library:</b>
		    <ul>
		    <li>Many improvements to the Art & Archaeology data and interface.  You can now <a href="/hopper/search">search</a> the 
		    	A&A data and image captions.
		    </li>
		    <li>Euclid's <i>Elements</i> have been added, as well as a large number of Plutarch texts, edited by Bernadotte Perrin.
		    Links to these texts can be found on the <a href="/hopper/collection.jsp?collection=Perseus:collection:Greco-Roman">Greek and 
		    Roman collection</a> page.
		    </li>
		    </ul>
		    </li>
		    </ul>
		    </li>
		    
		   </ul>
		<p><a href="/hopper/help/oldannounce.jsp">Read older announcements...</a></p> 
		</div>

		<div id="contact_info">
		    <p>Perseus contact and support <a href="/hopper/help">information</a>.</p>

		    <p>Perseus is a non-profit enterprise, located in the 
		    <a target="_blank"
		    href="http://ase.tufts.edu/classics"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/ClassicsDept');">Department 
		    of the Classics</a>, 
		    <a target="_blank" href="http://www.tufts.edu"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Tufts');">Tufts 
		    University</a>.</p>

			<p>The Perseus Project is funded by the 
			<a target="_blank" href="http://alpheios.net"
			onclick="javascript: pageTracker._trackPageview('/outgoing/Alpheios');">Alpheios 
			Project</a>, the 
			<a target="_blank" href="http://www.mellon.org/"
			onclick="javascript: pageTracker._trackPageview('/outgoing/Mellon');">Andrew
			W. Mellon Foundation</a>, the 
			<a target="_blank" href="http://www.ed.gov/"
			onclick="javascript: pageTracker._trackPageview('/outgoing/DeptOfEd');">U.S. 
			Department of Education</a>, the 
			<a target="_blank"
			href="http://www.imls.gov/"
			onclick="javascript: pageTracker._trackPageview('/outgoing/IMLS');">Institute 
			of Museum and Library Services</a>, the 
			<a target="_blank" href="http://www.neh.gov/"
			onclick="javascript: pageTracker._trackPageview('/outgoing/NEH');">National 
			Endowment for the Humanities</a>, 
			<a href="/hopper/help/support.jsp">private donations</a>, and 
			<a target="_blank"
			href="http://www.tufts.edu/"
			onclick="javascript: pageTracker._trackPageview('/outgoing/Tufts');">Tufts 
			University</a>.
			</p>
	    
		    <p>Support for the project has been provided by the 
		    <a target="_blank" href="http://www.learner.org/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Annenberg');">Annenberg/CPB 
		    Project</a>, 
		    <a target="_blank" href="http://www.apple.com/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Apple');">Apple Computer</a>, the 
		    <a target="_blank"
		    href="http://www.library.tufts.edu/tisch/Berger/bergerhome.html"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Berger');">Berger 
		    Family Technology Transfer Endowment</a>, 
		    <a target="_blank"
		    href="http://www.dli2.nsf.gov/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/DLI2');">Digital 
		    Libraries Initiative Phase 2</a>, 
		    the <a target="_blank"
		    href="http://www.ed.gov/FIPSE/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/FIPSE');">Fund
		    for the Improvement of Postsecondary Education</a> 
		    part of the <a target="_blank"
		    href="http://www.ed.gov/" 
		    onclick="javascript: pageTracker._trackPageview('/outgoing/DeptOfEd');">U.S. 
		    Department of Education</a>, 
		    the <a target="_blank"
		    href="http://www.getty.edu/grant/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Getty');">Getty 
		    Grant program</a>, the 
		    <a target="_blank" 
		    href="http://www.mla.org/" 
		    onclick="javascript: pageTracker._trackPageview('/outgoing/MLA');">Modern Language Association</a>, the 
		    <a target="_blank" href="http://arts.endow.gov/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/NEA');">National 
		    Endowment for the Arts</a>, the 
		    <a target="_blank" href="http://www.nsf.gov/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/NSF');">National 
		    Science Foundation</a>, the 
		    <a target="_blank" href="www.packhum.org/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Packard');">Packard 
		    Humanities Institute</a>, 
		    <a target="_blank" href="http://www.xerox.com/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Xerox');">Xerox Corporation</a>, 
		    <a target="_blank" href="http://web.bu.edu/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/BU');">Boston University</a>, and 
		    <a target="_blank" href="http://www.harvard.edu/"
		    onclick="javascript: pageTracker._trackPageview('/outgoing/Harvard');">Harvard University</a>.</p>
		</div> 
	    </div> 

	</div> 

	    <%@ include file="includes/index/sidecol.jsp" %>
    </div> <%-- home --%>
</div> <%-- main --%>

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
