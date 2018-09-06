package example.android.newsfeeds.network.retrofit;

import example.android.newsfeeds.models.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("articles")
    Call<News> getNewsFeeds(@Query("source") String source, @Query("apiKey") String apiKey);
}
