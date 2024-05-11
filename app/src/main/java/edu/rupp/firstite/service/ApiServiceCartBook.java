package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.AddCartBook;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServiceCartBook {
    @POST("/events/cart")
    Call<List<AddCartBook>> AddCartBook(@Header("Authorization") String authorizationHeader, @Body AddCartBook addCartBook);
}
