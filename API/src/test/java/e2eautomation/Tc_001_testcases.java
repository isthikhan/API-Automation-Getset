package e2eautomation;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import lib.createpost;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class Tc_001_testcases {

	@Test
	public void tcase1() {

		/// Post Request

		int input = 11;
		createpost cpost = new createpost();
		cpost.setId(input);
		cpost.setTitle("Training");
		cpost.setAuthor("Testhouse");

		ValidatableResponse response = given().contentType(ContentType.JSON).body(cpost).when()
				.post("http://localhost:3000/posts").then().contentType(ContentType.JSON);

		int responseid = response.extract().path("id");
		int Actualstatus = response.extract().response().getStatusCode();
		Assert.assertEquals(Actualstatus, 201);

		/// Get Request

		when().get("http://localhost:3000/posts/" + responseid).then().contentType(ContentType.JSON);

		String actTitle = response.extract().path("title");
		String actauthor = response.extract().path("author");

		Assert.assertEquals(actTitle, "Training");
		Assert.assertEquals(actauthor, "Testhouse");
		
		
		//update request
		
		createpost cpost1 = new createpost();
		cpost1.setId(input);
		cpost1.setTitle("updatedTraining");
		cpost1.setAuthor("updatedTesthouse");

		ValidatableResponse response1 = given().contentType(ContentType.JSON).body(cpost1).when()
				.put("http://localhost:3000/posts/"+input).then().contentType(ContentType.JSON);
		
		//updated verification
		ValidatableResponse response2 = when().get("http://localhost:3000/posts/" + responseid).then().contentType(ContentType.JSON);
		
		String actTitle1 = response2.extract().path("title");
		String actauthor1 = response2.extract().path("author");

		Assert.assertEquals(actTitle1, "updatedTraining");
		Assert.assertEquals(actauthor1, "updatedTesthouse");
		
		//Delete  request
		
		when().delete("http://localhost:3000/posts/" + responseid);
		ValidatableResponse response3 = when().get("http://localhost:3000/posts/" + responseid).then().contentType(ContentType.JSON);
		Assert.assertNotEquals(response3.extract().response().statusCode(), 201);

	}

}
