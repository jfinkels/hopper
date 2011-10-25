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

      <div id="content" class="2column"><H3><A NAME="vocab">Perseus
      Vocabulary Tool Help (deprecated)</A></H3>
      <div id="index_main_col">
				<b>last revised 10/25/02</b>
				<P>The Perseus Vocabulary Tool is designed to allow users to explore the vocabulary of the Greek and Latin texts in the Perseus Digital Library.  Using the Vocabulary Tool you can select a set of documents or document sections and then view a list of all of the words that appear in those texts.</P>

<ul>
<li><a href="#settingup">Setting Up Your List</a></li>
<ul>
<li><a href="#worksel">Selecting Works</a></li>
<li><a href="#sortorder">Sort Order</a></li>
<li><a href="#listlen">List Length</a></li>
<li><a href="#oopts">Output Formats</a></li>
<li><a href="#colsel">Column Selection</a></li>
</ul>
<li><a href="#viewing">Viewing the Results</a></li>
<ul>
<li><a href="#size">Vocabulary Size and Density</a></li>
<li><a href="#list">The Vocabulary List</a></li>
<li><a href="#freqs">Maximum, Minimum, and Weighted Frequencies</a></li>
<li><a href="#tfidf">Key Term Scores</a></li>
<li><a href="#refine">Refining Your Word List</a></li>
<li><a href="#other">Vocabulary In Other Languages and Collections</a></li>
</ul>
<li><a href="#tasks">Things You Can Do with the Vocabulary Tool</a></li>
<ul>
<li><a href="#comp">A Comprehensive Vocabulary List for a Work</a></li>
<li><a href="#authess">A List of Essential Words for an Author</a></li>
<li><a href="#inter">A List of Basic Words for Intermediate-Level Reading</a></li>
<li><a href="#compess">A List of Essential Words for a Comprehensive Greek or Latin Exam</a></li>
<li><a href="#keylist">A List of Keywords for a Text</a></li>
</ul>
</ul>
<hr>


<H2><a name="settingup">Setting Up Your List</a></H2>
<CENTER>
<img src="/img/help/vocab.first.gif" alt="A Guide To Using the Vocabulary Tool" border="1">
</CENTER>

<P>
<B><a name="worksel">Work Selection</a></B>:  When you first use the vocabulary tool, you are presented with a selection box that shows all of the works in a language in a Perseus collection.  You can select the documents for your vocabulary list by selecting the documents in this box.  As usual, Macintosh users can select more than one work by holding the Command key as they click, and Windows users can select more than one work by holding the Control key.  You may select as many works or parts of works as you like.
</P>

<P>
<B><a name="sortorder">Sort Order</a></B>:  It is possible to select several ways to sort your list.  Different sort orders are useful for different tasks.
<ul>
<li>An <STRONG>alphabetical listing</STRONG> will allow you to generate a traditional word list that you can use to help you study a text.  
<li>A list ordered by either <a href="#weight">weighted</a> or <a href="#max">maximum</a> <STRONG>frequency</STRONG> will allow you to generate a list of the most common words in a text.  This will allow you to know what words are important to learn to help you read a text more effectively.  
<li>A list ordered by the <STRONG>key word score</STRONG> will provide an initial guide to the distinctive words in your selection of texts.   Words with a high 'key word' score appear relatively often in your document selection but appear relatively infrequently in other documents in the collection in the Perseus Digital Library.  The five or ten words with the highest key word scores are frequently the names of the important people, places, and concepts in your selection of works.  See <a href="#tfidf">below</a> for more information and detailed examples.
</ul>


<P><STRONG><a name="listlen">List Length</a></STRONG>:  The tool also allows you to select the percentage of the words in a document that you want to include in your list.  As with the sort orders, the different percentages are useful for different purposes.  The vast majority of words in any text appear only once.  If you are looking for a list that contains the essential vocabulary for your selected texts, pick a higher percentage.  If you want a comprehensive list, pick a lower percentage or the "all words" option. Selecting an alphabetical listing of words requires the display of all words in your selection.  

<P><STRONG><a name="oopts">Output Formats</a></STRONG>:  The vocabulary tool provides two different ways to format your output.  You can choose a table that will provide attractive output in a web browser or a comma-delimited list that you can import into other software programs.  Note that some browsers have problems displaying very large tables;  if you are requesting a very long list, the comma-delimited version may work better.

<P><STRONG><a name="colsel">Column Selection</a></STRONG>:  The vocabulary tool can provide a great deal of information such as word frequencies, key term scores, percentages, and short definitions.  This option lets you select the data that appears in the vocabulary table so that you can use the format that is best suited for your needs.  A complete description of all of the available columns is provided <a href="#list">below</a>.

