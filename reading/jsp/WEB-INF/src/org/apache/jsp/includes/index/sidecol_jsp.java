package org.apache.jsp.includes.index;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class sidecol_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

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

      out.write("<div id=\"side_col\">\n    <div id=\"texts\" class=\"box\">\n\t<h4>Popular Texts</h4>\n\t<ul>\n\t    <li>Caesar, <span class=\"title\">Gallic War</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.02.0001\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.02.0002\">Latin</a>)</li>\n\t    <li>Catullus, <span class=\"title\">Carmina</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.02.0005\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.02.0003\">Latin</a>)</li>\n\t    <li>Cicero, <span class=\"title\">In Catilinam I</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.02.0019:text=Catil.:speech=1:chapter=1\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.02.0010:text=Catil.\">Latin</a>)</li>\n\t    <li>Vergil, <span class=\"title\">Aeneid</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.02.0054\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.02.0055\">Latin</a>)</li>\n\t    <li>Herodotus, <span class=\"title\">Histories</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.01.0126\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.01.0125\">Greek</a>)</li>\n");
      out.write("\t    <li>Homer, <span class=\"title\">Odyssey</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.01.0136\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.01.0135\">Greek</a>)</li>\n\t    <li>Plato, <span class=\"title\">Republic</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.01.0168\">English</a>, <a href=\"/hopper/text?doc=Perseus:text:1999.01.0167\">Greek</a>)</li>\n\t    <li>Tom Martin, <span class=\"title\">Overview of Classical Greek History from Mycenae to Alexander</span> (<a href=\"/hopper/text?doc=Perseus:text:1999.04.0009\">English</a>)</li>\n\t</ul>\n    </div>\n    <div id=\"artobj\" class=\"box\">\n\t<h4>Art and Archaeology</h4>\n\t<table style=\"text-align: center; margin-left: auto; margin-right: auto;\">\n\t<tr>\n\t<td>\n\t<a href=\"/hopper/artifact?name=Aegina,+Temple+of+Aphaia&object=Building\">\n\t    <img class=\"thumb_img\" src=\"http://images.perseus.tufts.edu/images/thumbs/1987.09.5/1987.09.2169\">\n\t</a>\n\t<div class=\"caption\">\n\tAegina, Temple of Aphaia\n\t</div>\n\t</td>\n\t\n\t<td>\n\t<a href=\"/hopper/artifact?name=BCMA+1914.6.6&object=Coin\">\n");
      out.write("\t\t<img class=\"thumb_img\" src=\"http://images.perseus.tufts.edu/images/thumbs/1989.00.1/1989.00.0214\">\n\t</a>\n\t<div class=\"caption\">\n\tSilver obol from Athens\n\t</div>\n\t</td>\n\t</tr>\n\t\n\t<tr>\n\t<td>\n\t<a href=\"/hopper/artifact?name=Baltimore,+Hopkins+AIA+B3&object=Vase\">\n\t    <img class=\"thumb_img\" src=\"http://images.perseus.tufts.edu/images/thumbs/1990.06.1/1990.06.0255\">\n\t</a>\n\t<div class=\"caption\">\n\tSatyr on Attic red figure vase\n\t</div>\n\t</td>\n\t<td>\n\t<a href=\"/hopper/artifact?name=Boston+03.743&object=Sculpture\">\n\t    <img class=\"thumb_img\" src=\"http://images.perseus.tufts.edu/images/thumbs/1992.03.1/1992.03.0464\">\n\t</a>\n\t<div class=\"caption\">\n\tThe Barlett Head\n\t</div>\n\t</td>\n\t</tr>\n\t</table>\n\t\n    </div> <!-- artobj -->\n    <div id=\"exhibits\" class=\"box\">\n    <h4>Exhibits</h4>\n\t\t    <a href=\"http://www.perseus.tufts.edu/Olympics/\"><img src=\"/img/olicon.gif\" alt=\"The Ancient Olympics\" /></a>\n\t\t    <a href=\"http://www.perseus.tufts.edu/Herakles/\"><img src=\"/img/herc.gif\" alt=\"Hercules: Greece's Greatest Hero\" /></a>\n");
      out.write("\t</div>\n\t\n    <div id=\"featured_sites\" class=\"box\">\n\t<h4>Featured Sites</h4>\n\t<ul>\n\t    <li><a target=\"_blank\" href=\"http://www.stoa.org/\"\n\t    onclick=\"javascript: pageTracker._trackPageview('/outgoing/Stoa');\">Stoa</a>: Open Access Publication\n\t    <ul>\n\t\t<li><a target=\"_blank\"\n\thref=\"http://www.stoa.org/projects/demos/home\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/StoaDemos');\">Demos</a></li>\n\t    </ul>\n\t    <li><a target=\"_blank\"\n\thref=\"http://archimedes.fas.harvard.edu/\" onclick=\"javascript:\n\tpageTracker._trackPageview('/outgoing/Archimedes');\">Archimedes</a>\n\t    <li><a target=\"_blank\" href=\"http://www.tei-c.org\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/TEI');\">The Text Encoding Initiative</a></li>\n\t    <li><a target=\"_blank\" href=\"http://dl.tufts.edu/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/TuftsDL');\">Tufts Digital Library</a></li>\n\t    <li><a target=\"_blank\"\n\thref=\"http://chs.harvard.edu/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/CHS');\">Center for Hellenic Studies</a></li>\n");
      out.write("\t    <li><a target=\"_blank\" href=\"http://arachne.uni-koeln.de/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Arachne');\">Arachne (DAI object database)</a></li>\n\t    <li><a target=\"_blank\" href=\"http://www.dainst.org/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/DAI');\">Deutsches Arch&auml;ologisches Institut</a></li>\n\t    <li><a target=\"_blank\"\n\thref=\"http://www.uni-mannheim.de/mateo/camenahtdocs/camena_e.html\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Camena');\">Camena-Latin\n\tTexts of Early Modern Europe</a></li>\n\t    <li><a target=\"_blank\" href=\"http://www.nyu.edu/isaw/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/ISAW');\">Institute for\n\tthe Study of the Ancient World</a></li>\n\t    <li><a target=\"_blank\"\n\thref=\"http://www.insaph.kcl.ac.uk/index.html\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Aphrodisias');\">Inscriptions of Aphrodisias</a></li>\n\t    <li><a target=\"_blank\"\n\thref=\"http://www.internetcentre.imperial.ac.uk/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/ICL');\">Imperial College London Internet Centre</a></li>\n");
      out.write("\t    <li><a target=\"_blank\"\n\thref=\"http://itreebank.marginalia.it/\" \n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/Thomisticus');\">Index Thomisticus</a></li>\n\t\t<li><a target=\"_blank\"\n\thref=\"http://perseus.uchicago.edu/\"\n\tonclick=\"javascript: pageTracker._trackPageview('/outgoing/PhiloLogic');\">Perseus under PhiloLogic</a></li>\n\t</ul>\n    </div> <!-- featured_sites -->\n</div> <!-- side_col -->\n");
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
