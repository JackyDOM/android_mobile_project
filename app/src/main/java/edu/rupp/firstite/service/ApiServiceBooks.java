package edu.rupp.firstite.service;

import java.util.List;

import edu.rupp.firstite.modals.Banner;
import edu.rupp.firstite.modals.Book;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiServiceBooks {
    @GET("/books/book")
    Call<List<Book>> loadBooks(@Header("Authorization") String token, @Query("bookType") String bookType);
}
