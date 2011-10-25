<html>
  <head><title>Perseus Help and Information Center</title>
    <link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
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
	  <h3>Perseus Help and Information Center</h3>
	  
	  <h4>Help Pages:</h4>
	  
	  <ul>
	    <li><a href="/hopper/help/quickstart">Perseus 4 Quick Start Guide</a><img src="/img/new.gif" alt="" border="0">
						<li><a href="/hopper/help/copyright">Copyright &amp; Warranty</a>: information on downloads, reuse, and permissions
	    
	    
						<li><a href="/hopper/help/faq">Frequently Asked Questions</a> (updated 08/07)
		  
		<li><a href="/hopper/help/startpoints">Starting Points</a> for exploring Perseus
		  
		<li><a href="/hopper/help/texts">Help with Texts</a>
		<li><a href="/hopper/help/vocab">Help with the Vocabulary Tool</a>
		<li><a href="/hopper/help/searching">Help with Searching and Search-related Tools</a>
		<li><a href="/hopper/help/archives">Archived help documents</a> for previous versions of Perseus (both on-line and on CD)
	  </ul>
	  
	  <h4>General and Contact Information:</h4>
	  <ul>
	    <li><a href="/hopper/help/oldannounce.jsp">News &amp;
		Announcements</a>
	    <li><a href="/hopper/help/support.jsp">Support Perseus</a>
	    <li><b>Contact Information:</b>
	      <ul>
		<li><a href="http://groups.yahoo.com/group/perseusproject/">The
		    Perseus Users' Forum at Yahoo! Groups</a>
		<li><a href="mailto:webmaster@perseus.tufts.edu">E-mail the
		    Webmaster</a><br>
		  <font size="-1">Note that all mail to the webmaster generates an
		    automatic reply to the originating e-mail address.<br>
		    
		    
		    <!--Read more in the <a href="/hopper/help/faq#mail">Perseus FAQ</a> on webmaster
		    mail auto replies for information on receiving a personal reply.<br>
		    
		    Please check this document prior to sending any correspondence to the
		    webmaster.--></font>
	      </ul>
	  </ul>
	  
	  <h4>Information on Perseus Collections:</h4>
	  <ul>
	    <li><a href="/hopper/text?doc=Perseus:text:1999.04.0053">Classics Collection Overview</a>
	    <li><a href="/hopper/text?doc=Perseus:text:1999.03.0024">Renaissance Collection Overview</a>
	    <li><a href="http://scriptorium.lib.duke.edu/papyrus/texts/DDBDP.html">The Duke Databank of Documentary Papyri</a>
	  </ul>
	  
	</div> <!-- main_col -->
      </div> <!-- 2column -->
    </div> <!-- main -->
    
    <!-- Google Analytics --> 
    <%@ include file="/includes/common/analytics.htm" %>
    
  </body>
</html>
