package testcases;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.equalTo;

import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RoughWork {
    final static String baseURI = "https://petstore.swagger.io/v2/pet";
    @Test
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

    @Test
    public void PostCreatePet(){
        String reqBody ="{\"id\":7723237, \"name\":\"Relishy\", \"status\":\"alive\"}";
        Response response = given().contentType(ContentType.JSON).body(reqBody)
                .when().post(baseURI);
        String body = response.getBody().asPrettyString();
        System.out.println(body);
    }

    @Test
    public void getCreatedPet(){
        Response response = given().contentType(ContentType.JSON)
                .when().get(baseURI+"/7723237");

        System.out.println("Response for the get request =>"+ response.asPrettyString());
        response.then().body("status", equalTo("alive"));

    }


    @Test(dataProvider = "petIdProvider")
    public void simple_get_test(String id) {
        Response response =
                given().contentType(ContentType.JSON) // Set headers
                        .when().get(baseURI +"/"+id); // Send GET request
        given().log().body();
                given().log().uri();
        // Print response
        System.out.println(response.asPrettyString());
        // Assertions
        response.then().body("status", equalTo("alive"));
    }

    @DataProvider
    public Object[][] petIdProvider() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] { { "7723237" }, { "77233" } };
        return testData;
    }

    @Test
    public void pathParameterTc(){
        String baseURI = "http://ip-api.com/json/{ipAddress}";

        Response response = given().contentType(ContentType.JSON)
                .when().pathParam("ipAddress","107.218.134.107")
                .get(baseURI);

        System.out.println(response.getBody().asPrettyString());

    }

    @Test
    public void schemaValidationTC(){
        Response response = given().contentType(ContentType.JSON)
                .when().get(baseURI+"/7723237");
        given().log().uri();
        given().log().body();

        System.out.println(response.prettyPrint());
        URL swaggerSchema = null;
        try {
            swaggerSchema = new URL("https://petstore.swagger.io/v2/swagger.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        response.then().body(matchesJsonSchema(swaggerSchema));

    }

    @Test
    public void usingFilesTc() throws FileNotFoundException {

        FileInputStream inputJson = new FileInputStream("input.json");
        File resJsonFile = new File("output.json");
        String reqBody = inputJson.toString();

        Response response = given().contentType(ContentType.JSON)
                .body(reqBody)
                .when().post(baseURI);
        given().log().body();
        String resBody = response.getBody().asPrettyString();
        System.out.println(resBody);

        try {
            resJsonFile.createNewFile();
            FileWriter writer = new FileWriter(resJsonFile.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
