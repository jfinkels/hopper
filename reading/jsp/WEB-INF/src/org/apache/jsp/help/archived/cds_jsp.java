package org.apache.jsp.help.archived;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class cds_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Perseus CDs: Help and Information</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\"\n    rel=\"stylesheet\"/>\n  </head>\n  <body>\n\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("archives", request.getCharacterEncoding()), out, false);
      out.write("\n\n<div id=\"content\" class=\"2column\">\n<div id=\"index_main_col\">\t\t\t\n<H3>Perseus CDs: Help and Information</H3>The project has published three CD-Rom versions of Perseus: Perseus 1.0 (Yale University Press, 1992); Perseus 2.0 (Mac only) (Yale University Press, 1996); Perseus 2.0 (Platform Independent) (Yale University Press, 2000)<p>\n\nDue to the unavailability of certain software on which these products rely, and the age of these products, we do not recommend purchasing them.  You may find these product incompatible with your current operating system, or otherwise difficult to use.<p>\n\nWe offer the following information for user reference, and cannot guarantee that we will be able to help you with any issues regarding purchase, installation, or operation of the CD-based versions of Perseus.<p>\n\n\n<h3>Help:</H3>\n<UL><li>Documentation:\n<UL><li><a href=\"Perseus2guide.pdf\">Platform-Independent Perseus 2.0 Documentation (download in .pdf format)</a>\n<li><a href=\"PIP2/index.html\">Platform-Independent Perseus 2.0 Documentation</a>\n");
      out.write("<li><a href=\"P2/toc.html\">Perseus 2.0 (Macintosh release) User's Guide, by William Merrill</a>\n<li><a href=\"P1/Doc1.info.html\">Perseus 1.0 Documentation, by M. J. Flanagan</a></UL>\n<p>\n<li>Support:<uL>\n<li><a href=\"http://groups.yahoo.com/group/perseusproject/\">The Perseus Users Forum at Yahoo! Groups</a>\n<li><a href=\"mailto:webmaster@perseus.tufts.edu\">E-mail the\nWebmaster</a><br>\n<font size=\"-1\">Note that all mail to the webmaster generates an\nautomatic reply to the originating e-mail address.<br>\n\n\n<!--Contact the publisher of Perseus CD products, Yale University Press (Special Projects Division), for all CD support questions. Please<b> do not</b> contact the Perseus Project.\n<UL>\n<LI><a href=\"http://www.yale.edu/yup/contact.html#technical\">Contact Information for Yale University Press</a>\n<LI><a href=\"http://www.yale.edu/yup/\">Yale University Press Home Page</a>\n<LI><a href=\"mailto:specproj.press@yale.edu\">E-mail Yale University Press</a>--></UL>\n</UL></UL>\n<!--<h3>Ordering CDs:</h3>\n<UL><li><a href=\"/order.html\">Overview of CD contents</a>\n");
      out.write("<li><a href=\"http://www.yale.edu/yup/P2order.html\">Order Form at Yale University Press</a>-->\n</UL>\n\n</div> <!-- index_main_col -->\n\t</div> <!-- content 2column -->\n</div> <!-- main -->\n\n    <!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n</body>\n</html>");
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
