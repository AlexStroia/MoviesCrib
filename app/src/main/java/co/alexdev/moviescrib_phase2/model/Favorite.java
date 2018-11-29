package co.alexdev.moviescrib_phase2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/*Used to store favorites movies in a separate table*/
@Entity(tableName = "movie_favorites", indices = {@Index(value = "title", unique = true)})
public class Favorite {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String title;
    private final String synopsis;
    private final String image;
    private final float rating;

    public Favorite(String title, String synopsis, String image, float rating) {
        this.title = title;
        this.synopsis = synopsis;
        this.image = image;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
