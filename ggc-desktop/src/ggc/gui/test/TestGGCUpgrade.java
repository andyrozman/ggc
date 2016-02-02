package ggc.gui.test;

import javax.swing.*;

import org.junit.Test;

import com.atech.upgrade.client.gui.UpgradeProgressIndicator;
import com.atech.upgrade.client.task.BackupDatabaseTask;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.core.util.upgrade.GGCUpgradeApplicationContext;

/**
 * Created by andy on 02.12.15.
 */
public class TestGGCUpgrade
{

    @Test
    public void doDbUpgrade()
    {
        JFrame f = new JFrame();

        // Init e and Db
        DataAccess dataAccess = DataAccess.createInstance(f);

        GGCDb db = new GGCDb(dataAccess);
        db.initDb();

        dataAccess.setDb(db);
        dataAccess.loadSpecialParameters();

        dataAccess.initPlugIns();

        GGCUpgradeApplicationContext context = new GGCUpgradeApplicationContext(dataAccess);

        BackupDatabaseTask databaseTask = new BackupDatabaseTask(context);

        databaseTask.executeTask(new UpgradeProgressIndicator());

    }

}
