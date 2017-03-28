package ngohoanglong.com.nowplaying.data.response;

import java.io.Serializable;

/**
 * Created by Long on 3/24/2017.
 */

public class BaseResponse implements Serializable {
    public static enum ResponseStatus{
        ISSUCCESSFULL,
        FAIL
    }

    protected ResponseStatus isSuccessfull;

    public BaseResponse(ResponseStatus isSuccessfull) {
        this.isSuccessfull = isSuccessfull;
    }

    public ResponseStatus isSuccessfull() {
        return isSuccessfull;
    }

    public void setSuccessfull(ResponseStatus successfull) {
        isSuccessfull = successfull;
    }
}
