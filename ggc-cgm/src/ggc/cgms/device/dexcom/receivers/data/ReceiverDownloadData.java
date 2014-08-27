package ggc.cgms.device.dexcom.receivers.data;

import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiverDownloadData {

    private String serialNumber = null;
    ArrayList<ReceiverDownloadDataConfig> configs = new ArrayList<ReceiverDownloadDataConfig>();
    // ReceiverDatabaseRecordsParser data = null;
    // Object data = null;

    HashMap<DataOutputParserType, List<?>> data = new HashMap<DataOutputParserType, List<?>>();


    public void addConfigurationEntry(String key, String value, boolean canBeTranslated) {
        configs.add(new ReceiverDownloadDataConfig(key, value, canBeTranslated));
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public ArrayList<ReceiverDownloadDataConfig> getConfigs() {
        return configs;
    }


    public void setConfigs(ArrayList<ReceiverDownloadDataConfig> configs) {
        this.configs = configs;
    }


    public void addConfigurationEntry(String string, int intValue, boolean canBeTranslated) {
        configs.add(new ReceiverDownloadDataConfig(string, "" + intValue, canBeTranslated));
    }


    public void addData(DataOutputParserType dataType, List<?> list) {
        this.data.put(dataType, list);
    }

}
