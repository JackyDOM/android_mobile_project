
package edu.rupp.firstite.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://elibra.skinme.store/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            long cacheSize = 10 * 1024 * 1024; // 10 MB
            File cacheDir = new File(context.getCacheDir(), "http-cache");
            Cache cache = new Cache(cacheDir, cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static class AuthInterceptor implements Interceptor {
        private final Context context;

        public AuthInterceptor(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String accessToken = prefs.getString("access_token", null);

            Request.Builder requestBuilder = chain.request().newBuilder();
            if (accessToken != null && !accessToken.isEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
            }

            return chain.proceed(requestBuilder.build());
        }
    }
}
