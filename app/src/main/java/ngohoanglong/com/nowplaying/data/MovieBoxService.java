package ngohoanglong.com.nowplaying.data;


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
import ngohoanglong.com.nowplaying.data.remote.MovieBoxApi;
import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.RequestMovieBySection;
import ngohoanglong.com.nowplaying.data.request.RequestSection;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseSection;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.*;


@Singleton
public class MovieBoxService implements RequestFactory {
    private static final String TAG = "MovieBoxService";

    private final MovieBoxApi movieBoxApi;


    @Inject
    public MovieBoxService(MovieBoxApi serviceApi) {
        movieBoxApi = serviceApi;
    }

    @RxLogObservable
    public Observable<List<Movie>> getMovies() {
        return movieBoxApi.getNowPlayingList()
                .map(new Func1<JsonObject, List<Movie>>() {
                    @Override
                    public List<Movie> call(JsonObject jsonObject) {
                        Log.d(TAG, "getNowPlayingList: "+jsonObject.toString());
                        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
                        List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"),listType);
                        return movies;
                    }
                });
    }

    @RxLogObservable
    public Observable<List<Movie>> getMovies(int page) {
        return movieBoxApi.getNowPlayingList(page)
                .delay(2, TimeUnit.SECONDS)
                .map(new Func1<JsonObject, List<Movie>>() {
                    @Override
                    public List<Movie> call(JsonObject jsonObject) {
                        Log.d(TAG, "getNowPlayingList: "+jsonObject.toString());
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


    @RxLogObservable
    public Observable<BaseResponse> getMovieSection(RequestSection requestSection) {
        return Observable.create(subscriber -> {
            subscriber.onNext(new ResponseSection());
        });
    }
    @RxLogObservable
    public Observable<BaseResponse> getMovieBySection(RequestMovieBySection requestSection) {
        return Observable.create(subscriber -> {
            subscriber.onNext(new ResponseSection());
        });
    }


    @Override
    public Observable<BaseResponse> sendRequest(BaseRequest requestType) {
        switch (requestType.getType(this)){
            case REQUEST_SECTION :
                return getMovieSection((RequestSection) requestType);
            case REQUEST_MOVIE_BY_SECTION :
                return getMovieBySection((RequestMovieBySection) requestType);

        }
        return null;
    }

    @Override
    public RequestType getRequestType(RequestSection requestSection) {
        return REQUEST_SECTION;
    }

    @Override
    public RequestType getRequestType(RequestMovieBySection requestMovieBySection) {
        return REQUEST_MOVIE_BY_SECTION;
    }
}