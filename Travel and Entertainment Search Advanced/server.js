const express = require('express');
const app = express();
const https = require("https");
var cors = require('cors');
const yelp = require('yelp-fusion');

const client = yelp.client("-qX2oWvRpECTv-oTzVH2QSEpADnOWdyJzblFukOQXVkdT43JHAnV1mmv_izSdV5594inz3deHeYou8cKUKZ-DAyjnRZJ2puWiIG9BVDebr9M4ny-JWhB275whMTGWnYx");


app.use(cors());

//const url =
  //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";

  
////////////////get nearby search results with categories
app.get('/coordinates/:location/radius/:radius/type/:category/keyword/:keyword', function(req, resq) {
	
	//url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + req.query.location + "&radius=" + req.query.radius + "&type=" + req.query.category + "&keyword=" + req.query.keyword + "&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";
	
	
	url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + req.params.location + "&radius=" + req.params.radius + "&type=" + req.params.category + "&keyword=" + encodeURIComponent(req.params.keyword) + "&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";
	
	
  //resq.send('user ' + req.params.id);
  resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  
  ////////////////////////////
  https.get(url, res => {
  res.setEncoding("utf8");
  
  res.on("data", data => {
    body += data;
  });
  res.on("end", () => {
    body = JSON.parse(body);
    resq.json(body);
  });
	});
  ////////////////////////////
});



////////////////get nearby search results without categories
app.get('/coordinates/:location/radius/:radius/keyword/:keyword', function(req, resq) {

	url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + req.params.location + "&radius=" + req.params.radius + "&keyword=" + encodeURIComponent(req.params.keyword) + "&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";
	
  resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  
  ////////////////////////////
  https.get(url, res => {
  res.setEncoding("utf8");
  
  res.on("data", data => {
    body += data;
  });
  res.on("end", () => {
    body = JSON.parse(body);
    resq.json(body);
  });
	});
  ////////////////////////////  
});


////////////////////////get coordinates of place
app.get('/specificLocation/:location', function(req, resq) {
	
	url = "https://maps.googleapis.com/maps/api/geocode/json?address="+ encodeURIComponent(req.params.location) +"&key=AIzaSyCfc5tXvbhuaCD7DD7eC8Q14l3kj4UGTcw";
	
	var testing = "";
	
  resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  body1 = "";
  
  ////////////////////////////
  https.get(url, res => {
  res.setEncoding("utf8");
  
  res.on("data", data => {
    body += data;
  });
  res.on("end", () => {
    body = JSON.parse(body);
	resq.json(body);
  });
	});
	
});



////////////////////////get coordinates of place
app.get('/specificLocation/:location/radius/:radius/type/:category/keyword/:keyword', function(req, resq) {
	
	url = "https://maps.googleapis.com/maps/api/geocode/json?address="+ encodeURIComponent(req.params.location) +"&key=AIzaSyCfc5tXvbhuaCD7DD7eC8Q14l3kj4UGTcw";
	
	var testing = "";
	
  resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  body1 = "";
  
  ////////////////////////////
  https.get(url, res => {
  res.setEncoding("utf8");
  
  res.on("data", data => {
    body += data;
  });
  res.on("end", () => {
    body = JSON.parse(body);
	
	
	///////////////////////////////////send another request for getting nearby search result
	var coordinates = body.results[0].geometry.location.lat + "," + body.results[0].geometry.location.lng;
    
	if (req.params.type == "Default") {
		var additional = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + coordinates + "&radius=" + req.params.radius + "&keyword=" + encodeURIComponent(req.params.keyword) + "&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";
	} else {
		var additional = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + coordinates + "&radius=" + req.params.radius + "&type=" + req.params.category + "&keyword=" + encodeURIComponent(req.params.keyword) + "&key=AIzaSyDypyputCQXH9jGbhPPF0bP_8zm51Sk3IM";
	}
	
		https.get(additional, res1 => {
			res1.setEncoding("utf8");
			
			res1.on("data", data => {
				body1 += data;
			});
			res1.on("end", () => {
				body1 = JSON.parse(body1);
				resq.json(body1);
			});
		});
	////////////////////////////////////////////////////
	
  });
	});
	
});




app.get('/pagetoken/:token', function(req, resq) {

	url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + req.params.token + "&key=AIzaSyDR8NhFJu9QohwdS9-6atigU8CCfkRMlBM";
	
  resq.setHeader('Access-Control-Allow-Origin','*');
  body = "";
  
  ////////////////////////////
  https.get(url, res => {
  res.setEncoding("utf8");
  
  res.on("data", data => {
    body += data;
  });
  res.on("end", () => {
    body = JSON.parse(body);
    resq.json(body);
  });
	});
  ////////////////////////////  
});


app.get('/yelp/name/:name/address1/:address1/city/:city/state/:state/country/:country/postal_code/:postal/latitude/:latitude/longitude/:longitude', function(req, resq) {

  
  ////////////////////////////
  // matchType can be 'lookup' or 'best'
client.businessMatch('best', {
  name: req.params.name,
  address1: req.params.address1,
  latitude: req.params.latitude,
  longitude: req.params.longitude,
  city: req.params.city,
  state: req.params.state,
  country: req.params.country,
  postal_code: req.params.postal
}).then(response => {
  
	if (typeof response.jsonBody.businesses[0] != 'undefined'){
	
	client.reviews(response.jsonBody.businesses[0].id).then(responseReview => {
	resq.json(responseReview.jsonBody);
  
	}).catch(error => {
	console.log(error);
	});
	} else {
		resq.send("No result");
	}
  
}).catch(e => {
  console.log(e);
});
  ////////////////////////////  
});






app.listen(process.env.port || 8081, () => 
console.log('Example app listening on port 8081!'));



///////////////////////////////////

////////////////////////////////////


