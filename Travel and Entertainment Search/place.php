<!DOCTYPE HTML>
<html>
<head>
<meta content="text/html;charset=utf-8" http-equiv="Content-Type">
<meta content="utf-8" http-equiv="encoding">
<style>
	table.main {
		border: 3px solid #dadada;
		margin-left: 30%;
		margin-top: 5%;
		width: 35%
	}
	
	table.norecords {
		border: 3px solid #dadada;
		margin-left: 25%;
		width: 45%;
		text-align: center;
	}
	
	table.records {
		margin-left: 11%;
		border-collapse: collapse;
		margin-bottom: 15%;
		
	}
	
	table.records td, table.records th {
		border: 3px solid #dadada;
	}
	
	label {
		font-weight: bold;
	}
	
	.travel {
		text-align: center;
		font-style: italic;
	}
	
	a {
		text-decoration: none;
		color: black;
	}
	
	h3 {
		text-align: center;
	}
	
</style>

<script type="text/javascript">
function myFunction() {
	
	
	var script = document.createElement('script');
	script.src = 'http://ip-api.com/json/?callback=getCurrent';

	document.getElementsByTagName('head')[0].appendChild(script);

}

function getCurrent(location) {
	document.getElementById("currentLocation").value = location.lat + "," + location.lon;
	
	if (location.lat) {
		document.getElementById("submit").disabled = false;
	}
	
}
</script>

<title>HW6</title>

</head>

<body onload="myFunction()">


<table bgcolor="#f5f5f5" class="main">
<tr>
<td>

<form method="post" action="place.php">

<h2 class=travel>Travel and Entertainment Search</h2>
<hr>
<label>Keyword </label>
<input type="text" name="keyword" REQUIRED value=""><br>
<label>Category </label>
<select name="category" id="category">
<option value="default">Default</option> 
<option value="cafe">cafe</option>
<option value="bakery">bakery</option>
<option value="restaurant">restaurant</option>
<option value="beauty_salon">beauty salon</option>
<option value="casino">casino</option>
<option value="movie_theater">movie theater</option>
<option value="lodging">lodging</option>
<option value="airport">airport</option>
<option value="train_station">train station</option>
<option value="subway_station">subway station</option>
<option value="bus_station">bus station</option>
</select>
<br>
<label>Distance(miles) </label><input type="text" name="distance" placeholder=10> <label>from</label> 
<input onclick="document.getElementById('location').disabled = true" type="radio" name="location" value="Here" checked>Here<br>
<input type="hidden" id="currentLocation" name="currentLocation" value="">

<input onclick="document.getElementById('location').disabled=false" type="radio" name="location" value="specify" style="margin-left:58%">
<input type="text" name="exactLocation" placeholder="location" id="location" disabled=true REQUIRED>

<br>
&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="submit" value="Search" id="submit" disabled=true>
<input type="button" name="clear" value="Clear" onclick="clearForm()">
 <br>
 <br>
</form>
 </td>
</tr>
</table>

<script type="text/javascript">
function clearForm() {
	document.getElementById("category").value = "default";
	document.getElementsByName("keyword")[0].value = "";
	document.getElementsByName("distance")[0].value = "";
	document.getElementsByName("location")[0].checked = "checked";
	document.getElementById("location").value = "";
	document.getElementById("location").disabled = true;
	
	document.getElementById("output").innerHTML = "";
	document.getElementById("tableReviews").innerHTML = "";
	document.getElementById("photos").innerHTML = "";
	document.getElementById("toShowReviews").innerHTML = "";
	document.getElementById("reviews").innerHTML = "";
	document.getElementById("toShowPhotos").innerHTML = "";
	document.getElementById("images").innerHTML = "";
	
	
}
</script>

