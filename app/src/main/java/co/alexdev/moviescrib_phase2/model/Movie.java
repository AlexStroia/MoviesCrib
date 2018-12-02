package co.alexdev.moviescrib_phase2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.SerializedName;

import co.alexdev.moviescrib_phase2.utils.Enums;

@Entity(tableName = "movies")
public final class Movie extends BaseObservable implements Parcelable {
    /*Used SerializedName so that Retrofit will know what type of data to extract if our params name are differnet*/
    @SerializedName("id")
    @PrimaryKey()
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("overview")
    private String overview;
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
        movieType = mMovieType.toString();
    }

    @Ignore
    public Movie() {
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
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getId() {
        return id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public double getVote_average() {
        return vote_average;
    }

    @Bindable
    public String getRelease_date() {
        return release_date;
    }

    @Bindable
    public String getPoster_path() {
        return poster_path;
    }

    @Bindable
    public String getOverview() {
        return overview;
    }

    @Bindable
    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
        notifyPropertyChanged(BR.movieType);
    }

    public Enums.MovieType getmMovieType() {
        return mMovieType;
    }

    public void setAddedToFavorite(boolean addedToFavorite) {
        this.isAddedToFavorite = addedToFavorite;
    }

    @Bindable
    public boolean isAddedToFavorite() {
        return isAddedToFavorite;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
        notifyPropertyChanged(BR.vote_average);
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
        notifyPropertyChanged(BR.release_date);
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
        notifyPropertyChanged(BR.poster_path);
    }

    public void setOverview(String overview) {
        this.overview = overview;
        notifyPropertyChanged(BR.overview);
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
