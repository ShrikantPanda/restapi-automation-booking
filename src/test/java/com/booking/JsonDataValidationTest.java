package com.booking;

import datashare.BookingData;
import datashare.PartialBookingData;
import datashare.Tokencreds;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;

import static datashare.BookingDataBuilder.getBookingData;
import static datashare.BookingDataBuilder.getPartialBookingData;
import static datashare.TokenBuilder.getToken;
import static io.restassured.RestAssured.given;

/**
 * To validate all the data passed in form of Json files
 **/
public class JsonDataValidationTest extends BaseClass {

    private BookingData        newBooking;
    private BookingData        updatedBooking;
    private PartialBookingData partialUpdateBooking;
    private Tokencreds         tokenCreds;
    private int                bookingId;

    @BeforeTest
    public void testSetup () {
        newBooking = getBookingData ();
        updatedBooking = getBookingData ();
        partialUpdateBooking = getPartialBookingData ();
        tokenCreds = getToken ();
    }

    @Test
    public void testCreateBookingJsonSchema () {

        BookingData newBooking = getBookingData ();
        InputStream createBookingJsonSchema = getClass ().getClassLoader ()
            .getResourceAsStream ("createbookingjsonschema.json");
        bookingId = given ().body (newBooking)
            .when ()
            .post ("/booking")
            .then ()
            .statusCode (200)
            .and()
            .assertThat ()
            .body (JsonSchemaValidator.matchesJsonSchema (createBookingJsonSchema))
            .and ()
            .extract ()
            .path ("bookingid");
    }

    @Test
    public void testGetBookingJsonSchema () {

        InputStream getBookingJsonSchema = getClass ().getClassLoader ()
            .getResourceAsStream ("getbookingjsonschema.json");

        given ().when ()
            .get ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .assertThat ()
            .body (JsonSchemaValidator.matchesJsonSchema (getBookingJsonSchema));
    }

    @Test
    public void testUpdateBookingJsonSchema() {
        InputStream updateBookingJsonSchema = getClass ().getClassLoader ()
            .getResourceAsStream ("updatebookingjsonschema.json");

        given ().when ().body (updatedBooking)
            .get ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .assertThat ()
            .body (JsonSchemaValidator.matchesJsonSchema (updateBookingJsonSchema));
    }

    @Test
    public void testUpdatePartialBookingJsonSchema() {
        InputStream updatePartialBookingJsonSchema = getClass ().getClassLoader ()
            .getResourceAsStream ("updatepartialbookingjsonschema.json");

        given ().when ().body (partialUpdateBooking)
            .get ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .assertThat ()
            .body (JsonSchemaValidator.matchesJsonSchema (updatePartialBookingJsonSchema));
    }

    @Test
    public void testCreateJsonSchema() {
        InputStream createTokenJsonSchema = getClass ().getClassLoader ()
            .getResourceAsStream ("createtokenjsonschema.json");

        given ().body (tokenCreds)
            .when ()
            .post ("/auth")
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body (JsonSchemaValidator.matchesJsonSchema (createTokenJsonSchema));
    }
}
