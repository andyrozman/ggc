package ggc.cgms.device.dexcom.receivers.data;

import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserType;
import ggc.cgms.util.DataAccessCGMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

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
