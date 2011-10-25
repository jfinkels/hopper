<html>
  <head>
    <title>Perseus Vocabulary Tool Help</title>
    <link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">

    <%@ include file="/includes/index/header.jsp" %>

    <div id="main">
      <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="vocab"/>
      </jsp:include>

      <div id="content" class="2column">
      <div id="index_main_col">
      <h3>Perseus Vocabulary Tool Help</h3>
				<p>The Perseus Vocabulary Tool is designed to allow users to explore the vocabulary of the non-English texts in the Perseus Digital Library.  Using 
				the Vocabulary Tool you can select a set of documents or document sections and then view a list of all of the words that appear in that selection.</p>

<ul>
<li>Setting Up Your List</li>
<ul>
<li><a href="#worksel">Selecting Sections or Works</a></li>
<li><a href="#sortorder">Sort Order</a></li>
<li><a href="#listlen">List Length</a></li>
<li><a href="#oopts">Output Formats</a></li>
<%--<li><a href="#colsel">Column Selection</a></li>--%>
</ul>
<li>Viewing the Results</li>
<ul>
<li><a href="#size">Vocabulary Size and Density</a></li>
<li><a href="#list">The Vocabulary List</a></li>
<li><a href="#freqs">Maximum, Minimum, and Weighted Frequencies</a></li>
<li><a href="#tfidf">Key Term Scores</a></li>
<li><a href="#refine">Refining Your Word List</a></li>
</ul>
<li>Things You Can Do with the Vocabulary Tool</li>
<ul>
<li><a href="#comp">A Comprehensive Vocabulary List for a Work</a></li>
<li><a href="#authess">A List of Essential Words for an Author</a></li>
<li><a href="#inter">A List of Basic Words for Intermediate-Level Reading</a></li>
<li><a href="#compess">A List of Essential Words for a Comprehensive Greek or Latin Exam</a></li>
<li><a href="#keylist">A List of Keywords for a Text</a></li>
</ul>
  <li><a href="#wft">Word Frequency Tool</a></li>
  <ul><li><a href="#max">Why are there Maximum and Minimum Frequencies?</a></li>
    <li><a href="#weight">What is a Weighted Frequency?</a></li>
    <li><a href="#rel">Why use relative frequencies?</a></li>
  </ul>
</ul>
<hr>


<h3><a name="settingup"></a>Setting Up Your List</h3>

<p>There are two ways to access the Vocab Tool depending on what you are looking for:</p>

<%-- Include image from text page with vocab tool --%>
<p><a name="worksel"></a><b>Subsection of a Text</b>: If you want the vocab for a particular section of a text, say <a href="/hopper/text?doc=Perseus:text:1999.01.0133:book=1">Book 
1 of the Iliad</a>, then you would view that section on the text page, and click <em>load</em> in the <b>Vocabulary Tool</b> box. By default, this box will show you 
the top 50% of words in that section, sorted by <a href="#weight">weighted frequency</a>. The box will provide a link to further refine how the vocab is sorted 
and displayed, as described <a href="#sortorder">below</a>.</p>

<center><img src="/img/help/vocab.first.gif"></center>
<p><b>Full Text(s)</b>: If you want to view the vocab for a full text or multiple texts, you will need to access the <a target="_blank" href="/hopper/vocablist">Vocabulary Tool</a> page. 
Here, you are presented with a selection box that shows all of the works in a language in a Perseus collection (see above figure).  You can select the documents for your vocabulary list by 
selecting the documents in this box.  As usual, Macintosh users can select more than one work by holding the Command key as they click, and Windows users can select more 
than one work by holding the Control key.  You may select as many works as you like, though your results may be slow to load if you choose too many texts.
</p>

<p>
<b><a name="sortorder"></a>Sort Order</b>:  It is possible to select several ways to sort your list.  Different sort orders are useful for different tasks.
<ul>
<li>An <strong>alphabetical listing</strong> will allow you to generate a traditional word list that you can use to help you study a text.  
<li>A list ordered by either <a href="#weight">weighted</a> or <a href="#max">maximum</a> <strong>frequency</strong> will allow you to generate a list of the most common 
words in a text.  This will allow you to know what words are important to learn to help you read a text more effectively.  
<li>A list ordered by the <strong>key word score</strong> will provide an initial guide to the distinctive words in your selection of texts.   Words with a high 'key word' 
score appear relatively often in your document selection but appear relatively infrequently in other documents in the collection in the Perseus Digital Library.  The five 
or ten words with the highest key word scores are frequently the names of the important people, places, and concepts in your selection of works.  See 
<a href="#tfidf">below</a> for more information and detailed examples.
</ul>


