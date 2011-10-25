package org.apache.jsp.help.archived;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class helparchives_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Perseus Help Archives</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\" rel=\"stylesheet\"/>\n  </head>\n  <body>\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("archives", request.getCharacterEncoding()), out, false);
      out.write("\n\n      <div id=\"content\" class=\"2column\">\n      <div id =\"index_main_col\">\n\t<h3>Deprecated or Obsolete Perseus Help &amp; Information</h3>\n\tPlease note, these files are provided for reference only and may not be accurate.\n\t<p>New information, when applicable, will be found on the <a href=\"/hopper/help\">main help page</a>.</p>\n\t<ul>\n\t<li><a href=\"/hopper/help/archived/oldfaq.jsp\">Frequently Asked Questions</a> (deprecated)\n\t<!--<li><a href=\"homehelp.html\">Home Page Help</a> (obsolete)-->\n\t\t\n\t<!--<li><a href=\"startingPoints.html\">Starting Points</a> for exploring Perseus (deprecated)-->\n\t\t\t\n\t<li><a href=\"/hopper/help/archived/fonthelp.jsp\">Greek Font Display Help</a> (deprecated)\n\t\t\t\n\t<li><a href=\"/hopper/help/archived/trans.jsp\">Greek and Latin Translation Tips</a> (deprecated)\n\t<!--<li><a href=\"VorHelp.html\">Lookup Tool Help</a> (deprecated)-->\n\t<!--<li><a href=\"lookuphelp2.html\">Lookup Tool Help: Part Two, Illustrated Sample Searches</a> (deprecated)-->\n\t<li><a href=\"/hopper/help/archived/TextHelp.jsp\">Texts and Word Study Tools Help</a> (deprecated)\n");
      out.write("\t<li><a href=\"/hopper/help/archived/VocabHelp.jsp\">Vocabulary Tool Help</a> (deprecated)\n\t<!--<li><a href=\"tochelp.html\">Using the Table of Contents </a>(obsolete)-->\n\t<!--<li><a href=\"timehelp.html\">Using Timelines </a>(obsolete)-->\n\t<!--<li><a href=\"vrhelp.html\">Virtual Reality Collection Help</a> (obsolete)-->\n\t<p><li><a href=\"/hopper/help/archived/cds.jsp\">Perseus CDs: Help &amp; Information</a> (deprecated)</p>\n\t</ul>\t\t\n\t\t\t\t\n\t<b>Information on former Perseus Collections:</b>\n\t<ul>\n\t<li><a href=\"http://dca.tufts.edu\">Bolles Collection on London:</a> now hosted by the Tufts Digital Collections and Archives\n\t\n\t\t\n\t\t \n\t<li><a href=\"http://memory.loc.gov/ammem/cbhtml/cbhome.html\">American Memory: California as I Saw It:</a> a special collection of the Library of Congress\n\t\n\t\t\t\n\t\t \n\t<li><a href=\"http://memory.loc.gov/ammem/lhcbhtml/lhcbhome.html\">American Memory: The Capital and the Bay:</a> a special collection of the Library of Congress\n\t\n\t\t\t\t\t\n\t\t\t\t\t\n\t\t\t \n\t<li><a href=\"http://memory.loc.gov/ammem/umhtml/umhome.html\">American Memory: Pioneering the Upper Midwest:</a> a special collection of the Library of Congress\n");
      out.write("\t\n\t\t\t\t\t\n\t\t\t\t\t\n\t\t\t\t\t \n\t<li><a href=\"http://dca.tufts.edu/?pid=72&c=99\">Tufts History Collection:</a> now hosted by the Tufts Digital Collections and Archives\n\t\n\t\t\t\t\t\n\t\t\t\t\t\n\t\t\t\t \n\t</ul>\n\t</div> <!-- index_main_col -->\n\t</div> <!-- content 2column -->\n</div> <!-- main -->\n\n    <!-- Google Analytics --> \n");
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
