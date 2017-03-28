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
import ngohoanglong.com.nowplaying.data.request.RequestNowPlaying;
import ngohoanglong.com.nowplaying.data.request.RequestSectionList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseMovieBySection;
import ngohoanglong.com.nowplaying.data.response.ResponseSection;
import rx.Observable;
import rx.Subscriber;

import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_MOVIE_BY_SECTION;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_NOW_PLAYING;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_SECTION;


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
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: "+jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
                    List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"),listType);
                    return movies;
                });
    }

    @RxLogObservable
    public Observable<List<Movie>> getMovies(int page) {
        Log.d(TAG, "getMovies: page"+page);
        return movieBoxApi.getNowPlayingList(page)
                .delay(2, TimeUnit.SECONDS)
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: "+jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
                    List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"),listType);
                    return movies;
                })
                .doOnNext(movies -> {

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
    public Observable<BaseResponse> getMovieSectionList(RequestSectionList requestSectionList) {
        return Observable.create(subscriber -> {

            List<BaseRequest> baseRequests = new ArrayList<BaseRequest>();
            for (int i = 0; i < 1; i++) {
                baseRequests.add(new RequestNowPlaying(1,"Now Playing "+i));
            }
            ResponseSection responseSection = new ResponseSection(BaseResponse.ResponseStatus.ISSUCCESSFULL,
                    "Movie Review",
                    baseRequests);
            subscriber.onNext(responseSection);
            subscriber.onCompleted();
        });
    }
    @RxLogObservable
    public Observable<BaseResponse> getMovieBySection(RequestMovieBySection requestSection) {
        return Observable.create(subscriber -> {
            subscriber.onNext(new ResponseSection(BaseResponse.ResponseStatus.ISSUCCESSFULL));
            subscriber.onCompleted();
        });
    }
    @RxLogObservable
    public Observable<BaseResponse> getMovieBySection(RequestNowPlaying requestNowPlaying) {
        Log.d(TAG, "getMovieBySection: "+requestNowPlaying.toString());
        return getMovies(requestNowPlaying.getPage())
                .map(movies -> new ResponseMovieBySection(
                        BaseResponse.ResponseStatus.ISSUCCESSFULL,
                        movies,
                        requestNowPlaying.upPage()
                ));
    }

    @Override
    public RequestType getRequestType(RequestNowPlaying requestNowPlaying) {
        return REQUEST_NOW_PLAYING;
    }

    @Override
    public Observable<BaseResponse> sendRequest(BaseRequest requestType) {
        switch (requestType.getType(this)){
            case REQUEST_SECTION :
                return getMovieSectionList((RequestSectionList) requestType);
            case REQUEST_MOVIE_BY_SECTION :
                return getMovieBySection((RequestMovieBySection) requestType);
            case REQUEST_NOW_PLAYING:
                return getMovieBySection((RequestNowPlaying) requestType);

        }
        return null;
    }

    @Override
    public RequestType getRequestType(RequestSectionList requestSectionList) {
        return REQUEST_SECTION;
    }

    @Override
    public RequestType getRequestType(RequestMovieBySection requestMovieBySection) {
        return REQUEST_MOVIE_BY_SECTION;
    }
}