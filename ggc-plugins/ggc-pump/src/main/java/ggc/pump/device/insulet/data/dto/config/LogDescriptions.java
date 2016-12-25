package main.java.ggc.pump.device.insulet.data.dto.config;

import java.util.ArrayList;
import java.util.List;

import main.java.ggc.pump.device.insulet.data.dto.AbstractRecord;
import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 20.05.15.
 */
public class LogDescriptions extends AbstractRecord
{

    LogHeader logHeader = null;
    List<LogDescription> logDescriptionList;


    public LogDescriptions()
    {
        super(true);
    }


    @Override
    public void customProcess(int[] data)
    {
        logHeader = new LogHeader();
        logHeader.process(data);

        logDescriptionList = new ArrayList<LogDescription>();

        for (int i = 0; i < logHeader.numLogDescriptions; i++)
        {
            int descOffset = 17 + (i * 18);

            LogDescription desc = new LogDescription();
            desc.process(data, descOffset);

            logDescriptionList.add(desc);
        }
    }


    @Override
    public String toString()
    {
        return "LogDescriptions{" + "logHeader=" + logHeader + ", logDescriptionList=" + logDescriptionList + '}';
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.LogDescriptions;
    }

}
