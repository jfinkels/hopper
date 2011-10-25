package org.apache.jsp.index.research;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class background_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Research Background</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("research", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("background", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n    \t<div id=\"index_main_col\">\n\t\t<div id=\"research\">\n\t\t\n<p>\nWhen we began work on Perseus in 1985, <a target=\"_blank\" \nhref=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00005\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/AnnenbergPDF');\">\nsupport from the Annenberg/CPB Project</a> allowed us to create a critical mass of information - \ntextual, archaeological, and artistic - about the ancient Greek world. As the \n<a target=\"_blank\"\nhref=\"http://www.dlib.org/dlib/january98/01crane.html\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/DLIBPerseus');\">Greek collections in Perseus \nmatured</a>, we were able not only to include <a href=\"http://www.perseus.tufts.edu/Articles/jact2000.html\">\nRoman civilization</a> but to explore <a href=\"http://perseus.mpiwg-berlin.mpg.de/Articles/cacm2000.pdf\">\nother areas in the humanities</a>, such as the history of science and early modern English, for which \nour collections and infrastructure were useful.\n");
      out.write("</p>\n<p>\nThis broader research agenda led in 1998 to a major grant from the Digital Library Initiative \nPhase 2, funded primarily by the National Endowment for the Humanities (NEH) and the National \nScience Foundation (NSF), which funded us to study the problems of creating a \n<a href=\"http://perseus.mpiwg-berlin.mpg.de/Articles/jcdl2003.pdf\">digital library for the \nhumanities as a whole</a>. With this as our foundational support, we were able to produce \ncollections on topics such as the <a href=\"http://perseus.mpiwg-berlin.mpg.de/Articles/jcdl01.pdf\">\nHistory and Topography of London</a>, the \n<a target=\"_blank\"\nhref=\"http://dl.tufts.edu//view_pdf.jsp?urn=tufts:facpubs:gcrane-2006.00001\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/TDLCwar');\">American Civil \nWar</a>, and early modern English, and services such as \n<a href=\"http://perseus.mpiwg-berlin.mpg.de/Articles/geodl01.pdf\">historical named entity \nidentification</a> and a digital library environment that anticipated services offered by giants such \n");
      out.write("as Yahoo and Google and whose full functionality still exceeds any system with which we are familiar, \nand a stream of publications on the methods involved.\n</p>\n<p>\nWe had already begun building what the NSF would in the early years of this century call \nCyberinfrastructure: an aggregate of collections and services, automatically linked and \nanalyzed, that begins to show emergent properties qualitatively distinct from the print \nworld. After having identified <a target=\"_blank\"\nhref=\"http://dl.tufts.edu//view_pdf.jsp?urn=tufts:facpubs:gcrane-2006.00002\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/TDLNextGen');\">\na number of practices that distinguish digital from print infrastructure</a>, we had \nbegun to <a target=\"_blank\"\nhref=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.003.00001\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/TDLNamedEntity');\">define \nthe services and collections</a> that this new digital infrastructure would require. We \nthen began addressing the problem of extracting the sophisticated knowledge needed for these \n");
      out.write("new services from very large collections with hundreds of thousands and millions of books, \navailable only as scanned page images. Planning grants from the Mellon Foundation allowed us \nto run a series of seminars and preliminary research on the general problem of \"What do you \ndo with a million books?\" and the more specific topic of \"Classics in the Million Book Library.\"\n</p>\n<p>\nOur research on a range of topics outside of Classics allowed us to see how the problems of \nclassical studies related to those of other disciplines. In exploring the challenges and \nopportunities of <a target=\"_blank\"\nhref=\"http://www.dlib.org/dlib/march06/crane/03crane.html\"\nonclick=\"javascript: pageTracker._trackPageview('/outgoing/DLIBMillion');\">very large \ncollections</a>, we looked at the problems of collections from the early modern period \n(which present the greatest challenges for automatic processing), from the 19th century (which \nprovide a best case, with many documents in easily analyzed print and a wealth of detailed \n");
      out.write("information about people, places, organizations and other topics in a modern format), and \nclassical studies (with the complex layouts of its critical editions, lexica, and other \nreference works, and its need to manage materials in not only Greek and Latin but English, \nFrench, German, and Italian, and, if we wanted to cover the full classical tradition, Arabic \nas well). Since classical editions were among the first printed books and classical scholarship \nhas not only continued ever since but also particularly flourished in the 19th century, we \nrealized that classical studies raised a superset of the challenges we had set out to study. \nWhen we considered as well that classical studies covered not only literature and history but \nancient science and medicine, and art and archaeology, we realized that classics covered a \nsuperset of the problems that many other fields within the humanities faced. Having worked on \nShakespeare and Early Modern English, <a target=\"_blank\"\nhref=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.001.00007\"\n");
      out.write("onclick=\"javascript: pageTracker._trackPageview('/outgoing/TDL19thC');\">\n19th century newspapers</a> and the American civil war, the city of London and the history of \nearly modern science, we decided that classical studies provided the best space within which \nto advance our work on a cyberinfrastructure for the humanities in general.\n</p>\n\n</div> <!-- Research div -->\n</div> <!-- main_col div -->\n\n</div> <!-- 2column div -->\n</div> <!-- main div -->\n\t\t\n<!-- Google Analytics --> \n");
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
