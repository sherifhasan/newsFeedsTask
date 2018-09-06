package example.android.newsfeeds.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = ArticlesProvider.AUTHORITY, database = ArticlesContract.ArticlesDatabase.class)
public class ArticlesProvider {
    public static final String AUTHORITY = "example.android.capestone";

    @TableEndpoint(table = ArticlesContract.ArticlesDatabase.ARTICLES_TABLE)
    public static class Articles {

        @ContentUri(path = ArticlesContract.ArticlesDatabase.ARTICLES_TABLE,
                type = "vnd.android.cursor.dir/list")
        public static final Uri articleUri = Uri.parse("content://" + AUTHORITY)
                .buildUpon()
                .appendPath(ArticlesContract.ArticlesDatabase.ARTICLES_TABLE)
                .build();
    }
}
