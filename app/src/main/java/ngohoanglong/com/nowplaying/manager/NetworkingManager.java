package ngohoanglong.com.nowplaying.manager;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Long on 2/8/2017.
 */

public class NetworkingManager {
    private ConnectivityManager connectivityManager;

    public NetworkingManager(Context context) {
        connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected() {
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
