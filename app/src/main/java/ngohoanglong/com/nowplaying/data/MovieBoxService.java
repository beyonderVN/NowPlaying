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

import io.realm.Realm;
import ngohoanglong.com.nowplaying.data.local.realmobject.MovieRO;
import ngohoanglong.com.nowplaying.data.local.realmobject.TagRO;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.remote.MovieBoxApi;
import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.BaseRequestMovieList;
import ngohoanglong.com.nowplaying.data.request.RequestLastedList;
import ngohoanglong.com.nowplaying.data.request.RequestMovieBySection;
import ngohoanglong.com.nowplaying.data.request.RequestNowPlaying;
import ngohoanglong.com.nowplaying.data.request.RequestPopularList;
import ngohoanglong.com.nowplaying.data.request.RequestSectionList;
import ngohoanglong.com.nowplaying.data.request.RequestUpComingList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseMovieBySection;
import ngohoanglong.com.nowplaying.data.response.ResponseSection;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_LASTED;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_MOVIE_BY_SECTION;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_NOW_PLAYING;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_POPULAR;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_SECTION;
import static ngohoanglong.com.nowplaying.data.RequestFactory.RequestType.REQUEST_UPCOMING;


@Singleton
public class MovieBoxService implements RequestFactory {
    private static final String TAG = "MovieBoxService";

    private final MovieBoxApi movieBoxApi;

    @Inject
    public MovieBoxService(MovieBoxApi serviceApi) {
        movieBoxApi = serviceApi;
    }

