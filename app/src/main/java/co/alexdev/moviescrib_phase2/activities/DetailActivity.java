
package co.alexdev.moviescrib_phase2.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.adapter.ReviewsMoviesAdapter;
import co.alexdev.moviescrib_phase2.adapter.TrailerAdapter;
import co.alexdev.moviescrib_phase2.databinding.ActivityDetailBinding;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;
import co.alexdev.moviescrib_phase2.viewmodel.DetailActivityViewModel;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {
    private static final String TAG = "DetailActivity";
    private static final String SCROLL_POSITION = "SCROLL_POSITION";
    private Toolbar customToolbar;
    private String YOUTUBE_API_KEY = "";
    private SharedPreferences mSharedPreferences;
    private DetailActivityViewModel vm;
    private YouTubePlayerSupportFragment youTubePlayerSupportFragment;
    private ReviewsMoviesAdapter reviewsMoviesAdapter;
    private List<Reviews> reviewsList = new ArrayList<>();
    private List<Trailer> trailerList = new ArrayList<>();
    private TrailerAdapter trailerAdapter;
    private int movieId;
    private int[] scrollPositions = new int[2];

    ActivityDetailBinding activityDetailBinding;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        scrollPositions[0] = activityDetailBinding.scrollView.getScrollX();
        scrollPositions[1] = activityDetailBinding.scrollView.getScrollY();

        outState.putIntArray(SCROLL_POSITION, scrollPositions);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        scrollPositions = savedInstanceState.getIntArray(SCROLL_POSITION);

        if(scrollPositions != null) {
            activityDetailBinding.scrollView.scrollTo(scrollPositions[0], scrollPositions[1]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_PLAYER_API_KEY);
        setCustomToolbar();

        final Intent intent = getIntent();
        final String movieKey = getString(R.string.selected_movie_key);

        /*Check if there is a intent who has the SELECTED_MOVIE key
         * SELECTED_MOVIE is the key that we use to check if the intent has the movieId that we want*/
        if (intent.hasExtra(movieKey)) {
            movieId = intent.getIntExtra(getString(R.string.selected_movie_key), 0);
            final LiveData<Movie> movie = vm.loadMovieById(movieId);

            movie.observe(DetailActivity.this, new Observer<Movie>() {
                @Override
                public void onChanged(@Nullable Movie movie) {
                    vm.setMovie(movie);
                    activityDetailBinding.invalidateAll();
                    populateView();
                }
            });

            activityDetailBinding.btnAddToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!vm.canStoreOfflineData) {
                        MovieUtils.showDialog(DetailActivity.this, Enums.DialogType.NO_OFFLINE_ENABLED, null);
                        return;
                    }
                    vm.onFavoriteButtonClick(activityDetailBinding.ivDetailPoster);
                    setFavoritesLayout(!vm.isAddedToFavorite);
                }
            });
        }
    }

    private void initView() {
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        activityDetailBinding.setLifecycleOwner(this);
        vm = ViewModelProviders.of(this).get(DetailActivityViewModel.class);
        activityDetailBinding.setVm(vm);
        initRecyclerView();
        customToolbar = findViewById(R.id.toolbar);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vm.canStoreOfflineData = mSharedPreferences.getBoolean(getString(R.string.store_offline_key), false);
    }

    private void initRecyclerView() {
        reviewsMoviesAdapter = new ReviewsMoviesAdapter(reviewsList);
        activityDetailBinding.rvReviews.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        activityDetailBinding.rvReviews.setAdapter(reviewsMoviesAdapter);
    }

    private void populateView() {
        setReviewsMovieAdapter();
        setTrailersRecycler();
        setTrailerForCurrentMovie(vm.getTrailerPath());
        setFavoritesLayout(vm.isAddedToFavorite);
        vm.loadImage(activityDetailBinding.ivDetailPoster);
    }

    private void setTrailersRecycler() {
        trailerAdapter = new TrailerAdapter(this, trailerList, this);
        activityDetailBinding.rvMoreTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityDetailBinding.rvMoreTrailers.setAdapter(trailerAdapter);
    }

    private void setReviewsMovieAdapter() {

        vm.getReviewsForCurrentMovie().observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(@Nullable List<Reviews> reviewsList) {
                reviewsMoviesAdapter.setReviewsList(reviewsList);
                activityDetailBinding.rvReviews.setAdapter(reviewsMoviesAdapter);

            }
        });
    }

    private void setTrailerForCurrentMovie(final LiveData<List<Trailer>> liveYoutubePath) {
        liveYoutubePath.observeForever(new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                if (trailers != null && !trailers.isEmpty()) {
                    final String path = trailers.get(0).getKey();
                    trailerList = trailers;
                    trailerAdapter.setTrailerList(trailerList);
                    initializeYoutubePlayerView(path);
                }
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
                youTubePlayer.cueVideo(id);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
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

    /*Set the favorite button and icon bassed on the isAddedToFavorite
     * isAddedToFavorite = True, movieId is added to favorite
     * isAddedToFavorite = False, movieId is not added to favorite
     * */
    private void setFavoritesLayout(boolean isAddedToFavorite) {
        final String addToFav = getString(R.string.add_to_favorites);
        final String removeFromFav = getString(R.string.remove_from_favorites);
        activityDetailBinding.btnAddToFav.setText(isAddedToFavorite ? removeFromFav : addToFav);
    }

    @Override
    public void onTrailerClickListener(int position) {
        String path = trailerList.get(position).getKey();
        vm.onWatchTrailerClick(path);
    }
}