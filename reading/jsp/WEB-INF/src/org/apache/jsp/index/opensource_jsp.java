package org.apache.jsp.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class opensource_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Open Source Code</title>\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body onload=\"checkRedirect();\">\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n    <div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("opensource", request.getCharacterEncoding()), out, false);
      out.write("\n\n<div id=\"content\" class=\"2column\">\n  <div id=\"index_main_col\">\n    <div id=\"opensource\">\n\n<p><b>Background and Purpose</b>\n<p>\nIn the earliest stages of the transition from a CD-Rom-based collection to the WWW site, it was clear that \nthe nature and scope of the Perseus resource demanded a flexible, extensible, and powerful data management \nsystem. Written mostly in Perl, the production version of the on-line Perseus text management system evolved \nand grew over eight years, becoming a uniquely powerful platform, capable of ingesting heterogeneous source \nmaterials and performing a range of automatic services. With few precedents and examples to follow, however, \nthe code behind this system reflected organic growth and experimentation, and became difficult to sustain, \nshare, and modify. While all versions of the Perseus Digital Library system were designed to be open-source \n(third parties did make use of the HyperTalk, Tcl/TK and Perl code), each of the previous incarnations of \nPerseus were complex and difficult to document, which presented obstacles to new avenues of collaborative \n");
      out.write("research and development.\n</p>\n<p>\nAs digital library systems matured in the early 00's, the project sought third party solutions for delivering \nresources. At the time, most digital libraries concentrated on locating objects and then left it to the users \nto make sense of what they had found. In contrast, Perseus had increasingly focused on giving users the tools \nto understand what the digital library gave them: the project depended upon a range of automatic linking, \ninformation extraction and visualization services that existing, largely catalog-oriented systems could not \nsupport. The project chose to build a new digital library system, designing it from the start to be \ninteroperable, modular, and open-source.\n</p>\n</p>\n\n<p><b>Open-Source Services</b>\n<p>\nThe Perseus Hopper is an open-source project providing a suite of services for interacting with textual \ncollections.  While as a whole it provides an integrated reading environment, its individual services are \ndesigned to be modular and can be grouped into three different classes.\n");
      out.write("</p>\n<p>\n<i><b>Linguistic support:</b></i> The Hopper itself is language independent, but the code includes native support \nfor Greek, Latin and Arabic.  Given a source text in any one of those three languages (either a text bundled \nwith the code release or a TEI-compliant XML text of the user's own), it provides services for automatic \nlemmatization (linking inflected word forms to the dictionary entries from which they're derived) and \nmorphological analysis (identifying, for instance, that the Latin word <i>amor</i> is a singular masculine \nnominative noun). At a broader level, it also enables corpus research by automatically generating word and \nlemma frequency information for the entire collection of texts supplied to it.\n</p>\n<p>\n<i><b>Contextualized reading:</b></i>  Since the Hopper is the underlying code base for the Perseus Digital Library, \nit reflects that same emphasis on being an integrated reading environment: much of its power derives not \nsimply from isolated textual services, but in the knowledge that emerges from the interaction of texts \n");
      out.write("themselves. Users can take advantage of this contextualization with the Greco-Roman and Arabic texts \nprovided, or specify themselves the higher-level relationship between their own texts (e.g., that document \nX <i>is a translation of</i> document Y) in order to create a reading environment where passages in a source text \nare accompanied by secondary resources such as translations and commentaries.  Contextualized reading also \nintersects with linguistic support -- since dictionaries are also supported as \"secondary\" resources, a \nreader can find not simply what dictionary entry a word in a source text is derived from, but also a \ndefinition of what that word means.  The library environment also includes an architecture for soliciting \nuser contributions in the form of \"voting\" -- this is implemented online in the Perseus Digital Library in \nthe form of user votes for morphological forms, but can be extended as well to accommodate other varieties \nof annotation.\n</p>\n<p>\n<i><b>Searching:</b></i> Users can not only read passages from texts, but use a suite of search tools to find what \n");
      out.write("they are looking for, in any of the languages the Hopper supports.  These search tools include word and \nphrase searches, in individual texts or collections. These searches include the option to search all possible \ninflections of a word, making them extremely powerful for morphologically rich languages like Greek, Latin \nand Arabic (e.g., a lemmatized search for the root form <i>sum</i> would also find documents containing the \ninflected forms <i>est</i> and <i>sunt</i>).  For Classical texts, which have a well-adopted citation scheme, users can \nnavigate a text by typing canonical abbreviations (e.g., Thuc. 1.24).  The Hopper also provides functionality \nto search and browse the tagged named entities (places, people, dates, and date ranges) in a corpus, and \nincludes an architecture for presenting archaeological artifact and image data, which is separate from the \nreading environment.\n</p>\n</p>\n\n<p><b>Extensibility</b>\n<p>\nThe code base itself invites two varieties of extensibility. On the one hand, while the code is bundled with \n");
      out.write("a collection of Greco-Roman and Arabic texts around which it has grown, users are able to include their own \nTEI-compliant XML texts as part of the reading environment and enable the same services for those texts as \nthose that are available online for Perseus' open-source editions.   As an API, the Perseus hopper also \nincludes a number of Java classes for interacting with texts outside of a reading environment -- one can, \nfor instance, use the linguistic services such as automatic lemmatization or morphological analysis as \nstandalone tools for analyzing not simply the bundled Perseus texts, but any text of their own as well.\n</p>\n<p>\nOn the other hand, the Java code itself is also designed with modularity and extensibility in mind. An \nexample of this is the variety of classes (all ultimately inherited from CorpusProcessor) to cycle through \nan entire collection of texts and perform some operation on each one.  The workflow to build the library \nenvironment relies on these classes to calculate word and lemma statistics for the corpus at large, to map \n");
      out.write("citations between texts, and to index them all in order to make them searchable later.  These classes are \neasily extensible for any task that requires iterating through an entire collection of texts.  The hopper \nsource code also includes a number of services for managing named entities such as people and places, and \nhas served as the foundation for visualization projects, plotting that data both geographically on a map and \nhistorically on a timeline. In terms of modularity, the hopper also includes a number of low-level classes \nfor manipulating text -- from finding all possible lemmas for a given Latin form to delimiting an accented \nGreek word.\n</p>\n</p>\n\n</div> <!-- opensource div -->\n</div> <!-- main_col div -->\n\n</div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n\n</body>\n</html>\n");
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
