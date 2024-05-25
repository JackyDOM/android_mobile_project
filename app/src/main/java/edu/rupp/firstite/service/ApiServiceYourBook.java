package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.YourBookModal;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiServiceYourBook {
    @GET("/events/payment")
    Call<List<YourBookModal>> loadYourBook(@Header("Authorization") String authorizationHeader);
    @DELETE("/events/payment/{id}")
    Call<Void> deleteYourBook(@Header("Authorization") String authHeader, @Path("id") int bookId);
}
