package example.android.newsfeeds.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.newsfeeds.R;
import example.android.newsfeeds.models.Article;

import static example.android.newsfeeds.utility.Utility.formatDate;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Listener listener;
    private Context context;
    private List<Article> newsList;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void updateAdapter(List<Article> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public void setListener(Listener mListener) {
        this.listener = mListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_raw_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.setContent(newsList != null ? newsList.get(position) : null);
        final Article article = newsList.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(article);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return newsList == null ? 0 : newsList.size();
    }

    public interface Listener {
        void onClick(Article article);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_title)
        TextView titleText = null;
        @BindView(R.id.news_card)
        CardView cardView = null;
        @BindView(R.id.news_published_at)
        TextView articleDate = null;
        @BindView(R.id.news_image)
        ImageView imageView = null;
        @BindView(R.id.article_author)
        TextView articleAuthor = null;
        private Article newsLetter;

        MyViewHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this, convertView);
        }

        void setContent(Article news) {
            newsLetter = news;
            if (newsLetter.getTitle() != null && !TextUtils.isEmpty(newsLetter.getTitle())) {
                titleText.setText(newsLetter.getTitle());
            }
            if (newsLetter.getAuthor() != null && !TextUtils.isEmpty(newsLetter.getAuthor())) {
                String author = "By " + newsLetter.getAuthor();
                articleAuthor.setText(author);
            }
            if (newsLetter.getPublishedAt() != null && !TextUtils.isEmpty(newsLetter.getPublishedAt().toString()))

                articleDate.setText(formatDate(newsLetter.getPublishedAt()));
            if (newsLetter.getUrlToImage() != null && !TextUtils.isEmpty(newsLetter.getUrlToImage())) {
                Picasso.get().load(newsLetter.getUrlToImage()).placeholder(R.drawable.place_holder).into(imageView);
            }
        }
    }
}