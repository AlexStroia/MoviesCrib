package co.alexdev.moviescrib_phase2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {

    @SerializedName("results")
    List<Trailer> response;

    public List<Trailer> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "TrailerResponse{" +
                "response=" + response +
                '}';
    }
}
