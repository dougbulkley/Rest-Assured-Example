package com.doug.rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;



/*
 * Restassured information found here:
 * https://github.com/jayway/rest-assured
 */

@SuppressWarnings("deprecation")
public final class RestAssuredExample {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static void main(String[] args) throws IOException {
    new RestAssuredExample().doMain(args);
  }

  public void doMain(String[] args) throws IOException {

    //RestAssured.baseURI  = "https://localhost";
    RestAssured.baseURI = "http://jsonplaceholder.typicode.com";
    //RestAssured.useRelaxedHTTPSValidation();
    RestAssured.port = 80;
    //RestAssured.basePath = "/config/v2";
    //RestAssured.authentication = basic(adminUser, adminUserPass);
    RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
    //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    getAllUsers();
    getSpecificUser();
    postToPosts();

  }

  public void getAllUsers() {
    try {
      when().
        get("/users").
      then().
        assertThat().statusCode(200).
        assertThat().body("any { it.containsKey('name') }", is(true));
    }
    catch (AssertionError ae) {
      System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ae.getMessage());
      System.exit(1);
    }

  }

  public void getSpecificUser() {
    try {
      when().
        get("/users/1").
      then().
        assertThat().statusCode(200).
        assertThat().body("id", equalTo(1)).
        assertThat().body("email", containsString("april")).
        assertThat().body("address.city", equalTo("Gwenborough"));
    }
    catch (AssertionError ae) {
      System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ae.getMessage());
      System.exit(1);
    }

  }

  public void postToPosts() {
    try {
      ObjectNode data = MAPPER.createObjectNode();
      data.put("title", "Doug Test");
      data.put("body", "This is a test");
      data.put("userId", "1");

      given().
        body(data).
      when().
        post("/posts/").
      then().
        assertThat().statusCode(201).
        assertThat().body("id", equalTo(101));
    }
    catch (AssertionError ae) {
      System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ae.getMessage());
      System.exit(1);
    }
  }



}
