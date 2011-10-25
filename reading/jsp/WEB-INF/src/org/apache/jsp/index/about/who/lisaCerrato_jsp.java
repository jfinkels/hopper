package org.apache.jsp.index.about.who;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class lisaCerrato_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Lisa Cerrato, Managing Editor</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("about", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("who", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t\t\t\t<div id=\"members\">Lisa Cerrato is managing editor of the Perseus Project, overseeing a variety of work. She is the main user support contact, oversees data management and content, provides support for all projects and staff, and performs other administrative duties. Lisa received a BA in Latin from Tufts University, and has been with the project since 1994. Her interests include furthering classical education, particularly Latin and Greek, teaching with technology, and user-driven content management.<h4>Selected Publications</h4>\n\t\t<ul>\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tGregory Crane, Alison Babeu, David Bamman, Lisa Cerrato, Rashmi Singhal. Tools for Thinking: ePhilology and Cyberinfrastructure (to appear). In <em>Promoting Digital Scholarship: Formulating Research Challenges In the Humanities, Social Sciences and Computation,</em> pages -, Washington, D. C. United States : Co-Sponsored by: Council on Library and Information Resources National Endowment for the Humanities, 2008-09.\n");
      out.write("\t\t\t</p>\n\t\t\t</li>\n\t\t\t<li>\n\t\t\t<p>\n\t\t\tGregory Crane, David Bamman, Lisa Cerrato, Alison Jones, David Mimno, Adrian Packel, David Sculley, Gabriel Weaver. Beyond Digital Incunabula: Modeling the Next Generation of Digital Libraries. In <em>Proceedings of the 10th European Conference on Research and Advanced Technology for Digital Libraries (ECDL 2006)</em>, pages 341-352, Alicante, Spain , 2006. (<a href=\"http://dl.tufts.edu//view_pdf.jsp?urn=tufts:facpubs:gcrane-2006.00002\">Full text</a>)</p>\n\t\t\t</li>\n\t\t\t<li><p>Gregory Crane, Clifford E. Wulfman, Lisa M. Cerrato, Anne Mahoney, Thomas L. Milbank, David Mimno, Jeffrey A. Rydberg-Cox, David A. Smith, Christopher York. Towards a Cultural Heritage Digital Library. In <em>Proceedings of the 3rd ACM/IEEE-CS Joint Conference on Digital Libraries</em>, pages 75-86, Houston, Texas , 2003-06. (<a href=\"http://www.perseus.tufts.edu/Articles/jcdl2003.html\">Abstract</a>) (<a href=\"http://perseus.mpiwg-berlin.mpg.de/Articles/jcdl2003.pdf\">Full text</a>)</p>\n\t\t\t\t\t\t\t\t<li><p>Lisa M. Cerrato, Robert F. Chavez. <em><a href=\"http://www.perseus.tufts.edu/hopper/text?doc=Perseus:text:1999.04.0053\">Perseus Classics Collection: An Overview</a></em>.</p></li>\n");
      out.write("\t\t\t\t\t\t\t\t<li><p>Maria Daniels, Lisa Cerrato, David A. Smith, Krista Woodbridge. <em><a href=\"http://www.perseus.tufts.edu/Olympics/\">The Ancient Olympics</a>.</em>  Special exhibit for the Perseus Digital Library.</p>\n\t\t\t\t\t\t\t<li>\n\t\t\t\t\t\t\t\t<p>Maria Daniels, Lisa Cerrato, Gregory Crane, William Merrill, Andrew Slayman, David A. Smith. <em><a href=\"http://www.perseus.tufts.edu/Herakles/\">Hercules: Greece's Greatest Hero</a></em>. Special exhibit for the Perseus Digital Library.</p>\n\t\t\t\t\t\t\t</li></ul>\n\t\t\t\t\t\t<h4>Contact</h4>\n\t\t\t\t\t\tFeel free to contact Lisa with questions about any aspect of the project, particularly issues regarding reuse and permissions.\n\t\t\t\t\t\t<p><a href=\"mailto:lisa.cerrato@tufts.edu\">lisa.cerrato@tufts.edu</a></p>\n\t\t\t\t\t</div>\n\t\t\t\t\t<!-- members div --></div> <!-- main_col div -->\n    \n    <div id=\"img_side_col\">\n    \t<!--<img src=\"/img/davidB.png\">-->\n    </div> <!-- img_side_col -->\n    \n    </div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
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
