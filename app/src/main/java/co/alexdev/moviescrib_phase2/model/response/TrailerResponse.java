package co.alexdev.moviescrib_phase2.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Trailer;

public class TrailerResponse {

    @SerializedName("results")
    private List<Trailer> response;

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
