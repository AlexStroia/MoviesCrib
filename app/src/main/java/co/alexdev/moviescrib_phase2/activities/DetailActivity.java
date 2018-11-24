package co.alexdev.moviescrib_phase2.activities;

import android.content.Intent;
import android.os.Bundle;
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

import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.MovieRequest;
import co.alexdev.moviescrib_phase2.model.Trailer;

public class DetailActivity extends YouTubeBaseActivity implements MovieRequest.MovieListListener {

    Toolbar customToolbar;
    ImageView iv_poster;
    TextView tv_detail_title;
    TextView tv_release_date;
    TextView tv_plot_synopsis;
    RatingBar rb_vote_average;
    TextView tv_vote_average;
    LinearLayout ll_add_to_favorites;
    YouTubePlayerView youTubePlayerView;
    String YOUTUBE_API_KEY = "";

    private Movie movie;
    private List<Trailer> trailerList = new ArrayList<>();

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
        tv_vote_average = findViewById(R.id.tv_vote_average);
        ll_add_to_favorites = findViewById(R.id.ll_add_to_favorites);
        youTubePlayerView = findViewById(R.id.youtube_player);

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
            setTrailerMovieAdapter();
        }
    }

    private void setTrailerMovieAdapter() {
        //trailerMoviesAdapter = new TrailerMoviesAdapter(this, trailerList, this);
        //  rv_trailers.setLayoutManager(new LinearLayoutManager(this));
        //    rv_trailers.setAdapter(trailerMoviesAdapter);
    }

    private void getTrailerForCurrentMovie() {
        if (movie != null) {
            MovieRequest.getVideoTrailers(this, movie.getId());
        }
    }

    private void setYoutubeTrailer(final String id) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
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
            tv_vote_average.setText(String.valueOf(vote_average));
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
}
