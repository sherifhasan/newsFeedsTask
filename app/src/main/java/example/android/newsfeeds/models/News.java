package example.android.newsfeeds.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import paperparcel.PaperParcel;


@PaperParcel
public class News implements Parcelable {

    public static final Creator<News> CREATOR = PaperParcelNews.CREATOR;
    @SerializedName("status")
    String status;

    @SerializedName("articles")
    List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        PaperParcelNews.writeToParcel(this, parcel, i);
    }
}
