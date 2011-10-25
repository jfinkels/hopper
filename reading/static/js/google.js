var NUM_WORDS = 5;
var SEPARATOR = ',';

var displayLanguage = null;
var data = null;
var colIndex = null;
var years = null;
var currentWord = null;

google.load("visualization", "1", {packages:["columnchart", "linechart"]});
google.setOnLoadCallback(drawChart);

function drawChart() { }
      
function draw(form) {
	years = new Array();
	var language = null;
	language = form.lang.value;
	if (language == "en") {
		language = "english";
	} else if (language == "la") {
		language = "latin";
	}
	displayLanguage = language.split('');
	displayLanguage[0] = displayLanguage[0].toUpperCase();
	displayLanguage = displayLanguage.join('');
	
	var visualizeWords = false;
	var words = new Array(NUM_WORDS);
	
	if (form.id == "word_search") {
		visualizeWords = true;
	}
	
	if (visualizeWords) {
		for (var i = 0; i < NUM_WORDS; i++) {
			if (form.elements['word'+i].value != null && form.elements['word'+i].value.length > 0) {
				words[i] = form.elements['word'+i].value;
			}
		}
	}
	
	var yTitle = "";
	if (form != null) {
		data = new google.visualization.DataTable();
		var formatter = new google.visualization.NumberFormat({groupingSymbol: ',', fractionDigits: 0});
    	data.addColumn('string', 'Year');
		var url = null;
		colIndex = 0;
		if (!visualizeWords) {
			colIndex++;
    		data.addColumn('number', displayLanguage);
			url = "/data/"+language+"/" + language + "All.txt";
			yTitle="# of words";
			loadRequest(url);
			formatter.format(data, colIndex);
		} else {
			for (var i = 0; i < NUM_WORDS; i++) {
  				if (words[i] != null && words[i].length > 0) {
  					currentWord = words[i];
  					colIndex++;
    				data.addColumn('number', words[i]);  				
					url = "/data/"+language+"/"+words[i]+".txt";
					yTitle="# of documents";
					loadRequest(url);
					formatter.format(data, colIndex);
  				}
  			}
		}

  		var div = document.getElementById('chart_div');
    	div.innerHTML = "";
    	
    	var chart = null;
    	for (var i = 0; i < form.chart.length; i++) {
    		if (form.chart[i].checked) {
    			if (form.chart[i].value == "bar") {
					chart = new google.visualization.ColumnChart(div);
    			} else {
					chart = new google.visualization.LineChart(div);    			
    			}
    		}
    	}
       	chart.draw(data, {title: displayLanguage+' Word Counts', legend: 'bottom', min: 0, titleX: 'Year', titleY: yTitle});
	}
}

function loadRequest(url) {
	xmlhttp=null;
	if (window.XMLHttpRequest) {// code for Firefox, Opera, IE7, etc.
  		xmlhttp=new XMLHttpRequest();
	} else if (window.ActiveXObject) {// code for IE6, IE5
  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (xmlhttp!=null) {
  		xmlhttp.open("GET",url,false);
  		xmlhttp.send(null);
      	if (xmlhttp.status==404) {
      		alert("We're sorry, there is no data for '"+currentWord+"'");
      	} else {
       		processCSVFile(xmlhttp.responseText);
       	}
	} else {
  		alert("Your browser does not support XMLHTTP.");
	}
}

function processCSVFile(httptext) {        
 	var textLines = httptext.split("\n");

	for (var i = 1; i < textLines.length-1; i++) {
  		var row = textLines[i].split(SEPARATOR);
  		var year = row[0].toString();
  		var count = row[1] * 1;

  		var yearIndex = years.indexOf(year);
  		if (yearIndex != -1) {
  			data.setValue(yearIndex, colIndex, count);  		
  		} else {
			var rowIndex = data.addRow();  		
	  		data.setValue(rowIndex, 0, year);
  			data.setValue(rowIndex, colIndex, count);
  			years.push(year);
  		}
  	}
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

if(!Array.indexOf){
  Array.prototype.indexOf = function(obj){
   for(var i=0; i<this.length; i++){
    if(this[i]==obj){
     return i;
    }
   }
   return -1;
  }
}