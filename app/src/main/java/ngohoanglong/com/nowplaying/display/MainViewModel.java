package ngohoanglong.com.nowplaying.display;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.remote.MovieBoxServiceApi;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.PostViewModel;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.TrailerMovieHM;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

import static ngohoanglong.com.nowplaying.display.MainViewModel.Mapper.tranToMovieTrailerVM;
import static ngohoanglong.com.nowplaying.display.MainViewModel.Mapper.tranToMovieVM;

/**
 * Created by Long on 3/14/2017.
 */

public class MainViewModel extends PostViewModel {
    private static final String TAG = "MainViewModel";
    MovieBoxServiceApi service;
    int page = 0;



    @Inject
    public MainViewModel(ThreadScheduler threadScheduler,
                         Resources resources,
                         MovieBoxServiceApi service
    ) {
        super(threadScheduler, resources);
        this.service = service;
    }

    protected PublishSubject<Boolean> refresh = PublishSubject.create();

    public boolean isNeedLoadFirst() {
        Log.d(TAG, "isNeedLoadFirst: " + posts.size());
        return posts == null || posts.isEmpty();
    }

    @RxLogObservable
    public void loadFirst() {

        if (isNeedLoadFirst()) {
            service.getMovieList(page)
                    .takeUntil(refresh)
                    .compose(withScheduler())
                    .map(new Func1<JsonObject, List<Movie>>() {
                        @Override
                        public List<Movie> call(JsonObject jsonObject) {
                            Log.d(TAG, "getMovieList: " + jsonObject.toString());
                            Type listType = new TypeToken<ArrayList<Movie>>() {
                            }.getType();
                            List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"), listType);
                            return movies;
                        }
                    })
                    .map(new Func1<List<Movie>, List<BaseHM>>() {
                        @Override
                        public List<BaseHM> call(List<Movie> movieList) {
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
                    .subscribe(new Action1<List<BaseHM>>() {
                        @Override
                        public void call(List<BaseHM> baseHMs) {
                            if (posts.size() > 0) {
                                updatePosts(posts);
                            } else {
                                Log.d(TAG, "loadFirst: return zero");
                            }
                        }
                    });
        }

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
//            list.add(item.getVoteAverage()>5?tranToMovieTrailerVM(item):tranToMovieVM(item));

                if (item.getVoteAverage() > 5) {
                    list.add(tranToMovieTrailerVM(item));

                } else {
                    list.add(tranToMovieVM(item));

                }
            }
            return list;
        }


    }
}