package ggc.connect.test;

import com.atech.utils.ATDataAccessLMAbstract;

import ggc.connect.defs.ConnectPluginDefinition;
import ggc.connect.util.DataAccessConnect;
import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 24.09.15.
 */
public class AbstractConnectTest
{

    protected DataAccess dataAccess;
    protected DataAccessConnect dataAccessConnect;

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
        dataAccessConnect = DataAccessConnect.createInstance(getPluginDefinition(dataAccess));

        if (initDb)
        {
            dataAccessConnect.createDb(dataAccess.getHibernateDb());
            dataAccessConnect.initAllObjects();
            dataAccessConnect.loadSpecialParameters();
            dataAccessConnect.setCurrentUserId(1);
            dataAccessConnect.initSpecial();
            dataAccessConnect.setGlucoseUnitType(dataAccess.getGlucoseUnitType());
        }

        this.contextReady = true;

    }


    private ConnectPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new ConnectPluginDefinition(da.getLanguageManager());
    }

}
