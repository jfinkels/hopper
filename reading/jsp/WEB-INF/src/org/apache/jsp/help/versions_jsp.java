package org.apache.jsp.help;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class versions_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Perseus Version History</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\"\n    rel=\"stylesheet\"/>\n  </head>\n  <body>\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()), out, false);
      out.write("\n      \n  <div id=\"content\" class=\"2column\">\n   <div id=\"index_main_col\">\n <h3>Perseus version history</h3>\n\t\t\t\t\t<hr>\n\t\t\t\t\t<ul>\n\t\t\t\t\t\t<li type=\"disc\">1985 Planning for Perseus begins\n\t\t\t\t\t\t<li type=\"disc\">1987 Establishment of project\n\t\t\t\t\t\t<li type=\"disc\">1992 Perseus 1.0 released by Yale University Press\n\t\t\t\t\t\t<li type=\"disc\">1995 Perseus WWW site begins, with addition of LSJ9\n\t\t\t\t\t\t<li type=\"disc\">1996 Perseus 2.0 (Mac only) released by Yale University Press\n\t\t\t\t\t\t<li type=\"disc\">1997 First release of Latin texts on Perseus as well as start of Renaissance collection\n\t\t\t\t\t\t<li type=\"disc\">1999 Perseus begins work under Digital Library Initiative, Phase 2\n\t\t\t\t\t\t<li type=\"disc\">2000 Perseus 2.0 (Platform Independent) released by Yale University Press\n\t\t\t\t\t\t<li type=\"disc\">2000 Perseus web site expanded and revised becoming &quot;Perseus 3.0&quot;; several new collections are added\n\t\t\t\t\t\t<li type=\"disc\">2005 Perseus 4.0 released on WWW\n\t\t\t\t\t\t<li type=\"disc\">2006 Release of Perseus TEI XML collections in Greek, Latin, and English under a Creative Commons Sharalike/Non-Commercial/Attribution license.\n");
      out.write("\t\t\t\t\t\t<li type=\"disc\">2007 Source code for Perseus 4.0 released\n\t\t\t\t\t</ul>\n\t\t\t\t\t<hr>\n\t\t\t\t</div> <!-- index_main_col -->\n\n     </div> <!-- content -->\n    </div> <!-- main -->\n    \n    <!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n  </body>\n</html>\n");
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
