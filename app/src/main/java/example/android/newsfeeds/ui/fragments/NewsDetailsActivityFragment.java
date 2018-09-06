package example.android.newsfeeds.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.android.newsfeeds.R;
import example.android.newsfeeds.models.Article;

import static example.android.newsfeeds.utility.Utility.formatDate;


public class NewsDetailsActivityFragment extends Fragment {
    private static final String EXTRA_ARTICLE = "article";
    private static final String SAVED_ARTICLE = "saved";
    Article article;
    @BindView(R.id.details_title)
    TextView title;
    @BindView(R.id.details_author)
    TextView author;
    @BindView(R.id.details_description)
    TextView description;
    @BindView(R.id.details_published_at)
    TextView publishedAt;
    @BindView(R.id.details_image)
    ImageView newsLogo;

    public NewsDetailsActivityFragment() {
    }

    public static NewsDetailsActivityFragment newInstance(Article article) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ARTICLE, article);
        NewsDetailsActivityFragment fragment = new NewsDetailsActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_details, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState == null) {
            if (getArguments() != null && getArguments().containsKey(EXTRA_ARTICLE) && getArguments().getParcelable(EXTRA_ARTICLE) != null) {
                article = getArguments().getParcelable(EXTRA_ARTICLE);
                assert article != null;
                initViews(article);
            }

        } else {
            article = savedInstanceState.getParcelable(SAVED_ARTICLE);
            assert article != null;
            initViews(article);
        }
        return rootView;
    }

    private void initViews(final Article article) {
        if (article.getAuthor() != null && !TextUtils.isEmpty(article.getAuthor())) {
            author.setText(article.getAuthor());
        }
        if (article.getTitle() != null && !TextUtils.isEmpty(article.getTitle())) {
            title.setText(article.getTitle());
        }
        if (article.getDescription() != null && !TextUtils.isEmpty(article.getDescription())) {
            description.setText(article.getDescription());
        }
        if (article.getPublishedAt() != null && !TextUtils.isEmpty(article.getPublishedAt().toString())) {
            publishedAt.setText(formatDate(article.getPublishedAt()));
        }
        if (article.getUrlToImage() != null && !TextUtils.isEmpty(article.getUrlToImage())) {
            Picasso.get().load(article.getUrlToImage()).into(newsLogo);
        }
    }

    @OnClick(R.id.open_website)
    void openUrl() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_ARTICLE, article);
    }
}