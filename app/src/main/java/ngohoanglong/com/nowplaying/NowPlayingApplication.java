package ngohoanglong.com.nowplaying;

import ngohoanglong.com.nowplaying.dependencyinjection.component.AppComponent;
import ngohoanglong.com.nowplaying.dependencyinjection.component.DaggerAppComponent;
import ngohoanglong.com.nowplaying.dependencyinjection.module.AppModule;

/**
 * Created by Long on 3/14/2017.
 */

public class NowPlayingApplication extends android.app.Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }


}
