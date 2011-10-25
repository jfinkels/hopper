var current = null;

function show(word) {
  entry = document.getElementById(word);
  if (current != null) {
    current.style.display = (current.style.display == "none" ) ? "" : "none";
  }
  entry.style.display = (entry.style.display == "none" ) ? "" : "none";
  current = entry;
}

function toggle(id) {
  element = document.getElementById(id + "-contents");
  elementLink = document.getElementById(id + "-link");
  element.style.display = (element.style.display == "none" ) ? "" : "none";
 
  if (elementLink.innerHTML == "show") {
    elementLink.innerHTML = "hide";
  }
  else {
    elementLink.innerHTML = "show";
  }
}    

// Like toggle(), but hides/shows the entire widget, not just its contents.
function toggleWidget(id) {
  element = document.getElementById(id);
  element.style.display = (element.style.display == "none" ) ? "" : "none";
}

function toggleExpand(targetID) {
    targetElement = document.getElementById(targetID);
    if (targetElement.style.display == "none") {
        document.images["img_" + targetID].src = "/img/south.gif";
        targetElement.style.display = "block";
    }
    else {
        document.images["img_" + targetID].src = "/img/east.gif";
        targetElement.style.display = "none";
    }

}

var navbarText = "Your current position in the text is marked in blue."

function showTitle(sender) {
    titleDiv = document.getElementById("navbar_title");
    titleDiv.innerHTML = navbarText + " Click to jump to <strong>"
	+ sender.title + "</strong>.";
}

function clearTitle() {
    titleDiv = document.getElementById("navbar_title");
    titleDiv.innerHTML = navbarText + " Click anywhere in the line to jump "
	+ "to another position."
}

function vocab_window() {
  var theWindow =
    window.open('','vocab','height=450,width=450,scrollbars=1,status=1,resizable=1');
  theWindow.focus();
  return theWindow;
}

var _POPUP_FEATURES = 'location=0, statusbar=0, menubar=0, width=400, height=300';

function raw_popup(url, target, features) {
  if (isUndefined(features)) {
    features = _POPUP_FEATURES;
  }
  if (isUndefined(target)) {
    target = '_blank';
  }
  var theWindow =
    window.open(url, target, features);
  theWindow.focus();
  return theWindow;
}

// For pages with multiple documents, keep a list of them so we know which one
// user has clicked on at any given time
var documents = new Array();

function addDocument(doc) {
    documents.push(doc);
}

function m(src, which, docIndex) {

    var linkText = src.getAttribute('href');
    var originalText = linkText;
    if (documents[docIndex]) {
	linkText = linkText + '&d=' + documents[docIndex];
	if (which != -1) {
	    linkText = linkText + '&i=' + which;
	}
    }

    var theWindow = openPopupWindow(linkText, 'morph');
    theWindow.focus();

    return theWindow;
}

function openPopupWindow(linkText, name) {
    return window.open(linkText, name);
}

function openWordStudy(src) {
    var linkText = src.getAttribute('href');
    var theWindow = openPopupWindow(linkText, 'morph');
}

function abbrev_help() {
	var theWindow = window.open('/hopper/abbrevhelp', 'abbrevHelp',
	    'height=600,width=450,scrollbars=1,status=1,resizable=1,toolbar=1');
	theWindow.focus();
}

function showNavbar() {
    var wrapper = document.getElementById("navbar_wrapper");
    wrapper.style.display = "block";

    var placeholder = document.getElementById("navbar_placeholder");
    placeholder.style.display = "none";
}

function hideNavbar() {
    var wrapper = document.getElementById("navbar_wrapper");
    wrapper.style.display = "none";

    var placeholder = document.getElementById("navbar_placeholder");
    placeholder.style.marginBottom = "10px";
    placeholder.style.display = "block";
}

function validate_required(field,alerttxt) {
	with (field) {
		if (value==null||value=="") {
			alert(alerttxt);return false;
		}
		else {
			return true
		}
	}
}

function validate_form(thisform,field,min) {
	with (thisform) {
		if (validate_required(field,"Please enter a search term")==false) {
		field.focus();return false;
		}
		if (min != null && validate_min_size(field,min,"Please enter at least " + min + " characters")==false) {
        field.focus();return false;
        }
	}
}

function validate_min_size(field,min,alerttext)
{
    with (field) {
        if (value==null||value==""||value.length < min) {
            alert(alerttext);return false;
        }
        else {
            return true
        }
    }

}

function checkAll (field) {
	for (var i = 0; i < field.length; i++) {
		field[i].checked = true;
	}
}

function uncheckAll (field) {
	for (var i = 0; i < field.length; i++) {
		field[i].checked = false;
	}
}

function checkRedirect() {
	var regexS = "[\\?&]redirect=true";
	var regex = new RegExp( regexS );
  	var results = regex.exec( window.location.href );
  	if (results == null)
    	return;
  	else {
  		var div = document.createElement('div');
  		div.setAttribute('id', 'redirect');
  		div.onclick=closeLayer;
  		var message = document.createElement('p');
  		message.innerHTML = "You have been automatically redirected to the new version of our website, Perseus 4.0."
  		div.appendChild(message);
  		
  		var submessage1 = document.createElement('p');
  		submessage1.setAttribute('class', 'greek');
  		submessage1.innerHTML = "We have attempted to automatically redirect you to the corresponding page in the new version. If we encountered an error in the redirect process, we have sent you to our home page. For further help, please contact webmaster@perseus.tufts.edu."
  		div.appendChild(submessage1);
  		var submessage2 = document.createElement('p');
  		submessage2.setAttribute('class', 'greek');
  		submessage2.innerHTML = "Some special content previously hosted by Perseus, but not integrated into the digital library collections, may not appear in P4. This applies to coursework-related content and data from other sources and collections."
		div.appendChild(submessage2);
  	
  		var greekReg = new RegExp("=[G|g]reek");
  		var greekResults = greekReg.exec(window.location.href);
  		if (greekResults != null) {
  			var greekMessage = document.createElement('p');
  			greekMessage.setAttribute('class', 'greek');
  			greekMessage.innerHTML = "If the results are not what you expected, you may need to reenter the Greek."
  			div.appendChild(greekMessage);
  		}

  		var close = document.createElement('p');
  		close.setAttribute('class', 'close');
  		close.innerHTML = "Click anywhere in the box to close.";
  		div.appendChild(close);
  		document.body.appendChild(div);
  	}
}

function closeLayer() {
	var div = document.getElementById('redirect');
	document.body.removeChild(div);
}

function toggleGreekDisplay(selectForm) {
    var selIndex = selectForm.selectedIndex;
    var greekDisplay = document.getElementById("enter_greek");
    if (selectForm.options[selIndex].value == "greek") {
	greekDisplay.style.display = "";
    } else {
	greekDisplay.style.display = "none";
    }
}
