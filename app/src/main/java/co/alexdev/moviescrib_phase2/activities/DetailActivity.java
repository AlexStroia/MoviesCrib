package co.alexdev.moviescrib_phase2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.adapter.ReviewsMoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.MovieRequest;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.model.MoviesListener;

public class DetailActivity extends YouTubeBaseActivity implements MoviesListener.MovieListListener {

    private Toolbar customToolbar;
    private ImageView iv_poster;
    private TextView tv_detail_title;
    private TextView tv_release_date;
    private TextView tv_plot_synopsis;
    private RatingBar rb_vote_average;
    private LinearLayout ll_add_to_favorites;
    private YouTubePlayerView youTubePlayerView;
    private RecyclerView rv_reviews;
    private String YOUTUBE_API_KEY = "";

    private ReviewsMoviesAdapter reviewsMoviesAdapter;
    private Movie movie;
    private List<Trailer> trailerList = new ArrayList<>();
    private List<Reviews> reviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        customToolbar = findViewById(R.id.toolbar);
        iv_poster = findViewById(R.id.iv_detail_poster);
        tv_detail_title = findViewById(R.id.tv_detail_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_plot_synopsis = findViewById(R.id.tv_plot_synopsis);
        tv_plot_synopsis.setMovementMethod(new ScrollingMovementMethod());
        rb_vote_average = findViewById(R.id.rb_vote_average);
        ll_add_to_favorites = findViewById(R.id.ll_add_to_favorites);
        youTubePlayerView = findViewById(R.id.youtube_player);
        rv_reviews = findViewById(R.id.rv_reviews);

        YOUTUBE_API_KEY = getResources().getString(R.string.YOUTUBE_PLAYER_API_KEY);

        setCustomToolbar();
        setRatingBar();

        final Intent intent = getIntent();
        final String movieKey = getString(R.string.selected_movie_key);

        /*Check if there is a intent who has the SELECTED_MOVIE key
         * SELECTED_MOVIE is the key that we use to check if the intent has the movie that we want*/
        if (intent.hasExtra(movieKey)) {
            movie = intent.getParcelableExtra(movieKey);
            displayData();
            getTrailerForCurrentMovie();
            getReviewsForCurrentMovie();
            setTrailerMovieAdapter();
        }
    }

    private void setTrailerMovieAdapter() {
        reviewsMoviesAdapter = new ReviewsMoviesAdapter(reviewsList);
        rv_reviews.setLayoutManager(new LinearLayoutManager(this));
        rv_reviews.setAdapter(reviewsMoviesAdapter);
    }

    private void getTrailerForCurrentMovie() {
        if (movie != null) {
            MovieRequest.getMovieTrailer(this, movie.getId());
        }
    }

    private void getReviewsForCurrentMovie() {
        if (movie != null) {
            MovieRequest.getMovieReviews(this, movie.getId());
        }
    }

    /*When the device is rotated, play the video on full screen*/
    private void setYoutubeTrailer(final String id) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
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

    /*First check if we have a movie, if we have populate the view with the specific detail*/
    private void displayData() {
        if (movie != null) {
            loadImage();
            final float vote_average = (float) (movie.getVote_average() != 0 ? movie.getVote_average() : 0);
            final float ratingStars = getRatingBarStars(vote_average);
            tv_detail_title.setText((movie.getTitle() != null && movie.getTitle().length() > 0) ? movie.getTitle() : "");
            tv_plot_synopsis.setText((movie.getOverview() != null && movie.getOverview().length() > 0) ? movie.getOverview() : "");
            tv_release_date.setText((movie.getRelease_date() != null && movie.getRelease_date().length() != 0) ? movie.getRelease_date() : "");
            rb_vote_average.setRating(ratingStars);
        }
    }

    /*Load the image with Picasso into our image*/
    private void loadImage() {
        final String imageUri = buildImageUri();
        Picasso.get().load(imageUri)
                .into(iv_poster);
    }

    /*Helper function that help us to build the URI for our image
     * pathForLargeImage - the basic path that every image has
     * imageUri - the unique URI that every image has */
    private String buildImageUri() {
        final String pathForLargeImage = getString(R.string.tmdb_image_url_large);
        final String imageUri = new StringBuilder().append(pathForLargeImage).append(movie.getPoster_path()).toString();
        return imageUri;
    }

    /*Set rating bar to step size
     * 0.5 - We can view half of a star not only a full star
     * 5 - Maximum number of stars */
    private void setRatingBar() {
        rb_vote_average.setStepSize(0.5f);
        rb_vote_average.setMax(5);
    }

    /*Calculate the rating stars*/
    private float getRatingBarStars(final float vote_average) {
        return vote_average / 2;
    }

    @Override
    public void onMostPopularListReceivedListener(List<Movie> movieList) {

    }

    @Override
    public void onTopRatedListReceivedListener(List<Movie> movieList) {

    }

    @Override
    public void onTrailerListReceivedListener(List<Trailer> trailerList) {
        if (trailerList != null && trailerList.size() > 0) {
            this.trailerList = trailerList;
            final String YOUTUBE_PATH = this.trailerList.get(0).getKey();
            setYoutubeTrailer(YOUTUBE_PATH);
        }
    }

    @Override
    public void onReviewsListReceivedListener(List<Reviews> reviewsList) {
        if (reviewsList != null && reviewsList.size() > 0) {
            this.reviewsList = reviewsList;
            reviewsMoviesAdapter.setReviewsList(reviewsList);
        }
    }
}
