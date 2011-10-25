//JavaScript for text.jsp

var req;
var lastID;
var loadingXML = false;

function loadVocabWgt(doc) {
   if (loadingXML) {
     return;
   }
   
   loadingXML = true;	

   var url = "vocab.wgt?works="
              + doc + "&usingChunks=true";
   
   var textLink = document.getElementById("vocab-link");
   lastID = "vocab"; 
 
    textLink.style.color = "black";
    textLink.style.textDecoration = "none";
    textLink.innerHTML = "loading...";
    textLink.setAttribute('href', 'javascript:()');
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = processReqChange;
        req.open("GET", url, true);
	req.setRequestHeader('If-Modified-Since','Wed, 15 Nov 1995 00:00:00 GMT');
        req.send(null);
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
            req.onreadystatechange = processReqChange;
            req.open("GET", url, true);
            req.send();
        }
    }

}

function loadXMLDoc(docID, query, docIndex) {

    if (loadingXML) {
	return;
    }

    loadingXML = true;

    var url = "loadquery?doc=" + query;
    if (docIndex != -1) {
	url += "&num=" + docIndex;
    }

    // branch for native XMLHttpRequest object
    var textLink = document.getElementById(docID + "-link");

    lastID = docID;

    textLink.style.color = "black";
    textLink.style.textDecoration = "none";
    textLink.innerHTML = "loading...";
    textLink.setAttribute('href', 'javascript:()');
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = processReqChange;
        req.open("GET", url, true);
	req.setRequestHeader('If-Modified-Since','Wed, 15 Nov 1995 00:00:00 GMT');
        req.send(null);
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
            req.onreadystatechange = processReqChange;
            req.open("GET", url, true);
            req.send();
        }
    }

}

function processReqChange() {
    if (req.readyState == 4) {
	if (req.status == 200) {
	    var textDisplay = document.getElementById(lastID + "-contents");

	    textDisplay.innerHTML = req.responseText;
	    textDisplay.style.display = "block";
	    
	    var textLink = document.getElementById(lastID + "-link");
	    textLink.className = "toggle";
	    //textLink.style.textDecoration = "underline";
	    textLink.style.color = "blue";
	    textLink.innerHTML = "hide";
	    textLink.setAttribute('href', 'javascript:toggle(\'' + lastID + '\')');
	} else {
	    alert("Problem! status = " + req.status + " " + req.statusText + " " + req.readyState);
	    textLink.innerHTML = "hide";	    	    
	    textLink.setAttribute('href', 'javascript:toggle(\'' + lastID + '\')');	
	}
	loadingXML = false;
    }
}

function showJumpHelp() {
  	var div = document.getElementById('jumphelp');
  	div.style.display = "";
}

function hideJumpHelp() {
	var div = document.getElementById('jumphelp');
  	div.style.display = "none";
}