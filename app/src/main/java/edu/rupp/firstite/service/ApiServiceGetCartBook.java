package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.modals.CartBookModal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceGetCartBook {

    @GET("/events/cart")
    Call<List<CartBookModal>> GetBookCart(@Header("Authorization") String authorizationHeader);
}
