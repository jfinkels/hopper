<%@page import="
	    java.io.BufferedWriter,
	    java.io.PrintWriter,
	    java.io.StringWriter,
	    javax.mail.Message,
	    javax.mail.Session,
	    javax.mail.internet.AddressException,
	    javax.mail.internet.MimeMessage,
	    java.util.Calendar,
	    java.util.Properties,
	    javax.mail.internet.InternetAddress,
	    javax.mail.Transport"
        pageEncoding="utf-8"
        contentType="text/html;charset=utf-8"%>

<%
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

%>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Error Submitted</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
<link rel="stylesheet" type="text/css" href="/css/error.css" />
</head>
<body>
<%@ include file="/includes/index/header.jsp" %>

<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
    </jsp:include>
    <div id="content" class="1column">
	<h2>Thank you!</h2>

	<p>Your report has been submitted. Thanks for your help!</p>

	<p><a href="/hopper/">Perseus Home Page</a></p>
    </div> <%-- content --%>
</div> <%-- main --%>

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>

