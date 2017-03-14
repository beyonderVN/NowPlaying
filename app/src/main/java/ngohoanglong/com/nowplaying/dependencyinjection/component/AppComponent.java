package ngohoanglong.com.nowplaying.dependencyinjection.component;

import javax.inject.Singleton;

import dagger.Component;
import ngohoanglong.com.nowplaying.dependencyinjection.module.AppModule;
import ngohoanglong.com.nowplaying.dependencyinjection.module.DataModule;
import ngohoanglong.com.nowplaying.dependencyinjection.module.MovieModule;
import ngohoanglong.com.nowplaying.display.MainActivity;


/**
 * Created by Long on 2/28/2017.
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    MovieComponent plus(MovieModule movieModule);

    void inject(MainActivity mainActivity);

}
