<html>
<head><title>Extending a Digital Library: Beginning a Roman Perseus</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
<script type="text/javascript" src="/js/hopper.js"></script>
</head>
<body onload="checkRedirect();">
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
    <jsp:param name="tabActive" value="about"/>
	<jsp:param name="subtabActive" value="publications"/>
    </jsp:include> 	
    <div id="content" class="2column">
        <div id="index_main_col">
		<div id="publications">
		
		<p>Creating an Infrastructure for Scholarly Publication On Line<br/>
		Anne Mahoney
		</p>
		
		<h4>Abstract</h4>
<p>The Stoa Consortium aims to promote collaborative scholarship, published on line and freely available to other scholars and general readers.  As a publisher, we must provide 
the mechanisms for making these scholarly works available.  As an on-line publisher, we intend to provide more than what a good print publisher can provide: electronic texts can 
be explicitly connected as books cannot. </p>
<p>A digital library project generally produces and controls its own texts and images.  This makes it relatively easy to interconnect those resources.  A digital publisher, on the 
other hand, does not produce texts itself, and indeed the works published under its auspices need not all live on the same computer system.  Interconnection in this environment 
requires more explict co-operation by the authors and editors of the text:  specifically, use of the same markup rules. </p>
<p>Markup for us means SGML or XML, conforming to the TEI DTD and following Stoa conventions for which features to mark, which values to use for certain attributes, and which 
meta-data to include in the document header.  We have found that most scholars wishing to publish texts with us do not know SGML in general, or this DTD in particular. Although 
they quickly see the benefits of structured markup, they must learn the language and the local idiom.  The first piece of publishing infrastructure, then, is support for markup: 
documentation, editing software, and validators. </p>
<p>Many authors also want detailed control over the appearance of their work.  They expect, sometimes unconsciously, that the technology of the Web as it exists today is and will 
be the final, best way to publish their work.  While we prefer to take a longer-term, more general view, we recognize that on-line publication today does in fact mean the Web.  
The second piece of infrastructure is display formatting, whether for the Web, for printing, or for some future delivery medium. </p>
<p>The third piece of infrastructure is interconnection, which is what makes on-line publication fundamentally different from print. Interconnection means that references to other 
objects, outside the text, can automatically be hyperlinked to those objects.  The typical digital library model is that objects within the library can be hyperlinked to each 
other, while objects outside are not linked. Since the digital publishing house does not "contain" its texts, on the other hand, there is no reason for it to restrict these 
reference connections to only works it has published itself.  The Stoa has developed a reference database that allows any work published by the Stoa to refer to any resource 
elsewhere on the Internet.  Hyperlinks are generated when the text is displayed, which means they are always as current as the content of the database. </p>

<p>The Stoa's tool set will be made available as open-source software, and we hope that other on-line publishers and digital libraries may wish to share reference information. </p>

<h4>Introduction</h4>
<p>The Stoa Consortium was founded in 1998 to promote collaborative scholarship  in the humanities, particularly the classics.  The Stoa also intends to promote  technical 
standards, for text encoding as well as for image capture, geographic  data recording, and other processes.  The best way to promote a standard is to  provide an application 
that uses it.  For the text encoding standards the Stoa  wishes to use, that application is the Perseus text-processing system.  The  Perseus system works with texts encoded in 
SGML<a href="#fn1" name="anch1"><sup>1</sup></a> , using  the DTD of the <a href="http://www.tei-c.org">Text Encoding Initiative</a>.   Those texts can be presented on the Web, 
converted to PDF, searched, interlinked,  and indexed, based on structural and semantic information in the markup. </p>
<p>The Perseus text system is an infrastructure, but it requires additional support,  both technical and social.  Technical support includes the ancillary applications that  
facilitate structured markup -- editors, syntax validators, documentation.  It  also includes applications relevant to the other parts of the system, for example  support for 
image captioning and curatorial cataloguing, but those applications are  outside the focus of the present paper. </p>
<p>Social support involves convincing the community that structured markup is  desirable and feasible.  Many potential Stoa collaborators are concerned that  structured markup 
may be difficult to learn;  they would rather use a familiar tool,  for example a word processor like Microsoft Word or a typesetting language like  TeX.  Others note that works 
published on the Web as it exists today must be  converted into HTML, and wonder why it's necessary to write in a different language,  then convert the text;  they'd rather write 
in HTML in the first place.  Still other  scholars expect, not unreasonably, that the technical details are the publisher's  problem;  just as they do not typeset their own books 
and articles for publication  in print, they do not see why they are expected to use an apparently complex  markup scheme for electronic publication.  While all these objections 
are  sensible enough, they do not take into account key reasons for using structured  markup.  I will discuss this further in section two. </p>

