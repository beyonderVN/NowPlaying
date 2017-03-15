package ngohoanglong.com.nowplaying.data.remote;


import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import ngohoanglong.com.nowplaying.data.model.Movie;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


@Singleton
public class MovieBoxService {
    private static final String TAG = "MovieBoxService";

    private final MovieBoxApi movieBoxApi;

    @Inject
    public MovieBoxService(MovieBoxApi serviceApi) {
        movieBoxApi = serviceApi;
    }

    @RxLogObservable
    public Observable<List<Movie>> getMovies() {
        return movieBoxApi.getMovieList()
                .map(new Func1<JsonObject, List<Movie>>() {
                    @Override
                    public List<Movie> call(JsonObject jsonObject) {
                        Log.d(TAG, "getMovieList: "+jsonObject.toString());
                        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
                        List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"),listType);
                        return movies;
                    }
                });
    }

    @RxLogObservable
    public Observable<List<Movie>> getMovies(int page) {
        return movieBoxApi.getMovieList(page)
                .delay(2, TimeUnit.SECONDS)
                .map(new Func1<JsonObject, List<Movie>>() {
                    @Override
                    public List<Movie> call(JsonObject jsonObject) {
                        Log.d(TAG, "getMovieList: "+jsonObject.toString());
                        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
                        List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"),listType);
                        return movies;
                    }
                });
    }

    @RxLogObservable
    public Observable<String> getTrailer(int page) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result="";
                try {
                    JsonObject jsonObject =  movieBoxApi.getTrailer(page).execute().body();

                    JsonArray json = jsonObject.getAsJsonArray("youtube");

                    if (json.size() > 0) {
                        result =  json.get(0).getAsJsonObject().get("source").getAsString();// parse the date instead of toString()
                        Log.d(TAG, "result: "+result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }

}