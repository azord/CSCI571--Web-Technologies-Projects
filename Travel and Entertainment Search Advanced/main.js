///////////////////get current location by IP
var currentLocationSearchFlag = false;


function myFunction() {	
	var script = document.createElement('script');
	script.src = 'http://ip-api.com/json/?callback=getCurrent';
	document.getElementsByTagName('head')[0].appendChild(script);

}

function getCurrent(location) {
	document.getElementById("currentLocation").value = location.lat + "," + location.lon;
	
	if (location.lat) {
		currentLocationSearchFlag = true;
	}
}
////////////////////////////////////////////
var input = document.getElementById('specifyLocation');

autocomplete = new google.maps.places.Autocomplete(input);


$(document).ready(function() {
$("input[name='location']").change(function(){
        if ($('#customRadio1').is(':checked')) {
      $("#specifyLocation").prop('disabled', true);
            validLocation = true;
           
        } else if ($('#customRadio2').is(':checked')) {
      $("#specifyLocation").prop('disabled', false);
            
            
            var value = $( "#specifyLocation" ).val();
       
        }
      });
	  
	  mainUrl = "http://localhost:8080/";
	

    $("#mainForm").submit(function(event) {
	
      event.preventDefault();
	  
	  var radius = $("#distance").val();
		
		if (radius == "") {
			radius = 10 * 16093;
		} else {
			radius = radius * 16093;
		}
		
		var category = $("#inlineFormCustomSelect").val();
		var keyword = $("#keyword").val();
		
		keyword = keyword.split(' ').join('+');
	  
	  if ($("#customRadio1").is(':checked')) {
		
		
	if (category == "Default") {
		var url = mainUrl + "coordinates/" + $("#currentLocation").val() + "/radius/" + radius + "/keyword/" + keyword;
	} 
	else {
		var url = mainUrl + "coordinates/" + $("#currentLocation").val() + "/radius/" + radius + "/type/" + category + "/keyword/" + keyword;
	}
		} 
	
	
	else {
		var specifyLocation = $("#specifyLocation").val();
		specifyLocation = specifyLocation.split(' ').join('+');
		
		var url = mainUrl + "specificLocation/" + specifyLocation + "/radius/" + radius + "/type/" + category + "/keyword/" + keyword;
		
	}
		$("#resultTable").hide();
		$("#progressBar").show();
		$("#detailsButton").hide();
	  
	  $.get(url, function(data) {
	  
		$("#resultTable").html("");
		
		$("#progressBar").hide();
		
		$("#resultTable").show();
		
		if (data.status == "OK") {
		
		$("#detailsButton").show();
		
		var table = "<table class='table table-hover'><thead><tr><th>#</th><th>Category</th>";
		
		table += "<th>Name</th><th>Address</th><th>Favorite</th><th>Details</th></tr></thead><tbody>";
		
		for (var i = 0; i < data.results.length; i++) 
		{
		table += "<tr><td>" + (i + 1) + "</td><td><img style=\"max-width: 40%; height: auto;\" src='" + data.results[i].icon + "'></td>";
		table += "<td class=\"text-nowrap\">" + data.results[i].name + "</td><td class=\"text-nowrap\">" + data.results[i].vicinity + "</td>";
		table += "<td><button type=\"button\" class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"far fa-star\"></span></button></td><td><button type=\"button\" ng-click=\"getDetails('" + data.results[i].place_id + "', " + data.results[i].geometry.location.lat + ", " + data.results[i].geometry.location.lng + ")\" ";
		table += "class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"fas fa-angle-right\"></span></button></td></tr>";
		}
		
		table += "</tbody></table>";
		
		if (typeof data.next_page_token != "undefined") {
			
			dataFromPage1 = data;
			
			table += "<div class=\"top\"><div class=\"d-flex justify-content-center\">";
			table += "<button type=\"button\" onclick=\"nextResultButton('" + data.next_page_token + "', 1)\" ";
			table += "class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
			table += "Next</button></div></div>";
		}
		
		} 
		
		else if (data.status == "ZERO_RESULTS") {
			var table = "<div class=\"alert alert-warning top\" role=\"alert\">No records.</div>";
		} else {
			var table = "<div class=\"alert alert-danger top\" role=\"alert\">Failed to get search results.</div>";
		}
		
		testing = table;
		
		angular.element('#resultAndDetail').scope().getResultTable();
		});
		
    });
	
	  });

	function nextResultButton(token, previousPage) {
	
	
		var url = mainUrl + "pagetoken/" + token;
		
		$("#resultTable").html("");
		$("#detailsButton").hide();
		
		$("#progressBar").show();
		
		$.get(url, function(data) {
			
		$("#progressBar").hide();
		
		$("#resultTable").show();
		
		if (data.status == "OK") {
		$("#detailsButton").show();
		
		var table = "<table class='table'><thead><tr><th>#</th><th>Category</th>";
		table += "<th>Name</th><th>Address</th><th>Favorite</th><th>Details</th></tr></thead><tbody>";
		
		for (var i = 0; i < data.results.length; i++) 
		{
		table += "<tr><td>" + (i + 1) + "</td><td>";
		table += "<img style=\"max-width: 40%; height: auto;\" src='" + data.results[i].icon + "'></td>";
		table += "<td class=\"text-nowrap\">" + data.results[i].name + "</td><td class=\"text-nowrap\">" + data.results[i].vicinity + "</td>";
		table += "<td><button type=\"button\" class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"far fa-star\"></span></button></td><td><button type=\"button\" ng-click=\"slideDetails=false\" ";
		table += "class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"fas fa-angle-right\"></span></button></td></tr>";
		}
		
		table += "</tbody></table>";
		
		table += "<div class=\"top\"><div class=\"d-flex justify-content-center\">";
		table += "<button type=\"button\" onclick=\"outputPreviousResultTable(" + previousPage + ")\" class=\"btn btn-light clear\" ";
		table += "style=\"background-color: white; border-color: silver;\">Previous</button>";
		
		
		if (typeof data.next_page_token !== "undefined") {
		
			if (previousPage == 1) {
				dataFromPage2 = data;
			}
		
			table += "<button type=\"button\" onclick=\"nextResultButton('" + data.next_page_token + "', 2)\" ";
			table += "class=\"btn btn-light clear ml-5\" style=\"background-color: white; border-color: silver;\">Next</button>";
			table += "</div></div>";
		}
		
		table += "</div></div>";
		}
		else if (data.status == "ZERO_RESULTS") {
			var table = "<div class=\"alert alert-warning top\" role=\"alert\">No records.</div>";
		} else 
		{
			var table = "<div class=\"alert alert-danger top\" role=\"alert\">Failed to get search results.</div>";
		}
		
		testing = table;
		
		angular.element('#resultAndDetail').scope().getResultTable();
		
		});
	}
	
	
	
	function outputPreviousResultTable(previousPage) {
		
		$("#resultTable").html("");
		
		var table = "<table class='table'><thead><tr><th>#</th><th>Category</th><th>Name</th><th>Address</th>";
		table += "<th>Favorite</th><th>Details</th></tr></thead><tbody>";
		
		if (previousPage == 1) {
			jsonObject = dataFromPage1;
		} else {
			jsonObject = dataFromPage2;
		}
		
		
		for (var i = 0; i < jsonObject.results.length; i++) 
		{
		table += "<tr><td>" + (i + 1) + "</td><td><img style=\"max-width: 40%; height: auto;\" src='" + jsonObject.results[i].icon + "'></td>";
		table += "<td class=\"text-nowrap\">" + jsonObject.results[i].name + "</td>";
		table += "<td class=\"text-nowrap\">" + jsonObject.results[i].vicinity + "</td>";
		table += "<td><button type=\"button\" class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"far fa-star\"></span></button></td><td><button type=\"button\" ng-click=\"slideDetails=false\" ";
		table += "class=\"btn btn-light clear\" style=\"background-color: white; border-color: silver;\">";
		table += "<span class=\"fas fa-angle-right\"></span></button></td></tr>";
		}
		
		table += "</tbody></table>";
		
		console.log(previousPage);
		
		if (previousPage != 1) {
		table += "<div class=\"top\"><div class=\"d-flex justify-content-center\"><button type=\"button\" ";
		table += "onclick=\"outputPreviousResultTable(" + (previousPage - 1) + ")\" class=\"btn btn-light clear\" ";
		table += "style=\"background-color: white; border-color: silver;\">Previous</button>";
		var nextPageClass = "ml-5";
		} else {
		table += "<div class=\"top\"><div class=\"d-flex justify-content-center\">";
		var nextPageClass = "";
		}
		
		
		if (typeof jsonObject.next_page_token != "undefined") {
			table += "<button type=\"button\" onclick=\"nextResultButton('" + jsonObject.next_page_token + "'," + previousPage + ")\" ";
			table += "class=\"btn btn-light clear " + nextPageClass + "\" style=\"background-color: white; ";
			table += "border-color: silver;\">Next</button></div></div>";
		}
		
		table += "</div></div>";
		
		
		testing = table;
		
		angular.element('#resultAndDetail').scope().getResultTable();

	}
	
	
	function showDetails(place_id, latitude, longitude) {
        var map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: latitude, lng: longitude},
          zoom: 15
        });

        var infowindow = new google.maps.InfoWindow();
        var service = new google.maps.places.PlacesService(map);

        service.getDetails({
          placeId: place_id
        }, function(place, status) {
          if (status === google.maps.places.PlacesServiceStatus.OK) {
				console.log(place);
          }
        });
		
		
      }
	  
	  function hideTabs(tabId) 	
	  {  
			if (tabId === "photos") {
				$("#info").hide();
				$("#mapDetail").hide();
				$("#reviews").hide();
				$("#photos").show();
				
			} else if (tabId === 'mapDetail') {
				$("#info").hide();
				$("#reviews").hide();
				$("#photos").hide();
				$("#mapDetail").show();
				
			} else if (tabId === 'reviews') {
				$("#info").hide();
				$("#photos").hide();
				$("#mapDetail").hide();
				$("#reviews").show();
				
			} else if (tabId === 'info') {
				$("#photos").hide();
				$("#mapDetail").hide();
				$("#reviews").hide();
				$("#info").show();
				
			}
	  }
	  
	  
	var app = angular.module('HW8', ["ngAnimate"])
    app.controller('formController', ['$scope', function($scope) {
	
	var input = document.getElementById('specifyLocation');

	autocomplete = new google.maps.places.Autocomplete(input);
	
	$scope.form = {
        location: true
    };
	
    }]);
	
	app.controller("resultAndDetail", function ($scope, $window, $compile) {
	$scope.slideDetails = true;
	
		
	$scope.getResultTable = function() {
		
		$scope.slideDetails = true;
		
		var $resultTable = "";
		
		$resultTable = $window.testing;
		
		var temp = $compile($resultTable)($scope);
		
		angular.element(document.getElementById("resultTable")).append(temp);
	}
	
	
	$scope.getDetails = function(place_id, latitude, longitude) {
		$scope.slideDetails = false;
		//angular.element(document.getElementById("info")).html("");
		
		$scope.address = "";
		$scope.phone_number = "";
		$scope.price_level = "";
		$scope.rating = "";
		$scope.google_page = "";
		$scope.website = "";
		$scope.open_now = "";
		$scope.weekday = [];
		
		var map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: latitude, lng: longitude},
          zoom: 15
        });

        var infowindow = new google.maps.InfoWindow();
        var service = new google.maps.places.PlacesService(map);

        service.getDetails({
          placeId: place_id
        }, function(place, status) {
          if (status === google.maps.places.PlacesServiceStatus.OK) {
					
				$scope.outputDetails(place);
					
          }
        });
	}
	
	$scope.outputDetails = function(place) {
		
		console.log(place);
		
		$scope.address = place.formatted_address;
		$scope.phone_number = place.international_phone_number;
		$scope.price_level = place.price_level;
		$scope.rating = place.rating;
		$scope.google_page = place.url;
		$scope.website = place.website;
		$scope.open_now = place.opening_hours.open_now;
		$scope.weekday = place.opening_hours.weekday_text;
		
		
		$scope.detailTable = "<table class=\"table table-striped\"><tbody>";
		
		if (place.formatted_address != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Address</th><td>";
			$scope.detailTable += $scope.address + "</td></tr>";
		}
		if (place.international_phone_number != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Phone Number</th><td>";
			$scope.detailTable += $scope.phone_number + "</td></tr>";
		}
		if (place.price_level != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Price Level</th><td>";
			$scope.detailTable += $scope.price_level + "</td></tr>";
		}
		if (place.rating != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Rating</th><td>";
			$scope.detailTable += $scope.rating + "</td></tr>";
		}
		if (place.url != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Google Page</th><td><a target=\"_blank\" href=\"";
			$scope.detailTable += $scope.google_page + "\">";
			$scope.detailTable += $scope.google_page + "</a></td></tr>";
		}
		if (place.website != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Website</th><td>";
			$scope.detailTable += $scope.website + "</td></tr>";
		}
		if (place.opening_hours != null) {	
			$scope.detailTable += "<tr><th scope=\"row\">Hours</th><td>";
			$scope.detailTable += $scope.website + "</td></tr>";
		}
		
		
		$scope.detailTable += "</tbody></table>";
     /*
    <tr>
      <th scope="row">Price Level</th>
      <td>Larry</td>
    </tr>
	<tr>
      <th scope="row">Rating</th>
      <td>Larry</td>
    </tr>
	<tr>
      <th scope="row">Google Page</th>
      <td>Larry</td>
    </tr>
	<tr>
      <th scope="row">Website</th>
      <td>Larry</td>
    </tr>
	<tr>
      <th scope="row">Hours</th>
      <td>Larry</td>
    </tr>
  </tbody>
</table>*/


	/*<div class="row">
  <div class="col">col</div>
  <div class="col">col</div>
  <div class="col">col</div>
  <div class="col">col</div>
</div>
	</div>*/



		
		console.log(place.photos[0].getUrl({'maxWidth': place.photos[0].width, 'maxHeight': place.photos[0].height}));
		
		angular.element(document.getElementById("detailName")).html(place.name);
		angular.element(document.getElementById("info")).html($scope.detailTable);
	}
	
	
	});
