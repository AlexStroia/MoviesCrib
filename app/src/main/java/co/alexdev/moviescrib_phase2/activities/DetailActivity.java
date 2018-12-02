
package co.alexdev.moviescrib_phase2.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.adapter.ReviewsMoviesAdapter;
import co.alexdev.moviescrib_phase2.database.MovieDatabase;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.ImageUtils;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;
import co.alexdev.moviescrib_phase2.viewmodel.DetailActivityViewModel;

public class DetailActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "DetailActivity";
    private Toolbar customToolbar;
    private ImageView iv_poster;
    private TextView tv_detail_title;
    private TextView tv_release_date;
    private TextView tv_plot_synopsis;
    private RatingBar rb_vote_average;
    private LinearLayout ll_add_to_favorites;
    private RecyclerView rv_reviews;
    private Button btn_favorites;
    private ImageView iv_heart;
    private String YOUTUBE_API_KEY = "";
    private String imageString;
    private boolean isAddedToFavorite = false;
    private boolean canStoreOfflineData = false;
    private SharedPreferences mSharedPreferences;
    private DetailActivityViewModel vm;

    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private ReviewsMoviesAdapter reviewsMoviesAdapter;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        vm = ViewModelProviders.of(this).get(DetailActivityViewModel.class);

        customToolbar = findViewById(R.id.toolbar);
        iv_poster = findViewById(R.id.iv_detail_poster);
        tv_detail_title = findViewById(R.id.tv_detail_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_plot_synopsis = findViewById(R.id.tv_plot_synopsis);
        tv_plot_synopsis.setMovementMethod(new ScrollingMovementMethod());
        rb_vote_average = findViewById(R.id.rb_vote_average);
        ll_add_to_favorites = findViewById(R.id.ll_add_to_favorites);
        rv_reviews = findViewById(R.id.rv_reviews);
        btn_favorites = findViewById(R.id.btn_add_to_fav);
        iv_heart = findViewById(R.id.iv_heart);
        reviewsMoviesAdapter = new ReviewsMoviesAdapter(new ArrayList<Reviews>());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        canStoreOfflineData = mSharedPreferences.getBoolean(getString(R.string.store_offline_key), false);

        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_PLAYER_API_KEY);

        setCustomToolbar();

        final Intent intent = getIntent();
        final String movieKey = getString(R.string.selected_movie_key);

        /*Check if there is a intent who has the SELECTED_MOVIE key
         * SELECTED_MOVIE is the key that we use to check if the intent has the movieId that we want*/
        if (intent.hasExtra(movieKey)) {
            movieId = intent.getIntExtra(getString(R.string.selected_movie_key), 0);
            final LiveData<Movie> liveMovie = vm.loadMovieById(movieId);

            liveMovie.observe(DetailActivity.this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    vm.movie = movie;
                    setReviewsMovieAdapter();
                    displayData(vm.movie);
                    setTrailerForCurrentMovie(vm.getTrailerPath());
                    vm.isAddedToFavorite = movie.isAddedToFavorite();
                    setFavoritesLayout(vm.isAddedToFavorite);
                }
            });

            btn_favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (vm.canStoreOfflineData) {
                        vm.onFavoriteButtonClick(iv_poster);
                        setFavoritesLayout(!vm.isAddedToFavorite);
                    } else {
                        MovieUtils.showDialog(DetailActivity.this, Enums.DialogType.NO_OFFLINE_ENABLED, null);
                    }
                }
            });
        }
    }

    private void setReviewsMovieAdapter() {
        vm.getReviewsForCurrentMovie().observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(@Nullable List<Reviews> reviewsList) {
                rv_reviews.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                rv_reviews.setAdapter(reviewsMoviesAdapter);
            }
        });
    }

    private void setTrailerForCurrentMovie(final LiveData<String> liveYoutubePath) {
        liveYoutubePath.observe(DetailActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                liveYoutubePath.removeObserver(this);
                initializeYoutubePlayerView(s);
            }
        });
    }

    /*When the device is rotated, play the video on full screen*/
    private void initializeYoutubePlayerView(final String id) {
        youTubePlayerSupportFragment = (YouTubePlayerSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerSupportFragment.setRetainInstance(true);

        if (youTubePlayerSupportFragment == null) {
            return;
        }
        youTubePlayerSupportFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRotated) {
                if (wasRotated) {
                    youTubePlayer.setFullscreen(true);
                }
                youTubePlayer.cueVideo(id);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    /*Set the custom toolbar with the navigation icon
     * Set the listener of the custom toolbar that when is clicked to finish this activity*/
    private void setCustomToolbar() {
        customToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        customToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*First check if we have a movieId, if we have populate the view with the specific detail*/
    private void displayData(Movie movie) {
        if (movie != null) {
            vm.loadImage(iv_poster);
            final float vote_average = (float) (movie.getVote_average() != 0 ? movie.getVote_average() : 0);
            final float ratingStars = vm.getRatingBarStars(vote_average);
            tv_detail_title.setText((movie.getTitle() != null && movie.getTitle().length() > 0) ? movie.getTitle() : "");
            tv_plot_synopsis.setText((movie.getOverview() != null && movie.getOverview().length() > 0) ? movie.getOverview() : "");
            tv_release_date.setText((movie.getRelease_date() != null && movie.getRelease_date().length() != 0) ? movie.getRelease_date() : "");
            rb_vote_average.setRating(ratingStars);
        }
    }

    /*Set the favorite button and icon bassed on the isAddedToFavorite
     * isAddedToFavorite = True, movieId is added to favorite
     * isAddedToFavorite = False, movieId is not added to favorite
     * */
    private void setFavoritesLayout(boolean isAddedToFavorite) {
        final String addToFav = getString(R.string.add_to_favorites);
        final String removeFromFav = getString(R.string.remove_from_favorites);
        btn_favorites.setText(isAddedToFavorite ? removeFromFav : addToFav);
        iv_heart.setVisibility(isAddedToFavorite ? View.GONE : View.VISIBLE);
    }

    private void registerSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void unregisterSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.store_offline_key))) {
            canStoreOfflineData = sharedPreferences.getBoolean(key, false);
        }
    }
}
