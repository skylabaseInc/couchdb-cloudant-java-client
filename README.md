A few test cases for Cloudant Java Connecting to CouchDB 2.1.0
--

 - Copy cloudant.properties_template to cloudant.properties
 - Add your Cloudant credentials to cloudant.properties
 - Create Database ```cloudant``` in your local CouchDB 
 - Update the Databse with the following documents:

```
{
    "_id": "68",
    "name": "Peanut butter",
    "title": "Peanut butter is the name",
    "description": "Peanut butter is the description",
    "prices": [
      {
        "storeName": "Morrisons",
        "price": 97
      },
      {
        "storeName": "Saintburrys",
        "price": 72
      },
      {
        "storeName": "Tesco",
        "price": 79
      }
    ],
    "rev": null
}

{
  "_id": "69",
  "name": "Beans",
  "title": "Beans is the name",
  "description": "Beans is the description",
  "prices": [
    {
      "storeName": "The Health Food Shop",
      "price": 97
    }
  ],
  "rev": null
}
```

 - Create views:
 
 ```
{
  "_id": "_design/ddoc",
  "_rev": "12-c8b90da86721e12b647e8bdb9ddf37ab",
  "views": {
    "find-by-product-name": {
      "map": "function (doc) {
				  if (doc.name) {
				    emit(doc.name, null);
				  }
		}"
    },
    "find-by-store-name": {
      "map": "function (doc) {
				  var shop, price, storename,storeprice;
				  if (doc.name && doc.prices) {
				      for (shop in doc.prices) {
				          storename = doc.prices[shop].storeName;
				          storeprice = doc.prices[shop].price;
				          emit(storename, storeprice);
				      }
				  }
		}"
    }
  },
  "indexes": {
    "find-by-store-name": {
      "analyzer": "keyword",
      "index": "function (doc) {
			        var shop, price, storename;
			        if (doc.name && doc.prices) {
			           for (shop in doc.prices) {
			              storename = doc.prices[shop].storeName;
			              index(\"storename\", storename, {\"store\": true});
			           }
			        }
		}"
    }
  }
}
```

 - Run tests:
 
 ```
 ./gradlew check
 ```






