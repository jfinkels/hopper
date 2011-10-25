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

<b><font face="Helvetica" size="4"><p>Extending a Digital Library:  Beginning a Roman Perseus</p>
</b></font><p>Gregory Crane</p>
<p>Tufts University</p>
<p>June 2000</p>

<div style="text-align:center">
<b><p>Abstract</b>:  After nine years focused on Greek culture, the Perseus Project received a Teaching with Technology Grant from the National Endowment in the Humanities to 
expand its coverage into the Roman world.  This paper describes how we chose to move to a second cultural area. Although Greek and Roman cultures are closely related, the domains 
are in many ways distinct.  This paper describes how we went about starting what we hope will prove a broad, open-ended database over time.  We hope that classicists will find the 
account helps them understand what Roman Perseus is and how it might develop but we also hope that our experiences will also help those planning other cultural digital libraries 
as they balance vast needs against finite resources.</p>
</div>

<p>In the summer of 1996, nine years after the Perseus Project began work on a digital library covering the classical Greek world, we received a National Endowment for the 
Humanities <i>Teaching with Technology Grant</i> to begin work on a Roman Perseus.  The amount of the grant was much smaller &#150; more than ten times smaller, in fact &#150; 
than that which we had been fortunate to receive in 1987 for Greek Perseus and we therefore could not hope to provide the same coverage for things Roman as for things Greek.  
Nevertheless, we were able to augment this initial support with funds from other sources (in particular Tufts University) and a great deal of in-kind labor.  A recent major grant 
($2,700,000) from the NEH-supported Digital Library Initiative is primarily designed to help us apply the methods and procedures developed for classics to other areas of the 
humanities, but it does allow us to refine and maintain our original work on Greece and Rome.  In the end, we created an initial digital library roughly on the Roman world roughly 
one third as large as we had created for Greece. </p>

<p>We are hardly the first group to develop electronic tools for the study of Roman culture.  One internet search engine 
(<a href="http://www.northernlight.com/">www.northernlight.com</A>) reported more than 48,000 sites related to classics on the World Wide Web when this article was being written 
May 2000).  Decades of work in computer aided instruction has preceded us, while the Packard Humanities Institute had already produced a major CD ROM of Latin texts, widely used 
by researchers in the field, and Videodisks and CD ROMs had appeared documenting Roman art and archaeology. The Teaching with Technology program, from the US <i>National Endowment 
for the Humanities</i> (NEH),  funded two other Roman projects:  "<i>VRoma</i>:  A Virtual Community for Teaching and Learning Classics," and the "Vergil Project." <i>VRoma</i> 
concentrated on training secondary school teachers and on developing an on-line interactive learning environment (a "Moo").  The Vergil Project focused on the work of one central 
and widely studied literary figure.  Our goals complemented those of our NEH sibling projects:  VRoma materials contain numerous links into Roman Perseus while we provided the 
Vergil Project with our morphological analyses of Vergil, thus facilitating the creation of their more refined linguistic database. </p>
<p>While we needed to acquire content &#150; texts, images, maps, commentaries etc. &#150; our primary concern was structure:  in a well-designed electronic resource, links, both 
manually composed and automatically generated, bind related pieces of information together, rendering the whole more useful than the sum of its parts.  Designing a digital library 
thus requires the judicious balance between content and form, with content chosen to improve the form and the form structured to enhance the content.</p>

<p>Above all, we needed to develop an open-ended digital library that could be maintained with minimal effort and that could easily grow over time.  We planned to create something 
that was useful but our main focus was on long term development.  The initial three years of development for <i>Roman Perseus</i> constitute (we hope) only the first phase of a 
much longer process.</p>
<p>Our fundamental goals were similar to those which we pursued in our initial nine years focused on Greece.  In designing Roman Perseus, we stressed the following.  First, we set 
out to collect a range of different types of information.  We obviously could not create a comprehensive digital library on Roman culture but we wanted to create a framework that 
could evolve and mature over time.  We therefore set out to include a range of materials so that there would subsequent additions could, insofar as possible, plug into existing 
slots.  Second, we structured the information in such a way that it would be useful for a long time and without a great deal of maintenance.  In some cases, we needed to 
compromise:  we had access to a $30,000 digital camera with 2000x3000 pixel resolution for museum photography but were hesitant to risk this expensive piece of equipment in the 
field, chosing instead to use a less expensive camera with 1000x1500 resolution.  The site photography will thus show its age quicker than the museum images.  In other cases we 
were able to avoid short term limitations:  thus, we invested in highly structured texts, for example, (SGML/XML with Text Encoding Initiative tags) rather than more loosely 
structured (but more easily managed) formats such as HTML.  Third, we designed the system to generate as many automatic links as possible.  We simply did not have the time or 
labor to connect documents by hand.  In the end, we created a foundation that could with reasonable effort be maintained and expanded over time.</p>
<p>The following sections describe the pieces of Roman Perseus.  Some elements are available already.  Others are still in preparation.  Those who wish to follow the progress of 
the project should refer to the Perseus web site:  http://www.perseus.tufts.edu.</p>
<b><i><font face="Helvetica"><p>The Components of Roman Perseus</p>
</b></i><p>First Things First &#150; Adding a Latin Lexicon</p>
</font><p>Texts and images are at the core of any cultural digital library, but we started our work by choosing and entering a full Latin Lexicon.  We chose to invest virtually 
all of our textual data entry budget in entering a large, complex lexicon, and this choice left us with no resources to enter source texts, translations, or commentaries.  We 
could have chosen a student lexicon for one fifth of the cost and our goal was, indeed, to create a resource for students of Latin.  We thus used our resources to make sure that 
we had a lexicon and kept our fingers crossed that we would somehow come up with on-line Latin texts.  Students could use Latin texts from some other source (such as the Packard 
Humanities Institute Latin CD ROM) but those familiar with Greek Perseus would be accustomed to the much greater integration between texts, lexicon and searching possible when all 
are part of the same system.  Of the three categories of link cited above, only the first (from inflected form to lexicon) would be possible if the texts were not part of the 
Roman Perseus system.</p>

