package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.AuthorBookDisplay;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceAuthorBook {
    @GET("/books/book")
    Call<List<AuthorBookDisplay>> FilterAuthorBook(@Header("Authorization") String authorizationHeader);
}
