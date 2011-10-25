package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;

public final class reporterror_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html;charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write('\n');

String host = "localhost";
String from = "noreply@perseus.tufts.edu";

String emailParam = request.getParameter("email");
if (emailParam != null && emailParam.length() > 0) {
    from = emailParam;
}

String to = "bugs@perseus.tufts.edu";

String subject = request.getParameter("subject") != null ? request.getParameter("subject") : "Error report";

String senderName = "Someone";
String nameParam = request.getParameter("name");
if (nameParam != null && nameParam.length() > 0) {
    senderName = nameParam;
}
String senderEmail = emailParam != null && emailParam.length() > 0
    ? emailParam : "no email given";

Properties props = System.getProperties();
props.put("mail.smtp.host", host);

StringWriter writer = new StringWriter();
PrintWriter printer = new PrintWriter(new BufferedWriter(writer));
printer.format("%tc%n%n", Calendar.getInstance())
    .format("Hello! This is an automated message from the website.%n%n")
    .format("%s (%s) has reported an error at the following URL: [%s].%n%n",
		senderName, senderEmail, request.getParameter("url"))
    .format("User-agent: %s%n%n", request.getHeader("User-Agent"))
    .format("Error details:%n%s: %s%n%nStack trace:%n%s",
	request.getParameter("exception-class"),
	request.getParameter("exception-message"),
	request.getParameter("exception-stack-trace"))
    .format("%n%nAdditional information, if any:%n%s%n",
	request.getParameter("additional_info"));

printer.close();

Session mailSession = Session.getDefaultInstance(props, null);

MimeMessage message = new MimeMessage(mailSession);
try {
    InternetAddress fromAddr = new InternetAddress(from);
    message.setFrom(fromAddr);
    message.setReplyTo(new InternetAddress[] { fromAddr });
} catch (AddressException ae) {
    // oh well
}
message.addRecipient(Message.RecipientType.TO,
    new InternetAddress(to));

message.setSubject("[Error report] " + subject);
message.setText(writer.toString());

Transport.send(message);


      out.write("\n<!DOCTYPE html \n     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html>\n<head>\n<title>Error Submitted</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/hopper.css\" />\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/error.css\" />\n</head>\n<body>\n");
      out.write("<div id=\"header\">\n\t<a id=\"banner\" href=\"/hopper/\">\n\t</a>\n\t<div id=\"header_side\">\n\t\t");
      out.write("<form action=\"/hopper/searchresults\" class=\"search_form\" onsubmit=\"return validate_form(this,q);\">\n    <input name=\"q\" />\n    <input type=\"submit\" value=\"Search\" />\n    <p>(\"Agamemnon\", \"Hom. Od. 9.1\", \"denarius\")</p>\n    <p><a href=\"/hopper/search\">All Search Options</a> [<a href=\"javascript:abbrev_help()\">view abbreviations</a>]</p>\n</form>\n");
      out.write("\n\t</div>\n</div>\n");
      out.write("\n\n<div id=\"main\">\n    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/includes/index/indexNav.jsp" + (("/includes/index/indexNav.jsp").indexOf('?')>0? '&': '?') + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("tabActive", request.getCharacterEncoding())+ "=" + org.apache.jasper.runtime.JspRuntimeLibrary.URLEncode("", request.getCharacterEncoding()), out, false);
      out.write("\n    <div id=\"content\" class=\"1column\">\n\t<h2>Thank you!</h2>\n\n\t<p>Your report has been submitted. Thanks for your help!</p>\n\n\t<p><a href=\"/hopper/\">Perseus Home Page</a></p>\n    </div> ");
      out.write("\n</div> ");
      out.write("\n\n<!-- Google Analytics --> \n");
      out.write("<script type=\"text/javascript\">\nvar gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\ndocument.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n</script>\n<script type=\"text/javascript\">\nvar pageTracker = _gat._getTracker(\"UA-4545594-2\");\npageTracker._initData();\npageTracker._trackPageview();\n</script>");
      out.write("\n\n</body>\n</html>\n\n");
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