<p>Our previous work with Greek Perseus provided three reasons for this counter-intuitive approach.  First, Latin texts and English translations are fairly regular in format and 
can, with effort (see below), be scanned with Optical Character Recognition software (OCR).  Dictionaries generally have small print, complex formatting and thousands of numbers 
(for which spell checking is useless).  We could collect texts over time &#150; individuals have and will continue to place individual texts on-line &#150; but no one was going to 
type in or OCR a major lexicon.  That required a one-time, major investment of capital and would be impossible without hard cash.  </p>
<p>Second, we needed an on-line lexicon so that we could "mine" it for stems to support morphological analysis.  The larger the lexicon, the more comprehensive the resulting 
database.  We knew from experience that students who restrict themselves to narrow reading lists or the limited texts in their course books would base their on-line papers and 
reports on a much wider range of texts.  </p>
<p>Third and most important, we were confident that the large lexicon would be more popular among students in the electronic environment than a smaller lexicon designed purposely 
for students.  We based this on our experiences with putting both the Liddell-Scott-Jones Greek-English Lexicon (9<SUP>th</SUP> ed.) and the intermediate Liddell-Scott Lexicon.   
The numerical majority of those reading Greek on the Perseus Web site seem to be students.  Given a choice of the large lexicon or the student lexicon, they immediately chose the 
large, research lexicon 50% more often.  We attributed this overwhelming preference to the greater readability of the on-line LSJ &#150; we were able to use large print, put blank 
lines between definitions, indent subordinate meanings, highlight citations and use other simple visual cues where the print version sacrificed readability to save space on the 
printed page.  When we subsequently added short definitions to the morphological information, the preference for the larger lexicon grew even further.  The short definition is, 
of course, not always correct and students still need to view the lexicon itself, but those readers who chose to view the lexicon picked LSJ three times as often as the 
intermediate Liddell Scott.  Clearly "middle Liddell" was useful but, if we had to chose, the larger lexicon was a better investment.  </p>
<p>We knew which lexicon we would probably work with.  Rights restrictions would have in any event spurred us to choose the public domain Lewis and Short Latin Lexicon over the 
more recent, and much admired, <i>Oxford Latin Dictionary</i> (<i>OLD</i>).  When we informally polled ou colleagues and advisors, we found nevertheless that Lewis and Short 
still had a substantial body of supporters &#150; roughly half of those we polled preferred Lewis and Short.  Most of those who preferred Lewis and Short cited the fact that it 
covers a wider range of Latin than the <i>OLD</i>.  Those who argued against Lewis and Short often complained of its dense and unreadable print &#150; a problem that we knew we 
could alleviate in the electronic version.</p>

<p>Ultimately, a private donor allowed us to enter the student version of the Lewis and Short lexicon.  When this goes on-line, it will allow us to replicate the on-going 
experiment available in Greek Perseus, where users can choose between the large and intermediate Greek lexica.</p>
<font face="Helvetica"><p>Latin Morpheus:  analyzing inflected forms</p>
</font><p>Our experience with Greek Perseus taught us early on what could be done with texts in an inflected language such as Latin and Greek.  The large number of endings and 
paradigms may daunt the student but these complexities encode a great deal more information in individual Greek and Latin words than is available in English.  The English sentence 
"time flies like an arrow" &#150; unambiguous to the native speaker who recognizes it as a proverb &#150; can linguistically be analyzed in at least two other ways: 
"time-flies" (like "fruit-flies") enjoy an arrow;  as an exhortation for someone to "time" the flies with a stopwatch.  Such ambiguities are rarer in inflected languages because 
the individual words are themselves less ambiguous.  The greater complexity of Greek and Latin morphology allows us automatically to generate various categories of links.  The 
following three categories of link </p>
<b><p>From inflected word to dictionary entry</b>:  the reader viewing a text can click on inflected forms and see the morphological analysis, base definition and other 
information.  The user sees that an inflected form such as <b>fecerat</b> is the "pluperfect indicative active third person singular" and from the verb <i>facio</i>, "to make, 
construct, etc."  The user can select the "L&amp;S" link to view the full dictionary entry in the Lewis and Short Lexicon.</p>

<p>The morphological information window provides links to two other functions that the morphological analyses make possible.</p>
<b><p>From inflected forms to usage statistics</b>:  once we are able to map inflected forms onto dictionary entries, we can provide usage statistics.  There are limits to this 
strategy.  Some words are lexically ambiguous: i.e., they could come from two different dictionary entries.  We thus provide maximum and minimum frequencies:  the maximum reflects 
the frequncy if every possible form came from the dictionary entry in question;  the minimum describes how many appear if only the unambiguous forms come from the current 
dictionary entry.</p>
<p>More interesting than the raw numbers are the normalized frequencies:  we show users how often a dictionary entry shows up in every 10,000 words.  These statistics often reveal 
preferences of style and content.  In the present case, Caesar uses the verb <i>facio</i> a bit more than the rest of the corpus, though the difference is not particulary great.  
In other cases, the bias is more striking.  Users who click on the "Frequency in other Authors" link will see at a glance that <i>facio</i> is much less common in some authors 
than in others.  Horace and Vergil generally avoid this pedestrian term, whereas Plautus (whose language reflects a much more colloquial level of diction) employs <i>facio </i> 
more than any other author.</p>
<p>Note that the page listing frequencies for a word across all text in Perseus includes a third category besides Maximum and Minimum.  The "weighted" frequency uses a statistic 
method to estimate the real frequency of a term.  Users can thus see the upper and lower bounds as well as a best-guess.</p>

<b><p>From dictionary entry to inflected forms &#150; morphologically sophisticated searching</b>:  The morphological links are bi-directional.  If we can go from an inflected 
form to its dictionary entry, we can also reverse these links, enabling morphologically sensitive searching.  The user can then search on <i>facio</i> and retrieve <i>fecerat</i> 
etc.  </p>
<p>Electronic tools may in the long run revolutionize teaching and learning but they tend to win initial acceptance by addressing long-standing problems. We developed a rule based 
system for the analysis of classical Greek morphology. The system was fairly elaborate &#150; more than 30,000 lines of code &#150; and required a great deal of labor to 
establish but it allowed us to link inflected forms  (e.g., <i>ois&ocirc;</i>) to their possible morphological analyses ("1<SUP>st</SUP> person singular future indicative active") 
and appropriate dictionary entries (<i>pher&ocirc;</i>, "to carry, lift"). Those reading Greek texts, starting with inflected forms, could click on words to determine their form 
and meaning.  Those searching for words could move in the opposite direction, asking for a dictionary entry (e.g., <i>pher&ocirc;</i>) and retrieving its inflected forms, even 
when these forms bore no visible resemblance to the dictionary entries (e.g., <i>ois&ocirc;</i> and <i>&ecirc;nenkon</i> are both forms of <i>pher&ocirc;</i>).  We knew both from 
anecdotal evidence and from the usage of Greek materials on the Perseus WWW site that these tools, made possible by the Greek morphological analyzer, were enormously popular, in 
that they addressed well understood problems that anyone reading and studying Greek understood.  </p>

