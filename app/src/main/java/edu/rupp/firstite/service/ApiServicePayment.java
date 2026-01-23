package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.PaymentCart;
import edu.rupp.firstite.modals.YourBookModal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServicePayment {
    @POST("/events/payment")
    Call<Void> makePayment(@Header("Authorization") String authorizationHeader, @Body PaymentCart paymentCart);
    @GET("/events/payment")
    Call<List<YourBookModal>> loadYourBook(@Header("Authorization") String authorizationHeader);
}
