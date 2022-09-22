package com.booking;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import static org.hamcrest.Matchers.lessThan;

/**
 * Base class to bundle URI and Headers API execution
 */
public class BaseClass {

    @BeforeClass
    public void setup () {

        RequestSpecification requestSpecification = new RequestSpecBuilder ()
            .setBaseUri ("https://restful-booker.herokuapp.com")
            .addHeader ("Content-Type", "application/json")
            .addHeader ("Accept", "application/json")
            .addFilter (new RequestLoggingFilter ())
            .addFilter (new ResponseLoggingFilter ())
            .build ();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder ().expectResponseTime (lessThan (20000L))
            .build ();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;

    }
}