<p>Our first goal was therefore to create a Latin version of our morphological analysis software.  Our hypothesis had been that classical Greek, with its accents that change 
from form to form, its dialects and its ability to add multiple prefixes to verbs, posed a superset of the problems that we would face in Latin.  We felt justified in addressing 
Greek first in part because it would entail solving a harder problem and that subsequent work on Latin would primarily involve simplifications.  We feared that had we chosen Latin 
first, a Greek morphological analyzer might have required a completely new system.  Few would doubt that Latin is morphologically simpler than Greek, but we could not be sure of 
the extent to which the computational problems of Latin constituted a subset of those in Greek until we actually built a system that managed both.</p>
<p>We discovered that our initial instincts were correct.  It had taken months of work to develop the software system for analyzing Greek.  It required less than two weeks of 
effort to make the same system work reasonably well for Latin.  Most of the effort consisted of relaxing constraints designed for Greek:  many routines devoted to adding or 
checking Greek accentuation, for example, needed to be turned off in the case of Latin.  Latin does not have the various dialects that appear in classical Greek.  Latin words do 
not have accents that shift back and forth over the word according to a various complex rules (with accompanying exceptions).  Nor does Latin add multiple preverbs to the 
beginnings of verbs and the morphological analyzer therefore does not need to anticipate possible forms such as <i>*parepisumba/llomen </i> (from *<i>para/+e)pi/+su/n+ba/llomen</i>).  
The computational mechanics of Latin morphology are essentially a subset of those used in Greek.</p>
<p>Data, rather than software, is the main barrier to the development of a morphological analyzer for Latin.   The system needs to be able to distinguish a noun (such as 
<i>natio</i>)<i> </i> from a verb (such as <i>capio</i>).  The simplest way to resolve this problem is to have a large list of stems so that the morphological analyzer can see 
that "nat-" is a nominal stem with endings in "io, ionis" etc. and "cap" an verbal stem with endings "io, is, etc."  We began by entering the most common (and thus normally the 
most irregular forms) by hand:  the verb "to be" (<i>sum</i>), pronouns etc.  The vast bulk of Latin morphology is, however, quite regular and we developed programs to extract the 
stems from the morphological sections of our on-line Lewis and Short Latin Lexicon.  This process is imperfect &#150; complex morphological information is often coded 
inconsistently and programs cannot anticipate all permutations.  Most of the entries in Lewis and Short (as indeed in any lexicon), however, describe relatively uncommon words 
with very simple morphological information.  We have been able to mine 44,701 nominal and 16,935 verbal stems from the print lexicon.  A series of programs scan the file, looking 
for typical patterns and identifying common morphological forms.  If these programs see "<b>inventor</b>, oris, <i>m.</i>," for example, they recognize that <i>inventor</i> is a 
masculine third declension noun and then break the form up for computational purposes into a stem (<i>invent-</i>) and ending (<i>-or, -oris</i>).</p>

<p>At present, we can automatically generate entries for 98% of the words in widely read authors such as Cicero and Vergil.  Of the remaining 2%, half are proper names, numerals 
or abbreviations (e.g., "M." for "Marcus").  For Plautus, whose language is more archaic and contains many unusual phenomena, the morphological analyzer is, at present, much less 
effective, providing analyses for only 80-85% of the words.  With effort, we can, of course, achieve 100% coverage for any fixed body of materials by adding the missing stems and 
inflections so that we can get analyses for every single word.  Even if we could go through all classical Latin texts in this way, the body of later Latin materials is enormous 
and there will always be unusual words that appear for the first time.  We should nevertheless be able to bring this morphological analyzer to the point where it can analyze &gt; 
99% of the words in any Latin text that models itself on classical Latin.</p>
<font face="Helvetica"><p>Latin Texts and English Translations</p>
</font><p>In our original proposal, we planned to create a small initial anthology of texts, reflecting a wide range of Latin.  After reflecting on the ways that Greek Perseus 
had been used and on what seemed to have made that resource particularly useful, we deviated from our initial plans.  </p>
<p>We chose not to begin with a wide but thin sampling of Latin.  We wanted to pick some period for which we could provide some depth.  We found in Greek Perseus that students 
were using a wider range of texts than was normal in a purely print environment:  authors such as Pausanias or the Attic Orators are widely read. In part, instructors, knowing 
that their students have ready on-line access to these authors, send their students to sources that they might previously have paraphrased or excerpted.  In part, links within 
Perseus direct readers to such background souces.  Students also augment their research by exploring more broadly &#150; all of Plato rather than just just a few dialogues, 
Xenophon, Diodorus and Plutarch as well as Thucydides, etc.  We wanted to create a critcal mass of materials relating to at least one period and for this we chose the first 
century B.C.  While much of the best work in Latin has centered around later periods and gone beyond the "golden age," teachers in the field reminded us in no uncertain terms 
that authors such as Vergil, Cicero and Livy and topics such as the fall of the Republic remained at the core of the secondary school and undergraduate Latin curricula.  And, 
of course, the first century B.C. in many ways laid the key cultural foundations for what followed.  </p>
<p>At present, we have 1.3 million words of Latin, representing works from nine widely read authors: we have the major extant works of Plautus, Suetonius, Vergil, and Catullus;  
besides these we have Caesar's <i>Gallic Wars</i>, Horace's <i>Odes</i>, Cicero's <i>Orations</i> and <i>Letters</i>, the first ten books of Livy and Ovid's <i>Metamorphoses</i>. 
We have entered and are preparing for release the lives of Plutarch not already in Perseus.  There are tentative plans for adding the Greek source text of Plutarch&#146;s lives, 
as well as important sources such as Appian and Dio Cassius.  There are still major gaps &#150; we do not, for example, have Sallust yet &#150; but the corpus that we have 
assembled represents a starting point.</p>

<p>After considerable thought about what source materials to use for the texts and translations, we resolved to avoid rights agreements that would constrain what we could do 
and how we could develop in the future.  We wanted to create a library that we could make freely available to anyone, without provoking fears, however illusory, that print sales 
would suffer. We found that more good public domain translations existed for Latin authors than for Greek and that there were plenty of materials on which to base our work. Our 
goal was not to lay the foundations for a long term effort. Public domain translations which can, over time, be revised or replaced, were therefore the logical starting point.</p>
<font face="Helvetica"><p>Commentaries</p>
</font><p>The traditional commentary instantiates in printed form a powerful version of hypertext and thus fits naturally within any electronic medium.  In Greek Perseus, we had 
focused on collecting as many texts as possible and had very little on-line annotation.  In Roman Perseus, we chose to devote some of our energies to establishing commentaries 
for key texts:  at present, we have entered and formatted John Connigton's <i>Aeneid</i>, Allen and Greenough's <i>Select Orations of Cicero</i> and <i>Caesar's Gallic War</i>, 
E. H. Donklin on Cicero's <i>Pro Roscio</i>, Frank Frost Abbott's <i>Selected Letters of Cicero</i>, Merrill's <i>Catullus</i>. Paul Shorey's <i>Horace: Odes and Epodes</i>.  
All of the preceding commentaries are either now available or in the final stages of preparation.</p>

