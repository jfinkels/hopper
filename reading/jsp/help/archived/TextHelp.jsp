<html>
  <head>
    <title>Perseus Help Archives</title>
    <link href="/css/hopper.css" type="text/css"
    rel="stylesheet"/>
  </head>
  <body>
    <%@ include file="/includes/index/header.jsp" %>

    <div id="main">
      <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="archives"/>
      </jsp:include>

    <div id="content" class="2column">
    <div id="index_main_col">
				<h3>Text and Word Study Tools Help (deprecated)</h3>
				<p><b>last revised: 09/29/03<br>
					</b>see the <a href="/help/texts.html">updated version of this document</a></p>
				<ul>
			<li><a href="#texts">Reading Texts</a>
			<ul>
				<li><a href="#about">About the texts</a>
				<li><a href="#lookup">Looking up passages, works, authors</a>
				<li><a href="#navigate">Getting around in the texts</a>
				<li><a href="#footer">Credits, sources, text quality</a>
				<li><a href="#links">Features of the texts</a>
				<li><a href="#display">Configuring your display</a>
			</ul>
			<li><a href="#start">Using Tools as your Starting Point</a>
			<ul>
				<li><a href="#dict">Dictionary Entry Lookup
</a>				<li><a href="#engind">English Index</a>
				<li><a href="#egws">English to Greek Word Search Tool</a>
				<li><a href="#elws">English to Latin Word Search Tool</a>
				<li><a href="#grmorph">Greek Morphological Analysis</a>
				<li><a href="#greekvocab">Greek Vocabulary Tool</a>
				<li><a href="#gwcst">Greek Words in Context Search Tool</a>
				<li><a href="#latmorph">Latin Morphological Analysis</a>
				<li><a href="#latinvocab">Latin Vocabulary Tool</a>
				<li><a href="#lwcst">Latin Words in Context Search Tool</a>
				<li>Tools integrated within the lexica:
				<ul>
					<li><a href="#stg">Synonym Tool</a>
					<li><a href="#gwct">Greek Word Collocation Tool</a>
				</ul>
				<li><a href="#wft">Word Frequency Tool (Greek or Latin)</a>
				<ul>
					<li><a href="#max">Why are there Maximums and Minimums?</a>
					<li><a href="#weight">What is a Weighted Frequency?</a>
					<li><a href="#rel">Why use relative frequencies?</a>
				</ul>
				<li><a href="VocabHelp.html">Vocabulary Tool</a>
			</ul>
			<li><a href="#chart">Comparative Chart of Tools</a>
			<li><a href="#know">Known Issues with Texts and Text Tools</a>
		</ul>
				<hr>
		For help with Greek font display, please see the <a href="fonthelp.html">Greek Font Help page.</a>
		<hr>
		<h2><a name="texts">Reading Texts</a></h2>
		<h3><a name="about">About</a> the texts</h3>
					Our texts are special Perseus-edited editions. Some were originally published in the Loeb Classical Library; others are available through the courtesy of other publishers. The Perseus Text Browser allows you to read these works broken into logical, quick-loading sections. Links at the top of each section allow you to go on to the next or previous section, or to jump to any other passage in the text. You may choose whether to read texts in English translation or in the original language, and you may choose to see more or fewer interconnections among the texts.
					<h3><a name="lookup">Looking</a> up passages, works, authors</h3>
					<p>You can view works by choosing them from the library or collection table of contents, by searching for them with the Lookup Tool, or, while already looking at a text, by typing the numerical reference into the &quot;Go to&quot; box and pressing return. If you already know the abbreviation for an author and work (i.e., Hom. Il. 1.1), you may also type the correct reference for any author, work, and book, chapter, section and/or line number (if applicable) into the Lookup Tool Search box at the top of each page and then pressing return. Perseus will display an example lookup for the work you are currently viewing, with correct abbreviations and format.</p>
		<p>Note the ways we found the sample text:</p>
		<p><img src="/img/help/homsearch1.gif" alt="" height="45" width="96" border="0"><img src="/img/help/homsearch2.gif" alt="" height="46" width="96" border="0"></p>
		<p>Those comfortable with URLs might notice that the actual URL for a passage is &quot;http://www.perseus.tufts.edu/cgi-bin/ptext?lookup=&quot; followed by the reference for the passage, typed as above except with &quot;+&quot; signs replacing spaces (which URLs aren't allowed to contain.) However, the Search Perseus/Lookup feature we've provided means you don't have to worry about that; it does it for you.</p>
					<p><a name="urls"><font size=-1><b>Document IDs:</b></font></a> URLs do not always contain the abbreviated author and title. Instead, they may contain the internal document identifier, a quick way for the system to locate the text you want to read. If you are making a link to a Perseus text from a page of your own, you should <strong>always</strong> use the more readable form. For example, for the opening of Homer's <i>Iliad</i>, use &quot;http://www.perseus.tufts.edu/cgi-bin/ptext?lookup=Hom.+Il.+1.1&quot; instead of &quot;http://www.perseus.tufts.edu/cgi-bin/ptext?doc=Perseus%3Atext%3A1999.01.0133&loc=1%2C1&quot;. The internal document ID encodes information about a particular version of the text. If you make links using the document ID, you override or disable readers' choice of versions. If you make links using the standard author and work abbreviations, readers will see the text in the language they prefer. When Perseus contains more than one version of a text, the preferred URL form, using the standard abbreviations, will appear at the bottom of the page. Abbreviations are also visible on the Perseus Table of Contents page, right after the language of the text.</p>
					<p></p>
					<h3><a name="navigate">Getting around in the texts</a></h3>
		<p>Once you're reading a text, you will find a variety of tools on your screen. Here's an overview of a sample text page:</p>
		<center>
			<img src="/img/help/hopper2.gif" border=1></center>
		<p><b>Version Selection:</b><br>
						At the top of the screen, below the name of the text, there will be, when applicable, links to alternate versions of the text. You can also use the Display Configuration Tool to make either original languages or translations the default for every text you look at. (For help with Greek font display, please see the Greek Font Help page.) Perseus contains alternate versions for some of the texts in the Renaissance collection as well.</p>
		<center>
			</center>
		<p><b>Navigation:</b><br>
			Next come the location tools. The blue <b>navigation bar</b> shows where you currently are relative to the whole text. When you are reading the very first page of a text, the red marker will appear all the way at the left; on the last page, it will appear all the way at the right. Click on the blue bar to jump to another approximate position.<br>
			The <b>Go To Box</b> allows you to jump to an exact position. Note that the Go To Box <i>only</i> works for the text you are reading; use the &quot;Search Perseus&quot; box in the top bar at left to go to a different text. In the Go To Box you can enter a reference, by book, chapter, section, and/or line. The Box initially contains the reference for the part of the text you are reading. You can separate the parts of the reference with commas, periods, or spaces.<br>
			The <b>Table of Contents link</b> brings you to a full table of contents for the work you are reading. For most texts, a short table of contents appears at the bottom of the sidebar at left. Entries in both the long and the short tables of contents are links back to the appropriate part of the text.<br>
			Below the Go-To Box and the Table of Contents link are <b>arrows to the left and the right</b>. These allow you to page through the text; the left arrow leads backward to the previous page and the right arrow forward to the next page. Naturally, the first page of a text has no previous page, so no left arrow, and the last page has no next page and no right arrow.</p>
		<p></p>
		<center>
			<img src="/img/help/navig.gif" border=1></center>
		<p></p>
		<p><a name="footer"><b>Credits, sources, text quality:</b></a><br>
			At the bottom of each page, below the text and annotations, you will see information about the text and how we created it. The &quot;Preferred URL for linking to this page&quot; appears when there are multiple translations or editions of the same work in the Perseus Digital Library. If you wish to make a link in a web page of your own to the page you are reading, we recommend you use this URL to make a link that will respect readers' preferences; see <a href=#urls>above.</a> The agency, corporation, or private donor who supported adding this text to the collection appears next, for most texts. Perseus is grateful to the many groups and individuals who have supported us.</p>
		<p>After the funder information comes source information. Most of the books in the Perseus Digital Library are electronic editions taken from printed books. This section of the page describes the printed book, usually including title, author, and publication date, and ISBN when available. If there is an ISBN, there will also be a link to Amazon.com, allowing you to purchase a copy of the book (assuming it is still in print). Finally, for some texts, we give information about the electronic edition itself: whether it was created by scanning the printed text and using optical character recognition (OCR), or by a professional data entry firm. Texts entered by the data entry firm have many fewer transcription errors, but data entry costs more money, so we cannot use it for all of our texts. Whether the original electronic version comes from OCR or from data entry, it must be proofread and converted to structured markup in SGML before it can be used in the digital library. Further editing may improve or refine the text. We qualify the completeness and accuracy of the proofreading as &quot;low,&quot; &quot;medium,&quot; or &quot;high degree of accuracy.&quot; A text at a medium degree of accuracy will have no more than 2 incorrect characters per thousand (roughly, per page), and probably has fewer. A text at a low degree of accuracy may have slightly more; a text at a high degree of accuracy will have fewer. <!--For more about document quality indicators, see <a href="/Articles/corpused2.html">this short paper.</a>--></p>
		<p><a name="atlas"><b>Other Features: Atlas Plotting:</b></a><br>
						For most texts in English, you will also see links to the Perseus Atlas. &quot;Plot sites on this page&quot; will appear when there are place names on the page you are reading. &quot;Plot sites in this book&quot; appears for texts that are divided into books, like Homer's <i>Iliad</i> or Thucydides' <i>History</i>. &quot;Plot sites in this document&quot; appears for any text that contains any place names at all. Each of these links brings you to the Atlas, with the selected places marked on the map. The Atlas will open in a new window, so you can arrange the text and the map together on your screen. Only places for which Perseus has latitude and longitude information will appear, however; coverage is currently better for ancient Greek sites than for other parts of the Mediterranean world. For some documents, like Caesar's <i>Gallic War</i>, many key places are not yet available.</p>
		<p><a name="timeline"><b>Timeline Plotting:</b></a><br>
						Texts in English may also have links to the Timeline Tool. &quot;Plot dates in this document&quot; appears whenever the text contains dates. The link brings you to a timeline showing the chronological scope of the text. The timeline opens in a new browser window, just as the Atlas does. This tool is most obviously useful for historical texts, but often provides interesting insights into other kinds of texts as well. See the Timeline Tool Help for more information.</p>
		<h3><a name=links>Features of the texts</a></h3>
		<p>The Perseus text tools are designed to help you analyze and discover the meaning of Greek and Latin words and the key ideas of Greek and Roman history and culture for yourself. As a result, within a text, many of the words appear as hyperlinks.</p>
		<p>For texts in English, including both translations of ancient texts and modern secondary sources, key words have links to the Perseus Lookup Tool. The Lookup Tool tells you what kinds of information are available about the key word: images, sites, art objects, and texts.</p>
		<p>For texts in Latin or Greek, most -- if not all -- of the words in the text are links to the <a name="wordstudy"></a><b>Word Study Tool</b>, giving an enhanced morphological analysis of each word. You may use the Display Configuration Tool to turn this feature on or off. The analysis shows the gender, number, and case for nouns and adjectives, or the tense, mood, and voice for verbs. This analysis also provides:</p>
		<ul>
			<li>a short definition of the word, automatically generated from the appropriate lexicon (Liddell-Scott-Jones (LSJ) for Greek, Lewis and Short (L &amp; S) for Latin).
			<li>links to Greek or Latin lexica. (For a Greek word, you may have a choice of links to either the comprehensive LSJ or the Intermediate Liddell-Scott Greek Lexicon, depending on how common the word is. For a Latin word, there is a link to the Lewis and Short Latin Dictionary.)
			<li>links to the Overview of Greek or Latin Syntax, to explain the use of the case, tense, mood, or voice.
			<li>a link to the Word Frequency Tool (Frequency in Other Authors).<li>a link to the Greek/Latin word search which will list the occurrences and citations of the given word in the current author.
			<li>alternate links (in the word count table itself) which will allow you to compare the word search and frequencies to the rest of the Perseus corpus.
		</ul>
		See below for more on <a href="#max">word counts</a> and <a href="#rel">relative frequencies.</a>
		<p>Here's a schematic of what this tool looks like, for <i>eth&ecirc;ke</i>, the last word in the second line of the <i>Iliad</i>:</p>
		<center>
			<img src="/img/help/morph.gif" border=1></center>
		<p><b>Commentaries and Cross References:</b><br>
						Asterisks (*) and crosses (+) in a Greek or Latin text are links to commentaries on this text, or references to this text from elsewhere in the Perseus Digital Library. You may use the Display Configuration Tool to control what kinds of reference links are displayed and how they appear on the screen. When Perseus has a commentary specifically about a particular work, asterisks in the text will indicate words that are discussed in the commentary. Each asterisk is a link; you will need to position your mouse pointer precisely. When you follow this link, the commentary will open up in a new window. The commentary is itself a text, and all the same navigation tools and morphological analysis tools are available for commentaries just as for primary source texts.<br>
						
			Here's a sample text:</p>
		<center>
			<img src="/img/help/comm.gif" border=1></center>
		<p>When the text you are reading is quoted by another text in Perseus, a cross in the text will indicate the words quoted in the other text. The cross is a link to the place of the quotation, and the other text will open up in a new window, just as commentaries do. Many of these quotations come from grammars, or commentaries on other texts. You can choose whether or not to see these links.</p>
		<p>Sometimes a commentary will have notes that do not refer directly to a single word or phrase in the text. Links to these notes will appear at the bottom of your screen, after any footnotes that belong to the text itself.</p>
		<h3><a name=display>Configuring your display</a></h3>
		<p>The Display Configuration Tool allows you to customize Perseus for yourself. The settings you choose will be stored in a cookie on your local computer: whatever choices you make will persist from one Perseus session to the next. The Display Configuration Tool is available from the &quot;Configure display&quot; link in the top bar of every Perseus page. It looks like this:</p>
		<center>
			<img src="/img/help/dispconf.gif"></center>
		<p><b>Default Text:</b> This allows you to choose whether translations or original Greek and Latin texts are displayed by default when you look up a work. Select &quot;translation&quot; to see the English version, &quot;original language&quot; to see the Greek or Latin.</p>
		<p><b>Word Study Links:</b> With this you may choose whether Greek and Latin words appearing in Perseus texts are links to the Word Study Tool. If you select &quot;yes,&quot; Greek and Latin words will have these links; if you select &quot;no,&quot; they will not. The Word Study Tool is described <a href="#links">above</a>. Note that this tool is available not only for complete texts in Greek or Latin but also for individual Greek or Latin words that are quoted in other texts. For example, if you are reading an English commentary on Vergil's <i>Aeneid</i>, Latin words that appear in the commentary will have links to the Word Study Tool just like the words in Vergil's own text.</p>
		<p><b>Greek Display:</b> This option controls how Greek is displayed. See the <a href="fonthelp.html">Greek Font Help</a> page for more information. &quot;Latin transliteration&quot; is relatively readable and will work on any system.</p>
		<p><b>Arabic Display:</b> This option controls how Arabic is displayed.</p>
		<p><b>Cross references:</b> This section controls which cross-reference links should be displayed. You may select as many of these choices as you like. &quot;Commentary&quot; links are references from a commentary on the work you are reading, if Perseus contains one. &quot;Cross reference&quot; links come from other works that quote the work you are reading; these may be reference grammars, commentaries, or other texts. You may request ordinary cross references, cross references from the footnotes of the referring work, or cross references from the index of the referring work, or any combination of these. Commentary and cross-reference links are most useful with original-language texts, but they are also available for use with English translations.</p>
		<p><b>Lookup Tool Links:</b> This option allows you to turn off Perseus Lookup Tool linking, or limit it to the collection you are currently using.</p>
		<p><b>Meter display:</b> This permits you to choose how you would like to display meter. For more see the Meter Font Help.</p>
		<p><b>Sanskrit display:</b> Like Greek Display and Arabic display: allows you to choose how you wish Sanskrit to be represented.</p>
		<p><b>Lemma mapping:</b> Finally, you may choose how the commentary and cross-reference links will appear. &quot;Don't mark lemmas&quot; means that there will be no marks in the main text. All commentary and cross-reference links will appear together at the bottom of the screen. &quot;Place mark at end of lemma&quot; means that the asterisk (*) for commentary links, or the cross (+) for cross-reference links, will appear in the text, next to the word or phrase that the comment refers to (the &quot;lemma&quot;). &quot;Italicize whole lemma and place mark&quot; means that the asterisk or cross will appear in the text, and the lemma being referred to will be italicized, so you can see whether a reference refers to a single word or to a phrase.</p>
		<p>When you've made your selections, choose &quot;set configuration&quot; to store them, or &quot;reset&quot; to return to the settings that were in effect when you entered the Display Configuration Tool. You will automatically be returned to the page you were reading, with your new settings applied. <i>Note</i>, however, that you will go back to the same version of the text -- original or translation -- even if you have changed the &quot;Default Text&quot; option. The &quot;Default Text&quot; option only applies when you look up a text by its author and title. If your browser cannot return you to the page you were reading, you will see a screen saying &quot;Your Perseus display configuration has been changed. You may now return to Perseus.&quot; The words &quot;return to Perseus&quot; are a link back to the page you were reading.</p>
		<p><b>Default settings:</b><br>
			Until you make your own selections, Perseus applies the following defaults:</p>
		<ul>
			<li>Default text: English translation
			<li>Word Study links: Yes
			<li>Greek display: Latin transliteration
			<li>Cross reference: Commentaries and ordinary cross references
			<li>Lemma mapping: Italicize whole lemma and place mark
		</ul>
		<p><font size=-1><b>Cookies and security:</b> Only the Perseus site can read your Perseus configuration cookie. It is stored on your machine, under your control. Perseus does not store any information about you.</font></p>
		<p></p>
		<h2><b><a name="start">Using tools as your starting point:</a></b></h2>
					On the Text Tools &amp; Lexica page, tools are listed as follows:
					<ul>
			<li><a name="dict"><b>Dictionary Entry Lookup:</b></a> The dictionary entry lookup tool searches all lexica in the library for four languages. First, select the portion of the word for which you would like to search from the first pop-up menu to the right of the Find box. The default is the beginning of the word ('begin'), such as words which start with abc. You may also choose to search for characters at the end of the word (ing), the exact word itself (amor),or a substring, which is a set of characters which may appear anywhere in the word. Next, choose the language which you would like to search from the pop-up menu to the right of the begin/end/exact/substring menu. Keep in mind, you'll only be searching <i>lexical dictionaries</i> within the Perseus library, and these may be narrow in scope, related to specific works, authors, or eras of literature. The default here is Greek, alphabetically the first language for which we feature lexica. (For tips on how to change your Greek display, see the <a href="#display">Configure Display help above</a>. If this is your first time at Perseus, you will be at the Latin transliteration by default.) Finally, you may choose to Sort Results Alpabetically (default) or Show More Common Words First. For information on word frequency calculations in Perseus, <a href="#wft">see below.</a>
			<p></p>
			<li><b><a name="engind">English Index:</a></b> The English Index, a subsidiary of the Lookup Tool, is the tool to use if you are searching for an English word, set of words, or partial words in the all of the primary and secondary texts of the Perseus digital library. For instance, if you want to see where &quot;Scylla&quot; is mentioned or what passages are translated as &quot;wine-dark sea,&quot; try the English Index first.<br>
							<i>Be aware that</i> the best tool to start with if you are new to Perseus is the Perseus Lookup Tool, located on every Perseus page, since it will tell you what types of information Perseus contains about your topic or question. The Perseus Lookup Tool searches across different types of information (images, plans, maps, and texts, etc.), but the English Index <i>only</i> searches texts.
			<p></p>
			<li><b><a name="egws">English to Greek Word Search Tool:</a></b> This tool searches the English words which appear in the Liddell-Scott-Jones Greek-English Lexicon (&quot;LSJ&quot;) entries. It is <i>not</i> an English to Greek translation tool, nor does Perseus contain an English to Greek dictionary. For an example of how this tool works, search for &quot;city.&quot; Amongst the results, you will see not only <i>polis</i> which means &quot;city,&quot; but also words like <i>hal&ocirc;simos</i> which appears in this list because it is defined as &quot;song of triumph on taking city.&quot;<br>
				There are several pop-up lists which assist with customizing your searches. For instance, with the Position of text pop-up list, select from the following options: <i>at the start...</i> (selecting this option and entering &quot;lov&quot; will return &quot;love,&quot; &quot;loving,&quot; &quot;lover,&quot; etc.); <i>at the end...</i> (selecting this option and entering &quot;iest&quot; will return &quot;priest,&quot; &quot;earliest,&quot; etc.); <i>exactly</i> (selecting this option will return searches for words identical to the one you have entered); and <i>anywhere...</i> (selecting this option and entering &quot;eep&quot; will return &quot;sleepless,&quot; &quot;deep,&quot; &quot;keeping,&quot; etc. Further, you may sort your results alphabetically or by frequency in the Perseus library.
			<p></p>
			<li><b><a name="elws">English to Latin Word Search Tool:</a></b> This tool works like the English to Greek Word Search Tool, described above. You may perform the same types of searches for English words which appear in the lexicon entries from the Lewis and Short Latin Dictionary (L&amp;S, or LS). See the above description for details on how this works.			<p></p>
			<li><b><a name="grmorph">Greek Morphological Analysis:</a></b> This tool analyses Greek words and tells you their possible morphological analyses and the dictionary entries from which they could be derived, providing a word study and links to other information on the chosen term. More on what this tool looks like<a href="#wordstudy"> is above.</a><br>
			<p></p>
			<li><b><a name="greekvocab">Greek Vocabulary Tool:</a></b> See <a href="VocabHelp.html">Vocabulary Tool help</a>.
			<p></p>
			<li><b><a name="gwst"><a name="gwcst">Greek Words in Context Search Tool:</a></a></b> This tool, a subsidiary of the Lookup Tool, will find one or more <i>complete</i> Greek words in the Greek texts in Perseus. For more on searching Greek and Latin texts for specific words or phrases, see the help for the Lookup Tool. <!--<p>	<li>This tool allows the user to find one or more Greek words in the Perseus digital library. You can perform two types of search with the Greek Word Search Tool: Lemmatized Search and Exact Match Search. In a lemmatized search, you may enter any number of inflected forms or dictionary entries. In an exact match search, the search program finds sentences that contain the exact form that you type in. If you enter pe/mpousi and a)/ggelon as Greek search terms, the program will find all sentences in Perseus that contain those words. All forms must be in beta code with accentuation, however. Further, you may require the appearance of a word by adding a plus sign (+) before the word or words; you may exclude a word with the minus sign (-). For example, see the search of <a href="/cgi-bin/phraseindex?phrase=kalo/s+a)gaqo/s&auth=plat.&display="><i>kalos</i> near <i>agathos</i> in Plato.</a><br>
				
				There are several pop-up lists which assist with customizing your searches. You may, for instance, limit your search to a particular author or type of work. Perseus categorizes Greek texts as follows: Drama, History, Poetry, Prose, Rhetoric, or Tragedy. The default searches the entire library with the exception of DDBDP. You may also limit your results to 10, 15, 25, or 50 results per page, and you may display these results in any of the Perseus Greek font display options. (As mentioned above, you must always enter your search in beta code with accentuation.)<br>
				
				You should note that for best results your query should be no longer than one sentence. Complex queries during periods of peak use can take a minute or more. Very common words are excluded from queries: see the tool page for the list. More searching tips and examples are provided on the tool page, as well.  -->
							<p></p>
			<li><b><a name="latmorph">Latin Morphological Analysis:</a></b> This tool analyses Latin words and tells you their possible morphological analyses and the dictionary entries from which they could be derived, providing a word study and links to other information on the chosen term. More on what this tool looks like <a href="#wordstudy">is above.</a><br>
			<p></p>
			<li><b><a name="latinvocab">Latin Vocabulary Tool:</a></b> See <a href="VocabHelp.html">Vocabulary Tool help</a>.
			<p></p>
			<li><b><a name="lwcst">Latin Words in Context Search Tool: </a></b>This tool, a subsidiary of the Lookup Tool, will find one or more <i>complete</i> Latin words in the Latin texts in Perseus. For more on searching Greek and Latin texts for specific words or phrases, see the help for the Lookup Tool.
							<p></p>
						
						
						
			<li><a name="lookup"><b>Lookup Tool:</b></a> see help here.
							<p></p>
						
						
			<li><a name="toc"><b>Perseus Table of Contents:</b></a> see help here.
							<p> <!-- <b><a name="moran">Word Study Tool (Greek or Latin):</a></b> This tool is linked from all Greek (and Latin) texts when you select the Word Study Links&quot; option in the Display Configuration Tool. All words which are analyzed by this Perseus tool are then made active links. For more information on how to use this tool from within a text and the options it offers, see the section on <a href="#morph">morphological analysis above</a>.
			 -->
			<!--<b><a name="ils">Intermediate Liddell-Scott Greek Lexicon:</a></b> This is the interface for searching the Intermediate Greek Lexicon. It works in the same way as the English to Greek Word Search above. There are several pop-up lists which assist with customizing your searches. You may search for strings of letters <i>at the start of dictionary entries, at the end of dictionary entries, exactly, and anywhere in dictionary entries.</i> For instance, if you search for &quot;lia&quot; <i>at the start...</i>, you'll get results like <i>liax</i> and <i>lian.</i> If you search for &quot;lia&quot; <i>at the end...</i>, you'll get results like <i>aphilia</i>, and <i>Thessalia</i>. If you search <i>anywhere</i>, you'll find <i>agalliasis</i> and <i>aiolias</i>, amongst others. (An <i>exact</i> search for this string has no results, since it is not a dictionary entry.) Further, you may sort your results alphabetically or by frequency in the Perseus library. And finally, you may limit your search to specific authors. You may search Perseus 2.0 texts, all literary Greek texts (does not include the DDBDP), Perseus Roman texts, or an individual Greek author. (Please note: There are a handful of works which appear on-line in the Perseus library which were not included on the Perseus 2.0 CD-Rom releases. This distinction is included in this tool for those familiar with Perseus 2.0 on CD. A chart of differences is <a href="/order.html">here</a>.) The default searches 2.0 texts.
			<p></p>
			<li><b><a name="lsj">Liddell-Scott-Jones Greek Lexicon:</a></b> The interface to the LSJ9 is identical to that of the Intermediate LS. Please <a href="#ils">read the above</a> for an explanation of how this tool works.<br>
			<ul> -->
						
						<li><b>Tools integrated within the lexica:
