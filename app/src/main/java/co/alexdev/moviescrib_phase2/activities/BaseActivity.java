package co.alexdev.moviescrib_phase2.activities;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.fragments.BaseFragment;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    /*Every position that exists in the navigation drawer*/
    private static final int MOST_POPULAR_POS = 0;
    private static final int TOP_RATED_POS = 1;
    private static final int FAVORITES_POS = 2;
    private static final int SETTINGS_POS = 3;

    private onViewPagerPositionChangedListener onViewPagerPositionChangedListener;

    public interface onViewPagerPositionChangedListener {
        void onViewPagerPositionChanged(final int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setActionBar();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
    }

    public void setViewPagerPositionListener(onViewPagerPositionChangedListener onViewPagerPositionChangedListener) {
        this.onViewPagerPositionChangedListener = onViewPagerPositionChangedListener;
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
        int menuItemPosition = 0;
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
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
                break;
        }

        onViewPagerPositionChangedListener.onViewPagerPositionChanged(menuItemPosition);
        return true;
    }
}