<?php
	if (isset($_GET['placeid'])) {
		$details = "https://maps.googleapis.com/maps/api/place/details/json?placeid=".$_GET['placeid']."&key=AIzaSyBwDHHNqD7DO9pAchf9ZaazBKvNHoSRxvM";
		
		$json_details = file_get_contents($details);
		
		$getPhotos = json_decode($json_details);
		
		$counter = 1;
		
			for ($i = 1; $i < 6; $i++) {
				if (file_exists('/home/scf-21/ordabeko/apache2/htdocs/hw/'.$i.'.jpg')) {
				unlink ('/home/scf-21/ordabeko/apache2/htdocs/hw/'.$i.'.jpg');
			}
			}
			
		if (!empty($getPhotos->result->photos)) {	
			foreach ($getPhotos->result->photos as $phots) {
			
				if ($counter == 6) {
					break;
				}
			
				$photos = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=750&photoreference=".$phots->photo_reference."&key=AIzaSyCfSpqqVHXA3dxb7opaOf5A7WKkMZ45N9E";
			
				$download = file_get_contents($photos);
			
				if (file_put_contents('/home/scf-21/ordabeko/apache2/htdocs/hw/'.$counter.'.jpg', file_get_contents($photos))) {
					$getPhotos->html_attributions[$counter-1] = $counter.".jpg";
				}
				$counter++;
			}	
		}
		
		$json_details = json_encode($getPhotos);
		
		
	$additional = json_encode(array('keyword' => $_GET['keyword'], 'defaultDistance' => $_GET['defaultDistance'], 'distance' => $_GET['distance'], 'category' => $_GET['category'], 'location' => $_GET['location'], 'exactLocation' => $_GET['exactLocation']));
	}
	else if (isset($_POST['submit'])) {
		
		if (empty($_POST['distance'])) 
		{	
			$distance = 10 * 16093;
			$defaultDistance = "true";
		} 
		else {
			$distance = $_POST['distance'] * 16093;
			$defaultDistance = "false";
		}
		
		if ($_POST['location'] === "specify") {
			
			$jsonurl = "https://maps.googleapis.com/maps/api/geocode/json?address=".urlencode($_POST['exactLocation'])."&key=AIzaSyBT1mySZIz3ow0jy8NMYVgyQpAVglPVmE4 ";
		
			$json = file_get_contents($jsonurl);
		
			$temporary = json_decode($json);
		
			$lat = $temporary->results[0]->geometry->location->lat;
			$long = $temporary->results[0]->geometry->location->lng;
			
			$geoLocation = json_encode(array('lat' => $lat, 'long' => $long));
			
			$exactL = 1;
			
			$exactLocation = utf8_encode($_POST['exactLocation']);
		
		} else {
			
			$exactL = 0;
			
			$tempArray = explode(",", $_POST['currentLocation']);
			
			$lat = $tempArray[0];
			$long = $tempArray[1];
			
			$geoLocation = json_encode(array('lat' => $lat, 'long' => $long));
		}
		
		echo "<br>";
		
		if ($_POST['category'] == "default") 
		{
		$nearby = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=".$lat.",".$long."&radius=".$distance."&keyword=".urlencode($_POST['keyword'])."&key=AIzaSyCnx5dbuI74DhCMX2BcdvWLzyeb35BuPJ0";
		} 
		else 
		{	
		$nearby = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=".$lat.",".$long."&radius=".$distance."&type=".$_POST['category']."&keyword=".urlencode($_POST['keyword'])."&key=AIzaSyCnx5dbuI74DhCMX2BcdvWLzyeb35BuPJ0";
		}
		
		$jsonGoogleNearBy = file_get_contents($nearby);
		
		$additional = json_encode(array('keyword' => $_POST['keyword'], 'defaultDistance' => $defaultDistance, 'distance' => $_POST['distance'], 'category' => $_POST['category'], 'location' => $_POST['location'], 'exactLocation' => $_POST['exactLocation']));
	}
?>


<div id="output"></div>

