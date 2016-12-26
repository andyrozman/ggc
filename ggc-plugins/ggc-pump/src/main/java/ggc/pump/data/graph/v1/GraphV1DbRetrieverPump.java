package ggc.pump.data.graph.v1;

import java.util.*;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.data.HbA1cValues;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.dto.BasalRatesDayDTO;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 26.01.16.
 */
public class GraphV1DbRetrieverPump implements GraphV1DbRetriever
{

    DataAccessPump dataAccess;
    private static List<PumpBaseType> baseTypeFilter = null;
    private static List<PumpAdditionalDataType> additionalTypeFilter = null;

    static
    {
        if (baseTypeFilter == null)
        {
            baseTypeFilter = Arrays.asList(PumpBaseType.Bolus, PumpBaseType.Basal, PumpBaseType.PenInjectionBasal,
                PumpBaseType.PenInjectionBolus);
        }

        if (additionalTypeFilter == null)
        {
            additionalTypeFilter = Arrays.asList(PumpAdditionalDataType.BloodGlucose,
                PumpAdditionalDataType.Carbohydrates);
        }

    }


    public GraphV1DbRetrieverPump()
    {
    }


    /**
     * {@inheritDoc}
     */
    public GlucoValues getGlucoValues(GregorianCalendar calendarFrom, GregorianCalendar calendarTo)
    {
        return createGlucoValues(calendarFrom, calendarTo);
    }


    /**
     * {@inheritDoc}
     */
    public HbA1cValues getHba1cValues(GregorianCalendar calendarTo)
    {
        createDataAccess();

        GregorianCalendar calendarFrom = (GregorianCalendar) calendarTo.clone();
        calendarFrom.add(Calendar.DAY_OF_YEAR, -90);

        List<PumpDataExtendedH> rangePumpValues = dataAccess.getDb().getRangePumpValuesExtendedRaw(calendarFrom,
            calendarTo, Arrays.asList(PumpAdditionalDataType.BloodGlucose));

        // System.out.println("Values count: " + rangePumpValues.size());

        HbA1cValues hbA1cValues = new HbA1cValues(calendarTo);

        for (PumpDataExtendedH pde : rangePumpValues)
        {
            DailyValuesRow row = new DailyValuesRow();
            row.setDateTime(getStripedTime(pde));
            row.setBG(GlucoseUnitType.mg_dL, Float.parseFloat(pde.getValue()));

            hbA1cValues.addDayValueRow(row);
        }

        hbA1cValues.processDayValues();

        return hbA1cValues;
    }


    private void createDataAccess()
    {
        if (dataAccess == null)
        {
            dataAccess = DataAccessPump.getInstance();
        }
    }


    private GlucoValues createGlucoValues(GregorianCalendar calendarFrom, GregorianCalendar calendarTo)
    {
        createDataAccess();

        DeviceValuesRange rangePumpValues = dataAccess.getDb().getRangePumpValues(calendarFrom, calendarTo,
            baseTypeFilter, additionalTypeFilter);

        Map<Long, DailyValuesRow> mapOfDailyValues = new HashMap<Long, DailyValuesRow>();

        for (DeviceValuesEntry deviceValuesEntry : rangePumpValues.getAllEntries())
        {
            PumpValuesEntry pve = (PumpValuesEntry) deviceValuesEntry;
            addPumpData(pve, mapOfDailyValues);
        }

        processPumpBasal(mapOfDailyValues, rangePumpValues);

        return new GlucoValues(calendarFrom, calendarTo, mapOfDailyValues.values());
    }


    private void processPumpBasal(Map<Long, DailyValuesRow> mapOfDailyValues, DeviceValuesRange rangePumpValues)
    {
        // TODO pump basal

        Map<String, BasalRatesDayDTO> basalRatesFromData = dataAccess.getPumpBasalManager()
                .getBasalRatesFromData(rangePumpValues);

    }


    // FIXME
    private void addPumpData(PumpValuesEntry pumpValuesEntry, Map<Long, DailyValuesRow> mapOfDailyValues)
    {
        long time = getStripedTime(pumpValuesEntry);

        if (mapOfDailyValues.containsKey(time))
        {
            addPumpDataToDailyValuesRow(mapOfDailyValues.get(time), pumpValuesEntry);
        }
        else
        {
            DailyValuesRow dailyValuesRow = new DailyValuesRow();
            dailyValuesRow.setDateTime(time);

            addPumpDataToDailyValuesRow(dailyValuesRow, pumpValuesEntry);

            mapOfDailyValues.put(time, dailyValuesRow);
        }
    }


    private void addPumpDataToDailyValuesRow(DailyValuesRow dailyValuesRow, PumpValuesEntry pumpValuesEntry)
    {
        if (pumpValuesEntry.getBaseType() == PumpBaseType.Bolus)
        {
            dailyValuesRow.setPumpBolus(addToFloat(dailyValuesRow.getPumpBolus(), pumpValuesEntry.getValue()));

            dailyValuesRow.setIns1((int) dailyValuesRow.getPumpBolus());
        }
        else
        {
            Collection<PumpValuesEntryExt> values = pumpValuesEntry.getAdditionalData().values();
            for (PumpValuesEntryExt pve : pumpValuesEntry.getAdditionalData().values())
            {
                if (pve.getTypeEnum() == PumpAdditionalDataType.Carbohydrates)
                {
                    dailyValuesRow.setCH(addToFloat(dailyValuesRow.getCH(), pumpValuesEntry.getValue()));
                }
                else if (pve.getTypeEnum() == PumpAdditionalDataType.BloodGlucose)
                {
                    dailyValuesRow.addPumpBg(dataAccess.getFloatValue(pumpValuesEntry.getValue()));
                    dailyValuesRow.setBG((int) dailyValuesRow.getPumpBgCalculated());
                }
            }
        }

    }


    private float addToFloat(float previous, String addValue)
    {
        float sum = previous;
        sum += dataAccess.getFloatValue(addValue);

        return sum;
    }


    private long getStripedTime(PumpValuesEntry pumpValuesEntry)
    {
        return ATechDate.convertATDate(pumpValuesEntry.getDateTime(), ATechDateType.DateAndTimeSec,
            ATechDateType.DateAndTimeMin);
    }


    private long getStripedTime(PumpDataExtendedH pde)
    {
        return ATechDate.convertATDate(pde.getDt_info(), ATechDateType.DateAndTimeSec, ATechDateType.DateAndTimeMin);
    }

}
