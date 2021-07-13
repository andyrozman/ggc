package ggc.plugin.device.impl.abbott.hid;

/**
 * Created by andy on 23/11/17.
 */
public interface AbbottHidDataConverter
{
    void convertData(AbbottHidTextCommand textCommand, String s);
}