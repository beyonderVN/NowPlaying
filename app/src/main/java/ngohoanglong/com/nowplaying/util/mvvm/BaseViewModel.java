package ngohoanglong.com.nowplaying.util.mvvm;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import rx.Observable;

/**
 * Created by nongdenchet on 8/1/16.
 */

public abstract class BaseViewModel extends BaseObservable {
    private ThreadScheduler threadScheduler;
    private Resources resources;

    public BaseViewModel(@NonNull ThreadScheduler threadScheduler,
                         @NonNull Resources resources) {
        this.threadScheduler = threadScheduler;
        this.resources = resources;
    }

    public <E> Observable.Transformer<E, E> withScheduler() {
        return observable -> observable.subscribeOn(threadScheduler.subscribeOn())
                .observeOn(threadScheduler.observeOn());
    }

    public String getString(int id) {
        return resources.getString(id);
    }

    public abstract void bindViewModel();
}
