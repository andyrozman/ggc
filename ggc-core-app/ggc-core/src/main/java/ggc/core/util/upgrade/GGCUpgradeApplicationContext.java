package ggc.core.util.upgrade;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.atech.db.hibernate.transfer.BackupRestoreBase;
import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.upgrade.client.data.UpgradeApplicationContextAbstract;

import ggc.core.db.tool.transfer.GGCBackupRestoreRunner;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 24.11.15.
 */
public class GGCUpgradeApplicationContext extends UpgradeApplicationContextAbstract
{

    DataAccess dataAccess;


    public GGCUpgradeApplicationContext(DataAccess dataAccess)
    {
        this.dataAccess = dataAccess;
    }


    public String getTitle(String className)
    {
        return "N/A " + className;
    }


    public boolean isTaskEnabled(String className)
    {
        return true;
    }


    public File getUpgradeDataDirectory()
    {
        return new File("../data/upgrade");
    }


    public String getApplicationRootDirectoryAsString()
    {
        return null;
    }


    public String getShortApplicationName()
    {
        return null;
    }


    public List<String> getBackupFileSet()
    {
        return null;
    }


    public BackupRestoreCollection getBackupRestoreCollection()
    {
        return dataAccess.getBackupRestoreCollection();
    }


    public String runBackupRestoreRunner(BackupRestoreWorkGiver backupRestoreWorkGiver,
            Map<String, BackupRestoreBase> backupObjects)
    {
        GGCBackupRestoreRunner runner = new GGCBackupRestoreRunner(backupObjects, backupRestoreWorkGiver);
        runner.executeBackup();

        return runner.getLastBackupFile();
    }


    public File getUpgradeFile()
    {
        return new File("../data/upgrade/Test123.zip");
    }

}
