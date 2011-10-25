package org.apache.jsp.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class help_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head><title>Perseus Help and Information Center</title>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body onload=\"checkRedirect();\">\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n    \n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()), out, false);
      out.write(" \t\n      <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t  <h3>Perseus Help and Information Center</h3>\n\t  \n\t  <h4>Help Pages:</h4>\n\t  \n\t  <ul>\n\t    <li><a href=\"/hopper/help/quickstart\">Perseus 4 Quick Start Guide</a><img src=\"/img/new.gif\" alt=\"\" border=\"0\">\n\t\t\t\t\t\t<li><a href=\"/hopper/help/copyright\">Copyright &amp; Warranty</a>: information on downloads, reuse, and permissions\n\t    \n\t    \n\t\t\t\t\t\t<li><a href=\"/hopper/help/faq\">Frequently Asked Questions</a> (updated 08/07)\n\t\t  \n\t\t<li><a href=\"/hopper/help/startpoints\">Starting Points</a> for exploring Perseus\n\t\t  \n\t\t<li><a href=\"/hopper/help/texts\">Help with Texts</a>\n\t\t<li><a href=\"/hopper/help/vocab\">Help with the Vocabulary Tool</a>\n\t\t<li><a href=\"/hopper/help/searching\">Help with Searching and Search-related Tools</a>\n\t\t<li><a href=\"/hopper/help/archives\">Archived help documents</a> for previous versions of Perseus (both on-line and on CD)\n\t  </ul>\n\t  \n\t  <h4>General and Contact Information:</h4>\n\t  <ul>\n\t    <li><a href=\"/hopper/help/oldannounce.jsp\">News &amp;\n");
      out.write("\t\tAnnouncements</a>\n\t    <li><a href=\"/hopper/help/support.jsp\">Support Perseus</a>\n\t    <li><b>Contact Information:</b>\n\t      <ul>\n\t\t<li><a href=\"http://groups.yahoo.com/group/perseusproject/\">The\n\t\t    Perseus Users' Forum at Yahoo! Groups</a>\n\t\t<li><a href=\"mailto:webmaster@perseus.tufts.edu\">E-mail the\n\t\t    Webmaster</a><br>\n\t\t  <font size=\"-1\">Note that all mail to the webmaster generates an\n\t\t    automatic reply to the originating e-mail address.<br>\n\t\t    \n\t\t    \n\t\t    <!--Read more in the <a href=\"/hopper/help/faq#mail\">Perseus FAQ</a> on webmaster\n\t\t    mail auto replies for information on receiving a personal reply.<br>\n\t\t    \n\t\t    Please check this document prior to sending any correspondence to the\n\t\t    webmaster.--></font>\n\t      </ul>\n\t  </ul>\n\t  \n\t  <h4>Information on Perseus Collections:</h4>\n\t  <ul>\n\t    <li><a href=\"/hopper/text?doc=Perseus:text:1999.04.0053\">Classics Collection Overview</a>\n\t    <li><a href=\"/hopper/text?doc=Perseus:text:1999.03.0024\">Renaissance Collection Overview</a>\n");
      out.write("\t    <li><a href=\"http://scriptorium.lib.duke.edu/papyrus/texts/DDBDP.html\">The Duke Databank of Documentary Papyri</a>\n\t  </ul>\n\t  \n\t</div> <!-- main_col -->\n      </div> <!-- 2column -->\n    </div> <!-- main -->\n    \n    <!-- Google Analytics --> \n    ");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n    \n  </body>\n</html>\n");
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