<p>Our goal was twofold.  First, we wanted to make an initial set of information available to individuals reading through the on-line texts.  Reaction has been positive and the 
notes now available are already of use.  More generally, we wanted to dramatize the notion that commentary could accompany on-line texts.  Electronic publication is the ideal 
medium for the commentator:  any individual can create layered commentaries with different levels of information for different audiences;  moreover, individual notes published 
and linked directly to widely read on-line editions will find a much wider audience than if they were only available as isolated notes in a single scholarly journal.  The author 
will not have to create a monumental edition and commentary to reach the general reader of a classical text and the impact of such individual notes will presumably increase.  At 
present, we are only beginning to accumulate new annotations, but accumulating new notes and commentaries will probably not remain a problem for very long.  The long term 
challenge will more likely be managing a wealth of on-line notes and information. </p>
<font face="Helvetica"><p>Allen and Greenough's Latin Grammar</p>
</font><p>We had entered Smyth's Greek Grammar and found this resource heavily used.  We also noted that the Allen and Greenough commentary on Cicero's speeches contained more 
than 500 cross references to the corresponding Allen and Greenough Latin Grammar.  We were able to raise enough support locally to send Allen and Greenough out for professional 
data entry.  The resulting on-line resource has become one of the most popular elements of Roman Perseus.</p>
<p>Allen and Greenough offered a number of technical challenges, the most significant of which was the challenge of distinguishing Latin from English.  In many texts, most Latin 
is in italics or some distinct typescript.  In Allen and Greenough, no visual cues distinguished the two.  Perseus depends in part upon its ability to distinguish Latin from 
Greek &#150; the overall Perseus Digital Library system indexes all Latin and Greek words, creating links between them and the on-line lexica.  We used the morphological analyzer 
to identify Latin words &#150; if a word failed the English spell-checker and it parsed as a valid Latin word, we assumed it was Latin.  When words (such as "pro") could be Latin 
or English but appeared between definite Latin words, then we assumed that they were part of a larger Latin phrase.  The resulting algorithm, while imperfect, produced useful 
results.</p>
<font face="Helvetica"><p>Roman Art in the Museum of Fine Arts, Boston</p>
</font><p>Maria Daniels, for ten years the photographer and image archivist for the Perseus Project, had taken original photography of Greek art in dozens of museums across Europe 
and North America.  With Roman Perseus we chose rather to concentrate our efforts on the Roman collection of the Museum of Fine Arts, Boston.  When we were collecting 35 mm film 
slides of Greek art for Perseus, we spent up to $40,000/year on film processing.  We were able, with support from Tufts University, to acquire a high end digital camera.  This 
piece of equipment cost us $30,000 at the time (mid 1997) but reduced the post-processing costs and allowed us to collect extensive imagery of museum objects, without regard to 
the expense of film.  </p>
<p>We collected 5,700 original images of 781 coins and 327 other objects, including portrait heads, sarcophagi, full-size statues, bronze statuettes, funerary reliefs, mosaics, 
pottery, glass, jewelry, and architectural fragments.  Al Kaiser, Pamela Russell, Amy C. Smith, and Maria Daniels worked closely with MFA curators John Herrmann and Mary Comstock 
to assemble documentation for these materials.  The first set of MFA materials &#150; the coins &#150; went on-line as part of Perseus in the spring of 2000.  The rest are 
being prepared for release.</p>

<font face="Helvetica"><p>General Site Coverage &#150; Photography</p>
</font><p>We set out to collect a core of images documenting Rome and Italy while beginning a series that began to represent the Roman provinces.  Given the time frame and 
resources at our disposal, we knew that we could not do a thorough survey but we felt that we could create an initial core that could, like the textual materials, grow over 
time.  The following table describes the visual materials already collected as part of Roman Perseus:</p>
<table border cellspacing="1" cellpadding="5" width="442">
<tr><th width="33%" valign="top">
Images</th>
<th width="33%" valign="top">
Location</th>
<th width="33%" valign="top">
Photographer</th>
</tr>

<tr><td width="33%" valign="top">
2963</td>
<td width="33%" valign="top">
Rome</td>
<td width="33%" valign="top">
Maria Daniels</td>
</tr>
<tr><td width="33%" valign="top">
182</td>
<td width="33%" valign="top">
Italy</td>
<td width="33%" valign="top">

Jacqui Carlon</td>
</tr>
<tr><td width="33%" valign="top">
543</td>
<td width="33%" valign="top">
Italy</td>
<td width="33%" valign="top">
Amy Smith</td>
</tr>
<tr><td width="33%" valign="top">
635</td>
<td width="33%" valign="top">

Italy</td>
<td width="33%" valign="top">
Jodi Magness</td>
</tr>
<tr><td width="33%" valign="top">
473</td>
<td width="33%" valign="top">
Spain/Portugal</td>
<td width="33%" valign="top">
Michael Ramage</td>
</tr>
<tr><td width="33%" valign="top">

375</td>
<td width="33%" valign="top">
Croatia</td>
<td width="33%" valign="top">
Maria Daniels</td>
</tr>
<tr><td width="33%" valign="top">
232</td>
<td width="33%" valign="top">
Jordan</td>
<td width="33%" valign="top">
Amy Smith</td>

</tr>
<tr><td width="33%" valign="top">
149</td>
<td width="33%" valign="top">
Spain</td>
<td width="33%" valign="top">
Al Kaiser</td>
</tr>
<tr><td width="33%" valign="top">
1274</td>
<td width="33%" valign="top">
Gaul</td>

<td width="33%" valign="top">
Maria Daniels</td>
</tr>
<tr><td width="33%" valign="top">
2545</td>
<td width="33%" valign="top">
Turkey</td>
<td width="33%" valign="top">
Maria Daniels</td>
</tr>
<tr><td width="33%" valign="top">
75</td>

<td width="33%" valign="top">
North Africa</td>
<td width="33%" valign="top">
Eva Stehle</td>
</tr>
<tr><td width="33%" valign="top">
400</td>
<td width="33%" valign="top">
Gaul</td>
<td width="33%" valign="top">
Amy Smith</td>
</tr>

<tr><td width="33%" valign="top">
55</td>
<td width="33%" valign="top">
Greece</td>
<td width="33%" valign="top">
Amy Smith</td>
</tr>
</table>

<p>&nbsp;</p>
<p>Classicists travel throughout the Roman world each year, collecting thousands of pictures illustrating archaeological sites from the city of Rome itself to remote locations in 
North Africa, Eastern Europe or the middle East.  The resulting slides can be scanned faster and at higher resolution each year.  Each year, digital cameras become cheaper and 
have higher resolution, thus providing an ever growing pool of materials that are already in digital form.  Most people find visiting archaeological sites and collecting pictures 
to be much more attractive than any form of text entry. Digital photographs are much easier to incorporate than slides.  However fast a slide scanner, it still requires labor to 
digitize the slides, make sure that they are not backwards, etc.  Where scholars might be nervous sending irreplaceable slides to a remote project for digitization, sending copies 
on tape or CD-R posed no risks for the originals.  We therefore felt even more strongly that our primary responsibility was to assemble an initial collection of images and then 
make it possible for others to contribute their images to Roman Perseus.</p>
<p>Digital images &#150; however well photographed &#150; are of little use without adequate and consistent cataloguing information.  Basic slide captions are useful but as 
collections grow, the consistency and detail of individual catalogue entries becomes increasingly important.  We already have c. 10,000 images for Roman sites, and that figure 
could easily grow over time to 100,000 or even 1,000,000.</p>

