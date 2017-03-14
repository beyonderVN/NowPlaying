package ngohoanglong.com.nowplaying.dependencyinjection.component;

import dagger.Subcomponent;
import ngohoanglong.com.nowplaying.dependencyinjection.ActivityScope;
import ngohoanglong.com.nowplaying.dependencyinjection.module.MovieModule;


/**
 * Created by Long on 2/28/2017.
 */
@ActivityScope
@Subcomponent(modules = {MovieModule.class})
public interface MovieComponent {

}
