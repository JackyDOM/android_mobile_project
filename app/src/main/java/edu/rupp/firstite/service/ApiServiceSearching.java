package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Author;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceSearching {
    @GET("/books/book/{id}")
    Call<List<Author>> loadSearchingImage(@Header("Authorization") String authorizationHeader);
}
