package org.apache.jsp.index.about.who;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class gregoryCrane_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Gregory Crane, Editor in Chief</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body onload=\"checkRedirect();\">\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("about", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("who", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"members\">\n\t\t<p>\n\t\tGregory Crane's interests are twofold. On the one hand, he has published on a wide range of \n\t\tancient Greek authors (including articles on Greek drama and Hellenistic poetry and a book \n\t\ton the <em>Odyssey</em>). Much of his traditional scholarly work has been devoted to \n\t\tThucydides; his book <em>The Blinded Eye: Thucydides and the New Written Word</em> appeared \n\t\tfrom Rowman and Littlefield in 1996; his second Thucydides book (<em>The Ancient Simplicity: \n\t\tThucydides and the Limits of Political Realism</em>) was published by the University of \n\t\tCalifornia Press in 1998.\n\t\t</p>\n\t\t<p>\n\t\tAt the same time, he has a long-standing interest in the relationship between the humanities \n\t\tand rapidly developing digital technology. He began this side of his work as a graduate \n\t\tstudent at Harvard when the Classics Department purchased its first TLG authors on magnetic \n\t\ttape in the summer of 1982. He developed a Unix-based full text retrieval system for the TLG \n");
      out.write("\t\tthat was widely used in North America and Europe in the middle 1980s. He also helped establish \n\t\ta typesetting consortium to facilitate scholarly publishing. Since 1985 he has been engaged \n\t\tin planning and development of the Perseus Project, which he directs as the Editor-in-Chief. \n\t\tBesides supervising the Perseus Project as a whole, he has been primarily responsible for the \n\t\tdevelopment of the morphological analysis system which provides many of the links within the \n\t\tPerseus database.\n\t\t</p>\n\t\t<p>\n\t\tFrom 1998 through 2006 he directed a grant from the <a href=\"http://www.dli2.nsf.gov/\">Digital \n\t\tLibrary Initiative</a> to study general problems of digital libraries in the humanities. Under \n\t\tthe DLI-2 program, he worked on a range of topics, including such topics as London, the history \n\t\tof Mechanics, and the American Civil War. Each of these collections provided new insights into \n\t\tthe implications of such new electronic tools on learning. In 2006, he produced a named entity \n\t\tidentification system, published a 55 million word collection, and authored several publications \n");
      out.write("\t\tdescribing the system.\n\t\t</p>\n\t\t<p>\n\t\tWith the rise of the Google Books project in 2004, he began to focus upon the problems and \n\t\topportunities that arise when whole libraries rather than curated collections become available \n\t\ton-line. The broad range of projects that he suppported with support from the DLI-2 program, \n\t\tthe Institute for Museum and Library Services, and the Mellon Foundation provided a broad \n\t\tfoundation within which to frame his current generation of research projects on Classical \n\t\tStudies at Perseus. Crane overees the overall research program at Perseus.\n\t\t</p>\n\t\t<p>\n\t\tCrane is especially interested in helping the emerging Cyberinfrastructure serve the needs of \n\t\tthe humanities in general and classical studies in particular.\n\t\t</p>\n\t\t<p>\n\t\t<a href=\"mailto:gregory.crane@tufts.edu\">gregory.crane@tufts.edu</a> | <a href=\"http://www.perseus.tufts.edu/~gcrane/grc.cv.pdf\">Curriculum Vitae</a>\n\t\t</p>\n\t\t</div> <!-- members div -->\n\t</div> <!-- main_col div -->\n    \n    <div id=\"img_side_col\">\n");
      out.write("    \t<img src=\"/img/gregC.jpg\">\n    </div> <!-- img_side_col -->\n    \n    </div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
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
