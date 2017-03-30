package ngohoanglong.com.nowplaying.display.v2;

import android.content.res.Resources;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseMovieBySection;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.PostViewModel;
import rx.subjects.PublishSubject;

/**
 * Created by Long on 3/14/2017.
 */

public class MovieListViewModel extends PostViewModel {
    private static final String TAG = "MainViewModel";
    protected PublishSubject<Boolean> refresh = PublishSubject.create();
    MovieBoxService service;

    MainViewModel.Section section;
    @Inject
    public MovieListViewModel(ThreadScheduler threadScheduler,
                              Resources resources,
                              MovieBoxService service
    ) {
        super(threadScheduler, resources);
        this.service = service;
    }

    public boolean isNeedLoadFirst() {
        Log.d(TAG, "isNeedLoadFirst: " + posts.size());
        return posts == null || posts.isEmpty();
    }

    @RxLogObservable
    public void loadFirst() {
        if (isNeedLoadFirst()) {
            service.sendRequest(section.baseRequest)
                    .takeUntil(refresh)
                    .compose(withScheduler())
                    .doOnSubscribe(() -> isLoadingMore.onNext(true))
                    .doOnTerminate(() -> isLoadingMore.onNext(false))
                    .subscribe(baseResponse -> {
                        isLoadingMore.onNext(false);
                        ResponseMovieBySection responseMovieBySection = (ResponseMovieBySection) baseResponse;
                        if(responseMovieBySection.isSuccessfull() == BaseResponse.ResponseStatus.ISSUCCESSFULL){
                            if (responseMovieBySection.getMovies().size() > 0) {
                                updatePosts(new Mapper().tranToVM(responseMovieBySection.getMovies()));
                            } else {
                                Log.d(TAG, "loadFirst: return zero");
                            }
                            section.baseRequest = responseMovieBySection.getRequestNowPlaying();
                        }

                    }, Throwable::printStackTrace);
        }

    }

    @RxLogObservable
    public void loadMore() {
        service.sendRequest(section.baseRequest)
                .takeUntil(refresh)
                .compose(withScheduler())
                .doOnSubscribe(() -> isLoadingMore.onNext(true))
                .doOnTerminate(() -> isLoadingMore.onNext(false))
                .subscribe(baseResponse -> {
                    isLoadingMore.onNext(false);
                    ResponseMovieBySection responseMovieBySection = (ResponseMovieBySection) baseResponse;
                    if(responseMovieBySection.isSuccessfull() == BaseResponse.ResponseStatus.ISSUCCESSFULL){
                        if (responseMovieBySection.getMovies().size() > 0) {
                            posts.addAll(new Mapper().tranToVM(responseMovieBySection.getMovies()));
                        } else {
                            Log.d(TAG, "loadFirst: return zero");
                        }
                        section.baseRequest = responseMovieBySection.getRequestNowPlaying();
                    }

                }, Throwable::printStackTrace);


    }

    @Override
    public void bindViewModel() {
        hideLoadingMore();
        posts = section.baseHMs;
    }

    public MovieListState getState(){
        return (MovieListState) super.getState();
    }
    public static class MovieListState extends PostsState {
    }

    public  class Mapper {
        public  BaseHM tranToMovieVM(Movie movie) {
            return new MovieHM(movie);
        }

        public  BaseHM tranToMovieTrailerVM(Movie movie) {
            TrailerMovieHM trailerMovieHM = new TrailerMovieHM(movie);
            trailerMovieHM.setFullSpan(true);
            return trailerMovieHM;
        }

        public  List<BaseHM> tranToVM(List<Movie> movieList) {
            List<BaseHM> list = new ArrayList<>();
            for (Movie item : movieList) {
                if (item.getVoteAverage() > 7) {
                    list.add(tranToMovieTrailerVM(item));
                } else {
                    list.add(tranToMovieVM(item));
                }
            }
            return list;
        }
    }
}
