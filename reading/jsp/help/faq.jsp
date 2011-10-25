<html>
  <head>
    <title>Perseus - Help - FAQ</title>
    <link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
    <%@ include file="/includes/index/header.jsp" %>

 <div id="main">
<jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
	<jsp:param name="subtabActive" value="faq"/>
      </jsp:include>
      
  <div id="content" class="2column">
   <div id="index_main_col">
 <h3>The Perseus WWW Frequently Asked Questions List</h3>
 <p>
This list is a summary of the most frequently asked questions received
by the Perseus <a href="mailto:webmaster@perseus.tufts.edu">webmaster</a>.  If the
answer to your question is not here, please <a href="mailto:webmaster@perseus.tufts.edu">e-mail us</a>.
<ol>
<p><b>About Perseus</b><br>
<li><a href="#whatis">What is the Perseus Digital Library Project?</a>
<li><a href="#free">Is this site free?</a>
<p><b>General Questions</b>
<li><a href="#start">Where do I start?</a>
<li><a href="#info">How do I find something in Perseus? What types of
searches can I do?</a>
<!-- <li><a href="#word">How do I find the meaning of this Greek or Latin
word?  My searches aren't returning any results.</a> -->
<li><a href="#trans">What is the (Greek/Latin/English) translation of
this phrase?</a>
<li><a href="#paper">Will you help me with my project or paper?</a>
<!-- <li><a href="#mail">Why do I receive an automatic reply to a webmaster
question?</a> -->

<p><b>Images</b>
<li><a href="#permiss">Why does it say that I do not have permission
to view the image I want to see and how do I get permission?</a>
<li><a href="#copy">I would like a copy of one of the images I've seen
in Perseus.  How do I get one?</a>
<p><b>Texts</b>
<li><a href="#text">Why don't you have a particular text?</a>
<li><a href="#scroll">How can I download a complete text file?</a>
<!-- <li><a href="#font">I'm having trouble viewing the Greek with the
Greek fonts.  What should I do?</a> -->
<!-- <li><a href="#unicode">Why do I see wacky question marks or boxes in
the middle of Latin words?</a> -->
<li><a href="#typo">Where do I report a typo? What if I disagree with
your translation?</a>
<!-- <li><a href="#hopper">What are the advantages of the Perseus text
system?</a> -->
</ol>

<hr><ol>
<li><a name="whatis"><b>What is the Perseus Project?</b></a>
<p>The Perseus Project is an evolving digital library of resources for
the study of the humanities. We are funded to perform research on
developing tools to provide users with improved access to various
types of materials. Past work has focused on building and linking
together collections. Current work considers ways of developing and
refining tools for presentation of the materials in the Perseus DL. We
are primarily a research project, although we do incorporate services
for our audience. For more on who we are and what we do, see the <a
href="/hopper/about">About Perseus page</a> and our
list of <a
href="/hopper/about/publications">publications</a>.</p>
<hr>
<li><a name="free"><b>Is this site free?</b></a>
<p>Currently, we offer access to materials free of charge, with the
exception of certain copyrighted images, <a href="#permiss">noted
below</a>. As we explain elsewhere, we are not funded to provide
services, but to do research on how people use these materials and how
use may be improved or refined. We offer no warranty on use, as
outlined <a href="http://www.perseus.tufts.edu/site.html">here</a>. Although portions of the data
may be derived from public domain materials, the Perseus Project has
created tools and structure to link this data to the remainder of the
library. Please see <a href="/hopper/help/copyright">copyright
information</a> for more on rights and right holders, and consider <a
href="/hopper/help/support.jsp">supporting Perseus</a> with a donation. </p>

