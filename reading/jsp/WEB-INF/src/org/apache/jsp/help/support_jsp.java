package org.apache.jsp.help;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class support_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Support Perseus</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\" rel=\"stylesheet\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body onload=\"checkRedirect();\">\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n    \n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()), out, false);
      out.write(" \t\n      <div id=\"content\" class=\"2column\">\n\t<div id=\"index_main_col\">\n\t<H3>Support Perseus</H3>\n\t\n\t<p>The Perseus Project needs your support. We offer this web site as a\n\t  free service. While we have been very fortunate to receive grants and\n\t  support from many parties, the job of creating on-line collections\n\t  that document human culture is daunting.  Help us expand our\n\t  collections and improve the services that we can offer.  In order to\n\t  add more materials to the Perseus Digital Library, we need support\n\t  from our friends. Please consider making a tax-deductible donation to\n\t  the project.  <p><center><table width=66% border=1><tr><td><b>Adopt a\n\t\t    book! Sponsor a collection!</b> We display the funder for each text\n\t\t  and image in the Perseus Digital Library. Some books cost many\n\t\t  thousands of dollars to place on-line, but for a gift of $1,000 or\n\t\t  more you can have a credit line for a professionally entered, well\n\t\t  tagged on-line book in Perseus. The funder's credit will be a\n\t\t  permanent part of the catalogue entry for this electronic book. In the\n");
      out.write("\t\t  Perseus Digital Library, each electronic page would include a line\n\t\t  such as:\n\t\t  <table border=4 align=center><tr><td>Mary Cole provided\n\t\t\tsupport for entering this text.</td></tr></table>\n\t\t  <p>If you would like to support the entry of particular books or collections \n\t\t  of materials, please contact Perseus at webmaster@perseus.tufts.edu. \n\t\t  </td></tr></table></center>\n\t\n\t\t<p><a href=\"supportperseus.pdf\" \n\t\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/SupportPerseus');\">Download the support form</a> in PDF format. \n\t\tYou may also utilize the on-line <a target=\"_blank\"\n\t\thref=\"http://www.tuftsgiving.org\">\n\t\tTufts advancement site</a>. [Please select \"Other\" in the Select a School option and indicate &quot;Perseus Project&quot; in the \"Other\" \n\t\ttext entry box which appears.]\n\t\t<br> Need Adobe Acrobat Reader? <a target=\"_blank\" \n\t\thref=\"http://www.adobe.com/products/acrobat/readstep2.html\"\n\t\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Adobe');\">\n\t\tVisit Adobe</a> for a free download.\n");
      out.write("\t  \n\t  </div> <!-- index_main_col -->\n      </div> <!-- content -->\n      </div> <!-- main -->\n      \n      <!-- Google Analytics --> \n");
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
