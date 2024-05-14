package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Author;
import edu.rupp.firstite.modals.Searching;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiServiceSearching {
    @GET("/books/book/{title}")
    Call<List<Searching>> loadSearchingImage(@Header("Authorization") String authorizationHeader, @Path("title") String title);
}
