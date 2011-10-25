<html>
  <head>
    <title>Open Source Code</title>
    <link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

    <div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="opensource"/>
    </jsp:include>

<div id="content" class="2column">
  <div id="index_main_col">
    <div id="opensource">

<p><b>Background and Purpose</b>
<p>
In the earliest stages of the transition from a CD-Rom-based collection to the WWW site, it was clear that 
the nature and scope of the Perseus resource demanded a flexible, extensible, and powerful data management 
system. Written mostly in Perl, the production version of the on-line Perseus text management system evolved 
and grew over eight years, becoming a uniquely powerful platform, capable of ingesting heterogeneous source 
materials and performing a range of automatic services. With few precedents and examples to follow, however, 
the code behind this system reflected organic growth and experimentation, and became difficult to sustain, 
share, and modify. While all versions of the Perseus Digital Library system were designed to be open-source 
(third parties did make use of the HyperTalk, Tcl/TK and Perl code), each of the previous incarnations of 
Perseus were complex and difficult to document, which presented obstacles to new avenues of collaborative 
research and development.
</p>
<p>
As digital library systems matured in the early 00's, the project sought third party solutions for delivering 
resources. At the time, most digital libraries concentrated on locating objects and then left it to the users 
to make sense of what they had found. In contrast, Perseus had increasingly focused on giving users the tools 
to understand what the digital library gave them: the project depended upon a range of automatic linking, 
information extraction and visualization services that existing, largely catalog-oriented systems could not 
support. The project chose to build a new digital library system, designing it from the start to be 
interoperable, modular, and open-source.
</p>
</p>

<p><b>Open-Source Services</b>
<p>
The Perseus Hopper is an open-source project providing a suite of services for interacting with textual 
collections.  While as a whole it provides an integrated reading environment, its individual services are 
designed to be modular and can be grouped into three different classes.
</p>
<p>
<i><b>Linguistic support:</b></i> The Hopper itself is language independent, but the code includes native support 
for Greek, Latin and Arabic.  Given a source text in any one of those three languages (either a text bundled 
with the code release or a TEI-compliant XML text of the user's own), it provides services for automatic 
lemmatization (linking inflected word forms to the dictionary entries from which they're derived) and 
morphological analysis (identifying, for instance, that the Latin word <i>amor</i> is a singular masculine 
nominative noun). At a broader level, it also enables corpus research by automatically generating word and 
lemma frequency information for the entire collection of texts supplied to it.
</p>
<p>
<i><b>Contextualized reading:</b></i>  Since the Hopper is the underlying code base for the Perseus Digital Library, 
it reflects that same emphasis on being an integrated reading environment: much of its power derives not 
simply from isolated textual services, but in the knowledge that emerges from the interaction of texts 
themselves. Users can take advantage of this contextualization with the Greco-Roman and Arabic texts 
provided, or specify themselves the higher-level relationship between their own texts (e.g., that document 
X <i>is a translation of</i> document Y) in order to create a reading environment where passages in a source text 
are accompanied by secondary resources such as translations and commentaries.  Contextualized reading also 
intersects with linguistic support -- since dictionaries are also supported as "secondary" resources, a 
reader can find not simply what dictionary entry a word in a source text is derived from, but also a 
definition of what that word means.  The library environment also includes an architecture for soliciting 
user contributions in the form of "voting" -- this is implemented online in the Perseus Digital Library in 
the form of user votes for morphological forms, but can be extended as well to accommodate other varieties 
of annotation.
</p>
<p>
<i><b>Searching:</b></i> Users can not only read passages from texts, but use a suite of search tools to find what 
they are looking for, in any of the languages the Hopper supports.  These search tools include word and 
phrase searches, in individual texts or collections. These searches include the option to search all possible 
inflections of a word, making them extremely powerful for morphologically rich languages like Greek, Latin 
and Arabic (e.g., a lemmatized search for the root form <i>sum</i> would also find documents containing the 
inflected forms <i>est</i> and <i>sunt</i>).  For Classical texts, which have a well-adopted citation scheme, users can 
navigate a text by typing canonical abbreviations (e.g., Thuc. 1.24).  The Hopper also provides functionality 
to search and browse the tagged named entities (places, people, dates, and date ranges) in a corpus, and 
includes an architecture for presenting archaeological artifact and image data, which is separate from the 
reading environment.
</p>
</p>

<p><b>Extensibility</b>
<p>
The code base itself invites two varieties of extensibility. On the one hand, while the code is bundled with 
a collection of Greco-Roman and Arabic texts around which it has grown, users are able to include their own 
TEI-compliant XML texts as part of the reading environment and enable the same services for those texts as 
those that are available online for Perseus' open-source editions.   As an API, the Perseus hopper also 
includes a number of Java classes for interacting with texts outside of a reading environment -- one can, 
for instance, use the linguistic services such as automatic lemmatization or morphological analysis as 
standalone tools for analyzing not simply the bundled Perseus texts, but any text of their own as well.
</p>
<p>
On the other hand, the Java code itself is also designed with modularity and extensibility in mind. An 
example of this is the variety of classes (all ultimately inherited from CorpusProcessor) to cycle through 
an entire collection of texts and perform some operation on each one.  The workflow to build the library 
environment relies on these classes to calculate word and lemma statistics for the corpus at large, to map 
citations between texts, and to index them all in order to make them searchable later.  These classes are 
easily extensible for any task that requires iterating through an entire collection of texts.  The hopper 
source code also includes a number of services for managing named entities such as people and places, and 
has served as the foundation for visualization projects, plotting that data both geographically on a map and 
historically on a timeline. In terms of modularity, the hopper also includes a number of low-level classes 
for manipulating text -- from finding all possible lemmas for a given Latin form to delimiting an accented 
Greek word.
</p>
</p>

</div> <!-- opensource div -->
</div> <!-- main_col div -->

</div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
