package ngohoanglong.com.nowplaying.data.request;

import java.io.Serializable;

import ngohoanglong.com.nowplaying.data.RequestFactory;

/**
 * Created by Long on 3/24/2017.
 */

public interface RequestType extends Serializable {
    RequestFactory.RequestType getType(RequestFactory requestFactory);
}
