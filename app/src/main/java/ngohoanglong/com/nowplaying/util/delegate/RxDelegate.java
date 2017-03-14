package ngohoanglong.com.nowplaying.util.delegate;

import android.util.Log;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Long on 2/27/2017.
 */

public class RxDelegate extends BaseDelegate {
    private static final String TAG = "RxDelegate";
    //    private AppComponent appComponent;
    private static final int START = 0;
    private static final int STOP = 1;
    private PublishSubject<Integer> stopEvent = PublishSubject.create();
    private PublishSubject<Integer> startEvent = PublishSubject.create();
    public Observable<Integer> stopEvent() {
        return stopEvent.asObservable()
                .doOnNext(integer -> Log.d(TAG, "stopEvent: "+integer));
    }
    public Observable<Integer> startEvent() {
        return startEvent.asObservable();
    }
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    public CompositeSubscription getCompositeSubscription(){
        return compositeSubscription;
    }

    @Override
    public void onStart() {
        startEvent.onNext(START);
    }

    @Override
    public void onStop() {
        stopEvent.onNext(STOP);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }
}
