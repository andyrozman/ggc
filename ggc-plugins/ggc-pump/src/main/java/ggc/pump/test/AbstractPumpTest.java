package main.java.ggc.pump.test;

import com.atech.utils.ATDataAccessLMAbstract;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import main.java.ggc.pump.defs.PumpPluginDefinition;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 24.09.15.
 */
public class AbstractPumpTest
{

    protected DataAccess dataAccess;
    protected DataAccessPump dataAccessPump;

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
        dataAccessPump = DataAccessPump.createInstance(getPluginDefinition(dataAccess));

        if (initDb)
        {
            dataAccessPump.createDb(dataAccess.getHibernateDb());
            dataAccessPump.initAllObjects();
            dataAccessPump.loadSpecialParameters();
            dataAccessPump.setCurrentUserId(1);
            dataAccessPump.initSpecial();
            dataAccessPump.setGlucoseUnitType(dataAccess.getGlucoseUnitType());
        }

        this.contextReady = true;

    }


    private PumpPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new PumpPluginDefinition(da.getLanguageManager());
    }

}