<p>The Perseus text system includes a variety of ways to change the display of  a text, some under the reader's control and some controlled by the author or  editor.  Stoa 
collaborators who expect detailed control over the presentation of  their work can have it, up to the limits imposed by HTML of course.  In the Perseus  Digital Library, each 
collection has its own logo and color scheme, but most of  them have a common appearance, indicating that they are all part of a single  digital library.  This is a choice made 
by the Perseus designers, not an intrinsic  feature of the software system, and it is in fact straightforward to change  the appearance of a collection of texts or even of an 
individual document.  I  will go into more details in section three. </p>
<p>The most important feature of the Perseus system is the array of sophisticated  features it provides for interconnecting texts.  Interconnection -- hyperlinking --  is what 
makes on-line publication fundamentally different from print.  Originally,  I expected to implement tools for the Stoa, and last year produced a rudimentary  linking system for 
certain kinds of references.  As I thought about the tools we  were producing for the Perseus Digital Library, however, I realized it was silly to  re-implement something similar, 
when what I really wanted for the Stoa was the full  power of the Perseus toolset.  The various automatically generated hypertexts, implicit  "information-push" searches, and 
dynamically generated reversible citations make this  digital library toolset one of the richest environments currently available for  on-line publication.  The Stoa has therefore 
adopted the Perseus toolset.  This has  also prompted the Perseus team to generalize the toolset and make it more widely  available.  Perseus has another collaborating project 
that hopes to install and use  this application, and we expect that in the next year or so we will be able to  make the text processing system available to other projects as open 
source software. </p>
<p>In what follows, I will discuss the Stoa's technical and social support for structured  markup;  how Stoa projects might exploit the display mechanisms of the Perseus text 
system;  and how structured markup facilitates interconnections among texts, not  just within a single collection but in a web of co-operating systems. </p>

<h4>Markup Support</h4>
<p>Technical support for structured markup is easy:  the basic tools are widely available.   Any number of editors, ranging from highly specialized SGML systems to general-purpose, 
configurable utilities, and at prices ranging from free to tens of thousands of dollars,  make it possible to edit structured text.  None of these editors is more difficult to  
learn than a modern word processor.   </p>
<p>It is also important to be able to verify the correctness of SGML markup, and for this one  uses a validator.  Some editors have built-in validation capabilities, and some can 
even ensure  that the text being edited is never invalid.  It's also possible to use a separate validation  program.  A compromise between integral and separate validation is an 
editor that can call upon  the validation program when you ask it to.  However one invokes it, the SGML validator  identifies any syntactic problems with the markup of the text, 
from mis-spelled tags to  missing structural levels.<a href="#fn2" name="anch2"><sup>2</sup></a>  </p>
<p>Intermediate between technical support and what I'm calling social support  is documentation.  Good software should always have good documentation, and good  standards 
necessarily have good documentation.  The documentation for the TEI DTD is excellent,  if a bit daunting in its magnitude.  The documentation for the DTD itself, however,  is not 
enough:  it's also necessary to explain the standards of the particular project,  and the rules imposed by the software that will process the SGML documents.  I have  written an 
<a href="http://www.stoa.org/markup">explanation</a> of the Stoa's  particular guidelines, in tutorial form;  it's available on the Stoa's web server, where it  has been accessed 
over 6,700 times in the past year.  Technical information about  the implementation of the text system is available in a series of research articles  by members of the Perseus 
technical staff.  Additional documentation is always desirable, however.  In this case, particular desiderata include a detailed listing of the  conventions for attribute values, 
the choices of features to be tagged, and other  project-specific and application-specific guidelines beyond the use of the TEI, as well  as detailed documentation of the major 
data structures and processing flow in the  Perseus text system. </p>