<script type="text/javascript">
	var detailsJson = JSON.parse(<?php echo json_encode($json_details); ?>);
	
	if (detailsJson != null) 
	{
		output = "<div style='left: 30%; right: 35%; position: absolute;'>";
		output += "<h3 style='position:absolute; margin-left:15%;'>"+ detailsJson.result.name  + "</h3>";
		output += "<br><br>";
		
		output += "<div style='margin-left: 34%; margin-top: 5%;' id='toShowReviews'>click to show reviews</div>";
		output += "<a href='javascript:void(0);' onclick='showReviews()'><img src='arrow_down.png' id='reviews' width='5%' style='margin-left:44%;'>";
		output += "</a>";
		output += "<div id='tableReviews'></div>";
		
		output += "<div style='margin-left: 34%;' id='toShowPhotos'>click to show photos</div>";
		output += "<a href='javascript:void(0);' onclick='showPhotos()'><img src='arrow_down.png' id='images' width='5%' style='margin-left:44%;'>";
		output += "</a>";
		output += "<div id='photos'></div>";
		output += "</div>";
		
		var additional = JSON.parse(<?php echo json_encode($additional); ?>);
		
		
		
	//////////////////////////	
	function showReviews() {
		if (document.getElementById('reviews').src == "http://cs-server.usc.edu:30378/hw/arrow_down.png") {
			
			if (document.getElementById('images').src == "http://cs-server.usc.edu:30378/hw/arrow_up.png") {
				document.getElementById('photos').innerHTML = "";
				document.getElementById('images').src = "http://cs-server.usc.edu:30378/hw/arrow_down.png";
				document.getElementById('toShowPhotos').innerHTML = "click to show photos";
				
			}
			
			document.getElementById('reviews').src = 'arrow_up.png';
			document.getElementById('toShowReviews').innerHTML = "click to hide reviews";
		
		var outputReviews = "<table style='border-collapse: collapse; margin-bottom: 10px;width:100%'>";
		
		
		if (typeof detailsJson.result.reviews == "undefined") {
				outputReviews += "<tr style='width:100%;'><td style='border: 3px solid #dadada;' ><h3 style='margin:0px;'>No Reviews Found</h3></td></tr>";
		
		} else {
			for (var i = 0; i < detailsJson.result.reviews.length; i++) 
			{
				if (i == 5) {
					break;
				}
				
				
				outputReviews += "<tr><td style='border: 3px solid #dadada;'>";
				
				
				if (typeof detailsJson.result.reviews[i].profile_photo_url == "undefined") {
	outputReviews += "<h3 style='margin: 0px;'>" + detailsJson.result.reviews[i].author_name;
				} else {
	outputReviews += "<h3 style='margin: 0px;'><img src='" + detailsJson.result.reviews[i].profile_photo_url + "' style=\"width: 7%;\"> " + detailsJson.result.reviews[i].author_name;	
				}
				
				outputReviews += "</h3></td></tr>";
				outputReviews += "<tr><td style='border: 3px solid #dadada;'>";
				outputReviews += detailsJson.result.reviews[i].text;
				outputReviews += "</td></tr>";
			}
			
			outputReviews += "</table>";
		}
	}
	else 
	{
			document.getElementById('reviews').src = 'arrow_down.png';
			document.getElementById('toShowReviews').innerHTML = "click to show reviews";
			
			var outputReviews = "";	
	}
 		
		document.getElementById('tableReviews').innerHTML = outputReviews;
	}	
		
	/////////////////////////////////////	
		
		
	////////////////////////////////////	
	function showPhotos() {
		if (document.getElementById('images').src == "http://cs-server.usc.edu:30378/hw/arrow_down.png") {
			
			if (document.getElementById('reviews').src == "http://cs-server.usc.edu:30378/hw/arrow_up.png") {
				document.getElementById('reviews').src = 'arrow_down.png';
				document.getElementById('toShowReviews').innerHTML = "click to show reviews";
				document.getElementById('tableReviews').innerHTML = "";
			}
			
			
			document.getElementById('images').src = 'arrow_up.png';
			document.getElementById('toShowPhotos').innerHTML = "click to hide photos";
			
		var outputPhotos = "<table style='border-collapse: collapse; margin-bottom: 100px;width:100%'>";
		
		if (detailsJson.html_attributions.length == 0) {
			outputPhotos += "<tr style='width:100%;'><td style='border: 3px solid #dadada;' ><h3 style='margin:0px;'>No Photos Found</h3></td></tr>";
		} 
		else {
			for (var i = 1; i <= detailsJson.html_attributions.length; i++) 
			{
				outputPhotos += "<tr><td style='border: 3px solid #dadada;'>";
				outputPhotos += "<a href='" + detailsJson.html_attributions[i-1] + "' target='_blank'><img src='" + detailsJson.html_attributions[i-1] + "' style=\"width: 95%;padding: 3%;\">";
				outputPhotos += "</a></td></tr>";
			}
		}
			
		outputPhotos += "</table>";
			
			
		} else {
			document.getElementById('images').src = 'arrow_down.png';
			document.getElementById('toShowPhotos').innerHTML = "click to show photos";
			
			var outputPhotos = "";
		}
		
			
			
			document.getElementById('photos').innerHTML = outputPhotos;
	}
	//////////////////////////////////////////////////
	
	
	
	/////////////////////////////////////////////////////retain form
	/////////////////set keyword
	var keyword = additional.keyword;
	document.getElementsByName("keyword")[0].value = keyword;
	
	/////////////set category
	
	var category = additional.category;
	document.getElementById("category").value = category;
	
	/////////////set distance
	
	var distance = additional.distance;
	var defaultDistance = additional.defaultDistance;
	
	if (defaultDistance == "false")
	{
		document.getElementsByName("distance")[0].value = distance;
	}
	
	//////////////set location
	
	var exactL = additional.location;
	
	
	if (exactL == "specify") 
	{
		var testingLocation = additional.exactLocation;
		
		document.getElementById('location').disabled = false;
		document.getElementById('location').value = testingLocation;
		document.getElementsByName('location')[1].checked = true;
	}
	
		
	}
	else {
	
	var outputJson = JSON.parse(<?php echo json_encode($jsonGoogleNearBy); ?>);
	var geoLocation = JSON.parse(<?php echo json_encode($geoLocation); ?>);
	var additional = JSON.parse(<?php echo json_encode($additional); ?>);
	
	//console.log(additional);
	
	if (outputJson.results.length == 0) {
		output = "<table bgcolor='#efefef' class='norecords'><tr><td>No Records have been found</td></tr></table>";
	}
	else
	{	
		output = "<table class='records'><tr><th style='width:77px'>Category</th><th style='width:585px'>Name</th><th style='width:450px'>Address</th></tr>";
		
		for (var i = 0; i < outputJson.results.length; i++) {
			output += "<tr><td><img src=\"";
			output += outputJson.results[i].icon;
			output += "\" style=\"padding-right: 25%;\"></td><td style='padding-left: 1%'><a href=\"place.php?placeid=";
			output += outputJson.results[i].place_id;
			output += "&keyword=";
			output += additional.keyword;
			output += "&defaultDistance=";
			output += additional.defaultDistance;
			output += "&distance=";
			output += additional.distance;
			output += "&category=";
			output += additional.category;
			output += "&location=";
			output += additional.location;
			output += "&exactLocation=";
			output += additional.exactLocation;
			output += "\">";
			output += outputJson.results[i].name;
			output += "</a></td><td style='padding-left: 1%'><a href='javascript:void(0);' onclick='showMap(\"";
			output += outputJson.results[i].id;
			output += "\",";
			output += outputJson.results[i].geometry.location.lat;
			output += ",";
			output += outputJson.results[i].geometry.location.lng;
			output += ",";
			output += geoLocation.lat;
			output += ",";
			output += geoLocation.long;
			output += ", \"";
			output += i;
			output += "\")'>";
			output += outputJson.results[i].vicinity;
			output += "</a><br><div id=";
			output += outputJson.results[i].id;
			output += " style='display:none; height: 274px; width: 28%; z-index: 5; position: absolute;'>";
			
			output += "</div>";
			output += "<div class=\"opt\" id=\"";
			output += i;
			output += "\" style='display:none;'>";
			output += "<a href=\"javascript:void(0);\" onclick=\"calculateAndDisplayRoute('WALKING')\">Walk there</a><br>";
			output += "<a href=\"javascript:void(0);\" onclick=\"calculateAndDisplayRoute('BICYCLING')\">Bike there</a><br>";
			output += "<a href=\"javascript:void(0);\" onclick=\"calculateAndDisplayRoute('DRIVING')\">Drive there</a>";
			output += "</div>";
			
			
			output += "</td>";
			output += "</tr>";
		}
		output += "</table>";
	}
		
	
	/////////////////////////////////////////////////////retain form
	/////////////////set keyword
	var keyword = additional.keyword;
	document.getElementsByName("keyword")[0].value = keyword;
	
	/////////////set category
	
	var category = additional.category;
	document.getElementById("category").value = category;
	
	/////////////set distance
	
	var distance = additional.distance;
	var defaultDistance = additional.defaultDistance;
	
	if (defaultDistance == "false")
	{
		document.getElementsByName("distance")[0].value = distance;
	}
	
	//////////////set location
	
	var exactL = additional.location;
	
	
	if (exactL == "specify") 
	{
		var testingLocation = additional.exactLocation;
		
		document.getElementById('location').disabled = false;
		document.getElementById('location').value = testingLocation;
		document.getElementsByName('location')[1].checked = true;
	}
	
	/////////////////////////////////////////////////////
	}
	
	document.getElementById("output").innerHTML = output;
