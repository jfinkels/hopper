<html>
  <head><title>Perseus Recent Announcements</title>
    <link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
    <div id="header">
      <%@ include file="/includes/index/header.jsp" %>
      
      <div id="main">
 	  <jsp:include page="/includes/index/indexNav.jsp">
	    <jsp:param name="tabActive" value="help"/>
	  </jsp:include>
	  
	  <div id="content" class="2column">
            <div id="index_main_col">
	      <div id="home">
	        <div id="news">
		  <h3>All Announcements</h3>
		  <ul>
		  
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

			<li>September 23, 2008:
			<ul><li><b>Updates to Perseus Digital Library: </b></li>
			<ul>
			  <li>The first release of the <a href="/hopper/collection?collection=Perseus:collection:Arabic">Arabic Collection</a>.</li>
			  <li><a href="/hopper/search">Search tools</a> have been refined.</li>
			  <li>Navigation in 4.0 has been updated.</li>
			  <li>Art & Archaeology data has been
			  updated. The A & A Artifact Browser can now be found
			  under the <a href="/hopper/collections">Collections</a> tab</li>
			  <li>General information on the Perseus
			  Project has been added: <a href="/hopper/research">Research</a>,
			  <a href="/hopper/grants">Grants</a>, 
			  <a href="/hopper/about/research">Research Opportunities</a>, and
			  <a href="/hopper/about/jobs">Job Opportunities</a>.</li>
			  <li>A dedicated <a href="/hopper/opensource">Open Source</a> section.</li>
			  <li>Our list
			  of <a href="/hopper/about/publications">Publications</a>
			  has been moved to the About section.</li>
			  <li>4.0 Help pages have been updated.</li>
			  <li>Additional hardware has been added to
			  the Perseus backend servers.</li>
			</ul>
			</ul>
			</li>
			
		    <li>May 13, 2008:
		      <ul>
				<li><strong>New updates to Perseus Hopper open source release:</strong> 
				The open source release of the Perseus Hopper has been updated. You now have 
				the choice of downloading the data generated by the installation process rather 
				than loading the data using the texts. The text files continue to be available 
				under the <a href="http://creativecommons.org/licences/by-nc-sa/3.0/us">Creative 
				Commons license</a>.
			  <ul>
			    <li>Download the latest release of source code from <a href="http://sourceforge.net/projects/perseus-hopper">SourceForge</a>.
			    <li>Download the text files or data <a href="/opensource/">here</a>.
			  </ul>
		      </ul>
		      
		    <li>March 28, 2008:
		      <ul>
				<li><b>Updates to Perseus Digital Library: </b>
			  <ul>
			    <li>Searching has been re-enabled.
			    <li>The first 4.0 release of the Renaissance Collection is now available.
			    <li>Word study tools and word frequencies have been refined.
			    <li>Additional memory has been added to all Perseus servers.
			  </ul>
		      </ul>
		      
		    <li><span class="date">February 7, 2008</span>:
		      <ul>
				<li><strong>Building a &quot;FRBR-Inspired&quot; Catalog</strong>: The 
				Perseus Digital Library has been exploring the creation of a FRBR-Inspired 
				catalog for classics, and with funding from the Mellon Foundation, has taken 
				some preliminary steps beyond our initial work first reported in 
				<a href="http://www.dlib.org/dlib/october05/crane/10crane.html">October 2005</a>. 
				If you are interested in reading more, please check out our new 
				<a href="http://www.perseus.tufts.edu/~ababeu/PerseusFRBRExperiment.pdf">report</a>.
		      </ul>
		      
		    <li><span class="date">November 9, 2007</span>:
		      <ul>
				<li><strong>Install Perseus 4.0 on your computer</strong>: All of the source code 
				for the Perseus Java Hopper and much of the content in Perseus is now available 
				under an open source license. You can download the code, compile it, and run it 
				on your own system. This requires more labor and a certain level of expertise for 
				which we can only provide minimal support. However, since it will be running on your 
				own machine, it can be much faster than our website, especially during peak usage 
				times. You also have the option to install only certain collections or texts on your 
				version, making it as specialized as you wish. Also, if you want to use a different 
				system to make the content available, you can do so within the terms of the 
				<a href="http://creativecommons.org/licences/by-nc-sa/3.0/us">Creative Commons</a> 
				license. This is the first step in open sourcing the code as you can modify the code 
				as much as you want, but at this time, we cannot integrate your changes back into our 
				system. That is our ultimate goal, so keep a look out for that!
			  <ul>
			    <li>Download source code <a href="http://sourceforge.net/projects/perseus-hopper">here</a>
			    <li>Download text data <a href="/opensource/">here</a>
			  </ul>
		      </ul>
		      
		      <!-- 
			   
			   <li><span class="date">March 27, 2008</span>:
			     <ul>
			       <li><strong>Site updates</strong>: Please be
				 patient as the website may
				 be slow for next day while we update the
				 website.
			       </li>
			     </ul>
			   </li>
			   
		    <li><span class="date">March 26, 2008</span>:
		      <ul>
			<li><strong>Searching function</strong>: The search function in Perseus 4.0 is not yet operable, but will be with the upcoming site update. This tool will be working shortly!</li>
		      </ul>
		    </li>
		    
		    -->
		    
		    <li><span class="date">November 15, 2006</span>: 
              <ul>
            		<li><strong>
             		    Classics in a Digital World</strong>: Curious about where classics might go in a digital
			  			world? See the <a href="http://dl.tufts.edu/view_pdf.jsp?urn=tufts:facpubs:gcrane-2006.00003">preprint
			    		of a new article about ePhilology</a> that will appear in The Blackwell Companion to Digital Literary Studies.
		      </ul>
		      
			<!--<li><span class="date">October 12, 2006</span>:
			    <ul>
			      <li><strong>Issues resolved</strong>: Morphological analyses should now be up and running again; many texts that were throwing errors are now working properly. Our sincere apologies for the issues. Please keep reporting any errors--we're not any more pleased about them than you are!</li>
			    </ul>
			</li>
			<li><span class="date">October 10, 2006</span>:
			  <ul>
			    <li><strong>More site issues</strong>: the
			      maintenance seems to have resulted in some problems.  One of them,
			      a display bug in Internet Explorer, was fixed this morning; others
			      (including "null" showing up in place of some texts and lexicon
			      entries not appearing on morphological analyses) we're hoping to
			      fix today or tommorrow.  We're very sorry for the trouble!</li>
			  </ul>
			</li>
			<li><span class="date">August 16, 2006</span>:
			  <ul>
		        <li><strong>Hardware problems!</strong>:  A
			  key server in the Perseus site has experience intermittent
			  hardware problems.  We are in the process of upgrading this
			  hardware and creating a more stable hardware environment.  We
			  apologize for the delays  and hope to have the problem resolved in
			  a few days.</li>
			<li><strong>Vocabulary Tool</strong>:  A first
			  release at a new Vocabulary Tool widget is now available on the
			  text page.</li>
			  </ul>
			  
			</li> -->
			
			<li><span class="date">June 21, 2006</span>:  Art and Archaeology Browser Updated
		    
		   	<li><span class="date">March 15, 2006</span>: Improvements to the Perseus Digital Library
			<ul>
			  <li><strong>Migration of core data to the Tufts University Repository</strong>: We are beginning 
			  to shift core Perseus data to the Tufts Institutional Repository, where it will become a part of 
			  the university's permanent collection. There are several implications: </li>
			  <li><strong>Preservation</strong>: This step addresses the long term needs of preservation and 
			  access: while Perseus has been in operation for almost two decades, libraries are better suited 
			  to maintain collections over time than particular projects. </li>
			  <li><strong>Separation of production from research</strong>: The on-line version of the 
			  Perseus Digital Library, now more than ten years old, has combined services with research 
			  and development activities. As time progresses, established services will shift to the 
			  institutional repository, with the Perseus Digital Library focusing progressively more 
			  on research and development. As research services become established and prove useful, 
			  they will subsequently migrate to the production server. </li>
			  <li><strong>Named entity browsing and searching</strong>: Perseus has extracted placenames 
			  and dates from full text for more than five years. This version of the Perseus Digital 
			  Library adds additional functionality:
			    <ul>
			      <li>You can now search for and browse placenames and dates in Perseus documents. </li>
			      <li>We are adding personal names and will soon add other categories (e.g., 
			      organizations). Personal names are in the new Perseus American collection and will 
			      be added to classical texts. Classical texts have placenames and dates marked in 
			      their public XML source.</li>
			    </ul>
			  </li>
			  <li><strong>Downloadable XML source texts</strong>: Public domain primary materials 
			  are now available under a Creative Commons license for download in their native XML 
			  format.</li>
			  <li><strong>Bug fixes and incremental modifications</strong>: many general optimizations 
			  have been implemented, and various display issues have been fixed, based on user 
			  reports.</li>
			  <li><strong>Improved hardware</strong>: we are adding new servers that are not only 
			  faster but easier to manage. This should improve not only speed but reliability.</li>
			</ul>
		    
		    <li><span class="date">May 27, 2005:</span><strong> <a href="http://www.perseus.tufts.edu/hopper/">
		    Perseus 4.0</a> released -- a new implementation of the Perseus Digital Library.</strong>
				<ul>
			  		<li>Perseus 4.0, a new Java-based version of the Perseus Digital Library, is available for testing. It
			    		contains a faster, more manageable back-end and a more modern look and feel.
			    		Many features of Perseus are now available as XML services -- for example, developers can extract <a href="http://www.perseus.tufts.edu/hopper/xmlchunk?doc=Perseus%3Atext%3A1999.02.0002%3Abook%3D1%3Achapter%3D1">
			      		well-formed XML fragments</a> of primary sources with full TEI-conformant
			   			markup in order to create their own front ends. <a href="/PR/perseus4.0.ann.full.html">Read more...</a></li>
				</ul>
			
		      <li><a href="/hopper/help/archived/archann.html">Earlier announcements</a>
		    </ul>
		</div> <!-- news -->
		
	      </div> <!-- home -->
	      
	    </div> <!-- main_col -->
	    
	  </div> <!-- content -->
	</div> <!-- main -->
	
	<!-- Google Analytics --> 
	<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>
