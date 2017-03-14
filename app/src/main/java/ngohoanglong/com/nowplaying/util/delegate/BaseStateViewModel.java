package ngohoanglong.com.nowplaying.util.delegate;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.mvvm.BaseViewModel;


/**
 * Created by Long on 2/27/2017.
 */

public abstract class BaseStateViewModel<S extends BaseState> extends BaseViewModel {
    public BaseStateViewModel(@NonNull ThreadScheduler threadScheduler, @NonNull Resources resources) {
        super(threadScheduler, resources);
    }
    protected S state;

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
    }

    public S saveInstanceState(){
        return state;
    }

    public  void returnInstanceState(S instanceState){
        state = instanceState;
    };

}
