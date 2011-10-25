package org.apache.jsp.index.about;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class researchOpportunities_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Research Opportunities</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("about", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("research", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n    \t<div id=\"index_main_col\">\n\t\t<div id=\"research\">\n\t\t\n<p>\nAs a digital infrastructure for the humanities in general and classics in particular takes shape, \nan unprecedented range of opportunities are emerging whereby students can contribute tangibly to \ntheir disciplines at an early stage of their education.  New collections and services allow \nundergraduates to conduct meaningful research projects, the results of which can be disseminated \nimmediately, linked to the primary sources to which they contribute and preserved in institutional \nrepositories for generations as part of the core collections on which humanities research depends.\n</p>\n<p>\nDuring the 2008-09 academic year, we particularly encourage students and classes to help produce, \nand to construct research projects based upon, treebanks for classical Greek and Latin now under \ndevelopment. A treebank is a large collection of syntactically parsed sentences, in which an annotator \nhas specified the exact syntactic relationship for every word in a sentence (e.g., what the subject \n");
      out.write("is, what the object is, where the prepositional phrase should be attached, which adjective modifies \nwhich noun, etc.) along with each word's morphological analysis (feminine singular nominative \nadjective) and the dictionary entry it's derived from (est as an inflection of <em>edo</em> rather than \n<em>sum</em>).  The following diagram represents a syntactic annotation of <em>ista meam norit \ngloria canitiem</em> (\"that glory will know my old age\") from Propertius 1.8.\n</p>\n<div style=\"text-align: center;\">\n<p>\n<img src=\"/img/ista.jpg\">\n</p>\n</div>\n<p>\nTreebanks will be the most important new resources for the study of Greek and Latin that have appeared \nsince the grand scholarly projects of 19th century philology.  On the one hand, treebanks make a whole \nnew generation of reading support possible: we will be able not only to ask what individual words mean \nbut also to view analyses of the syntactic structures of individual sentences.  Treebanks, however, \nalso allow us to place our current understanding of lexicography, linguistics and style on a wholly new, \n");
      out.write("quantifiable, transparent basis and to ask new questions about Greek and Latin that were not feasible \nwith print tools.  Treebanks are the classical equivalent of the genome for the life sciences.  We have \nreleased the first 50,000 word treebank for classical Latin and are beginning a major effort that is \ndesigned to produce a treebank with 1,000,000 words of classical Greek.\n</p>\n<p>\nWe urge classes and students of Greek and Latin to contribute to this larger effort in a variety of ways:\n</p>\n<ul>\n\t<ol>\n\t<p>\n\t<b>Create treebanks for particular works or sections of Greek and Latin and contribute these to \n\tthe larger Greek and Latin Treebanks</b>: Each sentence in the treebank lists who produced the \n\tsyntactic analysis. These producers can be individuals or groups such as classes. In our work so \n\tfar, two separate annotators have analyzed each sentence and an editor has gone through to resolve \n\tdiscrepancies to produce a single database with one best analysis per word.  Instructors can use \n\tthis model with classes, providing a final set of syntactic analyses that cites the students \n");
      out.write("\tindividually or the class as a whole. Individual contributors can submit their own syntactic \n\tanalyses for review and publication. This exercise will provide a mechanism with which students \n\tcan think about Greek and Latin in new ways while contributing to the sum of knowledge about the \n\tlanguages themselves. Many passages of Greek and Latin admit, of course, of multiple syntactic \n\tinterpretations.  We can accept data that includes multiple versions of the same sentence or \n\talternate interpretations for sentences already in the treebank.\n\t</p>\n\t</ol>\n\t<ol>\n\t<p>\n\t<b>Publish variant readings and accompanying annotations</b>: Some readings may have no impact \n\tupon the syntactic structure of a sentence (although substituting one verb or noun for another \n\tmay have a huge impact on the meaning).  Other readings will require substantial revision of \n\tthe syntactic analysis.  Adding syntactically interpreted analyses will allow us to evaluate \n\tthe syntactic impact of different editorial choices between editions and thus provides an \n");
      out.write("\tessential component for true digital editions.\n\t</p>\n\t</ol>\n\t<ol>\n\t<p>\n\t<b>Publish documented annotations with alternate interpretations</b>: In this case, we do not \n\tsimply list alternate interpretations but provide evidence that supports each interpretation. \n\tIn practice, this involves comparing passages where the structure is uncertain with similar \n\tpassages where the structure is less problematic.  As our treebanks grow in size, we will be \n\table to use them to place our intepretations on a basis that not only allows for quantification \n\tbut is transparent:  others can go beyond the numbers and check for themselves each passage \n\ton which the numbers are based. As the treebanks are still growing in size, however, we may \n\tfind it easier to conduct the more focused studies listed next.\n\t</p>\n\t</ol>\n\t<ol>\n\t<p>\n\t<b>Conduct original research on Greek and Latin lexicography, linguistics or stylistic analysis \n\tand publish the results with hypertextual links directly connecting your conclusions with the \n\tpassages on which they comment</b>: Even a treebank with one million words covers only a small \n");
      out.write("\tpercentage of classical Greek or Latin. You can build on pre-existing treebanks and/or contribute \n\tnew data to ask new questions that have never before been possible. The rising generation of \n\tstudents has an opportunity to develop a cumulative set of research projects that will shed \n\tincreasing light upon Greek and Latin. Each research publication will be submitted to editorial \n\treview and, if accepted, stored permanently in the Perseus Digital Library and the emerging \n\tdistributed Scaife Digital Library. This research can be as narrowly focused as \"Does Cicero's \n\tuse of the passive voice differ between his letters to Atticus and his Phillipics?\" or as broad \n\tas \"the use of &#960;&#959;&#953;&#941;&#969; in Greek.\"  Other options (to give you some ideas) include the following:\n\t</p>\n\t<ul>\n\t\t<li>\n\t\t<p>\n\t\t<b>Lexicographical</b>: Traditional dictionaries like the Oxford Latin Dictionary and the \n\t\tLSJ provide plentiful citations to support their definitions of words, usually with the most \n\t\tfrequent sense up front.  Are their judgments reflected in actual usage?  Does it differ \n");
      out.write("\t\tbetween authors?\n\t\t</p>\n\t\t</li>\n\t\t<li>\n\t\t<p>\n\t\t<b>Linguistic</b>: Classical Latin has been thought to use a word order in which the verb \n\t\tfollows the direct object (SOV), but the word order of its daughter languages (like Italian \n\t\tand Spanish) puts the verb before the direct object (SVO).  Did Classical Latin actually \n\t\thave this SOV word order, and if so, can we chart how it changed?\n\t\t</p>\n\t\t</li>\n\t\t<li>\n\t\t<p>\n\t\t<b>Stylistic</b>: What kind of verbs is Sallust's Catiline the subject of (i.e., what kind \n\t\tof actions does he undertake?)  How does this compare to the actions that Cicero attributes \n\t\tto him in his own text (<em>In Catilinam</em>)?  Is one more sympathetic than the other?\n\t\t</p>\n\t\t</li>\n\t</ul>\n\t</ol>\n</ul>\n<p>\nYou do not have to create a complete treebank for an author or work to generate important results \nif you create a thoughtful subset. We also support forums whereby people working on the Treebank \ncan identify the passages of interest to themselves, take responsibility for particular passages \n");
      out.write("and discuss questions about how to annotate them. By coordinating efforts, we can combine the work \nof many classes to provide much broader coverage for individual texts, authors and genres.\n</p>\n<p>\nInterested students and faculty should contact <a href=\"mailto:david.bamman@tufts.edu\">David \nBamman</a> or <a href=\"mailto:gregory.crane@tufts.edu\">Gregory Crane</a> at Perseus. Content \nsubmitted by February 1, 2009, will be included in May 2009 release of the classical Greek and \nLatin treebanks.\n</p>\n\n</div> <!-- research div -->\n</div> <!-- main_col div -->\n\n</div> <!-- 2column div -->\n</div> <!-- main div -->\n\t\t\n<!-- Google Analytics --> \n");
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
