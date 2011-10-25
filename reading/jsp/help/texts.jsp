<html>
<head><title>Perseus 4.0 Text Help</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
			<jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="texts"/>
	
    </jsp:include>
	<div id="content" class="2column">
		<div id="index_main_col">
		<h3>Perseus 4 Text Help</h3>
		<p><b><a id="quick" name="quick">Quick overview of the page:</a></b>
		<br>There are several ways to access a text: consult the <a href="quickstart">Quick Start Guide</a> for more help.  In this example, we'll start with the beginning of Ovid's 
		Metamorphoses in Latin. In most cases, the layout of a text page will be similar to this (there may be slight differences for secondary sources or reference works).</br>
					
		<p><img src="/img/help/texthelp2.png" alt="image of start of Ptext 1999.02.0029" border="1"><br>
		<font size="-1">Figure 1: Diagram of Ovid's Metamorphoses (Latin, Magnus, ed.) 1999.02.0029; center of page</br></font></p>
					
		<p>The center of a typical text page will have the essential information you need: focus text, navigation tools, credits information, and download links.</p>

		<p><img src="/img/help/texthelp1.png" alt="image of start of Ptext 1999.02.0029" border="1"><br>
		<font size="-1">Figure 2: Diagram of Ovid's Metamorphoses (Latin, Magnus, ed.) 1999.02.0029; sidebars of page</br></font></p>
					
		<p>The sidebars of the typical text page will have supplemental information about a text: the left sidebar has information the collections in which this work is found, 
		different ways of chunking the text, and a document-specific table of contents; the right sidebar has additional links to other editions of this work, cross references, 
		vocabulary, searching specific to this document, and display preferences.</p>
					
		<p><b><a id="persist" name="persist">Persistent content</a></b> appears at the top of each page above the gray bar with collection links. This content is regular throughout 
		Perseus and includes the document/collection/tool title and general searching box.
		<br><b><a id="title" name="title">Document Title</a></b> appears at the top of each page. If you are in a general part of the site (research, help, etc.) this may be identical 
		to the home page.  Otherwise, it will change to the specific document or tool you are using.</br>
		<br><b><a id="gensearch" name="gensearch">Searching (general)</a>:</b> the collection wide search tool appears in the top right corner of each page of Perseus.  The default 
		search is a metadata collection-wide search.  For searching related to the focus text and language, see the <b>document-focused searching</b> below.</br></p>
					
		<p><b><a id="center" name="center">Center of the Page</a></b>:
		<br><a id="focus" name="focus">The <b>Focus Text</b></a> is at the heart of the page. This is the selected document. The amount of text you see depends on the default chunking 
		settings for the particular work. Some works default to small sections; others, larger ones. In general, the selection tends to be small to enable faster page loading. To 
		change the size of the chunk you are viewing, see <b>Text Chunking Options</b> below.</br>
		<br><a id="browse" name="browse">Above the Focus Text and immediately below the gray collection links bar, is the <b>Browse Bar,</b></a> providing a quick overview of the 
		structure of the text and relative size of the section you are currently viewing. Your current position will be marked in blue; other parts or chunks of the text will be in 
		gray. Since every text is divided differently (books/chapters, sections, chapter/verse, etc.), the layout of the browse bar will vary. The browse bar permits you to jump to 
		different sections of a work and get a sense of the relative length of a work. (Is line 500 of this play near the beginning, middle, or end?) The browse bar of the lexica and 
		other reference works have a top line divided alphabetically, followed by further alphabetical subsections which allow you to jump to any given entry in three steps. As with 
		most features on the text pages, you may hide the browse bar by clicking the link on the right side of the bar.</br>
		<br><a id="navarr" name="navarr">Just above the Focus Text, blue <b>Navigation Arrows</b></a> appear, permitting you to "page" through the focus text section by section. You 
		may see both forward and back arrows or just one of the two, depending on your location in a given text.  When you are at the start of the text, as in the above pictured 
		example, only the forward, right-pointing, arrow appears.</br>
		<br><a id="jump" name="jump">To the right of the arrows, is the text <b>"Jump Box."</b></a> Similar in size and appearance to a typical search box, this box permits you to 
		jump to another section of the work or another Perseus work by using the standard Perseus text citation. It is preloaded with your current text section (Ov. Met. 1.1 in our 
		example). It is not a standard search box, however, and is only used for navigating within the texts.</br>
		<br><b>To use the Jump Box</b>, simply enter the standard Perseus citation abbreviation to jump to a specific author and work. This is most helpful if you are researching 
		several sections within a specific text. (Reading Ov. Met. 1.1, 5.3, and 8.2, for instance). <b>What is the standard Perseus citation abbreviation for a given author and 
		work?</b> Check the  [view abbreviations] link just under the top right general search box.  Note you can also use these citation abbreviations in the general search box, 
		too.</br>
		<br>J<a id="credits" name="credits">ust below the focus text, are the <b>Credits</b></a> where you will find bibliographic information about the document, funder information, 
		and notes on the level of accuracy, if applicable. Underneath the credits, are more navigation arrows (there so that you do not have to scroll up the page when reading a large 
		section of text) and an orange <a id="xml" name="xml"><b>XML button</b></a> which allows you to see the xml for this chunk of focus text.  You can either view this in your 
		browser or save it to use in another program. This button only offers xml for the chunk of text you are viewing and is available for all works in Perseus regardless of 
		copyright status.</br>
		<br><a id="downld" name="downld">At the bottom center of the page, <b>Download and License</b></a> information appears, if applicable. In this example, the focus text is in 
		the public domain, and a full download of the xml source of the text is offered under a Creative Commons license. Certain works are not offered for full download, either 
		because we do not believe them to be in the public domain or they are not ready for release. See the <a href="copyright">copyrights page</a> for more on permissions, downloads, 
		fair use, etc.</br></p>
					
		<p><b><a id="left" name="left">Left Side of the Page</a></b>:
		<br><a id="partof" name="partof"><b>This Text is Part of:</b></a> The uppermost gray box to the left of the text denotes the collection(s) and categories to which the current 
		text belongs and offers links to view similarly classified documents or works by the same author. There are often multiple versions of a work in Perseus (original language, 
		different translations).</br>
		<br><a id="chunk" name="chunk"><b>View Text Chunked by:</b></a> In most cases, you will have the option of changing the way the focus text is divided for reading. This box, 
		midway down the left sidebar, allows you to change the amount of text you see on a page.  Be aware that selecting another option, particularly a larger designation, such as 
		a whole book rather than a chapter or section, may slow performance.</br>
		<br><a id="toc" name="toc"><b>Table of Contents</b>:</a> for the focus text appears in the lower gray box of the left sidebar. For works with subsections or particularly large 
		documents, click on the blue toggle triangles to view more information. Table of Contents divisions will match those of the Browse bar, but often provide greater detail.</p>

		<p><a id="right" name="right"><b>Right Side of the Page</b>:</a>
		<br><a id="alted" name="alted"><b>Alternate Editions</b></a> When different Perseus editions or translations of the focus text are available, these will appear at the top of 
		the right sidebar. The language of the text and the editor(s) are displayed in a gray bar, along with "focus" and "load" links. Clicking the "focus" link will change the 
		central display text to the selected focus text. Clicking the "load" link will display the focus passage in the indicated alternate edition. Once you have loaded an alternate 
		edition, the "load" link changes to "show" or "hide." If the alternate edition is already being shown, the "hide" link will close it. To show it again, click "show." <i>Note 
		that Perseus is matching sections of texts based on numeration, not content. If alternate editions come from different sources, translations may not correspond exactly. 
		Editors may have numbered works differently, moved, or removed text based on variant manuscript traditions.</i></br>
		<br><a id="ents" name="ents"><b>Entities and Cross references</b></a> By default, the page shows links to automatically extracted named entities (people, places, dates) and 
		cross-references to the focus passage in other Perseus works, most commonly grammars, commentaries, and lexica. These may be hidden with the "hide" link. Named entities, 
		identified by the Perseus Digital Library system, may be sorted in other ways, as indicated. All entities are linked to Perseus searching for the chosen document. As with 
		other right sidebar items, these may be shown or hidden via "show/hide" links.</br>
		<br><a id="vocab" name="vocab"><b>Vocabulary Tool</b>:</a> When reading in a primary language edition, this shows a short vocabulary list for the current passage of the focus 
		text. This tool analyzes all of the forms in the given passage and sorts entry forms by frequency. Since some words may have more than one dictionary form, all possible forms 
		for a given word are included. These ambiguous words will produce multiple results, some of  which may not reflect the meaning of a word in the given passage. The usefulness 
		of the frequencies depends on the size of the passage. Larger passages may produce more useful results. You may link to the full <a href="/hopper/vocablist">Vocabulary Tool</a> 
		via this sidebar. For more information, see the <a href="vocab">Vocabulary Tool help</a>.</b></br>
					
		<br><a id="contextsearch" name="contextsearch"><b>Search (contextual)</b>:</a> A contextualized search box appears for the focus text: this is the same as the general search 
		box (upper right of the page) preloaded with settings pertaining to the focus text. It will match the language of the focus text by default (if you are reading a Latin text, 
		searching will be in Latin) and be preset to search only the document you are viewing. To search in another language, or further customize your search, follow the "More search 
		options" link to the general search tools.  If you are searching in an inflected language, you'll have the option of searching on inflected or exact forms. Other options may 
		appear depending on the work.</br>
						
		<br><a id="display" name="display"><b>Display Preferences</b>:</a> Here, set a preference for displaying Greek (or Arabic), either with a particular Greek font style or 
		transliteration and choose a default text view when reading works Perseus features in both original and translation. Choose "Original Language" if you prefer to study works 
		in the original Greek, Latin, et al; "Translation" gives you the English translation of the work. This is particularly useful if you regularly use Perseus citation 
		abbreviations to access works rather than collections or contents pages.You may also choose to show or hide the Browse bar by default. Clicking the Update Preferences button 
		will save your preferences for future Perseus sessions.</br>
		<hr>
		<i>revised  22-Dec-09, LMC</i>
		</p>			
	</div><!-- main_col div -->
				
	<div id="img_side_col" style="font-size:small; text-align:left">
		<h4>Perseus 4 Text Help</h4>
		<ul><li><a href="#quick">Quick overview of the page</a></li>
			<li><a href="#persist">Persistent content</a></li>
				<ul><li><a href="#title">Document title</a></li>
				<li><a href="#gensearch">Searching (general)</a></li>
				</ul>
			<li><a href="#center">Center of the page</a></li>
				<ul><li><a href="#focus">Focus Text</a></li>
				<li><a href="#browse">Browse Bar</a></li>
				<li><a href="#navarr">Navigation Arrows</a></li>
				<li><a href="#jump">Jump Box</a></li>
				<li><a href="#credits">Credits</a></li>
				<li><a href="#xml">XML Button</a></li>
				<li><a href="#downld">Download and License</a></li>
				</ul>
			<li><a href="#left">Left Side of the Page</a></li>
				<ul><li><a href="#partof">This Text is Part of</a></li>
				<li><a href="#chunk">View Text Chunked by</a></li>
				<li><a href="#toc">Table of Contents</a></li>
				</ul>
			<li><a href="#right">Right Side of the Page</a></li>
				<ul><li><a href="#alted">Alternate Editions</a></li>
				<li><a href="#ents">Entities and Cross references</a></li>
				<li><a href="#vocab">Vocabulary Tool</a></li>
				<li><a href="#contextsearch">Search (contextual)</a></li>
				<li><a href="#display">Display Preferences</a></li>
				</ul>
			</ul>
		</div>
	</div><!-- 2column div -->
</div><!-- main div -->
		
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>