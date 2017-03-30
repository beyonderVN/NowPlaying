package ngohoanglong.com.nowplaying.data.response;

import java.util.List;

import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.request.BaseRequest;

/**
 * Created by Long on 3/24/2017.
 */

public class ResponseMovieBySection extends BaseResponse {
    public ResponseMovieBySection(ResponseStatus isSuccessfull) {
        super(isSuccessfull);
    }

    BaseRequest requestNowPlaying ;
    List<Movie> movies;

    public ResponseMovieBySection(ResponseStatus isSuccessfull, List<Movie> movies,BaseRequest requestNowPlaying) {
        super(isSuccessfull);
        this.movies = movies;
        this.requestNowPlaying = requestNowPlaying;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public BaseRequest getRequestNowPlaying() {
        return requestNowPlaying;
    }

    public void setRequestNowPlaying(BaseRequest requestNowPlaying) {
        this.requestNowPlaying = requestNowPlaying;
    }
}
