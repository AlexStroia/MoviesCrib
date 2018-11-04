package co.alexdev.moviescrib.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import co.alexdev.moviescrib.Model.Movie;
import co.alexdev.moviescrib.R;

public class DetailActivity extends AppCompatActivity {

    private Toolbar customToolbar;
    private ImageView iv_poster;
    private TextView tv_detail_title;
    private TextView tv_release_date;
    private TextView tv_plot_synopsis;
    private RatingBar rb_vote_average;
    private TextView tv_vote_average;
    private ProgressBar pb_movie_loading;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        customToolbar = findViewById(R.id.toolbar);
        iv_poster = findViewById(R.id.iv_detail_poster);
        tv_detail_title = findViewById(R.id.tv_detail_title);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_plot_synopsis = findViewById(R.id.tv_plot_synopsis);
        rb_vote_average = findViewById(R.id.rb_vote_average);
        tv_vote_average = findViewById(R.id.tv_vote_average);
        pb_movie_loading = findViewById(R.id.pb_movie_loading);

        setCustomToolbar();
        setRatingBar();

        final Intent intent = getIntent();
        final String movieKey = getString(R.string.selected_movie_key);

        if (intent.hasExtra(movieKey)) {
            movie = intent.getParcelableExtra(movieKey);
            displayData();
        }
    }

    private void setCustomToolbar() {
        customToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        customToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

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

    private void loadImage() {
        final String imageUri = buildImageUri();
        Picasso.get().load(imageUri)
                .into(iv_poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        pb_movie_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    private String buildImageUri() {
        final String pathForLargeImage = getString(R.string.tmdb_image_url_large);
        final String imageUri = new StringBuilder().append(pathForLargeImage).append(movie.getPoster_path()).toString();
        return imageUri;
    }

    private void setRatingBar() {
        rb_vote_average.setStepSize(0.01f);
        rb_vote_average.setMax(5);
    }

    private float getRatingBarStars(final float vote_average) {
        return vote_average / 2;
    }
}
