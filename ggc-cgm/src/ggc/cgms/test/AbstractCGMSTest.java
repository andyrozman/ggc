package ggc.cgms.test;

import com.atech.utils.ATDataAccessLMAbstract;

import ggc.cgms.defs.CGMSPluginDefinition;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 24.09.15.
 */
public class AbstractCGMSTest
{

    protected DataAccess dataAccess;
    protected DataAccessCGMS dataAccessCGMS;

    protected boolean contextReady = false;


    protected void prepareContext()
    {
        if (contextReady)
            return;

        // Init core and Db
        dataAccess = DataAccess.getInstance();

        GGCDb db = new GGCDb(dataAccess);
        db.initDb();

        dataAccess.setDb(db);
        dataAccess.loadSpecialParameters();

        // Init Pump Context
        dataAccessCGMS = DataAccessCGMS.createInstance(getPluginDefinition(dataAccess));
        dataAccessCGMS.createDb(dataAccess.getHibernateDb());
        dataAccessCGMS.initAllObjects();
        dataAccessCGMS.loadSpecialParameters();
        dataAccessCGMS.setCurrentUserId(1);
        dataAccessCGMS.initSpecial();
        dataAccessCGMS.setGlucoseUnitType(dataAccess.getGlucoseUnitType());

        this.contextReady = true;

    }


    private CGMSPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new CGMSPluginDefinition(da.getLanguageManager());
    }

}
