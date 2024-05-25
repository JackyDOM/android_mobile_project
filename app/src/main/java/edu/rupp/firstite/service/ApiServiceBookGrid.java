package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.BookGridDisplay;
import edu.rupp.firstite.modals.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiServiceBookGrid {
    @GET("/books/category")
    Call<List<Category>> getCategories(@Header("Authorization") String authHeader);
    @GET("/books/book")
    Call<List<BookGridDisplay>> FilterByCategory(@Header("Authorization") String authorizationHeader,@Query("categoryName") String categoryName);
}
