package ggc.cgms.device.dexcom.receivers.data;

import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserType;
import ggc.cgms.util.DataAccessCGMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.atech.i18n.I18nControlAbstract;

public class ReceiverDownloadData
{

    private String serialNumber = null;
    ArrayList<ReceiverDownloadDataConfig> configs = new ArrayList<ReceiverDownloadDataConfig>();
    HashMap<String, ReceiverDownloadDataConfig> configMap = new HashMap<String, ReceiverDownloadDataConfig>();
    // ReceiverDatabaseRecordsParser data = null;
    // Object data = null;

    HashMap<DataOutputParserType, List<?>> data = new HashMap<DataOutputParserType, List<?>>();
    I18nControlAbstract i18nControl = DataAccessCGMS.getInstance().getI18nControlInstance();

    public void addConfigurationEntry(String key, String value, boolean canBeTranslated)
    {
        configs.add(new ReceiverDownloadDataConfig(key, value, canBeTranslated));
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public ArrayList<ReceiverDownloadDataConfig> getConfigs()
    {
        return configs;
    }

    public void setConfigs(ArrayList<ReceiverDownloadDataConfig> configs)
    {
        this.configs = configs;
    }

    public void addConfigurationEntry(String key, int intValue, boolean canBeTranslated)
    {
        ReceiverDownloadDataConfig cfg = new ReceiverDownloadDataConfig(key, "" + intValue, canBeTranslated);

        configs.add(cfg);
        configMap.put(key, cfg);
    }

    public void addData(DataOutputParserType dataType, List<?> list)
    {
        this.data.put(dataType, list);
    }

    public List<?> getDataByType(DataOutputParserType type)
    {
        return data.get(type);
    }

    public boolean containsConfiguration(String key)
    {
        return this.configMap.containsKey(key);
    }

    public ReceiverDownloadDataConfig getConfigByKey(String key)
    {
        return this.configMap.get(key);
    }

    public String getConfigValueByKey(String key)
    {
        ReceiverDownloadDataConfig cfg = getConfigByKey(key);

        if (cfg.isCanBeTranslated())
            return i18nControl.getMessage(cfg.getValue());
        else
            return cfg.getValue();
    }

}
