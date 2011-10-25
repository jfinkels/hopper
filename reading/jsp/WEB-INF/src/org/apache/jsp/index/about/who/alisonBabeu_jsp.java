package org.apache.jsp.index.about.who;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class alisonBabeu_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Alison Babeu, Digital Librarian</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("about", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("who", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"members\">\n\t\t<p>\n\t\t    Alison Babeu has served as the Digital Librarian and research coordinator for the Perseus Project\n\t\t    since 2004.    Before coming to Perseus, she worked as a librarian at both the Harvard Business School and the Boston Public Library.  \n\t\t    She has a BA in History from Mount Holyoke College and an MLS from Simmons College.  Her current projects include the development \n\t\t    of an open source library of classical texts and a FRBR-inspired catalog as part of the Mellon funded Cybereditions Project. \n\t\t</p>\n\t\t    <h4>Selected Publications</h4>\n\t\t    <ul>\n\t\t        <li>\n\t\t            <p>\n\t\t                Babeu, Alison. <a\n\t\t                    href=\"http://www.perseus.tufts.edu/~ababeu/PerseusFRBRExperiment.pdf\">\"Building a \"FRBR-Inspired\" Catalog: \n\t\t                    The Perseus Digital Library\n\t\t                    Experience.\"</a>(2008).</p>\n\t\t        </li>\n\t\t        <li>\n\t\t            <p>Babeu, Alison, David Bamman, Gregory Crane, Robert Kummer, Gabriel Weaver, <a\n");
      out.write("\t\t                href=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.003.00001\"> \"Named Entity Identification and Cyberinfrastructure.\" </a>in\n\t\t                <em>Proceedings of the 11th European Conference on Research\n\t\t                and Advanced Technology for Digital Libraries (ECDL 2007)</em>, (2007), pp.\n\t\t                259-270.</p>\n\t\t        </li>\n\t\t        <li>\n\t\t            <p>Stewart, Gordon, Gregory Crane and Alison Babeu, <a\n\t\t                href=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00006\">\"A New Generation of Textual Corpora: \n\t\t                Mining Corpora from Very Large Collections.\"</a>in <em>Proceedings of the 7th ACM/IEEE-CS joint conference on Digital libraries</em>\n\t\t            , (2007), pp. 356-365.</p>\n\t\t        </li>\n\t\t        <li>\n\t\t            <p>Crane, Gregory and Alison Jones, <a\n\t\t                href=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00007\">\"The Challenge\n\t\t                of Virginia Banks:  An Evaluation of Named Entity Analysis in a 19th Century\n");
      out.write("\t\t                Newspaper Collection.\"</a>in <em>Proceedings of the 6th ACM/IEEE-CS joint\n\t\t                    conference on Digital libraries</em>, (2006), pp. 31-40.</p>\n\t\t        </li>\n\t\t        <li>\n\t\t            <p>Mimno, David, Alison Jones, Gregory Crane. <a\n\t\t                href=\"http://www.dlib.org/dlib/october05/crane/10crane.html\">\"Hierarchical Catalog\n\t\t            Records: Implementing a FRBR Catalog.\"</a> in <em>D-Lib Magazine</em>(2005), 11 (10).</p>\n\t\t        </li>\n\t\t        <li>\n\t\t            <p>Mimno, David, Alison Jones, Gregory Crane. <a\n\t\t                href=\"http://www.cs.umass.edu/~mimno/papers/JCDL2005/f74-mimno.pdf\">\"Finding a\n\t\t                Catalog:  Generating Analytical Catalog Records from Well-Structured Digital\n\t\t                Texts.\"</a>in <em>JCDL '05: Proceedings of the 5th ACM/IEEE-CS joint conference on\n\t\t                    Digital libraries</em> (2005), pp. 271-280.</p>\n\t\t        </li>\n\t\t</div> <!-- members div -->\n\t</div> <!-- main_col div -->\n    \n    <div id=\"img_side_col\">\n");
      out.write("    \t<!--<img src=\"/img/davidB.png\">-->\n    </div> <!-- img_side_col -->\n    \n    </div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
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
