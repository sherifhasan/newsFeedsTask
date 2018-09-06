package example.android.newsfeeds.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.android.newsfeeds.database.ArticlesProvider;
import example.android.newsfeeds.models.Article;
import example.android.newsfeeds.models.News;
import example.android.newsfeeds.network.retrofit.ApiClient;
import example.android.newsfeeds.network.retrofit.ApiInterface;
import example.android.newsfeeds.ui.fragments.MainActivityFragment;
import example.android.newsfeeds.utility.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_AUTHOR;
import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_DESCRIPTION;
import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_IMAGE_URL;
import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_PUBLISHED;
import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_TITLE;
import static example.android.newsfeeds.database.ArticlesContract.ArticlesTableEntry.COLUMN_URL;
import static example.android.newsfeeds.utility.Utility.isNetworkConnected;
import static example.android.newsfeeds.utility.Utility.toDate;
import static example.android.newsfeeds.utility.Utility.toStringDate;


public class NewsPresenter {


    private List<Article> articleList;
    private Throwable error;
    private MainActivityFragment fragment;
    private Context context;

    public NewsPresenter(Context context) {
        this.context = context;
        if (isNetworkConnected(context)) {
            getNewsFeedsFromApi();
        } else {
            getNewsFeedsFromDatabase(context);
        }
    }


    private void getNewsFeedsFromApi() {

        ApiInterface apiService = ApiClient.getClient();
        Call<News> call = apiService.getNewsFeeds(Utility.SOURCE, Utility.API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("ok")) {
                    Log.d("Articles size: ", (response.body().getArticles().size() + toString()));
                    articleList = new ArrayList<>();
                    articleList.addAll(response.body().getArticles());
                    publish();
                } else {
                    Log.d("Get Articles : ", "Response is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Log.d("onFailure :", t.toString());
                error = t;
                publish();
            }
        });
    }


    private void insertArticles(final Context context) {
        context.getContentResolver().delete(ArticlesProvider.Articles.articleUri, null, null);
        ContentValues[] cvs = new ContentValues[articleList.size()];
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            cvs[i] = new ContentValues();
            if (article.getTitle() != null) {
                cvs[i].put(COLUMN_TITLE, article.getTitle());
            }
            if (article.getDescription() != null) {
                cvs[i].put(COLUMN_DESCRIPTION, article.getDescription());
            }
            if (article.getUrl() != null) {
                cvs[i].put(COLUMN_URL, article.getUrl());
            }
            if (article.getAuthor() != null) {
                cvs[i].put(COLUMN_AUTHOR, article.getAuthor());
            }
            if (article.getPublishedAt() != null) {
                cvs[i].put(COLUMN_PUBLISHED, toStringDate(article.getPublishedAt()));
            }
            if (article.getUrlToImage() != null) {
                cvs[i].put(COLUMN_IMAGE_URL, article.getUrlToImage());
            }
        }
        int numOfRows = context.getContentResolver().bulkInsert(ArticlesProvider.Articles.articleUri, cvs);
        Log.d("num of rows : ", String.valueOf(numOfRows));
    }


    private void getNewsFeedsFromDatabase(final Context context) {
        Cursor cursor = context.getContentResolver().query(ArticlesProvider.Articles.articleUri,
                null, null, null, null);
        List<Article> articles = new ArrayList<Article>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Article article = new Article();
                    article.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    article.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                    article.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                    article.setUrlToImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL)));
                    article.setPublishedAt(toDate(cursor.getString(cursor.getColumnIndex(COLUMN_PUBLISHED))));
                    article.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                    articles.add(article);
                } while (cursor.moveToNext());
            }
            cursor.close();
            articleList = new ArrayList<>();
            articleList.addAll(articles);
        }
    }

    public void onTakeView(MainActivityFragment view) {
        this.fragment = view;
        publish();
    }

    private void publish() {
        if (fragment != null) {
            if (articleList != null) {
                insertArticles(context);
                fragment.onArticleNext(articleList);
            } else if (error != null) {
                fragment.onArticleError(error);
            }
        }
    }
}