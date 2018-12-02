package co.alexdev.moviescrib_phase2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.activities.DetailActivity;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieApplicationRepository;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.ImageUtils;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;

public class DetailActivityViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "DetailActivityViewModel";
    public boolean canStoreOfflineData = false;
    private Resources resources;
    private SharedPreferences sharedPreferences;
    public boolean isAddedToFavorite = false;
    private String imageString;

    public Movie movie;

    public DetailActivityViewModel(@NonNull Application application) {

        super(application);

        initVM();
    }

    private void initVM() {
        resources = this.getApplication().getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        canStoreOfflineData = sharedPreferences.getBoolean(resources.getString(R.string.store_offline_key), false);
    }

    public void onFavoriteButtonClick(ImageView imageView) {
        Favorite favorite;
        if (!canStoreOfflineData) {
            MovieUtils.showDialog(this.getApplication(), Enums.DialogType.NO_OFFLINE_ENABLED, null);
            return;
        }
        if (canStoreOfflineData) {
            if (!isAddedToFavorite) {
                imageString = ImageUtils.encode(imageView);
                favorite = new Favorite(movie.getId(), movie.getTitle(), movie.getOverview(), imageString, (float) movie.getVote_average());
                insertToFavorite(favorite);
            } else {
                favorite = MovieApplicationRepository.getInstance(this.getApplication()).loadMovieFromFavoritesById(movie.getId());
                if (favorite != null) {
                    deleteFromFavorites(favorite);
                }
            }
            movie.setAddedToFavorite(!isAddedToFavorite);
            updateMovie(movie);
        }
    }

    /*Make a call to get the reviews from the repository and when the call is done, pass the values to movie reviews*/
    public LiveData<List<Reviews>> getReviewsForCurrentMovie() {
        final MutableLiveData<List<Reviews>> movieReviews = new MutableLiveData<>();
        if (movie != null) {
            final LiveData<List<Reviews>> reviewsForCurrentMovie = MovieApplicationRepository
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
            final LiveData<List<Trailer>> liveTrailerList = MovieApplicationRepository.getInstance(this.getApplication()).getMovieTrailerCall(movie.getId());
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

    public LiveData<Movie> loadMovieById(int id) {
        return MovieApplicationRepository.getInstance(this.getApplication()).loadMovieById(id);
    }

    public void insertToFavorite(Favorite favorite) {
        if (favorite != null) {
            MovieApplicationRepository.getInstance(this.getApplication()).insertToFavorite(favorite);
        }
    }

    public void deleteFromFavorites(Favorite favorite) {
        if (favorite != null) {
            MovieApplicationRepository.getInstance(this.getApplication()).deleteFromFavorite(favorite);
        }
    }

    private void updateMovie(Movie movie) {
        if (movie != null) {
            MovieApplicationRepository.getInstance(this.getApplication()).updateMovie(movie);
        }
    }

    /*Calculate the rating stars*/
    public float getRatingBarStars(final float vote_average) {
        return vote_average / 2;
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(resources.getString(R.string.store_offline_key))) {
            canStoreOfflineData = sharedPreferences.getBoolean(resources.getString(R.string.store_offline_key), false);
        }
    }
}
