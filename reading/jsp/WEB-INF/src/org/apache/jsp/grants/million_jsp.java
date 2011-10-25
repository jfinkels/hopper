package org.apache.jsp.grants;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class million_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Grants</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body onload=\"checkRedirect();\">\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("grants", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"grants\">\n\t<h3>Mining a Million Scanned Books: Linguistic and Structure Analysis, Fast Expanded Search, and Improved OCR</h3>\n\t\n\t<p>A collaborative project with <a target=\"_blank\" href=\"http://ciir.cs.umass.edu/index.html\" \n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Alpheios');\">UMass Amherst</a> and the \n\t<a target=\"_blank\" href=\"http://www.archive.org/\" \n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/InternetArchive');\">Internet Archive</a>\n\t<br/>National Science Foundation Award Number: IIS - <a target=\"_blank\" \n\thref=\"http://www.nsf.gov/awardsearch/showAward.do?AwardNumber=0910165\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/NSF0910165');\">0910165</a>\n\t<br/>Data-intensive Computing</p>\n\t\n\t<p><a href=\"/hopper/about/who/gregoryCrane\"><b>Gregory Crane, PI</b></a>\n\t<br/>Perseus Project/Classics Department\n\t<br/>134C Eaton Hall\n\t<br/>Tufts University\n\t<br/>Medford, MA 02155</p>\n");
      out.write("\t\n\t<p><a href=\"/hopper/about/who/davidBamman\"><b>David Bamman</b></a>\n\t<br/>Perseus Project\n\t<br/>134C Eaton Hall\n\t<br/>Tufts University\n\t<br/>Medford, MA 02155</p>\n\t\n\t<p><b>Project Summary</b></p>\n\t<p>The Center for Intelligent Information Retrieval at UMass Amherst, the Perseus Digital Library Project at \n\tTufts, and the Internet Archive are investigating large-scale information extraction and retrieval technologies \n\tfor digitized book collections.</p>\n\n\t<p>To provide effective analysis and search for scholars and the general public, and to handle the diversity \n\tand scale of these collections, this project focuses on improvements in seven interlocking technologies: improved \n\tOCR accuracy through word spotting, creating probabilistic models using joint distributions of features, and \n\tbuilding topic-specific language models across documents; structural metadata extraction, to mine headers, chapters, \n\ttables of contents, and indices; linguistic analysis and information extraction, to perform syntactic analysis and \n");
      out.write("\tentity extraction on noisy OCR output; inferred document relational structure, to mine citations, quotations, \n\ttranslations, and paraphrases; latent topic modeling  through time, to improve language modeling for OCR and \n\tretrieval, and to track the spread of ideas across periods and genres; query expansion for relevance models, to \n\timprove relevance in information retrieval by offline pre-processing of document comparisons; and interfaces for \n\texploratory data analysis, to provide users of the document collection with efficient tools to update complex models \n\tof important entities, events, topics, and linguistic features.</p>\n\n\t<p>When applied across large corpora, these technologies reinforce each other: improved topic modeling enables more \n\ttargeted language models for OCR; extracting structural metadata improves citation analysis; and entity extraction \n\timproves topic modeling and query expansion.</p>\n\n\t<p>The testbed for this project is the growing corpus of over one million open-access books from the Internet Archive.</p>\n");
      out.write("\t\n\t<p>Undergraduate students involved in this project:\n\t<ul>\n\t<li>John Frederick Owen</li>\n\t<li>Erin Shanahan</li>\n\t</ul>\n\t</p>\n\t\n\t<p><b>Publications:</b>\n\t<ul class=\"publications\">\n\t<li id=\"pub-bamman10-jcdl\">David Bamman, Alison Babeu, Gregory Crane. Transferring Structural Markup Across \n\tTranslations Using Multilingual Alignment and Projection. In <cite>Proceedings of the 10th ACM/IEEE-CS Joint \n\tConference on Digital libraries (JCDL 2010)</cite>, pages 11-20, Australia : ACM Digital Library, 2010-06. \n\t(<a href=\"http://www.perseus.tufts.edu/publications/jcdl27-bamman.pdf\">Full text</a>)</li>\n\t<li id=\"pub-crane10-mellon\">Gregory Crane. Give us editors! Re-inventing the edition and re-thinking the humanities. \n\tIn <cite>Online Humanities Scholarship: The Shape of Things to Come</cite>, University of Virgnia : Mellon \n\tFoundation, 2010-03. (<a target=\"_blank\" href=\"http://cnx.org/content/m34316/1.1/\">Full text</a>)</li>\n\t</ul>\n\t</p>\n\t\t</div> <!-- grants div -->\n\t</div> <!-- main_col div -->\n    </div> <!-- 2column div -->\n");
      out.write("</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n</body>\n</html>\n");
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
