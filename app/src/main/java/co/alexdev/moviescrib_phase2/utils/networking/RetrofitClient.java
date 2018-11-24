package co.alexdev.moviescrib_phase2.utils.networking;

import android.net.Uri;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*RetrofitClient that we use when we want to perform network requests*/
public class RetrofitClient {

    static final String URL_SCHEME = "https";
    static final String BASE_URL = "api.tmdb.org";
    static final String PATH = "/3/";
    static final String API_KEY = "";
    static final String API_KEY_IDENTIFIER = "api_key";

    final Retrofit retrofit;
    static RetrofitClient mInstance;

    /*Using an OkHttpClient and on top of it we are adding an interceptor used to intercept requests so we can add the API KEY*/
    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        /*Get the original requests
                         * From the requests we get the original URL
                         * After that we are creating a new url where using the original requests and adding a new query parameter
                         * After that we are adding it to the original requests
                         * After we are building the request
                         * And after we pass it to the request
                         * and pass it to the chain*/
                        Request originalRequest = chain.request();
                        HttpUrl originalHttpUrl = originalRequest.url();

                        HttpUrl newUrlWithApiKey = originalHttpUrl.newBuilder()
                                .addQueryParameter(API_KEY_IDENTIFIER, API_KEY)
                                .build();

                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .url(newUrlWithApiKey);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();


        retrofit = new Retrofit.Builder().baseUrl(getBaseEndpoint())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /*Singleton instance*/
    public static synchronized RetrofitClient shared() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    private String getBaseEndpoint() {
        Uri uri = buildUri();
        return uri.toString();
    }

    /*Build the Uri with the specific params*/
    private Uri buildUri() {
        Uri.Builder builder = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(BASE_URL)
                .path(PATH);
        return builder.build();
    }

    /*Create the Api Endpoints*/
    public MovieService getMovieApi() {
        return retrofit.create(MovieService.class);
    }
}
