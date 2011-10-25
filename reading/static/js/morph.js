// morph.js
// JavaScript for morph.jsp

function loadLexiconPreCallback(params) {
    var query = params["doc"];
    var url = "loadquery?doc=" + query;
    if (params["source"] != null) {
	url += "&form=" + encodeURIComponent(params["form"])
	    + "&which=" + params["which"]
	    + "&source=" + params["source"];
    }

    // branch for native XMLHttpRequest object
    var textLink = document.getElementById(query + "-link");

    lastInnerHTML = textLink.innerHTML;
    putVariable("lex.lastInnerHTML", lastInnerHTML);
    textLink.style.color = "black";
    textLink.style.textDecoration = "none";
    textLink.innerHTML = "loading...";
    textLink.setAttribute('onclick', 'showEntry(\'' + query + "-contents');");

    return url;
}

function loadLexiconCallback(req, params) {
    if (req.status != 200) {
	document.alert("Problem getting lexicon entry! HTTP status: " 
	    + req.statusText);
	return;
    }
    var query = params["doc"];
    var textDisplay = document.getElementById(query + "-contents");

    putVariable("lex.currentEntry", textDisplay);

    textDisplay.innerHTML += req.responseText;
    textDisplay.style.display = "block";

    // Scroll so that the lexicon entry makes it to the top of the window
    textDisplay.scrollIntoView(true);
    
    var textLink = document.getElementById(query + "-link");
    textLink.className = "toggle";
    textLink.style.textDecoration = "underline";
    textLink.style.color = "blue";
    textLink.innerHTML = getVariable("lex.lastInnerHTML");
    textLink.setAttribute('href', '#' + query + '-contents');
    textLink.setAttribute('onclick', 'javascript:showEntry(\''
			    + query + '-contents\')');
}
 
function showVotes() {
    votesDiv = document.getElementById("votes");
    votesDiv.style.display = "block";
}

function showEntry(word) {
  entry = document.getElementById(word);
  current = getVariable("lex.currentEntry");
  if (current != null && current != entry) {
    current.style.display = "none";
    //current.style.display = (current.style.display == "none" ) ? "" : "none";
  }
  entry.style.display = "";
  //entry.style.display = (entry.style.display == "none" ) ? "" : "none";
  putVariable("lex.currentEntry", entry);
}
