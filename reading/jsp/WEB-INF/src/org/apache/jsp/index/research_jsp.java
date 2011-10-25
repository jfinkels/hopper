package org.apache.jsp.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class research_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Research</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body onload=\"checkRedirect();\">\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("research", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"research\">\n\t\t\n<p>\nOur larger mission is to help make the full record for humanity as intellectually \naccessible as possible to every human being, providing information adapted to as \nmany linguistic and cultural backgrounds as possible.  Our work in the 2008-09 \nacademic year focuses upon the three most widespread classical languages of the \nMediterranean world: Greek, Latin, and Arabic. Many readers familiar with Greek \nand Latin do not realize that early Islamic scholars were the most advanced \nclassicists of their time. In some cases, Greek scholarship re-entered the West \nthrough the circulation of Arabic translations and some ancient Greek works now \nexist only in the Arabic translations that Islamic scholars produced. This work \nshould, however, be seen within the context of our broader mission and the more \nthan twenty years of research that preceded it. Augmenting access to the three \nclassical languages represents only one component, however significant, in the \n");
      out.write("network of resources that will make the full human record accessible in more ways \nand to wider audiences than ever before. Classicists and the scholars of the Western \nworld can only contribute to a part of this vast goal.\n</p>\n<h3>The Mission of Perseus</h3>\n<p>\nOur larger mission is to make the full record of humanity - linguistic sources, physical \nartifacts, historical spaces - as intellectually accessible as possible to every human \nbeing, regardless of linguistic or cultural background. Of course, such a mission can \nnever be fully realized any more than we can reach the stars by which we guide the twisting \npaths and blind allies though the world around us. Similar instincts motivated scholars at \nthe library at Alexandria in the 3rd century BCE, the Arab translators of Greek at Baghdad \nin the 9th century CE, the entrepreneurial printers of Greek and Latin in 15th century \nItaly, and the 19th century German scholars who built the infrastructure on which 20th \ncentury scholarship depended. None of these groups of scholars realized, of course, the \n");
      out.write("fullest vision of universal knowledge that moved them. But that idealized vision allowed \neach to change the worlds in which they lived and carry humanity a little farther. We do \nnot know what form such fundamental instruments as editions, lexica, encyclopedias, atlases, \ndiagrams, museum catalogues, and archaeological site reports will assume, but we know that \nthe infrastructure that we design now will materially enable or constrict how the next \ngeneration will be able to read languages from the past, scrutinize ancient artifacts, and \nexplore the historical spaces.\n</p>\n<p>\nPerseus has a particular focus upon the \n<a href=\"/hopper/collection?collection=Perseus:collection:Greco-Roman\">Greco-Roman world</a> and \nupon classical Greek and Latin, but the larger mission provides the distant, but fixed star by \nwhich we have charted our path for over two decades. <a href=\"/hopper/collection?collection=Perseus:collection:Renaissance\">\nEarly modern English</a>, the <a href=\"/hopper/collection?collection=Perseus:collection:cwar\">\n");
      out.write("American Civil War</a>, the <a target=\"_blank\"\nhref=\"http://nils.lib.tufts.edu/4000.01/\" \nonclick=\"javascript: pageTracker._trackPageview('/outgoing/London');\">History and Topography of London</a>, \nthe History of Mechanics, <a target=\"_blank\"\nhref=\"http://dca.lib.tufts.edu/scale/\" \nonclick=\"javascript: pageTracker._trackPageview('/outgoing/DCAScale');\">automatic identification and glossing of \ntechnical language in scientific documents</a>, customized reading support for Arabic language, and other \nprojects that we have undertaken allow us to maintain a broader focus and to demonstrate the commonalities \nbetween Classics and other disciplines in the humanities and beyond. At a deeper level, collaborations with \ncolleagues outside of classical studies make good on the claim that a classical education generally provides \nthose critical skills and that intellectual adaptability that we claim to instill in our students. We offer \nthe combination of classical and non-classical projects that we pursue as one answer to those who worry that \n");
      out.write("a classical education will leave them or their children with narrow, idiosyncratic skills.\n</p>\n<p>\nWithin this larger mission, we focus on three categories of access:\n</p>\n<p>\n<b>Human readable information</b>: digitized images of objects, places, inscriptions, and \nprinted pages, geographic information, and other digital representations of objects and \nspaces. This layer of functionality allows us to call up information relevant to a longitude \nand latitude coordinate or a library call number. In this stage digital representations \nprovide direct access to the physical senses of actual people in particular places and \ntimes. In some cases (such as high resolution, multi-spectral imaging), digital sources \nalready provide better physical access than has ever been feasible when human beings had \ndirect contact with the physical artifact.\n</p>\n<p>\n<b>Machine actionable knowledge</b>: catalogue records, encyclopedia articles, lexicon entries, \nand other structured information sources. Physical access can serve our senses but provides no \n");
      out.write("information about what we are encountering - in effect, physical access is like visiting a \nhistorical site about which we may know nothing and where any visible documentation is in a \nlanguage that we cannot understand. Machine actionable knowledge allows us to retrieve information \nabout what we are viewing. Thus, if we encounter a page from a Greek manuscript of Homer, we could \nat this stage find cleanly printed modern editions of the Greek, modern language translations, \ncommentaries and other background information about the passage on that manuscript page. If we \nmoved through a virtual Acropolis, we could retrieve background information about the buildings \nand the sculpture.\n</p>\n<p><b>Machine generated knowledge</b>: By analyzing existing information automated systems can \nproduce new knowledge. Machine actionable knowledge allows, for example, us to look up a dictionary \nentry (e.g., <em>facio</em>, \"to do, make\") in a dictionary or to find pre-existing translations \nfor a passage in Latin or Greek. Machine generated knowledge allows a machine to recognize that \n");
      out.write("<em>fecisset</em> is a pluperfect subjunctive form of <em>facio</em> and to provide reading \nsupport where there is no pre-existing human translation. Such reading support might include \nfull machine translation but also finer grained services such as word and phrase translation (e.g., \nrecognizing whether <em>orationes</em> in a given context more likely corresponds to English \n\"speeches,\" \"prayers\" or some other term), syntactic analysis (e.g., recognizing that \n<em>orationes</em> in a given passage is the object of a given verb), named entity identification \n(e.g., identifying <em>Antonium</em> in a given passage as a personal name and then as a reference \nto Antonius the triumvir).\n</p>\n\n</div> <!-- Research div -->\n</div> <!-- main_col div -->\n\n</div> <!-- 2column div -->\n</div> <!-- main div -->\n\t\t\n<!-- Google Analytics --> \n");
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