<p><strong><a name="listlen"></a>List Length</strong>:  The tool also allows you to select the percentage of the words in a document that you want to include in your 
list.  As with the sort orders, the different percentages are useful for different purposes.  The vast majority of words in any text appear only once.  If you are looking 
for a list that contains the essential vocabulary for your selected texts, pick a higher percentage.  If you want a comprehensive list, pick a lower percentage or the 
"all words" option. Selecting an alphabetical listing of words works best when displaying all words in your selection.  

<p><strong><a name="oopts">Output Formats</a></strong>:  The vocabulary tool provides two different ways to format your output.  You can choose a table that will provide 
attractive output in a web browser or an XML file that you can import into other software programs.  Note that some browsers have problems displaying very 
large tables;  if you are requesting a very long list, the XML file may work better.
<%--<p><strong><a name="colsel">Column Selection</a></strong>:  The vocabulary tool can provide a great deal of information such as word frequencies, key term scores, 
percentages, and short definitions.  This option lets you select the data that appears in the vocabulary table so that you can use the format that is best suited for 
your needs.  A complete description of all of the available columns is provided <a href="#list">below</a>.
--%>
<p>The defaults for these features are to sort by weighted frequency and to display the top 50%.  For a typical text this gives a list of 100 to 300 distinct words.</p>

<h3><a name="viewing"></a>Viewing the Results</h3>
<center><img src="/img/help/results.1.gif"></center>
<p>After you make your selection, the system will calculate a custom vocabulary list for your documents.  </p>

<%-- include image of useful information at top of vocab page --%>
<p><a name="size"></a>If you selected works through the Vocabulary Tool page, several numbers will appear at the top of your vocabulary list to help you understand general 
characteristics about the vocabulary of your selection.  

<ul>
<li>The <strong>number of words</strong> in your selection. </li>
<li><a name="unique"></a>The <strong>number of unique words</strong> in your selection, or the number of words that will appear on your list of if you select the 
"All Words" option.</li>
<li>A <a name="vocabdensity"></a><strong>vocabulary density score</strong> which is the ratio of the number of words in the document to the number of unique words in the 
document.</li>
</ul>
These three numbers are intended to help you understand the level of vocabulary complexity in your selection.  A work with more complex vocabulary will have more 
unique words while a work with simpler vocabulary will have fewer unique words.  The vocabulary density ratio provides a normalized mechanism for this same information.  
If the vocabulary density ratio is small, the vocabulary is more complex; as the number increases, the text becomes easier.  Another way to think about this ratio is that 
it is an expression of the number of words on average that you will encounter between every new word.  
</p>
<p>Compare the word counts and vocabulary density scores for <a href="/hopper/vocablist?works=Perseus:text:1999.01.0003&works=Perseus:text:1999.01.0005&works=Perseus:text:1999.01.0007&sort=weighted_freq&filt=50&filt_custom=&lang=greek">Aeschylus' 
<em>Oresteia</em></a> (a name for the trilogy of <em>Agamemnon</em>, <em>Eumenides</em>, <em>Libation Bearers</em>) and <a href="/hopper/vocablist?works=Perseus:text:1999.01.0201&sort=weighted_freq&filt=50&filt_custom=&lang=greek">Xenophon's 
<em>Anabasis</em></a>.  The <em>Oresteia</em> contains 19,707 words and 4,486 unique words with a vocabulary density score of 4.393.  This means that, on average, one 
out of every four words that a reader encounters will be new.  On the other hand, Xenophon's <em>Anabasis</em> contains 57,183 words with 4,007 unique words, for a 
vocabulary density score of 14.271.  The higher vocabulary density score suggests a much simpler vocabulary; on average only one in every fourteen words will be new.  
In fact, the <em>Anabasis</em> is almost three times longer than the <em>Oresteia</em> but it contains only about 2/3 as many unique words.</p>
<p>Similarly, <a href="/hopper/vocablist?works=Perseus:text:1999.02.0169&sort=weighted_freq&filt=50&filt_custom=&lang=la">Livy's <em>History</em>, books 1-10</a>, is 
159,186 words long but contains only 7,446 unique words, so its vocabulary density is 21.379.  <a href="/hopper/vocablist?works=Perseus:text:1999.02.0055&sort=weighted_freq&filt=50&filt_custom=&lang=la">Virgil's 
<em>Aeneid</em></a>, less than half as long (63,719 words), uses almost as many different words (6,677 of them), giving it a vocabulary density score of only 9.543.  In other 
words, while Livy's vocabulary is larger than Virgil's, new words do not appear as frequently.</p>

