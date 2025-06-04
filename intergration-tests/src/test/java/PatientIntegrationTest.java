import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {

	@BeforeAll
	static void setUp(){
		RestAssured.baseURI = "http://localhost:4004";
	}

	@Test
	public void shouldReturnPatientWithValidToken(){
		String jwtToken = JwtTokenTestUtil.getJwtToken();

		Response response = given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType(ContentType.JSON)
				.when()
				.get("/api/patients")
				.then()
				.statusCode(200)
				.body(notNullValue())
				.extract()
				.response();
		System.out.printf("Response from server: %s", response.getBody().prettyPrint());

	}
}
