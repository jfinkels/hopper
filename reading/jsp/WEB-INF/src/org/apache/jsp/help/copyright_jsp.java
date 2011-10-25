package org.apache.jsp.help;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class copyright_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("<html>\n  <head>\n    <title>Perseus Copyrights</title>\n    <link href=\"/css/hopper.css\" type=\"text/css\" rel=\"stylesheet\"/>\n    <script type=\"text/javascript\" src=\"/js/hopper.js\"></script>\n  </head>\n  <body onload=\"checkRedirect();\">\n    ");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n    \n    <div id=\"main\">\n      ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("help", request.getCharacterEncoding()) + "&" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("subtabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("copyright", request.getCharacterEncoding()), out, false);
      out.write("\n      \n      <div id=\"content\" class=\"2column\">\n\t<div id=\"index_main_col\">\n\t  <h3>Perseus Copyrights &amp; Warranty</h3>\n\t  <ul>\n\t    <li type=\"disc\"><a href=\"#copy\">Copyright\n\t    </a><li type=\"disc\"><a href=\"#down\">Downloads</a><li type=\"disc\"><a href=\"#warranty\">Warranty</a><li type=\"disc\"><a href=\"#links\">Links to Perseus</a>\n\t    <li type=\"disc\"><a href=\"#cite\">Citing this Web site in a bibliography</a>\n\t    <li type=\"disc\"><a href=\"#text\">Special Text Agreements</a>\n\t    <li type=\"disc\"><a href=\"#images\">Images</a>\n\t    <li type=\"disc\"><a href=\"#fair\">Fair Use</a>\n\t  </ul>\n\t  <a id=\"anchor\" name=\"copy\"></a><b>Copyright</b><br>\n\t  \n\t  The Trustees of Tufts College hold the overall copyright to the Perseus Digital Library; the materials therein (including all texts, translations, images, descriptions, drawings, etc.) are provided for the personal use of students, scholars, and the public. Any commercial use or publication without authorization is strictly prohibited. Materials within the Perseus DL have varying copyright statues: please <a href=\"mailto:webmaster@perseus.tufts.edu\">contact the project</a> for more information about a specific component or object. Copyright is protected by the copyright laws of the United States and the Universal Copyright Convention. <font size=\"-1\"><p><a href=\"http://www.yale.edu/yup/\">Yale University Press</a> distributes the <!--<a href=\"order.html\">-->CD ROM version<!--</a>--> of the Perseus Greek materials.</p></font>\n");
      out.write("\t  <p><a id=\"anchor\" name=\"down\"></a><b>Downloads</b><br>Perseus offers several open source downloads of this site and its components:\n\t    \n\t    <ul><li><b>Site:</b>  The Perseus 4.0 site (also known as the Perseus Hopper) is available via an open source code release.  <ul>\n\t\t  <li>Download the latest release of source code\n\t\t    from <a href=\"http://sourceforge.net/projects/perseus-hopper\">SourceForge</a>.</li>\n\t\t  <li>Download the text files or data <a href=\"/hopper/opensource/\"> here</a>.</li>\n\t\t</ul>\n\t\t<br><li><b>Texts:</b> Public domain texts are available as XML source downloads both in sections and as full texts, as are many text-based materials such as searches and word analysis results. <ul><li>To download a section of the work you are viewing or the results of a search or analysis, look for this link <img src=\"/img/xml.gif\">. <li>If a full-text download is offered, this will be noted following the Creative Commons license. Look for the &quot;XML version&quot; link below the license information.</ul>\n");
      out.write("\t\t<br>\n\t      <li><b>Art &amp; Archaeology module: </b>The Perseus Art &amp; Archaeology catalog of over 4,000 Greco-Roman objects and artifacts is available as a separate <a href=\"http://sourceforge.net/projects/perseus-artarch/\">open source module</a>. (You may also <a href=\"/hopper/artifactBrowser\">browse this data</a> on-line.)\n\t    </ul>\n\t    <a id=\"anchor\" name=\"warranty\"></a><b>Warranty</b><br>We provide access to this WWW version as an act of collegiality but without any guarantees, implicit or otherwise. We make no guarantees about access to any materials at any time or about the accuracy of any contents of the Perseus Digital Library.<p><a id=\"anchor\" name=\"links\"></a><b>Links to Perseus</b><br>\n\t    We welcome all links to the Perseus site and to any page therein provided that you properly attribute the source of the data and add a pointer, whenever appropriate.  Links to Perseus are always preferable to any copying of materials. Links provide a foundation for scholarly conversation across the WWW, linking avoids confusion of source attribution and gives credit for the work of the project, and some materials in Perseus are governed by special copyrights which prohibit reproduction outside of the Perseus environment, and thus should never be copied onto on another site without permission.</p>\n");
      out.write("\t  <p><a id=\"anchor\" name=\"cite\"></a><b>Citing this Web site in a bibliography<br>\n\t    </b>\n\t    If you wish to cite the Perseus Web site in a bibliography, please list it with\n\t    the URL, and the month and year you accessed it, as in the following example:\n\t    \n\t    \n\t  <p><u>Perseus Digital Library Project</u>. Ed. Gregory R. Crane. <i>date of last site update (see below).</i> Tufts University. <i>date you accessed the site &lt;</i>http://www.perseus.tufts.edu&gt;.</p>\n\t  <p>Note that the main Perseus Digital Library is updated daily. This is only a recommended format. Consult a style guide, your editor, or your instructor for the requisite bibliographic format for your paper or project.</p>\n\t  <P><a id=\"anchor\" name=\"text\"></a><b>Special Text Agreements</b><br>\n\t    Some texts in Perseus are used by agreement with the publishers who hold their copyrights.<blockquote>\n\t      <a href=\"http://www.hup.harvard.edu/loeb/\">\n\t\tLoeb Classical Library</a>\n\t      <br>\n\t      <a href=\"http://www.hup.harvard.edu/\">Harvard University Press</a><br>\n");
      out.write("\t      79 Garden Street<br>\n\t      Cambridge, MA 02138<br>\n\t      <p>\n\t\t<a href=\"http://www.oup-usa.org/\">Oxford University Press</a><br>\n\t\t\n\t\tWalton Street<br>\n\t\tOxford OX2 6DP<br>\n\t      <p>\n\t\t<a href=\"http://www.pullins.com\">The Focus Classical Library</a><br>\n\t\t10 State Street<br> PO Box 369<br>\n\t\tNewburyport MA 01950<br>\n\t    </blockquote>\n\t  <p>\n\t    Translations for Bacchylides and Pindar were done by Diane Svarlien for Perseus.\n\t    \n\t    \n\t    \n\t  <p><a id=\"anchor\" name=\"images\"></a><b>Images<br>\n\t    </b>Perseus does not provide volume redistribution of any images; nor do we have the staff to provide immediate assistance with image reproduction. If you wish on acquiring an image found in Perseus, please do the following:<br>\n\t  </p>\n\t  <p><font size=\"-1\">1) <b>Check the credit line.</b> All Perseus images contain a credit line. If the credit includes a museum, publisher, or other organization, you must contact that group for permission prior to contacting Perseus. Groups for which we have contact information are listed below. All of the information we have is listed here. (We either do not have current e-mail or telephone numbers for these groups, or they wish to be contacted only in writing.)<br>\n");
      out.write("\t      2) Provide the <b>Perseus acquisition number or URL</b> to the image page in which you are interested. Every Perseus image is referenced by a ten digit number of the format 9999.99.9999, where the first four digits are a year. This may be found at the top of the browser window or as part of the URL. Please include this when inquiring about an image or its status.<br>\n\t      3) <b>Allow time to process your request.</b> You will be asked to provide proof of granted permission from the copyright holder, where applicable. Since most of the groups listed prefer mail requests, you should allow time for this process as well as five to ten business days for Perseus to process your request (after we have received it). Since we are not an image repository, the quality of images we offer is not print-quality, and we need time to produce a suitable image.<br>\n\t      4) <b>Contact the <a href=\"mailto:webmaster@perseus.tufts.edu\">webmaster</a> with questions.</b> If you are unclear on the status of an image, or have other concerns, please contact the Perseus webmaster via e-mail. In order to assure that we see your request as soon as possible, <b>please include &quot;image permission request&quot; in the subject line of your message. We strongly prefer e-mail contact to phone or fax.</b></font></p>\n");
      out.write("\t  <p>Inquiries about obtaining rights to reuse images should be directed to the relevant museums:\n\t    \n\t    <blockquote>\n\t      <table border=0>\n\t\t<td valign=\"top\">\n\t\t  <p><i>For Berlin Museums:</i><br>\n\t\t    Art Resource<br>\n\t\t\t536 Broadway, 5th floor<br>\n\t\t    New York, NY 10012<br>\n\t\t    alternate:<br>\n\t\t    Bildarchiv Preussischer Kulturbesitz<br>\n\t\t    Postfach 61 03 17<br>\n\t\t    D-10925 Berlin<br>\n\t\t\tGermany<br>\n\t\t  \n\t\t  <p>\n\t\t    Dewing Greek Numismatic Foundation<br>\n\t\t    1566 A Washington Street<br>\n\t\t    P.O. Box 5973<br>\n\t\t    Holliston, MA  01746<br>\n\t\t  \n\t\t  <p>\n\t\t    Rights and Reproductions Office<br>\n\t\t    Harvard University Art Museums<br>\n\t\t    32 Quincy Street<br>\n\t\t    Cambridge, MA  02138<br>\n\t\t    <a href=\"http://www.artmuseums.harvard.edu/vr/index.html\">on-line request form</a><br>\n\t\t  \n\t\t  <p>\n\t\t    Indiana University Art Museum<br>\n\t\t    1133 E. 7th Street<br>\n\t\t    Bloomington, IN 47405-7509<br>\n\t\t    Attn: Adriana Calinescu<br>\n\t\t  \n\t\t  <p>\n\t\t\tJacklyn Burns<br>\n\t\t\tRights and Reproductions Coordinator<br>\n");
      out.write("\t\t\tPhoto Services<br>\n\t\t\tThe J. Paul Getty Museum<br>\n\t\t\t1200 Getty Center Drive<br>\n\t\t\tSuite 1000<br>\n\t\t\tLos Angeles, CA 90049-1687<br>\t\n\t\t</td>\n\t\t\n\t\t<td valign=\"top\">\n\t\t  <p>\n\t\t    Martin von Wagner-Museum<br>\n\t\t    der Universit&auml;t W&uuml;rzburg<br>\n\t\t    Antikenabteillung<br>\n\t\t    Residenzplatz 2, Tor A<br>\n\t\t    D-97070 W&uuml;rzburg<br>\n\t\t    Germany<br>\n\t\t  \n\t\t  <p>\n\t\t    Mus&eacute;e du Louvre<br>\n\t\t    Porte des Arts<br>\n\t\t    75058 Paris Cedex 01<br>\n\t\t    France<br>\n\t\t  \n\t\t  <p>\n\t\t    Rights and Reproductions<br>\n\t\t    Museum of Art<br>\n\t\t    Rhode Island School of Design<br>\n\t\t    Providence, RI 02903<br>\n\t\t  \n\t\t  <p>\n\t\t    Museum of Fine Arts - Enterprise<br>\n\t\t    580 Harrington Ave. Ste 301<br>\n\t\t    Boston, MA  02118-2440<br>\n\t\t    <a href=\"http://www.mfa.org/ip/drl.htm\">on-line request form</a><br>\n\t\t  \n\t\t  <p>\n\t\t    Royal Ontario Museum<br>\n\t\t    100 Queen's Park<br>\n\t\t    Toronto, Ontario M5S 2C6<br>\n\t\t    Canada<br>\n\t\t  \n\t\t  <p>\n\t\t    Michael Goodison<br>\n\t\t    Smith College Museum of Art<br>\n");
      out.write("\t\t    Northampton, MA 01063<br>\n\t\t</td>\n\t\t\n\t\t<td valign=\"top\">\n\t\t  <p>\n\t\t    Tampa Museum of Art<br>\n\t\t    600 N. Ashley Drive<br>\n\t\t    Tampa, FL 33602 <br>\n\t\t  \n\t\t  <p>\n\t\t    Toledo Museum of Art<br>\n\t\t    Registrar's Office<br>\n\t\t    P.O. Box 1013<br>\n\t\t    Toledo, OH  43697<br>\n\t\t  \n\t\t  <p>\n\t\t    University Museums<br>\n\t\t    The University of Mississippi<br>\n\t\t    University, MS  38677<br>\n\t\t  \n\t\t  <p>\n\t\t    The University Museum,<br>\n\t\t    University of Pennsylvania<br>\n\t\t    33rd and Spruce Streets<br>\n\t\t    Philadelphia, PA 19104<br>\n\t\t  \n\t\t  <p>\n\t\t    Williams College Museum of Art<br>\n\t\t    Main Street<br>\n\t\t    Williamstown, MA 01267<br>\n\t\t    Attn: Diane Agee, Museum Registrar<br>\n\t\t</td>\n\t    </table></blockquote>\n\t    \n\t    <!--The <a href=\"http://www.beazley.ox.ac.uk/\">Beazley Archive</a> at the\n\t\tUniversity of Oxford holds <a\n\t\thref=\"http://www.beazley.ox.ac.uk/BeazleyAdmin/Script2/WebSite.htm#Copyright\">copyright</a>\n\t\tto its database of Greek pottery.-->\n\t  <p><a id=\"anchor\" name=\"fair\"></a><b>Fair Use</b><br>\n");
      out.write("\t    For more information, see the Library of Congress documents on \n\t    <a href=\"http://lcweb.loc.gov/copyright/\">copyrights</a> and \n\t    <a href=\"http://lcweb.loc.gov/copyright/fls/fl102.pdf\">fair\n\t      use</a>. Researchers should read and consider the information provided\n\t    by the Library of Congress on its site and on the home page for each\n\t    American Memory collection regarding copyright and other legal rights\n\t    associated with materials in those collections.\n\t    \n\t</div> <!-- main_col -->\n      </div> <!-- 2column -->\n      \n    </div> <!-- main -->\n    \n    <!-- Google Analytics --> \n");
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
