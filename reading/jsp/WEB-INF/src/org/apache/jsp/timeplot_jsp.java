package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class timeplot_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Word Counts Visualization</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n<script type=\"text/javascript\" src=\"/js/timeplot.js\"></script>\n<script src=\"http://api.simile-widgets.org/timeplot/1.1/timeplot-api.js\" type=\"text/javascript\">\n</script>\n<script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n\n</head>\n<body onload=\"onLoad(null);\" onresize=\"onResize();\">\n<div id=\"header\">\n    <a id=\"logo\" href=\"/hopper/\"></a>\n    <div id=\"header_text\">\n\t<h1>Word Counts Visualization</h1>\n    </div>\n<div id=\"header_side\">\n\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n    </div>\n</div>\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "includes/index/indexNav.jsp" + (("includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    <div class=\"2column\">\n    <br/>\n    <div style=\"float: right; width:28%; font-size:small; text-align:center\">\n    \t<div id=\"side\">\n    \t</div>\n    \t<br/>\n        <b>Visualize counts for a language:</b><br>\n        <form id=\"lang_counts\" onsubmit=\"return onLoad(this);\">\n        <input type=\"checkbox\" name=\"showValues\"/>Show Counts\n        \t<select name=\"lang\">\n    \t<option value=\"en\">English</option>\n    \t<option value=\"greek\">Greek</option>\n    \t<option value=\"la\">Latin</option>\n    \t</select>\n        \t<input type=\"submit\" value=\"Submit\"/>\n        </form>\n        <br/>\n        <b>Visualize counts for an individual word <br/>(or compare up to five words):</b><br>\n    \t<form id=\"word_search\" onsubmit=\"return onLoad(this);\">\n    \tSearch in: <select name=\"lang\" onchange=\"toggleGreekDisplay(this)\">\n    \t<option value=\"en\">English</option>\n    \t<option value=\"greek\">Greek</option>\n    \t<option value=\"la\">Latin</option>\n    \t</select><br/>\n    \t<input type=\"checkbox\" name=\"showValues\"/>Show Counts<br/>\n    \tEnter word(s) to search (do not enter accents): <br/>\n");
      out.write("    \t1. <input type=\"text\" name=\"word0\"/><br/>\n    \t2. <input type=\"text\" name=\"word1\"/><br/>\n    \t3. <input type=\"text\" name=\"word2\"/><br/>\n    \t4. <input type=\"text\" name=\"word3\"/><br/>\n    \t5. <input type=\"text\" name=\"word4\"/><br/>\n    \t<input type=\"reset\" value=\"Clear\" name=\"clear\"/>\n    \t<input type=\"submit\" value=\"Search\" name=\"search\"/>\n    \t</form>\n    \t<div class=\"enter_greek\" id=\"enter_greek\" style=\"display: none;\">\n\t\t\tHow to enter text in Greek:<br />\n\t\t\t<img src=\"/img/keyCaps.gif\" alt=\"beta code instructions\"/>\n\t\t</div>\n    </div>\n    \t\n    \t<div id=\"chart_div\" style=\"width: 66%; height: 500px; margin-left:2%;\"></div>\n    </div> ");
      out.write("\n    </div> ");
      out.write("\n    \n<!-- Google Analytics --> \n");
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
