package ngohoanglong.com.nowplaying.data.remote;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MovieBoxApi {

    /**
     * Retrieve a list of competitions
     */


    @GET("movie/now_playing")
    Observable<JsonObject> getNowPlayingList();
    @GET("movie/now_playing")
    Observable<JsonObject> getNowPlayingList(@Query("page") int page);

    @GET("movie/popular")
    Observable<JsonObject> getPopularList(@Query("page") int page);

    @GET("movie/latest")
    Observable<JsonObject> getLastestList(@Query("page") int page);

    @GET("movie/upcoming")
    Observable<JsonObject> getUpcomingList(@Query("page") int page);

    @GET("movie/{id}/trailers")
    Call<JsonObject> getTrailer(@Path("id") int id);






}
