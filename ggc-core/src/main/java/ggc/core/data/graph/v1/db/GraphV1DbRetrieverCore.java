package ggc.core.data.graph.v1.db;

import java.util.GregorianCalendar;

import ggc.core.data.GlucoValues;
import ggc.core.data.HbA1cValues;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 26.01.16.
 */
public class GraphV1DbRetrieverCore implements GraphV1DbRetriever
{

    /**
     * {@inheritDoc}
     */
    public GlucoValues getGlucoValues(GregorianCalendar calendarFrom, GregorianCalendar calendarTo)
    {
        return new GlucoValues(calendarFrom, calendarTo, true);
    }


    /**
     * {@inheritDoc}
     */
    public HbA1cValues getHba1cValues(GregorianCalendar calendarTo)
    {
        return DataAccess.getInstance().getDb().getHbA1cForGraph(calendarTo);
    }
}
