package org.apache.jsp.index.about.who;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class davidBamman_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>David Bamman, Computational Linguist</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("about", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("who", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"members\">\n\t\t<p>\n\t\tDavid Bamman is a senior researcher in computational linguistics for the Perseus Project, \n\t\tfocusing especially on natural language processing for Latin and Greek, including treebank \n\t\tconstruction, computational lexicography, morphological tagging and word sense disambiguation.  \n\t\tDavid received a BA in Classics from the University of Wisconsin-Madison and an MA in Applied \n\t\tLinguistics from Boston University. He is currently leading the development of the <a href=\"http://nlp.perseus.tufts.edu/syntax/treebank\">Ancient Greek and Latin \n\t\tDependency Treebanks</a> and the <a href=\"http://nlp.perseus.tufts.edu/lexicon/\">Dynamic Lexicon Project</a>.\n\t\t</p>\n\t\t<h4>Selected Publications</h4>\n\t\t<ul>\n\t\t\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, Alison Babeu, and Gregory Crane, <a href=\"http://portal.acm.org/citation.cfm?id=1816123.1816126\">\"Transferring Structural Markup Across Translations Using Multilingual Alignment and Projection,\"</a> in: <em>Proceedings of the 10th ACM/IEEE-CS Joint Conference on Digital Libraries (JCDL 2010)</em>.  Winner, Best Paper Award.\n");
      out.write("\t\t\t</p>\n\t\t\t</li>\n\t\t\t\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, Francesco Mambrini and Gregory Crane, <a href=\"http://www.perseus.tufts.edu/~ababeu/tlt8.pdf\">\n\t\t\t\"An Ownership Model of Annotation: The Ancient Greek Dependency Treebank,\"</a> in: <em>Proceedings of the Eighth International Workshop on Treebanks and Linguistic \n\t\t\tTheories (TLT8)</em> (Milan, Italy: 2009). \n\t\t\t</p>\n\t\t\t</li>\n\t\t\t\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, and Gregory Crane, <a href=\"http://www.digitalhumanities.org/dhq/vol/003/1/000033.html\">\n\t\t\t\"Computational Linguistics and Classical Lexicography,\"</a>\n\t\t\t<em>Digital Humanities Quarterly</em> 3.1 (2009).\n\t\t\t</p>\n\t\t\t</li>\n\t\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, Marco Passarotti and Gregory Crane, <a href=\"http://ufal.mff.cuni.cz/pbml/90/art-bamman-et-al.pdf\">\n\t\t\t\"A Case Study in Treebank Collaboration \n\t\t\tand Comparison: Accusativus cum Infinitivo and Subordination in Latin,\"</a> <em>Prague Bulletin of \n\t\t\tMathematical Linguistics</em> 90 (2008).\n\t\t\t</p>\n\t\t\t</li>\n\t\t\t\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David and Gregory Crane, <a href=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.002.00004\">\"The Logic and Discovery of Textual Allusion,\"</a> in: \n");
      out.write("\t\t\t<em>Proceedings of the 2008 LREC Workshop on Language Technology for Cultural Heritage \n\t\t\tData (LaTeCH 2008)</em>.\n\t\t\t</p>\n\t\t\t</li>\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David and Gregory Crane, <a href=\"http://portal.acm.org/citation.cfm?id=1378889.1378892\">\"Building a Dynamic Lexicon from a Digital Library,\"</a> \n\t\t\tin: <em>Proceedings of the 8th ACM/IEEE-CS Joint Conference on Digital Libraries (JCDL \n\t\t\t2008)</em>.\n\t\t\t</p>\n\t\t\t</li>\n\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tCrane, Gregory, David Bamman, and Alison Babeu, \"ePhilology: When the Books Talk to Their \n\t\t\tReaders,\" in: Ray Siemens and Susan Schreibman (eds.), <em>Blackwell Companion to Digital \n\t\t\tLiterary Studies</em> (Oxford: Blackwell, 2007).\n\t\t\t</p>\n\t\t\t</li>\n\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, and Gregory Crane, <a href=\"http://www.aclweb.org/anthology-new/W/W07/W07-0905.pdf\">\"The Latin Dependency Treebank in a Cultural Heritage \n\t\t\tDigital Library,\"</a> in: <em>Proceedings of the 2007 ACL Workshop on Language Technology for \n\t\t\tCultural Heritage Data</em> (2007), pp. 33-40.\n");
      out.write("\t\t\t</p>\n\t\t\t</li>\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tBamman, David, and Gregory Crane, <a href=\"http://ufal.mff.cuni.cz/tlt2006/pdf/110.pdf\">\"The Design and Use of a Latin Dependency Treebank,\"</a> \n\t\t\tin: <em>Proceedings of the Fifth International Workshop on Treebanks and Linguistic \n\t\t\tTheories (TLT2006)</em> (Prague, Czech Republic: 2006), pp. 67-78.\n\t\t\t</p>\n\t\t\t</li>\n\t\t</ul>\n\t\t<h4>Contact</h4>\n\t\t<p>\n\t\tDavid is currently involved in collaborative projects with researchers in the US, Italy and \n\t\tGermany on a variety of topics ranging from syntactic annotation to multilingual named entity \n\t\tanalysis - please feel free to contact him concerning computational approaches to cultural \n\t\theritage material and digital humanities research in general. Students and faculty interested \n\t\tin the production or analysis of quantitative data in Classical languages should also feel \n\t\tfree to contact him.\n\t\t</p>\n\t\t<p>\n\t\t<a href=\"mailto:david.bamman@tufts.edu\">david.bamman@tufts.edu</a>\n\t\t</p>\n\t\t</div> <!-- members div -->\n\t</div> <!-- main_col div -->\n");
      out.write("    \n    <div id=\"img_side_col\">\n    \t<img src=\"/img/davidB.png\">\n    </div> <!-- img_side_col -->\n    \n    </div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n\n</body>\n</html>");
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