<p>Detailed specifications of what a photograph contains are important but consistency is even more crucial:  authority lists, long familiar to librarians (who specialize in 
integrating disparate pieces of information) tell us whether the official spelling is "Miletus" or "Miletos", and give us precise formulas to resolve ambiguities such as between 
Salamis near Athens and Salamis in Cyprus.  Individual contributors can do a reasonable job of such cataloguing and software can help resolve many inconsistencies and ambiguities, 
but an image archive should have a trained cataloguer to maintain consistency.  Support from NEH allowed us to structure the data that we have.  Maintaining this level of 
consistency presents a challenge over time.</p>
<p>One strategy may allow collections of archaeological (and, indeed, any geographically oriented images) to be managed more efficiently.  Small and relatively inexpensive Global 
Positioning Systems (GPS) have been available for years, allowing individuals to locate themselves to within c. 100 meters.  This is a tremendously useful tool for archaeologists, 
since they can record the location of a picture and then plot that location on a large-scale map.  If one is collecting images of remote remains in Turkey, for example, locating 
the overall site to within 100 meters is quite helpful.  </p>
<p>The 100 meter range gets us to the general area of a site but does not tell us much about where precisely we were in the site.  The precision of GPS data has, however, 
radically increased.  The US government had disrupted its satellite transmissions just enough to maintain the 100 meter precision &#150; the extra imprecision was designed to 
prevent hostile groups from using US GPS data against it.  The US government had planned to remove this disruption in 2002, on the grounds that the technology to get better 
accuracy would be ubiquitous at that point and the disruption irrelevant.  The US government removed the restriction, however, in May 2000, two years earlier than expected.</p>
<p>In local tests with a hand-held GPS, we have achieved accuracy of 5 to 9 feet.  We have also been able to link a GPS to a standard digital camera, so that each picture has 
its GPS data attached to it.  Images that include GPS data of such accuracy can be automatically plotted on site plans.</p>
<p>The consequences of such "geo-referenced" photography are profound.  Someone studying a particular archaeological site could select a rectangle 10 meters square and retrieve 
all photographs taken within that area.  For intensively studied sites (e.g., the Acropolis), this may well swamp the user but for most sites such spatial queries will be 
extremely powerful.  The GPS data thus provides a single, consistent reference scheme.  Whether the photographer has recorded Amyklai or Amyclae, users could locate all images 
representing that site with a map-based interface.</p>
<font face="Helvetica"><p>General Site Coverage &#150; Documentation</p>
</font><p>We commissioned new descriptions of sites and regions for Greek Perseus, but we had neither the time nor the resources to begin with the same approach in Roman Perseus.  
We needed to digitize some existing resource or resources as our starting point.  We were therefore fortunate to receive permission to include the <i>Princeton Encyclopedia of 
Classical Sites</i> (<i>PECS</i>).  Published in 1976 and now out of print, <i>PECS</i> was perfectly suited for conversion.  Its 1.2 million words and 5,000 entries cover the 
breadth of the ancient world, contributing to both the Greek and Roman dimensions of Perseus.  With <i>PECS</i> on-line, users can get a quick overview for virtually every major 
site in the ancient Greco-Roman world. We therefore used the more advanced OCR software that we acquired in early 1999 to enter <i>PECS</i>.  <i>PECS</i> was published as part of 
the Perseus Digital Library in May, 1999.</p>

<i><p>PECS</i> was published a quarter of a century ago, and its coverage thus does not include a generation of findings.  There are no active plans to create a systematic new 
version of <i>PECS</i>.  A new print edition of <i>PECS</i> would be a massive job, since each entry should ideally be revised before a complete publication is released.  In 
an electronic environment, new entries can be added incrementally.  We at Perseus have no plans to revise existing <i>PECS</i> articles &#150; our goal is simply to make those 
entries accessible as an integrated part of the overall Perseus Digital Library.  New articles on Greco-Roman sites, published as part of the Perseus Digital Library, would, 
however, automatically become part of the overall system.  The user clicking on "Ostia" would see not only the <i>PECS</i> article but all documents in Perseus that include Ostia 
in their title.  As the size of the Perseus digital library increases, we will need to structure this list more precisely, distinguishing documents that are surveys of Ostia from 
those that refer to some particular aspect of Ostia.</p>

<font face="Helvetica"><p>Intensive Visual Documentation of One Site:  Ostia</p>
</font><p>In Greek Perseus, we had been able to create hundreds of site plans, in some cases linking several phase plans together so that users could view the development of the 
site sequentially over time.  Such coverage was not generally feasible for the first phase of Roman Perseus, but we decided to choose one site that was manageable,  but complex 
in form and central in importance.  Ultimately, we to create a virtual tour for the Roman port city of Ostia, a well-studied and excavated site.  Smaller than Pompeii and 
Herculaneum, Ostia is nevertheless a very large and complex site and provides a crucial window onto Roman social and economic history.</p>
<p>We could not comprehensively document the site but we could create a framework to which documentation could be added over time.  Maria Daniels, then the Perseus Visual 
Collections Curator, collected 10,000 digital images of the site. Some were conventional photographs but many form the raw materials for Apple Quicktime VR &quot;panorama 
nodes&quot;:  users will be able to choose 105 QTVR panoramas on the map of Ostia.  These panoramas will allow users to interactively turn their perspective 360 degrees, 
providing a digital analogue to the user standing in one place and looking around in a circle.</p>
<p>At present, Genevieve Gessert, a research fellow at Yale University who specializes on Ostia, is preparing single sentence captions for each image that explain the monuments 
(or details thereof) visible in each image, the view shown, and any details of material, style, technique or iconography.  The result will be similar to Greek sites in Perseus 
but with a greater concentration of images and addition of QTVR.  Where the wide variety of sites covered in Greek Perseus provide breadth, Ostia in Roman Perseus is designed to 
provide an example of depth in visual documentation.</p>
<font face="Helvetica"><p>Written Documentation for Rome: Platner and Ashby</p>
</font><p>Lawrence Richardson's <i>Topographic Dictionary of Rome </i>(1992)<i> </i>is the most up-to-date survey of the city.  We have obtained the author&#146;s permission and 
have requested permissions from the publisher to include it as part of the Perseus Digital Library.  In the meantime, we have entered and are refining Samuel Platner and Thomas 
Ashby's 1929 <i>Topographical History of Ancient Rome</i>.  Its two thousand entries cover a wide range of topics.   Its coverage reflects the archaeological findings from the 
first quarter of the century but it remains an extremely useful compendium, with brief descriptions for many parts of the city and references to the most important ancient 
literary sources.  Since we regularly transform textual citations into on-line links, these latter will prove increasingly useful as more Latin source texts and translations 
become generally available on-line.</p>

