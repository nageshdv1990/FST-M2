package testcases;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.equalTo;

public class ProjectGithubRestAssured {

    RequestSpecification requestSpecification;
    String sshKey;
    int sshId;

    @BeforeClass
    public void beforeClass() {
        sshKey = "ssh-rsa AAAAB3NzaC1yWnBS9itX9kk+pwyRvmZWTQuvNFeC4mtLog7jXQMBrMCzuP+4PtOTvgCQ4f7wBUVKGaOJ6O9LxIkzaFyXqwSZ/GllmtRVJseJrgBvcB6rJIV5Rcaqc0hsH2I9op3wXghkXRHpoZ+khyY1HpOPDIR0nE/FE9tWe4A/G/MDmXvfNmu7hSA9fK10g5v0qECb0JfoJ1xDh4cfFIjycyubS/3wam7qULpq9c/XAptLh6KXV7956eoHoFOzkNIBG9S4ftxg54F2df4ZX3dqiLPwFR2kaRoVI2498q3UHb7";
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token ghp_Tez75EJF5JZg32XRLx6")
                .setBaseUri("https://api.github.com")
                .build();
    }

        @Test(priority = 1)
        public void addSSHKey(){
            String requestBody = "{\"title\": \"TestKey\", \"key\": \"" + sshKey + "\" }";
            Response response = given().spec(requestSpecification).body(requestBody)
                    .when().post("/user/keys");

            String responseBody = response.getBody().asPrettyString();
            System.out.println(responseBody);

            response.then().statusCode(201);
        }

        @Test(priority = 2)
        public void getSSHKey(){
            Response response = given().spec(requestSpecification)
                    .when().get("/user/keys");

            String responseBody = response.getBody().asPrettyString();
            System.out.println(responseBody);
            response.then().statusCode(200);
            sshId = response.then().extract().path("[0].'id'");
            System.out.println(sshId);

        }

        @Test(priority = 3)
        public void deleteSSHKey(){
            Response response = given().spec(requestSpecification)
                    .pathParam("keyId",sshId)
                    .when().delete("/user/keys/{keyId}");
            String responseBody = response.getBody().asPrettyString();
            System.out.println(responseBody);
            response.then().statusCode(204);


        }

}