<p>The defaults for these features are to sort by weighted frequency and to display the top 50%.  For a typical text this gives a list of 100 to 300 distinct words.  These are also the values used if you create a vocabulary list using the "Vocabulary in this document" link in the sidebar of a Perseus classical text.

<H2><a name="viewing">Viewing the Results</a></H2>
<P>
After you make your selection, the system will calculate a custom vocabulary list for your documents.  
<CENTER>
<img src="/img/help/results.1.gif" alt="Vocabulary Results Screen" border="1" align="middle">
</CENTER>

<P><STRONG><a name="size">Vocabulary Size and Density</a></STRONG>:  Several numbers will appear at the top of your vocabulary list to help you understand general characteristics about the vocabulary of your selection.  

<ul>
<li>The <STRONG>number of words</STRONG> in your selection. </li>
<li><a name="unique">The <STRONG>number of unique words</STRONG> in your selection</a>, or the number of words that will appear on your list of if you select the "All Words" option.</li>
<li>A <a name="vocabdensity"><STRONG>vocabulary density score</STRONG></a> which is the ratio of the number of words in the document to the number of unique words in the document.</li>
</ul>
<br>These three numbers are intended to help you understand the level of vocabulary complexity in your selection.  A work with more complex vocabulary will have more unique words while a work with simpler vocabulary will have fewer unique words.  The vocabulary density ratio provides a normalized mechanism for this same information.  If the vocabulary density ratio is small, the vocabulary is more complex; as the number increases, the text becomes easier.  Another way to think about this ratio is that it is an expression of the number of words on average that you will encounter between every new word.  
</P>
<P>Compare the word counts and vocabulary density scores for Aeschylus' <EM>Oresteia</EM> and Xenophon's <EM>Anabasis</EM>.  The <EM>Oresteia</EM> contains 18,934 words and 6,974 unique words with a vocabulary density score of 2.715.  This means that, on average, one out of every three words that a reader encounters will be new.  On the other hand, Xenophon's <EM>Anabasis</EM> contains 57,193 words with 4,358 unique words, for a vocabulary density score of 13.124.  The higher vocabulary density score suggests a much simpler vocabulary; on average only one in every thirteen words will be new.  In fact, the <EM>Anabasis</EM> is three times longer than the <EM>Oresteia</EM> but it contains only about 2/3 as many unique words.
<p>Similarly, Livy's <em>History</em>, books 1-10, is 159,132 words long but contains only 8,735 unique words, so its vocabulary density is 18.218.  Virgil's <em>Aeneid</em>, less than half as long (63719 words), uses almost as many different words (7,531 of them), giving it a vocabulary density score of only 8.461.  In other words, while Livy's vocabulary is larger than Virgil's, new words do not appear as frequently.

<P>
<STRONG><a name="list">The Vocabulary List</a></STRONG>:  Below the table header, the vocabulary list will appear along with a series of numbers to give you information about each word in the context of your list.  The actual contents of your list will vary based on the way that you customized the list and the sort order that you requested.

<ul>
<li><strong>Count</strong>:  The row number is supplied to help you keep your place in the table.  When you request the comma-separated output form, the count appears on every row;  in the tabular display form, the count appears on every tenth row.
<li><strong>Word</strong>:  The words in your vocabulary list are linked to the <a href="TextHelp.html#links">Word Study Tool</a>, from which you can get a short definition, a link to the full lexicon entry, and frequency information for this word in the corpus as a whole.
<li><STRONG><a name="freqs">Minimum</a>, <a name="max">Maximum</a>, and <a name="weight">Weighted Frequencies</a></STRONG>:  These numbers give you a sense of how common a word is in a text.  A more detailed description of the three different ways that we count words is provided <a href="TextHelp.html#max">in the word frequency tool help</a>.  If you have chosen to sort the list by weighted frequency (the default) or by maximum frequency, you will also see a running total and running percentage count for each word on the list.</li>
<li><STRONG><a name="defs">Short Definition</a></STRONG>:  The short definition provided is automatically extracted from either the Intermediate Liddell-Scott Greek lexicon or Lewis' Intermediate Latin Lexicon.  This definition is the one listed first in the dictionary entry for each word.  Thus, the definition provided for words with multiple senses may not be entirely correct for the works that you have selected.  If you would like to see the complete definition, you can look up the full definition in the dictionary useing the <a href="TextHelp.html#links">Word Study Tool</a>.  If you would prefer not to see the short definitions in your list, you can suppress them using the <a href="#colsel">column selection feature</a> of the vocabulary tool.
<li><strong><a name="searches">Searches</a></strong>:  The search feature allows you to search for each word in the text or group of texts whose vocabulary you've listed, and to look up the words in the lexica.  In each row, the "Search" link uses the Lookup Tool to search for the word in the text.  Other links go to this word's entry in the various lexica.  Here LSJ is the <cite>Liddell and Scott Greek Lexicon</cite> and L&S is <cite>Lewis and Short's Latin Dictionary</cite>;  these are the large lexica with full entries.  "Middle Liddell" and "Elem. Lewis" are intermediate lexica for Greek and Latin respectively, containing shorter definitions with less usage information.

