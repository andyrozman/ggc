package ggc.core.data.graph.v1.db;

import java.util.GregorianCalendar;

import ggc.core.data.GlucoValues;
import ggc.core.data.HbA1cValues;

/**
 * Created by andy on 26.01.16.
 */
public interface GraphV1DbRetriever
{

    /**
     * getGlucoValues - get Gluco Values for Graphs
     *
     * @param calendarFrom from
     * @param calendarTo to
     *
     * @return GlucoValues object
     */
    GlucoValues getGlucoValues(GregorianCalendar calendarFrom, GregorianCalendar calendarTo);


    /**
     * getHba1cValues - get values for HbA1c algorithm (it takes 3 months of data)
     *
     * @param calendarTo endDate
     *
     * @return HbA1cValues object
     */
    HbA1cValues getHba1cValues(GregorianCalendar calendarTo);

}
