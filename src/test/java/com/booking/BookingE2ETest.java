package com.booking;

import datashare.BookingData;
import datashare.PartialBookingData;
import datashare.Tokencreds;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static datashare.BookingDataBuilder.*;
import static datashare.TokenBuilder.getToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test Booking API E2E flow:
 * 1. Create booking
 * 2. GetBooking based on booking Id
 * 3. Update booking
 * 4. Update booking Partially
 * 5. Delete booking
 */
public class BookingE2ETest extends BaseClass {

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
    public void createBookingTest () {
        bookingId = given ().body (newBooking)
            .when ()
            .post ("/booking")
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body ("bookingid", notNullValue ())
            .body ("booking.firstname", equalTo (newBooking.getFirstname ()))
            .body ("booking.lastname", equalTo (newBooking.getLastname ()))
            .body ("booking.totalprice", equalTo (newBooking.getTotalprice ()))
            .body ("booking.depositpaid", equalTo (newBooking.isDepositpaid ()))
            .body ("booking.bookingdates.checkin", equalTo (newBooking.getBookingdates ()
                .getCheckin ()))
            .body ("booking.bookingdates.checkout", equalTo (newBooking.getBookingdates ()
                .getCheckout ()))
            .body ("booking.additionalneeds", equalTo (newBooking.getAdditionalneeds ()))
            .extract ()
            .path ("bookingid");
    }

    @Test
    public void getBookingTest () {
        given ().get ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body ("firstname", equalTo (newBooking.getFirstname ()))
            .body ("lastname", equalTo (newBooking.getLastname ()))
            .body ("totalprice", equalTo (newBooking.getTotalprice ()))
            .body ("depositpaid", equalTo (newBooking.isDepositpaid ()))
            .body ("bookingdates.checkin", equalTo (newBooking.getBookingdates ()
                .getCheckin ()))
            .body ("bookingdates.checkout", equalTo (newBooking.getBookingdates ()
                .getCheckout ()))
            .body ("additionalneeds", equalTo (newBooking.getAdditionalneeds ()));

    }

    @Test
    public void updateBookingTest () {
        given ().body (updatedBooking)
            .when ()
            .header ("Cookie", "token=" + generateToken ())
            .put ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body ("firstname", equalTo (updatedBooking.getFirstname ()))
            .body ("lastname", equalTo (updatedBooking.getLastname ()))
            .body ("totalprice", equalTo (updatedBooking.getTotalprice ()))
            .body ("depositpaid", equalTo (updatedBooking.isDepositpaid ()))
            .body ("bookingdates.checkin", equalTo (updatedBooking.getBookingdates ()
                .getCheckin ()))
            .body ("bookingdates.checkout", equalTo (updatedBooking.getBookingdates ()
                .getCheckout ()))
            .body ("additionalneeds", equalTo (updatedBooking.getAdditionalneeds ()));

    }

    @Test
    public void updatePartialBookingTest () {
        given ().body (partialUpdateBooking)
            .when ()
            .header ("Cookie", "token=" + generateToken ())
            .patch ("/booking/" + bookingId)
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body ("firstname", equalTo (partialUpdateBooking.getFirstname ()))
            .body ("lastname", equalTo (updatedBooking.getLastname ()))
            .body ("totalprice", equalTo (partialUpdateBooking.getTotalprice ()))
            .body ("depositpaid", equalTo (updatedBooking.isDepositpaid ()))
            .body ("bookingdates.checkin", equalTo (updatedBooking.getBookingdates ()
                .getCheckin ()))
            .body ("bookingdates.checkout", equalTo (updatedBooking.getBookingdates ()
                .getCheckout ()))
            .body ("additionalneeds", equalTo (updatedBooking.getAdditionalneeds ()));

    }

    @Test
    public void deleteBookingTest () {
        given ().header ("Cookie", "token=" + generateToken ())
            .when ()
            .delete ("/booking/" + bookingId)
            .then ()
            .statusCode (201);
    }

    @Test
    public void checkBookingIsDeleted () {
        given ().get ("/booking/" + bookingId)
            .then ()
            .statusCode (404);
    }

    //Creates a new auth token to use for access to the PUT and DELETE /booking
    private String generateToken () {
        return given ().body (tokenCreds)
            .when ()
            .post ("/auth")
            .then ()
            .statusCode (200)
            .and ()
            .assertThat ()
            .body ("token", Matchers.not (nullValue ()))
            .extract ()
            .path ("token");
    }

}