<%@ include file="includes/common/include.jsp"%>
<c:set var="noresults" value="${model.noresults}"/>
<c:set var="metadata" value="${model.metadata}"/>
<c:set var="corpusList" value="${model.corpusList}"/>
<c:set var="searchLangs" value="${model.searchLangs}"/>
<c:set var="defLookupLangs" value="${model.defLookupLangs}"/>
<c:set var="resolveFormLangs" value="${model.resolveFormLangs}"/>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Search Tools</title>
<link rel="stylesheet" type="text/css" href="/css/hopper.css" />
<link rel="stylesheet" type="text/css" href="/css/voting.css" />
<script type="text/javascript" src="/js/hopper.js"></script>
</head>

<body onload="checkRedirect();">

<div id="header">
    <a id="logo" href="/hopper/"></a>
    <div id="header_text">
	<h1>Perseus Search Tools</h1>
    </div>
</div>
<div id="main">
    <jsp:include page="includes/index/indexNav.jsp">
	<jsp:param name="tabActive" value=""/>
    </jsp:include>
    
    <div id="content" class="2column">
    <div id="main_col">   	
		<div id="search">
		<p style="text-align:center">General Search Tools</p>
	
	<div class="search_widget primary" id="lang_search">
	<div class="header" style="font-size:small">
	<a class="toggle" href="javascript:toggle('lang_search')" id="lang_search-link">hide</a>
	    Search the collections
	</div>
	<div class="contents" id="lang_search-contents">
	<p>Search all text in the Perseus Digital Library using a specific language.  
	This search will also return links to entries in language dictionaries (Lewis &amp; Short, LSJ, Buckwalter, etc.)
	</p>
	    <form class="search" action="searchresults">
	    <c:if test="${not empty metadata}">
	    <p><span style="color:red">Currently searching the following text:</span> 
	    <c:if test="${not empty metadata.creator}"><c:out value="${metadata.creator}" escapeXml="false"/>,</c:if>
	    <i><c:out value="${metadata.title}" escapeXml="false"/></i>
	    <c:if test="${not empty metadata.language}">(<c:out value="${metadata.language}"/>)</c:if>
	    </p>
		<input class="search_doc" type="hidden" name="doc" value="<c:out value="${metadata.documentID}"/>">
		<input type="hidden" name="inContent" value="true"/>
	    </c:if>
		Search in
<select name="target" onchange="javascript:setExpand(this)">
 <c:forEach var="lang" items="${searchLangs}">
	<option value="<c:out value="${lang.code}"/>" 
	<c:if test="${not empty metadata && metadata.language.code eq lang.code}">selected="selected"</c:if>
	><c:out value="${lang.name}"/></option>
	</c:forEach>
</select>
<br />
<div style="float:right; width:33%; margin-top:10px">
		<c:if test="${empty metadata}">
		Limit Search to:<br />
			<c:forEach var="corpus" items="${corpusList}">
			<input type="checkbox" value="<c:out value="${corpus.ID}"/>" name="collections" checked/><c:out value="${corpus.metadata.title}"/><br>
			</c:forEach>
		</c:if>
</div>
<div style="width:66%; margin-top:10px">
		<div class="search_field" id="all_words">
		    <label for="all_words">containing <em>all</em> of the words</label>
		    <input type="text" name="all_words" size="20" />
		    <br><span class="expand">
			<input type="checkbox" name="all_words_expand" /> Search for all possible forms
		    </span>
		</div>
		<div class="search_field" id="phrase">
		    <label for="all_words">containing the <em>exact phrase</em></label>
		    <input type="text" name="phrase" size="20" />
		</div>
		<div class="search_field" id="any_words">
		    <label for="any_words">containing <em>at least one</em> of the words</label>
		    <input type="text" name="any_words" size="20" />
		    <br><span class="expand">
			<input type="checkbox" name="any_words_expand" /> Search for all possible forms
		    </span>
		</div>
		<div class="search_field" id="exclude_words">
		    <label for="all_words"><em>without</em> the words</label>
		    <input type="text" name="exclude_words" size="20" />
		    <br><span class="expand">
			<input type="checkbox" name="exclude_words_expand" /> Search for all possible forms
		    </span>
		</div>
		</div>
		<input type="submit" name="search" value="Search" />
		<c:if test="${not empty metadata}">
			<input type="submit" name="searchAll" value="Search all texts"/>
		</c:if>
		<a href="/hopper/search">Clear this search</a>
	    </form>
	</div> <!-- widget contents -->
	</div> <!-- language search -->
	<br>
	<div class="search_widget primary" id="lexicon">
	<div class="header" style="font-size:small">
	<a class="toggle" href="javascript:toggle('lexicon')" id="lexicon-link">show</a>
    English-to-[Language] lookup
    </div>
    <div class="contents" id="lexicon-contents" style="display:none">
    <p>Find Greek, Latin, Arabic, etc words based on the English definitions in their dictionary entries. (Enter &ldquo;spirit&rdquo; to find <em>animus</em>, <em>genius</em>, <em>spiritus</em>, etc.)</p>
    <form id="deflookup" action="definitionlookup" onsubmit="return validate_form(this,q,2);">
        Search for <select name="type">
           	<option value="begin">words beginning with</option>
            <option value="end">words ending with</option>
            <option value="exact">the exact word</option>
            <option value="substring">words containing</option>
        </select>
        <input name="q" value="">
	in
	<select name="lang">
 		<c:forEach var="lang" items="${defLookupLangs}">
 			<option value="<c:out value="${lang.code}"/>"><c:out value="${lang.name}"/></option>
 		</c:forEach>
	</select>
	dictionary definitions

	<input type="submit" value="Search" />
    </form>
	</div>
