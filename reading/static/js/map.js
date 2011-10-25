var map;

    // Create our "tiny" marker icon
	var tinyIcon = new GIcon();
	tinyIcon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
	tinyIcon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
	tinyIcon.iconSize = new GSize(12, 20);
	tinyIcon.shadowSize = new GSize(22, 20);
	tinyIcon.iconAnchor = new GPoint(6, 20);
	tinyIcon.infoWindowAnchor = new GPoint(5, 1);
		
    // Set up our GMarkerOptions object
	markerOptions = { icon:tinyIcon };
	
    function initialize(mapID) {
    	if (GBrowserIsCompatible()) {
        	map = new GMap2(document.getElementById("map_canvas"));
        	map.addControl(new GLargeMapControl());
        	map.setCenter(new GLatLng(25,-2),2);
   		}
   		loadMap(mapID);
    }
    
    function loadMap(perseusID) {
    	if (GBrowserIsCompatible()) {
		map.clearOverlays();
        		
		var URL = "/xml/";
		if (perseusID != 'all') {
			URL += perseusID+".";
		}
		URL += "places.xml";
				        
        GDownloadUrl(URL, function(data, responseCode) {
        	var xml = GXml.parse(data);
        	var root = xml.documentElement;
        	var markers = root.getElementsByTagName("marker");
        	for (var i = 0; i < markers.length; i++) {
        		var xmlpoint = new GLatLng(parseFloat(markers[i].getAttribute("lat")),
        								parseFloat(markers[i].getAttribute("lon")));
        		var site = markers[i].getAttribute("site");
        		var marker = createMarker(xmlpoint, site);
        		map.addOverlay(marker);
        	}
        });
      }
    }
    
    function createMarker(point, site) {
    	var marker = new GMarker(point, markerOptions);
    	GEvent.addListener(marker, "click", function() {
    		marker.openInfoWindowHtml(site);
    	});
    	return marker;
    }