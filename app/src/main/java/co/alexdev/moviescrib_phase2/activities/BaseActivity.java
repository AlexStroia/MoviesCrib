package co.alexdev.moviescrib_phase2.activities;


import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.MoviesListener;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MoviesListener.onViewPagerPositionChangedListener {

    private static final String TAG = "BaseActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    /*Every position that exists in the navigation drawer*/
    private static final int MOST_POPULAR_POS = 0;
    private static final int TOP_RATED_POS = 1;
    private static final int FAVORITES_POS = 2;
    private static final int SETTINGS_POS = 3;
    private int menuItemPosition = 0;

    private MoviesListener.onNavigationViewPositionChangedListener mOnNavigationViewPositionChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Used when user navigates back from Settings Activity*/
        if(menuItemPosition == SETTINGS_POS) {
            navigationView.getMenu().getItem(SETTINGS_POS).setChecked(false);
        }
    }

    /*Set the action bar to the toolbar custom
     * enable the home button
     * set the home button with the burger icon*/
    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(MOST_POPULAR_POS).setChecked(true);
        navigationView.bringToFront();
    }

    public void setViewPagerPositionListener(MoviesListener.onNavigationViewPositionChangedListener onNavigationViewPositionChangedListener) {
        this.mOnNavigationViewPositionChangedListener = onNavigationViewPositionChangedListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {

            case R.id.nav_most_popular:
                Log.d(TAG, "onOptionsItemSelected: nav_most_popular");
                menuItemPosition = MOST_POPULAR_POS;
                break;

            case R.id.nav_top_rated:
                Log.d(TAG, "onOptionsItemSelected: nav_top_rated");
                menuItemPosition = TOP_RATED_POS;
                break;

            case R.id.nav_favourites:
                Log.d(TAG, "onOptionsItemSelected: nav_favorites");
                menuItemPosition = FAVORITES_POS;
                break;

            case R.id.nav_settings:
                Log.d(TAG, "onOptionsItemSelected: nav_settings");
                menuItemPosition = SETTINGS_POS;
                startSettingsActivity();
                break;
        }
        if (menuItemPosition != SETTINGS_POS) {
            mOnNavigationViewPositionChangedListener.onNavigationViewPositionChanged(menuItemPosition);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /*Triggered when the view pager position changes
     * This way it will sync with our menu*/
    @Override
    public void onViewPagerPositionChanged(int position) {
        Log.d(TAG, "onViewPagerPositionChanged: " + position);
        int currentCheckedItem = navigationView.getMenu().getItem(position).getItemId();
        if (currentCheckedItem != position) {
            navigationView.getMenu().getItem(position).setChecked(true);
        }
    }
}

