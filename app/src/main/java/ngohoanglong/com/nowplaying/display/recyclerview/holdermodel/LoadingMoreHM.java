package ngohoanglong.com.nowplaying.display.recyclerview.holdermodel;


import ngohoanglong.com.nowplaying.display.recyclerview.holderfactory.ViewTypeFactory;

/**
 * Created by Long on 10/5/2016.
 */

public class LoadingMoreHM extends BaseHM{

    @Override
    public int getVMType(ViewTypeFactory vmTypeFactory) {
        return vmTypeFactory.getType(this);
    }

}
