package example.android.newsfeeds.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public final static String API_KEY = "533af958594143758318137469b41ba9";
    public final static String SOURCE = "the-next-web";


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null;
    }

    public static String formatDate(Date date) {
        String dayOfTheWeek = (String) DateFormat.format("dd", date);
        String monthString = (String) DateFormat.format("MMM", date);
        String year = (String) DateFormat.format("yyyy", date);
        return monthString + " " + dayOfTheWeek + " , " + year;
    }

    public static Date toDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.ENGLISH);
        try {
            if (dateString != null)
                return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toStringDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.ENGLISH);
        return format.format(date);
    }
}