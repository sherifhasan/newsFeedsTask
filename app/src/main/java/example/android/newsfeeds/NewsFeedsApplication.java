package example.android.newsfeeds;

import android.app.Application;
import android.content.Context;


public class NewsFeedsApplication extends Application {
    static NewsFeedsApplication application;

    public static Context getAppContext() {
        return application.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
