<html>
<head><title>Web Delivery of High-Resolution Images from the Museum of Fine Arts, Boston</title>
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
<center>
<p>
Paper written for the session, "Making Data Work: Functional Design Strategies"<br> at <a href="http://www.archimuse.com/mw2000/">Museums and the Web 2000</a></p>
<p>April 17-19, 2000, Minneapolis, Minnesota<br>(Organized by Archives & Museum Informatics.)</p>

<p>
Maria Daniels, Perseus Project, Tufts University<br>
Send me your <a href="mailto:maria@perseus.tufts.edu">
comments</a> on this paper!<br>
©2000 Archives & Museum Informatics. All Rights Reserved.</p></center>
<hr size="4" width="95%" align="center">
<center><h3>Is Bigger Better? Web Delivery of High-Resolution Images 
	from the Museum of Fine Arts, Boston</h3>
<p><b>Abstract</b></p></center>

<blockquote> 
<p>At the turn 
	  of the twenty-first century, the first stage of museum digitization 
	  is now well underway. Many museums have initiated and made headway on 
	  projects to put their holdings on-line, package their collections management 
	  databases into Web sites, and produce curatorial content for a worldwide, 
	  on-line audience. As institutions move into the second phase of these 
	  developments, which emerging technologies show the most promise for 
	  museums and their audiences? Which principles should form the core of 
	  a museum's digitization strategy over the long term? How will improved 
	  access to museum collections affect research possibilities for a broad 
	  range of people, both in academia and among the general public?</p>
	<p>Working together since 1997 
	  to digitize an important collection of Roman art, the Perseus Project 
	  and the Museum of Fine Arts, Boston, have addressed these and other 
	  questions. This paper will focus on the development of a new archive 
	  of photographs, and discuss the decision-making and technical processes 
	  behind delivering high-resolution images of museum objects via the Web. 
	  Copyright protection, storage and delivery of many large files, interface, 
	  the viewer's experience, curatorial concerns, and other issues have 
	  been important considerations. Underlying our efforts has been the belief 
	  that, in the new environment of networked access to museum resources, 
	  museums have an opportunity to transform education.</p>
      </blockquote>
      <p>&nbsp;</p>
      <p>Perhaps one of the most frustrating things about 
	working on the content side of a digital library is knowing how many resources 
	in the library are not yet published and available to the world. Like 
	a museum curator with limited gallery space and a storeroom full of artworks, 
	a digital librarian is always thinking of the day, some time in the future, 
	when the unknown parts of the library will have their turn in the limelight. 
	This is why we are particularly happy to be able to present the work on 
	high-resolution images of Roman coins which the Perseus Project (<a href="http://www.perseus.tufts.edu">http://www.perseus.tufts.edu</a>) 
	has carried out in collaboration with the Museum of Fine Arts, Boston 
	(<a href="http://www.mfa.org">http://www.mfa.org</a>), with its curatorial 
	staff in Art of the Ancient World, John Herrmann, Mary Comstock, and Pam 
	Russell, and the museum's Director of Information Resources, Nancy Allen. 
	This collaborative effort with the MFA forms part of the continuing research 
	and development at the Perseus Project, an effort now in its thirteenth 
	year, at Tufts University. Our work represents another major step toward 
	the electronic delivery of scholarly-level museum resources in a permanent, 
	standardized, flexible way.</p>

      <p>The limitations of electronic resources, especially 
	for scholarly use, are well known to all of us; I outlined some of them 
	in another Museums and the Web conference paper (Daniels, 1997). At that 
	Los Angeles session, Prof. Charles Rhyne of Reed College made explicit 
	the particular problems of teaching art history using inadequate visual 
	materials, and stressed, as he has in many of his publications, the necessity 
	for scrutinizing technical details, such as brush marks, or small parts 
	of an object, like the dozens of tiny figures on a 12-foot-long Japanese 
	screen (Rhyne, 1997). We agreed that scholars, students, and teachers 
	would not truly be able to take advantage of the electronic environment 
	for their work until more, higher-resolution images were made available. 
	At Perseus, we had always concentrated on the <i>quantity</i> of images 
	we included, up to 109 detailed views of a <a href="/hopper/artifact?name=Harvard+1960.367&object=Vase">single 
	Greek vase</a> . However, in the back of my mind was the annoying knowledge 
	that the Perseus Project did, in fact, have an archive of scanned and 
	digital camera photographs at a much higher resolution than the minimal, 
	640x480 pictures we freely published on our web site. What was impeding 
	the distribution of images of a significantly higher quality? Could these 
	high-resolution pictures be the analytical tools our users still lacked?</p>
<p>Later that year, as we embarked on the Perseus-MFA 
	collaboration that continues today, the question of delivering higher-resolution 
	pictures was foremost. Work over the past two years has culminated in 
	the electronic publication of a major catalogue of Roman art, including 
	780 Roman coins and medallions, with a series of Perseus enhancements 
	including a tool enabling viewers to zoom in on 18-megabyte photographs, 
	examining the coins at up to 25 times their actual size. This paper will 
	begin by discussing the principles which guided us, then continue with 
	a short description of our working methods. Finally, it will venture to 
	suggest some of the earliest results of this work. The question of the 
	title, &quot;Is Bigger Better?&quot; is really a question for you, the 
	audience of museum professionals, art educators, and everyone interested 
	in electronic access to cultural resources. How can these big images be 
	integrated into an electronic museum experience, and what potential do 
	they hold for the transformation of museum collections into complete educational 
	resources?</p>
<p>As Skidmore &amp; Dowie (1999) point out, educators 
	implementing technology do best to begin by articulating the educational 
	objectives which need to be served. At the outset of our work, members 
	of the team, including curators, museum educators, technical staff, and 
	university faculty, worked together to clarify the educational goals of 
	this project, and, further, to express a set of principles which would 
	apply not only to the instructional uses of the data, but also to its 
	longevity and utility over time. It was particularly important that the 
	collaborators started with a discussion of the project's objectives; otherwise, 
	differences between the museum and the university perspectives might have 
	led to difficulties, had we failed to articulate our individual interests 
	and work together to integrate them into a single approach at the outset.</p>

<p>The first, and most important, principle guiding 
	our work with the MFA coins was the understanding that these objects are 
	transformative. A coin portrait of a Roman emperor like Trajan, a depiction 
	of the Mausoleum of Hadrian, a personification of Gaul being speared by 
	a Roman warrior, issued during Caesar's conquest of Gaul -- all of these 
	representations have the power to change the way people understand the 
	ancient world. Yet I am not making a special argument for Roman coins, 
	even though coins are among the hardest to view in a gallery setting, 
	the most challenging for curators and educators to label and explain, 
	and the easiest to overlook when they are surrounded by a building full 
	of larger, more emphatic art works. At the outset of our collaboration, 
	we agreed that the experience of seeing a Roman coin could be enlightening, 
	and we therefore included coins in our plans to digitize the Roman collection. 
	We also included glass, pottery, mosaics, jewelry, portable altars, mirrors, 
	statues, tomb reliefs, clothes pins, pieces of buildings, and other objects. 
	Our approach, in short, was that if an object is worth collecting, it 
	is worth digitizing. This principle was tested by the usual limitations 
	of time and money, of course, and we used a few broad themes, such as 
	daily life and Roman gods, to help direct our selection of objects. However, 
	we avoided a &quot;greatest hits&quot;, &quot;sculpture only&quot;, or 
	other such limiting approach to the collection, as much as possible, opting 
	instead to represent as wide and as deep a sample as we could. The work 
	presented here on coins is being extended to all 1,100 of the art works 
	we have documented thus far.</p>
<p>A second principle was that of integration, not 
	only of the different sources of information within the museum, but also 
	of the newly digitized material with existing resources at the MFA and 
	in the Perseus digital library. Written documentation for the Roman coins 
	came from extensive curatorial records which had not yet been added to 
	the museum's collections management database, and the museum insisted 
	that this digitization project, and all others, should produce data useful 
	for their main database. On the Perseus side, we were hoping to find added 
	value for the new objects by placing them in context with the other holdings 
	in the Perseus digital library, including photographs and descriptions 
	of Roman sites, an atlas of the ancient world, and the texts of Cicero, 
	Caesar, Josephus, and other authors. One other consideration was the integration 
	of several existing publications of the art works, where possible, in 
	order to avoid reduplication of efforts, and to bring new users to the 
	scholarship already in publication. The only exception to this integrative 
	approach was the creation of new photographs, which were deemed necessary 
	in order to provide consistent coverage of the collection. Many objects 
	had never been photographed in color, or never had detail photographs 
	made; the coins themselves were all photographed with a centimeter scale, 
	in order to convey their relative size.</p>
<p>A third underlying principle was one which hardly 
	needs elaboration here: control of ownership. Everyone involved agreed 
	that protecting these new resources, by asserting copyright, watermarking 
	data, and attaching ownership and credit information to the photographs, 
	was important. From the start, our goal was to publish the catalog of 
	coins on the World Wide Web, and make it freely available to the general 
	public. However, we chose to implement several safeguards, to ensure that 
	the pictures would not be separated from their descriptive data. In the 
	usual Perseus collaborative arrangement, the MFA retained ownership of 
	the data we produced, in exchange for allowing it to be published freely 
	on the Web. Thus, we had an obligation to protect the data, even as we 
	sought for it a wide general audience.</p>
<p>Several more principles guiding us in this work 
	related to the data collection process in a more technical way. First, 
	we determined to adopt and conform to existing standards, for the data 
	itself and for its structuring, as much as possible. Controlled vocabularies, 
	standardized spellings, TEI-conformant SGML tagging, standard image file 
	formats and processing, and compatible database structures at the MFA 
	and at Perseus all played roles in making our data coherent, and in allowing 
	this work to scale successfully. The curators invested their time and 
	expertise in determining whether the quality of existing documentation 
	was adequate, and, if not, creating new catalog information that would 
	meet their standard. Time spent building tables of credit lines for objects, 
	compiling alternate names for the same representation (e.g. Castor and 
	Pollux, <a href="/hopper/artifactSearch?q=dioscuri&artifact=yes&image=yes">the 
	Dioscuri</a>, marking up bibliographic references, and employing standard 
	vocabulary sources, like the <a href="http://shiva.pub.getty.edu/aat_browser/">Getty 
	Art &amp; Architecture Thesaurus</a>, contributed to the robustness of 
	the data, and ultimately saved some effort in the processes of data entry 
	and production for the web.</p>

<p>It is worth noting that in some cases, standard 
	classification systems have not yet developed to include the highly specialized 
	data sets relevant to our work; as J&ouml;rgensen (1999) indicates in 
	her thorough survey of image retrieval systems, it is still a struggle 
	to understand how people search for images, let alone to build adequate 
	generalized systems to aid those searches. An example is the <a href="http://shiva.pub.getty.edu/tgn_browser/">Getty 
	Thesaurus of Geographic Names</a>, which does not yet include all of the 
	geographic data from the Mediterranean which would be useful in classifying 
	the assorted geographic attributes of the art objects, including their 
	findspots, depictions of places, inscriptions naming places, or the sources 
	of the objects' raw materials. To address this problem, we have sought 
	help from classification schemes of other specialized sources, including 
	field-specific publications and other archives of ancient art, and in 
	some cases developed structures ourselves, based on the Perseus-specific 
	content. </p>
<p>A second, critically important technical principle 
	was longevity of data. One of the paradoxes of digitizing two-millennium-old 
	art works was the knowledge that they themselves were likely to be legible 
	long after our databases and our digital cameras crumbled into dust, if 
	we failed to make the data portable. While the Web is the immediate delivery 
	mechanism for our work, all of the data is structured so that it can be 
	republished in emerging formats for years to come. The Perseus Project 
	has a long experience in developing this type of portable data; for almost 
	a decade, the project developed back-end data in powerful relational databases 
	and structured SGML, but delivered the material on CD-ROMs in a low-cost, 
	widely available HyperCard front end. With the advent of the Web, we were 
	able to take this same data and port it to a Web front end within a matter 
	of weeks. We have also successfully built delivery software for the same 
	data for a platform-independent version of our CD-ROMs (Crane 2000).</p>
<p>The last technical principle we kept fixed firmly 
	in mind was a focus on automating the digitization process. So much of 
	the work necessarily had to be done by hand: the positioning and lighting 
	of each object for photography; the review of catalog information, and 
	writing of new materials; the checking to ensure images were correctly 
	attached to the corresponding objects. Yet in every possible instance, 
	we looked for automated tools which might speed the process, from batch 
	image-processing software to database error-checking scripts to automatic 
	tape backup.</p>
<p>Our final two requirements as we commenced this 
	work were seemingly disparate principles that have proven over time at 
	Perseus to be actually quite complementary: we sought to promote scholarship 
	and also to distribute our work to the widest possible audience. In pursuit 
	of the former goal, we adopted the highest academic standards in compiling 
	digitized resources; in pursuit of the latter, we recognized that the 
	distribution medium for our work already far outstrips the distribution 
	network for traditional scholarship, and labored to provide the interface, 
	organization, and tools that would facilitate the material's usefulness 
	to a broader range of users.</p>
<dir> 
	<blockquote> 
	  <p>The change in less than a decade from reliance 
	    on publication solely through hundreds or perhaps thousands of physical 
	    copies to a network reaching tens of millions of machines has no clear 
	    historical precedent. For the first time in history, it is possible 
	    to conceive of providing a vast number of people with an extensive 
	    set of tools and documents, facilitating types of exploratory learning 
	    once possible only at the greatest research centers. An audience for 
	    these tools and documents does exist; on the Perseus Web site, we 
	    already see concrete examples of the ever increasing audience that 
	    electronic publication can reach. (Crane, 1998)</p>
	</blockquote>

</dir>
      <p>The range of testimonials we constantly receive 
	from site visitors, preliminary analyses of Web logs, and the sheer quantity 
	of page requests the site receives (currently hovering around 200,000 
	page requests per 24-hour period), together indicate that a digital library 
	has the demonstrable ability to simultaneously serve scholarship and reach 
	an emerging broad audience. We will be able to evaluate site usage, to 
	learn more about our audience for the high-resolution images; for instance, 
	by comparing image use at Internet 1 and Internet 2 schools, we will be 
	able to investigate how having the infrastructure for large downloads 
	will affect use of those resources.</p>
      <p>To summarize, the principles informing our approach 
	to this work included: a broad inclusion of objects; integration of resources; 
	control of ownership; adherence to standards; emphasis on data longevity 
	and portability; automation of the digitization process; promotion of 
	scholarship; and, finally, acknowledgement of a wide audience in the general 
	public.</p>
      <p>From the principles we developed a fairly streamlined 
	work process that has taken advantage of many current standards in digital 
	library work. In addition to the standard classification systems and data 
	formats outlined above, we chose a range of software and hardware with 
	a preference for robust, open-source solutions. Written documentation 
	was produced in a FileMaker Pro database designed to be compatible with 
	both the Museum of Fine Arts' collections management database, in FileMaker 
	Pro, and with the Perseus art and archaeology databases, in 4<sup>th</sup> 
	Dimension. After completion, this data was transferred into PostGres, 
	an open-source SQL database program running on a Linux server, which allows 
	us to run certain indexing functions, automatically generate cross-links 
	with the rest of the digital library, and publish the data on the Web. 
	Thanks to these automated functions, Perseus can immediately associate 
	terms in the new coin documentation with existing terms in the digital 
	library, and lead curious readers to a wide array of additional information; 
	for instance, when a site like Ostia is depicted on a coin, the word &quot;Ostia&quot; 
	in the coin's description automatically links to other Perseus resources 
	on Ostia in the Lookup Tool, including an article on Ostia in the <i>Princeton 
	Encyclopedia of Classical Sites</i>, links to other objects depicting 
	or coming from Ostia, a set of pictures which include &quot;Ostia&quot; 
	in their captions, an atlas page that will plot Ostia on a map, and bibliographic 
	sources on <a href="http://www.perseus.tufts.edu/cgi-bin/sor?lookup=ostia">Ostia</a>.</p>

      <p>On the imaging side, the only stumbling points in 
	a very smooth process have been, first, the proprietary format of the 
	digital photographs as the camera initially stored them, and second, the 
	acquisition of enough hard disk space and RAM to manipulate, store and 
	deliver these quantities of visual information. Digital photographs were 
	made using a Kodak DCS 460 camera, chosen for its relatively high resolution 
	images, its compatibility with our existing system of Nikon lenses, and 
	its efficient work flow, with sufficient storage on removable cards. Once 
	the images were captured and backed up, they were converted to standard 
	TIFFs, a time-consuming process run under the Photoshop Acquire mode which 
	would have been greatly assisted by automated conversion software; we 
	were unable to develop this tool in-house, and Kodak did not produce it, 
	but fortunately a third party vendor, DSL Consulting Inc., has written 
	an<a href="http://www.kodak.com/cgi-bin/US/en/developers/solutions/search/webSolutionsSearch.cgi?SubmissionID=588&amp;amp;drgCurrentState=2"> 
	AutoAcquire script</a> that finally addresses this problem.</span></p>
      <p><span class="normal">Once the images were converted to their delivery 
	format, we employed an assortment of identification and marking measures. 
	Although current watermarking programs are not impervious to attack, they 
	are a perfect mechanism for linking museum identity with each image. As 
	Peticolas &amp; Anderson (1999) explain the current technical outlook, 
	&quot;although most [watermarking] schemes could survive basic manipulations 
	they would not cope with combinations of them or with random geometric 
	distortions.&quot; Still, visible and invisible watermarks are demonstrably 
	useful tools that can be employed first, to link ownership information 
	to image files, and second, to ensure that each image is difficult to 
	separate from this identifying metadata.</p>
      <p>In order to deliver large files across the net in 
	a reasonably short time, we chose to tile the coin files into <a href="/hopper/image?img=1997.03.0019">scrollable windows</a>; 
	the largest tiles are c. 475K in size and compress down to about 36K as 
	JPEGs, due to their limited color palettes. Like <a href="http://now.cs.berkeley.edu:80/Td/GridPix/">GridPix</a>, 
	a tool developed at Berkeley, and other tiling programs, the Perseus tiler 
	allows users to interactively zoom in to parts of an image and scroll 
	around it, while minimizing the waiting time for tiles to appear. We are 
	constantly reminded that many visitors to Perseus dial up over slow modems, 
	log in from geographically far-flung places, and compete for bandwidth 
	with hundreds of others using the same service providers. To speed access, 
	we generated all the different sizes and tiles in advance, not on the 
	fly, and stored the thumbnail size, small (600x400), medium (1530x1018), 
	and large (3060x2036) versions of the images in parallel directories on 
	our server. A database keeps track of what resolutions exist for each 
	image. For implementation, we first used standard archiving software to 
	retrieve the high-resolution, 18 megabyte digital photographs from a tape 
	archive, then employed open source image packages, including PNM and ImageMagick, 
	to convert them into their assorted derivative images.</p>

      <p>An important innovation of the Perseus tiling program 
	is a random rotation feature, which delivers each tile slightly rotated 
	a random amount either right or left, within a narrow range. While not 
	affecting the look of the displayed image, the random rotation protects 
	the tiles from re-use, by making it very difficult to reconstruct them 
	into a single, high-resolution image file.</p>
      <p>A late improvement to the scrollable image window 
	has been a side-by-side 
	image display tool, still in the final stages of development, which 
	allows for simultaneous display of two images at the series of available 
	resolutions. We strongly agree with commentators like Skidmore and Dowie 
	(1999) that side-by-side image presentation is a core pedagogical approach 
	in art history that has been sadly ignored by new technologies up until 
	now. In a 1997 evaluation, one of Rhyne's students commented that a setup 
	presenting two images provided &quot;distinct advantages&quot; for her 
	study, making &quot;manipulation and location easy&quot; and facilitating 
	&quot;side-to-side comparisons impossible in the museum.&quot; Her summary 
	opinion: &quot;The scant hour of frantic sketching and note-taking in 
	a crowded room has few advantages over these clear, beautiful, easily 
	maneuvered and compared images which students can investigate individually.&quot; 
	Our intention is that the Perseus image display tool will serve as a vessel 
	for teachers to construct and save sets of images in Perseus, just as 
	they now save sets of slides for the left and right carousels of their 
	slide lectures. This tool, like other Perseus resources, has been built 
	generically, so that it will be able to handle a variety of image inputs, 
	from coin pictures to page images of texts to custom maps.</p>

      <p>How will access to these big images affect research 
	possibilities for a broad audience? We feel much the same curiosity expressed 
	by Schwartz (1999), but enough confidence in what we have built to be 
	able to say we have indeed &quot;provided tools for accessing more (or 
	more meaningful) information than was previously available.&quot; The 
	museum staff are pleased to have created a new public platform for the 
	scholarship on so many objects in their collection, which has until now 
	found only a specialized audience. Users' first responses to the image 
	display tool and the high-resolution images have ranged from delight to 
	glee. Proper evaluation, both by internal groups of teachers, museum educators, 
	and advisors, and external evaluators, will take place as part of a grant 
	from the Digital Library Initiative, Phase Two, from the National Science 
	Foundation, the National Endowment for the Humanities, and other government 
	agencies. With this support, the Perseus-MFA collaboration is also continuing, 
	and the ongoing focus will be to build generic digital library tools for 
	the humanities which will be made accessible to the wide audience. This 
	set of tools is applicable not only to works of ancient art, but to other 
	humanities sources, such as a First Folio of Shakespeare, a Renaissance 
	Italian dictionary, the physical remains of ancient Rome, and a series 
	of Giza mastabas replete with hieroglyphic inscriptions. Visitors to the 
	Perseus web site will have the ability to examine high-resolution images 
	of any of these resources, using the same image tools developed for the 
	Roman art of the Museum of Fine Arts.</p>
      <p>We may not be able to judge right away whether, 
	as Schwartz asks, &quot;the site provide[s] a window of introduction that 
	will make the use of the museum more meaningful,&quot; even if it might 
	seem clear that giving visitors an improved capacity for scrutinizing 
	art in a way that is difficult to do through the vitrines, under the spotlights, 
	or among the crowds in the galleries, would necessarily bring more nuance 
	and meaning to their understanding of the works they see. My favorite 
	story on this subject is the true one of the Perseus web site user who 
	printed out catalog entries from the exhaustive Caskey-Beazley publication 
	of Greek vases in the MFA, came to Boston, and walked around the galleries 
	with printouts in hand, gaining appreciably more information from looking 
	at the objects in tandem with the thousand-word curators' essays than 
	he ever could have from the 300-word labels.</p>
      <p>Yet even with insufficient evidence to judge whether 
	the physical museum's use becomes more meaningful, we can be certain that 
	the museum has been transformed into a place now accessible, for the first 
	time, to a new constituency. Virtual visitors, who might never travel 
	to Boston, can now see the collection from the Ivory Coast, from Brazil, 
	from Italy, from Singapore, from other parts of our own country, and from 
	hundreds of remote places. These visitors embody the same attitude Dierking 
	&amp; Falk (1998) have observed in actual museum visitors: they &quot;profess 
	high to moderate interest in the subjects presented&quot; they have sought 
	the subject on the Web -- but at the same time profess &quot;low to moderate knowledge&quot; they have come looking for information. The virtual museumgoers already 
	demonstrate that providing on-line documentation of the museum's resources, particularly high-resolution images, transforms the museum and its collection from a local 
	storehouse open 60 hours a week to an internationally available educational resource open 24 hours a day, allowing more people to have an in-depth experience with the 
	art works than ever before, and democratizing the opportunity for a thoroughgoing educational involvement with the collection.</p>

      <p>In closing, I would like to thank Charles Rhyne for his challenging questions three years ago at this conference. I titled	my paper with a question, not because I can 
      provide a simple answer, but because I hope the collaborative work of the Perseus Project and the Boston Museum of Fine Arts will prompt your own questions, comments, and 
      ongoing	discussion about how high-resolution images can change our way of learning with art, by enhancing the way we are able to see it.
<p>
<b>Bibliography</b>

<p>Crane, G., Jacob, R., Taylor, H., Scaife, R., and Allen, N. (1998). A Digital Library for the Humanities [online]. Funded proposal submitted to the NEH-NSF Digital Libraries 
Initiative, Phase II. Available: <a href="http://www.perseus.tufts.edu/Props/DLI2/dli2.htm">http://www.perseus.tufts.edu/Props/DLI2/dli2.html</a>. (February 15, 2000).</p>

<p>Crane, G., ed. (2000). <i>Perseus 2.0: Interactive Sources and Studies on Ancient Greece. Platform-Independent Version.</i> New Haven: Yale University Press. 2000.</p>

<p>Daniels, M. (1997). A Wish-List of Web Resources for Humanities Scholarship [online]. Paper delivered at <i>Museums and the Web 97</i>. Available: 
<a href="http://www.perseus.tufts.edu/%7Emaria/MW97.html">http://www.perseus.tufts.edu/~maria/MW97.html</a>. (February 15, 2000).</p>
	 
<p>Dierking, L. &amp; Falk, J. (1998). Understanding Free-Choice Learning: A Review of the Research and its Application to Museum Web Sites. In D. Bearman &amp; J. Trant (Eds.) <i>Museums and the Web 97-99: Special Edition Proceedings</i>. CD ROM. Archives &amp; Museum Informatics, 1999.
</p>

	
<p>J&ouml;rgensen, C. (1999). Access to Pictorial Material: A Review of Current Research and Future Prospects. <i>Computers and the Humanities 33</i>, No. 4, 293-318.</p>
	
<p>Rhyne, C. (1997). Images as Evidence in Art History and Related Disciplines. In D. Bearman &amp; J. Trant (Eds.) <i>Museums and the Web 97: Selected Papers</i> (pp. 347-361). 
Pittsburgh: Archives &amp; Museum Informatics, 1997.</p>
	  

<p>Rhyne, C. (1997). Student Evaluation of the Usefulness of Computer Images in Art History and Related Disciplines. <i>Visual Resources: An International Journal of Documentation, 
XIII</i>, No. 1, 67-81.</p>
	
<p>Petitcolas, F. A. P. &amp; Anderson, R. J. (1999). Evaluation of Copyright Marking Systems. <i>Proceedings of IEEE Multimedia Systems '99</i>, vol. 1, 574-579.</p>
	
<p>Schwartz, D. (1999). Museums and Libraries in the Age of the Internet: Lessons Learned from a Collaborative Website. In D. Bearman &amp; J. Trant (Eds.) <i>Museums and the 
Web 97-99: Special Edition Proceedings</i>. CD ROM. Archives &amp; Museum Informatics, 1999.</p>

	
  <p>Skidmore, C. &amp; Dowie, S. (1999). Camera Lucida: AMICO in an Art History Classroom. In D. Bearman &amp; J. Trant (Eds.) <i>Museums and the Web 97-99: Special Edition 
  Proceedings</i>. CD ROM. Archives &amp; Museum Informatics, 1999.</p>
<p>
		</div> <!-- Publications div -->	
	</div> <!-- main_col div -->
        
    </div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
