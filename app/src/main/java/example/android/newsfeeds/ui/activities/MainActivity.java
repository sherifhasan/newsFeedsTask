package example.android.newsfeeds.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.newsfeeds.R;
import example.android.newsfeeds.models.Article;
import example.android.newsfeeds.ui.fragments.NewsDetailsActivityFragment;
import example.android.newsfeeds.utility.PanesHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PanesHandler {
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    Boolean twoPanes;
    @Nullable
    @BindView(R.id.details_container)
    FrameLayout detailsFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        twoPanes = detailsFrame != null;
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            Toast.makeText(this, getString(R.string.explore), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_liveChat) {
            Toast.makeText(this, getString(R.string.live_chat), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, getString(R.string.gallery), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_whishlist) {
            Toast.makeText(this, getString(R.string.wish_list), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_e_magazine) {
            Toast.makeText(this, getString(R.string.e_magazine), Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setSelectedPane(Article article, int pos) {
        if (twoPanes) {
            NewsDetailsActivityFragment fragment = NewsDetailsActivityFragment.newInstance(article);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragment, fragment.getClass().getSimpleName())
                    .commit();
        } else {
            NewsDetailsActivity.startActivity(getApplicationContext(), article);
        }
    }
}

