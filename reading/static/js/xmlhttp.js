/*
xmlhttp.js

The contents of this file are an attempt to provide some standard routines for
dealing with XMLHttpRequest. getVariable() and putVariable() provide persistent
storage of variables or values ("persistent" here means "until the user closes
the page"), while doXMLHttpRequest() does the actual request; it calls
pre-request and post-request callback functions provided by the user (it uses
the former to get the URL, and the latter to do something with the response).
It takes an array/hash of parameters, which it passes to the callback
functions.

Here's an attempt at an example:

function preCallback(params) {
    // this function should return the URL to access; it can also use
    // get/putVariable() to obtain values or store them for later
    return "http://localhost/myurl?option=" + params["option"];
}

function postCallback(req, params) {
    // this function should actually do something with the response; it can
    // also use get/putVariable() to put values or obtain values stored by,
    // say, thre pre-request callback
    document.alert("I got as a response: " = req.responseText);
}

// do things
params["option"] = 123;
doXMLHttpRequest(preCallback, postCallback, params);
// do more things

*/

// Are we in the middle of performing an XMLHttpRequest already?
var loadingXML = false;

// This acts as a hash of sorts for whatever we need to store during the
// course of an XMLHttpRequest (or multiple requests).
var stateVariables = {};

function getVariable(key) {
    return stateVariables[key.toString()];
}

function putVariable(key, value) {
    stateVariables[key.toString()] = value;
}

function doXMLHttpRequest(preCallback, callback, params) {

    if (loadingXML) {
	return;
    }

    var url = preCallback(params);
    if (url == null) {
	return;
    }
    loadingXML = true;

    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
	reqChangeFunc = createRecChangeFunc(req, params, callback);
        req.onreadystatechange = reqChangeFunc;
        req.open("GET", url, true);
        req.send(null);
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
	reqChangeFunc = createRecChangeFunc(req, params);
        if (req) {
            req.onreadystatechange = reqChangeFunc;
            req.open("GET", url, true);
            req.send();
        }
    }
}

// This actually creates the call to the callback function, with the request
// and the parameters. It may be more complex than it needs to be.
function createRecChangeFunc(req, params, callback) {
    reqChangeFunc = function() {
	function callbackFunction() {
	    callback(req, params);
	}
	processReqChange(callbackFunction);
    }

    return reqChangeFunc;
}

function processReqChange(callback) {
    if (req.readyState == 4) {
	callback();
	/*
	if (req.status == 200) {
	    callback();
	} else {
	    alert("Problem! status = " + req.status + " " + req.statusText);
	}
	*/
	loadingXML = false;
    }
}
