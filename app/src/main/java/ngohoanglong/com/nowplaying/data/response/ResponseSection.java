package ngohoanglong.com.nowplaying.data.response;

import java.util.List;

import ngohoanglong.com.nowplaying.data.request.BaseRequest;

/**
 * Created by Long on 3/24/2017.
 */

public class ResponseSection extends BaseResponse {
    public ResponseSection(ResponseStatus isSuccessfull) {
        super(isSuccessfull);
    }

    public String name;
    public List<BaseRequest> baseRequests;

    public ResponseSection(ResponseStatus isSuccessfull, String name, List<BaseRequest> baseRequests) {
        super(isSuccessfull);
        this.name = name;
        this.baseRequests = baseRequests;
    }
}
