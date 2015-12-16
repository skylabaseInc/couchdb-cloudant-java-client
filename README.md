A few test cases for Cloudant Java
--

 - Add your Cloudant credentials to cloudant.properties.
 - Create a database with the following document:

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
```

 - Create views:
 
 ```
 {
  "_id": "_design/ddoc",
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
  }
}
```






