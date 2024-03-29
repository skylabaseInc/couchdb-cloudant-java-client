import org.apache.commons.beanutils.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewResponse;
import com.cloudant.client.api.views.ViewResponse.Row;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Properties;


public class ExampleTest {

	CloudantClient client;
	Database db;

	@Before
	public void setup() throws FileNotFoundException, IOException {

		Properties props = new Properties();
		props.load(new FileInputStream("cloudant.properties"));

		client = ClientBuilder
				.url(new URL(props.getProperty("cloudant_account")))
				.username(props.getProperty("cloudant_username"))
				.password(props.getProperty("cloudant_password"))
				.build();

		db = client.database(props.getProperty("cloudant_database"), true);
	}

	@After
	public void tearDown() {
		client.shutdown();
	}

	@Test
	public void testFindAllProducts() throws IOException {

		List<String> list = db.getViewRequestBuilder("ddoc","find-by-product-name")
				.newRequest(Key.Type.STRING, Object.class)
				.limit(10)
				.includeDocs(true)
				.build()
				.getResponse()
				.getKeys();

		assertEquals(2, list.size());
	}


	@Test
	public void testFindByProductName() throws IOException {

		List<String> list = db.getViewRequestBuilder("ddoc","find-by-product-name")
				.newRequest(Key.Type.STRING, Object.class)
				.keys("Peanut butter")
				.limit(10)
				.includeDocs(true)
				.build()
				.getResponse()
				.getKeys();

		String[] expected = { "Peanut butter" };
		assertArrayEquals(expected, list.toArray());
	}

	@Test
	public void testFindByProductNameNoMatch() throws IOException {

		List<String> list = db.getViewRequestBuilder("ddoc","find-by-product-name")
				.newRequest(Key.Type.STRING, Object.class)
				.keys("XXXXX")
				.limit(10)
				.includeDocs(true)
				.build()
				.getResponse()
				.getKeys();

		assertEquals(0, list.size());
	}

	/*
	@Test
	public void testFindByStoreName() throws IOException {

		ViewResponse<String, Integer> response = db.getViewRequestBuilder("ddoc","find-by-store-name")
				.newRequest(Key.Type.STRING, Integer.class)
				.startKey("Mo")
				.endKey("T")
				.inclusiveEnd(true)
				.limit(10)
				.includeDocs(true)
				.descending(false)
				.build()
				.getResponse();

		List<Row<String, Integer>> rows = response.getRows();

		assertEquals("Morrisons", 			rows.get(0).getKey());
		assertEquals(Integer.valueOf(97), 	rows.get(0).getValue());

		assertEquals("Saintburrys", 		rows.get(1).getKey());
		assertEquals(Integer.valueOf(72), 	rows.get(1).getValue());
	}
	
	@Test
	public void testFindByStoreNameSearch() throws Exception {
		
		List<Object> products = db.search("ddoc/find-by-store-name")
			 	.limit(10)
			 	.sort("\"storename<string>\"")
			 	.includeDocs(true)
			 	.query("storename:T*", Object.class);

		assertEquals(2, products.size());
		
		// for debugging - print out json
		Gson gson = new Gson();
		System.out.println(gson.toJson(products.get(0)));
		System.out.println(gson.toJson(products.get(1)));

		Object peanutButter = products.get(0);
		Object beans = products.get(1);

		assertEquals("Peanut butter", JsonPath.read(peanutButter, "$.name"));
		assertEquals("Morrisons",     JsonPath.read(peanutButter, "$.prices[0].storeName"));
		assertEquals("Saintburrys",   JsonPath.read(peanutButter, "$.prices[1].storeName"));
		assertEquals("Tesco",         JsonPath.read(peanutButter, "$.prices[2].storeName"));
		
		assertEquals("Beans",                JsonPath.read(beans,        "$.name"));
		assertEquals("The Health Food Shop", JsonPath.read(beans,        "$.prices[0].storeName"));
	}
	*/

}
