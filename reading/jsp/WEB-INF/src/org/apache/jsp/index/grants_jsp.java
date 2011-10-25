package org.apache.jsp.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class grants_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n<head><title>Grants</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\"/>\n<script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n</head>\n<body onload=\"checkRedirect();\">\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("grants", request.getCharacterEncoding()), out, false);
      out.write(" \t\n    \n    <div id=\"content\" class=\"2column\">\n        <div id=\"index_main_col\">\n\t\t<div id=\"grants\">\n\t\t\t\n<p>\nSince we submitted our first pre-proposal for the Perseus Project in September 1985, we have \nreceived generous support from many sources.  These include major support from the Annenberg/CPB \nProjects (which invested $2.5 million with which the project began planning and developing \ncollections on classical Greece in 1987) and the Digital Library Initiative Phase 2 (which \nprovided $2.8 million in 1998 and allowed us to explore the issues of digital libraries for \nthe humanities in general).  The National Endowment for the Humanities, the National Science \nFoundation, the Institute for Museum and Library Services, the Fund for the Improvement for \nPostsecondary Education, the Department of Education, the Mellon Foundation, and the National \nEndowment for the Arts have all provided generous support.  We also gratefully thank private \nindividuals who have supported our research over the years. For an overview of the research that \n");
      out.write("we conducted, see <a href=\"/hopper/research/background\">here</a>.\n</p>\n<p>\nFor an overview of the research that we are currently pursuing, see \n<a href=\"/hopper/research/current\">here</a>. The following lists currently active grants that support \nthat research according the order in which they were funded.\n</p>\n<p>\n<ul>\n\t<li>\n\t<p>\n\t<a name=\"arabic\"></a><em>A Reading Environment for Arabic</em>: <u>Department of Education</u>: (2006: $432,000): \n\tThis grant has allowed us first to extend the reading support infrastructure already available \n\tfor Greek and Latin to Arabic as well, thus creating a much larger potential community to support \n\tthe same underlying infrastructure. At the same time, this grant is also allowing us to improve \n\tour ability to provide customized vocabulary support: given a reader familiar with vocabulary \n\tthrough, for example, unit six of volume 2 of the Al-Kitaab Arabic textbook, what words in a given \n\tchunk of Arabic are new? What chunks of Arabic would best match that reader's current vocabulary? \n");
      out.write("\tAs an initial sample of Arabic texts, we will adapt a version of Arabic Wikipedia. This project \n\tinvolves the publication of an Arabic-enabled version of the Perseus Digital Library system and \n\tintegration of that system with a Fedora Institutional Repository back end. A system that identifies \n\tArabic, Greek and Latin on an arbitrary web page and then generates links to dictionaries in all \n\tthree languages is available for download.\n\t</p>\n\t</li>\n\t<li>\n\t<p>\n\t<a name=\"scale\"></a><em>Scalable Named Entity Services for Classical Studies</em>: <u>National Endowment for the \n\tHumanities and the Institute for Museum and Library Services</u>: (2007: $349,939): This extends \n\twork done on named entity analysis of 19th century American historical documents to publications \n\tabout classical studies. There are four main goals of the project. First, we will produce a \n\tthesaurus of the most common proper names from Greco-Roman antiquity in Greek, Latin, English, \n\tFrench, German and Italian. Second, we will publish more fully tagged versions of the Smith's \n");
      out.write("\tdictionaries of Geography and of Biography, providing basic information for 20,000 people and \n\t10,000 places. Third, we will aggregate as many print-indices into a single digital database of \n\tpeople and places in particular passages. Fourth, we will apply named entity analysis to a testbed \n\tof materials about Greco-Roman antiquity.\n\t</p>\n\t</li>\n\t<li>\n\t<p>\n\t<a name=\"lexicon\"></a><a href=\"http://dl.tufts.edu/view_pdf.jsp?pid=tufts:PB.001.002.00003\"><em>The Dynamic Lexicon</em></a>: \n\t<u>The National Endowment for the Humanities</u>: (2008: $284,999).  This project involves creating \n\tnew reference works for Greek and Latin from a large collection of texts and structured knowledge \n\tsources (such as treebanks) within the cyberinfrastructure of a digital library.  Built on the \n\ttechnologies of parallel text analysis (including word sense induction and disambiguation) and automatic \n\tsyntactic parsing, these reference works will allow us to present the possible senses for any Greek or \n\tLatin word while also providing syntactic information and statistical data about its use in any \n");
      out.write("\tcollection of texts or any subset of that collection - not simply, for example, how <em>oratio</em> is \n\tused in all of Latin literature, but only within the works of Cicero (where it means \"oration\" or more \n\tgenerally the power of oratory) or the works of Jerome (where it means \"prayer\"), including quantified \n\tmeasures of its syntactic usage.  These methods will also let users search a text not only by word \n\tform, but also by word sense, syntactic subcategorization and selectional preference. \n\t</p>\n\t</li>\n\t<li>\n\t<p>\n\t<a name=\"cybereditions\"></a><em>Cybereditions</em>: <u>The Mellon Foundation</u>: (2008: $471,000). This project explores the \n\tproblem of mining high value data for a demanding scholarly audience from the image books in \n\temerging large digital collections.  Our work begins where the services currently under development \n\tby libraries and by Internet giants such as Google end - we seek to identify ways by which we can \n\tbridge the gap between those general services and the services that we will need so that \n");
      out.write("\tcyberinfrastructure can support scholars working with textual materials. \n\t</p>\n\t<p>\n\tWe are building a workflow that leads from page image to actionable data.  Humanists need access to \n\tthe earliest phases of processing - we need to be able to define the page layouts of editions and \n\tcommentaries and to recognize languages such as classical Greek for which general-purpose optical \n\tcharacter recognition (OCR) engines provide little support.  An application programming interface \n\t(API) that provides access to the searching or other services does little good if the crucial data \n\thas already been lost.\n\t</p>\n\t<p>\n\tThis project will result in three basic deliverables.  First, we will produce a testbed of image \n\tbooks with editions, commentaries and translations of the major classical authors, often in multiple \n\teditions, that survive from antiquity.  We will make this testbed available as a part of the Open \n\tContent Alliance (OCA), where it will be freely available.  Second, we will provide documentation \n");
      out.write("\tand evaluate methods for each stage of the workflow.  Third, we will provide the code and data sets \n\tthat we produce under a creative commons license.  Data sets in this case will include the textual \n\tdata that we have been able to extract, with automatically added markup.  This markup will include \n\tautomatically suggested corrections as well as original OCR output (allowing for flexible searching).\n\t</p>\n\t</li>\n\t<li>\n\t<p>\n\t<a name=\"philogrid\"></a><em>PhiloGrid</em>: <u>The National Endowment for the Humanities and the Joint Information Systems \n\tCommittee</u>: (2008: $240,000 shared evenly with <em>Imperial College London</em>) PhiloGrid, a \n\tcollaboration of the Perseus Digital Library at Tufts University in the United States and the \n\tInternet Centre at Imperial College London in the UK, proposes to create an expandable, Grid-enabled, \n\tweb service-driven virtual research environment for Greco-Roman antiquity based initially upon \n\topen-source texts and services from the Perseus Digital Library. First, we will add to the Perseus \n");
      out.write("\tDL Greek historians who exist only in fragmentary form. This task goes beyond simple data entry: we \n\twill create the first major digital collection of fragmentary authors designed from the start to \n\tinteract with multiple source editions. Second, we will create a repository of philological data \n\tabout the Greco-Roman world seeded with twenty years' worth of Perseus materials. The objects that \n\twe create will not only include books but every labeled object within each logical document. Third, \n\twe will convert the workflow that has evolved over the past ten years to process textual materials \n\tin Perseus into a grid-enabled workflow based on web services that can be applied to and customized \n\tfor many collections. Although this project will concentrate upon the classics collections in the \n\tPerseus DL, the new workflows will also process non-classical Perseus content, and will thus from \n\tthe start demonstrate their generality.\n\t</p>\n\t</li>\n\t<li>\n\t<p>\n\t<a name=\"greektreebank\"></a><em>The Ancient Greek Treebank Project</em>: \n");
      out.write("\t<a target=\"_blank\" href=\"http://alpheios.net\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Alpheios');\"><u>Alpheios Project</u></a>: (2008: $865,290). This project \n\twill enable us to create a treebank - a large collection of syntactically parsed sentences - for ca. \n\tone million words of Ancient Greek texts.  Treebanks are fundamental datasets that provide not only \n\treading support for students of Classical texts (for example, noting the subject of the sentence and \n\twhich adjectives modify which nouns), but also provide the basic quantitative data on which to build \n\tlarger linguistic and general philological arguments (see our call for *research opportunities*).  \n\tThe majority of the texts will consist of Homer, the tragedians and Plato, with selections from several \n\tother Classical authors as well.  This work complements our ongoing work on creating a Latin treebank.\n\t</p>\n\t</li>\n\t\n\t<li>\n\t<p>\n\t<a name=\"million\"></a><em><a href=\"/hopper/grants/million.jsp\">Mining a Million Scanned Books: Linguistic and Structure Analysis, Fast Expanded \n");
      out.write("\tSearch, and Improved OCR</a></em>: (2009). This project provides effective analysis and search for scholars and \n\tthe general public, and to handle the diversity and scale of these collections, this project focuses on \n\timprovements in seven interlocking technologies: improved OCR accuracy through word spotting, creating \n\tprobabilistic models using joint distributions of features, and building topic-specific language models across \n\tdocuments; structural metadata extraction, to mine headers, chapters, tables of contents, and indices; \n\tlinguistic analysis and information extraction, to perform syntactic analysis and entity extraction on noisy \n\tOCR output; inferred document relational structure, to mine citations, quotations, translations, and paraphrases; \n\tlatent topic modeling  through time, to improve language modeling for OCR and retrieval, and to track the spread \n\tof ideas across periods and genres; query expansion for relevance models, to improve relevance in information \n\tretrieval by offline pre-processing of document comparisons; and interfaces for exploratory data analysis, \n");
      out.write("\tto provide users of the document collection with efficient tools to update complex models of important \n\tentities, events, topics, and linguistic features.\n\t</p>\n\t</li>\n</ul>\n</p>\n\n\t\t</div> <!-- grants div -->\n\t</div> <!-- main_col div -->\n        <div id=\"img_side_col\" style=\"font-size:small; text-align:left\">\n<h4>Grants</h4>\n<ul>\n<li><a href=\"#arabic\">A Reading Environment for Arabic</a></li>\n<li><a href=\"#scale\">Scalable Named Entity Services for Classical Studies</a></li>\n<li><a href=\"#lexicon\">The Dynamic Lexicon</a></li>\n<li><a href=\"#cybereditions\">Cybereditions</a></li>\n<li><a href=\"#philogrid\">PhiloGrid</a></li>\n<li><a href=\"#greektreebank\">The Ancient Greek Treebank Project</a></li>\n<li><a href=\"#million\">Mining a Million Scanned Books</a></li>\n</ul>\n</div>\n    </div> <!-- 2column div -->\n</div> <!-- main div -->\n\n<!-- Google Analytics --> \n");
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
