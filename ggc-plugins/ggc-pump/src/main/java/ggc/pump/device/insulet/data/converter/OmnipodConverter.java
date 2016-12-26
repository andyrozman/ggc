package ggc.pump.device.insulet.data.converter;

import ggc.pump.device.insulet.data.dto.AbstractRecord;

/**
 * Created by andy on 21.05.15.
 */
public interface OmnipodConverter
{

    void convert(AbstractRecord record);


    void postProcessData();

}
