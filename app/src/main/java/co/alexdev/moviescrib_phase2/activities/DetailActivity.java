package co.alexdev.moviescrib_phase2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.R;

public class DetailActivity extends AppCompatActivity {

    private Toolbar customToolbar;
    private ImageView iv_poster;
    private TextView tv_detail_title;
    private TextView tv_release_date;
    private TextView tv_plot_synopsis;
    private RatingBar rb_vote_average;
    private TextView tv_vote_average;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        customToolbar = findViewById(R.id.toolbar);
        iv_poster = findViewById(R.id.iv_detail_poster);
        tv_detail_title = findViewById(R.id.tv_detail_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_release_date.setMovementMethod(new ScrollingMovementMethod());
        tv_plot_synopsis = findViewById(R.id.tv_plot_synopsis);
        rb_vote_average = findViewById(R.id.rb_vote_average);
        tv_vote_average = findViewById(R.id.tv_vote_average);

        setCustomToolbar();
        setRatingBar();

        final Intent intent = getIntent();
        final String movieKey = getString(R.string.selected_movie_key);

        /*Check if there is a intent who has the SELECTED_MOVIE key
         * SELECTED_MOVIE is the key that we use to check if the intent has the movie that we want*/
        if (intent.hasExtra(movieKey)) {
            movie = intent.getParcelableExtra(movieKey);
            displayData();
        }
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
}
