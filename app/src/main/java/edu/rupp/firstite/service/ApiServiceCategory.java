package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.CategoryBanner1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceCategory {
    @GET("/books/book")
    Call<List<CategoryBanner1>> loadCategoryImage(@Header("Authorization") String authorizationHeader);
}
