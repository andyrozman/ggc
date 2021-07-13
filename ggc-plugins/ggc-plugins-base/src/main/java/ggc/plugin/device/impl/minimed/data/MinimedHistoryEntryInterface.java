package ggc.plugin.device.impl.minimed.data;

import java.util.List;

/**
 * Created by andy on 7/24/18.
 */
public interface MinimedHistoryEntryInterface {

    void setData(List<Byte> listRawData, boolean doNotProcess);

}
