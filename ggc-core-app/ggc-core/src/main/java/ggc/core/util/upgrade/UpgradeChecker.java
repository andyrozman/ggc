package ggc.core.util.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.core.data.defs.GGCObservableType;
import ggc.core.enums.UpgradeCheckStatus;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 01.12.15.
 */
public class UpgradeChecker extends Thread
{

    private static final Logger LOG = LoggerFactory.getLogger(UpgradeChecker.class);

    DataAccess dataAccess;


    public UpgradeChecker(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }


    // TODO after upgrade is finished we need to implement this so that
    // application can check if upgrade is available

    public void run()
    {

    }


    public void setStatus(UpgradeCheckStatus status)
    {
        dataAccess.getObserverManager().setChangeOnEventSource(GGCObservableType.Status,
            "UPDATE_STATUS=" + status.getCode());
    }

}