<p>
<strong><a name="list"></a>The Vocabulary List</strong>: The vocabulary list will appear along with a series of numbers to give you information 
about each word in the context of your list.  The actual contents of your list will vary based on the way that you customized the list and the sort order that you 
requested.

<ul>
<li><strong>Count</strong>:  The row number is supplied to help you keep your place in the table. The count appears on every tenth row.
<li><strong>Word</strong>:  The words in your vocabulary list are linked to the Word Study Tool, from which you can get a short definition, a link to the full lexicon 
entry, and frequency information for this word in the corpus as a whole.
<li><strong><a name="freqs">Minimum</a>, <a name="max">Maximum</a>, and <a name="weight">Weighted Frequencies</a></strong>:  These numbers give you a sense of how common 
  a word is in a text.  A more detailed description of the three different ways that we count words <a href="#wft">is provided below</a>.  The maximum frequency, like
on other pages in Perseus, will link to a search of that word.</li>
<li><strong><a name="defs"></a>Definition</strong>:  The short definition provided is automatically extracted from various lexica in the Perseus collections.  This 
definition is the one listed first in the dictionary entry for each word.  Thus, the definition provided for words with multiple senses may not be entirely correct for 
the works that you have selected.  If you would like to see the complete definition, you can look up the full definition in the dictionary using the Word Study Tool.
<li><strong>Lexicon Entries</strong>: This provides you with links to the entries in our various lexica for the particular word.</li>
<li><strong><a name="tfidf"></a>Key Term Score</strong>:  As noted above, words with a high key term score appear relatively often in your selection of documents and 
relatively infrequently in the collection as a whole.  Words with a high key-term score are an automatically extracted variety of keyword that provides an initial guide 
to important people, places, and concepts in your selection.  Frequently appearing words that provide less guidance about the contents of your selection will have a low 
keyword score and the least important key words will have a score of 0.  <%--Very common words like <i>sum</i> or <i>ille</i> in Latin, <i>eimi</i> or <i>outos</i> in Greek, 
will always have a key term score of zero.--%>
<%--<br>Consider the words with a high key word scores for two documents.  Lysias' <em>On the Murder of Eratosthenes</em> and Book 21 of the <em>Odyssey</em>.  The top ten 
key words for <em>On the Murder of Eratosthenes</em> include the name Eratosthenes and words for adultery, a servant woman, a child, a door, and several words for entering 
a house.  Likewise, the top key words for Book 21 of the <em>Odyssey</em> include Antinous, Odysseus, Telemachus, and nouns and verbs associated with stretching and 
stringing a bow.  The key words for these two document do not, of course, capture all of the nuances of the actions being described, but they do provide a useful overview 
of elements that are potentially important as you read the texts.  --%>
<br><font size=-1><strong>Note</strong>: The quality of the key words will vary based on the size and 
similarity of the works that you select.  As with any automatic knowledge discovery procedure, these scores might provide an interesting guide to further exploration but 
they might not produce interesting or useful results for your selection of texts.</font>
</ul>
<p>
<strong><a name="refine"></a>Refining Your Word List</strong>:  To the right of your vocabulary list, you will find controls to refine your sort order, change the number of 
words that your list contains, or, if you chose full texts, the option to select new works by language.

<p><h3><a name="tasks"></a>Things You Can Do with the Vocabulary Tool</h3>
<p>The Vocabulary Tool is very versatile and it can be used in several ways to help you read a text in the Perseus Digital Library.


<ul>
	<li><strong><a name="comp"></a>A Comprehensive Vocabulary List for a Work</strong>:  If you want a comprehensive vocabulary list that you can consult as you read and 
review a text, you should select the text that you are trying to read in the select box.  Use the alphabetical sort order and show all words for the list size.  This 
will produce a comprehensive list of words in alphabetical order that you can annotate and consult easily as you are reading a text.</li>
	<li><strong><a name="authess"></a>A List of Essential Words for an Author</strong>:  If you want to improve your mastery of a particular Greek or Latin author, you should 
select all of the works by that author in the select box.  Select weighted frequency as your sort order and top 40% or top 50% as your list size option.  This will provide 
you with a list of 'essential words' that you should memorize to maximize your understanding of that author.</li>
	<li><strong><a name="inter"></a>A List of Basic Words for Intermediate-Level Reading</strong>:  If you are an intermediate-level student, beginning to read unadapted texts, 
