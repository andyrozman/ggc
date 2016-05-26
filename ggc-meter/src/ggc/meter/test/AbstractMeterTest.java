package ggc.meter.test;

import com.atech.utils.ATDataAccessLMAbstract;
import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.util.DataAccessMeter;


/**
 * Created by andy on 24.09.15.
 */
public class AbstractMeterTest
{

    protected DataAccess dataAccess;
    protected DataAccessMeter dataAccessMeter;

    protected boolean contextReady = false;
    protected boolean initDb = true;


    protected void prepareContext()
    {
        if (contextReady)
            return;

        // Init core and Db
        dataAccess = DataAccess.getInstance();

        if (initDb)
        {
            GGCDb db = new GGCDb(dataAccess);
            db.initDb();

            dataAccess.setDb(db);
            dataAccess.loadSpecialParameters();
        }

        // Init Pump Context
        dataAccessMeter = DataAccessMeter.createInstance(getPluginDefinition(dataAccess));

        if (initDb)
        {
            dataAccessMeter.createDb(dataAccess.getHibernateDb());
            dataAccessMeter.initAllObjects();
            dataAccessMeter.loadSpecialParameters();
            dataAccessMeter.setCurrentUserId(1);
            dataAccessMeter.initSpecial();
            dataAccessMeter.setGlucoseUnitType(dataAccess.getGlucoseUnitType());
        }

        this.contextReady = true;

    }


    private MeterPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new MeterPluginDefinition(da.getLanguageManager());
    }

}
