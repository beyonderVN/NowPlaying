package ngohoanglong.com.nowplaying.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import ngohoanglong.com.nowplaying.data.model.User;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Long on 2/10/2017.
 */

public class AuthManager {
    private static final String TAG = "AuthManager";
    public static final String USER = "USER";
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private BehaviorSubject<Boolean> isLoginSuccess;

    public AuthManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                "vivmall.com", Context.MODE_PRIVATE);
        gson = new Gson();
        if (getCurrentUser() != null) {
            isLoginSuccess = BehaviorSubject.create(true);
        }else {
            isLoginSuccess = BehaviorSubject.create(false);
        }
    }
    public AuthManager(Context context,Gson gson) {
        this.sharedPreferences = context.getSharedPreferences(
                "vivmall.com", Context.MODE_PRIVATE);
        this.gson = gson;
        if (getCurrentUser() != null) {
            isLoginSuccess = BehaviorSubject.create(true);
        }else {
            isLoginSuccess = BehaviorSubject.create(false);
        }
    }
    public Observable<Boolean> isLogin(){
        return isLoginSuccess.asObservable()
                .doOnNext(aBoolean -> Log.d(TAG, "doOnNext: "))
                .doOnSubscribe(() -> Log.d(TAG, "doOnSubscribe: "));
    }

    public User getCurrentUser() {
        String userJSON = sharedPreferences.getString(USER, "");
        if (!TextUtils.isEmpty(userJSON)) {
            return gson.fromJson(userJSON, User.class);
        }
        return null;
    }

    public void login(User currentUser) {
        sharedPreferences.edit()
                .putString(USER, gson.toJson(currentUser))
                .apply();
        isLoginSuccess.onNext(true);
    }
    public void signout() {
        sharedPreferences.edit()
                .remove(USER)
                .apply();
        isLoginSuccess.onNext(false);
    }


}
