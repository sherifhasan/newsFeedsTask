package example.android.newsfeeds.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class ApiClient {
    private static final String BASE_URL = "https://newsapi.org/v1/";
    private static ApiInterface retrofit;

    public static ApiInterface getClient() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        OkHttpClient.Builder OkHttpBuilder = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(OkHttpBuilder.build())
                .build().create(ApiInterface.class);

        return retrofit;
    }
}
