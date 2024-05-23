package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.YourBookModal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceYourBook {
    @GET("/events/payment")
    Call<List<YourBookModal>> loadYourBook(@Header("Authorization") String authorizationHeader);
}


