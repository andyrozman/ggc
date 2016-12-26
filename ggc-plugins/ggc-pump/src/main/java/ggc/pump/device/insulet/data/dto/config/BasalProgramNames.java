package ggc.pump.device.insulet.data.dto.config;

import java.util.HashMap;
import java.util.Map;

import ggc.pump.device.insulet.data.enums.OmnipodDataType;
import ggc.pump.device.insulet.util.InsuletUtil;

/**
 * Created by andy on 19.05.15.
 */
public class BasalProgramNames extends ConfigRecord
{

    Short numberOfBasalPrograms;
    Short enabledIndex;
    Short maxNameSize;
    Map<Integer, String> basalNames = new HashMap<Integer, String>();


    public BasalProgramNames()
    {
        super(true);
        InsuletUtil.setBasalProgramNames(this);
    }


    @Override
    public void customProcess(int[] data)
    {
        numberOfBasalPrograms = getShort(data, 2);
        enabledIndex = getShort(data, 4);
        maxNameSize = getShort(data, 6);

        for (int i = 8; i < this.length - 2; i += (2 + maxNameSize))
        {
            int index = getShort(data, i);
            String name = getString(data, i + 2, maxNameSize);
            name = createReadableString(name);

            basalNames.put(index, name);
        }
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.BasalProgramNames;
    }


    @Override
    public String toString()
    {
        return "BasalProgramNames [" + "numberOfBasalPrograms=" + numberOfBasalPrograms + ", enabledIndex="
                + enabledIndex + ", maxNameSize=" + maxNameSize + ", basalNames=" + basalNames + ']';
    }


    @Override
    public void writeConfigData()
    {
        // for (int i = 0; i < basalNames.size(); i++)
        // {
        // writeSetting(InsuletUtil.getI18nControl().getMessage("PCFG_BASAL_PROFILE")
        // + " #" + i, basalNames.get(i),
        // basalNames.get(i), PumpConfigurationGroup.Basal);
        // }
    }
}
