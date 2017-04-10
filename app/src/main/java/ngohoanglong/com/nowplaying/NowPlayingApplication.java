package ngohoanglong.com.nowplaying;

import android.content.Context;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;
import ngohoanglong.com.nowplaying.dependencyinjection.component.AppComponent;
import ngohoanglong.com.nowplaying.dependencyinjection.component.DaggerAppComponent;
import ngohoanglong.com.nowplaying.dependencyinjection.module.AppModule;

/**
 * Created by Long on 3/14/2017.
 */

public class NowPlayingApplication extends android.app.Application {
    private static final String TAG = NowPlayingApplication.class.getSimpleName();
    public static AppComponent appComponent;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(3) // Must be bumped when the schema changes
                .migration(new RealmMigration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        //delete olddata
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

    }

    class RealmMigration implements io.realm.RealmMigration{

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            Log.d(TAG, "oldVersion: "+oldVersion);
            RealmSchema schema = realm.getSchema();
            realm.deleteAll();
            if (oldVersion == 0) {
                schema.get("MovieRO")
                        .addRealmListField("tags", schema.get("TagRO"));
                oldVersion++;
            }
            if (oldVersion == 1) {
                schema.get("TagRO");
                Log.d(TAG, "migrate: "+schema.get("TagRO").getPrimaryKey());
                oldVersion++;
            }
            if (oldVersion == 2) {
                schema.get("TagRO")
                .addPrimaryKey("tagName")
                ;
                oldVersion++;
            }
        }
    }
}
