package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.CategoryBanner2;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceCategory2 {
    @GET("/books/book")
    Call<List<CategoryBanner2>> loadCategory2Image(@Header("Authorization") String authorizationHeader);
}

