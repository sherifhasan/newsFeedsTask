package example.android.newsfeeds.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.android.newsfeeds.database.ArticlesProvider;
import example.android.newsfeeds.models.Article;
import example.android.newsfeeds.models.News;
import example.android.newsfeeds.network.retrofit.RxClient;
import example.android.newsfeeds.network.retrofit.RxInterface;
import example.android.newsfeeds.ui.fragments.MainActivityFragment;
import example.android.newsfeeds.utility.Utility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

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
    private String error;
    private MainActivityFragment fragment;
    private Context context;
    private CompositeDisposable disposable;

    public NewsPresenter(Context context, CompositeDisposable disposable) {
        this.context = context;
        this.disposable = disposable;
        if (isNetworkConnected(context)) {
            getNewsRX();
        } else {
            getNewsFeedsFromDatabase();
        }
    }

    private void getNewsRX() {
        RxInterface rxApi = RxClient.getClient();
        final DisposableSingleObserver<News> subscription = rxApi.getNewsFeeds(Utility.SOURCE, Utility.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<News>() {
                    @Override
                    public void onSuccess(News news) {
                        if (news != null && news.getStatus().equals("ok")) {
                            Log.d("Articles size: ", (news.getArticles().size() + toString()));
                            articleList = new ArrayList<>();
                            articleList.addAll(news.getArticles());
                            insertArticles();
                            publish();
                        } else {
                            Log.d("Get Articles : ", "Response is null");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onFailure :", e.getMessage());
                    }
                });
        disposable.add(subscription);
    }

    private void insertArticles() {
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

    private void getNewsFeedsFromDatabase() {
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
            if (articleList.isEmpty()) {
                error = "No local database nor internet connection";
            }
            publish();
        }
    }

    public void onTakeView(MainActivityFragment view) {
        this.fragment = view;
        publish();
    }

    private void publish() {
        if (fragment != null) {
            if (articleList != null) {
                fragment.onArticleNext(articleList);
            } else if (error != null) {
                fragment.onArticleError(error);
            }
        }
    }
}