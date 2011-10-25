<%@page import="java.io.PrintWriter"
	isErrorPage="true"
        pageEncoding="utf-8"
        contentType="text/html;charset=utf-8"%>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>An Error Occurred</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
<link rel="stylesheet" type="text/css" href="/css/error.css" />
</head>
<body>
<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Perseus Digital Library</h1>
    </div>
    <div id="header_side">
	<%@ include file="/includes/head_search.html" %>
    </div>
</div>
<div id="main">
    <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
    </jsp:include> 
    
    <div id="content" class="1column" style="margin-left: 1%;">
	<h2>An Error Occurred</h2>

	<p>Sorry, we were unable to load the page you were looking for!</p>

	<p>You've encountered an error that probably occurred due to a problem on our end. The error has been logged, and we'll be looking into fixing it. If you'd like to help us out, you can fill out the following form:</p>

	<form action="reporterror.jsp" method="post" class="error">
	    <fieldset>
		<legend>Error Report</legend>

		<input type="hidden" name="url"
			value="<%= request.getAttribute("javax.servlet.error.request_uri") %>?<%= request.getQueryString() %>" />

	    <% if (exception != null) { %>
		<input type="hidden" name="exception-class"
			value="<%= exception.getClass().getName() %>" />

		<% if (exception.getMessage() != null) { %>
		<input type="hidden" name="exception-message"
			value="<%= exception.getMessage().replaceAll("\"", "'") %>" />
		<% } %>

		<input type="hidden" name="exception-stack-trace"
			value="<%

		 StackTraceElement[] stackTrace = exception.getStackTrace();
		 for (int i = 0; i < stackTrace.length; i++) {
		    out.print(stackTrace[i]);
		    if (i < stackTrace.length-1) out.print("\n");
		 }%>" />
	    <% } %>

		<table>
		    <tr>
			<td><label for="name">Your name (optional):</label>
			<td><input type="text" name="name" />
		    </tr>
		    <tr>
			<td><label for="email">Your email (optional):</label>
			<td><input type="text" name="email" />
		    </tr>
		    <tr>
			<td><label for="subject">Email subject (optional):</label>
			<td><input type="text" name="subject" />
		    </tr>
		</table>

		<p><strong>Clicking "Report Bug" will automatically send us the URL
		you were trying to access and a description of the error.</strong>
		If you wish, you may provide some additional information below:</p>

		<textarea name="additional_info">
		</textarea>
		<br />
		<input type="submit" value="Report Bug" />
	    </fieldset>
	</form>

	<p>Apologies for the problem, and thanks for your understanding.</p>

	<p><a href="/hopper/">Perseus Home Page</a></p>
    </div> <%-- content --%>
</div> <%-- main --%>
	 <!-- Google Analytics --> 
 
<%@ include file="/includes/common/analytics.htm" %>
</body>
</html>
