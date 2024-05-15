package edu.rupp.firstite.service;

import edu.rupp.firstite.modals.PaymentCart;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServicePayment {
    @POST("/events/payment")
    Call<Void> makePayment(@Header("Authorization") String authorizationHeader, @Body PaymentCart paymentCart);
}