</script>


<style>
	.opt {
	position: absolute;
    vertical-align:top;
    overflow:hidden;
	z-index: 5;
	background-color: #f5f5f5;
	
    
}
	.opt a  {
		text-decoration: none;
		display:inline-block; 
		color: black;
		padding:10px;
		margin:-4px -2px -5px -2px
	}
	
	.opt a:hover {
		background-color: #d8d7d7;
	}
</style>





<script>
	origLatitude = "";
	origLongitude = "";
	destLatitude = "";
	destLongitude = "";
	var markers = [];
	

	function showMap(id, destLat, destLng, origLat, origLng, travelMode) {
		
		origLatitude = origLat;
		origLongitude = origLng;
		destLatitude = destLat;
		destLongitude = destLng;
		
		
		if (document.getElementById(id).style.display == "none") 
		{
			document.getElementById(travelMode).style.display = "inline";
			document.getElementById(id).style.display = "inline";
		} else
		{
			document.getElementById(id).style.display = "none";
			document.getElementById(travelMode).style.display = "none";
		}
		
		initMap(id, destLat, destLng);	
	}

      function initMap(divId, destLat, destLng) {
		  
        directionsDisplay = new google.maps.DirectionsRenderer;
		directionsService = new google.maps.DirectionsService;
		
		ulu = {lat: destLat, lng: destLng};
		
        map = new google.maps.Map(document.getElementById(divId), {
          zoom: 13,
          center: ulu
        });
        directionsDisplay.setMap(map);
			
		addMarker();

	  }
	  
	  function addMarker() {
		  var marker = new google.maps.Marker({
          position: ulu,
          map: map
        });
		markers.push(marker);
	  }
	  
	  function setMat() {
		  for (var i = 0; i < markers.length; i++) 
		  {
		  markers[i].setMap(null);
		  }
	  }
	  
      function calculateAndDisplayRoute(selectedMode) {
		  
		  setMat();
       
	  directionsService.route({
          origin: {lat: origLatitude, lng: origLongitude},
          destination: {lat: destLatitude, lng: destLongitude},
          
		  
          travelMode: google.maps.TravelMode[selectedMode]
        }, function(response, status) {
          if (status == 'OK') {
            directionsDisplay.setDirections(response);
          } else {
            //window.alert('There is no way using this travel mode' + status);
            window.alert('There is no way using this travel mode');
          }
        });
      }
    </script>


<script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC2OQ1V8O6pvJlHQQFolBuQM0Tv4yVDVVg&callback=initMap">
</script>

</body>


</html>