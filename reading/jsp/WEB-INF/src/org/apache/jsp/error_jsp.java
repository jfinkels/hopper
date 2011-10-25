package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.PrintWriter;

public final class error_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/includes/head_search.html");
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
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    if (exception != null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n<!DOCTYPE html \n     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html>\n<head>\n<title>An Error Occurred</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\" />\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/error.css\" />\n</head>\n<body>\n<div id=\"header\">\n    <a id=\"logo\" href=\"/hopper/\"></a>\n    <div id=\"header_text\">\n\t<h1>Perseus Digital Library</h1>\n    </div>\n    <div id=\"header_side\">\n\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n    </div>\n</div>\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("", request.getCharacterEncoding()), out, false);
      out.write(" \n    \n    <div id=\"content\" class=\"1column\" style=\"margin-left: 1%;\">\n\t<h2>An Error Occurred</h2>\n\n\t<p>Sorry, we were unable to load the page you were looking for!</p>\n\n\t<p>You've encountered an error that probably occurred due to a problem on our end. The error has been logged, and we'll be looking into fixing it. If you'd like to help us out, you can fill out the following form:</p>\n\n\t<form action=\"reporterror.jsp\" method=\"post\" class=\"error\">\n\t    <fieldset>\n\t\t<legend>Error Report</legend>\n\n\t\t<input type=\"hidden\" name=\"url\"\n\t\t\tvalue=\"");
      out.print( request.getAttribute("javax.servlet.error.request_uri") );
      out.write('?');
      out.print( request.getQueryString() );
      out.write("\" />\n\n\t    ");
 if (exception != null) { 
      out.write("\n\t\t<input type=\"hidden\" name=\"exception-class\"\n\t\t\tvalue=\"");
      out.print( exception.getClass().getName() );
      out.write("\" />\n\n\t\t");
 if (exception.getMessage() != null) { 
      out.write("\n\t\t<input type=\"hidden\" name=\"exception-message\"\n\t\t\tvalue=\"");
      out.print( exception.getMessage().replaceAll("\"", "'") );
      out.write("\" />\n\t\t");
 } 
      out.write("\n\n\t\t<input type=\"hidden\" name=\"exception-stack-trace\"\n\t\t\tvalue=\"");


		 StackTraceElement[] stackTrace = exception.getStackTrace();
		 for (int i = 0; i < stackTrace.length; i++) {
		    out.print(stackTrace[i]);
		    if (i < stackTrace.length-1) out.print("\n");
		 }
      out.write("\" />\n\t    ");
 } 
      out.write("\n\n\t\t<table>\n\t\t    <tr>\n\t\t\t<td><label for=\"name\">Your name (optional):</label>\n\t\t\t<td><input type=\"text\" name=\"name\" />\n\t\t    </tr>\n\t\t    <tr>\n\t\t\t<td><label for=\"email\">Your email (optional):</label>\n\t\t\t<td><input type=\"text\" name=\"email\" />\n\t\t    </tr>\n\t\t    <tr>\n\t\t\t<td><label for=\"subject\">Email subject (optional):</label>\n\t\t\t<td><input type=\"text\" name=\"subject\" />\n\t\t    </tr>\n\t\t</table>\n\n\t\t<p><strong>Clicking \"Report Bug\" will automatically send us the URL\n\t\tyou were trying to access and a description of the error.</strong>\n\t\tIf you wish, you may provide some additional information below:</p>\n\n\t\t<textarea name=\"additional_info\">\n\t\t</textarea>\n\t\t<br />\n\t\t<input type=\"submit\" value=\"Report Bug\" />\n\t    </fieldset>\n\t</form>\n\n\t<p>Apologies for the problem, and thanks for your understanding.</p>\n\n\t<p><a href=\"/hopper/\">Perseus Home Page</a></p>\n    </div> ");
      out.write("\n</div> ");
      out.write("\n\t <!-- Google Analytics --> \n \n");
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
