<html>
  <head>
    <title>Open Source Code</title>
    <link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body>
<%@ include file="/includes/index/header.jsp" %>

    <div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="opensource"/>
	<jsp:param name="subtabActive" value="download"/>
    </jsp:include>

<div id="content" class="2column">
  <div id="index_main_col">
    <div id="opensource">
    <p style="color:red; text-align:center">This page is for system administrators. Perseus always provides users the 
    ability to read texts online, which are located on the <a href="/hopper/collections">Collections/Texts</a> page.</p>

<p><b>Update February 1, 2010</b>: A new release of the source code has been added to SourceForge. This code uses 
new data and tables in the database. If you want to use the latest source code, you <b>must</b> get the latest data 
from below.</p>

<b>Perseus' Java Hopper</b>
<div id="hopper" style="margin-left:30px">
The source code and much of the content for Perseus' Java Hopper was first released in November 2007, under open source 
licenses.  You will need a Unix-type machine and system administrative experience in order to be able to install Perseus 
on your own computer.


<p>
<b>Source Code</b> - The source code can be downloaded from <a href="http://sourceforge.net/projects/perseus-hopper" 
onclick="javascript: pageTracker._trackPageview('/outgoing/sourceForgeHopper');">SourceForge.net</a>
</p>

<p>
  <b>Text Files</b> - These are the
  original XML text files. Download these files if you are generating
  the data for the hopper yourself. Texts are licensed under the
  Creative Commons ShareAlike 3.0 <a
  href="http://creativecommons.org/licenses/by-sa/3.0/us/" 
  onclick="javascript: pageTracker._trackPageview('/outgoing/CCLicense');">License</a> 
  <ul>
  <li><a href="/hopper/opensource/downloads/texts/hopper-texts.tar.gz" 
  onclick="javascript: pageTracker._trackPageview('/opensource/allTexts');">Download all texts</a> (447 MB)</li>
  <li>Download individual collections of texts.  NOTE: If you download
  individual collections, place the directories downloaded in /sgml/texts/.  Some
  files may be duplicated in the different collections.</li>
    <ul>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-GreekRoman.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/GreekRomanTexts');">Download
    Greek and Roman collection texts</a> (99 MB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-DDBDP.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/DDBDPTexts');">Download Duke
    Databank of Documentary Papyri collection texts</a> (36 MB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-Germanic.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/GermanicTexts');">Download
    Germanic collections texts</a> (58 KB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-AmericanHistory.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/AmericanTexts');">Download
    American History collection texts</a> (210 MB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-RichmondTimes.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/RichTimesTexts');">Download
    Richmond Times collection texts</a> (85 MB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-Renaissance.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/RenaissanceTexts');">Download
    Renaissance collection texts</a> (15 MB)</li>
    <li><a href="/hopper/opensource/downloads/texts/hopper-texts-Arabic.tar.gz" 
    onclick="javascript: pageTracker._trackPageview('/opensource/ArabicTexts');">Download
    Arabic collection texts</a> (3.3 MB)</li>
    </ul>
  </ul>
  </p>

  <p>
  <b>Data</b> - Download these .tar.gz files if
  you prefer to use the provided database dumps and other generated data. 
  <ul>
  <li>Individual MySQL dumps:</li>
     <ul>
     <li><a href="/hopper/opensource/downloads/data/hib_artifact_keywords.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibArtifactKeywords');">hib_artifact_keywords</a> (129 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibArtifacts');">hib_artifacts</a> (332 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_atomic_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibAtomicArtifacts');">hib_atomic_artifacts</a> (389 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_building_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibBuildingArtifacts');">hib_building_artifacts</a> (73 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_chunks.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibChunks');">hib_chunks</a> (181 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_citations.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibCitations');">hib_citations</a> (4.4 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_coin_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibCoinArtifacts');">hib_coin_artifacts</a> (99 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_date_ranges.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibDateRanges');">hib_date_ranges</a> (17 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_dates.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibDates');">hib_dates</a> (293 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_entities.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibEntities');">hib_entities</a> (19 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_entity_occurrences.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibEntityOccurrences');">hib_entity_occurrences</a> (51 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_frequencies.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibFrequencies');">hib_frequencies</a> (477 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_gem_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibGemArtifacts');">hib_gem_artifacts</a> (1.9 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_image_names.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibImageNames');">hib_image_names</a> (569 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_images.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibImages');">hib_images</a> (2.5 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_lang_abbrevs.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibLangAbbrevs');">hib_lang_abbrevs</a> (955 bytes)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_languages.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibLanguages');">hib_languages</a> (993 bytes)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_lemmas.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibLemmas');">hib_lemmas</a> (1.4 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_parses.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibParses');">hib_parses</a> (10 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_person_names.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibPersonNames');">hib_person_names</a> (3.5 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_places.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibPlaces');">hib_places</a> (11 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_sculpture_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibSculptureArtifacts');">hib_sculpture_artifacts</a> (368 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_site_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibSiteArtifacts');">hib_site_artifacts</a> (71 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_toc_chunks.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibTocChunks');">hib_toc_chunks</a> (7.2 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_tocs.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibTocs');">hib_tocs</a> (37 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_vase_artifacts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibVaseArtifacts');">hib_vase_artifacts</a> (1.4 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/hib_word_counts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/hibWordCounts');">hib_word_counts</a> (51 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/metadata.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/metadata');">metadata</a> (367 KB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/morph_frequencies.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/morphFrequencies');">morph_frequencies</a> (118 KB)</li>
     
      <li><a href="/hopper/opensource/downloads/data/morph_votes.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/morphVotes');">morph_votes</a> (2.7 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/prior_frequencies.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/priorFrequencies');">prior_frequencies</a> (2.9 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/sense_votes.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/senseVotes');">sense_votes</a> (1.4 MB)</li>
     
     <li><a href="/hopper/opensource/downloads/data/senses.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/senses');">senses</a> (2.3 MB)</li>
     </ul>
  <li>Download processed XML texts and cache files. These
  directories go in /sgml/xml/.</li>
     <ul>
     <li><a href="/hopper/opensource/downloads/data/sgml.xml.texts.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/xmlTexts');">Texts</a> (426 MB)</li>
     <li><a href="/hopper/opensource/downloads/data/sgml.xml.cache.tar.gz" 
     onclick="javascript: pageTracker._trackPageview('/opensource/xmlCache');">Cache</a> (25 MB)</li>
     </ul>
  <li>Download <a href="/hopper/opensource/downloads/data/sgml.reading.index.tar.gz" 
  onclick="javascript: pageTracker._trackPageview('/opensource/luceneIndexes');">Lucene indexes</a> (205 MB). This directory goes in /sgml/reading/.</li>
  </ul>
  </p>
  </div>
<p><b>Perseus' Art & Archaeology Module</b></p>
<div id="aa" style="margin-left:30px">
  <p>
   The Art & Archaeology data and source code is now included with the Perseus hopper.
</p>
</div>
</div> <!-- opensource div -->
</div> <!-- main_col div -->

</div> <!-- 2column div -->
</div> <!-- main div -->

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
