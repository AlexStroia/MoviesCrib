package co.alexdev.moviescrib_phase2.utils;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.database.MovieDatabase;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieExecutor;
import co.alexdev.moviescrib_phase2.model.MoviesListener;

public class MovieUtils {

    /*Check if network is available*/
    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /*When adding a movie to the database, set type of movie to view it offline*/
    public static List<Movie> formatMoviesList(List<Movie> movieList, Enums.MovieType movieType) {
        for (Movie movie : movieList) {
            movie.setMovieType(movieType.toString());
        }
        return movieList;
    }

    /*Helper method to check if we have already a movie added to favorite
     * Iterate through favorites list
     * Iterate through movies list
     * check if we have an id match between favorites and movies
     * if yes, update current movie with the updated value
     * if we have a match it means that movie was added to favorite*/
    public static List<Movie> syncWithFavorites(final MovieDatabase mDb,final List<Movie> formatedList) {
        final LiveData<List<Favorite>> favoritesMovieList = mDb.movieDao().getFavoritesMovies();
        favoritesMovieList.observeForever(new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                favoritesMovieList.removeObserver(this);
                for (Favorite favorite : favorites) {
                    for (final Movie movie : formatedList)
                        if (favorite.getId() == movie.getId()) {
                            movie.setAddedToFavorite(true);
                            MovieExecutor.getInstance().getMovieIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDb.movieDao().updateMovie(movie);
                                }
                            });
                        }
                }
            }
        });
        return formatedList;
    }

    public static void showDialog(final Context context, Enums.DialogType dialogType, final MoviesListener.onNoFavoritesAdded mListener) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "";
        String title = "";

        switch (dialogType) {
            case NO_FAVORITES:
                message = context.getString(R.string.string_no_fav_message);
                title = context.getString(R.string.string_no_fav_title);
                builder.setPositiveButton(context.getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNoFavoritesMoviesAdded();
                    }
                });
                break;

            case NO_INTERNET:
                message = context.getString(R.string.string_error_msg);
                title = context.getString(R.string.string_error_title);
                builder.setPositiveButton(context.getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((Activity) context).finish();
                    }
                });
                break;

            case NO_OFFLINE_ENABLED:
                message = context.getString(R.string.string_no_offline_enabled);
                title = context.getString(R.string.string_error_title);
                builder.setPositiveButton(context.getString(R.string.string_ok), null);
                break;
        }
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error);

        dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
    }
}