<hr>
<li><a name="start"><b>Where do I start?</b></a>
<p>There are many ways of accessing the materials in the Perseus
digital library. In fact, there is more than one way of getting to the
core content of the digital library. <br>
<div align="center"><img src="/img/navigate.gif"><br><font
size="-1">Multiple ways of getting to core Perseus contents.<br>(Or,
there's more than one way to slay a Gorgon!)</font></div>Clearly,
there are many ways you can begin exploring Perseus content. Which one
best suits your particular needs? The answer to that question
typically depends on the type of information you require. Read on for
more information.<br>



<hr>
<li><b><a name="info">How do I find something in Perseus? What types
of searches can I do?</a></b>
<p> The Perseus digital library contains a
wealth of information but new users can have trouble accessing it all.
We at the project are committed to providing resources and tools to
use these resources to as many people as possible.  We are not,
however, a research service and we cannot answer specific questions or
perform customized searches for information.  We encourage users to
continue to make use of local libraries and librarians when doing
research projects.<p> Here are some general suggestions to help
introduce you to the way that the Perseus library is set up:<p> <a
name="lookup"><b>Start </b></a><b>with the <a
href="/hopper/search">Perseus lookup
tool</a>.</b> The lookup tool is a map to the contents of Perseus: its
results point you to the subject areas in which you will find
information about your search topic. It is a guide to information
within Perseus; you the user must decide which type of information
suits your interest, but be aware that for certain types of inquiries,
you may need another tool. The input box for the lookup tool is
beneath the &quot;Search&quot; icon on the home page next to the
banner image and on every other page in the same general location. You
need only type your query in the input box and click the
&quot;Search,&quot; or the &quot;return&quot; or &quot;enter&quot;
key. </a>

<!--<hr>
<li><b><a name="word">How do I find the meaning of this Greek or Latin
word?  My searches aren't returning any results.</a></b><p>If you are
a Greek or Latin novice and trying to translate a word or phrase,
check our <a href="trans.html">Greek and Latin translation tips.</a>
<p>First, check that you're using the correct tool; neither the lookup
tool nor the English index will find Greek or Latin words. Second, you
should check that your Greek or Latin word is properly spelled and
transliterated. While the <a href="#lookup">Perseus Lookup Tool</a>
mentioned above helps you find alternative spellings for various
English terms, you must be <b>precise</b> when searching for Greek or
Latin words.  For example, if you are searching for a Greek term in
the original Greek, be certain that you are transliterating the
letters according to the Perseus transliteration system: <br>
</p>
<CENTER><IMG SRC="/img/latin.gif"></CENTER><br>(The term "<a
href="/cgi-bin/resolveform?lookup=catharsis&type=begin&author=*2.0&options=Sort+Results+Alphabetically&.submit=Submit+Query&formentry=1&lang=Greek">catharsis</a>"
actually begins with a kappa in Greek, so while a search for
"catharsis" in the Greek lexicon will show no results, a search for
"<ahref="/cgi-bin/resolveform?lookup=katharsis&type=begin&author=*2.0&options=Sort+Results+Alphabetically&.submit=Submit+Query&formentry=1&lang=Greek">katharsis</a>"
will.)  Each page that calls for transliteration has a chart with the
correct character matches.<p>
A majority of Greek or Latin searching errors occur when a form other
than the basic, uninflected form has been entered.   You must be
certain that the word for which you are searching is the <b>correct
root form</b>, or else your results will be non-existent or
incomplete.  Just as you can't check an English dictionary for "swam,"
and you must look up the basic form of the word, "swim," you can't
check the Greek and Latin lexica for inflected forms of Greek and
Latin words.  For example, "ave" (as in "Ave, Maria") is a Latin word,
but <a
href="/cgi-bin/resolveform?lookup=ave&type=exact&author=*Roman&options=Sort+Results+Alphabetically&.submit=Submit+Query&formentry=1&lang=Latin">a
search for "ave" will fail</a>.  You must search for the root form of
"ave" which is "<a
href="/cgi-bin/lexindex?lookup=aveo%232&lang=Latin&corpus=Roman&display=">
aveo</a>."  If you are uncertain about the form of the word for which
you are searching, you may want to first use the morphological
analysis tool for <a
href="/cgi-bin/morphindex?lang=Greek&corpus=2.0&display=Latin+transliteration">Greek</a>
or <a href="/cgi-bin/morphindex?lang=Latin">Latin</a>.  This will tell
you the correct root of the word, or at least help narrow your
search.  <a
href="/cgi-bin/morphindex?lookup=ave&.submit=Analyze+Form&lang=Latin&corpus=Roman&author=&formentry=1">An
example of an analysis of the above Latin word "ave" is here.</a>
Note that the morphological analysis <b>does not "disambiguate"
forms:</b>  the Latin word "ave" could be derived from one of three
root words.  The reader must choose which of the three best fits the
context of the word.
<p>Searches of the Greek and Latin lexica are <b>single word searches
only</b>, as are Greek and Latin word searches.  Remember, that common
Greek terms have often been "Latinized" when they enter English, so
double check them before attempting a search in Greek.
<p><img src="/img/comptoolchart.gif" alt="" height="238"
width="776" border="0"></p> -->


<hr>
<li><b><a name="trans">What is the (Greek/Latin/English) translation
of this phrase?</a></b>
<p>If you are a Greek or Latin novice and trying to translate a word
or phrase, check our <a href="/hopper/help/archived/trans.html">Greek and Latin translation
tips</a> [note: these tips were written for Perseus 3.0; a 4.0 update is coming soon!]
<p>
Unfortunately, the Perseus Project does not have the staff or
resources to perform custom translation services. Many common phrases may be found via a quick Google search. If you are
learning Greek and/or Latin, you may wish to visit the forum page at
<a href="http://www.textkit.com">Textkit - Greek and Latin Learning
Tools</a>.<br>


Here are some of the most commonly asked about phrases (please don't
e-mail us about these!):
</p>
<UL><li><b>illegitimus non carborundum</b> (comes in many variations):
this is pseudo-Latin;  check the <a
href="http://www.google.com/search?hl=en&q=site%3Aomega.cohums.ohio-state.edu+classics-L+carborundum&btnG=Google+Search">
classics list archives</a> for more information.
<li><b>Lorem ipsum dolor sit amet</b>, . . .. :  it looks like Latin,
but it's nonsense. This is the filler text used to demonstrate
printing layouts which came into use not long after the printing press
itself; it persists in  software manuals to this day. It's believed to
be a bastardization of some Cicero.  Check the <a
href="http://www.google.com/search?hl=en&lr=&as_qdr=all&q=+site%3Aomega.cohums.ohio-state.edu+classics-L+lorem+ipsum&btnG=Search">classics
list archives for more information.</a>
<li><b>uva uvum (or uvam) vivendo varia fit</b> from <I>Lonesome
Dove.</I> Again, an instance of imperfect Latin. <a
href="http://www.library.txstate.edu/swwc/ld/ldex081a1.html">Check
this wonderful WWW page</a> for insight into the translation debate.
</ul>
<hr>
<li><b><a name="paper">Will you help me with my project or
paper?</a></b><p>
The Perseus Project is not a research service and we do not have the
staff or resources to fulfill all of the requests we receive for
assistance.  We will gladly assist our users in searching the Perseus
digital library, but we cannot answer specific research questions or
direct you to relevant sites or bibliography. <p>Some good starting
points are <a
href="http://en.wikipedia.org/wiki/Main_Page">Wikipedia</a>, <a
href="http://about.com/">About.com</a>,  <a
href="http://www.google.com/">Google</a>, and <a
href="http://www.yahoo.com">Yahoo!</a>.

<!-- <hr>
<li><b><a name="mail">Why do I receive an automatic reply to a
webmaster question?</a></b><p>
We receive an average of 20 mails to the webmaster each day; over 100
per week. This number is always increasing.  A majority of these
queries are answered here in the FAQ or elsewhere (<a
href="/help/">other help pages</a>, the <a
href="/copyright.html">copyright page</a>, <a
href="/PerseusInfo.html">About Perseus</a>, etc.). In order to
dedicate our limited resources to the questions pertaining to use of
the Perseus Digital Library, we have reluctantly instituted the
automatic reply. Be aware that <b>if you write from one e-mail address
and request an answer at another, you may not receive a reply. </b>
(The automatic reply contains suggestions for other WWW sites of
interest and links to pertinent parts of Perseus.) <p>
We also want to stress that all mail received at Perseus is not only
read, but also logged into a webmaster database of incoming mail.  We
sincerely appreciate your comments, praise, and suggestions for
improvement; unfortunately, we won't always be able to tell you so in
a personal reply. All reports of errors are investigated promptly.<p>
<b>Some tips on receiving a prompt, personal reply:</b> 1) Send your
mail in complete sentences, not single words or phrases. We can't
guess what your question is, so give as much information as
possible. Also, if you are reporting a problem, include a copy of the
relevant  URL(s). 2) Try not to use words like "urgent" or include
deadlines: our mail, when answered, is answered within five business
days and in the order it is received.  We don't prioritize answers,
respond on the weekends, or guarantee a reply. To that end.... 3) Do
not send homework questions.  We will help you use Perseus to find an
answer: we do not send out the answer.  If you ask an obvious homework
question, you will <b>not</b> receive a reply.  If you ask for help
using Perseus, you will. 4) Make your criticism constructive.  General
comments are good, but if you have a problem with Perseus <b>be
specific</b>. If you want to know why we do (or don't do) something,
we'll tell you. Just ask!  Be considerate of the webmaster and write
only mail you would like to receive. 5) Do not use all uppercase
letters: in most circles, this is considered bad "Netiquette" and
conveys a sense of urgency (see item 2 above) or shouting. No one
likes to be shouted at! <p><b>Remember the more information you give
us, the better equipped we are to answer you quickly, accurately and
completely!</b> -->

