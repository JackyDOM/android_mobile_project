package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Banner;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiServiceBanner {
    @GET("/books/book")
    Call<List<Banner>> loadBannerImage(@Header("Authorization") String authorizationHeader);

}