<font face="Helvetica"><p>Geographic Data</p>
</font><p>Neel Smith, Nicholas Cahill and Robert  Chavez have, among others, worked on the Perseus Atlas for years.  For Greco-Roman Perseus, we have been able to collect the 
coordinates for c. 3,000 ancient sites. These were stored in a Geographic Information System and can be plotted on any georeferenced digital maps.  Two issues constrain the use 
of our current and expanding geographic data.  </p>
<p>The precision of geographic coordinates is always limited. One second of latitude describes c. one mile at the equator.  Gazzetteers that include degrees and minutes for 
longitude and latitudes of ancient sites in the Mediterranean are normally accurate within roughly 1 km.  Handheld GPS units yielded coordinates accurate to within 100 m. 
until May 2000, when, as noted above, the US government increased the accuracy of the data and made it relatively easy for handheld GPS units to generate coordinates accurate 
to within less than 10 m.  With more specialized equipment, coordinates accurate to within 1 cm can be collected.  The more accurate the data, the more uses to which it can be 
put.  The weaknesses of coarse data appear as users zoom into higher resolution &#150; one can see this error by looking at coastal sites at high resolution, since the 
individual point will often appear just off-shore.  At this stage of development, however, coarse points accurate to c. 1 km are extremely valuable, since our first goal is 
locate as many major sites as possible on a small scale map.</p>
<p>As of May 2000, more than 90% of the point data in the Perseus Atlas comes from various gazetteers from sources such as the USGS and the National Imagery and Mapping Agency 
(NIMA).  These points are, for the most part, accurate to within 1 km.  A small but growing body of points are derived from GPS readings. In collaboration with the Stoa publishing 
consortium, we are actively soliciting more such point data from those visiting sites.</p>
<p>Consistent and flexible naming systems are the second factor that constrain the current Perseus Atlas.  We are currently preparing to integrate the comprehensive Getty 
<i>Thesaurus of Geographic Names</i> (<i>TGN</i>) into Perseus.  This includes more than a million places &#150; and coordinates &#150; world-wide.  The <i>TGN</i> provides basic 
coordinates for many of the sites relevant to a Roman Perseus and thus has data necessary to lay the foundation for an electronic atlas of the Greco-Roman world as a whole.  
Managing alternate names (e.g., Corinth or Korinthos) proves to be a non-trivial problem and we have been addressing it, extending the principles of the "Registry of Ancient
 Geographic Entities" proposed by Neel Smith.  We need thus to augment the TGN data, coordinating the ancient and modern place names.  PECS contains modern equivalents for almost 
 1,500 sites and this provides us with a starting point, but covers less than one third of the 5,000 articles.</p>

<p>Geographic data is crucial not only to the Perseus Digital Library but also to any library of historical or cultural materials.  Our goal is to collaborate with various groups 
ranging from the globally oriented Alexandria Digital Library based at the University of California at Santa Barbara to the Classical Atlas Project at the University of North 
Carolina to individual surveys and excavations.  The long-term goal is to generate an open-ended database of geographic data for the ancient world.</p>
<font face="Helvetica"><p>Bridging Gaps:  Shakespeare's Roman Plays</p>
</font><p>Roman culture has been our focus during this phase, but our long-term goal in Perseus is always to expand the audience for classics, and indeed for every subject on 
which we work.  We know from Greek Perseus that the library of sources and studies on Roman culture would reach a wider audience than conventional print publication &#150; the 
Web reaches far beyond the network within which printed academic publications circulate.  In the spring of 2000, traffic at the main Perseus site approahed 300,000 page 
impressions in peak twenty-four hour periods, with only a minority of the traffic coming from explicitly academic domains (e.g., *.edu, *.ac.uk).  Roman history has the added 
advantage that it has inspired a wide range of literary works in various European languages.  Shakespeare's Roman plays provide a particularly important bridge between English 
literature and the Roman world.  Julius Caesar has, in particular, been a traditional mainstay of secondary school curricula in the English speaking world.  Given our focus on 
the first century B.C., we decided to develop materials relating to the three Roman plays based upon Plutarch (<i>Julius Caesar</i>, <i>Antony and Cleopatra</i>, and 
<i>Coriolanus</i>) as well as to the outrageous <i>Titus Andronicus</i>.</p>
<p>We entered a modest set of interlocking resources on these plays.  For <i>Titus Andronicus</i>, we have merely entered the text of the play and its probable main source, the 
<i>Prose History of Titus Andronicus.</i>  For the Roman plays as a whole we have entered Skeat's edition of the relevant lives of North's Plutarch as well as M. W. 
MacCallum's<i> </i>fundamental survey, Shakespeare's <i>Roman Plays</i>.  For <i>Coriolanus</i>, we have entered the text and the Furness New Variorum Shakespeare edition.  
For <i>Julius Caesar</i> and <i>Antony and Cleopatra</i>, we have done substantially more.  A series of commentaries on these latter plays will be entered and placed on-line.</p>

<p>As part of our general Digital Library work, we plan to create an electronic version of Shakespeare's work as a whole, but the Roman plays will stand as a well-defined and 
well-developed subset of the whole.  All the texts will be hyperlinked together, with citations converted to links, textual notes and commentaries integrated with text, and 
bi-directional links between Shakespeare and various sources for Roman history, including, but not limited to, the texts which Shakespeare probably used as sources for his 
plays.</p>
<p>The Roman plays in Perseus will, we hope, attract students of English deeper into the literature, history and art of the Roman world, while encouraging classicists to explore 
the ways in which subsequent writers imagined the Romans.  </p>
<p>Traditionally, those reading the Roman plays of Shakespeare have had little direct contact with the Roman sources.  To some extent, this reflects the backgrounds of 
Shakespeareans and classicists, but purely formal choices reinforce the relative separation of the two traditions of scholarship.  Neither Skeat's edition of North's Plutarch 
nor the authoritative transcription of the <i>Life of Antony</i> in Spevack's New Variorum Shakespeare edition links North's Plutarch to the standard chapter breaks by which 
classical scholars cite Plutarch.  Thus, if a classics publication cites chapter 67 of the <i>Life of Antony</i>, for example, there is no easy way for the English professor to 
relate that section of text to Skeat or Spevack.  Such intellectually minor logistical barriers have a substantial influence upon scholarly practice.  We have added the classical 
citation scheme to our electronic edition of Skeat, making it feasible for readers to move between Skeat and the classical scholarship on Plutarch.  In an electronic environment, 
where citations can become links, the effects will become more substantial as increasing bodies of scholarship become available on-line the links from Skeat to classical 
scholarship grow richer.  In the long run, we hope to see more scholarship linking the classical and early modern periods.  In the shorter run, we hope to see students at various 
levels using the Perseus Digital Library to compare Shakespeare's Rome with which we find in our historical sources.</p>
<font face="Helvetica"><p>Overall Integration &#150; Harper's Classical Dictionary</p>
</font><p>The <i>Princeton Encyclopedia of Classical Sites</i> and Platner - Ashby's Topographical Dictionary of Rome allow users of the Perseus Digital Library to call up 
summary information about thousand of places.  In Greek Perseus, we mined discursive indices for basic information identifying people and places.  The Oxford Classical Dictionary, 
third edition (), would be an ideal on-line resource to provide more general information but we had decided that we would, at this early stage of development, work with 
unrestricted materials.  A great deal of public domain encyclopedic information exists.  For the purposes of the Perseus Digital Library, the basic information identifying people, 
places and things has not changed.  Since we have PECS already on-line, readers have access to more up-to-date surveys of archaeological data.  Outside of archaeology, the basic 
information as to people, places and things has remained fairly stable.</p>

