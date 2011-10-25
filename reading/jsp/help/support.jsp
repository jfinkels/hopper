<html>
  <head>
    <title>Support Perseus</title>
    <link href="/css/hopper.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/hopper.js"></script>
  </head>
  <body onload="checkRedirect();">
    <%@ include file="/includes/index/header.jsp" %>
    
    <div id="main">
      <jsp:include page="/includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value="help"/>
      </jsp:include> 	
      <div id="content" class="2column">
	<div id="index_main_col">
	<H3>Support Perseus</H3>
	
	<p>The Perseus Project needs your support. We offer this web site as a
	  free service. While we have been very fortunate to receive grants and
	  support from many parties, the job of creating on-line collections
	  that document human culture is daunting.  Help us expand our
	  collections and improve the services that we can offer.  In order to
	  add more materials to the Perseus Digital Library, we need support
	  from our friends. Please consider making a tax-deductible donation to
	  the project.  <p><center><table width=66% border=1><tr><td><b>Adopt a
		    book! Sponsor a collection!</b> We display the funder for each text
		  and image in the Perseus Digital Library. Some books cost many
		  thousands of dollars to place on-line, but for a gift of $1,000 or
		  more you can have a credit line for a professionally entered, well
		  tagged on-line book in Perseus. The funder's credit will be a
		  permanent part of the catalogue entry for this electronic book. In the
		  Perseus Digital Library, each electronic page would include a line
		  such as:
		  <table border=4 align=center><tr><td>Mary Cole provided
			support for entering this text.</td></tr></table>
		  <p>If you would like to support the entry of particular books or collections 
		  of materials, please contact Perseus at webmaster@perseus.tufts.edu. 
		  </td></tr></table></center>
	
		<p><a href="supportperseus.pdf" 
		onclick="javascript: pageTracker._trackPageview('/outgoing/SupportPerseus');">Download the support form</a> in PDF format. 
		You may also utilize the on-line <a target="_blank"
		href="http://www.tuftsgiving.org">
		Tufts advancement site</a>. [Please select "Other" in the Select a School option and indicate &quot;Perseus Project&quot; in the "Other" 
		text entry box which appears.]
		<br> Need Adobe Acrobat Reader? <a target="_blank" 
		href="http://www.adobe.com/products/acrobat/readstep2.html"
		onclick="javascript: pageTracker._trackPageview('/outgoing/Adobe');">
		Visit Adobe</a> for a free download.
	  
	  </div> <!-- index_main_col -->
      </div> <!-- content -->
      </div> <!-- main -->
      
      <!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>