<p>But social support extends beyond documentation.  While it's straightforward to  explain how to create SGML documents, it is also important to explain why.  As I  outlined 
above, there are several different reasons why  Stoa collaborators do not immediately embrace structured markup:  its presumed  difficulty, the apparent silliness of writing in 
one language only to convert at  once to another, and the principle that it should be the publisher's responsibility  to do the publishing of a text.  I will consider each of 
these in turn. </p>
<p>Structured markup is not in itself difficult, nor is it a form of computer  programming.  Whenever we begin a new scholarly article by sketching out a title,  an abstract, 
and section headings, we are making a structured text.  When we  format the abstract in a smaller type face from the rest of the text, or make the  section headings larger and 
bold, we are applying a markup that reflects the  structure.  All that SGML does is provide a formalism for expressing this kind  of structure in a way that <i>any</i> reader 
or <i>any</i> software  can exploit.  When we read a text, if we see occasional short lines of larger,  boldface letters, perhaps starting with a number, we recognize these 
lines as  section headings.  If we're skimming the text quickly, perhaps the section  headings are the only things we read.  We know these lines are section headings  because we 
have a great deal of experience reading scholarly articles, and we  know how they are ordinarily laid out.  We can recognize the section headings  even if the article is written 
in a language we can't read. </p>
<p>With SGML markup, the section headings are recognizable because they are  labelled.  Whereas in ordinary typographical "markup" the features are recognizable  by their 
appearance, and interpretable in the light of human experience, in  structured markup the features are recognizable and interpretable by their  names:  no human experience is 
required.  In other words, a computer program can  find the section headings, for example to produce a table of contents. </p>
<p>In the case of our section headings, it's pretty easy to recognize them  unambiguously:  nothing else in the text is likely to be a short line of larger,  boldface letters.  
Consider a harder case, a word or phrase in italics.  The  human reader easily recognizes whether an italicized phrase is a title, a  foreign phrase, a reported thought, or an 
emphasized phrase;  these are  conventional uses of italics, and context usually makes clear which is which.  A  program scanning a file, however, has no context, because it 
cannot actually  read the text.<a href="#fn3" name="anch3"><sup>3</sup></a>   If you want to list the works  cited in the article, or make a glossary of the foreign phrases, 
you'll need to  inspect all the italicized phrases and decide why each one is in italics.  With  SGML markup, instead of italicizing several different kinds of phrases for  
several different reasons, we mark each one with what it is:  a title, a  phrase in a particular language (which we specify), a reported thought, an  emphasized phrase, or 
whatever else it might be.  Display processing may indeed  render all of these as italics, but the additional information is available  to be exploited by indexing tools, 
automatic dictionary lookups, and so on.   </p>

<p>Structured markup, then, allows authors to do the same kinds of things we  do with our word processors, but also allows us to store more and clearer  information. </p>
<p>But if we are simply going to turn those titles, foreign words, and so on  back into italics, why did we bother marking them up?  This is the second  objection:  why write 
SGML if we are only displaying HTML? </p>
<p>If all you are going to do with your SGML documents is convert them to  HTML, then this objection is correct:  you have no need of SGML.  The point of  a digital library 
system, however, is that you can do much more with structured  documents.  Every important feature of the Perseus text processing system is  based on the fact that the documents 
are encoded in SGML, not HTML;  marked  for their structure, not their appearance.  That they are displayed on the Web  in HTML form is an ephemeral fact, not an intrinsic 
fact about the texts;  the  same SGML files can be converted to other formats for other purposes.  As new  display technologies are developed, it will be possible to use them to 
render  SGML files, based on the semantic information in their markup, without changing  the SGML files at all.  HTML files, on the other hand, will not necessarily  be 
automatically compatible with any other format at all.  The information  extraction and visualization tools of the Perseus text system also exploit  the structural and semantic 
information of the SGML files. </p>
<p>The third objection to SGML raised by some Stoa collaborators is that markup  should be the publisher's problem, just as it is in print.  Print publishers  accept text 
from authors, then convert it to whatever form their in-house system  requires.  Even if the publisher chooses to use SGML, the author might submit  the text on paper, to be 
re-keyed by the publisher's staff. </p>
<p>This is, indeed, how many publishers do business now, but not all.  In fact,  some publishers of printed books and journals ask for submissions in SGML,  according to a DTD 
they specify.  This is a great advantage for them, because  the texts come from their authors already in the correct form for further  processing.  It is also an advantage for 
the authors, because they can  unambiguously specify the semantics of their text.  Good SGML markup makes clear  which sections are major and which are sub-sections or 
sub-sub-sections or  sub-sub-sub-sections;  this is difficult to do with typography, and error  prone with outline numbering.  SGML markup can also identify words in other  
languages, to facilitate spell-checking;  can give regularized forms for words in  non-standard dialects<a href="#fn4" name="anch4"><sup>4</sup></a> ;  or can ensure  that 
footnotes sit next to the text they comment on, rather than on some other page.  An  author who marks up a manuscript with SGML is indicating exactly what each element  of 
the text <i>means</i>, not just what it might look like.  In asking for  submissions in SGML form, the Stoa is being progressive. </p>

