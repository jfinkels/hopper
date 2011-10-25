package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class ctsIndex_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
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

      out.write("<html>\n  <head>\n    <title>Perseus - CTS Service</title>\n  </head>\n  <body>\n<h1>Perseus Canonical Text Services</h1>\n<p>This is the base URL for a server\nsupporting version 1.1 of the CTS protocol.\n</p>\n\n<p>\n</p>\n\n<h2>This implementation of the CTS protocol</h2>\n<p>In addition to the request parameters defined by the CTS 1.1\nprotocol, this server accepts an optional parameter named\n<code>config</code> identifying\na configuration file.  This is an XML document with two elements\nidentifying the CTS TextInventory to use, and a directory\nin the local file system where TEI-conformant XML files are kept.\nThe <code>local</code> attribute in the TextInventory should\ngive file names that can be  concatenated with name of this\ndirectory.\n\n</p>\n<p>\nBy adding further configuration files pointing to a different\nTextInventory and possibly (but not necessarily) pointing to\na different directory for TEI files, you effectively\nimplement a new CTS service with a base URL of the form\n<br/>\n<code>http://yourmachine/path/to/servlet/CTS?config=yourconfigfile.xml&amp;</code>\n");
      out.write("\n</p>\n\n<p>If no <code>config</code> parameter is included, the\nserver uses the file named <code>config.xml</code>.\nIn this case, the base URL of your CTS service is\n<br/>\n<code>http://yourmachine/path/to/servlet/CTS?</code>\n\n</p>\n\n\n<h2>Requests defined by the CTS 1.1 protocol</h2>\n\n<p>\n<form method=\"get\" action=\"CTS\">\n\n<table>\n\n  <tr><td>configuration file:</td><td><input name=\"config\"\n  size=\"10\"/></td></tr>\n  <tr/>\n<tr><td>request:</td>\n<td><select name=\"request\">\n  <option value=\"GetCapabilities\"\n  selected=\"selected\">GetCapabilities</option>\n\n      <option value=\"GetWorks\">GetWorks</option>\n      <option value=\"GetTEIHeader\">GetTEIHeader</option>\n  <option value=\"GetValidReff\">GetValidReff</option>\t\n  <option value=\"GetPassage\">GetPassage</option>\n      <option value=\"DownloadText\">DownloadText</option>\n      <option value=\"GetCitationScheme\">GetCitationScheme</option>\n      <option value=\"GetFirstRef\">GetFirstRef</option>\n      <option value=\"GetPrevNext\">GetPrevNext</option>\n      <option value=\"GetPassagePlus\">GetPassagePlus</option>\n");
      out.write("</select>\n</td></tr>\n\n<tr><td>textgroup:</td><td> <input name=\"textgroup\" size=\"12\"/></td>\n<td> work: </td><td><input name=\"work\" size=\"12\"/></td></tr>\n<tr><td>edition:</td><td> <input name=\"edition\" size=\"12\"/></td>\n<td>or translation:</td><td><input name=\"translation\"\nsize=\"12\"/></td></tr>\n<td> exemplar:</td><td> <input name=\"exemplar\" size=\"12\"/></td></tr>\n<tr></tr>\n\n<tr><td>ref:</td><td><input name=\"ref\" size=\"12\"/></td>\n<td> or  range:</td><td><input name=\"range\" size=\"12\"/></td></tr>\n\n<td>collection:</td><td><input name=\"collection\" size=\"12\"/></td></tr>\n\n<tr>\n  <td>level for valid reff.:</td>\n  <td><input name=\"level\" size=\"4\"/></td>\n  </tr>\n\n<tr>\n  <td>\n# of units of surrounding context:</td><td><input name=\"contextsize\"\n  size=\"4\"/></d></tr>\n\n</table>\n<input type=\"submit\" value=\"Get request\"/>\n</form>\n</p>\n\t <!-- Google Analytics --> \n\t");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n  </body>\n</html>");
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
