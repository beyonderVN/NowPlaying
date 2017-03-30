package ngohoanglong.com.nowplaying.display.v2;

import android.content.res.Resources;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.delegate.BaseState;
import ngohoanglong.com.nowplaying.util.delegate.BaseStateViewModel;
import rx.Observable;

/**
 * Created by Long on 3/15/2017.
 */

public class MovieDetailViewModel extends BaseStateViewModel {

    MovieBoxService service;

    @Inject
    public MovieDetailViewModel(ThreadScheduler threadScheduler,
                                Resources resources,
                                MovieBoxService service
    ) {
        super(threadScheduler, resources);
        this.service = service;
    }

    @Override
    public void bindViewModel() {

    }

    public Observable<String> getYouTubeUrl(int id) {
        return service.getTrailer(id)
                .compose(withScheduler());

    }

    static class MovieDetailState extends BaseState{
        boolean isVisibale = false;
        Movie movie;
        String urlTrailer = "";
        public MovieDetailState() {
        }

        public MovieDetailState(Movie movie) {
            this.movie = movie;
        }

        public boolean isVisibale() {
            return isVisibale;
        }

        public void setVisibale(boolean visibale) {
            isVisibale = visibale;
        }

        public Movie getMovie() {
            return movie;
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        public String getUrlTrailer() {
            return urlTrailer;
        }

        public void setUrlTrailer(String urlTrailer) {
            this.urlTrailer = urlTrailer;
        }
    }

}
