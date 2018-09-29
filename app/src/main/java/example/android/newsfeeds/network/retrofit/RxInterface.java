package example.android.newsfeeds.network.retrofit;
import example.android.newsfeeds.models.News;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RxInterface {
    @GET("articles")
    Single<News> getNewsFeeds(@Query("source") String source, @Query("apiKey") String apiKey);
}