    @RxLogObservable
    public Observable<List<Movie>> getNowPlayingList(int page) {
        Log.d(TAG, "getNowPlayingList: page" + page);
        return movieBoxApi.getNowPlayingList(page)
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: " + jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>() {
                    }.getType();
                    List<Movie> movies = (new Gson()).fromJson(jsonObject.getAsJsonArray("results"), listType);
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
                String result = "";
                try {
                    JsonObject jsonObject = movieBoxApi.getTrailer(page).execute().body();

                    JsonArray json = jsonObject.getAsJsonArray("youtube");

                    if (json.size() > 0) {
                        result = json.get(0).getAsJsonObject().get("source").getAsString();// parse the date instead of toString()
                        Log.d(TAG, "result: " + result);
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


    public static final String NOW_PLAYING = "NowPlaying";
    public static final String UP_COMING = "UpComing";
    public static final String POPULAR = "Popular";
    public static final String LASTED = "LASTED";

    @RxLogObservable
    public Observable<BaseResponse> getMovieSectionList(RequestSectionList requestSectionList) {
        return Observable.create(subscriber -> {

            List<BaseRequest> baseRequests = new ArrayList<BaseRequest>();

            baseRequests.add(new RequestNowPlaying(1, NOW_PLAYING));
            baseRequests.add(new RequestUpComingList(1, UP_COMING));
            baseRequests.add(new RequestPopularList(1, POPULAR));

            List<String> urlBackgroundList = new ArrayList<String>();
            urlBackgroundList.add("https://pbs.twimg.com/profile_images/590418452831014912/mRwKtbE2_400x400.jpg");
            urlBackgroundList.add("https://pbs.twimg.com/profile_images/731520908251140096/C91vy0W4.jpg");
            urlBackgroundList.add("https://static.myfigurecollection.net/pics/figure/big/235467.jpg");

            ResponseSection responseSection = new ResponseSection(BaseResponse.ResponseStatus.ISSUCCESSFULL,
                    "Movie Review",
                    baseRequests,
                    urlBackgroundList
            );
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            for (BaseRequest baseRequest : baseRequests
                    ) {
                TagRO tagRO = realm.where(TagRO.class)
                        .equalTo("tagName", (((BaseRequestMovieList) baseRequest).getName()))
                        .findFirst();
                if (tagRO == null) {
                    tagRO = realm.createObject(TagRO.class, ((BaseRequestMovieList) baseRequest).getName());
                }
            }
            realm.commitTransaction();

            subscriber.onNext(responseSection);
            subscriber.onCompleted();
        })

                ;
    }

    @RxLogObservable
    public Observable<BaseResponse> getNowPlayingList(RequestMovieBySection requestSection) {
        return Observable.create(subscriber -> {
            subscriber.onNext(new ResponseSection(BaseResponse.ResponseStatus.ISSUCCESSFULL));
            subscriber.onCompleted();
        });
    }

    @RxLogObservable
    public Observable<BaseResponse> getNowPlayingList(RequestNowPlaying requestNowPlaying) {
        Log.d(TAG, "getNowPlayingList: " + requestNowPlaying.toString());
        return getNowPlayingList(requestNowPlaying.getPage())
                .doOnNext(new SaveMovieROAction(NOW_PLAYING))
                .map(movies -> new ResponseMovieBySection(
                        BaseResponse.ResponseStatus.ISSUCCESSFULL,
                        movies,
                        requestNowPlaying.upPage()
                ));
    }

    @RxLogObservable
    public Observable<BaseResponse> getLastestList(RequestLastedList request) {
        Log.d(TAG, "getLastestList: page" + request);
        return movieBoxApi.getUpcomingList(request.getPage())
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: " + jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>() {
                    }.getType();
                    return (new Gson()).<List<Movie>>fromJson(jsonObject.getAsJsonArray("results"), listType);
                })
                .doOnNext(new SaveMovieROAction(LASTED))
                .map(movies -> new ResponseMovieBySection(
                        BaseResponse.ResponseStatus.ISSUCCESSFULL,
                        movies,
                        request.upPage()
                ));
    }

    @RxLogObservable
    public Observable<BaseResponse> getPopularList(RequestPopularList request) {
        Log.d(TAG, "getPopularList: page" + request);
        return movieBoxApi.getPopularList(request.getPage())
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: " + jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>() {
                    }.getType();
                    return (new Gson()).<List<Movie>>fromJson(jsonObject.getAsJsonArray("results"), listType);
                })
                .doOnNext(new SaveMovieROAction(POPULAR))
                .map(movies -> new ResponseMovieBySection(
                        BaseResponse.ResponseStatus.ISSUCCESSFULL,
                        movies,
                        request.upPage()
                ));
    }

    @RxLogObservable
    public Observable<BaseResponse> getUpcomingList(RequestUpComingList request) {
        Log.d(TAG, "getUpcomingList: page" + request);
        return movieBoxApi.getUpcomingList(request.getPage())
                .map(jsonObject -> {
                    Log.d(TAG, "getNowPlayingList: " + jsonObject.toString());
                    Type listType = new TypeToken<ArrayList<Movie>>() {
                    }.getType();
                    return (new Gson()).<List<Movie>>fromJson(jsonObject.getAsJsonArray("results"), listType);
                })
                .doOnNext(new SaveMovieROAction(UP_COMING))
                .map(movies -> new ResponseMovieBySection(
                        BaseResponse.ResponseStatus.ISSUCCESSFULL,
                        movies,
                        request.upPage()
                ));
    }


    @Override
    public Observable<BaseResponse> sendRequest(BaseRequest requestType) {
        Observable<BaseResponse> baseResponseObservable = null;
        switch (requestType.getType(this)) {
            case REQUEST_SECTION:
                baseResponseObservable = getMovieSectionList((RequestSectionList) requestType);
                break;
            case REQUEST_MOVIE_BY_SECTION:
                baseResponseObservable = getNowPlayingList((RequestMovieBySection) requestType);
                break;
            case REQUEST_NOW_PLAYING:
                baseResponseObservable = getNowPlayingList((RequestNowPlaying) requestType);
                break;
            case REQUEST_LASTED:
                baseResponseObservable = getLastestList((RequestLastedList) requestType);
                break;
            case REQUEST_UPCOMING:
                baseResponseObservable = getUpcomingList((RequestUpComingList) requestType);
                break;
            case REQUEST_POPULAR:
                baseResponseObservable = getPopularList((RequestPopularList) requestType);
                break;
            default:
        }
        return baseResponseObservable
                .delay(1, TimeUnit.SECONDS);
    }

    @Override
    public RequestType getRequestType(RequestNowPlaying requestNowPlaying) {
        return REQUEST_NOW_PLAYING;
    }

    @Override
    public RequestType getRequestType(RequestLastedList requestLastedList) {
        return REQUEST_LASTED;
    }

    @Override
    public RequestType getRequestType(RequestPopularList requestPopularList) {
        return REQUEST_POPULAR;
    }

    @Override
    public RequestType getRequestType(RequestUpComingList requestUpComingList) {
        return REQUEST_UPCOMING;
    }

    @Override
    public RequestType getRequestType(RequestSectionList requestSectionList) {
        return REQUEST_SECTION;
    }

    @Override
    public RequestType getRequestType(RequestMovieBySection requestMovieBySection) {
        return REQUEST_MOVIE_BY_SECTION;
    }

    static class SavePOJOToRealmObject {
        static boolean save(Movie movie, String tag) {
            boolean isSaveSucessfull = false;

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            MovieRO movieRO = realm.where(MovieRO.class).equalTo("id", (movie.getId())).findFirst();
            if (movieRO == null) {

                movieRO = realm.createObject(MovieRO.class, movie.getId());
                movieRO.setAdult(movie.isAdult());
                movieRO.setBackdropPath(movie.getBackdropPath());
                movieRO.setOriginalLanguage(movie.getOriginalLanguage());
                movieRO.setOriginalTitle(movie.getOriginalTitle());
                movieRO.setOverview(movie.getOverview());
                movieRO.setPopularity(movie.getPopularity());
                movieRO.setPosterPath(movie.getPosterPath());
                movieRO.setTitle(movie.getTitle());
                movieRO.setReleaseDate(movie.getReleaseDate());
                movieRO.setVideo(movie.isVideo());
                movieRO.setVoteAverage(movie.getVoteAverage());
                movieRO.setVoteCount(movie.getVoteCount());

                TagRO tagRO = realm.where(TagRO.class)
                        .equalTo("tagName", tag)
                        .findFirst();
                if (tagRO == null) {
                    tagRO = realm.createObject(TagRO.class, tag);
                }
                movieRO.getTags().add(tagRO);


                isSaveSucessfull = true;
                Log.d(TAG, "save: create successfully");
            } else {
                Log.d(TAG, "save: object exited");


                boolean isHavingThisTag = false;
                for (TagRO tagRO1 : movieRO.getTags()
                        ) {
                    if (tagRO1 != null && tagRO1.getTagName() == tag) {
                        isHavingThisTag = true;
                    }
                }
                if (!isHavingThisTag) {
                    TagRO tagRO = realm.where(TagRO.class)
                            .equalTo("tagName", tag)
                            .findFirst();
                    if (tagRO == null) {
                        tagRO = realm.createObject(TagRO.class, tag);
                    }
                    movieRO.getTags().add(tagRO);
                }


                isSaveSucessfull = true;
            }
            realm.commitTransaction();
            return isSaveSucessfull;
        }
        static boolean save(List<Movie> movies, String tag) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            for (Movie movie:movies
                 ) {
                MovieRO movieRO = realm.where(MovieRO.class).equalTo("id", (movie.getId())).findFirst();
                if (movieRO == null) {

                    movieRO = realm.createObject(MovieRO.class, movie.getId());
                    movieRO.setAdult(movie.isAdult());
                    movieRO.setBackdropPath(movie.getBackdropPath());
                    movieRO.setOriginalLanguage(movie.getOriginalLanguage());
                    movieRO.setOriginalTitle(movie.getOriginalTitle());
                    movieRO.setOverview(movie.getOverview());
                    movieRO.setPopularity(movie.getPopularity());
                    movieRO.setPosterPath(movie.getPosterPath());
                    movieRO.setTitle(movie.getTitle());
                    movieRO.setReleaseDate(movie.getReleaseDate());
                    movieRO.setVideo(movie.isVideo());
                    movieRO.setVoteAverage(movie.getVoteAverage());
                    movieRO.setVoteCount(movie.getVoteCount());

                    TagRO tagRO = realm.where(TagRO.class)
                            .equalTo("tagName", tag)
                            .findFirst();
                    if (tagRO == null) {
                        tagRO = realm.createObject(TagRO.class, tag);
                    }
                    movieRO.getTags().add(tagRO);
                    Log.d(TAG, "save: create successfully");
                } else {
                    Log.d(TAG, "save: object exited");


                    boolean isHavingThisTag = false;
                    for (TagRO tagRO1 : movieRO.getTags()
                            ) {
                        if (tagRO1 != null && tagRO1.getTagName() == tag) {
                            isHavingThisTag = true;
                        }
                    }
                    if (!isHavingThisTag) {
                        TagRO tagRO = realm.where(TagRO.class)
                                .equalTo("tagName", tag)
                                .findFirst();
                        if (tagRO == null) {
                            tagRO = realm.createObject(TagRO.class, tag);
                        }
                        movieRO.getTags().add(tagRO);
                    }

                }
            }



            realm.commitTransaction();
            return true;
        }

    }

    static class SaveMovieROAction implements Action1<List<Movie>> {
        String tag;

        public SaveMovieROAction(String tag) {
            this.tag = tag;
        }

        @Override
        public void call(List<Movie> o) {
                Log.d(TAG, "SavePOJOToRealmObject.save: " + SavePOJOToRealmObject.save(o, tag));
        }
    }
}