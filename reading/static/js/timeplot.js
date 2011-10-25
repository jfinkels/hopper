var timeplot;

var NUM_WORDS = 5;

var colors = ['#FF0000','#00FF00','#0000FF','#00FFFF','#FF00FF'];

function onLoad(form) {
	var language = null;
	var displayLanguage = null;
	var words = new Array(NUM_WORDS);
	var visualizeWords = false;
	var showValues = new Array();
	var valuesChecked = false;
	
	if (form.showValues.checked==true) {
		valuesChecked = true;
	}
	
	if (form.id == "word_search") {
		visualizeWords = true;
	}
	
	language = form.lang.value;
	if (visualizeWords) {
		for (var i = 0; i < NUM_WORDS; i++) {
			if (form.elements['word'+i].value != null && form.elements['word'+i].value.length > 0) {
				words[i] = form.elements['word'+i].value;
				if (valuesChecked) {
					showValues[i] = true;
				}
			}
		}
	} else {
		if (valuesChecked) {
			showValues[0] = true;
		}
	}
		
	if (language == "en") {
		language = "english";
	} else if (language == "la") {
		language = "latin";
	}
	displayLanguage = language.split('');
	displayLanguage[0] = displayLanguage[0].toUpperCase();
	displayLanguage = displayLanguage.join('');
	
	var eventSources = new Array();
	var dataSources = new Array();
	for (var i = 0; i < NUM_WORDS; i++) {
		eventSources[i] = new Timeplot.DefaultEventSource();
		dataSources[i] = new Timeplot.ColumnSource(eventSources[i],1);
	}
	
	var valGeometry = new Timeplot.DefaultValueGeometry({
		gridColor: "#000000",
		axisLabelsPlacement: "left",
   		min: 0
	});
        	
	var timeGeometry = new Timeplot.DefaultTimeGeometry({
		gridColor: "#000000",
    	axisLabelsPlacement: "top"
	});
	
  	var plotInfo = createPlotInfo(showValues, valGeometry, timeGeometry, dataSources);
            
  	timeplot = Timeplot.create(document.getElementById("chart_div"), plotInfo);
  
  	var side=document.getElementById("side");
  	if (form != null) {
  		//language counts
  		if (!visualizeWords) {
  			var location = "/data/"+language+"/" + language + "All.txt";
  			timeplot.loadText(location, ",", eventSources[0]);
  			side.innerHTML = "Visualizing: <span style=\"color:"+colors[0]+"\">"+displayLanguage+"</span><br/>";
  		} 
  		
  		//word counts
  		else {
  			var html = "Visualizing: "+displayLanguage;
  			for (var i = 0; i < NUM_WORDS; i++) {
  				if (words[i] != null && words[i].length > 0) {
  					var location = "/data/"+language+"/";
  					location = location + words[i] + ".txt";  					
  					timeplot.loadText(location, ",", eventSources[i]);
  					html += ", <span style=\"color:" +colors[i]+"\">"+"\""+words[i]+"\"</span>";
  				}
  			}
  		  	html += "<br/>";
  			side.innerHTML = html;
  		}
  	}
  	
  	return false;
}

function createPlotInfo(showValues, valGeometry, timeGeometry, dataSources) {
	var plotInfo = new Array();
	for (var i = 0; i < NUM_WORDS; i++) {
		var id = "plot"+i;
		plotInfo[i] = Timeplot.createPlotInfo({
      		id: id,
     		dataSource: dataSources[i],
      		valueGeometry: valGeometry,
        	timeGeometry: timeGeometry,      
      		lineColor: colors[i],
      		showValues: showValues[i]
    	});
  	}
  	return plotInfo;
}

var resizeTimerID = null;
function onResize() {
    if (resizeTimerID == null) {
        resizeTimerID = window.setTimeout(function() {
            resizeTimerID = null;
            timeplot.repaint();
        }, 100);
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
