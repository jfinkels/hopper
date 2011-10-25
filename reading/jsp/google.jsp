<html>
  <head><title>Word Counts Visualization</title>
  <link rel="stylesheet" type="text/css" href="/css/hopper.css"/>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript" src="/js/google.js"></script>
  </head>

  <body>
  <div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Word Counts Visualization</h1>
    </div>
<div id="header_side">
	<%@ include file="/includes/head_search.html" %>
    </div>
</div>
<div id="main">
    <jsp:include page="includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
    </jsp:include> 	
    <div class="2column">
    <br/>
     <div style="float: right; width:28%; font-size:small; text-align:center">
    <b>Visualize counts for a language:</b><br>
        <form id="lang_counts" onsubmit="draw(this); return false;">
        <i>Choose chart type:</i>
		<input type="radio" name="chart" value="bar" checked="checked"/>Bar Chart
		<input type="radio" name="chart" value="line"/>Line Chart<br/>
        <select name="lang">
    		<option value="en">English</option>
    		<option value="greek">Greek</option>
    		<option value="la">Latin</option>
    	</select>
        	<input type="submit" value="Submit"/>
        </form>
        <br/>
        <b>Visualize counts for an individual word <br/>(or compare up to five words):</b><br>
    	<form id="word_search" onsubmit="draw(this); return false;">
    	<i>Choose chart type:</i>
		<input type="radio" name="chart" value="bar" checked="checked"/>Bar Chart
		<input type="radio" name="chart" value="line"/>Line Chart<br/>
    	Search in: <select name="lang" onchange="toggleGreekDisplay(this)">
    	<option value="en">English</option>
    	<option value="greek">Greek</option>
    	<option value="la">Latin</option>
    	</select><br/>
    	Enter word(s) to search (do not enter accents): <br/>
    	1. <input type="text" name="word0"/><br/>
    	2. <input type="text" name="word1"/><br/>
    	3. <input type="text" name="word2"/><br/>
    	4. <input type="text" name="word3"/><br/>
    	5. <input type="text" name="word4"/><br/>
    	<input type="reset" value="Clear" name="clear"/>
    	<input type="submit" value="Search" name="search"/>
    	</form>
    	<div class="enter_greek" id="enter_greek" style="display: none;">
			How to enter text in Greek:<br />
			<img src="/img/keyCaps.gif" alt="beta code instructions"/>
		</div>
     </div>

	<div id="chart_div" style="width: 66%; height: 500px; margin-left:2%;"></div>
    
    </div> <%-- 2column --%>
    </div> <%-- main --%>

<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>
  </body>
</html>