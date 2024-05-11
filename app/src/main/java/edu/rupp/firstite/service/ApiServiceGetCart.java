package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Book;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceGetCart {
    @GET("/events/cart")
    Call<List<Book>> loadCartImage(@Header("Authorization") String authorizationHeader);
}
