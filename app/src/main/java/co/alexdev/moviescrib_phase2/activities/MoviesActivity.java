package co.alexdev.moviescrib_phase2.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.adapter.MoviesAdapter;
import co.alexdev.moviescrib_phase2.adapter.PagerAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;

/*Main activity where we can see a list with all the movies*/
public class MoviesActivity extends AppCompatActivity {
    private static final String TAG = "MoviesActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<Movie> mMoviesList = new ArrayList<>();

    private Toast mToast;
    private MoviesAdapter mMoviesAdapter;
    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        initView();
    }

    private void initView() {
        setupViewPager();
        setupTabLayout();
    }

    private void setupViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
