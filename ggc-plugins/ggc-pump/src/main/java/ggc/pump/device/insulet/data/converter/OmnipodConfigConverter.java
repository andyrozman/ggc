package main.java.ggc.pump.device.insulet.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.output.OutputWriter;
import main.java.ggc.pump.device.insulet.data.dto.AbstractRecord;
import main.java.ggc.pump.device.insulet.data.dto.config.ConfigRecord;

/**
 * Created by andy on 21.05.15.
 */
public class OmnipodConfigConverter implements OmnipodConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(OmnipodConfigConverter.class);

    OutputWriter outputWriter;


    public OmnipodConfigConverter(OutputWriter writer)
    {
        this.outputWriter = writer;
    }


    public void convert(AbstractRecord record)
    {
        switch (record.getOmnipodDataType())
        {
            case BasalProgramNames:
            case ManufacturingData:
            case EEpromSettings:
            case PdmVersion:
            case IbfVersion:
            case Profile:
                {
                    ConfigRecord cr = (ConfigRecord) record;
                    cr.writeConfigData();
                }
                break;

            // case Profile:
            // {
            // System.out.println(record.getOmnipodDataType().name() + " N/A");
            // }
            // break;

            // ignored
            case LogDescriptions:
                break;

            case LogRecord:
            case HistoryRecord:
                LOG.debug(String.format("This type %s is not supported by this converter.",
                    record.getOmnipodDataType().name()));
                break;

            default:
            case None:
            case UnknownRecord:
            case SubRecord:
                LOG.debug(String.format("This type %s can not be converted [class=%s].", record.getOmnipodDataType(),
                    record.getClass().getSimpleName()));
                break;
        }
    }


    public void postProcessData()
    {

    }
}
