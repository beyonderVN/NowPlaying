package ngohoanglong.com.nowplaying.display;

import android.content.res.Resources;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.remote.MovieBoxService;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.BaseViewModel;
import rx.Observable;

/**
 * Created by Long on 3/15/2017.
 */

public class MovieDetailViewModel extends BaseViewModel {

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
}