<li><STRONG><a name="tfidf">Key Term Score</a></STRONG>:  As noted above, words with a high key term score appear relatively often in your selection of documents and relatively infrequently in the collection as a whole.  Words with a high key-term score are an automatically extracted variety of keyword that provides an initial guide to important people, places, and concepts in your selection.  Frequently appearing words that provide less guidance about the contents of your selection will have a low keyword score and the least important key words will have a score of 0.  Very common words like <i>sum</i> or <i>ille</i> in Latin, <i>eimi</i> or <i>outos</i> in Greek, will always have a key term score of zero.
<br>Consider the words with a high key word scores for two documents.  Lysias' <EM>On the Murder of Eratosthenes</EM> and Book 21 of the <EM>Odyssey</EM>.  The top ten key words for <EM>On the Murder of Eratosthenes</EM> include the name Eratosthenes and words for adultery, a servant woman, a child, a door, and several words for entering a house.  Likewise, the top key words for Book 21 of the <EM>Odyssey</EM> include Antinous, Odysseus, Telemachus, and nouns and verbs associated with stretching and stringing a bow.  The key words for these two document do not, of course, capture all of the nuances of the actions being described, but they do provide a useful overview of elements that are potentially important as you read the texts.  <br><font size=-1><STRONG>Note</STRONG>: The quality of the key words will vary based on the size and similarity of the works that you select.  As with any automatic knowledge discovery procedure, these scores might provide an interesting guide to further exploration but they might not produce interesting or useful results for your selection of texts.</font>
</ul>
<P>
<STRONG><a name="refine">Refining Your Word List</a></STRONG>:  At the bottom of your vocabulary list, you will find the same controls that you used to establish your initial vocabulary list.  This will allow you to select new works, refine your sort order, or change the number of words that your list contains.  
<BR>
<STRONG><a name="other">Vocabulary in Other Languages and Other Collections</a>:</STRONG>  At the very end of the initial selection screen and each vocabulary lists are links that will display the vocabulary tool for other languages and other collections in the Perseus Digital Library.  

<P><H2><a name="tasks">Things You Can Do with the Vocabulary Tool</a></H2>
<P>The Vocabulary Tool is very versatile and it can be used in several ways to help you read a text in the Perseus Digital Library.


				<ul>
<li><STRONG><a name="comp">A Comprehensive Vocabulary List for a Work</a></STRONG>:  If you want a comprehensive vocabulary list that you can consult as you read and review a text, you should select the text that you are trying to read in the select box.  Use the alphabetical sort order and show all words for the list size.  This will produce a comprehensive list of words in alphabetical order that you can annotate and consult easily as you are reading a text.</li>
<li><STRONG><a name="authess">A List of Essential Words for an Author</a></STRONG>:  If you want to improve your mastery of a particular Greek or Latin author, you should select all of the works by that author in the select box.  Select weighted frequency as your sort order and top 40% or top 50% as your list size option.  This will provide you with a list of 'essential words' that you should memorize to maximize your understanding of that author.</li>
<li><strong><a name=inter>A List of Basic Words for Intermediate-Level Reading</a></strong>:  If you are an intermediate-level student, beginning to read unadapted texts, select five or six texts of interest in the select box.  Select weighted frequency as your sort order and top 50% or top 60% as your list size option.  This will give you a sense of the most important words in the language;  when you are familar with these words, you can begin reading, confident that you will know half to two-thirds of the words on a typical page.
<li><STRONG><a name="compess">A List of Essential Words for a Comprehensive Greek or Latin Exam</a></STRONG>:  If you are an advanced student preparing for comprehensive exams, select a large list of authors that are appropriate for the requirements of your exam in the language box.  Select weighted frequency as your sort order and top 70% or top 80% as your list size option.  This will provide you with a list of important words to help you prepare for your exam.</li>
<li><STRONG><a name="keylist">A List of Key Words for a Text</a></STRONG>:  If you want a quick overview of the potentially important words and concepts in a text, select the text that interests you with a sort order of key word score and a list size of top 10%.  This will provide a short list of potentially important words to be aware of as you read the text.</li>
</ul>
				<hr>
				<i>revised  25-Oct-02, AEM</i>
</div> <!-- index_main_col -->
	</div> <!-- content 2column -->
</div> <!-- main -->

    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