<hr>
<li><b><a name="permiss">Why</a>
<a name="sculpt">does it say that I do not have permission
to view the image I want to see and how do I get permission?</a></b>
<p>
Many of the <a href="http://www.perseus.tufts.edu/hopper/collections">art
and archaeology</a> objects in the Perseus digital library are owned
by museums.  Perseus <a
href="/hopper/help/copyright">does not have
permission</a> to display all of the images of these objects on the
WWW. Currently, we provide images from the following:<ul>
<li>all of the images in the
<a
href="/hopper/artifactBrowser?object=Building">Building</a>
and <a
href="/hopper/artifactBrowser?object=Site">site</a>
catalogs; 
<li>the sculpture collections from the<ul>
<li><a
href="/hopper/artifactBrowser?object=Sculpture&field=Collection&value=Museum+of+Fine+Arts,+Boston">Museum
of Fine Arts, Boston</a>, <li>the <a
href="/hopper/artifactBrowser?object=Sculpture&field=Collection&value=Berlin,+Antikenmuseen">Berlin
Antikenmuseen</a>, and <li>the <a
href="/hopper/artifactBrowser?object=Sculpture&field=Collection&value=Paris,+Mus%26%23233%3Be+du+Louvre">Mus&eacute;e
du Louvre, Paris</a>;
</ul><li>the <a
href="/hopper/artifactBrowser?object=Coin&field=Collection&value=Arthur+S.+Dewing+Collection">Dewing coin collection</a>;

