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
        sshKey = "ssh-rsa ";
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token ")
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