select five or six texts of interest in the select box.  Select weighted frequency as your sort order and top 50% or top 60% as your list size option.  This will give you 
a sense of the most important words in the language;  when you are familar with these words, you can begin reading, confident that you will know half to two-thirds of the 
words on a typical page.
	<li><strong><a name="compess"></a>A List of Essential Words for a Comprehensive Greek or Latin Exam</strong>:  If you are an advanced student preparing for comprehensive 
exams, select a large list of authors that are appropriate for the requirements of your exam in the language box.  Select weighted frequency as your sort order and top 
70% or top 80% as your list size option.  This will provide you with a list of important words to help you prepare for your exam.</li>
	<li><strong><a name="keylist"></a>A List of Key Words for a Text</strong>:  If you want a quick overview of the potentially important words and concepts in a text, select 
the text that interests you with a sort order of key word score and a list size of top 10%.  This will provide a short list of potentially important words to be aware of 
as you read the text.</li>
</ul>

    <p><li><b><a name="wft">Word Frequency Tool (Greek or Latin):</a></b> If you are searching for occurrences of specific Greek or Latin words, you may use the Word Frequency Tool. There are several options for displaying results. <i>Sort Authors Alphabetically</i> is the default option. <i>Sort Authors by Type of Literature</i> will sort results according to types such as comedy, history, tragedy,etc. <i>Sort Authors by Date</i> will list authors starting from the earliest work to the latest based on the best evidence we have for each author. <i>Words in Author</i> will sort results from the author with the most words in Perseus, to the author with the fewest. <i>Maximum Instances</i> will sort results from the most possible instances in a given author; <i>Minimum Instances</i> reverses this list and starts with the fewest. <i>Maximum Frequency/10K</i> will sort the results from the highest incidence of relative frequency to the lowest; <i>Minimum Frequency/10K</i> reverses this list and begins with the lowest relative frequency.<br>
      <b><a name="max">Why are there Maximum and Minimum Frequencies?</a></b> Although Perseus can disambiguate a vast majority of Greek and Latin words, there are some forms which may be derived from more than one lexicon entry. (E.g. &quot;flies&quot; may be an instance of the verb &quot;to fly&quot; <i>or </i>the noun &quot;fly&quot;, so Perseus would include it in the count for both words. On the other hand, there's no doubt that &quot;sneezed&quot; is a form of &quot;to sneeze&quot;) In cases where the maximum instances differ from the minimum, the maximum are all of the possible occurrences of a given lemma, and the minimum are all of the occurrences of the word which the computer has disambiguated. So, all ambiguous forms are included in a maximum count, and excluded from the minimum. This is also true of the relative frequency calculations.<br>
        <b><a name="weight">What is a Weighted Frequency?</a></b> A weighted frequency tells you whether the actual frequency count for a word (if this were possible) would be closer to the minimum or maximum frequency score. The weighted frequency is determined by assigning a weight to each inflected form based on the number of possible dictionary forms from which the inflected form could be derived. For example, an unambiguous word would have a weight of 1, a word that could be derived from two dictionary headwords would receive a weight of 1/2, a word that could be from 3 different headwords is given the weight of 1/3, etc. The weighted frequency is calculated as the sum of the weights for each inflected form that appears in a text. If the weighted score is equal to the average of the minimum and maximum score, you know that the word is entirely ambiguous in all of its forms. On the other hand, if the minimum, maximum, and weighted scores are all the same, you know the word is entirely <i>un</i>ambiguous in all of its forms. As the weight approaches the maximum score, it becomes more likely that the maximum count is closer to the actual count; the actual count would be greater than the weighted score and less than or equal to the maximum.<br>
          <b><a name="rel">Why use relative frequencies?</a></b> Relative frequencies are based on occurrences of a given word per 10,000 words. For instance, in the case of the Greek verb <i>pemp&ocirc;</i>, Plutarch uses this verb 146 times, which is unimpressive compared with Xenophon's maximum of 350 times. Yet, the corpus of Plutarch on-line in Perseus is about 107,000 words compared with Xenophon's 312,000. So, the relative frequency in Plutarch is 13.67 at its maximum, compared with Xenophon's maximum of 11.21. When making comparisons between authors, it is most useful to know the relative frequency for a given word rather than the word count itself, since the size of the corpora vary.


<hr>
<i>revised  22 Feb, LMC</i>
</div> <!-- index_main_col -->
	</div> <!-- content 2column -->
</div> <!-- main -->

    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
