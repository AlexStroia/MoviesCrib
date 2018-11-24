package co.alexdev.moviescrib_phase2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class Movie implements Parcelable {
    /*Used SerializedName so that Retrofit will know what type of data to extract if our params name are differnet*/
    @SerializedName("id")
    private final int id;
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

    /*Parcel in to get the converted stream into our data*/
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
        poster_path = in.readString();
        overview = in.readString();
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

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
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
    }
}