<p>Two major possibilities presented themselves.  The massive Smith's encyclopedias cover topics such as daily life, biographies of mythological and historical figures, and 
geography.  As a single on-line, integrated resource, these would be of immense value, but they are too large for us at this point.  They could not be entered effectively with 
OCR software but the data entry bill alone would be more than $50,000.  We hope to receive support to enter them on-line in the near future, but we cannot manage them yet.</p>
<p>We chose therefore to enter Harper's Classical Dictionary, a substantial work with more than 11,000 entries and one which maintains a loyal following (in one recent thread on 
a discussion list for Latin instruction, for example, teachers discussed ways in which to acquire copies of this out of print work).   Professor Bernie Frischer at UCLA had run 
Harper's through OCR software in the late 1980s, but the output was rough and not suitable for publication.  Even the state-of-the-art software at our disposal more than ten 
years later was, in our opinion, inadequate.  We were just able to muster the resources to send Harper's out for professional data entry.</p>
<p>Harper's will strengthen the Greco-Roman collection of Perseus in two ways.  First, it will provide a great deal more general information.  Harper's contains entries on topics 
for which we could offer no information &#150; especially, for the post-classical Greek and overall Roman worlds, which both lay beyond the initial focus of Greek Perseus.  And 
even when we had entries, most of the Perseus "encyclopedia" articles consist of single sentences or brief identifiers.  </p>
<p>Second, the greater detail and relatively consistent format of Harper's means that we can extract information about the various entries. With an encyclopedia that includes 
many very precise dates (e.g., the British <i>Dictionary of National Biography</i>), we can extract most birth and death dates.  An intelligent digital library system could use 
that information to suggest that the Oliver Cromwell who appeared in a 19<SUP>th</SUP> century context might be the biographer "(1742?-1821)" rather than his much more famous 
namesake with dates "(1599-1658)".  We have no precise knowledge about the birth and death dates of many, if not most, figures from the ancient world and few entries in Harper's 
contain birth and death dates that a program could readily extract.  Such chronological information can be extremely valuable &#150; since we know, for example, that Herodotus' 
Histories were composed well before the birth of Alexander the Great, a digital library system could automatically infer that the Macedonian king Alexander who urges that 
Athenians to collaborate with Persia at Hdt. 8.140 is not Alexander the Great.  </p>

