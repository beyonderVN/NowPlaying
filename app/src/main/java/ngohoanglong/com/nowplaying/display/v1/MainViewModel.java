package ngohoanglong.com.nowplaying.display.v1;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.PostViewModel;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import static ngohoanglong.com.nowplaying.display.v1.MainViewModel.Mapper.tranToMovieTrailerVM;
import static ngohoanglong.com.nowplaying.display.v1.MainViewModel.Mapper.tranToMovieVM;

/**
 * Created by Long on 3/14/2017.
 */

public class MainViewModel extends PostViewModel {
    private static final String TAG = "MainViewModel";
    protected PublishSubject<Boolean> refresh = PublishSubject.create();
    MovieBoxService service;
    int page = 1;

    @Inject
    public MainViewModel(ThreadScheduler threadScheduler,
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
        Log.d(TAG, "loadFirst: page: " + page);
        if (isNeedLoadFirst()) {
            service.getMovies(page)
//                    .delay(0, TimeUnit.SECONDS)
                    .takeUntil(refresh)
                    .compose(withScheduler())
                    .map(new Func1<List<Movie>, List<BaseHM>>() {
                        @Override
                        public List<BaseHM> call(List<Movie> movieList) {
                            Log.d(TAG, "getNowPlayingList: " + movieList.size());
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
                    })
                    .subscribe(new Action1<List<BaseHM>>() {
                        @Override
                        public void call(List<BaseHM> baseHMs) {
                            if (baseHMs.size() > 0) {
                                updatePosts(baseHMs);
                                page++;
                            } else {
                                Log.d(TAG, "loadFirst: return zero");
                            }
                        }
                    }, Throwable::printStackTrace);
        }

    }

    @RxLogObservable
    public void loadMore() {
        Log.d(TAG, "loadMore:  page: " + page);
        service.getMovies(page)
                .delay(1, TimeUnit.SECONDS)
                .takeUntil(refresh)
                .compose(withScheduler())
                .map(new Func1<List<Movie>, List<BaseHM>>() {
                    @Override
                    public List<BaseHM> call(List<Movie> movieList) {
                        Log.d(TAG, "getNowPlayingList: " + movieList.size());
                        List<BaseHM> list = new ArrayList<>();
                        for (Movie item : movieList) {
                            if (item.getVoteAverage() > 5) {
                                list.add(tranToMovieTrailerVM(item));
                            } else {
                                list.add(tranToMovieVM(item));
                            }
                        }
                        return list;
                    }
                })
                .doOnSubscribe(() -> {
                    isLoadingMore.onNext(true);
                })

                .subscribe(new Action1<List<BaseHM>>() {
                    @Override
                    public void call(List<BaseHM> baseHMs) {
                        Log.d(TAG, "loadMorePosts: ");
                        isLoadingMore.onNext(false);
                        posts.addAll(baseHMs);
                        page++;
                    }
                }, Throwable::printStackTrace)
        ;


    }

    @Override
    public void bindViewModel() {
        loadFirst();
    }

    public static class MainState extends PostsState {
        public MainState(ObservableArrayList<BaseHM> baseHMs) {
            super(baseHMs);
        }
    }

    public static class Mapper {
        public static BaseHM tranToMovieVM(Movie movie) {
            return new MovieHM(movie);
        }

        public static BaseHM tranToMovieTrailerVM(Movie movie) {
            TrailerMovieHM trailerMovieHM = new TrailerMovieHM(movie);
            trailerMovieHM.setFullSpan(true);
            return trailerMovieHM;
        }

        public static List<BaseHM> tranToVM(List<Movie> movieList) {
            List<BaseHM> list = new ArrayList<>();
            for (Movie item : movieList) {
                if (item.getVoteAverage() > 6) {
                    list.add(tranToMovieTrailerVM(item));
                } else {
                    list.add(tranToMovieVM(item));
                }
            }
            return list;
        }
    }
}
