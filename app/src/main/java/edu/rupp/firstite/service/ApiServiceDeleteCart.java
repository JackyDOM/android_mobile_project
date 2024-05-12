package edu.rupp.firstite.service;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiServiceDeleteCart {
    @DELETE("events/cart/{id}")
    Call<Void> deleteCartItem(@Header("Authorization") String authorizationHeader, @Path("id") int itemId);
}
