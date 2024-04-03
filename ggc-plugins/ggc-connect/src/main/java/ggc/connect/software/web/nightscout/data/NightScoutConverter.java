package ggc.connect.software.web.nightscout.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.cgms.data.writer.CGMSValuesWriter;
import ggc.connect.software.web.nightscout.data.defs.TreatmentEventType;
import ggc.connect.software.web.nightscout.data.dto.Entry;
import ggc.connect.software.web.nightscout.data.dto.Status;
import ggc.connect.software.web.nightscout.data.dto.Treatment;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 4/1/18.
 */
public class NightScoutConverter {

    private static final Logger LOG = LoggerFactory.getLogger(NightScoutConverter.class);

    int unknownTreatments = 0;
    CGMSValuesWriter cgmsValuesWriter = null;


    public NightScoutConverter(OutputWriter outputWriter)
    {
        cgmsValuesWriter = CGMSValuesWriter.getInstance(outputWriter);
    }




    public void convert(Entry entry)
    {
        LOG.debug(entry.toString());

        switch(entry.getEntryType())
        {


            case Calibration:
                //LOG.info("Entry [type=Calibration,dt={},value={}]", entry.getDateString(), entry.);

                //cgmsValuesWriter.writeObject("SensorCalibration", null, null);
                break;

            case MeterBg:
                //cgmsValuesWriter.writeObject()
                break;


            case Sgv:
                break;

            default:
                LOG.warn("Unknown EntryType: {}", entry.getEntryType());

        }


        if ("sgv".equals(entry.getEntryType()))
        {

        }
        else if ("cal".equals(entry.getType()))
        {

        }
        else if ("mbg".equals(entry.getType()))
        {

        }
        else
        {
            System.out.println("Unknown entry: " + entry);
        }




    }

    public void convert(Status status)
    {
        System.out.println("Unknown status: " + status);
    }

    public void convert(Treatment treatment)
    {
        TreatmentEventType eventType = TreatmentEventType.getByDescription(treatment.getEventType());

        switch(eventType)
        {
            // not supported yet
            case CorrectionBolus:
            case TempBasal:
            case SensorChange:
            case SensorStart:
            case BolusWizard:
            case SiteChange:
            case PumpBatteryChange:
            case InsulinChange:
            case Note:
            case Announcement:
            case ProfileSwitch:
            case MealBolus:
                break;



            default:
            case Unknown: {
                System.out.println("Unknown treatment: " + treatment);
                unknownTreatments++;
            } break;
        }

        //System.out.println("Unknown treatment: " + treatment);
    }

    public void showEndStatus()
    {
        System.out.println("Unknown treatments: " + unknownTreatments);
    }

} 