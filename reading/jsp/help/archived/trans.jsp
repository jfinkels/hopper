<html>
  <head>
    <title>Perseus Translation Tips (deprecated)</title>
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
		<h3>Greek and Latin Translation Tips (deprecated)</h3>
		<b>last revised: 08/26/04</b><p>
		<font size="-1">This is a step-by-step guide for those unfamiliar with or new to classical Greek and Latin who want to use Perseus to translate a word or phrase from one of these languages into English. Well over half of the e-mail the Perseus webmaster receives concerns translation, so we hope this document proves helpful. As always, please send comments to the <a href="mailto:webmaster@perseus.tufts.edu">webmaster</a>.</font>
		<ul>
			<li><a href="#general">General advice for both languages </a>
			<ul>
				<li><a href="#inflected">What's an inflected language (and why does it matter)? </a>
				<li><a href="#dictionary">Why can't you just take me to the dictionary when I enter my word? </a>
				<li><a href="#one">One word at a time! </a>
				<li><a href="#proper">Proper nouns and capitalization </a>
				<li><a href="#order">Word order doesn't matter, or does it? </a>
				<li><a href="#classical">Classical, not modern</a>
				<li><a href="#abbrev">What about all of these abbreviations?</a>
				<li><a href="#learn">Where to learn more</a>
			</ul>
			<li><a href="#greek">Translating from Greek</a>
			<ul>
				<li><a href="#choose">Choose your tool </a>
				<li><a href="#translit">Transliteration </a>
				<li><a href="#compound">Compound words and terms </a>
				<li><a href="#samples">Sample translations and hints </a>
			</ul>
			<li><a href="#translat">Translating from Latin</a>
			<ul>
				<li><a href="#latsam">Sample translations and hints</a>
			</ul>
		</ul>
		<p><b><font size="+1">General advice for both languages</font><br>
			</b></p>
		<p><a name="inflected"></a><b>What's an inflected language (and why does it matter)?</b><br>
			Greek and Latin are inflected languages, which means that the construction of the words is changed to convey meaning. Whereas English relies mainly on word order for meaning, Greek and Latin rely on changes to the words themselves. For example, the sentence in English, &quot;Matt threw the ball to Lisa,&quot; has several easily identifiable grammatical parts. <i>Matt</i> is the subject; <i>threw</i> the verb; <i>the ball</i> is the direct object; and <i>Lisa</i> the indirect object. If we change the word order to say, &quot;Lisa threw the ball to Matt,&quot; we change the meaning of the sentence.<br>
			If we were expressing this in an inflected language, we would change the root words to reflect their grammatical role: each noun in the sentence adopts a case which tells us whether the word is the subject, the direct object, the indirect object, a possessive, or some other construction. And, of course, each verb changes according to its tense, mood, voice (active or passive), person (first, second, or third), and number (whether the subject is singular or plural); this happens to a certain extent in English, although it there tend to be fewer English verb forms than in either Greek or Latin.<br>
					<b>What does this mean for the novice translator?</b> Basically, it means that the form of the word you want to translate <i>may not appear in the dictionary</i>. It may not even look remotely like its dictionary entry root word. Just as you have to look under <i>throw</i>, not <i>threw</i>, you'll have to know the root form (the lemma) of your word or words if you want to define it. So, we offer tools which analyze the form of the word to help you determine its role in the sentence or phrase and to point you to the dictionary entry form of the word. As we emphasize throughout this page, we don't have translation tools <i>per se</i>. For more on what we have and why that is, please keep reading.<br>
		</p>
		<p><a name="dictionary"></a><b>Why can't you just take me to the dictionary when I enter my word?<br>
			</b>There are forms of words in both languages which have more than one lemma. Sometimes, a single inflected form could be from one of several roots. There are even some rare forms which could be either nouns or verbs. This is why you want to start with the morphological analysis tool to help you figure out the root for your word. If there are several options, the tool will present you with all of the choices. This means, in Perseus terms, that your word is ambiguous: it could derive from more than one root. Even when you access the morph analysis tool from within a text, you will not find disambiguated meanings. Our texts are not disambiguated and our tools are working independent of context.<br>
		</p>
		<p><a name="one"></a><b>One word at a time!<br>
			</b>Greek and Latin are often called word-based morphologies, meaning they rely on the word as the unit of meaning. What's important to know, is that Perseus word analysis tools work on one word at a time. You can't enter a phrase into these tools and receive a translation. There are many hundreds of sites and books, including English dictionaries, which reference common foreign language phrases. Of course, we do have tools to help you research groups of Greek or Latin words, study collocation and phrase use, and see what authors use what terms. We don't offer a translation tool, though. Due to the way these languages work, refining the word-based tools is a more effective approach for the project than trying to develop a translator like <a href="http://babelfish.altavista.com/">Babelfish</a>. At this writing, we are unaware of any such web- or software-based tool for classical Greek or Latin. (We'd love to know if there is any out there!)<br>
		</p>
		<p><a name="proper"></a><b>Proper nouns and capitalization</b><br>
			While the tools have excellent coverage for both languages (and we are improving them all the time), coverage for proper nouns is less extensive. For such terms, you may wish to consult a commentary. Also, <b>do not capitalize</b> any forms entered in the word study analysis tools, regardless of whether or not you are searching a proper noun.<br>
		</p>
		<p><a name="order"></a><b>Word order doesn't matter, or does it?</b><br>
			Many people say &quot;word order doesn't matter&quot; in Greek or Latin. This is true in the basic sense that you could scramble your words and still come out with an identical meaning, but there is a standard word order to both languages. Poetry alters word order to take advantage of many interesting devices and constructions In general, there are composition standards in both languages which provide a framework for the word order.<br>
		</p>
		<p><a name="classical"></a><b>Classical, not modern<br>
			</b>Many users write with phrases and words which are clearly post-classical. Our tools are built on the texts included in Perseus, and those texts are concentrated in the classical period. Both languages, however, have continued to evolve to the present day (yes, Latin, too! the Vatican has constructed many new words and phrases for modern life, not to mention the wealth of neo-Latin scientific terms). Thus, a term from your biology book, your doctor's diagnosis, or a Greek relative's favorite saying may not be found using our tools. Even Greek and Latin from the early-Christian period uses many terms which are not found in the classical authors. We encourage you to try analyzing your term, but keep in mind that if it is derived from a non-Perseus source, a later period, or a modern work, the chances of obtaining results are reduced.<br>
		</p>
		<p><a name="abbrev"></a><b>What about all of these abbreviations?<br>
			</b>If you are new to Greek and Latin or your grammar is rusty, you may find the analyses given by the tools a bit confusing. Basically, there will be five pieces of information on a verb form (tense, mood, voice, person and number), and three on a noun (gender, case, and number). (A participle, or verbal adjective, will have the format of a verb with slight variation, but an adjective will follow a noun format.)<br>
		</p>
		<p><a name="learn"></a><b>Where to learn more</b><br>
			For more on how the syntax of each language works, we recommend the <a href="/cgi-bin/ptext?doc=Perseus%3Atext%3A1999.04.0052&query=toc&layout=&loc=pres">Overview of Greek Syntax</a> and the <a href="/cgi-bin/ptext?doc=Perseus%3Atext%3A1999.04.0022&layout=&loc=noun_agree&query=toc">Overview of Latin Syntax</a>. A glance at the contents of each of these documents will give you a sense of the tenses, moods, voices, and cases you'll encounter. And if you are interested in more in depth information, consult our on-line texts:<a href="/cgi-bin/ptext?doc=Perseus%3Atext%3A1999.04.0001&layout=&loc=&query=toc"> A Greek Grammar for Colleges</a> or <a href="/cgi-bin/ptext?doc=Perseus%3Atext%3A1999.04.0001&layout=&loc=&query=toc">Allen and Greenough's New Latin Grammar for Schools and Colleges</a>. Or, for in depth study of Greek and/or Latin, <a href="http://www.textkit.com/">Textkit - Greek and Latin Learning Tools</a> is a fine site featuring public domain texts and instructional books in downloadable .pdf format and a helpful forum.</p>
		<p><a name="greek"></a><font size="+1"><b>Translating from Greek</b></font></p>
		<p><a name="choose"></a><b>Choose your tool</b><br>
						If you have a Greek word in front of you, start with the Greek Morphological Analysis Tool for word study. In many cases, accents are not mandatory in this tool, but they are recommended for optimal results. The most up-to-date version of this tool will be found using the previous link <b>or</b> under the Tools table of contents, linked from every page. Keep in mind, this is not a translation tool. For searching when you are uncertain of the spelling/transliteration or accentuation, try a partial word search with the Dictionary Entry Lookup. You won't find an inflected form, but you may be able to pinpoint the correct dictionary entry.<br>
					</p>
		<p><a name="translit"></a><b>Transliteration</b><br>
						
			Entering Greek script with an English keyboard presents a challenge when done on the WWW. There are a variety of font display issues with different browsers and platforms, and we offer <a href="fonthelp.html">many options for viewing our texts in Greek</a>. Even if you have fonts installed, however, <b>you won't be able to type</b> your query in a Greek font into the search box. Regardless of which method of display you choose, you'll always see the Roman alphabet in the search box. Even the Enter Text in Greek tool will convert the Greek script to Roman letters.<br>
			The default setting you will see upon using this tool for the first time, is Latin transliteration. There are various ways of conveying this, so a chart of how Perseus does this is featured on this page. (Our table is derived from the <i>Chicago Manual of Style</i>.) Some key things to know: you <b>must</b> distinguish between epsilon (e) and eta (e^ or &ecirc;); omicron (o) and omega (o^ or &ocirc;). Most searches may be performed without accents: if there is ambiguity in the results, the tool will ask for clarification. Always check your transliteration against the charts provided: <b>transliteration problems account for the vast majority of searching errors.<br>
			</b></p>
		<p><b><a name="compound"></a>Compound words and terms<br>
			</b>Much of the Greek terminology you'll encounter is formed by combining two or more words. Analgesic, for example, has it roots in <i>analg&ecirc;sia</i>.<img src="/img/help/analg.gif" alt="" height="15" width="75" align="absbottom" border="0"> This a negation, expressed by &quot;<i>a</i>&quot; (plus &quot;<i>n</i>&quot; because it precedes another vowel here) of the term for &quot;sense of pain,&quot; <i>alg&ecirc;sis </i>(from <i>algos </i>= pain). The word nostalgia, has the same root, and literally means &quot;pain for returning home.&quot; This is a combination of <i>nostos</i> <img src="/img/help/nostos.gif" alt="" height="14" width="50" align="absbottom" border="0"> and <i>algos</i>, <img src="/img/help/algos.gif" alt="" height="15" width="41" align="absbottom" border="0"> and if you were researching this word, you would want to translate each word separately, as there is no combined form in the classical lexicon.<br>
					</p>
		<p><a name="samples"></a><b>Sample translations and hints<br>
			</b>For the word <img src="/img/help/sophr.gif" alt="" height="13" width="89" align="absbottom" border="0">, our first example, begin by checking the transliteration guide. Second, enter this into the morphological analysis tool for Greek. Thus, we enter, so^phrosu/ne^s, into the analysis tool. (Note that if you enter this same word into the dictionary tool, you'll get no results.) The second possible analysis given in Perseus will lead you to the common Greek term typically rendered in English as <i>sophrosyne</i>.<br>
			Perhaps you don't know the correct way of expressing your search term: maybe you are working from something you heard or you cannot remember the letters of the Greek alphabet well enough to avoid the transliteration problems discussed above. A good way of using Perseus tools is to search for words beginning with certain letters (or ending with certain letters; or containing a specific combination of letters). For this type of search, the Greek Morphological Analysis Tool for word study won't be helpful: this tool requires a <i>complete and correct</i> term. But for exploration of the language, a partial word search with the Dictionary Entry Lookup can provide some interesting results. A search for dictionary entries beginning with phil- returns over 900 results, but a quick scan of this list will reveal some words familiar to even the Greek novice: <i>philadelphia</i>, <i>philanthr&ocirc;pos</i>, and <i>philosophia</i>, for instance.<br>
							In some instances, your search may return more than one entry, for example, a search for the word kudos. If you are viewing Perseus with default Latin transliteration, you will see two words which look identical but have very different meanings. This is because they are accented in different ways (something you will see if you change your display to beta code or another display format which indicates accentuation). Which one is the common term used in everyday English? Look at the frequency charts for a hint. There are more instances of one term than another: this is clue to check that definition first. Indeed the more common entry is the one which is familiar to English speakers, <i>kudos</i> (with a circumflex over the upsilon)<img src="/img/help/kudos.gif" alt="" height="14" width="38" align="absbottom" border="0">, meaning &quot;glory, renown.&quot;</p>
		<p><a name="translat"></a><font size="+1"><b>Translating from Latin</b><br>
			</font></p>
		<p>If you have a Latin word in front of you, start with the Latin Morphological Analysis Tool for word study. The most up-to-date version of this tool will be found using the previous link <b>or</b> under the Tools table of contents, linked from every page. Keep in mind, this is not a translation tool. For searching when you are uncertain of the spelling, try a partial word search with the Dictionary Entry Lookup. You won't find an inflected form, but you may be able to pinpoint the correct dictionary entry.</p>
		<p><a name="latsam"></a><b>Sample translations and hints</b><br>
							Begin with the example, <i>carpe diem</i>. Recall that the tools are single-word only, so entering the entire phrase won't return any results. Start with <i>carpe</i> and use the morphological analysis tool. You'll see that the root verb is <i>carpo</i>, meaning &quot;pick, pluck, gather, etc.&quot; Now, try <i>diem</i> in the analysis tool. Here, you see the word is <i>dies</i>, meaning &quot;day.&quot; Although this is most commonly translated as &quot;seize the day,&quot; you can see that <i>carpo</i> has many more nuanced meanings, and that, in fact, this verb is commonly used to convey plucking flowers or fruits, (...&quot;gather ye rosebuds...&quot;) and poetically represents snatching something away. The phrase is less a call to make wise use of your time, and more an exhortation to enjoy the moment at hand.<br>
							Another common request is for the motto, <i>e pluribus unum</i>, which means, &quot;out of many, one.&quot; Start with <i>e</i> in the analysis tool; continue to <i>pluribus</i> with the same tool. You'll notice that there is no short definition given for this form: try the link to the Elem. Lewis (short for Charlton T. Lewis, <i>An Elementary Latin Dictionary</i>). Finally, use the analysis tool for <i>unum</i>.<br>
						<br>
						
			You may find it difficult to determine what role in the phrase or sentence each word plays, for more on grammar and syntax we recommend the <a href="#learn">sources above</a>. As we note in the <a href="faq.html">FAQ</a>, we don't have the resources to perform custom translations. There are sites which will help: try a simple search on your phrase and see what you get.</p>
</div> <!-- index_main_col -->

</div> <!-- content -->
</div> <!-- main -->

    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
	




