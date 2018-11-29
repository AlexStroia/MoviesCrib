package co.alexdev.moviescrib_phase2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import co.alexdev.moviescrib_phase2.model.Movie;

public class ImageUtils {

    /*1. Get teh bitmap *
    2. Create a bytearrayoutput stream
    3. Compress the image into a PNG, with the quality of 100 and pass the byte array
    4. convert the outputstream to a byteArray
    return a base64 encoded string/
     */
    public static String encode(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, 0);
    }

    /*Decode the byte array and convert it into a bitmap using BitmapFactory*/
    public static Bitmap decode(String imageByteArray) {
        byte[] byteArray = Base64.decode(imageByteArray, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    //TODO this will be added to the view model later
    /*When adding a movie to the database, set type of movie to view it offline*/
    public static List<Movie> formatMoviesList(List<Movie> movieList, Enums.MovieType movieType) {
        for (Movie movie : movieList) {
            movie.setMovieType(movieType.toString());
        }
        return movieList;
    }
}