<li>and vase collections from the
<ul>
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Berlin,+Antikenmuseen">Berlin
Antikenmuseen</a>,
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Paris,+Mus%26%23233%3Be+du+Louvre">Mus&eacute;e
du Louvre, Paris</a>,<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Museum+of+Fine+Arts,+Boston">Museum
of Fine Arts, Boston</a>, 
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Cambridge,+Harvard+University+Art+Museums">Harvard
University Art Museums</a>,
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=University+Museums,+University+of+Mississippi
">University of Mississippi</a>,
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=University+Museum,+University+of+Pennsylvania">University
of Pennsylvania</a>,
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Museum+of+Art,+Rhode+Island+School+of+Design">Rhode
Island School of Design</a>,
<li><a
href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Tampa+Museum+of+Art">Tampa
Museum of Art</a> and
<li><a href="/hopper/artifactBrowser?object=Vase&field=Collection&value=Toledo+Museum+of+Art">Toledo Museum of Art</a>.
</ul>
</ul>
<hr>
<li><b><a name="copy">I would like a copy of one of the images I've
seen in Perseus, how do I get one?</a></b><p>
The credit notices for all images appear at the top of each image
page.  If this notice reads "...courtesy of X museum," you must
contact the named museum for permission to reproduce their image.
Addresses for museums are at the bottom of our <a
href="/hopper/help/copyright">copyright page</a>. <p>The Perseus Project does
not have permission or resources to redistribute images. No image
contained in the Perseus digital library may be reproduced in any form
without permission of the copyright holders. We cannot authorize reuse
of Perseus images on other WWW sites, even for educational, non-profit
use. If you have further questions about images, <a
href="mailto:webmaster@perseus.tufts.edu">please contact us</a>.

<hr>
<li><b><a name="text">Why don't you have a particular text?</a></b><p>
The Perseus Project's initial focus was the ancient Greek world.
Subsequent growth of the library into the Roman era and the
Renaissance, including the works of Christopher Marlowe, has been a
gradual process. Since we are a grant-funded project, we have limited
resources at our disposal. If you have suggestions for future work,
please let us know. We log all suggestions for future expansion.

<hr>
<li><b><a name="scroll">How can I download a complete text
file?</a></b><p>
Our <a href="/hopper/help/copyright">copyright</a> agreements with the
publishers of our texts do not permit us to offer full text
downloading for all works in Perseus at this time. Texts for which xml downloads are available are indicated as such by a creative commons license and links to download options. You will find these in the center of a text viewing page, below the main focus text. We do not offer other formats for texts at this time. 
 We realize that users with slower connections or those who prefer reading texts off of a hard copy, not
