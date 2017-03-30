package ngohoanglong.com.nowplaying.dependencyinjection.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.data.remote.MovieBoxApi;
import ngohoanglong.com.nowplaying.data.remote.MovieBoxServiceFactory;


/**
 * Created by Long on 2/28/2017.
 */
@Module
public class DataModule {
    @Provides
    @Singleton
    MovieBoxApi provideMovieBoxServiceApi() {
        return MovieBoxServiceFactory.makeService();
    }

    @Provides
    @Singleton
    MovieBoxService provideMovieBoxService(MovieBoxApi movieBoxApi) {
        return new MovieBoxService(movieBoxApi);
    }
}
