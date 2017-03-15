package ngohoanglong.com.nowplaying.dependencyinjection.component;

import dagger.Subcomponent;
import ngohoanglong.com.nowplaying.dependencyinjection.ActivityScope;
import ngohoanglong.com.nowplaying.dependencyinjection.module.MovieModule;
import ngohoanglong.com.nowplaying.display.DragPanelMovieDetailDelegate;


/**
 * Created by Long on 2/28/2017.
 */
@ActivityScope
@Subcomponent(modules = {MovieModule.class})
public interface MovieComponent {
    void inject(DragPanelMovieDetailDelegate dragPanelMovieDetailDelegate);
}
