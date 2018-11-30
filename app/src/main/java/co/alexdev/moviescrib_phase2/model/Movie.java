package co.alexdev.moviescrib_phase2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import co.alexdev.moviescrib_phase2.utils.Enums;

@Entity(tableName = "movies")
public final class Movie implements Parcelable {
    /*Used SerializedName so that Retrofit will know what type of data to extract if our params name are differnet*/
    @SerializedName("id")
    @PrimaryKey()
    private int id;
    @SerializedName("title")
    private final String title;
    @SerializedName("vote_average")
    private final double vote_average;
    @SerializedName("release_date")
    private final String release_date;
    @SerializedName("poster_path")
    private final String poster_path;
    @SerializedName("overview")
    private final String overview;
    /*Initialy the movies are not added to favorites*/
    private boolean isAddedToFavorite;
    @Ignore
    private Enums.MovieType mMovieType = Enums.MovieType.NOT_SET;
    @SerializedName("movie_type")
    private String movieType;

    public Movie(int id, String title, double vote_average, String release_date, String poster_path, String overview) {
        this.id = id;
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.overview = overview;
        /*This will be formated when we make the request*/
        movieType = mMovieType.toString();
    }


    /*Parcel in to get the converted stream into our data*/
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        movieType = in.readString();
        isAddedToFavorite = (in.readByte() == 0);
    }

    /*Creator of the parcel that let us to send data between activity
     * When we send na object between activity we convert it to a stream of data and after we deserialize it*/
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    public Enums.MovieType getmMovieType() {
        return mMovieType;
    }

    public void setAddedToFavorite(boolean addedToFavorite) {
        this.isAddedToFavorite = addedToFavorite;
    }

    public boolean isAddedToFavorite() {
        return isAddedToFavorite;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                ", isAddedToFavorite=" + isAddedToFavorite +
                ", mMovieType=" + mMovieType +
                ", movieType='" + movieType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeDouble(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(movieType);
        parcel.writeByte((byte) (isAddedToFavorite ? 1 : 0));
    }
}
