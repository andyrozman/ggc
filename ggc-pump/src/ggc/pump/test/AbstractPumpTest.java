package ggc.pump.test;

import com.atech.i18n.mgr.LanguageManager;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCLanguageManagerRunner;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 24.09.15.
 */
public class AbstractPumpTest
{

    protected DataAccess dataAccess;
    protected DataAccessPump dataAccessPump;


    protected void prepareContext()
    {
        // Init core and Db
        dataAccess = DataAccess.getInstance();

        GGCDb db = new GGCDb(dataAccess);
        db.initDb();

        dataAccess.setDb(db);
        dataAccess.loadSpecialParameters();

        // Init Pump Context
        dataAccessPump = DataAccessPump.createInstance(new LanguageManager(new GGCLanguageManagerRunner()));
        dataAccessPump.createDb(dataAccess.getHibernateDb());
        dataAccessPump.initAllObjects();
        dataAccessPump.loadSpecialParameters();
        dataAccessPump.setCurrentUserId(1);
        dataAccessPump.setBGMeasurmentType(dataAccess
                .getIntValueFromString(dataAccess.getSpecialParameters().get("BG")));

    }

}