the computer screen, prefer a downloading option.  The addresses for the
publishers of the texts are provided on our <a
href="/hopper/help/copyright">copyright page</a> if our readers wish to
purchase the books. <a href="http://www.textkit.com">Textkit - Greek and Latin Learning
Tools</a> is a fine site with many public domain texts in downloadable .pdf format.
<!--<hr>
<li><b><a name="font">I'm having trouble viewing the Greek with the
Greek fonts.  What should I do?</a></b>
<p>
First, check our <a href="/Help/fonthelp.html">font help page</a> for
more information on the many options for viewing Greek. This page
contains important instructions, information, and notices of known
font &quot;bugs.&quot; Please follow all of the steps outlined on
these pages before contacting us with your questions. Also note that
these instructions are subject to change, so if you have used fonts in
the past but they are not working now, check the updated
instructions. If you are having difficulty installing fonts, please
check your user manual or ask your local technical advisor. The
Perseus Project does not have the staff to answer font installation
questions. Perseus provides some fonts for reading texts: you may not
be able to use these fonts to type Greek characters. -->
<!-- <hr><li><a name="unicode"><b>Why do I see wacky question marks or boxes in
the middle of Latin words?</b></a>
<p>This is a problem with Unicode display on some browsers and
platforms. Not all browsers are able to correctly map Unicode
characters. You'll most likely encounter this problem with macrons
appearing in Latin words (such as in the Latin grammar or Latin
dictionaries). If your system should be able to read Unicode (Windows
running IE), make sure you have followed the steps in the <a
href="/help/fonthelp.html">font display help section</a>. Also, double
check that your browser is accepting cookies.</p>
<p>If you are using another system, such as a Mac, we suggest changing
the way your display is configured to read Greek. (This sounds
counter-intuitive for reading Latin!) Recent testing on a Mac
eliminated the question marks by setting the Greek display to
SuperGreek on both IE and Netscape. (NB: Netscape 4.75 was tested, not
Netscape 6.) Again, be sure that you are accepting cookies. You need
not install SuperGreek for this to work. The macrons will appear as
circumflex accents, not macrons, but you may find this less
troublesome to view. Tips on configuring your display are linked from
the <a href="/help/TextHelp.html#display">text help section</a>.</p> -->

<hr><li><b><a name="typo">Where do I report a typo? What if I disagree
with your translation?</a></b><p>
At this time, all reports of typographical errors should be sent to
the Perseus <a
href="mailto:webmaster@perseus.tufts.edu">webmaster</a>. Please
provide the exact text citation. We rely on our users to bring these
to our attention, and welcome all reports. These will be logged and
investigated. In the case of variant readings, Perseus follows the
original print edition of the on-line text.<p><b>Perseus does not
translate works.</b> You may disagree with the translation of a work provided in Perseus; this is to be expected. Keep in mind, however, that Perseus is a digital library and we are not the authors of the translations in the digital library, no more than your local librarian is the author of every book in his or her library.<p><b>Perseus does not alter the content of a print work</b> when it
is converted to an electronic version except to regularize spelling
and remove archaisms. In some rare cases, we have entered older source
materials which may contain outdated information which has been
superseded by recent research. Always check the publication date on
materials. We have made an effort to provide as many materials as
possible, and in doing so, we have had to rely on older, public domain
sources.</p>
<!-- <li><a name="hopper"><b>What are the advantages of the Perseus text
system?</b></a>
<p>We implemented the current version of the text system in response
to user comments and suggestions. First, users had trouble finding
texts and works within Perseus. The new <a
href="/cgi-bin/perscoll">Perseus table of contents page</a> shows you
all of the contents of Perseus at once. You may customize this listing
for specific collections and/or languages. This is done automatically
now, and not on a static page. This means that the contents are always
up-to-date. The basis of this system is a new and improved means of
easily adding texts to the Perseus digital library, following the
example of Perseus images. Second, users requested more display
options, such as a &quot;cookie&quot; for preserving display options
and the choice of the default text language. Third, navigation within
a text was not intuitive. We now have a go to navigation box, a
navigation bar, and arrows within every text. This helps you find a
specific passage more quickly and see your relative location within a
text. Fourth, we improved the searching tools, added new texts and
commentaries, and added new features such automatic text links to the
new <a href="/cgi-bin/patlas">Perseus Atlas</a> and cross reference
links. Beneath it all, we more than doubled our processor power for
WWW service. This new system means we will be better able to deliver
future additions to the Perseus Digital Library. So, check the <a
href="/help/tochelp.html">table of contents help page</a> and the <a
href="/help/TextHelp.html">updated text help page</a> and try it
out!</p> -->
</ol>
<hr>
<i>document created: 1/12/98</i><br>
<i>document last revised: 8/30/07</i><br>
<i>LMC</i>
</div> <!-- main_col -->

     </div> <!-- content 2column -->
    </div> <!-- main -->
    
    <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>
