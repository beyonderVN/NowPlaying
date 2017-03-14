package ngohoanglong.com.nowplaying.util.recyclerview.holdermodel;


import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.util.recyclerview.holderfactory.ViewTypeFactory;

/**
 * Created by Long on 10/5/2016.
 */

public class MovieHM extends BaseHM{

    private Movie movie;

    public MovieHM(Movie movie) {
        this.movie = movie;
    }
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public int getVMType(ViewTypeFactory vmTypeFactory) {
        return vmTypeFactory.getType(this);
    }

    @Override
    public String toString() {
        return movie.toString();
    }
}
