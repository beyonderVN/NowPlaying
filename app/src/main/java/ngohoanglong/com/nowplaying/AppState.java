package ngohoanglong.com.nowplaying;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ngohoanglong.com.nowplaying.display.v2.MainViewModel;

/**
 * Created by Long on 3/29/2017.
 */

public class AppState implements Serializable{
    private static final String TAG = "AppState";
    List<MainViewModel.Section> sections = new ArrayList<>();

    static AppState APPSTATE;

    public static AppState getAPPSTATE(){
        if(APPSTATE==null) {
            SharedPreferences sharedPreferences;
            sharedPreferences = NowPlayingApplication.context.getSharedPreferences(
                    "NowPlaying", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("AppState", "");
            if (!Objects.equals(json, "") && !TextUtils.isEmpty(json)) {
                Log.d(TAG, "getAPPSTATE: "+json);
                APPSTATE = gson.fromJson(json, AppState.class);
            }else {
                APPSTATE = new AppState();
            }
        }
        return APPSTATE;
    }

    public List<MainViewModel.Section> getSections() {
        return sections;
    }

    public void setSections(List<MainViewModel.Section> sections) {
        this.sections = sections;
    }

    public void saveAppState(){
        SharedPreferences sharedPreferences;
        sharedPreferences = NowPlayingApplication.context.getSharedPreferences(
                "NowPlaying", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPreferences.edit()
                .putString("AppState", gson.toJson(this))
                .apply();
    }
}
