package co.alexdev.moviescrib_phase2.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Reviews;

/*Response class for reviews*/
public class ReviewsResponse {

    @SerializedName("results")
    private List<Reviews> response;

    public List<Reviews> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ReviewsResponse{" +
                "response=" + response +
                '}';
    }
}