</b>			<ul><li><b><a name="stg">Synonym Tool: </a></b>The synonym tool is fully integrated with the Lewis and Short Latin Lexicon and the Liddell, Scott, and Jones Greek Lexicon in Perseus. To see the possible synonyms for a word, simply look up that word in the Greek or Latin lexicon. A table will appear at the head of each dictionary entry suggesting possible synonyms for the word. You can view the dictionary entries for the suggested synonyms by clicking on the word. A list of more suggested synonyms is available by following the links below the table. The synonym lists are generated by computational analysis of dictionary definitions and not every suggested synonym will be appropriate.<br>
										See, for example, the definitions for the Latin <i>pudor</i> or the Greek <i>muthos</i>.<br>
								
								
								
			<li><b><a name="gwct">Word Collocation Tool: </a></b>This tool allows users to see the words that are likely to appear within five words of each other in Perseus Greek or Latin texts. It is fully integrated into Lewis and Short Latin Lexicon and the Liddell, Scott, and Jones Greek Lexicon. While looking at a lexicon entry, you will see a table with the header &quot;Words That Regularly Appear With...&quot; This shows the most common collocates for that word. Clicking on the corpus name (e.g., &quot;Greek/Latin Prose/Poetry/Texts&quot;) takes you to the complete collocation table which features a complete list of collocations, <a href="#mutual">mutual information scores</a>, and links to the word search tool. Clicking on any word in either the short table (in the LSJ entry) or the complete table will take you to the lexicon entry for that word.<br>
				<!--If you select an author in the Greek dictionary lookup tool, or if you link into the electronic lexicon while reading a Greek text in Perseus, an expanded version of the collocation table will appear in the dictionary entry, showing the most common collocations for types of literature within Perseus. See, for example, the collocation data for the word <a href="/cgi-bin/lexindex?lookup=kalo/s&lang=Greek&author=plat."><i>kalos</i> in Plato</a>. This includes not only the general collocation, but a line for collocations within works of prose. This is not available for Latin texts at this time as the Perseus Latin corpus is not large enough to produce statistically significant results.<br>-->
				<b>A note on collocation:</b> This sort of collocation information can yield interesting information about language usage. For example, in English, collocation data shows that the mutual information score for the words &quot;strong&quot; and &quot;tea&quot; is much higher than the score for &quot;powerful&quot; and &quot;tea.&quot; This suggests that it is much more common to speak of &quot;strong tea&quot; than &quot;powerful tea.&quot; Collocation data can also provide a quick overview of the sense in which an author uses a word. For example, if the most common collocates of the word &quot;bank&quot; in a collection of texts were words such as &quot;water,&quot; &quot;shade,&quot; or &quot;cool,&quot; we would know that the author probably was writing about rivers rather than financial institutions.<br>
					Collocation information yields similar information about Greek texts as well. Just as in English, commonly used word pairs have a high mutual information score. For example, the mutual information score for the Greek words <i>agathos</i> and <i>kalos</i> is quite high. It is also possible to use collocation data to determine the semantic range of a word in a Greek text. For example, the most common collocates of the word <i>thuo</i> are, as one might expect, the implements, objects, and personnel associated with sacrifice.<br>
					<b><a name="mutual">What are mutual information scores?</a></b> These lists of commonly co-occurring words are created by calculating a mutual information score for every Greek word pair in the Perseus corpus. A mutual information score is used in place of raw pair frequencies to account for the fact that the most frequent word pairs in any collection of texts will be combinations involving the definite article and other function words. As the mutual information score increases, the words have a higher likelihood of appearing together. The maximum mutual information score varies based on the size of the corpus being considered. Thus, a mutual information score of 80 suggests a strong association in Greek prose, but only a medium association in the whole Perseus corpus. While we are currently investigating ways to more precisely gauge the significance of the results in different corpora, for now the scores should be used as a guide for locating potentially interesting word pairs. 
		</ul>
		<!--<li><b><a name="lsld">Lewis and Short Latin Dictionary:</a></b> The interface for the Lewis and Short Latin Dictionary is based on that for the Greek lexica. You have several searching options: you may find strings of letters <i>at the start of dictionary entries, at the end of dictionary entries, exactly, and anywhere in dictionary entries.</i> For instance, if you search for &quot;for&quot; <i>at the start...</i>, you'll get results such as <i>foras</i>, and <i>fortuna</i>. If you search for &quot;for&quot; <i>at the end...</i>, you'll get results such as <i>affor</i>, and <i>praefor</i>. The same search for &quot;for&quot; occuring <i>exactly</i>, returns the entry for the verb, <i>for, fatus.</i> And a search for &quot;for&quot; anywhere, will return results such as <i>afformido</i>, and <i>reformatio</i>. You may sort your results alphabetically or by frequency in the Perseus library. And finally, you may limit your search to specific authors. The default searches the catalog of Perseus Roman texts. Please note that you won't see any results by choosing a Greek author unless he quotes Latin.<br>
		<ul>
			<li><b><a name="stl">Synonym Tool: </a></b>This tool is <a href="#stg">explained above</a>.
		</ul>-->
		<li><b><a name="wft">Word Frequency Tool (Greek or Latin):</a></b> If you are searching for occurrences of specific Greek or Latin words, you may use the Word Frequency Tool. There are several options for displaying results. <i>Sort Authors Alphabetically</i> is the default option. <i>Sort Authors by Type of Literature</i> will sort results according to types such as comedy, history, tragedy,etc. <i>Sort Authors by Date</i> will list authors starting from the earliest work to the latest based on the best evidence we have for each author. <i>Words in Author</i> will sort results from the author with the most words in Perseus, to the author with the fewest. <i>Maximum Instances</i> will sort results from the most possible instances in a given author; <i>Minimum Instances</i> reverses this list and starts with the fewest. <i>Maximum Frequency/10K</i> will sort the results from the highest incidence of relative frequency to the lowest; <i>Minimum Frequency/10K</i> reverses this list and begins with the lowest relative frequency.<br>
			<b><a name="max">Why are there Maximum and Minimum Frequencies?</a></b> Although Perseus can disambiguate a vast majority of Greek and Latin words, there are some forms which may be derived from more than one lexicon entry. (E.g. &quot;flies&quot; may be an instance of the verb &quot;to fly&quot; <i>or </i>the noun &quot;fly&quot;, so Perseus would include it in the count for both words. On the other hand, there's no doubt that &quot;sneezed&quot; is a form of &quot;to sneeze&quot;) In cases where the maximum instances differ from the minimum, the maximum are all of the possible occurrences of a given lemma, and the minimum are all of the occurrences of the word which the computer has disambiguated. So, all ambiguous forms are included in a maximum count, and excluded from the minimum. This is also true of the relative frequency calculations.<br>
			<b><a name="weight">What is a Weighted Frequency?</a></b> A weighted frequency tells you whether the actual frequency count for a word (if this were possible) would be closer to the minimum or maximum frequency score. The weighted frequency is determined by assigning a weight to each inflected form based on the number of possible dictionary forms from which the inflected form could be derived. For example, an unambiguous word would have a weight of 1, a word that could be derived from two dictionary headwords would receive a weight of 1/2, a word that could be from 3 different headwords is given the weight of 1/3, etc. The weighted frequency is calculated as the sum of the weights for each inflected form that appears in a text. If the weighted score is equal to the average of the minimum and maximum score, you know that the word is entirely ambiguous in all of its forms. On the other hand, if the minimum, maximum, and weighted scores are all the same, you know the word is entirely <i>un</i>ambiguous in all of its forms. As the weight approaches the maximum score, it becomes more likely that the maximum count is closer to the actual count; the actual count would be greater than the weighted score and less than or equal to the maximum.<br>
			<b><a name="rel">Why use relative frequencies?</a></b> Relative frequencies are based on occurrences of a given word per 10,000 words. For instance, in the case of the Greek verb <i>pemp&ocirc;</i>, Plutarch uses this verb 146 times, which is unimpressive compared with Xenophon's maximum of 350 times. Yet, the corpus of Plutarch on-line in Perseus is about 107,000 words compared with Xenophon's 312,000. So, the relative frequency in Plutarch is 13.67 at its maximum, compared with Xenophon's maximum of 11.21. When making comparisons between authors, it is most useful to know the relative frequency for a given word rather than the word count itself, since the size of the corpora vary.
		<h2><a name="Anchor-Comparative-49575"></a><b><a name="chart">Comparative Chart of Tools</a></b></h2>
		<h2><img src="/img/help/comptoolchart.gif" alt="" height="238" width="776" border="0"></h2>
			<h2><b><a name="know">Known Issues with Text Tools:</a></b></h2>
			<b>Indexing Issues in all Lexica: </b>This issue appears in several forms, most often you will find that you are not receiving anticipated results after performing a dictionary entry search. 1) A legitimate dictionary entry appears in the results list, but does not include a link to any lexicon. To work around this issue, choose a nearby entry, and page forwards or backwards (using the navigation arrows just under the blue bar) to locate the entry. 2) You receive results that read &quot;No such file XML///.xml.&quot; or something similar. This often occurs when linking from the word study tool for morphological analysis to the dictionary. Try going to the dictionary entry lookup tool and proceeding from there as above. Again, the entry is probably there but the tool is not finding it. 3) You receive results with only a proper noun where a common noun also exists. For this, also, try entering the lexicon via a nearby entry and paging backwards or forwards. We appreciate any reports of a specific word or words related to this issue and any variations of this issue you may encounter. If you are working with many texts or words, please group these reports in as few e-mails as possible. If you are aware of the issue, please mention that no reply is necessary; otherwise, we will probably direct you back to this document for assistance. All reports will be logged for future reference.
			<p><b>Words followed by numeration in the lexica:</b> Often you will see morphological or lexicon results which suggest a lexicon entry followed by a numerical designation (e.g., <i>nepos#1</i>). When you click on this term, you get the message &quot;No entry in the dictionary matches your request.&quot; This is a problem with the numerical designation; the word is present in the lexicon. To see it, change the URL. In this example for <i>nepos,</i> the URL is:<br>
				<font size=-1>http://www.perseus.tufts.edu/cgi-bin/lexindex?lookup=nepos%&author=*Roman&lang=Latin&corpus=Roman</font><br>
				
		Remove the &quot;%&quot; so that the URL reads:<br>
				<font size=-1>http://www.perseus.tufts.edu/cgi-bin/lexindex?lookup=nepos&author=*Roman&lang=Latin&corpus=Roman</font><br>
				
		Hit return, and you'll be taken to the lexicon entry.
</p>
			<p><b>Compound verb forms:</b> Sometimes the morphological analysis tool will suggest a lexicon entry with a hyphen in it, for instance, the morphological analysis of <i>increantur</i> suggests that the form is from <i>in-creo</i>. This is the case, but the Latin lexicon does not have such an entry, so you are told that &quot;No entry in the dictionary matches your request.&quot; You should try to look at the root verb form, in this case, the portion which follows the hyphen, <i>creo</i>. You may do so by following the steps to looking up a lexicon form above, or by altering the URL.<br>
			The given URL for <i>in-creo</i> is:<br>
			<font size=-1>http://www.perseus.tufts.edu/cgi-bin/lexindex?lookup=in-creo&db=ls&lang=Latin&corpus=Roman&author=&formentry=0</font><br>
			You may change this to read <i>creo,</i> like so:<br>
			<font size=-1>http://www.perseus.tufts.edu/cgi-bin/lexindex?lookup=creo&db=ls&lang=Latin&corpus=Roman&author=&formentry=0</font><br>
			Hit return and you'll be taken to the lexicon entry.<br>
		</p>
			<hr>
			<i>revised 29 Sept, 2003 LMC</i><br>
		<font size=-1>Screen images in this file created July 2003.</font></ul>
				</div> <!-- index_main_col -->
	</div> <!-- content 2column -->
</div> <!-- main -->

    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
