package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Author;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceAuthor {

    @GET("/author/author")
    Call<List<Author>> loadAuthorImage(@Header("Authorization") String authorizationHeader);
}
