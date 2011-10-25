<html>
<head><title>Perseus 4.0 Quick Start Guide</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
			<jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="quickstart"/>
	
    </jsp:include>
			<div id="content" class="2column">
				<div id="index_main_col">
					<h3>Perseus 4 Quick Start Guide</h3>
					<p><b>Find a specific text or work:</b></p>
					<p><a id="nav" name="nav"></a><b>Navigate to the work via the Collections: </b></p>
					<p>In some instances, the exact title or Perseus title of a specific work may be unknown. The best way to browse through the works in Perseus is through the Collections.</p>
				
				
					<p><img src="/img/help/qs1.png" alt="image of P4 home page with collections highlighted" border="1"><br>
						<font size="-1">Figure 1: P4 home page (http://www.perseus.tufts.edu/) with Collections link highlighted<br>
						</font></p>
				
					<p>The resulting page will list the available collections in the current release of the Perseus Digital Library.</p>
				
					<p><img src="/img/help/qs2.png" alt="P4 collections (http://www.perseus.tufts.edu/hopper/collections)" border="1"><br>
						<font size="-1">Figure 2: P4 collections (<a href="/hopper/collections">http://www.perseus.tufts.edu/hopper/collections</a>)<br>
						</font></p>
				
					<p>A classical work will be in the Greek and Roman Materials, so in this example, link to that collection<font size="-1">.</font></p>
				
					<p><img src="/img/help/qs3.png" alt="Choosing Greek and Roman Materials (http://www.perseus.tufts.edu/hopper/collections)" border="1"><br>
						<font size="-1">Figure 3: Choosing Greek and Roman Materials (http://www.perseus.tufts.edu/hopper/collections)<br>
						</font></p>
					<p><img src="/img/help/qs4.png" alt="http://www.perseus.tufts.edu/hopper/collection?collection=Perseus:collection:Greco-Roman" border="1"><br>
						<font size="-1">Figure 4: Greek and Roman Materials page<br>
							(<a href="/hopper/collection?collection=Perseus:collection:Greco-Roman">http://www.perseus.tufts.edu/hopper/collection?collection=Perseus:collection:Greco-Roman</a>)<br>
						</font></p>
				
					<p>Works are listed under author names. To find an author, you can search the browser page, or simply scroll down. When there is a blue arrow beside the author name, this means there is a sublist of works related to this author. Simply click the arrow to display the list.</p>
				
						<p><font size="-1"><img src="/img/help/qs5.png" alt="Revealing works associated with Homer" border="1"><br>
							Figure 5: Revealing works associated with author Homer</font></p>
				
					<p>The list will indicate the primary language of the work and information on the editor when available.</p>
					<p></p>
					<p><a id="search" name="search"></a><b>Search on the author and /or title: </b></p>
					<p>If you know the Perseus author and/or title of the work, you can enter this information in any search box, such as the one on the home page. Keep in mind, if you are using a variation on a name or a title, it may not be the name used in Perseus, and it may not return the results you are expecting.<br>
					</p>
					<p>In this example, <tt>Homer</tt> has been entered in the search box.</p>
				
				
					<p><img src="/img/help/qs6.png" alt="close up of search box on P4 home page" border="1"><br>
						<font size="-1">Figure 6: P4 home page (http://www.perseus.tufts.edu) with close up of search box<br>
						</font></p>
				
					<p>The returned results will appear similar to this.</p>
				
					<p><img src="/img/help/qs7a.png" border="1"><br>
						<font size="-1">Figure 7: P4 search results for Homer<br>
							(<a href="/hopper/searchresults?q=Homer">http://www.perseus.tufts.edu/hopper/searchresults?q=Homer</a>)<br>
						</font></p>

					<p>When your search contains a word which pertains to the author (or editor) or title of a work in Perseus, the tool returns a subset of results culled from the collections.  In this example, there are several primary texts and commentaries which pertain to Homer. This is not a <a href="#searching">collection-wide data search</a>, so you are not seeing all of the times to which "Homer" is referred in other texts or artifacts; rather, it's a metadata search. By clicking on the <tt>Homer, Iliad (Greek)</tt> link, we are taken to the Greek of the Iliad:<br>
						<img src="/img/help/qs8.png" alt="http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0133" border="1"><br>
							<font size="-1">Figure 8: <tt>Homer, Iliad (Greek)</tt> link for search of <tt>Homer</tt><br>
								(<a href="/hopper/text?doc=Perseus:text:1999.01.0133">http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0133</a>)</font><br>
					</p>
		
					<p>Perhaps you wanted another version? A translation of the Iliad, for instance. All you need to do is <b>change the focus text</b> by selecting one of the alternate versions in the upper right hand part of the second column.<br>
					</p>
				
					<p><img src="/img/help/qs9.png" alt="(http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0133)" border="1"><br>
						<font size="-1">Figure 9: Other versions of this work<br>
						</font></p>
				
					<p>Simply click the focus link next to the version you would like to study. In this example, the focus has been changed to the English 1924 version, the first version listed in the right side bar.<br>
					</p>
				
					<p><img src="/img/help/qs10.png" alt="(http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0133)" border="1"><br>
						<font size="-1">Figure 10: Changing the focus text<br>
						</font><img src="/img/help/qs11.png" alt="(http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0134:book=1:card=1)" border="1"><br>
						<font size="-1">Figure 11: New focus text (Murray, 1924 ed.)<br>
							(<a href="/hopper/text?doc=Perseus:text:1999.01.0134:book=1:card=1">http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.01.0134:book=1:card=1</a>)<br>
						</font></p>
					<p></p>
		<p><a id="analyze" name="analyze"></a><b>Translate a word.</b></p>
					<p>Many users want to use Perseus to translate an ancient Greek or Latin word. A majority of the time, the word in question is inflected, meaning it is not a form you would find in the dictionary (i.e. a lemma). (For example, in English, you will not find the word <tt>taught </tt>in the dictionary: you must look under <tt>teach</tt>.) Start by <b>analyzing the form with the Word Study Tool</b> rather than looking in the dictionary, since the form may not be in the dictionary.<br>
					</p>
				
					<p>To go to the <b>word study tool, </b>select<b> All Search </b>options under any search box.<br>
					</p>
				
						<p><img src="/img/help/qs12.png" alt="all search options link"><br>
						<font size="-1">Figure 12: All Search Options link<br>
						</font></p>
					<p><img src="/img/help/qs13.png" alt="Perseus Search Tools page (http://www.perseus.tufts.edu/hopper/search)"><br>
						<font size="-1">Figure 13: Perseus Search Tools page (<a href="/hopper/search">http://www.perseus.tufts.edu/hopper/search</a>)</font></p>
				
					<p>To translate a word, use the word study tool in the right hand column of the page. In our example we will translate the Greek<img src="/img/help/qs13a.png" alt="" align="absbottom" border="0"> and the Latin <tt>sequitur</tt>. Click show to show the tool and change the language as appropriate.<br>
						<b>Note:</b> You can only enter <b>one word at a time</b> and not phrases. You <b>must transliterate the Greek</b> according to the guide (you can't enter a Greek font).<br>
					</p>
				
					<p><img src="/img/help/qs14.png" alt="word study examples" border="1"><br>
						<font size="-1">Figure 14: Two word study examples: Greek (L) and Latin (R)<br>
						</font>Note the results for the search of &nbsp;&nbsp;<img src="/img/help/qs13a.png" alt="" align="absbottom" border="0">, which will appear in a new browser window.<br>
					</p>
					<p><img src="/img/help/qs15.png" alt="analysis of legousin" border="1"><br>
						<font size="-1">Figure 15: Results of analysis of legousin.<br>
							(<a href="/hopper/morph?l=legousin&amp;la=greek">http://www.perseus.tufts.edu/hopper/morph?l=legousin&amp;la=greek</a>)</font><br>
				
					<p>In this case, there are three possible verbs from which this form is derived. In each case, the analysis provides a summary of possible forms, and a short definition (in the upper right of each box). To see a full definition, choose one of the lexica listed:<br>
						<br>
					</p>
					<table border="1" cellspacing="0" cellpadding="1">
						<tr>
							<td><font size="-1">LSJ</font></td>
							<td><font size="-1">Greek-English Lexicon by Liddell, Scott &amp; Jones</font></td>
							<td><font size="-1">detailed large lexicon with citations and definitions</font></td>
						</tr>
						<tr>
							<td><font size="-1">Middle Liddell</font></td>
							<td><font size="-1">Intermediate Greek-English Lexicon</font></td>
							<td><font size="-1">lexicon with more concise definitions, fewer citations</font></td>
						</tr>
						<tr>
							<td><font size="-1">Autenrieth</font></td>
							<td><font size="-1">A Homeric Dictionary by Georg Autenrieth</font></td>
							<td><font size="-1">specialized dictionary</font></td>
						</tr>
						<tr>
							<td><font size="-1">Slater</font></td>
							<td><font size="-1">Lexicon to Pindar by William J. Slater</font></td>
							<td><font size="-1">specialized dictionary</font></td>
						</tr>
					</table>
						<p>The dictionary entry will appear in the bottom of the current window and give you the option of opening a new browser window into the lexicon.<br>
					</p>
				
					<p><img src="/img/help/qs16.png" alt="Middle Liddell entry for legw3" border="1"><br>
						<font size="-1">Figure 16: Selecting Middle Liddell for legw3<br>
						</font><img src="/img/help/qs17.png" alt="word study tool window with dictionary entry" border="1"><br>
						<font size="-1">Figure 17: Resulting dictionary entry in bottom of word study tool window.<br>
						</font></p>
				
					<p>Note you can also <b>search</b> for this form with the search link, <b>view word frequency statistics</b>, and <b>view the entry in a new window</b>.<br>
					</p>
					<p><img src="/img/help/qs18.png" alt="Results of analysis of sequitur" border="1"><br>
						<font size="-1">Figure 18: Results of analysis of sequitur.<br>
							(<a href="/hopper/morph?l=sequitur&amp;la=la">http://www.perseus.tufts.edu/hopper/morph?l=sequitur&amp;la=la</a>)<br>
						</font></p>
				
					<p>In this case, there is one possible verb from which this form is derived. The analysis provides a summary of the form, and a short definition (in the upper right of each box). To see a full definition, choose one of the lexica listed:</p>
						<table border="1" cellspacing="0" cellpadding="1">
						<tr>
							<td><font size="-1">Lewis &amp; Short</font></td>
							<td><font size="-1">Latin Dictionary by C. T. Lewis &amp; C. Short</font></td>
							<td><font size="-1">detailed large lexicon with citations and definitions</font></td>
						</tr>
						<tr>
							<td><font size="-1">Elem. Lewis</font></td>
							<td><font size="-1">Elementary Latin Dictionary, C. T. Lewis</font></td>
							<td><font size="-1">lexicon with more concise definitions, fewer citations</font></td>
						</tr>
					</table>
						<p>The dictionary entry will appear in the bottom of the current window and give you the option of opening a new browser window into the lexicon.<br>
						Note you can also <b>search</b> for this form with the search link, <b>view word frequency statistics</b>, and <b>view the entry in a new window</b>.</p>
				
								<p><a id="searching" name="searching"></a><b>Searching the collection.</b></p>
								<p><b>General Searching.</b></p>
								<p>In the top right corner of every page of Perseus, there is a search box, searching examples, and links to specific searching tools. </p>
								<p><img src="/img/help/qs19.png" alt="Search box" border="1"><br>
									<font size="-1">Figure 19: Perseus search box.<br>
									</font></p>
								<p>Note that beneath the box, there are two other links, <tt>All Search options</tt> <tt>[view abbreviations]</tt>. The link to <tt>All Search options</tt> takes you to all searching tools in Perseus, including language-based tools, such as word study, artifact searching, and named entity searching.  <tt>View abbreviations</tt> reveals a list of the Perseus text abbreviations which enable you to jump to specific work and section, particularly useful if you are studying a specific work in detail. For example, if you wanted to read book 3, chapter 9 of Julius Caesar's Gallic War, enter <tt>Caes. Gal. 3.9</tt> in any search box. You will be taken to the work in the default language you have chosen in your Perseus <a href="#preferences">display preference settings</a>.</p>
								<p>For a general, collection-wide search, enter your search term, i.e. <tt>Ajax</tt>. <a href="/hopper/searchresults?q=Ajax">Search for Ajax.</a>  The first results, if applicable, will be for metadata (instances of the search term appearing in Perseus author names or titles). If you are not interested in authors or works, a common search starting point, simply do a full collection wide search by following the link on the second line (<font size="-1">"If you would like to search the content of all documents, please click here."</font>)</p>
								<p><img src="/img/help/qs19a.png" alt="Further searching" border="1"><br>
									<font size="-1">Figure 19a: Broaden your search.<br>
									</font></p>
								
								<p>After a content search, you will see results similar to those pictured below.</p>
								<p><img src="/img/help/qs20.png" alt="Search results" border="1"><br>
									<font size="-1">Figure 19: Mock up of results of a full collection search for "Ajax."<br>
									</font></p>
								<p>The schematic above illustrates the layout of a search result page. The tools on the sidebar enable you to refine your current search (change the language or other parameters), narrow down your search (choose a specific text or subset of texts), and see results elsewhere (such as artifact collections or images).<br>Note that results are grouped by document:  if there are multiple results in a listed document, you will see a <tt>More (xx)</tt> link at the top of the each document listing which enables you to view more results for the selected document.</br></p>
								
								<p><a id="preferences" name="preferences"></a><b>Display preference settings.</b></p>
								<p>There are a few settings which enable you to customize your preferences for repeated visits to Perseus. Visible in the lower right corner on the gray sidebar when reading any text, Display Preferences are retained for your next visit. This is done with a "cookie" for your browser, so if you reopen Perseus with a different browser or on a different computer, you will need to reset the preferences.</p>
								<p><img src="/img/help/qs21.png" alt="Display preferences" border="1"><br>
									<font size="-1">Figure 20: Display preferences box.<br>
									</font></p>
								
								<p>Here you can change how your browser views Greek or Arabic.  You can also change which version of a work you see by default. (For instance, if you are jumping to Caesar's Gallic War using the Perseus abbreviation do you want the focus text to be English or Latin?) In addition, you can choose to view or hide the browse bar above each text.</p>
								
			</div><!-- main_col div -->
				
				<div id="img_side_col" style="font-size:small; text-align:left">
					<h4>Quick Topics</h4>
					<ul>
						<li>Find a specific text or work:
						<ul>
							<li><a href="#nav">Navigate to the work via the Collections</a>
							<li><a href="#search">Search for the author and /or title</a>
						</ul>
						<li><a href="#analyze">Translate a word</a>
						<li><a href="#searching">Searching the collection</a>
							<ul><li>General searching
								<!-- Going to add another page for this?? <li><a href="#specsearch">Specific types of searching</a> -->
								
							</ul>
							<li><a href="#preferences">Display preference settings</a>
					</ul>
				</div>
				
			</div><!-- 2column div -->
			</div><!-- main div -->
		
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>