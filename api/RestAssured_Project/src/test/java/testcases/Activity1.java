package testcases;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity1 {
    final static String baseURI = "https://petstore.swagger.io/v2/pet";

    @Test(priority = 1)
    public void PostCreatePet(){
        String reqBody ="{\"id\":7723237, \"name\":\"Relishy\", \"status\":\"alive\"}";
        Response response = given().contentType(ContentType.JSON).body(reqBody)
                .when().post(baseURI);
        String body = response.getBody().asPrettyString();
        System.out.println(body);
        response.then().statusCode(200);
        response.then().body("id", equalTo(7723237));
        response.then().body("name", equalTo("Relishy"));
        response.then().body("status", equalTo("alive"));
    }

    @Test(priority = 2)
    public void GetPetDetails(){

        Response response = given().contentType(ContentType.JSON)
                .when().get(baseURI+"/findByStatus?status=available");

        String responseBody = response.getBody().asString();
        List<Header> responseHeader = response.getHeaders().asList();
        System.out.println("Response body is =>"+responseBody);
        System.out.println("Response Headers is =>"+ responseHeader);
        Boolean headerAvailability =  response.then().extract().headers().hasHeaderWithName("Date");
        response.then().statusCode(200);

        System.out.println(headerAvailability);
        System.out.println("First id => "+response.then().extract().path("[0].'id'"));
    }

    @Test(priority=3)
    public void deletePet() {
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .when().pathParam("petId", "7723237") // Set path parameter
                        .delete(baseURI + "/{petId}"); // Send DELETE request

        // Assertion
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("7723237"));
    }




}
