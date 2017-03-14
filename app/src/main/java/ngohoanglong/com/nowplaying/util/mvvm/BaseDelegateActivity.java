package ngohoanglong.com.nowplaying.util.mvvm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ngohoanglong.com.nowplaying.util.delegate.LifecycleDelegate;

/**
 * Created by Long on 2/27/2017.
 */

public abstract class BaseDelegateActivity extends AppCompatActivity {
    protected List<LifecycleDelegate> lifecycleDelegates = new ArrayList<>();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onStart();
        }

    }

    @Override
    protected void onStop() {
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onSaveInstanceState(bundle);
        }
    }
}
