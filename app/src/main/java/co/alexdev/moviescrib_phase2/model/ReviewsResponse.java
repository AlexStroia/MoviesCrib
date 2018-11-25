package co.alexdev.moviescrib_phase2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*Response class for reviews*/
public class ReviewsResponse {

    @SerializedName("results")
    private List<Reviews> response;

    public List<Reviews> getResponse() {
        return response;
    }

    public void setResponse(List<Reviews> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ReviewsResponse{" +
                "response=" + response +
                '}';
    }
}
