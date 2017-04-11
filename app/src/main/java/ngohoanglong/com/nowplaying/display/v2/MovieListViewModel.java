package ngohoanglong.com.nowplaying.display.v2;

import android.content.res.Resources;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.data.local.realmobject.MovieRO;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.request.BaseRequestMovieList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseMovieBySection;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.PostViewModel;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Long on 3/14/2017.
 */

public class MovieListViewModel extends PostViewModel {
    private static final String TAG = "MovieListViewModel";
    protected PublishSubject<Boolean> refresh = PublishSubject.create();
    MovieBoxService service;
    PublishSubject<String> errorStringPublishSubject = PublishSubject.create();

    Observable<String> getErrorStringPublishSubject(){
        return errorStringPublishSubject.asObservable()
                .compose(withScheduler());
    }

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


                    }, throwable -> {
                        throwable.printStackTrace();
                        errorStringPublishSubject.onNext(throwable.getMessage());
                    });
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


                },throwable -> {
                    throwable.printStackTrace();
                    errorStringPublishSubject.onNext(throwable.getMessage());
                });

    }

    @Override
    public void bindViewModel() {
        hideLoadingMore();
        posts = section.baseHMs;

        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovieRO> movieROs = realm.where(MovieRO.class)
                .contains("tags.tagName",((BaseRequestMovieList)section.baseRequest).getName())
                .findAll();
        Log.d(TAG, "loadMore: +movieROs.size()"+((BaseRequestMovieList)section.baseRequest).getName()
                + ": "+movieROs.size());
        movieROs.addChangeListener(new RealmChangeListener<RealmResults<MovieRO>>() {
            @Override
            public void onChange(RealmResults<MovieRO> element) {
                Log.d(TAG, "onChange: "+element.size());
            }
        });
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
//              keep this gor movieROs.addChangeListener working o.O
                Log.d(TAG, "movieROs.size(): "+((BaseRequestMovieList)section.baseRequest).getName()+": "+movieROs.size());
            }
        });

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
