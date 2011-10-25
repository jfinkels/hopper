package org.apache.jsp.index.opensource;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class download_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(3);
    _jspx_dependants.add("/includes/index/header.jsp");
    _jspx_dependants.add("/includes/index/../head_search.html");
    _jspx_dependants.add("/includes/common/analytics.htm");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\n  <head>\n    <title>Open Source Code</title>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n    <div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("opensource", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("download", request.getCharacterEncoding()), out, false);
      out.write("\n\n<div id=\"content\" class=\"2column\">\n  <div id=\"index_main_col\">\n    <div id=\"opensource\">\n    <p style=\"color:red; text-align:center\">This page is for system administrators. Perseus always provides users the \n    ability to read texts online, which are located on the <a href=\"/hopper/collections\">Collections/Texts</a> page.</p>\n\n<p><b>Update February 1, 2010</b>: A new release of the source code has been added to SourceForge. This code uses \nnew data and tables in the database. If you want to use the latest source code, you <b>must</b> get the latest data \nfrom below.</p>\n\n<b>Perseus' Java Hopper</b>\n<div id=\"hopper\" style=\"margin-left:30px\">\nThe source code and much of the content for Perseus' Java Hopper was first released in November 2007, under open source \nlicenses.  You will need a Unix-type machine and system administrative experience in order to be able to install Perseus \non your own computer.\n\n\n<p>\n<b>Source Code</b> - The source code can be downloaded from <a href=\"http://sourceforge.net/projects/perseus-hopper\" \n");
      out.write("onclick=\"javascript: pageTracker._trackPageview('/outgoing/sourceForgeHopper');\">SourceForge.net</a>\n</p>\n\n<p>\n  <b>Text Files</b> - These are the\n  original XML text files. Download these files if you are generating\n  the data for the hopper yourself. Texts are licensed under the\n  Creative Commons ShareAlike 3.0 <a\n  href=\"http://creativecommons.org/licenses/by-sa/3.0/us/\" \n  onclick=\"javascript: pageTracker._trackPageview('/outgoing/CCLicense');\">License</a> \n  <ul>\n  <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts.tar.gz\" \n  onclick=\"javascript: pageTracker._trackPageview('/opensource/allTexts');\">Download all texts</a> (447 MB)</li>\n  <li>Download individual collections of texts.  NOTE: If you download\n  individual collections, place the directories downloaded in /sgml/texts/.  Some\n  files may be duplicated in the different collections.</li>\n    <ul>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-GreekRoman.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/GreekRomanTexts');\">Download\n");
      out.write("    Greek and Roman collection texts</a> (99 MB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-DDBDP.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/DDBDPTexts');\">Download Duke\n    Databank of Documentary Papyri collection texts</a> (36 MB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-Germanic.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/GermanicTexts');\">Download\n    Germanic collections texts</a> (58 KB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-AmericanHistory.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/AmericanTexts');\">Download\n    American History collection texts</a> (210 MB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-RichmondTimes.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/RichTimesTexts');\">Download\n    Richmond Times collection texts</a> (85 MB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-Renaissance.tar.gz\" \n");
      out.write("    onclick=\"javascript: pageTracker._trackPageview('/opensource/RenaissanceTexts');\">Download\n    Renaissance collection texts</a> (15 MB)</li>\n    <li><a href=\"/hopper/opensource/downloads/texts/hopper-texts-Arabic.tar.gz\" \n    onclick=\"javascript: pageTracker._trackPageview('/opensource/ArabicTexts');\">Download\n    Arabic collection texts</a> (3.3 MB)</li>\n    </ul>\n  </ul>\n  </p>\n\n  <p>\n  <b>Data</b> - Download these .tar.gz files if\n  you prefer to use the provided database dumps and other generated data. \n  <ul>\n  <li>Individual MySQL dumps:</li>\n     <ul>\n     <li><a href=\"/hopper/opensource/downloads/data/hib_artifact_keywords.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibArtifactKeywords');\">hib_artifact_keywords</a> (129 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibArtifacts');\">hib_artifacts</a> (332 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_atomic_artifacts.tar.gz\" \n");
      out.write("     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibAtomicArtifacts');\">hib_atomic_artifacts</a> (389 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_building_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibBuildingArtifacts');\">hib_building_artifacts</a> (73 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_chunks.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibChunks');\">hib_chunks</a> (181 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_citations.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibCitations');\">hib_citations</a> (4.4 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_coin_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibCoinArtifacts');\">hib_coin_artifacts</a> (99 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_date_ranges.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibDateRanges');\">hib_date_ranges</a> (17 KB)</li>\n");
      out.write("     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_dates.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibDates');\">hib_dates</a> (293 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_entities.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibEntities');\">hib_entities</a> (19 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_entity_occurrences.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibEntityOccurrences');\">hib_entity_occurrences</a> (51 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_frequencies.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibFrequencies');\">hib_frequencies</a> (477 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_gem_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibGemArtifacts');\">hib_gem_artifacts</a> (1.9 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_image_names.tar.gz\" \n");
      out.write("     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibImageNames');\">hib_image_names</a> (569 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_images.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibImages');\">hib_images</a> (2.5 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_lang_abbrevs.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibLangAbbrevs');\">hib_lang_abbrevs</a> (955 bytes)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_languages.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibLanguages');\">hib_languages</a> (993 bytes)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_lemmas.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibLemmas');\">hib_lemmas</a> (1.4 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_parses.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibParses');\">hib_parses</a> (10 MB)</li>\n");
      out.write("     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_person_names.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibPersonNames');\">hib_person_names</a> (3.5 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_places.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibPlaces');\">hib_places</a> (11 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_sculpture_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibSculptureArtifacts');\">hib_sculpture_artifacts</a> (368 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_site_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibSiteArtifacts');\">hib_site_artifacts</a> (71 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_toc_chunks.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibTocChunks');\">hib_toc_chunks</a> (7.2 MB)</li>\n     \n");
      out.write("     <li><a href=\"/hopper/opensource/downloads/data/hib_tocs.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibTocs');\">hib_tocs</a> (37 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_vase_artifacts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibVaseArtifacts');\">hib_vase_artifacts</a> (1.4 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/hib_word_counts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/hibWordCounts');\">hib_word_counts</a> (51 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/metadata.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/metadata');\">metadata</a> (367 KB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/morph_frequencies.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/morphFrequencies');\">morph_frequencies</a> (118 KB)</li>\n     \n      <li><a href=\"/hopper/opensource/downloads/data/morph_votes.tar.gz\" \n");
      out.write("     onclick=\"javascript: pageTracker._trackPageview('/opensource/morphVotes');\">morph_votes</a> (2.7 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/prior_frequencies.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/priorFrequencies');\">prior_frequencies</a> (2.9 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/sense_votes.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/senseVotes');\">sense_votes</a> (1.4 MB)</li>\n     \n     <li><a href=\"/hopper/opensource/downloads/data/senses.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/senses');\">senses</a> (2.3 MB)</li>\n     </ul>\n  <li>Download processed XML texts and cache files. These\n  directories go in /sgml/xml/.</li>\n     <ul>\n     <li><a href=\"/hopper/opensource/downloads/data/sgml.xml.texts.tar.gz\" \n     onclick=\"javascript: pageTracker._trackPageview('/opensource/xmlTexts');\">Texts</a> (426 MB)</li>\n     <li><a href=\"/hopper/opensource/downloads/data/sgml.xml.cache.tar.gz\" \n");
      out.write("     onclick=\"javascript: pageTracker._trackPageview('/opensource/xmlCache');\">Cache</a> (25 MB)</li>\n     </ul>\n  <li>Download <a href=\"/hopper/opensource/downloads/data/sgml.reading.index.tar.gz\" \n  onclick=\"javascript: pageTracker._trackPageview('/opensource/luceneIndexes');\">Lucene indexes</a> (205 MB). This directory goes in /sgml/reading/.</li>\n  </ul>\n  </p>\n  </div>\n<p><b>Perseus' Art & Archaeology Module</b></p>\n<div id=\"aa\" style=\"margin-left:30px\">\n  <p>\n   The Art & Archaeology data and source code is now included with the Perseus hopper.\n</p>\n</div>\n</div> <!-- opensource div -->\n</div> <!-- main_col div -->\n\n</div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n\n</body>\n</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
