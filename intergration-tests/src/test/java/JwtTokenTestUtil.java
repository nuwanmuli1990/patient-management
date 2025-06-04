import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class JwtTokenTestUtil {

	public static String getJwtToken(){
		String loginPayload = """
					{
						"email": "testuser@test.com",
						"password": "password123"
					}
				""";

		return given()
				.contentType(ContentType.JSON)
				.body(loginPayload)
				.when()
				.post("/auth/login")
				.then()
				.statusCode(200)
				.extract()
				.jsonPath().getString("jwtToken");
	}
}
