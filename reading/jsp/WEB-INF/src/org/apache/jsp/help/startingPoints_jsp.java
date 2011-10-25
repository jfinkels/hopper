package org.apache.jsp.help;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class startingPoints_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Perseus - Help - Starting Points</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\" rel=\"stylesheet\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body onload=\"checkRedirect();\">\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n    \n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("startpoints", request.getCharacterEncoding()), out, false);
      out.write("\n      \n      <div id=\"content\" class=\"2column\">\n\t\n\t<div id=\"index_main_col\">\n\t  <h3>Starting Points in Perseus</h3>\n\t  <p>\n\t    \n\t    The Perseus digital library provides a variety of resources on the ancient world. \n\t    Here are a few jumping-off places which will help introduce you to what's in Perseus: \n\t    The <a href=\"#overview\">Historical Overview</a>, the <a href=\"#texts\">primary texts</a>\n\t    <!--, the <a href=\"#atlas\">Atlas</a>-->, and some sample <a href=\"#a&a\">art & archaeology</a> \n\t    searches, along with the large site plans.  General <a href=\"#tools\">help</a> is also available.\n\t  <p>\n\t    <hr size=2>\n\t    \n\t    <h3><a name=\"overview\">Overview</a> of Greek Culture and History</h3>\n\t    \n\t    <ul>\n\t      <li><a href=\"/hopper/text?doc=Perseus%3Atext%3A1999.04.0009\"> Tom Martin's extensive \n\t      <B>Overview of Archaic & Classical Greek History</B></a> covers not only political history, \n\t      but also cultural, religious, and social aspects of ancient Greece.<br>\n\t\t\t\t\t\t\t<font size=-1>The overview has been cross-linked to relevant resources in other parts of Perseus, including images from art and archaeology, passages in the texts, and Greek terms defined in our lexica. These links are fully functional in the web version of the Overview, with the exclusion of those 2.0 images which we do not have permission to distribute freely to the general public.</font>\n");
      out.write("\t    </ul>\n\t    \n\t    <h3>Introduction to the Primary <a name=\"texts\">Texts</a></h3>\n\t    \n\t    <ul>\n\t      <li>Read your favorite <a href=\"/hopper/collection?collection=Perseus:collection:Greco-Roman\">\n\t      Greek or Roman work in translation</a> from our texts, which include \n\t      <a href=\"/hopper/text?doc=Perseus:text:1999.01.0136\">Homer's Odyssey</a>, \n\t      <a href=\"/hopper/text?doc=Perseus:text:1999.01.0004\">Aeschylus' Agammemnon</a>, \n\t      <a href=\"/hopper/text?doc=Perseus:text:1999.01.0035\">Aristophanes' Lysistrata</a>, \n\t      <a href=\"/hopper/text?doc=Perseus:text:1999.02.0019:text=Catil.:speech=1:chapter=1\">Cicero's orations against Catiline</a>, and \n\t      <a href=\"/hopper/text?doc=Perseus:text:1999.02.0054\">Vergil's Aeneid</a>.\n\t    </ul>\n\t    \n\t    <h3>Introduction to the <a name=\"a&a\">Art & Archaeology</a> Catalogs</h3>\n\t    \n\t    Perseus contains thousands of images and descriptions of vases, coins,\n\t    sculpture, buildings, and sites.  There are many ways to \n\t    <a href=\"/hopper/artifactBrowser\" target=\"_blank\">explore and search these catalogs</a> - here are a few examples.<p> \n");
      out.write("\t    \n\t    <ul>\n\t      <!-- <li><a href=\"/hopper/artifactBrowser?object=coin&field=Keyword&keyclass=Animals&keyword=eagle\"><img height=\"30\" src=\"/img/owl.gif\"\n\t\t   align=\"center\" border=\"0\"><b>Eagle Coins</b></a>\n\t\t   <br>\n\t\t   <br>\n\t      We're all familiar with the eagle on the back of the U.S. quarter. Here are some ancient coins with eagles on them. Descriptions of all coins are available to everyone; pictures of the Dewing coins are available.<p>-->\n\t\t<li><a href=\"http://www.perseus.tufts.edu/hopper/image?img=1990.33.1040\">\n\t\t<img height=\"30\" src=\"/img/site.gif\" align=\"center\" border=\"0\"> <b>The Site of Delphi</b></a><br>\n\t\t  <br>\n\t\t  Visit the Sanctuary of Apollo at Delphi, using Delphi's large site plan, a \n\t\t  clickable map that shows you plan drawings of the buildings there, along with photographs.<p>\n\t\t \n\t\t <li><a href=\"/hopper/artifactBrowser?object=Building&field=Building%20Type&value=Fortification\">\n\t\t <img height=\"30\" src=\"/img/aphaia.gif\" align=\"center\" border=\"0\"> <b>Fortification Architecture</b></a><br>\n");
      out.write("\t\t      <br> The ancient Greeks protected their cities from invaders with massive fortification \n\t\t      walls. See some of the remaining walls and towers.<p>\n\t\t\t\n\t\t<!-- <li><a href=\"/hopper/artifactBrowser?object=vase&field=Keyword&keyclass=Animals&keyword=dog\"><img height=\"30\" src=\"/img/smVase2.gif\" align=\"center\" border=\"0\"> <B>Dog Pictures on Vases</B></a><br>\n\t\t\t     <br> See some vase paintings of dogs.  Descriptions of vases are available to everyone; pictures are available for vases listed under the Vase Catalog link <a href=\"/art&arch.html#vaseims\">on the Art and Archaeology page</a><br><br> -->\n\t\t\n\t\t<li> &nbsp; <a href=\"/hopper/artifactSearch?q=ara+pacis+augustae&image=yes\">\n\t\t<img height=30 width=35 src=\"/img/columns.gif\" align=center border=0> <b>Roman Architecture</b></a><br>\n\t\t\t  <br> Explore the Emperor Augustus's altar to Peace, an ornate monument whose sculptures show Augustus and his family, Aeneas, and Mother Earth.<br>\n\t    </ul>\n\t    \n\t    <h3><a name=\"tools\">Tools and Resources</a></h3>\n\t  <p>See the Perseus <a href=\"/hopper/help\">Help Center</a> for general \n");
      out.write("\t  information about using Perseus.  The <a href=\"/hopper/help/faq\">Frequently Asked Questions\n\t  </a> list is one good starting point.  At the top of most Perseus pages -- like this one! -- \n\t  you will find handy links to the most-used pages:  the Tables of Contents for the collections, \n\t  the Configure Display tool for making Perseus work the way <i>you</i> want it to, the Help Center, \n\t  the Tools menu, and more.  There's also a Search box, in the right-hand corner.  Look for these \n\t  links in the colored \"top bar\" at the top of your window.\n\t  <p>The colored \"side bar\" at the left of most Perseus pages contains links relevant to whatever \n\t  you are reading.  When there are place names in the text (in English), for example, the side bar \n\t  will offer a link to the Atlas;  when there are dates, you'll see a link to the Timeline Tool.  \n\t  When you're reading a Greek or Latin text, the side bar will give you links to the dictionaries, \n\t  grammars, and Vocabulary Tool.  And you'll also find a short Table of Contents for the text you're \n");
      out.write("\t  reading.  \n\t  <p>Between the general top bar links and the specialized side bar links, you always have the tools \n\t  and references you need just a click or two away.\n        </div>\n\n      </div>\n\n    </div>\n        <!-- Google Analytics --> \n");
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
