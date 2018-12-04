package co.alexdev.moviescrib_phase2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieAppRepo;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.utils.ImageUtils;

public class DetailActivityViewModel extends AndroidViewModel {

    private static final String TAG = "DetailActivityViewModel";
    private final static String YOUTUBE_SCHEME = "https";
    private final static String YOUTUBE_AUTHORITY = "youtube.com";

    public boolean canStoreOfflineData = false;
    public boolean isAddedToFavorite = false;
    private String imageString;
    private Movie movie;
    private Resources resources;

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
        resources = this.getApplication().getResources();
    }

    public void onFavoriteButtonClick(ImageView imageView) {
        Favorite favorite;
        if (canStoreOfflineData) {
            if (!isAddedToFavorite) {
                imageString = ImageUtils.encode(imageView);
                favorite = new Favorite(movie.getId(), movie.getTitle(), movie.getOverview(), imageString, (float) movie.getVote_average());
                insertToFavorite(favorite);
            } else {
                deleteFromFavorites();
            }
        }
        movie.setAddedToFavorite(!isAddedToFavorite);
        updateMovie(movie);
    }

    public void onWatchTrailerClick() {
        final Application app = this.getApplication();
        final LiveData<String> path = getTrailerPath();
        getTrailerPath().observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                path.removeObserver(this);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(app.getString(R.string.youtube_url) + s));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                app.startActivity(intent);
            }
        });
    }

    /*Make a call to get the reviews from the repository and when the call is done, pass the values to movie reviews*/
    public LiveData<List<Reviews>> getReviewsForCurrentMovie() {
        final MutableLiveData<List<Reviews>> movieReviews = new MutableLiveData<>();
        if (movie != null) {
            final LiveData<List<Reviews>> reviewsForCurrentMovie = MovieAppRepo
                    .getInstance(this.getApplication())
                    .getMovieReviewsCall(movie.getId());
            reviewsForCurrentMovie.observeForever(new Observer<List<Reviews>>() {
                @Override
                public void onChanged(@Nullable List<Reviews> reviewsList) {
                    reviewsForCurrentMovie.removeObserver(this);
                    movieReviews.setValue(reviewsList);
                }
            });
        }
        return movieReviews;
    }

    /*Check the observer for the trailer, and when a list of trailer is received, get the key for the first one and return it */
    public LiveData<String> getTrailerPath() {
        final MutableLiveData<String> youtube_path = new MutableLiveData<>();
        if (movie != null) {
            final LiveData<List<Trailer>> liveTrailerList = MovieAppRepo
                    .getInstance(this.getApplication())
                    .getMovieTrailerCall(movie.getId());
            liveTrailerList.observeForever(new Observer<List<Trailer>>() {
                @Override
                public void onChanged(@Nullable List<Trailer> trailers) {
                    liveTrailerList.removeObserver(this);
                    youtube_path.setValue(trailers.get(0).getKey());
                }
            });
        }
        return youtube_path;
    }

    /*Load movie by the given id*/
    public LiveData<Movie> loadMovieById(int id) {
        return MovieAppRepo.getInstance(this.getApplication()).loadMovieById(id);
    }

    /*Insert the movie to favorite*/
    private void insertToFavorite(Favorite favorite) {
        if (favorite != null) {
            MovieAppRepo.getInstance(this.getApplication()).insertToFavorite(favorite);
        }
    }

    /*Delete movie from favorites*/
    private void deleteFromFavorites(Favorite favorite) {
        if (favorite != null) {
            MovieAppRepo.getInstance(this.getApplication()).deleteFromFavorite(favorite);
        }
    }

    public void updateMovie(Movie movie) {
        if (movie != null) {
            MovieAppRepo.getInstance(this.getApplication()).updateMovie(movie);
        }
    }

    /*Calculate the rating stars*/
    public float getRatingBarStars(final float vote_average) {
        if (vote_average != 0) {
            return vote_average / 2;
        }
        return 0;
    }

    private void deleteFromFavorites() {
        final LiveData<Favorite> favoriteLiveData = MovieAppRepo.getInstance(this.getApplication()).loadMovieFromFavoritesById(movie.getId());
        favoriteLiveData.observeForever(new Observer<Favorite>() {
            @Override
            public void onChanged(@Nullable Favorite favorite) {
                favoriteLiveData.removeObserver(this);
                deleteFromFavorites(favorite);
            }
        });
    }

    /*Helper function that help us to build the URI for our image
     * pathForLargeImage - the basic path that every image has
     * imageUri - the unique URI that every image has */
    private String buildImageUri() {
        String imageUri = "";
        if (movie != null) {
            final String pathForLargeImage = resources.getString(R.string.tmdb_image_url_large);
            imageUri = new StringBuilder().append(pathForLargeImage).append(movie.getPoster_path()).toString();
        }
        return imageUri;
    }

    /*Load the image with Picasso into our image*/
    public void loadImage(ImageView imageView) {
        if (movie != null) {
            final String imageUri = buildImageUri();
            Picasso.get().load(imageUri)
                    .into(imageView);
        }
    }

    public void setMovie(Movie movie) {
        this.movie = new Movie();
        this.movie.setId(movie.getId());
        this.movie.setTitle(movie.getTitle());
        this.movie.setOverview(movie.getOverview());
        this.movie.setMovieType(movie.getMovieType());
        this.movie.setVote_average(movie.getVote_average());
        this.movie.setRelease_date(movie.getRelease_date());
        this.movie.setPoster_path(movie.getPoster_path());
        isAddedToFavorite = movie.isAddedToFavorite();
    }

    public Movie getMovie() {
        return movie = movie != null ? movie : new Movie();
    }
}
