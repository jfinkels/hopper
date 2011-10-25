<html>
<head><title>Perseus 4.0 Searching Help</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
			<jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="searching"/>
	
    </jsp:include>
	<div id="content" class="2column">
		<div id="index_main_col">
		<h3>Perseus 4 Searching Help</h3>
		<p><b><a id="quick" name="quick">Quick overview of the page:</a></b>
		<br>To reach the main searching page, click the link to All Search Options which appears in the upper right corner of the page under the search box.</br>
			
			<p><img src="/img/help/qs6.png" alt="image of http://www.perseus.tufts.edu/" border="1"><br>
				<font size="-1">Figure 1: Image of home page with searching box highlighted</br></font></p>
									
		<p>When you first open the searching page, most of the tools will be hidden.  The main search tool, however, will be visible.</p>
			
			<p><img src="/img/help/searching1.png" alt="image of http://www.perseus.tufts.edu/hopper/search" border="1"><br>
				<font size="-1">Figure 2: Image of main searching page: http://www.perseus.tufts.edu/hopper/search</br></font></p>

			<p><b><a id="types" name="types">Types of searches:</a></b>
		<br>There are several different types of content in Perseus; many materials have specialized search tools:</br>
		<br>General Searching tools:</br>
			<ul><li>Collection search:  the main search tool, and the broadest, most-encompassing search</li>
			<li>English to [Language] Lookup: searches the English definitions of language-specific lexica</li>
			<li>Dictionary Entry Lookup: search the entries in language-specific dictionaries</li>
			<li>Art and Archaeology Search: search the artifact collections and/or image captions</li>
			</ul>
				<br>Specialized searching and analysis:</br>
				<ul><li>Named Entity Search Tools: find named entities identified within the collections (places, people and dates)</li>
				<li>Word Study Tool: analyzes the morphology of inflected word forms</li>
					<li>Vocabulary Tool: produces a vocabulary list for a text or set of texts</li></ul>
			</p>
			
			<p><b><a id="collection" name="collection">Collection search</a></b>
			<br>This is the main search tool, and provides the broadest, most-encompassing search of the digital libary.</br>
				<br>Features and options:</br>
				<ul><li>Multi-lingual. With it, search in English or other languages (only search in one language at a time). Note that languages which do not use the Roman alphabet (such as Greek) require transliteration.</li>
				<li>Narrow search by collection.  With the checkboxes, limit your search only to the checked collections.</li>
					<li>Search for all forms of an inflected word. For languages like Greek and Latin, use the checkbox "Search for all possible forms" (or "Expand" to find all recognized forms of your word or words. So, for example, if <a href="/hopper/searchresults?target=greek&collections=Perseus%3Acollection%3AGreco-Roman&collections=Perseus%3Acollection%3AArabic&collections=Perseus%3Acollection%3AGermanic&collections=Perseus%3Acollection%3Acwar&collections=Perseus%3Acollection%3ARenaissance&collections=Perseus%3Acollection%3ARichTimes&collections=Perseus%3Acollection%3ADDBDP&all_words=lo%2Fgoi&all_words_expand=on&phrase=&any_words=&exclude_words=&search=Search">searching on all possible forms of lo/goi</a>, you'll see results for lo/gous, lo/gou, etc. Leaving the box unchecked however, <a href="/hopper/searchresults?target=greek&collections=Perseus%3Acollection%3AGreco-Roman&collections=Perseus%3Acollection%3AArabic&collections=Perseus%3Acollection%3AGermanic&collections=Perseus%3Acollection%3Acwar&collections=Perseus%3Acollection%3ARenaissance&collections=Perseus%3Acollection%3ARichTimes&collections=Perseus%3Acollection%3ADDBDP&all_words=lo%2Fgoi&phrase=&any_words=&exclude_words=&search=Search">searches only for the specified form.</a></li>
					<li>Limit the search. Choose to search on a word or phrase and require or exclude certain words. For example, <a href="/hopper/searchresults?target=en&collections=Perseus%3Acollection%3AGreco-Roman&collections=Perseus%3Acollection%3AArabic&collections=Perseus%3Acollection%3AGermanic&collections=Perseus%3Acollection%3Acwar&collections=Perseus%3Acollection%3ARenaissance&collections=Perseus%3Acollection%3ARichTimes&collections=Perseus%3Acollection%3ADDBDP&all_words=&phrase=swift+footed&any_words=&exclude_words=Apollo&search=Search">search on the phrase "swift footed" and exclude the word Apollo.</a></li>
					<li>Quick metadata results. If the tool recognizes your search terms as part of the collection metadata, such as an author name or work title, a short list of these results may appear rather than a full collection search. This allows for faster searching and linking. To proceed with a full search, simply click the link provided. See an example <a href="/hopper/help/quickstart#searching">here</a>.</li></ul></p>
			
			<p><b><a id="engtolang" name="engtolang">English to [Language] Lookup</a></b>
				<br>The Engligh to [Language] Lookup assists with translation from English into one of the featured languages, but is not a translation tool per se. Perseus does not provide natural-language-style translation tools where one enters a word or phrase and receives an alternate language equivalent. (The Word Study Tool analyzes inflected forms from the original language, <a href="/hopper/help/quickstart#analyze">as noted here</a> and below.)  In lieu of direct translation, this tool searches words marked as English definitions or glosses in Perseus dictionaries. So, a search on "love" in Latin dictionaries, will return many entries which may or may not have a direct correlation. A quick scan of the most common words will assist in narrowing down the results.</br></p>
			
			<p><b><a id="dictent" name="dictent">Dictionary Entry Lookup</a></b>
				<br>The Dictionary Entry Lookup searches the dictionaries and lexica for entries only. Words in the dictionaries not marked as entries will not be found. This tool is most helpful if you know the starting characters of an entry and want to browse the list of entries which match your search.</br></p>
			
			<p><b><a id="artarch" name="artarch">Art and Archaeology Search</a></b>
				<br>Although the general collection search will return results in the Art and Archaeology artifacts, this tool provides an additional means of searching only this subcollection of materials. You may limit your search to the artifacts, including the names and descriptions, or the captions of images in the collection, or search both sets of data together.</br></p>
			
			<p><b><a id="entity" name="entity">Named Entity Search Tools</a></b>
				<br>Through a combination of automatic data retrieval and other refinements, the digital library has compiled a set of named entities in various collections which help the user study marked places, people and dates. This is particularly useful when names are amibiguous (Athens, Greece versus Athens, Georgia, United States; Alexander or Washington). More tips on using these tools are listed when you "show" the full tool.</br></p>
			
			<p><b><a id="wordstudy" name="wordstudy">Word Study Tool</a></b>
				<br>Known also as the morphological analysis tool, this tool allows the user to analyze a form in an inflected language. For some languages, you may need to transliterate the word before entering it. (Note that for Greek we recommend transliteration according to the chart above the tool, but some users find that Unicode Greek will work well with this tool only). If Perseus recognizes the form, an analysis, short definition, and links to word search results will be provided. More information is in the <a href="hopper/help/quickstart#analyze">quick start guide</a>.</br></p>
			
			<p><b><a id="vocab" name="vocab">Vocabulary Tool</a></b>
				<br>A link is provided to the full Vocabulary Tool for featured languages. More help for this tool is <a href="/hopper/help/vocab">here.</a></br></p>
			
			
			
		<hr>
		<i>created 12-Feb-10, LMC</i>
		</p>			
	</div><!-- main_col div -->
				
	<div id="img_side_col" style="font-size:small; text-align:left">
		<h4>Perseus 4 Text Help</h4>
		<ul><li><a href="#quick">Quick overview of the page</a></li>
			<li><a href="#types">Types of Searches</a></li>
				<ul><li><a href="#collection">Collection Search</a></li>
				<li><a href="#engtolang">English to [Language] Lookup</a></li>
			<li><a href="#dictent">Dictionary Entry Lookup</a></li>
					<li><a href="#artarch">Art and Archaeology Search</a></li>
					<li><a href="#entity">Named Entity Search Tools</a></li>
					<li><a href="#wordstudy">Word Study Tool</a></li>
				<li><a href="#vocab">Vocabulary Tool</a></li>
					</ul>
	
				</ul>
	
		</div>
	</div><!-- 2column div -->
</div><!-- main div -->
		
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>