<p>Nevertheless, the Harper's entries contain a great deal of useful information.  Greek names generally appear both in Roman transliteration and in Greek &#150; since LSJ has 
only a small number of proper names, we can use Harper's to increase about ability to recognize and morphologically analyze Greek names.  The length of an entry and the 
"importance" of a figure are related &#150; thus the Cleopatra with the longest entry is the Egyptian Queen associated with Marc Antony (Cleopatra number 7 in Harper's) is also 
the longest.  Furthermore, even though we cannot generally extract precise dates, we can analyze the general dates cited in an entry (e.g., "5ith century") and thus develop 
chronological profiles for the person or thing described. Such simple measures prove very useful when helping determine to which Cleopatra a particular passage refers.</p>
<b><i><font face="Helvetica"><p>Conclusions</p>
</b></i></font><p>The phrase "Greco-Roman" exists because Roman culture evolved with very close ties to Greek antecedents.  The Greeks, subjugated politically and often exploited 
economically, colonized the minds and indeed the culture of their conquerors.  The Roman digital library not only draws strength from, but in some measure demands as its 
prerequisite, a preexisting digital library on Greece.  Roman Perseus was the logical next step for us after nearly a decade focused exclusively on Greece.  Nevertheless, though 
the two datasets overlap, Roman Perseus had very different needs.  Latin is, like Greek, a highly inflected language and the language specific aspects of Roman culture are crucial 
to any information system aimed at the study of Rome.  At the moment, the following conclusions suggest themselves to us.</p>
<p>First, while the World Wide Web allows almost anyone to publish, isolated web publications are not ideal, for the fragmented web is less than the sum of its parts.  Most of us 
recognize the value of peer review, but even the best content stands at a disadvantage if it is not published in a standardized form.  A Latin text, for example, that is published 
as a part of a larger system (such as that which we developed for Roman Perseus) is automatically linked to other resources (e.g., a Latin lexicon, morphologically clever searches 
etc.).  Whatever the long-term role of the Perseus Digital Library, we need some centralizing entities that can help structure and maintain web resources over the long run.  In 
recognition of this need, we have formed a partnership with the <i>Stoa Publishing Consortium</i> (http://www.stoa.org).</p>
<p>Second, its by no means clear what group or groups should help organize electronic publications in classics (and in virtually every academic discipline).  The roles of author, 
publisher, librarian and reader are now being redefined &#150; and, in some cases, challenged.  Some functions are now, for all practical purposes, free:  a single machine can 
distribute thousands, if not millions, copies of a document each day.  Other functions remain expensive:  creating well-structured electronic publications is expensive, even if 
government grants or volunteer labor conceals the costs.  We do not yet have a viable economic model to expand and maintain electronic resources in classics or in any other field.  
Whatever model does emerge, classics is not large enough to go its own way. We will need to collaborate with other disciplines to develop a shared infrastructure.  The need to 
collaborate helped us decide to begin expanding the Perseus Digital Library beyond classics.</p>

<p>Third, whatever the challenges that we face, we are already able to make progress.  The Roman materials in the Perseus Digital Library are, and will remain, a work in progress.  
We have only made a small contribution to a much larger process, but, much as we need to do, we can already do a great deal now.  </p>
<b><i><font face="Helvetica"><p>Appendix:  Getting Texts and Translations on-line</p>
</b></i></font><p>Ideally, texts are sent out for professional data entry &#150; this provides the most reliable method for building a digital collection from paper source 
materials.  Not having the resources for this approach, we needed to build up the texts in Roman Perseus with OCR software.  A number of individuals have devoted &#150; and 
continue to devote &#150; a great deal of effort to scanning texts for use on-line.  Our mixed experiences in applying OCR technology are worth describing because they reflect 
the problems that many individuals and groups face as they attempt to enhance the on-line resources.</p>
<p>One strategy uses OCR software in an efficient and strategic way as part of "image front" systems.  Some digital libraries display images of the original source texts but let 
the user search the results of OCR software.  The OCR may contain numerous errors but most of the words are accurately transcribed and the resulting text is well-suited to 
searching.  System designers can thus enter large bodies of materials with little manual labor.  By our estimates, image front data entry costs c. one tenth as much as fully 
tagged professionally entered text.</p>
<p>In Perseus, however, we work intensively with the highly structured full text.  When readers view a Latin text in Perseus, for example, they expect to see links from each 
inflected form to the dictionary.  We could not therefore use the image-front technique if we were to extend the intensively hyperlinked Perseus model to Latin.</p>
<p>In developing Greek Perseus, we were able to use OCR software for some work:  Kenneth Morrell, then at St Olaf's College, supervised a team of students who entered a large 
number of modern and thus cleanly printed English translations for Greek Perseus, but with Roman Perseus we lacked the funding to support even such extensive student labor.  
Nevertheless, we chose to push ahead with OCR and to do the best that we could.  In the end, virtually all of our Roman Perseus textual data entry was done by volunteers and 
project members on their own time.  We accumulated more than two million words of Latin source texts and English translations representing an initial set of ten authors.  The 
process of accumulating these texts was, however, not painless nor were the results always what we had hoped.</p>

<p>No method of data entry is perfect but the 99.95% accuracy guaranteed by most professional data entry firms is a reasonable standard for most work.  OCR software is most 
cost-effective when one can achieve this level of accuracy by applying a simple spell-checker to the OCR output.  If the OCR software produces results that are so rough that 
they need to be proof-read, then the cost saving over professional data entry are marginal at best and the quality much less reliable.  Like many individuals contributing 
on-line texts, we found that we had little or no hard cash to pay for textual data entry and needed to use instead our time instead.  When we totaled up the hours we spent and 
the salary that those hours were worth, the OCR process was often no cheaper &#150; and indeed in many cases more expensive &#150; than professional data entry.</p>
<p>Latin source texts were often on good quality paper and clear print, but the demands for accuracy are even higher for Latin than for English.  The English reader can normally 
negotiate occasional OCR errors but a student struggling to translate a passage needs to be confident that the text is sound.  We did not have access to a Latin spell-checker, but 
our Latin morphological analysis system, however, helped identify misspelled words.  We were also able to cross-check our Latin texts against those available on the PHI CD ROM.  
Nevertheless, the process was not smooth.  Chapter and section breaks needed to be entered manually &#150; most texts mark section breaks in the margin and do not identify where 
in the line the break takes place.  Since many lines have more than one full stop, automated methods cannot always identify which full stop is the section break.  More importantly, 
marginal numbers confuse the OCR software, since they are often on a slightly different line than the adjacent text.  Often, numbers such as "10" are identified as "IO" 
(a reasonable analysis, given that many fonts do not distinguish between "1" and "I").  Some errors are especially insidious &#150; the marginal number "15" is often scanned as 
"is" &#150; a very common valid Latin word &#150; and thus phantom instances of the third person pronoun can be added to a text.  The Latin texts of Cicero already entered (mainly 
the orations and the letters) contain almost  800 Greek words, phrases and quotations, all of which had to be entered by hand.</p>
<p>Our emphasis on public domain materials presented particular technical problems for OCR software. However good the content, the physical editions themselves were often 
problematic as source material.  The stereotype printing that made 19<SUP>th</SUP> century books inexpensive vehicles for disseminating texts is poor in quality, with many 
damaged letters.  The editor in chief of the Perseus Project personally scanned, OCR'd, spell-checked and formatted Latin source texts and English translations for all of 
Cicero's orations and letters, but the results were uneven.  The Latin editions were in clean type and on good paper, and we could cross-check the editions that we scanned 
against texts available on the PHI Latin CD ROM, but the English translations were rough.  E. B. Shuckburgh's four volume translation of Cicero's letters required a great 
deal of labor to tag.  Four hundred Greek quotations needed to be identified and entered by hand.  More significantly, Shuckburgh includes 5,500 notes (the number and quality of 
the notes were, in fact, one of the main attractions of Shuckburgh's work), but OCR software has little success identifying superscript numbers marking these notes.  Most of the 
11,000 note markers in the text and note headers on the bottom of the page had to be identified by hand.  Likewise, the OCR software produced at best indifferent resuls for small 
print and especially the italics common in notes.  Marginalia in the introductions confused the OCR software and needed to be fixed by hand. Overall, the Shuckburgh letters 
required a great deal of labor and surely cost more in time than the price of data entry.</p>

<p>Difficult as Shuckburgh's letters proved, the basic text lent itself to reasonably accurate OCR.  C. D. Yonge's translation of Cicero's Orations appeared in the extensive, but 
cheaply produced, "Bohn's Library."  The uneven and often broken stereotyped print produced marginally useful results.  We spent a great deal of labor spell-checking and 
perfecting the texts, but the results were not what we had hoped.  We have at present manually read through just over one half of the orations.</p>
<p>From 1997 through early 1999, we worked with off-the-shelf commercial OCR software &#150; with only modest differences between those packages that we evaluated.  In early 
1999, we identified a much more powerful &#150; and vastly more expensive &#150; system.  Where the consumer packages cost c. $100 and were available in any computer stores, 
the high end system came from a small company and cost c. $30,000.  After we had received our new support from the Digital Library Initiative and after extensive evaluation, we 
invested in this system.  In the best case (running text with no notes and clear print), we can generate in a few hours and with 99.95% accuracy fully formatted text that would 
have cost $1,000 from a professional data entry firm.  Some texts have so much formatting that they still need to be sent out for professional data entry, but investing in this 
package radically enhanced what we could do with OCR software.  We acquired this package too late to help with the initial development of Roman Perseus, but it will, we hope, 
allow us to fill in some of the gaps over time.</p>
<p>Our conclusions are that standard OCR software is adequate for those willing to spend a fair amount of time entering cleanly printed, lightly formatted source texts.  Given 
enough labor, anyone can convert a document from print to electronic form, but that labor can become far greater than most anticipate.  If one plans in any event to go over a 
text word by word &#150; an eminently feasible proposition if one is dealing with a life of Suetonius or one book of Livy &#150; then cleaning up numerous errors is not a huge 
burden.   If one wants to enter a large body of material quickly, OCR is problematic. Those planning major data entry projects should devote as many resources as possible to 
professional data entry, using OCR as little as possible.</p>

<p>Getting paper commentaries on-line requires a great deal of tedious work.  With the exception of Conington, all the commentaries were entered with the older OCR software and 
needed extensive hand formatting and attention.  Even commentaries printed cleanly on clear paper prove to be difficult to enter.  Conventional OCR software has difficulty with 
ligatures and italic texts and very ineffective when the two are combined.  The format of commentaries is precise and important &#150; bold often delimits the beginning and end 
of the quoted text on which a note comments but OCR software cannot reliably detect bold.  We had to remark the lemmas by hand &#150; a task which we could only, at best, 
automate.  Of the three categories of text that we collected with OCR &#150; Latin source texts, English translation, and commentaries &#150; the commentaries required the most 
work.  The elaborate OCR software that we acquired after most of the text entry for Roman Perseus had been completed ameliorated the situation, but still left a great deal of 
formatting to be done by hand.</p>


		</div> <!-- Publications div -->	
	</div> <!-- main_col div -->
        
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
