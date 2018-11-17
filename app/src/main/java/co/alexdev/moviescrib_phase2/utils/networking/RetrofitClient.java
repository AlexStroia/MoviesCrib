package co.alexdev.moviescrib.utils.networking;

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

public class RetrofitClient {

    private static final String URL_SCHEME = "https";
    private static final String BASE_URL = "api.tmdb.org";
    public static final String PATH = "/3/";
    private static final String API_KEY = "ENTER YOUR API KEY HERE";
    private static final String API_KEY_IDENTIFIER = "api_key";

    private final Retrofit retrofit;
    private static RetrofitClient mInstance;

    private RetrofitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
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

    private Uri buildUri() {
        Uri.Builder builder = new Uri.Builder()
                .scheme(URL_SCHEME)
                .authority(BASE_URL)
                .path(PATH);
        return builder.build();
    }

    public MovieApi getMovieApi() {
        return retrofit.create(MovieApi.class);
    }
}