<h4>Display</h4>
<p>Regular users of the Perseus Digital Library are accustomed to the look of its  texts.  In most of the Perseus collections, text is displayed in a single window,  with global 
tools in a top bar and collection-specific tools in a side bar.  The background  color of these tool bars identifies the collection.  All Perseus text pages look  much alike. </p>
<p>The designers of the Perseus Digital Library web site chose to retain a common  style throughout, but the appearance of the pages is highly configurable.  In fact, one  of the 
collections, the Tufts University History collection, has a distinctly  different appearance and somewhat different behavior. </p>
<p>The Perseus text processing system allows specification of a display template  for a collection, to be used by all the texts in the collection;  an individual  text may also 
have an overriding template of its own.  The templates for HTML  display are written in HTML augmented with calls to processing functions that can  fetch text or meta-data.  It 
is also possible to override portions of the SGML  conversion rules for particular elements, and this too may be done at the level  of the collection or of the individual 
document. </p>

<h4>Interconnection</h4>
<p>The point of using a digital library system is to interconnect the objects in  the library.  In a print library, books are implicitly connected:  when a book about  Sophocles 
refers to Aeschylus, the author indicates the play and line number, and the  reader is free to pull out a text of the older playwright and read the passage.  In  a well-implemented 
digital library, those connections will be explicit.  When the  on-line edition of the Sophocles book refers to Aeschylus, there will be a live link  at the point of the citation, 
which the reader can follow to find the passage in  Aeschylus.  In the Perseus Digital Library, moreover, this link goes both ways.  Someone  reading Aeschylus will see a link 
back to the book about Sophocles which refers to  the passage. </p>
<p>At present, these interconnections operate only within a single digital library.   The Perseus group expects to make interconnections available among co-operating libraries,  
forming a federation.  This will involve common meta-data rules and naming standards,  so that for example Homer's <a href="/hopper/text?doc=Hom.+Il."><i>Iliad</i></a> is always  
identified in the same way.  Each library in the federated group must make its  catalog available to the others.  Suppose a reader in one of the libraries requests  a section 
of a book about Sophocles.  The digital library system will display the  desired section, and will link the references to Sophocles's plays to copies of those  texts available 
at another co-operating library, for example at Perseus.  The Perseus  group expects to work on implementing such a system after the text processing system  is in use at the 
Stoa. </p>
<p>Citations are not the only kind of interconnections among texts, or between texts  and other objects.  The Perseus text processing system can recognize place names,  dates, 
and keywords in texts and link them automatically to appropriate tools, based  on the contents of the digital library.  These implicit searches will work in any  digital library 
that uses the Perseus text processing system.  As with citations,  however, it is also quite desirable to make these searches work throughout a group of  co-operating libraries;  
this is a future direction. </p>

<h4>Conclusions</h4>
<p>The Perseus digital library system is a powerful tool for on-line publishing of  structured text.  The Stoa will be installing this system for its own texts, and  will 
ultimately join in a federated digital library group with the Perseus Digital  Library.  To make the Perseus system work for Stoa collaborators, those collaborators  will need 
to embrace structured markup. </p>

<hr>
<p><a href="#anch1" name="fn1"><sup>1</sup></a>&nbsp;Or XML;  the differences  between SGML and XML are not significant for the present paper.</p>
<p><a href="#anch2" name="fn2"><sup>2</sup></a>&nbsp;Most SGML validators cannot verify the semantics of the  markup, however.  For example, they cannot check whether text marked 
as prose is really  verse, or whether a quote marked as Latin is really French.  Moreover, they usually cannot  check whether syntactically valid SGML conforms to the project's 
guidelines.  For example,  it is valid according to the TEI DTD to mark a phrase as a quotation, without indicating a  language.  When the quotation is in a different language 
from the surrounding text, the  Stoa's standards require that the language be indicated.  SGML validators cannot verify  this because all they know is that the DTD allows 
<b>quote</b> elements with or without the  <b>lang</b> attribute.</p>
<p><a href="#anch3" name="fn3"><sup>3</sup></a>&nbsp;This is generally true of text processors.  Programs that  can parse natural language, or that have access to dictionaries 
for the main  language of the text or of foreign languages that might appear in it, can  often disambiguate italics as humans do.</p>
<p><a href="#anch4" name="fn4"><sup>4</sup></a>&nbsp;e.g. Attic equivalents for the comic Spartan dialect  in <a href="/hopper/text?doc=Aristoph.+Lys."><i>Lysistrata</i></a>

		</div> <!-- Publications div -->	
	</div> <!-- main_col div -->
        
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