</div> <!-- Definition Lookup -->
    <br>
    <div class="search_widget primary" id="resolveform">
    <div class="header" style="font-size:small">
    	<a class="toggle" href="javascript:toggle('resolveform')" id="resolveform-link">show</a>
		Dictionary Entry Lookup
	</div>
	<div class="contents" id="resolveform-contents" style="display:none">
    <p>(Search for <em>words starting with</em> "am" in Latin to find <em>amo</em>, <em>amarus</em>, <em>amplus</em>, etc.)</p>
    <form action="resolveform" method="get" onsubmit="return validate_form(this,lookup);">
      Search for
      <select name="type">
	    <option value="start">words starting with</option>
	    <option value="end">words ending with</option>
	    <option value="substring">words containing</option>
	    <option value="exact">the exact word</option>
      </select>
      <input type="text" name="lookup" value="" />
      in
      <select name="lang">
      	<c:forEach var="lang" items="${resolveFormLangs}">
 			<option value="<c:out value="${lang.code}"/>"><c:out value="${lang.name}"/></option>
 		</c:forEach>
      </select>
      <input type="submit" value="Search" />
    </form>
	</div>
</div> <!-- Dictionary Lookup -->
    <br>
    <div class="search_widget primary" id="artarch">
		<div class="header" style="font-size:small">
			<a class="toggle" href="javascript:toggle('artarch')" id="artarch-link">show</a>
			Art & Archaeology Search	
		</div>    
		<div class="contents" id="artarch-contents" style="display:none">
		<p>Search our collection of artifacts and/or image captions.</p>
		<form action="artifactSearch" method="get" onsubmit="return validate_form(this,q);">
		Search term: <input type="text" name="q" value=""/><br><br>
		
		<input type="checkbox" name="artifact" value="yes" checked/>Search artifacts
		<input type="checkbox" name="image" value="yes" checked/>Search image captions
		<br><br>
		<input type="submit" value="Search"/>
		</form>	
		</div>
    </div> <!-- Art & Archaeology Search -->
    
    </div> <!-- search tools -->
<p style="text-align:center">Named Entity Search Tools</p>
<%@ include file="/includes/nesearch.html" %>
<br>
</div> <!-- main_col -->
<div id="side_col">
<div id="enter_greek">
How to enter text in Greek:<br />
<img src="/img/keyCaps.gif" alt="beta code instructions"/>
</div>
<div class="search_widget primary" id="studyTool">
	<div class="header">
		<a class="toggle" href="javascript:toggle('studyTool')" id="studyTool-link">hide</a>
		Word Study Tool
	</div>
	<div class="contents" id="studyTool-contents">
	<form action="/hopper/morph" name="f" target="morph" onsubmit="return validate_form(this,l);">
	    Get Info for <input name="l" /> in 

	    <select name="la">
	    <c:forEach var="lang" items="${defLookupLangs}">
 			<option value="<c:out value="${lang.code}"/>"><c:out value="${lang.name}"/></option>
 		</c:forEach>
	    </select>
	    <input type="submit" value="Go"
	    onclick="openWordStudy(this)" />
	</form>
	</div>
</div> <!-- studyTool box -->
<br>
<div class="search_widget primary">
<div class="header">
		<a target="_blank" href="/hopper/vocablist">link</a>
		Vocabulary Tool
	</div>
</div>
</div> <!-- side_col -->
    
		</div> <!-- content -->
		 </div> <!-- main -->
		 
<!-- Google Analytics --> 
<%@ include file="/includes/common/analytics.htm" %>

</body>
</html>
