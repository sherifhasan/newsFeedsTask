package example.android.newsfeeds.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.newsfeeds.R;
import example.android.newsfeeds.models.Article;
import example.android.newsfeeds.presenter.NewsPresenter;
import example.android.newsfeeds.ui.activities.NewsDetailsActivity;
import example.android.newsfeeds.ui.adapters.NewsAdapter;


public class MainActivityFragment extends Fragment {

    private static final String NEWS_LIST = "news_list";

    NewsPresenter newsPresenter;
    List<Article> articleList;
    NewsAdapter newsAdapter;
    LinearLayoutManager layoutManager;
    @BindView(R.id.news_fragment)
    LinearLayout newsLayout;
    @BindView(R.id.news_list)
    RecyclerView recyclerView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        ButterKnife.bind(this, rootView);

        newsAdapter = new NewsAdapter(getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(newsAdapter);

        newsAdapter.setListener(new NewsAdapter.Listener() {
            @Override
            public void onClick(Article article) {
                NewsDetailsActivity.startActivity(getActivity(), article);
            }
        });

        if (savedInstanceState != null) {
            articleList = savedInstanceState.getParcelableArrayList(NEWS_LIST);
            newsAdapter.updateAdapter(articleList);
        }
        return rootView;
    }

    public void onArticleNext(List<Article> articles) {
        articleList = articles;
        newsAdapter.updateAdapter(articleList);
    }

    public void onArticleError(Throwable throwable) {
        Snackbar.make(newsLayout, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        newsPresenter = new NewsPresenter(getActivity());
        newsPresenter.onTakeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        newsPresenter.onTakeView(null);
        if (getActivity() != null && !getActivity().isChangingConfigurations())
            newsPresenter = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NEWS_LIST, (ArrayList<Article>) articleList);
    }
}
